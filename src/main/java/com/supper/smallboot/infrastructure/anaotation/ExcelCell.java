package com.supper.smallboot.infrastructure.anaotation;

import java.lang.annotation.*;

/**
 * @Author ldh
 * @Description
 * @Date 9:54 2022-08-24
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelCell {

    String name() default "";


}
