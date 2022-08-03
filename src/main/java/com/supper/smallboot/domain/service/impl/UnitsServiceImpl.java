package com.supper.smallboot.domain.service.impl;

import com.handday.formless.framework.event.publish.AsuraDomainEventPublisher;
import com.handday.formless.framework.redis.RedisRepository;
import com.supper.smallboot.application.mq.subscribe.CustomerUpdateExtPublish;
import com.supper.smallboot.biz.dto.CustomerDTO;
import com.supper.smallboot.domain.event.DemoEvent;
import com.supper.smallboot.domain.service.UnitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 9:26 2022-08-03
 **/
@Service
public class UnitsServiceImpl implements UnitsService {

    @Autowired
    private CustomerUpdateExtPublish customerUpdateExtPublish;

    @Autowired
    private RedisRepository redisRepository;
    /**
     * 测试事件分发
     *
     * @param corpId 企业ID
     * @author ldh
     * @date 2022-08-03 9:20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean testEvent(Long corpId) {
        //事件通知（事物完成后执行）
        AsuraDomainEventPublisher.publish(new DemoEvent(corpId,1));
        return Boolean.TRUE;
    }

    /**
     * 发送Mq信息
     *
     * @param dto 参数
     * @author ldh
     * @date 2022-08-03 9:34
     */
    @Override
    public Boolean sendMq(List<CustomerDTO.CustomerInfoDTO> dto) {
        return customerUpdateExtPublish.sendCustomerUpdateExt(dto);
    }
}
