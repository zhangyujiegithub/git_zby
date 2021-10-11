package com.biaozhunyuan.tianyi.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.client.CustomerDetailsActivity.PRPROJECT_LISTDATA;

/**
 * 客户详情 : 项目报备申请
 */

public class ClientReportListFragment extends Fragment{

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Client> demand;
    private int pageIndex = 1;
    private Project mProject;
    private CommanAdapter<Client> adapter;
    private List<Client> listdata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
            mProject = (Project) getArguments().getSerializable(PRPROJECT_LISTDATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientinfo_list,null);
        initView(v);
        initDemand();
        getList();
        setOnTouchEvent();
        return v;
    }

    private void setOnTouchEvent() {

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
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情表单 + mClientId + "&formTableName=" + mProject.getTableName();
        demand = new Demand(Client.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                listdata = demand.data;


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
    private CommanAdapter<Client> getAdapter(List<Client> gridItems) {
        return new CommanAdapter<Client>(gridItems, getActivity(),
                R.layout.item_contract_list) {
            @Override
            public void convert(int position, Client item, BoeryunViewHolder viewHolder) {


            }
        };
    }
    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
    }

    public static ClientReportListFragment newInstance(String Id,Project project) {
        ClientReportListFragment fragment = new ClientReportListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        args.putSerializable(PRPROJECT_LISTDATA, project);
        fragment.setArguments(args);
        return fragment;
    }
}
