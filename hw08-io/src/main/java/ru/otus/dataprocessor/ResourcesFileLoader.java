package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import ru.otus.model.Measurement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @SneakyThrows
    @Override
    public List<Measurement> load() {
        String json = Files.readString(Path.of(ClassLoader.getSystemResource(fileName).getFile()));

        return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
        });
    }
}
