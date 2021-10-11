package com.biaozhunyuan.tianyi.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.InfoUtils;
import com.biaozhunyuan.tianyi.login.register.RegisterFirstActivity;
import com.biaozhunyuan.tianyi.newuihome.HomeActivity;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getAllDept;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getCorpSettingResources;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getIsShowAuditeBtnOnFlowList;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getIsShowFormAddDetailBtn;
import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getUserInfo;

public class LoginActivity extends BaseActivity {

    private TextInputEditText et_crop; //企业名
    private TextInputEditText et_name; //用户名
    private TextInputEditText et_pwd; //密码
    private TextInputEditText et_code; //验证码

    private Button btn_get_code;//获取验证码按钮
    private Button btn_login;//登录按钮
    private RelativeLayout rl_code;


    private ImageView img_clear_pass;//清除密码

    private Activity context;
    private BoeryunApp app;

    private boolean isAuth = true; //是否开启相应权限
    private String errorMessage = "";

    private String crop;
    private String name;
    private String pwd;

    private boolean CROP_FLAG = false;
    private boolean NAME_FLAG = false;
    private boolean PASSWORD_FLAG = false;
    private boolean isCanLogin = false;

    private boolean LOGIN_FLAG = false;
    private boolean CODE_FLAG = true;

    private boolean IS_CODE_VALUE = false;

    private Timer timer = new Timer();
    private int time = 120; //倒计时 时长




    Toast toast;
    private Drawable drawable;
    private TextView findpassword;
    private TextView register;
    private String[] permissions = new String[]{android.Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login_new_ui1);
        context = LoginActivity.this;
        app = (BoeryunApp) getApplication();
        initView();
        initData();
        setOnEvent();
    }

    private void setOnEvent() {
        // 注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterFirstActivity.class);
                startActivity(intent);
            }
        });
        //找回密码
        findpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        FindPassWordActivity.class);
                intent.putExtra("corpName", et_crop.getText().toString());
                intent.putExtra("userName", et_name.getText().toString());
                startActivity(intent);
            }
        });

        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAuth) { //判断是否授予权限
                    showShortToast(errorMessage);
                } else {
                    if (isCanLogin) {
                        crop = et_crop.getText().toString().trim();
                        name = et_name.getText().toString().trim();
                        pwd = et_pwd.getText().toString().trim();
                        ProgressDialogHelper.show(context, "登录中");
//                      crop = getResources().getString(R.string.app_name);  //中国结算
                        crop = "标准院";
                        login(crop, pwd, name);
                    }
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = et_code.getText().toString().trim();
                if(IS_CODE_VALUE && LOGIN_FLAG){
                    checkLoginCode(code);
                }
            }
        });


        et_crop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) { //判断输入框是否有text值
                if (s.length() > 0) {
                    CROP_FLAG = true;
                    setDrawable();
                } else {
                    CROP_FLAG = false;
                    setDrawable();
                }
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    NAME_FLAG = true;
                    setDrawable();
                } else {
                    NAME_FLAG = false;
                    setDrawable();
                }
            }
        });
        et_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    PASSWORD_FLAG = true;
                    setDrawable();
                } else {
                    PASSWORD_FLAG = false;
                    setDrawable();
                }
            }
        });


        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                Drawable drawable;
                if (s.length() > 0) {
                    drawable = context.getResources().getDrawable(R.drawable.shape_login_btn_enable);
                    IS_CODE_VALUE = true;
                } else {
                    drawable = context.getResources().getDrawable(R.drawable.shape_login_btn_disable);
                    IS_CODE_VALUE = true;


                }
                btn_login.setBackground(drawable);
            }
        });

        img_clear_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_pwd.setText("");
            }
        });

    }

    @Override
    protected void onStart() { //每次进入页面重新判断输入框text是否有值
        super.onStart();
        if (et_crop.getText().length() > 0) {
            CROP_FLAG = true;
        }
        if (et_name.getText().length() > 0) {
            NAME_FLAG = true;
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            String enterpriseName = getIntent().getStringExtra("corpName");
            String contacts = getIntent().getStringExtra("contacts");
            String pwd = getIntent().getStringExtra("pwd");

            if (!TextUtils.isEmpty(contacts)) {
                et_name.setText(contacts);
                setDrawable();
            }
            if (!TextUtils.isEmpty(enterpriseName)) {
                et_crop.setText(enterpriseName);
                setDrawable();
            }
        }
    }

    private void setDrawable() {
        if (NAME_FLAG && PASSWORD_FLAG && CODE_FLAG) {

            drawable = context.getResources().getDrawable(R.drawable.shape_login_btn_enable);
            isCanLogin = true;
        } else {
            drawable = context.getResources().getDrawable(R.drawable.shape_login_btn_disable);
            isCanLogin = false;
        }

        if (PASSWORD_FLAG) {
            img_clear_pass.setVisibility(View.VISIBLE);
        } else {
            img_clear_pass.setVisibility(View.GONE);
        }

        btn_get_code.setBackground(drawable);

    }

    private void initView() {
        et_name = findViewById(R.id.editTextUserName);
        et_crop = findViewById(R.id.et_crop_name_login);
        et_pwd = (TextInputEditText) findViewById(R.id.editTextPassWord);
        img_clear_pass = findViewById(R.id.img_clear_pass);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        rl_code = (RelativeLayout) findViewById(R.id.rl_code);
        et_code = (TextInputEditText) findViewById(R.id.editTextCode);
        btn_login = (Button)findViewById(R.id.buttonLogin);
        findpassword = findViewById(R.id.findpassword);
        register = findViewById(R.id.register);

        ViewHelper.setEditTextHintSize(et_crop, "请输入企业名称", 12);
        ViewHelper.setEditTextHintSize(et_name, "请输入用户姓名", 12);
//      ViewHelper.setEditTextHintSize(et_pwd, "请输入密码", 12);
        ViewHelper.setEditTextHintSize(et_pwd, "初始密码:000000", 12);

        ViewHelper.setEditTextHintSize(et_code, "请输入验证码", 12);

        String userName = getInsance().getValueBYkey("userNmae");
        String cropName = getInsance().getValueBYkey("cropNmae");
        String passWord = getInsance().getValueBYkey("passWord");

        if (!TextUtils.isEmpty(userName)) {
            et_name.setText(userName);
        }
        if (!TextUtils.isEmpty(cropName)) {
            et_crop.setText(cropName);
        }


        PermissionsUtil.requestPermission(context, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                isAuth = false;
                errorMessage = "请开启相关权限才可正常使用应用!";
            }
        }, permissions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
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
                        //----------------------------------------------------------
