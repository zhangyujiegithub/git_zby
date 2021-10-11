package com.biaozhunyuan.tianyi.task;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.j256.ormlite.dao.Dao;
import com.loonggg.weekcalendar.entity.CalendarData;
import com.loonggg.weekcalendar.view.WeekCalendar;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

import static android.app.Activity.RESULT_OK;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

/**
 * Created by 王安民 on 2017/10/8.
 * 任务周视图
 */

public class TaskWeekViewFragment extends Fragment {


    private final int[] mSateBgColors = new int[]{R.color.color_status_qidong, R.color.color_status_zanting, R.color.color_status_wancheng, R.color.color_status_gezhi, R.color.color_status_chongqi, R.color.color_status_tijiao};
    private HorizontalScrollView horizontalScrollView_task_week_view;

    private ViewHolder mViewHolder;

    private Demand<Task> demand;
    private Context mContext;
    private TaskListActivityNew activity;

    private HashMap<Integer, TasksCompletion> mSelectCompletionMap = new HashMap<Integer, TasksCompletion>();
    private Task mSelectTask;
    private LinearLayout mSelectllRoot;
    private List<String> dateList;
    private List<TasksCompletion> users;
    private int lastPosition = -1;
    private Latest currentUser;

    private boolean isSelect = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime_start = "";  //按照时间过滤
    private String currentTime_end = "";  //按照时间过滤
    public static boolean isResume = false;
    private static final String TASK_WEEK_VIEW_CHECK = "TASK_WEEK_VIEW_CHECK";
    private View addView;
    private Display display;
    private DictionaryHelper helper;
    private ORMDataHelper dataHelper;
    private List<Latest> recentList; //最近选择的员工
    private boolean currentTaskChageStatus = true; //当前选中的任务 是否可改变任务状态

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_week_view, null);
        initView(view);
        initData();
        createAddView();
        initDemand();
        getTaskList(currentUser);
        setOnEvent();
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS: //选择执行人
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        User user = userList.getUsers().get(0);
                        receiveSelectedUser(user);
                    }
                    break;
            }
        }
    }

    private void initData() {
        Date date = new Date();
        dateList = ViewHelper.getWeeks(date);
        mContext = getContext();
        activity = (TaskListActivityNew) mContext;
        helper = new DictionaryHelper(mContext);
        currentUser = ViewHelper.turnLatest(Global.mUser);
        recentList = new ArrayList<>();
        dataHelper = ORMDataHelper.getInstance(mContext);
        currentTime_start = dateList.get(0);
        currentTime_end = dateList.get(dateList.size() - 1);
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    private void initView(View view) {
        mViewHolder = new ViewHolder(view);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            getTaskList(currentUser);
            isResume = false;
        }
    }

    private class ViewHolder {
        public View rootView;
        public HorizontalScrollView horizontalScrollView_task_week_view;
        public WeekCalendar weekcalendar_task_week_view;

        public LinearLayout ll_user_root_task_week_view;

        private LinearLayout ll_root_day1_week_view;
        private LinearLayout ll_root_day2_week_view;
        private LinearLayout ll_root_day3_week_view;
        private LinearLayout ll_root_day4_week_view;
        private LinearLayout ll_root_day5_week_view;
        private LinearLayout ll_root_day6_week_view;
        private LinearLayout ll_root_day7_week_view;

        private RelativeLayout rl_info_task_week_view;
        private CircleImageView avatarView;
        private TextView tvContent;
        private TextView tvName;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.horizontalScrollView_task_week_view = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView_task_week_view);
            TaskWeekViewFragment.this.horizontalScrollView_task_week_view = this.horizontalScrollView_task_week_view;
            this.weekcalendar_task_week_view = (WeekCalendar) rootView.findViewById(R.id.weekcalendar_task_week_view);
