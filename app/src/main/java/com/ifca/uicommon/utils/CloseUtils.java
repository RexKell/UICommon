package com.ifca.uicommon.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by rex on 2017/5/12.
 * 功能：
 */

public class CloseUtils {
    /**
     * 关闭IO流
     * @param closeables 需要关闭的流对象组
     * */
    public static void closeIO(Closeable... closeables){
        if (closeables==null){
            return;
        }
        for (Closeable closeable:closeables){
            if (closeable!=null){
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
