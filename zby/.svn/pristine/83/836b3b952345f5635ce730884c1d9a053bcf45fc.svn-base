package com.biaozhunyuan.tianyi.cnis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.cnis.model.ReadDocRegistration;
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
 * 阅件登记页面
 *
 * @author GaoB
 * @description:
 * @date : 2020/11/23 9:44
 */
public class ReadDocRegistrationActivity extends BaseActivity {

    //title
    private static String DOC_TITLE = "阅件登记";
    //workflowTemplateId
    private static String workflowTemplateId = "9c2558a510c042778e6ajg6c36bb4321";
    //formName
    private static String FORMNAME = "阅件登记";
    //status
    private static String STATUS = "0";
    //showDelete
    private static String SHOWDELETE = "1";
    //showCancel
    private static String SHOWCANCEL = "1";

    private DictionaryHelper dictionaryHelper;


    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private RelativeLayout rl_nodata;
    private CommanAdapter<ReadDocRegistration> adapter;
    private List<ReadDocRegistration> list;
    private Demand demand;
    private int pageIndex = 1; //页码


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_registration);
        dictionaryHelper = new DictionaryHelper(this);
        initView();
        initData();
        setOnEvent();
        ProgressDialogHelper.show(this);
        getReadDocRegistration();
    }


    private void initView() {
        headerView = findViewById(R.id.header_read_registration_list);
        lv = findViewById(R.id.lv_read_registration_list);
        rl_nodata = findViewById(R.id.rl_nodata);
    }

    private void initData() {
        initDemand();
    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.标准院公文列表;
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("title", DOC_TITLE);
        keyMap.put("workflowTemplateId", workflowTemplateId);
        keyMap.put("formName", FORMNAME);
        keyMap.put("status", STATUS);
        keyMap.put("showDelete", SHOWDELETE);
        keyMap.put("showCancel", SHOWCANCEL);
        demand = new Demand();
        demand.pageSize = 10;
        pageIndex = 1;
        demand.dictionaryNames = "发文单位.base_department";
        demand.src = url;
        demand.keyMap = keyMap;
    }


    private void setOnEvent() {

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getReadDocRegistration();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getReadDocRegistration();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ReadDocRegistration doc = adapter.getDataList().get(position - 1);
                    Intent intent = new Intent(ReadDocRegistrationActivity.this, FormInfoActivity.class);
                    intent.putExtra("formName", FORMNAME);
                    intent.putExtra("formDataId", doc.getUuid());
                    intent.putExtra("createrId", doc.getCreatorId());
                    intent.putExtra("workflowTemplateId", doc.getWorkflowTemplate());
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


    private void getReadDocRegistration() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), ReadDocRegistration.class);
                    for (ReadDocRegistration readDocRegistration : list) {
                        readDocRegistration.setDepartName(demand.getDictName(readDocRegistration, "发文单位"));
                    }
                    lv.onRefreshComplete();
                    if (list != null) {
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                            if (list.size() > 0) {
                                rl_nodata.setVisibility(View.GONE);
                                lv.setVisibility(View.VISIBLE);
                            } else {
                                rl_nodata.setVisibility(View.VISIBLE);
                                lv.setVisibility(View.GONE);
                            }
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

    private CommanAdapter<ReadDocRegistration> getAdapter(final List<ReadDocRegistration> list) {
        return new CommanAdapter<ReadDocRegistration>(list, this, R.layout.item_read_doc_registration_list) {
            @Override
            public void convert(int position, final ReadDocRegistration item, BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setTextValue(R.id.tv_creator_read_doc_registration_item, dictionaryHelper.getUserNameById(item.getCreatorId())); //拟稿人

                    viewHolder.setTextValue(R.id.tv_item_read_doc_registration_type, item.getDepartName());//单位

                    viewHolder.setTextValue(R.id.tv_creatTime_read_doc_registration_item, item.getCreateTime()); //时间

                    viewHolder.setTextValue(R.id.content_read_doc_registration_list, item.get标题()); //标题

                    viewHolder.setTextValue(R.id.tv_status_item_read_doc_registration, item.getCurrentState()); //当前节点

                    ImageUtils.displyUserPhotoById(ReadDocRegistrationActivity.this, item.getCreatorId(),
                            viewHolder.getView(R.id.head_item_read_doc_registration_list), Color.parseColor("#3366CC"));

                }
            }
        };
    }


}
