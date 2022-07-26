package com.supper.smallboot.infrastructure.repository;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author ldh
 * @Description mongodb数据操作
 * @Date 11:39 2022-07-26
 **/
public class MongoRepository {

    public final static String TABLE_NAME = "erp_goods";

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     *  批量插入数据
     * @author ldh
     * @date 2022-07-26 11:48
     * @param list
     * @param corpId
     */
    public Boolean pageBatchInsert(List<Class<T>> list, Long corpId) {
        mongoTemplate.insert(list, getTableName(corpId, TABLE_NAME));
        return true;
    }


    public String getTableName(Long corpId, String tableName) {
        long last = corpId % 100;
        return tableName + "_" + last;
    }
}
