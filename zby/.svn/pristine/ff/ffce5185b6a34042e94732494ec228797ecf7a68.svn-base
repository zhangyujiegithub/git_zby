package com.biaozhunyuan.tianyi.client;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.contact.ContactNewActivity;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.task.TaskNewActivity;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 客户详情页面，带有联系记录、合同、工作计划等信息
 * <p/>
 * 2016/11/03 16:03
 */
public class ClientRelatedInfoActivity extends BaseActivity {

    public final static String EXTRA_CLIENT = "extra_client_ClientInfoAndContactActivity";

    private Context mContext;
    private Dynamic dynamic;

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();

    //顶部果冻按钮
    private List<TextView> mTvTabs = new ArrayList<TextView>();
    //选中顶部标题
    private TextView tvCheckedTab;

    private BoeryunHeaderView mHeader;
    private TextView tvContact;
    private TextView tvAddress;
    private TextView tvPhone;
    private ViewPager mVp;
    private LinearLayout llTab;
    private View view_back;
    private TextView tvDetails;
    private ImageView ivAdd;

    private TextView num_contract;
    private TextView num_money;
    private TextView num_visit;
    private TextView num_plan;
    public static boolean isFinish = false;

    private Client mClient = null;//,"#E9B57E","#7A91E9"
    private String[] colorArr = {"#26BCBA", "#E97E9F", "#B7C950", "#0BCA77","#E9B57E","#7A91E9" };
    private ImageView iv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_info_and_contact);

        mContext = ClientRelatedInfoActivity.this;
        findViews();
        extractIntent();
