package com.biaozhunyuan.tianyi.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.PhotoHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.DecodeUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.utils.DecodeUtils.unicodeToString;

/**
 * Created by 王安民 on 2017/10/2.
 * 活动详情页  采用webview展示
 */

public class ActivityInfoActivity extends BaseActivity {
    public static final String EXTRA_URL = "extral_url_webview_normal";
    public static final String EXTRA_TITLE = "extral_title_webview_normal";
    private Context mContext;
    private WebView webView;
    private BoeryunHeaderView headerView;
    private DictIosPickerBottomDialog mDictIosPickerBottomDialog;
    private String mUrl;
    private String mTitle;
    //    private ProgressBar progressBar;
    private Dialog progressDialog;
    private ActivityModel mActiviity;

    private String shareUrl = "";

    private TextView invite_customer;//邀请客户
    private TextView scan_sign;//扫码签到
    private TextView share_info;//分享

    public static final int FILECHOOSER_RESULTCODE = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;
    private final int REQUEST_SELECT_CLIENT = 3;
    private final int REQUEST_CODE_ASK_CALL_PHONE = 4;
    /**
     * 扫描二维码获取企业信息
     */
    private final int PHOTO_PIC = 213;

    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    private ValueCallback<Uri> mUploadMessage;
    private Bitmap bitmap;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                String string = (String) msg.obj;

                try {
                    File file = new File(PhotoHelper.PATH, "logo.jpg");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                    showShare(string);
                } catch (IOException e) {
                    e.printStackTrace();
                    showShare(string);
                }
            } else if (msg.what == 2) {
                String string = (String) msg.obj;

                showShare(string);
            } else if (msg.what == 3) {
                String string = (String) msg.obj;

                try {
                    File file = new File(PhotoHelper.PATH, "logo.jpg");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    bos.flush();
                    bos.close();
                    showShare(string);
                } catch (IOException e) {
                    e.printStackTrace();
                    showShare(string);
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_layout);
        intData();
        initView();
        loadWebView(mUrl);
        setOnEvent();
        String[] permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionsUtil.requestPermission(mContext, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, permissions);
    }

    private void intData() {
        mContext = this;
        mDictIosPickerBottomDialog = new DictIosPickerBottomDialog(mContext);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mActiviity = (ActivityModel) getIntent().getExtras().getSerializable("activityInfo");
        if (mActiviity != null) {
            shareInfo();
        }
        Logger.i("url" + mUrl);
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {

            @Override
            public void onClickSaveOrAdd() {

            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickBack() {
                finish();
            }
        });


        share_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mActiviity != null) {
//                    shareApplication(Global.BASE_JAVA_URL + "html/crm/market/marketingActivity?uuid=" + mActiviity.getUuid() + "&agentId=" + Global.mUser.getUuid());
//                }
                if (!TextUtils.isEmpty(shareUrl)) {
//                    showShare(StrUtils.removeRex(shareUrl, "\""));
                    createBitmap(StrUtils.removeRex(shareUrl, "\""));
                }
            }
        });

        invite_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityInfoActivity.this, ClientListActivity.class);
                intent.putExtra("isSelectCliet", true);
                startActivityForResult(intent, REQUEST_SELECT_CLIENT);
            }
        });

        scan_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDownPop();
                Intent intent = new Intent(ActivityInfoActivity.this, CaptureActivity.class);
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ActivityInfoActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                        return;
                    } else {
                        startActivityForResult(intent, PHOTO_PIC);
                    }
                } else {
                    startActivityForResult(intent, PHOTO_PIC);
                }
            }
        });
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview_webview);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_webview);
//        progressBar = (ProgressBar) findViewById(R.id.pbar_calendar_item);
        invite_customer = (TextView) findViewById(R.id.invite_customer);
        scan_sign = (TextView) findViewById(R.id.btn_scan_to_signIn_customer);
        share_info = (TextView) findViewById(R.id.btn_share_customer);
        invite_customer = (TextView) findViewById(R.id.invite_customer);
//        progressBar.setMax(100);

        if (!TextUtils.isEmpty(mTitle)) {
            headerView.setTitle(mTitle);
        }

        progressDialog = new Dialog(ActivityInfoActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
    }

