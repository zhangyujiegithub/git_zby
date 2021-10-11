package com.biaozhunyuan.tianyi.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.bespoke.BespokeListActivity;
import com.biaozhunyuan.tianyi.bespoke.BespokeNewActivity;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
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
 * Created by 王安民 on 2017/9/23.
 * 产品列表页面
 */

public class ProductListActivity extends BaseActivity {

    private final int REQUEST_SELECT_CLIENT = 1;

    private Context context;
    private Demand demand;
    private int pageIndex = 1; //当前页
    private CommanAdapter<Product> adapter;
    private String dictionary;

    private BoeryunHeaderView headerView;
    private BoeryunSearchView searchView;
    private PullToRefreshAndLoadMoreListView lv;
    private String productId = "";
    private Product selectProduct;

    public static boolean isResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);
        context = ProductListActivity.this;

        initViews();
        initDemand();
        ProgressDialogHelper.show(ProductListActivity.this);
        getProductList();
        setOnEvent();
    }

    @Override
    protected void onResume() {
        if (isResume) {
            startActivity(new Intent(this, BespokeListActivity.class));
            isResume = false;
            finish();
        }
        super.onResume();
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
                            Intent intent = new Intent(context, BespokeNewActivity.class);
                            intent.putExtra("productId", productId);
                            intent.putExtra("productInfo", selectProduct);
                            intent.putExtra("customerId", client.getUuid());
                            intent.putExtra("advisorId", client.getAdvisor());
                            intent.putExtra("customerName", client.getName());
                            startActivity(intent);
                        }
                    }
            }
        }
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_product_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_view_product_list);
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.lv_product_list);
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
                getProductList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getProductList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product item = adapter.getDataList().get(position - 1);
                String url = Global.BASE_JAVA_URL + GlobalMethord.产品详情2 + item.getUuid() + "&type=1";
                Intent intent = new Intent(context, ProductInfoActivity.class);
                intent.putExtra(ProductInfoActivity.EXTRA_TITLE, "产品详情");
                intent.putExtra(ProductInfoActivity.PRODUCT_ID, item.getUuid());
                intent.putExtra(ProductInfoActivity.EXTRA_URL, url);
                startActivity(intent);
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
                lv.startRefresh();
                Map<String, String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_name", "1|" + str);
                demand.keyMap = searchMap;
                getProductList();
            }
        });
    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.产品列表;
        demand = new Demand();
        demand.pageSize = 10;
        demand.sort = "";
        demand.sortField = "name desc";
        demand.dictionaryNames = "operatorHQ.base_staff,productType.dict_product_type,status.dict_product_status,riskLevelType.dict_product_risklevel";
        demand.src = url;
    }


    private void getProductList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Product> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Product.class);

                    if (list != null) {
                        dictionary = JsonUtils.getStringValue(JsonUtils.pareseData(response), "dictionary");

                        for (Product product : list) {
                            String typeName = JsonUtils.getDictValueById(JsonUtils.getDictByName(dictionary, "productType.dict_product_type"), product.getProductType());
                            product.setProductTypeName(typeName);
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


    private CommanAdapter<Product> getAdapter(List<Product> list) {
        return new CommanAdapter<Product>(list, context, R.layout.item_product_list) {
            @Override
            public void convert(int position, final Product item, BoeryunViewHolder viewHolder) {
                String type = item.getProductTypeName();
                TextView tv_type = viewHolder.getView(R.id.tv_type_product_list);

                if (!TextUtils.isEmpty(type)) {
                    tv_type.setVisibility(View.VISIBLE);
                    tv_type.setText(type);
                } else {
                    tv_type.setVisibility(View.GONE);
                }


                TextView tv_jizhun = viewHolder.getView(R.id.yeji_jizhun_product_list);

                if (TextUtils.isEmpty(item.getFloatYield() + "") || "null".equals(item.getFloatYield() + "")) {  //业绩比较基准
                    tv_jizhun.setVisibility(View.GONE);
                } else {
                    tv_jizhun.setVisibility(View.VISIBLE);
                    tv_jizhun.setText(item.getFloatYield() + "%");
                }

                if (!TextUtils.isEmpty(item.getCollectStart()) && !TextUtils.isEmpty(item.getCollectEnd())) {
                    int leaveDay = DateTimeUtil.getBetweenDays(item.getCollectStart(), item.getCollectEnd());
                    viewHolder.setTextValue(R.id.shengyu_product_list, leaveDay + "天");  //剩余天数
                }

//
                viewHolder.setTextValue(R.id.tv_guimo_product_list, item.getFundScale());  //规模
                viewHolder.setTextValue(R.id.edu_product_list, TextUtils.isEmpty(item.getReserved()) ? "0" : item.getReserved());  //剩余额度
//                viewHolder.setTextValue(R.id.tv_rengou_qidian_product_list, item.getMinDeal() + "");  //认购起点
//                viewHolder.setTextValue(R.id.tv_muji_shijian_product_list, item.getCollectStart() + "--" + item.getCollectEnd());  //募集时间
                viewHolder.setTextValue(R.id.tv_name_product_list, item.getName()); //产品名称
//
                TextView tvBespoke = viewHolder.getView(R.id.tv_yuyue_product_list);

                tvBespoke.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(context, BespokeNewActivity.class);
//                        intent.putExtra("productId", item.getUuid());
//                        startActivity(intent);
                        productId = item.getUuid();
                        selectProduct = item;
                        Intent intent = new Intent(context, ClientListActivity.class);
                        intent.putExtra("isSelectCliet", true);
                        startActivityForResult(intent, REQUEST_SELECT_CLIENT);
                    }
                });
            }
        };
    }

}
