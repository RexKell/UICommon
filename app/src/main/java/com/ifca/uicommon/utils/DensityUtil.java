package com.ifca.uicommon.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by rex on 2017/4/27.
 * 功能：dp  px  sp转换工具
 */

public class DensityUtil {
    /**
     * dp 转px
     */
    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

/**
 * sp转px*/
    public static int sp2px(Context context,float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale+0.5f);
    }

    /**px 转 sp*/
    public static int px2sp(Context context,float pxValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue/fontScale+0.5f);
    }

}
