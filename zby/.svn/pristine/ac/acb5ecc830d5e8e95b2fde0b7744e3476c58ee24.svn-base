package com.biaozhunyuan.tianyi.common.utils;


import android.os.Handler;
import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.model.user.Jurisdiction;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/9/21.
 * 获取员工信息帮助类
 */

public class InfoUtils {

    private static ORMDataHelper dataHelper;


    /**
     * 获取所有员工
     */
    public static void getAllStaff(final Handler handler) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Global.BASE_JAVA_URL + GlobalMethord.全体员工;

                StringRequest.getSync(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        List<User> list = JsonUtils.ConvertJsonToList(response, User.class);
                        Logger.i("所有员工的数量：" + list.size());
                        if (list != null) {
                            try {
                                if (dataHelper == null) {
                                    dataHelper = ORMDataHelper
                                            .getInstance(BoeryunApp.getInstance());
                                }

                                Dao<User, Integer> userDao = dataHelper
                                        .getUserDao();
                                if (list != null && list.size() > 0) {
                                    for (User user : list) {
                                        List<User> deleteUsers = userDao.queryBuilder()
                                                .where().eq("uuid", user.getUuid()).query();
                                        if (deleteUsers != null && deleteUsers.size() > 0) {
                                            userDao.update(user);
                                        } else {
                                            userDao.create(user);
                                        }
                                    }
                                    if (handler != null) {
                                        handler.sendEmptyMessage(1);
                                    }
                                }
                            } catch (Exception e1) {
                                Logger.e(getClass().getSimpleName() + "用户列表更新失败：" + e1);
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
        });
    }


    /**
     * 获取更新员工信息并且更新本地数据库（每次进入首页的时候调用，第一次登陆的时候不用调用，因为已经获取了所有员工）
     */
    public static void getUpdateUserAndUpdateLocalData() {
        if (dataHelper == null) {
            dataHelper = ORMDataHelper
                    .getInstance(BoeryunApp.getInstance());
        }

        final Dao<User, Integer> userDao = dataHelper
                .getUserDao();
        String lastUpdateTime = "";
        User user = null;
        try {
            //根据lastUpdateTime倒叙排列获取第一个员工
            user = userDao.queryBuilder()
                    .orderBy("lastUpdateTime", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user != null) {
            //获取这个员工的最后更新时间
            lastUpdateTime = user.getLastUpdateTime();
            //如果最后时间为空，获取一周之前的时间传入（获取 一周之前到现在更新的员工信息）
            if (TextUtils.isEmpty(lastUpdateTime)) {
                lastUpdateTime = ViewHelper.getFormerlyDateStr(7);
            }
        }

        Executor executor = Executors.newSingleThreadExecutor();
        final String finalLastUpdateTime = lastUpdateTime;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Global.BASE_JAVA_URL + GlobalMethord.全体员工 + "?updateTime=" + finalLastUpdateTime;

                StringRequest.getSync(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        List<User> list = JsonUtils.ConvertJsonToList(response, User.class);
                        Logger.i("更新员工的数量：" + list.size());
                        if (list != null && list.size() > 0) {

                            for (User user : list) {
                                try {
                                    List<User> deleteUsers = userDao.queryBuilder()
                                            .where().eq("uuid", user.getUuid()).query();
                                    if (deleteUsers != null && deleteUsers.size() > 0) {
                                        userDao.delete(deleteUsers.get(0));
                                        userDao.create(user);
                                    } else {
                                        userDao.create(user);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
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
        });
    }


    /**
     * 获取所有部门
     */
    public static void getAllDept() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Global.BASE_JAVA_URL + GlobalMethord.部门列表;
                StringRequest.getSync(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Organize> list = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "deptAll"), Organize.class);
                            if (list != null) {
                                try {
                                    if (dataHelper == null) {
                                        dataHelper = ORMDataHelper
                                                .getInstance(BoeryunApp.getInstance());
                                    }

                                    Dao<Organize, Integer> deptDao = dataHelper
                                            .getDeptDao();
                                    if (list != null && list.size() > 0) {
                                        for (Organize organize : list) {
                                            List<Organize> deleteUsers = deptDao.queryBuilder()
                                                    .where().eq("uuid", organize.getUuid()).query();
                                            if (deleteUsers != null && deleteUsers.size() > 0) {
                                                deptDao.update(organize);
                                            } else {
                                                deptDao.create(organize);
                                            }
                                        }
                                    }
                                } catch (Exception e1) {
                                    Logger.e(getClass().getSimpleName() + "部门列表更新失败：" + e1);
                                }
                            }
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
        });
    }


    public static void getUserInfo() {
        getUserInfo(null);
    }


    public static void getUserInfo(final Handler handler) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Global.BASE_JAVA_URL + GlobalMethord.用户信息;

