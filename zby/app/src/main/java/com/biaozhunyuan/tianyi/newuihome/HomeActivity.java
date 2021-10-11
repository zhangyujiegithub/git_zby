package com.biaozhunyuan.tianyi.newuihome;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.buglist.BugInfoActivity;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.client.ClientRelatedInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.AndroidBug5497Workaround;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.log.LogInfoActivity;
import com.biaozhunyuan.tianyi.notice.NoticeInfoActivity;
import com.biaozhunyuan.tianyi.task.TaskInfoActivityNew;
import com.biaozhunyuan.tianyi.task.TaskListActivityNew;
import com.biaozhunyuan.tianyi.utils.SystemUtil;
import com.coloros.mcssdk.PushManager;
import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.gyf.barlibrary.ImmersionBar;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS_STAFF_VIEW;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HomeActivity extends BaseActivity implements View.OnClickListener {


    private RelativeLayout rl_menu;   //菜单
    private RelativeLayout rl_contact;
    private RelativeLayout rl_home;   //首页
    private Context context;
    private long lastClickTime;

    private ImageView iv_munu;
    private ImageView iv_contact;
    private ImageView iv_home;

    private TextView tv_menu;
    private TextView tv_contact;
    private TextView tv_home;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private AddressListFragment contactAllFragment;
    private MineFragment mineFragment;

    private FragmentTransaction fragmentTransaction1;
    private ServiceConnection sc;
    private Dialog progressDialog;
    private ProgressBar bar;
    private TextView tvProgress;
    private TextView tvSize;
    private boolean isPgsShow = false;

    private TextView tvRedUnRead;
    private boolean FLAG = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_home_new_ui, null);
        setContentView(view);
        context = HomeActivity.this;
        AndroidBug5497Workaround.assistActivity(HomeActivity.this);
        initView();
        initData();
        initPushNotification();
//        bindSocketService();
        updateVersion();
