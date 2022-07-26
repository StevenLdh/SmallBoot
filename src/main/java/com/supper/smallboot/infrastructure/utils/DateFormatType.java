package com.supper.smallboot.infrastructure.utils;

import lombok.Getter;

/** 日期的格式化类型 */
public enum DateFormatType {

    /** yyyy-MM-dd HH:mm:ss */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
    YYYY_MM_DDTHH_MM_SS("yyyy-MM-ddTHH:mm:ss"),
    /** yyyy-MM-dd HH:mm */
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
    /** yyyy-MM-dd */
    YYYY_MM_DD("yyyy-MM-dd"),
    /** yyyy-MM */
    YYYY_MM("yyyy-MM"),

    /** yyyy/MM/dd */
    USA_YYYY_MM_DD("yyyy/MM/dd"),
    /** MM/dd/yyyy HH:mm:ss */
    USA_MM_DD_YYYY_HH_MM_SS("MM/dd/yyyy HH:mm:ss"),
    /** yyyy年MM月dd日 HH时mm分ss秒 */
    CN_YYYY_MM_DD_HH_MM_SS("yyyy年MM月dd日 HH时mm分ss秒"),
    /** yyyy年MM月dd日 */
    CN_YYYY_MM_DD("yyyy年MM月dd日"),

    /** yyyy/MM/dd HH:mm */
    CN_YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),
    /** yyyy/MM/dd HH:mm:ss */
    YYYY_MM_DD_HH_MM_SS_01("yyyy/MM/dd HH:mm:ss"),
    /** yyyyMMdd */
    YYYYMMDD("yyyyMMdd"),
    /** yyyyMMddHHmmss */
    YYYYMMDDHHMMSS("yyyyMMddHHmmss"),
    ;

    @Getter()
    private String value;

    DateFormatType(String value) {
        this.value = value;
    }

}
