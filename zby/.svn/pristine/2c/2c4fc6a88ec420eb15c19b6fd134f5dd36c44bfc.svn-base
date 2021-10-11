package com.biaozhunyuan.tianyi.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.address.AddressPagerAdapter;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.chatLibary.chat.ChartIntentUtils;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.CustomViewPager;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;


/**
 * 选择联系人
 */

public class SelectedNotifierActivity extends BaseActivity {
    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private CustomViewPager viewPager;
    private AddressPagerAdapter pagerAdapter;
    private EditText searchView;
    private RelativeLayout rl_search_layout;
    private RelativeLayout rlroot;
    private ListView lv_search_list;
    private ImageView iv_search_no_result;
    private boolean isSingleSelect = false; //任务执行人单选 任务参与人多选
    private boolean isShowRecently = false; //是否展示最近选择人列表
    private Context context;
    private List<Fragment> fragmentList;
    private DictionaryHelper dictionaryHelper;
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private String[] tabTitles = new String[0]; //tab的标题
    private TextView count_user;
    private TextView tv_selected_text;
    private List<User> selectUsers_transfer = new ArrayList<>(); //传给所有选中员工的界面
    private SelectedRecentContactsFragment recentContactsFragment;
    private SelectedNotifierFragment selectedNotifierFragment;
    private SelectedMineDepartmentFragment selectedMineDepartmentFragment;
    private LinearLayout bottom_select;
    public List<User> selectUsers = new ArrayList<>(); //去重后的员工集合
    public List<User> unClickAbleUsers = new ArrayList<>(); //不可点击的员工集合
    private CommanAdapter<User> adapter;
    private List<User> users;
    private List<User> allUsers;
    private User other_user = new User();
    private String newseleteduser;
    private boolean isDepartMent = false;
    private String selectedAdvisorIds = ""; //已选择的员工id
    private String select_text = "已选择:"; //已选择
    private TextView tvSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_notifier);
        dictionaryHelper = new DictionaryHelper(context);
        initViews();
        getIntentData();
        context = SelectedNotifierActivity.this;
        initData();
        setOnEvent();
    }

    private void getIntentData() {
        isSingleSelect = getIntent().getBooleanExtra("isSingleSelect", true);
        isShowRecently = getIntent().getBooleanExtra("isShowRecently", true);

        unClickAbleUsers = (List<User>) getIntent().getSerializableExtra("unClickAbleUsers");
        if (unClickAbleUsers == null) {
            unClickAbleUsers = new ArrayList<>();
        }
        String title = getIntent().getStringExtra("title");
        if (isSingleSelect) {
            headerView.setRightTitleVisible(false);
        }
        if (!TextUtils.isEmpty(title)) {
            headerView.setTitle(title);
        }

        /**
         * 共享客户
         */
        if (getIntent().getStringExtra("selectedAdvisorIds") != null
                && getIntent().getStringExtra("selectedAdvisorNames") != null) {
            selectedAdvisorIds = getIntent().getStringExtra("selectedAdvisorIds");
            select_text = getIntent().getStringExtra("select_text");
            if (!TextUtils.isEmpty(selectedAdvisorIds)) {
                setShareClientData();
            }
        }

        if (isSingleSelect) { //判断单选还是多选
            bottom_select.setVisibility(View.GONE);
        } else {
            bottom_select.setVisibility(View.VISIBLE);
        }

        tv_selected_text.setText(select_text);
        if (unClickAbleUsers.size() > 0) {
            count_user.setText(unClickAbleUsers.size() + "位员工");
        }
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        recentContactsFragment = new SelectedRecentContactsFragment();
        selectedNotifierFragment = new SelectedNotifierFragment();
        selectedMineDepartmentFragment = new SelectedMineDepartmentFragment();
        if (isShowRecently) {
            tabTitles = Arrays.copyOf(tabTitles, tabTitles.length + 1);
            tabTitles[tabTitles.length - 1] = "最近";
            fragmentList.add(recentContactsFragment);
        }
        tabTitles = Arrays.copyOf(tabTitles, tabTitles.length + 1);
        tabTitles[tabTitles.length - 1] = "按员工";
        fragmentList.add(selectedMineDepartmentFragment);
        tabTitles = Arrays.copyOf(tabTitles, tabTitles.length + 1);
        tabTitles[tabTitles.length - 1] = "按部门";
        fragmentList.add(selectedNotifierFragment);
        pagerAdapter = new AddressPagerAdapter(fm, fragmentList);
        indicator.setVisibleTabCount(tabTitles.length);
        indicator.setTabItemTitles(tabTitles);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager, 0);
    }

    /**
     * 已共享客户选中
     */
    private void setShareClientData() {
        allUsers = new ArrayList<>();
        allUsers = dictionaryHelper.getAllStaff();
        for (User user : allUsers) {
            if (selectedAdvisorIds.contains(StrUtils.pareseNull(user.getUuid()))) {
                addSelected(user);
            }
        }
    }

    private void initViews() {

        headerView = (BoeryunHeaderView) findViewById(R.id.header_address_list);
        indicator = findViewById(R.id.incator_title_address_list);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager_address_list);
        searchView = findViewById(R.id.et_search);
        count_user = (TextView) findViewById(R.id.tv_count_select_user);
        tv_selected_text = (TextView) findViewById(R.id.tv_selected_text);
        tvSure = (TextView) findViewById(R.id.tv_sure_select_user);
        rl_search_layout = (RelativeLayout) findViewById(R.id.rl_search_layout_address);
        rlroot = (RelativeLayout) findViewById(R.id.rl_root);
        lv_search_list = (ListView) findViewById(R.id.lv_searched_user_address_list);
        iv_search_no_result = (ImageView) findViewById(R.id.iv_no_result_search_layout_address);
        bottom_select = findViewById(R.id.bottom_select);

    }

    private void setOnEvent() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {

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

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (TextUtils.isEmpty(str)) {
                    rl_search_layout.setVisibility(View.GONE);
                    rlroot.setVisibility(View.VISIBLE);
                } else {
                    rl_search_layout.setVisibility(View.VISIBLE);
                    rlroot.setVisibility(View.GONE);
                    getStaffByFilter(str);
                }
            }
        });

        lv_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                other_user = users.get(position);
                other_user.setSelected(!other_user.isSelected());
                if (other_user.isSelected()) {
                    addSelected(other_user);
                } else {
                    removeSelected(other_user);
                }
                adapter.notifyDataSetChanged();
                if (isSingleSelect) {
                    returnResult();
                }
                dictionaryHelper.insertLatest(other_user);
            }
        });

        count_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectUsers.size() != 0 || unClickAbleUsers.size() != 0) {
                    selectUsers_transfer.clear();
                    for (User u : selectUsers) {
                        u.setSelected(false);
                        selectUsers_transfer.add(u);
                    }

                    Intent intent = new Intent(SelectedNotifierActivity.this, SelectedUserListActivity.class);
                    Bundle bundle = new Bundle();
                    UserList userList = new UserList();
                    userList.setUsers(selectUsers_transfer);
                    bundle.putSerializable("userlist", userList);
                    bundle.putSerializable("unClickableList", (Serializable) unClickAbleUsers);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 200);
                } else {
                    Toast.makeText(SelectedNotifierActivity.this, "没有选择员工", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    isDepartMent = true;
                } else {
                    isDepartMent = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
                users = JsonUtils.ConvertJsonToList(response, User.class);

                if (users != null && users.size() > 0) {
                    iv_search_no_result.setVisibility(View.GONE);
                    for (User uu : users) {
                        for (User u : selectUsers) {
                            if (u.getUuid().equals(uu.getUuid())) {
                                uu.setSelected(true);
                            }
                        }
                    }
                    adapter = getUserAdapter(users);
                    lv_search_list.setAdapter(adapter);

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
        return new CommanAdapter<User>(list, context, R.layout.item_select_infrom) {

            @Override
            public void convert(int position, final User item, BoeryunViewHolder viewHolder) {
                //设置条目数据
                viewHolder.setUserPhotoById(R.id.head_item_workmate, dictionaryHelper.getUser(item.getUuid()));
                viewHolder.setTextValue(R.id.name, item.getName());
                ImageView iv_select = viewHolder.getView(R.id.choose_item_select_user);
//                //设置条目数据
//                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
//                viewHolder.setTextValue(R.id.position_item_workmate, item.getPost());
//                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
//                viewHolder.setUserPhoto(R.id.head_item_workmate, item.getAvatar());
//                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
//                if(Global.CURRENT_CROP_NAME.equals("天立化工")){
//                    if(!TextUtils.isEmpty(item.getPhoneExt())){
//                        viewHolder.setTextValue(R.id.tel_item_workmate,item.getPhoneExt());
//                    } else {
//                        viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
//                    }
//                } else {
//                    viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
//                }
//                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());
////               users.remove(mUser);//下面的列表去除当前用户，展示在最上面
//                viewHolder.getView(R.id.choose_item_select_user).setVisibility(View.VISIBLE);
                if (item.isSelected()) { //被选中
                    iv_select.setVisibility(View.VISIBLE);
                } else { //取消选中
                    iv_select.setVisibility(View.GONE);
                }

            }
        };
    }

    //从集合移除选中的员工
    public void removeSelected(User user) {
        User uu = null;
        for (User u : selectUsers) {
            if (u.getUuid().equals(user.getUuid())) {
                uu = u;
            }
        }
        if (uu != null) {
            selectUsers.remove(uu);
        }

        if (!isSingleSelect) {
            int count = selectUsers.size() + unClickAbleUsers.size();
            count_user.setText(count + "位员工");
            if (selectUsers.size() > 0) {
                tvSure.setBackgroundColor(getResources().getColor(R.color.hanyaRed));
            } else {
                tvSure.setBackgroundColor(getResources().getColor(R.color.show_bg));
            }
        }
    }

    //向集合添加员工
    public void addSelected(User user) {
        selectUsers = removeDuplicate(user);
        if (!isSingleSelect) {
            int count = selectUsers.size() + unClickAbleUsers.size();
            count_user.setText(count + "位员工");
            if (selectUsers.size() > 0) {
                tvSure.setBackgroundColor(getResources().getColor(R.color.hanyaRed));
            } else {
                tvSure.setBackgroundColor(getResources().getColor(R.color.show_bg));
            }
        }
    }


    //把list里的对象遍历一遍，用list.contain()，如果不存在就放入到另外一个list集合中
    public List<User> removeDuplicate(User user) {
        boolean isRePeat = false;
        for (User user1 : selectUsers) {
            if (user.getUuid().equals(user1.getUuid())) {
                isRePeat = true;
            }
        }
        if (!isRePeat) {
            selectUsers.add(user);
        }
        return selectUsers;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 111) {
            Bundle bunble = data.getExtras();
            newseleteduser = "newseleteduser";
            UserList userList1 = (UserList) bunble.getSerializable(newseleteduser);
            selectUsers.clear();
            selectUsers = userList1.getUsers();
//            //整理后的选择通知人
            if (selectUsers != null) {
                for (User user : selectUsers) {
                    user.setSelected(true);
                }
            }
            int count = selectUsers.size() + unClickAbleUsers.size();
            count_user.setText(count + "位员工");
            if (selectUsers.size() > 0) {
                tvSure.setBackgroundColor(getResources().getColor(R.color.hanyaRed));
            } else {
                tvSure.setBackgroundColor(getResources().getColor(R.color.show_bg));
            }
            this.setUserList(selectUsers);
            SelectedMineDepartmentFragment.isResume = true;
            SelectedMineDepartmentFragment.isResume = true;
            SelectedRecentContactsFragment.isResume = true;
            selectedMineDepartmentFragment.notifyList();
            selectedNotifierFragment.notifyList();
            recentContactsFragment.notifyList();

            if (users != null && users.size() > 0) {

                for (User uu : users) {
                    uu.setSelected(false);
                    for (User u : selectUsers) {
                        if (u.getUuid().equals(uu.getUuid())) {
                            uu.setSelected(true);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void setUserList(List<User> list) {
        this.selectUsers = list;
    }

    public List<User> getUserList() {
        return selectUsers;
    }

    public List<User> getUnClickAbleUsers() {
        return unClickAbleUsers;
    }

    /**
     * 返回数据
     */
    public void returnResult() {
        if (getIntent().getStringExtra("intent_tag") != null) {
            String participant = "";
            String staffIds = "";
            for (User user : selectUsers) {
                participant += user.getName() + ",";
                staffIds += user.getUuid() + ",";
            }
            if (participant.length() > 0) {
                staffIds = staffIds.substring(0, staffIds.length() - 1);
            }
            if (selectUsers != null && selectUsers.size() > 0) {
                ChartIntentUtils.startChatInfo(this, staffIds);
                finish();
            } else {
                showShortToast("您并没有选择联系人");
            }
        } else {
            if (selectUsers.size() > 0) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                UserList userList = new UserList();
                userList.setUsers(selectUsers);
                bundle.putSerializable(RESULT_SELECT_USER, userList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                showShortToast("没有选择员工");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isDepartMent) {
            selectedNotifierFragment.back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        searchView.setOnCancleSearch(false);
//    }

}
