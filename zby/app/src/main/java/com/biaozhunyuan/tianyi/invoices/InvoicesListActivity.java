package com.biaozhunyuan.tianyi.invoices;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

/**
 * 单据
 */
@SuppressLint("NewApi")
public class InvoicesListActivity extends BaseActivity {

    private ViewPager viewpager;
    private BoeryunHeaderView headerView;
    private SimpleIndicator indicatior;
    private List<Fragment> mFragments;
    private List<Project> projectForm = new ArrayList<>();
    private List<Project> projectRemoveForm = new ArrayList<>();
    private String[] tableTitles = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_invoices_list);
        initView();
        initData();
        getTableTitle();
        setOnTouch();
    }

    private void getTableTitle() {
        ProgressDialogHelper.show(this);
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取单据详情标签页;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<Project> projectList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Project.class);
                for (Project p1 : projectList) {

                    if(projectForm.size() > 0){ //判断当前集合是否为空 为空的话直接添加一条
                        boolean isExist = false; //判断是否添加数据
                        for (int i = 0;i<projectForm.size();i++){ //遍历集合 判断集合中是否已经含有此条
                            if(projectForm.get(i).getTableName().equals(p1.getTableName())){
                                isExist = true; //如果已经存在 把标志位设为true
                                break;
                            }
                        }
                        if(!isExist){ //如果不存在添加此条客户的联系记录
                            projectForm.add(p1);
                        } else { //如果存在 标志位改为false继续走下一次循环
                            projectRemoveForm.add(p1);
                        }
                    }else {
                        projectForm.add(p1);
                    }

//                    boolean b = projectForm.stream().anyMatch(o -> o.getTableName().equals(p1.getTableName()));
//                    if (!b) {
//                        projectForm.add(p1);
//                    } else {
//                        projectRemoveForm.add(p1);
//                    }
                }
                for (Project p : projectForm){
                    for (Project p1 : projectRemoveForm){
                        if(p.getTableName().equals(p1.getTableName())){
                            p.setHost(p.getHost() + "," + p1.getHost());
                        }
                    }
                }
                if (projectForm.size() > 0) {
                    int length = tableTitles.length;
                    for (int i = 0; i < projectForm.size(); i++) {
                        tableTitles = Arrays.copyOf(tableTitles, length += 1);
                        tableTitles[length - 1] = projectForm.get(i).getFormName();
                        mFragments.add(InvoicesCommanListFragment.newInstance(projectForm.get(i)));
                    }
                }
                initData();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void setOnTouch() {
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

    private void initData() {
        indicatior.setTabItemTitles(tableTitles);
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
                return view == ((Fragment) object).getView();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }
        });

        viewpager.setOffscreenPageLimit(mFragments.size());//预加载
        indicatior.setViewPager(viewpager, 0);
        ProgressDialogHelper.dismiss();
    }

    private void initView() {
        mFragments = new ArrayList<>();

        viewpager = findViewById(R.id.viewpager);
        headerView = findViewById(R.id.boeryun_headerview);
        indicatior = findViewById(R.id.simpleindicatior);
    }

}
