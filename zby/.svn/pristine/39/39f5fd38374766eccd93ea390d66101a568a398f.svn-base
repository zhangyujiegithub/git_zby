package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;

/**
 * 首页用户自定义排版: 嵌套H5页面
 */

public class HomeNormalWebViewLayout extends LinearLayout {

    private Context mContext;
    private WebView webView;
    private TextView tvTitle;
    private String webViewUrl = "";
    private String title = "";

    public HomeNormalWebViewLayout(Context context, String url, String title) {
        super(context);
        webViewUrl = url;
        this.title = title;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 16, 0, 0);
        this.setLayoutParams(layoutParams); //设置上边距
        this.setBackgroundColor(Color.parseColor("#ffffff"));

        View mParentView = LayoutInflater.from(context).
                inflate(R.layout.include_home_webview, this, true);

        initView(mParentView);

        initData();

        loadWebView();
    }

    private void loadWebView() {
        webView.setInitialScale(25);
        // 得到浏览器设置
        WebSettings webSettings = webView.getSettings();
//        // 设置浏览器中javascript可执行
//        webSettings.setJavaScriptEnabled(true);
        // 设置webview推荐使用的窗口
        webSettings.setUseWideViewPort(true);
        // 置webview加载的页面的
        webSettings.setLoadWithOverviewMode(true);
        // 是否显示缩放图标，默认显示
        // 设置网页内容自适应屏幕大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = mContext.getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);
        synCookies(mContext);
        Logger.i("url_webview::" + webViewUrl);
        //headers.put("Cookie",);
        webView.loadData("", "text/html", "UTF-8");
        webView.loadUrl(webViewUrl);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
            }
        });
    }

    private void initData() {
        tvTitle.setText(title);
    }

    private void initView(View view) {
        webView = view.findViewById(R.id.webview);
        tvTitle = view.findViewById(R.id.tv_title);
    }
    /**
     * 同步一下cookie
     */
    public void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.JSESSIONID + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.JSESSIONID));
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
    }
}
