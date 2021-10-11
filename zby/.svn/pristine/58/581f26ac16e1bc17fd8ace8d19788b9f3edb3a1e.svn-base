package com.biaozhunyuan.tianyi.common.model.request;

import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.other.SearchModel;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/8/7.
 * <p>
 * 分页加载需要上传的实体类
 */

public class Demand<T> {
    public int pageSize;
    public int pageIndex;
    private String SARG;
    public String sort;
    public String filter;
    public String sortField;
    public String dictionaryNames;
    public String endtime;
    public String starttime;
    public String customerType;
    public String keyword;
    public String key;
    public String value;
    public Map<String, String> keyMap;
    public String fuzzySearch;
    public String searchField;
    public List<T> data;
    public String dictionary;
    public String src;
    public HashMap<String, Map<String, String>> dict;
    private Class<T> clazz;


    public Demand(Class<T> thisClass) {
        clazz = thisClass;
    }

    public Demand() {

    }

    public String getDictName(T item, String key) {
        String value = "";
        try {
            Field field = item.getClass().getDeclaredField(key);
            field.setAccessible(true);
            Object obj = field.get(item);
            String arr[] = dictionaryNames.split(",");
            String dictName = "";
            for (String s : arr) {
                if (s.indexOf(key) >= 0) {
                    dictName = s;
                    break;
                }
            }
            if (TextUtils.isEmpty(dictName)) {
                LogUtils.e("demand", "字典名没有找到::" + key);
            }
            if (dict.get(dictName) == null) {
                return "";
            }
            value = dict.get(dictName).get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value == null ? "" : value;
    }

    public void init(final StringResponseCallBack stringResponseCallBack) {
        dict = new HashMap<String, Map<String, String>>();
        Map<String, String> param = new HashMap<String, String>();
        param.put("pageSize", pageSize + "");
        param.put("pageIndex", pageIndex + "");
        param.put("sort", sort);
        param.put("filter", filter);
        param.put("endtime", endtime);
        param.put("starttime", starttime);
        param.put("keyword", keyword);
        param.put("sortField", sortField);
        param.put("customerType", customerType);
        if (!TextUtils.isEmpty(key)) {
            param.put(key, value);
        }
        param.put("dictionaryNames", dictionaryNames);
        param.put("fuzzySearch", fuzzySearch);
        if (keyMap != null) {
            Set<Map.Entry<String, String>> entries = keyMap.entrySet();
            for (Map.Entry<String, String> set : entries) {
                param.put(set.getKey(), set.getValue() == null ? "" : set.getValue());
            }
        }
        param.put("expressions", getExpressionJson());
        StringRequest.postAsyn(src, param, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    data = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), clazz);
                    dictionary = JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "dictionary");
                    Map<String, List<Map>> dictMap = GsonTool.jsonToHas(dictionary);
                    for (Map.Entry<String, List<Map>> entry : dictMap.entrySet()) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (Map<String, String> m : entry.getValue()) {
                            map.put(m.get("value"), m.get("text"));
                        }
                        dict.put(entry.getKey(), map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                stringResponseCallBack.onResponse(response);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                stringResponseCallBack.onFailure(request, ex);
            }

            @Override
            public void onResponseCodeErro(String result) {
                stringResponseCallBack.onResponseCodeErro(result);
            }
        });
    }

    /**
     * 设置模糊搜索
     *
     * @param tableName
     */
    public void setFuzzySearch(final String tableName) {
        setFuzzySearch(tableName, "");
    }

    public void setFuzzySearch(final String tableName, final String dictNames) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getSearhFields(tableName, dictNames);
            }
        });
    }

    /**
     * 清除模糊搜索参数
     */
    public void resetFuzzySearchField(boolean isSearch) {
        if (isSearch) {
            searchField = SARG;
        } else {
            searchField = "";
            fuzzySearch = "";
        }
    }

    private void getSearhFields(String tableName, final String dictNames) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.模糊搜索 + "?tableName=" + tableName;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SearchModel> searchModels = JsonUtils.jsonToArrayEntity
                        (JsonUtils.pareseData(response), SearchModel.class);
                if (searchModels != null && searchModels.size() > 0) {
                    keyMap = new HashMap<>();
                    String dictionaryName = "";
                    for (SearchModel searchModel : searchModels) {
                        if ("3".equals(searchModel.getInputType())) {
                            keyMap.put("searchField_bool_" + searchModel.getFieldName(), "");
                        } else if ("4".equals(searchModel.getInputType())) {
                            keyMap.put("searchField_datetime_" + searchModel.getFieldName(), "");
                        } else if (("6".equals(searchModel.getInputType())
                                || "8".equals(searchModel.getInputType()))
                                && !TextUtils.isEmpty(searchModel.getDictionary())) {
                            keyMap.put("searchField_dictionary_" + searchModel.getFieldName(), "");

                            dictionaryName += searchModel.getFieldName() + "." + searchModel.getDictionary() + ",";
                        } else {
                            keyMap.put("searchField_string_" + searchModel.getFieldName(), "");
                        }
                    }
                    if (!TextUtils.isEmpty(dictionaryName)) {
                        if (!TextUtils.isEmpty(dictNames)) {
                            dictionaryNames = dictionaryName + dictNames;
                        } else {
                            dictionaryNames = dictionaryName.substring(0, dictionaryName.length() - 1);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
            }
        });
    }

    private String getExpressionJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jo = new JSONObject();
        try {
            jo.put("field", "数量");
            jo.put("expression", "count(1)");
            jsonArray.put(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }
}
