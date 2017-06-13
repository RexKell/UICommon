package com.ifca.uicommon.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by rex on 2017/5/27.
 * 功能：键盘相关工具类
 */

public final class KeyboardUtils {
    /**
     * 动态隐藏软键盘
     * @param activity activity
     * */
    public static void hideSoftInput(Activity activity){
        View view=activity.getCurrentFocus();
        if (view==null){
            view=new View(activity);
        }
        InputMethodManager imm=(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm==null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

    }
    /**
     * 动态隐藏软键盘
     * @param context 上下文
     *          @param view 视图
     * */
    public static void hideSoftInput(Context context,View view){
        InputMethodManager manager=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
       if (manager==null) return;
        manager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    /**
     *动态显示软键盘
     * @param editText 输入框
     * */
    public static void showSoftInput(Context context,EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputMethodManager=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager==null) return;
        inputMethodManager.showSoftInput(editText,0);
    }
    /**
    * 切换键盘显示与否状态
     * */
    public static void toggleSoftInput(Context context){
        InputMethodManager inputMethodManager=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager==null) return;
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

}
