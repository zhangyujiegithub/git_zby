package com.biaozhunyuan.tianyi.attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.SegmentView;
import com.gyf.barlibrary.ImmersionBar;
import com.loonggg.weekcalendar.view.WeekCalendar;

import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/10/26.
 * 考勤列表页面
 */

public class AttendanceSalaryActivity extends BaseActivity {

    private Context context;
    private DictionaryHelper helper;
    private Demand<OaAttendance> demand;
    private int pageIndex = 1;
    private CommanAdapter<OaAttendance> adapter;
    private int posi = 0;

    private SegmentView segmentView;
    private ImageView iv_cancle;
    private PullToRefreshAndLoadMoreListView lv;
    private WeekCalendar weekCalendar;
    private String date = "";
    private String category = "sign";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                lv.loadCompleted();
                Toast.makeText(context, "已经加载了全部数据", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagsalarylist);
        context = AttendanceSalaryActivity.this;
        ImmersionBar.with(this).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).fitsSystemWindows(true).init();
        helper = new DictionaryHelper(context);
        initViews();
        initDemand();
        String date1 = ViewHelper.getDateToday();
        date = date1;
        getAttenDanceList(date1, "sign");
        setOnEvent();
    }

    private void initViews() {
        segmentView = (SegmentView) findViewById(R.id.segment_tag_salary);
        iv_cancle = (ImageView) findViewById(R.id.imageViewCancel);
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.listView_tagsalarylist_show);
        weekCalendar = (WeekCalendar) findViewById(R.id.weekcalendar_tag_salary);

        segmentView.setSegmentText("考勤记录", 0);
        segmentView.setSegmentText("定位记录", 1);

