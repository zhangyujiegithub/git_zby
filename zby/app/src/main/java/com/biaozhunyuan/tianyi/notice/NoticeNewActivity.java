package com.biaozhunyuan.tianyi.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.TimePickerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * 新建通知
 */

public class NoticeNewActivity extends BaseActivity {


    private final String[] mTypes = new String[]{"选择员工", "选择部门", "全体员工"};
    public static final String url = Global.BASE_JAVA_URL + GlobalMethord.新建通知;

    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private Toast toast;
    private BoeryunHeaderView headerView;
    private EditText et_tittle; //通知标题
    private EditText et_content; //通知内容
    private LinearLayout ll_receiver; // 通知接收人
    private MultipleAttachView attachView; //附件
    private TimePickerView pickerView;
    private DictionaryHelper dictionaryHelper;
    private TextView tv_receiver_add;
    public static final int REQUEST_SELECT_PARTICIPANT = 101;//选择通知人
    public static final int REQUEST_SELECT_SECTION = 102;//选择通知部门
    private Notice notice = new Notice();
    private String mParticipant = ""; //参与人id
    private Context context;
    private DictIosPickerBottomDialog iosPickerBottomDialog;
    private static final String REQUEST_RECIPIENTLD = "";
    private List<String> select_dept = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notice);
        dictionaryHelper = new DictionaryHelper(context);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnEvent();
        initData();
        initDialog();
    }

    private void initDialog() {
        iosPickerBottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                if (index == 0) {
                    Intent intent = new Intent(NoticeNewActivity.this, SelectedNotifierActivity.class);
                    intent.putExtra("isSingleSelect",false);
                    startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                } else if (index == 1) {
                    Intent intent = new Intent(NoticeNewActivity.this, SelectedDepartmentActicity.class);
                    intent.putExtra("NoticeNewDepartment", false);
                    startActivityForResult(intent, REQUEST_SELECT_SECTION);
                } else if (index == 2) {
                    tv_receiver_add.setText("全体员工");
                    notice.setRecipientIds("全体");
                    notice.setReciveCategory("全体");
                    notice.setCategoryId("0f799151ae3311e7bd4940f2e92d4562");
                }
            }
        });

    }

    private void initData() {
        context = this;
        iosPickerBottomDialog = new DictIosPickerBottomDialog(context);

    }

    private void initViews() {
        headerView = findViewById(R.id.header_new_task);
        et_tittle = findViewById(R.id.et_title_notice_add);
        et_content = findViewById(R.id.et_content_notice_add);
        ll_receiver = findViewById(R.id.ll_receiver_notice_add);
        attachView = findViewById(R.id.multipleattachview_notice_add);
        tv_receiver_add = findViewById(R.id.tv_receiver_notice_add);

        attachView.loadImageByAttachIds("");
        attachView.setIsAdd(true);
        notice.setRecipientIds("");
    }

    /**
     * 判断是否有携带的数据
     */
    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            notice = (Notice) getIntent().getSerializableExtra("noticeinfo");
            if (notice != null) {
                showInfo();
            }
        }
    }

    /**
     * 展示任务信息
     */
    private void showInfo() {

        attachView.loadImageByAttachIds(notice.getAttachmentIds());
    }


    /***
     *  点击事件
     */

    private void setOnEvent() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (isChecked()) {
                    AlertDialog dialog = new AlertDialog(NoticeNewActivity.this).builder()
                            .setTitle("发布通知")
                            .setMsg("确认发布?")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ProgressDialogHelper.show(context,"保存中");
                                    saveTask();
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    dialog.show();

                }
            }

            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {


            }
        });
        //选择通知人
        ll_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosPickerBottomDialog.show(mTypes);
            }
        });

    }

    /**
     *
     */
    private void saveTask() {
        attachView.uploadImage("notice", new IOnUploadMultipleFileListener() {
            @Override
            public void onStartUpload(int sum) {

            }

            @Override
            public void onProgressUpdate(int completeCount) {

            }

            @Override
            public void onComplete(String attachIds) {
                Log.e("attachIds", attachIds);
                notice.setAttachmentIds(attachIds);
                submitNotice();
            }
        });

    }

    private void submitNotice() {
        Log.e("tag", "" + notice.getStartTime());

        StringRequest.postAsyn(url, notice, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Log.e("tag", response);
                showShortToast("发布成功");
                NoticeListActivity.isResume = true;
                ProgressDialogHelper.dismiss();
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                showShortToast(INFO_ERRO_SERVER);
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast("发布失败");
            }
        });


    }

    /**
     * 判断是否符合发布条件
     *
     * @return
     */
    private boolean isChecked() {
        String et_title = et_tittle.getText().toString().trim();
        String et_contnt = et_content.getText().toString().trim();
        String tx_participant = tv_receiver_add.getText().toString().trim();

        if (notice == null) {
            notice = new Notice();
        }


        if (TextUtils.isEmpty(et_contnt)) {
            showShortToast("请添加通知内容");
            return false;
        }
        if (TextUtils.isEmpty(et_title)) {
            showShortToast("请添加通知标题");
            return false;
        }
        if (TextUtils.isEmpty(tx_participant)) {
            showShortToast("请选择通知人");
            return false;
        }
        if(et_contnt.contains("%")){
            showShortToast("非法字符%!");
        }


        notice.setStartTime(ViewHelper.getDateString());
        notice.setIsSubmit(true);
        notice.setCategoryName("线上管理中心");
        notice.setContent(et_contnt);
        notice.setTitle(et_title);
        notice.setIsTop(true);
        return true;
    }

    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(NoticeNewActivity.this, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PARTICIPANT: //接受通知人
                    Bundle bundle1 = data.getExtras();
                    UserList userList1 = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
                    if (userList1 != null) {
                        List<User> users = userList1.getUsers();
                        String participant = "";
                        for (User user : users) {
                            mParticipant += user.getUuid() + ",";
                            participant += user.getName() + ",";
                        }
                        if (mParticipant.length() > 0) {
                            mParticipant = mParticipant.substring(0, mParticipant.length() - 1);
                            participant = participant.substring(0, participant.length() - 1);
                        }
                        if (notice == null) {
                            notice = new Notice();
                        }
                        notice.setRecipientIds(mParticipant);
                        notice.setReciveCategory("选择员工");
                        notice.setCategoryId("0f799151ae3311e7bd4940f2e92d4562");
                        tv_receiver_add.setText(participant);
                    }
                    break;
                case REQUEST_SELECT_SECTION: //接受通知部门
                    Bundle bundle = data.getExtras();
                    select_dept = (List<String>) bundle.getSerializable(RESULT_SELECT_USER);
                    String participant = "";
                    String participantIDs = "";

                    if (select_dept.size() > 0) {
                        for (String s : select_dept) {
                            participant += dictionaryHelper.getDepartNameById(s) + ",";
                            participantIDs += s + ",";
                        }
                        if (participant.length() > 0) {
                            participant = participant.substring(0, participant.length() - 1);
                        }

                        if (participantIDs.length() > 0) {
                            participantIDs = participantIDs.substring(0, participantIDs.length() - 1);
                        }

                        if (notice == null) {
                            notice = new Notice();
                        }
                        notice.setRecipientIds(REQUEST_RECIPIENTLD);
                        notice.setReciveCategory("选择部门");
                        notice.setCategoryId("0f799151ae3311e7bd4940f2e92d4562");
                        notice.setRecipientIds(participantIDs);
                        tv_receiver_add.setText(participant);
                        select_dept.clear();
                    }

                default:
                    attachView.onActivityiForResultImage(requestCode,
                            resultCode, data);
                    break;
            }
        }
    }
}
