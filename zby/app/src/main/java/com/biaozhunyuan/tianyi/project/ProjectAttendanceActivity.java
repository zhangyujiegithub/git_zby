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
 * @Description 项目打卡页面
 * @date create in 2020年3月23日16:12:09
 */
public class ProjectAttendanceActivity extends BaseActivity implements BDLocationListener {

    private ImageView imageViewCancel;
    private LinearLayout selectAddress;
    private LinearLayout selectAddress1;
    private ImageView iv_signin_cemera;  //签到调用相机
    private ImageView iv_signout_cemera; //签退调用相机
    private ImageView ivSignOutSide; //外出打卡调用相机
    private ImageView imageViewNew; //考勤记录
    private ImageView ivSetting; //考勤设置
    private MultipleAttachView signin_img;  //签到图片
    private MultipleAttachView signout_img; //签退图片
    private TextView tv_signout_address_name; //当前定位
    private TextView tv_signin_address_name;  //当前定位
    private TextView tvOutAddress;  //外出定位地址
    private TextView tvOutAddressName;  //外出定位地址
    private TextView tv_update_out;  //刷新签退
    private TextView tvSignOut;  //外出打卡
    private ScrollView llAttendance; //签到布局
    private RelativeLayout rlOutside;//外出打卡布局
    public LinearLayout ll_before_signin;
    public LinearLayout ll_before_signout;
    public LinearLayout ll_signin; //签到
    public LinearLayout ll_signout; //签退
    public ConstraintLayout cl_after_signin; //签到
    public ConstraintLayout cl_after_signout; //签退
    public TextView tc_singin; //签到当前时间
    public TextView tc_singout; //签退当前时间
    public TextView tv_sigin_address; //签到范围地址
    public TextView tv_signout_address;  //签退范围地址
    public TextView tv_signin_address_after;   //签到的定位地址
    public TextView tv_signout_address_after;   //签退的定位地址
    public TextView tv_worktime;   //工作时间
    public TextView tv_getoffwork_time;   //下班时间
    public TextView tv_signout_time;   //签退的时间
    public TextView tv_singin_time;     //签到的时间
    public TextView tv_signout_details;   //签到的详细
    public TextView tv_signin_details;   //签退的详细
    public TextView tvWifiName;   //签退WiFi名称
    public TextView tvWifiNameSignIn;   //签到WiFi名称
    public Button btnBuqian;   //补签按钮
    private LinearLayout llSelectProjectSignIn; //签到选择项目
    private TextView tvSelectProjectSignIn; //签到选择项目
    private LinearLayout llSelectProjectSignOut; //签退选择项目
    private TextView tvSelectProjectSignOut; //签到选择项目
    private ImageView ivIsOnRangeSignIn;
    private ImageView ivIsOnRangeSignOut;
    public LinearLayout llWifiOnRange;
    public LinearLayout llWifiOnRangeSignIn;


    private Uri contentUri;
    private String mPictureFile = "";
    private String writeToPath = ""; // 文件路径
    private String spPath = ""; // 压缩后的文件路径
    private String attachId = "";
    private DictIosPickerBottomDialog dialog;
    private BaiduPlace mBaiduPlace;
    private String dateToday;   //今天日期
    private Context mContext;
    private final int SELECT_LOCATION_CODE = 1101; // 选择地址
    private final int CAMERA_TAKE_HELPER = 1001;//拍照
    private final int SIGN_SELECT_PROJECT = 1002;//签到选择项目
    private boolean isSignIn = true;
    private Project mProject;
    private ProjectAttendanceInfo attendanceInfo;

    /**
     * 定位所需字段
     */
    String mLocation = null;
    private String mCity;
    private String mCountry;
    private int radiusMap = 150;// 默认区域半径
    /**
     * 上传到服务器的经纬度
     */
    double mLatitude;
    double mLongitude;
    private double mLat;
    private double mLog;
    private double Latitude;
    private double Longitude;
    private Double lat = 0.0; // 纬度
    private Double lng = 0.0;  // 经度
    private Double range = 0.0;  // 范围(米)
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
                        Toast.makeText(ProjectAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
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
        refreshLocation();//实时刷新位置，每分钟刷新一次
    }


