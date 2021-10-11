package com.biaozhunyuan.tianyi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.login.LoginActivity;
import com.biaozhunyuan.tianyi.newuihome.HomeActivity;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.InfoUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.SystemUtil;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getCorpSettingResources;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getUserInfo;

/**
 * Created by 王安民 on 2017/10/3.
 */

public class NavActivity extends BaseActivity {
    private Activity context;
    private BoeryunApp app;
    private String dataType = ""; //推送数据类型
    private String dataId = ""; //推送数据id
    private String URL = ""; //推送需要跳转的URL，不需要跳转URL为空

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    isFirst();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = NavActivity.this;
        app = (BoeryunApp) getApplication();
        setContentView(R.layout.activity_nav);
        initPush();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            dataId = getIntent().getStringExtra("pushDataId");
            dataType = getIntent().getStringExtra("pushDataType");
            URL = getIntent().getStringExtra("URL");
            if ("URL".equals(dataType) && !TextUtils.isEmpty(URL)) {
                Intent intent = new Intent(this, WebviewNormalActivity.class);
                intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dataType);
                intent.putExtra(WebviewNormalActivity.EXTRA_URL, URL);
                startActivity(intent);
                finish();
            } else if (!TextUtils.isEmpty(dataId) && !TextUtils.isEmpty(dataType)) {
                intoInfoByPushType();
            } else {
                isGoHome();
            }
        } else {
            isGoHome();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        XGPushClickedResult clickedResult = XGPushManager
                .onActivityStarted(this);

        if (clickedResult != null) {
            String customContent = clickedResult.getCustomContent();
            Logger.i("clickedResult:" + customContent);

            JSONObject jo = null;
            try {
                jo = new JSONObject(customContent);
                // 获取动态类型 和 数据编号
                dataType = jo.getString("dataType");
                dataId = jo.getString("dataId");
                String url = jo.getString("url");
                if ("URL".equals(dataType) && !TextUtils.isEmpty(url)) {
                    Intent intent = new Intent(this, WebviewNormalActivity.class);
                    intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dataType);
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
                    startActivity(intent);
                    finish();
                } else {
                    intoInfoByPushType();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isGoHome();
            }
        } else {
            getIntentData();
        }


    }


    /**
     * 根据推送数据类型进入详情页面
     */
    private void intoInfoByPushType() {
        Dynamic dynamic = new Dynamic();
        dynamic.setDataType(dataType);
        dynamic.setDataId(dataId);
        Intent intent = new Intent();
        intent.setClass(context, HomeActivity.class);
        intent.putExtra("dynamicInfo", dynamic);
        startActivity(intent);
        finish();
    }


    private void initPush() {
        String mobileType = SystemUtil.getDeviceBrand(); //手机类型
        LogUtils.i("手机类型：：：", mobileType);
        if ("huawei".equalsIgnoreCase(mobileType) || "honor".equalsIgnoreCase(mobileType)) { //华为手机
            HMSAgent.connect(this, new ConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    LogUtils.i("HMS connect end:", rst + "");
                }
            });
        }
    }

    /**
     * 是否进入首页
     */
    private void isGoHome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 判断是否第一次登录
     */
    private void isFirst() {
        boolean isFirst = getInsance().getValueBYkey("isExit", true);
        String userName = getInsance().getValueBYkey("userNmae");
        String passWord = getInsance().getValueBYkey("passWord");
        String cropName = getInsance().getValueBYkey("cropNmae");
        if (!isFirst) { //如果不是第一次登录
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord)) {
                login(cropName, passWord, userName);
            } else {
                skipActivity(LoginActivity.class);
                finish();
            }
        } else {
            skipActivity(LoginActivity.class);
            finish();
        }
    }


    /**
     * 登录用户
     *
     * @param corpName 公司名称
     * @param passWord 密码
     * @param userName 用户名
     */
    public void login(final String corpName, final String passWord, final String userName) {
        final String url = Global.BASE_JAVA_URL + GlobalMethord.登录;
        Logger.i(url);
        JSONObject jo = new JSONObject();
        try {
            jo.put("txtUsername", userName);
            jo.put("enterpriseName", corpName);
            jo.put("txtPassword", passWord);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i(url + response);
                try {
                    String data = JsonUtils.getStringValue(response, "Data");
                    Logger.i("登录返回值：" + response);
                    if (data.equals("1")) {
//                        app.setmChatServer(new ChatServer(context));
                        getUserInfo(new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                if (msg.what == 1) {
                                    InfoUtils.getUpdateUserAndUpdateLocalData();
                                    Global.CURRENT_CROP_NAME = corpName;
                                    getCorpSettingResources();
                                    skipActivity(HomeActivity.class);
                                    finish();
                                } else {
                                    skipActivity(LoginActivity.class);
                                    ProgressDialogHelper.dismiss();
                                    showShortToast("登录失败");
                                    finish();
                                }
                            }
                        });
                    } else {
                        ProgressDialogHelper.dismiss();
                        showShortToast("登录失败:用户名或者密码错误");
                        skipActivity(LoginActivity.class);
                        finish();
                    }
                } catch (JSONException e) {
                    showShortToast("登录失败");
                    skipActivity(LoginActivity.class);
                    finish();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Logger.i("登录访问失败");
                showShortToast("登录失败");
                skipActivity(LoginActivity.class);
                finish();
                ProgressDialogHelper.dismiss();

            }

            @Override
            public void onResponseCodeErro(String result) {
                Logger.i("登录返回数据失败");
                showShortToast("登录失败");
                skipActivity(LoginActivity.class);
                finish();
                ProgressDialogHelper.dismiss();
            }
        });
    }

    /**
     * @param newActivity 新跳转页面
     */
    private void skipActivity(final Class<?> newActivity) {
        Intent intent = new Intent();
        intent.setClass(NavActivity.this, newActivity);
        startActivity(intent);
    }

}
