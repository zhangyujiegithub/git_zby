package com.biaozhunyuan.tianyi.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.attendance.Attendance;
import com.biaozhunyuan.tianyi.attendance.AttendanceListAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.log.LogInfoActivity;
import com.biaozhunyuan.tianyi.log.LogItemAdapter;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.task.TaskInfoActivity;
import com.biaozhunyuan.tianyi.task.TaskItemAdapter;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.HorizontalListView;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.helper.ViewHelper.formatDateToStr;
import static com.biaozhunyuan.tianyi.common.utils.JsonUtils.ConvertJsonToList;

/**
 * Created by 王安民 on 2017/8/19.
 * 通用的过滤页面，过滤日志任务（可以按照日期过滤，按照员工过滤）
 */

public class CommonFilterActivity extends Activity {

    public static final int TYPE_FILTER_LOG = 1;    //日志列表
    public static final int TYPE_FILTER_TASK = 2;   //任务列表
    public static final int TYPE_FILTER_ATTENDANCE = 3;   //考勤列表

    private int type = -1;


    private BoeryunHeaderView headerView;
    private HorizontalListView horizontalListView;
    private HorizontalScrollView scrollView;
    private LinearLayout ll_staff;
    private TextView tv_today; //返回到今天按钮

    private CoordinatorLayout content;
    private MonthPager monthPager;
    private RecyclerView rvToDoList;
    private List<User> users;

    /**
     * 日志
     */
    private List<WorkRecord> recordList;
    private LogItemAdapter recordAdapter;
    private Demand<WorkRecord> logDemand;


    /**
     * 任务
     */
    private List<Task> taskList;
    private TaskItemAdapter taskItemAdapter;
    private Demand<Task> taskDemand;


    /**
     * 考勤
     */
    private List<Attendance> attendanceList;
    private Demand<Attendance> attendanceDemand;
    private AttendanceListAdapter attendanceListAdapter;


    private ScaleAnimation scaleBigAnimation;
    private ScaleAnimation scaleSmallAnimation;

    private int lastPosition = -1;
    private boolean isSelect = false;

    private ArrayList<Calendar> currentCalendars = new ArrayList<Calendar>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_filter);
        context = this;
        findViews();
        initAnimation();
        initStaffHeader();
        initCalendar();
        initIntentData();
        setOnEvent();
    }


    /**
     * 初始化日历
     */
    private void initCalendar() {
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewheight(Utils.dpi2px(context, 270));
        rvToDoList = (RecyclerView) findViewById(R.id.list);
        rvToDoList.setHasFixedSize(true);
        //这里用线性显示 类似于listview
        rvToDoList.setLayoutManager(new LinearLayoutManager(this));
        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();
    }

    /**
     * 初始化传递的数据 以此来判断显示日志列表还是任务列表
     */
    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            int dividerHeight = 10;
            type = getIntent().getIntExtra("type_filter", -1);

            Date date = new Date();
            String currentDate = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
            currentTime = currentDate;
            if (type == TYPE_FILTER_LOG) {      //日志列表
                initLogDemand();
                getWorkList();
            } else if (type == TYPE_FILTER_TASK) {   //任务列表
                initTaskDemand();
                getTaskList();
            } else if (type == TYPE_FILTER_ATTENDANCE) {//考勤列表
                initAttendanceDemand();
                getAttendanceList();
                dividerHeight = 1;
            }

            rvToDoList.addItemDecoration(new RecycleViewDivider(
                    context, LinearLayoutManager.HORIZONTAL, (int) ViewHelper.dip2px(context, dividerHeight), ContextCompat.getColor(context, R.color.dividerColor)));
        }
    }


    private void findViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_common_filter_list);
        content = (CoordinatorLayout) findViewById(R.id.content);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        tv_today = (TextView) findViewById(R.id.back_to_today);
        horizontalListView = (HorizontalListView) findViewById(R.id.lv_head_view);
        scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView_filter_activity);
        ll_staff = (LinearLayout) findViewById(R.id.ll_user_root_filter_activity);

        headerView.setTitle(formatDateToStr(new Date(), "yyyy年MM月dd日 ") + DateTimeUtil.getWeek(new Date()));
