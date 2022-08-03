package com.supper.smallboot.domain.service;

import com.supper.smallboot.biz.dto.CustomerDTO;
import com.supper.smallboot.biz.vo.CustomerVO;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 11:31 2022-07-12
 **/
public interface ElasticService {
    /**
     * ES保存数据
     * @author ldh
     * @date 2022-07-26 10:38
     * @param dto
     * @param corpId
     */
    public Boolean saveCustomer(List<CustomerDTO.CustomerInfoDTO> dto,Long corpId);

    /**
     * ES获取数据
     * @author ldh
     * @date 2022-07-26 10:39
     * @param corpId
     */
    public List<CustomerVO.CustomerInfoVO> getCustomerList(Long corpId);



}
