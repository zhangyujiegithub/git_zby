package com.biaozhunyuan.tianyi.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王安民 on 2017/9/5.
 * 选择任务执行人和参与人。任务执行人单选，参与人多选。
 */

public class SelectUserActivity extends Activity {

    private BoeryunHeaderView headerView;
    private TextView tv_dept;
    private NoScrollListView lv_staff;
    private LinearLayout ll_myselef;
    private TextView count_user;//已选择员工的数量
    private TextView tv_sure;//确定按钮

    private ImageView iv_select;
    private CircleImageView iv_head;//头像
    private TextView tv_name;
    private TextView tv_pos;//职位


    private DictionaryHelper dictionaryHelper;
    private List<User> users;
    private List<User> selectUsers = new ArrayList<User>();
    private User mUser = new User();
    private CommanAdapter<User> adapter;
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";

    private Context context;

    private boolean isSingleSelect = false;

    private String title = "";
    private LinearLayout bottom_select;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_user);

        context = getBaseContext();
        dictionaryHelper = new DictionaryHelper(context);
        initViews();
        getShowStaff();
        setOnEvent();
    }


    private void initViews() {
        isSingleSelect = getIntent().getBooleanExtra("isSingleSelect", false);
        title = getIntent().getStringExtra("title");
        headerView = (BoeryunHeaderView) findViewById(R.id.header_select_user);
        lv_staff = (NoScrollListView) findViewById(R.id.lv_staff_list_select_user);
        ll_myselef = (LinearLayout) findViewById(R.id.ll_my_select_user);
        iv_select = (ImageView) findViewById(R.id.select_my_select_user);
        iv_head = (CircleImageView) findViewById(R.id.head_select_user);
        tv_name = (TextView) findViewById(R.id.name_select_user);
        tv_pos = (TextView) findViewById(R.id.position_select_user);
        count_user = (TextView) findViewById(R.id.tv_count_select_user);
        tv_dept = (TextView) findViewById(R.id.tv_dept_name_select_user);
        tv_sure = (TextView) findViewById(R.id.tv_sure_select_user);

        lv_staff.setFocusable(false);


        tv_dept.setText(dictionaryHelper.getDepartNameById(Global.mUser.getDepartmentId()));

        if (!TextUtils.isEmpty(title)) {
            headerView.setTitle(title);
        }
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

        ll_myselef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setSelected(!mUser.isSelected());
                if (mUser.isSelected()) {
                    iv_select.setImageResource(R.drawable.ic_select);
                    selectUsers.add(mUser);
                } else {
                    iv_select.setImageResource(R.drawable.ic_cancle_select);
                    selectUsers.remove(mUser);
                }
                count_user.setText(selectUsers.size() + "位同事");
                if (isSingleSelect) {
                    returnResult();
                }
            }
        });

        lv_staff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = users.get(position);
                user.setSelected(!user.isSelected());//设置员工选择为反选
                if (user.isSelected()) {
                    selectUsers.add(user);
                } else {
                    selectUsers.remove(user);
                }
                adapter.notifyDataSetChanged();
                count_user.setText(selectUsers.size() + "位同事");
                if (isSingleSelect) {
                    returnResult();
                }
            }
        });


        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

    }

    private void getShowStaff() {


//        users = dictionaryHelper.getStaffByDeptId(Global.mUser.getDepartmentId());
        users = dictionaryHelper.getAllStaff();
        if (users != null) {
            for (User user : users) {
                if (user.getUuid().equals(Global.mUser.getUuid())) {
                    mUser = user;
                    break;
                }
            }

            users.remove(mUser);//下面的列表去除当前用户，展示在最上面
            adapter = getAdapter(users);
            lv_staff.setAdapter(adapter);
            //显示当前员工信息
            isSelect();
            ImageUtils.displyImageById(mUser.getAvatar(), iv_head);
            tv_name.setText(mUser.getName());
            tv_pos.setText(mUser.getPost());

        }

    }

    /**
     * 判断是否被选中
     */
    private void isSelect() {
        if (mUser.isSelected()) {
            iv_select.setImageResource(R.drawable.ic_select);
        } else {
            iv_select.setImageResource(R.drawable.ic_cancle_select);
        }
    }


    private CommanAdapter<User> getAdapter(List<User> list) {
        return new CommanAdapter<User>(list, context, R.layout.item_select_user) {
            @Override
            public void convert(int position, User item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.name_item_select_user, StrUtils.pareseNull(item.getName()));
                viewHolder.setTextValue(R.id.position_item_select_user, StrUtils.pareseNull(item.getPost()));
                viewHolder.setUserPhotoById(R.id.head_item_select_user, item);

                if (item.isSelected()) { //被选中
                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_select);
                } else { //取消选中
                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_cancle_select);
                }
            }
        };
    }


    /**
     * 返回数据
     */
    private void returnResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        UserList userList = new UserList();
        userList.setUsers(selectUsers);
        bundle.putSerializable(RESULT_SELECT_USER, userList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
