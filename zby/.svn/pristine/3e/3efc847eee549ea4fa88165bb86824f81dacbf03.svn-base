package com.biaozhunyuan.tianyi.project;

import android.content.Context;
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

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRPROJECT_LISTDATA;
import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRSTR;
import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRURL;

/**
 * 项目详情 : 通用的表单列表
 */

public class ProjectCommanListFragment extends LazyFragment {


    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Project> demand;
    private int pageIndex = 1;
    private CommanAdapter<Project> adapter;
    private Project mProject;
    private Context context;
    private DictionaryHelper dictionaryHelper;
    private TextView nullData;
    private List<Project> projectList = new ArrayList<>();
    private Project projectListData = new Project();
    private List<Project> relevanceDynamicFormList = new ArrayList<>();
    private int leftPadding;
    private String PROJECT_FORMNAME = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getSerializable(PRPROJECT_LISTDATA) != null) {
            projectListData = (Project) getArguments().getSerializable(PRPROJECT_LISTDATA);
            PROJECT_FORMNAME = projectListData.getFormName();
        }
    }


    public static ProjectCommanListFragment newInstance(Project project) {
        Bundle args = new Bundle();
        args.putSerializable(PRPROJECT_LISTDATA, project);
        ProjectCommanListFragment fragment = new ProjectCommanListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project_info, null);
        getIntentData();
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

    private void getIntentData() {
        if (getActivity().getIntent().getSerializableExtra("Project") != null) {
            mProject = (Project) getActivity().getIntent().getSerializableExtra("Project");
        }
    }

    private void setOnTouch() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectList.clear();
                pageIndex = 1;
                getDynamicForm();
            }
        });
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getDynamicForm();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project item = adapter.getItem(position - 1);
                String url = ProjectInfoActivity.PRURL1 + item.getWorkflowTemplateId() + "&id=" + item.getFormDataId();
                Intent intent = new Intent(context, FormInfoActivity.class);
                intent.putExtra("exturaUrl", url);
                intent.putExtra("formDataId", item.getFormDataId());
                intent.putExtra("projectId", mProject.getUuid());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = context.getResources().getDisplayMetrics().density;
        leftPadding = (int) (5 * scale + 0.5f);

        String url = Global.BASE_JAVA_URL + GlobalMethord.项目表单 + mProject.getUuid() + "&formTableName=" + projectListData.getFormTableName();
        demand = new Demand<>(Project.class);
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.src = url;

    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<Project> data = demand.data;
                if (data != null && data.size() > 0) {
                    for (Project p : data) {
                        projectList.add(p);
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
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

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
        nullData = v.findViewById(R.id.tv_null_data);
        nullData.setText(PRSTR + projectListData.getFormName());
    }

    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems, getActivity(), R.layout.item_invoicescontract_list) {
            public void convert(int position, final Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_customer_name, "单号: " + item.getSerialNumber());
                viewHolder.setTextValue(R.id.tv_business_come, item.getCurrentState());
                viewHolder.setTextValue(R.id.tv_business_time, ViewHelper.getDateStringFormat(item.getCreateTime()));
                LinearLayout ll_gathering = viewHolder.getView(R.id.ll_gathering);
                LinearLayout ll_amount = viewHolder.getView(R.id.ll_amount);
                if (PROJECT_FORMNAME.equals("订单")) {
                    ll_gathering.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_gathering, TextUtils.isEmpty(item.getReceivedAmount()) ? "0" : item.getReceivedAmount());
                }
                if (PROJECT_FORMNAME.equals("合同") || projectListData.getFormName().equals("订单")
                        || projectListData.getFormName().equals("收款单")) {
                    ll_amount.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_amount, TextUtils.isEmpty(item.getAmount()) ? "0" : item.getAmount());
                }
                if (PROJECT_FORMNAME.equals("工单")) {
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
                if (PROJECT_FORMNAME.equals("采购合同")) {
                    if (item.getCurrentState().equals("已完成")) {
                        view.setVisibility(View.VISIBLE);
                    } else {
                        view.setVisibility(View.GONE);
                    }
                }
            }
        };
    }

    private void createUi(final Project project, LinearLayout linearLayout, final Project item) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(
                R.color.white));
        params.setMargins(0, 0, 12, 0);
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
                        "?dynamicTabId=" + project.getUuid() + "&createFrom=" + item.getUuid()
                        + "&hostMajorKey=" + mProject.getUuid() + "&createFormTab=" + projectListData.getUuid();
                StringRequest.getAsyn(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String data = JsonUtils.pareseData(response);
                            String str = "";
                            if (!TextUtils.isEmpty(data)) {
                                String[] split = data.split("&");
                                for (int i = 0; i < split.length; i++) {
                                    String[] split1 = split[i].split("=");
                                    if (split1.length > 1) {
                                        str += "&" + split1[0] + "=" + URLEncoder.encode(split1[1], "UTF-8");
                                    }
                                }
                            }

//                            String encode = URLEncoder.encode(data, "UTF-8");
                            String formUrl = PRURL + project.getWorkflowTemplateId() + "&projectId=" + mProject.getUuid() + str;
//                            String url = "http://i.tysoft.com/wf/form/vsheet/formForMobile?id=0&workflowTemplateId=f3f6afbe8fae4ab0bddc483cf9531e30&&&detaildata=productContent_1234%7C%7Cquantities_5.00%5EproductContent_23456%7C%7Cquantities_3.00%5E";
                            Intent intent = new Intent(context, FormInfoActivity.class);
                            intent.putExtra("exturaUrl", formUrl);
                            intent.putExtra("formDataId", "0");
                            intent.putExtra("projectId", mProject.getUuid());
                            startActivity(intent);
                        } catch (UnsupportedEncodingException e) {
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
        linearLayout.addView(textView);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            ((ProjectInfoActivity) getActivity()).setHeaderViewRight(StrUtils.pareseNull(projectListData.getCreateFrom()).contains("host"));
        }
    }
}
