package org.openhab.binding.mqtt.tasmota.utils;

public class ExceptionHelper {

    static final String allowedClassNames = "org.openhab.binding.mqtt.*";

    /**
     * If throwable is null no message is added to output
     *
     * @param throwable
     * @return
     */
    public static String compactStackTrace(Throwable throwable) {

        String logMessage = "";
        if (null == throwable) {
            throwable = new Throwable();
        } else {
            logMessage += throwable.getClass().getSimpleName() + ": ";
        }
        String message = throwable.getMessage();
        if (null == message) {
            message = throwable.getLocalizedMessage();
        }
        if (null != message) {
            logMessage = logMessage + " " + message.toString() + "\n";
        }

        // dump exception stack if throwable is specified
        if (null != throwable) {
            StackTraceElement[] traceElement = throwable.getStackTrace();
            if ((null != traceElement) && (traceElement.length > 0)) {
                if (!java.lang.Throwable.class.equals(throwable.getClass())) {
                    logMessage += throwable.getClass() + "\n";
                }
                for (StackTraceElement stackTraceElement : traceElement) {
                    String className = stackTraceElement.getClassName();
                    if (!className.matches(ExceptionHelper.class.getName()) //
                            && className.matches(allowedClassNames)) {
                        logMessage += "    at " + className + "." + stackTraceElement.getMethodName() + "("
                                + stackTraceElement.getFileName() + ':' + stackTraceElement.getLineNumber() + ")\n";
                    }
                }
            }
        }

        Throwable cause = throwable.getCause();
        while (null != cause) {
            StackTraceElement[] causeTraces = cause.getStackTrace();
            if ((null != causeTraces) && (causeTraces.length > 0)) {
                logMessage += "Caused By:\n";
                logMessage += cause.getClass() + ": " + cause.getMessage() + "\n";
                for (StackTraceElement causeTrace : causeTraces) {
                    String className = causeTrace.getClassName();
                    if (!className.matches(ExceptionHelper.class.getName()) //
                            && className.matches(allowedClassNames)) {
                        logMessage += "    at " + causeTrace.getClassName() + "." + causeTrace.getMethodName() //
                                + '(' + causeTrace.getFileName() + ':' + causeTrace.getLineNumber() + ")\n";
                    }
                }
            }
            // fetch next cause
            cause = cause.getCause();
        }

        return logMessage;
    }

    public static String compactStackTrace() {
        return compactStackTrace(null);
    }
}
