package com.biaozhunyuan.tianyi.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.CustomDayView;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.model.other.SearchModel;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.HolidayUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.utils.ToastUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.contact.ContactNewActivity;
import com.biaozhunyuan.tianyi.models.SelectStageItem;
import com.biaozhunyuan.tianyi.models.SelectStringItem;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

import static android.app.Activity.RESULT_OK;
import static com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity.RESULT_SELECT_USER;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_PARTICIPANT;

/**
 * 客户列表 :客户
 */
@SuppressLint("ValidFragment")
public class ClientlistCustomerFragment extends LazyFragment {

    private Demand demand = new Demand();
    private String dictionaryNames;
    private Context context;
    private int pageIndex = 1; //当前页
    private int REQUEST_CODE_FENPEI_CLIENT = 88;//分配客户的requestCode
    private CommanAdapter<Client> adapter;
    //    private CommanAdapter<Contact> menuAdapter;
    private BoeryunSearchView searchView;
    private NoScrollListView lv;
    private DictionaryHelper helper;
    private Set<Integer> mSelectPosSet = new HashSet<Integer>();
    private TagFlowLayout flowlayout;
    private ArrayList<String> datas = new ArrayList();
    private List<String> phoneList;
    private int fragmentID = 0;
    private AutoMaxHeightViewpager vp;
    private View v;
    private List<Client> list;
    private DictIosPickerBottomDialog bottomDialog;
    private TagAdapter<String> tagAdapter;
    private String shareClientIds = ""; //共享客户id
    private String currentCustomerId = ""; //当前选中客户id
    public static boolean isResume = false;
    private User mUser = Global.mUser;
    private Boolean isBelowPhoneCanVisibile; //下属客户电话是否可见
    private boolean isCanFenPeiClient = false; //是否有分配客户的权限


    /**
     * 过滤
     */
    private LinearLayout llFilter;
    private RelativeLayout rlFilterType;
    private TextView tvFilterType;
    private ImageView ivFilterType;
    private RelativeLayout rlFilterStage;
    private TextView tvFilterStage;
    private ImageView ivFilterStage;
    private RelativeLayout rlFilterTime;
    private ImageView ivFilterTime;
    private TextView tvFilterTime;

    private PopupWindow popFilterType;
    private PopupWindow popFilterStage;
    private PopupWindow popFilterTime;

    private CommanAdapter<SelectStringItem> typeAdapter;
    private CommanAdapter<SelectStageItem> stageAdapter;
    private List<SelectStringItem> typeList;
    private List<SelectStageItem> stageList;
    private List<SelectStageItem> sourceList;
    private int filterGroupPos = 0;
    private String[] filterTitles = new String[]{"阶段", "客户来源"};

    private SelectStageItem currentStage;
    private SelectStageItem currentSource;
    private 动态表单ViewModel tableGrid;
    private String customerTypeName;
    private BaseSelectPopupWindow calendarPop;//选择日期的popupwindow
    private CalendarViewAdapter calendarAdapter;

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


