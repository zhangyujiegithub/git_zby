package com.biaozhunyuan.tianyi.attendance;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.Calendar;
import java.util.Date;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/25.
 * 考勤统计页面
 */

public class AttendanceTotalActivity extends Activity {

    private BoeryunHeaderView headerView;
    private LinearLayout ll_select_time;
    private TextView tv_time;
    private TextView tv_name; //名称
    private TextView tv_dept_name;  //部门名称
    private TextView count_chidao; //迟到次数
    private TextView count_zaotui; //早退次数
    private TextView count_kuanggong; //矿工次数
    private TextView count_jiaban; //加班小时数
    private TextView count_chuchai; //出差天数
    private TextView count_qingjia; //请假天数
    private TextView count_tiaoxiu; //调休天数
    private TextView count_nianjia; //年假天数
    private TextView count_waichu; //外出天数

    private TimePickerView pickerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_total);

        initViews();
        getThisMonthAttendance();
        setOnEvent();
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_attendance_total);
        ll_select_time = (LinearLayout) findViewById(R.id.ll_select_time_attendance_total);
        tv_time = (TextView) findViewById(R.id.tv_time_attendance_total);
        count_chidao = (TextView) findViewById(R.id.tv_attendance_total_count_late);
        count_zaotui = (TextView) findViewById(R.id.tv_attendance_total_count_early_back);
        count_kuanggong = (TextView) findViewById(R.id.tv_attendance_total_count_not_work);
        count_jiaban = (TextView) findViewById(R.id.tv_attendance_total_count_jiaban);
        count_chuchai = (TextView) findViewById(R.id.tv_attendance_total_count_chucha);
        count_qingjia = (TextView) findViewById(R.id.tv_attendance_total_count_qingjia);
        count_tiaoxiu = (TextView) findViewById(R.id.tv_attendance_total_count_tiaoxiu);
        count_nianjia = (TextView) findViewById(R.id.tv_attendance_total_count_nianjia);
        count_waichu = (TextView) findViewById(R.id.tv_attendance_total_count_waichu);

        tv_name = (TextView) findViewById(R.id.tv_name_attendance_total);
        tv_dept_name = (TextView) findViewById(R.id.tv_dept_name_attendance_total);

        Calendar calendar = Calendar.getInstance();
        tv_time.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");


        pickerView = new TimePickerView(AttendanceTotalActivity.this, TimePickerView.Type.YEAR_MONTH);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {   //选择日期显示的时间可能有偏差，要经过处理
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 2;
                int year = calendar.get(Calendar.YEAR);
                if (month > 12) {
                    month = 1;
                    year += 1;
                }
                tv_time.setText(year + "年" + (month) + "月");
            }
        });
    }


    /**
     * 获取当月的考勤
     */
    private void getThisMonthAttendance() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.考勤统计;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i("考勤统计：" + response);
                AttendanceTotal total = JsonUtils.ConvertJsonObject(response, AttendanceTotal.class);
                if (total != null) {
                    initData(total);
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
     * 显示数据
     *
     * @param total
     */
    private void initData(AttendanceTotal total) {
        count_chidao.setText(StrUtils.pareseNull(total.getChiDao()));
        count_zaotui.setText(StrUtils.pareseNull(total.getZaoTui()));
        count_kuanggong.setText(StrUtils.pareseNull(total.getKuangGong()));
        count_jiaban.setText(StrUtils.pareseNull(total.getJiaBan()));
        count_chuchai.setText(StrUtils.pareseNull(total.getChuChai()));
//        count_qingjia.setText();
        count_tiaoxiu.setText(StrUtils.pareseNull(total.getJiaBanTiaoXiu()));
//        count_nianjia.setText();
        count_waichu.setText(StrUtils.pareseNull(total.getWaiChu()));
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        ll_select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
            }
        });
    }
}
