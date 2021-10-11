package com.biaozhunyuan.tianyi.apply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.WorkflowTemplate;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/28.
 * 新建申请列表
 */

public class NewApplyListActivity extends Activity {

    private Context context;

    private BoeryunHeaderView headerView;
    private NoScrollListView lv;

    private List<WorkflowTemplate> applyList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_apply_list);
        context = getBaseContext();
        initViews();
        initData();
        getAppList();
        setOnEvent();
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_new_apply_list);
        lv = (NoScrollListView) findViewById(R.id.lv_new_apply_list);
    }

    private void initData() {

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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkflowTemplate template = applyList.get(position);
                Intent intent = new Intent(NewApplyListActivity.this, FormInfoActivity.class);
                intent.putExtra("formName", template.getFormName());
                intent.putExtra("workflowTemplateId", template.getUuid());
                intent.putExtra("formDataId", "0");
                intent.putExtra("createrId", "");
                startActivity(intent);
                finish();
            }
        });
    }


    private CommanAdapter<WorkflowTemplate> getAdapter(final List<WorkflowTemplate> list) {
        return new CommanAdapter<WorkflowTemplate>(list, context, R.layout.item_new_apply_list) {
            @Override
            public void convert(int position, WorkflowTemplate item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.name_new_apply_list, item.getFormName());
            }
        };
    }


    /**
     * 获取新建申请列表
     */
    private void getAppList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.新建申请列表;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                try {
                    applyList = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(result, "考勤类"), WorkflowTemplate.class);
                    if (applyList != null) {
                        lv.setAdapter(getAdapter(applyList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