//        horizontalListView.setAdapter(getAdapter());
    }


    private void initAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        scaleBigAnimation = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //0.5秒完成动画
        scaleBigAnimation.setDuration(200);
        scaleBigAnimation.setFillAfter(true);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(scaleBigAnimation);

        scaleSmallAnimation = new ScaleAnimation(1.2f, 1, 1.2f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //0.5秒完成动画
        scaleSmallAnimation.setDuration(200);
        scaleSmallAnimation.setFillAfter(true);
    }


    /**
     * 初始化头布局过滤员工
     */
    private void initStaffHeader() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.下属员工 + "?staffId=" + Global.mUser.getUuid();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                users = ConvertJsonToList(response, User.class);

                if (users != null) {
                    for (int i = 0; i < users.size(); i++) {
                        View view1 = View.inflate(context, R.layout.item_head_view, null);
                        TextView tvName = (TextView) view1.findViewById(R.id.tv_name_header_view);
                        CircleImageView view2 = (CircleImageView) view1.findViewById(R.id.head_header_view);
                        tvName.setText(users.get(i).getName());
                        ImageUtils.displyImageById(new DictionaryHelper(context).getUserPhoto(users.get(i).getUuid()), view2);
                        ll_staff.addView(view1);
                    }

                    for (int i = 0; i < users.size(); i++) {
                        final int finalI = i;
                        ll_staff.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lastPosition != -1) {
                                    ll_staff.getChildAt(lastPosition).startAnimation(scaleSmallAnimation);
                                }

                                if (!isSelect) {  //未选中状态，点击选中，设置背景为灰色，条件为选中的员工
                                    isSelect = true;
                                    ll_staff.getChildAt(finalI).startAnimation(scaleBigAnimation);
                                } else {    //选中状态下，点击分两种情况，点击别的位置，取消背景，点击的位置设为灰色，条件为选中的员工；点击原来的位置，取消背景，条件为空。
                                    if (lastPosition != -1) {
                                        if (lastPosition == finalI) {
                                            isSelect = false;
                                            ll_staff.getChildAt(finalI).startAnimation(scaleSmallAnimation);
                                        } else {
                                            isSelect = true;
                                            ll_staff.getChildAt(finalI).startAnimation(scaleBigAnimation);
                                        }
                                    }
                                }

                                /**
                                 * 点击头像过滤
                                 */
                                if (isSelect) { //选中状态
                                    currentId = users.get(finalI).getUuid();
                                } else { //未选中状态默认为当前用户id
                                    currentId = Global.mUser.getUuid();
                                }


                                if (type == TYPE_FILTER_LOG) {      //日志列表
                                    logDemand.src = getLogUrl(currentTime, currentId);
                                    getWorkList();
                                } else if (type == TYPE_FILTER_TASK) {   //任务列表
                                    taskDemand.src = getTaskUrl(currentTime, currentId);
                                    getTaskList();
                                } else if (type == TYPE_FILTER_ATTENDANCE) {//考勤列表
                                    attendanceDemand.src = getAttendanceUrl(currentTime, currentId);
                                    getAttendanceList();
                                }
                                lastPosition = finalI;
                            }
                        });
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
//        List<String> list = new ArrayList<String>();
//        list.add("张三");
//        list.add("李四");
//        list.add("王五");
//        list.add("赵六");
//        list.add("王安民");
//        list.add("王满强");
//        list.add("赵统");
//        list.add("王美娜");
//        list.add("谭海兵");
//        list.add("钟斌");
//        list.add("蔡文伟");

    }

    private void setOnEvent() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {

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


        /**
         * 返回到今天（设置日期为今天，刷新日历）
         */
        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDate today = new CalendarDate();
                String tempDateStr = today.year + "-" + today.month + "-" + today.day;
                currentTime = formatDateToStr(ViewHelper.formatStrToDateAndTime(tempDateStr + " 00:00:00"), "yyyy-MM-dd");
                calendarAdapter.notifyDataChanged(today);
                if (type == TYPE_FILTER_LOG) {      //日志列表
                    logDemand.src = getLogUrl(currentTime, currentId);
                    getWorkList();
                } else if (type == TYPE_FILTER_TASK) {   //任务列表
                    taskDemand.src = getTaskUrl(currentTime, currentId);
                    getTaskList();
                } else if (type == TYPE_FILTER_ATTENDANCE) {//考勤列表
                    attendanceDemand.src = getAttendanceUrl(currentTime, currentId);
                    getAttendanceList();
                }
            }
        });


        /**
         * 顶部员工头像的点击事件
         */
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int finalI = position;
                if (lastPosition != -1) {
                    horizontalListView.getChildAt(lastPosition).setBackgroundColor(Color.TRANSPARENT);
                }

                if (!isSelect) {  //未选中状态，点击选中，设置背景为灰色，条件为选中的员工
                    isSelect = true;
                    horizontalListView.getChildAt(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                } else {    //选中状态下，点击分两种情况，点击别的位置，取消背景，点击的位置设为灰色，条件为选中的员工；点击原来的位置，取消背景，条件为空。
                    if (lastPosition != -1) {
                        if (lastPosition == finalI) {
                            isSelect = false;
                            horizontalListView.getChildAt(finalI).setBackgroundColor(Color.TRANSPARENT);
                        } else {
                            isSelect = true;
                            horizontalListView.getChildAt(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                        }
                    }
                }
                lastPosition = finalI;
            }
        });
    }

    /**
     * onWindowFocusChanged回调时，将当前月的种子日期修改为今天
     *
     * @return void
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }

    /**
     * 初始化对应功能的listener
     *
     * @return void
     */
    private void initToolbarClickListener() {
        if (calendarAdapter.getCalendarType() == CalendarAttr.CalendayType.MONTH) {
//            Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);//默认展示周日历
//            calendarAdapter.switchToWeek(monthPager.getRowIndex() + 1);
        }
//        backToday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickBackToDayBtn();
//            }
//        });
//        scrollSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendayType.WEEK) {
//                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
//                    calendarAdapter.switchToMonth();
//                } else {
//                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
//                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
//                }
//            }
//        });
//        themeSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                refreshSelectBackground();
//            }
//        });
//        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
//            }
//        });
//        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
//            }
//        });
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     *
     * @return void
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.CalendayType.MONTH,
                customDayView);
        initMarkData();
        initMonthPager();
    }

    /**
     * 初始化标记数据，HashMap的形式，可自定义
     *
     * @return void
     */
    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-19", "0");
        markData.put("2017-8-20", "0");
        markData.put("2017-8-21", "1");
        markData.put("2017-8-22", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        String dateStr = date.getYear() + "年" + date.getMonth() + "月" + date.getDay() + "日";
        String tempDateStr = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
        String tempOverDateStr = formatDateToStr(ViewHelper.getTomorrow(ViewHelper.formatStrToDateAndTime(tempDateStr + " 00:00:00")), "yyyy-MM-dd");
        String tempStartDateStr = formatDateToStr(ViewHelper.formatStrToDateAndTime(tempDateStr + " 00:00:00"), "yyyy-MM-dd");
        currentTime = tempStartDateStr;
//        String tempEndDateStr = date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " 23:59:59";
        String weekStr = DateTimeUtil.getWeek(ViewHelper.formatStrToDateAndTime(tempDateStr + " 00:00:00"));
        headerView.setTitle(dateStr + " " + weekStr);

//        String filter = "lastUpdateTime >= '" + tempDateStr + "' and lastUpdateTime <= '" + tempEndDateStr + "'";
        String filter = "lastUpdateTime between '" + tempStartDateStr + "' and '" + tempOverDateStr + "'";

        if (type == TYPE_FILTER_LOG) {      //日志列表
            logDemand.src = getLogUrl(tempStartDateStr, currentId);
            getWorkList();
        } else if (type == TYPE_FILTER_TASK) {   //任务列表
            taskDemand.src = getTaskUrl(tempStartDateStr, currentId);
            getTaskList();
        } else if (type == TYPE_FILTER_ATTENDANCE) {//考勤列表
            attendanceDemand.src = getAttendanceUrl(tempStartDateStr, currentId);
            getAttendanceList();
        }
    }

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 返回到当天
     */
    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }


    /**
     * 刷新日历
     */
    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
    }

    private void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }

    private void initLogDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志过滤 + "?date=" + currentTime + "&uuid=" + currentId;
        logDemand = new Demand<>(WorkRecord.class);
        logDemand.pageSize = 999;
        logDemand.pageIndex = 1;
        logDemand.sort = "desc";
