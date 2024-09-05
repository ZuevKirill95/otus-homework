package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Этот класс нужно реализовать

@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final BlockingQueue<SensorData> dataBuffer;
    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        addData(data);

        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    private void addData(SensorData data) {
        readWriteLock.readLock().lock();
        try {
            dataBuffer.add(data);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void flush() {
        readWriteLock.writeLock().lock();
        if (!dataBuffer.isEmpty()) {
            try {
                int size = dataBuffer.size();
                ArrayList<SensorData> dataList = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    SensorData taken = dataBuffer.take();
                    dataList.add(taken);
                }

                writer.writeBufferedData(dataList);
                dataBuffer.clear();
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            }
        }
        readWriteLock.writeLock().unlock();
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
