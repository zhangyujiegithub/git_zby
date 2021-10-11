package com.biaozhunyuan.tianyi.common.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommanAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private Context context;
    private int mLayoutId;

    /**
     * 通用内容适配器
     *
     * @param list      数据源
     * @param context   当前上下文
     * @param mLayoutId item布局资源id
     */
    public CommanAdapter(List<T> list, Context context, int mLayoutId) {
        super();
        this.context = context;
        this.mLayoutId = mLayoutId;
        if (list == null) {
            this.mList = new ArrayList<T>();
        } else {
            this.mList = list;
        }
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoeryunViewHolder vh = BoeryunViewHolder.getInstance(position,
                convertView, parent, context, mLayoutId);
        convert(position, mList.get(position), vh);


//        AnimateUtil.setFillingAnimate(vh.getConvertView());
        return vh.getConvertView();
    }

    /**
     * 获取当前数据源
     */
    public List<T> getDataList() {
        return mList;
    }

    /***
     * 向底部添加一条
     *
     * @param t
     * @param isClearOld
     */
    public void addBottom(T t, boolean isClearOld) {
        if (isClearOld) {
            mList.clear();
        }
        mList.add(t);
        notifyDataSetChanged();
    }

    /**
     * 在指定位置添加一条
     *
     * @param t        添加数据
     * @param position 指定位置下标
     */
    public void addData(T t, int position) {
        mList.add(position, t);
        notifyDataSetChanged();
    }

    /***
     * 向底部添加一个新的集合
     *
     * @param isClearOld 是否清空旧数据
     */
    public void addBottom(List<T> list, boolean isClearOld) {
        addBottom(mList.size(), list, isClearOld);
    }

    /***
     * 向底部添加一个新的集合
     *
     * @param isClearOld 是否清空旧数据
     */
    public void addBottom(int pos, List<T> list, boolean isClearOld) {
        if (isClearOld) {
            mList.clear();
            pos = 0;
        }
        mList.addAll(pos, list);
        notifyDataSetChanged();
    }

    /***
     * 向顶部添加一条新数据
     */
    public void addTop(T t) {
        mList.add(0, t);
    }

    /***
     * 向顶部添加一条新数据
     *
     * @param isClearOld 是否清空旧数据
     */
    public void addTop(T t, boolean isClearOld) {
        if (isClearOld) {
            mList.clear();
        }
        mList.add(0, t);
        notifyDataSetChanged();
    }

    // 向对顶部添加多条
    public void addTop(List<T> data, boolean isClearOld) {
        if (isClearOld) {
            mList.clear();
        }
        mList.addAll(0, data);
        notifyDataSetChanged();
    }

    /***
     * 根据序号移除其中某一条
     *
     * @param pos 序号
     */
    public void removeAtPos(int pos) {
        if (pos < mList.size()) {
            mList.remove(pos);
            notifyDataSetChanged();
        }
    }

    /***
     * 移除其中某一条
     *
     */
    public void remove(T t) {
        mList.remove(t);
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        mList.remove(pos);
        notifyDataSetChanged();
    }

    public void insert(int pos, T t) {
        mList.add(pos, t);
        notifyDataSetChanged();
    }

    /***
     * 移除部分集合的数据源
     *
     * @param list 数据源
     */
    public void removeList(List<T> list) {
        mList.removeAll(list);
        notifyDataSetChanged();
    }

    /**
     * 填充新的适配数据，清空原有数据
     *
     * @param data
     */
    public void changeData(List<T> data) {
        if (data != null) {
            mList = data;
        }
        notifyDataSetChanged();
    }

    /**
     * 填充新的适配数据，清空原有数据,并刷新适配器
     */
    public void clearData() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 填充新的适配数据，清空原有数据,不刷新适配器
     */
    public void clearData(boolean norefersh) {
        mList.clear();
    }

    public abstract void convert(int position, T item,
                                 BoeryunViewHolder viewHolder);


}
