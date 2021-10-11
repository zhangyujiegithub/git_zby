package com.biaozhunyuan.tianyi.newuihome;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.attach.OpenFilesIntent;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

import okhttp3.Request;

/**
 * 标准院通知栏目详情页面
 *
 * @author GaoB
 * @description:
 * @date : 2020/11/20 10:57
 */

@SuppressLint("NewApi")
public class NoticeDetailActivity extends BaseActivity implements DownloadListener {

    private Notice notice;
    private WebView mWebView;
    private ProgressBar progressBar;
    private BoeryunHeaderView headerView;
    private Dialog progressDialog;
    private String mUrl;
    private String mTitle;
    private Demand demand;
    private String readId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details_new);
        getIntentData();
        getRead();
        initView();
        setEvent();
    }

    private void getRead() {
        demand = new Demand();
        String url = Global.BASE_JAVA_URL + GlobalMethord.REDED;
        Map<String, String> map =  new HashMap<>();
        map.put("releaseId", readId);
        demand.keyMap =map;
        demand.src = url;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    String state=JsonUtils.getStringValue(response,"Status");
                    LogUtils.e("TAG","TAG===>>"+state);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private void initView() {
        mWebView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.pbar_notic_details);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_webview);

        progressBar.setMax(100);
        if (!TextUtils.isEmpty(mTitle)) {
            headerView.setTitle(mTitle);
        }

        progressDialog = new Dialog(NoticeDetailActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(R.color.translate_quarter_gray);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("加载中...");
        progressDialog.show();
        initWebView();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            notice = (Notice) getIntent().getSerializableExtra("Notice");
            readId=notice.getUuid();
            String title = getIntent().getStringExtra("NoticeType");
            if (title != null) {
                mTitle = title;
            }
            if (notice != null) {
                initData();
            }
        }
    }

    private void initData() {
        if ("院内共享".equals(mTitle)) {
            mUrl = Global.BASE_JAVA_URL + GlobalMethord.院内共享详情 + "?uuid=" + notice.getUuid();
        } else {
            mUrl = Global.BASE_JAVA_URL + GlobalMethord.标准院首页栏目详情
                    + "?uuid=" + notice.getUuid();
        }
//        mTitle = notice.getTitle();
    }


    private void setEvent() {
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
    }


    private void initWebView() {
        mWebView.setInitialScale(20);
        // 得到浏览器设置
        WebSettings webSettings = mWebView.getSettings();
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
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);
        synCookies(this);

        mWebView.loadData("", "text/html", "UTF-8");
        mWebView.loadUrl(mUrl);
//        Toast.makeText(this,"" + mUrl, Toast.LENGTH_SHORT).show();
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString().trim();
                view.loadUrl(url);
                if (url.contains("/attachment/downloadFile?")) { //附件下载
                    mWebView.stopLoading();
                    String[] split = url.split("id=");
                    ProgressDialogHelper.show(NoticeDetailActivity.this);
                    getAttactName(split[1]);
                } else if (url.contains("/Platform/attached/file")) {
                    mWebView.stopLoading();
                    String urlDecode = URLDecoder.decode(url);
                    if (!TextUtils.isEmpty(urlDecode)) {
                        final String fileName = urlDecode.substring(urlDecode.lastIndexOf("/"));
                        ProgressDialogHelper.show(NoticeDetailActivity.this);
                        StringRequest.downloadFile(url, fileName, new StringResponseCallBack() {
                            @Override
                            public void onResponse(String response) {
                                ProgressDialogHelper.dismiss();
                                open(NoticeDetailActivity.this, fileName);
                            }

                            @Override
                            public void onFailure(Request request, Exception ex) {
                                ProgressDialogHelper.dismiss();
                            }

                            @Override
                            public void onResponseCodeErro(String result) {
                                ProgressDialogHelper.dismiss();
                            }
                        });

                    }

                }


                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                    progressDialog.dismiss();
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

    }

    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * 获取文件名称
     *
     * @param attachId
     */
    private void getAttactName(String attachId) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("attachIds", attachId);
        } catch (JSONException e) {
            e.printStackTrace();
            ProgressDialogHelper.dismiss();
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.附件列表;
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i(response);
                List<Attach> attaches = JsonUtils.ConvertJsonToList(response, Attach.class);
//                String fileName = "";
                if (null != attaches && attaches.size() > 0) {
//                    fileName = attach.filename;
                    if (null != attaches.get(0) && !TextUtils.isEmpty(attaches.get(0).filename)) {
                        downloadFile(attachId, attaches.get(0).filename);
                    } else {
                        ProgressDialogHelper.dismiss();
                    }
                } else {
                    ProgressDialogHelper.dismiss();
                }

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }


    /**
     * 下载文件，下载完成并打开文件
     *
     * @param fileId
     * @param fileName
     */
    private void downloadFile(String fileId, String fileName) {

        String downUrl = Global.BASE_JAVA_URL + GlobalMethord.显示附件
                + fileId;
        StringRequest.downloadFile(downUrl, fileName, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
//                showShortToast("文件已下载到文件管理sd卡根目录中");
                open(NoticeDetailActivity.this, fileName);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }


    /***
     * 打开附件文件的方法
     *
     * @param name
     *            后缀名
     */
    public void open(Context mContext, String name) {
        String fileName = FilePathConfig.getCacheDirPath() + File.separator
                + name;
        File currentPath = new File(fileName);
        if (currentPath != null && currentPath.isFile()) {
            Logger.i("pathname" + "-->" + fileName);
            Intent intent = null;
            if (checkEndsWithInStringArray(fileName, mContext.getResources()
                    .getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingImage))) {
                intent = OpenFilesIntent.getImageFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingWebText))) {
                intent = OpenFilesIntent.getHtmlFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingPackage))) {
                intent = OpenFilesIntent.getApkFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingAudio))) {
                intent = OpenFilesIntent.getAudioFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingVideo))) {
                intent = OpenFilesIntent.getVideoFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingText))) {
                intent = OpenFilesIntent.getTextFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingPdf))) {
                intent = OpenFilesIntent.getPdfFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingWord))) {
//                intent = OpenFilesIntent.getWordFileIntent(currentPath);
                intent = OpenFilesIntent.getWpsFileIntent(currentPath, mContext);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingExcel))) {
                intent = OpenFilesIntent.getExcelFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(com.biaozhunyuan.tianyi.common.R.array.fileEndingPPT))) {
                intent = OpenFilesIntent.getPPTFileIntent(currentPath);
            } else {
                intent = OpenFilesIntent.getOtherFileIntent(currentPath);
            }

            if (intent != null) {
                try {
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("Open" + e.getMessage() + "");
                    Toast.makeText(mContext, "系统未检测到打开文件的程序，请选择",
                            Toast.LENGTH_LONG).show();
                    intent = OpenFilesIntent.getOtherFileIntent(currentPath);
                    try {
                        mContext.startActivity(intent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Logger.e("Open2" + e.getMessage() + "");
                    }
                }
            }
        } else {
            Toast.makeText(mContext, "抱歉,这不是一个合法文件！", Toast.LENGTH_LONG).show();
        }
    }


    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }


    /**
     * 同步一下cookie
     */
    public void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
        String newCookie = cookieManager.getCookie("http://crm.tysoft.com");
        Logger.d(newCookie);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewParent parent = mWebView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mWebView);
        }
        mWebView.stopLoading();
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.clearHistory();
        mWebView.clearView();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;

    }
}
