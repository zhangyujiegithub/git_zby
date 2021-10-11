package com.biaozhunyuan.tianyi.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.CustomDayView;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.HolidayUtils;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.models.SelectStageItem;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.task.TasksCompletion;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.ShapeUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.loonggg.weekcalendar.view.WeekCalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/10/9.
 * 联系记录列表
 */

public class ContactListFragment extends Fragment {

    public static boolean isResume = false;
    private Context context;
    private CommanAdapter adapter;
    private List<Contact> recordList;
    private DictionaryHelper helper;
    //    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1; //当前页
    private Demand<Contact> demand;
//    private BoeryunSearchViewNoButton searchView;

    /**
     * 顶部过滤
     */
    private HorizontalScrollView scrollView;
    private LinearLayout ll_staff;
    private RelativeLayout rlSelectTime;
    private WeekCalendar weekCalendar;
    private BoeryunSearchView searchView;
    //    private List<User> userList;

    private RelativeLayout rl_back_calander;
    private RelativeLayout rl_show_calander;
    private RelativeLayout rl_hide_calander;
    private ImageView iv_show_calander;
    private ImageView iv_hide_calander;
    private TextView tv_current_time;
    private ImageView iv_hide_head;
    private LinearLayout llFilter;


    /**
     * 过滤
     */
    private RelativeLayout rlFilterStage;
    private TextView tvFilterStage;
    private ImageView ivFilterStage;
    private RelativeLayout rlFilterTime;
    private ImageView ivFilterTime;
    private TextView tvFilterTime;

    private PopupWindow popFilterStage;
    private PopupWindow popFilterTime;
    private BaseSelectPopupWindow calendarPop;//选择日期的popupwindow
    private CalendarViewAdapter calendarAdapter;

    private SelectStageItem currentStage;
    private SelectStageItem currentSource;
    private List<SelectStageItem> stageList;
    private List<SelectStageItem> sourceList;
    private int filterGroupPos = 0;
    private String[] filterTitles = new String[]{"阶段"};
    private boolean isSelectStartTime = true;
    private boolean isSelectCreateTime = true;
    private String selectCreateTime = "";  //选择的创建时间类型：今天、昨天、不限、最近7天、自定义
    private String selectContactTime = ""; //选择的联系时间类型：今天、昨天、不限、最近7天、自定义
    private String selectCreateStartTime = "";//创建时间为自定义的开始时间
    private String selectCreateEndTime = "";//创建时间为自定义的结束时间
    private String selectContactStartTime = "";
    private String selectContactEndTime = "";
    private TextView tvAllTime;
    private TextView tvToday;
    private TextView tvYesterday;
    private TextView tvLast7Day;
    private TextView tvCreateTime;
    private TextView tvContactTime;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private NoScrollListView lvChild;


