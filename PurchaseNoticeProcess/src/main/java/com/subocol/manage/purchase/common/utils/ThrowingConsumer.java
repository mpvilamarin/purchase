package com.subocol.manage.purchase.common.utils;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends RuntimeException> {

    void accept(T t) throws E;

    static <T, E extends RuntimeException> Consumer<T> wrapExceptions(
            ThrowingConsumer<T, E> throwingConsumer) throws ExceptionUtil {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (ExceptionUtil ex) {
                throw (ex);
            }
        };

    }
}
