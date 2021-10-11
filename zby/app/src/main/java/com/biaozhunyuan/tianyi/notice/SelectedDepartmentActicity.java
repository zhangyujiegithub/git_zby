package com.biaozhunyuan.tianyi.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.j256.ormlite.dao.Dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王璞 on 2018/3/30.
 * <p>
 * 选择通知部门
 */

public class SelectedDepartmentActicity extends BaseActivity {

    private List<Organize> allDept = new ArrayList<Organize>();
    private List<Organize> myDept = new ArrayList<Organize>();
    private List<Organize> showDept = new ArrayList<Organize>();
    private List<String> selectUsers_all = new ArrayList<>();
    private NoScrollListView lv_myDept;
    private NoScrollListView lv_allDept;
    private TextView count_user;//已选择员工的数量
    private TextView tv_sure;//确定按钮
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private boolean isRoot = true;  //是否是根节点
    private boolean isUserList = false;  //是否员工列表
    private LinearLayout ll_dept; //顶部部门名称，动态添加部门名称
    private TextView tv_myDept; // 我的组织按钮
    private ORMDataHelper dataHelper;
    private TextView other_dept;
    private CommanAdapter<Organize> myAdapter;
    private BoeryunHeaderView headerView;
    private List<TextView> textViewList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_department);
        initView();
        getAllDept();
        setOnEvent();
    }

    private void setOnEvent() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                returnResult();
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

        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

    }

    private void initView() {
        dataHelper = ORMDataHelper.getInstance(SelectedDepartmentActicity.this);
        lv_myDept = (NoScrollListView) findViewById(R.id.lv_my_dept_organize);
        lv_allDept = (NoScrollListView) findViewById(R.id.lv_all_dept_organize);
        other_dept = (TextView) findViewById(R.id.tv_other_dept_organize);
        count_user = (TextView) findViewById(R.id.tv_count_select_user);
        tv_sure = (TextView) findViewById(R.id.tv_sure_select_user);
        ll_dept = (LinearLayout) findViewById(R.id.ll_dept_names_organize);
        tv_myDept = (TextView) findViewById(R.id.tv_back_first_organize);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_select_user);
