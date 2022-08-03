package com.supper.smallboot.application.thirdsevices;

import com.handday.formless.framework.common.apiresult.DataResult;
import com.supper.smallboot.application.thirdsevices.fallback.GoodsServiceFailBackImpl;
import com.supper.smallboot.biz.dto.WarehouseDTO;
import com.supper.smallboot.biz.vo.WarehouseVO;
import com.supper.smallboot.infrastructure.config.FeignConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author ldh
 * @Description 商品服务
 * @Date 11:23 2022-07-26
 **/
@FeignClient(name = "${goods.service.feign-service-name}", url = "https://dev-hdsaas.facehand.cn/gateway/goodsservice" ,path = "/api/v1", fallback = GoodsServiceFailBackImpl.class,configuration = {FeignConfig.class})
public interface GoodsService {

    @ApiOperation("根据仓库id,获得仓库列表")
    @GetMapping("/warehouse/get_warehouse_list")
    DataResult<List<WarehouseVO.WarehouseGetVo>> getWarehouseList(@RequestBody WarehouseDTO.ListQueryDTO listQueryDTO);

}
