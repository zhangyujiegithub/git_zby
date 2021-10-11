package com.biaozhunyuan.tianyi.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;


/**
 * Created by 王安民 on 2017/9/23.
 * activity的基类
 */
@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity{


    private int netMobile;

    private BaseActivity activity;

    /***/
    public String TAG = getClass().getSimpleName();

    private Toast toast;

    private BoeryunApp application;

    public final String INFO_ERRO_SERVER = "网络不给力，请稍后再试";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (application == null) {
            // 得到Application对象
            application = (BoeryunApp) getApplication();
        }
        activity = this;// 把当前的上下文对象赋值给BaseActivity
        //如果是从后台杀死恢复来的，回调onCreate的时候会传递一个非空的Bundle savedInstanceState给当前Activity
        // 只要判断这个非空就能知道是否是恢复流程,判断如果是被后台杀死，重新进入应用，否则静态变量被回收，会报异常
        if (savedInstanceState != null) {
            reInitApp();
            return;
        }

        addActivity();// 调用添加方法
        ExistApplication.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
//        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).init();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);//启用硬件加速
//        getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
        initDialog();
        setStatusTextColor(true,this);
    }

    @Override
    protected void onStart() { //每次回到界面重新判断当前有无网络连接
        super.onStart();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(final String status) { // 另一台设备登录，登出的事件
        if ("502".equals(status)) {
            getInsance().saveValueBYkey("isExit", true);
            final AlertDialog dialog = new AlertDialog(this).builder();
            dialog.setCancelable(false)
                    .setMsg("您的账号在另外一台设备登录!")
                    .setTitle("提示")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dissMiss();
                            EventBus.getDefault().removeStickyEvent(status); //移除事件
                            if (EventBus.getDefault().isRegistered(BaseActivity.this)) {
                                EventBus.getDefault().unregister(BaseActivity.this);
                            }
                            removeALLActivity();
                            clearToken();
                            ORMDataHelper.getInstance(getBaseContext()).deleteOldDb();
                            XGPushManager.unregisterPush(getBaseContext());

                            ComponentName comp = new ComponentName(activity, "com.biaozhunyuan.tianyi.NavActivity");
                            Intent intent = new Intent();
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    private void initDialog() {
        //跳转设置移动网络界面
        alertDialog = new AlertDialog(this).builder()
                .setMsg("请检查网络连接!")
                .setTitle("网络错误")
                .setCancelable(false)
                .setPositiveButton("设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转设置移动网络界面
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }).setNegativeButton("退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //退出app
//                        System.exit(0);
                        removeALLActivity();//执行移除所以Activity方法
                    }
                });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }

    /******************** SKip跳转 *************************************/
    /**
     * 跳转传值
     *
     * @param class1 下一个界面
     * @param bundle 传递的值
     */
    protected void skip(Class<?> class1, Bundle bundle) {
        Intent intent = new Intent(this, class1);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 不传值的跳转
     *
     * @param class1 下一个界面
     */
    protected void skip(Class<?> class1) {
        skip(class1, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ImmersionBar.with(this).destroy();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        removeActivity();
    }

    /**
     *  
     * 判断有无网络 。 
     *
     * @return  true 有网,
     *  false 没有网络. 
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;
        }
        return false;
    }
    //得到当前所有在任务栈内的activity
    public List<Activity> getActivityList(){
        return application.getActivityList();
    }

    // 添加Activity方法
    public void addActivity() {
        application.addActivity(activity);// 调用Application的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity(activity);// 调用Application的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity();// 调用Application的销毁所有Activity方法
    }

    /**
     * 清除用户的token
     */
    private void clearToken() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.清除设备;

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

    /**
     * 内存不够时
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_MODERATE) {
            ExistApplication.getInstance().exit(true);
        }
    }


    /**
     * 重新初始化应用界面，清空当前Activity棧，并启动欢迎页面
     */
    private void reInitApp() {
        removeALLActivity();

        ComponentName comp = new ComponentName(activity, "com.biaozhunyuan.tianyi.NavActivity");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        startActivity(intent);
    }

    /**
     * 设置状态栏文字色值为深色调
     *
     * @param useDart  是否使用深色调
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setStatusTextColor(boolean useDart, Activity activity) {
        if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
            //OPPO
            setOPPOStatusTextColor(useDart, activity);
        }
    }
    /**
     * 设置OPPO手机状态栏字体为黑色(colorOS3.0,6.0以下部分手机)
     *
     * @param lightStatusBar
     * @param activity
     */
    private static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;

    private static void setOPPOStatusTextColor(boolean lightStatusBar, Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int vis = window.getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightStatusBar) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (lightStatusBar) {
                vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            } else {
                vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            }
        }
        window.getDecorView().setSystemUiVisibility(vis);
    }


}
