package com.biaozhunyuan.tianyi.cnis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 临时劳务页面
 */
public class LaoWuActivity extends BaseActivity {

    private BoeryunHeaderView header;
    private SimpleIndicator indicator;
    private ViewPager pager;

    private Context context;
    private String[] tabs = new String[]{"已办", "全部"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        context = LaoWuActivity.this;
        initViews();
        setOnEvent();
    }

    private void initViews() {
        header = findViewById(R.id.header);
        indicator = findViewById(R.id.indicator);
        indicator.setTabItemTitles(tabs);
        pager = findViewById(R.id.pager);
        indicator.setViewPager(pager, 0);
        header.setTitle("临时劳务招聘");


        List<Fragment> fragments = new ArrayList<>();
        fragments.add(LaoWuFragment.newInstance(false));
        fragments.add(LaoWuFragment.newInstance(true));

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
    }


    private void setOnEvent() {
        header.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {
                Intent intent = new Intent();
                intent.setClass(context, FormInfoActivity.class);
                intent.putExtra("workflowTemplateId", "3fe9e1c9cf6b4db5ba53574384ffd344");
                intent.putExtra("formDataId", "0");
                startActivity(intent);
            }
        });
    }
}
