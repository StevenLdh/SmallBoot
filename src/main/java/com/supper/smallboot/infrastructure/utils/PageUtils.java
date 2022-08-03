package com.supper.smallboot.infrastructure.utils;

import com.google.common.collect.Lists;
import com.handday.formless.framework.common.vo.PageInfo;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Created by 2021/12/30
 *
 * @author xuguang
 * @version V1.0
 * @description: 分页查询工具
 */
@UtilityClass
public class PageUtils {

    /**
     * 每页分页处理的条数
     */
    public static Integer PAGE_SIZE = 1000;
    /**
     * 数据量大时分页查询, 再把查询返回的每页结果合并为一个集合
     *
     * @param <E> 返回值集合中的元素
     * @param <P> 查询入参, 需要继承 PageInfo.PageVo
     */
    public <P extends PageInfo.PageVo, E> List<E> queryList(P p, Function<P, PageInfo<E>> queryFunction) {
        long total = queryTotal(p, queryFunction);
        if (total == 0) {
            return Collections.emptyList();
        }
        List<E> failList = Lists.newArrayList();
        long pageIndex = (total / PAGE_SIZE) + 1;
        for (int i = 1; i <= pageIndex; i++) {
            List<E> failPage = queryPage(i, p, queryFunction);
            failList.addAll(failPage);
        }
        return failList;
    }

    /**
     * 查询总数
     */
    private <P extends PageInfo.PageVo, E> long queryTotal(P p,
                                                           Function<P, PageInfo<E>> queryFunction) {
        p.setPageNum(1);
        p.setPageSize(1);
        PageInfo<E> pageInfo = queryFunction.apply(p);
        if (pageInfo == null || CollectionUtils.isEmpty(pageInfo.getList()) || pageInfo.getTotal() == 0) {
            return 0;
        }
        return pageInfo.getTotal();
    }

    /**
     * 查询分页
     */
    private <P extends PageInfo.PageVo, E> List<E> queryPage(int pageNum,
                                                             P p,
                                                             Function<P, PageInfo<E>> queryFunction) {
        p.setPageNum(pageNum);
        p.setPageSize(PAGE_SIZE);
        PageInfo<E> pageInfo = queryFunction.apply(p);
        if (pageInfo == null || CollectionUtils.isEmpty(pageInfo.getList())) {
            return Collections.emptyList();
        }
        return pageInfo.getList();
    }

}
