package com.biaozhunyuan.tianyi.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.business.Business;
import com.biaozhunyuan.tianyi.business.BusinessInfoActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * 客户列表 :商机
 */

@SuppressLint("ValidFragment")
public class ClientlistbusinessFragment extends LazyFragment {

    private BoeryunSearchView seachButton;
    //    private PullToRefreshAndLoadMoreListView lv;
    private NoScrollListView lv;
    private Demand<Business> demand;
    private int pageIndex = 1;
    private CommanAdapter<Business> adapter;
    private Context context;
    private DictionaryHelper dictionaryHelper;
    private int fragmentID = 0;
    private AutoMaxHeightViewpager vp;
    private View v;
    public static boolean isResume = false;


    public ClientlistbusinessFragment(AutoMaxHeightViewpager vp, int fragmentID) {
        this.vp = vp;
        this.fragmentID = fragmentID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_client_client, null);
        initView(v);
        initData();
        vp.setObjectForPosition(v, fragmentID);
        getBusinessList(null);
        setOnTouchEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isResume){
            pageIndex = 1;
            getBusinessList(null);
            isResume = false;
        }

    }

    private void setOnTouchEvent() {
//        /**
//         * 查看更多
//         */
//        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                getBusinessList();
//            }
//        });
//
//        /**
//         * 下拉刷新
//         */
//        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageIndex = 1;
//                getBusinessList();
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
                Intent intent = new Intent(context, BusinessInfoActivity.class);
//                    Business item = adapter.getItem(position - 1);
                Business item = adapter.getItem(position);
                item.setCanSave((item.getCustomerId() == null || item.getProjectId() == null)
                        && Global.mUser.getUuid().equals(item.getAdvisorId()));
                intent.putExtra("Business", item);
                startActivity(intent);
//                }
            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                pageIndex = 1;
                demand.resetFuzzySearchField(false);
                getBusinessList(null);
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
                getBusinessList(null);
            }
        });

    }

    private void getBusinessList(final RefreshLayout refreshLayout) {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Business> data = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Business.class);
                    for (final Business task : data) {
                        try {
                            task.setSourceName(demand.getDictName(task, "source"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (refreshLayout != null) {
                        refreshLayout.finishRefresh();
                    }
//                    lv.onRefreshComplete();
                    if (pageIndex == 1) {
                        adapter = getAdapter(data);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.addBottom(data, false);
                        if (data != null && data.size() == 0) {
                            if (refreshLayout != null) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                        if (refreshLayout != null) {
                            refreshLayout.finishLoadMore();
                        }
//                        lv.loadCompleted();
                    }
                    vp.resetHeight(fragmentID);
                    pageIndex += 1;
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

    public void loadMoreData(RefreshLayout refreshLayout) {
        getBusinessList(refreshLayout);
    }

    public void refreshData(RefreshLayout refreshLayout) {
        pageIndex = 1;
        getBusinessList(refreshLayout);
    }

    private void initData() {
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        demand = new Demand();
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        demand.dictionaryNames = "source.dict_sale_chance_source";
        demand.sortField = "createTime desc";
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.商机列表;
        demand.setFuzzySearch("crm_sale_chance");
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        seachButton = v.findViewById(R.id.seach_button);
    }

    private CommanAdapter<Business> getAdapter(List<Business> taskList) {
        return new CommanAdapter<Business>(taskList, context, R.layout.item_business_list) {
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
                                        Intent intent = new Intent(getActivity(), FormInfoActivity.class);
                                        intent.putExtra("exturaUrl", url);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
                                                "?workflowTemplateId=f3f6afbe8fae4ab0bddc483cf9531e28&id=0&chanceId=" + item.getUuid()
                                                + "&code=" + item.getCode() + "&advisorId=" + item.getAdvisorId() + "&region=" + item.getRegion()
                                                + "&regionProvince=" + item.getProvince() + "&source=" + item.getSource() + "&projectName=" + item.getName();
                                        Intent intent = new Intent(getActivity(), FormInfoActivity.class);
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
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            vp.resetHeight(fragmentID);
        }else {
            seachButton.setOnCancleSearch(false);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        seachButton.setOnCancleSearch(false);
    }
}