//            this.lv_task_week_view = (ListView) rootView.findViewById(R.id.lv_task_week_view);
            this.ll_user_root_task_week_view = (LinearLayout) rootView.findViewById(R.id.ll_user_root_task_week_view);
            this.ll_root_day1_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day1_week_view);
            this.ll_root_day2_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day2_week_view);
            this.ll_root_day3_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day3_week_view);
            this.ll_root_day4_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day4_week_view);
            this.ll_root_day5_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day5_week_view);
            this.ll_root_day6_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day6_week_view);
            this.ll_root_day7_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day7_week_view);
            this.rl_info_task_week_view = (RelativeLayout) rootView.findViewById(R.id.rl_info_task_week_view);

            this.avatarView = (CircleImageView) rootView.findViewById(R.id.circularAvatar_info_task_week_view);
            this.tvContent = (TextView) rootView.findViewById(R.id.tv_info_content_task_week_view);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name_task_week_view);
        }
    }

    private void setOnEvent() {
        mViewHolder.weekcalendar_task_week_view.setOnPageChangedListener(new WeekCalendar.OnPageChangedListener() {
            @Override
            public void onPageChange(List<CalendarData> dataList) {
//                mSelectCompletionMap.clear();
                getTaskList(currentUser);
            }
        });

        mViewHolder.rl_info_task_week_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectTask != null) {
                    Intent intent = new Intent(mContext, TaskInfoActivityNew.class);
                    Task task = mSelectTask;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtra("taskIntentInfo", bundle);
                    startActivity(intent);
                    mViewHolder.rl_info_task_week_view.setVisibility(View.GONE);
                }
            }
        });
    }

    private void clearAllTaskView() {
        mViewHolder.ll_root_day1_week_view.removeAllViews();
        mViewHolder.ll_root_day2_week_view.removeAllViews();
        mViewHolder.ll_root_day3_week_view.removeAllViews();
        mViewHolder.ll_root_day4_week_view.removeAllViews();
        mViewHolder.ll_root_day5_week_view.removeAllViews();
        mViewHolder.ll_root_day6_week_view.removeAllViews();
        mViewHolder.ll_root_day7_week_view.removeAllViews();
    }


    private void initDemand() {
        demand = new Demand<Task>(Task.class);
        demand.pageSize = 1000;
        demand.pageIndex = 1;
        demand.sort = "desc";
        demand.sortField = "creationTime";
        demand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
    }

    public void getTaskList() {
        getTaskList(null);
    }

    private void getTaskList(Latest latest) {
//        ProgressDialogHelper.show(mContext);
        if (mViewHolder != null) {
            clearAllTaskView();
        }
        final List<CalendarData> calendarDatas = mViewHolder.weekcalendar_task_week_view.getCurrentWeekDatas();
        CalendarData data_end = calendarDatas.get(calendarDatas.size() - 1);
        CalendarData data_start = calendarDatas.get(0);
        currentTime_end = data_end.getDateStr();
        currentTime_start = data_start.getDateStr();
        initStaffHeader(latest);
        for (int i = 0; i < calendarDatas.size(); i++) {
            final CalendarData data = calendarDatas.get(i);
            String url = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?beginTime=" + data.getDateStr() + "&executorIds=" + currentUser.getUuid();
            demand.src = url;
            final int finalI = i;
            StringRequest.postAsyn(demand.src, demand, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
//                    ProgressDialogHelper.dismiss();
                    try {
                        List<Task> list = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), Task.class);
                        LinearLayout llRoot = getllRootView(finalI);
                        String date = data.getDateStr();
                        llRoot.setTag(data);
                        for (Task task : list) {
                            Logger.i("CalendarData_date:::" + "ssignTime=" + task.getCreationTime());
                            if (!TextUtils.isEmpty(task.getBeginTime()) && task.getBeginTime().contains(date)) {
                                generateTaskItem(llRoot, task);
                            }
                        }
                        generateAddButton(llRoot, date);
                    } catch (JSONException e) {
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

    }

    private LinearLayout getllRootView(int i) {
        LinearLayout llRoot;
        switch (i) {
            case 0:
                llRoot = mViewHolder.ll_root_day1_week_view;
                llRoot.setTag(0);
                break;
            case 1:
                llRoot = mViewHolder.ll_root_day2_week_view;
                llRoot.setTag(1);
                break;
            case 2:
                llRoot = mViewHolder.ll_root_day3_week_view;
                llRoot.setTag(2);
                break;
            case 3:
                llRoot = mViewHolder.ll_root_day4_week_view;
                llRoot.setTag(3);
                break;
            case 4:
                llRoot = mViewHolder.ll_root_day5_week_view;
                llRoot.setTag(4);
                break;
            case 5:
                llRoot = mViewHolder.ll_root_day6_week_view;
                llRoot.setTag(5);
                break;
            case 6:
                llRoot = mViewHolder.ll_root_day7_week_view;
                llRoot.setTag(6);
                break;
            default:
                llRoot = mViewHolder.ll_root_day1_week_view;
                llRoot.setTag(0);
                break;
        }
        return llRoot;
    }


    /**
     * 添加 新建按钮
     *
     * @param llRoot
     * @param date
     */
    private void generateAddButton(LinearLayout llRoot, final String date) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_task_week_view, null);
        final TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content_task_week_view);
        final ImageView ivAdd = (ImageView) itemView.findViewById(R.id.iv_add_task_week_view);

        tvContent.setVisibility(View.GONE);
        ivAdd.setVisibility(View.VISIBLE);
        llRoot.addView(itemView);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setBeginTime(date + " 00:00:00");
                task.setEndTime(date + " 23:59:59");
                task.setExecutorIds(currentUser.getUuid());
                task.setCreatorId(Global.mUser.getUuid());
                task.setCreationTime(ViewHelper.getCurrentFullTime());
                task.setStatus(TaskStatusEnum.进行中.getName());
                Intent intent = new Intent(getActivity(), TaskInfoActivityNew.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskInfo", task);
                intent.putExtra("taskIntentInfo", bundle);
                startActivity(intent);
            }
        });
    }

    public void generateTaskItem(LinearLayout llRoot, final Task item) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_task_week_view, null);
        final TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content_task_week_view);
        String content = item.getContent();
        if (!TextUtils.isEmpty(content)) {
            content = content.replaceAll(" ", "") + "    ";

            content = content.length() >= 4 ? content.substring(0, 4) : content;
            if (content.length() > 2) {
                content = content.substring(0, 2) + "\n" + content.substring(2, content.length());
            }
            tvContent.setText(content);
            if (isAdded()) {
                if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                    tvContent.setBackgroundResource(R.drawable.bg_task_yiyuqi);
                    tvContent.setTextColor(getResources().getColor(R.color.color_text_weiwancheng));
                } else if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
                    tvContent.setBackgroundResource(R.drawable.bg_task_jinxingzhong);
                    tvContent.setTextColor(getResources().getColor(R.color.color_text_success));
                } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                    tvContent.setBackgroundResource(R.drawable.bg_task_yiwancheng);
                    tvContent.setTextColor(getResources().getColor(R.color.color_text_qidong));
                } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
                    tvContent.setBackgroundResource(R.drawable.bg_task_yiquxiao);
                    tvContent.setTextColor(getResources().getColor(R.color.color_status_cancel));
                }
            }
            ViewTreeObserver vto = tvContent.getViewTreeObserver(); //自适应屏幕宽高时防止拉伸 textview变形

            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    tvContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int height = tvContent.getMeasuredHeight();
                    int width = tvContent.getMeasuredWidth();
                    if (height > width) {
                        tvContent.setWidth(height);
                    } else if (height < width) {
                        tvContent.setHeight(width);
                    } else {

                    }
                }
            });

            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectTask = item;
                    mSelectllRoot = llRoot;
