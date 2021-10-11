package com.biaozhunyuan.tianyi.notice;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 可选中联系人的最近联系人界面
 */

public class SelectedRecentContactsFragment extends LazyFragment {

    private ListView lv;
    private ImageView no_recent;
    private ORMDataHelper dataHelper;
    private Dao<Latest, Integer> latestDao;
    private DictionaryHelper dictionaryHelper;
    private List<Latest> latests = new LinkedList<>();
    private SelectedNotifierActivity notifierActivity;
    private List<User> selectUsers = new ArrayList<>();
    private List<User> unClickableUsers = new ArrayList<>();
    private List<User> currentUsers = new ArrayList<>();
    private boolean isSingleSelect = false;
    private CommanAdapter<Latest> adapter;
    public static boolean isResume = false;
    private String selectedAdvisorIds = ""; //已选择的员工id
    private String selectedAdvisorNames = ""; //已选择的员工名称
    private static boolean isFirst = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.select_recentcontact, null);
        isFirst = true;
        initViews(v);
        getIntentData();
        initData();
        setOnEvent();
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            notifyList();
        }
    }

    public void notifyList() {
        if (notifierActivity == null) {
            return;
        }
        selectUsers = notifierActivity.getUserList();
        for (Latest user : latests) {
            if (selectUsers.size() > 0) {
                user.setSelected(false);
                for (User u : selectUsers) {
                    if (user.getUuid().equals(u.getUuid())) {
                        user.setSelected(true);
                    }
                }
            } else {
                user.setSelected(false);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = getUserAdapter(latests);
            lv.setAdapter(adapter);
        }
    }

    private void initViews(View view) {
        isSingleSelect = getActivity().getIntent().getBooleanExtra("isSingleSelect", true);
        lv = (ListView) view.findViewById(R.id.lv_recent_contacts);
        no_recent = (ImageView) view.findViewById(R.id.iv_no_recent_contacts);
    }

    private void initData() {
        dataHelper = ORMDataHelper.getInstance(getActivity());
        latestDao = dataHelper.getLatestDao();
        dictionaryHelper = new DictionaryHelper(getActivity());
    }


    private void setOnEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Latest lt = latests.get(position);
                if (!lt.isClickable()) {
                    return;
                }
                lt.setSelected(!lt.isSelected());
                User user = ViewHelper.turnUser(lt);
                if (user.isSelected()) {
                    notifierActivity.addSelected(user);
                } else {
                    notifierActivity.removeSelected(user);
                }
                adapter.notifyDataSetChanged();
                if (isSingleSelect) {
                    notifierActivity.returnResult();
                }
                dictionaryHelper.insertLatest(user);
            }
        });
    }


    /**
     * 获取员工列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<Latest> getUserAdapter(List<Latest> list) {
        return new CommanAdapter<Latest>(list, getActivity(), R.layout.item_select_infrom) {

            @Override
            public void convert(int position, final Latest item, BoeryunViewHolder viewHolder) {
                //设置条目数据
                viewHolder.setTextValue(R.id.name, item.getName());
                viewHolder.setTextValue(R.id.position_item_workmate, item.getPost());
                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
                viewHolder.setUserPhoto(R.id.head_item_workmate, item.getUuid());
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                if (Global.CURRENT_CROP_NAME.equals("天立化工")) {
                    if (!TextUtils.isEmpty(item.getPhoneExt())) {
                        viewHolder.setTextValue(R.id.tel_item_workmate, item.getPhoneExt());
                    } else {
                        viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                    }
                } else {
                    viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                }
                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());
                ImageView ivSelect = viewHolder.getView(R.id.choose_item_select_user);
                final List<String> showList = new ArrayList<>();

                if (!TextUtils.isEmpty(item.getTelephone())) {
                    showList.add(item.getTelephone());
                }
                if (!TextUtils.isEmpty(item.getMobile())) {
                    showList.add(item.getMobile());
                }

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


            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        notifierActivity = (SelectedNotifierActivity) getActivity();

        unClickableUsers = notifierActivity.getUnClickAbleUsers();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            try {
                List<User> selectUsers = notifierActivity.getUserList();
                latests = latestDao.queryForAll();
                for (Latest lt : latests) {
                    for (User selectUser : selectUsers) {
                        if (lt.getUuid().equals(selectUser.getUuid())) {
                            lt.setSelected(true);
                        }
                    }
                    for (User unClickableUser : unClickableUsers) {
                        if (lt.getUuid().equals(unClickableUser.getUuid())) {
                            lt.setClickable(false);
                        }
                    }
                }
                Collections.reverse(latests);
                if (latests != null && latests.size() > 0) {
                    no_recent.setVisibility(View.GONE);
                    adapter = getUserAdapter(latests);
                    lv.setAdapter(adapter);
                } else {
                    no_recent.setVisibility(View.VISIBLE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            for (Latest u : latests) {
                u.setSelected(false);
            }
        }
    }

}
