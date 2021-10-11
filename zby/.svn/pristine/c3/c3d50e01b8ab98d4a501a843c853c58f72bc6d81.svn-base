package com.biaozhunyuan.tianyi.attendance;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;

import java.util.List;

/**
 * Created by 王安民 on 2017/8/25.
 * 日历查看页面考勤列表适配器
 */

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.ViewHolder> {

    private List<Attendance> attendanceList;
    private final LayoutInflater layoutInflater;
    private Context context;


    public AttendanceListAdapter(List<Attendance> list, Context context) {
        attendanceList = list;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AttendanceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_attendance_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AttendanceListAdapter.ViewHolder holder, int position) {
        Attendance item = attendanceList.get(position);
        String week = DateTimeUtil.getWeek(ViewHelper.formatStrToDateAndTime(item.getAttendanceDate()));
        holder.tv_date.setText(ViewHelper.convertStrToFormatDateStr(item.getAttendanceDate(), "MM月dd日") + " " + week);
        holder.current_state.setText(item.getStatus()); //显示当天的状态
        if (!TextUtils.isEmpty(item.getCheckInTime())) {
            if (item.isComeLate()) {  //如果有签到时间  判断是否迟到
                holder.status_in.setText("迟到");
                holder.status_in.setTextColor(Color.RED);
            } else {
                holder.status_in.setText("正常");
                holder.status_in.setTextColor(context.getResources().getColor(R.color.color_message));
            }
            holder.sign_in.setText(ViewHelper.convertStrToFormatDateStr(item.getCheckInTime(), "HH:mm"));
        } else { //没有签到时间判断 状态
            if (!TextUtils.isEmpty(item.getStatus())) {
//                String[] statusArr = item.getStatus().split(",");
                holder.status_in.setText("");
//                holder.status_in.setTextColor(context.getResources().getColor(R.color.fuzhuselan));
            } else {
                if (week.contains("日") || week.contains("六")) {
                    holder.status_in.setText("");
                } else {
                    holder.status_in.setText("缺卡");
                    holder.status_in.setTextColor(Color.RED);
                }
            }
            holder.sign_in.setText("无数据");
        }
        if (!TextUtils.isEmpty(item.getCheckOutTime())) { //如果有签退时间  判断是否早退

            if (item.isLeaveEarly()) {
                holder.status_out.setText("早退");
                holder.status_out.setTextColor(Color.RED);
            } else {
                holder.status_out.setText("正常");
                holder.status_out.setTextColor(context.getResources().getColor(R.color.color_message));
            }
            holder.sign_out.setText(ViewHelper.convertStrToFormatDateStr(item.getCheckOutTime(), "HH:mm"));
        } else { //没有签退时间判断 状态
            if (!TextUtils.isEmpty(item.getStatus())) {
//                String[] statusArr = item.getStatus().split(",");
                holder.status_out.setText("");
//                holder.status_out.setTextColor(context.getResources().getColor(R.color.fuzhuselan));
            } else {
                if (week.contains("日") || week.contains("六")) {
                    holder.status_out.setText("");
                } else {
                    holder.status_out.setTextColor(Color.RED);
                    holder.status_out.setText("缺卡");
                }
            }
            holder.sign_out.setText("无数据");
        }
    }

    @Override
    public int getItemCount() {
        return attendanceList == null ? 0 : attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_date;
        private TextView sign_in;
        private TextView sign_out;
        private TextView status_in;
        private TextView status_out;
        private TextView current_state;

        public ViewHolder(View view) {
            super(view);
            tv_date = (TextView) view.findViewById(R.id.tv_time_attendance);
            sign_in = (TextView) view.findViewById(R.id.tv_time_sign_in);
            sign_out = (TextView) view.findViewById(R.id.tv_time_sign_out);
            status_in = (TextView) view.findViewById(R.id.tv_status_sign_in);
            status_out = (TextView) view.findViewById(R.id.tv_status_sign_out);
            current_state = (TextView) view.findViewById(R.id.tv_status_attendance_current_day);
        }
    }
}
