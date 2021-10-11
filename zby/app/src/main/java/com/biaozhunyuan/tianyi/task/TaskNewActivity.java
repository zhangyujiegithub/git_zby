package com.biaozhunyuan.tianyi.task;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.client.ClientTaskListFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DateDeserializer;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.EmojiUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.biaozhunyuan.tianyi.view.VoiceInputView;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/23.
 * 新建任务页面
 */

public class TaskNewActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENT_NAME = "ClientInfoActivity_clientName";
    public static final String EXTRA_CLIENT_ID = "ClientInfoActivity_id";

    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private BoeryunHeaderView headerView;
    private MultipleAttachView attachView;
    private VoiceInputView inputView;


    private TextView begin_time;  //开始时间
    private TextView over_time;     //结束时间

    private LinearLayout ll_excutors;//选择执行人
    private LinearLayout ll_participant;//选择参与人
    private LinearLayout ll_client;//选择客户

    private TextView tv_excutors;//执行人
    private TextView tv_participant;//参与人
    private TextView tv_client;//客户
    private ImageView iv_mutiTask; //是否每行发布一条任务,默认为false
    private boolean isMutiTask = false;//是否每行发布一条任务,默认为false
    private volatile int mTaskCount;

    private EditText et_content;

    private Toast toast;
    private Context context;
    private TimePickerView pickerView;
    private TimePickerView overPickerView;

    public static final int REQUEST_SELECT_PARTICIPANT = 101; //选择参与人
    public static final int REQUEST_SELECT_EXCUTORS = 102; //选择执行人
    public static final int REQUEST_SELECT_EXCUTORS_STAFF_VIEW = 666; //选择执行人
    private final int REQUEST_SELECT_CLIENT = 1; //选择客户

    private Task mTask = new Task();
    private DictionaryHelper helper;

    private String mExcutors = ""; //执行人id
    private String mParticipant = ""; //参与人id
    public static boolean isFinish = false;
    private String dateString;
    private SimpleDateFormat format;
    private Date dateBegin;
    private Date dateNow;
    private Date dateOver;
    private SimpleDateFormat format1;
    private LinearLayout line_newTask;
    private TextView tv_project; //选择项目
    private TextView tv_task_type; //选择任务类型
    private TextView tv_period_type; //选择周期类型
    private TextView tv_period_date; //选择周期日
    private TextView tv_start_time; //选择开始时间
    private TextView tv_end_time; //选择结束时间
    private List<字典> 字典s = new ArrayList<>();
    private ArrayList<String> datas = new ArrayList();
    private DictIosPickerBottomDialog dialog_project;
    private DictIosPickerBottomDialog dialog_task_type;
    private DictIosPickerBottomDialog dialog_period_type;
    private DictIosPickerBottomDialog dialog_period_date;
    private String[] taskTypes = new String[]{"普通任务", "周期任务"};
    private LinearLayout ll_period_type; //周期类型
    private LinearLayout ll_period_date; //周期日
    private String periodType; //选择的周期类型的uuid
    private String URL;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        dateString = ViewHelper.getCurrentStringTime();
        helper = new DictionaryHelper(this);
        initViews();
        initData();
        getCompanyProject();
        initIntentData();
        setOnEvent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PARTICIPANT: //选择参与人
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
                            try {
                                mParticipant = mParticipant.substring(0, mParticipant.length() - 1);
                                participant = participant.substring(0, participant.length() - 1);
                            } catch (StringIndexOutOfBoundsException e) {
                                mParticipant = mParticipant.substring(0, mParticipant.length() - 1);
                            }
                        }
                        if (mTask == null) {
                            mTask = new Task();
                        }
                        mTask.setParticipantIds(mParticipant);
                        tv_participant.setText(participant);
                    }

                    break;
                case REQUEST_SELECT_EXCUTORS: //选择执行人
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        try {
                            List<User> users = userList.getUsers();
                            mExcutors = users.get(0).getUuid();
                            tv_excutors.setText(users.get(0).getName());
                        } catch (IndexOutOfBoundsException e) {
                            Toast.makeText(TaskNewActivity.this, "没有选择执行人", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (mTask == null) {
                        mTask = new Task();
                    }
                    mTask.setExecutorIds(mExcutors);
                case REQUEST_SELECT_CLIENT: //选择客户
                    if (data != null && data.getExtras() != null) {
                        Client client = (Client) data.getExtras().getSerializable("clientInfo");
                        if (client != null) {
                            mTask.setCustomerId(client.getUuid());
                            tv_client.setText(client.getName());
                        }
                    }

                default:
                    attachView.onActivityiForResultImage(requestCode,
                            resultCode, data);
                    break;
            }
        }
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_new_task);
        attachView = (MultipleAttachView) findViewById(R.id.attach_add_task);

        et_content = (EditText) findViewById(R.id.et_content_new_task);

        ll_excutors = (LinearLayout) findViewById(R.id.ll_choosr_excutors_task_new);
        ll_participant = (LinearLayout) findViewById(R.id.ll_choosr_participant_task_new);
        ll_client = (LinearLayout) findViewById(R.id.ll_client_task_add);

        tv_excutors = (TextView) findViewById(R.id.new_task_excutor);
        tv_participant = (TextView) findViewById(R.id.new_task_participant);
        tv_client = (TextView) findViewById(R.id.tv_client_task_add);
        iv_mutiTask = findViewById(R.id.iv_muti_task);

        begin_time = (TextView) findViewById(R.id.new_task_begin_time);
        over_time = (TextView) findViewById(R.id.new_task_over_time);
        inputView = (VoiceInputView) findViewById(R.id.voice_view_new_task);
        line_newTask = findViewById(R.id.line_newtask);

        tv_project = findViewById(R.id.new_task_project);
        tv_task_type = findViewById(R.id.new_task_type);
        tv_period_date = findViewById(R.id.new_task_period_date);
        tv_period_type = findViewById(R.id.new_task_period_type);
        tv_end_time = findViewById(R.id.tv_task_end_time);
        tv_start_time = findViewById(R.id.tv_task_start_time);

        ll_period_type = findViewById(R.id.ll_choosr_period_type_task_new);
        ll_period_date = findViewById(R.id.ll_choosr_period_date_task_new);

        String[] permission = new String[]{android.Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        PermissionsUtil.requestPermission(context, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, permission);
        inputView.setRelativeInputView(et_content);
        begin_time.setText(ViewHelper.formatDateToStr(new Date(), "yyyy/MM/dd"));
        over_time.setText(ViewHelper.formatDateToStr(ViewHelper.toDate(ViewHelper.getDateToday() + " 23:59:59"), "yyyy/MM/dd"));
        attachView.loadImageByAttachIds("");
        attachView.setIsAdd(true);
//        inputView.closeAllKeyBoard();
    }

    private void initData() {
        context = TaskNewActivity.this;
        pickerView = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        overPickerView = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
        overPickerView.setTime(ViewHelper.toDate(ViewHelper.getDateToday() + " 23:59:59"));
        overPickerView.setCyclic(true);
        overPickerView.setCancelable(true);

        dialog_project = new DictIosPickerBottomDialog(context);
        dialog_task_type = new DictIosPickerBottomDialog(context);
        dialog_period_type = new DictIosPickerBottomDialog(context);
        dialog_period_date = new DictIosPickerBottomDialog(context);
        mTask.setExecutorIds(Global.mUser.getUuid());
        tv_excutors.setText(Global.mUser.getName());
    }

    /**
     * 判断是否有携带的数据
     */
    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            mTask = (Task) getIntent().getSerializableExtra("taskInfo");
            if (mTask != null) {
                showInfo();
            }
            Boolean isNewTask = getIntent().getBooleanExtra("isNewTask", true);
            if (isNewTask) {
                line_newTask.setVisibility(View.VISIBLE);
            } else {
                headerView.setTitle("编辑任务");
                line_newTask.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 展示任务信息
     */
    private void showInfo() {

        //判断是否为周期任务
        if (mTask.isPeriodTask()) {
            tv_task_type.setText("周期任务");
            ll_period_date.setVisibility(View.VISIBLE);
            ll_period_type.setVisibility(View.VISIBLE);
            tv_start_time.setText("周期开始时");
            tv_end_time.setText("周期结束时");
            begin_time.setText(mTask.getCycleStartTime());
            over_time.setText(mTask.getCycleEndTime());
            tv_period_type.setText(mTask.getCycleTypeName());
            tv_period_date.setText(mTask.getCycleDayName());
        } else {
            tv_task_type.setText("普通任务");
            begin_time.setText(ViewHelper.convertStrToFormatDateStr(mTask.getBeginTime(), "yyyy/MM/dd"));
            over_time.setText(ViewHelper.convertStrToFormatDateStr(mTask.getEndTime(), "yyyy/MM/dd"));
        }

        et_content.setText(mTask.getContent());
        attachView.loadImageByAttachIds(mTask.getAttachmentIds());
        tv_excutors.setText(helper.getUserNamesById(mTask.getExecutorIds()));
        tv_participant.setText(helper.getUserNamesById(mTask.getParticipantIds()));
        if (!TextUtils.isEmpty(mTask.getBeginTime())) {
            begin_time.setText(ViewHelper.convertStrToFormatDateStr(mTask.getBeginTime(), "yyyy/MM/dd"));
        }
        if (!TextUtils.isEmpty(mTask.getEndTime())) {
            over_time.setText(ViewHelper.convertStrToFormatDateStr(mTask.getEndTime(), "yyyy/MM/dd"));
        }

        tv_client.setText(mTask.getCustomerName()); //客户名称
        if (!TextUtils.isEmpty(mTask.getCustomerId())) {
            getCustomer(mTask.getCustomerId());
        }

    }

    private void setOnEvent() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (isChecked()) {
                    ProgressDialogHelper.show(context, "保存中");
                    attachView.uploadImage("task", new IOnUploadMultipleFileListener() {
                        @Override
                        public void onStartUpload(int sum) {

                        }

                        @Override
                        public void onProgressUpdate(int completeCount) {

                        }

                        @Override
                        public void onComplete(String attachIds) {
                            mTask.setAttachmentIds(attachIds);
                            //如果是选中 每行单独发布一条任务
                            if (isMutiTask) {
                                String[] contentArrs = mTask.getContent().split("\n");
                                mTaskCount = contentArrs.length;
                                for (String content : contentArrs) {
                                    if (!TextUtils.isEmpty(content)) {
                                        mTask.setContent(content);
                                        saveTask();
                                    } else {
                                        mTaskCount--;
                                    }
                                }
                            } else {
                                mTaskCount = 1;
                                saveTask(); //保存任务
                            }
                        }
                    });
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

        begin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
            }
        });


        over_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overPickerView.show();
            }
        });

        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                begin_time.setText(ViewHelper.formatDateToStr(date, "yyyy/MM/dd"));
            }
        });

        overPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                over_time.setText(ViewHelper.formatDateToStr(date, "yyyy/MM/dd"));
            }
        });

        //选择参与人
        ll_participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskNewActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "选择参与人");
                startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
            }
        });

        //选择执行人
        ll_excutors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskNewActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", true);
                intent.putExtra("title", "选择执行人");
                startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
            }
        });

        ll_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClientListActivity.class);
                intent.putExtra("isSelectCliet", true);
                startActivityForResult(intent, REQUEST_SELECT_CLIENT);
            }
        });

        //是否每行发布一条任务
        iv_mutiTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMutiTask = !isMutiTask;
                if (isMutiTask) {
                    iv_mutiTask.setImageResource(R.drawable.ic_select);
                } else {
                    iv_mutiTask.setImageResource(R.drawable.ic_cancle_select);
                }
            }
        });

        //选择项目
        tv_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_project.show(datas);
                dialog_project.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        tv_project.setText(字典s.get(index).getName());
                        mTask.setProjectId(字典s.get(index).getUuid());
                    }
                });
            }
        });


        tv_task_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_task_type.show(taskTypes);
                dialog_task_type.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        tv_task_type.setText(taskTypes[index]);
                        if (taskTypes[index].equals("周期任务")) {
                            ll_period_date.setVisibility(View.VISIBLE);
                            ll_period_type.setVisibility(View.VISIBLE);
                            tv_start_time.setText("周期开始时");
                            tv_end_time.setText("周期结束时");
                        } else {
                            ll_period_date.setVisibility(View.GONE);
                            ll_period_type.setVisibility(View.GONE);
                            tv_end_time.setText("开始日期");
                            tv_start_time.setText("结束日期");
                            tv_period_type.setText("");
                            tv_period_date.setText("");
                            mTask.setCycleDay("");
                            mTask.setCycleType("");
                        }
                    }
                });
            }
        });

        //选择周期类型
        tv_period_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_period_type.show("dict_cycle_type", true);
                dialog_period_type.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                    @Override
                    public void onSelectedDict(字典 dict) {
                        periodType = dict.getUuid();
                        tv_period_type.setText(dict.getName());
                        mTask.setCycleType(periodType);
                    }
                });
            }
        });
        //选择周期日
        tv_period_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tv_period_type.getText().toString())) {
                    dialog_period_date.show("dict_cycle_days", true, "parent='" + periodType + "'", "");
                    dialog_period_date.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                        @Override
                        public void onSelectedDict(字典 dict) {
                            tv_period_date.setText(dict.getName());
                            mTask.setCycleDay(dict.getUuid());
                        }
                    });
                } else {
                    showShortToast("请先选择周期类型");
                }
            }
        });

    }


    /**
     * 保存任务
     */
    private void saveTask() {
        if (DateDeserializer.dateIsBeforoNow(mTask.getEndTime())) { //结束时间在现在之前的，是已逾期任务
            mTask.setStatus(TaskStatusEnum.已逾期.getName());
        } else { //否则是正在进行中的任务
            mTask.setStatus(TaskStatusEnum.进行中.getName());
        }
        if (tv_task_type.getText().toString().equals("周期任务")) {
            URL = Global.BASE_JAVA_URL + GlobalMethord.周期任务保存;
        } else {
            URL = Global.BASE_JAVA_URL + GlobalMethord.任务保存;
        }

        StringRequest.postAsyn(URL, mTask, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                mTaskCount--;
                if (mTaskCount == 0) {
                    if (isFinish) {
                        TaskInfoActivity.isFinish = true;
                    }
                    showShortToast("保存成功");
                    ProgressDialogHelper.dismiss();
                    if (!mTask.isFromTurnChat()) { //如果是从聊天转过来的任务 退回到聊天界面 不需要刷新列表
                        TaskListActivity.isResume = true;
                        TaskDayViewFragment.isResume = true;
                        TaskListFragment.isReasume = true;
                        TaskLaneFragment.isReasume = true;
                        TaskWeekFragment.isResume = true;
                        TaskPeriodicFragment.isResume = true;
                        ClientTaskListFragment.isResume = true;
                    }
                    finish();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                String data = JsonUtils.pareseData(result);
                showShortToast(data);
            }
        });


    }


    /**
     * 判断是否符合保存条件
     *
     * @return
     */
    private boolean isChecked() {
        format = new SimpleDateFormat("yyyy-MM-dd");
        format1 = new SimpleDateFormat("yyyy/MM/dd");

        String content = et_content.getText().toString().trim();
        String beginTime = begin_time.getText().toString().trim();
        String overTime = over_time.getText().toString().trim();

        try {
            dateNow = format.parse(dateString);
            dateBegin = format1.parse(beginTime);
            dateOver = format1.parse(overTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (mTask == null) {
            mTask = new Task();
        }
        if (TextUtils.isEmpty(content)) {
            showShortToast("内容不能为空！");
            return false;
        }
        if (TextUtils.isEmpty(mTask.getExecutorIds())) {
            showShortToast("执行人不能为空！");
            return false;
        }
//        if (TextUtils.isEmpty(mTask.getProjectId())) {
//            showShortToast("请选择项目！");
//            return false;
//        }
        if (TextUtils.isEmpty(beginTime)) {
            showShortToast("开始时间不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(overTime)) {
            showShortToast("结束时间不能为空！");
            return false;
        }
//        if (dateNow.getTime() > dateBegin.getTime()) {
//            showShortToast("不能新建任务的开始时间在今天之前的任务");
//            return false;
//        }
        if (dateBegin.getTime() > dateOver.getTime()) {
            showShortToast("任务结束时间不能小于开始时间");
            return false;
        }
        if (content.contains("%")) {
            showShortToast("非法字符:%!");
            return false;
        }

        if (EmojiUtils.containsEmoji(content)) {
            showShortToast("不支持表情符号!");
            return false;
        }
        mTask.setContent(content);
        mTask.setBeginTime(beginTime);
        mTask.setEndTime(overTime);
        mTask.setCycleEndTime(overTime);
        mTask.setCycleStartTime(beginTime);
        return true;
    }


    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }


    private void getCustomer(String uuid) {
        String url = Global.BASE_JAVA_URL + "oa/recruitmentRequirements/RecruitmentRequirements/getDict?tableName=crm_customer&id=" + uuid;
//        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典2 + "?tableName=crm_customer&ids=" + uuid;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                tv_client.setText(JsonUtils.pareseData(response));
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
     * 获取可选择的项目
     */
    private void getCompanyProject() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取可选择的项目;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                if (字典s.size() > 0) {
                    for (字典 z : 字典s) {
                        datas.add(z.getName());
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

}
