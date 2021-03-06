package com.biaozhunyuan.tianyi.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.attendance.BaiduPlace;
import com.biaozhunyuan.tianyi.attendance.LocationListActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.EarthMapUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.ToastUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.helper.BaiduLocator;
import com.biaozhunyuan.tianyi.helper.PhotoHelper;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

/**
 * @author wanganmin
 * @Description ??????????????????
 * @date create in 2020???3???23???16:12:09
 */
public class ProjectAttendanceActivity extends BaseActivity implements BDLocationListener {

    private ImageView imageViewCancel;
    private LinearLayout selectAddress;
    private LinearLayout selectAddress1;
    private ImageView iv_signin_cemera;  //??????????????????
    private ImageView iv_signout_cemera; //??????????????????
    private ImageView ivSignOutSide; //????????????????????????
    private ImageView imageViewNew; //????????????
    private ImageView ivSetting; //????????????
    private MultipleAttachView signin_img;  //????????????
    private MultipleAttachView signout_img; //????????????
    private TextView tv_signout_address_name; //????????????
    private TextView tv_signin_address_name;  //????????????
    private TextView tvOutAddress;  //??????????????????
    private TextView tvOutAddressName;  //??????????????????
    private TextView tv_update_out;  //????????????
    private TextView tvSignOut;  //????????????
    private ScrollView llAttendance; //????????????
    private RelativeLayout rlOutside;//??????????????????
    public LinearLayout ll_before_signin;
    public LinearLayout ll_before_signout;
    public LinearLayout ll_signin; //??????
    public LinearLayout ll_signout; //??????
    public ConstraintLayout cl_after_signin; //??????
    public ConstraintLayout cl_after_signout; //??????
    public TextView tc_singin; //??????????????????
    public TextView tc_singout; //??????????????????
    public TextView tv_sigin_address; //??????????????????
    public TextView tv_signout_address;  //??????????????????
    public TextView tv_signin_address_after;   //?????????????????????
    public TextView tv_signout_address_after;   //?????????????????????
    public TextView tv_worktime;   //????????????
    public TextView tv_getoffwork_time;   //????????????
    public TextView tv_signout_time;   //???????????????
    public TextView tv_singin_time;     //???????????????
    public TextView tv_signout_details;   //???????????????
    public TextView tv_signin_details;   //???????????????
    public TextView tvWifiName;   //??????WiFi??????
    public TextView tvWifiNameSignIn;   //??????WiFi??????
    public Button btnBuqian;   //????????????
    private LinearLayout llSelectProjectSignIn; //??????????????????
    private TextView tvSelectProjectSignIn; //??????????????????
    private LinearLayout llSelectProjectSignOut; //??????????????????
    private TextView tvSelectProjectSignOut; //??????????????????
    private ImageView ivIsOnRangeSignIn;
    private ImageView ivIsOnRangeSignOut;
    public LinearLayout llWifiOnRange;
    public LinearLayout llWifiOnRangeSignIn;


    private Uri contentUri;
    private String mPictureFile = "";
    private String writeToPath = ""; // ????????????
    private String spPath = ""; // ????????????????????????
    private String attachId = "";
    private DictIosPickerBottomDialog dialog;
    private BaiduPlace mBaiduPlace;
    private String dateToday;   //????????????
    private Context mContext;
    private final int SELECT_LOCATION_CODE = 1101; // ????????????
    private final int CAMERA_TAKE_HELPER = 1001;//??????
    private final int SIGN_SELECT_PROJECT = 1002;//??????????????????
    private boolean isSignIn = true;
    private Project mProject;
    private ProjectAttendanceInfo attendanceInfo;

