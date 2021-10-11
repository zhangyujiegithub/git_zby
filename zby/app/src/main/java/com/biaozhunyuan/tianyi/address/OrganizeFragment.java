package com.biaozhunyuan.tianyi.address;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.chatLibary.chat.ChartIntentUtils;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/9/1.
 * 通讯录页面——组织列表
 * 废弃，使用OrganizeFragment2 替代
 * 该列表会出现 如果部门下面有部门和员工的情况下，不显示员工的问题。
 * use {@link OrganizationFragment}
 */


@Deprecated
public class OrganizeFragment extends Fragment {

    private List<Organize> allDept = new ArrayList<Organize>();
    private List<Organize> myDept = new ArrayList<Organize>();
    private List<Organize> showDept = new ArrayList<Organize>();

    private NoScrollListView lv_myDept;
    private NoScrollListView lv_allDept;

    private Dao<Latest, Integer> latestDao;

    private long lastClickTime;
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
        View view = inflater.inflate(R.layout.fragment_organize, null);
        initViews(view);
        getAllDept();
        return view;
    }

    private void initViews(View view) {
        dataHelper = ORMDataHelper.getInstance(getActivity());
        preferencesHelper = new SharedPreferencesHelper(getActivity());
        dialog = new DictIosPickerBottomDialog(getActivity());
        latestDao = dataHelper.getLatestDao();
        dictionaryHelper = new DictionaryHelper(getActivity());
        lv_myDept = (NoScrollListView) view.findViewById(R.id.lv_my_dept_organize);
        lv_allDept = (NoScrollListView) view.findViewById(R.id.lv_all_dept_organize);

        other_dept = (TextView) view.findViewById(R.id.tv_other_dept_organize);
        ll_dept = (LinearLayout) view.findViewById(R.id.ll_dept_names_organize);
        tv_myDept = (TextView) view.findViewById(R.id.tv_back_first_organize);
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
                if (!isUserList) {
                    isRoot = false;
                    for (TextView tv : textViewList) {
                        tv.setTextColor(getResources().getColor(R.color.hanyaRed));
                    }
                    final TextView tv = new TextView(getActivity()); //动态添加部门名称
                    tv.setTextColor(getResources().getColor(R.color.text_info));
                    tv.setTextSize(14);
                    tv.setText("-->" + showDept.get(position).getName());
                    tv.setTag(showDept.get(position).getUuid());
                    textViewList.add(tv);
                    ll_dept.addView(tv);
                    isRoot();
                    if (isLastDept(allDept, showDept.get(position).getUuid())) {
                        isUserList = true;
                        showUsers = dictionaryHelper.getStaffByDeptId(showDept.get(position).getUuid());
                        lv_allDept.setAdapter(getUserAdapter(showUsers));
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
                            lv_allDept.setAdapter(getAdapter(showDept));

                            List<TextView> removeList = textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()); //移除点击的textview后面的view
                            for (int i = 0; i < removeList.size(); i++) {
                                ll_dept.removeView(removeList.get(i));
                            }
                            textViewList.removeAll(removeList);

                            textViewList.subList(textViewList.indexOf(tv) + 1, textViewList.size()).clear();
                        }
                    });
                } else {  //如果是最后一级，员工列表
                    User user = showUsers.get(position);
                    ChartIntentUtils.startChatInfo(getActivity(), user.getUuid());
                    dictionaryHelper.insertLatest(user);
                }
            }
        });


        lv_myDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getStaffById(myDept.get(position).getUuid());
                showUsers = dictionaryHelper.getStaffByDeptId(myDept.get(position).getUuid());
                lv_allDept.setAdapter(getUserAdapter(showUsers));
                isUserList = true;
                isRoot = false;
                isRoot();
                tv_myDept.setTextColor(getResources().getColor(R.color.hanyaRed));
                final TextView tv = new TextView(getActivity()); //动态添加部门名称
                tv.setTextColor(getResources().getColor(R.color.text_info));
                tv.setTextSize(14);
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
     * 获取部门列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<Organize> getAdapter(List<Organize> list) {
        return new CommanAdapter<Organize>(list, getActivity(), R.layout.item_organize) {
            @Override
            public void convert(int position, Organize item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_organize, item.getName());
                viewHolder.setTextValue(R.id.tv_staff_count_item_organize, "(" + item.getStaffNumber() + ")");

            }
        };
    }

    /**
     * 根据部门id获取部门下属员工
     *
     * @param deptId
     */
    private void getStaffById(String deptId) {

        //flag=1 获取在职员工，flag=4获取所有员工
        String url = Global.BASE_JAVA_URL + GlobalMethord.部门员工 + "?id=" + deptId + "&flag=1";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<User> users = null;
                try {
                    users = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), User.class);
                    lv_allDept.setAdapter(getUserAdapter(users));
                } catch (JSONException e) {
                    e.printStackTrace();
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


    /**
     * 获取员工列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<User> getUserAdapter(List<User> list) {
        return new CommanAdapter<User>(list, getActivity(), R.layout.item_workmate_list) {

            @Override
            public void convert(int position, final User item, BoeryunViewHolder viewHolder) {
                final List<String> showList = new ArrayList<>();

                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                TextView tvPosition = viewHolder.getView(R.id.position_item_workmate);//职位
                if (TextUtils.isEmpty(item.getPost())) {
                    tvPosition.setVisibility(View.GONE);
                } else {
                    tvPosition.setVisibility(View.VISIBLE);
                    tvPosition.setText(StrUtils.removeSpace(item.getPost()));
                }

                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
                viewHolder.setUserPhoto(R.id.head_item_workmate, item.getUuid());
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                if (Global.CURRENT_CROP_NAME.equals("天立化工")) {
                    if (!TextUtils.isEmpty(item.getPhoneExt())) {
                        viewHolder.setTextValue(R.id.tel_item_workmate, item.getPhoneExt());
                        if (!TextUtils.isEmpty(item.getPhoneExt())) {
                            showList.add(item.getPhoneExt());
                        }
                    } else {
                        if (!TextUtils.isEmpty(item.getMobile())) {
                            showList.add(item.getMobile());
                        }
                        viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                    }
                } else {
                    viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                    if (!TextUtils.isEmpty(item.getPhoneExt())) {
                        showList.add(item.getPhoneExt());
                    }
                    if (!TextUtils.isEmpty(item.getMobile())) {
                        showList.add(item.getMobile());
                    }
                }
                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());


                ImageView iv_call = viewHolder.getView(R.id.iv_call_phone_workmate);
                ImageView iv_message = viewHolder.getView(R.id.iv_send_message);

                iv_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showList.size() == 0) {
                            Toast.makeText(getActivity(), "没有联系方式", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.setTitle("发短信给" + item.getName());
                            dialog.show(showList);
                            dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                @Override
                                public void onSelected(int index) {

                                    String num = showList.get(index - 1);
//                                if(num.equals(StrUtils.pareseNull(item.getPhoneExt()))){
//                                    Toast.makeText(getActivity(),"该号码不支持接收短信",Toast.LENGTH_SHORT).show();
//                                } else {
                                    Uri smsToUri = Uri.parse("smsto:" + num);
                                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                                    startActivity(intent);
                                    dictionaryHelper.insertLatest(item);
//                                }
//                                if (!TextUtils.isEmpty(item.getTelephone())) { //座机不为空，证明肯定有座机号
//                                    if (index == 1) {
//                                        num = item.getTelephone();
//                                    }
//                                    if (!TextUtils.isEmpty(item.getMobile())) {
//                                        if (index == 2) {
//                                            num = item.getMobile();
//                                        }
//                                    }
//                                } else {
//                                    if (index == 1) {
//                                        num = item.getMobile();
//                                    }
//                                }

                                }
                            });
                        }
                    }
                });


                //弹出打电话的弹出框
                iv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setTitle("联系" + item.getName());
                        dialog.show(showList);

                        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_DIAL);
//                                if (index != 0) {
//                                    String num = "";
//                                    if (!TextUtils.isEmpty(item.getTelephone())) { //座机不为空，证明肯定有座机号
//                                        if (index == 1) {
//                                            num = item.getTelephone();
//                                        }
//                                        if (!TextUtils.isEmpty(item.getMobile())) {
//                                            if (index == 2) {
//                                                num = item.getMobile();
//                                            }
//                                        }
//                                    } else {
//                                        if (index == 1) {
//                                            num = item.getMobile();
//                                        }
//                                    }
//
//                                }
                                intent.setData(Uri.parse("tel:" + showList.get(index - 1)));
                                dictionaryHelper.insertLatest(item);
                                //添加到最近联系人
                                startActivity(intent);
                            }
                        });
                    }
                });


            }
        };
    }


    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
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
