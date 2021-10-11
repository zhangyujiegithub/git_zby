package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.example.chat.R;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import static com.biaozhunyuan.tianyi.common.helper.ORMDataHelper.getInstance;


public class UserInfoActivity extends BaseActivity {

    private CircleImageView ivHead;
    private ImageView ivBack;
    private TextView tvName;
    private TextView tvPhone;
    private TextView tvPosition;
    private TextView tvDepart;
    private LinearLayout llFoundChat;
    private LinearLayout llCallPhone;
    private LinearLayout llsendMsg;
//    private LinearLayout llDept;


    private Context mContext;
    private String userId;
    private User mUser;
    private DictionaryHelper helper;
    private ORMDataHelper dataHelper;
    private List<Organize> allDept;
    String deptName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initViews();
        getIntentData();
        setOnEvent();
    }

    private void initViews() {
        ivHead = findViewById(R.id.iv_head);
        tvName = findViewById(R.id.tv_name);
        tvPhone = findViewById(R.id.tv_phone);
        tvPosition = findViewById(R.id.tv_position);
        tvDepart = findViewById(R.id.tv_department);
        ivBack = findViewById(R.id.iv_back);
        llFoundChat = findViewById(R.id.ll_found_chat);
//        llDept = findViewById(R.id.ll_dept);
        llCallPhone = findViewById(R.id.ll_call_phone);
        llsendMsg = findViewById(R.id.ll_send_msg);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getIntentData() {
        mContext = UserInfoActivity.this;
        helper = new DictionaryHelper(mContext);
        dataHelper = getInstance(mContext);
        if (getIntent().getExtras() != null) {
            userId = getIntent().getStringExtra("userId");
            mUser = helper.getUser(userId);

            if (mUser != null) {
                initData();
            }
        }
    }

    private void initData() {
        ImageUtils.displyUserPhotoById(mContext, mUser.getUuid(), ivHead);
        tvName.setText(mUser.getName());
        tvPhone.setText(mUser.getMobile());
        tvPosition.setText(mUser.getPost());
        getAllDept();
        addMyDept();
        addAboveDept(mUser.getDepartmentId());
//        addRootDept();
        tvDepart.setText(deptName);
    }

    private void setOnEvent() {
        llFoundChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChartIntentUtils.startChatInfo(mContext, mUser.getUuid());
                finish();
            }
        });

        llsendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = mUser.getMobile();
                if (TextUtils.isEmpty(num)) {
                    showShortToast("没有手机号");
                    return;
                }
                Uri smsToUri = Uri.parse("smsto:" + num);
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                startActivity(intent);
            }
        });

        llCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = mUser.getMobile();
                if (TextUtils.isEmpty(num)) {
                    showShortToast("没有手机号");
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + num));
                startActivity(intent);
            }
        });
    }

    /***
     * 获取部门列表
     */
    private void getAllDept() {
        Dao<Organize, Integer> deptDao = dataHelper.getDeptDao();
        try {
            allDept = deptDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addMyDept() {

        deptName = helper.getDepartNameById(mUser.getDepartmentId());
    }

    private void addAboveDept(String deptId) {
        Organize myDept = null;
        Organize aboveDept = null;
        for (Organize organize : allDept) {
            if (deptId.equals(organize.getUuid())) {
                myDept = organize;
            }
        }

        if (myDept == null)
            return;

        for (Organize organize : allDept) {
            if (organize.getUuid().equals(myDept.getParent())) {
                aboveDept = organize;
                if ("0".equals(organize.getParent())) {
                    deptName = "全部组织 > " + deptName;
                } else {
                    deptName = organize.getName() + " > " + deptName;
                }

            }

        }

        if (aboveDept == null) {
            return;
        }

        for (Organize organize : allDept) {
            if (organize.getUuid().equals(aboveDept.getParent())) {
                addAboveDept(aboveDept.getUuid());
            }
        }
    }

    private void addRootDept() {
        deptName = "全部组织 > " + deptName;
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

}
