package com.biaozhunyuan.tianyi.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.client.ClientTaskListFragment;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DateDeserializer;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.supportAndComment.SupportListPost;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.EmojiUtils;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.view.NumImageView;
import com.biaozhunyuan.tianyi.view.TimePickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_PARTICIPANT;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

@SuppressLint("NewApi")
public class TaskInfoActivityNew extends BaseActivity {

    /**
     * 右箭头
     */
    private ImageView view7;
    private ImageView view8;
    private ImageView view1;
    private ImageView view2;
    private ImageView view3;
    private ImageView view4;
    private ImageView view5;
    private ImageView view6;

    private View line1;
    private View line2;

    private BoeryunHeaderView headerView;
    private AvatarImageView ivHeader; //创建人头像
    private TextView tvCreatorName; //创建人姓名
    private TextView tvCreateTime; //创建时间
    private EditText etContent; //任务内容
    private ImageView ivStatus; //任务状态
    private TextView tvExecutor; //执行人
    private TextView tvTaskType; //任务类型
    private TextView tvStartTime; //任务时间
    private TextView tvEndTime; //任务时间
    private TextView tvParticipant; //任务参与人
    private TextView tvProject; //关联项目
    private MultipleAttachView attachView;//附件
    private EditText etComment; //评论的输入框
    private NumImageView ivLikeNum; //点赞和点赞数量
    private Task mTask;
    private Dynamic dynamic;
    private DictionaryHelper helper;
    private boolean isMineTask; //是否是自己得任务
    private ArrayList<String> projectsList;
    private List<字典> projects;
    private LinearLayout ll_period_type; //周期类型
    private LinearLayout ll_period_date; //周期日
    private TextView tv_period_type; //选择周期类型
    private TextView tv_period_date; //选择周期日
    private String periodType; //选择的周期类型的uuid
    private DictIosPickerBottomDialog dialog_project;
    private DictIosPickerBottomDialog dialog_task_type;
    private DictIosPickerBottomDialog dialog_period_type;
    private DictIosPickerBottomDialog dialog_period_date;
    private TimePickerView pickerView;
    private TimePickerView overPickerView;
    private String[] taskTypes = new String[]{"普通任务", "周期任务"};
    private String URL;
    private LinearLayout llCommand;
    private LinearLayout llCommont;
    private NoScrollListView listview;
    private TextView tv_nocomment;
    private TextView tvEndTimeTit;
    private TextView tvStartTimeTit;
    private Date dateBegin;
    private Date dateOver;

