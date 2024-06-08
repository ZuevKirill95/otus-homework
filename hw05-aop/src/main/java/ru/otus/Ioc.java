package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Ioc {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {
    }

    static TestLoggingInterface createTestLogging() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLogging;
        private final Map<Method, String> cache = new HashMap<>();

        DemoInvocationHandler(TestLoggingInterface testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (cache.containsKey(method)) {
                String logMessage = cache.get(method);

                if (!logMessage.isEmpty()) {
                    LOGGER.info(logMessage);
                }
            } else {
                Method targetMethod = testLogging.getClass().getMethod(method.getName(), method.getParameterTypes());

                if (targetMethod.isAnnotationPresent(Log.class)) {
                    String logMessage = "Parameters of method %s: %s".formatted(method.getName(), Arrays.toString(args));
                    cache.put(method, logMessage);

                    LOGGER.info(logMessage);
                } else {
                    cache.put(method, "");
                }
            }

            return method.invoke(testLogging, args);
        }
    }
}
