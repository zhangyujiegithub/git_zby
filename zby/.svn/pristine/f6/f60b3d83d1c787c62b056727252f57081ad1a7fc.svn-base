package com.biaozhunyuan.tianyi.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.model.form.FieldInfo;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.common.model.request.ReturnModel;
import com.biaozhunyuan.tianyi.common.model.request.VmBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtils {

    public final static String KEY_STATUS = "Status";
    public final static String KEY_MESSAGE = "Message";
    public final static String KEY_DATA = "Data";

    /****
     * 根据键值获取json中对应的value
     *
     * @param json
     * @param key
     * @return
     * @throws JSONException
     */
    public static String getStringValue(String json, String key)
            throws JSONException {
        JSONObject jo = new JSONObject(json);
        return jo.getString(key);
    }


    /**
     * json转换为map类型
     *
     * @param json 要解析的json
     * @param ob   要转换的实体类
     * @param <T>
     * @return
     */
    public static <T> Map<String, List<T>> JsonToMap(String json, Class<T> ob) {
        Map<String, List<T>> map = new HashMap<>();
        if (!TextUtils.isEmpty(json)) {
            try {
                Map<String, net.sf.json.JSONArray> map1 = net.sf.json.JSONObject.fromObject(json);
                for (Map.Entry<String, net.sf.json.JSONArray> entry : map1.entrySet()) {
                    String key = entry.getKey();
                    List<T> list = jsonToArrayEntity(entry.getValue().toString(), ob);
                    map.put(key, list);
                }
            } catch (Exception e) {
                map = new HashMap<>();
            }
        }
        return map;
    }

    /**
     * 解析表单中字典的json数据
     *
     * @param jsonString 数据
     * @return
     */
    public static HashMap<String, List<HashMap<String, String>>> ConvertJsonToMap(
            String jsonString) {
        HashMap<String, List<HashMap<String, String>>> map = new HashMap<String, List<HashMap<String, String>>>();
        JSONObject jsonObjectsStart;
        try {
            // 解析外面一层
            jsonObjectsStart = new JSONObject(jsonString);
            for (String name : (List<String>) jsonObjectsStart.names()) {
                jsonObjectsStart.getJSONArray(name);
                //map.put(name,((HashMap<String, String>) jsonObjectsStart.getJSONArray(name)));
            }
//            Iterator iteratorStart = jsonObjectsStart.keys();
//            while (iteratorStart.hasNext()) {
//                String objectStart = (String) iteratorStart.next();
//                // 解析里面
//                JSONObject object = jsonObjectsStart.getJSONObject(objectStart);
//                Iterator<String> iterator = object.keys();
//                HashMap<String, String> arrayMap = new HashMap<String, String>();
//                while (iterator.hasNext()) {
//                    String object2 = (String) iterator.next();
//                    arrayMap.put(object2, object.getString(object2));
//                }
            // map.put(objectStart, arrayMap);
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 解析表单中Fields的json数据,转为FieldInfo对象的字段名和字段值的map集合，必须是FieldInfo类型
     *
     * @param jsonStr json字符串
     * @return
     */
    public static HashMap<String, String> ConvertFieldsJson2Map(String jsonStr) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<FieldInfo>>() {
        }.getType(); // 指定集合对象属性
        List<FieldInfo> list = gson.fromJson(jsonStr, type);
        HashMap<String, String> map = new HashMap<String, String>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String name = list.get(i).fieldName;
                String value = list.get(i).fieldValue;
                map.put(name, value);
            }
        }
        return map;
    }

    /**
     * 解析json字符串（通用泛型方法）
     *
     * @param json 波尔云服务器返回的json字符串
     * @param type 实体类型
     * @return
     */
    public static <T> List<T> ConvertJsonToList(String json, Class<T> type) {
        json = json + "";
        json = json.trim();
        try {
            JSONObject jsonObjectsStart = new JSONObject(json);
            json = jsonObjectsStart.get("Data").toString();
            Logger.i(json);
            /**
             * 日志内容换行
             */
            Log.d("3keno3", json);

            GsonBuilder gsonb = new GsonBuilder();

            String regEx = "[0-9]{2}T[0-9]{2}"; // 表示11T11这样的数据
            Pattern pat = Pattern.compile(regEx);

            Gson gson = gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JSONArray jsonArray = new JSONArray(json);
            ArrayList<T> list = new ArrayList<T>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String str = jsonObject.toString();
                Matcher mat = pat.matcher(jsonObject.toString());
                while (mat.find()) {
                    String temp = str.substring(mat.start(), mat.end());
                    str = str.replaceAll(temp, temp.replace("T", " "));// temp.substring(0,temp.lastIndexOf("T"))+" ");
                }
                list.add(gson.fromJson(str, type));
            }
            Logger.i("list.size():" + list.size());
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("JsonUtils", ex.getMessage() + "\r\n" + ex.getStackTrace());
            return null;
        }
    }

    /**
     * 解析 {"pageIndex":0,"pageSize":0,"total":0,"data":[]} 这种数据类型的
     *
     * @param res 返回的数据
     * @param c   要转换的类
     * @param <T> 泛型
     * @return 抓换后的集合
     */
    public static <T> List<T> getDataToList(String res, Class c) {
        List<T> list = new ArrayList<>();
        try {
            list = jsonToArrayEntity(getStringValue(pareseData(res), "data"), c);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 解析实体类中是list集合的属性
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ArrayList<List<T>> ConvertJsonToListModle(String json, Class<T> type) {

        ArrayList<List<T>> list1 = new ArrayList<List<T>>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonObject = jsonArray.getJSONArray(i);
                ArrayList<T> list = new ArrayList<T>();
                for (int j = 0; j < jsonObject.length(); j++) {
                    JSONObject object = jsonObject.getJSONObject(j);
                    list.add(gson.fromJson(object.toString(), type));
                }
                list1.add(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list1;
    }


    /***
     * 把新版Crm接口中带字典集合 的数据源转为通用VmBase实体
     *
     * @param json 数据源
     * @param type 泛型类型
     * @return
     * @throws JSONException
     */
    public static <T> VmBase<T> convertJsonToVmBase(String json, Class<T> type)
            throws JSONException {
        VmBase<T> vmBase = new VmBase<T>();
        List<T> list = JsonUtils.ConvertJsonToList(
                StrUtils.removeRex(JsonUtils.getStringValue(json, "Data")),
                type);
        vmBase.Data = list;
        List<? extends VmBase> baseList = JsonUtils.ConvertJsonToList(json,
                vmBase.getClass());
        if (baseList != null && baseList.size() > 0) {
            vmBase.Dict = baseList.get(0).Dict;
        }
        return vmBase;
    }

    /**
     * 解析json字符串（通用泛型方法）
     *
     * @param jsonString 正常的包括一个集合形式的json字符串，
     * @param type       实体类型
     * @return
     */
    public static <T> List<T> pareseJsonToList(String jsonString, Class<T> type) {
        jsonString = jsonString + "";
        jsonString = jsonString.trim();
        try {
            GsonBuilder gsonb = new GsonBuilder();
            String regEx = "[0-9]{2}T[0-9]{2}"; // 表示11T11这样的数据
            Pattern pat = Pattern.compile(regEx);

            Gson gson = gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            JSONArray jsonArray = new JSONArray(jsonString);
            ArrayList<T> list = new ArrayList<T>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String str = jsonObject.toString();
                Matcher mat = pat.matcher(jsonObject.toString());
                while (mat.find()) {
                    String temp = str.substring(mat.start(), mat.end());
                    str = str.replaceAll(temp, temp.replace("T", " "));// temp.substring(0,temp.lastIndexOf("T"))+" ");
                }
                list.add(gson.fromJson(str, type));
            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("JsonUtils", ex.getMessage() + "\r\n" + ex.getStackTrace());
            return null;
        }
    }

    /**
     * （通用方法）解析Json字符串为指定对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T ConvertJsonObject(String json, Class<T> type) {
        json = json + "";
        json = json.trim();
        try {
            JSONObject jsonObjectsStart = new JSONObject(json);
            json = jsonObjectsStart.get("Data").toString();

            json = StrUtils.removeRex(json);

            GsonBuilder gsonb = new GsonBuilder();

            String regEx = "[0-9]{2}T[0-9]{2}"; // 表示11T11这样的数据
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(json);
            while (mat.find()) {
                String temp = json.substring(mat.start(), mat.end());
                json = json.replaceAll(temp, temp.replace("T", " "));// temp.substring(0,temp.lastIndexOf("T"))+" ");
            }
            Gson gson = gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return gson.fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("JsonUtils", ex.getMessage() + "\r\n" + ex.getStackTrace());
            return null;
        }
    }

    /**
     * （通用方法）解析Json字符串为指定对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T ConvertJsonObject(String json, Type type) {
        json = json + "";
        json = json.trim();
        try {
            JSONObject jsonObjectsStart = new JSONObject(json);
            json = jsonObjectsStart.get("Data").toString();

            json = StrUtils.removeRex(json);

            GsonBuilder gsonb = new GsonBuilder();

            String regEx = "[0-9]{2}T[0-9]{2}"; // 表示11T11这样的数据
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(json);
            while (mat.find()) {
                String temp = json.substring(mat.start(), mat.end());
                json = json.replaceAll(temp, temp.replace("T", " "));// temp.substring(0,temp.lastIndexOf("T"))+" ");
            }
            Gson gson = gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            return gson.fromJson(json, type);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("JsonUtils", ex.getMessage() + "\r\n" + ex.getStackTrace());
            return null;
        }
    }

    /**
     * 针对Gson对泛型的支持不足
     *
     * @param raw
     * @param args
     * @return
     */
    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    /**
     * 解析结果状态
     * <p/>
     * 获得Status:0为失败，1代表成功
     * <p/>
     * Message：服务器返回信息
     *
     * @param json
     * @return
     */
    public static ReturnModel<String> pareseResult(String json) {
        ReturnModel<String> returnModel = new ReturnModel<String>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            returnModel.Status = jsonObject.getInt("Status");
            returnModel.Message = jsonObject.getString("Message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnModel;
    }

    @Deprecated
    // 使用getStringValue(json)方法替代即可
    public static String parseLoginMessage(String data) {
        String dataString = "";
        try {
            // 将服务器的json解析，拿取Data
            JSONObject jsonObject = new JSONObject(data);
            dataString = jsonObject.get("Message").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataString == null ? "" : dataString;
    }

    /**
     * 解析状态码 ReturenModel中的状态码
     *
     * @param data
     * @return
     */
    // 使用getStringValue(json)方法替代即可
    public static String parseStatus(String data) {
        String dataString = "";
        try {
            // 将服务器的json解析，拿取Data
            JSONObject jsonObject = new JSONObject(data);
            dataString = jsonObject.get("Status").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataString == null ? "" : dataString;
    }

    /**
     * 解析结果状态
     * <p/>
     * 获得Data中的字符串
     *
     * @param json
     * @return
     */
    public static String pareseData(String json) {
        String result = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            result = jsonObject.getString("Data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析结果Message
     * <p/>
     * 获得Message中的字符串
     *
     * @param json
     * @return
     */
    public static String pareseMessage(String json) {
        String result = "";
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json.toLowerCase());
            result = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存实体对象转化为JSONObject对象, 需要转化的实体对象必须所有属性public修饰
     *
     * @param info 实体对象（）
     * @param c    实体对象类型
     * @return Json对象
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static JSONObject initJsonObj(Object info, Class c)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        JSONObject jo = new JSONObject();
        Field[] fields = c.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object obj = field.get(info);
            jo.put(field.getName(), obj);
            Logger.i(field.getName() + "---" + obj);
        }
        return jo;
    }

    /**
     * 保存实体对象转化为JSONObject对象[Google库转换]
     *
     * @param info 实体对象（）
     * @param
     * @return Json对象
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static String initJsonObject(JSONObject info)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        String strReqst = "";
        Iterator it = info.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = (String) info.get(key);
            strReqst += key + "=" + value + "&";

        }
        if (strReqst.length() > 0) {
            strReqst = strReqst.substring(0, strReqst.length() - 1);
        }

        return strReqst;
    }

    /**
     * 保存实体对象转化为JSONObject对象[Google库转换]
     *
     * @param info 实体对象（）
     * @param
     * @return Json对象
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static JSONObject initJsonObj(Object info)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(info);
        JSONObject jo = new JSONObject(jsonStr);
        return jo;
    }

    /**
     * 保存实体对象转化为JSONObject对象[Google库转换]
     *
     * @param info 实体对象（）
     * @param
     * @return Json对象
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws JSONException
     */
    public static String initJsonString(Object info)
            throws IllegalArgumentException, IllegalAccessException,
            JSONException {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(info);
        return jsonStr;
    }


    /**
     * 使用反射的方法将object类型装换map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, String> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.get(obj) == null) {
                map.put(field.getName(), null);
            } else {
                map.put(field.getName(), field.get(obj) + "");
            }
        }
        return map;
    }


    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
     *
     * @param parameterMap 需要转化的键值对集合
     * @return 字符串
     */
    public static String convertStringParamter(Map<String, String> parameterMap) {
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator iterator = parameterMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    if (parameterBuffer.length() > 0) {
                        parameterBuffer.append("&");
                    }
                    value = parameterMap.get(key);
                    try {
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    parameterBuffer.append(key).append("=").append(value);
                }

            }

        }
        return parameterBuffer.toString();
    }


    /**
     * 根据字典名称获取字典集合
     *
     * @param dictName
     * @return
     */
    public static List<ReturnDict> getDictByName(String dictJson, String dictName) {
        List<ReturnDict> returnDicts = null;
        try {
            String dic2 = JsonUtils.getStringValue(dictJson, dictName);
            returnDicts = GsonTool.jsonToArrayEntity(dic2, ReturnDict.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnDicts;
    }


    /**
     * 根据所需字典id获取value
     *
     * @param list 字典集合
     * @param id
     * @return
     */
    public static String getDictValueById(List<ReturnDict> list, String id) {
        String value = "";
        if (list != null) {
            for (ReturnDict dict : list) {
                if (dict.value.equals(id)) {
                    value = dict.text;
                    return value;
                }
            }
        }
        return value;
    }


    public static <T> List<T> jsonToArrayEntity(String jsonArray, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
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

    /**
     * 将 json转化成实体
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToEntity(String json, Class<T> clazz) throws ParseException {

        try {
            Gson g = new Gson();
            T respones = g.fromJson(json, clazz);
            return respones;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParseException("json转换成实体" + e.toString());

        }
    }

}