    /**
     * 获取当前打卡信息
     */
    private void getCurrentSignInfo() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.项目获取当前打卡信息;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ll_before_signout.setVisibility(View.GONE);
                cl_after_signin.setVisibility(View.GONE);
                String res = JsonUtils.pareseData(response);
                if (TextUtils.isEmpty(res)) { //没有打卡信息，显示签到按钮
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
            if (attendanceInfo.isSignedIn()) { //如果已经签到
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
     * 实时刷新位置,1分钟刷新一次
     */
    private void refreshLocation() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShortToast("已超时，正在重新定位");
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
                    Toast.makeText(ProjectAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(1);
                }
            }
        });
        ll_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation == null) {
                    Toast.makeText(ProjectAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(2);
                }
            }
        });


        //签到选择项目
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
                //进入项目补签表单页面
                Intent intent = new Intent();
                intent.setClass(mContext, FormInfoActivity.class);
                intent.putExtra("workflowTemplateId", "4fc38d6d26554391aab36037948c01f2");
                intent.putExtra("formDataId", "0");
                intent.putExtra("createrId", "");
                startActivity(intent);
            }
        });


        //签退选择项目
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

        intent.putExtra("isShowOutSide", false);
        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }


    /**
     * 打卡
     *
     * @param type 1：签到；2：签退
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
            ToastUtils.showShort("请选择项目");
            return;
        }
        if (type == 1) { //签到
            url = Global.BASE_JAVA_URL + GlobalMethord.项目签到;
        } else {  //签退
            url = Global.BASE_JAVA_URL + GlobalMethord.项目签退;
        }

        StringRequest.postAsyn(url, attendance, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort("打卡成功");
                if (type == 1) { //如果是签到
                    getCurrentSignInfo();
                } else { //签退
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
     * 上传考勤图片 到服务器
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
     * 直接拍照或者重新拍照
     */
    private void takeCamreaOrReTake(ImageView iv) {
        if ("unTake".equals(iv.getTag())) {
            takeCemera();
        } else {
            dialog.show("重新获取");
        }
    }

    /**
     * 调用相机拍照
     */
    private void takeCemera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统相机
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
                    // 采样图片（缩小）
                    Bitmap uploadPhoto = BitmapHelper
                            .decodeSampleBitmapFromFile(writeToPath, 300, 300);
                    // 更改路径:使用采样后的路径
                    spPath = PhotoHelper.PATH + "/sf_" + mPictureFile;
                    BitmapHelper.createThumBitmap(spPath, uploadPhoto);
                    if (llAttendance.getVisibility() == View.VISIBLE) {
                        if (ll_before_signin.getVisibility() == View.VISIBLE) { //签到
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
                    showShortToast("调用系统相机异常");
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
                CharSequence text = "需要连接到4G或者wifi因特网！";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                BaiduLocator.stop();
            }
            mLocation = location.getAddrStr();
            // mProvince = location.getProvince();
            mCity = location.getCity();
            mCountry = location.getCountry();

            //实例化一个当前定位到的地址对象
            mBaiduPlace = new BaiduPlace();
            mBaiduPlace.location = new BaiduPlace.Location();
            mBaiduPlace.location.lat = mLat;
            mBaiduPlace.location.lng = mLog;
            mBaiduPlace.name = mLocation;
            mBaiduPlace.address = mCity;
            mBaiduPlace.city = mCity;

            if (TextUtils.isEmpty(mLocation)) {
                tv_signin_address_name.setText("暂未定位到位置，点击可调整定位地址..");
                tv_signout_address_name.setText("暂未定位到位置，点击可调整定位地址..");
                tvOutAddress.setText("暂未定位到位置，点击可调整定位地址..");
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
            String url = "http://api.map.baidu.com/place/v2/search?query=楼$酒店$大厦$公司$小区$中心$公交$银行$学校$街道$路&bounds="
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
                        Logger.i("地址个数::" + bpList.size());
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