//        lv.setIsCanLoadMore(false);
    }

    private void setOnEvent() {
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getAttenDanceList(date, category);
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.sendEmptyMessageDelayed(1, 500);
//                getAttenDanceList(date, category);
            }
        });

        segmentView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                posi = position;
                if (position == 0) {
                    category = "sign";
                    pageIndex = 1;
                    lv.startRefresh();
                    getAttenDanceList(date, category);
                } else if (position == 1) {
                    category = "pin";
                    pageIndex = 1;
                    lv.startRefresh();
                    getAttenDanceList(date, category);
                }
            }
        });

        weekCalendar.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                date = time;
                pageIndex = 1;
                lv.startRefresh();
                getAttenDanceList(date, category);
            }
        });
    }

    private void initDemand() {
        demand = new Demand<OaAttendance>();
        demand.pageIndex = pageIndex;
        demand.pageSize = 999;
    }


    private void getAttenDanceList(String date, final String category) {
        String url;
        if (category.equals("sign")) {
//            url = Global.BASE_JAVA_URL + GlobalMethord.考勤记录 + "?date=" + date + "&category=" + category + "&uuid=" + Global.mUser.getUuid();
            url = Global.BASE_JAVA_URL + GlobalMethord.考勤记录 + "?startTime=" + date + "&endTime=" + date;
        } else {
//            url = Global.BASE_JAVA_URL + GlobalMethord.考勤记录 + "?date=" + date + "&category=" + category + "&uuid=" + Global.mUser.getUuid();
            url = Global.BASE_JAVA_URL + GlobalMethord.外出定位记录 + "?startTime=" + date + "&endTime=" + date;
        }
        demand.pageIndex = pageIndex;
        StringRequest.postAsyn(url, demand, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<OaAttendance> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), OaAttendance.class);
                if (list != null) {
                    lv.onRefreshComplete();
                    if (pageIndex == 1) {
                        if (category.equals("sign")) {
                            adapter = getAdapter(list);
                        } else {
                            adapter = getOutSideAdapter(list);
                        }
                        lv.setAdapter(adapter);

                    } else {
                        adapter.addBottom(list, false);
                        if (list != null && list.size() == 0) {
                            lv.loadAllData();
                        }
                        lv.loadCompleted();
                    }
                    pageIndex += 1;
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

    private CommanAdapter<OaAttendance> getOutSideAdapter(List<OaAttendance> list) {
        return new CommanAdapter<OaAttendance>(list, context, R.layout.outside_tagsalarylist_listviewlayout) {
            @Override
            public void convert(int position, OaAttendance item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.circularAvatar_tag_item, item.getStaffId());
                viewHolder.setTextValue(R.id.textView6, item.getCreateTime());
                viewHolder.setTextValue(R.id.name_attentdace_list, helper.getUserNameById(item.getStaffId()));
                viewHolder.setTextValue(R.id.outside_address, item.getAddress());
                viewHolder.setTextValue(R.id.tv_remark, item.getRemark());
                MultipleAttachView attachView = viewHolder.getView(R.id.attachView);
                if (!TextUtils.isEmpty(item.getPic())) {
                    attachView.loadImageByAttachIds(item.getPic());
                    attachView.setVisibility(View.VISIBLE);
                } else {
                    attachView.setVisibility(View.GONE);
                }

                TextView tvRemark = viewHolder.getView(R.id.tv_remark);
                if (!TextUtils.isEmpty(item.getRemark())) {
                    tvRemark.setText(item.getRemark());
                    tvRemark.setVisibility(View.VISIBLE);
                } else {
                    tvRemark.setVisibility(View.GONE);
                }



                viewHolder.getView(R.id.ll_position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item != null) {
                            Intent intent = new Intent(context, MapViewActivity.class);
                            intent.putExtra("longitude", item.getLng());
                            intent.putExtra("latitude", item.getLat());
                            startActivity(intent);
                        }
                    }
                });
            }
        };
    }

    private CommanAdapter<OaAttendance> getAdapter(List<OaAttendance> list) {
        return new CommanAdapter<OaAttendance>(list, context, R.layout.tagsalarylist_listviewlayout) {
            @Override
            public void convert(int position, OaAttendance item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.circularAvatar_tag_item, item.getCreatorId());
                viewHolder.setTextValue(R.id.name_attentdace_list, helper.getUserNameById(item.getCreatorId()));

                MultipleAttachView viewSignIn = viewHolder.getView(R.id.attach_signIn_attendance_list);
                MultipleAttachView viewSignOut = viewHolder.getView(R.id.attach_signOut_attendance_list);
                TextView late = viewHolder.getView(R.id.tv_signIn_time);
                TextView early = viewHolder.getView(R.id.tv_signOut_time);
                LinearLayout ll_becauselate = viewHolder.getView(R.id.ll_becauselate);
                LinearLayout ll_becauseearly = viewHolder.getView(R.id.ll_becauseearly);

                if (item.getComeLate() == null) {
                    item.setComeLate(false);
                }
                if (item.getLeaveEarly() == null) {
                    item.setLeaveEarly(false);
                }

                if (posi == 0) {
                    viewHolder.setTextValue(R.id.tv_signIn_time, item.getCheckInTime());
                    viewHolder.setTextValue(R.id.tv_signIn_address, item.getAddressSignin());
                    viewHolder.setTextValue(R.id.tv_signOut_time, item.getCheckOutTime());
                    viewHolder.setTextValue(R.id.tv_signOut_address, item.getAddressSignout());
                    viewHolder.getView(R.id.ll_item_qiantu).setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(item.getPicSignin())) {
                        viewSignIn.setVisibility(View.VISIBLE);
                        viewSignIn.loadImageByAttachIds(item.getPicSignin());
                    } else {
                        viewSignIn.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(item.getPicSignout())) {
                        viewSignOut.setVisibility(View.VISIBLE);
                        viewSignOut.loadImageByAttachIds(item.getPicSignout());
                    } else {
                        viewSignOut.setVisibility(View.GONE);
                    }

                } else if (posi == 1) {
                    viewHolder.setTextValue(R.id.tv_signIn_time, item.getCreationTime());
                    viewHolder.setTextValue(R.id.tv_signIn_address, item.getAddressPin());

                    viewHolder.getView(R.id.ll_item_qiantu).setVisibility(View.GONE);

                    viewSignOut.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(item.getPicPin())) {
                        viewSignIn.setVisibility(View.VISIBLE);
                        viewSignIn.loadImageByAttachIds(item.getPicPin());
                    } else {
                        viewSignIn.setVisibility(View.GONE);
                    }
                }
                if (!TextUtils.isEmpty(item.getCheckInTime())) {
                    if (item.getComeLate()) {  //如果有签到时间  判断是否迟到
                        late.setText(item.getCheckInTime() + "(迟到)");
                        late.setTextColor(Color.parseColor("#FF7F66"));
                        ll_becauselate.setVisibility(View.VISIBLE);
                        viewHolder.setTextValue(R.id.becauselate, item.getComeLateReason());
                    } else {
                        ll_becauselate.setVisibility(View.GONE);
                        late.setTextColor(getResources().getColor(R.color.color_message));
                    }
                }
                if (!TextUtils.isEmpty(item.getCheckOutTime())) { //如果有签退时间  判断是否早退
                    if (item.getLeaveEarly()) {
                        early.setText(item.getCheckOutTime() + "(早退)");
                        early.setTextColor(Color.RED);
                        ll_becauseearly.setVisibility(View.VISIBLE);
                        viewHolder.setTextValue(R.id.because_early, item.getLeaveEarlyReason());
                    } else {
                        ll_becauseearly.setVisibility(View.GONE);
                        early.setTextColor(getResources().getColor(R.color.color_message));
                    }
                }
            }
        };
    }
}
