package com.biaozhunyuan.tianyi.workorder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 工单列表
 */
public class WorkOrderListActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private SimpleIndicator indicatior;
    private ViewPager viewpager;
    private String strings[] = {"进行中","已完成"};
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_list);
        initView();
        initData();
        setOnTouch();
    }
    private void setOnTouch() {
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
                String url = Global.BASE_JAVA_URL + GlobalMethord.工单获取流程编号;
                StringRequest.getAsyn(url, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        List<WorkOrder> workOrders = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), WorkOrder.class);
                        for(WorkOrder w : workOrders){
                            if(w.getBuiltin().equals("workorder")){
                                Intent intent = new Intent(WorkOrderListActivity.this, FormInfoActivity.class);
                                intent.putExtra("workflowTemplateId",w.getWorkflowTemplateId());
                                intent.putExtra("formDataId","0");
                                startActivity(intent);
                                break;
                            }
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
        });
    }
    private void initData() {
        indicatior.setTabItemTitles(strings);
        mFragments = new ArrayList<>();
        mFragments.add(new WorkOrderDoingFragment());
        mFragments.add(new WorkOrderFinishFragment());
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
            @Override
            public int getCount() {
                return mFragments.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((Fragment)object).getView();
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }
        });
        indicatior.setViewPager(viewpager,0);
        String jurisdictionList = PreferceManager.getInsance().getValueBYkey("JurisdictionList");
        if(jurisdictionList.contains("新建工单")){
            headerview.setRightIconVisible(true);
            headerview.setRightIconDrawable(R.drawable.icon_headerview_add);
        }
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        indicatior = findViewById(R.id.simpleindicatior);
        viewpager = findViewById(R.id.viewpager);
    }

}
