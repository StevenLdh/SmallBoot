package com.supper.smallboot.controller;


import com.supper.smallboot.biz.dto.CustomerDTO;
import com.supper.smallboot.biz.vo.CustomerVO;
import com.supper.smallboot.domain.service.ElasticService;
import com.supper.smallboot.infrastructure.anaotation.Login;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private ElasticService elasticService;

    @PostMapping("/save")
    @ApiOperation("保存数据")
    public Boolean save(@RequestBody @Validated List<CustomerDTO.CustomerInfoDTO> dto) {
        return  elasticService.saveCustomer(dto,150000004L);
    }
    @GetMapping("/get")
    @ApiOperation("获取数据")
    @Login
    public List<CustomerVO.CustomerInfoVO> getAll() {
        return elasticService.getCustomerList(150000004L);
    }
}
