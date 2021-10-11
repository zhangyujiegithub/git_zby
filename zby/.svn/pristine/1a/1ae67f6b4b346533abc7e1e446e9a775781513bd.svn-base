package com.biaozhunyuan.tianyi.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.client.ClientTaskListFragment;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.AttachPlayHelper;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.supportAndComment.CommonFragment;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.supportAndComment.SupportListPost;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BottomCommentView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/24.
 * 任务详情页面
 */

public class TaskInfoActivity extends BaseActivity {


    private BoeryunHeaderView headerView;
    private NoScrollListView lv_attach;
//    private TextView tv_status;

    private TextView tv_excutor; //执行人
    private TextView tv_creator; //发布人
    private TextView tv_content; //内容
    private TextView tv_time;
    private TextView tv_participant;    //参与人
    private TextView tv_begin;    //开始时间
    private TextView tv_over;    //结束时间
    private TextView tv_remind;    //是否提醒
    private CircleImageView iv_head;
    private BottomCommentView commentView;
    private Context context;
    private DictionaryHelper helper;
    private CommonFragment fragment;
    private Task mTask;
    private Dynamic dynamic;
    private DictIosPickerBottomDialog dialog;
    private String[] status = new String[]{"完成", "取消"};
    public static boolean isFinish = false;
    private LinearLayout ll_support;
    private TextView tv_comment;
    private ImageView iv_support;
    private TextView tv_support;
    private TextView tv_nocomment;
    private String supportUser = "";
    private TextView tv_supportuser;
    private NoScrollListView lv;
    private ImageView iv_comment;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private TextView tv_viewcount;
    private TextView tvStatus;
    private TextView tv_startTime; //开始时间
    private TextView tv_endTime;  //结束时间
    private TextView tv_periodType; //周期类型
    private TextView tv_periodDate; //周期日
    private LinearLayout ll_period; //周期任务容器
    private TextView tv_project;
    private TextView tv_taskType;
    private ImageView ivStatus;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info_new);
        context = TaskInfoActivity.this;
        helper = new DictionaryHelper(context);
        initViews();
        initIntentData();
        setOnEvent();
    }


    @Override
    protected void onStart() {
        if (isFinish) {
            finish();
            isFinish = false;
        }
        super.onStart();
    }

    private void getSupportList() {
        SupportListPost post = new SupportListPost();
        post.setDataType("任务计划");
        post.setDataId(mTask.getUuid());
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞列表;
        supportUser = "";
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    tv_support.setText(list.size() + "");
                    ll_support.setVisibility(View.GONE);
                    for (int i = 0; i < list.size(); i++) {
                        supportUser += helper.getUserNameById(list.get(i).getFromId()) + "、";
                    }
                    String substring = supportUser.substring(0, supportUser.length() - 1);
                    tv_supportuser.setText(substring);
                } else {
                    ll_support.setVisibility(View.GONE);
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
                    tv_comment.setText(list.size() + "");
                    tv_nocomment.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setAdapter(getAdapter(list));
                } else {
                    tv_nocomment.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
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


    private void initViews() {
        tv_viewcount = findViewById(R.id.tv_viewcount);
        iv_comment = findViewById(R.id.iv_comment);
        lv = findViewById(R.id.spaceinfo_listview);
        iv_support = findViewById(R.id.iv_support);
        tv_support = findViewById(R.id.tv_support);
        tv_comment = findViewById(R.id.tv_comment);
        tv_supportuser = findViewById(R.id.tv_support_user);
        tv_nocomment = findViewById(R.id.tv_nocomment);
        ll_support = findViewById(R.id.ll_support_list);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_task_info);
        tv_excutor = (TextView) findViewById(R.id.task_info_excutor);
        tv_creator = (TextView) findViewById(R.id.tv_name_task_info);
        tv_content = (TextView) findViewById(R.id.content_task_info);
        tvStatus = (TextView) findViewById(R.id.task_info_status);
        tv_participant = (TextView) findViewById(R.id.task_info_participant);
        tv_begin = (TextView) findViewById(R.id.task_info_beginTime);
        tv_over = (TextView) findViewById(R.id.task_info_overTime);
        tv_time = (TextView) findViewById(R.id.tv_time_task_info);
        lv_attach = (NoScrollListView) findViewById(R.id.lv_attach_list);
//        tv_status = (TextView) findViewById(R.id.status_task_info);
        dialog = new DictIosPickerBottomDialog(TaskInfoActivity.this);
        iv_head = (CircleImageView) findViewById(R.id.iv_head_task_info);
        commentView = findViewById(R.id.comment_log_info);
        tv_startTime = findViewById(R.id.tv_task_info_starttime);
        tv_endTime = findViewById(R.id.tv_task_info_endtime);
        tv_periodType = findViewById(R.id.task_info_peroid_type);
        tv_periodDate = findViewById(R.id.task_info_period_date);
        ll_period = findViewById(R.id.ll_task_info_period);
        tv_project = findViewById(R.id.task_info_project);
        tv_taskType = findViewById(R.id.task_info_type);
        ivStatus = findViewById(R.id.iv_status);
    }

    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("taskIntentInfo");
            if (bundle != null) {
                mTask = (Task) bundle.getSerializable("taskInfo");
            }
            dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            if (mTask != null) {
                initData();
                getCommentList();
                getSupportList();
            }
            if (dynamic != null) {
                getTaskInfo();
            }
        }
    }

    private void initData() {
        //判断是否为周期任务
        if (mTask.isPeriodTask()) {
            String cycleStartTime = mTask.getCycleStartTime();
            String cycleEndTime = mTask.getCycleEndTime();
            if (mTask.getCycleStartTime().contains(" 00:00:00")) {
                cycleStartTime = ViewHelper.getStringFormat(mTask.getCycleStartTime(), "yyyy-MM-dd");
            }
            if (mTask.getCycleEndTime().contains(" 00:00:00")) {
                cycleEndTime = ViewHelper.getStringFormat(mTask.getCycleEndTime(), "yyyy-MM-dd");
            }
            tv_taskType.setText("周期任务");
            ll_period.setVisibility(View.VISIBLE);
            tv_startTime.setText("周期开始时");
            tv_endTime.setText("周期结束时");
            headerView.setRightTitleVisible(false);
            tv_begin.setText(cycleStartTime);
            tv_over.setText(cycleEndTime);
            tv_periodType.setText(mTask.getCycleTypeName());
            ImageUtils.displyImageById(helper.getUserPhoto(mTask.getExecutorIds()), iv_head);
            tv_creator.setText(helper.getUserNameById(mTask.getExecutorIds()));
            tv_periodDate.setText(ViewHelper.convertStrToFormatDateStr(mTask.getCycleDayName(), "yyyy年MM月dd日"));
        } else {
            tv_creator.setText(helper.getUserNameById(mTask.getCreatorId()));
            tv_begin.setText(ViewHelper.convertStrToFormatDateStr(mTask.getBeginTime(), "yyyy年MM月dd日"));
            tv_over.setText(ViewHelper.convertStrToFormatDateStr(mTask.getEndTime(), "yyyy年MM月dd日"));
            ImageUtils.displyImageById(helper.getUserPhoto(mTask.getCreatorId()), iv_head);
            if(!mTask.getStatus().equals(TaskStatusEnum.已完成) || !mTask.getStatus().equals(TaskStatusEnum.已取消)){
                if(DateTimeUtil.getBetweenDays(ViewHelper.getDateToday(),
                        ViewHelper.formatStrToStr(mTask.getEndTime(),"yyyy-MM-dd")) >= 0){
                    //结束时间大于今天时间 说明没有预期
                    tv_over.setTextColor(Color.parseColor("#000000"));
                } else {
                    //结束时间小于今天时间 说明任务逾期
                    tv_over.setTextColor(Color.parseColor("#dc1414"));
                }
            }
        }
        getProjectName(mTask.getProjectId(), tv_project);//设置项目名称
        tv_excutor.setText(helper.getUserNamesById(mTask.getExecutorIds()));
        tv_content.setText(mTask.getContent());
        tvStatus.setText(getStatusById(mTask.getStatus()));
        tv_participant.setText(helper.getUserNamesById(mTask.getParticipantIds()));
        tv_time.setText(ViewHelper.getDateStringFormat(mTask.getCreationTime()));

        if ((TaskStatusEnum.已取消.getName().equals(mTask.getStatus()) || TaskStatusEnum.已完成.getName().equals(mTask.getStatus()))) {
            headerView.tvRightTitle.setVisibility(View.GONE);
        }
        if (!StrUtils.pareseNull(mTask.getCreatorId()).equals(Global.mUser.getUuid())){
//                && !StrUtils.pareseNull(mTask.getExecutorIds()).equals(Global.mUser.getUuid())) {
            headerView.tvRightTitle.setVisibility(View.GONE);
        }
        if (TaskStatusEnum.进行中.getName().equals(mTask.getStatus()) || TaskStatusEnum.已逾期.getName().equals(mTask.getStatus())) {
            ivStatus.setVisibility(View.VISIBLE);
        } else {
            ivStatus.setVisibility(View.GONE);
        }

        AttachPlayHelper helper = new AttachPlayHelper(TaskInfoActivity.this, mTask.getAttachmentIds(), lv_attach);

//        commentView.setIsLike(mTask.isLike());
//        SupportListPost post = new SupportListPost();
//        post.setDataId(mTask.getUuid());
//        post.setDataType("任务计划");
//        fragment = CommonFragment.newInstance(post);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.frame_comment_log, fragment);
//        transaction.commit();


        commentView.setIsLike(mTask.isLike());

//        tv_comment.setText(mTask.getCommentNumber() + "");
//        tv_support.setText(mTask.getLikeNumber() + "");

        if (mTask.isLike()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
        }
        tv_viewcount.setText("浏览" + mTask.getFavoriteNumber() + "次");

    }


    private void getTaskInfo() {
        ProgressDialogHelper.show(context);
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

    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list) {
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

    private void setOnEvent() {
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw(mTask);
            }
        });
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {//编辑任务，跳转到新建任务页面
                Intent intent = new Intent(TaskInfoActivity.this, TaskNewActivity.class);
                intent.putExtra("taskInfo", mTask);
                intent.putExtra("isNewTask", false);
                startActivity(intent);
                TaskNewActivity.isFinish = true;
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

//
//        commentView.setOnSupportListener(new BottomCommentView.SupportListener() {
//            @Override
//            public void onSupport() {
//                SupportAndCommentPost post = new SupportAndCommentPost();
//                post.setDataId(mTask.getUuid());
//                post.setDataType("任务计划");
//                post.setFromId(Global.mUser.getUuid());
//                post.setToId(mTask.getUuid());
//                if (mTask.isLike()) { //取消点赞
//                    commentView.cancleSupport(post);
//                    mTask.setLike(false);
//                } else {
//                    mTask.setLike(true);
//                    commentView.support(post);
//                }
//            }
//        });

        commentView.setOnSupportSuccessListener(new BottomCommentView.SupportSuccessListener() {
            @Override
            public void onSupportSuccess() { //点赞成功，重新加载点赞列表
//                fragment.reloadSupport();
                getSupportList();
                TaskDayViewFragment.isResume = true;
                ClientTaskListFragment.isResume = true;
                TaskListFragment.isReasume = true;
            }
        });

        commentView.setOnCommentListener(new BottomCommentView.CommentListener() {
            @Override
            public void onComment(String count) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mTask.getUuid());
                post.setDataType("任务计划");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mTask.getExecutorIds());
                post.setContent(count);
                commentView.comment(post);
            }
        });


        commentView.setOnCommentSuccessListener(new BottomCommentView.CommentSuccessListener() {
            @Override
            public void onCommentSuccess() { //评论成功，重新加载点赞列表
//                fragment.reloadComment();
                tv_comment.setText((mTask.getCommentNumber() + 1) + "");
                getCommentList();
                TaskDayViewFragment.isResume = true;
                ClientTaskListFragment.isResume = true;
                TaskListFragment.isReasume = true;
            }
        });
        iv_support.setOnClickListener(new View.OnClickListener() {

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
                    tv_support.setText(likeNumber + "");
                    commentView.cancleSupport(post);
                    mTask.setLike(false);
                } else {
                    likeNumber = (mTask.getLikeNumber() + 1);
                    tv_support.setText(likeNumber + "");
                    mTask.setLike(true);
                    commentView.support(post);
                }
                mTask.setLikeNumber(likeNumber);
                if (mTask.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new DictIosPickerBottomDialog(TaskInfoActivity.this);
                if (TaskStatusEnum.进行中.getName().equals(mTask.getStatus())) {
                    dialog.show(status);
                } else if (TaskStatusEnum.已逾期.getName().equals(mTask.getStatus())) {
                    dialog.show(status);
                } else {
                    Toast.makeText(context, "当前任务状态下不能修改任务状态!", Toast.LENGTH_SHORT).show();
                }
                dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        saveTask(mTask, index + 1);
                    }
                });
            }
        });


    }

    /**
     * 根据任务状态id获取任务状态名称
     *
     * @param uuid
     * @return
     */
    public String getStatusById(String uuid) {
        String status = "";
        if (TaskStatusEnum.已完成.getName().equals(uuid)) {
            status = TaskStatusEnum.已完成.getStatus();
        } else if (TaskStatusEnum.进行中.getName().equals(uuid)) {
            status = TaskStatusEnum.进行中.getStatus();
        } else if (TaskStatusEnum.已取消.getName().equals(uuid)) {
            status = TaskStatusEnum.已取消.getStatus();
        } else if (TaskStatusEnum.已逾期.getName().equals(uuid)) {
            status = TaskStatusEnum.已逾期.getStatus();
        }
        return status;
    }


    /**
     * 完成任务
     */
    private void saveTask(Task task, int status) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.改变任务状态 + "?uuid=" + task.getUuid() + "&ticket=" + status;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "修改任务状态成功!", Toast.LENGTH_SHORT).show();
                TaskListActivity.isResume = true;
                TaskDayViewFragment.isResume = true;
                TaskListFragment.isReasume = true;
                TaskWeekFragment.isResume = true;
                ClientTaskListFragment.isResume = true;
                TaskLaneFragment.isReasume = true;
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Task task) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
//        et_comment.setText("");
//        InputSoftHelper.hiddenSoftInput(getActivity(), et_comment);
//        ll_bottom.setVisibility(View.GONE);
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TaskInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                task.setCommentNumber(task.getCommentNumber() + 1);
                tv_comment.setText(task.getCommentNumber() + "");
                TaskDayViewFragment.isResume = true;
                ClientTaskListFragment.isResume = true;
                TaskListFragment.isReasume = true;
                getCommentList();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Log.e("tag", "评论失败");
            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void popWiw(final Task item) {

        popWiw = new BaseSelectPopupWindow(this, R.layout.edit_data);
        // popWiw.setOpenKeyboard(true);
        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWiw.setFocusable(true);
        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWiw.setShowTitle(false);
        popWiw.setBackgroundDrawable(new ColorDrawable(0));
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final TextView send = popWiw.getContentView().findViewById(
                R.id.btn_send);
        final TextEditTextView edt = popWiw.getContentView().findViewById(
                R.id.edt_content);

        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (TextUtils.isEmpty(edt.getText())) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                        String content = edt.getText().toString().trim();
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getExecutorIds());
                        post.setDataType("任务计划");
                        post.setDataId(item.getUuid());
                        post.setContent(content);
                        comment(post, item);
                        popWiw.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                    // /提交内容
                    String content = edt.getText().toString().trim();

                    SupportAndCommentPost post = new SupportAndCommentPost();
                    post.setFromId(Global.mUser.getUuid());
                    post.setToId(item.getExecutorIds());
                    post.setDataType("任务计划");
                    post.setDataId(item.getUuid());
                    post.setContent(content);
                    comment(post, item);
                    popWiw.dismiss();
                }
            }
        });
        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 根据项目id 获取项目名称
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
}
