package com.biaozhunyuan.tianyi.view.bragboard.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.task.OaWorkLaneList;
import com.biaozhunyuan.tianyi.task.WorkScheduleList;
import com.biaozhunyuan.tianyi.view.bragboard.callback.DragVerticalAdapter;
import com.biaozhunyuan.tianyi.view.bragboard.helper.DragHelper;
import com.biaozhunyuan.tianyi.view.bragboard.model.DragColumn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Request;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/4/3
 * @discription 垂直排列的子项卡片
 * @usage null
 */
public abstract class VerticalAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements DragVerticalAdapter {

    protected Context mContext;
    private List<WorkScheduleList> mData;
    private static final String ONDRAG_PAGER_POSITION_ID= "ONDRAG_PAGER_POSITION_ID";  //抓起卡片时当前列的uuid
    @NonNull
    private DragHelper dragHelper;

    private int mDragPosition;//正在拖动的 View 的 position
    private boolean mHideDragItem; // 是否隐藏正在拖动的 position
    private List<OaWorkLaneList> workLaneList;//泳道列表集合

    public VerticalAdapter(Context context, @NonNull DragHelper dragHelper,List<DragColumn> oaWorkLaneList) {
        this.mContext = context;
        this.mData = new ArrayList<>();
        this.dragHelper = dragHelper;
        workLaneList = new ArrayList<>();
        for (DragColumn column : oaWorkLaneList){
            OaWorkLaneList column1 = (OaWorkLaneList) column;
            workLaneList.add(column1);
        }
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (position == mDragPosition && mHideDragItem) {
            holder.itemView.setVisibility(View.INVISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        final WorkScheduleList item = mData.get(holder.getAdapterPosition());
        holder.itemView.setTag(item);

        onBindViewHolder(mContext, holder, item, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onDrag(int page,int position) {
        mDragPosition = position;
        mHideDragItem = true;
        //记录抓起卡片时当前列的uuid
        PreferceManager.getInsance().saveValueBYkey(ONDRAG_PAGER_POSITION_ID,workLaneList.get(page).getUuid());
        notifyItemChanged(position);
    }

    public void onDrop(int page, int position, WorkScheduleList tag) {
        mHideDragItem = false;
        notifyItemChanged(position);
        String currentLaneId = PreferceManager.getInsance().getValueBYkey(ONDRAG_PAGER_POSITION_ID);
        moveTaskCard(tag.getUuid(),currentLaneId,workLaneList.get(page).getUuid());
    }

    /**
     * 获取
     * @return
     */
    public List<OaWorkLaneList> getTaskLaneData(){
        return workLaneList;
    }

    @Override
    public void onDragOut() {
        if (mDragPosition >= 0 && mDragPosition < mData.size()) {
            mData.remove(mDragPosition);
            notifyDataSetChanged();// 此处如果用 notifyItemRemove 下一次选定时的 position 是错的
            mDragPosition = -1;
        }
    }

    public void onDragIn(int position, WorkScheduleList item) {
        if (position > mData.size()) {// 如果拖进来时候的 position 比当前 列表的长度大，就添加到列表末端
            position = mData.size();
        }
        mData.add(position, item);
        notifyItemInserted(position);
        mDragPosition = position;
        mHideDragItem = true;
    }

    @Override
    public void updateDragItemVisibility(int position) {
        if (mDragPosition >= 0 && mDragPosition < mData.size() && position < mData.size() && mDragPosition != position) {
            if (Math.abs(mDragPosition - position) == 1) {
                notifyItemChanged(mDragPosition);
                Collections.swap(mData, mDragPosition, position);
                mDragPosition = position;
                notifyItemChanged(position);
            } else {
                notifyItemChanged(mDragPosition);
                if (mDragPosition > position) {
                    for (int i = mDragPosition; i > position; i--) {
                        Collections.swap(mData, i, i - 1);
                        notifyItemChanged(i);
                    }
                } else {
                    for (int i = mDragPosition; i < position; i++) {
                        Collections.swap(mData, i, i + 1);
                        notifyItemChanged(i);
                    }
                }
                mDragPosition = position;
                notifyItemChanged(position);
            }
        }
    }

    public void dragItem(View columnView, int position) {
        dragHelper.dragItem(columnView, position);
    }

    public void dragItem(VH holder) {
        dragItem(holder.itemView, holder.getAdapterPosition());
    }

    public void setDragHelper(DragHelper dragHelper) {
        this.dragHelper = dragHelper;
    }

    public abstract void onBindViewHolder(Context context, VH holder, @NonNull WorkScheduleList item, final int position);

    public void setData(List<WorkScheduleList> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public List<WorkScheduleList> getData() {
        return mData;
    }

    /**
     * 移动任务卡片
     * @param workTaskInfoId 拖动的任务Id
     * @param currentLaneId 当前所在泳道Id
     * @param targetLaneId 拖到目标泳道Id
     */
    private void moveTaskCard(String workTaskInfoId,String currentLaneId,String targetLaneId){
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情操作卡片
                + "?workTaskInfoId=" + workTaskInfoId + "&currentLaneId=" + currentLaneId + "&targetLaneId=" + targetLaneId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext,"操作失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
