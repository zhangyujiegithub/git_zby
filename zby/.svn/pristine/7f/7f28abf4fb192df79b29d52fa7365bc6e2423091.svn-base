package com.biaozhunyuan.tianyi.newuihome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.CustomerDetailsActivity;
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.log.LogInfoActivity;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.Notice;
import com.biaozhunyuan.tianyi.notice.NoticeInfoActivity;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.task.TaskInfoActivityNew;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * 消息
 */

public class MessageFragment extends Fragment {

    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Dynamic> demand;

    private List<Dynamic> list;
    private DictionaryHelper helper;
    private CommanAdapter<Dynamic> adapter;
    private int pageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, null);
//        initDemand();
        //判断当前设备版本号是否为4.4以上，如果是，则通过调用setTranslucentStatus让状态栏变透明
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatus(true);
//        }
        helper = new DictionaryHelper(getActivity());
        lv = view.findViewById(R.id.lv_fragment_dynamic);
        lv.setDivider(null);
        initDemand();
        setOnEvent();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getList();
        }
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态列表 + Global.mUser.getUuid();
        demand = new Demand(Dynamic.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "createTime";
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
                Dynamic dynamic = adapter.getItem(position - 1);

                if (dynamic != null) {
                    getDynamicInfo(dynamic);

                }
            }
        });
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        ProgressDialogHelper.show(getActivity());
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
                        lv.loadAllData();
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
        return new CommanAdapter<Dynamic>(list, getActivity(), R.layout.item_dynamic) {
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
//        ProgressDialogHelper.show(getActivity());
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态详情 + "?dataId=" + dynamic.getDataId() + "&dataType=" + dynamic.getDataType();
        if ("流程实例编号".equals(dynamic.getDataType())) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), FormInfoActivity.class);
            String url1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
            intent.putExtra("exturaUrl", url1);
            startActivity(intent);
        } else {
            StringRequest.getAsyn(url, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    ProgressDialogHelper.dismiss();
                    try {
                        Intent intent = new Intent();
                        switch (dynamic.getDataType()) {
                            case "日志消息":
                                intent.setClass(getActivity(), LogInfoActivity.class);
                                WorkRecord log = getInfoClass(response, WorkRecord.class);
                                intent.putExtra("logInfo", log);
                                break;
                            case "任务消息":
                                intent.setClass(getActivity(), TaskInfoActivityNew.class);
                                Task task = getInfoClass(response, Task.class);
                                intent.putExtra("taskInfo", task);
                                break;
                            case "通知消息":
                                intent.setClass(getActivity(), NoticeInfoActivity.class);
                                Notice notice = getInfoClass(response, Notice.class);
                                intent.putExtra("noticeItem", notice);
                                break;
                            case "客户消息":
                                intent.setClass(getActivity(), CustomerDetailsActivity.class);
                                Client client = getInfoClass(response, Client.class);
                                intent.putExtra(CustomerDetailsActivity.EXTRA_CLIENT, client);
                                break;
                            case "项目联系提醒":
                            case "客户联系提醒":
                                intent.setClass(getActivity(), AddRecordActivity.class);
                                Contact contact = getInfoClass(response, Contact.class);
                                intent.putExtra("contactInfo", contact);
                                break;
                            case "应付提醒" :
                                intent.setClass(getActivity(), FormInfoActivity.class);
                                String url2 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                                intent.putExtra("exturaUrl", url2);
                                intent.putExtra("formDataId", dynamic.getDataId());
                                intent.putExtra("isShowCancelPush", true);
                                break;
                        }
                        intent.putExtra("dynamicInfo", dynamic);
                        startActivity(intent);
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
