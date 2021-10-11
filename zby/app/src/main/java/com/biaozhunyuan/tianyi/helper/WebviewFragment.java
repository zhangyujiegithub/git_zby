package com.biaozhunyuan.tianyi.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.OpenIntentUtils;
import com.biaozhunyuan.tianyi.utils.UrlUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/7/23.
 */

public class WebviewFragment extends Fragment {

    private String url = "";
    private WebView webView;
    private BoeryunApp app;
    private Context mContext;
    private boolean isResume = false;
    private String customerId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("URL");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null && isResume) {
            webView.reload();
            isResume = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview_fragment, null);
        webView = view.findViewById(R.id.webview_fragment);
        app = (BoeryunApp) getActivity().getApplication();
        mContext = getActivity();
        loadWebView();
        return view;
    }


    public static WebviewFragment getInstance(String url) {
        WebviewFragment webviewFragment = new WebviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        webviewFragment.setArguments(bundle);
        return webviewFragment;
    }

    public void reLoad() {
        if (webView != null) {
            webView.reload();
        }
    }

    public void setCustomerId(String id) {
        customerId = id;
    }

    /**
     * 浏览器模式加载表单
     */
    private void loadWebView() {
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
        webSettings.setSupportZoom(false);
        // 缩放按钮
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(false);
        synCookies(getActivity());
        Logger.i("url_webview::" + url);
        webView.loadData("", "text/html", "UTF-8");
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("wf/form/vsheet/form?")) { //跳转到表单页面
                    webView.stopLoading();
                    webView.goBack();
                    isResume = true;
                    String str = UrlUtils.formatEncode(url).replaceAll("%25", "%");
                    String[] splitId = url.split("id=");
                    String[] split = str.split("\\?");

                    Map<String, String> map = new HashMap<>();
                    List<String> detailId = new ArrayList<>();
                    String[] split2 = url.split("id=0");
                    if (split2.length > 1 && !TextUtils.isEmpty(split2[1])) {
                        String[] split3 = split2[1].split("detaildata=");
                        if (split3.length > 1) {
                            //取到detaildata，将所有^替换成|，再以|分割
                            String[] splitDetail = split3[1].replaceAll("\\^", "\\|").split("\\|");
                            for (String s : splitDetail) {
                                if (!TextUtils.isEmpty(s) && s.contains("detailId_")) {
                                    detailId.add(s.replace("detailId_", ""));
                                }
                            }
                        }


                        String s;
                        if (split3.length > 0) {
                            s = split3[0];
                        } else {
                            s = split2[1];
                        }
                        String[] split0 = s.split("&");
                        for (int i = 0; i < split0.length; i++) {
                            String[] split1 = split0[i].split("=");
                            if (split1.length > 1) {
                                map.put(split1[0], split1[1]);
                            }
                        }
                    }
                    String formUrl = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?" + split[1];
                    Intent intent = new Intent(mContext, FormInfoActivity.class);
                    intent.putExtra("exturaUrl", formUrl);
                    if (map.size() > 0) {
                        intent.putExtra("extentMap", (Serializable) map);
                    }
                    if (detailId.size() > 0) {
                        intent.putExtra("detailIds", (Serializable) detailId);
                    }
                    if (splitId[1].length() > 1) {
                        intent.putExtra("formDataId", splitId[1].split("&")[0]);
                    }
                    startActivity(intent);
                } else if (url.contains("#/order/detail")) {
                    if (!url.contains("http")) {
                        url = "http://" + url;
                    }
                    webView.stopLoading();
                    Intent intent = new Intent();
                    intent.setClass(mContext, WebviewNormalActivity.class);
                    intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, "详情");
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
                    startActivity(intent);
                    webView.goBack();
                } else if (url.contains("downLoadFile")) {
                    webView.stopLoading();
                    webView.goBack();
                    String path = url.substring(url.indexOf("path=") + 5);
                    String name = (ViewHelper.getCurrentFullTime() + ".pdf");
                    name = name.replaceAll("-", "_");
                    name = name.replaceAll(" ", "_");
                    name = name.replaceAll(":", "_");

                    if (!TextUtils.isEmpty(customerId)) {
                        String filepath = FilePathConfig.getCacheDirPath() + "/" + customerId + ".pdf";
                        File file = new File(filepath);
                        if (file.exists() && file.length() > 0) {
                            OpenIntentUtils.openFile(mContext, filepath);
                        } else {
                            downloadFile(path, customerId + ".pdf");
                        }
                    } else {
                        downloadFile(path, name);
                    }
                }
                super.onPageFinished(view, url);
            }
        });
    }

    private void downloadFile(String path, String name) {
        ProgressDialogHelper.show(mContext, "下载中...");
        StringRequest.downloadFile(path, name, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                OpenIntentUtils.openFile(mContext,
                        FilePathConfig.getCacheDirPath() + "/" + response);
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
     * 同步一下cookie
     */
    public void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(".tysoft.com", CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
        String newCookie = cookieManager.getCookie("http://crm.tysoft.com");
        Logger.d(newCookie);
    }
}
