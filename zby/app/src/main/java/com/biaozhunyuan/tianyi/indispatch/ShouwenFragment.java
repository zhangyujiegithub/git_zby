package com.biaozhunyuan.tianyi.indispatch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


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

public class ShouwenFragment extends FragmentActivity {

    private Context context;
    private String[] titles = new String[]{"待办收文", "待收收文", "已办收文", "全部收文"};

    private Indicator tabView;
    private BoeryunViewpager viewpager;
//    private HeaderFilter headerFilter;
    private List<Fragment> fragments = new ArrayList<>();
    DaiBanShouWenFragment daiBanShouWenFragment = new DaiBanShouWenFragment();
    DaiYueShouWenFragment daiYueShouWenFragment = new DaiYueShouWenFragment();
    YiBanShouWenFragment yiBanShouWenFragment = new YiBanShouWenFragment();
    QuanBuShouWenFragment quanBuShouWenFragment = new QuanBuShouWenFragment();
    private BoeryunHeaderView headerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_shouwen);
        initViews();
    }

    private void initViews() {
        tabView = findViewById(R.id.indicator_shouwen_list);
        viewpager = findViewById(R.id.pager_shouwen_list);
//        headerFilter = findViewById(R.id.header_filter_shouwen_fragment);
        headerView = findViewById(R.id.boeryun_headerview);
        tabView.setTabItemTitles(titles);
        tabView.setRelateViewPager(viewpager);

        fragments.add(daiBanShouWenFragment);
        fragments.add(daiYueShouWenFragment);
        fragments.add(yiBanShouWenFragment);
        fragments.add(quanBuShouWenFragment);
        viewpager.setEnabled(false);

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

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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


//        //过滤时间
//        headerFilter.setOnSelectDateListener(new HeaderFilter.onSelectDateListener() {
//            @Override
//            public void selectDate(String beginTime, String endTime) {
//                Logger.i("开始时间：" + beginTime + "，结束时间：" + endTime);
//                int currentPosition = viewpager.getCurrentItem();
//                if (currentPosition == 0) {
//                    daiBanShouWenFragment = (DaiBanShouWenFragment) fragments.get(currentPosition);
//                    daiBanShouWenFragment.setSelectDate(beginTime, endTime);
//                } else if (currentPosition == 1) {
//                    daiYueShouWenFragment = (DaiYueShouWenFragment) fragments.get(currentPosition);
//                    daiYueShouWenFragment.setSelectDate(beginTime, endTime);
//                } else if (currentPosition == 2) {
//                    yiBanShouWenFragment = (YiBanShouWenFragment) fragments.get(currentPosition);
//                    yiBanShouWenFragment.setSelectDate(beginTime, endTime);
//                } else if (currentPosition == 3) {
//                    quanBuShouWenFragment = (QuanBuShouWenFragment) fragments.get(currentPosition);
//                    quanBuShouWenFragment.setSelectDate(beginTime, endTime);
//                }
//            }
//        });
    }
}
