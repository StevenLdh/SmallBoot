package com.supper.smallboot.domain.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @Author ldh
 * @Description
 * @Date 14:02 2022-08-01
 **/
@Slf4j
@Component
public class DemoEventListener {

    /**
     * 监听的参数
     * @author ldh
     * @date 2022-08-01 14:13
     * @param demoEvent 事件
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void demoEventListener(DemoEvent demoEvent){
        log.info("Event传递的参数：corpId={},customerQty={}",demoEvent.getCorpId(),demoEvent.getCustomerQty());
    }
}
