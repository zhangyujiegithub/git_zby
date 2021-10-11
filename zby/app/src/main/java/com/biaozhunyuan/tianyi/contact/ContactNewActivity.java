package com.biaozhunyuan.tianyi.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.attendance.BaiduPlace;
import com.biaozhunyuan.tianyi.attendance.LocationListActivity;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.ClientContactListFragment;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.BDLocationHelper;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/10/11.
 * 新建联系记录页面
 */

public class ContactNewActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    //    private TextView tv_time;
//    private CircleImageView iv_head;
//    private TextView tv_name;
    private EditText et_content;
    private EditText et_client;
    private EditText tv_contact_time;
    private EditText next_contact_time;
    private EditText next_contact_content; //下次联系内容
    private LinearLayout ll_next_contact_content; //下次联系内容
    private TextView tv_location;
    private MultipleAttachView attachView;

    private DictionaryHelper helper;
    private final int REQUEST_SELECT_CLIENT = 1;
    public static final int SELECT_LOCATION_CODE = 1101; // 选择地址-0
    private Contact mContact = new Contact();
    private List<ContactStatus> status;
    private List<String> statusName = new ArrayList<String>();
    private DictIosPickerBottomDialog dialog;
    private TimePickerView pickerView;
    private TagFlowLayout flowLayout;
    final String[] mAreaTypeArrs = new String[]{"潜在客户", "意向客户", "成交客户"};
    final String[] selectContents = new String[]{"签订合同", "项目在开发阶段", "客户验收"};
    private TagAdapter<String> tagAdapter;//标签适配器


    public static final String EXTRA_CLIENT_NAME = "ClientInfoActivity_clientName";
    public static final String EXTRA_CLIENT_ID = "ClientInfoActivity_id";
    private String time = "";
    private LinearLayout ll_status;
    private TextView tv_status;
    private static final String 潜在客户 = "32906b1d67b9430e8b4d43260241d978";
    private static final String 意向客户 = "d76e717a839c4b13ad252af76938be91";
    private static final String 成交客户 = "f945c18627634ce18a979601f2fde16d";
    private String client_status[] = {"潜在客户", "意向客户", "成交客户"};
    private String client_status1[] = {潜在客户, 意向客户, 成交客户};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        helper = new DictionaryHelper(getBaseContext());
        dialog = new DictIosPickerBottomDialog(ContactNewActivity.this);
        getStatusName();
        initViews();
        initData();
        addFlowContent();
        initIntentData();
        setOnEvent();

        String[] permission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA};
        PermissionsUtil.requestPermission(getBaseContext(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {

            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, permission);
    }

    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            String clientName = getIntent().getStringExtra(EXTRA_CLIENT_NAME);
            mContact.setCustomerId(getIntent().getStringExtra(EXTRA_CLIENT_ID));

            et_client.setText(clientName);

//            if (!TextUtils.isEmpty(clientName) && !TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_CLIENT_ID))) {
//                et_client.setEnabled(false);
//            }
            if (getIntent().getExtras().getSerializable("contactInfo") != null) {  //跟进记录详情
                mContact = (Contact) getIntent().getExtras().getSerializable("contactInfo");
                if (mContact != null) {
                    et_content.setText(mContact.getContent());
                    et_client.setText(mContact.getCustomerName());
                    tv_status.setText(mContact.getStageName());
                    next_contact_content.setText(mContact.getNextContactContent());
                    tv_contact_time.setText(mContact.getContactTime());
                    next_contact_time.setText(mContact.getNextContactTime());
                    tv_location.setText(mContact.getAddress());
                    attachView.loadImageByAttachIds(mContact.getAttachment());
                    getStatusNameById();
                    if (mContact.getAdvisorId().equals(Global.mUser.getUuid())) { //当前用户是新建联系人
                        et_client.setEnabled(true);
                        et_content.setEnabled(true);
                        tv_contact_time.setEnabled(true);
                        tv_location.setEnabled(true);
                        attachView.setIsAdd(true);
                        next_contact_time.setEnabled(true);
//                        headerView.ivSave.setVisibility(View.VISIBLE);
                    } else {
                        ll_next_contact_content.setVisibility(View.GONE);
                        et_client.setEnabled(false);
                        et_content.setEnabled(false);
                        tv_contact_time.setEnabled(false);
                        tv_location.setEnabled(false);
                        attachView.setIsAdd(false);
                        next_contact_time.setEnabled(false);
//                        headerView.ivSave.setVisibility(View.GONE);
                        headerView.setTitle("跟进记录详情");
                    }
                }
            } else {
                requestLocation();
            }
        } else {
            requestLocation();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_CLIENT:
                    if (data != null && data.getExtras() != null) {
                        Client client = (Client) data.getExtras().getSerializable("clientInfo");
                        if (client != null) {
                            mContact.setCustomerId(client.getUuid());
                            et_client.setText(client.getName());
                        }
                    }
                    break;
                case SELECT_LOCATION_CODE:
                    BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
                            requestCode, data);
                    if (place != null) {
                        mContact.setAddress(place.name + " (" + place.address + ")");
                        mContact.setLat(place.location.lat + "");
                        mContact.setLng(place.location.lng + "");
                        tv_location.setText(StrUtils.pareseNull(mContact.getAddress()));
                    }
                    break;
            }
        }
        attachView.onActivityiForResultImage(requestCode,
                resultCode, data);
    }

    private void initViews() {
        ll_status = findViewById(R.id.ll_status);
        tv_status = findViewById(R.id.tv_status);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_new_contact);
        tv_location = (TextView) findViewById(R.id.tvLocation_newconstact);
        et_client = (EditText) findViewById(R.id.etClientName_newconstact1);
        et_content = (EditText) findViewById(R.id.editTextContent_constact1);
        tv_contact_time = (EditText) findViewById(R.id.etContactStatus_newconstact1);
        flowLayout = findViewById(R.id.flow_view);
        next_contact_time = (EditText) findViewById(R.id.tv_new_client_contact_project);
        attachView = (MultipleAttachView) findViewById(R.id.attach_add_constact);
        next_contact_content = findViewById(R.id.et_next_contact_content);
        ll_next_contact_content = findViewById(R.id.ll_next_contact_content);

        attachView.loadImageByAttachIds("");
        attachView.setIsAdd(true);
        flowLayout.setVisibility(View.VISIBLE);

    }

    private void addFlowContent() {
        tagAdapter = new TagAdapter<String>(selectContents) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                final TextView tv = (TextView) getLayoutInflater().inflate(R.layout.flag_adapter,
                        flowLayout, false);
                tv.setText(s);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_content.setText(et_content.getText().toString() + tv.getText().toString());
                    }
                });
                return tv;
            }
        };
        flowLayout.setAdapter(tagAdapter);
    }


    private void initData() {
        pickerView = new TimePickerView(ContactNewActivity.this, TimePickerView.Type.ALL);
        pickerView.setTime(new Date());
        pickerView.setCyclic(true);
        pickerView.setCancelable(true);

    }

    private void requestLocation() {
        BDLocationHelper bdLocationHelper = new BDLocationHelper(getBaseContext());
        bdLocationHelper
                .setOnReceivedLocationListener(new BDLocationHelper.OnReceivedLocationListerner() {
                    @Override
                    public void onReceived(String mLoc, double mLong,
                                           double mLati) {
                        mContact.setAddress(mLoc);
                        mContact.setLng(mLong + "");
                        mContact.setLat(mLati + "");
                        tv_location.setText(mLoc);
                    }
                });
    }


    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                mContact.setAdvisorId(Global.mUser.getUuid());
                mContact.setContactTime(tv_contact_time.getText().toString().trim());
                mContact.setContent(et_content.getText().toString().trim());
                mContact.setNextContactContent(next_contact_content.getText().toString().trim());
                if (TextUtils.isEmpty(mContact.getContent())) {
                    showShortToast("内容不能为空");
                    return;
                } else if (TextUtils.isEmpty(mContact.getCustomerId())) {
                    showShortToast("客户不能为空");
                    return;
                } else if (TextUtils.isEmpty(mContact.getContactTime())) {
                    showShortToast("联系时间不能为空");
                    return;
                }
                if (!TextUtils.isEmpty(mContact.getNextContactTime())) {
                    if (TextUtils.isEmpty(mContact.getNextContactContent())) {
                        showShortToast("下次联系内容不能为空");
                        return;
                    }
                }
                if (!TextUtils.isEmpty(mContact.getNextContactContent())) {
                    if (TextUtils.isEmpty(mContact.getNextContactTime())) {
                        showShortToast("下次联系时间不能为空");
                        return;
                    }
                }
                if (!TextUtils.isEmpty(mContact.getStage())) {
                    if (TextUtils.isEmpty(mContact.getStageName())) {
                        showShortToast("客户阶段不能为空");
                        return;
                    }
                }


                if (!TextUtils.isEmpty(mContact.getNextContactContent()) && !TextUtils.isEmpty(mContact.getNextContactTime())) {
                    mContact.setHasNextContact(true);
                } else {
                    mContact.setHasNextContact(false);
                }
                ProgressDialogHelper.show(ContactNewActivity.this, "保存中");
                attachView.uploadImage("contacts", new IOnUploadMultipleFileListener() {
                    @Override
                    public void onStartUpload(int sum) {

                    }

                    @Override
                    public void onProgressUpdate(int completeCount) {

                    }

                    @Override
                    public void onComplete(String attachIds) {
                        if (!TextUtils.isEmpty(attachIds)) {
                            mContact.setAttachment(attachIds);
                        }
                        saveTask();
                    }
                });
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
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(client_status);
                dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        tv_status.setText(client_status[index]);
                        mContact.setStatus(client_status1[index]);
                        mContact.setStageName(client_status1[index]);
                    }
                });
            }
        });

        et_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactNewActivity.this, ClientListActivity.class);
                intent.putExtra("isSelectCliet", true);
                startActivityForResult(intent, REQUEST_SELECT_CLIENT);
            }
        });

        tv_contact_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = "this";
                pickerView.show();
            }
        });

