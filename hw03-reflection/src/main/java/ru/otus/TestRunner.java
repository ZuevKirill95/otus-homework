package ru.otus;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@UtilityClass
public final class TestRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunner.class);

    public static <T> void run(Class<T> tClass) {
        int failedTestsCount = 0;

        T object = null;
        List<Method> testMethods =
                MethodUtils.getMethodsListWithAnnotation(tClass, Test.class, false, true);

        for (Method method : testMethods) {
            LOGGER.info("_________Test_Begin_________");
            try {
                object = tClass.getConstructor().newInstance();

                invokeMethods(object, Before.class);

                MethodUtils.invokeMethod(object, true, method.getName());

                LOGGER.info("Test {} passed", method.getName());

                invokeMethods(object, After.class);
                LOGGER.info("_________Test_end_________\n");
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof AssertionError assertionError) {
                    LOGGER.warn(assertionError.getMessage());
                    failedTestsCount++;

                    if (object != null){
                        invokeMethods(object, After.class);
                    }
                    LOGGER.info("_________Test_end_________\n");
                } else {
                    LOGGER.error("Test {} выбросил исключение", method.getName(), e);
                    failedTestsCount++;

                    if (object != null){
                        invokeMethods(object, After.class);
                    }
                    LOGGER.info("_________Test_end_________\n");
                }
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        resultStatistic(testMethods, failedTestsCount);
    }

    private static void resultStatistic(List<Method> testMethods, int failedTestsCount) {
        int testCount = testMethods.size();
        int successTestsCount = testCount - failedTestsCount;

        LOGGER.info("Статистика: Всего тестов {}. Успешные {}. Проваленные {}.",
                testCount, successTestsCount, failedTestsCount);
    }

    private static void invokeMethods(Object object, Class<? extends Annotation> annotationClass) {
        List<Method> methodsListWithAnnotation =
                MethodUtils.getMethodsListWithAnnotation(object.getClass(), annotationClass, false, true);

        for (Method method : methodsListWithAnnotation) {
            try {
                MethodUtils.invokeMethod(object, true, method.getName());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