    private int calanderType = 0;//日历的状态：0=显示，1=隐藏   默认为显示状态
    private boolean isHeadShow = false;
    private List<User> users;
    private List<TasksCompletion> userList;
    private int lastPosition = -1;
    private boolean isSelect = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_shouwen, null);
        initData();
        initViews(view);
        initStaffHeader();
        initFilterTStagePop();
        initFilterTimePop();
        ProgressDialogHelper.show(getActivity());
        initDemand();
        getWorkList();
        setEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            getWorkList();
            isResume = false;
        }
    }

    private void initData() {
        context = getActivity();
        recordList = new ArrayList<Contact>();
        helper = new DictionaryHelper(getActivity());

        stageList = new ArrayList<>();
        sourceList = new ArrayList<>();
        SelectStageItem item = new SelectStageItem();
        item.setName("全部");
        stageList.add(item);
        SelectStageItem sourceItem = new SelectStageItem();
        sourceItem.setName("全部");
        currentSource = sourceItem;
        sourceList.add(sourceItem);
        currentStage = new SelectStageItem();
        currentStage.setUuid("");
        currentStage.setName("全部");
    }

    private void initViews(View view1) {
        lv = (PullToRefreshAndLoadMoreListView) view1.findViewById(R.id.lv_common_shouwen);

        View view = View.inflate(context, R.layout.header_worklog, null);
        RelativeLayout rl_time = (RelativeLayout) view.findViewById(R.id.rl_time_header);
        ll_staff = (LinearLayout) view.findViewById(R.id.ll_user_root_task_day_view);
        rlSelectTime = (RelativeLayout) view.findViewById(R.id.rl_select_time);
        rlSelectTime.setVisibility(View.GONE);
        llFilter = (LinearLayout) view.findViewById(R.id.ll_filter);
        llFilter.setVisibility(View.VISIBLE);
        scrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView_task_day_view);
        weekCalendar = (WeekCalendar) view.findViewById(R.id.weekcalendar_task_day_view);
        searchView = view.findViewById(R.id.search_view);
        tv_current_time = (TextView) view.findViewById(R.id.tv_current_time);
        iv_hide_calander = (ImageView) view.findViewById(R.id.iv_hide_calander);
        iv_show_calander = (ImageView) view.findViewById(R.id.iv_home_show_calander);
        rl_show_calander = (RelativeLayout) view.findViewById(R.id.rl_home_calander);
        rl_hide_calander = (RelativeLayout) view.findViewById(R.id.rl_home_yearmonth);
        rl_back_calander = (RelativeLayout) view.findViewById(R.id.rl_calendar_month);
        ivFilterStage = view.findViewById(R.id.iv_filter_stage);
        tvFilterStage = view.findViewById(R.id.tv_filter_stage);
        tvFilterTime = view.findViewById(R.id.tv_filter_time);
        rlFilterStage = view.findViewById(R.id.rl_filter_client_stage);
        rlFilterTime = view.findViewById(R.id.rl_filter_client_time);
        ivFilterTime = view.findViewById(R.id.iv_filter_time);

        iv_hide_head = (ImageView) view.findViewById(R.id.iv_hide_head_log_List);
        weekCalendar.setVisibility(View.VISIBLE);
        lv.addHeaderView(view);
        rl_time.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);

        calanderType = 1;
        showCanlender();

        scrollView.setVisibility(View.GONE);
        String time = ViewHelper.getDateTodayStr();

        currentTime = time;

        String yue = time.substring(5, 7);
        String ri = time.substring(8, 10);
        if (yue.substring(0, 1).equals("0")) {
            yue = yue.substring(1, 2);
        }
        if (ri.substring(0, 1).equals("0")) {
            ri = ri.substring(1, 2);
        }
        time = time.substring(0, 4) + "年" + yue + "月" + ri + "日";
        tv_current_time.setText(currentTime);
    }


    private void getClientListByFilter() {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(selectCreateTime)) {
            if (!"不限".equals(selectCreateTime)) { //不是不限，要添加过滤条件
                String createTimeStr = "";
                if ("今天".equals(selectCreateTime)) {
                    createTimeStr = ViewHelper.getDateToday() + " 00:00|" + ViewHelper.getDateToday() + " 23:59";
                } else if ("昨天".equals(selectCreateTime)) {
                    createTimeStr = ViewHelper.getDateYestoday() + " 00:00|" + ViewHelper.getDateYestoday() + " 23:59";
                } else if ("最近7天".equals(selectCreateTime)) {
                    ArrayList<String> totalDaysList = ViewHelper.getTotalDaysList(7);
                    String startTime = totalDaysList.get(0);
                    String endTime = totalDaysList.get(totalDaysList.size() - 1);
                    createTimeStr = startTime + " 00:00|" + endTime + " 23:59";
                } else if ("自定义".equals(selectCreateTime)) {
                    createTimeStr = selectCreateStartTime + " 00:00|" + selectCreateEndTime + " 23:59";
                }
                map.put("searchField_datetime_createTime", createTimeStr);
            } else {
                map.put("searchField_datetime_createTime", "");
            }
        }
        if (!TextUtils.isEmpty(selectContactTime)) {
            if (!"不限".equals(selectCreateTime)) { //不是不限，要添加过滤条件
                String createTimeStr = "";
                if ("今天".equals(selectContactTime)) {
                    createTimeStr = ViewHelper.getDateToday() + " 00:00|" + ViewHelper.getDateToday() + " 23:59";
                } else if ("昨天".equals(selectContactTime)) {
                    createTimeStr = ViewHelper.getDateYestoday() + " 00:00|" + ViewHelper.getDateYestoday() + " 23:59";
                } else if ("最近7天".equals(selectContactTime)) {
                    ArrayList<String> totalDaysList = ViewHelper.getTotalDaysList(7);
                    String startTime = totalDaysList.get(0);
                    String endTime = totalDaysList.get(totalDaysList.size() - 1);
                    createTimeStr = startTime + " 00:00|" + endTime + " 23:59";
                } else if ("自定义".equals(selectContactTime)) {
                    createTimeStr = selectCreateStartTime + " 00:00|" + selectCreateEndTime + " 23:59";
                }
                map.put("searchField_datetime_contactTime", createTimeStr);
            } else {
                map.put("searchField_datetime_contactTime", "");
            }
        }
        if ("全部".equals(currentStage.getName())) {
            map.put("searchField_dictionary_stage", "");
        } else {
            map.put("searchField_dictionary_stage", currentStage.getUuid());
        }
        if ("全部".equals(currentSource.getName())) {
            map.put("searchField_dictionary_source", "");
        } else {
            map.put("searchField_dictionary_source", currentSource.getUuid());
        }

        if (!TextUtils.isEmpty(demand.fuzzySearch)) {
            map.put("searchField_string_content", demand.fuzzySearch);
        }
        if (adapter != null) {
            adapter.clearData();
        }
        demand.keyMap = map;
        pageIndex = 1;
        getWorkList();
    }


    private void showCanlender() {
        if (calanderType == 0) {
            rl_show_calander.setVisibility(View.VISIBLE);
            rl_hide_calander.setVisibility(View.GONE);
            iv_hide_calander.setImageResource(R.drawable.arrow_up);
            calanderType = 1;
        } else if (calanderType == 1) {
            rl_show_calander.setVisibility(View.GONE);
            rl_hide_calander.setVisibility(View.VISIBLE);
            iv_hide_calander.setImageResource(R.drawable.arrow_down);
            calanderType = 0;
        }
    }


    private void setEvent() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchView.setOnCancleSearch();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getWorkList();
            }
        });


        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                lv.startRefresh();
                demand.resetFuzzySearchField(false);
                pageIndex = 1;
                getWorkList();
            }

            @Override
            public void OnClick() {

            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
                demand.fuzzySearch = str;
                if (!TextUtils.isEmpty(selectCreateTime) ||
                        !TextUtils.isEmpty(selectContactTime) ||
                        !"全部".equals(currentStage.getName())) {
                    if (TextUtils.isEmpty(str)) {
                        demand.keyMap.put("searchField_string_content", "");
                    } else {
                        demand.keyMap.put("searchField_string_content", "1|" + str);
                    }
                }
                demand.resetFuzzySearchField(true);
                lv.startRefresh();
                getWorkList();
            }
        });


        /**
         * 隐藏/展示日历
         */
        rl_back_calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCanlender();
            }
        });

        iv_hide_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHeadShow) {
                    scrollView.setVisibility(View.GONE);
                    isHeadShow = false;
                    iv_hide_head.setImageResource(R.drawable.icon_show_head);
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    isHeadShow = true;
                    iv_hide_head.setImageResource(R.drawable.icon_hide_head);
                }
            }
        });

        weekCalendar.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                currentTime = time;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表 +
                        "?from=" + currentTime + " 00:00:00" + "&to=" + currentTime + " 23:59:59" + "&uuid=" + currentId;
                pageIndex = 1;
                getWorkList();
            }
        });


        rlFilterStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popFilterStage == null)
                    return;
                if (!popFilterStage.isShowing()) {
                    if (popFilterTime.isShowing()) {
                        tvFilterTime.setTextColor(getResources().getColor(R.color.text_black));
                        ivFilterTime.setImageResource(R.drawable.icon_arrow_down_black);
                        popFilterTime.dismiss();
                    }
                    tvFilterStage.setTextColor(getResources().getColor(R.color.hanyaRed));
                    ivFilterStage.setImageResource(R.drawable.icon_arrow_up_blue);
                    popFilterStage.showAsDropDown(rlFilterStage);
                } else {
                    tvFilterStage.setTextColor(getResources().getColor(R.color.text_black));
                    ivFilterStage.setImageResource(R.drawable.icon_arrow_down_black);
                    popFilterStage.dismiss();
                }
            }
        });

        rlFilterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popFilterTime.isShowing()) {
                    if (popFilterStage != null && popFilterStage.isShowing()) {
                        tvFilterStage.setTextColor(getResources().getColor(R.color.text_black));
                        ivFilterStage.setImageResource(R.drawable.icon_arrow_down_black);
                        popFilterStage.dismiss();
                    }
                    tvFilterTime.setTextColor(getResources().getColor(R.color.hanyaRed));
                    ivFilterTime.setImageResource(R.drawable.icon_arrow_up_blue);
                    popFilterTime.showAsDropDown(rlFilterTime);
                } else {
                    ivFilterTime.setImageResource(R.drawable.icon_arrow_down_black);
                    popFilterTime.dismiss();
                }
            }
        });
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表2 + "?customerId=&projectId=&chanceId=";
        demand = new Demand<>(Contact.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "createTime";
        demand.dictionaryNames = "projectId.crm_project,customerId.crm_customer,stage.dict_contact_stage,contactWay.dict_contact_way";
        demand.src = url;
        demand.setFuzzySearch("crm_contact", "customerId.crm_customer,contactWay.dict_contact_way");
    }


    private void getWorkList() {
        ProgressDialogHelper.show(getActivity());
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                recordList = demand.data;

                try {
                    for (Contact project : recordList) {
                        project.setCustomerName(demand.getDictName(project, "customerId"));
                        project.setProjectName(demand.getDictName(project, "projectId"));
                        project.setStageName(demand.getDictName(project, "stage"));
                        project.setContactWayName(demand.getDictName(project, "contactWay"));
                    }
                } catch (Exception e) {

                }

                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(recordList);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(recordList, false);
                    if (recordList != null && recordList.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position >= 2) {
                            Contact record = (Contact) adapter.getDataList().get(position - 2);
                            Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("contactInfo", record);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    }
                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                lv.onRefreshComplete();
                lv.loadCompleted();
            }
        });


    }


    private void initFilterTStagePop() {
        View view = View.inflate(context, R.layout.pop_list_filter, null);
        popFilterStage = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popFilterStage.setOutsideTouchable(false);
        popFilterStage.setAnimationStyle(R.style.PopupWindowAnimation);
        NoScrollListView lvGroup = view.findViewById(R.id.lv_list_group);
        lvChild = view.findViewById(R.id.lv_list_child);
        View bgView = view.findViewById(R.id.bg_view);

        List<String> titles = Arrays.asList(filterTitles);
        CommanAdapter<String> stageAdapter = getFilterAdapter(titles);
        lvGroup.setAdapter(stageAdapter);
        lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterGroupPos = position;
                for (int i = 0; i < parent.getCount(); i++) { //给选中的条目设置背景色
                    View v = parent.getChildAt(i);
                    if (position == i) {
                        v.setBackgroundColor(getResources().getColor(R.color.tv_search_grey));
                    } else {
                        v.setBackgroundColor(Color.WHITE);
                    }
                }
                if (position == 0) {
                    if (stageList.size() == 1) { //有一个默认的全部
                        getCustomDicts("dict_contact_stage");
                    } else {
                        lvChild.setAdapter(getChildAdapter(stageList));
                    }
                } else if (position == 1) {
                    if (sourceList.size() == 1) { //有一个默认的全部
                        getCustomDicts("dict_customer_origin");
                    } else {
                        lvChild.setAdapter(getChildAdapter(sourceList));
                    }
                }
            }
        });

        lvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (filterGroupPos == 0) {
                    currentStage = stageList.get(position);
                    for (int i = 0; i < stageList.size(); i++) {
                        if (position == i) {
                            stageList.get(i).setSelect(true);
                        } else {
                            stageList.get(i).setSelect(false);
                        }
                    }
                } else if (filterGroupPos == 1) {
                    currentSource = sourceList.get(position);
                    for (int i = 0; i < sourceList.size(); i++) {
                        if (position == i) {
                            sourceList.get(i).setSelect(true);
                        } else {
                            sourceList.get(i).setSelect(false);
                        }
                    }
                }
                for (int i = 0; i < parent.getCount(); i++) { //给选中的条目设置背景色
                    View v = parent.getChildAt(i);
                    if (position == i) {
                        v.setBackgroundColor(getResources().getColor(R.color.tv_search_grey));
                    } else {
                        v.setBackgroundColor(Color.WHITE);
                    }
                }
                getClientListByFilter();
                popFilterStage.dismiss();
            }
        });


        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFilterStage.setTextColor(getResources().getColor(R.color.text_black));
                ivFilterStage.setImageResource(R.drawable.icon_arrow_down_black);
                popFilterStage.dismiss();
            }
        });
    }

    private void initFilterTimePop() {
        View view = View.inflate(context, R.layout.popup_select_time_client, null);
        View bg_view = view.findViewById(R.id.bg_view);
        tvCreateTime = view.findViewById(R.id.tv_create_time);
        tvContactTime = view.findViewById(R.id.tv_contact_time);
        tvStartTime = view.findViewById(R.id.tv_start_time);
        tvEndTime = view.findViewById(R.id.tv_end_time);
        tvAllTime = view.findViewById(R.id.tv_all_time);
        tvToday = view.findViewById(R.id.tv_today);
        tvYesterday = view.findViewById(R.id.tv_yesterday);
        tvLast7Day = view.findViewById(R.id.tv_last_7_day);

        popFilterTime = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popFilterTime.setOutsideTouchable(false);
        popFilterTime.setAnimationStyle(R.style.PopupWindowAnimation);


        tvCreateTime.setTextColor(getResources().getColor(R.color.hanyaRed));
        bg_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFilterTime.setImageResource(R.drawable.icon_arrow_down_black);
                popFilterTime.dismiss();
            }
        });

        tvCreateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectCreateTime = true;
                initTimePopSelectStatus();
            }
        });

        tvContactTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectCreateTime = false;
                initTimePopSelectStatus();
            }
        });

        tvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectCreateTime) {
                    selectCreateTime = "今天";
                } else {
                    selectContactTime = "今天";
                }

                setTextSelect(tvToday);
                popFilterTime.dismiss();
                getClientListByFilter();
            }
        });

        tvAllTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectCreateTime) {
                    selectCreateTime = "不限";
                } else {
                    selectContactTime = "不限";
                }
                setTextSelect(tvAllTime);
                popFilterTime.dismiss();
                getClientListByFilter();
            }
        });

        tvYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectCreateTime) {
                    selectCreateTime = "昨天";
                } else {
                    selectContactTime = "昨天";
                }

                setTextSelect(tvYesterday);
                popFilterTime.dismiss();
                getClientListByFilter();
            }
        });

        tvLast7Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectCreateTime) {
                    selectCreateTime = "最近7天";
                } else {
                    selectContactTime = "最近7天";
                }

                setTextSelect(tvLast7Day);
                popFilterTime.dismiss();
                getClientListByFilter();
            }
        });

        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectStartTime = true;
                showSelectCalendar();
            }
        });

        tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectStartTime = false;
                showSelectCalendar();
            }
        });
    }


    /**
     * 显示日历 选择日期
     */
    @SuppressLint("WrongViewCast")
    private void showSelectCalendar() {
        if (calendarPop == null) {
            calendarPop = new BaseSelectPopupWindow(context, R.layout.pop_add_task_calendar);
            calendarPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            calendarPop.setFocusable(true);
            calendarPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            calendarPop.setShowTitle(false);
            calendarPop.setBackgroundDrawable(new ColorDrawable(0));
            TextView currentData = calendarPop.getContentView().findViewById(R.id.tv_currentData);
            ImageView ivLeft = calendarPop.getContentView().findViewById(R.id.iv_left);
            LinearLayout llDate = calendarPop.getContentView().findViewById(R.id.ll_select_date);
            ImageView ivRight = calendarPop.getContentView().findViewById(R.id.iv_right);
            currentData.setText(ViewHelper.formatDateToStr(new Date(), "yyyy-MM"));
            MonthPager monthPager = calendarPop.getContentView().findViewById(R.id.calendar_view);
            CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
            llDate.setVisibility(View.GONE);

            OnSelectDateListener onSelectDateListener = new OnSelectDateListener() {
                @Override
                public void onSelectDate(CalendarDate date) {

                    String time = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                    if (isSelectStartTime) {
                        tvStartTime.setText(time);
                        if (isSelectCreateTime) {
                            selectCreateStartTime = time;
                        } else {
                            selectContactStartTime = time;
                        }
                    } else {
                        tvEndTime.setText(time);
                        if (isSelectCreateTime) {
                            selectCreateEndTime = time;
                        } else {
                            selectContactEndTime = time;
                        }
                    }
                    String startTime = tvStartTime.getText().toString();
                    String endTime = tvEndTime.getText().toString();

                    //如果开始和结束时间都不为空
                    if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                        if (isSelectCreateTime) {
                            selectCreateTime = "自定义";
                        } else {
                            selectContactTime = "自定义";
                        }
                        initTimePopSelectStatus();
                        popFilterTime.dismiss();
                        getClientListByFilter();
                    }
                    calendarPop.dismiss();
                }

                @Override
                public void onSelectOtherMonth(int offset) {
                    //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                    monthPager.selectOtherMonth(offset);
                }
            };
            monthPager.setViewheight(Utils.dpi2px(context, 270));
            calendarAdapter = new CalendarViewAdapter(
                    context,
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
                        currentData.setText(date.getYear() + "年" + date.getMonth() + "月");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //向前翻页
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
                }
            });

            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //向后翻页
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
                }
            });

            calendarPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
        }
        calendarPop.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_tasklane_trello, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    private void initTimePopSelectStatus() {
        if (isSelectCreateTime) {
            tvCreateTime.setTextColor(getResources().getColor(R.color.hanyaRed));
            tvContactTime.setTextColor(getResources().getColor(R.color.text_black));

            if ("今天".equals(selectCreateTime)) {
                setTextSelect(tvToday);
            } else if ("昨天".equals(selectCreateTime)) {
                setTextSelect(tvYesterday);
            } else if ("不限".equals(selectCreateTime)) {
                setTextSelect(tvAllTime);
            } else if ("最近7天".equals(selectCreateTime)) {
                setTextSelect(tvLast7Day);
            } else {
                setTextSelect(null);
                if ("自定义".equals(selectCreateTime)) {
                    tvStartTime.setText(selectCreateStartTime);
                    tvEndTime.setText(selectCreateEndTime);
                }
            }
        } else {
            tvContactTime.setTextColor(getResources().getColor(R.color.hanyaRed));
            tvCreateTime.setTextColor(getResources().getColor(R.color.text_black));

            if ("今天".equals(selectContactTime)) {
                setTextSelect(tvToday);
            } else if ("昨天".equals(selectContactTime)) {
                setTextSelect(tvYesterday);
            } else if ("不限".equals(selectContactTime)) {
                setTextSelect(tvAllTime);
            } else if ("最近7天".equals(selectContactTime)) {
                setTextSelect(tvLast7Day);
            } else {
                setTextSelect(null);
                if ("自定义".equals(selectContactTime)) {
                    tvStartTime.setText(selectContactStartTime);
                    tvEndTime.setText(selectContactEndTime);
                }
            }
        }
    }

    private void setTextSelect(TextView tv) {
        tvStartTime.setText("");
        tvEndTime.setText("");
        if (tv == null) {
            tvAllTime.setBackgroundResource(R.drawable.shape_round_gray);
            tvToday.setBackgroundResource(R.drawable.shape_round_gray);
            tvYesterday.setBackgroundResource(R.drawable.shape_round_gray);
            tvLast7Day.setBackgroundResource(R.drawable.shape_round_gray);
        } else if (tv.equals(tvAllTime)) {
            tvAllTime.setBackgroundResource(R.drawable.tv_tag_bg_selected);
            tvToday.setBackgroundResource(R.drawable.shape_round_gray);
            tvYesterday.setBackgroundResource(R.drawable.shape_round_gray);
            tvLast7Day.setBackgroundResource(R.drawable.shape_round_gray);
        } else if (tv.equals(tvToday)) {
            tvToday.setBackgroundResource(R.drawable.tv_tag_bg_selected);
            tvAllTime.setBackgroundResource(R.drawable.shape_round_gray);
            tvYesterday.setBackgroundResource(R.drawable.shape_round_gray);
            tvLast7Day.setBackgroundResource(R.drawable.shape_round_gray);
        } else if (tv.equals(tvYesterday)) {
            tvYesterday.setBackgroundResource(R.drawable.tv_tag_bg_selected);
            tvAllTime.setBackgroundResource(R.drawable.shape_round_gray);
            tvToday.setBackgroundResource(R.drawable.shape_round_gray);
            tvLast7Day.setBackgroundResource(R.drawable.shape_round_gray);
        } else if (tv.equals(tvLast7Day)) {
            tvLast7Day.setBackgroundResource(R.drawable.tv_tag_bg_selected);
            tvAllTime.setBackgroundResource(R.drawable.shape_round_gray);
            tvToday.setBackgroundResource(R.drawable.shape_round_gray);
            tvYesterday.setBackgroundResource(R.drawable.shape_round_gray);
        }
    }

    public void getCustomDicts(final String dictTableName) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;

        final DictData dictData = new DictData();
        dictData.setDictionaryName(dictTableName);
        dictData.setFull(true);
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SelectStageItem> stageLists = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), SelectStageItem.class);

                if (stageLists != null) {
                    if ("dict_contact_stage".equals(dictTableName)) {
                        stageList.addAll(stageLists);
                        lvChild.setAdapter(getChildAdapter(stageList));
                    } else if ("dict_customer_origin".equals(dictTableName)) {
                        sourceList.addAll(stageLists);
                        lvChild.setAdapter(getChildAdapter(sourceList));
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


    private CommanAdapter<String> getFilterAdapter(List<String> list) {
        return new CommanAdapter<String>(list, context, R.layout.item_dictionary) {
            @Override
            public void convert(int position, String item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, item);
            }
        };
    }


    private CommanAdapter<SelectStageItem> getChildAdapter(List<SelectStageItem> list) {
        return new CommanAdapter<SelectStageItem>(list, context, R.layout.item_dictionary2) {
            @Override
            public void convert(int position, SelectStageItem item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, item.getName());
                if (item.isSelect()) {
                    viewHolder.setBackgroundColor(getResources().getColor(R.color.tv_search_grey));
                } else {
                    viewHolder.setBackgroundColor(getResources().getColor(R.color.white));
                }
            }
        };
    }


    /**
     * 初始化头布局过滤员工
     */
    private void initStaffHeader() {
        final String url = Global.BASE_JAVA_URL + GlobalMethord.任务查看员工 + "?from=";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                userList = JsonUtils.ConvertJsonToList(response, TasksCompletion.class);

                if (userList != null) {
                    for (int i = 0; i < userList.size(); i++) {
                        View view1 = View.inflate(context, R.layout.item_head_view, null);
                        TextView tvName = (TextView) view1.findViewById(R.id.tv_name_header_view);
                        CircleImageView view2 = (CircleImageView) view1.findViewById(R.id.head_header_view);
                        tvName.setText(userList.get(i).getStaffName());
                        ImageUtils.displyImageById(new DictionaryHelper(context).getUserPhoto(userList.get(i).getStaffId()), view2);
                        ll_staff.addView(view1);
                    }

                    for (int i = 0; i < userList.size(); i++) {
                        final int finalI = i;
                        ll_staff.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lastPosition != -1) {
                                    ll_staff.getChildAt(lastPosition).setBackgroundColor(Color.TRANSPARENT);
                                }

                                if (!isSelect) {  //未选中状态，点击选中，设置背景为灰色，条件为选中的员工
                                    isSelect = true;
                                    ll_staff.getChildAt(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                                } else {    //选中状态下，点击分两种情况，点击别的位置，取消背景，点击的位置设为灰色，条件为选中的员工；点击原来的位置，取消背景，条件为空。
                                    if (lastPosition != -1) {
                                        if (lastPosition == finalI) {
                                            isSelect = false;
                                            ll_staff.getChildAt(finalI).setBackgroundColor(Color.TRANSPARENT);
                                        } else {
                                            isSelect = true;
                                            ll_staff.getChildAt(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                                        }
                                    }
                                }

                                /**
                                 * 点击头像过滤
                                 */
                                if (isSelect) { //选中状态
                                    currentId = userList.get(finalI).getStaffId();
                                } else { //未选中状态默认为当前用户id
                                    currentId = Global.mUser.getUuid();
                                }
                                lastPosition = finalI;
                                demand.src = Global.BASE_JAVA_URL + GlobalMethord.日志过滤 + "?date=" + currentTime + "&uuid=" + currentId;
                                pageIndex = 1;
                                getWorkList();
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
    }


//    /**
//     * 初始化头布局过滤员工
//     */
//    private void initStaffHeader() {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.下属员工 + "?staffId=" + Global.mUser.getUuid();
//        StringRequest.getAsyn(url, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                users = JsonUtils.ConvertJsonToList(response, User.class);
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
//    }

    private CommanAdapter<Contact> getAdapter(
            List<Contact> gridItems) {
        return new CommanAdapter<Contact>(gridItems, context,
                R.layout.item_contract_list) {
            @SuppressLint("NewApi")
            @Override
            public void convert(int position, final Contact item,
                                BoeryunViewHolder viewHolder) {

                String name = helper.getUserNameById(item.getAdvisorId());
//                if (!TextUtils.isEmpty(item.getCustomerName())) {
//                    name += "(" + item.getCustomerName() + ")";
//                }
                viewHolder.setTextValue(R.id.tv_name_contact_item, name);
                viewHolder.setTextValue(R.id.tv_advisor_contact_item, TextUtils.isEmpty(item.getCustomerName()) ? item.getProjectName() : item.getCustomerName());
                if (StrUtils.pareseNull(item.getContactTime()).contains(" 00:00:00")) {
                    viewHolder.setTextValue(R.id.tv_time_contact_item,
                            ViewHelper.getDateStringFormat(item.getContactTime(), "yyyy-MM-dd"));
                } else {
                    viewHolder.setTextValue(R.id.tv_time_contact_item,
                            ViewHelper.getDateStringFormat(StrUtils.pareseNull(item.getContactTime()), "yyyy-MM-dd HH:mm"));
                }
                viewHolder.setTextValue(R.id.content_contact_list, item.getContent());
                viewHolder.setUserPhoto(R.id.head_item_contact_list, item.getAdvisorId());

                MultipleAttachView attachView = viewHolder.getView(R.id.attach_item_contact);

                if (!TextUtils.isEmpty(item.getAttachment())) {
                    attachView.setVisibility(View.VISIBLE);
                    attachView.loadImageByAttachIds(item.getAttachment());
                } else {
                    attachView.setVisibility(View.GONE);
                    attachView.loadImageByAttachIds("");
                }


                TextView tvStage = viewHolder.getView(R.id.tv_status_item_contact);

                if (!TextUtils.isEmpty(item.getStageName())) {
                    tvStage.setVisibility(View.VISIBLE);
                    tvStage.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getResources().getColor(R.color.hanyaRed), ViewHelper.dip2px(getActivity(), 5), 0));
                    tvStage.setText(item.getStageName());
                } else {
                    tvStage.setVisibility(View.GONE);
                }

                //评论
                viewHolder.getView(R.id.ll_item_log_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWiw(item);
                    }
                });

                LinearLayout ll_support = viewHolder.getView(R.id.ll_item_log_support);//点赞
                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                final TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);
                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("联系记录");
                        post.setDataId(item.getUuid());
                        if (item.isLike()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });


                if (item.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
//                    tv_support.setTextColor(getActivity().getResources().getColor(R.color.color_support_text_like));
                    tv_support.setTextColor(Color.parseColor("#01E0DF"));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
//                    tv_support.setTextColor(getActivity().getResources().getColor(R.color.color_support_text));
                    tv_support.setTextColor(Color.parseColor("#999999"));
                }
                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");

            }
        };
    }


    private void popWiw(final Contact item) {

        popWiw = new BaseSelectPopupWindow(getActivity(), R.layout.edit_data);
        // popWiw.setOpenKeyboard(true);
        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWiw.setFocusable(true);
        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWiw.setShowTitle(false);
        popWiw.setBackgroundDrawable(new ColorDrawable(0));
        InputMethodManager im = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
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
                        post.setToId(item.getCreatorId());
                        post.setDataType("联系记录");
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
                    post.setToId(item.getCreatorId());
                    post.setDataType("联系记录");
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
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_task_list, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Contact space) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
//        et_comment.setText("");
//        InputSoftHelper.hiddenSoftInput(getActivity(), et_comment);
//        ll_bottom.setVisibility(View.GONE);
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                space.setCommentNumber(space.getCommentNumber() + 1);
                adapter.notifyDataSetChanged();
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

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 点赞
     *
     * @param post
     * @param record
     */
    private void support(SupportAndCommentPost post, final Contact record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLike(true);
                adapter.notifyDataSetChanged();
//                ll_bottom.setVisibility(View.GONE);
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
     * 取消点赞
     *
     * @param post   要取消点赞的实体的ID
     * @param record
     */
    private void cancleSupport(SupportAndCommentPost post, final Contact record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLike(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setSoftHide();
    }
}
