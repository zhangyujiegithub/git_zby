package com.biaozhunyuan.tianyi.common.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    Context context;


    public SharedPreferencesHelper(Context c, String name) {
        context = c;
        sp = context.getSharedPreferences(name, 0);
        editor = sp.edit();
    }

    public SharedPreferencesHelper(Context c) {
        context = c;
        sp = context.getSharedPreferences("ZL.Phone.UserInfo", 0);
        editor = sp.edit();
    }

    // 向SharedPreferences中注入数据
    public void putValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        // 这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.commit();
    }

    // 向SharedPreferences中注入数据
    public void putBooleanValue(String key, boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        // 这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.commit();
    }

    public static void saveStringArray(Context context, String key, String[] booleanArray) {
        SharedPreferences prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        JSONArray jsonArray = new JSONArray();
        for (String b : booleanArray) {
            jsonArray.put(b);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, jsonArray.toString());
        editor.commit();
    }

    // 向SharedPreferences中注入数据
    public void putIntValue(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        // 这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.commit();
    }


    public static String[] getStringArray(Context context, String key, int arrayLength) {
        SharedPreferences prefs = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String[] resArray = new String[arrayLength];
        Arrays.fill(resArray, true);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString(key, "[]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                resArray[i] = jsonArray.getString(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resArray;
    }

    // 根据Key获取对应的Value
    public String getValue(String key) {
        return sp.getString(key, "");
    }

    // 根据Key获取对应的Value
    public boolean getBooleanValue(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    // 根据Key获取对应的Value
    public int getIntValue(String key) {
        return sp.getInt(key, 0);
    }


    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
    public <T> boolean putListData(String key, List<T> list) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        if (list != null && list.size() > 0) {
            String type = list.get(0).getClass().getSimpleName();
            JsonArray array = new JsonArray();
            try {
                switch (type) {
                    case "Boolean":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Boolean) list.get(i));
                        }
                        break;
                    case "Long":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Long) list.get(i));
                        }
                        break;
                    case "Float":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Float) list.get(i));
                        }
                        break;
                    case "String":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((String) list.get(i));
                        }
                        break;
                    case "Integer":
                        for (int i = 0; i < list.size(); i++) {
                            array.add((Integer) list.get(i));
                        }
                        break;
                    default:
                        Gson gson = new Gson();
                        for (int i = 0; i < list.size(); i++) {
                            JsonElement obj = gson.toJsonTree(list.get(i));
                            array.add(obj);
                        }
                        break;
                }
                editor.putString(key, array.toString());
                result = true;

            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
        } else {
            editor.putString(key, "");
            result = true;
        }
        editor.apply();
        return result;
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = sp.getString(key, "");
        LogUtils.i("key=" + key + "\n value=", json);
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }


    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 将实体类转化为json保存到本地
     *
     * @param key   保存的key
     * @param clazz 要保存的实体类
     * @param <T>   保存实体类的泛型
     */
    public <T> T getObjectBuKey(String key, Class<T> clazz) {
        T object = null;
        String value = sp.getString(key, "");

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
     * 将实体类转化为json保存到本地
     *
     * @param key   保存的key
     * @param clazz 要保存的实体类
     * @param <T>   保存实体类的泛型
     */
    public <T> void saveObjectBuKey(String key, T clazz) {
        SharedPreferences.Editor editor = sp.edit();
        String value = "";
        try {
            value = JsonUtils.initJsonString(clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.e("BoeryunApplication:", value);
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @return HashMap
     */
    public <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = sp.getString(key, "");
        HashMap<String, V> map = new HashMap<>();
        if (null == json) {
            json = "";
        }
        try {
            Gson gson = new Gson();
            JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                String entryKey = entry.getKey();
                JsonObject value = (JsonObject) entry.getValue();
                map.put(entryKey, gson.fromJson(value, clsV));
            }
        } catch (Exception e) {
            return map;
        }
        return map;
    }

    // 清除SharedPreferences中的数据，比如点击“忘记密码”
    public void clear() {
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