    public ClientlistCustomerFragment(AutoMaxHeightViewpager vp, int fragmentID) {
        this.vp = vp;
        this.fragmentID = fragmentID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_client_client, null);
        initView(v);
        initData();
        initFilterTypePop();
        initFilterTimePop();
        initFilterTStagePop();
        getSearhFields("crm_customer");
        initDemand();
        vp.setObjectForPosition(v, fragmentID);
        ProgressDialogHelper.show(getActivity());
        getClientList(null);
        setOnEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isResume) {
            pageIndex = 1;
            ProgressDialogHelper.show(getActivity());
            getClientList(null);
            isResume = false;
        }
    }

    private void initData() {
        context = getActivity();
        helper = new DictionaryHelper(context);
        bottomDialog = new DictIosPickerBottomDialog(context);
        flowlayout.setMaxSelectCount(1);

        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        datas.add("全部客户");
        datas.add("跟进中的客户");
        datas.add("10天未联系客户");
        datas.add("本月新客户");
        datas.add("计划联系客户");
        datas.add("最近联系客户");
        datas.add("最近生日客户");
        datas.add("我关注的");
        datas.add("我的客户");
        typeList = new ArrayList<>();
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
        for (String data : datas) {
            typeList.add(new SelectStringItem(data));
        }


        tagAdapter = new TagAdapter<String>(datas) {
            @Override
            public View getView(FlowLayout parent, int position, String t) {
                Logger.i("tagA" + position + "--" + t);
                TextView tv = (TextView) mInflater.inflate(
                        R.layout.item_label_customer_list, flowlayout, false);
                tv.setText(t);
                return tv;
            }
        };
        flowlayout.setAdapter(tagAdapter);
        tagAdapter.setSelectedList(0);

        String jurisdictionList = PreferceManager.getInsance().getValueBYkey("JurisdictionList");
        if (!TextUtils.isEmpty(jurisdictionList)
                && jurisdictionList.contains("批量分配客户")) { //是否有分配客户的权限
            isCanFenPeiClient = true;
        }
    }

    private void initDemand() {
        customerTypeName = "跟进中的客户"; //默认显示跟进中的客户
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户列表 + "?customerStatus=0&isFocused=0";// + Global.mUser.getUuid() + "&flag=1";
        demand.pageSize = 10;
//        demand.sortField = "IFNULL(a.lastUpdateTime,IFNULL(a.lastContactTime,a.createTime)) desc";
        demand.dictionaryNames = "customerType.dict_customer_type,passportTypeId.dict_user_passtype,source.dict_customer_source,industry.dict_customer_industry,advisor.base_staff";
        demand.src = url;
//        demand.setFuzzySearch("crm_customer");
    }


    /**
     * 获取是否下属客户电话可见
     */
    private void getIsBelowPhoneCanVisibile() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业CRM配置 + "?name=下属客户电话是否可见";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String result = JsonUtils.getStringValue(JsonUtils.pareseData(response), "value");
                    if (!TextUtils.isEmpty(result)) {
                        if ("是".equals(result)) {
                            isBelowPhoneCanVisibile = true;
                        } else {
                            isBelowPhoneCanVisibile = false;
                        }
                    } else {
                        isBelowPhoneCanVisibile = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isBelowPhoneCanVisibile = true;
                }
                getClientList(null);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                isBelowPhoneCanVisibile = true;
                getClientList(null);
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

    private void setOnEvent() {
        flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                demand.resetFuzzySearchField(false);
                mSelectPosSet = selectPosSet;
                String customerType = "";
                for (Integer pos : mSelectPosSet) {
                    customerType = datas.get(pos);
                }
                getClientListByFilter(customerType);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((ClientListActivity) getActivity()).closeDrawerLayout();
                Intent intent = new Intent(context, CustomerDetailsActivity.class);
                intent.putExtra(CustomerDetailsActivity.EXTRA_CLIENT, adapter.getDataList().get(position));
                Client item = adapter.getItem(position);
                if (!isBelowPhoneCanVisibile && !Global.mUser.getUuid().equals(item.getAdvisorId()) && !item.getShare()) {
                    intent.putExtra("isCanShowPhone", false);
                }
                startActivity(intent);
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                ProgressDialogHelper.show(context);
                demand.resetFuzzySearchField(true);
                pageIndex = 1;
                demand.fuzzySearch = str;
                demand.dictionaryNames = dictionaryNames;
                getClientList(null);
            }
        });
        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                ProgressDialogHelper.show(context);
                pageIndex = 1;
                demand.sortField = "IFNULL(a.lastUpdateTime,IFNULL(a.lastContactTime,a.createTime)) desc";
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表;
                demand.customerType = "";
                demand.dictionaryNames = "customerType.dict_customer_type,passportTypeId.dict_user_passtype,source.dict_customer_source,industry.dict_customer_industry,advisor.base_staff";
                demand.resetFuzzySearchField(false);
                getClientListByFilter(customerTypeName);
