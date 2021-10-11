package com.biaozhunyuan.tianyi.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.utils.ToastUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.contact.ContactNewActivity;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 客户列表 :公海
 */

@SuppressLint("ValidFragment")
public class ClientlistpublicFragment extends LazyFragment {
    private Demand demand = new Demand();
    private Context context;
    private int pageIndex = 1; //当前页
    private CommanAdapter<Client> adapter;
    private BoeryunSearchView searchView;
    //    private PullToRefreshAndLoadMoreListView lv;
    private NoScrollListView lv;
    private DictionaryHelper helper;
    public static boolean isResume = false;
    private int fragmentID = 0;
    private Boolean ispublicClientPhoneCanVisibile; //公海客户电话是否可见
    private AutoMaxHeightViewpager vp;
    private View v;

    public ClientlistpublicFragment(AutoMaxHeightViewpager vp, int fragmentID) {
        this.vp = vp;
        this.fragmentID = fragmentID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_client_client, null);
        initView(v);
        initData();
        vp.setObjectForPosition(v, fragmentID);
        initDemand();
        ProgressDialogHelper.show(getActivity());
        getClientList(null);
        setOnEvent();
        return v;
    }

    private void initData() {
        context = getActivity();
        helper = new DictionaryHelper(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (isResume) {
            pageIndex = 1;
            getClientList(null);
            ProgressDialogHelper.show(getActivity());
            isResume = false;
        }

    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.公海客户列表;// + Global.mUser.getUuid() + "&flag=1";
        demand.pageSize = 10;
        demand.sort = "";
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "customerType.dict_customer_type,passportTypeId.dict_user_passtype,source.dict_customer_source,industry.dict_customer_industry,advisor.base_staff";
        demand.src = url;
        demand.setFuzzySearch("crm_customer");
    }

    private void setOnEvent() {

//
//        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageIndex = 1;
//                getClientList();
//            }
//        });
//
//        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                getClientList();
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position > 0) {
//                    Intent intent = new Intent(context, CustomerDetailsActivity.class);
////                    intent.putExtra(CustomerDetailsActivity.EXTRA_CLIENT, adapter.getDataList().get(position - 1));
//                    intent.putExtra(CustomerDetailsActivity.EXTRA_CLIENT, adapter.getDataList().get(position));
//                    startActivity(intent);
                Client client = adapter.getDataList().get(position);
                Intent intent = new Intent(context, ChClientInfoActivity.class);
                PreferceManager.getInsance().saveValueBYkey("customer" + Global.mUser.getUuid(), "customer_public");
//                intent.putExtra("isReadOnly",false);
                intent.putExtra("isShowContactAndPhone", ispublicClientPhoneCanVisibile);
                intent.putExtra(ChClientInfoActivity.EXTRA_CLIENT_ID, client.getUuid());
                startActivity(intent);
//                }
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                pageIndex = 1;
//                lv.startRefresh();
                demand.fuzzySearch = str;
                demand.resetFuzzySearchField(true);
//                demand.searchField_string_name = "1|" + str;
                getClientList(null);
            }
        });
        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                pageIndex = 1;
                demand.sortField = "createTime desc";
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.客户列表;
                demand.customerType = "";
                demand.resetFuzzySearchField(false);
                adapter.clearData();
                getClientList(null);
            }

            @Override
            public void OnClick() {

            }
        });
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        searchView = v.findViewById(R.id.seach_button);
    }

    public void loadMoreData(RefreshLayout refreshLayout) {
        getClientList(refreshLayout);
    }

    public void refreshData(RefreshLayout refreshLayout) {
        pageIndex = 1;
        demand.resetFuzzySearchField(false);
        getClientList(refreshLayout);
    }

    /**
     * 获取公海电话是否可见
     */
    private void getPublicClientPhoneCanVisibile() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业CRM配置 + "?name=公海客户电话是否可见";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String result = JsonUtils.getStringValue(JsonUtils.pareseData(response), "value");
                    if (!TextUtils.isEmpty(result)) {
                        if ("是".equals(result)) {
                            ispublicClientPhoneCanVisibile = true;
                        } else {
                            ispublicClientPhoneCanVisibile = false;
                        }
                    } else {
                        ispublicClientPhoneCanVisibile = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ispublicClientPhoneCanVisibile = true;
                }
                getClientList(null);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ispublicClientPhoneCanVisibile = true;
                getClientList(null);
            }
        });
    }

    /**
     * 获取客户列表
     */
    private void getClientList(final RefreshLayout refreshLayout) {
        if (ispublicClientPhoneCanVisibile == null) {
            //先获取是否下属客户电话可见
            getPublicClientPhoneCanVisibile();
            return;
        }
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Client> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Client.class);

                    if (refreshLayout != null) {
                        refreshLayout.finishRefresh();
                    }
