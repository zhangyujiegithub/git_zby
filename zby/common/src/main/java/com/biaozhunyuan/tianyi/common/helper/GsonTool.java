package com.biaozhunyuan.tianyi.common.helper;


import com.alibaba.fastjson.JSON;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * gson的操作工具类 @Expose
 *  @SerializedName("name")
 *
 * @author 林夕   下午2:03:09   2013年12月6日
 */
public class GsonTool {

    /**
     * 将 json转化成实体
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToEntity(String json, Class<T> clazz) throws ParseException {
       //Logger.e("--json--" + json);
        try {
            //Gson g = new Gson();
            //T respones = g.fromJson(json, clazz);
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
//            Logger.e(e.getLocalizedMessage(), e);
            throw new ParseException("json转换成实体" + json);

        }
    }


    public static <T> List<T> jsonToArrayEntity(String jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            JSONArray ja = new JSONArray(jsonArray);
            int j = ja.length();
            for (int i = 0; i < j; i++) {
                String jsonStr = ja.opt(i).toString();
                list.add(jsonToEntity(jsonStr, clazz));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getLocalizedMessage(), e);
        }

        return list;
    }


    public static List<String> jsonToStringArrayEntity(String jsonArray) {

        List<String> list = new ArrayList<String>();
        try {
            JSONArray ja = new JSONArray(jsonArray);
            for (int i = 0; i < ja.length(); i++) {
                list.add(ja.get(i).toString());
            }
        } catch (Exception e) {
            Logger.e(e.getLocalizedMessage(), e);
        }

        return list;
    }


    /**
     * 将实体转换成json
     *
     * @param clazz
     * @return
     */
    public static <T> String entityToJson(T clazz) throws ParseException {
        try {
            Gson g = new Gson();
            String json = g.toJson(clazz);
            return json;
        } catch (Exception e) {
            Logger.e(e.getLocalizedMessage(), e);
            throw new ParseException("实体转换成json");
        }

    }

    /**
     * 将 json 转换成hasmap
     *
     * @param json
     * @return
     */
    public static HashMap jsonToHas(String json) throws ParseException {
        try {
            Gson gson = new Gson();
            HashMap hm = gson.fromJson(json, HashMap.class);
            return hm;
        } catch (Exception e) {
            // TODO: handle exception
            Logger.e(e.getLocalizedMessage(), e);
            throw new ParseException("json转成hasmap" + json);
        }

    }

    /**
     * 实体转换成 hasmap
     *
     * @param t
     * @return
     */
    public static <T> HashMap EntityToHas(T t) throws ParseException {
        String json = entityToJson(t);
        HashMap hm = jsonToHas(json);
        return hm;
    }


}
