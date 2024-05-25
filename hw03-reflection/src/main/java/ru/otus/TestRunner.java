package ru.otus;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
                T prevObject = invokeMethods(tClass, Before.class, null);
                object = tClass.getConstructor().newInstance();
                moveFieldValuesFromPrevObjectToNewObject(object, prevObject);

                MethodUtils.invokeMethod(object, true, method.getName());

                LOGGER.info("Test {} passed", method.getName());

                invokeMethods(tClass, After.class, object);
                LOGGER.info("_________Test_end_________\n");
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof AssertionError assertionError) {
                    LOGGER.warn(assertionError.getMessage());
                    failedTestsCount++;

                    invokeMethods(tClass, After.class, object);
                    LOGGER.info("_________Test_end_________\n");
                } else {
                    LOGGER.error("Test {} выбросил исключение", method.getName(), e);
                    failedTestsCount++;

                    invokeMethods(tClass, After.class, object);
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

    private static <T> T invokeMethods(Class<T> tClass, Class<? extends Annotation> annotationClass, T prevObject) {
        List<Method> methodsListWithAnnotation =
                MethodUtils.getMethodsListWithAnnotation(tClass, annotationClass, false, true);

        for (Method method : methodsListWithAnnotation) {
            try {
                T object = tClass.getConstructor().newInstance();
                moveFieldValuesFromPrevObjectToNewObject(object, prevObject);

                MethodUtils.invokeMethod(object, true, method.getName());

                prevObject = object;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return prevObject;
    }

    private static <T> void moveFieldValuesFromPrevObjectToNewObject(T object, T prevObject)
            throws IllegalAccessException {
        Field[] declaredFields = object.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            if (prevObject != null) {
                for (Field prevField : prevObject.getClass().getDeclaredFields()) {
                    if (prevField.getName().equals(field.getName())) {
                        Object prevValue = FieldUtils.readField(prevObject, prevField.getName(), true);
                        FieldUtils.writeField(object, field.getName(), prevValue, true);
                    }
                }
            }
        }
    }
}
