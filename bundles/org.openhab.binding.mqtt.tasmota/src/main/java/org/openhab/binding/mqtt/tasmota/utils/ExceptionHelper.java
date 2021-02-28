/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 * <p>
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 * <p>
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mqtt.tasmota.utils;

import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * @author Jörg Ostertag - Initial contribution
 */
@NonNullByDefault
public class ExceptionHelper {

    static final String allowedClassNames = "org.openhab.binding.mqtt.*";

    /**
     * return a compact Stacktrace only containing references to my subproject.
     * <p>
     * If throwable is null no message is added to output
     *
     * @param throwable
     * @return
     */
    public static String compactStackTrace(@Nullable Throwable throwable) {

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

    /**
     * Returns a string which can be used to reference the Method in the Eclipse
     * Debug output. The benefit is that eclipse will convert this line to a
     * clickable link which brings you directly to the method definition.
     *
     * @param method
     * @return
     */
    public static String getEclipseDebugString(Method method) {
        final Class<?> clazz = method.getDeclaringClass();
        final ClassPool pool = ClassPool.getDefault();
        int lineNumber = 0;
        try {
            final CtClass ctClass = pool.get(clazz.getName());
            final CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName());
            lineNumber = ctMethod.getMethodInfo().getLineNumber(0);
        } catch (final NotFoundException e) {
        }

        return clazz.getName() + "." + method.getName() + "(" + clazz.getSimpleName() + ".java:" + lineNumber + ")";
    }

    /**
     * return a compact Stacktrace only containing references to my subproject.
     */
    public static String compactStackTrace() {
        return compactStackTrace(null);
    }
}
