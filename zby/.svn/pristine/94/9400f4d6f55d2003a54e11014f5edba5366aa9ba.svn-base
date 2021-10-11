package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectInfoActivity;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.client.CustomerDetailsActivity.PRPROJECT_LISTDATA;
import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRURL;

/**
 * 客户详情 : 通用的表单列表
 */

public class ClientCommanListFragment extends LazyFragment {

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private Project mProject;
    private CommanAdapter<Project> adapter;
    private List<Project> listdata;
    private List<Project> relevanceDynamicFormList = new ArrayList<>();
    private int leftPadding;

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
        getDynamicForm();
    }

    /**
     * 获取列表条目 可关联的表单
     */
    private void getDynamicForm() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取关联表单 + mProject.getUuid();
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

    private void setOnTouchEvent() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Project item = adapter.getItem(position - 1);
                    String url = ProjectInfoActivity.PRURL1 + item.getWorkflowTemplateId() + "&id=" + item.getUuid();
                    Intent intent = new Intent(getActivity(), FormInfoActivity.class);
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
        demand = new Demand(Project.class);
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

    private CommanAdapter<Project> getAdapter(List<Project> list) {
        return new CommanAdapter<Project>(list, getActivity(), R.layout.item_invoicescontract_list) {
            @Override
            public void convert(int position, Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, "单号: " + (TextUtils.isEmpty(item.getSerialNumber()) ? "无单号" : item.getSerialNumber()));
                viewHolder.setTextValue(R.id.tv_business_come, item.getCurrentState());
                viewHolder.setTextValue(R.id.tv_business_time, item.getCreateTime());

                LinearLayout ll_gathering = viewHolder.getView(R.id.ll_gathering);
                LinearLayout ll_amount = viewHolder.getView(R.id.ll_amount);
                if (mProject.getFormName().equals("订单")) {
                    ll_gathering.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_gathering, TextUtils.isEmpty(item.getReceivedAmount()) ? "0" : item.getReceivedAmount());
                }
                if (mProject.getFormName().equals("合同") || mProject.getFormName().equals("订单")
                        || mProject.getFormName().equals("收款单")) {
                    ll_amount.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_amount, TextUtils.isEmpty(item.getAmount()) ? "0" : item.getAmount());
                }
                if (mProject.getFormName().equals("工单")) {
                    ll_amount.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_amount, TextUtils.isEmpty(item.getBalance()) ? "0" : item.getBalance());
                }

                LinearLayout view = viewHolder.getView(R.id.ll_btn_parent);
                if (view.getChildCount() == 0) {
                    if (relevanceDynamicFormList.size() > 0) {
                        for (int i = 0; i < relevanceDynamicFormList.size(); i++) {
                            Project project = relevanceDynamicFormList.get(i);
                            createUi(project, view, item);
                        }
                    }
                }
            }
        };
    }

    private void createUi(final Project project, LinearLayout linearLayout, final Project contactProject) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getActivity().getResources().getColor(
                R.color.white));
        params.setMargins(0, 0, 12, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textView.setBackground(getResources().getDrawable(R.drawable.project_stage_bg));
        textView.setLayoutParams(params);
        textView.setPadding(leftPadding * 2, leftPadding, leftPadding * 2, leftPadding);
        textView.setGravity(Gravity.CENTER);
        textView.setText("录入" + project.getFormName());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Global.BASE_JAVA_URL + GlobalMethord.项目新建表单获取参数 +
                        "?dynamicTabId=" + project.getUuid() +
                        "&createFrom=" + project.getCreateFrom() +
                        "&hostMajorKey=" + mProject.getUuid() +
                        "&createFormTab=" + mProject.getUuid();
                StringRequest.getAsyn(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        String data = JsonUtils.pareseData(response);
                        String formUrl = PRURL + project.getWorkflowTemplateId() + "&dynamicTabId=" + mProject.getUuid()
                                + "&createFrom=" + project.getUuid() + "&hostMajorKey=" + mProject.getUuid()
                                + "&customerId=" + contactProject.getCustomerId() + data;
                        Intent intent = new Intent(getActivity(), FormInfoActivity.class);
                        intent.putExtra("exturaUrl", formUrl);
//                        intent.putExtra("customerId", contactProject.getCustomerId());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {

                    }

                    @Override
                    public void onResponseCodeErro(String result) {
                        Toast.makeText(getActivity(),JsonUtils.pareseData(result),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        linearLayout.addView(textView);
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        leftPadding = (int) (5 * scale + 0.5f);
    }

    public static ClientCommanListFragment newInstance(String Id, Project project) {
        ClientCommanListFragment fragment = new ClientCommanListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        args.putSerializable(PRPROJECT_LISTDATA, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            ((CustomerDetailsActivity) getActivity()).setHeaderViewRight(StrUtils.pareseNull(mProject.getCreateFrom()).contains("host"));
        }
    }
}