//        lv_allDept.setFocusable(false);
        headerView.setTitle("选择部门");

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
            //去掉自己的部门
            for (int i = 0; i < list.size(); i++) {
                if (myDept.get(0).getUuid().equals(list.get(i).getUuid())) {
                    list.remove(i);
                    break;
                }
            }
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
                if (isLastDept(allDept, showDept.get(position).getUuid())) { //判断传入部门下面是否还有部门
                    showDept.get(position).setSelected(!showDept.get(position).isSelected());
                    if (showDept.get(position).isSelected()) {
                        selectUsers_all.add(showDept.get(position).getUuid());
                    } else {
                        selectUsers_all.remove(showDept.get(position).getUuid());
                    }

                    myAdapter = getAdapter(showDept);
                    lv_allDept.setAdapter(myAdapter);
                    count_user.setText(selectUsers_all.size() + "个部门");
                } else {
                    isRoot = false;
                    final TextView tv = new TextView(SelectedDepartmentActicity.this); //动态添加部门名称
                    tv.setTextColor(getResources().getColor(R.color.text_info));
                    tv.setTextSize(15);
                    tv.setText("-->" + showDept.get(position).getName());
                    tv.setTag(showDept.get(position).getUuid());
                    textViewList.add(tv);
                    ll_dept.addView(tv);
                    isRoot();
                    showDept = getBelowDept(allDept, showDept.get(position).getUuid());
                    lv_allDept.setAdapter(getAdapter(showDept));

                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isUserList = false;
                            tv.setTextColor(getResources().getColor(R.color.text_info));
                            showDept = getBelowDept(allDept, (String) tv.getTag());
//                                setSelectUsers(showDept, selectUsersName_all);
                            lv_allDept.setAdapter(getAdapter(showDept));
                            List<TextView> removeList = textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()); //移除点击的textview后面的view
                            for (int i = 0; i < removeList.size(); i++) {
                                ll_dept.removeView(removeList.get(i));
                            }
                            textViewList.removeAll(removeList);

                            textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()).clear();
                            isRoot();
                        }
                    });
                }
                isRoot();
            }
        });

        lv_myDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDept.get(position).setSelected(!myDept.get(position).isSelected());
                if (myDept.get(position).isSelected()) {
                    selectUsers_all.add(myDept.get(position).getUuid());
                } else {
                    selectUsers_all.remove(myDept.get(position).getUuid());
                }
                count_user.setText(selectUsers_all.size() + "个部门");
                myAdapter = getUserAdapter(myDept);
                lv_myDept.setAdapter(myAdapter);
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
     * 判断是否是根节点,根据根节点显示和隐藏布局
     */
    private void isRoot() {
        if (isRoot) {
            lv_myDept.setVisibility(View.VISIBLE);
            other_dept.setVisibility(View.VISIBLE);
            tv_myDept.setText("我的组织");
        } else {
            lv_myDept.setVisibility(View.GONE);
            other_dept.setVisibility(View.GONE);
            tv_myDept.setText("全部组织");
        }
    }

    /**
     * 获取部门列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<Organize> getAdapter(final List<Organize> list) {
        return new CommanAdapter<Organize>(list, SelectedDepartmentActicity.this, R.layout.item_organize) {
            @Override
            public void convert(int position, Organize item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_organize, item.getName());
                viewHolder.setTextValue(R.id.tv_staff_count_item_organize, "(" + item.getStaffNumber() + ")");

                ImageView iv_select = viewHolder.getView(R.id.choose_item_select_user);
                if (item.isSelected()) { //被选中
                    iv_select.setVisibility(View.VISIBLE);
                } else { //取消选中
                    iv_select.setVisibility(View.GONE);
                }

            }
        };
    }


    /**
     * 获取员工列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<Organize> getUserAdapter(final List<Organize> list) {
        return new CommanAdapter<Organize>(list, SelectedDepartmentActicity.this, R.layout.item_organize_select) {
            @Override
            public void convert(int position, Organize item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_organize, item.getName());
                viewHolder.setTextValue(R.id.tv_staff_count_item_organize, "(" + item.getStaffNumber() + ")");
                ImageView iv_select = viewHolder.getView(R.id.choose_item_select_user);
                if (item.isSelected()) { //被选中
//                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_select);
                    iv_select.setVisibility(View.VISIBLE);
                } else { //取消选中
//                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_cancle_select);
                    iv_select.setVisibility(View.GONE);
                }
                count_user.setText(selectUsers_all.size() + "个部门");
            }
        };
    }

    /**
     * 返回数据
     */
    private void returnResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        List<String> select_dept = new ArrayList<>();
        for (String l : selectUsers_all) {
            select_dept.add(l);
        }
        bundle.putSerializable(RESULT_SELECT_USER, (Serializable) select_dept);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 重写返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (textViewList.size() > 0) {
                isUserList = false;
                ll_dept.removeView(textViewList.get(textViewList.size() - 1));
                textViewList.remove(textViewList.size() - 1);
                if (textViewList.size() == 0) {
                    isRoot = true;
                    if (allDept != null) {
                        String id = getFirstDeptID(allDept);
                        showDept = getBelowDept(allDept, id);
//                        setSelectUsers(showDept, selectUsersName_all);
                        lv_allDept.setAdapter(getAdapter(showDept));
                    }
                    for (int i = 0; i < textViewList.size(); i++) { //移除添加的textview
                        ll_dept.removeView(textViewList.get(i));
                    }
                    isRoot();
                } else {
                    textViewList.get(textViewList.size() - 1).setTextColor(getResources().getColor(R.color.text_name));
                    showDept = getBelowDept(allDept, (String) textViewList.get(textViewList.size() - 1).getTag());
//                    setSelectUsers(showDept, selectUsersName_all);
                    lv_allDept.setAdapter(getAdapter(showDept));
                    isRoot();
                }
            } else if (textViewList.size() == 0) {
                finish();
            }

            return false;
        }
        return true;
    }

}
