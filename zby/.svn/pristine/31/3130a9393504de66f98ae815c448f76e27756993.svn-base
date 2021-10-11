package com.biaozhunyuan.tianyi.client;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.MyContacts;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.utils.ContactUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.utils.ToastUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.utils.PinYinUtil;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.IndexBar;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import okhttp3.Request;

/**
 * 从通讯录导入客户
 */
public class ImportCustomerActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private ListView lv;
    private IndexBar indexBar;
    private BoeryunSearchView searchView;


    private Context mContext;
    private List<MyContacts> mList;
    private List<MyContacts> searchList = new ArrayList<>();
    private CommanAdapter<MyContacts> adapter;
    private 动态表单ViewModel formModel;
    private int currindex = 0;
    private List<MyContacts> selectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_customer);
        initViews();
        initData();
        setOnEvent();
    }

    private void initViews() {
        headerView = findViewById(R.id.headerview);
        lv = (ListView) findViewById(R.id.listView_address_worker_list);
        indexBar = (IndexBar) findViewById(R.id.index_bar_inner_communicate_list);
        searchView = findViewById(R.id.search_view);

    }

    private void initData() {
        mContext = ImportCustomerActivity.this;
        mList = ContactUtils.getAllContacts(this);
        if (mList != null) {
            Collections.sort(mList, new PinyinComparator());
            adapter = getAdapter(mList);
            lv.setAdapter(adapter);
        }
    }


    /**
     * 判断是否可以新建客户
     */
    private void isCanCreateCustomer(MyContacts contacts) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.判断是否可以新建客户;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.pareseData(response);
                    String isCreatable = JsonUtils.getStringValue(data, "isCreatable");
                    if ("true".equals(isCreatable)) {
                        if (formModel != null) {
                            addCustomer(contacts);
                        } else {
                            getTableGraid(contacts);
                        }
                    } else {
                        showShortToast("拥有客户数据已达到最大限制");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (formModel != null) {
                        addCustomer(contacts);
                    } else {
                        getTableGraid(contacts);
                    }
                }


            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                if (formModel != null) {
                    addCustomer(contacts);
                } else {
                    getTableGraid(contacts);
                }
            }
        });
    }

    private void getTableGraid(MyContacts contacts) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态字段;

        Map<String, String> defaultValuesJSONString = new HashMap<>();
        defaultValuesJSONString.put("lastUpdateTime", ViewHelper.getCurrentFullTime());
        defaultValuesJSONString.put("createTime", ViewHelper.getCurrentFullTime());
        defaultValuesJSONString.put("giveupTime", ViewHelper.getCurrentFullTime());
        defaultValuesJSONString.put("creatorId", Global.mUser.getUuid());
        defaultValuesJSONString.put("advisorId", Global.mUser.getUuid());
        defaultValuesJSONString.put("category", "caef186cd6a44abe9052c72229d2060c");

        Map<String, String> map = new HashMap<>();
        Gson gson = new Gson();
        map.put("type", "crm_customer");
        map.put("id", "0");
        map.put("defaultValuesJSONString", gson.toJson(map));

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                try {
                    formModel = JsonUtils.jsonToEntity(
                            result, 动态表单ViewModel.class);
                    if (formModel != null) {
                        addCustomer(contacts);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void addCustomer(MyContacts contacts) {
        ProgressDialogHelper.show(mContext, "导入中...");
        boolean isRepeat = false;
        for (表单字段 表单字段 : formModel.表单字段s) {
            if ("name".equals(表单字段.Name)) {
                表单字段.Value = contacts.getName();
            } else if ("contact".equals(表单字段.Name)) {
                表单字段.Value = contacts.getName();
            } else if ("mobile".equals(表单字段.Name) || "phone".equals(表单字段.Name)) {
                表单字段.Value = StrUtils.removeSpace(contacts.getPhone());
            }
            if (表单字段.Required && TextUtils.isEmpty(表单字段.Value)) {
                if ("string".equals(表单字段.DataType)) {
                    表单字段.Value = "-";
                } else if ("number".equals(表单字段.DataType)) {
                    表单字段.Value = "0";
                } else if ("datetime".equals(表单字段.DataType)) {
                    表单字段.Value = ViewHelper.getCurrentFullTime();
                } else if ("dropdown".equals(表单字段.DataType)
                        || "mutiSelect".equals(表单字段.DataType)
                        || "combobox".equals(表单字段.DataType)) {
                    表单字段.Value = "111";
                }
            }
            if ("mobile".equals(表单字段.Name) && 表单字段.UniqueField) {//判断字段是否重复
                isRepeat = isFieldRepeat(表单字段);
            }
        }
        if (!isRepeat) {
            saveCustomer();
        }
    }

    /**
     * 判断字段是否重复
     */
    private boolean isFieldRepeat(表单字段 field) {
        CountDownLatch latch = new CountDownLatch(1);
        final boolean[] isRepeat = {false};


        CheckOutRepetition checkOutRepetition = new CheckOutRepetition();
        checkOutRepetition.setFieldName(field.Name);
        checkOutRepetition.setId("0");
        checkOutRepetition.setTableName("crm_customer");
        checkOutRepetition.setValue(StrUtils.removeSpace(field.Value));

        String url = Global.BASE_JAVA_URL + GlobalMethord.校验动态字段重复;
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringRequest.postSync(url, checkOutRepetition, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        String data = JsonUtils.pareseData(response);
                        if ("0".equals(data)) {
                            isRepeat[0] = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort(field.DisplayName + "已存在");
                                }
                            });
                            ProgressDialogHelper.dismiss();
                        }
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {

                    }

                    @Override
                    public void onResponseCodeErro(String result) {
                        latch.countDown();
                    }
                });
            }
        }).start();


        try {
            latch.await();//等到计数为0时才会继续执行下去
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isRepeat[0];
    }

    private void saveCustomer() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.保存动态字段;
        StringRequest.postAsynNoMap(url, "crm_customer", formModel.表单字段s, new StringResponseCallBack() {
            @Override
            public void onResponseCodeErro(String result) {
                ToastUtils.showShort(JsonUtils.pareseMessage(result));
            }

            @Override
            public void onResponse(String response) {
                currindex += 1;
                if (currindex == selectList.size()) {
                    ToastUtils.showShort("导入成功");
                    ProgressDialogHelper.dismiss();
                    selectList.clear();
                    currindex = 0;
                    for (MyContacts myContacts : mList) {
                        myContacts.setSelected(false);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }
        });
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (selectList != null && selectList.size() > 0) {
                    for (MyContacts contacts : selectList) {
                        isCanCreateCustomer(contacts);
                    }
                } else {
                    showShortToast("请选择要导入的联系人");
                }
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

        indexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                showShortToast(s.charAt(0) + "");
                //该字母首次出现的位置
                int position = getPositionForSection(s.toLowerCase().charAt(0), mList);
                if (position >= 0) {
                    lv.setSelection(position);
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyContacts item = adapter.getItem(position);
                item.setSelected(!item.isSelected());
                if (item.isSelected()) {
                    selectList.add(item);
                } else {
                    selectList.remove(item);
                }
                adapter.notifyDataSetChanged();
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                searchList.clear();
                if (!TextUtils.isEmpty(str)) {
                    for (MyContacts contacts : mList) {
                        if (contacts.getName().contains(str)) {
                            searchList.add(contacts);
                        }
                    }
                    adapter = getAdapter(searchList);
                    lv.setAdapter(adapter);
                } else {
                    adapter = getAdapter(mList);
                    lv.setAdapter(adapter);
                }
            }
        });

    }


    private CommanAdapter<MyContacts> getAdapter(final List<MyContacts> list) {
        return new CommanAdapter<MyContacts>(list, mContext, R.layout.item_import_customer) {

            @Override
            public void convert(int position, final MyContacts item, BoeryunViewHolder viewHolder) {

                TextView tv_first = viewHolder.getView(R.id.tv_add_client_contract_sort);


                viewHolder.setUserPhotoById("", item.getName(), R.id.head_item_workmate);
                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getPhone()) ? "无" : item.getPhone());

                if (item.isSelected()) {
                    viewHolder.setImageResoure(R.id.iv_send_message, R.drawable.select_on);
                } else {
                    viewHolder.setImageResoure(R.id.iv_send_message, R.drawable.select_off);
                }

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


            }


        };
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position, List<MyContacts> list) {
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
    public int getPositionForSection(int section, List<MyContacts> list) {
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

    public class PinyinComparator implements Comparator<MyContacts> {

        public int compare(MyContacts o1, MyContacts o2) {

            String name1 = PinYinUtil.toPinyin(o1.getName());
            String name2 = PinYinUtil.toPinyin(o2.getName());
            if (TextUtils.isEmpty(name1)) {
                name1 = "#";
            }
            if (TextUtils.isEmpty(name2)) {
                name2 = "#";
            }
            String a = "" + name1.charAt(0);
            String b = "" + name2.charAt(0);
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if ((String.valueOf(name1.charAt(0))).equals("#")) {
                return 1;
            } else if (String.valueOf(name2.charAt(0)).equals("#")) {
                return -1;
            } else {
//                return ("" + name1.charAt(0)).compareTo(("" + name2.charAt(0)));
                return a.equals(b) ? 0 : (a.compareTo(b) > 0 ? 1 : -1);
            }
        }
    }

}
