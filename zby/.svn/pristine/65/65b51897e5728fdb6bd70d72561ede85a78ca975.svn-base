package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * 客户详情 其他联系方式
 */

public class ClientContactWayListFragment extends Fragment{
    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Client> demand;
    private int pageIndex = 1;
    private DictionaryHelper helper;
    private CommanAdapter<Client> adapter;
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
        View v = inflater.inflate(R.layout.fragment_clientinfo_list, null);
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(getActivity(),ClientNewContactWayActivity.class);
                    intent.putExtra("id",adapter.getItem(position - 1).getUuid());
                    intent.putExtra("isCanSave",((CustomerDetailsActivity)getActivity()).isMineCustomer());
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
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情其他联系方式 + mClientId;
        demand = new Demand(Client.class);
        demand.sortField = "uuid desc";
        demand.pageSize = 10;
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Client> data = demand.data;

                    lv.onRefreshComplete();
                    if (pageIndex == 1) {
                        adapter = getAdapter(data);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.addBottom(data, false);
                        lv.loadCompleted();
                    }
                    pageIndex += 1;
                } catch (Exception e) {

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

    private CommanAdapter<Client> getAdapter(List<Client> gridItems) {
        return new CommanAdapter<Client>(gridItems, getActivity(),
                R.layout.item_contactway_list) {
            @Override
            public void convert(int position, Client item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name,item.getName());
                viewHolder.setTextValue(R.id.tv_contactway_department,item.getDepartment());
                viewHolder.setTextValue(R.id.tv_contactway_post,item.getPosition());
                viewHolder.setTextValue(R.id.tv_contactway_phone,item.getPhone());
                viewHolder.setTextValue(R.id.tv_contactway_email,item.getEmail());
                viewHolder.setTextValue(R.id.tv_contactway_qq,item.getQQ());
                viewHolder.setTextValue(R.id.tv_contactway_address,item.getAddress());
                viewHolder.setTextValue(R.id.tv_contactway_remark,item.getRemark());
            }
        };
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        helper = new DictionaryHelper(getActivity());
    }

    public static ClientContactWayListFragment newInstance(String Id) {
        ClientContactWayListFragment fragment = new ClientContactWayListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }
}
