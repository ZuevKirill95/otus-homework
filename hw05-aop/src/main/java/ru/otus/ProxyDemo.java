package ru.otus;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface testLoggingInterface = Ioc.createTestLogging();

        testLoggingInterface.calculation(1);
        testLoggingInterface.calculation(1, 2);
        testLoggingInterface.calculation(1, 2, "str");
        testLoggingInterface.calculation("notLog");
    }
}
