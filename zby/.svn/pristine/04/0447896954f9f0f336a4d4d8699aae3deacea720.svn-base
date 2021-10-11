package com.biaozhunyuan.tianyi.cnis.senddocfragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.cnis.model.BacklogDoc;
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
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 发文列表页面
 *
 * @author kjx
 * @since 2015/03/12
 */
@SuppressLint("ValidFragment")
public class BacklogListFragment extends LazyFragment {


    //category
    private static String CATEGORY = "f4c2c84c0b90418fae46ff5915b63c0x";
    //fields
    private static String FIELDS = "文号";


    private Context context;
    private DictionaryHelper dictionaryHelper;
    private Demand demand;
    public static boolean isResume = false;

    private PullToRefreshAndLoadMoreListView lv;
    private RelativeLayout rl_nodata;


    private List<BacklogDoc> list;
    private CommanAdapter<BacklogDoc> adapter;

    private int pageIndex = 1; //页码


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        View view = View.inflate(context, R.layout.fragment_send_doc_backlog_list, null);
        initViews(view);
        initDemand();
//        ProgressDialogHelper.show(context);
//        getApplyList();
        setOnEvent();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);

    }


    @Override
    public void onStart() {
        super.onStart();
        ProgressDialogHelper.show(context);
        getApplyList();
    }

    private void initViews(View view) {
        lv = (PullToRefreshAndLoadMoreListView) view.findViewById(R.id.lv_send_doc_backlog_list);
        rl_nodata = (RelativeLayout) view.findViewById(R.id.rl_nodata);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isResume = false;
    }

    public void reloadData() {
        pageIndex = 1;
        getApplyList();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    private void setOnEvent() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getApplyList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getApplyList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    BacklogDoc template = adapter.getDataList().get(position - 1);
                    Intent intent = new Intent(context, FormInfoActivity.class);
                    intent.putExtra("formName", template.get标题());
                    intent.putExtra("formDataId", template.getFormDataId());
                    intent.putExtra("createrId", template.getCreatorId());
                    intent.putExtra("workflowTemplateId", template.getWorkflowTemplate());
                    startActivity(intent);
                }
            }
        });

    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.标准院待办列表;
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("category", CATEGORY);
        keyMap.put("fields", FIELDS);

        demand = new Demand();
        demand.pageSize = 10;
        demand.pageIndex = pageIndex;
        demand.sort = "desc";
        demand.sortField = "lastUpdateTime";
        demand.dictionaryNames = "creatorId.base_staff,prevStepAuditorId.base_staff";
        demand.keyMap = keyMap;
        demand.src = url;
    }


    /**
     * 获取申请列表
     */
    private void getApplyList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), BacklogDoc.class);
                    for (BacklogDoc backlogDoc : list) {
                        backlogDoc.set拟稿人(demand.getDictName(backlogDoc, "creatorId"));
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

    private CommanAdapter<BacklogDoc> getAdapter(List<BacklogDoc> list) {
        return new CommanAdapter<BacklogDoc>(list, context, R.layout.item_backlog_doc_list) {
            @Override
            public void convert(int position, BacklogDoc item, BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setTextValue(R.id.tv_creator_backlog_doc_item, item.get拟稿人()); //创建人

                    viewHolder.setTextValue(R.id.tv_item_backlog_doc_type, item.getCode()); //类型

                    viewHolder.setTextValue(R.id.tv_name_contact_item, "接收时间"); //接收时间标题

                    viewHolder.setTextValue(R.id.tv_creatTime_backlog_doc_item, item.getLastUpdateTime()); //接收时间

                    viewHolder.setTextValue(R.id.tv_no_backlog_doc_item, item.get文号()); //文号

                    viewHolder.setTextValue(R.id.content_backlog_doc_list, item.get标题()); //标题

                    viewHolder.setTextValue(R.id.tv_status_item_backlog_doc, item.getCurrentState()); //当前节点

                    if(TextUtils.isEmpty(item.get文号())){
                        viewHolder.getView(R.id.tv_no_backlog_doc_item).setVisibility(View.GONE);//文号
                    }

                    ImageUtils.displyUserPhotoById(context, item.getCreatorId(),
                            viewHolder.getView(R.id.head_item_backlog_doc_list), Color.parseColor("#3366CC"));

                }


            }
        };
    }

}
