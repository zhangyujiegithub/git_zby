package com.biaozhunyuan.tianyi.expenseaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.io.Serializable;
import java.util.List;

public class InvoiceListActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private Button btn_goon;
    private ListView lv;

    private List<Invoice> invoiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        initView();
        getIntentData();
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

        btn_goon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //新建费用报销单
                String url = Global.BASE_JAVA_URL + "wf/form/vsheet/formForMobile?id=0&workflowTemplateId=c09223b3908045a3847775ebd7cb9ce4";
                Intent intent = new Intent(InvoiceListActivity.this, FormInfoActivity.class);
                intent.putExtra("exturaUrl", url);
                intent.putExtra("invoiceList", (Serializable) invoiceList);
                startActivity(intent);
            }
        });
    }

    private void getIntentData() {
        if (getIntent().getExtras().getSerializable("invoiceList") != null) {
            invoiceList = (List<Invoice>) getIntent().getExtras().getSerializable("invoiceList");
            if (invoiceList != null) {
                lv.setAdapter(getAdapter(invoiceList));
            }
        }
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        lv = findViewById(R.id.lv);
        btn_goon = findViewById(R.id.btn_goon);
    }

    private CommanAdapter<Invoice> getAdapter(final List<Invoice> list) {
        return new CommanAdapter<Invoice>(list, InvoiceListActivity.this, R.layout.item_invoice_list) {
            @Override
            public void convert(int position, final Invoice item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_number, "No." + item.getInvoiceNum());  //发票号码
                viewHolder.setTextValue(R.id.tv_amount, "¥" + item.getAmountInFiguers());  //金额
                viewHolder.setTextValue(R.id.tv_type, item.getCommodityName().get(0).getWord());  //类型
                viewHolder.setTextValue(R.id.tv_time, item.getInvoiceDate());  //开票日期

                ImageView iv_delete = viewHolder.getView(R.id.iv_delete);

                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        invoiceList.remove(item);
                        notifyDataSetChanged();
                    }
                });
            }
        };
    }


}
