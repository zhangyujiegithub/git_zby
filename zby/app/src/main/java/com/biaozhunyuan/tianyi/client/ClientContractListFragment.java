package com.biaozhunyuan.tianyi.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.helper.ZLServiceHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.client.CustomerDetailsActivity.PRPROJECT_LISTDATA;

/**
 * 客户关联合同列表Fragment
 */
public class ClientContractListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private static final String PARAM_PROJECT_ID = "PARAM_PROJECT_ID";
    // TODO: Rename and change types of parameters
    private String mClientId = "";

    private Context context;
    private CommanAdapter<Project> adapter;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1; //当前页
    private Demand<Project> demand;
    public static boolean isResume = false;
    private DictionaryHelper dictionaryHelper;

    private ZLServiceHelper zlServiceHelper = new ZLServiceHelper();
    private Project mProject;


    public ClientContractListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Id 编号
     * @return A new instance of fragment ClientContactListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientContractListFragment newInstance(String Id, Project project) {
        ClientContractListFragment fragment = new ClientContractListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        args.putSerializable(PRPROJECT_LISTDATA, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_contact_list, container, false);
        context = getActivity();
        initViews(view);
        initDemand();
        setOnEvent();
        return view;
    }


    private void initDemand() {
        demand = new Demand(Project.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "customerId.crm_customer,projectId.crm_project";
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户详情表单 + mClientId + "&formTableName=" + mProject.getTableName();
    }

    @Override
    public void onResume() {
        pageIndex = 1;
        getContractList();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initViews(View view) {
        dictionaryHelper = new DictionaryHelper(getActivity());
        lv = (PullToRefreshAndLoadMoreListView) view.findViewById(R.id.lv_fragment_client_contact_list);
    }

    private void getContractList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Project> data = demand.data;
                    for (Project contract : data) {
                        contract.setCustomerName(demand.getDictName(contract, "customerId"));
                        contract.setName(demand.getDictName(contract, "projectId"));
                    }
                    lv.onRefreshComplete();
                    if (pageIndex == 1) {
                        adapter = getAdapter(data);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.addBottom(data, false);
                        if (data != null && data.size() == 0) {
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

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
            mProject = (Project) getArguments().getSerializable(PRPROJECT_LISTDATA);
        }
    }

    private void setOnEvent() {


        /**
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getContractList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getContractList();
            }
        });

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
    }

    private CommanAdapter<Project> getAdapter(List<Project> list) {
        return new CommanAdapter<Project>(list, context, R.layout.item_invoicescontract_list) {
            @Override
            public void convert(int position, Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, item.getSerialNumber());
                viewHolder.setTextValue(R.id.tv_business_come, item.getCurrentState());
                viewHolder.setTextValue(R.id.tv_business_time, item.getCreateTime());
            }
        };
    }
}
