package com.supper.smallboot.biz.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Created by 2021/8/19
 *
 * @author xuguang
 * @version V1.0
 * @description: 仓库相关的前端入参对象
 */
public class WarehouseDTO {

    @ApiModel(description = "通过仓库id查询仓库列表")
    @Data
    @Accessors(chain = true)
    public static class ListQueryDTO {
        @ApiModelProperty(value = "企业id",required = true)
        @NotNull(message = "企业id不能为空")
        private Long corpId;

        @ApiModelProperty(value = "仓库id集合",required = true)
        @NotEmpty(message = "仓库id集合不能为空")
        private List<Long> idList;

        public static ListQueryDTO assemblyData(Set<Long> warehouseIds, Long corpId) {
            return new ListQueryDTO().setCorpId(corpId).setIdList(Lists.newArrayList(warehouseIds));
        }
    }
}
