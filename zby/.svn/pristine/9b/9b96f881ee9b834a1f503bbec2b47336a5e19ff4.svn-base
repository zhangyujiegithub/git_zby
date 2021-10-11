package com.biaozhunyuan.tianyi.bespoke;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.product.Product;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/10/1.
 * 预约选择界面
 */

public class BespokeNewActivity extends BaseActivity {

    private Context context;
    private final int REQUEST_SELECT_CLIENT = 1;
    private Product mProduct;
    private String productId = "";
    private String shareType = "";
    private String amountLower = "";
    private String duration = "";
    private String customerId = "";
    private String advisorId = "";
    private String expectedAnnualYield = "";
    private String floatingDuration = "";
    private List<ShareType> typeList;
    private List<Yield> yieldList;
    private List<ProductIssue> issueList;
    private List<String> issueShowList = new ArrayList<String>();
    private ProductIssue selectIssure = null;

    private BoeryunHeaderView headerView;
    private DictIosPickerBottomDialog dialog;
    private DictIosPickerBottomDialog dialog2;
    private TextView et_fene;// 份额类型
    private EditText et_money;//预约金额
    private TextView et_qixian;//投资期限
    private TextView et_client;//客户
    private Button btn_bespoke;

    private String errorMessage = "";

    private RelativeLayout rl_fene;
    private RelativeLayout rl_qixian;
    private RelativeLayout rl_money;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_new_bespoke);
        context = BespokeNewActivity.this;
        dialog = new DictIosPickerBottomDialog(context);
        dialog2 = new DictIosPickerBottomDialog(context);
        initViews();
        getIntentData();
        setOnEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_CLIENT:
                    if (data != null && data.getExtras() != null) {
                        Client client = (Client) data.getExtras().getSerializable("clientInfo");
                        if (client != null) {
                            customerId = client.getUuid();
                            advisorId = client.getAdvisor();
                            et_client.setText(client.getName());
                            validateClient(customerId); //验证客户
                        }
                    }
            }
        }
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            mProduct = (Product) getIntent().getSerializableExtra("productInfo");
            productId = getIntent().getStringExtra("productId");
            customerId = getIntent().getStringExtra("customerId");
            advisorId = getIntent().getStringExtra("advisorId");
            et_client.setText(getIntent().getStringExtra("customerName"));
            et_client.setEnabled(false);
            et_client.setFocusable(false);

            if (!TextUtils.isEmpty(productId)) {
//                getCommissionRate();
                getProductIssure(productId);
            }
        }
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_new_bespoke);
        et_fene = (TextView) findViewById(R.id.et_bespoke_fene_leixing);
        et_money = (EditText) findViewById(R.id.et_bespoke_yuyue_jine);
        et_qixian = (TextView) findViewById(R.id.et_bespoke_touzi_qixian);
        et_client = (TextView) findViewById(R.id.et_bespoke_client);
        btn_bespoke = (Button) findViewById(R.id.btn_bespoke_new_bespoke);
        rl_fene = (RelativeLayout) findViewById(R.id.rl_bespoke_new_fene);
        rl_qixian = (RelativeLayout) findViewById(R.id.rl_bespoke_new_qixian);
        rl_money = (RelativeLayout) findViewById(R.id.rl_bespoke_new_money);
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    int money = Integer.parseInt(s.toString());
                    getDurations(money * 10000);
                }
            }
        });

        et_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ClientListActivity.class);
                intent.putExtra("isSelectCliet", true);
                startActivityForResult(intent, REQUEST_SELECT_CLIENT);
            }
        });

        btn_bespoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(et_money.getText().toString().trim())) {
                    showShortToast("预约金额不能为空!");
                    return;
                }

