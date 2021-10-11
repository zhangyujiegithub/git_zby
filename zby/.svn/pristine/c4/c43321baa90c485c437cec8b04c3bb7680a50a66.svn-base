package com.biaozhunyuan.tianyi.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * 商机列表
 */
public class BusinessListActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private BoeryunSearchView seachButton;
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Business> demand;
    private int pageIndex = 1;
    private CommanAdapter<Business> adapter;
    private Context context;
    private DictionaryHelper dictionaryHelper;
    public static boolean isResume = false;
    boolean isCanNewBusiness = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);
        initView();
        initData();
        getBusinessNewMoudle();
        getBusinessList();
        setOnTouchEvent();
    }

    private void getBusinessNewMoudle() {
        String permission = PreferceManager.getInsance().getValueBYkey("JurisdictionList");
        if (permission.contains("新建商机")) {
            headerview.setRightIconVisible(true);
        }else {
            headerview.setRightIconVisible(false);
        }
        /*String url = Global.BASE_JAVA_URL + GlobalMethord.查询是否可直接新建 + "?id=ab77c83744464c30a32d15df68c8ef45";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    Project project = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Project.class);
                    if (project != null && project.getValue().equals("否")) {
                        isCanNewBusiness = false;
                    }
                    if (isCanNewBusiness) {
                        headerview.setRightIconVisible(true);
                    } else {
                        headerview.setRightIconVisible(false);
                    }
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
        });*/

    }

    @Override
    protected void onResume() {
        if (isResume) {
            lv.startRefresh();
            seachButton.setOnCancleSearch();
        }
        super.onResume();
    }

    private void setOnTouchEvent() {
        /**
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getBusinessList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBusinessList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(BusinessListActivity.this, BusinessInfoActivity.class);
                    Business item = adapter.getItem(position - 1);
                    item.setCanSave((item.getCustomerId() == null || item.getProjectId() == null)
                            && Global.mUser.getUuid().equals(item.getAdvisorId()));
                    intent.putExtra("Business", item);
                    startActivity(intent);
                }

            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                pageIndex = 1;
                demand.resetFuzzySearchField(false);
                getBusinessList();
            }

            @Override
            public void OnClick() {

            }
        });
        seachButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
                demand.fuzzySearch = str;
                demand.resetFuzzySearchField(true);
                getBusinessList();
            }
        });

        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {
                startActivity(new Intent(BusinessListActivity.this, BusinessAddActivity.class));
            }
        });
    }

    private void getBusinessList() {
        ProgressDialogHelper.show(this);
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Business> data = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Business.class);
                    for (final Business task : data) {
                        try {
                            task.setSourceName(demand.getDictName(task, "source"));
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

    private void initData() {
        context = BusinessListActivity.this;
        dictionaryHelper = new DictionaryHelper(context);

        demand = new Demand();
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        demand.dictionaryNames = "source.dict_sale_chance_source";
        demand.sortField = "code desc";
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.商机列表;
        demand.setFuzzySearch("crm_sale_chance");
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        seachButton = findViewById(R.id.seach_button);
        lv = findViewById(R.id.lv);
    }

    private CommanAdapter<Business> getAdapter(List<Business> taskList) {
        return new CommanAdapter<Business>(taskList, this, R.layout.item_business_list) {
            @Override
            public void convert(int position, final Business item, final BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setTextValue(R.id.tv_business_time, item.getCreateTime() != null
                            ? ViewHelper.turnDate(item.getCreateTime(),
                            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm") : "");
                    viewHolder.setTextValue(R.id.tv_customer_name, dictionaryHelper.getUserNameById(item.getAdvisorId()));
                    viewHolder.setTextValue(R.id.tv_business_number, item.getAddress());
                    viewHolder.setTextValue(R.id.tv_business_customer, item.getContactPer());
                    viewHolder.setTextValue(R.id.tv_business_come, item.getSourceName());
                    viewHolder.setTextValue(R.id.tv_business_source, item.getName());
                    TextView btn = viewHolder.getView(R.id.btn_turn_project);

                    if ((item.getCustomerId() == null || item.getProjectId() == null)
                            && Global.mUser.getUuid().equals(item.getAdvisorId())) {
                        btn.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.GONE);
                    }
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StringRequest.getAsyn(Global.BASE_JAVA_URL + GlobalMethord.验证商机是否报备 + "?chanceCode=" + item.getCode(), new StringResponseCallBack() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        String id = JsonUtils.getStringValue(JsonUtils.pareseData(response), "uuid");
                                        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
                                                "?workflowTemplateId=f3f6afbe8fae4ab0bddc483cf9531e28&id=" + id + "&chanceId=" + item.getUuid()
                                                + "&code=" + item.getCode() + "&advisorId=" + item.getAdvisorId() + "&region=" + item.getRegion()
                                                + "&regionProvince=" + item.getProvince() + "&source=" + item.getSource() + "&projectName=" + item.getName();
                                        Intent intent = new Intent(BusinessListActivity.this, FormInfoActivity.class);
                                        intent.putExtra("formDataId", id);
                                        intent.putExtra("exturaUrl", url);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
                                                "?workflowTemplateId=f3f6afbe8fae4ab0bddc483cf9531e28&id=0&chanceId=" + item.getUuid()
                                                + "&code=" + item.getCode() + "&advisorId=" + item.getAdvisorId() + "&region=" + item.getRegion()
                                                + "&regionProvince=" + item.getProvince() + "&source=" + item.getSource() + "&projectName=" + item.getName();
                                        Intent intent = new Intent(BusinessListActivity.this, FormInfoActivity.class);
                                        intent.putExtra("exturaUrl", url);
                                        startActivity(intent);
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
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        seachButton.setOnCancleSearch(false);
    }
}
