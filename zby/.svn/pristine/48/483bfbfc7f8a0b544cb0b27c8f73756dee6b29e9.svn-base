package com.biaozhunyuan.tianyi.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRSTR;

/**
 * 里程碑
 */

public class ProjectMilestoneFragment extends LazyFragment {


    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private CommanAdapter<Project> adapter;
    private Project mProject;
    private DictionaryHelper helper;
    private Context mContext;
    public static boolean isReasume = false;
    private TextView nullData;
    private List<Project> projectList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project_info, null);
        getIntentData();
        initView(v);
        initData();
        getList();
        setOnTouch();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isReasume) {
            projectList.clear();
            pageIndex = 1;
            getList();
        }
    }

    private void setOnTouch() {
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                projectList.clear();
                getList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Project item = adapter.getItem(position - 1);
                    Intent intent = new Intent(mContext, ProjectMilestoneInfoActivity.class);
                    intent.putExtra("Project", item);
                    intent.putExtra("isCanSave", mProject.isCanNewSamplepaint());
                    intent.putExtra("projectName", mProject.getName());
                    startActivity(intent);
                }
            }
        });
    }

    private void getIntentData() {
        if (getActivity().getIntent().getSerializableExtra("Project") != null) {
            mProject = (Project) getActivity().getIntent().getSerializableExtra("Project");
        }
    }

    private void initData() {
        mContext = getActivity();
        helper = new DictionaryHelper(mContext);

        String url = Global.BASE_JAVA_URL + GlobalMethord.项目里程碑 + mProject.getUuid();
        demand = new Demand<>(Project.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "status.crm_project_milestone_status,stageId.crm_project_milestone_stage,projectStatus.dict_project_status";
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<Project> data = demand.data;
                if(data.size()>0){
                    for(Project p : data){
                        projectList.add(p);
                    }
                    for (final Project task : data) {
                        try {
//                            task.setMilestoneName(demand.getDictName(task, "name"));
                            task.setStatusName(demand.getDictName(task, "status"));
                            task.setProjectStatusName(demand.getDictName(task, "projectStatus"));
                            task.setStageName(demand.getDictName(task, "stageId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                    showOrHiddenList(true);
                }else {
                    if(projectList.size()>0){
                        showOrHiddenList(true);
                    }else {
                        showOrHiddenList(false);
                    }
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


    private void showOrHiddenList(boolean isShowList){
        lv.loadCompleted();
        if(isShowList){
            nullData.setVisibility(View.GONE);
        }else {
            nullData.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        nullData = v.findViewById(R.id.tv_null_data);
        nullData.setText(PRSTR + "里程碑");
    }


    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems, getActivity(), R.layout.item_project_milestone_list) {
            public void convert(int position, final Project item, BoeryunViewHolder viewHolder) {
                String statusName = item.getStatusName();
                viewHolder.setTextValue(R.id.tv_name_contact_item, item.getStageName());
                viewHolder.setTextValue(R.id.tv_status_item_contact, "当前状态: " + statusName);
                viewHolder.setTextValue(R.id.tv_time_contact_item, item.getMonth());
//                viewHolder.setTextValue(R.id.tv_plan_area, item.getPaintArea());
                viewHolder.setTextValue(R.id.tv_remark, item.getRemark());
                TextView finish = viewHolder.getView(R.id.btn_finish);
                if ("已完成".equals(statusName)) {
                    finish.setVisibility(View.GONE);
                } else {
                    finish.setVisibility(View.VISIBLE);
                }
                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Project project = new Project();
                        project.setStageId(item.getStageId());
                        project.setProjectId(item.getProjectId());
                        StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.验证里程碑阶段是否提交过,project,
                                new StringResponseCallBack() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            String uuid = JsonUtils.getStringValue(JsonUtils.pareseData(response), "uuid");
                                            String url = ProjectInfoActivity.PRURL1 + "cd67ae4b89c94a42b66a632348652124" +
                                                        "&projectId=" + item.getProjectId() +
                                                        "&milestoneName=" + item.getStageId() +
                                                        "&advisorId=" + item.getAdvisorId() +
                                                        "&id=" + uuid;

                                            Intent intent = new Intent(mContext, FormInfoActivity.class);
                                            intent.putExtra("exturaUrl", url);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            String url = ProjectInfoActivity.PRURL + "cd67ae4b89c94a42b66a632348652124" +
                                                    "&projectId=" + item.getProjectId() +
                                                    "&milestoneName=" + item.getStageId() +
                                                    "&advisorId=" + item.getAdvisorId();
                                            Intent intent = new Intent(mContext, FormInfoActivity.class);
                                            intent.putExtra("exturaUrl", url);
                                            startActivity(intent);
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
                });
            }
        };
    }

}
