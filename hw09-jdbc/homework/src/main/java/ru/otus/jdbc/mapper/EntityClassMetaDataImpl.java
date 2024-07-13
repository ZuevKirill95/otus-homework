package ru.otus.jdbc.mapper;

import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.name = entityClass.getSimpleName();
        this.constructor = getConstructor(entityClass);
        this.idField = getIdField(entityClass);
        this.allFields = getAllFields(entityClass);
        this.fieldsWithoutId = getFieldsWithoutId(entityClass);
    }

    private Constructor<T> getConstructor(Class<T> entityClass) {
        try {
            return entityClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getIdField(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    private List<Field> getAllFields(Class<T> entityClass) {
        return List.of(entityClass.getDeclaredFields());
    }

    private List<Field> getFieldsWithoutId(Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();

        return Arrays.stream(fields)
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;

    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
