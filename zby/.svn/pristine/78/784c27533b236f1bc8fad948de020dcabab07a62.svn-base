package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.Backlog;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 作者： GaoB
 * 日期： 2020-07-03 14:07
 * 描述： 标准院待办列表页面
 */
public class BacklogListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private Context mContext;
    private DictionaryHelper dictionaryHelper;

    private CommanAdapter<Backlog> adapter;
    private List<Backlog> list;
    private Demand demand;

    private int pageIndex = 1; //页码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog_list);
        dictionaryHelper = new DictionaryHelper(this);
        initView();
        initData();
        setOnEvent();
        ProgressDialogHelper.show(this);
        getBackLogList();
    }


    private void initView() {
        headerView = findViewById(R.id.header);
        lv = findViewById(R.id.lv_backlog_list);
    }

    private void initData() {
        mContext = BacklogListActivity.this;
        initDemand();
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.标准院我的待办;
        Map<String, String> keyMap = new HashMap<>();
//        keyMap.put("category", CATEGORY);
//        keyMap.put("fields", FIELDS);
        keyMap.put("isFirstJump", "1");


        demand = new Demand();
        demand.pageSize = 10;
        pageIndex = 1;
//        demand.sort = "desc";
        demand.sortField = "lastStepCompleteTime desc,submissionTime desc";
        demand.dictionaryNames="prevStepAuditorId.base_staff,creatorId.base_staff";
        demand.keyMap = keyMap;
        demand.src = url;
    }


    private void setOnEvent() {

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBackLogList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getBackLogList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Backlog template = adapter.getDataList().get(position - 1);
                    Intent intent = new Intent(BacklogListActivity.this, FormInfoActivity.class);
                    intent.putExtra("formName", "待办文件");
                    intent.putExtra("formDataId", template.getFormDataId());
                    intent.putExtra("createrId", template.getCreatorId());
                    intent.putExtra("workflowTemplateId", template.getWorkflowTemplate());
                    startActivity(intent);
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

    private void getBackLogList() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), Backlog.class);
                    for (Backlog backlog : list) {
                        backlog.setPrevStepAuditor(demand.getDictName(backlog, "prevStepAuditorId"));
                    }

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
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }


    private CommanAdapter<Backlog> getAdapter(final List<Backlog> list) {
        return new CommanAdapter<Backlog>(list, this, R.layout.item_backlog_list) {
            @Override
            public void convert(int position, final Backlog item, BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setTextValue(R.id.tv_creator_backlog_item, dictionaryHelper.getUserNameById(item.getCreatorId())); //创建人

                    viewHolder.setTextValue(R.id.tv_item_backlog_type, item.getFormName()); //类型


                    viewHolder.setTextValue(R.id.tv_doctyep_backlog_item, item.get文号()); //文号

                    viewHolder.setTextValue(R.id.tv_creatTime_backlog_item, item.getLastStepCompleteTime()); //接收时间

                    viewHolder.setTextValue(R.id.tv_prevAuditor_backlog_item, item.getPrevStepAuditor()); //上一办理人

                    viewHolder.setTextValue(R.id.content_backlog_list, item.getFormName() + item.getRemark()); //标题

                    viewHolder.getView(R.id.tv_status_item_backlog).setVisibility(View.GONE);


                    if(TextUtils.isEmpty(item.get文号())){
                        viewHolder.getView(R.id.tv_doctyep_backlog_item).setVisibility(View.GONE);//文号
                    }

                    ImageUtils.displyUserPhotoById(BacklogListActivity.this, item.getCreatorId(),
                            viewHolder.getView(R.id.head_item_backlog_list), Color.parseColor("#3366CC"));



//                   viewHolder.setTextValue(R.id.tv_status_item_backlog, item.getCurrentState()); //当前节点

                }
            }
        };
    }

}