//                    lv.onRefreshComplete();
                    if (pageIndex == 1) {
                        adapter = getAdapter(list);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.addBottom(list, false);
                        if (list != null && list.size() == 0) {
                            if (refreshLayout != null) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                        if (refreshLayout != null) {
                            refreshLayout.finishLoadMore();
                        }
//                        lv.loadCompleted();
                    }
                    pageIndex += 1;
                    vp.resetHeight(fragmentID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private CommanAdapter<Client> getAdapter(List<Client> list) {
        return new CommanAdapter<Client>(list, context, R.layout.item_new_client) {
            @Override
            public void convert(int position, final Client item, BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhoto(R.id.circleImageView, item.getAdvisor());//理财师头像
                viewHolder.setTextValue(R.id.advisor_name, helper.getUserNameById(item.getAdvisor())); //理财师
                viewHolder.setTextValue(R.id.client_name, item.getName()); //客户名称
                if (!ispublicClientPhoneCanVisibile) {
                    viewHolder.setTextValue(R.id.contact_number, "******"); //客户手机号
                    viewHolder.setTextValue(R.id.contact_name, "***"); //客户联系人
                } else {
                    viewHolder.setTextValue(R.id.contact_name, TextUtils.isEmpty(item.getContact()) ? "无" : item.getContact()); //客户联系人
                    viewHolder.setTextValue(R.id.contact_number, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile()); //客户手机号
                }

                viewHolder.setTextValue(R.id.contact_location, TextUtils.isEmpty(item.getAddress()) ? "无" : item.getAddress()); //客户地址
                viewHolder.getView(R.id.imageView15).setVisibility(View.GONE);
                viewHolder.getView(R.id.imageView16).setVisibility(View.VISIBLE);
                if (StrUtils.pareseNull(item.getAdvisor()).equals(Global.mUser.getUuid())) {
                    viewHolder.getView(R.id.imageView14).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.imageView14).setVisibility(View.GONE);
                }

                viewHolder.getView(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //打电话
                        TextView tvNumber = viewHolder.getView(R.id.contact_number);
                        if ("******".equals(tvNumber.getText().toString())) {
                            ToastUtils.showShort("只能给自己的客户拨打电话");
                            return;
                        }
                        if (!TextUtils.isEmpty(item.getMobile())) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + item.getMobile()));
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "该客户没有录入联系手机号码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                viewHolder.getView(R.id.imageView14).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //新建跟进记录
                        Intent intent = new Intent(context,
                                AddRecordActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ContactNewActivity.EXTRA_CLIENT_NAME, item.getName());
                        bundle.putString(ContactNewActivity.EXTRA_CLIENT_ID, item.getUuid());

                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                viewHolder.getView(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ClientListActivity) getActivity()).openDrawerLayout(item);
                    }
                });


                //提取客户
                viewHolder.getView(R.id.imageView16).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog(getActivity());
                        dialog
                                .builder()
                                .setTitle("提示")
                                .setMsg("确定要提取客户吗")
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        isCustomerDistributable(item.getUuid());
                                    }
                                });
                        dialog.show();
                    }
                });
            }
        };
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            vp.resetHeight(fragmentID);
        } else {
            searchView.setOnCancleSearch(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setOnCancleSearch(false);
    }

    /**
     * 判断客户是否可以被分配
     *
     * @param cilentId
     */
    private void isCustomerDistributable(String cilentId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.判断公海客户是否可被分配;

        Map<String, String> map = new HashMap<>();
        map.put("customerNumberToAdd", "1");
        map.put("userId", Global.mUser.getUuid());

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                takePublicCustomer(cilentId);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ToastUtils.showShort(JsonUtils.pareseMessage(result));
            }
        });
    }

    private void takePublicCustomer(String clientId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.提取公海客户;

        Map<String, String> map = new HashMap<>();
        map.put("ids", clientId);

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort("提取成功");
                refreshData(null);
                EventBus.getDefault().postSticky("提取客户成功");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ToastUtils.showShort(JsonUtils.pareseMessage(result));
            }
        });
    }
}