//        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
//            @Override
//            public void onSelected(int index) {
//                tv_contact_time.setText(status.get(index).getName());
//                mContact.setStage(status.get(index).getUuid());
//            }
//        });

        next_contact_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
                time = "next";
            }
        });

        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if ("this".equals(time)) {
                    tv_contact_time.setText(ViewHelper.formatDateToStr(date));
                    mContact.setContactTime(ViewHelper.formatDateToStr(date));
                } else if ("next".equals(time)) {
                    next_contact_time.setText(ViewHelper.formatDateToStr(date));
                    mContact.setNextContactTime(ViewHelper.formatDateToStr(date));
                }
            }
        });

        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation(ContactNewActivity.this, 0, 0);
            }
        });
    }

    /***
     * 从参数经纬度附近地址列表选择一个地点
     * SelectLocationBiz.SELECT_LOCATION_CODE
     *
     * @param context
     *            上下文
     * @param lat
     *            经度
     * @param lng
     *            纬度
     */
    private void selectLocation(Context context, double lat, double lng) {

        Intent intent = new Intent(context, LocationListActivity.class);
        if (lng != 0 && lat != 0) {
            intent.putExtra(LocationListActivity.LATITUDE, lat);
            intent.putExtra(LocationListActivity.LONGITUDE, lng);
        }

        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }

    /***
     * 获取定位地址
     *
     * @param country 县
     * @param city    市
     * @param place
     * @return
     */
    private String getLocationAddress(final String country, final String city,
                                      BaiduPlace place) {
        String address = place.name + " (" + place.address + ")";
        if (!TextUtils.isEmpty(city) && !address.contains(city)) {
            address = city + " " + address;
        }
        return address;
    }

    private void getStatusName() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录状态;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                status = JsonUtils.jsonToArrayEntity(result, ContactStatus.class);
                statusName.clear();
                for (ContactStatus s : status) {
                    statusName.add(s.getName());
                }
            }
        });
    }

    private void saveTask() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.添加跟进记录;
        StringRequest.postAsyn(url, mContact, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存成功!");
                ContactDayViewFragment.isResume = true;
                ClientContactListFragment.isResume = true;
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存失败!");
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存失败!");
            }
        });
    }

    private void getStatusNameById() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录状态;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                List<ContactStatus> status = JsonUtils.jsonToArrayEntity(result, ContactStatus.class);
                for (ContactStatus s : status) {
                    if (s.getUuid().equals(mContact.getStage())) {
                        tv_contact_time.setText(s.getName());
                        return;
                    }
                }
            }
        });
    }
}
