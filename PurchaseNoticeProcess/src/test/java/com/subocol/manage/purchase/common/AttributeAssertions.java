package com.subocol.manage.purchase.common;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author DANR
 * @version 1.0
 * @since 9/08/2023
 */
public class AttributeAssertions {

    public static <T, S> void assertAttributesEqual(T expected, S actual) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (expected == null || actual == null) {
            Assertions.assertEquals(expected, actual);
            return;
        }

        Class<?> firstClass = expected.getClass();
        Class<?> secondClass = actual.getClass();

        Field[] fields = firstClass.getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();

            String getPrefix;

            if (field.getType() == boolean.class)
                getPrefix = "is";
            else
                getPrefix = "get";

            String name = getPrefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method firstGetter = firstClass.getMethod(name);
            Method secondGetter = secondClass.getMethod(name);

            Object expectedValue = firstGetter.invoke(expected);
            Object actualValue = secondGetter.invoke(actual);

            if (isPrimitiveType(expectedValue) && isPrimitiveType(actualValue)) {

                Assertions.assertEquals(expectedValue, actualValue, "Attribute mismatch for field: " + fieldName);
                System.out.println("Assertion passed for class: " + firstClass.getSimpleName() + " and field: " + fieldName);

            } else if (isCollection(expectedValue) && isCollection(actualValue)) {
                int expectedSize = ((Collection<?>) expectedValue).size();
                int actualSize = ((Collection<?>) actualValue).size();

                Assertions.assertEquals(expectedSize, actualSize, "Size mismatch for collection field: " + fieldName);
                System.out.println("Assertion passed for class: " + firstClass.getSimpleName() + " and size of collection field: " + fieldName);

            }

        }
    }

    private static boolean isPrimitiveType(Object value) {
        return value instanceof Integer || value instanceof Long || value instanceof String || value instanceof Date || value instanceof Boolean
                || value instanceof BigDecimal || value instanceof Double || value instanceof Float || value instanceof Byte || value instanceof Short;
    }

    private static boolean isCollection(Object value) {
        return value instanceof List || value instanceof Set<?>;
    }

}
