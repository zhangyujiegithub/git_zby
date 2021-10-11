package com.biaozhunyuan.tianyi.log;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.common.MyItemClickListener;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;

import java.util.List;

/**
 * Created by 王安民 on 2017/8/21.
 * <p>
 * 日志列表的适配器
 */

public class LogItemAdapter extends RecyclerView.Adapter<LogItemAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<WorkRecord> recordList;
    private MyItemClickListener itemClickListener;

    /**
     * 通用内容适配器
     *
     * @param list    数据源
     * @param context 当前上下文
     */
    public LogItemAdapter(List list, Context context) {
        this.recordList = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_log_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        WorkRecord item = recordList.get(position);
//        Date date = ViewHelper.formatStrToDateAndTime(item.getLastUpdateTime(), "yyyy-MM-dd kk:mm:ss");

        holder.tv_time.setText(ViewHelper.getDateStringFormat(item.getCreationTime()));
        holder.tv_content.setText(item.getContent());
        holder.tv_creator.setText(new DictionaryHelper(context).getUserNameById(item.getCreatorId()));
        ImageUtils.displyImageById(new DictionaryHelper(context).getUserPhoto(item.getCreatorId()), holder.iv_head);

        if (!TextUtils.isEmpty(item.getAttachmentIds())) {
            String[] arr = item.getAttachmentIds().split(",");
            if (arr.length > 0) {
                holder.ll_attach.setVisibility(View.VISIBLE);
                holder.tv_content.setText(arr.length + "个附件");
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
        return recordList == null ? 0 : recordList.size();
    }

    public void setOnitemClickListener(MyItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_time;
        TextView tv_content;
        TextView tv_attch_count;
        TextView tv_creator;
        LinearLayout ll_attach;
        CircleImageView iv_head;
        MyItemClickListener listener;

        ViewHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time_log_item);
            tv_content = (TextView) view.findViewById(R.id.content_log_list);
            tv_attch_count = (TextView) view.findViewById(R.id.tv_attch_count_log_list);
            tv_creator = (TextView) view.findViewById(R.id.tv_name_log_item);
            ll_attach = (LinearLayout) view.findViewById(R.id.ll_attch_log_list);
            iv_head = (CircleImageView) view.findViewById(R.id.head_item_log_list);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.onItemClick(v, getPosition());
            }
        }
    }
}