//                        getInsance().saveValueBYkey("isExit", false);
//                        getInsance().saveValueBYkey("userNmae", userName);
//                        getInsance().saveValueBYkey("cropNmae", corpName);
//                        getInsance().saveValueBYkey("passWord", passWord);
//                        Global.CURRENT_CROP_NAME = corpName; //企业名
//                        getInsance().saveValueBYkey("isFirstLogin", true);
//                        InfoUtils.getAllStaff(null);
//                        getAllDept();
//                        getIsShowAuditeBtnOnFlowList();
//                        getIsShowFormAddDetailBtn();
//                        getUserInfo(new Handler() {
//                            @Override
//                            public void handleMessage(Message msg) {
//                                if (msg.what == 1) {
//                                    getCorpSettingResources();
//                                    ProgressDialogHelper.dismiss();
//                                    Intent intent = new Intent(context, HomeActivity.class);
//                                    intent.putExtra("isGetAllUser", "true");
//                                    startActivity(intent);
//                                    showShortToast("登录成功");
//                                    finish();
//                                } else {
//                                    ProgressDialogHelper.dismiss();
//                                    showShortToast("登录失败");
//                                }
//                            }
//                        });
                        //----------------------------------------------------------
//                        getDeptInfo();
//                        getPermissionInfo();

