package com.supper.smallboot.infrastructure.anaotation;

import java.lang.annotation.*;

/**
 * @author GuiWu
 * @version V1.0
 * @description: Login 小程序登录拦截
 * @date 1/15/22 2:40 PM
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
