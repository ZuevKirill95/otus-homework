package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sql = entitySQLMetaData.getSelectByIdSql();

        return dbExecutor.executeSelect(connection, sql, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createInstance(rs);
                }
                return null;
            } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();

        return dbExecutor
                .executeSelect(connection, sql, Collections.emptyList(), rs -> {
                    var result = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            T instance = createInstance(rs);

                            result.add(instance);
                        }
                        return result;
                    } catch (SQLException | InstantiationException | IllegalAccessException |
                             InvocationTargetException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new DataTemplateException("Unexpected error"));
    }

    private T createInstance(ResultSet rs)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        T instance = entityClassMetaData.getConstructor().newInstance();

        entityClassMetaData.getAllFields()
                .forEach(field -> {
                    String name = field.getName();
                    try {
                        Object value = rs.getObject(name);
                        field.setAccessible(true);
                        field.set(instance, value);
                    } catch (SQLException | IllegalAccessException e) {
                        throw new DataTemplateException(e);
                    }
                });

        return instance;
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            String sql = entitySQLMetaData.getInsertSql();
            List<Object> params = getParamsWithoutId(client);

            return dbExecutor.executeStatement(
                    connection, sql, params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        String updateSql = entitySQLMetaData.getUpdateSql();
        try {
            ArrayList<Object> updateParams = new ArrayList<>(getParamsWithoutId(client));
            updateParams.add(entityClassMetaData.getIdField().get(client.getClass()));
            dbExecutor.executeStatement(
                    connection, updateSql, updateParams);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getParamsWithoutId(T client) {
        return entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(client);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
