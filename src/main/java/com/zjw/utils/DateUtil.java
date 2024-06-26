package com.zjw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author 朱俊伟
 * @since 2024/04/14 00:19
 */
public class DateUtil {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    static {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+8"));
    }
    public static String currentDate() {
        String str = null;
        try {
            str = sdf.format(new Date());
        } catch (Exception e) {
        }
        return str;
    }

    public static String dateToStr(Date date) {
        String str = null;
        try {
            str = sdf.format(date);
        } catch (Exception e) {
        }
        return str;
    }
}