//        preventProjectInSeaEveryDay();
        getIntentData();
        setOnTouch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            Dynamic dynamic = (Dynamic) intent.getSerializableExtra("dynamicInfo");
            if (dynamic != null) {
                intoInfoByPushType(dynamic);
            }
            String isGetAllUser = intent.getStringExtra("isGetAllUser");
            /*if ("true".equals(isGetAllUser) && backlogFragment != null) {
                backlogFragment.getAllUser();
            }*/
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);  //注释这行代码，防止应用闪退之后，回到首页fragment出现重叠的情况
    }

    private void setOnTouch() {

    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            Dynamic dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            if (dynamic != null) {
                intoInfoByPushType(dynamic);
            }
            /*String isGetAllUser = getIntent().getStringExtra("isGetAllUser");
            if ("true".equals(isGetAllUser) && backlogFragment != null) {
                backlogFragment.getAllUser();
            }*/
        }
    }

    private void initData() {
        contactAllFragment = new AddressListFragment();
        homeFragment = new HomeFragment();
        mineFragment = new MineFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1
                .add(R.id.fl_home_fragment, homeFragment)
                .add(R.id.fl_home_fragment, contactAllFragment)
                .add(R.id.fl_home_fragment, mineFragment);
        fragmentTransaction1.hide(contactAllFragment).hide(mineFragment).show(homeFragment);
        fragmentTransaction1.commit();
        changeTab(1);
        changeStatusBar(1);
    }

    private void initView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        tvRedUnRead = findViewById(R.id.red_point_message);


        tv_menu = findViewById(R.id.tv_home_navigation); //菜单
        tv_contact = findViewById(R.id.tv_home_share);
        tv_home = findViewById(R.id.tv_home_set);    //首页


        iv_munu = findViewById(R.id.iv_home_navigation); //菜单
        iv_contact = findViewById(R.id.iv_home_share);
        iv_home = findViewById(R.id.iv_home_set);  //首页


        rl_menu = findViewById(R.id.rl_home_navigation); //菜单
        rl_contact = findViewById(R.id.rl_home_share);  //我的
        rl_home = findViewById(R.id.rl_home_set);  //首页


        rl_menu.setOnClickListener(HomeActivity.this);
        rl_contact.setOnClickListener(HomeActivity.this);
        rl_home.setOnClickListener(HomeActivity.this);
    }

    /**
     * 切换fragment改变状态栏背景色和字体色
     *
     * @param num 当前第几个fragment
     */
    private void changeStatusBar(int num) {
        switch (num) {
            case 1:
                ImmersionBar.with(this).statusBarColor(R.color.new_theme_blue).statusBarDarkFont(false).fitsSystemWindows(true).init();
                break;
            case 2:
                ImmersionBar.with(this).statusBarColor(R.color.new_theme_blue).statusBarDarkFont(false).fitsSystemWindows(true).init();
                break;
            case 3:
                ImmersionBar.with(this).statusBarColor(R.color.hanyaRed).statusBarDarkFont(false).fitsSystemWindows(true).init();
                break;
        }
    }


    /**
     * 切换tab按钮的样式
     *
     * @param num 当前是第几个
     */
    private void changeTab(int num) {
        switch (num) {
            case 1:
                iv_munu.setImageResource(R.drawable.new_home_menu);
                iv_contact.setImageResource(R.drawable.new_home_mine);
                iv_home.setImageResource(R.drawable.new_home_home_select);


                tv_home.setTextColor(getResources().getColor(R.color.new_navigation_text_blue));
                tv_contact.setTextColor(getResources().getColor(R.color.new_navigation_text_gray));
                tv_menu.setTextColor(getResources().getColor(R.color.new_navigation_text_gray));

                break;
            case 2:
                iv_contact.setImageResource(R.drawable.new_home_mine);
                iv_munu.setImageResource(R.drawable.new_home_menu_select);
                iv_home.setImageResource(R.drawable.new_home_home);


                tv_menu.setTextColor(getResources().getColor(R.color.new_navigation_text_blue));
                tv_contact.setTextColor(getResources().getColor(R.color.new_navigation_text_gray));
                tv_home.setTextColor(getResources().getColor(R.color.new_navigation_text_gray));
                break;
            case 3:
                iv_contact.setImageResource(R.drawable.new_home_mine_select);
                iv_munu.setImageResource(R.drawable.new_home_menu);
                iv_home.setImageResource(R.drawable.new_home_home);

                tv_home.setTextColor(getResources().getColor(R.color.new_navigation_text_gray));
                tv_menu.setTextColor(getResources().getColor(R.color.new_navigation_text_gray));
                tv_contact.setTextColor(getResources().getColor(R.color.new_navigation_text_blue));
                break;
        }
    }


    /**
     * 重写返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FLAG) {
//                menu.toggle();
            }/* else if (contactAllFragment != null && contactAllFragment.isDepart) {
                //监听返回键，部门列表返回上一个部门
                contactAllFragment.backLast();
                return false;
            }*/ else {
                if (System.currentTimeMillis() - lastClickTime > 2000) {
                    Toast.makeText(this, "再按一次退出" + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                    lastClickTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS_STAFF_VIEW: //选择执行人
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        try {
                            List<User> users = userList.getUsers();
//                            backlogFragment.receiveSelectedUser(users.get(0));
                        } catch (IndexOutOfBoundsException e) {
                            showShortToast("没有选择执行人");
                        }
                    }
            }
        }
    }

    /***
     * 检查版本更新
     */
    private void updateVersion() {
        /** 新版本 **/
        new PgyUpdateManager.Builder()
                .setForced(true)                //设置是否强制提示更新,非自定义回调更新接口此方法有用
                .setUserCanRetry(false)         //失败后是否提示重新下载，非自定义下载 apk 回调此方法有用
                .setDeleteHistroyApk(false)     // 检查更新前是否删除本地历史 Apk， 默认为true
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        //没有更新是回调此方法
                        Log.d("pgyer", "there is no new version");
                    }

                    @Override
                    public void onUpdateAvailable(AppBean appBean) {
                        //有更新回调此方法
                        Log.d("pgyer", "there is new version can update"
                                + "new versionCode is " + appBean.getVersionCode());
                        AlertDialog dialog = new AlertDialog(HomeActivity.this).builder()
                                .setTitle("有新版本")
                                .setMsg("检测到新版本" + appBean.getVersionName() + "，请及时更新")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //调用以下方法，DownloadFileListener 才有效；
                                        //如果完全使用自己的下载方法，不需要设置DownloadFileListener
                                        PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                                    }
                                })
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        dialog.show();
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        //更新检测失败回调
                        //更新拒绝（应用被下架，过期，不在安装有效期，下载次数用尽）以及无网络情况会调用此接口
                        Log.e("pgyer", "check update failed ", e);
                    }
                })
                //注意 ：
                //下载方法调用 PgyUpdateManager.downLoadApk(appBean.getDownloadURL()); 此回调才有效
                //此方法是方便用户自己实现下载进度和状态的 UI 提供的回调
                //想要使用蒲公英的默认下载进度的UI则不设置此方法
                .setDownloadFileListener(new DownloadFileListener() {
                    @Override
                    public void downloadFailed() {
                        //下载失败
                        Log.e("pgyer", "download apk failed");
                    }

                    @Override
                    public void downloadSuccessful(File uri) {
                        Log.e("pgyer", "download apk success");
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            setBackgroundLight();
                        }
                        // 使用蒲公英提供的安装方法提示用户 安装apk
                        PgyUpdateManager.installApk(uri);
                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        Integer[] clone = integers.clone();
                        for (Integer integer : clone) {
                            Log.e("pgyer", "update download apk progress" + integer);
                            if (progressDialog == null) {
                                progressDialog = new Dialog(HomeActivity.this, R.style.progress_dialog);
                                progressDialog.setContentView(R.layout.dialog_download_progress);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setCancelable(true);
                                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                bar = progressDialog.findViewById(R.id.pb_main_download);
                                tvProgress = progressDialog.findViewById(R.id.tv_progress);
                                tvSize = progressDialog.findViewById(R.id.tv_size_download);
                            }
                            if (!isPgsShow) {
                                progressDialog.show();
                                setBackgroundDark();
                                isPgsShow = true;
                            }
                            bar.setProgress(integer);
                            tvProgress.setText(integer + "%");
                            tvSize.setText((39 * integer / 100) + "M/39M");
                        }

                    }
                })
                .register();
    }


    private void setBackgroundDark(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.6f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void setBackgroundLight(){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 绑定设备token
     */
    private void bindToken(String token, String mobileType) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.绑定设备;

        JSONObject jo = new JSONObject();
        try {
            jo.put("token", token);
            jo.put("allowPush", "1");
            jo.put("deviceType", "android");
            jo.put("mobileType", mobileType);
            jo.put("otherToken", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
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
     * 初始化集成信鸽推送
     */
    private void initPushNotification() {
        String mobileType = SystemUtil.getDeviceBrand(); //手机类型
        LogUtils.i("手机类型：：：", mobileType);
        if ("huawei".equalsIgnoreCase(mobileType) || "honor".equalsIgnoreCase(mobileType)) { //华为手机
            HMSAgent.init(this); //华为推送初始化
            HMSAgent.Push.getToken(new GetTokenHandler() {
                @Override
                public void onResult(int rst) {
                    LogUtils.i("华为推送注册结果码：", "" + rst);
                }
            });
        } else if ("xiaomi".equalsIgnoreCase(mobileType)) {//小米手机
            if (shouldInit()) {
                MiPushClient.registerPush(this, Global.xmAppId, Global.xmAppKey); //小米推送初始化
            }
        } else if ("vivo".equalsIgnoreCase(mobileType)) {
            PushClient.getInstance(getApplicationContext()).turnOnPush(new IPushActionListener() {
                @Override
                public void onStateChanged(int state) {
                    if (state != 0) {
                        LogUtils.i("Vivo注册失败", "打开push异常[" + state + "]");
                    } else {
                        LogUtils.i("打开push成功", state + "");
                        String token = PushClient.getInstance(getApplicationContext()).getRegId();
                        bindToken(token, "vivo");
                        LogUtils.i("VivoToken：：：", token);
                    }
                }
            });
        } else if ("oppo".equalsIgnoreCase(mobileType)) {
            boolean isSupport = PushManager.isSupportPush(this);
            registerChannal();
            if (isSupport) {
                PushManager.getInstance().register(context, Global.oppoAppKey, Global.oppoAppSecret, new PushCallback() {

                    //注册的结果,如果注册成功,registerID就是客户端的唯一身份标识
                    @Override
                    public void onRegister(int responseCode, String registerID) {
                        bindToken(registerID, "oppo");
                    }

                    @Override
                    public void onUnRegister(int i) {

                    }

                    @Override
                    public void onGetAliases(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onSetAliases(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onUnsetAliases(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onSetUserAccounts(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onUnsetUserAccounts(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onGetUserAccounts(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onSetTags(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onUnsetTags(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onGetTags(int i, List<SubscribeResult> list) {

                    }

                    @Override
                    public void onGetPushStatus(int i, int i1) {

                    }

                    @Override
                    public void onSetPushTime(int i, String s) {

                    }

                    @Override
                    public void onGetNotificationStatus(int i, int i1) {

                    }
                });
            }
        } else {
            //开启信鸽日志输出
            XGPushConfig.enableDebug(this, true);
            //XGPushConfig.setHuaweiDebug(true);
            //XGPushConfig.enableOtherPush(getApplicationContext(), true);//打开第三方推送

            XGPushManager.registerPush(this, new XGIOperateCallback() {//信鸽注册代码
                @Override
                public void onSuccess(Object data, int flag) {
                    String deviceToken = data.toString();
                    PreferceManager.getInsance().saveValueBYkey("deviceToken", deviceToken);
                    Log.d("TPush", "注册成功，设备token为：" + deviceToken);
                    bindToken(deviceToken, "其他");
                }

                @Override
                public void onFail(Object data, int errCode, String msg) {
                    Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (v.getId()) {

            case R.id.rl_home_navigation:
                changeTab(2);
                changeStatusBar(2);
                fragmentTransaction.hide(mineFragment).hide(homeFragment).show(contactAllFragment);
//                fragmentTransaction.replace(R.id.fl_home_fragment,new MenuHomeFragment());
                break;

            case R.id.rl_home_share:
                changeTab(3);
                changeStatusBar(3);
                fragmentTransaction.hide(homeFragment).hide(mineFragment).show(mineFragment);

                break;

            case R.id.rl_home_set:
                changeTab(1);
                changeStatusBar(1);
                fragmentTransaction.hide(mineFragment).hide(contactAllFragment).show(homeFragment);
                break;
        }

        fragmentTransaction.commit();
    }

    /**
     * 根据推送数据类型进入详情页面
     */
    private void intoInfoByPushType(Dynamic dynamic) {
        Intent intent = new Intent();
        switch (dynamic.getDataType()) {
            case "流程实例编号":
                intent.setClass(context, FormInfoActivity.class);
                String url1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                intent.putExtra("exturaUrl", url1);
                intent.putExtra("formDataId", dynamic.getDataId());
                break;
            case "日志消息":
                intent.setClass(context, LogInfoActivity.class);
                break;
            case "任务消息":
                intent.setClass(context, TaskInfoActivityNew.class);
                break;
            case "通知消息":
                intent.setClass(context, NoticeInfoActivity.class);
                break;
            case "客户消息":
                intent.setClass(context, ClientRelatedInfoActivity.class);
                break;
            case "项目联系提醒":
            case "客户联系提醒":
                intent.setClass(context, AddRecordActivity.class);
                break;
            case "离线消息":
//                intent.setClass(context, ChatActivity.class);
//                GroupSession session = new GroupSession();
//                session.setChatId(dynamic.getDataId());
//                intent.putExtra("chatUser", session);
//                intent.putExtra("isPush", true);
//                break;
                return;
            case "空间消息":
                String url = Global.BASE_JAVA_URL + GlobalMethord.空间详情H5 + dynamic.getDataId();
                intent.setClass(context, WebviewNormalActivity.class);
                intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, dynamic.getDataType());
                intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
                break;
            case "今日待办任务":
                intent.setClass(context, TaskListActivityNew.class);
                break;
            case "BUG消息":
                intent.setClass(context, BugInfoActivity.class);
                break;
            case "应付提醒":
                intent.setClass(context, FormInfoActivity.class);
                String url2 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + dynamic.getDataId();
                intent.putExtra("exturaUrl", url2);
                intent.putExtra("formDataId", dynamic.getDataId());
                intent.putExtra("isShowCancelPush", true);
                break;

        }
        intent.putExtra("dynamicInfo", dynamic);
        startActivity(intent);
    }


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        //关闭socket服务
        super.onDestroy();
    }

    private void registerChannal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "tianyi";
            String channelName = "天衣办公";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

}
