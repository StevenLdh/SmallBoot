package com.supper.smallboot.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

@Slf4j
public class DateUtil {

    /** 当前时间 */
    public static Date now() {
        return new Date();
    }

    /** 当前时间 */
    public static long nowTimeMillis() {
        return System.currentTimeMillis();
    }

    /** 获取当前时间日期的字符串 */
    public static String now(DateFormatType dateFormatType) {
        return format(now(), dateFormatType);
    }
    /** 格式化日期 yyyy-MM-dd */
    public static String formatDate(Date date) {
        return format(date, DateFormatType.YYYY_MM_DD);
    }
    /** 格式化日期和时间 yyyy-MM-dd HH:mm:ss */
    public static String formatFull(Date date) {
        return format(date, DateFormatType.YYYY_MM_DD_HH_MM_SS);
    }

    /** 格式化日期对象成字符串 */
    public static String format(Date date, DateFormatType type) {
        if (Objects.isNull(date) || Objects.isNull(type)) {
            return StringUtils.EMPTY;
        }
        return DateTimeFormat.forPattern(type.getValue()).print(date.getTime());
    }

    /**
     * 返回当前时间的格式:如 20120203
     */
    public static String getNowFormatYYYYMMDD() {
        return DateUtil.format(DateUtil.now(), DateFormatType.YYYYMMDD);
    }

    /**
     * 返回当前时间的格式:如 20120203125959
     */
    public static String getNowFormatYYYYMMDDHHMMSS() {
        return DateUtil.format(DateUtil.now(), DateFormatType.YYYYMMDDHHMMSS);
    }

    /**
     * 将字符串转换成 Date 对象
     *
     * @see DateFormatType
     */
    public static Date parse(String source) {
        if (Objects.isNull(source)) {
            return null;
        }
        source = source.trim();

        long datetime = NumberUtils.toLong(source);
        if (datetime > 0) {
            return new Date(datetime);
        }
        for (DateFormatType type : DateFormatType.values()) {
            try {
                Date date = DateTimeFormat.forPattern(type.getValue()).parseDateTime(source).toDate();
                if (date != null) {
                    return date;
                }
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 时区转换
     * @param time 时间字符串
     * @param pattern 格式 "yyyy-MM-dd HH:mm"
     * @param nowTimeZone eg:+8，0，+9，-1 等等
     * @return
     */
    public static String timeZoneTransfer(Date time, String pattern, String nowTimeZone) {
        if(time == null){
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT" + nowTimeZone));
        return simpleDateFormat.format(time);
    }

    /**
     * 休眠一定时间, 单位毫秒
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("sleep error , errorMsg = {}",e.getMessage(),e);
        }
    }

    public static Date parse(Long time) {
        return new Date(time);
    }

    /**
     * @return
     * @Author ldh
     * @Description 根据毫秒获取时间
     * @Date 11:17 2022-06-30
     * @Param []
     **/
    public static String getDateString(Long m) {
        if (m <= 0) {
            return "";
        }
        return DateUtil.format(DateUtil.parse(m), DateFormatType.CN_YYYY_MM_DD_HH_MM);
    }
}
