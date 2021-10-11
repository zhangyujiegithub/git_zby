package com.biaozhunyuan.tianyi.expenseaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.expenseaccount.camera.CameraActivity;
import com.biaozhunyuan.tianyi.expenseaccount.camera.FileUtil;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.Base64Util;
import com.biaozhunyuan.tianyi.common.utils.HttpUtil;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class IdentifyResultActivity extends BaseActivity {

    private static final int REQUEST_CODE_VATINVOICE = 141;

    private BoeryunHeaderView headerview;
    private TextView tv_number; //发票号码
    private TextView tv_amount; //发票金额
    private TextView tv_type; //发票类型
    private TextView tv_count; //发票数量
    private TextView tv_total_amount; //发票总金额
    private Button btn_add; //继续添加
    private Button btn_finish; //添加完成
    private Invoice invoice;
    private List<Invoice> dataList = new ArrayList<>();


    private List<Invoice> invoices = new ArrayList<>();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    ProgressDialogHelper.dismiss();
                    try {
                        String result = msg.getData().getString("result");
                        String attachId = msg.getData().getString("attachId");
                        String words_result = JsonUtils.getStringValue(result, "words_result");
                        Invoice invoice = JsonUtils.jsonToEntity(words_result, Invoice.class);
                        if (invoice != null) {
                            invoice.setAttachId(attachId);
                            tv_amount.setText(invoice.getAmountInFiguers());
                            tv_number.setText(invoice.getInvoiceNum());
                            tv_type.setText( invoice.getCommodityName().get(0).getWord());
                            invoices.add(invoice);
                            displayTotal();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_result);
        initView();
        getIntentExture();
        setOnTouch();
    }

    private void getIntentExture() {
        if (getIntent().getExtras() != null) {
            Invoice invoice = (Invoice) getIntent().getSerializableExtra("scanResult");
            if (invoice != null) {
                invoices.add(invoice);
                initData(invoice);
                displayTotal();
            }
        }
    }

    private void setOnTouch() {
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


        //继续添加发票
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipIdentifyInvoice();
            }
        });


        //发票添加完毕，跳转到列表页面
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IdentifyResultActivity.this, InvoiceListActivity.class);
                intent.putExtra("invoiceList", (Serializable) invoices);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        tv_number = findViewById(R.id.tv_number);
        tv_amount = findViewById(R.id.tv_amount);
        tv_type = findViewById(R.id.tv_type);
        tv_count = findViewById(R.id.tv_count);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        btn_add = findViewById(R.id.btn_add);
        btn_finish = findViewById(R.id.btn_finish);
    }

    private void initData(Invoice invoice) {
        tv_number.setText(invoice.getInvoiceNum());
        tv_amount.setText(invoice.getAmountInFiguers());
        tv_type.setText( invoice.getCommodityName().get(0).getWord());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，增值税发票
        if (requestCode == REQUEST_CODE_VATINVOICE && resultCode == Activity.RESULT_OK) {
            getInvoiceInfo();
//            getGeneralInfo();
        }
    }


    /**
     * 获取增值发票信息
     */
    private void getInvoiceInfo() {
        ProgressDialogHelper.show(this, "识别中...");
        String accessToken = PreferceManager.getInsance().getValueBYkey("BaiduDiscriminateAccessToken");

        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice";

        byte[] imgData;

        String params;
        try {
            imgData = com.biaozhunyuan.tianyi.common.utils.FileUtil.readFileByBytes(FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            String imgStr = Base64Util.encode(imgData);
            params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8") + "&accuracy" + "=" + "high";

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String result = null;
                    String attachId = UploadHelper.uploadFileGetAttachId("apply", new File(FileUtil.getSaveFile(getApplication()).getAbsolutePath()));
                    try {
                        result = HttpUtil.post(otherHost, accessToken, params);
                        Message msg = new Message();
                        msg.what = 1;
                        msg.getData().putString("attachId",attachId);
                        msg.getData().putString("result",result);
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LogUtils.i("scanResult:", result);
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示总金额和总数量
     */
    private void displayTotal() {
        double total = 0;
        for (Invoice invoice1 : invoices) {
            if (TextUtils.isEmpty(invoice1.getAmountInFiguers())) {
                invoice1.setAmountInFiguers("0");
            }
            total += Double.valueOf(invoice1.getAmountInFiguers());
        }
        //显示总金额和总数量
        tv_total_amount.setText(total + "");
        tv_count.setText(invoices.size() + "");
    }

    /**
     * 调用相机识别发票
     */
    private void skipIdentifyInvoice() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_VATINVOICE);
    }
}
