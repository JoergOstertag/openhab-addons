package org.openhab.binding.mqtt.tasmota.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.platform.commons.util.StringUtils;
import org.openhab.binding.mqtt.tasmota.utils.ExceptionHelper;
import org.openhab.binding.mqtt.tasmota.utils.FileUtils;
import org.openhab.binding.mqtt.tasmota.utils.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

public class AbstractTest {

    private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getName());

    // Stopwatch to see how long a test was running
    StopWatch stopWatch;

    // Stored testClassName for use in Helpers
    String testClassName;

    private String testMethodEclipseDebugString;

    // Stored testMethodName for use in Helpers
    String testMethodName;

    /**
     * At End of Test log testClass,TestMethodName and the time it took. An example
     * message looks like: <br>
     *
     * <pre>
     * Finished Test name.ostertag.test.objects.TestObjectsTest.testFilenameAddition took 0.007 sec.
     * </pre>
     */
    @AfterEach
    public void afterEach(TestInfo testInfo) {
        final long time = stopWatch.getTime();

        LOGGER.info("Finished Test " + testMethodEclipseDebugString + " took " + (time / 1000.0) + " sec.");
        LOGGER.info("==========================================================================================");
    }

    /**
     * Simplified Assertion to see if the result of an object-value in string
     * representation is as expected. This gives you a nice view in eclipse for each
     * sub-value which differs in the JUnit-test-result<br>
     *
     * @param value
     */
    public void assertExpectedResult(Object value) {
        assertExpectedResult(value, "");
    }

    /**
     * Simplified Assertion to see if the result of an object-value in string
     * representation is as expected. This gives you a nice view in eclipse for each
     * sub-value which differs in the JUnit-test-result<br>
     * <br>
     * The object-value is recursively converted into a string. So we have all
     * values of the object and its sub-objects in a human readable string. Then
     * this string-representation of the Object is compared against a file located
     * in the expectedResults directory
     * src/test/resources/expectedResult/<package-name>/<testName>-<filenameAddition>.txt
     * <br>
     * <p>
     * Before comparing a seenResult File is written to always be able to see the
     * resulting string-representation of the object-value. <br/>
     * <p>
     * In case of the results not being the same this method writes a file in
     * src/test/errorResults/. In case of a correct but changed result the
     * developer/tester can simply copy this file into the same location in
     * seenResults.<br/>
     * <p>
     * If no seen Result File is found we use a String which tells us which
     * seenResult-file we would expect.<br>
     * e.g.: !!!!! ERROR: Missing expected Result File
     * src/test/resources/expectedResults/name/ostertag/test/objects/TestObjectsTest/testList.txt
     *
     * @param value
     */
    public void assertExpectedResult(Object value, String filenameAddition) {
        String valueString = "ERROR in conversion";
        try {
            valueString = JsonHelper.toJson(value);
        } catch (final JsonProcessingException e) {
            LOGGER.error("ERROR converting Object-Value into String representation\n"
                    + ExceptionHelper.compactStackTrace(e));
        }

        // Get the filenames for this TestMethod + filenameAddition
        final String filenameSeenResults = getResultFileName(ResultFileType.SEEN_RESULTS, filenameAddition);
        final String filenameExpectedResults = getResultFileName(ResultFileType.EXPECTED_RESULTS, filenameAddition);
        final String filenameErrorResults = getResultFileName(ResultFileType.ERROR_RESULTS, filenameAddition);

        // Write the seen Result
        FileUtils.writeFile(filenameSeenResults, valueString);

        // Get the Expected Result
        String expectedResult = FileUtils.readFile(filenameExpectedResults);
        if (null == expectedResult) {
            LOGGER.warn("Missing Expected Result File; please create the file " + filenameExpectedResults);
            expectedResult = "!!!!! ERROR: Missing expected Result File\n" + filenameExpectedResults;
        }
        if ((null != expectedResult) && expectedResult.equals(valueString)) {
            LOGGER.debug("assert for expected Result (" + testMethodName + "-" + filenameExpectedResults + ") is OK");
        } else {
            // The result Strings differ so we write an erroResults File
            FileUtils.writeFile(filenameErrorResults, valueString);

            LOGGER.error("If the new result is as expected please move \n" //
                    + "         " + filenameErrorResults + "\n" //
                    + "  ==>    " + filenameSeenResults + "\n" //
            );

            // This triggers a test Fail with the information about expectedResult and
            // SeenResult
            assertEquals(expectedResult, valueString);

        }
    }

    /**
     * Memorize Test Name,Class<br>
     * Start timing
     *
     * @param testInfo
     */
    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        final Method testMethod = testInfo.getTestMethod().get();
        testMethodName = testMethod.getName();

        final Class<?> testClass = testInfo.getTestClass().get();
        testClassName = testClass.getCanonicalName();

        // Store the MethodName-String with line-number
        testMethodEclipseDebugString = ExceptionHelper.getEclipseDebugString(testMethod);

        // measure time the test will take
        stopWatch = new StopWatch();
        stopWatch.start();

        LOGGER.info("------------------------------------------------------------------------------------------");
        LOGGER.info("Starting Test " + testMethodEclipseDebugString);
    }

    /**
     * Get the result filename. These look like:<br>
     * src/test/resources/expectedResults/name/ostertag/junit5/assertExpectedResult/AssertExpectedTest/test01.txt
     *
     * @param fileNameType
     * @param filenameAddition
     * @return
     */
    public String getResultFileName(ResultFileType fileNameType, String filenameAddition) {
        final String testPackage = testClassName.replaceAll("\\.", "/");
        final String testName = testMethodName;
        final String fileTypeString = fileNameType.asValue();
        String filename = "src/test/resources/" + fileTypeString + "/" + testPackage + "/" + testName;
        if (StringUtils.isNotBlank(filenameAddition)) {
            filename += "-" + filenameAddition;
        }
        filename += ".txt";
        return filename;
    }
}
