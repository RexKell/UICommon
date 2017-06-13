package com.ifca.uicommon.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by rex on 2017/5/9.
 * 功能：网络请求工具
 */

public final class NetWorkUtils {
    public enum NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_NO,
        NETWORD_UNKNOW

    }

    public NetWorkUtils() {
    }

    /**
     * 跳转到网络设置界面
     */
    public static void openWirelessSetting(Context context) {
        if (Build.VERSION.SDK_INT > 10) {
            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            context.startActivity(new Intent(Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    /**
     * 获取活动网络信息
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     *
     * @return true 是
     * false 否
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isAvailableByPing(String ip) {
        StringBuffer sf = new StringBuffer();
        sf.append("ping -c 1 -w 1 ");
        sf.append(ip);
        ShellUtils.CommandResult result = ShellUtils.execCmd(sf.toString(), false);
        boolean ret = result.result == 0;
        if (result.errorMsg != null) {
            LogUtil.d("isAvailableByPing errorMsg", result.errorMsg);
        }
        if (result.successMsg != null) {
            LogUtil.d("isAvailableByPing successMsg", result.successMsg);
        }
        sf=null;
        return ret;
    }

    /**
     * 判断移动数据是否打开
     */
    public static boolean getDataEnabled(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnableMethod = telephonyManager.getClass().getDeclaredMethod("getDataEnabled");
            if (null != getMobileDataEnableMethod) {
                return (boolean) getMobileDataEnableMethod.invoke(telephonyManager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开或关闭移动数据
     *
     * @param enabled {@code true}:打开
     *                {@code false}:关闭
     */
    public static void setDataEnabled(Context context, boolean enabled) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Method setMobileDataEnableMethod = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnableMethod) {
                setMobileDataEnableMethod.invoke(telephonyManager, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络是否是4G
     */
    public static boolean is4G(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否打开
     *
     * @return {@code true}:是 {@code false}:否
     */
    public static boolean getWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 设置wifi开关状态
     */
    public static void setWifiEnable(Context context, boolean enable) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (enable) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
        wifiManager.setWifiEnabled(enable);
    }

    /**
     * 判断wifi是否连接状态
     * @return {@code true}:是
     * {@code false}:否
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取网络运营商名称
     * @return 运营商名称
     * */
    public static String getNetworkOperatorName(Context context){
        TelephonyManager tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm!=null?tm.getNetworkOperatorName():"";
    }

    private static final int NETWORK_TYPE_GSM      = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN    = 18;
    /**
     * 获取当前网络类型
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
     *
     * @return 网络类型
     * <ul>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_WIFI   } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_4G     } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_3G     } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_2G     } </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORD_UNKNOW} </li>
     * <li>{@link NetWorkUtils.NetworkType#NETWORK_NO     } </li>
     * </ul>
     */
    public static NetworkType getNetworkType(Context context) {
        NetworkType netType = NetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NetworkType.NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NetworkType.NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NetworkType.NETWORK_4G;
                        break;
                    default:
                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NetworkType.NETWORK_3G;
                        } else {
                            netType = NetworkType.NETWORD_UNKNOW;
                        }
                        break;
                }
            } else {
                netType = NetworkType.NETWORD_UNKNOW;
            }
        }
        return netType;
    }

    /**
     * 获取IP 地址
     *@param useIPv4 是否用IPv4
     *
     * @return  IP地址
     * */

    public static String getIPAddress(boolean useIPv4){
        try {
            for(Enumeration<NetworkInterface> nis=NetworkInterface.getNetworkInterfaces();nis.hasMoreElements();){
                NetworkInterface networkInterface=nis.nextElement();
                //防止小米手机返回10.0.2.15
                if (!networkInterface.isUp())
                    continue;
                for (Enumeration<InetAddress> address=networkInterface.getInetAddresses();address.hasMoreElements();){
                    InetAddress inetAddress=address.nextElement();
                    if (!inetAddress.isLoopbackAddress()){
                      String hostAddress=inetAddress.getHostAddress();
                        boolean isIPv4=hostAddress.indexOf(":")<0;
                        if (useIPv4){
                            if (isIPv4) return hostAddress;
                        }else {
                            if (!isIPv4){
                                int index=hostAddress.indexOf('%');
                                return index<0?hostAddress.toUpperCase():hostAddress.substring(0,index).toUpperCase();
                            }
                        }
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名ip地址
     *@param domain 域名
     *              @return ip地址
     * */
    public static String getDomainAddress(final String domain){
        try {
            ExecutorService exec=Executors.newCachedThreadPool();
            Future<String> fs=exec.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    InetAddress inetAddress;
                        inetAddress=InetAddress.getByName(domain);
                        return inetAddress.getHostAddress();
                }
            });
            return fs.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


}
