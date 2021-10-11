package com.biaozhunyuan.tianyi.apply;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： bohr
 * 日期： 2020-07-03 16:34
 * 描述： 通用的待办/已办申请列表页面
 */
public class CommonApplyListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private ViewPager pager;
    private List<String> titleList;
    private String unDoUrl;
    private String doUrl;
    private String title;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_apply_list);
        initView();
        getIntentData();
        setOnEvent();
    }

    private void initView() {
        headerView = findViewById(R.id.header);
        indicator = findViewById(R.id.indicator);
        pager = findViewById(R.id.view_pager);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra("title");
            //已办列表URL
            doUrl = getIntent().getStringExtra("doUrl");
            //已办列表URL
            unDoUrl = getIntent().getStringExtra("unDoUrl");
            titleList = new ArrayList<>();
            titleList.add("待办" + title);
            titleList.add("已办" + title);
            initData();
        }
    }

    private void initData() {
        headerView.setTitle(title);
        indicator.setTabItemTitles(titleList);
        fragments = new ArrayList<>();
        fragments.add(ApplyListFragment.getInstance(unDoUrl));
        fragments.add(ApplyListFragment.getInstance(doUrl));

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
    }
}
