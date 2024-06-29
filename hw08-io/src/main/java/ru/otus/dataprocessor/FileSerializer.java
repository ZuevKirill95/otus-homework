package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Map;

public class FileSerializer implements Serializer {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @SneakyThrows
    @Override
    public void serialize(Map<String, Double> data) {
        OBJECT_MAPPER.writeValue(new File(fileName), data);
    }
}
