package com.biaozhunyuan.tianyi.ybkj;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.models.CategoryBean;
import com.biaozhunyuan.tianyi.view.NumImageView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 右侧主界面ListView的适配器
 *
 * @author Administrator
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean> foodDatas;

    public HomeAdapter(Context context, List<CategoryBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }

    @Override
    public int getCount() {
        return foodDatas!=null ? foodDatas.size():0;
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold ;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = View.inflate(context, R.layout.item_home_category, null);
            viewHold.tv_name =  convertView.findViewById(R.id.item_home_name);
            viewHold.iv_icon =  convertView.findViewById(R.id.item_album);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        CategoryBean categoryBean=foodDatas.get(position);
        viewHold.tv_name.setText(categoryBean.getIconName());
        Glide.with(context)
                .load(categoryBean.getIcon())
                .into(viewHold.iv_icon);
        return convertView;
    }

    private static class ViewHold {
        private TextView tv_name;
        private NumImageView iv_icon;
    }

}
