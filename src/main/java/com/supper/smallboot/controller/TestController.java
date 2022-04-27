package com.supper.smallboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by 2022/1/27
 * 正常开发需删除此类
 * @author SubDong
 * @version V1.0
 * @description: 一句话描述其作用
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @PostMapping("/post")
    public Object post() {
        return "Ok";
    }

}