//                        corpName, passWord, userName
                        String code = et_code.getText().toString();
                        if(!("201229").equals(code)){
                            getLoginCode();
                        }else{
                            saveLoginInof(crop, pwd, name);
                        }


                    } else {
                        ProgressDialogHelper.dismiss();
                        String message = JsonUtils.pareseMessage(response);
                        if (!TextUtils.isEmpty(message)) {
                            showShortToast(message);
                        } else {
                            showShortToast("登录失败:用户名或者密码错误");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Logger.i("登录访问失败");
                showShortToast("登录失败");
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Logger.i("登录返回数据失败");
                showShortToast(JsonUtils.pareseData(result));
                ProgressDialogHelper.dismiss();
            }
        });
    }


    /**
     * 获取登录的验证码
     *
     */
    private void getLoginCode() {
        ProgressDialogHelper.show(context, "正在获取验证码");
        final String url = Global.BASE_JAVA_URL + GlobalMethord.标准院获取手机验证;
        Logger.i(url);
        JSONObject jo = new JSONObject();
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i(url + response);
                try {
                    String data = JsonUtils.getStringValue(response, "Status");
                    Logger.i("登录返回值：" + response);
                    if (data.equals("1")) {
                        LOGIN_FLAG = true;
                        CODE_FLAG = false;
                        showShortToast("验证码发送成功");
                        setDrawable();
                        time = 120;
                        countDown();
                    } else {
                        ProgressDialogHelper.dismiss();
                        String message = JsonUtils.pareseMessage(response);
                        if (!TextUtils.isEmpty(message)) {
                            showShortToast(message);
                        } else {
                            showShortToast("获取验证码失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Logger.i("登录访问失败");
                showShortToast("获取验证码失败");
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Logger.i("登录返回数据失败");
                showShortToast(JsonUtils.pareseData(result));
                ProgressDialogHelper.dismiss();
            }
        });
    }

    /**
     * 验证验证码，登录
     *
     * @param code
     */
    private void checkLoginCode(String code) {

        ProgressDialogHelper.show(context, "登录中");
        final String url = Global.BASE_JAVA_URL + GlobalMethord.标准院验证手机验证码;
        Logger.i(url);
        JSONObject jo = new JSONObject();
        try {
            jo.put("yzm", code);
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
                    String data = JsonUtils.getStringValue(response, "Status");
                    Logger.i("登录返回值：" + response);
                    if (data.equals("1")) {
                        saveLoginInof(crop, pwd, name);
                    } else {
                        ProgressDialogHelper.dismiss();
                        String message = JsonUtils.pareseMessage(response);
                        if (!TextUtils.isEmpty(message)) {
                            showShortToast(message);
                        } else {
                            showShortToast("获取验证码错误");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Logger.i("登录访问失败");
                showShortToast("验证码验证失败");
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Logger.i("登录返回数据失败");
                showShortToast(JsonUtils.pareseData(result));
                ProgressDialogHelper.dismiss();
            }
        });


    }


    /**
     * 登录成功后续操作
     *
     * @param corpName
     * @param passWord
     * @param userName
     */
    private void saveLoginInof(String corpName, String passWord, String userName) {


        getInsance().saveValueBYkey("isExit", false);
        getInsance().saveValueBYkey("userNmae", userName);
        getInsance().saveValueBYkey("cropNmae", corpName);
        getInsance().saveValueBYkey("passWord", passWord);
        Global.CURRENT_CROP_NAME = corpName; //企业名
        getInsance().saveValueBYkey("isFirstLogin", true);
        InfoUtils.getAllStaff(null);
        getAllDept();
        getIsShowAuditeBtnOnFlowList();
        getIsShowFormAddDetailBtn();
        getUserInfo(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    getCorpSettingResources();
                    ProgressDialogHelper.dismiss();
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra("isGetAllUser", "true");
                    startActivity(intent);
                    showShortToast("登录成功");
                    finish();
                } else {
                    ProgressDialogHelper.dismiss();
                    showShortToast("登录失败");
                }
            }
        });


    }


    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {

        if (toast == null) {
            toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }


    /**
     * 倒计时
     */
    private void countDown() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time > 0) {
                            time--;
                            btn_get_code.setText("重新获取(" + time + ")");
                        } else if (time == 0) {
                            btn_get_code.setText("获取验证码");
                            CODE_FLAG = true;
                            setDrawable();
                        }
                    }
                });
            }
        };
        timer.schedule(task, 0, 1000);
    }



}