    /**
     * ??????????????????
     */
    String mLocation = null;
    private String mCity;
    private String mCountry;
    private int radiusMap = 150;// ??????????????????
    /**
     * ??????????????????????????????
     */
    double mLatitude;
    double mLongitude;
    private double mLat;
    private double mLog;
    private double Latitude;
    private double Longitude;
    private Double lat = 0.0; // ??????
    private Double lng = 0.0;  // ??????
    private Double range = 0.0;  // ??????(???)
    private LatLng end = null;
    private Timer timer = new Timer();
    private String currentTime = "12:00:00";
    private boolean isFirstLoad = true;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tc_singin.setText(ViewHelper.getCurrentTime());
                    tc_singout.setText(ViewHelper.getCurrentTime());
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int type = (int) msg.obj;
                    if (mLocation == null) {
                        Toast.makeText(ProjectAttendanceActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        sign(type);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_project);
        initView();
        initData();
        getCurrentSignInfo();
        setOnEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad) {
            isFirstLoad = false;
        } else {
            getCurrentSignInfo();
        }
    }

    private void initData() {
        mContext = ProjectAttendanceActivity.this;
        if (lat != 0.0 && lng != 0.0 && range != 0.0) {
            end = new LatLng(lat, lng);
        }
        currentTime = ViewHelper.formatDateToStr(new Date(), "kk:mm:ss");
        dateToday = ViewHelper.getDateToday();
        tc_singin.setText(currentTime);
        tc_singout.setText(currentTime);
        new TimeThread().start();
        try {
            requestLocating();
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshLocation();//??????????????????????????????????????????
    }


    /**
     * ????????????????????????
     */
    private void getCurrentSignInfo() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.??????????????????????????????;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ll_before_signout.setVisibility(View.GONE);
                cl_after_signin.setVisibility(View.GONE);
                String res = JsonUtils.pareseData(response);
                if (TextUtils.isEmpty(res)) { //???????????????????????????????????????
                    ll_before_signin.setVisibility(View.VISIBLE);
                } else {
                    try {
                        attendanceInfo = JsonUtils.jsonToEntity(res, ProjectAttendanceInfo.class);
                        initLayout();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

    private void initLayout() {
        if (attendanceInfo != null) {
            if (attendanceInfo.isSignedIn()) { //??????????????????
                ll_before_signin.setVisibility(View.GONE);
                ll_before_signout.setVisibility(View.VISIBLE);
                cl_after_signin.setVisibility(View.VISIBLE);
                tv_singin_time.setText(attendanceInfo.getSignInDateTime());
                tv_signin_address_after.setText(attendanceInfo.getSignInAddress());
                tv_signin_details.setText(attendanceInfo.getProjectName());
                tvSelectProjectSignOut.setText(attendanceInfo.getProjectName());
                llSelectProjectSignOut.setEnabled(false);
                llSelectProjectSignOut.setClickable(false);
                btnBuqian.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(attendanceInfo.getSignInAttachmentIds())) {
                    signin_img.setVisibility(View.VISIBLE);
                    signin_img.loadImageByAttachIds(attendanceInfo.getSignInAttachmentIds());
                }
            }
        }
    }


    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    /**
     * ??????????????????,1??????????????????
     */
    private void refreshLocation() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast("??????????????????????????????");
                        try {
                            mLocation = "";
                            iv_signin_cemera.setTag("unTake");
                            iv_signout_cemera.setTag("unTake");
                            requestLocating();
                        } catch (Exception e) {
                            Logger.e("locating::" + "failed:" + e.getLocalizedMessage());
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000 * 60, 1000 * 60);
    }


    private void requestLocating() throws Exception {
        BaiduLocator.requestLocation(getApplicationContext(), ProjectAttendanceActivity.this);
    }

    private void setOnEvent() {
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_signin_cemera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamreaOrReTake(iv_signin_cemera);
            }
        });
        iv_signout_cemera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamreaOrReTake(iv_signout_cemera);
            }
        });

        ll_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation == null) {
                    Toast.makeText(ProjectAttendanceActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(1);
                }
            }
        });
        ll_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation == null) {
                    Toast.makeText(ProjectAttendanceActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(2);
                }
            }
        });


        //??????????????????
        llSelectProjectSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectListActivity.class);
                intent.putExtra("isSelectProject", true);
                startActivityForResult(intent, SIGN_SELECT_PROJECT);
            }
        });

        btnBuqian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????????????????????????????
                Intent intent = new Intent();
                intent.setClass(mContext, FormInfoActivity.class);
                intent.putExtra("workflowTemplateId", "4fc38d6d26554391aab36037948c01f2");
                intent.putExtra("formDataId", "0");
                intent.putExtra("createrId", "");
                startActivity(intent);
            }
        });


        //??????????????????
        llSelectProjectSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectListActivity.class);
                intent.putExtra("isSelectProject", true);
                startActivityForResult(intent, SIGN_SELECT_PROJECT);
            }
        });

        selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation(ProjectAttendanceActivity.this, mLat, mLog);
            }
        });

        selectAddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation(ProjectAttendanceActivity.this, mLat, mLog);
            }
        });

        imageViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProjectAttendanceSalaryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signOn(int type) {
        File file = new File(spPath);
        if (!TextUtils.isEmpty(spPath) && file.exists()) {
            uploadPhoto(type, spPath);
        } else {
            sign(type);
        }
    }

    /***
     * ??????????????????????????????????????????????????????
     * SelectLocationBiz.SELECT_LOCATION_CODE
     *
     * @param context
     *            ?????????
     * @param lat
     *            ??????
     * @param lng
     *            ??????
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

        intent.putExtra("isShowOutSide", false);
        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }


    /**
     * ??????
     *
     * @param type 1????????????2?????????
     */
    private void sign(int type) {

        String url;
        ProjectAttendanceParam attendance = new ProjectAttendanceParam();
        if (mProject != null) {
            attendance.setProjectId(mProject.getUuid());
        }
        attendance.setLatitude(mLatitude);
        attendance.setLongitude(mLongitude);
        attendance.setAddress(mLocation);
        attendance.setAttachmentIds(attachId);
        if (type == 2) {
            attendance.setAttendanceId(attendanceInfo.getUuid());
            attendance.setProjectId(attendanceInfo.getProjectId());
        }
        if (TextUtils.isEmpty(attendance.getProjectId())) {
            ToastUtils.showShort("???????????????");
            return;
        }
        if (type == 1) { //??????
            url = Global.BASE_JAVA_URL + GlobalMethord.????????????;
        } else {  //??????
            url = Global.BASE_JAVA_URL + GlobalMethord.????????????;
        }

        StringRequest.postAsyn(url, attendance, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort("????????????");
                if (type == 1) { //???????????????
                    getCurrentSignInfo();
                } else { //??????
                    getCurrentSignInfo();
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
     * ?????????????????? ????????????
     *
     * @param path
     * @return
     */
    private void uploadPhoto(final int type, final String path) {
        new Thread() {
            @Override
            public void run() {
                File file = new File(path);
                attachId = UploadHelper.uploadFileGetAttachId("attendance", file);
                Message msg = new Message();
                msg.obj = type;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * ??????????????????????????????
     */
    private void takeCamreaOrReTake(ImageView iv) {
        if ("unTake".equals(iv.getTag())) {
            takeCemera();
        } else {
            dialog.show("????????????");
        }
    }

    /**
     * ??????????????????
     */
    private void takeCemera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// ??????????????????
        mPictureFile = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        File file = new File(PhotoHelper.PATH, mPictureFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentUri = FileProvider.getUriForFile(ProjectAttendanceActivity.this, "com.biaozhunyuan.tianyi.fileprovider", file);
        } else {
            contentUri = Uri.fromFile(file);
        }
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("path", mPictureFile);
        editor.commit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, CAMERA_TAKE_HELPER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_LOCATION_CODE) {
                BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
                        requestCode, data);
                if (place != null) {
                    mLocation = getLocationAddress(mCountry, mCity, place);
                    if (place.location != null) {
                        mBaiduPlace = place;
                        mLatitude = place.location.lat;
                        mLongitude = place.location.lng;
                        tv_signin_address_name.setText(place.name);
                        tv_signout_address_name.setText(place.name);
                        tvOutAddress.setText(place.name);
                        tvOutAddressName.setText(place.name);
                        tv_sigin_address.setText(place.address);
                        tv_signout_address.setText(place.address);
                    }
                }
            } else if (requestCode == CAMERA_TAKE_HELPER) {
                if (data == null) {
                    Logger.i("onActivityResult::" +
                            "onActivityResult data isnull");
                }
                SharedPreferences sp = getSharedPreferences("config",
                        Context.MODE_PRIVATE);
                mPictureFile = sp.getString("path", "");

                if (!TextUtils.isEmpty(mPictureFile)) {
                    writeToPath = PhotoHelper.PATH + "/" + mPictureFile;
                    Logger.i("onActivityResult::" + "writeToPath " + writeToPath);
                    // ????????????????????????
                    Bitmap uploadPhoto = BitmapHelper
                            .decodeSampleBitmapFromFile(writeToPath, 300, 300);
                    // ????????????:????????????????????????
                    spPath = PhotoHelper.PATH + "/sf_" + mPictureFile;
                    BitmapHelper.createThumBitmap(spPath, uploadPhoto);
                    if (llAttendance.getVisibility() == View.VISIBLE) {
                        if (ll_before_signin.getVisibility() == View.VISIBLE) { //??????
                            iv_signin_cemera.setTag("take");
                            iv_signin_cemera.setImageBitmap(PhotoHelper.disposeBitmapForListView(spPath));
                            ivSignOutSide.setImageBitmap(PhotoHelper.disposeBitmapForListView(spPath));
                        } else {
                            iv_signout_cemera.setTag("take");
                            iv_signout_cemera.setImageBitmap(PhotoHelper.disposeBitmapForListView(spPath));
                            ivSignOutSide.setImageBitmap(PhotoHelper.disposeBitmapForListView(spPath));
                        }
                    } else {
                        ivSignOutSide.setTag("take");
                        ivSignOutSide.setImageBitmap(PhotoHelper.disposeBitmapForListView(spPath));
                    }
                } else {
                    showShortToast("????????????????????????");
                }
            } else if (requestCode == SIGN_SELECT_PROJECT) {
                mProject = (Project) data.getSerializableExtra("selectProject");
                if (mProject != null) {
                    if (isSignIn) {
                        tvSelectProjectSignIn.setText(mProject.getName());
                    } else {
                        tvSelectProjectSignOut.setText(mProject.getName());
                    }
                }
            }
        }
    }


    /***
     * ??????????????????
     *
     * @param country ???
     * @param city    ???
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


    @Override
    public void onReceiveLocation(BDLocation location) {
        Logger.i("BDLocationListener:::onReceiveLocation is running");
        if (location == null) {
            Logger.i("BDLocationListener::onReceiveLocation is null");
        } else {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            mLatitude = location.getLatitude();
            Latitude = location.getLatitude();
            mLat = location.getLatitude();
            // mLatitude = 42.478988;
            // mLat = 42.478988;
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            mLongitude = location.getLongitude();
            Longitude = location.getLongitude();

            mLog = location.getLongitude();
            // mLongitude = 99.820494;
            // mLog = 99.820494;
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Context context = getApplicationContext();
                CharSequence text = "???????????????4G??????wifi????????????";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                BaiduLocator.stop();
            }
            mLocation = location.getAddrStr();
            // mProvince = location.getProvince();
            mCity = location.getCity();
            mCountry = location.getCountry();

            //?????????????????????????????????????????????
            mBaiduPlace = new BaiduPlace();
            mBaiduPlace.location = new BaiduPlace.Location();
            mBaiduPlace.location.lat = mLat;
            mBaiduPlace.location.lng = mLog;
            mBaiduPlace.name = mLocation;
            mBaiduPlace.address = mCity;
            mBaiduPlace.city = mCity;

            if (TextUtils.isEmpty(mLocation)) {
                tv_signin_address_name.setText("???????????????????????????????????????????????????..");
                tv_signout_address_name.setText("???????????????????????????????????????????????????..");
                tvOutAddress.setText("???????????????????????????????????????????????????..");
            }
            iv_signin_cemera.setImageResource(R.drawable.icon_attendance_cemera);
            ivSignOutSide.setImageResource(R.drawable.icon_attendance_cemera);
            iv_signout_cemera.setImageResource(R.drawable.icon_attendance_cemera);
            tv_signin_address_name.setText(mLocation);
            tv_signout_address_name.setText(mLocation);
            tvOutAddress.setText(mLocation);
            Logger.e("BDLocationListener" + sb.toString());
            BaiduLocator.stop();

            String locationRect = EarthMapUtils.getLocationRect(mLat, mLog,
                    radiusMap);
            String url = "http://api.map.baidu.com/place/v2/search?query=???$??????$??????$??????$??????$??????$??????$??????$??????$??????$???&bounds="
                    + locationRect
                    + "&output=json&ak=1lefPqdAWokm2tpRg3o9IhUfwjxvk1ci";
            getLocationList(url, mCountry, mCity);
        }
    }


    private void getLocationList(final String url, final String country,
                                 final String city) {

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                try {
                    JSONObject jo = new JSONObject(result);
                    int status = jo.getInt("status");
                    String message = jo.getString("message");
                    String results = jo.getString("results");
                    if (status == 0 && "ok".equals(message)) {
                        List<BaiduPlace> bpList = JsonUtils.pareseJsonToList(
                                results, BaiduPlace.class);
                        Logger.i("????????????::" + bpList.size());
                        if (bpList.size() > 0) {
                            BaiduPlace baiduPlace = bpList.get(0);
                            mLocation = getLocationAddress(country, city, baiduPlace);
                            lat = baiduPlace.location.lat;
                            mLat = baiduPlace.location.lat;
                            Longitude = baiduPlace.location.lng;
                            mLongitude = baiduPlace.location.lng;
                            tv_signin_address_name.setText(baiduPlace.name);
                            tv_signout_address_name.setText(baiduPlace.name);
                            tvOutAddress.setText(baiduPlace.name);
                            tvOutAddressName.setText(baiduPlace.address);
                            tv_sigin_address.setText(baiduPlace.address);
                            tv_signout_address.setText(baiduPlace.address);
                        }
                    }
                } catch (Exception e) {
                    Logger.e("" + e.getMessage());
                }
            }
        });
    }


    private void initView() {
        selectAddress = findViewById(R.id.ll_select_address);
        selectAddress1 = findViewById(R.id.ll_select_address1);
        imageViewCancel = findViewById(R.id.imageViewCancel);
        tv_signin_address_name = findViewById(R.id.tv_signin_address_name);
        tvOutAddress = findViewById(R.id.tv_sign_outSide_address_name);
        tvOutAddressName = findViewById(R.id.tv_sign_outSide_address);
        tv_update_out = findViewById(R.id.update_sign_Out);
        tvSignOut = findViewById(R.id.tv_sign_out);
        llAttendance = findViewById(R.id.ll_attendance);
        rlOutside = findViewById(R.id.rl_outside);
        tv_signout_address_name = findViewById(R.id.tv_signout_address_name);
        signin_img = findViewById(R.id.iv_signin_img);
        signout_img = findViewById(R.id.iv_signout_img);
        imageViewNew = findViewById(R.id.imageViewNew);
        ivSetting = findViewById(R.id.iv_wifi_setting);
        ll_before_signin = findViewById(R.id.ll_before_signin);
        ll_before_signout = findViewById(R.id.ll_before_signout);
        ll_signin = findViewById(R.id.ll_signin);
        llWifiOnRange = findViewById(R.id.ll_on_wifi_range);
        llWifiOnRangeSignIn = findViewById(R.id.ll_on_wifi_range_signIn);
        ll_signout = findViewById(R.id.ll_signout);
        cl_after_signin = findViewById(R.id.cl_after_signin);
        cl_after_signout = findViewById(R.id.cl_after_signout);
        tc_singin = findViewById(R.id.tc_singin);
        tc_singout = findViewById(R.id.tc_singout);
        tv_sigin_address = findViewById(R.id.tv_signin_address);
        tv_signout_address = findViewById(R.id.tv_signout_address);
        tv_signin_address_after = findViewById(R.id.tv_signin_address_after);
        tv_signout_address_after = findViewById(R.id.tv_signout_address_after);
        tv_worktime = findViewById(R.id.tv_worktime);
        tv_getoffwork_time = findViewById(R.id.tv_getoffwork_time);
        tv_signout_time = findViewById(R.id.tv_signout_time);
        tv_singin_time = findViewById(R.id.tv_singin_time);
        tv_signout_details = findViewById(R.id.tv_signout_details);
        tv_signin_details = findViewById(R.id.tv_signin_details);
        tvWifiName = findViewById(R.id.tv_wifi_name);
        tvWifiNameSignIn = findViewById(R.id.tv_wifi_name_sign_in);
        btnBuqian = findViewById(R.id.btn_buqian);
        iv_signin_cemera = findViewById(R.id.iv_signin_cemera);
        iv_signout_cemera = findViewById(R.id.iv_signout_cemera);
        ivIsOnRangeSignOut = findViewById(R.id.iv_wifi_name_sign_out);
        ivIsOnRangeSignIn = findViewById(R.id.iv_wifi_name_sign_in);
        ivSignOutSide = findViewById(R.id.iv_sign_outSide_camera);
        llSelectProjectSignIn = findViewById(R.id.ll_select_project_sign_in);
        tvSelectProjectSignIn = findViewById(R.id.tv_select_project_sign_in);
        llSelectProjectSignOut = findViewById(R.id.ll_select_project_sign_out);
        tvSelectProjectSignOut = findViewById(R.id.tv_select_project_sign_out);
        dialog = new DictIosPickerBottomDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }
}
