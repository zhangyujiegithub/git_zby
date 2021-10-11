package com.biaozhunyuan.tianyi.wechat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.address.AddressPagerAdapter;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： bohr
 * 日期： 2020-06-15 11:46
 * 描述：微信聊天联系人列表页面
 */
public class WeChatContactListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private ViewPager viewPager;

    private Context mContext;
    private String staffId;
    private String[] tabTitles = new String[]{"联系人", "群聊"}; //tab的标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_contact_list);
        getIntentData();
        initViews();
        initData();
        setOnEvent();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            staffId = getIntent().getStringExtra("UserId");
        }
    }

    private void initViews() {
        headerView = findViewById(R.id.header_contact_list);
        indicator = findViewById(R.id.incator_title_address_list);
        viewPager = findViewById(R.id.viewPager_address_list);
    }

    private void initData() {
        mContext = WeChatContactListActivity.this;
        indicator.setTabItemTitles(tabTitles);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(WeChatContactListFragment.getInstance(staffId));
        fragmentList.add(WeChatRoomListFragment.getInstance(staffId));
        FragmentManager fm = getSupportFragmentManager();
        AddressPagerAdapter pagerAdapter = new AddressPagerAdapter(fm, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager, 0);
    }


    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {

            }

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

