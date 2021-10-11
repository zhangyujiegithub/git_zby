package com.biaozhunyuan.tianyi.address;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
 * Created by wangAnMin on 2018/5/24.
 */

public class AddressListFragment extends Fragment {
    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private CustomViewPager viewPager;
    private AddressPagerAdapter pagerAdapter;
    private BoeryunSearchView searchView;
    private RelativeLayout rl_search_layout;
    private ListView lv_search_list;
    private ImageView iv_search_no_result;
    private SharedPreferencesHelper preferencesHelper;


    private Context context;
    private List<android.support.v4.app.Fragment> fragmentList;
    private DictIosPickerBottomDialog dialog;
    private DictionaryHelper dictionaryHelper;

    private String[] tabTitles = new String[]{"最近", "同事", "组织"}; //tab的标题
    public boolean isDepart = false;//是否是部门列表
    private OrganizationFragment organizeFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address_list, null);
        context = getActivity();
        preferencesHelper = new SharedPreferencesHelper(context);
        initViews(view);
        initData();
        setOnEvent();
        return view;
    }


    private void initData() {

        fragmentList = new ArrayList<>();
        FragmentManager fm = getFragmentManager();
        dialog = new DictIosPickerBottomDialog(context);
        organizeFragment = new OrganizationFragment();
        dictionaryHelper = new DictionaryHelper(context);
        fragmentList.add(new RecentContactsFragment());
        fragmentList.add(new WorkMateFragment());
        fragmentList.add(organizeFragment);
        pagerAdapter = new AddressPagerAdapter(fm, fragmentList);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager, 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    isDepart = true;
                } else {
                    isDepart = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initViews(View view) {
        headerView = (BoeryunHeaderView) view.findViewById(R.id.header_address_list);
        indicator = (SimpleIndicator) view.findViewById(R.id.incator_title_address_list);
        viewPager = (CustomViewPager) view.findViewById(R.id.viewPager_address_list);
        searchView = (BoeryunSearchView) view.findViewById(R.id.search_view_address_list);
        searchView.setHint("按姓名搜索");

        rl_search_layout = (RelativeLayout) view.findViewById(R.id.rl_search_layout_address);
        lv_search_list = (ListView) view.findViewById(R.id.lv_searched_user_address_list);
        iv_search_no_result = (ImageView) view.findViewById(R.id.iv_no_result_search_layout_address);
        headerView.setBackIconVisible(false);
        indicator.setTabItemTitles(tabTitles);
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {

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
                ChartIntentUtils.startChatInfo(getActivity(), user.getUuid());
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
                final List<String> showList = new ArrayList<>();
                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                viewHolder.setTextValue(R.id.position_item_workmate, item.getPost());
                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
                viewHolder.setUserPhoto(R.id.head_item_workmate, item.getUuid());
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                if (Global.CURRENT_CROP_NAME.equals("天立化工")) {
                    if (!TextUtils.isEmpty(item.getPhoneExt())) {
                        viewHolder.setTextValue(R.id.tel_item_workmate, item.getPhoneExt());
                        if (!TextUtils.isEmpty(item.getPhoneExt())) {
                            showList.add(item.getPhoneExt());
                        }
                    } else {
                        if (!TextUtils.isEmpty(item.getMobile())) {
                            showList.add(item.getMobile());
                        }
                        viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                    }
                } else {
                    viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                    if (!TextUtils.isEmpty(item.getPhoneExt())) {
                        showList.add(item.getPhoneExt());
                    }
                    if (!TextUtils.isEmpty(item.getMobile())) {
                        showList.add(item.getMobile());
                    }
                }
                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());


                ImageView iv_call = viewHolder.getView(R.id.iv_call_phone_workmate);
                ImageView iv_message = viewHolder.getView(R.id.iv_send_message);


                iv_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(item.getMobile())) {
                            Toast.makeText(getContext(), "手机号为空", Toast.LENGTH_SHORT).show();
                        } else {
                            doSendSMSTo(item.getMobile(), "");
                        }
                    }
                });



                //弹出打电话的弹出框
                iv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showList.size() == 0) {
                            Toast.makeText(context, "没有联系方式", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.setTitle("联系" + item.getName());
                            dialog.show(showList);

                            dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                @Override
                                public void onSelected(int index) {
                                    if (index != 0) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_DIAL);
//                                    String num = "";
//                                    if (!TextUtils.isEmpty(item.getTelephone())) { //座机不为空，证明肯定有座机号
//                                        if (index == 1) {
//                                            num = item.getTelephone();
//                                        }
//                                        if (!TextUtils.isEmpty(item.getMobile())) {
//                                            if (index == 2) {
//                                                num = item.getMobile();
//                                            }
//                                        }
//                                    } else {
//                                        if (index == 1) {
//                                            num = item.getMobile();
//                                        }
//                                    }
                                        intent.setData(Uri.parse("tel:" + showList.get(index - 1)));
                                        dictionaryHelper.insertLatest(item);
                                        //添加到最近联系人
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });


            }
        };
    }

    public void backLast() {
        if (organizeFragment != null) {
            organizeFragment.backLast();
        }
    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
        }
    }

//    @Override
//    protected void onFragmentVisibleChange(boolean isVisible) {
//        if(isVisible){
//            ImmersionBar.with(getActivity()).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).init();
//        }else{
//
//        }
//    }
}
