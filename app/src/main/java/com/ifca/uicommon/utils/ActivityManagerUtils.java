package com.ifca.uicommon.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by rex on 2017/5/17.
 * 功能：Activity 堆栈管理工具类
 */

public class ActivityManagerUtils {
    private static ActivityManagerUtils instance;
    private static Stack<Activity> stacks;

    public ActivityManagerUtils() {
    }

    /**
     * 单一实例
     */
    public static ActivityManagerUtils getInstance() {
        if (instance == null) {
            instance = new ActivityManagerUtils();
        }
        if (stacks == null) {
            stacks = new Stack<Activity>();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity 传入处理的Activity
     */
    public void addActivity(Activity activity) {
        stacks.add(activity);
    }

    /**
     * 结束当前的activity
     */
    public void finishActivity(Activity activity) {
        stacks.remove(activity);
        activity.finish();
        activity = null;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : stacks) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        return stacks.lastElement();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = stacks.size(); i < size; i++) {
            if (stacks.get(i) != null) {
                stacks.get(i).finish();
            }
        }
        stacks.clear();
    }

    /**
     * 退出应用
     */
    public void exitApp(Context context) {
        finishAllActivity();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.restartPackage(context.getPackageName());
        System.exit(0);
    }
}
