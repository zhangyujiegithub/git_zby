package com.biaozhunyuan.tianyi.utils;

import android.content.Context;
import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.helper.Param;
import com.biaozhunyuan.tianyi.helper.ParamCallback;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.models.EnumFunctionPoint;
import com.biaozhunyuan.tianyi.models.MenuChildItem;
import com.biaozhunyuan.tianyi.models.MenuFunction;
import com.biaozhunyuan.tianyi.common.model.other.FunctionBoard;
import com.biaozhunyuan.tianyi.newuihome.MenuCount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;


public class ParamUtils {

    /**
     * 用户自定义日期维护
     */
    public static String USER_DEFINE_DATE_MAINTENANCE = "TODAY";

    /**
     * 功能模块权限
     */
    public static String FUNCTION_MOUDLE_PERMISSIONS = "UserFunctionPointList";

    /**
     * 首页功能看板
     */
    public static String MENUHOME_FUNCTION_BOARD = "MENU_HOME_FUNCTION_BOARD";


    public static String MENU_COUNT = "菜单点击次数";

    public static String MENU_PREFERENCE = "MENU_PREFERENCE" + Global.mUser.getUuid();

    private static SharedPreferencesHelper preferencesHelper;

    /**
     * 向服务器发送请求 存数据
     *
     * @param key
     * @param value
     */
    public static void addParam(String key, String value) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.存储Value;
        final Param param = new Param();
        if (TextUtils.isEmpty(Global.mUser.getUuid())) {
            return;
        }
        param.setKey(key + "-" + Global.mUser.getUuid());
        param.setValue(value);
        StringRequest.postAsyn(url, param, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    /**
     * 向服务器发送请求取出数据
     *
     * @param key
     * @param paramCallback
     */
    public static void getParam(final String key, final ParamCallback paramCallback) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取Value;
        Param param = new Param();
        param.setKey(key + "-" + Global.mUser.getUuid());
        StringRequest.postAsyn(url, param, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.pareseData(response);
                    String value = JsonUtils.getStringValue(data, "value");
                    if (!TextUtils.isEmpty(value)) {//如果value不为null 说明取到了值
                        paramCallback.onParam(value);
                    } else {
                        paramCallback.onFailure();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    paramCallback.onFailure();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                paramCallback.onFailure();
            }

            @Override
            public void onResponseCodeErro(String result) {
                paramCallback.onFailure();
            }
        });
    }


    /**
     * 获取菜单次数列表
     */
    public static void getMenuCountList(Context context) {
        preferencesHelper = new SharedPreferencesHelper(context);
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取Value;
        Param param = new Param();
        param.setKey(MENU_COUNT + "-" + Global.mUser.getUuid());

        StringRequest.postAsyn(url, param, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.pareseData(response);
                    String value = JsonUtils.getStringValue(data, "value");
                    List<MenuCount> menuCounts = JsonUtils.jsonToArrayEntity(value, MenuCount.class);
                    preferencesHelper.putListData(MENU_PREFERENCE, menuCounts);
                } catch (JSONException e) {
                    e.printStackTrace();
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


    /**
     * 按照点击次数给菜单排序
     *
     * @param list 排好序的菜单集合
     * @return
     */
    public static List<MenuChildItem> sortMenuByClickNumber(List<MenuChildItem> list) {
        List<MenuCount> listData = preferencesHelper.getListData(MENU_PREFERENCE, MenuCount.class);

        if (listData.size() == 0) { //如果本地没有保存就
            for (MenuChildItem item : list) {
                MenuCount count = new MenuCount();
                count.setTittle(item.title);
                count.setCount(0);
                listData.add(count);
            }
            preferencesHelper.putListData(MENU_PREFERENCE, listData);
        } else {
            for (MenuChildItem item : list) {
                //判断本地是否包含该菜单，如果包含，更新点击次数
                //如果不包含，在本地添加该菜单
                boolean isContain = false;
                for (MenuCount count : listData) {
                    if (item.title.equals(count.getTittle())) {
                        item.count = count.getCount();
                        isContain = true;
                        continue;
                    }
                }
                if (!isContain) {
                    MenuCount count = new MenuCount();
                    count.setTittle(item.title);
                    count.setCount(0);
                    listData.add(count);
                }
            }
            preferencesHelper.putListData(MENU_PREFERENCE, listData);
        }
        return list;
    }


    /**
     * 点击次数加一并上传
     *
     * @param point
     */
    public static void clickMenuAddParams(EnumFunctionPoint point) {
        MenuFunction function = new MenuFunction();
        MenuChildItem menuByPoint = function.getMenuByPoint(point);
        List<MenuCount> listData = preferencesHelper.getListData(MENU_PREFERENCE, MenuCount.class);
        if (menuByPoint != null) {
            for (MenuCount count : listData) {
                if (count.getTittle().equals(menuByPoint.title)) {
                    count.setCount(count.getCount() + 1);
                    break;
                }
            }
            preferencesHelper.putListData(MENU_PREFERENCE, listData);
            try {
                addParam(MENU_COUNT, JsonUtils.initJsonString(listData));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * list转换json
     *
     * @param list
     * @return
     */
    public static String listToJson(List<FunctionBoard> list) {
        JSONArray jsonArray = new JSONArray();
        for (FunctionBoard f : list) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("index", f.getIndex());
                jo.put("title", f.getFunction());
                jsonArray.put(jo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

}
