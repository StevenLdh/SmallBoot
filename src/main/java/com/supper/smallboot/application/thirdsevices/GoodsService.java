package com.supper.smallboot.application.thirdsevices;

import com.supper.smallboot.application.thirdsevices.fallback.GoodsServiceFailBackImpl;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ldh
 * @Description 商品服务
 * @Date 11:23 2022-07-26
 **/
@FeignClient(name = "${goods.service.feign-service-name}", /* url = "http://127.0.0.1:9003" , */path = "/api/v1", fallback = GoodsServiceFailBackImpl.class)
public interface GoodsService {

}
