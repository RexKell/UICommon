package com.ifca.uicommon.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by rex on 2017/4/27.
 * 功能：主要功能  清除应用内/外缓存数据，清除数据库，清除shardPreference,
 * 清除files 和清除自定义目录
 */

public class DataCleanManager {
    /**
     * 清除本应用内部缓存（/data/data/..packname../cache）
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除该应用的所有数据库文件
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * 清除该应用某个数据库文件
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除本应用SharedPreference(/data/data/..packname../shared_prefs)
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 清除应用文件目录下的内容
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * 清除应用外部缓存文件
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 删除的文件目录
     */
    public static void cleanFiles(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 删除文件目录下的文件，如果传入文件不做处理
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * 清除本应用所以的数据
     * */
    public static void cleanApplicationData(Context context){
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }
}
