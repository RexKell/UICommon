package com.ifca.uicommon.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rex on 2017/4/26.
 * 功能：字符串处理工具类
 */

public class StringUtil {
    /**
     * 检查手机号码运营商类型
     *
     * @param mobile
     * @return Unicom 联通
     * CHA 电信
     * CMCC 移动
     *
     */
    public static String checkMobileType(String mobile) {
        if (isNotNull(mobile)) {
             if (checkRegEx(mobile,"^1(3[0-2]|5[5,6]|8[5,6])\\d{8}$")){
                 return "Unicom";
             }else if (checkRegEx(mobile, "^1(3[3]|5[3]|8[0,9])\\d{8}$")){
                 return "CHA";
             }else if (checkRegEx(mobile,"^1(3[4-9]|5[012789]|8[78])\\d{8}$")){
               return "CMCC";
             }
        }
        return "";
    }

    /**
     * 特殊字符校验,只能是数字,英文字母和中文
     *
     * @param strs
     * @return 不存在返回true
     */
    public static boolean matchSpecial(String... strs) {
        for (String str : strs) {
            Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
            Matcher m = p.matcher(str);
            if (!m.matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 网络地址校验
     * @param text
     * @return 如果为网络地址则返回真
     */
    public static boolean matchSite(String text) {
        if (Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b").matcher(text).matches()) {
            return true;
        }
        return Pattern.compile("(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*").matcher(text).matches();
    }
    /**
     * 邮箱地址校验
     * @param text
     * @return 如果为邮箱地址则返回真
     */
    public static boolean matchEmail(String text) {
        return Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text).matches();
    }
    /**
     * 手机号码校验
     * @param text
     * @return 如果为手机号码则返回真
     */
    public static boolean matchPhone(String text) {
        return Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches();
    }
    /**
     * 对比校验
     * @param str
     * @param  regEx
     * @return 如果为相同则返回真
     */
    public static boolean checkRegEx(String str, String regEx) {
        boolean result = false;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            result = true;
        }
        return result;
    }

    public static String generateUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断字符串不为空
     *
     * @param strs
     * @return
     */
    public static boolean isNotNull(String... strs) {
        for (String str : strs) {
            if (isNull(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return null == str || "".equals(str) || "null".equals(str);
    }

    /**
     * 判断多个字符串为空
     *
     * @param strs
     * @return
     */
    public static boolean isNull(String... strs) {
        for(String str:strs) {
            if(isNotNull(str)){
                return false;
            }
        }
        return true;
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public static String md5(String key) {
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            key = StringUtil.bytes2HexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            key = String.valueOf(key.hashCode());
        }
        return key;
    }

    /**
     * byte数组转字符
     *
     * @param bytes
     * @return
     */
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * long转byte
     *
     * @param l
     * @return
     */
    public static byte[] longToByte(long l) {
        byte[] byt;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeLong(l);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byt = baos.toByteArray();
        return byt;
    }

    /**
     * byte 转long
     *
     * @param byt
     * @return
     */
    public static long byteToLong(byte[] byt) {
        long l = 0;
        ByteArrayInputStream bais = new ByteArrayInputStream(byt);
        DataInputStream dis = new DataInputStream(bais);
        try {
            l = dis.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * url格式转换
     *
     * @param requestUrl
     * @param mapParams
     * @return
     */
    public static String replaceURL(String requestUrl, Map<String, String> mapParams) {
        // 替换参数数据集
        String patternString = "\\{(" + join(mapParams.keySet(), "|") + ")\\}";

        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(requestUrl);

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, mapParams.get(matcher.group(1)));
        }

        matcher.appendTail(sb);

        matcher.reset();

        return sb.toString();
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String join(Iterator<?> iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return objecttoString(first);
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256); // Java default is 16,
        // probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String objecttoString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String inStrNull(String str) {
        return str == null ? "0" : str.trim();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 将JSON对象转换成URL拼接字符
     * @param json
     * @return
     */
    public static String json2url(JSONObject json) {
        StringBuffer sf = new StringBuffer();
        JSONArray names_json = json.names();
        String name = null, value = null;
        for (int i = 0; i < names_json.length(); i++) {
            try {
                name = names_json.getString(i);
                value = json.getString(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (value != null) {
                sf.append("&" + name + "=" + value);
            }
        }
        if (sf.length() > 0) {
            sf.deleteCharAt(0);
        }
        return sf.toString();
    }
    /**
     * 检测String是否全是中文
     */
    public static boolean checkNameChese(String name) {
        boolean res = true;
        char[] cTemp = name.toCharArray();

        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }

        return res;
    }

    /**
     * 判定输入汉字是否是中文
     */
    public static boolean isChinese(char c) {
        for (char param : chineseParam) {
            if (param == c) {
                return false;
            }
        }

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    private static char[] chineseParam = new char[]{'」', '，', '。', '？', '…', '：', '～', '【', '＃', '、', '％', '＊', '＆', '＄', '（', '‘', '’',
            '“', '”', '『', '〔', '｛', '【', '￥', '￡', '‖', '〖', '《', '「', '》', '〗', '】', '｝', '〕', '』', '”', '）', '！', '；', '—'};

}
