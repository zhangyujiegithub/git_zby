package com.biaozhunyuan.tianyi.notice;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.address.NoVerticalScrollManager;
import com.biaozhunyuan.tianyi.address.Organization;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全部部门
 */
public class SelectedNotifierFragment extends LazyFragment {
    private List<Organize> allDept = new ArrayList<>();
    private List<Organize> myDept = new ArrayList<>();
    private List<Organize> showDept = new ArrayList<>();

    private List<Organization> myDeptShow = new ArrayList<>(); //我的部门下展示的数据
    private List<Organization> otherDeptShow = new ArrayList<>();  //别的部门下展示的数据

    private OrganizeAdapter myOrganizeAdapter;
    private OrganizeAdapter OtherOrganizeAdapter;

    private RecyclerView lv_myDept;
    private RecyclerView lv_allDept;

    private boolean isRoot = true;  //是否是根节点

    private TextView other_dept;
    private LinearLayout ll_dept; //顶部部门名称，动态添加部门名称
    private TextView tv_myDept; // 我的组织按钮

    private List<TextView> textViewList = new ArrayList<>();
    private ORMDataHelper dataHelper;
    private DictionaryHelper dictionaryHelper;
    private boolean isSingleSelect = false; //任务执行人单选 任务参与人多选
    private SelectedNotifierActivity notifierActivity;
    public static boolean isResume = false;
    private String selectedAdvisorIds = ""; //已选择的员工id
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private List<User> selectUsers = new ArrayList<User>(); //已选择的员工
    private List<User> unClickAbleUsers = new ArrayList<User>(); //已选择的员工
    private List<User> currentUsers = new ArrayList<>(); //当前部门员工

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_infrom, null);
        initViews(v);
        getIntentData();
        getAllDept();
        setOnEvent();
        return v;
    }

    private void initViews(View view) {
        dataHelper = ORMDataHelper.getInstance(getActivity());
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

    /**
     * 共享客户
     */
    private void getIntentData() {
        isSingleSelect = getActivity().getIntent().getBooleanExtra("isSingleSelect", true);
        if (getActivity().getIntent().getStringExtra("selectedAdvisorIds") != null
                && getActivity().getIntent().getStringExtra("selectedAdvisorNames") != null) {
            selectedAdvisorIds = getActivity().getIntent().getStringExtra("selectedAdvisorIds");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (notifierActivity == null) {
            notifierActivity = (SelectedNotifierActivity) getActivity();
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
                for (User unClickAbleUser : unClickAbleUsers) {
                    if (user.getUuid().equals(unClickAbleUser.getUuid())) {
                        user.setClickable(false);
                    }
                }
            } else {
                user.setSelected(false);
            }
        }
        OtherOrganizeAdapter.notifyDataSetChanged();
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
                if (allDept != null) {
                    String id = getFirstDeptID(allDept);
                    showDept = getBelowDept(allDept, id);
                    //获取总部门下面的员工列表
                    currentUsers = dictionaryHelper.getStaffByDeptId(id);
                    setSelectedUser();
                    //根据部门下的部门和员工列表 得到 总得数据列表
                    otherDeptShow = getOrganizations(showDept, currentUsers);
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
            currentUsers = dictionaryHelper.getStaffByDeptId(ogz.getOrganize().getUuid());
            setSelectedUser();
            //根据部门下的部门和员工列表 得到 总得数据列表
            otherDeptShow = getOrganizations(showDept, currentUsers);
            OtherOrganizeAdapter.replaceAll(otherDeptShow);
            setSelectedUser();

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setTextColor(getResources().getColor(R.color.text_info));
                    showDept = getBelowDept(allDept, (String) tv.getTag());
                    //获取总部门下面的员工列表
                    currentUsers = dictionaryHelper.getStaffByDeptId((String) tv.getTag());
                    setSelectedUser();
                    //根据部门下的部门和员工列表 得到 总得数据列表
                    otherDeptShow = getOrganizations(showDept, currentUsers);
                    OtherOrganizeAdapter.replaceAll(otherDeptShow);
                    setSelectedUser();
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
            if (!user.isClickable()) {
                return;
            }
            dictionaryHelper.insertLatest(user);
            user.setSelected(!user.isSelected());
            if (user.isSelected()) {
                notifierActivity.addSelected(user);
            } else {
                notifierActivity.removeSelected(user);
            }
            if (isSingleSelect) {
                notifierActivity.returnResult();
            }
            OtherOrganizeAdapter.notifyDataSetChanged();
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
            lv_myDept.setAdapter(myOrganizeAdapter);
        }

        if (allDept != null) {
            String id = getFirstDeptID(allDept);
            //获取总部门下面的部门列表
            showDept = getBelowDept(allDept, id);
            //获取总部门下面的员工列表
            currentUsers = dictionaryHelper.getStaffByDeptId(id);
            //根据部门下的部门和员工列表 得到 总得数据列表
            otherDeptShow = getOrganizations(showDept, currentUsers);
            OtherOrganizeAdapter = new OrganizeAdapter(getActivity(), otherDeptShow);
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
                //客户已共享员工选中
                if (!TextUtils.isEmpty(selectedAdvisorIds)) {
                    if (selectedAdvisorIds.contains(user.getUuid())) {
                        user.setSelected(true);
                    }
                }
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

    /**
     * 返回上一步
     */
    public void back() {
        if (textViewList.size() > 0) {
            ll_dept.removeView(textViewList.get(textViewList.size() - 1));
            textViewList.remove(textViewList.size() - 1);
            if (textViewList.size() == 0) {
                isRoot = true;
                if (allDept != null) {
                    String id = getFirstDeptID(allDept);
                    showDept = getBelowDept(allDept, id);
                    //获取总部门下面的员工列表
                    currentUsers = dictionaryHelper.getStaffByDeptId(id);
                    setSelectedUser();
                    //根据部门下的部门和员工列表 得到 总得数据列表
                    otherDeptShow = getOrganizations(showDept, currentUsers);
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
                currentUsers = dictionaryHelper.getStaffByDeptId((String) textViewList.get(textViewList.size() - 1).getTag());
                setSelectedUser();
                //根据部门下的部门和员工列表 得到 总得数据列表
                otherDeptShow = getOrganizations(showDept, currentUsers);
                OtherOrganizeAdapter.replaceAll(otherDeptShow);
                isRoot();
            }
        } else if (textViewList.size() == 0) {
            getActivity().finish();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        notifierActivity = (SelectedNotifierActivity) getActivity();

        unClickAbleUsers = notifierActivity.getUnClickAbleUsers();
    }

    public void setSelectedUser() {
        for (User user : currentUsers) {
            for (User u : selectUsers) {
                if (user.getUuid().equals(u.getUuid())) {
                    user.setSelected(true);
                }
            }
            for (User unClickAbleUser : unClickAbleUsers) {
                if (user.getUuid().equals(unClickAbleUser.getUuid())) {
                    user.setClickable(false);
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
                if (OtherOrganizeAdapter != null) {
                    OtherOrganizeAdapter.notifyDataSetChanged();
                }
            }
        } else {
            for (User u : currentUsers) {
                u.setSelected(false);
            }
        }
    }

    static class OrganizeAdapter extends RecyclerView.Adapter<OrganizeAdapter.BaseAdapter> {

        private List<Organization> datas;
        private Context mContext;
        private OrganizeAdapter.OnItemClickListener itemClickListener;
        private DictIosPickerBottomDialog dialog;
        private DictionaryHelper helper;

        public OrganizeAdapter(Context context, List<Organization> organizations) {
            datas = organizations;
            mContext = context;
            helper = new DictionaryHelper(mContext);
            dialog = new DictIosPickerBottomDialog(mContext);
        }

        public void replaceAll(List<Organization> list) {
            datas.clear();
            if (list != null && list.size() > 0) {
                datas.addAll(list);
            }
            notifyDataSetChanged();
        }

        @Override
        public OrganizeAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 1:
                    return new organizeAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organize, parent, false));
                case 2:
                    return new userAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_infrom, parent, false));
            }
            return null;
        }

        public class BaseAdapter extends RecyclerView.ViewHolder {

            public BaseAdapter(View itemView) {
                super(itemView);
            }

            void setData(int position, Object object) {

            }
        }

        @Override
        public void onBindViewHolder(OrganizeAdapter.BaseAdapter holder, int position) {
            holder.setData(position, datas.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return datas.get(position).getDataType();
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }


        /**
         * 员工类型holder
         */
        private class userAdapter extends OrganizeAdapter.BaseAdapter {

            private AvatarImageView ivHead;
            private TextView tvName;
            //            private TextView tvPos;
//            private TextView tvTel;
            private ImageView ivSelect;
            private LinearLayout root;

            public userAdapter(View itemView) {
                super(itemView);

                ivHead = itemView.findViewById(R.id.head_item_workmate);
                tvName = itemView.findViewById(R.id.name);
//                tvName = itemView.findViewById(R.id.name_item_workmate);
//                tvPos = itemView.findViewById(R.id.position_item_workmate);
//                tvTel = itemView.findViewById(R.id.tel_item_workmate);
                ivSelect = itemView.findViewById(R.id.choose_item_select_user);
                root = itemView.findViewById(R.id.root);
            }

            @Override
            void setData(int position, Object object) {
                super.setData(position, object);
//                List<String> showList = new ArrayList<>();
                Organization organization = (Organization) object;
                User item = organization.getUser();

                tvName.setText(item.getName());
//                tvPos.setText(item.getPost());
                ImageUtils.displyUserPhotoById(mContext, item.getUuid(), ivHead);


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

                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(position, organization);
                        }
                    }
                });
            }
        }

        /**
         * 部门类型holder
         */
        private class organizeAdapter extends OrganizeAdapter.BaseAdapter {
            private TextView tvName;
            private TextView tvCount;
            private LinearLayout root;

            public organizeAdapter(View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name_item_organize);
                tvCount = itemView.findViewById(R.id.tv_staff_count_item_organize);
                root = itemView.findViewById(R.id.root);
            }

            @Override
            void setData(int position, Object object) {
                super.setData(position, object);

                Organization organization = (Organization) object;
                Organize organize = organization.getOrganize();

                tvName.setText(organize.getName());
                tvCount.setText("(" + organize.getStaffNumber() + ")");


                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(position, organization);
                        }
                    }
                });
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int pos, Organization ogz);
        }

        public void setOnItemClickListener(OrganizeAdapter.OnItemClickListener listener) {
            itemClickListener = listener;
        }
    }
}
