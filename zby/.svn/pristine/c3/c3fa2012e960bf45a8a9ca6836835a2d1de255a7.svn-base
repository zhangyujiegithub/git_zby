package com.biaozhunyuan.tianyi.cnis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.cnis.senddocfragment.BacklogListFragment;
import com.biaozhunyuan.tianyi.cnis.senddocfragment.HandleListFragment;
import com.biaozhunyuan.tianyi.cnis.senddocfragment.SendListFragment;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 发文管理
 * @author GaoB
 * @description:
 * @date : 2020/11/23 9:41
 */
public class SendDocManageActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private ViewPager pager;
    private String[] titles = new String[]{"发文列表", "待办列表", "已办列表"};
    private List<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_doc);
        initView();
        setEvent();
    }


    private void initView() {
        indicator = findViewById(R.id.incator_title_send_doc_list);
        headerView = findViewById(R.id.header_send_doc_list);
        pager = findViewById(R.id.pager_send_doc_list);
        initFragment();
    }


    private void initFragment() {

        indicator.setTabItemTitles(titles);

        fragments = new ArrayList<>();
        SendListFragment sendListFragment = new SendListFragment();
        BacklogListFragment backlogListFragment = new BacklogListFragment();
        HandleListFragment handleListFragment = new HandleListFragment();
        fragments.add(sendListFragment);
        fragments.add(backlogListFragment);
        fragments.add(handleListFragment);
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

        pager.setOffscreenPageLimit(0);
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
