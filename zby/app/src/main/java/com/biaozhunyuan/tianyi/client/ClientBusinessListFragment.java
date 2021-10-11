package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
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
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * 客户详情 : 商机列表
 */

public class ClientBusinessListFragment extends Fragment{

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Business> demand;
    private int pageIndex = 1;
    private CommanAdapter<Business> adapter;
    private List<Business> listdata;
    private DictionaryHelper dictionaryHelper;
    public static boolean isResume = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientinfo_list,null);
        initView(v);
        isResume = false;
        initDemand();
        getList();
        setOnTouchEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isResume){
            lv.startRefresh();
            pageIndex = 1;
            getList();
            isResume = false;
        }
    }

    private void setOnTouchEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    Intent intent = new Intent(getActivity(), BusinessInfoActivity.class);
                    Business item = adapter.getItem(position - 1);
                    item.setCanSave(((CustomerDetailsActivity)getActivity()).isMineCustomer());
                    intent.putExtra("Business", item);
                    startActivity(intent);
                }

            }
        });


        /**
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getList();
            }
        });
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情商机列表 + mClientId;
        demand = new Demand(Business.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "source.dict_sale_chance_source";
        demand.src = url;
        demand.setFuzzySearch("crm_sale_chance");
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                listdata = demand.data;
                for (final Business task : listdata) {
                    try {
                        task.setSourceName(demand.getDictName(task, "source"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(listdata);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(listdata, false);
                    if (listdata != null && listdata.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }
    private CommanAdapter<Business> getAdapter(List<Business> gridItems) {
        return new CommanAdapter<Business>(gridItems, getActivity(),
                R.layout.item_business_list) {
            @Override
            public void convert(int position, final Business item, BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setTextValue(R.id.tv_business_time, item.getCreateTime()!=null
                            ? ViewHelper.turnDate(item.getCreateTime(),
                            "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm") : "");
                    viewHolder.setTextValue(R.id.tv_customer_name, dictionaryHelper.getUserNameById(item.getAdvisorId()));
                    viewHolder.setTextValue(R.id.tv_business_number, item.getAddress());
                    viewHolder.setTextValue(R.id.tv_business_customer, item.getContactPer());
                    viewHolder.setTextValue(R.id.tv_business_come, item.getSourceName());
                    viewHolder.setTextValue(R.id.tv_business_source, item.getName());
                    TextView btn = viewHolder.getView(R.id.btn_turn_project);

                    if((item.getCustomerId() == null || item.getProjectId() == null)
                            && Global.mUser.getUuid().equals(item.getAdvisorId())){
                        btn.setVisibility(View.VISIBLE);
                    }else {
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
                                                "?workflowTemplateId=f3f6afbe8fae4ab0bddc483cf9531e28&id="+id+"&chanceId=" + item.getUuid()
                                                + "&code=" + item.getCode() + "&advisorId=" + item.getAdvisorId() + "&region=" + item.getRegion()
                                                + "&regionProvince=" + item.getProvince() + "&source=" + item.getSource() + "&projectName=" + item.getName();
                                        Intent intent = new Intent(getActivity(), FormInfoActivity.class);
                                        intent.putExtra("exturaUrl",url);
                                        startActivity(intent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
                                                "?workflowTemplateId=f3f6afbe8fae4ab0bddc483cf9531e28&id=0&chanceId=" + item.getUuid()
                                                + "&code=" + item.getCode() + "&advisorId=" + item.getAdvisorId() + "&region=" + item.getRegion()
                                                + "&regionProvince=" + item.getProvince() + "&source=" + item.getSource() + "&projectName=" + item.getName();
                                        Intent intent = new Intent(getActivity(), FormInfoActivity.class);
                                        intent.putExtra("exturaUrl",url);
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
    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        dictionaryHelper = new DictionaryHelper(getActivity());

    }

    public static ClientBusinessListFragment newInstance(String Id) {
        ClientBusinessListFragment fragment = new ClientBusinessListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }
}