                StringRequest.getSync(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        Global.mUser = JsonUtils.ConvertJsonObject(response, User.class);
                        SharedPreferencesHelper helper = new SharedPreferencesHelper(BoeryunApp.getInstance().getApplicationContext());
                        helper.saveObjectBuKey("GLOBAL_USER", Global.mUser);
                        if (handler != null) {
                            handler.sendEmptyMessage(1);
                        }
                        if (dataHelper == null) {
                            dataHelper = ORMDataHelper
                                    .getInstance(BoeryunApp.getInstance());
                        }

                        Dao<User, Integer> userDao = dataHelper
                                .getUserDao();
                        List<User> deleteUsers = null;
                        try {
                            deleteUsers = userDao.queryBuilder()
                                    .where().eq("uuid", Global.mUser.getUuid()).query();
                            if (deleteUsers != null && deleteUsers.size() > 0) {
                                userDao.delete(deleteUsers);
                                userDao.create(Global.mUser);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
//                if (Global.mUser != null) {
//                    StaffMineFragment.tv_name.setText(Global.mUser.getName() + "，你好");
//                    StaffMineFragment.tv_pos.setText(Global.mUser.getPost());
//                }
                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {
                        if (handler != null) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onResponseCodeErro(String result) {
                        if (handler != null) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
            }
        });

    }

    /**
     * 获取企业配置
     */
    public static void getCorpSettingResources() {
        final String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业资源文件配置 + Global.mUser.getCorpId() + ".json";
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                StringRequest.getAsyn(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {

                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {

                    }

                    @Override
                    public void onResponseCodeErro(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String contactTime = jsonObject.optString("contactTime");
                            String contactTitle = jsonObject.optString("contactTitle");
                            String content = jsonObject.optString("content");
                            String stage = jsonObject.optString("stage");
                            String nextContactContent = jsonObject.optString("nextContactContent");
                            String nextContactTime = jsonObject.optString("nextContactTime");
                            String printShowName = jsonObject.optString("printShowName");

                            Global.CONTACT_TIME = TextUtils.isEmpty(contactTime) ? "联系时间" : contactTime;
                            Global.CONTACT_CONTENT = TextUtils.isEmpty(content) ? "联系内容" : content;
                            Global.CONTACT_STAGE = TextUtils.isEmpty(stage) ? "阶段" : stage;
                            Global.CONTACT_TITLE = TextUtils.isEmpty(contactTitle) ? "联系记录" : contactTitle;
                            Global.CONTACT_NEXTCONTACTTIME = TextUtils.isEmpty(nextContactTime) ? "下次联系时间" : nextContactTime;
                            Global.CONTACT_NEXTCONTENT = TextUtils.isEmpty(nextContactContent) ? "下次联系内容" : nextContactContent;
                            Global.FORM_PAPER = TextUtils.isEmpty(printShowName) ? "套打" : printShowName;
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                });
            }
        });
    }

    public static void getDeptInfo() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Global.BASE_JAVA_URL + GlobalMethord.岗位信息;

                StringRequest.getSync(url, new StringResponseCallBack() {
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
        });

    }

    public static void getPermissionInfo() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String url = Global.BASE_JAVA_URL + GlobalMethord.权限信息;

                StringRequest.getSync(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        List<Jurisdiction> jurisdictions = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Jurisdiction.class);
                        PreferceManager.getInsance().saveValueBYkey("JurisdictionList", listTurnJSONArray(jurisdictions));
                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {

                    }

                    @Override
                    public void onResponseCodeErro(String result) {
                        PreferceManager.getInsance().saveValueBYkey("JurisdictionList", "");
                    }
                });
            }
        });
    }


    /**
     * list转换json
     */
    public static String listTurnJSONArray(List<Jurisdiction> list) {
        String MenuChildItemstr = "";
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (list.size() > 0) {
            JSONObject ItemObj = null;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                ItemObj = new JSONObject();
                try {
                    ItemObj.put("name", list.get(i).getName());
                    ItemObj.put("parent", list.get(i).getParent());
                    ItemObj.put("sort", list.get(i).getSort());
                    ItemObj.put("uuid", list.get(i).getUuid());
                    jsonArray.put(ItemObj);
//                jsonObject.put("Data" , jsonArray);// 获得JSONObject的String
                    MenuChildItemstr = jsonArray.toString().trim();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return MenuChildItemstr;
    }


    /**
     * 获取配置是否在待审批列表显示 审核按钮
     */
    public static void getIsShowAuditeBtnOnFlowList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业配置 + "?name=流程列表不显示审批按钮";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                PreferceManager.getInsance().saveValueBYkey("IsShowAuditeBtnOnFlowList",false);
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
     * 获取配置是否在待审批列表显示 审核按钮
     */
    public static void getIsShowFormAddDetailBtn() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业配置 + "?name=不能添加明细行";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                PreferceManager.getInsance().saveValueBYkey("IsShowFormAddDetailBtn",false);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

}
