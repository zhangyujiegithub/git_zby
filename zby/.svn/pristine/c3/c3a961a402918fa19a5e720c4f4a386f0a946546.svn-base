package com.biaozhunyuan.tianyi.expenseaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;
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
import java.net.URLEncoder;

/**
 * 报销单据
 */

public class ExpenseAccountActivity extends BaseActivity {

    private TextView tv_sheet; //费用报销单
    private TextView tv_travel; //差旅费报销单
    private TextView tv_businessentertainment; //业务招待报销单
    private BoeryunHeaderView headerview;
    private static final int REQUEST_CODE_VATINVOICE = 131;
    private boolean hasGotToken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenseaccount);
        initView();
        setOnTouch();
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
        tv_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipIdentifyInvoice();
            }
        });
        tv_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipIdentifyInvoice();
            }
        });
        tv_businessentertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipIdentifyInvoice();
            }
        });
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        tv_sheet = findViewById(R.id.tv_expensesclaimsheet); //费用报销单
        tv_travel = findViewById(R.id.tv_travelexpenseclaim); //差旅费报销单
        tv_businessentertainment = findViewById(R.id.tv_businessentertainmentexpenseaccount); //业务招待报销单
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
     * 普通方式获取发票信息
     */
    private void getGeneralInfo() {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setVertexesLocation(true);
        param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL);
        param.setImageFile(new File(FileUtil.getSaveFile(getApplication()).getAbsolutePath()));
        OCR.getInstance(this).recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                for (WordSimple wordSimple : result.getWordList()) {
                    Word word = (Word) wordSimple;
                    sb.append(word.getWords());
                    sb.append("\n");
                }
                LogUtils.i("扫描结果：", sb.toString());
            }

            @Override
            public void onError(OCRError error) {

            }
        });
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
                    String attachId = UploadHelper.uploadFileGetAttachId("apply", new File(FileUtil.getSaveFile(getApplication()).getAbsolutePath()));
                    String result = null;
                    try {
                        result = HttpUtil.post(otherHost, accessToken, params);
                        ProgressDialogHelper.dismiss();
                        try {
                            String words_result = JsonUtils.getStringValue(result, "words_result");
                            Invoice invoice = JsonUtils.jsonToEntity(words_result, Invoice.class);
                            invoice.setAttachId(attachId);
                            Intent intent = new Intent(ExpenseAccountActivity.this, IdentifyResultActivity.class);
                            intent.putExtra("scanResult", invoice);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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
}
