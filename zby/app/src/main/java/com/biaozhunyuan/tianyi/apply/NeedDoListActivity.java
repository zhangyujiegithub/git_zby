package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
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
 * 日期： 2020-07-03 14:07
 * 描述： 中国结算待办列表页面
 */
public class NeedDoListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private ViewPager pager;

    private Context mContext;
    private String[] titles = new String[]{"收文待办", "发文待办", "签报待办", "跨地待办", "软件开发", "部门审批"};
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_do_list);
        initView();
        initData();
        setOnEvent();
    }


    private void initView() {
        headerView = findViewById(R.id.header);
        indicator = findViewById(R.id.indicator);
        pager = findViewById(R.id.view_pager);
    }

    private void initData() {
        mContext = NeedDoListActivity.this;
        indicator.setTabItemTitles(titles);
        indicator.setViewPager(pager, 0);
        fragments = new ArrayList<>();
        fragments.add(ApplyListFragment.getInstance("oa/governmentDocument/receiveManage/getReceiveList?nodeId=-1")); //收文待办
        fragments.add(ApplyListFragment.getInstance("oa/governmentDocument/sendManage/getReceiveList?nodeId=-1")); //发文待办
        fragments.add(ApplyListFragment.getInstance("oa/governmentDocument/receiveManage/getReceiveList?nodeId=-1"));
        fragments.add(ApplyListFragment.getInstance("oa/governmentDocument/receiveManage/getReceiveList?nodeId=-1"));
        fragments.add(ApplyListFragment.getInstance("oa/governmentDocument/receiveManage/getReceiveList?nodeId=-1"));
        fragments.add(ApplyListFragment.getInstance("oa/governmentDocument/receiveManage/getReceiveList?nodeId=-1"));

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
        pager.setOffscreenPageLimit(4);//预加载
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
