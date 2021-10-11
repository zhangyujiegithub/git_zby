package com.biaozhunyuan.tianyi.project;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;

import java.util.List;

import okhttp3.Request;

/**
 * 作者： wanganmin
 * 日期： 2020-03-24 10:38
 * 描述：项目打卡记录页面
 */
public class ProjectAttendanceSalaryActivity extends BaseActivity {

    private BoeryunHeaderView header;
    private PullToRefreshAndLoadMoreListView listView;

    private Context mContext;
    private int pageIndex = 1;
    private CommanAdapter<ProjectAttendanceInfo> adapter;
    private Demand<ProjectAttendanceInfo> demand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_attendance_salary);
        initView();
        initDemand();
        getList();
        setEvent();
    }

    private void initView() {
        header = findViewById(R.id.header);
        listView = findViewById(R.id.list_view);
        mContext = ProjectAttendanceSalaryActivity.this;
    }

    private void setEvent() {
        header.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        listView.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getList();
            }
        });

        listView.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageIndex += 1;
                getList();
            }
        });
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.项目打卡记录;
        demand = new Demand<>(ProjectAttendanceInfo.class);
        demand.pageSize = 10;
        demand.sort = "";
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "";
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                listView.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getOutSideAdapter(demand.data);
                    listView.setAdapter(adapter);
                } else {
                    adapter.addBottom(demand.data, false);
                    if (demand.data != null && demand.data.size() == 0) {
                        listView.loadAllData();
                    }
                    listView.loadCompleted();
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


    private CommanAdapter<ProjectAttendanceInfo> getOutSideAdapter(List<ProjectAttendanceInfo> list) {
        return new CommanAdapter<ProjectAttendanceInfo>(list, mContext, R.layout.item_sign_salary_project) {
            @Override
            public void convert(int position, ProjectAttendanceInfo item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_signIn_time, item.getSignInDateTime());
                viewHolder.setTextValue(R.id.tv_signIn_address, item.getSignInAddress());
                viewHolder.setTextValue(R.id.tv_project_sign_in, item.getProjectName());
                viewHolder.setTextValue(R.id.tv_signOut_time, item.getSignOutDateTime());
                viewHolder.setTextValue(R.id.tv_signOut_address, item.getSignOutAddress());
                viewHolder.setTextValue(R.id.tv_project_sign_out, item.getProjectName());
                MultipleAttachView viewSignIn = viewHolder.getView(R.id.attach_signIn_attendance_list);
                MultipleAttachView viewSignOut = viewHolder.getView(R.id.attach_signOut_attendance_list);
                if (!TextUtils.isEmpty(item.getSignInAttachmentIds())) {
                    viewSignIn.setVisibility(View.VISIBLE);
                    viewSignIn.loadImageByAttachIds(item.getSignInAttachmentIds());
                } else {
                    viewSignIn.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getSignOutAttachmentIds())) {
                    viewSignOut.setVisibility(View.VISIBLE);
                    viewSignOut.loadImageByAttachIds(item.getSignOutAttachmentIds());
                } else {
                    viewSignOut.setVisibility(View.GONE);
                }
            }
        };
    }
}
