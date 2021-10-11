package com.biaozhunyuan.tianyi.wechat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.chatLibary.chat.ChartIntentUtils;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.wechat.model.RecentContactStaff;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/9/7.
 * 最近联系人页面
 */

public class RecentWeChatFragment extends Fragment {

    private ListView lv;
    private ImageView no_recent;

    private ORMDataHelper dataHelper;
    private Dao<Latest, Integer> latestDao;
    private DictionaryHelper dictionaryHelper;
    private boolean isShowWeRecord = false;

    private List<Latest> latests = new ArrayList<>();
    private List<User> datas = new ArrayList<>();
    private DictIosPickerBottomDialog dialog;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_contacts, null);
        initViews(view);
        initData();
        getRecentWeChatStaff();
        setOnEvent();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*try {
            latests = latestDao.queryForAll();
            Collections.reverse(latests);
            if (latests != null && latests.size() > 0) {
                no_recent.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(getUserAdapter(latests));
            } else {
                no_recent.setVisibility(View.VISIBLE);
                lv.setVisibility(View.GONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void initViews(View view) {
        lv = (ListView) view.findViewById(R.id.lv_recent_contacts);
        no_recent = (ImageView) view.findViewById(R.id.iv_no_recent_contacts);
    }

    private void initData() {
        dataHelper = ORMDataHelper.getInstance(getActivity());
        dialog = new DictIosPickerBottomDialog(getActivity());
        latestDao = dataHelper.getLatestDao();
        dictionaryHelper = new DictionaryHelper(getActivity());
    }

    public void setIsShowWeRecord(boolean isShowWeRecord) {
        this.isShowWeRecord = isShowWeRecord;
    }

    private void setOnEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) lv.getAdapter().getItem(position);
                if (isShowWeRecord) {
                    Intent intent = new Intent(getActivity(), WeChatContactListActivity.class);
                    intent.putExtra("UserId", user.getUuid());
                    startActivity(intent);
                } else {
                    ChartIntentUtils.startChatInfo(getActivity(), user.getUuid());
                    dictionaryHelper.insertLatest(user);
                }
            }
        });
    }

    /**
     * 获取最近和联系人聊天的员工
     */
    private void getRecentWeChatStaff() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.最近微信聊天员工;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<RecentContactStaff> staffs = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), RecentContactStaff.class);
                if (staffs != null && staffs.size() > 0) {
                    for (RecentContactStaff staff : staffs) {
                        User user = dictionaryHelper.getUser(staff.getStaffId());
                        datas.add(user);
                    }
                    lv.setAdapter(getUserAdapter(datas));
                }
                if (datas.size() > 0) {
                    no_recent.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setAdapter(getUserAdapter(datas));
                } else {
                    no_recent.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
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
                final List<String> showList = new ArrayList<String>();

                if (isShowWeRecord) {
                    viewHolder.getView(R.id.iv_send_message).setVisibility(View.GONE);
                    viewHolder.getView(R.id.iv_call_phone_workmate).setVisibility(View.GONE);
                    viewHolder.getView(R.id.imageView8).setVisibility(View.GONE);
                }

                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                TextView tvPosition = viewHolder.getView(R.id.position_item_workmate);//职位
                if (TextUtils.isEmpty(item.getPost())) {
                    tvPosition.setVisibility(View.GONE);
                } else {
                    tvPosition.setVisibility(View.VISIBLE);
                    tvPosition.setText(StrUtils.removeSpace(item.getPost()));
                }
//                viewHolder.setTextValue(R.id.email_item_workmate, item.getEmail());
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
                ImageView iv_message = viewHolder.getView(R.id.iv_send_message);

                ImageView iv_call = viewHolder.getView(R.id.iv_call_phone_workmate);

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
                        if (showList.size() == 0) {
                            Toast.makeText(getActivity(), "没有联系方式", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.setTitle("联系" + item.getName());
                            dialog.show(showList);

                            dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                @Override
                                public void onSelected(int index) {
                                    if (index != 0) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_DIAL);
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
                                        intent.setData(Uri.parse("tel:" + showList.get(index - 1)));
                                        dictionaryHelper.insertLatest(item);
                                        //添加到最近联系人
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });


            }
        };
    }
}
