package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData<?> entityClassMetaData;
    private final String sqlSelectAll;
    private final String sqlSelectById;
    private final String sqlInsert;
    private final String sqlUpdate;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
        this.sqlSelectAll = "select * from %s".formatted(entityClassMetaData.getName());
        this.sqlSelectById = createSqlSelectById();
        this.sqlInsert = createSqlInsert();
        this.sqlUpdate = createSqlUpdate();
    }

    private String createSqlSelectById() {
        String selectValues = entityClassMetaData.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        return "select %s from %s where %s  = ?".formatted(selectValues,
                entityClassMetaData.getName(), entityClassMetaData.getIdField().getName());
    }

    private String createSqlInsert() {
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();

        String insertValues = fieldsWithoutId.stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));

        String wildcards = fieldsWithoutId.stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));


        return "insert into %s(%s) values (%s)".formatted(entityClassMetaData.getName(), insertValues, wildcards);
    }

    private String createSqlUpdate() {
        String updateValues = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining("= ?,"));

        return "update %s set %s = ? where %s = ?".formatted(entityClassMetaData.getName(), updateValues,
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() {
        return sqlSelectAll;
    }

    @Override
    public String getSelectByIdSql() {
        return sqlSelectById;
    }

    @Override
    public String getInsertSql() {
        return sqlInsert;
    }

    @Override
    public String getUpdateSql() {
        return sqlUpdate;
    }
}
