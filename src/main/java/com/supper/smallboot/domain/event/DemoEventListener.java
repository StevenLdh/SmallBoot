package com.supper.smallboot.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @Author ldh
 * @Description
 * @Date 14:02 2022-08-01
 **/
@Slf4j
public class DemoEventListener {

    /**
     * 监听的参数
     * @author ldh
     * @date 2022-08-01 14:13
     * @param demoEvent
     */
    @EventListener
    @Async
    public void demoEventListener(DemoEvent demoEvent){
        log.info("Event传递的参数：corpId={},customerQty={}",demoEvent.getCorpId(),demoEvent.getCustomerQty());
    }
}
