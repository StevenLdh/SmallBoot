package com.supper.smallboot.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Created by 2021/9/27
 *
 * @author xuguang
 * @version V1.0
 * @description: 扩展Validator, 支持在mvc中校验入参集合
 */
@Component("listValidator")
public class ListValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator delegate;


    @Override
    public boolean supports(Class<?> clazz) {
        return this.delegate != null;
    }


    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof List) {
            ((List<?>) target).forEach(a -> delegate.validate(a, errors));
            return;
        }
        delegate.validate(target, errors);
    }
}
