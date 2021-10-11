package com.biaozhunyuan.tianyi.dispatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.ShouWenInfoActivity;
import com.biaozhunyuan.tianyi.apply.model.WorkflowInstance;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/3/23.
 * 待阅收文列表页面
 */

public class DaiYueFaWenFragment extends Fragment {

    private Context context;
    private Demand<WorkflowInstance> demand;
    private DictionaryHelper dictionaryHelper;
    private CommanAdapter<WorkflowInstance> adapter;
    private int pageIndex = 1;

    private PullToRefreshAndLoadMoreListView lv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_shouwen, null);
        lv = view.findViewById(R.id.lv_common_shouwen);
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        initDemand();

        setOnEvent();
        return view;
    }

    @Override
    public void onStart() {
        pageIndex = 1;
        getDaiBanList();
        super.onStart();
    }

    private void initDemand() {
        demand = new Demand<>(WorkflowInstance.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "submissionTime";
        demand.dictionaryNames = "";
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.已办发文列表;
    }

    private void setOnEvent() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getDaiBanList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getDaiBanList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    WorkflowInstance template = adapter.getItem(position - 1);
                    Intent intent = new Intent(context, ShouWenInfoActivity.class);
                    intent.putExtra("formName", template.getFormName());
                    intent.putExtra("workflowId", template.getUuid());
                    intent.putExtra("formDataId", template.getFormDataId());
                    intent.putExtra("createrId", template.getCreatorId());
                    intent.putExtra("workflowTemplateId", template.getWorkflowTemplate());
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * 获取待办列表
     */
    private void getDaiBanList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<WorkflowInstance> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), WorkflowInstance.class);

                    lv.onRefreshComplete();
                    if (list != null) {
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.addBottom(list, false);
                            if (list != null && list.size() == 0) {
                                lv.loadAllData();
                            }
                            lv.loadCompleted();
                        }
                    }

                    pageIndex += 1;
                } catch (JSONException e) {
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


    private CommanAdapter<WorkflowInstance> getAdapter(List<WorkflowInstance> list) {
        return new CommanAdapter<WorkflowInstance>(list, context, R.layout.askforleavelist_item_new) {
            @Override
            public void convert(int position, final WorkflowInstance item, BoeryunViewHolder viewHolder) {

                viewHolder.setTextValue(R.id.item_title, item.getTitle());
                viewHolder.setTextValue(R.id.tv_creater_apply_item, dictionaryHelper.getUserNameById(item.getCreatorId())); //创建人名称
                viewHolder.setTextValue(R.id.tv_current_state_apply_list, item.getCurrentState()); //当前审批状态
//                viewHolder.setTextValue(R.id.tv_name_apply_list, item.getFormName()); //表单名称
                viewHolder.setTextValue(R.id.tv_time_apply_item, ViewHelper.getDateStringFormat(item.getCreateTime())); //创建时间

                viewHolder.setUserPhoto(R.id.iv_head_item_apply_list, item.getCreatorId());//创建人头像
            }
        };
    }

}
