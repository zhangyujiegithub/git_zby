package com.biaozhunyuan.tianyi.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.utils.ClickUtil;
import com.biaozhunyuan.tianyi.common.utils.NetUtils;
import com.biaozhunyuan.tianyi.helper.BaiduLocator;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.helper.PhotoHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.EarthMapUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.MapDistanceUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.SegmentView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/10/26.
 * 签到页面
 */
public class NewAttendanceActivity extends BaseActivity implements BDLocationListener {

    public static final int SELECT_LOCATION_CODE = 1101; // 选择地址
    public final int CAMERA_TAKE_HELPER = 1001;//拍照
    private static final int msgKey1 = 1;
    private String writeToPath = ""; // 文件路径
    private String spPath = ""; // 压缩后的文件路径
    private String mPictureFile = "";

    private String attachId = "";
    private boolean isSignIn = true;//是否是签到
    private boolean isFirstVisibal = true;

    private Uri contentUri;
    //    private Context context;
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
    public TextView tvWifiNameSignIn;   //签到WiFi名称
    public TextView tvLocateSignIn;   //签到地点名称
    public TextView tvWifiNameSignOut;   //签退WiFi名称
    public TextView tvLocateSignOut;   //签退地点名称
    private ImageView ivIsOnWifiRangeSignIn;
    private ImageView ivIsOnLocateRangeSignIn;
    private ImageView ivIsOnWifiRangeSignOut;
    private ImageView ivIsOnLocateRangeSignOut;
    public LinearLayout llWifiOnRangeSignIn;
    public LinearLayout llLocateOnRangeSignIn;
    public LinearLayout llWifiOnRangeSignOut;
    public LinearLayout llLocateRangeSignOut;

    /**
     * 最近更新时间
     */
    private long mLastUpdateTime;
    private Timer timer = new Timer();

    /**
     * 上传到服务器的经纬度
     */
    double mLatitude;
    double mLongitude;

    private double Latitude;
    private double Longitude;

    private boolean onWifiRange = false;  //是否在WiFi考勤范围内
    private boolean onLocationRange = false;//是否在地点考勤范围内

    /**
     * 定位所需字段
     */
    String mLocation = null;
    private String mCity;
    private String mCountry;

    private String sign = "";
    private Double lat = 0.0; // 纬度
    private Double lng = 0.0;  // 经度
    private Double range = 0.0;  // 范围(米)
    private Boolean isCanSignOutRange; //在打卡范围内是否可以签退
    private AttendanceSetting setting = null;
    private DictIosPickerBottomDialog dialog;

    /**
     * 记录初次定位的经纬度
     */
    private double mLat;
    private double mLog;
    private int radiusMap = 150;// 默认区域半径

    private int COMELATE = 1; //迟到
    private int LEAVEEARLY = 2; // 早退

    public static String SIGNOUT_TIME = "18:00:00";//异常签退提醒时间

    private List<BaiduPlace> bpList;
    private LatLng end = null;

    private String currentTime = "12:00:00";
    public static final String TIME = "12:00:00";

    private String dateToday;   //今天日期
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
    private SegmentView swithView; //切换按钮
    private ScrollView llAttendance; //签到布局
    private RelativeLayout rlOutside;//外出打卡布局

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int type = (int) msg.obj;
                    if (mLocation == null) {
                        Toast.makeText(NewAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                    } else {
                        isCanSign(type);
                    }
                    break;
            }
            return false;
        }
    });
    private ImageView imageViewCancel;
    private LinearLayout selectAddress;
    private LinearLayout selectAddress1;
    private BaiduPlace mBaiduPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_new);
