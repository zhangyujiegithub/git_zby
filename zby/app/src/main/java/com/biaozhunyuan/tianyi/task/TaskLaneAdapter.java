package com.biaozhunyuan.tianyi.task;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;

import java.util.List;

/**
 * Created by wam on 2017/3/13.
 * 任务泳道图的任务列表的适配器
 */
public class TaskLaneAdapter extends BaseAdapter {
    private List<Task> mList;
    private Context mContext;
    private DictionaryHelper helper;

    public TaskLaneAdapter(List<Task> list, Context context) {
        mList = list;
        mContext = context;
        helper = new DictionaryHelper(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Task item = mList.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_task_lane_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (AvatarImageView) convertView.findViewById(R.id.header_task_lane_item);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content_task_lane_item);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_client_name_contact_lane);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUtils.displyUserPhotoById(mContext, item.getCreatorId(), viewHolder.imageView);
        viewHolder.tv_content.setText(item.getContent());
        viewHolder.tv_name.setText(helper.getUserNameById(item.getCreatorId()));
        return convertView;
    }

    private class ViewHolder {
        private AvatarImageView imageView;
        private TextView tv_content;
        private TextView tv_name;
    }
}
