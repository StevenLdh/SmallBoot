package com.supper.smallboot.domain.service.impl;

import com.supper.smallboot.biz.dto.CustomerDTO;
import com.supper.smallboot.biz.vo.CustomerVO;
import com.supper.smallboot.domain.service.ElasticService;
import com.supper.smallboot.infrastructure.utils.ElasticUtil;
import io.seata.spring.annotation.GlobalTransactional;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @Author ldh
 * @Description ES数据处理
 * @Date 11:32 2022-07-12
 **/
@Service
public class ElasticServiceImpl implements ElasticService {


    private static final String CORP_ID = "corpId";

    /**
     * @param dto
     * @Author ldh
     * @Description 保存数据
     * @Date 11:34 2022-07-12
     * @Param []
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public Boolean saveCustomer(List<CustomerDTO.CustomerInfoDTO> dto, Long corpId) {
        String indexName = ElasticUtil.createIndexName(corpId, CustomerDTO.CustomerInfoDTO.class);
        boolean flag = Boolean.TRUE;
        if (ElasticUtil.isExists(indexName)) {
            StringQuery stringQuery = new StringQuery(QueryBuilders.matchQuery(CORP_ID, corpId).toString());
            ElasticUtil.deleteDocument(indexName, stringQuery, CustomerDTO.CustomerInfoDTO.class);
        } else {
            flag = ElasticUtil.createIndex(indexName, CustomerDTO.CustomerInfoDTO.class);
        }
        if (flag) {
            ElasticUtil.save(indexName, dto);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @return
     * @Author ldh
     * @Description 获取全部数据
     * @Date 11:43 2022-07-12
     * @Param []
     **/
    @Override
    public List<CustomerVO.CustomerInfoVO> getCustomerList(Long corpId) {
        String indexName = ElasticUtil.createIndexName(corpId, CustomerDTO.CustomerInfoDTO.class);
        return ElasticUtil.matchQuery(indexName, CORP_ID, corpId.toString(), CustomerVO.CustomerInfoVO.class);
    }
}
