package com.biaozhunyuan.tianyi.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.CustomDayView;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.HolidayUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.HorizontalRecentlySelectedStaffView;
import com.chy.srlibrary.SwipeMenu;
import com.chy.srlibrary.SwipeMenuItem;
import com.chy.srlibrary.interfaceutil.SwipeMenuCreatorInterfaceUtil;
import com.chy.srlibrary.slistview.SMListView;
import com.chy.srlibrary.slistview.SMRListView;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.loonggg.weekcalendar.entity.CalendarData;
import com.loonggg.weekcalendar.view.WeekCalendar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static android.app.Activity.RESULT_OK;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

/**
 * ??????:????????????????????????
 */

public class TaskAssignFragment extends Fragment {

    private Demand<Task> demand;
    private CommanAdapter<Task> adapter;
    private SMRListView lv;
    public static boolean isResume = false;
    private TaskListActivityNew mContext;
    public int pageIndex = 1;
    private String currentTime = "";
    private User currentUser = new User();
    private SmartRefreshLayout refreshLayout;
    private BaseSelectPopupWindow calendarPop;
    private WeekCalendar weekCalendar;
    private TextView tvAllTask;
    private TaskListActivityNew activity;
    private HorizontalRecentlySelectedStaffView staffView;
    private List<String> selectDates = new ArrayList<>();
    private onSelectDateListener selectDateListener;
    private onSelectUserListener selectUserListener;
    private Map<String, String> keyMap;
    private DictionaryHelper helper;
    private Task currentselectItem;//???????????????????????????

