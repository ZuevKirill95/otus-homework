package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import static ru.otus.Asserts.assertEquals;
import static ru.otus.Asserts.assertTrue;

public class TestClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestClass.class);

    private int number1;
    private int number2;
    private boolean isTrue;

    @Before
    void initialNumber() {
        LOGGER.info("before1");
        number1 = 2;
        number2 = 3;
    }

    @Before
    void initialBoolean() {
        LOGGER.info("before2");
        isTrue = true;
    }

    @Test
    public void testThrowException() {
        throw new RuntimeException("Some exception");
    }

    @Test
    void successTest1() {
        assertEquals(5, number1 + number2);
    }

    @Test
    public void successTest2() {
        assertTrue(isTrue);
    }

    @Test
    public void failedTest1() {
        assertTrue(!isTrue);
    }

    @Test
    public void failedTest2() {
        assertEquals(4, number1 + number2);
    }

    @After
    void after1() {
        LOGGER.info("after1. Values = isTrue: {}, number1: {}, number2: {}", isTrue, number1, number2);
    }

    @After
    void after2() {
        LOGGER.info("after2. Values = isTrue: {}, number1: {}, number2: {}", isTrue, number1, number2);
    }
}
