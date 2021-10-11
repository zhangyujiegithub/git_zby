package com.biaozhunyuan.tianyi.attch;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;

import java.util.List;

public class InfoAdapter extends BaseAdapter {

    View.OnClickListener myAdapterCBListener;
    private List<ReturnDict> mlist;
    private Context mContext;

    public InfoAdapter(Context pContext, List<ReturnDict> list, OnClickListener listener) {
        this.mContext = pContext;
        this.mlist = list;
        this.myAdapterCBListener = listener;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public ReturnDict getItem(int arg0) {
        return (ReturnDict) mlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        ViewHolder holder;
        if (view == null || (holder = (ViewHolder) view.getTag()) == null) {
            view = View.inflate(mContext, R.layout.taskclassify, null);
            holder = new ViewHolder();
            holder.Classify = (TextView) view
                    .findViewById(R.id.tv_classifyname);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.Classify.setText(mlist.get(position).text);

        return view;
    }

    public void setList(List<ReturnDict> mlist) {
        this.mlist = mlist;
    }

    final class ViewHolder {
        // public ImageView img;
        public TextView Classify;

    }
}
