package com.biaozhunyuan.tianyi.workorder;

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
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * 工单列表:进行中
 */

public class WorkOrderDoingFragment extends Fragment{

    private PullToRefreshAndLoadMoreListView lv;
    private BoeryunSearchView seachButton;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private String url;
    private CommanAdapter<Project> adapter;
    private DictionaryHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workorder,null);
        initView(v);
        initDemand();
        onTouch();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        pageIndex = 1;
        getList();
    }

    private void onTouch() {
        seachButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
                demand.fuzzySearch = str;
                demand.resetFuzzySearchField(true);
                getList();
            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                pageIndex = 1;
                demand.resetFuzzySearchField(false);
                getList();
            }

            @Override
            public void OnClick() {

            }
        });
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                seachButton.setOnCancleSearch();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    Project item = adapter.getItem(position - 1);
                    Intent intent = new Intent(getActivity(),FormInfoActivity.class);
                    intent.putExtra("formDataId",item.getUuid());
                    intent.putExtra("workflowTemplateId",item.getWorkflowTemplate());
                    startActivity(intent);
                }
            }
        });
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Project> data = demand.data;
                    for (Project contract : data) {
                        contract.setExecutorName(demand.getDictName(contract,"executor"));
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
                }catch (Exception e){

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
        return new CommanAdapter<Project>(gridItems, getActivity(), R.layout.item_workorder_list) {
            public void convert(int position, final Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name,item.getSerialNumber());
                viewHolder.setTextValue(R.id.tv_business_customer,item.getContent());
                viewHolder.setTextValue(R.id.tv_workorder_advisor,item.getExecutorName());
                viewHolder.setTextValue(R.id.tv_business_time,item.getCreateTime());
                viewHolder.setTextValue(R.id.tv_amount,item.getBalance());
                MultipleAttachView view = viewHolder.getView(R.id.multi_attach_notice_item);
                if(!TextUtils.isEmpty(item.getAttachmentIds())){
                    view.setVisibility(View.VISIBLE);
                    view.loadImageByAttachIds(item.getAttachmentIds());
                }else {
                    view.setVisibility(View.GONE);
                }
            }
        };
    }

    private void initDemand() {
        demand = new Demand(Project.class);
        url = Global.BASE_JAVA_URL + GlobalMethord.工单列表 + "?complete=false&isGrab=true";
        demand.src = url;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "executor.base_staff";
        demand.pageSize = 10;
        demand.setFuzzySearch("crm_workorder");
    }

    private void initView(View v) {
        helper = new DictionaryHelper(getActivity());
        seachButton = v.findViewById(R.id.seach_button);
        lv = v.findViewById(R.id.lv);
    }

    @Override
    public void onPause() {
        super.onPause();
        seachButton.setOnCancleSearch(false);
    }
}
