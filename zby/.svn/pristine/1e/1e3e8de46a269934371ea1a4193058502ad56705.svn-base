package com.biaozhunyuan.tianyi.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.NavActivity;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.buglist.BugInfoActivity;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.client.ClientRelatedInfoActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.log.LogInfoActivity;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.NoticeInfoActivity;
import com.biaozhunyuan.tianyi.task.TaskInfoActivityNew;
import com.biaozhunyuan.tianyi.task.TaskListActivityNew;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/5/16.
 * 动态列表页面
 */

public class DynamicActivity extends BaseActivity {


    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Dynamic> demand;
    private Context mContext;
    private List<Dynamic> list;
    private DictionaryHelper helper;
    private CommanAdapter<Dynamic> adapter;
    private int pageIndex = 1;
    private BoeryunHeaderView headerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dynamic);
        mContext = DynamicActivity.this;
        helper = new DictionaryHelper(mContext);
        lv = findViewById(R.id.lv_fragment_dynamic);
        headerView = findViewById(R.id.header_new_bespoke);
        lv.setDivider(null);
        initDemand();
        getList();
        setOnEvent();
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态列表 + Global.mUser.getUuid();
        demand = new Demand(Dynamic.class);
        demand.pageSize = 10;
        demand.sort = "desc";
//        demand.sortField = "createTime";
        demand.dictionaryNames = "";
        demand.src = url;
    }

    private void setOnEvent() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });


        //根据动态类型判断跳转页面
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Dynamic dynamic = adapter.getItem(position - 1);

                    if (dynamic != null) {
                        getDynamicInfo(dynamic);

                    }
                }
            }
        });

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
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        ProgressDialogHelper.show(mContext);
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                list = demand.data;

                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(list);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(list, false);
                    if (list != null && list.size() == 0) {
                        Toast.makeText(mContext, "暂无更多动态", Toast.LENGTH_SHORT).show();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;
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

    private CommanAdapter<Dynamic> getAdapter(List<Dynamic> list) {
        return new CommanAdapter<Dynamic>(list, mContext, R.layout.item_dynamic) {
            @Override
            public void convert(int position, Dynamic item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhotoById(R.id.avatar_dynamic, helper.getUser(item.getCreatorId()));
                viewHolder.setTextValue(R.id.tv_content_dynamic, item.getMessage());
                viewHolder.setTextValue(R.id.tv_time_dynamic, item.getCreateTime());
                viewHolder.setTextValue(R.id.tv_name_dynamic_item, helper.getUserNameById(item.getCreatorId()));
            }
        };
    }


    /**
     * 获取动态详情
     *
     * @param dynamic
     */
    private void getDynamicInfo(final Dynamic dynamic) {
//        ProgressDialogHelper.show(mContext);
        if ("流程实例编号".equals(dynamic.getDataType())) {
            Intent intent = new Intent();
            intent.setClass(mContext, FormInfoActivity.class);
            String url1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
            intent.putExtra("exturaUrl", url1);
            startActivity(intent);
        } else {
            if (TextUtils.isEmpty(dynamic.getDataType())) {
                return;
            }
            Intent intent = new Intent();
            switch (dynamic.getDataType()) {
                case "流程实例编号":
                    intent.setClass(mContext, FormInfoActivity.class);
                    String url1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                    intent.putExtra("exturaUrl", url1);
                    break;
                case "日志消息":
                    intent.setClass(mContext, LogInfoActivity.class);
                    break;
                case "任务消息":
                    intent.setClass(mContext, TaskInfoActivityNew.class);
                    break;
                case "通知消息":
                    intent.setClass(mContext, NoticeInfoActivity.class);
                    break;
                case "客户消息":
                    intent.setClass(mContext, ClientRelatedInfoActivity.class);
                    break;
                case "项目联系提醒":
                case "客户联系提醒":
                    intent.setClass(mContext, AddRecordActivity.class);
                    break;
                case "离线消息":
                    intent.setClass(mContext, NavActivity.class);
//                intent.setClass(mContext, ChatActivity.class);
//                GroupSession session = new GroupSession();
//                session.setChatId(dynamic.getDataId());
//                intent.putExtra("chatUser", session);
//                intent.putExtra("isPush", true);
                    break;
                case "URL":
                    intent.setClass(mContext, WebviewNormalActivity.class);
                    intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dynamic.getDataType());
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, dynamic.getUrl());
                    break;
                case "空间消息":
                    String url = Global.BASE_JAVA_URL + GlobalMethord.空间详情H5 + dynamic.getDataId();
                    intent.setClass(mContext, WebviewNormalActivity.class);
                    intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dynamic.getDataType());
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
                    break;
                case "今日待办任务":
                    intent.setClass(mContext, TaskListActivityNew.class);
                    break;
                case "BUG消息":
                    intent.setClass(mContext, BugInfoActivity.class);
                    break;
                case "应付提醒" :
                    intent.setClass(mContext, FormInfoActivity.class);
                    String url2 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                    intent.putExtra("exturaUrl", url2);
                    intent.putExtra("formDataId", dynamic.getDataId());
                    intent.putExtra("isShowCancelPush", true);
                    break;

            }
            intent.putExtra("dynamicInfo", dynamic);
            startActivity(intent);
        }
    }


    /**
     * 根据返回的数据获取一个实体
     *
     * @param response
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T getInfoClass(String response, Class<T> clazz) {
        T t = null;
        List<T> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), clazz);
        if (list != null && list.size() > 0) {
            t = list.get(0);
        }
        if (t != null) {
            return t;
        }
        return t;
    }

}