    public String currentDate;
    public String currentUserId;
    /**
     * ????????????
     */
    private PopupWindow popupWindow;
    private LinearLayout llTimeTypes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_today_view, null);
        initView(view);
        initData();
        initPopwindow();
        initDemand();
        getTaskData();
        getTaskList();
        setOnTouchEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            isResume = false;
            pageIndex = 1;
            getTaskList();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS: //???????????????
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        User user = userList.getUsers().get(0);

                        if (staffView != null) {
                            staffView.reloadStaffList(user);
                        }
                    }
                    break;
            }
        }
    }

    private void initData() {
        mContext = (TaskListActivityNew) getActivity();
        currentTime = ViewHelper.getDateToday();
        helper = new DictionaryHelper(mContext);
        activity = (TaskListActivityNew) getActivity();
        refreshLayout.setReboundDuration(200);//??????????????????????????????
        refreshLayout.setDragRate(0.7f);//??????????????????/????????????????????????=????????????
        refreshLayout.setEnableLoadMore(true);//??????????????????????????????
        refreshLayout.setEnableRefresh(true);//??????????????????????????????
        refreshLayout.setEnableOverScrollDrag(true);//?????????????????????????????????????????????1.0.4
        refreshLayout.setEnableAutoLoadMore(true);//????????????????????????????????????????????????????????????
    }

    private void initPopwindow() {
        View view = View.inflate(mContext, R.layout.popup_select_time_types, null);
        popupWindow = new PopupWindow(view, (int) ViewHelper.dip2px(mContext, 100), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout llWeek = view.findViewById(R.id.ll_this_week);
        LinearLayout llMonth = view.findViewById(R.id.ll_this_month);
        LinearLayout llAll = view.findViewById(R.id.ll_all_task);

        llWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllTask.setText("??????");
                popupWindow.dismiss();
                List<String> dateThisWeeks = ViewHelper.getDateThisWeeks();
                if (dateThisWeeks.size() > 6) {
                    String startTime = dateThisWeeks.get(0) + " 00:00:00";
                    String endTime = dateThisWeeks.get(6) + " 23:59:59";
                    keyMap.put("beginTime", startTime);
                    keyMap.put("endTime", endTime);

                    pageIndex = 1;
                    getTaskData(startTime, endTime);
                    getTaskList();
                }
            }
        });

        llMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tvAllTask.setText("??????");
                String startTime = ViewHelper.formatDateToStr1
                        (ViewHelper.getFirstDateOfThisMonth()) + " 00:00:00";
                String endTime = ViewHelper.formatDateToStr1
                        (ViewHelper.getLastDateOfThisMonth()) + " 23:59:59";

                keyMap.put("beginTime", startTime);
                keyMap.put("endTime", endTime);

                getTaskData(startTime, endTime);
                pageIndex = 1;
                getTaskList();
            }
        });

        llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                tvAllTask.setText("??????");
                keyMap.put("beginTime", "");
                keyMap.put("endTime", "");
                String startTime = weekCalendar.getCurrentWeekDatas().get(0) + " 00:00:00";
                String endTime = weekCalendar.getCurrentWeekDatas().get(6) + " 23:59:59";
                getTaskData(startTime, endTime);
                pageIndex = 1;
                getTaskList();
            }
        });

    }

    /**
     * ???????????????????????????
     */
    public void getTaskList() {
        demand.keyMap = keyMap;
        demand.pageIndex = pageIndex;
        if (pageIndex == 1) {
            ProgressDialogHelper.show(mContext);
        }
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
//                List<Task> tasks = new ArrayList<>();
                for (final Task task : demand.data) {
                    try {
                        task.setCreatorName(demand.getDictName(task, "creatorId"));
                        task.setExecutorNames(demand.getDictName(task, "executorIds"));
//                        task.setCustomerName(demand.getDictName(task, "customerId"));
                        task.setParticipantNames(new DictionaryHelper(mContext).getUserNamesById(task.getParticipantIds()));
                        task.setLike(task.isLike());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /*if (task != null) {
                        //?????????????????????????????????
                        if (TaskStatusEnum.?????????.getName().equals(task.getStatus())) {
                            if (isInThisWeek(task.getBeginTime())) {
                                tasks.add(task);
                            }
                        } else {
                            tasks.add(task);
                        }
                    }*/
                }


                if (pageIndex == 1) {
                    adapter = getTaskAdapter(demand.data);
                    lv.setAdapter(adapter);
                    refreshLayout.resetNoMoreData();
                } else {
                    adapter.addBottom(demand.data, false);
                    if (demand.data != null && demand.data.size() == 0) {
                        refreshLayout.finishLoadMoreWithNoMoreData();//???????????????????????????????????????
                    }
                }
                pageIndex += 1;
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
     * ????????????????????????
     *
     * @param time
     * @return
     */
    private boolean isInThisWeek(String time) {
        List<String> weeks = ViewHelper.getWeeks(new Date());
        try {
            return DateTimeUtil.yearMonthBetween(time, weeks.get(0), weeks.get(6));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     *
     * @param listview
     */
    private void setListSwipeEnable(SMRListView listview) {
        listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        /**
                         * oldPos  ????????????????????????position
                         * setSwipeEnable() ?????????????????????
                         *           ?????????false????????????????????????
                         *           ?????????true????????????
                         *           ????????????true
                         *
                         * ???????????????????????????????????????????????????????????????
                         */
                        int oldPos = listview.pointToPosition((int) event.getX(), (int) event.getY());
                        if (oldPos < 0) {
                            // ??????????????????????????????Item????????????false
                            return false;
                        }
                        Task item = (Task) listview.getAdapter().getItem(oldPos);
                        if (item != null) {
                            if (StrUtils.pareseNull(item.getStatus()).equals(TaskStatusEnum.?????????.getName()) ||
                                    StrUtils.pareseNull(item.getStatus()).equals(TaskStatusEnum.?????????.getName())) {
                                // ????????????????????????????????????
                                listview.setSwipeEnable(false);
                            } else {
                                listview.setSwipeEnable(true);
                            }
                        }

                        break;
                }
                return false;
            }
        });
    }


    private CommanAdapter<Task> getTaskAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, getActivity(), R.layout.item_task_assign_list) {

            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_excutor_name, helper.getUserNamesById(item.getExecutorIds()));
                TextView time = viewHolder.getView(R.id.time_item_task_list);
                time.setVisibility(View.VISIBLE);
                time.setText(DateTimeUtil.dateformatTime(ViewHelper.formatStrToDateAndTime(item.getBeginTime()), false));

                TextView tvContent = viewHolder.getView(R.id.task_content);
                tvContent.setText(item.getContent());
                final ImageView ivStatus = viewHolder.getView(R.id.task_status);
                /**
                 * ??????????????????????????????????????????
                 */
                if (TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_finish);
                    tvContent.setTextColor(getResources().getColor(R.color.text_time));
                    time.setTextColor(getResources().getColor(R.color.text_time));
                } else {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                    tvContent.setTextColor(getResources().getColor(R.color.black));

                    //???????????????????????????????????????0???
                    //???????????????????????????????????? -1???
                    //???????????????????????????????????? 1???
                    if (DateTimeUtil.compareDateByDay(ViewHelper.formatStrToStr(item.getEndTime(), "yyyy-MM-dd"),
                            ViewHelper.getDateToday()) == -1) {
                        //?????????????????????????????? ??????????????????
                        time.setTextColor(Color.parseColor("#dc1414"));
                    } else {
                        //?????????????????????????????? ??????????????????
                        time.setTextColor(getResources().getColor(R.color.black));
                    }
                }

                ivStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "??????????????????,????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    /**
     * ??????????????????
     */
    private void initSMListView() {
        final SwipeMenuCreatorInterfaceUtil creator = new SwipeMenuCreatorInterfaceUtil() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#EC4D3D")));
                deleteItem.setWidth(250);
                deleteItem.setTitle("??????");
                deleteItem.setTitleColor(Color.parseColor("#ffffff"));
                deleteItem.setTitleSize(14);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem dateItem = new SwipeMenuItem(mContext);
                dateItem.setBackground(new ColorDrawable(Color.parseColor("#417EBF")));
                dateItem.setWidth(250);
                dateItem.setTitle("??????");
                dateItem.setTitleColor(Color.parseColor("#ffffff"));
                dateItem.setTitleSize(14);
                menu.addMenuItem(dateItem);
            }
        };
        // ??????????????????(??????????????????????????????????????????????????????)
        lv.setMenuCreator(creator);
        // ????????????????????????????????????????????????????????????????????????????????????SwipMenuListView??????????????????
        // ?????????????????????
        lv.setOnMenuItemClickListener(new SMListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int i1) {
                Task item = adapter.getItem(i);
                currentselectItem = item;
                if (item.getCreatorId().equals(Global.mUser.getUuid())) {
                    switch (i1) {
                        case 0: //??????
                            deleteTask(item);
                            break;
                        case 1: //??????
                            showSelectCalendar(item);
                            break;
                    }
                } else {
                    Toast.makeText(mContext, "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        setListSwipeEnable(lv);
    }

    /**
     * ????????????
     */
    private void deleteTask(Task item) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.??????????????????????????????;
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
                String data = JsonUtils.pareseData(response);
                Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                mContext.refreshList(false);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT);
            }
        });

    }

    /**
     * ???????????? ????????????
     */
    @SuppressLint("WrongViewCast")
    private void showSelectCalendar(Task item) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        if (calendarPop == null) {
            calendarPop = new BaseSelectPopupWindow(mContext, R.layout.pop_add_task_calendar);
            calendarPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            calendarPop.setFocusable(true);
            calendarPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            calendarPop.setShowTitle(false);
            calendarPop.setBackgroundDrawable(new ColorDrawable(0));
            TextView currentData = calendarPop.getContentView().findViewById(R.id.tv_currentData);

            ImageView ivLeft = calendarPop.getContentView().findViewById(R.id.iv_left);
            ImageView ivRight = calendarPop.getContentView().findViewById(R.id.iv_right);
            TextView tvToday = calendarPop.getContentView().findViewById(R.id.tv_toady);
            TextView tvTomorrow = calendarPop.getContentView().findViewById(R.id.tv_tomorrow);

            currentData.setText(ViewHelper.formatDateToStr(new Date(), "yyyy-MM"));
            MonthPager monthPager = calendarPop.getContentView().findViewById(R.id.calendar_view);
            CustomDayView customDayView = new CustomDayView(mContext, R.layout.custom_day);
            OnSelectDateListener onSelectDateListener = new OnSelectDateListener() {
                @Override
                public void onSelectDate(CalendarDate date) {
                    String selectTime = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                    currentselectItem.setBeginTime(selectTime);
                    currentselectItem.setEndTime(selectTime);
                    upDateTask(currentselectItem);
                    calendarPop.dismiss();
                }

                @Override
                public void onSelectOtherMonth(int offset) {
                    //????????? -1????????????????????????????????? ??? 1?????????????????????????????????
                    monthPager.selectOtherMonth(offset);
                }
            };
            monthPager.setViewheight(Utils.dpi2px(mContext, 270));
            CalendarViewAdapter calendarAdapter = new CalendarViewAdapter(
                    mContext,
                    onSelectDateListener,
                    CalendarAttr.CalendayType.MONTH,
                    customDayView);
            HashMap<String, String> holidayMap = HolidayUtils.getHolidayMap();
            calendarAdapter.setMarkData(holidayMap);

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
                    ArrayList<Calendar> currentCalendars = calendarAdapter.getPagers();
                    if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                        CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                        currentData.setText(date.getYear() + "???" + date.getMonth() + "???");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //????????????
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
                }
            });
            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //????????????
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
                }
            });
            tvToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //??????
                    item.setEndTime(ViewHelper.getDateToday());
                    tvToday.setTextColor(Color.parseColor("#5B8AF2"));
                    tvToday.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_left_select));
                    tvTomorrow.setTextColor(Color.parseColor("#78787C"));
                    tvTomorrow.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_right));
                    calendarPop.dismiss();
                    cancelSelectState(calendarAdapter);

                    currentselectItem.setBeginTime(ViewHelper.getCurrentFullTime());
                    currentselectItem.setEndTime(ViewHelper.getCurrentFullTime());
                    upDateTask(currentselectItem);
                }
            });
            tvTomorrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //??????
                    item.setEndTime(ViewHelper.getDateTomorrow());
                    tvTomorrow.setTextColor(Color.parseColor("#5B8AF2"));
                    tvTomorrow.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_right_select));
                    tvToday.setTextColor(Color.parseColor("#78787C"));
                    tvToday.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_left));
                    calendarPop.dismiss();
                    cancelSelectState(calendarAdapter);

                    currentselectItem.setBeginTime(ViewHelper.getTomorrowTime());
                    currentselectItem.setEndTime(ViewHelper.getTomorrowTime());
                    upDateTask(currentselectItem);
                }
            });
            calendarPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lp.alpha = 1f;
                    getActivity().getWindow().setAttributes(lp);
                    mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
        }

        lp.alpha = 0.65f;
        getActivity().getWindow().setAttributes(lp);
        calendarPop.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_tasklane_trello, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * ??????????????????
     */
    private void upDateTask(Task task) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;
        StringRequest.postAsyn(url, task, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                mContext.refreshList(false);
                pageIndex = 1;
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
     * ????????????????????????????????????
     *
     * @param calendarAdapter
     */
    private void cancelSelectState(CalendarViewAdapter calendarAdapter) {
        ArrayList<Calendar> calendars = calendarAdapter.getPagers();
        for (int i = 0; i < calendars.size(); ++i) {
            Calendar calendar = calendars.get(i);
            calendar.cancelSelectState();
        }
    }


    private void setOnTouchEvent() {
        weekCalendar.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                if (selectDateListener != null) {
                    currentDate = time;
                    selectDateListener.onSelectDate(time);
                }
                currentTime = time;
                keyMap.put("beginTime", time + " 00:00:00");
                keyMap.put("endTime", time + " 23:59:59");
                pageIndex = 1;
                getTaskList();
            }
        });
        staffView.setOnSelectedUserListener(new HorizontalRecentlySelectedStaffView.OnSelectedUserListener() {
            @Override
            public void onSelected(User user) {
                if (selectUserListener != null) {
                    currentUserId = user.getUuid();
                    selectUserListener.onSelectUser(user);
                }
                currentUser = user;
                pageIndex = 1;
                keyMap.put("userId", user.getUuid());
                getTaskList();
                List<CalendarData> currentWeekDatas = weekCalendar.getCurrentWeekDatas();
                getTaskData(currentWeekDatas.get(0).getDateStr(),
                        currentWeekDatas.get(currentWeekDatas.size() - 1).getDateStr());
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(mContext, TaskInfoActivityNew.class);
                    Task task = adapter.getItem(position - 1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtra("taskIntentInfo", bundle);
                    startActivity(intent);
                }
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getTaskList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                getTaskList();
            }
        });
        weekCalendar.setOnPageChangedListener(new WeekCalendar.OnPageChangedListener() {
            @Override
            public void onPageChange(List<CalendarData> dataList) {
                getTaskData(dataList.get(0).getDateStr(), dataList.get(dataList.size() - 1).getDateStr());
            }
        });

        llTimeTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(llTimeTypes);
            }
        });
    }

    private void initDemand() {
        demand = new Demand<Task>(Task.class);
        demand.pageSize = 10;
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.????????????;
        keyMap = new HashMap<>();
        keyMap.put("creatorId", Global.mUser.getUuid());
        keyMap.put("isShowAllHistory", "true");
        keyMap.put("isMyZhipai", "true");
        keyMap.put("index ", "taskList");


        /**
         * ???????????????????????????
         */
        String startTime = weekCalendar.getCurrentWeekDatas().get(0).getDateStr() + " 00:00:00";
        String endTime = weekCalendar.getCurrentWeekDatas().get(6).getDateStr() + " 23:59:59";
        keyMap.put("beginTime", startTime);
        keyMap.put("endTime", endTime);
        demand.keyMap = keyMap;
        demand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff,customerId.crm_customer";
    }

    private void initView(View view) {
        lv = view.findViewById(R.id.lv_task_day_list);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        View view1 = View.inflate(getActivity(), R.layout.list_header_task, null);
        llTimeTypes = (LinearLayout) view1.findViewById(R.id.ll_select_time_types);
        lv.addHeaderView(view1);
        weekCalendar = (WeekCalendar) view1.findViewById(R.id.weekcalendar_task_day_view);

        tvAllTask = view1.findViewById(R.id.tv_all_view);
        staffView = view1.findViewById(R.id.staff_view);
        staffView.setFragment(TaskAssignFragment.this);
        initSMListView();
    }

    /**
     * ?????????????????????????????????
     */
    private void getTaskData() {
        List<CalendarData> currentWeekDatas = weekCalendar.getCurrentWeekDatas();
        getTaskData(currentWeekDatas.get(0).getDateStr(),
                currentWeekDatas.get(currentWeekDatas.size() - 1).getDateStr());
    }

    /**
     * ?????????????????????????????????
     *
     * @param startTime ???????????????????????????
     * @param endTime   ???????????????????????????
     */
    public void getTaskData(String startTime, String endTime) {
        String userId = "";
        if (currentUser != null) {
            userId = TextUtils.isEmpty(currentUser.getUuid()) ? "" : currentUser.getUuid();
        }
        if (TextUtils.isEmpty(startTime)) {
            startTime = weekCalendar.getCurrentWeekDatas().get(0).getDateStr();
        }
        if (TextUtils.isEmpty(endTime)) {
            endTime = weekCalendar.getCurrentWeekDatas().get(6).getDateStr();
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.???????????????????????????
                + "?beginTime=" + startTime + " 00:00:00&endTime=" + endTime
                + " 23:59:59&executorIds=" + userId
                + "&creatorId=" + Global.mUser.getUuid() + "&isMyZhipai=true"
                + "&projectId=" + StrUtils.pareseNull(keyMap.get("projectId"))
                + "&status=" + StrUtils.pareseNull(keyMap.get("status"));
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                selectDates.clear();
                List<Task> data = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Task.class);
                for (Task task : data) {
                    if (task.isHave()) {
                        String[] split = task.getBeginTime().split(" ");
                        selectDates.add(split[0]);
                    }
                }
                if (!selectDates.isEmpty()) {
                    weekCalendar.setSelectDates(selectDates);
                } else {
                    weekCalendar.clearMarkDay();
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

    public void getFilterList(TaskFilter filter) {
        pageIndex = 1;
        keyMap.put("status", filter.getStatus());
        keyMap.put("projectId", filter.getProjectId());
        List<CalendarData> currentWeekDatas = weekCalendar.getCurrentWeekDatas();
        getTaskData(currentWeekDatas.get(0).getDateStr(),
                currentWeekDatas.get(currentWeekDatas.size() - 1).getDateStr());
        getTaskList();
    }

    public interface onSelectDateListener {
        void onSelectDate(String date);
    }

    public void setOnSelectDateListener(onSelectDateListener listener) {
        this.selectDateListener = listener;
    }

    public interface onSelectUserListener {
        void onSelectUser(User user);
    }

    public void setOnSelectUserListener(onSelectUserListener listener) {
        this.selectUserListener = listener;
    }
}
