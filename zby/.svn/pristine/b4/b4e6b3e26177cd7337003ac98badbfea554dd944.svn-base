package com.biaozhunyuan.tianyi.contact;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;

import java.util.List;

/**
 * Created by wam on 2017/3/13.
 * 任务泳道图的任务列表的适配器
 */
public class ContactLaneAdapter extends BaseAdapter {
    private List<Contact> mList;
    private Context mContext;
    private List<ReturnDict> dicts;
    private DictionaryHelper helper;

    public ContactLaneAdapter(List<Contact> list, Context context, String dictionary) {
        mList = list;
        mContext = context;
        dicts = JsonUtils.getDictByName(dictionary, "customerId.crm_customer");
        helper = new DictionaryHelper(context);
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
        Contact item = mList.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_task_lane_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (AvatarImageView) convertView.findViewById(R.id.header_task_lane_item);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content_task_lane_item);
            viewHolder.tv_customer = (TextView) convertView.findViewById(R.id.tv_customer_task_lane_item);
            viewHolder.tv_client = (TextView) convertView.findViewById(R.id.tv_client_name_contact_lane);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_contact_lane);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageUtils.displyUserPhotoById(mContext,item.getAdvisorId() + "", viewHolder.imageView);
        viewHolder.tv_customer.setText(!TextUtils.isEmpty(item.getCustomerName()) ? item.getCustomerName(): StrUtils.pareseNull(item.getProjectName()));
        viewHolder.tv_content.setText(item.getContent());
//        viewHolder.tv_client.setText(JsonUtils.getDictValueById(dicts, item.getCustomerId()));
        viewHolder.tv_client.setText(helper.getUserNameById(item.getAdvisorId()));
        if(item.getContactTime().contains(" 00:00:00")){
            viewHolder.tv_time.setText(ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd"));
        }else {
            viewHolder.tv_time.setText(ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd HH:mm"));
        }

        return convertView;
    }

    private class ViewHolder {
        private AvatarImageView imageView;
        private TextView tv_content;
        private TextView tv_customer;
        private TextView tv_client;
        private TextView tv_time;
    }

    /**
     * 填充新的适配数据，清空原有数据
     */
    public void clearData() {
        mList.clear();
        notifyDataSetChanged();
    }
}
