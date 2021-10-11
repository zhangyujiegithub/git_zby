package com.biaozhunyuan.tianyi.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;

public class ReSetPasswordActivity extends Activity {

    private Context mContext;
    private BoeryunHeaderView header;
    private BoeryunApp app;
    private EditText etOld;
    private EditText etNew;
    private EditText etNewAgain;
    private Button btnSet;
    private Toast toast;
    private boolean flag_old = false; //是否查看明文--老的密码,默认为不显示
    private boolean flag_new = false; //是否查看明文--新的密码
    private boolean flag_new2 = false; //是否查看明文--新的确认密码

    private ImageView iv_old;
    private ImageView iv_new;
    private ImageView iv_confirm_new;
    private String contractName;
    private String enterpriseName;
    private String oldpsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initData();
        initViews();
        setOnEvent();
    }

    private void initData() {
        mContext = this;
        app = (BoeryunApp) getApplication();
        contractName = getInsance().getValueBYkey("userNmae");
        enterpriseName = getInsance().getValueBYkey("cropNmae");
        oldpsw = getInsance().getValueBYkey("passWord");
    }

    private void initViews() {
        header = (BoeryunHeaderView) findViewById(R.id.header_set_pwd);
        etOld = (EditText) findViewById(R.id.et_old_pwd);
        etNew = (EditText) findViewById(R.id.et_new_pwd);
        etNewAgain = (EditText) findViewById(R.id.et_new_pwd2);
        btnSet = (Button) findViewById(R.id.btn_set_pwd);

        iv_new = (ImageView) findViewById(R.id.iv_set_password_new_read);
        iv_confirm_new = (ImageView) findViewById(R.id.iv_set_password_new2_read);
        iv_old = (ImageView) findViewById(R.id.iv_set_password_old_read);
    }

    private void setOnEvent() {
        header.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {

            @Override
            public void onClickSaveOrAdd() {

            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickBack() {
                finish();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = etOld.getText().toString().trim();
                String newPwd = etNew.getText().toString().trim();
                String newPwdAgain = etNewAgain.getText().toString().trim();

                if (TextUtils.isEmpty(oldPwd)) {
                    showShortToast("请输入原密码");
                    return;
                }

                if (!oldpsw.equals(oldPwd)) {
                    showShortToast("原密码输入错误");
                    return;
                }

                if (TextUtils.isEmpty(newPwd)) {
                    showShortToast("请输入新密码");
                    return;
                }

                if (newPwd.length() < 6) {
                    showShortToast("密码长度至少为6位");
                    return;
                }

                if (!newPwd.equals(newPwdAgain)) {
                    showShortToast("两次密码不一致，请重新输入");
                    return;
                }

                resetPassword(newPwd);
            }
        });

        iv_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_old) {
                    //输入对用户可见的密码
                    etOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_old.setImageResource(R.drawable.icon_set_password_unread);
                    flag_old = false;
                } else {
                    etOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_old.setImageResource(R.drawable.icon_set_password_read);
                    flag_old = true;
                }
            }
        });

        iv_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_new) {
                    //输入对用户可见的密码
                    etNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_new.setImageResource(R.drawable.icon_set_password_unread);
                    flag_new = false;
                } else {
                    etNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_new.setImageResource(R.drawable.icon_set_password_read);
                    flag_new = true;
                }
            }
        });

        iv_confirm_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_new2) {
                    //输入对用户可见的密码
                    etNewAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_confirm_new.setImageResource(R.drawable.icon_set_password_unread);
                    flag_new2 = false;
                } else {
                    etNewAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_confirm_new.setImageResource(R.drawable.icon_set_password_read);
                    flag_new2 = true;
                }
            }
        });
    }

    private void resetPassword(String newPwd) {
        LogUtils.i("ResetPasswordActivity", "开始重新设置密码中。。。");
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.重置密码 + "?pwd=" + newPwd;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast("修改失败");
            }

            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    String status = JsonUtils.parseStatus(response);
                    String data = JsonUtils.getStringValue(response, "Data");
                    if ("1".equals(status)) {
                        LogUtils.i("ResetPasswordActivity", "修改成功");
                        reLogin();
                    } else {
                        showShortToast("修改失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                showShortToast("失败");
                ProgressDialogHelper.dismiss();
            }
        });
    }

    /**
     * 下线通知
     */
    private void reLogin() {
        AlertDialog dialog = new AlertDialog(this).builder()
                .setTitle("重新登录")
                .setMsg("修改密码成功")
                .setCancelable(false)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 关闭现有所有打开的activity
                        BoeryunApp app = (BoeryunApp) getApplication();
                        app.removeALLActivity();
                        getInsance().saveValueBYkey("isExit", true);
                        clearToken();
                        ORMDataHelper.getInstance(mContext).deleteOldDb();
                        XGPushManager.unregisterPush(mContext);
                        Intent intent = new Intent(mContext,
                                LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        finish();
                    }
                });
        dialog.show();
    }

    /**
     * 清除用户的token
     */
    private void clearToken() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.清除设备;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
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
}
