package com.biaozhunyuan.tianyi.login;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;


/**
 * 服务条款页面，用webview加载网页
 */
public class ServiceItemActivity extends BaseActivity {

    private WebView webView;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_item);
        initView();
        initData();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView_service_item);
        iv_back = (ImageView) findViewById(R.id.iv_back_service_item);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        webView.loadUrl(Global.BASE_JAVA_URL + GlobalMethord.服务条款);
    }
}
