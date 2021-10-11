/**
 *
 */
package com.biaozhunyuan.tianyi.common.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;


/**
 * @author linxi
 * @since preferce管理类  这个是个单利类 可压抑用系统的路径 data/下的SYSTEM_CACHE 配置文件 也可以继续拧自己简历配置文件
 */
public class PreferceManager {


    private static PreferceManager instance;
    private String SYSTEM_CACHE = "SYSTEM_CACHE";

    private PreferceManager() {
    }

    public static PreferceManager getInsance() {
        if (instance == null) {
            instance = new PreferceManager();
        }
        return instance;
    }

    public void init(String path) {
        if (StrUtils.isNullOrEmpty(path)) {
            return;
        }
        SYSTEM_CACHE = path;
    }


    /**
     * 用sdk的缓存路径  SYSTEM_CACHE 文件中
     *
     * @param value 需要储存的value
     * @param key   需要 储存的key
     */
    public void saveValueBYkey(String key, String value) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void saveValueBYkey(String key, int value) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }


    public void saveValueBYkey(String key, boolean value) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    /**
     * 将实体类转化为json保存到本地
     *
     * @param key   保存的key
     * @param clazz 要保存的实体类
     * @param <T>   保存实体类的泛型
     */
    public <T> T getObjectByKey(String key, Class<T> clazz) {
        LogUtils.e("BoeryunApplication:", "获取本地用户信息");
        T object = null;
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        String value = preferences.getString(key, "");

        LogUtils.e("BoeryunApplication:", value);
        if (!TextUtils.isEmpty(value)) {
            try {
                object = JsonUtils.jsonToEntity(value, clazz);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return object;
    }


    /**
     * 清除sdk SYSTEM_CACHE 中数据
     */
    public void clearTable() {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 取值 SYSTEM_CACHE  中的value
     *
     * @param key
     * @return value
     */
    public String getValueBYkey(String key) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public int getValueBYkey(String key, int defaultInt) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultInt);
    }

    public boolean getValueBYkey(String key, boolean defaultBoolean) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultBoolean);
    }

    public void deleValueAndkey(String key) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                SYSTEM_CACHE, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, "");
        editor.commit();
    }


    /**
     * 用sdk的缓存路径  自定义文件名称 文件中
     *
     * @param value     需要储存的value
     * @param key       需要 储存的key
     * @param tableName 需要 创建的表名称
     */
    public void saveValueBYkeyFromTable(String value, String key, String tableName) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 清除sdk 自定义文件名称 中数据
     *
     * @param tableName 表名称
     */
    public void clearTableFromTable(String tableName) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }


    /**
     * 取值 自定义文件名称  中的value
     *
     * @param key
     * @param tableName 表名称
     * @return value
     */
    public String getValueBYkeyFromTable(String key, String tableName) {
        SharedPreferences preferences = BoeryunApp.getInstance().getSharedPreferences(
                tableName, Context.MODE_PRIVATE);
        String value = preferences.getString(key, "");
        return value;
    }

}
