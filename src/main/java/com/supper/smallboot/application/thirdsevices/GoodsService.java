package com.supper.smallboot.application.thirdsevices;

import com.supper.smallboot.application.thirdsevices.fallback.GoodsServiceFailBack;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author ldh
 * @Description
 * @Date 11:23 2022-07-26
 **/
@FeignClient(name = "${goods.service.feign-service-name}", /* url = "http://127.0.0.1:9003" , */path = "/api/v1", fallback = GoodsServiceFailBack.class)
public interface GoodsService {

}
