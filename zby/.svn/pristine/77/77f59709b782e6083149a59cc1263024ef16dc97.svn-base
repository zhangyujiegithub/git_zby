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
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * 客户详情 : 分配记录
 */

public class ClinetAmendantListFragment extends Fragment {

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Client> demand;
    private int pageIndex = 1;
    private CommanAdapter<Client> adapter;
    private List<Client> listdata;
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
        View v = inflater.inflate(R.layout.fragment_clientinfo_list,null);
        initView(v);
        initDemand();
        setOnTouchEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        pageIndex = 1;
        getList();
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
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情修改记录列表 + mClientId;
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
                R.layout.item_amendant_list) {
            @Override
            public void convert(int position, Client item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_creatorId,"修改人 : " + helper.getUserNameById(item.getCreatorId()));
                viewHolder.setTextValue(R.id.tv_keyShowName,"数据字段 : " + StrUtils.pareseNull(item.getKeyShowName()));
                viewHolder.setTextValue(R.id.tv_createTime,item.getCreateTime());
                viewHolder.setTextValue(R.id.tv_oldCustomerShowValue,"旧数据 : " + StrUtils.pareseNull(item.getOldCustomerShowValue()));
                viewHolder.setTextValue(R.id.tv_newCustomerShowValue,"新数据 : " + StrUtils.pareseNull(item.getNewCustomerShowValue()));
            }
        };
    }
    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        helper = new DictionaryHelper(getActivity());
    }

    public static ClinetAmendantListFragment newInstance(String Id) {
        ClinetAmendantListFragment fragment = new ClinetAmendantListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }
}
