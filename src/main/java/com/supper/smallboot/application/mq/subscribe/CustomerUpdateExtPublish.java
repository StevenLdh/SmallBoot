package com.supper.smallboot.application.mq.subscribe;

import com.supper.smallboot.biz.dto.CustomerDTO;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 14:22 2022-07-26
 **/
public interface CustomerUpdateExtPublish {

    /**
     * 发送消息
     * @author ldh
     * @date 2022-07-26 14:23
     * @param dto
     */
     Boolean sendCustomerUpdateExt(List<CustomerDTO.CustomerInfoDTO> dto);
}
