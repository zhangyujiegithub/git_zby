package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.WorkflowTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by lenovo on 2018/4/3.
 */

public class NewFormAdapter extends BaseAdapter {

    private Map<String, List<WorkflowTemplate>> templateMap;
    private Context context;
    private List<WorkflowTemplate> templates = new ArrayList<>();

    public NewFormAdapter(Map<String, List<WorkflowTemplate>> map, Context context) {
        this.context = context;
        templateMap = map;
        for (Map.Entry<String, List<WorkflowTemplate>> entries : map.entrySet()) {
            WorkflowTemplate template = new WorkflowTemplate();
            if (!TextUtils.isEmpty(entries.getKey())) {
                template.setFormClassify(true);
                template.setFormClassifyName(entries.getKey());
                templates.add(template);
            }
            templates.addAll(entries.getValue());
        }
    }

    @Override
    public int getCount() {
        return templates.size();
    }

    @Override
    public WorkflowTemplate getItem(int position) {
        return templates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ask_more_item, null);
        WorkflowTemplate item = getItem(position);
        View line = view.findViewById(R.id.line);
        View small_line = view.findViewById(R.id.small_line);
        View small_line1 = view.findViewById(R.id.small_line1);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon_new_apply);
        TextView tvName = (TextView) view.findViewById(R.id.tv_form_name);
//        ImageView ivWeb = (ImageView) view.findViewById(R.id.iv_form_web);
        ImageView iv_right = (ImageView) view.findViewById(R.id.iv_right);
        LinearLayout ll_parent = (LinearLayout) view.findViewById(R.id.ll_parent);
        iv_icon.setVisibility(View.GONE);
        if (item.isFormClassify()) {  //是分类标题
            ll_parent.setBackgroundColor(Color.parseColor("#ffffff"));
            small_line1.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
            small_line.setVisibility(View.VISIBLE);
            tvName.setText(item.getFormClassifyName());
            iv_icon.setBackground(context.getResources().getDrawable(R.drawable.icon_apply_new_financial));
            iv_icon.setVisibility(View.VISIBLE);
            iv_right.setVisibility(View.GONE);
            tvName.setTextColor(context.getResources().getColor(R.color.text_mine));
            tvName.setTextSize(16);
            view.setEnabled(false);
        } else {
            ll_parent.setBackground(context.getResources().getDrawable(R.drawable.selector_list_item));
            small_line1.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            small_line.setVisibility(View.GONE);
            view.setEnabled(true);
            tvName.setText("    " + item.getFormName());
            iv_right.setVisibility(View.VISIBLE);
            tvName.setTextColor(context.getResources().getColor(R.color.text_gray));
            tvName.setTextSize(14);
        }
        return view;
    }

    private Drawable getDrawable(String formName) {
        Drawable drawable = null;
        switch (formName) {
            case "发文类":
                drawable = context.getResources().getDrawable(R.drawable.icon_apply_new_dispatch);
                break;
            case "行政类":
                drawable = context.getResources().getDrawable(R.drawable.icon_apply_new_xingzheng);
                break;
            case "财务类":
                drawable = context.getResources().getDrawable(R.drawable.icon_apply_new_financial);
                break;
            default:
                drawable = context.getResources().getDrawable(R.drawable.icon_apply_new_unfiled);
                break;

        }
        return drawable;
    }

    public void setList(List<WorkflowTemplate> list) {
        this.templates = list;
    }
}