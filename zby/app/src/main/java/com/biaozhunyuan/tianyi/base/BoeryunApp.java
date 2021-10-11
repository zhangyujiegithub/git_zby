package com.biaozhunyuan.tianyi.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.biaozhunyuan.tianyi.NavActivity;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.chatLibary.chat.ChartIntentUtils;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.attch.ImageLoaderWithCookie;
import com.biaozhunyuan.tianyi.chatLibary.chat.SocketService;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.helper.CrashHandler;
import com.biaozhunyuan.tianyi.login.LoginActivity;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;

/**
 * 应用程序入口
 *
 * @author BOHR
 */
public class BoeryunApp extends com.biaozhunyuan.tianyi.common.base.BoeryunApp {
    private Context context;
    private ORMDataHelper dbHelper;
    private static HashMap<String, SoftReference<List<字典>>> mDictHashMap;
    private SocketService socketService;
    private User mUser;
    private int activityNumber = 0;
    private boolean isRunInBackground;
    public List<Activity> oList;
    static BoeryunApp instance;

    /**
     * 获取所有的application的单例
     *
     * @return 程序的单例
     */
    public static BoeryunApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        dbHelper = ORMDataHelper.getInstance(context);
        oList = new ArrayList<Activity>();
        initBackgroundCallBack();
        CrashHandler.getInstance().init(this);
        // 应用程序入口处调用,避免手机内存过小，杀死后台进程,造成SpeechUtility对象为null
        // 设置你申请的应用appid
        SpeechUtility.createUtility(BoeryunApp.this, "appid=59cca5dc");
        //SDKInitializer.initialize(context);// 百度地图初始化
        initSetting();
        initImageLoader();
        initBaiduDiscriminate();
        //手机内存过小，杀死后台进程，造成Global.mUser为null，进入应用获取本地数据给Global.mUser赋值
        if (Global.mUser == null || (Global.mUser != null && TextUtils.isEmpty(Global.mUser.getUuid()))) {
            SharedPreferencesHelper helper = new SharedPreferencesHelper(getApplicationContext());
            Global.mUser = helper.getObjectBuKey("GLOBAL_USER", User.class);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbHelper.close();
    }

    /**
     * 初始化提示信息
     */
    private void initSetting() {
        SharedPreferences sp = getSharedPreferences("remind", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean("isFirst", true);
        if (isFirst) { // 如果是首次登陆
            Editor editor = sp.edit();
            editor.putBoolean("notice", true);
            editor.putBoolean("email", false); // 邮件默认不提示
            editor.putBoolean("log", false); // 日志默认不提示
            editor.putBoolean("client", true);
            editor.putBoolean("order", true);
            editor.putBoolean("contact", true);
            editor.putBoolean("task", true);
            editor.putBoolean("saleChance", true);
            editor.putBoolean("approval", true);
            editor.putBoolean("isFirst", false); // 登陆过后，修改首次登陆标志位
            editor.commit();
        }
    }


    /**
     * 初始化百度识别
     */

    private void initBaiduDiscriminate() {
        OCR.getInstance(getApplicationContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                // 调用成功，返回AccessToken对象
                String token = result.getAccessToken();
                PreferceManager.getInsance().saveValueBYkey("BaiduDiscriminateAccessToken", token);
                LogUtils.i("百度识别调用成功：", token);
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError子类SDKError对象
                LogUtils.i("百度识别调用失败：", error.getMessage());
            }
        }, getApplicationContext(), "UxcKfW4LpkVCy8tXO4ZP70PU", "n9jTtmErzDy4S7kd1FW5Dxyh7ZYYCM7I");
    }


    /***
     * 初始化ImageLoader，程序启动时初始化一次即可
     */
    private void initImageLoader() {
        // 文件缓存路径
        File cacheDir = new File(FilePathConfig.getCacheDirPath());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.default_error) //
                .showImageOnFail(R.drawable.default_error) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .imageDownloader(new ImageLoaderWithCookie(getApplicationContext()))
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCacheSize(5 * 1024 * 1024) // 内存缓存容量5M
                .diskCacheSize(50 * 1024 * 1024) // 内存缓存容量50M
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(cacheDir))// 设置缓存路径
                .writeDebugLogs()
                .build();
        L.writeLogs(false);
        ImageLoader.getInstance().init(config);
    }

    /***
     * 初始化腾讯数据统计
     */
//    private void initTecenMAT() {
//        StatService.onResume(getApplicationContext());
//    }

    /***
     * 获取字典集合引用
     *
     * @return
     */
    public static HashMap<String, SoftReference<List<字典>>> getDictHashMap() {
        if (mDictHashMap == null) {
            mDictHashMap = new HashMap<String, SoftReference<List<字典>>>();
        }
        return mDictHashMap;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    public SocketService getSocketService() {
        return socketService;
    }

    public void setSocketService(SocketService socketService) {
        this.socketService = socketService;
        ChartIntentUtils.socketService = socketService;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    /**
     * 不跟随系统字体大小改变app字体
     *
     * @return
     */

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        android.content.res.Configuration newConfig = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (resources != null && newConfig.fontScale != 1) {
            newConfig.fontScale = 1;
            if (Build.VERSION.SDK_INT >= 17) {
                Context configurationContext = createConfigurationContext(newConfig);
                resources = configurationContext.getResources();
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
            } else {
                resources.updateConfiguration(newConfig, displayMetrics);
            }
        }
        return resources;
    }
    /**
     * 得到当前所有在栈中的activity
     */
    public List<Activity> getActivityList(){
        return oList;
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        // 判断当前集合中不存在该Activity
        if (!oList.contains(activity)) {
            oList.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        for (Activity activity : oList) {
            activity.finish();
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (oList.contains(activity)) {
            oList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    private void initBackgroundCallBack() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                activityNumber++;
                if (isRunInBackground) {
                    //应用从后台回到前台 需要做的操作
                    back2App(activity);
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                activityNumber--;
                if (activityNumber == 0) {
                    //应用进入后台 需要做的操作
                    leaveApp(activity);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    /**
     * 从后台回到前台需要执行的逻辑
     *
     * @param activity
     */
    private void back2App(Activity activity) {
        LogUtils.i("BoeryunApp", "回到前台");
        isRunInBackground = false;
        if (socketService != null && !getInsance().getValueBYkey("isExit", false)
                && !(activity instanceof NavActivity) && !(activity instanceof LoginActivity)) {  //从后台回到前台,重连socket，获取未读消息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    socketService.isReConnect = true;
                    socketService.releaseSocket(0);
                }
            }).start();
        }
    }

    /**
     * 离开应用 压入后台或者退出应用
     *
     * @param activity
     */
    private void leaveApp(Activity activity) {
        LogUtils.i("BoeryunApp", "退到后台");
        isRunInBackground = true;
        if (socketService != null) {
            socketService.isReConnect = false;
            socketService.releaseSocket(0);
        }
    }

}
