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
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.client.CustomerDetailsActivity.PRPROJECT_LISTDATA;

/**
 * 客户详情 工单
 */

public class ClientWorkOrderListFragment extends Fragment {
    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private CommanAdapter<Project> adapter;
    private List<Project> listdata;
    private Project mProject;
    public static boolean isResume = false;

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
                    Project item = adapter.getItem(position - 1);
                    Intent intent = new Intent(getActivity(), FormInfoActivity.class);
                    intent.putExtra("formDataId", item.getUuid());
                    intent.putExtra("workflowTemplateId", item.getWorkflowTemplateId());
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
        demand = new Demand(Project.class);
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "executor.base_staff";
        demand.pageSize = 10;
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Project> data = demand.data;
                    for (Project contract : data) {
                        contract.setExecutorName(demand.getDictName(contract, "executor"));
                    }
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

    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems, getActivity(),
                R.layout.item_workorder_list) {
            @Override
            public void convert(int position, Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, item.getSerialNumber());
                viewHolder.setTextValue(R.id.tv_business_customer, item.getContent());
                viewHolder.setTextValue(R.id.tv_workorder_advisor, item.getExecutorName());
                viewHolder.setTextValue(R.id.tv_business_time, item.getCreateTime());
                MultipleAttachView view = viewHolder.getView(R.id.multi_attach_notice_item);
                if (!TextUtils.isEmpty(item.getAttachmentIds())) {
                    view.setVisibility(View.VISIBLE);
                    view.loadImageByAttachIds(item.getAttachmentIds());
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        };
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
    }

    public static ClientWorkOrderListFragment newInstance(String Id, Project project) {
        ClientWorkOrderListFragment fragment = new ClientWorkOrderListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        args.putSerializable(PRPROJECT_LISTDATA, project);
        fragment.setArguments(args);
        return fragment;
    }
}
