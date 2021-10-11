package com.biaozhunyuan.tianyi.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.State;
import com.ldf.calendar.interf.IDayRenderer;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.DayView;

/**
 * Created by ldf on 17/6/26.
 */

public class CustomDayView extends DayView {

    private final CalendarDate today = new CalendarDate();
    private TextView dateTv;
//    private ImageView marker;
    private View selectedBackground;
    private View todayBackground;
    private final TextView remark;
    private final TextView tvHoliday;
    private boolean isShowSelected;

    /**
     * 构造器
     *
     * @param context        上下文
     * @param layoutResource 自定义DayView的layout资源
     */
    public CustomDayView(Context context, int layoutResource) {
        super(context, layoutResource);
        isShowSelected = true;
        dateTv = (TextView) findViewById(R.id.date);
//        marker = (ImageView) findViewById(R.id.maker);
        remark = findViewById(R.id.remark);
        tvHoliday = findViewById(R.id.tvHoliday);
        selectedBackground = findViewById(R.id.selected_background);
        todayBackground = findViewById(R.id.today_background);
    }

    @Override
    public void refreshContent() {
        renderToday(day.getDate());
        renderSelect(day.getState());
        renderMarker(day.getDate(), day.getState());
        super.refreshContent();
    }

    private void renderMarker(CalendarDate date, State state) {
        if (Utils.loadMarkData().containsKey(date.toString())) {
            if (state == State.SELECT || date.toString().equals(today.toString())) {
                remark.setVisibility(INVISIBLE);
                if(state != State.SELECT){
                    dateTv.setTextColor(Color.parseColor("#333333"));
                }
            } else {
                remark.setVisibility(INVISIBLE);
                if (Utils.loadMarkData().get(date.toString()).equals("0")) { //法定节假日
                    setHoliday(true);
                } else if(Utils.loadMarkData().get(date.toString()).equals("1")){ //调班
                    setHoliday(false);
                } else {
                    setHoliday(true);
                    remark.setVisibility(VISIBLE);
                    String s = Utils.loadMarkData().get(date.toString());
                    remark.setText(s);
                }
            }
        } else {
            remark.setVisibility(INVISIBLE);
            tvHoliday.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置休假或者调班
     * @param isHoliday
     */
    private void setHoliday(boolean isHoliday){
        tvHoliday.setVisibility(VISIBLE);
        if(isHoliday){
            tvHoliday.setBackgroundResource(R.drawable.bg_day_type_blue);
            tvHoliday.setText("休");
        } else {
            tvHoliday.setBackgroundResource(R.drawable.bg_day_type_red);
            tvHoliday.setText("班");
        }
    }

    private void renderSelect(State state) {
        if (state == State.SELECT && isShowSelected) {
            selectedBackground.setVisibility(VISIBLE);
            dateTv.setTextColor(Color.WHITE);
        } else if (state == State.NEXT_MONTH || state == State.PAST_MONTH) {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#d5d5d5"));
        } else {
            selectedBackground.setVisibility(GONE);
            dateTv.setTextColor(Color.parseColor("#111111"));
        }
    }

    private void renderToday(CalendarDate date) {
        if (date != null) {
            if (date.equals(today)) {
                dateTv.setText("今");
                todayBackground.setVisibility(VISIBLE);
            } else {
                dateTv.setText(date.day + "");
                todayBackground.setVisibility(GONE);
            }
        }
    }

    /**
     * 设置当前选中日期状态是否显示
     * @param isShow
     */
    public void setIsShowSelected(boolean isShow){
        this.isShowSelected = isShow;
    }

    @Override
    public IDayRenderer copy() {
        return new CustomDayView(context, layoutResource);
    }
}
