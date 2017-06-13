package com.ifca.uicommon.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.bumptech.glide.util.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rex on 2017/5/27.
 * 功能：
 */

public class CrashUtils implements Thread.UncaughtExceptionHandler {
    private static CrashUtils mInstance;

    private Thread.UncaughtExceptionHandler mHandler;

    private boolean mInitialized;
    private String crashDir;
    private String versionName;
    private int versionCode;


    public CrashUtils() {
    }

    /**
     * 获取单例
     * 在Application 中初始化{@code CrashUtils.getInstance().init(this)}
     * 需要添加权限{@code <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE">}
     */
    public static CrashUtils getInstance() {
        if (mInstance == null) {
            synchronized (CrashUtils.class) {
                if (mInstance == null) {
                    mInstance = new CrashUtils();
                }
            }
        }
        return mInstance;

    }

    /**
     * 初始化
     *
     * @return {@code true}:成功
     * {@code false}：失败
     */
    public boolean init(Context context) {
        if (mInitialized) return true;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File baseCache = context.getExternalCacheDir();
            if (baseCache == null) {
                return false;
            }
            crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        } else {
            File baseCache = context.getCacheDir();
            if (baseCache == null) return false;
            crashDir = baseCache.getPath() + File.separator + "crash" + File.separator;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        return mInitialized = true;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        String now = new SimpleDateFormat("yyMMdd HH-mm-ss", Locale.getDefault()).format(new Date());
        final String fullPath = crashDir + now + ".txt";
        if (!createOrExistsFile(fullPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(fullPath, false));
                    pw.write(getCrashHead());
                    throwable.printStackTrace(pw);
                    Throwable cause = throwable.getCause();
                    while (cause != null) {
                        cause.printStackTrace(pw);
                        cause = cause.getCause();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (pw != null) {
                        pw.flush();
                        pw.close();
                    }
                }
            }
        }).start();
        if (mHandler != null) {
            mHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        StringBuffer stringBuffer = new StringBuffer("");
        stringBuffer.append("\n************* Crash Log Head ***********")
                .append("\n设备厂商: " + Build.MANUFACTURER)
                .append("\n设备型号: " + Build.MODEL)
                .append("\n 系统版本号" + Build.VERSION.RELEASE)
                .append("\n Android Sdk:" + Build.VERSION.SDK_INT)
                .append("\n 日志VersionName:" + versionName)
                .append("\n 日志版本号：" + versionCode)
                .append("\n************* Crash Log Head ***********\n");
        return stringBuffer.toString();
    }

    /**
     * 判断文件是否存在，不存在则创建
     *
     * @param filePath 文件路径
     * @return {@code true}:存在或创建成功
     * {@code false}:创建失败
     */
    private static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return file.isFile();
        }
    }

}
