package com.biaozhunyuan.tianyi.client;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.activity.ActivityListActivity;
import com.biaozhunyuan.tianyi.activity.ActivityModel;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 选择客户 预约活动
 */
public class SelectedCustomerActivity extends BaseActivity {

    private PullToRefreshAndLoadMoreListView lv;
    private BoeryunHeaderView headerView;
    private CommanAdapter<ActivityModel> adapter;
    private Demand<ActivityModel> demand;
    private int pageIndex = 1;
    private ActivityModel activity = new ActivityModel();
    private List<ActivityModel> selectCustomer = new ArrayList<>();
    private String customerIds = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_customer);
        initView();
        initDemand();
        getIntentData();
        getCustomerList();
        setOnTouch();
    }

    private void setOnTouch() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if(selectCustomer.size()>0) {
                    customerIds = "";
                    for (int i = 0; i < selectCustomer.size(); i++) {
                        if (selectCustomer.get(i).isSelected()) {
                            customerIds += selectCustomer.get(i).getUuid() + ",";
                        }
                    }
                    String substring = customerIds.substring(0, customerIds.length() - 1);
                    orderActivity(substring);
                } else {
                    showShortToast("没有选择客户");
                }
            }

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
                getCustomerList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getCustomerList();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ActivityModel item = adapter.getItem(position - 1);
                    if (item.isSelected()) {
                        removeSelected(item);
                    } else {
                        addSelected(item);
                    }
                    item.setSelected(!item.isSelected());
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void orderActivity(String customerIds) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.预约活动;
        JSONObject js = new JSONObject();
        try {
            js.put("customerIds",customerIds);
            js.put("advisorId",Global.mUser.getUuid());
            js.put("activityId",activity.getUuid());

            StringRequest.postAsyn(url,js, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    showShortToast("预约成功");
                    ActivityListActivity.isResume = true;
                    finish();
                }

                @Override
                public void onFailure(Request request, Exception ex) {

                }

                @Override
                public void onResponseCodeErro(String result) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //从集合移除选中的客户
    public void removeSelected(ActivityModel user) {
        ActivityModel uu = null;
        for (ActivityModel u : selectCustomer) {
            if (u.getUuid().equals(user.getUuid())) {
                uu = u;
            }
        }
        if (uu != null) {
            selectCustomer.remove(uu);
        }
    }

    //添加客户
    public void addSelected(ActivityModel user) {
        selectCustomer.add(user);
    }


    private void getIntentData() {
        if (getIntent().getExtras().getSerializable("activity") != null) {
            activity = (ActivityModel) getIntent().getExtras().getSerializable("activity");
        }
    }

    private void getCustomerList() {
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.选择可预约活动的客户列表 + activity.getUuid();
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<ActivityModel> data = demand.data;
                if (data != null) {
                    lv.onRefreshComplete();
                    if (pageIndex == 1) {
                        adapter = getAdapter(data);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.addBottom(data, false);
                        if (data != null && data.size() == 0) {
                            lv.loadAllData();
                        }
                        lv.loadCompleted();
                    }
                    pageIndex += 1;

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

    private CommanAdapter<ActivityModel> getAdapter(List<ActivityModel> list) {
        return new CommanAdapter<ActivityModel>(list, this, R.layout.item_select_customer_list) {
            @Override
            public void convert(int position, final ActivityModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                viewHolder.setTextValue(R.id.tel_item_workmate, item.getCustomerNumber());
                viewHolder.setTextValue(R.id.landline_item_workmate, item.getMobile());
                if (item.isSelected()) {
                    viewHolder.getView(R.id.choose_item_select_user).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.choose_item_select_user).setVisibility(View.GONE);
                }
            }
        };
    }

    private void initDemand() {
        demand = new Demand<>(ActivityModel.class);
//        demand.sortField = "createTime desc";
        demand.dictionaryNames = "";
        demand.pageSize = 10;
    }

    private void initView() {
        lv = findViewById(R.id.lv);
        headerView = findViewById(R.id.boeryun_headview);
    }
}
