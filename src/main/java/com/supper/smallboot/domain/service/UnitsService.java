package com.supper.smallboot.domain.service;

import com.supper.smallboot.biz.dto.CustomerDTO;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 9:26 2022-08-03
 **/
public interface UnitsService {
    /**
     * 测试事件分发
     * @author ldh
     * @date 2022-08-03 9:20
     * @param corpId 企业ID
     */
    public Boolean  testEvent(Long corpId);

    /**
     * 发送Mq信息
     * @author dto 参数
     * @date 2022-08-03 9:34
     * @param corpId 企业信息
     */
    public  Boolean sendMq(List<CustomerDTO.CustomerInfoDTO> dto);

}
