package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);

    private static final Object lock = new Object();
    private static volatile String runningThread = "t1";

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> printNumbers("t2"));
        Thread thread2 = new Thread(() -> printNumbers("t1"));
        thread1.setName("t1");
        thread2.setName("t2");

        thread1.start();
        thread2.start();
    }

    private static void printNumbers(String anotherThreadName) {
        synchronized (lock) {
            try {
                for (int i = 1; i <= 10; i++) {
                    while (!runningThread.equals(Thread.currentThread().getName())) {
                        lock.wait();
                    }
                    logger.info("count {}", i);
                    runningThread = anotherThreadName;
                    lock.notifyAll();
                }

                for (int i = 9; i > 0; i--) {
                    while (!runningThread.equals(Thread.currentThread().getName())) {
                        lock.wait();
                    }
                    logger.info("count {}", i);
                    runningThread = anotherThreadName;
                    lock.notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}