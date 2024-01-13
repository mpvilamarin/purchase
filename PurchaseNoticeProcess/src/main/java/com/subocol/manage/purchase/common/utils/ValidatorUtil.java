package com.subocol.manage.purchase.common.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;


@Configuration
public class ValidatorUtil {

    @Bean
    public Validator validator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public static List<String> getAllMessageOfValidator(Set<ConstraintViolation<Object>> listValidation) {
        return listValidation.stream().map(ConstraintViolation::getMessage).toList();
    }

}
