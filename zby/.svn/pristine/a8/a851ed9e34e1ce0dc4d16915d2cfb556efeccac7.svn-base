package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.IndicatorTabView;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

/**
 * 台账列表
 */

public class ClientStandingBookListFragment extends Fragment {

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private IndicatorTabView indicator;
    private BoeryunViewpager viewPager;
    private ImageView iv_add;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private String[] tableTitles = {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_standingbook,null);
        initView(v);
        getStadingBook();
        setOnTouch();
        return v;
    }

    private void setOnTouch() {
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StandingBookInfoActivity.class);
                intent.putExtra("tableName",tableTitles[viewPager.getCurrentItem()]);
                intent.putExtra("customerId",mClientId);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        indicator.setTabItemTitles(tableTitles);
        viewPager.setAdapter(new ClientDetailFragmentAdapter(getChildFragmentManager(), mFragmentList));
        indicator.setRelateViewPager(viewPager);
    }

    private void getStadingBook() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情动态台账列表 + mClientId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String data = JsonUtils.pareseData(response);
                String standingBook = data.substring(1, data.length() - 1);
                String[] split = standingBook.replace("\"","").split(",");
                int length = tableTitles.length;
                if(split.length > 0){ //如果台账类型大于1
                    indicator.setVisibility(View.VISIBLE);
                    for (int i =0;i<split.length;i++){
                        tableTitles = Arrays.copyOf(tableTitles, length += 1);
                        tableTitles[length - 1] = split[i];
                        mFragmentList.add(ClientStandingBookFragment.newInstance(mClientId,split[i]));
                    }
                } else { //如果只有一种台账 隐藏指示器
                    indicator.setVisibility(View.GONE);
                    tableTitles[length - 1] = split[0];
                    mFragmentList.add(ClientStandingBookFragment.newInstance(mClientId,split[0]));
                }
                initData();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
            }
        });

    }

    private void initView(View v) {
        CustomerDetailsActivity activity = (CustomerDetailsActivity) getActivity();
        indicator = v.findViewById(R.id.simpleindicatior);
        viewPager = v.findViewById(R.id.vp_client_inf_and_contact);
        iv_add = v.findViewById(R.id.iv_add_relate);

        //非自己的客户不可编辑新建保存台账
        if(activity.isMineCustomer()){
            iv_add.setVisibility(View.VISIBLE);
        }else {
            iv_add.setVisibility(View.GONE);
        }
    }


    public static ClientStandingBookListFragment newInstance(String Id) {
        ClientStandingBookListFragment fragment = new ClientStandingBookListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }
}
