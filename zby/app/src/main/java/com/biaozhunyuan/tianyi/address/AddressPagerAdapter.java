package com.biaozhunyuan.tianyi.address;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 王安民 on 2017/8/28.
 * 通讯录viewpager的适配器
 */

public class AddressPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private FragmentManager fm;

    public AddressPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments != null) {
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragments != null) {
            return fragments.size();
        }
        return 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    //    private List<View> list;
//
//    public AddressPagerAdapter(List<View> list) {
//        this.list = list;
//    }
//
//    @Override
//    public int getMsgTotalCount() {
//        return list.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        container.addView(list.get(position));
//        return list.get(position);
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(list.get(position));
//    }
}
