package com.biaozhunyuan.tianyi.client;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
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

/**
 * 台账详情
 */
public class StandingBookInfoActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private IndicatorTabView simpleindicator;
    private BoeryunViewpager mViewPager;
    private String customerId = "";
    private String tableName = "";
    private String id = "0";
    public List<ChClientTabFragment> mFragments;
    private HashMap<String, ArrayList<表单字段>> mFormDataMap;
    private boolean isMineCustomer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standing_book_info);
        initView();
        getIntentData();
        getDynamicFields();
        setOnTouch();
    }

    private void setOnTouch() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                ArrayList<表单字段> list = getAllFormList();
                String result = ChClientBiz.checkNull(list);
                if (!TextUtils.isEmpty(result)) {
                    showShortToast(result);
                } else {
                    String idCardReg = ChClientBiz.checkCardRegEx(list);
                    if (!TextUtils.isEmpty(idCardReg)) {
                        // 先校验身份证号
                        showShortToast(idCardReg);
                    } else {
//                        result = ChClientBiz.checkRegEx(list);
                        if (!TextUtils.isEmpty(result)) {
                            showShortToast(result);
                        } else {
                            saveCustomerForm(list);
                        }
                    }
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
    }

    private ArrayList<表单字段> getAllFormList() {
        ArrayList<表单字段> list = new ArrayList<表单字段>();
        for (ChClientTabFragment fragment : mFragments) {
            list.addAll(fragment.getFormList());
        }
        return list;
    }

    private void saveCustomerForm(ArrayList<表单字段> formList) {
        ProgressDialogHelper.show(StandingBookInfoActivity.this);
        String url = Global.BASE_JAVA_URL + GlobalMethord.保存动态字段;
        StringRequest.postAsynNoMap(url, tableName, formList, new StringResponseCallBack() {
            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                String s = JsonUtils.pareseMessage(result);
                showShortToast(s);
                Logger.d(TAG + result + "");
            }

            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    String data = JsonUtils.getStringValue(response, "Data");
                    showShortToast("保存成功");
                    ClientStandingBookFragment.isResume = true;
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                Logger.e(TAG + ex + "");
                showShortToast("网络不给力，请稍后再试");
            }
        });
    }

    private void getDynamicFields() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.CRM动态字段;

        JSONObject object = new JSONObject();
        try {
            object.put("type", tableName);
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
                                form.TypeName = "未分类";
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

                                    if (!TextUtils.isEmpty(customerId)) {
                                        for (表单字段 field : formList) {
                                            if (field.Name.contains("customerId")) {
                                                field.Value = customerId;
                                            }
                                        }
                                    }

                                    ChClientTabFragment fragment = ChClientTabFragment
                                            .newInstance(formList, isMineCustomer,tableName,id);
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
                        mViewPager.setOffscreenPageLimit(1);
                        mViewPager.setEnabled(true);
                        simpleindicator.setRelateViewPager(mViewPager);

//                        for (ChClientTabFragment fragment : mFragments) {
//                            fragment.setFormListAddress(mLocation, mProvince, mCity);
//                        }

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

    private void getIntentData() {
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            tableName = getIntent().getStringExtra("tableName");
            isMineCustomer = getIntent().getBooleanExtra("isMineCustomer",true);
            headerView.setRightTitleVisible(isMineCustomer);
        } else {
            tableName = getIntent().getStringExtra("tableName");
            customerId = getIntent().getStringExtra("customerId");
        }
    }

    private void initView() {
        headerView = findViewById(R.id.boeryun_headerview);
        simpleindicator = findViewById(R.id.simpleindicator_ch_client_info);
        mViewPager = findViewById(R.id.vp_ch_client_info);

        mFragments = new ArrayList<ChClientTabFragment>();
        mFormDataMap = new HashMap<String, ArrayList<表单字段>>();
    }
}
