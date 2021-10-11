package com.biaozhunyuan.tianyi.apply;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.common.global.GlobalMethord;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 申请与审批页卡切换适配器
 */
public class ApplyViewPagerAdapter extends FragmentStatePagerAdapter {

    private String mFilter;

    private HashMap<String, AskMeFragment> mMap = new HashMap<String, AskMeFragment>();

    /**
     * 接口名数组：待我审批、我的申请、抄送列表、我已审批
     * 所有 ：Flow/GetAllFlow/
     */
    private String[] methodNames = new String[]{
            GlobalMethord.待我审批, GlobalMethord.我发起的, GlobalMethord.抄送列表, GlobalMethord.我审批的};
//    private String[] methodCRMNames = new String[]{
//            GlobalMethord.CRM待我审批, GlobalMethord.CRM我发起的, GlobalMethord.CRM抄送列表, GlobalMethord.CRM我审批的};

    public ApplyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ApplyViewPagerAdapter(FragmentManager fm, String filter) {
        super(fm);
        this.mFilter = filter;
    }

    @Override
    public AskMeFragment getItem(int pos) {
        AskMeFragment frament = new AskMeFragment(mFilter);
        frament.mMethodName = methodNames[pos];
        mMap.put(methodNames[pos], frament);
        Bundle args = new Bundle();
        args.putString(AskMeFragment.EXTRAS_ARG, mFilter);
        frament.setArguments(args);
        return frament;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = ((Fragment) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((Fragment) obj).getView();
    }

    public AskMeFragment getItemByPos(int pos) {
        return mMap.get(methodNames[pos]);
    }

    /**
     * 给fragment设置员工编号。
     *
     * @param value
     */
    public void setValue(String value, int position) {
        AskMeFragment askMeFragment = getItemByPos(position);
        if (askMeFragment != null) {
            askMeFragment.setValue(value);
        }
    }

    public void reloadData(int position) {
//        AskMeFragment askMeFragment = getItemByPos(position);
//        if (askMeFragment != null) {
//            askMeFragment.reloadData();
//        }
        Set<Map.Entry<String, AskMeFragment>> entries = mMap.entrySet();
        Iterator<Map.Entry<String, AskMeFragment>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AskMeFragment> next = iterator.next();
            next.getValue().reloadData();
        }
    }

    @Override
    public int getCount() {
        return methodNames.length;
    }

}
