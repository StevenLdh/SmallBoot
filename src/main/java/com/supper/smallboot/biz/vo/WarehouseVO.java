package com.supper.smallboot.biz.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author GuiWu
 * @version V1.0
 * @description: 仓库返回参数
 * @date 7/22/21 9:41 AM
 */
public class WarehouseVO {
    @Data
    @ApiModel("根据corpId获得WarehouseVO")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WarehouseGetVo implements Serializable {

        private static final long serialVersionUID = -5582706075438066707L;

        @ApiModelProperty("根据corpId获得WarehouseVO-主键ID")
        private Long id;

        @ApiModelProperty("根据corpId获得WarehouseVO-名称")
        private String name;

        @ApiModelProperty("根据corpId获得WarehouseVO-是否默认0--否 1--是")
        private Integer isDefault;
    }
}
