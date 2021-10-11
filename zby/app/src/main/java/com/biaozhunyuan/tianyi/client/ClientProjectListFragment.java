package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectInfoActivity;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * 客户详情 : 项目列表
 */

public class ClientProjectListFragment extends Fragment {

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private CommanAdapter<Project> adapter;
    private List<Project> listdata;
    public static boolean isResume = false;
    private DictionaryHelper helper;

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
        View v = inflater.inflate(R.layout.fragment_clientinfo_list, null);
        initView(v);
        initDemand();
        getList();
        setOnTouchEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isResume) {
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
                if (position > 0) {
                    Intent intent = new Intent(getActivity(), ProjectInfoActivity.class);
                    Project item = adapter.getItem(position - 1);
                    intent.putExtra("Project", item);
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
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情项目列表 + mClientId;
        demand = new Demand(Project.class);
        demand.pageSize = 10;
        demand.dictionaryNames = "stage.dict_contact_stage,advisorId.base_staff";
        demand.sortField = "createTime desc";
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                listdata = demand.data;

                List<ReturnDict> stageList = JsonUtils.getDictByName(demand.dictionary, "stage.dict_contact_stage");
                for (Project contact : demand.data) {
                    contact.setStageName(JsonUtils.getDictValueById(stageList, contact.getStage()));
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

    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems, getActivity(),
                R.layout.item_projectlist) {
            @Override
            public void convert(int position, final Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, item.getName());
                viewHolder.setTextValue(R.id.tv_customer_stage, ViewHelper.getDateStringFormat(item.getCreateTime()));
                viewHolder.setUserPhoto(R.id.circleImageView, item.getAdvisorId());
                viewHolder.setTextValue(R.id.advisor_name, helper.getUserNameById(item.getAdvisorId()));
//                TextView tv_lose = viewHolder.getView(R.id.tv_lose);
//
//                if (!"6".equals(item.getStatus()) && !"7".equals(item.getStatus())
//                        && item.getAdvisorId().equals(Global.mUser.getUuid())) {
//                    tv_lose.setVisibility(View.VISIBLE);
//                } else {
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

                if (item.getAdvisorId().equals(Global.mUser.getUuid())) {
                    item.setCanNewSamplepaint(true);
                } else {
                    item.setCanNewSamplepaint(false);
                }

//                tv_lose.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
//                                "?workflowTemplateId=1d27ae4b89c94a42b66a632348652124&id=0&projectId=" + item.getUuid()
//                                + "&chanceCode=" + item.getChanceCode() + "&customerId=" + item.getCustomerId();
//                        Intent intent = new Intent(getActivity(), FormInfoActivity.class);
//                        intent.putExtra("exturaUrl", url);
//                        intent.putExtra("projectId", item.getUuid());
//                        startActivity(intent);
//                    }
//                });

            }
        };
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        helper = new DictionaryHelper(getActivity());
    }

    public static ClientProjectListFragment newInstance(String Id) {
        ClientProjectListFragment fragment = new ClientProjectListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }
}
