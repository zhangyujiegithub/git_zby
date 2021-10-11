package com.biaozhunyuan.tianyi.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import java.util.ArrayList;
import java.util.List;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;

/**
 * Created by 王安民 on 2017/10/9.
 * 联系记录列表页面
 */

public class ContactRecordListActivity extends BaseActivity {

    private SimpleIndicator tabView;
    private BoeryunViewpager viewpager;
    private List<Fragment> mFragments;
    private BoeryunHeaderView headerView;
    private ConatactLaneFragment conatactLaneFragment;
    private boolean isShowRightTitle = true; //泳道图右上角是否显示更换项目或者客户联系记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initViews();
        setOnEvent();
    }

    private void initViews() {
        String cropName = getInsance().getValueBYkey("cropNmae");
        headerView = (BoeryunHeaderView) findViewById(R.id.header_contact_list);
        viewpager = (BoeryunViewpager) findViewById(R.id.viewpager_contact_view);
        tabView = findViewById(R.id.indicator_contact_view);
        headerView.setTitle(Global.CONTACT_TITLE);
        headerView.setRightTitle("项目泳道图");
        conatactLaneFragment = new ConatactLaneFragment();
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new ContactListFragment());
        mFragments.add(new ContactDayViewFragment());
//        mFragments.add(new ContactWeekViewFragment());
        tabView.setTabItemTitles(new String[]{"列表", "日视图", "泳道图"});//, "周视图",
        if(!cropName.equals("莱恩斯")){
            mFragments.add(conatactLaneFragment);
        } else {
            isShowRightTitle = false;
            mFragments.add(new NullFragment());
        }
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
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == ((Fragment) obj).getView();
            }
        });
        viewpager.setOffscreenPageLimit(2);//预加载
        tabView.setViewPager(viewpager, 0);
    }

    /**
     * 在泳道图中 判断当前为项目还是客户的联系记录
     * @return
     */
    public String getRightTitle(){
        return headerView.getRightTitleText();
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                ProgressDialogHelper.show(ContactRecordListActivity.this).show();
                conatactLaneFragment.updateData(headerView);
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
                startActivity(new Intent(ContactRecordListActivity.this, AddRecordActivity.class));
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2 && isShowRightTitle) {
                    headerView.setRightTitleVisible(true);
                } else {
                    headerView.setRightTitleVisible(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
