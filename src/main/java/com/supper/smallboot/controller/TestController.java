package com.supper.smallboot.controller;

import com.supper.smallboot.application.thirdsevices.GoodsService;
import com.supper.smallboot.biz.dto.WarehouseDTO;
import com.supper.smallboot.biz.vo.WarehouseVO;
import com.supper.smallboot.infrastructure.anaotation.Login;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 9:32 2022-08-03
 **/
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/get_filter")
    @ApiOperation("获取数据用于拦截使用")
    @Login
    public Boolean getFilter() {
        return Boolean.TRUE;
    }

    @GetMapping("/get_warehouse")
    @ApiOperation("获取仓库列表")
    @Login
    public List<WarehouseVO.WarehouseGetVo> getWarehouseList() {
        return goodsService.getWarehouseList(new WarehouseDTO.ListQueryDTO().setCorpId(150000004L).setIdList(Collections.singletonList(182873957205999616L))).getData();
    }
}
