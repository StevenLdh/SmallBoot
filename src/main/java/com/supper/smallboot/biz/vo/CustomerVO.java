package com.supper.smallboot.biz.vo;

import com.supper.smallboot.infrastructure.anaotation.ExcelCell;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author ldh
 * @Description
 * @Date 11:36 2022-07-12
 **/
public class CustomerVO {

    @ApiModel(description = "客户资料")
    @Data
    @Accessors(chain = true)
    public static class CustomerInfoVO {

        @ApiModelProperty(value = "企业ID", required = true)
        @ExcelCell(name = "企业ID")
        private Long corpId;

        @ApiModelProperty(value = "客户名称", required = true)
        @ExcelCell(name = "客户名称")
        private String customerName;

        @ApiModelProperty(value = "客户编号", required = true)
        @ExcelCell(name = "客户编号")
        private String customerNum;
    }
}
