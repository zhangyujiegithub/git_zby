package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表单JS需要选择的页面
 */
public class FormJsSelectActivity extends BaseActivity {
    private BoeryunHeaderView headerView;
    private ListView lv;

    private Context mContext;
    private boolean isSingleSelect = true;
    private CommanAdapter<Map<String, String>> adapter;
    private List<Map<String, String>> maps = new ArrayList<>();
    private List<Map<String, String>> returnList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_js_select);
        mContext = FormJsSelectActivity.this;
        initView();
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            maps = (List<Map<String, String>>) getIntent().getSerializableExtra("FormJsSelect");
            isSingleSelect = getIntent().getBooleanExtra("isSingleSelect", true);
            adapter = getAdapter();
            lv.setAdapter(adapter);
            if (!isSingleSelect) {
                headerView.setRightTitleVisible(true);
                headerView.setRightTitle("确定");
            }
        }
    }

    private void initView() {
        headerView = findViewById(R.id.headerview);
        lv = findViewById(R.id.lv);

        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                setReturnResult();
            }

            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> item = adapter.getItem(position);
                if (isSingleSelect) {
                    returnList.add(item);
                    setReturnResult();
                } else {
                    if (returnList.contains(item)) {
                        returnList.remove(item);
                    } else {
                        returnList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setReturnResult() {
        Intent intent = new Intent(mContext, FormInfoActivity.class);
        intent.putExtra("selectData", (Serializable) returnList);
        setResult(RESULT_OK, intent);
        finish();
    }

    private CommanAdapter<Map<String, String>> getAdapter() {
        return new CommanAdapter<Map<String, String>>(maps, mContext, R.layout.item_form_js_select) {
            @Override
            public void convert(int position, Map<String, String> item, BoeryunViewHolder holder) {
                LinearLayout ll = holder.getView(R.id.ll_content);
                ll.removeAllViews();
                if (item != null) {
                    for (Map.Entry<String, String> entry : item.entrySet()) {
                        LinearLayout llCon = new LinearLayout(mContext);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        llCon.setLayoutParams(params);

                        TextView tvKey = new TextView(mContext);
                        tvKey.setTextSize(14);
                        tvKey.setTextColor(getResources().getColor(R.color.text_black));
                        tvKey.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tvKey.setText(entry.getKey() + ":  ");

                        TextView tvValue = new TextView(mContext);
                        tvValue.setTextSize(14);
                        tvValue.setTextColor(getResources().getColor(R.color.text_info));
                        tvValue.setText(entry.getValue());

                        llCon.addView(tvKey);
                        llCon.addView(tvValue);
                        ll.addView(llCon);
                    }

                    if (returnList.contains(item)) {
                        holder.setImageResoure(R.id.iv_select, R.drawable.icon_status_finish);
                    } else {
                        holder.setImageResoure(R.id.iv_select, R.drawable.icon_status_);
                    }
                }
            }
        };
    }
}
