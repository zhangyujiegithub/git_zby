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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 全部部门
 */
public class SelectedNotifierFragmentNew extends LazyFragment {
    private List<Organize> allDept = new LinkedList<Organize>();
    private List<Organize> myDept = new LinkedList<Organize>();
    private List<Organize> showDept = new LinkedList<Organize>();
    private List<User> currentUsers = new ArrayList<>(); //当前部门员工
    private NoScrollListView lv_myDept;
    private NoScrollListView lv_allDept;
    private boolean isRoot = true;  //是否是根节点
    private boolean isUserList = false;  //是否员工列表
    private List<TextView> textViewList = new ArrayList<>();
    private TextView other_dept;
    private LinearLayout ll_dept; //顶部部门名称，动态添加部门名称
    private TextView tv_myDept; // 我的组织按钮
    private ORMDataHelper dataHelper;
    private Dao<Latest, Integer> latestDao;
    private DictionaryHelper dictionaryHelper;
    private List<User> users;
    private List<User> selectUsers = new ArrayList<User>(); //已选择的员工
    private User mUser = new User();
    private CommanAdapter<User> adapter;
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private Context context;
    private User other_user = new User();
    private boolean isSingleSelect = false; //任务执行人单选 任务参与人多选
    private SelectedNotifierActivity notifierActivity;
    public static boolean isResume = false;
    private String selectedAdvisorIds = ""; //已选择的员工id
    private String selectedAdvisorNames = ""; //已选择的员工名称

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_infrom_new, null);
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        initViews(v);
        getIntentData();
        getShowStaff();
        getAllDept();
        return v;
    }
    /**
     * 共享客户
     */
    private void getIntentData() {
        if(getActivity().getIntent().getStringExtra("selectedAdvisorIds")!=null
                &&getActivity().getIntent().getStringExtra("selectedAdvisorNames")!=null){
            selectedAdvisorIds = getActivity().getIntent().getStringExtra("selectedAdvisorIds");
            selectedAdvisorNames = getActivity().getIntent().getStringExtra("selectedAdvisorNames");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (notifierActivity == null) {
            notifierActivity = (SelectedNotifierActivity) getActivity();
        }

        if (isResume) {
            selectUsers = notifierActivity.getUserList();
            for (User user : currentUsers) {
                if(selectUsers.size()>0){
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
    }

    private void initViews(View v) {

        isSingleSelect = getActivity().getIntent().getBooleanExtra("isSingleSelect", true);
        dataHelper = ORMDataHelper.getInstance(getActivity());
        latestDao = dataHelper.getLatestDao();
        dictionaryHelper = new DictionaryHelper(getActivity());
        lv_myDept = (NoScrollListView) v.findViewById(R.id.lv_my_dept_organize);
        lv_allDept = (NoScrollListView) v.findViewById(R.id.lv_all_dept_organize);
        other_dept = (TextView) v.findViewById(R.id.tv_other_dept_organize);
        ll_dept = (LinearLayout) v.findViewById(R.id.ll_dept_names_organize);
        tv_myDept = (TextView) v.findViewById(R.id.tv_back_first_organize);

    }

    private void getShowStaff() {
//        users = dictionaryHelper.getStaffByDeptId(Global.mUser.getDepartmentId());
        users = dictionaryHelper.getAllStaff();
        //客户已共享员工选中
        if(!TextUtils.isEmpty(selectedAdvisorIds)){
            for(User lt : users){
                if(selectedAdvisorIds.contains(lt.getUuid())){
                    lt.setSelected(true);
                }
            }
        }
        if (users != null) {
            for (User user : users) {
                if (user.getUuid().equals(Global.mUser.getUuid())) {
                    mUser = user;
                    break;
                }
            }
        }
        setSelectedUser();
        adapter = getUserAdapter(users);
        lv_allDept.setAdapter(adapter);
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
                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                viewHolder.setTextValue(R.id.position_item_workmate, item.getPost());
                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
                viewHolder.setUserPhoto(R.id.head_item_workmate, item.getUuid());
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                if(Global.CURRENT_CROP_NAME.equals("天立化工")){
                    if(!TextUtils.isEmpty(item.getPhoneExt())){
                        viewHolder.setTextValue(R.id.tel_item_workmate,item.getPhoneExt());
                    } else {
                        viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                    }
                } else {
                    viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                }
                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());
                ImageView iv_select = viewHolder.getView(R.id.choose_item_select_user);
                final List<String> showList = new ArrayList<>();

                if (!TextUtils.isEmpty(item.getTelephone())) {
                    showList.add(item.getTelephone());
                }
                if (!TextUtils.isEmpty(item.getMobile())) {
                    showList.add(item.getMobile());
                }
//               users.remove(mUser);//下面的列表去除当前用户，展示在最上面
                if (item.isSelected()) { //被选中
//                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_select);
                    iv_select.setVisibility(View.VISIBLE);
                } else { //取消选中
//                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_cancle_select);
                    iv_select.setVisibility(View.GONE);
                }

            }
        };
    }

    /**
     * 判断传入部门下面是否还有部门
     *
     * @return
     */
    private boolean isLastDept(List<Organize> list, String id) {
        if (list != null) {
            for (Organize organize : list) {
                if (organize.getParent().equals(id)) { //根部门的父节点的id为0
                    return false;
                }
            }
            return true;
        }
        return false;
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
            List<Organize> list = deptDao.queryForAll();
            for (Organize organize : list) {
                if (organize.getUuid().equals(Global.mUser.getDepartmentId())) { //得到自己的部门
                    myDept.add(organize);
                } else {
                    allDept.add(organize);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (myDept != null) {
            lv_myDept.setAdapter(getAdapter(myDept));
        }

        if (allDept != null) {
            String id = getFirstDeptID(allDept);
            showDept = getBelowDept(allDept, id);
            lv_allDept.setAdapter(getAdapter(showDept));
        }


        /**
         * 其他部门的点击事件
         */  //不是员工列表部门树才可点击
        lv_allDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!isUserList) {
                    isRoot = false;
                    //动态添加部门名称
                    for (TextView tv : textViewList) {
                        tv.setTextColor(getResources().getColor(R.color.hanyaRed));
                    }
                    final TextView tv = new TextView(context);
                    tv.setTextColor(getResources().getColor(R.color.text_info));
                    tv.setTextSize(17);
                    tv.setText("-->" + showDept.get(position).getName());
                    tv.setTag(showDept.get(position).getUuid());
                    textViewList.add(tv);
                    ll_dept.addView(tv);
                    isRoot();
                    if (isLastDept(allDept, showDept.get(position).getUuid())) {
                        isUserList = true;
                        currentUsers = dictionaryHelper.getStaffByDeptId(showDept.get(position).getUuid());
                        setSelectedUser();
                        adapter = getUserAdapter(currentUsers);
                        lv_allDept.setAdapter(adapter);
                    } else {
                        showDept = getBelowDept(allDept, showDept.get(position).getUuid());
                        lv_allDept.setAdapter(getAdapter(showDept));
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isUserList = false;
                            tv.setTextColor(getResources().getColor(R.color.text_info));
                            showDept = getBelowDept(allDept, (String) tv.getTag());
                            if (showDept.size() > 0) {
                                lv_allDept.setAdapter(getAdapter(showDept));
                            } else {
                                setSelectedUser();
                                adapter = getUserAdapter(currentUsers);
                                lv_allDept.setAdapter(adapter);
                            }

                            List<TextView> removeList = textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()); //移除点击的textview后面的view
                            for (int i = 0; i < removeList.size(); i++) {
                                ll_dept.removeView(removeList.get(i));
                            }
                            //subList方法它返回原来list的从[fromIndex, toIndex)之间这一部分的视图，
                            // 之所以说是视图，是因为实际上，返回的list是靠原来的list支持的
                            //所以，对原来的list和返回的list做的非结构性修改都会影响到彼此对方
                            //textviewList要放到最后移除元素
                            textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()).clear();
                        }
                    });
                } else {
                    other_user = currentUsers.get(position);
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
            }
        });


        lv_myDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getStaffById(myDept.get(position).getUuid());
                currentUsers = dictionaryHelper.getStaffByDeptId(myDept.get(position).getUuid());
                setSelectedUser();
                adapter = getUserAdapter(currentUsers);
                lv_allDept.setAdapter(adapter);
                isUserList = true; //进入员工列表
                isRoot = false;  // 不是根节列表
                isRoot();
                tv_myDept.setTextColor(getResources().getColor(R.color.titlecolor));
                final TextView tv = new TextView(context); //动态添加部门名称
                tv.setTextColor(getResources().getColor(R.color.text_info));
                tv.setTextSize(15);
                tv.setText("-->" + myDept.get(position).getName());
                if (showDept.size() > 0) {
                    tv.setTag(showDept.get(position).getUuid());
                }
                textViewList.add(tv);
                ll_dept.addView(tv);

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
                    lv_allDept.setAdapter(getAdapter(showDept));
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
     * 返回上一级操作
     */
    public void back() {
        if (textViewList.size() > 0) {
            isUserList = false;
            ll_dept.removeView(textViewList.get(textViewList.size() - 1));
            textViewList.remove(textViewList.size() - 1);
            if (textViewList.size() == 0) {
                isRoot = true;
                if (allDept != null) {
                    String id = getFirstDeptID(allDept);
                    showDept = getBelowDept(allDept, id);
                    lv_allDept.setAdapter(getAdapter(showDept));
                }
                for (int i = 0; i < textViewList.size(); i++) { //移除添加的textview
                    ll_dept.removeView(textViewList.get(i));
                }
                isRoot();
            } else {
                textViewList.get(textViewList.size() - 1).setTextColor(getResources().getColor(R.color.text_info));
                showDept = getBelowDept(allDept, (String) textViewList.get(textViewList.size() - 1).getTag());
                lv_allDept.setAdapter(getAdapter(showDept));
                isRoot();
            }
        } else {
            if (notifierActivity == null) {
                notifierActivity = (SelectedNotifierActivity) getActivity();
                notifierActivity.finish();
            } else {
                notifierActivity.finish();
            }
        }
    }

    /**
     * 判断是否是根节点,根据根节点显示和隐藏布局
     */
    private void isRoot() {
        if (isRoot) {
            lv_myDept.setVisibility(View.VISIBLE);
            other_dept.setVisibility(View.VISIBLE);
            tv_myDept.setText("我的组织");
            tv_myDept.setTextColor(getResources().getColor(R.color.titlecolor));
            textViewList.clear();
        } else {
            lv_myDept.setVisibility(View.GONE);
            other_dept.setVisibility(View.GONE);
            tv_myDept.setText("全部组织");
            tv_myDept.setTextColor(getResources().getColor(R.color.hanyaRed));
        }
    }

    /**
     * 获取部门列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<Organize> getAdapter(List<Organize> list) {
        return new CommanAdapter<Organize>(list, context, R.layout.item_organize) {
            @Override
            public void convert(int position, Organize item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_organize, item.getName());
                viewHolder.setTextValue(R.id.tv_staff_count_item_organize, "(" + item.getStaffNumber() + ")");

            }
        };
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
}
