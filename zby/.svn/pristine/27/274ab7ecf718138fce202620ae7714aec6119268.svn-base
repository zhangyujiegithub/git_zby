package com.biaozhunyuan.tianyi.bespoke;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/10/1.
 * 预约列表
 */

public class BespokeListActivity extends BaseActivity {
    private BoeryunHeaderView headerView;
    private BoeryunSearchView searchView;
    private PullToRefreshAndLoadMoreListView lv;

    private Context context;
    private Demand demand;
    private int pageIndex = 1; //当前页
    private CommanAdapter<Bespoke> adapter;
    private String dictionary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bespoke_list);

        context = BespokeListActivity.this;
        initViews();
        initDemand();
        ProgressDialogHelper.show(context);
        getBespokeList();
        setOnEvent();
    }


    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_bespoke_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_view_bespoke_list);
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.lv_bespoke_list);
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

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBespokeList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getBespokeList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bespoke template = adapter.getDataList().get(position - 1);
                Intent intent = new Intent(BespokeListActivity.this, FormInfoActivity.class);
                intent.putExtra("formName", "预约");
                intent.putExtra("workflowTemplateId", "cdbb796a0b174499923870350fe9b399");
                intent.putExtra("formDataId", template.getUuid());
                intent.putExtra("createrId", template.getAdvisorId());
                startActivity(intent);
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
                lv.startRefresh();
                Map<String, String> searchMap = new HashMap<>();
                searchMap.put("searchField_dictionary_productId", str);
                demand.keyMap = searchMap;
                getBespokeList();
            }
        });
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.预约列表;
        demand = new Demand();
        demand.pageSize = 10;
        demand.sort = "";
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "productId.crm_product,customerId.crm_customer,customerType.dict_customer_type,advisorId.base_staff,departmentId.base_department,areaId.base_department,creatorId.base_staff";
        demand.src = url;
    }


    private void getBespokeList() {
        demand.pageIndex = pageIndex;
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.预约列表;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Bespoke> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Bespoke.class);
                    if (list != null) {
                        for (Bespoke bespoke : list) {
                            try {
                                bespoke.setAdvisorName(demand.getDictName(bespoke, "advisorId"));
                                bespoke.setProductName(demand.getDictName(bespoke, "productId"));
                                bespoke.setCustomerName(demand.getDictName(bespoke, "customerId"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        lv.onRefreshComplete();
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.addBottom(list, false);
                            if (list != null && list.size() == 0) {
                                lv.loadAllData();
                            }
                            lv.loadCompleted();
                        }
                        pageIndex += 1;

                    }
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

    private CommanAdapter<Bespoke> getAdapter(List<Bespoke> list) {
        return new CommanAdapter<Bespoke>(list, context, R.layout.item_bespoke_list) {
            @Override
            public void convert(int position, final Bespoke item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_client_ch_contactlist_item, item.getCustomerName());//advisorId.base_staff
                viewHolder.setTextValue(R.id.tv_project_ch_contactlist_item, item.getProductName());//productId.crm_product
                viewHolder.setTextValue(R.id.tv_no_ch_contactlist_item, item.getSerial());//合同编号
                viewHolder.setTextValue(R.id.tv_qixian_ch_contactlist_item, item.getAdvisorName() + "");//理财经理
                viewHolder.setTextValue(R.id.tv_total_ch_contactlist_item, item.getAmountLower() + "");//投资金额
                viewHolder.setTextValue(R.id.tv_jizhun_ch_contactlist_item, "0");//业绩比较基准

                TextView addContract = viewHolder.getView(R.id.tv_add_contract_bespoke_item);
                TextView tvStatus = viewHolder.getView(R.id.tv_type_ch_contactlist_item);

                if (TextUtils.isEmpty(item.getFlowStatus())) {
                    tvStatus.setVisibility(View.GONE);
                } else {
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(item.getFlowStatus());
                }

                if (item.isComplete() && TextUtils.isEmpty(item.getContractId())) {
                    addContract.setVisibility(View.VISIBLE);

                    addContract.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 +
                                    "?workflowTemplateId=b8d3ffd9192c4cfb80c385e5e76f21e4&id=0&contractType=46155ec539ad4e59a0490d9497b8e69c&customerId="
                                    + item.getCustomerId() + "&productId=" + item.getProductId() + "&advisorId=" + item.getAdvisorId() + "&amountLower=" + item.getAmountLower() +
                                    "&duration=" + item.getDuration() + "&floatingDuration=" + item.getFloatingDuration() + "&shareType=" + item.getShareType() + "&operatorHQ=" + item.getOperatorHQ()
                                    + "&expectedAnnualYield=" + item.getExpectedAnnualYield() + "&expectedAnnualYieldType=" + item.getExpectedAnnualYieldType()
                                    + "&reservationId=" + item.getUuid() + "&issue=" + item.getIssue() + "&batchNumber=" + item.getBatchNumber();

                            Intent intent = new Intent(context, FormInfoActivity.class);
                            intent.putExtra("exturaUrl", url);
                            intent.putExtra("formName", "合同申请表");
                            intent.putExtra("formDataId", "0");
                            intent.putExtra("workflowTemplateId", "b8d3ffd9192c4cfb80c385e5e76f21e4");
                            startActivity(intent);
                        }
                    });
                } else {
                    addContract.setVisibility(View.GONE);
                }


            }
        };
    }
}
