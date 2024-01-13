package com.subocol.manage.purchase.common.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Port {
    String value() default "";
}
