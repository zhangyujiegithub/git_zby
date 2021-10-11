package com.biaozhunyuan.tianyi.address;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.chatLibary.chat.ChartIntentUtils;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.wechat.WeChatContactListActivity;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王安民 on 2017/9/1.
 * 通讯录页面——组织列表
 */

public class OrganizationFragment extends Fragment {

    private List<Organize> allDept = new ArrayList<Organize>();
    private List<Organize> myDept = new ArrayList<Organize>();
    private List<Organize> showDept = new ArrayList<Organize>();

    private List<Organization> myDeptShow = new ArrayList<>(); //我的部门下展示的数据
    private List<Organization> otherDeptShow = new ArrayList<>();  //别的部门下展示的数据

    private OrganizeAdapter myOrganizeAdapter;
    private OrganizeAdapter OtherOrganizeAdapter;

    private RecyclerView lv_myDept;
    private RecyclerView lv_allDept;

    private Dao<Latest, Integer> latestDao;

    private long lastClickTime;
    private boolean isShowWeRecord = false;
    private boolean isRoot = true;  //是否是根节点
    private boolean isUserList = false;  //是否员工列表

    private TextView other_dept;
    private LinearLayout ll_dept; //顶部部门名称，动态添加部门名称
    private TextView tv_myDept; // 我的组织按钮

