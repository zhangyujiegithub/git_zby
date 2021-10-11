package com.biaozhunyuan.tianyi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS_STAFF_VIEW;

/**
 * 列表布局展示最近指派/选择员工头像,点击返回User对象
 */

public class ListRecentlySelectedStaffView extends RelativeLayout {

    private Context mContext;
    private View mParentView;
    private DictionaryHelper dictionaryHelper;
    private ORMDataHelper dataHelper;
    private List<User> AllStaffList; //全体员工的集合
    private User mUser;
    private OnKeyBoardSelectedUserListener mOnKeyBoardSelectedUserListener; //员工接口回调
    //    private GridView gridView;
    private CommanAdapter<User> adapter; //gridview适配器
    private ListView mLv;
    private BoeryunSearchViewNoButton seachButton;
    private User currentUser = Global.mUser;
    private Button tvLookAll;
    private EditText etSearch;

    public ListRecentlySelectedStaffView(Context context) {
        super(context);
    }

    public ListRecentlySelectedStaffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        dictionaryHelper = new DictionaryHelper(mContext);
        dataHelper = ORMDataHelper.getInstance(mContext);
        AllStaffList = new ArrayList<>();
        mUser = Global.mUser;
        mUser.setSelected(true);
        mParentView = LayoutInflater.from(context).
                inflate(R.layout.view_list_recently_seletedstaff, this, true);
        initView(mParentView);
        getStaffList();
        setOnTouch();
    }

    private void setOnTouch() {
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User item = adapter.getItem(position);
                List<User> dataList = adapter.getDataList();
                for (int i = 0; i < dataList.size(); i++) {
                    User u = dataList.get(i);
                    if (StrUtils.pareseNull(u.getUuid()).equals(item.getUuid())) {
                        if (u.isSelected()) { //如果已经选中 提示最少选择一个执行人
                            break;
                        } else {
                            u.setSelected(true);
                            currentUser = u;
                            InputSoftHelper.hiddenSoftInput(getContext(), etSearch);
                            etSearch.setText("");
                            etSearch.clearFocus();
                        }
                    } else { //其余都设为未选中
                        u.setSelected(false);
                    }
                }
                getStaffList();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (!TextUtils.isEmpty(str)) {
                    getStaffByFilter(str);
                }
            }
        });


        tvLookAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", true);
                intent.putExtra("isShowRecently", false);
                intent.putExtra("title", "选择成员");
                ((Activity) mContext).startActivityForResult(intent, REQUEST_SELECT_EXCUTORS_STAFF_VIEW);
            }
        });
    }


    public ListRecentlySelectedStaffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(View view) {
        if (view != null) {
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.list_header_staffview, null);
            mLv = view.findViewById(R.id.staff_listview);
            tvLookAll = view1.findViewById(R.id.tv_lookAll);
            etSearch = view.findViewById(R.id.et_search);
            seachButton = view.findViewById(R.id.seach_button);
            seachButton.setHint("按姓名搜索");
            mLv.addFooterView(view1);
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
        getStaffList(user);
    }


    /**
     * 更新最近选择和选择选中的员工
     */
    private void getStaffList(User selectedUser) {
        currentUser = selectedUser;
        getStaffList();
    }

    /**
     * 获取部门下的所有员工和最近选择
     */
    private void getStaffList() {
        AllStaffList.clear();
        try {
            Dao<Latest, Integer> latestDao = dataHelper.getLatestDao();
            List<Latest> latests = latestDao.queryForAll();
            for (Latest latest : latests) { //先插入最近联系人
                if (!latest.getUuid().equals(mUser.getUuid()) && !latest.getUuid().equals(currentUser.getUuid())) { //自己放到最前面
                    AllStaffList.add(ViewHelper.turnUser(latest));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<User> staffMyDept = dictionaryHelper.getStaffByDeptId(mUser.getDepartmentId());
        for (User user : staffMyDept) { //再插入我的部门员工
            if (!user.getUuid().equals(mUser.getUuid()) && !user.getUuid().equals(currentUser.getUuid())) {
                //自己放到最前面 和已选中的员工
                if (!removeDuplicate(user)) {
                    AllStaffList.add(user);
                }
            }
        }
        AllStaffList.add(0, mUser);
        if (!currentUser.getUuid().equals(mUser.getUuid())) {
            AllStaffList.add(1, currentUser);
        }
        setSelectedUser(AllStaffList, currentUser);
        generationTaskCompletionView(AllStaffList, currentUser);
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
        return isRePeat;
    }


    /**
     * 生成头像view添加到父容器中
     *
     * @param userList
     */
    private void generationTaskCompletionView(List<User> userList, User selectedUser) {
        adapter = getTaskAdapter(userList);
        mLv.setAdapter(adapter);
        dictionaryHelper.insertLatest(selectedUser);
        addKeyBoardSelectedUserListener(selectedUser);
    }


    private CommanAdapter<User> getTaskAdapter(List<User> userList) {
        return new CommanAdapter<User>(userList, mContext, R.layout.item_select_executor_list) {

            @Override
            public void convert(int position, final User item, final BoeryunViewHolder viewHolder) {
                TextView tvName = viewHolder.getView(R.id.tv_name_avatar);
                viewHolder.getView(R.id.ll_task_item_parent).setVisibility(VISIBLE);
                viewHolder.setUserPhotoById(R.id.circularAvatar_input_user, item);
                tvName.setText(item.getName());
                if (item.isSelected()) { //判断是否选中
                    viewHolder.getView(R.id.iv_select).setVisibility(VISIBLE);
//                        selectIv.setBackgroundResource(R.drawable.ic_select);
                } else {
                    viewHolder.getView(R.id.iv_select).setVisibility(GONE);
//                        selectIv.setBackgroundResource(R.drawable.ic_cancle_select);
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
    public void setOnKeyBoardSelectedUserListener(ListRecentlySelectedStaffView.OnKeyBoardSelectedUserListener mOnKeyBoardSelectedUserListener) {
        this.mOnKeyBoardSelectedUserListener = mOnKeyBoardSelectedUserListener;
    }

    public interface OnKeyBoardSelectedUserListener {
        void onSelected(User user);
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
                List<User> users = JsonUtils.ConvertJsonToList(response, User.class);
                if (users != null && users.size() > 0) {
                    setSelectedUser(users, currentUser);
                    adapter = getTaskAdapter(users);
                    mLv.setAdapter(adapter);
                } else {
                    adapter.clearData();
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

    private void setSelectedUser(List<User> list, User user) {
        for (User u : list) {
            if (StrUtils.pareseNull(u.getUuid()).equals(user.getUuid())) {
                u.setSelected(true);
            } else {
                u.setSelected(false);
            }
        }
    }

    private void removeUserOnList(User user) {
        for (int i = 0; i < AllStaffList.size(); i++) {
            User user1 = AllStaffList.get(i);
            if (StrUtils.pareseNull(user1.getUuid()).equals(user.getUuid())) {
                AllStaffList.remove(user1);
            }
        }
    }

}

