package com.biaozhunyuan.tianyi.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.BaiduLocator;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;

import okhttp3.Request;


/**
 * 新建外勤签到页面
 */
public class OutsideSignActivity extends BaseActivity implements BDLocationListener {


    private BoeryunHeaderView headerView;
    private TextView tvTime;
    private TextView tvAddress;
    private TextView tvClient;
    private LinearLayout llRefreshLocation;
    private LinearLayout llSelectLocation;
    private LinearLayout llSelectClient;
    private EditText etRemark;
    private Button btnSign;
    private MultipleAttachView attachView;


    private final int SELECT_LOCATION_CODE = 1101; // 选择地址
    private Context mContext;
    private double mLatitude;
    private double mLongitude;
    private String mLocation = "";
    private BaiduPlace mBaiduPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_sign);
        mContext = OutsideSignActivity.this;
        initViews();
        refreshLocation();
        setOnEvent();
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            LogUtils.i("BDLocationListener::", "onReceiveLocation is null");
        } else {
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            mLocation = location.getAddrStr();

            mBaiduPlace = new BaiduPlace();
            mBaiduPlace.location = new BaiduPlace.Location();
            mBaiduPlace.location.lat = mLatitude;
            mBaiduPlace.location.lng = mLongitude;
            mBaiduPlace.name = mLocation;
            mBaiduPlace.address = location.getCity();
            mBaiduPlace.city = location.getCity();

            if (TextUtils.isEmpty(mLocation)) {
                tvAddress.setText("暂未定位到位置，点击刷新可调整定位地址..");
            } else {
                tvAddress.setText(mLocation);
            }

            if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Context context = getApplicationContext();
                CharSequence text = "需要连接到4G或者wifi因特网！";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                BaiduLocator.stop();
            }
            BaiduLocator.stop();
        }
    }

    private void initViews() {
        headerView = findViewById(R.id.header);
        tvTime = findViewById(R.id.tv_time);
        tvAddress = findViewById(R.id.tv_address);
        tvClient = findViewById(R.id.tv_client);
        etRemark = findViewById(R.id.et_remark);
        llRefreshLocation = findViewById(R.id.refresh_location);
        llSelectLocation = findViewById(R.id.ll_select_locate);
        llSelectClient = findViewById(R.id.ll_select_client);
        btnSign = findViewById(R.id.btn_sign);
        attachView = findViewById(R.id.attach_view);


        headerView.setBackIconVisible(true);
        headerView.setTitle("新建外勤");
        attachView.setIsAdd(true);
        attachView.setMaxCount(9);
        attachView.loadImageByAttachIds("");
        attachView.setOnlyCanTackPhoto(true);
        tvTime.setText(ViewHelper.getDateStringNoSeconds());
    }

    private void requestLocating() throws Exception {
        BaiduLocator.requestLocation(getApplicationContext(), this);
    }


    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        llRefreshLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshLocation();
            }
        });

        llSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation(mContext, mLatitude, mLongitude);
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mLocation)) {
                    showShortToast("未定位到地址，请尝试切换网络或打开定位");
                    return;
                }
                uploadImage();
            }
        });
    }

    private void refreshLocation() {
        try {
            requestLocating();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Bundle bundle = new Bundle();
        bundle.putSerializable("location_place", mBaiduPlace);
        intent.putExtras(bundle);

        intent.putExtra("isShowOutSide", true);
        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_LOCATION_CODE) {
                BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
                        requestCode, data);
                if (place != null) {
                    if (place.location != null) {
                        mBaiduPlace = place;
                        mLatitude = place.location.lat;
                        mLongitude = place.location.lng;
                        tvAddress.setText(place.name);
                    }
                }
            } else {
                attachView.onActivityiForResultImage(requestCode,
                        resultCode, data);
            }
        }
    }


    /**
     * 外出打卡
     */
    private void uploadImage() {
        attachView.uploadImage("sign", new IOnUploadMultipleFileListener() {
            @Override
            public void onStartUpload(int sum) {

            }

            @Override
            public void onProgressUpdate(int completeCount) {

            }

            @Override
            public void onComplete(String attachIds) {
                OaAttendance attendance = new OaAttendance();
                attendance.setCreatorId(Global.mUser.getUuid());
                attendance.setLatitudePin(mLatitude);
                attendance.setLongitudePin(mLongitude);
                attendance.setAddressPin(mLocation);
                attendance.setPicPin(attachIds);
                attendance.setRemark(etRemark.getText().toString().trim());
                attendance.setSignOut(true);
                signOutSide(attendance);
            }
        });

    }

    private void signOutSide(OaAttendance attendance) {
        ProgressDialogHelper.show(mContext,"签到中");
        String url = Global.BASE_JAVA_URL + GlobalMethord.考勤;
        StringRequest.postAsyn(url, attendance, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("签到成功");
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("签到失败:" + JsonUtils.pareseData(result));
            }
        });
    }
}
