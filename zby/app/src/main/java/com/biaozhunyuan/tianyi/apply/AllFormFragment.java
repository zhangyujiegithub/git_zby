package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.ClassificationTable;
import com.biaozhunyuan.tianyi.apply.model.WorkflowTemplate;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

/**
 * 新建申请
 */

public class AllFormFragment extends BaseActivity {
    public static final String TAG = "AllFormFragment";
    private Context context;
    private ListView lv;
    private ImageView iv_back;
    private BoeryunSearchViewNoButton searchView;
    private List<ClassificationTable> mList = new ArrayList<ClassificationTable>();

    private List<WorkflowTemplate> allForm = new ArrayList<>();
    private List<WorkflowTemplate> searchForm = new ArrayList<>();
    private Map<String, List<WorkflowTemplate>> listMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_form);
        initView();
    }

    private void initView() {
        context = AllFormFragment.this;
        iv_back = findViewById(R.id.imageViewCancel);
        lv = findViewById(R.id.lv_all_askform);
        searchView = findViewById(R.id.search_view_new_form);
        searchView.setHint("搜索表单名称");
        setOnClickListener();
        getAllFormList();

    }

    private void getAllFormList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.新建申请列表;
//        String apply = PreferceManager.getInsance().getValueBYkey(Global.mUser.getUuid() + "APPLY");
//        if(!TextUtils.isEmpty(apply)&&apply.equals("21fe612a54d842238c7cc2fbe58c4ed4")){
//            url = Global.BASE_JAVA_URL + GlobalMethord.CRM新建申请列表;
//        }
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                LogUtils.e(TAG, TAG + "请求失败");

            }

            @Override
            public void onResponseCodeErro(String result) {
                listMap = JsonUtils.JsonToMap(result, WorkflowTemplate.class);
                lv.setAdapter(new NewFormAdapter(listMap, context));

                if (listMap != null) {
                    Set<Map.Entry<String, List<WorkflowTemplate>>> entries = listMap.entrySet();
                    for (Map.Entry<String, List<WorkflowTemplate>> entry : entries) {
                        List<WorkflowTemplate> list = entry.getValue();
                        if (list != null) {
                            allForm.addAll(list);
                        }
                    }
                }
            }
        });
    }

    private void setOnClickListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                WorkflowTemplate template = (WorkflowTemplate) lv.getAdapter().getItem(position);
                if (!template.isFormClassify()) {
                    Intent intent = new Intent(context, FormInfoActivity.class);
                    intent.putExtra("formName", template.getFormName());
                    intent.putExtra("formDataId", "0");
                    intent.putExtra("createrId", template.getCreatorId());
                    intent.putExtra("workflowTemplateId", template.getUuid());
                    startActivity(intent);
                }
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (!TextUtils.isEmpty(str)) {
                    searchFormfreshLis(str);
                } else {
                    lv.setAdapter(new NewFormAdapter(listMap, context));
                }
            }
        });


    }


    /**
     * 搜索表单并且刷新列表
     *
     * @param str
     */
    private void searchFormfreshLis(String str) {
        searchForm.clear();
        for (WorkflowTemplate workflowTemplate : allForm) {
            if (workflowTemplate.getFormName().contains(str)) {
                searchForm.add(workflowTemplate);
            }
        }
        Map<String, List<WorkflowTemplate>> map = new HashMap<>();
        map.put("", searchForm);
        lv.setAdapter(new NewFormAdapter(map, context));

    }


}
