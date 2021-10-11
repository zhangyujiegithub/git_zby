package com.biaozhunyuan.tianyi.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 表单采购明细
 */
public class FormSelectPurchaseActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private ListView lv;
    private List<Project> list = new ArrayList<>();
    private CommanAdapter<Project> adapter;
    public static final String REQUEST_SELECT_PURCHASE = "REQUEST_SELECT_PURCHASE";//转下一位审核人


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_select_purchase);
        String url = getIntent().getStringExtra("url");
        initView();
        getList(url);
        setOnTouchEvent();
    }

    private void setOnTouchEvent() {
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if(list.size()>0) {
                    returnResult(list);
                }else {
                    showShortToast("还没有选择");
                }
            }

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
                Project item = adapter.getItem(position);
//                if(item.isSelect()){
//                    list.remove(position);
//                }else {
//                    list.add(item);
//                }
//                item.setSelect(!item.isSelect());
//                adapter.notifyDataSetChanged();
                list.add(item);
                returnResult(list);
            }
        });
    }

    private void getList(String url) {
        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {
                try {
                    List<Project> data = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Project.class);
                    if(data!=null){
                        adapter = getAdapter(data);
                        lv.setAdapter(adapter);
                    }
                } catch (JSONException e) {
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

    private void initView() {
        headerview = findViewById(R.id.headerview);
        lv = findViewById(R.id.lv);
        headerview.setRightTitleVisible(false);
    }

    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems,this, R.layout.item_form_select_purchase_list) {
            public void convert(int position, Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_productname,item.getAssetName());
                viewHolder.setTextValue(R.id.tv_producttype,item.getSpecificationType());
                viewHolder.setTextValue(R.id.tv_productunit,item.getUnit());
                viewHolder.setTextValue(R.id.tv_productprice,item.getPrice());

//                if(item.isSelect()){
//                    viewHolder.setImageResoure(R.id.iv_select,R.drawable.ic_select);
//                }else {
//                    viewHolder.setImageResoure(R.id.iv_select,R.drawable.ic_cancle_select);
//                }
            }
        };
    }


    /**
     * 返回数据
     */
    private void returnResult(List<Project> list) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        ProjectList projectList = new ProjectList();
        projectList.setProjects(list);
        bundle.putSerializable(REQUEST_SELECT_PURCHASE,projectList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
