package com.biaozhunyuan.tianyi.dispatch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.Indicator;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangAnMin on 2018/3/22.
 * 公文系统 收文列表页面
 */

public class FawenActivity extends FragmentActivity {

    private Context context;
    private String[] titles = new String[]{"待办发文", "我的发文", "已办发文", "全部发文"};

    private Indicator tabView;
    private BoeryunViewpager viewpager;
    private ImageView iv_back;
    private List<Fragment> fragments = new ArrayList<>();
    private BoeryunHeaderView headerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fawen);
        initViews();
    }

    private void initViews() {
        tabView = findViewById(R.id.indicator_shouwen_list);
        viewpager = findViewById(R.id.pager_shouwen_list);
        iv_back = findViewById(R.id.iv_back_fawen_activity);
        headerView = findViewById(R.id.boeryun_headerview);

        tabView.setTabItemTitles(titles);
        tabView.setRelateViewPager(viewpager);

        fragments.add(new DaiBanFaWenFragment());
        fragments.add(new YiBanFaWenFragment());
        fragments.add(new DaiYueFaWenFragment());
        fragments.add(new YiYueFaWenFragment());
        viewpager.setEnabled(true);

        viewpager.setOffscreenPageLimit(4);
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
