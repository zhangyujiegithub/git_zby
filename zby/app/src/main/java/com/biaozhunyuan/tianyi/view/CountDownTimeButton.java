package com.biaozhunyuan.tianyi.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.biaozhunyuan.tianyi.R;


/**
 * 倒计时的button
 */
public class CountDownTimeButton extends AppCompatButton implements View.OnClickListener {
//    private  Button btn_yanzhengma;

    private boolean isHuoDongBaoMing;
    private Context context;

    public boolean isHuoDongBaoMing() {
        return isHuoDongBaoMing;
    }

    public void setHuoDongBaoMing(boolean huoDongBaoMing) {
        isHuoDongBaoMing = huoDongBaoMing;
    }

    public CountDownTimeButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CountDownTimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
//        btn_yanzhengma=(Button)findViewById(R.id.btn_yanzhengma);
//        btn_yanzhengma.setOnClickListener(this);


    }


    private void initView() {


        setOnClickListener(this);
    }

    int cycle = 1;
    String defalutMessage = "";
    int count = 0;
    private final int DISABLE_STAUES = 1;
    private final int ABLE_STAUES = 0;
    int wight;
    int height;

    private boolean canRun = true;
    CountHandler handler;

    public void setCycle(int second) {
        cycle = second;
        defalutMessage = getText().toString();
        count = second;
        handler = new CountHandler();
    }


    @Override
    public void onClick(View v) {

        if (isEnabled()) {
            if (startCountLisener != null && startCountLisener.startCount()) {
                wight = getMeasuredWidth();
                height = getMeasuredHeight();
                new CountThread().start();
            }
        }
    }

    public void count() {
        if (isEnabled()) {

            if (startCountLisener != null && startCountLisener.startCount()) {
                wight = getMeasuredWidth();
                height = getMeasuredHeight();
                new CountThread().start();
            }


        }
    }

    public interface StartCountLisener {
        boolean startCount();
    }

    StartCountLisener startCountLisener;

    public void setStartCountLisener(StartCountLisener startCountLisener) {

        this.startCountLisener = startCountLisener;
    }

    private class CountThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (canRun && count > 0) {
                handler.sendEmptyMessage(DISABLE_STAUES);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            handler.sendEmptyMessage(ABLE_STAUES);

        }
    }

    private class CountHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);

            if (msg.what == DISABLE_STAUES) {
                setWidth(wight);
                setHeight(height);
                setText("重新发送" + count + "秒");
                count = count - 1;
                if (isHuoDongBaoMing) {
                    setTextSize(15);
                    setTextColor(getResources().getColor(R.color.texthintColor));
                } else {
                    setTextSize(10);
                    setTextColor(getResources().getColor(R.color.white));
                    setBackgroundResource(R.drawable.forget_password_button_bg_gray_cycle);
                }
                setEnabled(false);

            } else if (msg.what == ABLE_STAUES) {
                count = cycle;
                setText(defalutMessage);
                if (!isHuoDongBaoMing) {
                    setBackgroundResource(R.drawable.forget_password_button_bg_cycle);
                }
                setTextColor(getResources().getColor(R.color.white));
                setEnabled(true);

            }
        }
    }

    public void setCanRun() {
        canRun = true;
    }


    public void stopCount() {
        count = cycle;
        setText(defalutMessage);
        setEnabled(true);
        canRun = false;
    }


}
