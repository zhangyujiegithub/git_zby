package com.biaozhunyuan.tianyi.apply;

import android.view.View;

import com.biaozhunyuan.tianyi.view.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class TagAdapter<T> {
    private List<T> mTagDatas;
    private OnDataChangedListener mOnDataChangedListener;
    private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

    public TagAdapter(List<T> datas) {
        mTagDatas = datas;
    }

    public TagAdapter(T[] datas) {
        mTagDatas = new ArrayList<T>(Arrays.asList(datas));
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public void setSelectedList(int... pos) {
        for (int i = 0; i < pos.length; i++)
            mCheckedPosList.add(pos[i]);
        notifyDataChanged();
    }

    public void setSelectedList(List<Integer> pos) {
        for (int i = 0; i < pos.size(); i++)
            mCheckedPosList.add(pos.get(i));
        notifyDataChanged();
    }

    public void setSelectedList(int pos) {
        mCheckedPosList.clear();
        mCheckedPosList.add(pos);
        notifyDataChanged();
    }

    public void setDeleteList(int... pos) {
        for (int i = 0; i < pos.length; i++)
            mCheckedPosList.remove(pos[i]);
        notifyDataChanged();
    }
    public List<T> getData(){
        return mTagDatas;
    }
    public void addBottom(List<T> list,boolean isClear){
        if(isClear){
            mTagDatas.clear();
        }
        mTagDatas.addAll(mTagDatas.size(),list);
        notifyDataChanged();
    }

    public HashSet<Integer> getSelectedList() {
        return mCheckedPosList;
    }

    public HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }

    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public void notifyDataChanged() {
        mOnDataChangedListener.onChanged();
    }

    public T getItem(int position) {
        return mTagDatas.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

    public interface OnDataChangedListener {
        void onChanged();
    }

}