    private List<TextView> textViewList = new ArrayList<>();
    private List<User> showUsers = new ArrayList<>();
    private ORMDataHelper dataHelper;
    private DictionaryHelper dictionaryHelper;
    private DictIosPickerBottomDialog dialog;
    private SharedPreferencesHelper preferencesHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organize2, null);
        initViews(view);
        getAllDept();
        setOnEvent();
        return view;
    }

    private void initViews(View view) {
        dataHelper = ORMDataHelper.getInstance(getActivity());
        preferencesHelper = new SharedPreferencesHelper(getActivity());
        dialog = new DictIosPickerBottomDialog(getActivity());
        latestDao = dataHelper.getLatestDao();
        dictionaryHelper = new DictionaryHelper(getActivity());
        lv_myDept = (RecyclerView) view.findViewById(R.id.lv_my_dept_organize);
        lv_allDept = (RecyclerView) view.findViewById(R.id.lv_all_dept_organize);

        other_dept = (TextView) view.findViewById(R.id.tv_other_dept_organize);
        ll_dept = (LinearLayout) view.findViewById(R.id.ll_dept_names_organize);
        tv_myDept = (TextView) view.findViewById(R.id.tv_back_first_organize);

        lv_myDept.setLayoutManager(new NoVerticalScrollManager(getActivity()));
        lv_allDept.setLayoutManager(new NoVerticalScrollManager(getActivity()));

        lv_myDept.setNestedScrollingEnabled(false);
        lv_allDept.setNestedScrollingEnabled(false);

    }

    public void setIsShowWeRecord(boolean isShowWeRecord) {
        this.isShowWeRecord = isShowWeRecord;
    }


    private void setOnEvent() {
        myOrganizeAdapter.setOnItemClickListener(new OrganizeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Organization ogz) {
                showBelowData(ogz);
            }
        });

        OtherOrganizeAdapter.setOnItemClickListener(new OrganizeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, Organization ogz) {
                showBelowData(ogz);
            }
        });

        /**
         * 回到根组织
         */
        tv_myDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRoot = true;
                isUserList = false;
                if (allDept != null) {
                    String id = getFirstDeptID(allDept);
                    showDept = getBelowDept(allDept, id);
                    //获取总部门下面的员工列表
                    List<User> users = dictionaryHelper.getStaffByDeptId(id);
                    //根据部门下的部门和员工列表 得到 总得数据列表
                    otherDeptShow = getOrganizations(showDept, users);
                    OtherOrganizeAdapter.replaceAll(otherDeptShow);
                }
                tv_myDept.setText("我的组织");
                for (int i = 0; i < textViewList.size(); i++) { //移除添加的textview
                    ll_dept.removeView(textViewList.get(i));
                }
                isRoot();
            }
        });
    }


    /**
     * 显示部们下面的数据
     *
     * @param ogz
     */
    private void showBelowData(Organization ogz) {
        if (ogz.getDataType() == 1) { //部门类型
            isRoot = false;
            for (TextView tv : textViewList) {
                tv.setTextColor(getResources().getColor(R.color.hanyaRed));
            }
            final TextView tv = new TextView(getActivity()); //动态添加部门名称
            tv.setTextColor(getResources().getColor(R.color.text_info));
            tv.setTextSize(14);
            tv.setText("-->" + ogz.getOrganize().getName());
            tv.setTag(ogz.getOrganize().getUuid());
            textViewList.add(tv);
            ll_dept.addView(tv);
            isRoot();

            //获取总部门下面的部门列表
            showDept = getBelowDept(allDept, ogz.getOrganize().getUuid());
            //获取总部门下面的员工列表
            List<User> users = dictionaryHelper.getStaffByDeptId(ogz.getOrganize().getUuid());
            //根据部门下的部门和员工列表 得到 总得数据列表
            otherDeptShow = getOrganizations(showDept, users);
            OtherOrganizeAdapter.replaceAll(otherDeptShow);


            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isUserList = false;
                    tv.setTextColor(getResources().getColor(R.color.text_info));
                    showDept = getBelowDept(allDept, (String) tv.getTag());
                    //获取总部门下面的员工列表
                    List<User> users = dictionaryHelper.getStaffByDeptId((String) tv.getTag());
                    //根据部门下的部门和员工列表 得到 总得数据列表
                    otherDeptShow = getOrganizations(showDept, users);
                    OtherOrganizeAdapter.replaceAll(otherDeptShow);

                    List<TextView> removeList = textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()); //移除点击的textview后面的view
                    for (int i = 0; i < removeList.size(); i++) {
                        ll_dept.removeView(removeList.get(i));
                    }
                    textViewList.removeAll(removeList);

                    textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()).clear();
                }
            });
        } else { //员工类型
            User user = ogz.getUser();
            if (isShowWeRecord) {
                Intent intent = new Intent(getActivity(), WeChatContactListActivity.class);
                intent.putExtra("UserId", user.getUuid());
                startActivity(intent);
            } else {
                ChartIntentUtils.startChatInfo(getActivity(), user.getUuid());
                dictionaryHelper.insertLatest(user);
            }
        }
    }


    /**
     * 获取根节点部门的id
     *
     * @return
     */
    private String getFirstDeptID(List<Organize> list) {
        if (list != null) {
            for (Organize organize : list) {
                if (organize.getParent().equals("0")) { //根部门的父节点的id为0
                    return organize.getUuid();
                }
            }
        }
        return "";
    }


    /**
     * 根据部门列表和传入部门id获取传入部门的子部门
     *
     * @param list
     * @param deptID
     * @return
     */
    private List<Organize> getBelowDept(List<Organize> list, String deptID) {
        showDept.clear();
        if (list != null) {
            for (Organize organize : list) {
                if (deptID.equals(organize.getParent())) { //根部门的父节点的id为0
                    showDept.add(organize);
                }
            }
        }
        return showDept;
    }


    /***
     * 获取部门列表
     */
    private void getAllDept() {

        Dao<Organize, Integer> deptDao = dataHelper.getDeptDao();
        try {
            allDept = deptDao.queryForAll();
            for (Organize organize : allDept) {
                if (organize.getUuid().equals(Global.mUser.getDepartmentId())) { //得到自己的部门
                    myDept.add(organize);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (myDept != null) {
            //获取我的部门展示的数据
            myDeptShow = getOrganizations(myDept, null);
            myOrganizeAdapter = new OrganizeAdapter(getActivity(), myDeptShow);
            myOrganizeAdapter.setIsShowWeChatRecord(isShowWeRecord);
            lv_myDept.setAdapter(myOrganizeAdapter);
        }

        if (allDept != null) {
            String id = getFirstDeptID(allDept);
            //获取总部门下面的部门列表
            showDept = getBelowDept(allDept, id);
            //获取总部门下面的员工列表
            List<User> users = dictionaryHelper.getStaffByDeptId(id);
            //根据部门下的部门和员工列表 得到 总得数据列表
            otherDeptShow = getOrganizations(showDept, users);
            OtherOrganizeAdapter = new OrganizeAdapter(getActivity(), otherDeptShow);
            OtherOrganizeAdapter.setIsShowWeChatRecord(isShowWeRecord);
            lv_allDept.setAdapter(OtherOrganizeAdapter);
        }

    }

    private List<Organization> getOrganizations(List<Organize> myDept, List<User> users) {
        List<Organization> organizations = new ArrayList<>();
        if (myDept != null) {
            for (Organize organize : myDept) {
                Organization organization = new Organization();
                organization.setDataType(1);
                organization.setOrganize(organize);
                organizations.add(organization);
            }
        }
        if (users != null) {
            for (User user : users) {
                Organization organization = new Organization();
                organization.setDataType(2);
                organization.setUser(user);
                organizations.add(organization);
            }
        }
        return organizations;
    }


    /**
     * 判断是否是根节点,根据根节点显示和隐藏布局
     */
    private void isRoot() {
        if (isRoot) {
            lv_myDept.setVisibility(View.VISIBLE);
            other_dept.setVisibility(View.VISIBLE);
            tv_myDept.setText("我的组织");
            tv_myDept.setTextColor(getResources().getColor(R.color.text_info));
            textViewList.clear();
        } else {
            lv_myDept.setVisibility(View.GONE);
            other_dept.setVisibility(View.GONE);
            tv_myDept.setText("全部组织");
            tv_myDept.setTextColor(getResources().getColor(R.color.hanyaRed));
        }
    }

    public void backLast() {
        if (textViewList.size() > 0) {
            isUserList = false;
            ll_dept.removeView(textViewList.get(textViewList.size() - 1));
            textViewList.remove(textViewList.size() - 1);
            if (textViewList.size() == 0) {
                isRoot = true;
                if (allDept != null) {
                    String id = getFirstDeptID(allDept);
                    showDept = getBelowDept(allDept, id);
                    //获取总部门下面的员工列表
                    List<User> users = dictionaryHelper.getStaffByDeptId(id);
                    //根据部门下的部门和员工列表 得到 总得数据列表
                    otherDeptShow = getOrganizations(showDept, users);
                    OtherOrganizeAdapter.replaceAll(otherDeptShow);
                }
                for (int i = 0; i < textViewList.size(); i++) { //移除添加的textview
                    ll_dept.removeView(textViewList.get(i));
                }
                isRoot();
            } else {
                textViewList.get(textViewList.size() - 1).setTextColor(getResources().getColor(R.color.text_info));
                showDept = getBelowDept(allDept, (String) textViewList.get(textViewList.size() - 1).getTag());
                //获取总部门下面的员工列表
                List<User> users = dictionaryHelper.getStaffByDeptId((String) textViewList.get(textViewList.size() - 1).getTag());
                //根据部门下的部门和员工列表 得到 总得数据列表
                otherDeptShow = getOrganizations(showDept, users);
                OtherOrganizeAdapter.replaceAll(otherDeptShow);
                isRoot();
            }
        } else if (textViewList.size() == 0) {
            if (System.currentTimeMillis() - lastClickTime > 2000) {
                Toast.makeText(getActivity(), "再按一次退出" + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                lastClickTime = System.currentTimeMillis();
            } else {
                getActivity().finish();
            }
        }
    }
}