//                    if (mViewHolder.rl_info_task_week_view.getVisibility() == View.GONE) {
//                        mViewHolder.rl_info_task_week_view.setVisibility(View.VISIBLE);
//                    }

                    //顯示底部任務詳情
//                    mViewHolder.tvContent.setText(StrUtils.pareseNull(item.getContent()));
//                    mViewHolder.tvName.setText(StrUtils.pareseNull(new DictionaryHelper(mContext).getUserNameById(item.getCreatorId())));
//                    ImageUtils.displyUserPhotoById(mContext, item.getExecutorIds(), mViewHolder.avatarView);
//                    mViewHolder.avatarView.displayNameByUserId(item.getExecutor());
                    showDiaglog(mSelectTask, mSelectllRoot);

                }
            });
            llRoot.addView(itemView);
        }
    }

    private void showDiaglog(Task mTask, LinearLayout tag) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_task_week_view, null);
        Dialog alertDialog = new Dialog(mContext, R.style.AlertDialogStyle);
        alertDialog.setContentView(view);
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

        TextView msg = view.findViewById(R.id.txt_msg);
        TextView status = view.findViewById(R.id.task_info_status);
        TextView excutor = view.findViewById(R.id.task_info_excutor);
        TextView begin = view.findViewById(R.id.task_info_beginTime);
        TextView over = view.findViewById(R.id.task_info_overTime);
        Button finish = view.findViewById(R.id.btn_neg);
        Button info = view.findViewById(R.id.btn_pos);
        msg.setText(mTask.getContent());
        String statusName = getStatusById(mTask.getStatus());
        status.setText(statusName);
        if (statusName.equals("已完成")) {
            finish.setText("该任务已完成");
            currentTaskChageStatus = false;
        } else if (statusName.equals("已取消")) {
            finish.setText("该任务已取消");
            currentTaskChageStatus = false;
        } else {
            currentTaskChageStatus = true;
        }
        excutor.setText(helper.getUserNamesById(mTask.getExecutorIds()));
        begin.setText(ViewHelper.convertStrToFormatDateStr(mTask.getBeginTime(), "yyyy年MM月dd日"));
        over.setText(ViewHelper.convertStrToFormatDateStr(mTask.getEndTime(), "yyyy年MM月dd日"));

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTaskChageStatus) {
                    saveTask(mTask, tag);
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(mContext, "此状态不可更改", Toast.LENGTH_SHORT).show();
                }
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
                Intent intent = new Intent(mContext, TaskInfoActivityNew.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskInfo", mTask);
                intent.putExtra("taskIntentInfo", bundle);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
    private void saveTask(Task task, LinearLayout layout) {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.改变任务状态 + "?uuid=" + task.getUuid() + "&ticket=1";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                updateWeekView(layout);
                activity.refreshList(false);
                Toast.makeText(getActivity(), "修改任务状态成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                Toast.makeText(getActivity(), JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT);
            }
        });
    }


    /**
     * 任务状态变更后刷新当前列表
     *
     * @param llRoot
     */
    private void updateWeekView(LinearLayout llRoot) {
        llRoot.removeAllViews();
        CalendarData data = (CalendarData) llRoot.getTag();
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?beginTime=" + data.getDateStr() + "&executorIds=" + currentUser.getUuid();
        demand.src = url;
        StringRequest.postAsyn(demand.src, demand, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
//                    ProgressDialogHelper.dismiss();
                try {
                    List<Task> list = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), Task.class);
                    String date = data.getDateStr();
                    for (Task task : list) {
                        Logger.i("CalendarData_date:::" + "ssignTime=" + task.getCreationTime());
                        if (!TextUtils.isEmpty(task.getBeginTime()) && task.getBeginTime().contains(date)) {
                            generateTaskItem(llRoot, task);
                        }
                    }
                    generateAddButton(llRoot, date);
                    ProgressDialogHelper.dismiss();
                } catch (JSONException e) {
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
     * 初始化头布局过滤员工
     */
    private void initStaffHeader(Latest latest) {
        Latest mine = ViewHelper.turnLatest(Global.mUser);
        recentList.clear();
        try {
            Dao<Latest, Integer> latestDao = dataHelper.getLatestDao();
            List<Latest> latests = latestDao.queryForAll();
            for (int i = 0; i < latests.size(); i++) { //先插入最近联系人
                Latest lat = latests.get(i);
                if (!removeDuplicate(lat)) {
                    if (latest != null && latest.getUuid().equals(lat.getUuid())) {
                        //如果有传进来一个latest对象 放到下面添加 添加到第一位
                    }
//                    else if (lat.getUuid().equals(currentUser.getUuid())){
//                        //当前选中的员工 放到下面添加 添加到第一位
//                    }
                    else {
                        if (lat.getUuid().equals(mine.getUuid())) {
                            //自己放到下面添加 添加到第一位
                        } else {
                            recentList.add(lat);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (latest != null) { //添加到第一个位置
            recentList.add(0, latest);
        }
//        if (!removeDuplicate(currentLatest)) {
//            recentList.add(0, currentLatest);
//        }
        if (!removeDuplicate(mine)) {
            recentList.add(0, mine);
        }
        generationTaskCompletionView(recentList, latest);
    }

    //把list里的对象遍历一遍，用list.contain()，如果不存在就放入到另外一个list集合中
    public boolean removeDuplicate(Latest user) {
        boolean isRePeat = false;
        for (Latest user1 : recentList) {
            if (user.getUuid().equals(user1.getUuid())) {
                isRePeat = true;
                break;
            }
        }
        return isRePeat;
    }


    private void createAddView() {
        addView = LayoutInflater.from(mContext).inflate(R.layout.item_input_add, null);
        ImageView ivAdd = addView.findViewById(R.id.iv_item_input_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", true);
                intent.putExtra("title", "选择执行人");
                startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
            }
        });
    }

    public void generationTaskCompletionView(final List<Latest> mList, Latest latest) {
        ProgressDialogHelper.dismiss();
        mViewHolder.ll_user_root_task_week_view.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            final Latest item = mList.get(i);
            final int pos = i;
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_user_and_progress, null);
            final View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_task_user_and_progress_transpant, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name_avatar);
            CircleImageView view2 = (CircleImageView) view.findViewById(R.id.circularAvatar_task_user);
            String valueBYkey = PreferceManager.getInsance().getValueBYkey(TASK_WEEK_VIEW_CHECK);

            if (latest != null) { //如果传过来一个latest对象 则选中
                if (item.getUuid().equals(latest.getUuid())) {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg));
                }
            } else {
                if (TextUtils.isEmpty(valueBYkey)) {
                    if (item.getUuid().equals(Global.mUser.getUuid())) {
                        view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                    }
                } else {
                    if (valueBYkey.equals(item.getUuid())) {
                        view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                    } else {
                        view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg));
                    }
                }
            }

            ImageUtils.displyUserPhotoById(mContext, item.getUuid() + "", view2);
            if (TextUtils.isEmpty(item.getName())) {
                tvName.setText(helper.getUserNameById(item.getName()));
            } else {
                tvName.setText(item.getName());
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClick()) {
                        ProgressDialogHelper.show(getActivity());
                        PreferceManager.getInsance().saveValueBYkey(TASK_WEEK_VIEW_CHECK, item.getUuid());
                        if (lastPosition != -1 && lastPosition != pos) {
                            mViewHolder.ll_user_root_task_week_view.getChildAt(lastPosition).setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                        }
                        lastPosition = pos;
                        currentUser = item;
                        getTaskList();
                    } else {
                        Toast.makeText(mContext, "操作过快", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mViewHolder.ll_user_root_task_week_view.addView(view);
            mViewHolder.ll_user_root_task_week_view.addView(view1);
            if (i == mList.size() - 1) {
                mViewHolder.ll_user_root_task_week_view.addView(addView);
            }
        }
    }

    private long lastClickTime = 0;//上次点击时间

    /**
     * 设置当前是否可点击切换员工
     */
    private boolean isClick() {
        long currentClickTime = SystemClock.uptimeMillis();
        if (currentClickTime - lastClickTime <= 500) {
            return false;
        } else {
            lastClickTime = currentClickTime;
            return true;
        }
    }


    /**
     * 收取选择的员工
     */
    public void receiveSelectedUser(User user) {
        horizontalScrollView_task_week_view.scrollTo(0, 0);
        currentUser = ViewHelper.turnLatest(user);
        getTaskList(ViewHelper.turnLatest(user));
    }

    @Override
    public void onDestroy() {
        PreferceManager.getInsance().saveValueBYkey(TASK_WEEK_VIEW_CHECK, "");
        super.onDestroy();
    }
}