//                if (TextUtils.isEmpty(duration)) {
//                    showShortToast("没有投资期限!");
//                    return;
//                }
                if (TextUtils.isEmpty(customerId)) {
                    showShortToast("客户不能为空!");
                    return;
                }

                if (!TextUtils.isEmpty(errorMessage)) {
                    showShortToast(errorMessage);
                    return;
                }

                validateEdu(amountLower);

            }
        });
    }


    private void startInfoActivity() {
        String issue = "";
        String batchNumber = "";
        if (selectIssure != null) {
            issue = selectIssure.getIssue();
            batchNumber = selectIssure.getBatchNumber();
        }

        String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
                "?workflowTemplateId=cdbb796a0b174499923870350fe9b399&id=0&contractType=46155ec539ad4e59a0490d9497b8e69c&customerId="
                + customerId + "&type=46155ec539ad4e59a0490d9497b8e69c&serial=SYZC00000124&issue=" + issue +
                "&batchNumber=" + batchNumber + "&productId=" + productId + "&advisorId=" + advisorId + "&amountLower=" + amountLower +
                "&duration=" + duration + "&floatingDuration=" + floatingDuration + "&expectedAnnualYield=" + expectedAnnualYield;

        Intent intent = new Intent(context, FormInfoActivity.class);
        intent.putExtra("exturaUrl", url);
        intent.putExtra("formName", "预约");
        intent.putExtra("formDataId", "0");
        intent.putExtra("workflowTemplateId", "cdbb796a0b174499923870350fe9b399");
        startActivity(intent);
        finish();
    }


    private void validateClient(String clientId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.预约验证客户 + "?uuid=" + clientId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if ("1".equals(JsonUtils.parseStatus(response))) {
                    errorMessage = "";
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                errorMessage = JsonUtils.pareseMessage(result);
            }
        });
    }


    /**
     * 获取份额类型
     */
    private void getCommissionRate() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.预约查看份额类型;

        JSONObject jo = new JSONObject();
        try {
            jo.put("productId", productId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    typeList = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(response, "Data"), ShareType.class);
                    if (typeList != null && typeList.size() > 0) {
                        shareType = typeList.get(0).getShareType();
                        if (!TextUtils.isEmpty(shareType)) {
                            rl_fene.setVisibility(View.VISIBLE);
                        }
                        et_fene.setText(shareType);  //显示份额类型

                        final List<String> list = new ArrayList<String>();
                        for (ShareType type : typeList) {
                            list.add(type.getShareType());
                        }

                        et_fene.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (typeList.size() > 1) {
                                    dialog.show(list);
                                }
                            }
                        });

                        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {
                                shareType = list.get(index);
                                et_fene.setText(shareType);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
     * 获取期限
     *
     * @param money
     */
    private void getDurations(int money) {
        amountLower = money + "";
        String url = Global.BASE_JAVA_URL + GlobalMethord.预约查看期限;

        JSONObject jo = new JSONObject();
        try {
            jo.put("productId", productId);
            jo.put("amount", money + "");
            if (selectIssure != null) {
                jo.put("issue", selectIssure.getIssue());
                jo.put("batchNumber", selectIssure.getBatchNumber());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    yieldList = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(response, "Data"), Yield.class);
                    if (yieldList != null && yieldList.size() > 0) {
                        duration = yieldList.get(0).getDuration() + "";
                        if (!TextUtils.isEmpty(duration)) {
                            rl_qixian.setVisibility(View.VISIBLE);
                        } else {
                            rl_qixian.setVisibility(View.GONE);
                        }
//                        et_qixian.setText(duration);  //显示期限
                        expectedAnnualYield = yieldList.get(0).getYield() + "";
                        floatingDuration = yieldList.get(0).getFloatingDuration() + "";

                        final List<String> list = new ArrayList<String>();
                        for (Yield yield : yieldList) {
                            list.add(yield.getDuration() + "+" + yield.getFloatingDuration());
                        }

                        et_qixian.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.show(list);
                                dialog2.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                    @Override
                                    public void onSelected(int index) {
                                        duration = yieldList.get(index).getDuration() + "";
                                        expectedAnnualYield = yieldList.get(index).getYield() + "";
                                        floatingDuration = yieldList.get(index).getFloatingDuration() + "";
                                        et_qixian.setText(list.get(index));
                                    }
                                });
                            }
                        });
                    } else {
                        rl_qixian.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(JsonUtils.pareseMessage(result));
            }
        });
    }


    /**
     * 获取在售期次
     */
    private void getProductIssure(String productId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.产品期次 + "?productId=" + productId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                issueList = JsonUtils.ConvertJsonToList(response, ProductIssue.class);

                if (issueList != null && issueList.size() > 0) {
                    for (int i = 0; i < issueList.size(); i++) {
                        ProductIssue issue = issueList.get(i);
                        issueShowList.add(issue.getIssue() + "-" + issue.getBatchNumber());
                    }
                }

                et_fene.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (issueShowList.size() > 0) {
                            dialog.show(issueShowList);
                        } else {
                            showShortToast("该产品没有在售期次");
                        }
                    }
                });

                dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        selectIssure = issueList.get(index);
                        et_fene.setText(issueShowList.get(index));
                    }
                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }


    private void validateEdu(String investAmounts) {
        Double b = Double.valueOf(investAmounts);
        BigDecimal bg = BigDecimal.valueOf(b);
        String url = Global.BASE_JAVA_URL + GlobalMethord.验证额度 + "?productId=" + productId + "&investAmounts=" + bg
                + "&raisingAmounts=" + mProduct.getRaisingAmount() + "&reserveCountControl=" + mProduct.getReserveCountControl()
                + "&maxCustomerAccount=" + mProduct.getMaxCustomerAccount();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                startInfoActivity();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(JsonUtils.pareseData(result));
            }
        });
    }
}
