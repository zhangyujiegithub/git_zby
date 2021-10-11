package com.biaozhunyuan.tianyi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;

/**
 * 表格布局展示最近指派/选择员工头像,点击返回User对象
 */

public class GridRecentlySelectedStaffView extends RelativeLayout {

    private Context mContext;
    private View mParentView;
    private DictionaryHelper dictionaryHelper;
    private ORMDataHelper dataHelper;
    private List<User> AllStaffList; //全体员工的集合
    private User mUser;
    private OnKeyBoardSelectedUserListener mOnKeyBoardSelectedUserListener; //员工接口回调
    private GridView gridView;
    private CommanAdapter<User> adapter; //gridview适配器

    public GridRecentlySelectedStaffView(Context context) {
        super(context);
    }

    public GridRecentlySelectedStaffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        dictionaryHelper = new DictionaryHelper(mContext);
        dataHelper = ORMDataHelper.getInstance(mContext);
        AllStaffList = new ArrayList<>();
        mUser = Global.mUser;
        mParentView = LayoutInflater.from(context).
                inflate(R.layout.view_grid_recently_seletedstaff, this, true);
        initView(mParentView);
        getStaffList();
        setOnTouch();
    }

    private void setOnTouch() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != AllStaffList.size() - 1) { //如果是点击最后一个条目 加号按钮 另处理
                    User item = adapter.getItem(position);
                    for (User u : AllStaffList) {
                        if (StrUtils.pareseNull(u.getUuid()).equals(item.getUuid())) {
                            if (u.isSelected()) { //如果已经选中 提示最少选择一个执行人
                                Toast.makeText(mContext, "最少选择一个任务执行人", Toast.LENGTH_SHORT).show();
                                break;
                            } else {
                                u.setSelected(true);
                                addKeyBoardSelectedUserListener(item);
                            }
                        } else { //其余都设为未选中
                            u.setSelected(false);
                        }
                    }
                } else { //如果是点击最后一个条目 跳转选择执行人界面
                    Intent intent = new Intent(mContext, SelectedNotifierActivity.class);
                    intent.putExtra("isSingleSelect", true);
                    intent.putExtra("title", "选择执行人");
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
                }
            }
        });
    }


    public GridRecentlySelectedStaffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(View view) {
        if (view != null) {
            gridView = view.findViewById(R.id.gridView);
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
        addKeyBoardSelectedUserListener(user);
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
        AllStaffList.add(new User());//添加一个空user 占一个元素位置 用来添加'+'按钮
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


    /**
     * 生成头像view添加到父容器中
     *
     * @param userList
     */
    private void generationTaskCompletionView(List<User> userList, User selectedUser) {
        if (selectedUser == null) {
//            mUser.setSelected(true); //默认选中自己
            adapter = getTaskAdapter(userList);
            gridView.setAdapter(adapter);
            addKeyBoardSelectedUserListener(mUser);
        } else {
            for (User u : userList) {
                if (StrUtils.pareseNull(u.getUuid()).equals(selectedUser.getUuid())) {
                    u.setSelected(true);
                } else {
                    u.setSelected(false);
                }
            }
            adapter.notifyDataSetChanged();
            addKeyBoardSelectedUserListener(selectedUser);
        }
    }


    private CommanAdapter<User> getTaskAdapter(List<User> userList) {
        return new CommanAdapter<User>(userList, mContext, R.layout.item_select_executor) {

            @Override
            public void convert(int position, final User item, final BoeryunViewHolder viewHolder) {
//                ImageView selectIv = viewHolder.getView(R.id.item_input_selected);
                if (position != AllStaffList.size() - 1) { //判断是否为最后一个条目
                    TextView tvName = viewHolder.getView(R.id.tv_name_avatar);
                    viewHolder.getView(R.id.ll_task_item_parent).setVisibility(VISIBLE);
                    viewHolder.getView(R.id.ivAdd).setVisibility(GONE);
                    viewHolder.setUserPhotoById(R.id.circularAvatar_input_user, item);
                    tvName.setText(item.getName());
                    if (item.isSelected()) { //判断是否选中
                        tvName.setTextColor(Color.parseColor("#eea73c"));
//                        selectIv.setBackgroundResource(R.drawable.ic_select);
                    } else {
                        tvName.setTextColor(Color.parseColor("#000000"));
//                        selectIv.setBackgroundResource(R.drawable.ic_cancle_select);
                    }
                } else { //如果是最后一条 添加一个'+'按钮
                    viewHolder.getView(R.id.ll_task_item_parent).setVisibility(INVISIBLE);
                    viewHolder.getView(R.id.ivAdd).setVisibility(VISIBLE);
                }
            }
        };
    }


    private void addKeyBoardSelectedUserListener(User user) {
        if (mOnKeyBoardSelectedUserListener != null) {
            mOnKeyBoardSelectedUserListener.onSelected(user);
        }
    }

    /***
     * 监听选择的员工
     *
     * @param mOnKeyBoardSelectedUserListener
     */
    public void setOnKeyBoardSelectedUserListener(GridRecentlySelectedStaffView.OnKeyBoardSelectedUserListener mOnKeyBoardSelectedUserListener) {
        this.mOnKeyBoardSelectedUserListener = mOnKeyBoardSelectedUserListener;
    }

    public interface OnKeyBoardSelectedUserListener {
        void onSelected(User user);
    }

}

