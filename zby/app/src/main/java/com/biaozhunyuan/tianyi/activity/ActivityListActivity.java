package com.biaozhunyuan.tianyi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.client.SelectedCustomerActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.global.Global.BASE_JAVA_URL;

/**
 * Created by 王安民 on 2017/10/2.
 * 市场活动列表页
 */

public class ActivityListActivity extends BaseActivity {


    private BoeryunHeaderView headerView;
    private BoeryunSearchView searchView;
    private PullToRefreshAndLoadMoreListView lv;

    private Context context;
    private Demand<ActivityModel> demand;
    private int pageIndex = 1; //当前页
    private CommanAdapter<ActivityModel> adapter;
    private String dictionary;
    private DictionaryHelper helper;
    private Date date;
    private SimpleDateFormat format;
    public static boolean isResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity);
        date = new Date();
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        context = ActivityListActivity.this;
        helper = new DictionaryHelper(context);
        initViews();
        initDemand();
        ProgressDialogHelper.show(ActivityListActivity.this);
        getBespokeList();
        setOnEvent();
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_activity_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_view_activity_list);
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.lv_activity_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isResume) {
            pageIndex = 1;
            getBespokeList();
            isResume = false;
        }
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBespokeList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getBespokeList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityModel item = adapter.getDataList().get(position - 1);
                String url = BASE_JAVA_URL + GlobalMethord.活动详情 + item.getUuid();
                Intent intent = new Intent(context, ActivityInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activityInfo", item);
                intent.putExtra(ActivityInfoActivity.EXTRA_TITLE, "活动详情");
                intent.putExtra(ActivityInfoActivity.EXTRA_URL, url);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                Map<String, String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_theme", "1|" + str);
                demand.keyMap = searchMap;
                pageIndex = 1;
                lv.startRefresh();
                getBespokeList();
            }
        });
        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                Map<String, String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_theme", "");
                demand.keyMap = searchMap;
                pageIndex = 1;
                lv.startRefresh();
                getBespokeList();
            }

            @Override
            public void OnClick() {

            }
        });

    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.市场活动;
        demand = new Demand(ActivityModel.class);
        demand.pageSize = 10;
        demand.sortField = "time desc";
        demand.dictionaryNames = "typeId.dict_activity_type,departmentIds.base_department,creatorDepartmentId.base_department";
        demand.src = url;
    }


    private void getBespokeList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                List<ActivityModel> list = demand.data;
                if (list != null) {
                    try {
                        for (ActivityModel project : list) {
                            project.setTypeName(demand.getDictName(project, "typeId"));
                            project.setDepartmentIdsName(demand.getDictName(project, "departmentIds"));
                            project.setCreatorDepartmentIdName(demand.getDictName(project, "creatorDepartmentId"));
                        }
                    } catch (Exception e) {

                    }

                    lv.onRefreshComplete();
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
                    pageIndex += 1;

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

    private CommanAdapter<ActivityModel> getAdapter(List<ActivityModel> list) {
        return new CommanAdapter<ActivityModel>(list, context, R.layout.item_activity_list) {
            @Override
            public void convert(int position, final ActivityModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_activity_address, item.getPlace()); //地址
                viewHolder.setTextValue(R.id.tv_activity_type, item.getTypeName()); //活动类型
                viewHolder.setTextValue(R.id.tv_activity_time, ViewHelper.getStringFormat(item.getTime(), "yyyy-MM-dd")); //活动时间
                viewHolder.setTextValue(R.id.tv_activity_applynum, "报名人数 :" + item.getCustomerCount()); //报名人数
                viewHolder.setTextValue(R.id.tv_activity_name, item.getTheme()); //活动名称
                TextView apply = viewHolder.getView(R.id.tv_activity_apply);

                int timeCompareSize = ViewHelper.getTimeCompareSize(format.format(date), item.getTime(), format);
                if (timeCompareSize == 3) {
                    apply.setVisibility(View.VISIBLE);
                } else {
                    apply.setVisibility(View.GONE);
                }

                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SelectedCustomerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("activity", item);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
    }
}
