package com.biaozhunyuan.tianyi.notice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.utils.PinYinUtil;
import com.biaozhunyuan.tianyi.view.IndexBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 我的部门
 */

public class SelectedMineDepartmentFragment extends LazyFragment {
    private ListView my_dept;
    private boolean isSingleSelect = false; //任务执行人单选 任务参与人多选
    private Context context;
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private DictionaryHelper dictionaryHelper;
    private User mUser = new User();
    private CommanAdapter<User> adapter;
    private List<User> selectUsers = new ArrayList<>(); //已选择的员工
    private List<User> currentUsers = new LinkedList<>(); //当前部门员工
    private List<User> unClickableUsers = new LinkedList<>();
    private SelectedNotifierActivity notifierActivity;
    public static boolean isResume = false;
    private String selectedAdvisorIds = ""; //已选择的员工id
    private String selectedAdvisorNames = ""; //已选择的员工名称
    private IndexBar indexBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selectedminedepart, null);
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        initView(v);
        getIntentData();
        setOnTouch();
        return v;
    }

    /**
     * 共享客户
     */
    private void getIntentData() {
        if (getActivity().getIntent().getStringExtra("selectedAdvisorIds") != null
                && getActivity().getIntent().getStringExtra("selectedAdvisorNames") != null) {
            selectedAdvisorIds = getActivity().getIntent().getStringExtra("selectedAdvisorIds");
            selectedAdvisorNames = getActivity().getIntent().getStringExtra("selectedAdvisorNames");
        }
        unClickableUsers = notifierActivity.getUnClickAbleUsers();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && currentUsers.size() > 0) {
        } else {
            getShowStaff();
        }
        if (isResume) {
            notifyList();
        }
    }

    public void notifyList() {
        if (notifierActivity == null) {
            return;
        }
        selectUsers = notifierActivity.getUserList();
        for (User user : currentUsers) {
            if (selectUsers.size() > 0) {
                for (User u : selectUsers) {
                    if (user.getUuid().equals(u.getUuid())) {
                        user.setSelected(true);
                    }
                }
            } else {
                user.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setOnTouch() {
        indexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Toast.makeText(context,s.charAt(0) + "",Toast.LENGTH_SHORT).show();
                //该字母首次出现的位置
                int position = getPositionForSection(s.toLowerCase().charAt(0), currentUsers);
                if (position >= 0) {
                    my_dept.setSelection(position);
                }
            }
        });
        my_dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User other_user = currentUsers.get(position);
                if (!other_user.isClickable()) {
                    return;
                }
                other_user.setSelected(!other_user.isSelected());
                if (other_user.isSelected()) {
                    notifierActivity.addSelected(other_user);
                } else {

                    notifierActivity.removeSelected(other_user);
                }
                adapter.notifyDataSetChanged();

                if (isSingleSelect) {
                    notifierActivity.returnResult();
                }
                dictionaryHelper.insertLatest(other_user);
            }
        });
    }

    private void initView(View v) {
        isSingleSelect = getActivity().getIntent().getBooleanExtra("isSingleSelect", true);
        my_dept = v.findViewById(R.id.lv_my_dept_organize);
        indexBar = v.findViewById(R.id.index_bar_inner_communicate_list);
    }

    private void getShowStaff() {
        currentUsers = dictionaryHelper.getAllStaff();
        Collections.sort(currentUsers, new PinyinComparator());
        if (currentUsers != null) {
            //客户已共享员工选中
            if (!TextUtils.isEmpty(selectedAdvisorIds)) {
                for (User lt : currentUsers) {
                    if (selectedAdvisorIds.contains(lt.getUuid())) {
                        lt.setSelected(true);
                    }
                    for (User unClickableUser : unClickableUsers) {
                        if (lt.getUuid().equals(unClickableUser.getUuid())) {
                            lt.setClickable(false);
                        }
                    }
                }
            }
            for (User lt : currentUsers) {
                for (User unClickableUser : unClickableUsers) {
                    if (lt.getUuid().equals(unClickableUser.getUuid())) {
                        lt.setClickable(false);
                    }
                }
            }
            for (User user : currentUsers) {
                if (user.getUuid().equals(Global.mUser.getUuid())) {
                    mUser = user;
                    break;
                }
            }
        }
        adapter = getUserAdapter(currentUsers);
        my_dept.setAdapter(adapter);
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
            public void convert(int position, User item, BoeryunViewHolder viewHolder) {
                //设置条目数据
                viewHolder.setUserPhotoById(R.id.head_item_workmate,item);
                viewHolder.setTextValue(R.id.name, item.getName());
                ImageView ivSelect = viewHolder.getView(R.id.choose_item_select_user);
                if (!item.isClickable()) {
                    ivSelect.setVisibility(View.VISIBLE);
                    ivSelect.setImageResource(R.drawable.ic_select_gray);
                } else {
                    if (item.isSelected()) {
                        ivSelect.setVisibility(View.VISIBLE);
                        ivSelect.setImageResource(R.drawable.ic_select);
                    } else {
                        ivSelect.setVisibility(View.INVISIBLE);
                    }
                }

                //根据position获取分类的首字母的char ascii值
                int section = getSectionForPosition(position, list);
                String name = PinYinUtil.toPinyin(item.getName());
                if (TextUtils.isEmpty(name)) {
                    name = "#";
                } else {
                    if (!(name.charAt(0) >= 'a' && name.charAt(0) <= 'z')) {//第一个字符不在a-z之间
                        name = "#";
                    }
                }
                TextView tv_first = viewHolder.getView(R.id.tv_add_client_contract_sort);
                //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section, list)) {
                    tv_first.setVisibility(View.VISIBLE);
                    tv_first.setText(("" + name.charAt(0)));
                } else {
                    tv_first.setVisibility(View.GONE);
                }
            }
        };
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position, List<User> list) {
        String name = PinYinUtil.toPinyin(list.get(position).getName());
        if (TextUtils.isEmpty(name)) {
            name = "#";
        } else {
            if (!(name.charAt(0) >= 'a' && name.charAt(0) <= 'z')) {//第一个字符不在a-z之间
                name = "#";
            }
        }
        return name.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section, List<User> list) {
        for (int i = 0; i < list.size(); i++) {
            String sortStr = PinYinUtil.toPinyin(list.get(i).getName());
            if (TextUtils.isEmpty(sortStr)) {
                sortStr = "#";
            } else {
                if (!(sortStr.charAt(0) >= 'a' && sortStr.charAt(0) <= 'z')) {//第一个字符不在a-z之间
                    sortStr = "#";
                }
            }
            char firstChar = sortStr.charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        notifierActivity = (SelectedNotifierActivity) getActivity();
    }


    public void setSelectedUser() {
        for (User user : currentUsers) {
            for (User u : selectUsers) {
                if (user.getUuid().equals(u.getUuid())) {
                    user.setSelected(true);
                }
            }
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            if (notifierActivity != null) {
                selectUsers = notifierActivity.getUserList();
                if (selectUsers.size() > 0) {
                    setSelectedUser();
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            for (User u : currentUsers) {
                u.setSelected(false);
            }
        }
    }

    /**
     * 按照名字将联系人排序
     */

    public class PinyinComparator implements Comparator<User> {

        public int compare(User o1, User o2) {

            String name1 = PinYinUtil.toPinyin(o1.getName());
            String name2 = PinYinUtil.toPinyin(o2.getName());
            if (TextUtils.isEmpty(name1)) {
                name1 = "#";
            }
            if (TextUtils.isEmpty(name2)) {
                name2 = "#";
            }
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if ((String.valueOf(name1.charAt(0))).equals("#")) {
                return 0;
            } else if (String.valueOf(name2.charAt(0)).equals("#")) {
                return 1;
            } else {
                return ("" + name1.charAt(0)).compareTo(("" + name2.charAt(0)));
            }
        }
    }

}
