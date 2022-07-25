package com.supper.smallboot.infrastructure.utils;

import com.handday.formless.framework.common.exception.BizException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * Created by 2021/6/17
 *
 * @author youzhe
 * @version V1.0
 * @description: 判断异常统一处理
 */
public class Assert {
    /** 条件为 true 则抛出业务异常 */
    public static void isTrue(Boolean flag, String message) {
        if (flag) {
            assertException(message);
        }
    }

    /** 对象为 null、空字符串时, 则抛出异常 */
    public static void isNull(Object object, String message) {
        if (object == null || StringUtils.EMPTY.equals(object.toString().trim())) {
            assertException(message);
        }
    }

    /** 列表为 null 或 长度为 0 时则抛出异常 */
    public static <T> void isEmpty(Collection<T> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            assertException(message);
        }
    }

    /**
     * 集合不为空, 就抛异常
     */
    public static <T> void isNotEmpty(Collection<T> collection, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            assertException(message);
        }
    }


    /** 数组为 null 或 长度为 0 时则抛出异常 */
    public static <T> void isEmpty(T[] array, String message) {
        if (array == null || array.length == 0) {
            assertException(message);
        }
    }

    /** 无条件抛出业务异常 */
    public static void assertException(String msg) {
        throw new BizException(msg);
    }

    /**
     * 如果value不是空,就报错
     */
    public static void isNotBlank(String  value, String message) {
        if (StringUtils.isNotBlank(value)) {
            assertException(message);
        }
    }
}