//                getClientList(null);
            }

            @Override
            public void OnClick() {

            }
        });

        rlFilterType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popFilterType.isShowing()) {
                    if (popFilterStage != null && popFilterStage.isShowing()) {
                        tvFilterStage.setTextColor(getResources().getColor(R.color.text_black));
                        ivFilterStage.setImageResource(R.drawable.icon_arrow_down_black);
                        popFilterStage.dismiss();
                    }
                    if (popFilterTime.isShowing()) {
                        ivFilterTime.setImageResource(R.drawable.icon_arrow_down_black);
                        popFilterTime.dismiss();
                    }
                    tvFilterType.setTextColor(getResources().getColor(R.color.hanyaRed));
                    ivFilterType.setImageResource(R.drawable.icon_arrow_up_blue);
                    popFilterType.showAsDropDown(rlFilterType);

                } else {
                    tvFilterType.setTextColor(getResources().getColor(R.color.text_black));
                    ivFilterType.setImageResource(R.drawable.icon_arrow_down_black);
                    popFilterType.dismiss();
                }

            }
        });

        rlFilterStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popFilterStage == null)
                    return;
                if (!popFilterStage.isShowing()) {
                    if (popFilterType.isShowing()) {
                        tvFilterType.setTextColor(getResources().getColor(R.color.text_black));
                        ivFilterType.setImageResource(R.drawable.icon_arrow_down_black);
                        popFilterType.dismiss();
                    }
                    if (popFilterTime.isShowing()) {
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
                    if (popFilterType.isShowing()) {
                        tvFilterType.setTextColor(getResources().getColor(R.color.text_black));
                        ivFilterType.setImageResource(R.drawable.icon_arrow_down_black);
                        popFilterType.dismiss();
                    }
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

        popFilterTime.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvFilterTime.setTextColor(getResources().getColor(R.color.text_black));
                ivFilterTime.setImageResource(R.drawable.icon_arrow_down_black);
            }
        });
    }

    private void getClientListByFilter(String customerType) {
        customerTypeName = customerType;
        if (!TextUtils.isEmpty(customerType)) {
            demand.sortField = "IFNULL(a.lastUpdateTime,IFNULL(a.lastContactTime,a.createTime)) desc";
            if (customerType.equals("全部客户")) {
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表;
                demand.customerType = "";
            } else if (customerType.equals("跟进中的客户")) {
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表 + "?customerStatus=0&isFocused=0";
                demand.customerType = "";
            } else if (customerType.equals("我关注的")) {
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表 + "?isFocused=1";
                demand.customerType = "";
            } else if (customerType.equals("我的客户")) {
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表 + "?isFocused=0&customerBelong=myCustomer";
                demand.customerType = "";
            } else if ("本月新客户".equals(customerType)) {
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表;
                demand.customerType = "";

                String thisMonthFirstDay = ViewHelper.formatDateToStr1(ViewHelper.getFirstDateOfThisMonth());
                String today = ViewHelper.getCurrentFullTime();
                String createTimeStr = "startCreateTime=" + thisMonthFirstDay + " 00:00:00"
                        + "&finishCreateTime=" + today;
                demand.src += "?" + createTimeStr;
            } else {
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户分类;
                demand.customerType = customerType;
            }
        }
        if (!TextUtils.isEmpty(selectCreateTime)) {
            if (!"不限".equals(selectCreateTime)) { //不是不限，要添加过滤条件
                String createTimeStr = "";
                if ("今天".equals(selectCreateTime)) {
                    createTimeStr = "createTime=" + ViewHelper.getDateToday();
                } else if ("昨天".equals(selectCreateTime)) {
                    createTimeStr = "createTime=" + ViewHelper.getDateYestoday();
                } else if ("最近7天".equals(selectCreateTime)) {
                    ArrayList<String> totalDaysList = ViewHelper.getTotalDaysList(7);
                    String startTime = totalDaysList.get(0);
                    String endTime = totalDaysList.get(totalDaysList.size() - 1);
                    createTimeStr = "startCreateTime=" + startTime
                            + "&finishCreateTime=" + endTime;
                } else if ("自定义".equals(selectCreateTime)) {
                    createTimeStr = "startCreateTime=" + selectCreateStartTime
                            + "&finishCreateTime=" + selectCreateEndTime;
                }
                if (demand.src.contains("?")) {
                    demand.src += "&" + createTimeStr;
                } else {
                    demand.src += "?" + createTimeStr;
                }
            }
        }
        if (!TextUtils.isEmpty(selectContactTime)) {
            if (!"不限".equals(selectContactTime)) { //不是不限，要添加过滤条件
                String createTimeStr = "";
                if ("今天".equals(selectContactTime)) {
                    createTimeStr = "contactTime=" + ViewHelper.getDateToday();
                } else if ("昨天".equals(selectContactTime)) {
                    createTimeStr = "contactTime=" + ViewHelper.getDateYestoday();
                } else if ("最近7天".equals(selectContactTime)) {
                    ArrayList<String> totalDaysList = ViewHelper.getTotalDaysList(7);
                    String startTime = totalDaysList.get(0);
                    String endTime = totalDaysList.get(totalDaysList.size() - 1);
                    createTimeStr = "startContactTime=" + startTime
                            + "&finishContactTime=" + endTime;
                } else if ("自定义".equals(selectContactTime)) {
                    createTimeStr = "startContactTime=" + selectContactStartTime
                            + "&finishContactTime=" + selectContactEndTime;
                }
                if (demand.src.contains("?")) {
                    demand.src += "&" + createTimeStr;
                } else {
                    demand.src += "?" + createTimeStr;
                }
            }
        }
        if (adapter != null) {
            adapter.clearData();
        }
        pageIndex = 1;
        getClientList(null);
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        searchView = v.findViewById(R.id.seach_button);
        flowlayout = v.findViewById(R.id.tgflowlayout_multi_picker);
        llFilter = v.findViewById(R.id.ll_filter);
        llFilter.setVisibility(View.VISIBLE);

        rlFilterType = v.findViewById(R.id.rl_filter_client_type);
        tvFilterType = v.findViewById(R.id.tv_filter_type);
        ivFilterType = v.findViewById(R.id.iv_filter_type);
        ivFilterStage = v.findViewById(R.id.iv_filter_stage);
        tvFilterStage = v.findViewById(R.id.tv_filter_stage);
        tvFilterTime = v.findViewById(R.id.tv_filter_time);
        rlFilterStage = v.findViewById(R.id.rl_filter_client_stage);
        rlFilterTime = v.findViewById(R.id.rl_filter_client_time);
        ivFilterTime = v.findViewById(R.id.iv_filter_time);
    }

    private void initFilterTypePop() {
        View view = View.inflate(context, R.layout.pop_list_item, null);
        popFilterType = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popFilterType.setOutsideTouchable(false);
        popFilterType.setAnimationStyle(R.style.PopupWindowAnimation);
        NoScrollListView lv = view.findViewById(R.id.lisview);
        View bgView = view.findViewById(R.id.bg_view);
        typeList.get(1).setSelect(true);
        typeAdapter = new CommanAdapter<SelectStringItem>(typeList, context, R.layout.item_pop_client_filter) {
            @Override
            public void convert(int position, SelectStringItem item, BoeryunViewHolder viewHolder) {
                TextView tvName = viewHolder.getView(R.id.textView);
                tvName.setText(item.getName());
                if (item.isSelect()) {
                    tvName.setTextColor(getResources().getColor(R.color.hanyaRed));
                    viewHolder.getView(R.id.iv_select).setVisibility(View.VISIBLE);
                } else {
                    tvName.setTextColor(getResources().getColor(R.color.text_black));
                    viewHolder.getView(R.id.iv_select).setVisibility(View.GONE);
                }

            }
        };

        lv.setAdapter(typeAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (SelectStringItem item : typeList) {
                    item.setSelect(false);
                }
                //防止本月新客户和时间选择冲突，本月新客户的时候置空时间选择类型
                if ("本月新客户".equals(typeList.get(position).getName())) {
                    selectCreateTime = "";
                }
                typeList.get(position).setSelect(true);
                tvFilterType.setText(typeList.get(position).getName());
                getClientListByFilter(typeList.get(position).getName());
                typeAdapter.notifyDataSetChanged();
                tvFilterType.setTextColor(getResources().getColor(R.color.text_black));
                ivFilterType.setImageResource(R.drawable.icon_arrow_down_black);
                popFilterType.dismiss();
            }
        });

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvFilterType.setTextColor(getResources().getColor(R.color.text_black));
                ivFilterType.setImageResource(R.drawable.icon_arrow_down_black);
                popFilterType.dismiss();
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
                pageIndex = 1;
                getClientList(null);
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
                getClientListByFilter(customerTypeName);
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
                getClientListByFilter(customerTypeName);
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
                getClientListByFilter(customerTypeName);
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
                getClientListByFilter(customerTypeName);
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
                        getClientListByFilter(customerTypeName);
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

    public void loadMoreData(RefreshLayout refreshLayout) {
        getClientList(refreshLayout);
    }

    public void refreshData(RefreshLayout refreshLayout) {
        pageIndex = 1;
//        searchView.setOnCancleSearch();
        demand.resetFuzzySearchField(false);
        getClientList(refreshLayout);
//        tagAdapter.setSelectedList(0);
    }

    /**
     * 获取客户列表
     */
    private void getClientList(final RefreshLayout refreshLayout) {
        if (isBelowPhoneCanVisibile == null) {
            //先获取是否下属客户电话可见
            getIsBelowPhoneCanVisibile();
            return;
        }
        demand.pageIndex = pageIndex;
        Map<String, String> map = demand.keyMap;
        if (map == null) {
            map = new HashMap<>();
        }
        if (demand.src.contains(GlobalMethord.客户列表)) {
            if ("全部".equals(currentStage.getName())) {
                map.put("stages", "");
            } else {
                map.put("stages", currentStage.getUuid());
            }
            if ("全部".equals(currentSource.getName())) {
                map.put("searchField_dictionary_source", "");
            } else {
                map.put("searchField_dictionary_source", currentSource.getUuid());
            }
        } else {
            map.put("type", "undefined");
            if ("全部".equals(currentStage.getName())) {
                map.put("searchField_dictionary_contactStage", "");
            } else {
                map.put("searchField_dictionary_contactStage", currentStage.getUuid());
            }
        }
        demand.keyMap = map;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Client.class);
                    if (list != null) {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.addBottom(list, false);
                            if (list != null && list.size() == 0) {
                                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                            if (refreshLayout != null) {
                                refreshLayout.finishLoadMore();
                            }
                        }
                        pageIndex += 1;
                        vp.resetHeight(fragmentID);
                    } else {
                        if (adapter != null) {
                            adapter.clearData();
                        }
                    }
                    ProgressDialogHelper.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (adapter != null) {
                        adapter.clearData();
                    }
                    ProgressDialogHelper.dismiss();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
