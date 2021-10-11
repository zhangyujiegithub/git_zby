package com.biaozhunyuan.tianyi.client;

import android.content.Context;
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
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectInfoActivity;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.client.CustomerDetailsActivity.PRPROJECT_LISTDATA;

/**
 * 客户详情 : 订单列表
 */

public class ClientOrderListFragment extends Fragment{

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Client> demand;
    private int pageIndex = 1;
    private CommanAdapter<Client> adapter;
    private List<Client> listdata;
    private DictionaryHelper dictionaryHelper;
    private Context context;
    private Project mProject;

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
                    Client item = adapter.getItem(position - 1);
                    String url = ProjectInfoActivity.PRURL1 + item.getWorkflowTemplateId() + "&id=" + item.getUuid();
                    Intent intent = new Intent(context, FormInfoActivity.class);
                    intent.putExtra("exturaUrl", url);
                    intent.putExtra("formDataId", item.getFormDataId());
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
                R.layout.item_customer_order_list) {
            @Override
            public void convert(int position, final Client item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_contact_item, item.getSerialNumber());
                viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getCreateTime()));
                viewHolder.setTextValue(R.id.home_product_yeji, item.getCurrentState());
                viewHolder.setTextValue(R.id.home_product_jine, item.getCustomerId());
                viewHolder.setTextValue(R.id.tv_project_name, "项目名称 :" + item.getName());
                viewHolder.setTextValue(R.id.tv_yewuyuan, dictionaryHelper.getUserNameById(item.getCreatorId()));
                TextView tv = viewHolder.getView(R.id.tv_finish);
                if (item.isCanNewSamplepaint()) {
                    tv.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.GONE);
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = ProjectInfoActivity.PRURL + "52555af7d71944168996db3a45bacf74" + "&projectId=" + item.getUuid() + "&amount=" + item.getAmountFieldName();
                        Intent intent = new Intent(context, FormInfoActivity.class);
                        intent.putExtra("exturaUrl", url);
                        startActivity(intent);
                    }
                });

            }
        };
    }
    private void initView(View v) {
        context = getActivity();
        lv = v.findViewById(R.id.lv);
        dictionaryHelper = new DictionaryHelper(context);

    }

    public static ClientOrderListFragment newInstance(String Id, Project project) {
        ClientOrderListFragment fragment = new ClientOrderListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        args.putSerializable(PRPROJECT_LISTDATA,project);
        fragment.setArguments(args);
        return fragment;
    }
}
