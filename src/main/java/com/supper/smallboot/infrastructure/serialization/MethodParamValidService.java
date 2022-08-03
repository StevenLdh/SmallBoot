package com.supper.smallboot.infrastructure.serialization;

import com.supper.smallboot.infrastructure.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

/**
 * Created by 2021/9/23
 *
 * @author xuguang
 * @version V1.0
 * @description: 方法参数校验
 * 类似于controller层的hibernate校验,但是扩展到其他层. 注意:抛出的是BizException
 */
@Component
public class MethodParamValidService {

    @Autowired
    @Qualifier("listValidator")
    private Validator validator;

    /**
     * 校验方法参数
     *
     * @param param 参数
     */
    public void validMethodParam(Object param) {
        WebDataBinder webDataBinder = new WebDataBinder(param);
        webDataBinder.addValidators(validator);
        webDataBinder.validate(new Object[]{});
        if (webDataBinder.getBindingResult().hasErrors()) {
            StringBuilder builder = new StringBuilder();
            webDataBinder.getBindingResult().getFieldErrors().forEach(s -> builder.append(s.getDefaultMessage()).append(";"));
            Assert.assertException(builder.toString());
        }
    }
}
