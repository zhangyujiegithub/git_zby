package com.biaozhunyuan.tianyi.cnis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.cnis.sealusefragment.AppliedSealListFragment;
import com.biaozhunyuan.tianyi.cnis.sealusefragment.BacklogSealListFragment;
import com.biaozhunyuan.tianyi.cnis.sealusefragment.HandleSealListFragment;
import com.biaozhunyuan.tianyi.cnis.sealusefragment.QuerySealListFragment;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 印章使用管理
 * @author GaoB
 * @description:
 * @date : 2020/11/23 9:47
 */
public class SealUseActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private ViewPager pager;
    private String[] titles = new String[]{"已填写申请", "待办申请", "已办申请","部门用章查询"};
    private List<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seal_use);
        initView();
        setEvent();
    }


    private void initView() {
        indicator = findViewById(R.id.incator_title_seal_use_list);
        headerView = findViewById(R.id.header_seal_use_list);
        pager = findViewById(R.id.pager_seal_use_list);
        initFragment();
    }


    private void initFragment() {

        indicator.setTabItemTitles(titles);
        fragments = new ArrayList<>();

        AppliedSealListFragment appliedSealListFragment = new AppliedSealListFragment();
        BacklogSealListFragment backlogSealListFragment = new BacklogSealListFragment();
        HandleSealListFragment handleSealListFragment = new HandleSealListFragment();
        QuerySealListFragment querySealListFragment = new QuerySealListFragment();

        fragments.add(appliedSealListFragment);
        fragments.add(backlogSealListFragment);
        fragments.add(handleSealListFragment);
        fragments.add(querySealListFragment);

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == ((Fragment) obj).getView();
            }
        });
        indicator.setViewPager(pager, 0);
    }


    private void setEvent() {

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
}
