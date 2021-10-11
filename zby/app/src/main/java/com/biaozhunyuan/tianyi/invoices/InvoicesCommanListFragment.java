package com.biaozhunyuan.tianyi.invoices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectInfoActivity;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRPROJECT_LISTDATA;
import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRSTR;
import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRURL;

/**
 * 单据 : 通用的列表
 */

public class InvoicesCommanListFragment extends Fragment {

    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private CommanAdapter<Project> adapter;
    private Context context;
    private DictionaryHelper dictionaryHelper;
    private TextView nullData;
    private List<Project> projectList = new ArrayList<>();
    private Project projectListData = new Project();
    private BoeryunSearchView seach_button;
    private int leftPadding;
    private List<Project> relevanceDynamicFormList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getSerializable(PRPROJECT_LISTDATA) != null) {
            projectListData = (Project) getArguments().getSerializable(PRPROJECT_LISTDATA);
        }
    }


    public static InvoicesCommanListFragment newInstance(Project project) {
        Bundle args = new Bundle();
        args.putSerializable(PRPROJECT_LISTDATA, project);
        InvoicesCommanListFragment fragment = new InvoicesCommanListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, null);
        initView(v);
        initData();
        setOnTouch();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        pageIndex = 1;
        getDynamicForm();
    }


    /**
     * 获取列表条目 可关联的表单
     */
    private void getDynamicForm() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取关联表单 + projectListData.getUuid();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                relevanceDynamicFormList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Project.class);
                getList();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                getList();
            }
        });
    }

    private void setOnTouch() {
        seach_button.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
                demand.fuzzySearch = str;
                demand.resetFuzzySearchField(true);
                getList();
            }
        });
        seach_button.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                projectList.clear();
                pageIndex = 1;
                demand.resetFuzzySearchField(false);
                getDynamicForm();
            }

            @Override
            public void OnClick() {

            }
        });
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                seach_button.setOnCancleSearch();
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
                    String url = ProjectInfoActivity.PRURL1 + item.getWorkflowTemplateId() + "&id=" + item.getUuid();
                    Intent intent = new Intent(context, FormInfoActivity.class);
                    intent.putExtra("exturaUrl", url);
                    intent.putExtra("formDataId", item.getFormDataId());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = context.getResources().getDisplayMetrics().density;
        leftPadding = (int) (5 * scale + 0.5f);

        String url = Global.BASE_JAVA_URL + GlobalMethord.单据详情表单 + projectListData.getTableName();
        demand = new Demand<>(Project.class);
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        demand.dictionaryNames = "customerId.crm_customer,projectId.crm_project";
        demand.sortField = "createTime desc";
        demand.src = url;
        demand.setFuzzySearch(projectListData.getTableName());
    }

    private void getList() {
        ProgressDialogHelper.show(getActivity());
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response)     {
                ProgressDialogHelper.dismiss();
                List<Project> data = demand.data;
                try {
                    if (data.size() > 0) {
                        for (Project p : data) {
                            projectList.add(p);
                        }
                        for (Project project : data) {
                            project.setCustomerName(demand.getDictName(project, "customerId"));
                            project.setProjectName(demand.getDictName(project, "projectId"));
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
                    } else {
                        if (projectList.size() > 0) {
                            showOrHiddenList(true);
                        } else {
                            showOrHiddenList(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });

    }

    private void showOrHiddenList(boolean isShowList) {
        lv.loadCompleted();
        if (isShowList) {
            nullData.setVisibility(View.GONE);
        } else {
            nullData.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        seach_button = v.findViewById(R.id.seach_button);
        nullData = v.findViewById(R.id.tv_null_data);
        nullData.setText(PRSTR + projectListData.getFormName());
    }

    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems, getActivity(), R.layout.item_invoices_comman_list) {
            public void convert(int position, final Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, "单号: " + item.getSerialNumber());
                viewHolder.setTextValue(R.id.tv_business_come, item.getCurrentState());
                viewHolder.setTextValue(R.id.tv_apply_advisorid, dictionaryHelper.getUserNameById(item.getCreatorId()));
                viewHolder.setTextValue(R.id.tv_business_time, ViewHelper.getDateStringFormat(item.getCreateTime()));
                LinearLayout ll_customer = viewHolder.getView(R.id.ll_customer);
                LinearLayout ll_project = viewHolder.getView(R.id.ll_project);
                if (projectListData.getHost().contains("customer")) {
                    ll_customer.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_customer_customerid,item.getCustomerName());
                } else {
                    ll_customer.setVisibility(View.GONE);
                }
                if (projectListData.getHost().contains("project")) {
                    ll_project.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_project_projectid,item.getProjectName());
                } else {
                    ll_project.setVisibility(View.GONE);
                }
                LinearLayout ll_gathering = viewHolder.getView(R.id.ll_gathering);
                LinearLayout ll_amount = viewHolder.getView(R.id.ll_amount);
                if(projectListData.getFormName().equals("订单")){
                    ll_gathering.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_gathering, TextUtils.isEmpty(item.getReceivedAmount()) ? "0" : item.getReceivedAmount());
                }
                if(projectListData.getFormName().equals("合同")||projectListData.getFormName().equals("订单")
                        ||projectListData.getFormName().equals("收款单")){
                    ll_amount.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_amount, TextUtils.isEmpty(item.getAmount()) ? "0" : item.getAmount());
                }
                if( projectListData.getFormName().equals("工单")){
                    ll_amount.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_amount, TextUtils.isEmpty(item.getBalance()) ? "0" : item.getBalance());
                }

                LinearLayout ll_btn_parent = viewHolder.getView(R.id.ll_btn_parent);
                if(ll_btn_parent.getChildCount()==0){
                    if(relevanceDynamicFormList.size()>0){
                        for(int i =0;i<relevanceDynamicFormList.size();i++){
                            Project project = relevanceDynamicFormList.get(i);
                            createUi(project,ll_btn_parent,item);
                        }
                    }
                }
            }
        };
    }

    private void createUi(final Project project, LinearLayout linearLayout, final Project contactProject) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(
                R.color.white));
        params.setMargins(0,0,12,0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textView.setBackground(getResources().getDrawable(R.drawable.project_stage_bg));
        textView.setLayoutParams(params);
        textView.setPadding(leftPadding * 2, leftPadding, leftPadding * 2, leftPadding);
        textView.setGravity(Gravity.CENTER);
        textView.setText("新建" + project.getFormName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Global.BASE_JAVA_URL + GlobalMethord.项目新建表单获取参数 +
                        "?dynamicTabId=" + project.getUuid() + "&createFrom=" + project.getCreateFrom() + "&hostMajorKey=" + contactProject.getUuid();
                StringRequest.getAsyn(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        String data = JsonUtils.pareseData(response);
                        String formUrl = PRURL + project.getWorkflowTemplateId()
                                + "&customerId=" + StrUtils.pareseNull(contactProject.getCustomerId()) + data;
                        Intent intent = new Intent(context, FormInfoActivity.class);
                        intent.putExtra("exturaUrl", formUrl);
                        intent.putExtra("formDataId", "0");
                        startActivity(intent);
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
        linearLayout.addView(textView);
    }
    @Override
    public void onPause() {
        super.onPause();
        seach_button.setOnCancleSearch(false);
    }
}
