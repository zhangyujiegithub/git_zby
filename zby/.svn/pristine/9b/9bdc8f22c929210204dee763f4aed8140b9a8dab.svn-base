package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.PinYinUtil;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.IndexBar;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Request;

/**
 * 联系人
 */

public class ContactFragment extends Fragment {

    private ListView listview_contact;
    private Context context;
    private IndexBar indexBar;
    private Toast toast;
    private List<User> users = new ArrayList<User>();
    private ORMDataHelper dataHelper;
    private DictionaryHelper dictionaryHelper;
    private Dao<User, Integer> userDao;
    private DictIosPickerBottomDialog dialog;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Collections.sort(users, new PinyinComparator());
                    listview_contact.setAdapter(getAdapter(users));
            }
            return false;
        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(v);
        getAllStaff();
        setOnEvent();
        return v;
    }


    /**
     * 获取所有员工
     */
    private void getAllStaff() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.公司员工 + Global.mUser.getDepartmentId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                users = JsonUtils.ConvertJsonToList(response, User.class);
                Logger.i("部门员工的数量：" + users.size());
                if (users != null) {
                    handler.sendEmptyMessage(1);
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

    private void setOnEvent() {
        indexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                showShortToast(s.charAt(0) + "");
                //该字母首次出现的位置
                int position = getPositionForSection(s.toLowerCase().charAt(0), users);
                if (position >= 0) {
                    listview_contact.setSelection(position);
                }
            }
        });
    }

    private void initView(View v) {
        dataHelper = ORMDataHelper.getInstance(getActivity().getApplicationContext());
        dialog = new DictIosPickerBottomDialog(getActivity());
        dictionaryHelper = new DictionaryHelper(getActivity());
        context = getActivity();
        listview_contact = v.findViewById(R.id.listView_contact);
        indexBar = (IndexBar) v.findViewById(R.id.index_bar_inner_communicate_list);
    }

    private CommanAdapter<User> getAdapter(final List<User> list) {
        return new CommanAdapter<User>(list, context, R.layout.item_workmate_list) {

            @Override
            public void convert(int position, final User item, BoeryunViewHolder viewHolder) {

                TextView tv_first = viewHolder.getView(R.id.tv_add_client_contract_sort);
                ImageView iv_call = viewHolder.getView(R.id.iv_call_phone_workmate);
                ImageView iv_message = viewHolder.getView(R.id.iv_send_message);

                viewHolder.setUserPhotoById(R.id.head_item_workmate, item);
                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                viewHolder.setTextValue(R.id.position_item_workmate, StrUtils.removeSpace(item.getPost()));
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                viewHolder.setTextValue(R.id.email_item_workmate, TextUtils.isEmpty(item.getEnterpriseMailbox()) ? "无" : item.getEnterpriseMailbox());


                //根据position获取分类的首字母的char ascii值
                int section = getSectionForPosition(position, list);
                String name = PinYinUtil.toPinyin(item.getName());
                if (TextUtils.isEmpty(name)) {
                    name = "#";
                } else {
                    if (!(name.charAt(0) >= 'a' && name.charAt(0) <= 'z')) {//第一个字符不在a-z之间
                        name = "#";
                    }
                }

                //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section, list)) {
                    tv_first.setVisibility(View.VISIBLE);
                    tv_first.setText(("" + name.charAt(0)));
                } else {
                    tv_first.setVisibility(View.GONE);
                }


                final List<String> showList = new ArrayList<>();

                if (!TextUtils.isEmpty(item.getTelephone())) {
                    showList.add(item.getTelephone());
                }
                if (!TextUtils.isEmpty(item.getMobile())) {
                    showList.add(item.getMobile());
                }
                iv_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setTitle("发短信给" + item.getName());
                        dialog.show(showList);
                        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {

                                String num = "";
                                if (!TextUtils.isEmpty(item.getTelephone())) { //座机不为空，证明肯定有座机号
                                    if (index == 1) {
                                        num = item.getTelephone();
                                    }
                                    if (!TextUtils.isEmpty(item.getMobile())) {
                                        if (index == 2) {
                                            num = item.getMobile();
                                        }
                                    }
                                } else {
                                    if (index == 1) {
                                        num = item.getMobile();
                                    }
                                }
                                Uri smsToUri = Uri.parse("smsto:" + num);
                                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                                startActivity(intent);
                                dictionaryHelper.insertLatest(item);
                            }
                        });
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
                                if (index != 0) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_DIAL);
                                    String num = "";
                                    if (!TextUtils.isEmpty(item.getTelephone())) { //座机不为空，证明肯定有座机号
                                        if (index == 1) {
                                            num = item.getTelephone();
                                        }
                                        if (!TextUtils.isEmpty(item.getMobile())) {
                                            if (index == 2) {
                                                num = item.getMobile();
                                            }
                                        }
                                    } else {
                                        if (index == 1) {
                                            num = item.getMobile();
                                        }
                                    }
                                    dictionaryHelper.insertLatest(item);
                                    intent.setData(Uri.parse("tel:" + num));
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });


            }


        };
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position, List<User> list) {
        String name = PinYinUtil.toPinyin(list.get(position).getName());
        if (TextUtils.isEmpty(name)) {
            name = "#";
        } else {
            if (!(name.charAt(0) >= 'a' && name.charAt(0) <= 'z')) {//第一个字符不在a-z之间
                name = "#";
            }
        }
        return name.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section, List<User> list) {
        for (int i = 0; i < list.size(); i++) {
            String sortStr = PinYinUtil.toPinyin(list.get(i).getName());
            if (TextUtils.isEmpty(sortStr)) {
                sortStr = "#";
            } else {
                if (!(sortStr.charAt(0) >= 'a' && sortStr.charAt(0) <= 'z')) {//第一个字符不在a-z之间
                    sortStr = "#";
                }
            }
            char firstChar = sortStr.charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 按照名字将联系人排序
     */

    public class PinyinComparator implements Comparator<User> {

        public int compare(User o1, User o2) {

            String name1 = PinYinUtil.toPinyin(o1.getName());
            String name2 = PinYinUtil.toPinyin(o2.getName());
            if (TextUtils.isEmpty(name1)) {
                name1 = "#";
            }
            if (TextUtils.isEmpty(name2)) {
                name2 = "#";
            }
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if ((String.valueOf(name1.charAt(0))).equals("#")) {
                return 0;
            } else if (String.valueOf(name2.charAt(0)).equals("#")) {
                return 1;
            } else {
                return ("" + name1.charAt(0)).compareTo(("" + name2.charAt(0)));
            }
        }
    }

    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }


}
