package com.biaozhunyuan.tianyi.address;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.chatLibary.chat.ChartIntentUtils;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.CustomViewPager;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/28.
 * 员工通讯录列表
 */

public class AddressListActivity extends FragmentActivity {

    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private CustomViewPager viewPager;
    private AddressPagerAdapter pagerAdapter;
    private BoeryunSearchView searchView;
    private RelativeLayout rl_search_layout;
    private ListView lv_search_list;
    private ImageView iv_search_no_result;


    private Context context;
    private List<Fragment> fragmentList;
    private DictIosPickerBottomDialog dialog;
    private DictionaryHelper dictionaryHelper;
    private SharedPreferencesHelper preferencesHelper;
    private String[] tabTitles = new String[]{"最近", "组织", "同事"}; //tab的标题


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list1);
        context = AddressListActivity.this;
        preferencesHelper = new SharedPreferencesHelper(this);
        initViews();
        initData();
        setOnEvent();
    }

    private void initData() {

        fragmentList = new ArrayList<Fragment>();
        FragmentManager fm = getSupportFragmentManager();
        dialog = new DictIosPickerBottomDialog(AddressListActivity.this);
        dictionaryHelper = new DictionaryHelper(context);
        fragmentList.add(new RecentContactsFragment());
        fragmentList.add(new OrganizeFragment());
        fragmentList.add(new WorkMateFragment());
        pagerAdapter = new AddressPagerAdapter(fm, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager,0);

    }


    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_address_list);
        indicator =  findViewById(R.id.incator_title_address_list);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager_address_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_view_address_list);
        searchView.setHint("按姓名搜索");

        rl_search_layout = (RelativeLayout) findViewById(R.id.rl_search_layout_address);
        lv_search_list = (ListView) findViewById(R.id.lv_searched_user_address_list);
        iv_search_no_result = (ImageView) findViewById(R.id.iv_no_result_search_layout_address);


        indicator.setTabItemTitles(tabTitles);
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

        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                rl_search_layout.setVisibility(View.GONE);
            }

            @Override
            public void OnClick() {
                rl_search_layout.setVisibility(View.VISIBLE);
            }
        });

        //输入框搜索内容
        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (TextUtils.isEmpty(str)) {
                    rl_search_layout.setVisibility(View.VISIBLE);
                    iv_search_no_result.setVisibility(View.VISIBLE);
                } else {
                    getStaffByFilter(str);
                }
            }
        });


        lv_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) lv_search_list.getItemAtPosition(position);
                ChartIntentUtils.startChatInfo(context, user.getUuid());
                dictionaryHelper.insertLatest(user);
            }
        });


    }


    /**
     * 搜索员工
     *
     * @param filter
     */
    private void getStaffByFilter(String filter) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.员工搜索 + filter;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

                List<User> users = JsonUtils.ConvertJsonToList(response, User.class);

                if (users != null && users.size() > 0) {
                    iv_search_no_result.setVisibility(View.GONE);
                    lv_search_list.setAdapter(getUserAdapter(users));
                } else {
                    iv_search_no_result.setVisibility(View.VISIBLE);
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


    /**
     * 获取员工列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<User> getUserAdapter(List<User> list) {
        return new CommanAdapter<User>(list, context, R.layout.item_workmate_list) {

            @Override
            public void convert(int position, final User item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                viewHolder.setTextValue(R.id.position_item_workmate, item.getPost());
                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
                viewHolder.setUserPhoto(R.id.head_item_workmate, item.getUuid());
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());


                ImageView iv_call = viewHolder.getView(R.id.iv_call_phone_workmate);

                final List<String> showList = new ArrayList<>();

                if (!TextUtils.isEmpty(item.getTelephone())) {
                    showList.add(item.getTelephone());
                }
                if (!TextUtils.isEmpty(item.getMobile())) {
                    showList.add(item.getMobile());
                }


                //弹出打电话的弹出框
                iv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setTitle("联系" + item.getName());
                        dialog.show(showList);

                        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {
                                if (index != 0) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_DIAL);
                                    String num = "";
                                    if (!TextUtils.isEmpty(item.getTelephone())) { //座机不为空，证明肯定有座机号
                                        if (index == 1) {
                                            num = item.getTelephone();
                                        }
                                        if (!TextUtils.isEmpty(item.getMobile())) {
                                            if (index == 2) {
                                                num = item.getMobile();
                                            }
                                        }
                                    } else {
                                        if (index == 1) {
                                            num = item.getMobile();
                                        }
                                    }
                                    intent.setData(Uri.parse("tel:" + num));
                                    dictionaryHelper.insertLatest(item);
                                    //添加到最近联系人
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });


            }
        };
    }

}
