package com.ifca.uicommon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rex on 2017/4/27.
 * 功能：时间 日期处理工具
 */

public class DateUtil {
    /**
     * 日期 时间格式为字符串*/
    public static String dateTransString(Date date,String pattern){
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
