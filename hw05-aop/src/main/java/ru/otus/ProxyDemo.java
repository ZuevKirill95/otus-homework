package ru.otus;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface testLoggingInterface = (TestLoggingInterface) Ioc.createProxy(new TestLogging());

        testLoggingInterface.calculation(1);
        testLoggingInterface.calculation(1, 2);
        testLoggingInterface.calculation(1, 2, "str");
        testLoggingInterface.calculation("notLog");

        SomeInterface someInterface = (SomeInterface) Ioc.createProxy(new SomeClass());

        someInterface.someMethod(1);
    }
}
