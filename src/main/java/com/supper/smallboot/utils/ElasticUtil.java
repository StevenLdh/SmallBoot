package com.supper.smallboot.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.handday.formless.framework.common.exception.BizException;
import com.handday.formless.framework.common.utils.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wusiwei
 */
@Slf4j
@Component
public class ElasticUtil {

    private static String env;
    private static final int DEFAULT_SHARDS = 3;
    private static final int DEFAULT_REPLICAS = 1;
    private static final String SHARDS = "index.number_of_shards";
    private static final String REPLICAS = "index.number_of_replicas";
    private static RestHighLevelClient client;
    private static ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    public ElasticUtil(ElasticsearchRestTemplate elasticsearchRestTemplate, RestHighLevelClient client) {
        ElasticUtil.elasticsearchRestTemplate = elasticsearchRestTemplate;
        ElasticUtil.client = client;
    }

    @Value("${spring.profiles.active}")
    public void setEnv(String env) {
        ElasticUtil.env = env.toLowerCase();
    }

    /**
     * 创建索引
     *
     * {@link ElasticsearchRestTemplate } + {@link RestHighLevelClient }
     *
     * ElasticsearchRestTemplate.indexOps({@param indexName}.create()) 创建索引只支持 {@link Document } 参数
     * 如需动态索引索引，则不支持分片、副本。 如需自定义分片、副本，则又不支持动态索引
     * 方法主要使用 RestHighLevelClient 创建动态索引
     * 使用 {@link IndexOperations } 生成 mapping {@link org.springframework.data.elasticsearch.core.document.Document}
     *
     * 注意：
     *      文档类型 {@param clz } 字段未指定 {@link org.springframework.data.elasticsearch.annotations.Field }
     *      则会默认生成Mapping
     */
    public static <T> boolean createIndex(String indexName, Class<T> clz) {
        if (ObjectUtil.isNull(clz)) {
            throw new RuntimeException("参数类不能为空");
        }
        if (StrUtil.isEmpty(indexName)) {
            throw new RuntimeException("索引不能为空");
        }
        int shards = DEFAULT_SHARDS, replicas = DEFAULT_REPLICAS;
        Document annotation = clz.getAnnotation(Document.class);
        if (ObjectUtil.isNotNull(annotation)) {
            shards = annotation.shards() > 1 ? annotation.shards() : DEFAULT_SHARDS;
            replicas = annotation.replicas() > 1 ? annotation.replicas() : DEFAULT_REPLICAS;
        }
        boolean acknowledged = false;
        try {
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(clz);
            org.springframework.data.elasticsearch.core.document.Document mapping = indexOperations.createMapping(clz);
            createIndexRequest.settings(Settings.builder().put(SHARDS, shards).put(REPLICAS, replicas));
            createIndexRequest.mapping(mapping);
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse != null) {
                acknowledged = createIndexResponse.isAcknowledged();
            }
        } catch (Exception e) {
            log.error("创建索引失败，索引名称：{}，错误：{}", indexName, e);
        }
        return acknowledged;
    }

    /**
     * 根据索引名判断索引是否存在
     */
    public static boolean isExists(String indexName) {
        return elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName)).exists();
    }

    /**
     * 单条插入
     */
    public static <T> String save(String indexName, T t) {
        if (null == t) {
            return null;
        }
        String id = getDocumentId(t);
        IndexQuery build = new IndexQueryBuilder()
                .withId(id)
                .withObject(t).build();
        elasticsearchRestTemplate.index(build, IndexCoordinates.of(indexName));
        return id;
    }

    /**
     * 批量插入
     */
    public static <T> void save(String indexName, List<T> sourceList) {
        List<IndexQuery> queryList = new ArrayList<>();
        for (T source : sourceList) {
            String id = getDocumentId(source);
            IndexQuery build = new IndexQueryBuilder().withId(id).withObject(source).build();
            queryList.add(build);
        }
        if (queryList.size() > 0) {
            elasticsearchRestTemplate.bulkIndex(queryList, IndexCoordinates.of(indexName));
        }
    }

    /**
     * 根据索引、企业 清空文档
     */
    public static <T> void deleteIndex(String indexName) {
        elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName)).delete();
    }

    /**
     * 根据索引、企业 清空文档
     */
    public static <T> boolean deleteDocument(String indexName, Query query, Class<T> clz) {
        elasticsearchRestTemplate.delete(query, clz, IndexCoordinates.of(indexName));
        return true;
    }

    /**
     * 根据id删除数据
     */
    public static String deleteById(String id, String indexName) {
        return elasticsearchRestTemplate.delete(id, IndexCoordinates.of(indexName));
    }

    /**
     * 根据id批量删除数据
     */
    public static boolean deleteByIds(String docIdName, List<String> ids, String indexName) {
        StringQuery query = new StringQuery(QueryBuilders.termsQuery(docIdName, ids).toString());
        elasticsearchRestTemplate.delete(query, null, IndexCoordinates.of(indexName));
        return true;
    }

    /**
     * 精确匹配
     */
    public static <T> List<T> termsQuery(String indexName, String key, Object value, Class<T> clz) {
        return query(indexName, new TermsQueryBuilder(key, value), clz);
    }

    /**
     * 精确匹配
     */
    public static <T> List<T> termsQuery(String indexName, TermsQueryBuilder queryBuilder, Class<T> clz) {
        return query(indexName, queryBuilder, clz);
    }

    /**
     * 精确匹配 -- 分词
     */
    public static <T> List<T> matchQuery(String indexName, String key, String value, Class<T> clz) {
        return query(indexName, new MatchQueryBuilder(key, value), clz);
    }

    /**
     * 通配符匹配 -- 避免*开始
     */
    public static <T> List<T> wildcardQuery(String indexName, String key, String value, Class<T> clz) {
        return query(indexName, new WildcardQueryBuilder(key, value), clz);
    }

    /**
     * 复合查询
     * 只匹配简单的key value查询
     */
    public static <T> List<T> boolTermQuery(String indexName, Map<String, Object> param, Class<T> clz) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> row : param.entrySet()) {
            queryBuilder.must(QueryBuilders.termQuery(row.getKey(), row.getValue()));
        }
        return query(indexName, queryBuilder, clz);
    }

    /**
     * 复合查询
     * 只匹配简单的key value查询 -- 分词
     */
    public static <T> List<T> boolMatchQuery(String indexName, Map<String, Object> param, Class<T> clz) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> row : param.entrySet()) {
            queryBuilder.must(QueryBuilders.matchQuery(row.getKey(), row.getValue()));
        }
        return query(indexName, queryBuilder, clz);
    }

    /**
     * 查询
     */
    public static <T> List<T> query(String indexName, QueryBuilder queryBuilder, Class<T> clz) {
        SearchHits<T> search ;
        try {
            Query query = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
            search = elasticsearchRestTemplate.search(query, clz, IndexCoordinates.of(indexName));
            if (search.hasSearchHits()) {
                return search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("查询失败，索引名称：{}，错误：{}", indexName, e);
        }
        return Lists.newArrayList();
    }

    /**
     * 获取标注了 {@link Id }  值
     */
    public static <T> String getDocumentId(T t) {
        String documentId = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            List<Field> listWithAnnotation = FieldUtils.getFieldsListWithAnnotation(t.getClass(), Id.class);
            if (CollectionUtil.isEmpty(listWithAnnotation)) {
                return documentId;
            }
            Field field = listWithAnnotation.get(0);
            if (field == null) {
                return documentId;
            }
            field.setAccessible(true);
            Object obj = FieldUtils.readField(field, t);
            if (null == obj) {
                return documentId;
            }
            documentId = String.valueOf(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return documentId;
    }

    /**
     * 创建索引名称
     */
    public static <T> String createIndexName(Long corpId, Class<T> clz) {
        Document annotation = clz.getAnnotation(Document.class);
        if (null == annotation) {
            throw new BizException("未找到索引文档");
        }
        return createIndexName(annotation.indexName(), corpId);
    }

    /**
     * 创建索引名称
     */
    public static String createIndexName(String indexName, Long corpId) {
        if (StringUtils.isBlank(indexName)) {
            throw new BizException("未找到索引文档");
        }
        if (corpId == null || corpId == 0) {
            throw new BizException("未找到企业ID");
        }
        return ElasticUtil.env + "-" + indexName + "-" + (corpId % 100);
    }

    public static String getEnv() {
        return env;
    }
}
