package com.biaozhunyuan.tianyi.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.MyItemClickListener;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.model.Task;

import java.util.List;

/**
 * Created by 王安民 on 2017/8/23.
 * 任务过滤页面recycle的内容适配器
 */

public class TaskItemAdapter extends RecyclerView.Adapter<TaskItemAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<Task> taskList;
    private MyItemClickListener itemClickListener;

    /**
     * 通用内容适配器
     *
     * @param list    数据源
     * @param context 当前上下文
     */
    public TaskItemAdapter(List list, Context context) {
        this.taskList = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_task_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Task item = taskList.get(position);
//        Date date = ViewHelper.formatStrToDateAndTime(item.getLastUpdateTime(), "yyyy-MM-dd kk:mm:ss");

        holder.tv_time.setText(ViewHelper.getDateStringFormat(item.getCreationTime()));
        holder.tv_deadLine.setText(ViewHelper.convertStrToFormatDateStr(item.getEndTime(), "yyyy-MM-dd kk:mm"));
        holder.tv_content.setText(item.getContent());
        holder.tv_creator.setText(item.getCreatorName());
        holder.tv_excutor.setText(item.getExecutorNames());

        /**
         * 根据任务状态枚举类型显示状态
         */
        if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
            holder.iv_status.setImageResource(R.drawable.ic_task_done);
        } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
            holder.iv_status.setImageResource(R.drawable.ic_task_going);
        } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
            holder.iv_status.setImageResource(R.drawable.ic_task_cancle);
        } else {
            holder.iv_status.setImageResource(R.drawable.ic_task_late);
        }

        if (!TextUtils.isEmpty(item.getAttachmentIds())) {
            String[] arr = item.getAttachmentIds().split(",");
            if (arr.length > 0) {

                holder.ll_attach.setVisibility(View.VISIBLE);
                holder.tv_attch_count.setText(arr.length + "个附件");
            }
        } else {
            holder.ll_attach.setVisibility(View.GONE);
        }

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }

    public void setOnitemClickListener(MyItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_time;
        TextView tv_content;
        TextView tv_attch_count;
        TextView tv_excutor;
        TextView tv_creator;
        TextView tv_deadLine;
        LinearLayout ll_attach;
        ImageView iv_status;
        MyItemClickListener listener;

        ViewHolder(View view) {
            super(view);
            iv_status = (ImageView) view.findViewById(R.id.iv_status_task);
            tv_time = (TextView) view.findViewById(R.id.tv_time_task_item);
            tv_content = (TextView) view.findViewById(R.id.content_task_list);
            tv_creator = (TextView) view.findViewById(R.id.tv_creater_task_item);
            tv_excutor = (TextView) view.findViewById(R.id.tv_excutor_task_item);
            tv_deadLine = (TextView) view.findViewById(R.id.deadline_task_item);
            tv_attch_count = (TextView) view.findViewById(R.id.tv_attch_count_task_list);
            ll_attach = (LinearLayout) view.findViewById(R.id.ll_attch_task_list);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.onItemClick(v, getPosition());
            }
        }
    }
}
