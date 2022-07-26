package com.supper.smallboot.application.mq.subscribe.impl;

import com.supper.smallboot.application.mq.channel.CustomerUpdateExtChannel;
import com.supper.smallboot.application.mq.subscribe.CustomerUpdateExtPublish;
import com.supper.smallboot.biz.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 14:10 2022-07-26
 **/
@EnableBinding({CustomerUpdateExtChannel.class})
@Service
public class CustomerUpdateExtPublishImpl implements CustomerUpdateExtPublish {

    @Autowired
    private CustomerUpdateExtChannel customerUpdateExtChannel;

    /**
     * 发送消息
     * @author ldh
     * @date 2022-07-26 14:16
     * @param dto
     */
    @Override
    public Boolean sendCustomerUpdateExt(List<CustomerDTO.CustomerInfoDTO> dto){
        Message<List<CustomerDTO.CustomerInfoDTO>> build= MessageBuilder.withPayload(dto).build();
        return customerUpdateExtChannel.output().send(build);
    }
}
