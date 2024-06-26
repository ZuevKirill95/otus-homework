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

    static Object createProxy(Object target) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LogInvocationHandler(target));
    }

    static class LogInvocationHandler implements InvocationHandler {
        private final Object testLogging;
        private final Map<Method, String> cache = new HashMap<>();

        LogInvocationHandler(Object testLogging) {
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
