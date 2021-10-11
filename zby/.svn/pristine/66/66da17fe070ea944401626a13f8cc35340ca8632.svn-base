package com.biaozhunyuan.tianyi.contract;

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
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
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
 * 合同列表
 */

public class ContractListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private BoeryunSearchView searchView;
    private PullToRefreshAndLoadMoreListView lv;

    private Context context;
    private Demand demand;
    private int pageIndex = 1; //当前页
    private CommanAdapter<Contract> adapter;
    private String dictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_list);

        context = ContractListActivity.this;
        initViews();
        initDemand();
        ProgressDialogHelper.show(ContractListActivity.this);
        getContractList();
        setOnEvent();
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_contract_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_view_contract_list);
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.lv_contract_list);
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
                getContractList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getContractList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contract template = adapter.getDataList().get(position - 1);
                Intent intent = new Intent(ContractListActivity.this, FormInfoActivity.class);
                intent.putExtra("formName", "合同申请表");
                if (ContractTypeEnum.受让.getUuid().equals(template.getContractType())) {
                    intent.putExtra("workflowTemplateId", "7b02fa27d82349cf830ae1fcb3a76985");
                } else {
                    intent.putExtra("workflowTemplateId", "b8d3ffd9192c4cfb80c385e5e76f21e4");
                }
                if (template.getOriginalContractId() != null && template.getOriginalContractId().length() > 0) {
                    intent.putExtra("formDataId", template.getOriginalContractId());
                } else {
                    intent.putExtra("formDataId", template.getUuid());
                }
                intent.putExtra("createrId", "");
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
                getContractList();
            }
        });
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.合同列表;
        demand = new Demand();
        demand.pageSize = 10;
        demand.sort = "";
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "customerId.crm_customer,productId.crm_product,branchId.base_department,advisorId.base_staff,customerType.dict_customer_type,certificateType.dict_user_passtype";
        demand.src = url;
    }


    private void getContractList() {
        demand.pageIndex = pageIndex;
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.合同列表;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Contract> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Contract.class);
                    dictionary = JsonUtils.getStringValue(JsonUtils.pareseData(response), "dictionary");
                    if (list != null) {
                        for (Contract contract : list) {
                            contract.setAdvisorName(JsonUtils.getDictValueById(JsonUtils.getDictByName(dictionary, "advisorId.base_staff"), contract.getAdvisorId()));
                            contract.setProductName(JsonUtils.getDictValueById(JsonUtils.getDictByName(dictionary, "productId.crm_product"), contract.getProductId()));
                            contract.setCustomerName(JsonUtils.getDictValueById(JsonUtils.getDictByName(dictionary, "customerId.crm_customer"), contract.getCustomerId()));
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

    private CommanAdapter<Contract> getAdapter(List<Contract> list) {
        return new CommanAdapter<Contract>(list, context, R.layout.item_contact_list) {
            @Override
            public void convert(int position, Contract item, BoeryunViewHolder viewHolder) {

                String Yield = item.getExpectedAnnualYield() + "";
                String transferSymbol = "";
                if (item.getTransferSymbol() != null) {
                    transferSymbol = item.getTransferSymbol() > 0 ? "(已转" + item.getTransferSymbol() + "次)" : "";
                }
                viewHolder.setTextValue(R.id.tv_client_ch_contactlist_item, item.getCustomerName());//advisorId.base_staff
                viewHolder.setTextValue(R.id.tv_project_ch_contactlist_item, item.getProductName());//productId.crm_product
                viewHolder.setTextValue(R.id.tv_no_ch_contactlist_item, "#" + item.getSerial() + transferSymbol);//合同编号
                viewHolder.setTextValue(R.id.tv_qixian_ch_contactlist_item, item.getDuration() + "");//投资期限
                viewHolder.setTextValue(R.id.tv_total_ch_contactlist_item, item.getOriginalContractAmountLower() + "");//投资金额
                viewHolder.setTextValue(R.id.tv_jizhun_ch_contactlist_item, StrUtils.isEmpty(Yield) ? "0%" : item.getExpectedAnnualYield() + "%");//业绩比较基准

                TextView tvStatus = viewHolder.getView(R.id.tv_type_ch_contactlist_item);

                if (TextUtils.isEmpty(item.getFlowStatus())) {
                    tvStatus.setVisibility(View.GONE);
                } else {
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(item.getFlowStatus());
                }
            }
        };
    }
}
