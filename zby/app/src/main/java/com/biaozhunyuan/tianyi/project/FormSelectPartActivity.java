package com.biaozhunyuan.tianyi.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 表单零部件选择列表
 */
public class FormSelectPartActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private ListView lv;
    private List<Project> list = new ArrayList<>();
    private CommanAdapter<Project> adapter;
    public static final String REQUEST_SELECT_PART = "REQUEST_SELECT_PART";
    private BoeryunSearchViewNoButton searchButton;
    private Demand<Project> demand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_select_part);
        initView();
        initDemand();
        getList();
        setOnTouchEvent();
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + "psi/inventoryEdit/getSkuList?type=all&uuid=全部&printModel=1&invMaster=1";
        demand = new Demand<>(Project.class);
        demand.src = url;
    }

    private void setOnTouchEvent() {
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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
        searchButton.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                Map<String, String> searchMap = new HashMap<>();
                if (!TextUtils.isEmpty(str)) {
                    searchMap.put("searchField_string_name", "1|" + str);
                    demand.keyMap = searchMap;
                    getList();
                } else {
                    searchMap.put("searchField_string_name", "");
                    demand.keyMap = searchMap;
                    getList();
                }
            }
        });
    }

    private void getList() {
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<Project> data = demand.data;
                adapter = getAdapter(data);
                lv.setAdapter(adapter);
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
        searchButton = findViewById(R.id.seach_button);
    }

    private CommanAdapter<Project> getAdapter(List<Project> gridItems) {
        return new CommanAdapter<Project>(gridItems, this, R.layout.item_form_select_part_list) {
            public void convert(int position, Project item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_productname, item.getCode()); //代码
                viewHolder.setTextValue(R.id.tv_producttype, item.getQtyRepertory() + "");//数量
                viewHolder.setTextValue(R.id.tv_productunit, item.getName()); //名称
                viewHolder.setTextValue(R.id.tv_productprice, item.getModel());//型号
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
        bundle.putSerializable(REQUEST_SELECT_PART, projectList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
