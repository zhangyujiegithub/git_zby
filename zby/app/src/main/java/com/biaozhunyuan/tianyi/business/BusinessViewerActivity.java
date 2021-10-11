package com.biaozhunyuan.tianyi.business;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.client.ChClientTabFragment;
import com.biaozhunyuan.tianyi.client.动态表单ViewModel;
import com.biaozhunyuan.tianyi.client.动态表单分类;
import com.biaozhunyuan.tianyi.client.客户;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.IndicatorTabView;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class BusinessViewerActivity extends BaseActivity {

    private HashMap<String, ArrayList<表单字段>> mFormDataMap;
    private List<ChClientTabFragment> mFragments;
    private IndicatorTabView simpleindicator;
    private Context mContext;
    private 客户 mClient;
    private String mClientId;
    private LinearLayout llContactList;
    private LinearLayout llAddContact;
    private BoeryunViewpager mViewPager;
    private BoeryunHeaderView headerview;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_viewer);
        initView();
        initData();
    }

    private void initData() {
        mContext = this;
        mFragments = new ArrayList<ChClientTabFragment>();
        mFormDataMap = new HashMap<String, ArrayList<表单字段>>();
        if(getIntent().getExtras()!=null){
            Bundle extras = getIntent().getExtras();
            code = extras.getString("code");
            getBusinessId(code);
        }
    }

    private void getBusinessId(String code) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取商机编号 + code;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String uuid = JsonUtils.getStringValue(JsonUtils.pareseData(response), "uuid");
                    getList(uuid);
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

    private void getList(String id) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.CRM动态字段;

        JSONObject object = new JSONObject();
        try {
            object.put("type", "crm_sale_chance");
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String result) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String response) {
                ProgressDialogHelper.dismiss();
                动态表单ViewModel formViewModel = null;
                try {
                    formViewModel = JsonUtils.jsonToEntity(
                            response, 动态表单ViewModel.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (formViewModel != null) {
                    List<String> titles = new ArrayList<String>();
                    for (动态表单分类 categray : formViewModel.动态表单分类s) {
                        titles.add(categray.分类名称);
                        Logger.i("CategrayTag::" + categray.分类名称 + "");
                        mFormDataMap.put(categray.分类名称, new ArrayList<表单字段>());
                    }
                    if (formViewModel.表单字段s != null
                            && formViewModel.表单字段s.size() > 0) {
                        // 根据分类Tab
                        for (表单字段 form : formViewModel.表单字段s) {
                            if (form.Name.equals("uuid")) {
                                form.TypeName = "商机信息";
                                mFormDataMap.put(form.TypeName,
                                        new ArrayList<表单字段>());
                                mFormDataMap.get(form.TypeName).add(form);
                            }
                        }
                    }

                    if (formViewModel.表单字段s != null
                            && formViewModel.表单字段s.size() > 0) {
                        // 根据分类Tab
                        for (表单字段 form : formViewModel.表单字段s) {
                            if (!mFormDataMap.containsKey(form.TypeName)) {
                                mFormDataMap.put(form.TypeName,
                                        new ArrayList<表单字段>());
                            }
//							if(!"编号".equals(form.Name))
//							{   //完成分类，编号字段不显示
//								mFormDataMap.get(form.TypeName).add(form);
//							}
                            if (!form.Name.equals("uuid")) {
                                mFormDataMap.get(form.TypeName).add(form);
                            }
                        }

                        表单字段 tempIdForm = null;
                        String firstKey = "";
                        Iterator<Map.Entry<String, ArrayList<表单字段>>> item = mFormDataMap.entrySet().iterator();
                        while (item.hasNext()) {
                            Map.Entry<String, ArrayList<表单字段>> entry = item.next();
                            List<表单字段> formList = entry.getValue();
                            String formName = entry.getKey();

                            //特殊处理，当分类下没有内容 或只有一个编号字段则去除tab
                            if (formList == null || formList.size() == 0) {
//                                ||
                                titles.remove(formName);
                            } else if ((formList.size() == 1 && "编号".equals(formList.get(0).Name))) {
                                titles.remove(formName);
                                tempIdForm = formList.get(0);
                            }

                            if (TextUtils.isEmpty(firstKey)) {
                                firstKey = formName;
                            }
                        }


                        if (tempIdForm != null) {
                            //特殊处理id字段到第一分类
                            mFormDataMap.get(firstKey).add(tempIdForm);
                        }
                        for (int i = 0; i < titles.size(); i++) {
                            String s = titles.get(i);
                            if (TextUtils.isEmpty(s)) {
//                                titles.set(titles.indexOf(s), "其他");
                                titles.remove(i);
                                titles.add("其他");
                            }
                        }

//                        mIndicator.setTabItemTitles(titles);

                        for (String title : titles) {
                            if (title.equals("其他")) {
                                title = "";
                            }
                            Iterator<Map.Entry<String, ArrayList<表单字段>>> it = mFormDataMap
                                    .entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, ArrayList<表单字段>> entry = it
                                        .next();
                                String keyStr = entry.getKey();
                                Logger.i("EQU" + keyStr + "---" + title);
                                if (keyStr.equals(title)) {
                                    ArrayList<表单字段> formList = entry.getValue();

//                                    if (mClient != null && mClient.编号 == 0) {
//                                        for (表单字段 field : formList) {
//                                            if ("电话".equals(field.Name)) {
//                                                field.Value = mClient.电话;
//                                            }
//
//                                            if ("手机".equals(field.Name)) {
//                                                field.Value = mClient.手机;
//                                            }
//                                        }
//                                    }


                                    ChClientTabFragment fragment = ChClientTabFragment
                                            .newInstance(formList,false,"crm_sale_chance",id);
                                    mFragments.add(fragment);
                                }
                            }
                        }

                        for (int i = 0; i < titles.size(); i++) {
                            String s = titles.get(i);
                            if ("其他".equals(s)) {
//                                titles.set(titles.indexOf(s), "其他");
                                titles.remove(s);
                            }
                        }
//                        mIndicator.setTabItemTitles(titles);
                        simpleindicator.setTabItemTitles(titles);
                        if (titles == null || titles.size() == 0 || titles.size() == 1) {
//                            mIndicator.setVisibility(View.GONE);
                            simpleindicator.setVisibility(View.GONE);
                        }
                        mViewPager.setOffscreenPageLimit(1);
                        mViewPager.setAdapter(new FragmentPagerAdapter(
                                getSupportFragmentManager()) {

                            @Override
                            public int getCount() {
                                // TODO Auto-generated method stub
                                return mFragments.size();
                            }

                            @Override
                            public Fragment getItem(int position) {
                                return mFragments.get(position);
                            }
                        });
                        mViewPager.setEnabled(true);
                        simpleindicator.setRelateViewPager(mViewPager);

                    } else {
//                        mIndicator.setVisibility(View.GONE);
                        simpleindicator.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }
        });


    }


    private void initView() {
        headerview = findViewById(R.id.headerview);
        simpleindicator = findViewById(R.id.simpleindicator_ch_client_info);
        mViewPager =  findViewById(R.id.vp_ch_client_info);

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

    }


}
