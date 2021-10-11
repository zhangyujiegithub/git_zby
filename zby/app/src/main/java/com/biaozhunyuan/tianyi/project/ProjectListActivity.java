package com.biaozhunyuan.tianyi.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.CustomDayView;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.model.other.SearchModel;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.HolidayUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.models.SelectStageItem;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 项目列表
 */

public class ProjectListActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private CommanAdapter<Project> adapter;
    private BoeryunSearchView seachButton;
    private List<Project> projectList = new ArrayList<>();
    private ImageView ivAdd;
    boolean isCanNewProject = false;
    private DictionaryHelper helper;
    private String dictionaryNames = "";
    public static boolean isResume = false;
    private boolean isSelectProject = false;//是否是选择项目
    private Context context;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
        isResume = false;
        initView();
        initFilterTStagePop();
        initFilterTimePop();
        initData();
        getProjectList();
        getProjectNewMoudle();
        setOnTouchEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isResume) {
            seachButton.setOnCancleSearch();
            isResume = false;
        }
    }

    private void getProjectList() {
        ProgressDialogHelper.show(this);
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                List<Project> data = demand.data;
                for (Project contact : demand.data) {
                    try {
                        contact.setStageName(demand.getDictName(contact, "stage"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(data);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(data, false);
                    if (data != null && data.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;
//                }else {
//                    showShortToast("没有更多数据");
//                    lv.loadCompleted();
//                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                lv.onRefreshComplete();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private void getProjectNewMoudle() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.查询是否可直接新建 + "?id=ab77c83744464c30a32d15df68c8ef23";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    Project project = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Project.class);
                    if (project != null) {
                        if ("直接新建".equals(project.getValue())) {
                            isCanNewProject = true;
                            ivAdd.setVisibility(View.VISIBLE);
                        } else {
                            isCanNewProject = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private CommanAdapter<Project> getAdapter(List<Project> taskList) {
        return new CommanAdapter<Project>(taskList, this, R.layout.item_projectlist) {
            @Override
            public void convert(int position, final Project item, final BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, item.getName());
                viewHolder.setTextValue(R.id.tv_customer_stage, ViewHelper.getDateStringFormat(item.getCreateTime()));
                viewHolder.setTextValue(R.id.advisor_name, helper.getUserNameById(StrUtils.pareseNull(item.getAdvisorId())));
                viewHolder.setUserPhoto(R.id.circleImageView, StrUtils.pareseNull(item.getAdvisorId()));
//                TextView tv_lose = viewHolder.getView(R.id.tv_lose);

//                if(!"6".equals(item.getStatus())&&!"7".equals(item.getStatus())){
//                    if(!TextUtils.isEmpty(item.getAdvisorId())){
//                        if(!item.getAdvisorId().equals(Global.mUser.getUuid())){
//                            tv_lose.setVisibility(View.GONE);
//                        }else {
//                            tv_lose.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }else {
//                    tv_lose.setVisibility(View.GONE);
//                }

                if (!TextUtils.isEmpty(item.getStageName())) {
                    viewHolder.setTextValue(R.id.tv_customer_jieduan, item.getStageName());
                    viewHolder.getView(R.id.tv_customer_jieduan).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.tv_customer_jieduan).setVisibility(View.GONE);
                }
                viewHolder.setTextValue(R.id.tv_amount, item.getAmount());
                viewHolder.setTextValue(R.id.tv_address, item.getAddress());

                if (!TextUtils.isEmpty(item.getAdvisorId())) {
                    if (item.getAdvisorId().equals(Global.mUser.getUuid())) {
                        item.setCanNewSamplepaint(true);
                    } else {
                        item.setCanNewSamplepaint(false);
                    }
                } else {
                    if (Global.mUser.getUuid().equals(item.getCreatorId()))
                        item.setCanNewSamplepaint(false);
                }


//                tv_lose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
//                                "?workflowTemplateId=1d27ae4b89c94a42b66a632348652124&id=0&projectId=" + item.getUuid()
//                                + "&chanceCode=" + item.getChanceCode() + "&customerId=" + item.getCustomerId();
//                        Intent intent = new Intent(ProjectListActivity.this, FormInfoActivity.class);
//                        intent.putExtra("exturaUrl", url);
//                        intent.putExtra("projectId",item.getUuid());
//                        startActivity(intent);
//                    }
//                });
            }
        };
    }

    private void setOnTouchEvent() {
        seachButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                demand.fuzzySearch = str;
                demand.resetFuzzySearchField(true);
                pageIndex = 1;
                demand.dictionaryNames = dictionaryNames;
                getClientListByFilter();
            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                demand.resetFuzzySearchField(false);
                demand.dictionaryNames = "stage.crm_project_stage,advisorId.base_staff";
                pageIndex = 1;
                getProjectList();
            }

            @Override
            public void OnClick() {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    if (isSelectProject) { //选择项目
                        Intent intent = new Intent();
                        Project item = adapter.getItem(position - 1);
                        intent.putExtra("selectProject", item);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Intent intent = new Intent(ProjectListActivity.this, ProjectInfoActivity.class);
                        Project item = adapter.getItem(position - 1);
//                Project item = adapter.getItem(position - 1);
                        intent.putExtra("Project", item);
                        startActivity(intent);
                    }
                }
            }
        });
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                startActivity(new Intent(ProjectListActivity.this, CartogramMapActivity.class));
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
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getProjectList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getProjectList();
//                seachButton.setOnCancleSearch();
//                projectList.clear();
//                adapter.clearData();
//                adapter.notifyDataSetChanged();
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProjectListActivity.this, ProjectAddActivity.class));
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

    private void initData() {
        demand = new Demand<>(Project.class);
        demand.pageSize = 10;
        demand.pageIndex = pageIndex;
        demand.dictionaryNames = "stage.crm_project_stage,advisorId.base_staff";
        demand.sortField = "createTime desc";
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.项目列表;
//        demand.setFuzzySearch("crm_project");
        getSearhFields("crm_project");
    }

    private void initView() {
        context = ProjectListActivity.this;
        headerview = findViewById(R.id.boeryun_headerview);
        lv = findViewById(R.id.lv);
        ivAdd = findViewById(R.id.iv_add_relate);
        seachButton = findViewById(R.id.seach_button);

        rlFilterStage = findViewById(R.id.rl_filter_client_stage);
        rlFilterTime = findViewById(R.id.rl_filter_client_time);
        ivFilterTime = findViewById(R.id.iv_filter_time);
        ivFilterStage = findViewById(R.id.iv_filter_stage);
        tvFilterStage = findViewById(R.id.tv_filter_stage);
        tvFilterTime = findViewById(R.id.tv_filter_time);

        if (Global.CURRENT_CROP_NAME.equals("莱恩斯")) {
            headerview.setRightTitleVisible(true);
            headerview.setRightTitle("统计");
        }
        helper = new DictionaryHelper(this);
        if (getIntent().getExtras() != null) {
            isSelectProject = getIntent().getBooleanExtra("isSelectProject", false);
        }

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

    private void getSearhFields(String tableName) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.模糊搜索 + "?tableName=" + tableName;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SearchModel> searchModels = JsonUtils.jsonToArrayEntity
                        (JsonUtils.pareseData(response), SearchModel.class);
                if (searchModels != null && searchModels.size() > 0) {
                    demand.keyMap = new HashMap<>();
                    String dictionaryName = "";
                    for (SearchModel searchModel : searchModels) {
                        if ("3".equals(searchModel.getInputType())) {
                            demand.keyMap.put("searchField_bool_" + searchModel.getFieldName(), "");
                        } else if ("4".equals(searchModel.getInputType())) {
                            demand.keyMap.put("searchField_datetime_" + searchModel.getFieldName(), "");
                        } else if (("6".equals(searchModel.getInputType())
                                || "8".equals(searchModel.getInputType()))
                                && !TextUtils.isEmpty(searchModel.getDictionary())) {
                            demand.keyMap.put("searchField_dictionary_" + searchModel.getFieldName(), "");

                            dictionaryName += searchModel.getFieldName() + "." + searchModel.getDictionary() + ",";
                        } else {
                            demand.keyMap.put("searchField_string_" + searchModel.getFieldName(), "");
                        }
                    }
                    if (dictionaryName.length() > 0) {
                        dictionaryNames = dictionaryName.substring(0, dictionaryName.length() - 1);
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
                    if ("crm_project_stage".equals(dictTableName)) {
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
        if ("全部".equals(currentStage.getName())) {
            map.put("searchField_dictionary_stage", "");
        } else {
            map.put("searchField_dictionary_stage", currentStage.getUuid());
        }
        if ("全部".equals(currentSource.getName())) {
            map.put("searchField_dictionary_source", "");
        } else {
            map.put("searchField_dictionary_source", "1|" + currentSource.getUuid());
        }

        if (!TextUtils.isEmpty(demand.fuzzySearch)) {
            map.put("searchField_string_name", demand.fuzzySearch);
        }
        if (adapter != null) {
            adapter.clearData();
        }
        demand.keyMap = map;
        pageIndex = 1;
        getProjectList();
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
                        getCustomDicts("crm_project_stage");
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

        tvContactTime.setVisibility(View.GONE);

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
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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


    @Override
    public void onPause() {
        super.onPause();
        seachButton.setOnCancleSearch(false);
    }
}