//                lv.onRefreshComplete();
                if (adapter != null) {
                    adapter.clearData();
                }
            }
        });

    }

    private CommanAdapter<Client> getAdapter(final List<Client> list) {
        return new CommanAdapter<Client>(list, context, R.layout.item_new_client) {
            @Override
            public void convert(int position, final Client item, BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setUserPhoto(R.id.circleImageView, item.getAdvisorId());//理财师头像
                    viewHolder.setTextValue(R.id.advisor_name, helper.getUserNameById(item.getAdvisorId())); //理财师
                    viewHolder.setTextValue(R.id.client_name, item.getName()); //客户名称
                    if (!isBelowPhoneCanVisibile && !Global.mUser.getUuid().equals(item.getAdvisorId()) && !item.getShare()) {
                        //如果下属客户电话不可见  并且  不是登录用户的客户    并且  不是共享客户，设置手机号为星号
                        viewHolder.setTextValue(R.id.contact_number, "******"); //客户手机号
                        viewHolder.setTextValue(R.id.contact_name, "***"); //客户联系人
                    } else {
                        viewHolder.setTextValue(R.id.contact_name, TextUtils.isEmpty(item.getContact()) ? "无" : item.getContact()); //客户联系人
                        viewHolder.setTextValue(R.id.contact_number, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile()); //客户手机号
                    }
                    if (mUser.getCorpId().equals("1139")) {
                        float fwidth = 15;
                        ImageView balance = viewHolder.getView(R.id.imageView5);
                        ViewGroup.LayoutParams lp = balance.getLayoutParams();
                        lp.width = (int) ViewHelper.dip2px(context, fwidth);
                        lp.height = (int) ViewHelper.dip2px(context, fwidth);
                        balance.setLayoutParams(lp);
                        balance.setImageResource(R.drawable.icon_client_balance);
                        viewHolder.setTextValue(R.id.contact_location, TextUtils.isEmpty(item.getBalance()) ? "无" : item.getBalance()); //金额
                    } else {
                        viewHolder.setTextValue(R.id.contact_location, TextUtils.isEmpty(item.getAddress()) ? "无" : item.getAddress()); //客户地址
                    }

                    if (StrUtils.pareseNull(item.getAdvisor()).equals(Global.mUser.getUuid())) {
                        viewHolder.getView(R.id.imageView14).setVisibility(View.VISIBLE);
                        viewHolder.getView(R.id.imageView15).setVisibility(View.VISIBLE);
                        viewHolder.getView(R.id.imageView17).setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.getView(R.id.imageView14).setVisibility(View.GONE);
                        viewHolder.getView(R.id.imageView15).setVisibility(View.GONE);
                        viewHolder.getView(R.id.imageView17).setVisibility(View.GONE);
                    }

                    if (isCanFenPeiClient) {
                        viewHolder.getView(R.id.imageView11).setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.getView(R.id.imageView11).setVisibility(View.GONE);
                    }

                    viewHolder.getView(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //打电话
                            TextView tvNumber = viewHolder.getView(R.id.contact_number);
                            if ("******".equals(tvNumber.getText().toString())) {
                                ToastUtils.showShort("只能给自己的客户拨打电话");
                                return;
                            }
                            String url = Global.BASE_JAVA_URL + GlobalMethord.获取客户联系方式 + "?customerId=" + item.getUuid();
                            StringRequest.getAsyn(url, new StringResponseCallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        List<Client> data = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Client.class);
                                        if (data.size() > 0) {
                                            phoneList = new ArrayList<>();
                                            if (!TextUtils.isEmpty(item.getMobile())) {
                                                phoneList.add(item.getMobile());
                                            }
                                            if (!TextUtils.isEmpty(item.getPhone())) {
                                                phoneList.add(item.getPhone());
                                            }
                                            for (Client client : data) {
                                                phoneList.add(client.getPhone());
                                            }
                                            bottomDialog.show(phoneList);
                                            bottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                                @Override
                                                public void onSelected(int index) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_DIAL);
                                                    intent.setData(Uri.parse("tel:" + phoneList.get(index)));
                                                    startActivity(intent);
                                                    ((ClientListActivity) getActivity()).closeDrawerLayout();
                                                    addRecord(item);
                                                }
                                            });
                                        } else {
                                            if (!TextUtils.isEmpty(item.getMobile())) {
                                                if (!TextUtils.isEmpty(item.getPhone())) {
                                                    phoneList = new ArrayList<>();
                                                    phoneList.add(item.getPhone());
                                                    phoneList.add(item.getMobile());
                                                    bottomDialog.show(phoneList);
                                                    bottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                                        @Override
                                                        public void onSelected(int index) {
                                                            Intent intent = new Intent();
                                                            intent.setAction(Intent.ACTION_DIAL);
                                                            intent.setData(Uri.parse("tel:" + phoneList.get(index)));
                                                            startActivity(intent);
                                                            ((ClientListActivity) getActivity()).closeDrawerLayout();
                                                            addRecord(item);
                                                        }
                                                    });
                                                } else {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_DIAL);
                                                    intent.setData(Uri.parse("tel:" + item.getMobile()));
                                                    startActivity(intent);
                                                    ((ClientListActivity) getActivity()).closeDrawerLayout();
                                                    addRecord(item);
                                                }
                                            } else {
                                                Toast.makeText(context, "该客户没有录入联系手机号码", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (JSONException e) {
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
                    });
                    viewHolder.getView(R.id.imageView15).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = Global.BASE_JAVA_URL + GlobalMethord.客户已共享列表 + item.getUuid();
                            StringRequest.getAsyn(url, new StringResponseCallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        currentCustomerId = item.getUuid();
                                        ShareClient shareClient = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), ShareClient.class);
                                        Intent intent = new Intent(context, SelectedNotifierActivity.class);
                                        intent.putExtra("isSingleSelect", false);
                                        intent.putExtra("title", "共享客户");
                                        intent.putExtra("select_text", "已共享:");
                                        intent.putExtra("selectedAdvisorIds", shareClient.getIds());
                                        intent.putExtra("selectedAdvisorNames", shareClient.getNames());
                                        startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                                    } catch (ParseException e) {
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
                    });


                    viewHolder.getView(R.id.imageView11).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //分配客户
                            currentCustomerId = item.getUuid();
                            Intent intent = new Intent(context, SelectedNotifierActivity.class);
                            intent.putExtra("isSingleSelect", true);
                            intent.putExtra("title", "分配客户");
                            intent.putExtra("select_text", "已共享:");
                            startActivityForResult(intent, REQUEST_CODE_FENPEI_CLIENT);
                        }
                    });

                    viewHolder.getView(R.id.imageView14).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //新建跟进记录
                            ((ClientListActivity) getActivity()).closeDrawerLayout();
                            Intent intent = new Intent(context,
                                    AddRecordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(ContactNewActivity.EXTRA_CLIENT_NAME, TextUtils.isEmpty(item.getName()) ? "--" : item.getName());
                            bundle.putString(ContactNewActivity.EXTRA_CLIENT_ID, item.getUuid());
                            bundle.putString("advisorId", item.getAdvisor());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    viewHolder.getView(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((ClientListActivity) getActivity()).openDrawerLayout(item);
                        }
                    });

                    viewHolder.getView(R.id.imageView17).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog dialog = new AlertDialog(getActivity());
                            dialog
                                    .builder()
                                    .setTitle("提示")
                                    .setMsg("确定将客户放入公海吗")
                                    .setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    })
                                    .setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            putCustomerInPublic(item.getUuid());
                                        }
                                    });
                            dialog.show();
                        }
                    });


                    if (position == list.size() - 1) {
                        ProgressDialogHelper.dismiss();
                    }
                } else {
                    viewHolder.setUserPhoto(R.id.circleImageView, Global.mUser.getUuid());//理财师头像
                }
            }
        };
    }

    private void putCustomerInPublic(String uuid) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.将客户放入公海;

        Map<String, String> map = new HashMap<>();
        map.put("ids", uuid);

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort("放入公海成功");
                refreshData(null);
                EventBus.getDefault().postSticky("放入公海成功");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ToastUtils.showShort(JsonUtils.pareseMessage(result));
            }
        });
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
                    if (!TextUtils.isEmpty(dictionaryName)) {
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

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            vp.resetHeight(fragmentID);
        } else {
            searchView.setSoftHide();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setSoftHide();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_SELECT_PARTICIPANT) {
            Bundle bundle1 = data.getExtras();
            UserList userList1 = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
            shareClientIds = "";
            if (userList1 != null) {
                List<User> users = userList1.getUsers();
                for (User user : users) {
                    shareClientIds += user.getUuid() + ",";
                }
                if (shareClientIds.length() > 0) {
                    shareClientIds = shareClientIds.substring(0, shareClientIds.length() - 1);
                }
            }
            JSONObject js = new JSONObject();
            try {
                js.put("customerIds", currentCustomerId);
                js.put("staffIds", shareClientIds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.共享客户, js, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Request request, Exception ex) {

                }

                @Override
                public void onResponseCodeErro(String result) {
                    Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FENPEI_CLIENT) { //分配客户
            Bundle bundle1 = data.getExtras();
            UserList userList1 = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
            if (userList1 != null) {
                List<User> users = userList1.getUsers();
                if (users.size() > 0) {
                    User user = users.get(0);
                    isCustomerDistributable(user.getUuid());
                }
            }
        }
    }


    /**
     * 拨打电话的同时生成一条联系记录
     */
    private void addRecord(Client client) {
        getTableGrid(client);
    }

    /**
     * 获取动态字段
     *
     * @param client
     */
    private void getTableGrid(Client client) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.CRM动态字段;
        Map<String, String> map = new HashMap<>();
        map.put("type", "crm_contact");
        map.put("id", client.getUuid());


        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                try {
                    tableGrid = JsonUtils.jsonToEntity(
                            result, 动态表单ViewModel.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (tableGrid != null && tableGrid.表单字段s != null
                        && tableGrid.表单字段s.size() > 0) {
                    // 根据分类Tab
                    for (表单字段 form : tableGrid.表单字段s) {
                        if (form.Name.equals("createTime")) {
                            form.Value = ViewHelper.getCurrentFullTime();
                            break;
                        }
                    }
                }
                saveTableGrid(client);
            }
        });
    }

    private void saveTableGrid(Client client) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.保存动态字段;

        StringRequest.postAsynNoMap(url, "crm_contact", tableGrid.表单字段s, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.getStringValue(response, "Data");
                    saveRecord(client, data);
                } catch (JSONException e) {
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


    private void saveRecord(Client client, String uuid) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.添加跟进记录;

        Contact contact = new Contact();
        contact.setUuid(uuid);
        contact.setAdvisorId(Global.mUser.getUuid());
        contact.setContactTime(ViewHelper.getCurrentFullTime());
        contact.setStage(TextUtils.isEmpty(client.getContactStage()) ? "1" : client.getContactStage());
        contact.setContactWay("3");
        contact.setCustomerId(client.getUuid());
        contact.setContent("在" + ViewHelper.getCurrentFullTime() + "打电话给客户" + client.getName());

        StringRequest.postAsyn(url, contact, new StringResponseCallBack() {
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
     * 判断客户是否可以被分配
     */
    private void isCustomerDistributable(String toAdvisorId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.判断公海客户是否可被分配;

        Map<String, String> map = new HashMap<>();
        map.put("customerNumberToAdd", "1");
        map.put("userId", Global.mUser.getUuid());

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                fenPeiClient(toAdvisorId);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ToastUtils.showShort(JsonUtils.pareseMessage(result));
            }
        });
    }


    /**
     * 分配客户
     */
    private void fenPeiClient(String toAdvisorId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.分配客户;

        Map<String, String> map = new HashMap<>();
        map.put("customerIds", currentCustomerId);
        map.put("toAdvisorId", toAdvisorId);

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort("分配成功");
                refreshData(null);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ToastUtils.showShort(JsonUtils.pareseMessage(result));
            }
        });
    }
}