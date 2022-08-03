package com.supper.smallboot.controller;

import com.supper.smallboot.biz.dto.CustomerDTO;
import com.supper.smallboot.domain.service.UnitsService;
import com.supper.smallboot.infrastructure.anaotation.Login;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 9:25 2022-08-03
 **/
@RestController
@RequestMapping("/api/v1/units")
public class UnitsController {

    @Autowired
    private UnitsService unitsService;

    @GetMapping("/event")
    @ApiOperation("测试事件分发")
    @Login
    public Boolean testEvent() {
        return unitsService.testEvent(150000004L);
    }

    @PostMapping("/send_mq")
    @ApiOperation("发送Mq信息")
    @Login
    public Boolean sendMq(@RequestBody @Validated List<CustomerDTO.CustomerInfoDTO> dto) {
        return unitsService.sendMq(dto);
    }
}