//        initNum();
        setOnEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFinish) {
            isFinish = false;
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 加载客户相关信息，联系记录列表，工作计划列表
     *
     * @param clientId
     */
    private void loadRelatedInfos(String clientId) {
//        mFragmentList=;
        mFragmentList.add(ClientContactListFragment.newInstance(clientId));
        mFragmentList.add(ClientTaskListFragment.newInstance(clientId));
        mFragmentList.add(ClientContractListFragment.newInstance(clientId,new Project()));
//        mFragmentList.add(new NullFragment());
//        mFragmentList.add(new NullFragment());
        mFragmentList.add(ClientAttachFragment.newInstance(clientId));
//        mFragmentList.add(ContractListFragment.newInstance(clientId));
//        mFragmentList.add(ReceipeiptListFragment.newInstance(clientId));
//        mFragmentList.add(RemindFragment.newInstance(clientId, 1));
        mVp.setAdapter(new ClientDetailFragmentAdapter(getSupportFragmentManager(), mFragmentList));
    }

//    private void initNum() {
//        String url = Global.BASE_URL + "Customer/GetCustomerSituationStatistics";
//        QmContract contract = new QmContract();
//        contract.PageSize = 20;
//        contract.Offset = 0;
//        contract.Filter = "客户=" + mClient.编号;
//
//        StringRequest.postAsyn(url, contract, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                response = JsonUtils.pareseData(response);
//                response = StrUtils.removeRex(response);
//                response = JsonUtils.pareseData(response);
//                response = StrUtils.removeRex(response);
//                LogUtils.i(TAG, response);
//                try {
//                    String numContract = JsonUtils.getStringValue(response, "合同");
//                    String numMoney = JsonUtils.getStringValue(response, "收款");
//                    String numVisit = JsonUtils.getStringValue(response, "拜访量");
//                    String numPlan = JsonUtils.getStringValue(response, "工作计划");
//
//                    num_contract.setText(numContract);
//                    num_money.setText(numMoney);
//                    num_visit.setText(numVisit);
//                    num_plan.setText(numPlan);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//
//    }

    private void findViews() {
        iv_phone = findViewById(R.id.iv_phone_ico_client_inf_and_contact);
        mHeader = (BoeryunHeaderView) findViewById(R.id.header_client_inf_and_contact);
        tvContact = (TextView) findViewById(R.id.tv_contact_client_inf_and_contact);
        tvAddress = (TextView) findViewById(R.id.tv_address_client_inf_and_contact);
        ivAdd = (ImageView) findViewById(R.id.iv_add_relate);
        view_back = findViewById(R.id.view_client_info);
        tvDetails = (TextView) findViewById(R.id.tv_client_info_details);
        tvPhone = (TextView) findViewById(R.id.tv_phone_client_inf_and_contact);
        mVp = (ViewPager) findViewById(R.id.vp_client_inf_and_contact);
        llTab = (LinearLayout) findViewById(R.id.ll_tab_client_related_info);
        num_contract = (TextView) findViewById(R.id.client_info_contract_num);
        num_money = (TextView) findViewById(R.id.client_info_shoukuan_num);
        num_visit = (TextView) findViewById(R.id.client_info_baifang_num);
        num_plan = (TextView) findViewById(R.id.client_info_plan_num);

        for (int i = 0; i < llTab.getChildCount(); i++) {
            mTvTabs.add((TextView) llTab.getChildAt(i));
            if (i == 0) {
                //记录默认选中Tab
                tvCheckedTab = (TextView) llTab.getChildAt(i);
                tvCheckedTab.setTextColor(getResources().getColor(R.color.white));
                tvCheckedTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        }


    }

    private void setOnEvent() {

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVp.getCurrentItem() == 0) {
                    Intent intent = new Intent(mContext,
                            ContactNewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactNewActivity.EXTRA_CLIENT_NAME, mClient.getName());
                    bundle.putString(ContactNewActivity.EXTRA_CLIENT_ID, mClient.getUuid());

                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mVp.getCurrentItem() == 1) {
                    Intent intent = new Intent(mContext, TaskNewActivity.class);
                    Bundle bundle = new Bundle();
                    Task task = new Task();
                    task.setExecutorIds(Global.mUser.getUuid());
                    task.setBeginTime(ViewHelper.getCurrentFullTime());
                    task.setCustomerId(mClient.getUuid());
                    task.setCustomerName(mClient.getName());
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if(mVp.getCurrentItem() == 5) {

                }
            }
        });

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 1 ) {
                    ivAdd.setVisibility(View.VISIBLE);
                } else {
                    ivAdd.setVisibility(View.GONE);
                }
                updateTabStatus(mTvTabs.get(position), position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClient.getMobile() != null) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mClient.getMobile()));
                    startActivity(intent);
                } else {
                    Toast.makeText(ClientRelatedInfoActivity.this, "没有记录该客户手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChClientInfoActivity.class);
                intent.putExtra(ChClientInfoActivity.EXTRA_CLIENT_ID, mClient.getUuid());
                startActivity(intent);
            }
        });

        mHeader.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {
                showShortToast("进入客户详情页面");
            }
        });

       /* mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < mTvTabs.size()) {
                    updateTabStatus(mTvTabs.get(position), position);
                    mVp.setCurrentItem(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        for (int i = 0; i < mTvTabs.size(); i++) {
            final int pos = i;
            final TextView tvTab = mTvTabs.get(i);
            final int finalI = i;
            mTvTabs.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTabStatus(tvTab, finalI);

//                    if (pos <= mVp.getChildCount()) {
                    mVp.setCurrentItem(pos);
//                    }
                }
            });
        }
    }

    /**
     * 更新Tab状态栏
     *
     * @param checkedTab
     */
    private void updateTabStatus(TextView checkedTab, int position) {
        if (tvCheckedTab != null) {
            //先把之前选中tab恢复默认状态
            tvCheckedTab.setTextColor(getResources().getColor(R.color.lightgray));
            tvCheckedTab.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        //根据点击的tab项更新下方横条的背景色
        view_back.setBackgroundColor(Color.parseColor(colorArr[position]));

        //记录最新选中Tab并高亮显示字体
        tvCheckedTab = checkedTab;
        tvCheckedTab.setTextColor(getResources().getColor(R.color.white));
        tvCheckedTab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    public static Intent newIntent(Context context, 客户 client) {
        Intent intent = new Intent(context, ClientRelatedInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CLIENT, client);
        intent.putExtras(bundle);
        return intent;
    }

    private void extractIntent() {
        Bundle bundle = getIntent().getExtras();
        dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
        if (dynamic != null) {
            getClientInfo();
        }
        if (bundle == null) {
            return;
        }

        Client client = (Client) bundle.getSerializable(EXTRA_CLIENT);
        if (client != null) {
            initClinetViews(client);
            loadRelatedInfos(client.getUuid());
            mClient = client;
        }
    }

    /**
     * 初始化顶部客户内容显示
     *
     * @param client 客户实体
     */
    private void initClinetViews(Client client) {
        mHeader.setTitle(StrUtils.pareseNull(client.getName()));
        tvAddress.setText(StrUtils.pareseNull(client.getAddress()));
//        if (!TextUtils.isEmpty(client.getAdvisor())) {
//            tvPhone.setText(client.getAdvisor().equals(Global.mUser.getUuid()) ? StrUtils.pareseNull(client.getMobile()) : "******");
        if (client != null) {
            tvPhone.setText(client.getMobile());
        } else {
            iv_phone.setVisibility(View.GONE);
            tvPhone.setText("");
        }

//        }
        tvContact.setText(StrUtils.pareseNull(client.getContact()));
    }


    private void getClientInfo() {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态详情 + "?dataId=" + dynamic.getDataId() + "&dataType=" + dynamic.getDataType();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Client> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Client.class);
                    if (list != null && list.size() > 0) {
                        mClient = list.get(0);
                    }
                    if (mClient != null) {
                        initClinetViews(mClient);
                        loadRelatedInfos(mClient.getUuid());
                    }
                } catch (Exception e) {
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
}
