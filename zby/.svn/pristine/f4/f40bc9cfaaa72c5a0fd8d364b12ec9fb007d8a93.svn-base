package com.biaozhunyuan.tianyi.view.bragboard.model;

import android.support.annotation.IntRange;

import com.biaozhunyuan.tianyi.task.WorkScheduleList;

import java.util.List;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/11/30
 * @discription null
 * @usage null
 */
public interface DragColumn {
    List<? extends WorkScheduleList> getItemList();

    @IntRange(from = 0)
    int getColumnIndex();

    void setColumnIndex(@IntRange(from = 0) int columnIndexInHorizontalRecycleView);
}
