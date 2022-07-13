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
     * @return
     * @Author ldh
     * @Description 保存数据
     * @Date 11:34 2022-07-12
     * @Param []
     **/
    public Boolean saveCustomer(List<CustomerDTO.CustomerInfoDTO> dto,Long corpId);

    /**
     * @return
     * @Author ldh
     * @Description 获取全部数据
     * @Date 11:43 2022-07-12
     * @Param []
     **/
    public List<CustomerVO.CustomerInfoVO> getCustomerList(Long corpId);

}
