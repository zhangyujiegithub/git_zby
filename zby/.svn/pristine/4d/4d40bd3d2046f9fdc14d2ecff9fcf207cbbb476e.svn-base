package com.biaozhunyuan.tianyi.newuihome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.Notice;
import com.biaozhunyuan.tianyi.notice.NoticeInfoActivity;
import com.biaozhunyuan.tianyi.notice.NoticeNewActivity;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;

import java.util.List;

import okhttp3.Request;

/**
 * 首页用户自定义排版: 通知
 */
@SuppressLint("NewApi")
public class HomeNoticeLayout extends LinearLayout {

    private Context mContext;
    private Activity activity;
    private NoScrollListView lv;
    private ImageView ivAdd;
    private Demand<Notice> demand;
    private LinearLayout ll_notice;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x111) {
                List<Notice> data = (List<Notice>) msg.obj;
                createView(data);
            }
        }
    };
    private LinearLayout llParent;

    public HomeNoticeLayout(Context context) {
        super(context);
        init(context);
    }

    public HomeNoticeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeNoticeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void refreshData() {
        getWorkList();
    }

    private void init(Context context) {
        this.mContext = context;
        this.activity = (Activity) mContext;

        View mParentView = LayoutInflater.from(context).
                inflate(R.layout.include_home_notice, this, true);

        initView(mParentView);

        initData();

        setOnTouch();
    }

    private void setOnTouch() {
        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, NoticeNewActivity.class));
            }
        });
    }

    private void initData() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.未读通知;
        demand = new Demand<>(Notice.class);
        demand.sort = "desc";
        demand.pageSize = 5;
        demand.sortField = "creationTime";
        demand.dictionaryNames = "creatorId.base_staff,categoryId.oa_notice_category";
        demand.src = url;
        getWorkList();
    }

    /**
     * 获取列表信息并展示
     */
    private void getWorkList() {
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (demand.data != null && demand.data.size() > 0) {
                    llParent.setVisibility(VISIBLE);
                    lv.setVisibility(VISIBLE);

                    Message message = handler.obtainMessage();
                    message.what = 0x111;
                    message.obj = demand.data;
                    handler.sendMessage(message);
                } else {
                    llParent.setVisibility(GONE);
                    lv.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
            }

            @Override
            public void onResponseCodeErro(String result) {
            }
        });

    }


    /**
     * \设置数据已读
     *
     * @param dataId   数据的uuid
     * @param dataType 数据的类型
     */
    private void setDataRead(String dataId, String dataType) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.设置已读 + "?dataId=" + dataId + "&dataType=" + dataType;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }


    private void createView(List<Notice> list) {
        ll_notice.removeAllViews();
        for (Notice notice : list) {
            View view = inflate(mContext, R.layout.item_home_notice_list, null);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                    (int) ViewHelper.dip2px(mContext, 50));
            view.setLayoutParams(params);
            TextView tvTittle = view.findViewById(R.id.item_home_dynamic_content);
            TextView tvName = view.findViewById(R.id.item_home_dynamic_username);
            TextView tvTime = view.findViewById(R.id.item_home_dynamic_time);


            tvTittle.setText(notice.getTitle());
            tvName.setText(notice.getCreatorName());
            tvTime.setText(ViewHelper.formatString(notice.getLastUpdateTime()));
            ll_notice.addView(view);

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NoticeInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("noticeItem", notice);
                    intent.putExtra("noticeInfo", bundle);
                    mContext.startActivity(intent);
                    setDataRead(notice.getUuid(), "通知");
                }
            });

        }
    }

    private void initView(View mParentView) {
        ivAdd = mParentView.findViewById(R.id.iv_add_notice);
        lv = mParentView.findViewById(R.id.lv_apply);
        llParent = mParentView.findViewById(R.id.ll_parent);
        ll_notice = mParentView.findViewById(R.id.ll_notice);
    }
}
