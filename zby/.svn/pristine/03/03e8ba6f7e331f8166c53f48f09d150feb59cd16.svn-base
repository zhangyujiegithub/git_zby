package com.biaozhunyuan.tianyi.log;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.common.utils.EmojiUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.biaozhunyuan.tianyi.view.VoiceInputView;
import com.biaozhunyuan.tianyi.wheel.WheelUtil;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import okhttp3.Request;


/**
 * Created by 王安民 on 2017/8/15.
 * 新建日志页面
 */

public class LogNewActivity extends AppCompatActivity {


    private BoeryunHeaderView headerView;
    private MultipleAttachView attachView;
    private EditText et_content;
    private TextView tv_time;
    private VoiceInputView voiceInputView;
    private LinearLayout llCatrgory;
    private TextView tvCategory;

    private String content = "";

    private Context mContext;
    private WorkRecord mRecord = new WorkRecord();
    private String createTime; //创建时间
    private TimePickerView pickerView;
    private int currentPosition = 0;
    private String logType = "普通日志";
    private boolean isEditChanged = false;
    private String currentContent = "";
    private String[] categorys = new String[]{"普通日志", "周总结", "月总结", "下周目标", "下月目标"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        initViews();
        initIntentData();
        setOnEvent();
        PermissionsUtil.requestPermission(mContext, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        },android.Manifest.permission.RECORD_AUDIO);
    }

    private void initViews() {
        pickerView = new TimePickerView(LogNewActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);

        headerView = (BoeryunHeaderView) findViewById(R.id.header_log_new);
        attachView = (MultipleAttachView) findViewById(R.id.attach_add_log);
        et_content = (EditText) findViewById(R.id.et_content_worklog);
        tv_time = (TextView) findViewById(R.id.tv_time_worklog);
        voiceInputView = (VoiceInputView) findViewById(R.id.voice_view_new_log);
        llCatrgory = findViewById(R.id.ll_select_category_log);
        tvCategory = findViewById(R.id.tv_area_share_new);
        attachView.loadImageByAttachIds("");
        attachView.setIsAdd(true);
        createTime = ViewHelper.getDateToday();
        tv_time.setText(createTime);

        voiceInputView.setRelativeInputView(et_content);
    }


    private void initIntentData() {
        mContext = LogNewActivity.this;
        if (getIntent().getExtras() != null) {  //如果是编辑日志显示编辑的当前日志
            Bundle bundle = getIntent().getBundleExtra("logInfomation");
            if (bundle != null) {
                mRecord = (WorkRecord) bundle.getSerializable("logInfo");
                if (mRecord != null) {
                    Date date = ViewHelper.formatStrToDateAndTime(mRecord.getCreationTimeToString(), "yyyy-MM-dd kk:mm:ss");
                    isEditChanged = false;
                    currentContent = mRecord.getContent();
                    et_content.setText(mRecord.getContent());
                    createTime = mRecord.getCreationTimeToString();
                    tv_time.setText(mRecord.getCreationTimeToString());
                }
            }
            if (getIntent().getStringExtra("log_content") != null) {
                content = getIntent().getStringExtra("log_content");
                getCurrentLog(categorys[0], content);
            }
        } else { //如果是新建日志获取当天的日志
            getCurrentLog(categorys[0], content);
        }
    }

    private void setOnEvent() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {

                ProgressDialogHelper.show(LogNewActivity.this, "保存中");
                uploadAttachAndSaveLog(true, -1);

            }

            @Override
            public void onClickBack() {
                String content = et_content.getText().toString().trim();
                if (!TextUtils.isEmpty(content) && isEditChanged) {
                    showMessage(true, 0);
                } else {
                    finish();
                }
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && !currentContent.equals(s.toString())) {
                    currentContent = s.toString();
                    isEditChanged = true;
                }

            }
        });

        llCatrgory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelUtil.alertBottomWheelOption(LogNewActivity.this, categorys, new WheelUtil.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        String content = et_content.getText().toString().trim();
                        if (postion != currentPosition) {
                            if (!TextUtils.isEmpty(content) && isEditChanged) {
                                showMessage(false, postion);
                                return;
                            } else {
                                switchToOtherType(postion);
                            }
                        }
                    }
                });
            }
        });
    }

    private void switchToOtherType(int postion) {
        if (postion != 0) { //如果不是普通日志，把当前的日志置空，否则会选择别的类型不起作用
            headerView.setTitle("新建" + categorys[postion]);
        } else {
            headerView.setTitle("新建日志");
        }
        tvCategory.setText(categorys[postion]);
        logType = categorys[postion];
        ProgressDialogHelper.show(mContext);
        getCurrentLog(categorys[postion], content);
        currentPosition = postion;
        isEditChanged = false;
    }


    private void uploadAttachAndSaveLog(boolean isFinish, int switchPosition) {
        attachView.uploadImage("task", new IOnUploadMultipleFileListener() {
            @Override
            public void onStartUpload(int sum) {

            }

            @Override
            public void onProgressUpdate(int completeCount) {

            }

            @Override
            public void onComplete(String attachIds) {
                mRecord.setAttachmentIds(attachIds);
                saveLog(isFinish, switchPosition);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            default:
                attachView.onActivityiForResultImage(requestCode,
                        resultCode, data);
                break;
        }
    }

    /**
     * 弹出是否保存提示框
     */
    protected void showMessage(boolean isFinish, int typePos) {
        final AlertDialog alertDialog = new AlertDialog(this).builder();
        alertDialog.setTitle("是否保存");
        alertDialog.setMsg("是否保存当前编辑的内容");
        alertDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFinish) {
                    finish();
                } else {
                    switchToOtherType(typePos);
                    alertDialog.dissMiss();
                }
            }
        }).setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAttachAndSaveLog(isFinish, typePos);
            }
        });
        alertDialog.show();
    }


    /**
     * 获取当天的日志并显示到当前页面
     *
     * @param logType 日志类型：普通日志，周总结，月总结，下周目标，下月目标
     */
    private void getCurrentLog(String logType, String content) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.当天日志 + "?logType=" + logType;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i("当天日志：" + response);
                mRecord = JsonUtils.ConvertJsonObject(response, WorkRecord.class);

                if (mRecord != null) {
                    et_content.setText(mRecord.getContent() + content);
                    et_content.setSelection(et_content.getText().toString().length());
                    currentContent = mRecord.getContent() + content;
                    isEditChanged = false;
                    attachView.loadImageByAttachIds(mRecord.getAttachmentIds());
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                et_content.setText(content);
                attachView.loadImageByAttachIds("");
                mRecord = new WorkRecord();
            }
        });
    }


    /**
     * 保存日志
     */
    private void saveLog(boolean isFinish, int switchPosition) {
        String content = et_content.getText().toString().trim();
        ProgressDialogHelper.dismiss();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(mContext, "内容不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.contains("%")) {
            Toast.makeText(mContext, "非法字符:%", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.contains("&")) {
            Toast.makeText(mContext, "非法字符:&", Toast.LENGTH_SHORT).show();
            return;
        }

        if (EmojiUtils.containsEmoji(content)) {
            Toast.makeText(mContext, "不支持表情符号!", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("log", content);
            jo.put("time", createTime);
            jo.put("logType", logType);
            if (mRecord != null) {
                jo.put("attachmentIds", mRecord.getAttachmentIds());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.日志保存, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (switchPosition >= 0) {
                    switchToOtherType(switchPosition);
                }
                Logger.i("保存日志：" + response);
                String status = JsonUtils.parseStatus(response);
                if (status.equals("1")) {
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                    LogListActivity.isResume = true;
                    if (isFinish) {
                        finish();
                    }
                } else {
                    try {
                        String errorMessage = JsonUtils.getStringValue(response, JsonUtils.KEY_MESSAGE);
                        Toast.makeText(mContext, "保存失败:" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String response) {
                Toast.makeText(mContext, JsonUtils.pareseData(response), Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        });

    }
}
