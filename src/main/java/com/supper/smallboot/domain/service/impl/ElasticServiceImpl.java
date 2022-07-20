package com.supper.smallboot.domain.service.impl;

import com.supper.smallboot.biz.dto.CustomerDTO;
import com.supper.smallboot.biz.vo.CustomerVO;
import com.supper.smallboot.domain.service.ElasticService;
import com.supper.smallboot.utils.ElasticUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author ldh
 * @Description
 * @Date 11:32 2022-07-12
 **/
@Service
public class ElasticServiceImpl implements ElasticService {


    private static final String CORP_ID = "corpId";
    /**
     * @param dto
     * @return
     * @Author ldh
     * @Description 保存数据
     * @Date 11:34 2022-07-12
     * @Param []
     */
    @Override
    public Boolean saveCustomer(List<CustomerDTO.CustomerInfoDTO> dto, Long corpId) {
        String indexName = ElasticUtil.createIndexName(corpId, CustomerDTO.CustomerInfoDTO.class);
        if (ElasticUtil.isExists(indexName)) {
            StringQuery stringQuery = new StringQuery(QueryBuilders.matchQuery(CORP_ID, corpId).toString());
            ElasticUtil.deleteDocument(indexName, stringQuery, CustomerDTO.CustomerInfoDTO.class);
        }
        if (ElasticUtil.createIndex(indexName, CustomerDTO.CustomerInfoDTO.class)) {
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
