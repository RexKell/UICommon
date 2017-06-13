package com.ifca.uicommon.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rex on 2017/4/27.
 * 功能：
 */

public class SecuritySharePreferences implements SharedPreferences {
    private Context mContext;
    private SharedPreferences mSharePreferences;


    public SecuritySharePreferences(Context context, String name, int mode) {
        mContext = context;
        if (TextUtils.isEmpty(name)) {
            mSharePreferences = PreferenceManager.getDefaultSharedPreferences(context);
        } else {
            mSharePreferences = mContext.getSharedPreferences(name, mode);
        }
    }

    /**
     * 加密调用
     */
    private String encryptPreference(String plainText) {
        return SharepreferencesEncryptUtil.getInstance(mContext).encrypt(plainText);
    }

    private String decryptPreference(String cipherText) {
        return SharepreferencesEncryptUtil.getInstance(mContext).decrypt(cipherText);
    }

    @Override
    public Map<String, ?> getAll() {
        Map<String, ?> encryptMap = mSharePreferences.getAll();
        Map<String, String> decryptMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : encryptMap.entrySet()) {
            Object cipherText = entry.getValue();
            if (cipherText != null) {
                decryptMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        return decryptMap;
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        String encryptValue = mSharePreferences.getString(encryptPreference(key), null);
        return encryptValue == null ? defValue : decryptPreference(encryptValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        Set<String> encryptSet = mSharePreferences.getStringSet(encryptPreference(key), null);
        if (encryptSet == null) {
            return defValues;
        }
        final Set<String> decryptSet = new HashSet<>();
        for (String encryptValue : encryptSet) {
            decryptSet.add(decryptPreference(encryptValue));
        }
        return decryptSet;
    }

    @Override
    public int getInt(String key, int defValue) {
        String encryptValue = mSharePreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Integer.parseInt(decryptPreference(encryptValue));
    }

    @Override
    public long getLong(String key, long defValue) {
        String encryptValue = mSharePreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Long.parseLong(decryptPreference(encryptValue));
    }

    @Override
    public float getFloat(String key, float defValue) {
        String encryptValue = mSharePreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Float.parseFloat(decryptPreference(encryptValue));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String encryptValue = mSharePreferences.getString(encryptPreference(key), null);
        if (encryptValue == null) {
            return defValue;
        }
        return Boolean.parseBoolean(decryptPreference(encryptValue));
    }

    @Override
    public boolean contains(String key) {
        return mSharePreferences.contains(encryptPreference(key));
    }

    @Override
    public SecurityEditor edit() {
        return new SecurityEditor();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharePreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharePreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
    /**
     * 处理加密过渡
     */
    public void handleTransition(){
        Map<String, ?> oldMap = mSharePreferences.getAll();
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : oldMap.entrySet()){
            newMap.put(encryptPreference(entry.getKey()), encryptPreference(entry.getValue().toString()));
        }
        Editor editor = mSharePreferences.edit();
        editor.clear().commit();
        for (Map.Entry<String, String> entry : newMap.entrySet()){
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.commit();
    }
    /**
     * 自动加密Editor
     * */
    final  class SecurityEditor implements Editor{
        private Editor mEditor;
        public SecurityEditor() {
            mEditor=mSharePreferences.edit();
        }

        @Override
        public Editor putString(String key, String value) {
            mEditor.putString(encryptPreference(key),encryptPreference(value));
            return this;
        }

        @Override
        public Editor putStringSet(String key, Set<String> values) {
            Set<String> encryptSet=new HashSet<>();
            for (String value:values){
                encryptSet.add(encryptPreference(value));
            }
            mEditor.putStringSet(encryptPreference(key),encryptSet);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Integer.toString(value)));
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Long.toString(value)));
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Float.toString(value)));
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            mEditor.putString(encryptPreference(key), encryptPreference(Boolean.toString(value)));
            return this;
        }

        @Override
        public Editor remove(String key) {
            mEditor.remove(encryptPreference(key));
            return this;
        }

        /**
         * Mark in the editor to remove all values from the preferences.
         * @return this
         */
        @Override
        public Editor clear() {
            mEditor.clear();
            return this;
        }

        /**
         * 提交数据到本地
         * @return Boolean 判断是否提交成功
         */
        @Override
        public boolean commit() {

            return mEditor.commit();
        }

        /**
         * Unlike commit(), which writes its preferences out to persistent storage synchronously,
         * apply() commits its changes to the in-memory SharedPreferences immediately but starts
         * an asynchronous commit to disk and you won't be notified of any failures.
         */
        @Override
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public void apply() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mEditor.apply();
            } else {
                commit();
            }
        }
    }
}