//        logDemand.pagination = false;
        logDemand.sortField = "creationTime";
        logDemand.dictionaryNames = "creatorId.base_staff";
        logDemand.src = url;
    }


    private void initTaskDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?creationTime=" + currentTime + "&executorIds=" + currentId;
        taskDemand = new Demand<>(Task.class);
        taskDemand.pageSize = 999;
        taskDemand.pageIndex = 1;
        taskDemand.sort = "desc";
//        taskDemand.pagination = false;
        taskDemand.sortField = "creationTime";
        taskDemand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        taskDemand.src = url;
    }

    private void initAttendanceDemand() {
        CalendarDate date = new CalendarDate();
        String url = Global.BASE_JAVA_URL + GlobalMethord.考勤过滤 + "?time=" + currentTime + "&staffId=" + Global.mUser.getUuid();
        attendanceDemand = new Demand<>(Attendance.class);
        attendanceDemand.pageSize = 999;
        attendanceDemand.pageIndex = 1;
//        attendanceDemand.pagination = false;
        attendanceDemand.sortField = "attendanceDate desc";
        attendanceDemand.dictionaryNames = "creatorId.base_staff,creationDepartmentId.base_department";
        attendanceDemand.src = url;
    }


    /**
     * 获取任务列表
     */
    private void getTaskList() {

        taskDemand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                taskList = taskDemand.data;
                for (Task task : taskDemand.data) {
                    try {
                        task.setCreatorName(taskDemand.getDictName(task, "creatorId"));
                        task.setExecutorNames(taskDemand.getDictName(task, "executorIds"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                taskItemAdapter = new TaskItemAdapter(taskList, context);
                rvToDoList.setAdapter(taskItemAdapter);


                taskItemAdapter.setOnitemClickListener(new MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postion) {
                        Intent intent = new Intent(CommonFilterActivity.this, TaskInfoActivity.class);
                        Task task = taskList.get(postion);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("taskInfo", task);
                        intent.putExtra("taskIntentInfo", bundle);
                        startActivity(intent);
                    }
                });
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
     * 获取日志列表
     */

    private void getWorkList() {
        StringRequest.postAsyn(logDemand.src, logDemand, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    recordList = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), WorkRecord.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (recordList != null) {
                    recordAdapter = new LogItemAdapter(recordList, context);
                    rvToDoList.setAdapter(recordAdapter);

                    recordAdapter.setOnitemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            WorkRecord record = recordList.get(postion);

                            Intent intent = new Intent(CommonFilterActivity.this, LogInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("logInfo", record);
                            intent.putExtra("logInfoExtras", bundle);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

//        logDemand.init(new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                recordList = logDemand.data;
//
//                if (recordList != null) {
//                    for (WorkRecord task : recordList) {
//                        try {
//                            task.setCreatorName(logDemand.getDictName(task, "creatorId"));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    recordAdapter = new LogItemAdapter(recordList, context);
//                    rvToDoList.setAdapter(recordAdapter);
//                }
//
//
//                recordAdapter.setOnitemClickListener(new MyItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int postion) {
//                        WorkRecord record = recordList.get(postion);
//
//                        Intent intent = new Intent(CommonFilterActivity.this, LogInfoActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("logInfo", record);
//                        intent.putExtra("logInfoExtras", bundle);
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
    }


    /**
     * 获取考勤列表
     */
    private void getAttendanceList() {
        StringRequest.postAsyn(attendanceDemand.src, attendanceDemand, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i("考勤：" + response);
                Attendance attendance = JsonUtils.ConvertJsonObject(response, Attendance.class);
                attendanceList = new ArrayList<Attendance>();
                if (attendance != null) {
                    attendanceList.add(attendance);
                }
                attendanceListAdapter = new AttendanceListAdapter(attendanceList, context);
                rvToDoList.setAdapter(attendanceListAdapter);
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
     * 考勤列表过滤获取url
     *
     * @param date
     * @param id
     * @return
     */
    private String getAttendanceUrl(String date, String id) {
        return Global.BASE_JAVA_URL + GlobalMethord.考勤过滤 + "?time=" + date + "&staffId=" + id;
    }

    /**
     * 日志列表过滤获取url
     *
     * @param date
     * @param id
     * @return
     */
    private String getLogUrl(String date, String id) {
        return Global.BASE_JAVA_URL + GlobalMethord.日志过滤 + "?date=" + date + "&uuid=" + id;
    }

    /**
     * 任务列表过滤获取url
     *
     * @param date
     * @param id
     * @return
     */
    private String getTaskUrl(String date, String id) {
        return Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?date=" + date + "&uuid=" + id;
    }
}
