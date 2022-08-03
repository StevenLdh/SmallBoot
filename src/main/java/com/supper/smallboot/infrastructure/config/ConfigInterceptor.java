package com.supper.smallboot.infrastructure.config;

import com.supper.smallboot.infrastructure.utils.Assert;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Created by 2021/9/28
 *
 * @author xuguang
 * @version V1.0
 * @description: erp配置接口的feign拦截器
 */
@Order(2)
@Slf4j
public class ConfigInterceptor implements RequestInterceptor {


    private String openId;

    /**
     * 通用url参数
     */
    public static final String CORPID = "corpId";
    public static final String OPENID = "openId";

    public ConfigInterceptor(String openId) {
        this.openId = openId;
    }

    /**
     * 通用头部
     **/
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String AUTHORIZATION = "Authorization";
    public static final String HANDDAYOPENID = "HanddayOpenID";

    @Override
    public void apply(RequestTemplate template) {
        //有些请求的Content-Length丢失了,导致接口返回411, 需要手动加上
        if (CollectionUtils.isEmpty(template.headers().get(CONTENT_LENGTH))) {
            template.header(CONTENT_LENGTH, template.body() == null ? "0" : String.valueOf(template.body().length));
        }

        String corpId = getCorpId(template);
        template.removeHeader(AUTHORIZATION);
        template.header(AUTHORIZATION, corpId);
        template.header(HANDDAYOPENID, openId);
    }

    /**
     * 获取企业id
     * 如果url上面有corpId的入参,优先取它. 否则取登录态里面的corpId
     */
    public static String getCorpId(RequestTemplate template) {
        List<String> urlCorpIdList = template.queries().entrySet().stream()
                .filter(a -> Objects.equals(CORPID, a.getKey()))
                .flatMap(a -> a.getValue().stream()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(urlCorpIdList)) {
            return urlCorpIdList.get(0);
        }

        Long currentCorpId = 15000004L;
        //OauthUtil.getCorpIdNullable();
        Assert.isNull(currentCorpId, "获取登录信息异常");
        return String.valueOf(currentCorpId);
    }

}
