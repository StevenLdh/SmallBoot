package com.supper.smallboot.application.thirdsevices.fallback;

import com.handday.formless.framework.common.apiresult.DataResult;
import com.supper.smallboot.application.thirdsevices.GoodsService;
import com.supper.smallboot.biz.dto.WarehouseDTO;
import com.supper.smallboot.biz.vo.WarehouseVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author ldh
 * @Description
 * @Date 11:31 2022-07-26
 **/
@Component
public class GoodsServiceFailBackImpl implements GoodsService {

    @Override
    public DataResult<List<WarehouseVO.WarehouseGetVo>> getWarehouseList(WarehouseDTO.ListQueryDTO listQueryDTO) {
        return null;
    }
}
