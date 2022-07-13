package com.supper.smallboot.biz.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ldh
 * @Description
 * @Date 11:36 2022-07-12
 **/
public class CustomerVO {

    @ApiModel(description = "客户资料")
    @Data
    public static class CustomerInfoVO {

        @ApiModelProperty(value = "企业ID", required = true)
        private Long corpId;

        @ApiModelProperty(value = "客户名称", required = true)
        private String customerName;

        @ApiModelProperty(value = "客户编号", required = true)
        private String customerNum;
    }
}
