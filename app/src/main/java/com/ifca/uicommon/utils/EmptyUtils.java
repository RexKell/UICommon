package com.ifca.uicommon.utils;

import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Created by rex on 2017/5/31.
 * 功能：检查空相关工具类
 */

public final class EmptyUtils {
    /**
     * 判断数据对象是否为空
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String && object.toString().length() == 0) {
            return true;
        }
        if (object.getClass().isArray() && Array.getLength(object) == 0) {
            return true;
        }
        if (object instanceof Collection && ((Collection) object).isEmpty()) {
            return true;
        }
        if (object instanceof Map && ((Map) object).isEmpty()) {
            return true;
        }
        if (object instanceof SparseArray&&((SparseArray)object).size()==0){
            return true;
        }
        if (object instanceof SparseBooleanArray &&((SparseBooleanArray)object).size()==0){
            return true;
        }
        if (object instanceof SparseIntArray && ((SparseIntArray) object).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (object instanceof SparseLongArray && ((SparseLongArray) object).size() == 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
