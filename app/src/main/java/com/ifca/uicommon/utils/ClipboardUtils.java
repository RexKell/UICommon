package com.ifca.uicommon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by rex on 2017/5/31.
 * 功能：剪贴板相关工具类
 */

public class ClipboardUtils {
    /**
     * 复制文本到剪贴板
     * @param text 文件
     * */
    public static void copyText(CharSequence text, Context context){
        ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("text",text));
    }

    /**
     * 获取剪切板文本
     * @return 剪切板的文本
     * */
    public static CharSequence getText(Context context){
        ClipboardManager clipboard=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=clipboard.getPrimaryClip();
        if (clipData!=null&&clipData.getItemCount()>0){
            return clipData.getItemAt(0).coerceToText(context);
        }
        return null;
    }
    /**
     * 复制Uri到剪切板
     * @param uri URI
     * */
    public static void copyUri(Context context,Uri uri){
        ClipboardManager clipboar=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboar.setPrimaryClip(ClipData.newUri(context.getContentResolver(),"uri",uri));
    }

    /**
     * 获取剪贴板的uri
     * @return 剪贴板的uri
     * */
    public static Uri getUri(Context context){
        ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=clipboardManager.getPrimaryClip();
        if (clipData!=null&&clipData.getItemCount()>0){
            return clipData.getItemAt(0).getUri();
        }
        return null;
    }

    /**
    * 复制意图到剪贴板
    * */
    public static void copyIntent(Context context,Intent intent){
        ClipboardManager clipboard=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newIntent("intent",intent));
    }
    /**
     * 获取剪贴板的意图
     * @return 剪贴板的意图
     * */
    public static Intent getIntent(Context context){
        ClipboardManager clipboardManager=(ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=clipboardManager.getPrimaryClip();
        if (clipData!=null&&clipData.getItemCount()>0){
            return clipData.getItemAt(0).getIntent();
        }
        return null;
    }
}
