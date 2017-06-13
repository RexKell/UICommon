package com.ifca.uicommon.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by rex on 2017/4/27.
 * 功能：打印日志工具类
 */

public class LogUtil {

    public static boolean logOff=true;

    public static int level= Log.ERROR;


    public static void trace(int type,String tag,String msg){
        if (logOff){
            switch (type){
                case Log.VERBOSE:
                    Log.v(tag,msg);
                    break;
                case Log.DEBUG:
                    Log.d(tag,msg);
                    break;
                case Log.INFO:
                    Log.i(tag,msg);
                    break;
                case Log.WARN:
                    Log.w(tag,msg);
                    break;
                case Log.ERROR:
                    Log.e(tag,msg);
                    break;
            }
        }
        if (type>=level){

        }
    }

    public static void v(String tag,String msg){
        if (logOff){
            Log.v(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if (logOff){
            Log.i(tag,msg);
        }
    }
    public static void w(String tag,String msg){
        if (logOff){
            Log.w(tag,msg);
        }
    }
    public static void e(String tag,String msg){
        if (logOff){
            Log.e(tag,msg);
        }
    }
    public static void d(String tag,String msg){
        if (logOff){
            Log.d(tag,msg);
        }
    }
    /**
     * Write Log file to the SDcard
     * @param type
     * @param msg
     * */
    private static void writeLog(int type ,String msg){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return;
        }
        try {
            final HashMap<Integer,String> logMap=new HashMap<Integer, String>();
            logMap.put(Log.VERBOSE,"VERBOSE");
            logMap.put(Log.DEBUG,"DEBUG");
            logMap.put(Log.INFO,"INFO");
            logMap.put(Log.WARN,"WARN");
            logMap.put(Log.ERROR,"ERROR");
           final StackTraceElement tag=new Throwable().fillInStackTrace().getStackTrace()[2];
           msg=new StringBuffer().append("\r\n").append(DateUtil.dateTransString(new Date(),"yyyyMMddHHmmss"))
                   .append(logMap.get(type)).append(tag.getClassName())
                   .append("-").append(tag.getMethodName()).append("():")
                   .append(msg).toString();
           final String fileName=new StringBuffer().append("common_log_")
                   .append(DateUtil.dateTransString(new Date(),"yyyyMMddHHmm")).append(".log").toString();
           recordLog(FileUtils.getAppDirsPath(),fileName,msg,true);
        }catch (Exception e){
            LogUtil.trace(Log.ERROR,"LogUtil:",e.getMessage());
        }
    }

    /**
     * Write Log
     * @param logDir  Log 日志保存目录
     * @param msg  Log 内容
     * @param bool save as type,false 重新写入，true 在原有文件后增加
     *
     * */
    private static void recordLog(String logDir,String fileName,String msg,boolean bool){
      createDir(logDir);
        File saveFile=new File(logDir+"/"+fileName);
        try {
            if (!bool&&saveFile.exists()){
                saveFile.delete();
                saveFile.createNewFile();
                FileOutputStream fileOutputStream=new FileOutputStream(saveFile);
                fileOutputStream.write(msg.getBytes());
                fileOutputStream.close();
            }else if(bool&&saveFile.exists()){
                FileOutputStream fos=new FileOutputStream(saveFile);
                fos.write(msg.getBytes());
                fos.close();
            }else if (bool&&!saveFile.exists()){
                saveFile.createNewFile();
                final FileOutputStream fos=new FileOutputStream(saveFile,bool);
                fos.write(msg.getBytes());
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static File createDir(String dir){
        File file=new File(dir);
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }
}
