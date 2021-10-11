package com.biaozhunyuan.tianyi.common.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 实现横向一排显示最近指派/选择员工头像,点击返回User对象
 */

public class HorizontalRecentlySelectedStaffView extends RelativeLayout {

    private Context mContext;
    private View mParentView;
    private RelativeLayout rl_parent;
    private LinearLayout ll_user;
    private DictionaryHelper dictionaryHelper;
    private ORMDataHelper dataHelper;
    private List<User> AllStaffList;
    private List<AvatarImageView> imgList;
    //    private List<TextView> tvList;
    private List<ImageView> ivList;
    private int lastPosition = -1;
    private User mUser;
    private OnSelectedUserListener mOnSelectedUserListener;
    private HorizontalScrollView horizontalScl;
    private Fragment mFragment;

    public HorizontalRecentlySelectedStaffView(Context context) {
        super(context);
    }

    public HorizontalRecentlySelectedStaffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        dictionaryHelper = new DictionaryHelper(mContext);
        dataHelper = ORMDataHelper.getInstance(mContext);
        imgList = new ArrayList<>();
//        tvList = new ArrayList<>();
        ivList = new ArrayList<>();
        AllStaffList = new ArrayList<>();
        mUser = Global.mUser;
        mParentView = LayoutInflater.from(context).
                inflate(R.layout.view_horizontal_recently_seletedstaff, this, true);
        initView(mParentView);
        getStaffList();
    }


    public HorizontalRecentlySelectedStaffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(View view) {
        if (view != null) {
            ll_user = view.findViewById(R.id.ll_user_root_task_day_view);
            horizontalScl = view.findViewById(R.id.horizontalScrollView);
            rl_parent = view.findViewById(R.id.rl_parent);
            horizontalScl.setSmoothScrollingEnabled(true);
        }

    }

    /**
     * 重新加载所有员工和最近选择列表
     *
     * @param user 传入一个user对象 默认选中该user
     */
    public void reloadStaffList(User user) {
        getStaffList(user);
    }

    /**
     * 重新加载所有员工和最近选择列表
     *
     * @param userUuid 传入一个uuid 默认选中与该uuid对应的user
     */
    public void reloadStaffList(String userUuid) {
        User user = dictionaryHelper.getUser(userUuid);
        addSelectedUserListener(user);
        getStaffList(user);
    }


    /**
     * 更新最近选择和选择选中的员工
     */
    private void getStaffList(User selectedUser) {
        try {
            Dao<Latest, Integer> latestDao = dataHelper.getLatestDao();
            List<Latest> latests = latestDao.queryForAll();
            for (int i = 0; i < latests.size(); i++) { //先插入最近联系人
                User user = ViewHelper.turnUser(latests.get(i));
                if (!removeDuplicate(user)) {
                    AllStaffList.add(0, user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        generationTaskCompletionView(AllStaffList, selectedUser);
    }

    /**
     * 获取部门下的所有员工和最近选择
     */
    private void getStaffList() {
        try {
            Dao<Latest, Integer> latestDao = dataHelper.getLatestDao();
            List<Latest> latests = latestDao.queryForAll();
            for (Latest latest : latests) { //先插入最近联系人
                if (!latest.getUuid().equals(mUser.getUuid())) { //自己放到最前面
                    AllStaffList.add(ViewHelper.turnUser(latest));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<User> staffMyDept = dictionaryHelper.getStaffByDeptId(mUser.getDepartmentId());
        for (User user : staffMyDept) { //再插入我的部门员工
            if (!user.getUuid().equals(mUser.getUuid())) { //自己放到最前面
                if (!removeDuplicate(user)) {
                    AllStaffList.add(user);
                }
            }
        }
        AllStaffList.add(0, mUser);
        generationTaskCompletionView(AllStaffList, null);
    }

    //把list里的对象遍历一遍，用list.contain()，如果不存在就放入到另外一个list集合中
    public boolean removeDuplicate(User user) {
        boolean isRePeat = false;
        for (User user1 : AllStaffList) {
            if (user.getUuid().equals(user1.getUuid())) {
                isRePeat = true;
                break;
            }
        }
//        if (!isRePeat) {
//            AllStaffList.add(0, user);
//        }
        return isRePeat;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }


    /**
     * 生成头像view添加到父容器中
     *
     * @param userList
     */
    private void generationTaskCompletionView(List<User> userList, User selectedUser) {
        ll_user.removeAllViews();
        imgList.clear();
//        tvList.clear();
        ivList.clear();
        final View add = LayoutInflater.from(mContext).inflate(R.layout.item_input_add, null);
        ImageView ivAdd = add.findViewById(R.id.iv_item_input_add);
        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName comp = new ComponentName(mContext, "com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity");
                Intent intent = new Intent();
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "选择执行人");
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                if (mFragment != null) {
                    mFragment.startActivityForResult(intent, 102);
                } else {
                    ((Activity) mContext).startActivityForResult(intent, 102);
                }
            }
        });
        for (int i = 0; i < userList.size(); i++) {
            final User item = userList.get(i);
            final int pos = i;
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_input_user_list, null);
            final View line = LayoutInflater.from(mContext).inflate(R.layout.item_input_transpant, null);
            AvatarImageView mImg = view.findViewById(R.id.circularAvatar_input_user);
            ImageView selectedStatus = view.findViewById(R.id.item_input_selected);
            TextView tv_name = view.findViewById(R.id.tv_name_avatar);
//            ImageUtils.displyUserPhotoById(mContext, item.getUuid(), mImg);
            tv_name.setText(item.getName());

            boolean isSelectCurrentUser = false;
            try {
                //是否选择当前员工
                isSelectCurrentUser = item.getUuid().equals(mUser.getUuid());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (selectedUser != null && StrUtils.pareseNull(selectedUser.getUuid()).equals(StrUtils.pareseNull(item.getUuid()))) { //如果从选择员工界面返回过来一个user对象 默认选中
//                tv_name.setTextColor(Color.parseColor("#FFF70302"));
//                ImageUtils.displyUserPhotoById(mContext, item.getUuid(), mImg, item.getName());
                lastPosition = pos;
                selectedStatus.setVisibility(VISIBLE);
                addSelectedUserListener(item);
                horizontalScl.scrollTo(0, 0);
            } else if (selectedUser == null && isSelectCurrentUser) { //如果是首次进入 默认选中自己
//                tv_name.setTextColor(Color.parseColor("#FFF70302"));
//                ImageUtils.displyUserPhotoById(mContext, item.getUuid(), mImg,item.getName());
                lastPosition = pos;
//                selectedStatus.setVisibility(VISIBLE);
//                addSelectedUserListener(item);
            }
            ImageUtils.displyUserPhotoById(mContext, item.getUuid(), mImg);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastPosition = pos;
                    dictionaryHelper.insertLatest(item);
                    addSelectedUserListener(item);
                    for (int i = 0; i < imgList.size(); i++) {
                        AvatarImageView img = imgList.get(i);
                        if (i == lastPosition) {
//                            tvList.get(i).setTextColor(Color.parseColor("#FFF70302"));
                            ivList.get(i).setVisibility(VISIBLE);
//                            ImageUtils.displyUserPhotoById(mContext, userList.get(i).getUuid(), img, userList.get(i).getName(),true);
                        } else {
                            ivList.get(i).setVisibility(GONE);
//                            tvList.get(i).setTextColor(Color.parseColor("#000000"));
//                            ImageUtils.displyUserPhotoById(mContext, userList.get(i).getUuid(), img, userList.get(i).getName(),false);
                        }
                    }

                }
            });
//            tvList.add(tv_name);
            ivList.add(selectedStatus);
            imgList.add(mImg);
            ll_user.addView(view);
            ll_user.addView(line); //分割线
        }
        ll_user.addView(add);
    }

    private void addSelectedUserListener(User user) {
        if (mOnSelectedUserListener != null) {
            mOnSelectedUserListener.onSelected(user);
        }
    }

    /***
     * 监听选择的员工
     *
     * @param mOnSelectedUserListener
     */
    public void setOnSelectedUserListener(OnSelectedUserListener mOnSelectedUserListener) {
        this.mOnSelectedUserListener = mOnSelectedUserListener;
    }

    public interface OnSelectedUserListener {
        void onSelected(User user);
    }

}

