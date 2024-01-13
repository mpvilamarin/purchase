package com.subocol.manage.purchase.common.utils;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 31/07/2023
 */
@Slf4j
public class NativeQueryUtils {

    private NativeQueryUtils() {
    }

    public static StringBuilder getInsertIntoQueryByList(Class<?> clazz, List<?> objects) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(getSchemaAndDbName(clazz))
                .append(" ( ").append(getEntityColumnValues(clazz)).append(" ) VALUES ");

        if (!objects.isEmpty()) {
            queryBuilder.append(getAttributeValues(objects.get(0)));
            if (objects.size() > 1) {
                for (int i = 1; i < objects.size(); i++) {
                    queryBuilder.append(", ");
                    queryBuilder.append(getAttributeValues(objects.get(i)));
                }
            }
        }
        log.info("Dynamic Native Query Created: " + queryBuilder);
        return queryBuilder;
    }

    public static StringBuilder getInsertIntoQuery(Class<?> clazz, Object object) {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(getSchemaAndDbName(clazz))
                .append(" ( ").append(getEntityColumnValues(clazz)).append(" ) VALUES ");

        queryBuilder.append(getAttributeValues(object));

        log.info("Dynamic Native Query Created: " + queryBuilder);
        return queryBuilder;
    }

    private static String getEntityColumnValues(Class<?> clazz) {
        StringBuilder columnValues = new StringBuilder();

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 1; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Column.class)) {
                Column annotation = fields[i].getAnnotation(Column.class);
                String columnName = annotation.name();
                columnValues.append(columnName).append(", ");
            } else if (fields[i].isAnnotationPresent(JoinColumn.class)) {
                JoinColumn annotation = fields[i].getAnnotation(JoinColumn.class);
                String columnName = annotation.name();
                columnValues.append(columnName).append(", ");
            }
        }
        //DELETE LAST COMMA
        if (columnValues.length() > 2) {
            columnValues.delete(columnValues.length() - 2, columnValues.length());
        }

        return columnValues.toString();
    }

    private static String getSchemaAndDbName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            String schema = tableAnnotation.schema();
            String dbName = tableAnnotation.name();

            return schema + "." + dbName;
        }

        return null;
    }

    private static String getAttributeValues(Object object) {
        StringBuilder attributeValues = new StringBuilder(" ( ");

        Field[] fields = object.getClass().getDeclaredFields();

        boolean skipFirstField = true;

        for (Field field : fields) {
            field.setAccessible(true);

            if (skipFirstField) {
                skipFirstField = false;
                continue;
            }

            Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                value = null;
            }

            if (value != null)
                setGrammarByType(attributeValues, field, value);
            else
                attributeValues.append("null, ");

        }

        //delete last comma
        if (attributeValues.length() > 2) {
            attributeValues.delete(attributeValues.length() - 2, attributeValues.length());
        }
        attributeValues.append(" ) ");
        return attributeValues.toString();
    }

    private static void setGrammarByType(StringBuilder attributeValues, Field field, Object value) {

        if (field.getType() == String.class) {
            attributeValues.append("'").append(value).append("', ");
        } else if (field.getType() == Timestamp.class) {
            attributeValues
                    .append("TO_TIMESTAMP( ")
                    .append(((Timestamp) value).getTime() / 1000)
                    .append("), ");
        } else if (field.getType().isAnnotationPresent(Table.class)) {
            //This is for the Entities Relations to extract the id
            if (value != null) {
                Field idField;
                try {
                    idField = value.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    attributeValues.append(idField.get(value)).append(", ");
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // Handle the exception if necessary
                }
            }
        } else {
            attributeValues.append(value).append(", ");
        }

    }

}