//        ImmersionBar.with(this).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).fitsSystemWindows(true).init();
//        context = NewAttendanceActivity.this;
        initView();
        initUpdateTime();
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
        getPermission();
        getDistanceFromTheCompany();
        getWorkTime();

        setOnEvent();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstVisibal) {
            ShowAndHideSign();
        }
        isFirstVisibal = false;
    }

    /**
     * 根据权限判断是否显示考勤配置按钮
     */
    private void getPermission() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.功能模块权限;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (response.contains("考勤设置")) {
                    ivSetting.setVisibility(View.VISIBLE);
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


    private void initView() {
        selectAddress = findViewById(R.id.ll_select_address);
        selectAddress1 = findViewById(R.id.ll_select_address1);
        imageViewCancel = findViewById(R.id.imageViewCancel);
        tv_signin_address_name = findViewById(R.id.tv_signin_address_name);
        tvOutAddress = findViewById(R.id.tv_sign_outSide_address_name);
        tvOutAddressName = findViewById(R.id.tv_sign_outSide_address);
        tv_update_out = findViewById(R.id.update_sign_Out);
        swithView = findViewById(R.id.switch_view_attendance);
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
        llWifiOnRangeSignOut = findViewById(R.id.ll_on_wifi_range);
        llLocateRangeSignOut = findViewById(R.id.ll_on_locate_range_sign_out);
        llWifiOnRangeSignIn = findViewById(R.id.ll_on_wifi_range_signIn);
        llLocateOnRangeSignIn = findViewById(R.id.ll_on_locate_range_signIn);
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
        tvWifiNameSignOut = findViewById(R.id.tv_wifi_name);
        tvLocateSignOut = findViewById(R.id.tv_locate_sign_out);
        tvWifiNameSignIn = findViewById(R.id.tv_wifi_name_sign_in);
        tvLocateSignIn = findViewById(R.id.tv_wifi_locate_sign_in);
        iv_signin_cemera = findViewById(R.id.iv_signin_cemera);
        iv_signout_cemera = findViewById(R.id.iv_signout_cemera);
        ivIsOnWifiRangeSignOut = findViewById(R.id.iv_wifi_name_sign_out);
        ivIsOnLocateRangeSignOut = findViewById(R.id.iv_locate_name_sign_out);
        ivIsOnWifiRangeSignIn = findViewById(R.id.iv_wifi_name_sign_in);
        ivIsOnLocateRangeSignIn = findViewById(R.id.iv_locate_name_sign_in);
        ivSignOutSide = findViewById(R.id.iv_sign_outSide_camera);
        dialog = new DictIosPickerBottomDialog(this);

        swithView.setSegmentText("定位考勤", 0);
        swithView.setSegmentText("外出打卡", 1);
    }

    private void setOnEvent() {
        ll_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation == null) {
                    Toast.makeText(NewAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(1);
                }
            }
        });
        ll_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation == null) {
                    Toast.makeText(NewAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(2);
                }
            }
        });

        tv_update_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocation == null) {
                    Toast.makeText(NewAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(2);
                }
            }
        });
        tv_signin_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAttendanceActivity.this, AttendanceSalaryActivity.class);
                startActivity(intent);
            }
        });
        tv_signout_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAttendanceActivity.this, AttendanceSalaryActivity.class);
                startActivity(intent);
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
        ivSignOutSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCamreaOrReTake(ivSignOutSide);
            }
        });
        imageViewNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAttendanceActivity.this, AttendanceSalaryActivity.class);
                startActivity(intent);
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewAttendanceActivity.this, TagSettingActivity.class);
                startActivity(intent);
            }
        });

        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                if (index == 0) {
                    takeCemera();
                }
            }
        });

        ClickUtil.clicks(selectAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation(NewAttendanceActivity.this, mLat, mLog);
            }
        });

        ClickUtil.clicks(selectAddress1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocation(NewAttendanceActivity.this, mLat, mLog);
            }
        });

        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swithView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position == 0) {
                    llAttendance.setVisibility(View.VISIBLE);
                    rlOutside.setVisibility(View.GONE);
                } else if (position == 1) {
                    llAttendance.setVisibility(View.GONE);
                    rlOutside.setVisibility(View.VISIBLE);
                }
            }
        });

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mLocation == null) {
                    Toast.makeText(NewAttendanceActivity.this, "还没有定位到准确地点", Toast.LENGTH_SHORT).show();
                } else {
                    signOn(5);
                }*/
                startActivity(new Intent(getBaseContext(), OutsideSignActivity.class));
            }
        });
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

    private void signOn(int type) {
        File file = new File(spPath);
        if (!TextUtils.isEmpty(spPath) && file.exists()) {
            uploadPhoto(type, spPath);
        } else {
            isCanSign(type);
        }
    }

    /**
     * 签到，签退或外出考勤
     *
     * @param signType 1==签到；2==签退；3==外出考勤
     */
    private void isCanSign(final int signType) {

        String toast = "";
        if (signType == 1) {
            toast = "签到";
        } else if (signType == 2) {
            toast = "签退";
        } else if (signType == 3) {
            toast = "外出签到";
        } else if (signType == 4) {
            toast = "外出签退";
        } else if (signType == 5) {
            toast = "外出定位";
        }
        if (setting != null && setting.WifiList != null
                && setting.WifiList.size() > 0) {
            if (setting.LocationList != null
                    && setting.LocationList.size() > 0) {
                if (!onWifiRange && !onLocationRange) {
                    showShortToast("未在WiFi或地点考勤范围内，不能进行考勤！");
                    return;
                }
            } else {
                if (!onWifiRange) {
                    showShortToast("未在WiFi或地点考勤范围内，不能进行考勤！");
                    return;
                }
            }
        }

        if (setting != null && setting.LocationList != null
                && setting.LocationList.size() > 0) {
            if (!onLocationRange) {
                showShortToast("未在WiFi或地点考勤范围内，不能进行考勤！");
                return;
            }
        }

    /*if (isCanSignOutRange != null && signType != 5) { //不在打卡地点范围内
        if (!isCanSignOutRange && !onLocationRange) { //签到不在打卡范围内，并且不允许范围外打卡
            if (signType == 1) {
                showShortToast("没在指定地点考勤范围内，不能进行考勤！");
            } else if (signType == 2) { //如果是签退，不允许在范围外打卡，需要看今天是否有外出打卡记录，如果有，可以签退。
                String outTime = dateToday + " " + tc_singout.getText().toString();
                OaAttendance attendance = new OaAttendance();
                attendance.setLatitudeSignout(mLatitude);
                attendance.setLongitudeSignout(mLongitude);
                attendance.setAddressSignout(mLocation);
                attendance.setCheckOutTime(outTime);
                attendance.setPicSignout(attachId);
                attendance.setSignOut(false);
                getTodayOutSign(attendance);
            }
            return;
        }
    }*/
        final OaAttendance attendance = new OaAttendance();
        attendance.setCreatorId(Global.mUser.getUuid());
        if (signType == 1) { //签到
            attendance.setLatitudeSignin(mLatitude);
            attendance.setLongitudeSignin(mLongitude);
            attendance.setAddressSignin(mLocation);
            attendance.setCheckInTime(dateToday + " " + tc_singin.getText().toString().trim());
            attendance.setPicSignin(attachId);
            attendance.setSignOut(false);
        } else if (signType == 2) { //签退
            final String outTime = dateToday + " " + tc_singout.getText().toString();
            String time = outTime.substring(outTime.indexOf(" "));
            int compareSize = ViewHelper.getTimeCompareSize(SIGNOUT_TIME, time);
            attendance.setLatitudeSignout(mLatitude);
            attendance.setLongitudeSignout(mLongitude);
            attendance.setAddressSignout(mLocation);
            attendance.setCheckOutTime(outTime);
            attendance.setPicSignout(attachId);
            attendance.setSignOut(false);
            if ((compareSize == 1 || compareSize == 2)) {
                final AlertDialog alertDialog = new AlertDialog(NewAttendanceActivity.this).builder();
                final String finalToast = toast;
                alertDialog.setMsg("现在是早退时间，是否签退").setTitle("签退")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dissMiss();
                            }
                        })
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postSign(signType, finalToast, attendance);
                            }
                        });
                alertDialog.show();
                return;
            }
        } else if (signType == 3) {
            attendance.setLatitudeSignin(mLatitude);
            attendance.setLongitudeSignin(mLongitude);
            attendance.setAddressSignin(mLocation);
            attendance.setPicSignin(attachId);
            attendance.setCheckInTime(tc_singin.getText().toString().trim());
            attendance.setSignOut(true);
        } else if (signType == 4) {
            attendance.setLatitudeSignout(mLatitude);
            attendance.setLongitudeSignout(mLongitude);
            attendance.setAddressSignout(mLocation);
            attendance.setPicSignout(attachId);
            attendance.setCheckOutTime(tc_singout.getText().toString().trim());
            attendance.setSignOut(true);
        } else if (signType == 5) {
            attendance.setLatitudePin(mLatitude);
            attendance.setLongitudePin(mLongitude);
            attendance.setAddressPin(mLocation);
            attendance.setPicPin(attachId);
            attendance.setSignOut(true);
        }

        postSign(signType, toast, attendance);
    }


    /**
     * 打卡
     *
     * @param signType   打卡类型
     * @param toast
     * @param attendance
     */
    private void postSign(final int signType, String toast, OaAttendance attendance) {
        ProgressDialogHelper.show(NewAttendanceActivity.this, toast + "中");
        final String finalToast = toast;
        String url = Global.BASE_JAVA_URL + GlobalMethord.考勤;
        StringRequest.postAsyn(url, attendance, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    Attendance data = JsonUtils.jsonToEntity(JsonUtils.getStringValue(response, "Data"), Attendance.class);
                    boolean leaveEarly = data.isLeaveEarly();
                    boolean comeLate = data.isComeLate();
                    String uuid = data.getUuid();
                    if (leaveEarly && signType == 2) {
                        showShortToast("您早退了,请说明原因");
                        Intent intent = new Intent(NewAttendanceActivity.this, PresentationConditionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("because", LEAVEEARLY);
                        bundle.putString("uuid", uuid);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (comeLate && signType == 1) {
                        showShortToast("您迟到了,请说明原因");
                        Intent intent = new Intent(NewAttendanceActivity.this, PresentationConditionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("because", COMELATE);
                        bundle.putString("uuid", uuid);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        showShortToast(finalToast + "成功");
                    }
                    ShowAndHideSign();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                showShortToast(finalToast + "失败");
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast(finalToast + "失败:" + JsonUtils.pareseData(result));
            }
        });
    }


    /**
     * 获取今天的外出定位记录
     *
     * @param oaAttendance
     */
    private void getTodayOutSign(final OaAttendance oaAttendance) {
        String date = ViewHelper.getDateToday();
        String url = Global.BASE_JAVA_URL + GlobalMethord.外出定位记录 + "?startTime=" + date + "&endTime=" + date + "&pageIndex=1&pageSize=1";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<OaAttendance> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), OaAttendance.class);
                if (list != null && list.size() > 0) {  //如果是签退，不允许在范围外打卡，需要看今天是否有外出打卡记录，如果有，可以签退。
                    postSign(2, "签退", oaAttendance);
                } else {
                    showShortToast("没在指定地点考勤范围内，不能进行考勤！");
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("没在指定地点考勤范围内，不能进行考勤！");
            }
        });
    }

    /**
     * 获取上下班时间
     */
    private void getWorkTime() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.工作时间;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String data = JsonUtils.pareseData(response);
                try {
                    String startWorkTime = JsonUtils.getStringValue(data, "startWorkTime");
                    String endWorkTime = JsonUtils.getStringValue(data, "endWorkTime");
                    tv_worktime.setText(startWorkTime);
                    tv_getoffwork_time.setText(endWorkTime);
                    SIGNOUT_TIME = endWorkTime + ":00";
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
     * 获取非外出考勤坐标和签到范围
     */
    private void getDistanceFromTheCompany() {
        PreferceManager.getInsance().saveValueBYkey("lat", lat + "");
        PreferceManager.getInsance().saveValueBYkey("lng", lng + "");
        PreferceManager.getInsance().saveValueBYkey("range", range + "");
        String url = Global.BASE_JAVA_URL + GlobalMethord.非外出考勤坐标范围;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.pareseData(response);
                    String city = JsonUtils.getStringValue(data, "city");
                    range = Double.parseDouble(JsonUtils.getStringValue(data, "radius"));
                    lng = Double.parseDouble(JsonUtils.getStringValue(data, "lng"));
                    lat = Double.parseDouble(JsonUtils.getStringValue(data, "lat"));
                    isCanSignOutRange = Boolean.parseBoolean(JsonUtils.getStringValue(data, "outAttendance"));
//                    tv_sigin_address.setText(city);
//                    tv_signout_address.setText(city);
                    PreferceManager.getInsance().saveValueBYkey("lat", lat + "");
                    PreferceManager.getInsance().saveValueBYkey("lng", lng + "");
                    PreferceManager.getInsance().saveValueBYkey("range", range + "");
                    end = new LatLng(lat, lng);
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
     * 获取AttendanceSetting
     * 是否是签到
     */
    private void getSetting(final boolean isSignIn) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业配置 + "?name=考勤配置";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "考勤信息：" + response);

                try {
                    setting = JsonUtils.jsonToEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "value"), AttendanceSetting.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String bssid = NetUtils.getWifiSsidAddress(getBaseContext());

                if (setting != null) {
                    if (setting.WifiList != null && setting.WifiList.size() > 0) {
                        if (isSignIn) {
                            llWifiOnRangeSignIn.setVisibility(View.VISIBLE);
                        } else {
                            llWifiOnRangeSignOut.setVisibility(View.VISIBLE);
                        }

                        for (WifiInfo info : setting.WifiList) {
                            if (info.BSSID.equals(bssid)) {
                                if (isSignIn) {
                                    ivIsOnWifiRangeSignIn.setImageResource(R.drawable.icon_status_selected);
                                    tvWifiNameSignIn.setText("已进入WI-FI考勤范围:" + info.SSID);
                                } else {
                                    ivIsOnWifiRangeSignOut.setImageResource(R.drawable.icon_status_selected);
                                    tvWifiNameSignOut.setText("已进入WI-FI考勤范围:" + info.SSID);
                                }
                                onWifiRange = true;
                                break;
                            }
                        }

                        if (!onWifiRange) {
                            if (isSignIn) {
                                ivIsOnWifiRangeSignIn.setImageResource(R.drawable.icon_del);
                                tvWifiNameSignIn.setText("未进入WI-FI考勤范围");
                            } else {
                                ivIsOnWifiRangeSignOut.setImageResource(R.drawable.icon_del);
                                tvWifiNameSignOut.setText("未进入WI-FI考勤范围");
                            }
                        }
                    } else {
                        if (isSignIn) {
                            llWifiOnRangeSignIn.setVisibility(View.GONE);
                        } else {
                            llWifiOnRangeSignOut.setVisibility(View.GONE);
                        }
                    }
                    if (setting.LocationList != null && setting.LocationList.size() > 0) {
                        if (isSignIn) {
                            llLocateOnRangeSignIn.setVisibility(View.VISIBLE);
                        } else {
                            llLocateRangeSignOut.setVisibility(View.VISIBLE);
                        }

                        for (LocationInfo info : setting.LocationList) {
                            String distance = MapDistanceUtils.getInstance().getShortDistance(mLongitude, mLatitude, info.Longitude, info.Latitude);
                            LogUtils.i("和地点间的距离", info.Address.substring(0, 10) + "距离：......" + MapDistanceUtils.getInstance().getShortDistance(mLongitude, mLatitude, info.Longitude, info.Latitude));
                            if (!distance.contains("千米")) {
                                distance = distance.replace("米", " ");
                                String ran = distance.trim().split("\\.")[0];
                                if (TextUtils.isEmpty(ran)) {
                                    ran = "0";
                                }
                                int dis = Integer.valueOf(ran);
                                if (dis <= setting.Range) {
                                    onLocationRange = true;
                                    if (isSignIn) {
                                        ivIsOnLocateRangeSignIn.setImageResource(R.drawable.icon_status_selected);
                                        tvLocateSignIn.setText("已进入指定地点打卡范围内");
                                    } else {
                                        ivIsOnLocateRangeSignOut.setImageResource(R.drawable.icon_status_selected);
                                        tvLocateSignOut.setText("已进入指定地点打卡范围内");
                                    }
                                }
                            }
                        }

                        if (!onLocationRange) {
                            if (isSignIn) {
                                ivIsOnLocateRangeSignIn.setImageResource(R.drawable.icon_del);
                                tvLocateSignIn.setText("未进入指定地点打卡范围内");
                            } else {
                                ivIsOnLocateRangeSignOut.setImageResource(R.drawable.icon_del);
                                tvLocateSignOut.setText("未进入指定地点打卡范围内");
                            }
                        }

                    } else {
                        if (isSignIn) {
                            llLocateOnRangeSignIn.setVisibility(View.GONE);
                        } else {
                            llLocateRangeSignOut.setVisibility(View.GONE);
                        }
                    }


                    if (setting.WifiList != null && setting.WifiList.size() > 0) {
                        //wifi和地址 满足其中一项，另外一个不满足也不用显示
                        if (isSignIn) {
                            if (onLocationRange && !onWifiRange) {
                                llWifiOnRangeSignIn.setVisibility(View.GONE);
                            }
                        } else {
                            if (onLocationRange && !onWifiRange) {
                                llWifiOnRangeSignOut.setVisibility(View.GONE);
                            }
                        }
                    }

                    if (setting.LocationList != null && setting.LocationList.size() > 0) {
                        //wifi和地址 满足其中一项，另外一个不满足也不用显示
                        if (isSignIn) {
                            if (!onLocationRange && onWifiRange) {
                                llLocateOnRangeSignIn.setVisibility(View.GONE);
                            }
                        } else {
                            if (!onLocationRange && onWifiRange) {
                                llLocateRangeSignOut.setVisibility(View.GONE);
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                LogUtils.i(TAG, "考勤错误信息：" + result);
            }
        });
    }

    /**
     * 根据当前是否签到和签退 显示不同布局
     */
    private void ShowAndHideSign() {
        ProgressDialogHelper.show(NewAttendanceActivity.this);
        String url = Global.BASE_JAVA_URL + GlobalMethord.考勤记录 + "?startTime=" + dateToday + " 00:00:00" + "&endTime=" + dateToday + " 23:59:59";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<OaAttendance> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), OaAttendance.class);
                ll_before_signin.setVisibility(View.GONE);
                ll_before_signout.setVisibility(View.GONE);
                cl_after_signin.setVisibility(View.GONE);
                cl_after_signout.setVisibility(View.GONE);
                if (list != null && list.size() > 0) {
                    OaAttendance oaAttendance = list.get(0);
                    if (oaAttendance == null || oaAttendance.getCheckInTime() == null) {  //签到时间为空 当前未签到
                        ll_before_signin.setVisibility(View.VISIBLE);
                        getSetting(true);
                    } else if (oaAttendance.getCheckInTime() != null && oaAttendance.getCheckOutTime() == null) {  //签到时间不为空 签退时间为空 当前已经签到 没有签退
                        isSignIn = false;
                        getSetting(false);
                        cl_after_signin.setVisibility(View.VISIBLE);
                        ll_before_signout.setVisibility(View.VISIBLE);
                        if (oaAttendance.getPicSignin() != null) { //如果有签到图片就加载
                            signin_img.setVisibility(View.VISIBLE);
                            signin_img.loadImageByAttachIds(oaAttendance.getPicSignin());
                        } else {
                            signin_img.setVisibility(View.GONE);
                        }
                        String[] checkInTime = oaAttendance.getCheckInTime().split(" ");
                        if (oaAttendance.getComeLate()) {
                            tv_singin_time.setText(checkInTime[1] + "(迟到)");
                            tv_singin_time.setTextColor(Color.parseColor("#FF7F66"));
                        } else {
                            tv_singin_time.setText(checkInTime[1] + "(正常)");
                            tv_singin_time.setTextColor(Color.parseColor("#333333"));
                        }
                        tv_signin_address_after.setText(oaAttendance.getAddressSignin());
                    } else if (oaAttendance.getCheckOutTime() != null && oaAttendance.getCheckInTime() != null) { //签到时间和签退时间都不为空 说明已经签到签退
                        cl_after_signin.setVisibility(View.VISIBLE);
                        cl_after_signout.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(oaAttendance.getPicSignin())) { //如果有签到图片就加载
                            signin_img.setVisibility(View.VISIBLE);
                            signin_img.loadImageByAttachIds(oaAttendance.getPicSignin());
                        } else {
                            signin_img.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(oaAttendance.getPicSignout())) {
                            signout_img.setVisibility(View.VISIBLE);
                            signout_img.loadImageByAttachIds(oaAttendance.getPicSignout());
                        } else {
                            signout_img.setVisibility(View.GONE);
                        }
                        String[] checkInTime = oaAttendance.getCheckInTime().split(" ");
                        String[] checkOutTime = oaAttendance.getCheckOutTime().split(" ");
                        if (oaAttendance.getComeLate()) {
                            tv_singin_time.setText(checkInTime[1] + "(迟到)");
                            tv_singin_time.setTextColor(Color.parseColor("#FF7F66"));
                        } else {
                            tv_singin_time.setText(checkInTime[1] + "(正常)");
                            tv_singin_time.setTextColor(Color.parseColor("#333333"));
                        }
                        if (oaAttendance.getLeaveEarly()) {
                            tv_signout_time.setText(checkOutTime[1] + "(早退)");
                            tv_signout_time.setTextColor(Color.parseColor("#FF7F66"));
                        } else {
                            tv_signout_time.setText(checkOutTime[1] + "(正常)");
                            tv_signout_time.setTextColor(Color.parseColor("#333333"));
                        }
                        tv_signin_address_after.setText(oaAttendance.getAddressSignin());
                        tv_signout_address_after.setText(oaAttendance.getAddressSignout());
                    } else {
                        getSetting(true);
                        ll_before_signin.setVisibility(View.VISIBLE);
                    }
                } else {
                    getSetting(true);
                    ll_before_signin.setVisibility(View.VISIBLE);
                }
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
//                finish();
                showShortToast("加载失败,请稍后重试");
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
//                finish();
                showShortToast("加载失败,请稍后重试");
            }
        });

    }

    /***
     * 初始化最后更新时间
     */
    private void initUpdateTime() {
        mLastUpdateTime = new Date().getTime();
    }

    private void requestLocating() throws Exception {
        BaiduLocator.requestLocation(getApplicationContext(), NewAttendanceActivity.this);
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        Logger.i("BDLocationListener:::onReceiveLocation is running");
        if (location == null) {
            ShowAndHideSign();
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

            currentDistance();
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


            // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            // LinearLayout.LayoutParams.MATCH_PARENT,
            // LinearLayout.LayoutParams.WRAP_CONTENT);
            // // params.gravity = Gravity.LEFT;
            // mTvAddress.setLayoutParams(params);
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
//            mTvAddress.setText(mLocation);

            if (TextUtils.isEmpty(mLocation)) {
                tv_signin_address_name.setText("暂未定位到位置，点击可调整定位地址..");
                tv_signout_address_name.setText("暂未定位到位置，点击可调整定位地址..");
                tvOutAddress.setText("暂未定位到位置，点击可调整定位地址..");
            }
            iv_signin_cemera.setImageResource(R.drawable.icon_attendance_cemera);
            ivSignOutSide.setImageResource(R.drawable.icon_attendance_cemera);
            iv_signout_cemera.setImageResource(R.drawable.icon_attendance_cemera);
            spPath = "";
            tv_signin_address_name.setText(mLocation);
            tv_signout_address_name.setText(mLocation);
            tvOutAddress.setText(mLocation);
            initUpdateTime(); // 定位成功，更新记录时间
            Logger.e("BDLocationListener" + sb.toString());
            BaiduLocator.stop();

            String locationRect = EarthMapUtils.getLocationRect(mLat, mLog,
                    radiusMap);
            String url = "http://api.map.baidu.com/place/v2/search?query=楼$酒店$大厦$公司$小区$中心$公交$银行$学校$街道$路&bounds="
                    + locationRect
                    + "&output=json&ak=1lefPqdAWokm2tpRg3o9IhUfwjxvk1ci";
            getLocationList(url, mCountry, mCity);
            ShowAndHideSign();
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
                        bpList = JsonUtils.pareseJsonToList(
                                results, BaiduPlace.class);
                        Logger.i("地址个数::" + bpList.size());
                        if (bpList.size() > 0) {
                            if (bpList.size() > 1) {
                                for (int i = 0; i < bpList.size(); i++) {
                                    BaiduPlace temp = bpList.get(i);
                                    Logger.d("distance2::" + temp.name
                                            + temp.address + "---"
                                            + temp.location.lat + ","
                                            + temp.location.lng);
                                }

                                sortByDistance(bpList);

                                // 根据距离由近到远排序
                                for (int i = 0; i < bpList.size(); i++) {
                                    BaiduPlace temp = bpList.get(i);
                                    Logger.i("distance::" + temp.name
                                            + temp.address + "---"
                                            + temp.location.lat + ","
                                            + temp.location.lng);
                                }
                            }
                            BaiduPlace bPlace = null;
                            String bYkey = "";
                            int i = ViewHelper.getTimeCompareSize(TIME, currentTime);
                            if (i == 1) {  //上午 获取签到地点
                                bYkey = PreferceManager.getInsance().getValueBYkey("SigninAddressplace" + Global.mUser.getUuid());
                            } else {   //下午 获取签退地点
                                bYkey = PreferceManager.getInsance().getValueBYkey("SignoutAddressplace" + Global.mUser.getUuid());
                            }
                            for (BaiduPlace bp : bpList) {
                                if (bp.name.equals(bYkey)) {  //优先匹配用户上次首选坐标点
                                    bPlace = bp;
                                }
                            }
                            if (bPlace == null) {  //如果没有上次用户首选,则显示距离签到点最近的地点
                                bPlace = bpList.get(0);
                            }
                            mLatitude = bPlace.location.lat;
                            mLongitude = bPlace.location.lng;
                            mLocation = getLocationAddress(country, city, bPlace);

                            currentDistance();
                            ShowAndHideSign();
                            BaiduPlace finalBPlace = bPlace;
                            NewAttendanceActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(context, "当前定位地址: " + mLocation, Toast.LENGTH_SHORT).show();
                                    tv_signin_address_name.setText(finalBPlace.name);
                                    tv_signout_address_name.setText(finalBPlace.name);
                                    tvOutAddress.setText(finalBPlace.name);
                                    tvOutAddressName.setText(finalBPlace.address);
                                    tv_sigin_address.setText(finalBPlace.address);
                                    tv_signout_address.setText(finalBPlace.address);
//                                    mTvAddress.setGravity(Gravity.LEFT
//                                            | Gravity.CENTER_VERTICAL);
//                                    mTvAddress.setText(mLocation);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    Logger.e("" + e.getMessage());
                }
            }
        });
    }


    /**
     * 根据距离对POI集合由近到远排序
     */
    private void sortByDistance(List<BaiduPlace> list) {
        // 计算个点和初次定位的距离，然后保存到集合
        for (int i = 0; i < list.size(); i++) {
            // 如果个数大于1，计算距离最近的一个
            BaiduPlace item = list.get(i);
            // 计算和初次定位距离
            double distance = Math.sqrt(Math.pow(item.location.lat - mLat, 2)
                    + Math.pow(item.location.lng - mLog, 2));
            list.get(i).setDistance(distance);
        }
        if (end == null) {   //如果没有固定签到地点则按照距离定位点由近到远排序
            // 根据距离由近到远排序
            for (int i = 0; i < list.size(); i++) {
                // 计算和初次定位距离
                double distance1 = list.get(i).getDistance();
                for (int j = 0; j < list.size(); j++) {
                    double distance2 = list.get(j).getDistance();
                    if (distance2 > distance1) {
                        BaiduPlace temp = list.get(j);
                        list.set(j, list.get(i));
                        list.set(i, temp);
                    }
                }
            }
        } else {  //如果有固定签到地点则按照 距离签到地点的距离 由近到远排序
            //根据签到地点的距离 由距离从近到远排序
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    BaiduPlace temp = null;
                    LatLng start1 = new LatLng(list.get(i).location.lat, list.get(i).location.lng);
                    LatLng start2 = new LatLng(list.get(j).location.lat, list.get(j).location.lng);
                    double distance1 = ViewHelper.getDistance(end, start1);
                    double distance2 = ViewHelper.getDistance(end, start2);
                    if (distance1 < distance2) {
                        temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }

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
     * 调用相机拍照
     */
    private void takeCemera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用系统相机
        mPictureFile = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        File file = new File(PhotoHelper.PATH, mPictureFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentUri = FileProvider.getUriForFile(NewAttendanceActivity.this, "com.biaozhunyuan.tianyi.fileprovider", file);
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

//        intent.putExtra("isShowOutSide", true);
        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }

    /**
     * 当前距离签到点距离
     */
    private void currentDistance() {
        /*LatLng start = new LatLng(mLatitude, mLongitude);
        if (end != null) {
            double distance = ViewHelper.getDistance(start, end);
            if (distance <= range) { //当前距离公司距离如果小于可签到范围 为正常签到
                onLocationRange = true;
                if (isSignIn) {
                    llWifiOnRangeSignIn.setVisibility(View.VISIBLE);
                    ivIsOnWifiRangeSignIn.setImageResource(R.drawable.icon_status_selected);
                    tvWifiNameSignIn.setText("已经进入指定地点打卡范围内");
                } else {
                    llWifiOnRangeSignOut.setVisibility(View.VISIBLE);
                    tvWifiNameSignOut.setText("已经进入指定地点打卡范围内");
                    ivIsOnWifiRangeSignOut.setImageResource(R.drawable.icon_status_selected);
                }
            } else if (distance > range && !isCanSignOutRange) {  //当前距离公司距离如果小于可签到范围 为外出签到
                if (isSignIn) {
                    llWifiOnRangeSignIn.setVisibility(View.VISIBLE);
                    tvWifiNameSignIn.setText("未进入指定地点打卡范围内");
                    ivIsOnWifiRangeSignIn.setImageResource(R.drawable.icon_del);
                } else {
                    llWifiOnRangeSignOut.setVisibility(View.VISIBLE);
                    tvWifiNameSignOut.setText("未进入指定地点打卡范围内");
                    ivIsOnWifiRangeSignOut.setImageResource(R.drawable.icon_del);
                }
                onLocationRange = false;
            }
        } else {   //如果没有固定签到地点则统一视为正常签到和外出签到

        }*/
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
                currentDistance();
            } else if (requestCode == CAMERA_TAKE_HELPER) {
                if (data == null) {
                    Logger.i("onActivityResult::" +
                            "onActivityResult data isnull");
                    // Toast.makeText(TagActivity.this, "调用系统相机异常",
                    // Toast.LENGTH_SHORT).show();
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
                    // mImageViewCamera.setImageBitmap(uploadPhoto);
//                    iv_photo.setImageBitmap(PhotoHelper
//                            .disposeBitmapForListView(spPath));
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
                    initUpdateTime();
                } else {
                    showShortToast("调用系统相机异常");
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
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }

    }

//    /**
//     * 自适应屏幕宽高时防止拉伸 ImageView变形
//     * @param iv
//     */
//    private void setImageSquare(ImageView iv){
//        ViewTreeObserver vto = iv.getViewTreeObserver();
//
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                iv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int height = iv.getMeasuredHeight();
//                int width = iv.getMeasuredWidth();
//                if (height > width) {
//                    iv.setMaxHeight(width);
//                } else if (height < width) {
//                    iv.setMaxWidth(height);
//                } else {
//
//                }
//            }
//        });
//    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    tc_singin.setText(getTime());
                    tc_singout.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };

    //获得当前年月日时分秒星期
    public String getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒
        if (mSecond.length() == 1) {
            mSecond = "0" + mSecond;
        }
        if (mHour.length() == 1) {
            mHour = "0" + mHour;
        }
        if (mMinute.length() == 1) {
            mMinute = "0" + mMinute;
        }

        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
//        return mYear + "年" + mMonth + "月" + mDay+"日"+"  "+"星期"+mWay+"  "+mHour+":"+mMinute+":"+mSecond;
        return mHour + ":" + mMinute + ":" + mSecond;
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