//    @Override
//    // 设置回退
//    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack(); // goBack()表示返回WebView的上一页面
//            return true;
//        }
//        return false;
//    }

    /**
     * 浏览器模式加载表单
     */
    private void loadWebView(String EXTRA_URL) {
        // requestFocusForForm();
        // textViewTitle.setText(typeName);
        // 相当于打开浏览器对象
        // 为25%，最小缩放等级，将页面所有内容显示在手机屏幕上
        webView.setInitialScale(25);
        // 得到浏览器设置
        WebSettings webSettings = webView.getSettings();
        // 设置浏览器中javascript可执行
        webSettings.setJavaScriptEnabled(true);
        // 设置webview推荐使用的窗口
        webSettings.setUseWideViewPort(true);
        // 置webview加载的页面的
        webSettings.setLoadWithOverviewMode(true);
        // 设置缩放控制
        webSettings.setBuiltInZoomControls(true);
        // 支持缩放
        webSettings.setSupportZoom(true);
        // 缩放按钮
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);
        synCookies(mContext, EXTRA_URL);
        Logger.i("url_webview::" + EXTRA_URL);
        //headers.put("Cookie",);
        webView.loadUrl(EXTRA_URL);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
//                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    progressDialog.dismiss();
                } else {
//                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//                    progressBar.setProgress(newProgress);//设置进度值
                }
            }


            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });

    }

    private void shareInfo() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.活动地址 + "?uuid=" + mActiviity.getUuid() + "&agentId=" + Global.mUser.getUuid();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                shareUrl = unicodeToString(result);
            }
        });
    }

    private void shareClientInfo(String customerId, String developmentId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.活动地址 + "?uuid=" + mActiviity.getUuid() + "&agentId=" + Global.mUser.getUuid() + "&customerId=" + customerId + "&developmentId=" + developmentId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
//                showShare(DecodeUtils.unicodeToString(result));
                createBitmap(DecodeUtils.unicodeToString(result));
            }
        });
    }

    private void saveClient(final String clientId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户活动列表;

        JSONObject jo = new JSONObject();
        try {
            jo.put("marketActivityId", mActiviity.getUuid());
            jo.put("customerIds", clientId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String developmentId = JsonUtils.pareseData(response);
                shareClientInfo(clientId, developmentId);
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
     * 同步一下cookie
     */
    public void synCookies(Context context, String url) {
        //        List<Cookie> cookies = CookieUtil.getInstance().loadForRequest(HttpUrl.get(URI.create(url)));
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                cookieManager.setCookie(url, cookie.name() + "=" + cookie.value());
//            }
//        }
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.JSESSIONID + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.JSESSIONID));
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
    }


    // 2.回调方法触发本地选择文件
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);
    }

    // 3.选择图片后处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else if (requestCode == REQUEST_SELECT_CLIENT) {
            if (data != null && data.getExtras() != null) {
                Client client = (Client) data.getExtras().getSerializable("clientInfo");
                if (client != null) {
//                    shareApplication("www.baidu.com");
                    saveClient(client.getUuid());
                }
            }
        } else if (requestCode == PHOTO_PIC) {
            if (data != null && data.getExtras() != null) {
                String scanCode = data.getStringExtra(CaptureActivity.RESULT_SCAN_CODE);
                Logger.i("扫描结果:" + scanCode);

                loadWebView(scanCode);
            }
        }
    }

    // 4. 选择内容回调到Html页面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }


    /**
     * 分享信息
     *
     * @param info 要分享的信息
     */
    private void shareApplication(String info) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "f分享");
        share_intent.putExtra(Intent.EXTRA_TEXT, info);
        share_intent = Intent.createChooser(share_intent, "分享");
        startActivity(share_intent);
    }

    private void showDownPop() {
        View view = View.inflate(mContext, R.layout.pop_scan_download, null);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout rlErweima = (RelativeLayout) view.findViewById(R.id.rl_erweima);

        rlErweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window.showAtLocation(headerView, Gravity.NO_GRAVITY, 0, 0);
    }


    private void createBitmap(final String text) {
        if (!TextUtils.isEmpty(mActiviity.getAttachImg())) {
            final String url = Global.BASE_JAVA_URL + GlobalMethord.显示附件 + mActiviity.getAttachImg();
            try {
                String cookie = CookieUtils.cookieHeader(url);
                final DisplayImageOptions options = new DisplayImageOptions
                        .Builder()
                        .extraForDownloader(cookie)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        bitmap = ImageLoader.getInstance().loadImageSync(url, options);

                        if (bitmap != null) {
                            Message message = new Message();
                            message.what = 1;
                            message.obj = text;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 2;
                            message.obj = text;
                            handler.sendMessage(message);
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Message message = new Message();
            message.what = 3;
            message.obj = text;
            handler.sendMessage(message);
        }
    }

    private void showShare(String text) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        File file = new File(PhotoHelper.PATH, "logo.jpg");


// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(text);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mActiviity.getTopic());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (file.exists()) {
            oks.setImagePath(file.getAbsolutePath());//确保SDcard下面存在此张图片
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(text);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(text);

// 启动分享GUI
        oks.show(this);
    }
}
