package com.biaozhunyuan.tianyi.project;

import android.content.Context;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

public class CartogramMapActivity extends BaseActivity {


    private WebView mWebView;
    private BoeryunHeaderView headerview;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartogram_map);
        initView();
        loadWebView();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mWebView.reload();
    }

    /**
     * 浏览器模式加载表单
     */
    private void loadWebView() {
        url = Global.BASE_JAVA_URL + GlobalMethord.莱恩斯统计图;
        mWebView.setInitialScale(25);
        // 得到浏览器设置
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);// 可以使用插件
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
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
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);
        synCookies(CartogramMapActivity.this);
        //headers.put("Cookie",);
        mWebView.loadData("", "text/html", "UTF-8");


        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        };
        mWebView.setWebViewClient(wvc);

        mWebView.loadUrl(url);

    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        mWebView = findViewById(R.id.webview);

        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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
