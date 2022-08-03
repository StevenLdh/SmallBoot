package com.supper.smallboot.controller;

import com.supper.smallboot.infrastructure.anaotation.Login;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ldh
 * @Description
 * @Date 9:32 2022-08-03
 **/
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/get_filter")
    @ApiOperation("获取数据用于拦截使用")
    @Login
    public Boolean getFilter() {
        return Boolean.TRUE;
    }
}
