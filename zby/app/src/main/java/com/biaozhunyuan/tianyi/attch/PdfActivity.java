package com.biaozhunyuan.tianyi.attch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunProgressDialog;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 王安民 on 2017/9/21.
 * 展示pdf文件
 */

public class PdfActivity extends Activity {

    private BoeryunHeaderView toolBar;
    private PDFView pdfView;


    private String Url;
    private String title;
    private BoeryunProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_show);

        initViews();
        initData();
    }


    private void initViews() {
        toolBar = (BoeryunHeaderView) findViewById(R.id.commonToolBar);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        dialog = new BoeryunProgressDialog(PdfActivity.this);

        toolBar.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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


    protected void initData() {
        Url = getIntent().getStringExtra("filepath");
        title = getIntent().getStringExtra("title");
        toolBar.setTitle(title);
        dialog.show();
        dialog.setTitle("加载中");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Url.replace("\\", "/");
                String cookie = CookieUtils.cookieHeader(url);
                try {
                    URL uri = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                    conn.setRequestProperty("Accept-Charset", "UTF-8");
                    conn.setRequestProperty("connection", "keep-Alive");
                    conn.setRequestProperty("Cookie", cookie);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    pdfView.fromStream(is).load();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), "加载失败", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();
        Logger.e(Url);
    }
}