    public static boolean isResume = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_new2);
        helper = new DictionaryHelper(this);
        dialog_project = new DictIosPickerBottomDialog(this);
        dialog_task_type = new DictIosPickerBottomDialog(this);
        dialog_period_type = new DictIosPickerBottomDialog(this);
        dialog_period_date = new DictIosPickerBottomDialog(this);
        pickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        overPickerView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        overPickerView.setTime(ViewHelper.toDate(ViewHelper.getDateToday() + " 23:59:59"));
        overPickerView.setCyclic(true);
        overPickerView.setCancelable(true);
        initView();
        getCompanyProject();
        getIntentData();
        setOnTouch();
    }

    private void setOnTouch() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (isChecked()) {
                    ProgressDialogHelper.show(TaskInfoActivityNew.this, "保存中");
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
                            saveTask(); //保存任务
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
        tvExecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
                    Intent intent = new Intent(TaskInfoActivityNew.this, SelectedNotifierActivity.class);
                    intent.putExtra("isSingleSelect", true);
                    intent.putExtra("title", "选择执行人");
                    startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
                }
            }
        });
        tvParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
                    Intent intent = new Intent(TaskInfoActivityNew.this, SelectedNotifierActivity.class);
                    intent.putExtra("isSingleSelect", false);
                    intent.putExtra("title", "选择参与人");
                    startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                }
            }
        });
        tvProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
                    dialog_project.show(projectsList);
                    dialog_project.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                        @Override
                        public void onSelected(int index) {
                            tvProject.setText(projects.get(index).getName());
                            mTask.setProjectId(projects.get(index).getUuid());
                        }
                    });
                }
            }
        });
        tvTaskType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
                    dialog_task_type.show(taskTypes);
                    dialog_task_type.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                        @Override
                        public void onSelected(int index) {
                            tvTaskType.setText(taskTypes[index]);
                            if (taskTypes[index].equals("周期任务")) {
                                ll_period_date.setVisibility(View.VISIBLE);
                                ll_period_type.setVisibility(View.VISIBLE);
                                line1.setVisibility(View.VISIBLE);
                                line2.setVisibility(View.VISIBLE);
                                tvEndTimeTit.setText("周期结束时");
                                tvStartTimeTit.setText("周期开始时");
                            } else {
                                ll_period_date.setVisibility(View.GONE);
                                ll_period_type.setVisibility(View.GONE);
                                line1.setVisibility(View.GONE);
                                line2.setVisibility(View.GONE);
                                tvEndTimeTit.setText("结束时间");
                                tvStartTimeTit.setText("开始时间");
                                tv_period_type.setText("");
                                tv_period_date.setText("");
                                mTask.setCycleDay("");
                                mTask.setCycleType("");
                            }
                        }
                    });
                }
            }
        });
        //选择周期类型
        tv_period_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
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
            }
        });
        //选择周期日
        tv_period_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
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
            }
        });
        ivLikeNum.setOnClickListener(new View.OnClickListener() {
            private int likeNumber;

            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mTask.getUuid());
                post.setDataType("任务计划");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mTask.getExecutorIds());
                if (mTask.isLike()) { //取消点赞
                    likeNumber = mTask.getLikeNumber() - 1;
                    ivLikeNum.setImageDrawable(getResources().getDrawable(R.drawable.icon_like));
                    ivLikeNum.setNum(likeNumber);
                    cancleSupport(post);
                    mTask.setLike(false);
                } else {
                    likeNumber = (mTask.getLikeNumber() + 1);
                    ivLikeNum.setNum(likeNumber);
                    ivLikeNum.setImageDrawable(getResources().getDrawable(R.drawable.icon_like_select));
                    mTask.setLike(true);
                    support(post);
                }
                mTask.setLikeNumber(likeNumber);
            }
        });
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    SupportAndCommentPost post = new SupportAndCommentPost();
                    post.setDataId(mTask.getUuid());
                    post.setDataType("任务计划");
                    post.setFromId(Global.mUser.getUuid());
                    post.setToId(mTask.getExecutorIds());
                    post.setContent(etComment.getText().toString());
                    comment(post);
                }
                return false;
            }
        });
        ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mTask.getUuid())) {
                    showShortToast("此状态不可更改");
                    return;
                }
                if (mTask.getExecutorIds().equals(Global.mUser.getUuid())) {
                    if (TaskStatusEnum.进行中.getName().equals(mTask.getStatus()) ||
                            TaskStatusEnum.已逾期.getName().equals(mTask.getStatus())) {
                        saveTask(mTask, 1);
                    } else if (TaskStatusEnum.已完成.getName().equals(mTask.getStatus())) {
                        saveTask(mTask, 3);
                    } else {
                        showShortToast("当前任务状态下不能修改任务状态!");
                    }
                } else {
                    showShortToast("您不是执行人，无法修改任务状态!");
                }
            }
        });
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
                    pickerView.show();
                }
            }
        });
        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMineTask) {
                    overPickerView.show();
                }
            }
        });

        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvStartTime.setText(ViewHelper.formatDateToStr(date, "yyyy-MM-dd"));
            }
        });

        overPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvEndTime.setText(ViewHelper.formatDateToStr(date, "yyyy-MM-dd"));
            }
        });
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("taskIntentInfo");
            if (bundle != null) {
                mTask = (Task) bundle.getSerializable("taskInfo");
            }
            dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            if (mTask != null) {
                initData();
            }
            if (dynamic != null) {
                getTaskInfo();
            }
        }
    }

    /**
     * 显示任务详细
     */
    private void initData() {
        getCommentList();
        if (Global.mUser.getUuid().equals(mTask.getCreatorId()) && !TaskStatusEnum.已完成.getName().equals(mTask.getStatus())) {
            isCanEditTask(true);
        } else {
            isCanEditTask(false);
        }
        headerView.setRightTitleVisible(isMineTask);
        etContent.setEnabled(isMineTask);
        attachView.setIsAdd(isMineTask);
        if (mTask.isPeriodTask()) { //判断是否为周期任务
            ll_period_date.setVisibility(View.VISIBLE);
            ll_period_type.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.VISIBLE);
            ImageUtils.displyUserPhotoById(this, mTask.getCreatorId(), ivHeader);
            tvTaskType.setText("周期开始时");
            tvStartTimeTit.setText("周期结束时");
            tvStartTime.setText(ViewHelper.resetDateStringFormat(mTask.getCycleStartTime(), "yyyy-MM-dd"));
            tvEndTime.setText(ViewHelper.resetDateStringFormat(mTask.getCycleEndTime(), "yyyy-MM-dd"));
            tv_period_date.setText(mTask.getCycleDayName());
            tv_period_type.setText(mTask.getCycleTypeName());
        } else {
            ll_period_date.setVisibility(View.GONE);
            ll_period_type.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            tvStartTime.setText(ViewHelper.resetDateStringFormat(mTask.getBeginTime(), "yyyy-MM-dd"));
            tvEndTime.setText(ViewHelper.resetDateStringFormat(mTask.getEndTime(), "yyyy-MM-dd"));
            ImageUtils.displyUserPhotoById(this, mTask.getCreatorId(), ivHeader);
        }

        if (TextUtils.isEmpty(mTask.getUuid())) {
            llCommand.setVisibility(View.GONE);
            llCommont.setVisibility(View.GONE);
        }
        tvCreateTime.setText(StrUtils.pareseNull(mTask.getCreationTime()) + "创建");//创建时间
        tvCreatorName.setText(helper.getUserNameById(mTask.getCreatorId()));//创建人名称
        //设置头像
        ivLikeNum.setNum(mTask.getLikeNumber()); //设置点赞数量
        if (mTask.isLike()) {
            ivLikeNum.setImageDrawable(getResources().getDrawable(R.drawable.icon_like_select));
        } else {
            ivLikeNum.setImageDrawable(getResources().getDrawable(R.drawable.icon_like));
        }
        if (!TextUtils.isEmpty(mTask.getProjectId())) {
            getProjectName(mTask.getProjectId(), tvProject); //设置项目名称
        }
        etContent.setText(mTask.getContent()); //任务内容
        tvExecutor.setText(helper.getUserNameById(mTask.getExecutorIds()));//执行人名称
        tvParticipant.setText(helper.getUserNamesById(mTask.getParticipantIds()));//任务参与人
        attachView.loadImageByAttachIds(mTask.getAttachmentIds());
        /**
         * 根据任务状态枚举类型显示状态
         */
        if (TaskStatusEnum.已完成.getName().equals(mTask.getStatus())) {
            ivStatus.setImageResource(R.drawable.icon_status_finish);
        } else if (TaskStatusEnum.进行中.getName().equals(mTask.getStatus())) {
            ivStatus.setImageResource(R.drawable.icon_status_);
        } else if (TaskStatusEnum.已取消.getName().equals(mTask.getStatus())) {
            ivStatus.setImageResource(R.drawable.icon_status_);
        } else if (TaskStatusEnum.已逾期.getName().equals(mTask.getStatus())) {
            ivStatus.setImageResource(R.drawable.icon_status_);
        }
    }

    /**
     * 根据是否可编辑任务 显示或隐藏右箭头
     */
    private void isShowRightArrow() {
        if (isMineTask) {
            headerView.setTitle("编辑任务");
            headerView.setRightTitle("保存");
            headerView.setRightTitleVisible(true);
            etContent.setEnabled(true);
            ColorStateList csl = getResources().getColorStateList(R.color.black);
            etContent.setTextColor(csl);
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
            view5.setVisibility(View.VISIBLE);
            view6.setVisibility(View.VISIBLE);
            view7.setVisibility(View.VISIBLE);
            view8.setVisibility(View.VISIBLE);
        } else {
            headerView.setTitle("查看任务");
            headerView.setRightTitleVisible(false);
            etContent.setEnabled(false);
            ColorStateList csl = getResources().getColorStateList(R.color.text_time);
            etContent.setTextColor(csl);
            view1.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.INVISIBLE);
            view4.setVisibility(View.INVISIBLE);
            view5.setVisibility(View.INVISIBLE);
            view6.setVisibility(View.INVISIBLE);
            view7.setVisibility(View.INVISIBLE);
            view8.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 完成任务
     *
     * @param status 任务状态：1==已完成，3==未完成
     */
    private void saveTask(Task task, int status) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.改变任务状态 + "?uuid=" + task.getUuid() + "&ticket=" + status;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (status == 1) {
                    mTask.setStatus(TaskStatusEnum.已完成.getName());
                    ivStatus.setImageResource(R.drawable.icon_status_finish);

                    isCanEditTask(false);
                } else {
                    mTask.setStatus(TaskStatusEnum.进行中.getName());
                    ivStatus.setImageResource(R.drawable.icon_status_);

                    isCanEditTask(true);
                }
                if (!mTask.isFromTurnChat()) { //如果是从聊天转过来的任务 退回到聊天界面 不需要刷新列表
                    TaskTodayFragment.isResume = true;
                    TaskAssignFragment.isResume = true;
                    TaskListFragmentNew.isReasume = true;
                    TaskLaneFragment.isReasume = true;
                    TaskWeekFragment.isResume = true;
                    TaskPeriodicFragment.isResume = true;
                    ClientTaskListFragment.isResume = true;
                }
                showShortToast("修改任务状态成功!");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(JsonUtils.pareseData(result));
            }
        });
    }


    /**
     * 是否可以编辑任务
     *
     * @param canEditTask
     */
    private void isCanEditTask(boolean canEditTask) {
        isMineTask = canEditTask;
        isShowRightArrow();
    }

    private void initView() {
        headerView = findViewById(R.id.boeryun_headerview);
        ivHeader = findViewById(R.id.iv_header);
        tvCreatorName = findViewById(R.id.tv_creatorId);
        tvCreateTime = findViewById(R.id.tv_create_time);
        etContent = findViewById(R.id.et_content);
        ivStatus = findViewById(R.id.task_status);
        tvExecutor = findViewById(R.id.new_task_excutor);
        tvTaskType = findViewById(R.id.new_task_type);
        tvStartTime = findViewById(R.id.tv_task_time_start);
        tvEndTime = findViewById(R.id.tv_task_time_end);
        tvParticipant = findViewById(R.id.new_task_participant);
        tvProject = findViewById(R.id.new_task_project);
        attachView = findViewById(R.id.attach_add_task);
        etComment = findViewById(R.id.et_comment);
        ivLikeNum = findViewById(R.id.like_num);
        ll_period_type = findViewById(R.id.ll_choosr_period_type_task_new);
        ll_period_date = findViewById(R.id.ll_choosr_period_date_task_new);
        tv_period_date = findViewById(R.id.new_task_period_date);
        tv_period_type = findViewById(R.id.new_task_period_type);
        tvEndTimeTit = findViewById(R.id.tv_time_end_title);
        tvStartTimeTit = findViewById(R.id.tv_time_start_title);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);
        view7 = findViewById(R.id.view7);
        view8 = findViewById(R.id.view8);
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);

        llCommand = findViewById(R.id.ll_command);
        llCommont = findViewById(R.id.ll_bottom_comment);
        listview = findViewById(R.id.taskinfo_listview);
        tv_nocomment = findViewById(R.id.tv_nocomment);
        attachView.loadImageByAttachIds("");
    }


    /**
     * 通过任务id查询任务详情
     */
    private void getTaskInfo() {
        ProgressDialogHelper.show(this);
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态详情 + "?dataId=" + dynamic.getDataId() + "&dataType=" + dynamic.getDataType();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Task> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Task.class);
                    if (list != null && list.size() > 0) {
                        mTask = list.get(0);
                    }
                    if (mTask != null) {
                        initData();
                        getCommentList();
                        getSupportList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    /**
     * 根据项目id 获取项目名称
     *
     * @param uuid
     * @param tv
     */
    private void getProjectName(String uuid, TextView tv) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取项目名称;
        JSONObject js = new JSONObject();
        try {
            js.put("ids", uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, js, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                tv.setText(JsonUtils.pareseData(response));
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }

    private void getSupportList() {
        SupportListPost post = new SupportListPost();
        post.setDataType("任务计划");
        post.setDataId(mTask.getUuid());
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞列表;
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    ivLikeNum.setNum(list.size());
                    mTask.setLikeNumber(list.size());
                    ivLikeNum.setImageDrawable(getResources().getDrawable(R.drawable.icon_like));
                    for (int i = 0; i < list.size(); i++) {

                        //点赞的人中有当前员工
                        if (list.get(i).getFromId().equals(Global.mUser.getUuid())) {
                            ivLikeNum.setImageDrawable(getResources().getDrawable(R.drawable.icon_like_select));
                            mTask.setLike(true);
                            break;
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

    private void getCommentList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论列表 + "?desc=createTime";
        SupportListPost post = new SupportListPost();
        post.setDataType("任务计划");
        post.setDataId(mTask.getUuid());
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    tv_nocomment.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    listview.setAdapter(getAdapter(list));
                } else {
                    tv_nocomment.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.GONE);
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

    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list_new) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
                TextView tv_content = viewHolder.getView(R.id.tv_time_task_item);
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getFromId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNameById(item.getFromId()));//点赞人姓名
                viewHolder.setTextValue(R.id.tv_time, ViewHelper.convertStrToFormatDateStr(item.getTime(), "MM月dd日 HH:mm"));//点赞时间
                tv_content.setText(item.getContent());  //评论内容
            }
        };
    }

    /**
     * 获取可选择的项目
     */
    private void getCompanyProject() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取可选择的项目;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                projectsList = new ArrayList<>();
                projects = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                if (projects.size() > 0) {
                    for (字典 z : projects) {
                        projectsList.add(z.getName());
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

    /**
     * 保存任务
     */
    private void saveTask() {
        if (DateDeserializer.dateIsBeforoNow(mTask.getEndTime())) { //结束时间在现在之前的，是已逾期任务
            mTask.setStatus(TaskStatusEnum.已逾期.getName());
        } else { //否则是正在进行中的任务
            mTask.setStatus(TaskStatusEnum.进行中.getName());
        }
        if (tvTaskType.getText().toString().equals("周期任务")) {
            //如果是普通任务修改为周期任务，先删除原普通任务
            deleteTask(mTask);
            URL = Global.BASE_JAVA_URL + GlobalMethord.周期任务保存;
        } else {
            URL = Global.BASE_JAVA_URL + GlobalMethord.任务保存;
        }

        StringRequest.postAsyn(URL, mTask, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("保存成功");
                ProgressDialogHelper.dismiss();
                if (!mTask.isFromTurnChat()) { //如果是从聊天转过来的任务 退回到聊天界面 不需要刷新列表
                    TaskTodayFragment.isResume = true;
                    TaskAssignFragment.isResume = true;
                    TaskListFragmentNew.isReasume = true;
                    TaskLaneFragment.isReasume = true;
                    TaskWeekFragment.isResume = true;
                    TaskPeriodicFragment.isResume = true;
                    ClientTaskListFragment.isResume = true;
                }
                finish();
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
     * 删除任务
     */
    private void deleteTask(Task item) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情删除卡片;
        JSONObject jo = new JSONObject();
        try {
            jo.put("ids", item.getUuid());
            jo.put("tableName", "oa_work_scheduling");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
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
     * 判断是否符合保存条件
     *
     * @return
     */
    private boolean isChecked() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String content = etContent.getText().toString().trim();
        String beginTime = tvStartTime.getText().toString().trim();
        String overTime = tvEndTime.getText().toString().trim();

        try {
            dateBegin = format.parse(beginTime);
            dateOver = format.parse(overTime);
        } catch (ParseException e) {
            try {
                dateBegin = format1.parse(beginTime);
                dateOver = format1.parse(overTime);
            } catch (ParseException e1) {
                dateBegin = new Date();
                dateOver = new Date();
                e1.printStackTrace();
            }
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
     * 点赞
     *
     * @param post
     */
    public void support(SupportAndCommentPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                TaskTodayFragment.isResume = true;
                TaskAssignFragment.isResume = true;
                TaskListFragmentNew.isReasume = true;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ivLikeNum.setImageDrawable(getDrawable(R.drawable.icon_like));
//                ivLikeNum.setBackgroundResource(R.drawable.icon_like);
                ivLikeNum.setNum(mTask.getLikeNumber());
            }
        });
    }

    /**
     * 取消点赞
     *
     * @param post 要取消点赞的实体的ID
     */
    public void cancleSupport(SupportAndCommentPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                TaskTodayFragment.isResume = true;
                TaskAssignFragment.isResume = true;
                TaskListFragmentNew.isReasume = true;
//                ivLikeNum.setBackground(getDrawable(R.drawable.icon_like));
//                ivLikeNum.setNum(mTask.getLikeNumber());
//                Toast.makeText(TaskInfoActivityNew.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ivLikeNum.setNum(mTask.getLikeNumber());
                ivLikeNum.setImageDrawable(getDrawable(R.drawable.icon_like_select));
                showShortToast("取消点赞失败");
            }
        });
    }


    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
        etComment.setText("");
        InputSoftHelper.hiddenSoftInput(this, etComment);

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                getCommentList();
                Toast.makeText(TaskInfoActivityNew.this, "留言成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                getCommentList();
                showShortToast(JsonUtils.pareseData(result));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS: //选择执行人
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        try {
                            List<User> users = userList.getUsers();
                            mTask.setExecutorIds(users.get(0).getUuid());
                            tvExecutor.setText(users.get(0).getName());
                        } catch (IndexOutOfBoundsException e) {
                            showShortToast("没有选择指派人");
                        }
                    }
                    break;
                case REQUEST_SELECT_PARTICIPANT: //选择参与人
                    Bundle bundle1 = data.getExtras();
                    UserList userList1 = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
                    if (userList1 != null) {
                        List<User> users = userList1.getUsers();
                        String participant = "";
                        String mParticipant = "";
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
                        mTask.setParticipantIds(mParticipant);
                        tvParticipant.setText(participant);
                    }
                    break;
                default:
                    attachView.onActivityiForResultImage(requestCode,
                            resultCode, data);
                    break;
            }
        }
    }
}
