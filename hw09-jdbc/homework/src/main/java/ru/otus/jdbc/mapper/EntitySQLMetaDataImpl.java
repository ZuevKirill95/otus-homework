package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from %s".formatted(entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        String selectValues = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        return "select %s from %s where %s  = ?".formatted(selectValues,
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();

        String insertValues = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String wildcards = fieldsWithoutId.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));


        return "insert into %s(%s) values (%s)".formatted(entityClassMetaData.getName(), insertValues, wildcards);
    }

    @Override
    public String getUpdateSql() {
        String updateValues = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining("= ?,"));

        return "update %s set %s = ? where %s = ?".formatted(entityClassMetaData.getName(), updateValues,
                entityClassMetaData.getIdField().getName());
    }
}
