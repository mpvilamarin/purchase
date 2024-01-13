package com.subocol.manage.purchase.common.utils;

import org.modelmapper.ModelMapper;

import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
public class MapperUtil {

    private static final ModelMapper modelMapper = new ModelMapper();

    private MapperUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static <S, T> T convert(S source, Class<T> targetClass) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper.map(source, targetClass);
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .toList();
    }
}
