package com.biaozhunyuan.tianyi.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.client.ClientTaskListFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/23.
 * 新建任务页面
 */

@SuppressLint("NewApi")
public class NewTaskNewActivity extends BaseActivity {

    private Context context;
    private MultipleAttachView attachView;
    private TextView tv_executor; //任务执行人
    private TextView tv_status; //任务状态
    private TextView tv_complete; //已完成状态
    private TextView tv_unfinished; //未完成状态
    private TextView tv_audit; //待审核状态
    private Drawable drawable_select; //标签的选中状态
    private Drawable drawable_unselect; //标签的未选中状态
    private TextView tv_aftertomorrow; //标签任务开始时间 后天
    private TextView tv_tomorrow; //标签任务开始时间 明天
    private TextView tv_today; //标签任务开始时间 今天
    private TextView tv_oneweek; //标签任务开始时间 一周后
    private TextView tv_aftertomorrow_end; //标签任务结束时间 后天
    private TextView tv_tomorrow_end; //标签任务结束时间 明天
    private TextView tv_today_end; //标签任务结束时间 今天
    private TextView tv_oneweek_end; //标签任务结束时间 一周后
    private TextView tv_remind_after; //标签任务提醒 稍后
    private TextView tv_remind_1hour; //标签任务提醒 1小时后
    private TextView tv_remind_3hour; //标签任务提醒 3小时后
    private EditText et_content;
    private Task mTask;
    private String beginTime = "";//任务开始时间
    private String overTime = ""; //任务结束时间
    public static boolean isFinish = false;
    private volatile int mTaskCount = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_newtask);
        mTask = new Task();
        context = NewTaskNewActivity.this;
        initView();
        initIntentData();
        initData();
        setOnTouch();
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
        }
    }

    /**
     * 展示任务信息
     */
    private void showInfo() {
        et_content.setText(mTask.getContent());
        attachView.loadImageByAttachIds(mTask.getAttachmentIds());
    }

    private void setOnTouch() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        findViewById(R.id.iv_back_headerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawableSelect(tv_status,tv_complete);
                setDrawableUnSelect(tv_unfinished,tv_audit);
            }
        });
        tv_unfinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawableSelect(tv_status,tv_unfinished);
                setDrawableUnSelect(tv_complete,tv_audit);
            }
        });
        tv_audit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawableSelect(tv_status,tv_audit);
                setDrawableUnSelect(tv_complete,tv_unfinished);
            }
        });

        tv_aftertomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setBeginTime(simpleDateFormat.format(ViewHelper.getTomorrow(ViewHelper.getTomorrow(new Date()))));
                setDrawableSelect(tv_aftertomorrow);
                setDrawableUnSelect(tv_tomorrow,tv_today,tv_oneweek);
            }
        });
        tv_tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setBeginTime(simpleDateFormat.format(ViewHelper.getTomorrow(new Date())));
                setDrawableSelect(tv_tomorrow);
                setDrawableUnSelect(tv_aftertomorrow,tv_today,tv_oneweek);
            }
        });
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setBeginTime(ViewHelper.getDateToday());
                setDrawableSelect(tv_today);
                setDrawableUnSelect(tv_aftertomorrow,tv_tomorrow,tv_oneweek);
            }
        });
        tv_oneweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setBeginTime(simpleDateFormat.format(ViewHelper.getAfterWeekDate(new Date())));
                setDrawableSelect(tv_oneweek);
                setDrawableUnSelect(tv_aftertomorrow,tv_tomorrow,tv_today);
            }
        });

        tv_aftertomorrow_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setEndTime(simpleDateFormat.format(ViewHelper.getTomorrow(ViewHelper.getTomorrow(new Date()))));
                setDrawableSelect(tv_aftertomorrow_end);
                setDrawableUnSelect(tv_tomorrow_end,tv_today_end,tv_oneweek_end);
            }
        });
        tv_tomorrow_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setEndTime(simpleDateFormat.format(ViewHelper.getTomorrow(new Date())));
                setDrawableSelect(tv_tomorrow_end);
                setDrawableUnSelect(tv_aftertomorrow_end,tv_today_end,tv_oneweek_end);
            }
        });
        tv_today_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setEndTime(ViewHelper.getDateToday());
                setDrawableSelect(tv_today_end);
                setDrawableUnSelect(tv_aftertomorrow_end,tv_tomorrow_end,tv_oneweek_end);
            }
        });
        tv_oneweek_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setEndTime(simpleDateFormat.format(ViewHelper.getAfterWeekDate(new Date())));
                setDrawableSelect(tv_oneweek_end);
                setDrawableUnSelect(tv_aftertomorrow_end,tv_tomorrow_end,tv_today_end);
            }
        });

        tv_remind_3hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawableSelect(tv_remind_3hour);
                setDrawableUnSelect(tv_remind_1hour,tv_remind_after);
            }
        });
        tv_remind_1hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawableSelect(tv_remind_1hour);
                setDrawableUnSelect(tv_remind_3hour,tv_remind_after);
            }
        });
        tv_remind_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDrawableSelect(tv_remind_after);
                setDrawableUnSelect(tv_remind_1hour,tv_remind_3hour);
            }
        });
        findViewById(R.id.tv_right_title_headerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            saveTask();
                            //如果是选中 每行单独发布一条任务
//                            if (isMutiTask) {
//                                String[] contentArrs = mTask.getContent().split("\n");
//                                mTaskCount = contentArrs.length;
//                                for (String content : contentArrs) {
//                                    if (!TextUtils.isEmpty(content)) {
//                                        mTask.setContent(content);
//                                        saveTask();
//                                    } else {
//                                        mTaskCount--;
//                                    }
//                                }
//                            } else {
//                                mTaskCount = 1;
//                                saveTask(); //保存任务
//                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 判断是否符合保存条件
     *
     * @return
     */
    private boolean isChecked(){
//        format = new SimpleDateFormat("yyyy-MM-dd ");
//        format1 = new SimpleDateFormat("yyyy/MM/dd");

        String content = et_content.getText().toString().trim();
        mTask.setExecutorIds(Global.mUser.getUuid());
//        String beginTime = begin_time.getText().toString().trim();
//        String overTime = over_time.getText().toString().trim();

//        try {
//            dateNow = format.parse(dateString);
//            dateBegin = format1.parse(beginTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


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
        if (TextUtils.isEmpty(mTask.getBeginTime())) {
            showShortToast("开始时间不能为空！");
            return false;
        }
//
        if (TextUtils.isEmpty(mTask.getEndTime())) {
            showShortToast("结束时间不能为空！");
            return false;
        }
//        if (dateNow.getTime() > dateBegin.getTime()) {
//            showShortToast("不能新建任务的开始时间在今天之前的任务");
//            return false;
//        }

        mTask.setContent(content);
        return true;
    }
    private void saveTask(){
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务保存;

        StringRequest.postAsyn(url, mTask, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
//                mTaskCount--;
//                if (mTaskCount == 0) {
                    if (isFinish) {
                        TaskInfoActivity.isFinish = true;
                    }
                    showShortToast("保存成功");
                    ProgressDialogHelper.dismiss();
                    TaskListActivity.isResume = true;
                    TaskDayViewFragment.isResume = true;
                    TaskListFragment.isReasume = true;
                    TaskWeekFragment.isResume = true;
                    ClientTaskListFragment.isResume = true;
                    TaskLaneFragment.isReasume = true;
                    finish();
//                }
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

    private void initData() {
        drawable_select = context.getResources().getDrawable(R.drawable.task_new_status_bg_comlete_select);
        drawable_unselect = context.getResources().getDrawable(R.drawable.task_new_status_bg_comlete);
        tv_executor.setText(Global.mUser.getName());
        attachView.loadImageByAttachIds("");
        attachView.setIsAdd(true);
    }


    private void initView() {

        et_content = findViewById(R.id.et_content_share);
        tv_remind_after = findViewById(R.id.tv_task_remind_after);
        tv_remind_1hour = findViewById(R.id.tv_task_remind_1hour);
        tv_remind_3hour = findViewById(R.id.tv_task_remind_3hour);
        attachView = findViewById(R.id.addImg_share);
        tv_executor = findViewById(R.id.tv_task_executor);
        tv_status = findViewById(R.id.tv_task_status);
        tv_complete = findViewById(R.id.tv_task_complete);
        tv_unfinished = findViewById(R.id.tv_task_unfinished);
        tv_audit = findViewById(R.id.tv_task_audit);
        tv_aftertomorrow_end = findViewById(R.id.tv_task_time_aftertomorrow_end);
        tv_aftertomorrow = findViewById(R.id.tv_task_time_aftertomorrow);
        tv_tomorrow = findViewById(R.id.tv_task_time_tomorrow);
        tv_tomorrow_end = findViewById(R.id.tv_task_time_tomorrow_end);
        tv_today = findViewById(R.id.tv_task_time_today);
        tv_today_end = findViewById(R.id.tv_task_time_today_end);
        tv_oneweek = findViewById(R.id.tv_task_time_oneweek);
        tv_oneweek_end = findViewById(R.id.tv_task_time_oneweek_end);
    }

    private void setDrawableSelect(TextView tv){
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setBackground(drawable_select);
    }

    private void setDrawableSelect(TextView tv,TextView tv1){
        tv.setText(tv1.getText());
        tv1.setTextColor(Color.parseColor("#ffffff"));
        tv1.setBackground(drawable_select);
    }

    private void setDrawableUnSelect(TextView tv,TextView tv1){
        tv.setBackground(drawable_unselect);
        tv1.setBackground(drawable_unselect);
        tv.setTextColor(Color.parseColor("#BABABA"));
        tv1.setTextColor(Color.parseColor("#BABABA"));
    }

    private void setDrawableUnSelect(TextView tv,TextView tv1,TextView tv2){
        tv.setBackground(drawable_unselect);
        tv1.setBackground(drawable_unselect);
        tv2.setBackground(drawable_unselect);
        tv.setTextColor(Color.parseColor("#BABABA"));
        tv1.setTextColor(Color.parseColor("#BABABA"));
        tv2.setTextColor(Color.parseColor("#BABABA"));
    }
}
