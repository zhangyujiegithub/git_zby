package com.biaozhunyuan.tianyi.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.Request;

/**
 * Created by wam on 2017/5/5.
 * <p/>
 * 考勤设置页面 可选择指定wifi和指定地点
 */
public class TagSettingActivity extends BaseActivity {

    public static int REQUEST_SELECT_WIFI = 1;

    public static final int SELECT_LOCATION_CODE = 1101; // 选择地址
    private Context mContext;

    private BoeryunHeaderView headerView;
    private LinearLayout addWifi;
    private ArrayList<WifiInfo> wifiList = new ArrayList<>();
    private HashSet<WifiInfo> wifiSet = new HashSet<>();
    private ArrayList<LocationInfo> locationList = new ArrayList<>();
    private NoScrollListView lv_wifi;
    private LinearLayout ll_fast_sign;
    private LinearLayout ll_add_address;
    private LinearLayout ll_choose_range;
    private ImageView iv_fast_sign;
    private TextView tv_range;
    private NoScrollListView lv_location;
    private boolean isFastSign = false;
    private DictIosPickerBottomDialog bottomDialog;
    private Button btn_save;

    private int range = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_setting);
        initViews();
        getSetting();
        setOnEvent();
    }


    private void initViews() {
        mContext = getBaseContext();
        bottomDialog = new DictIosPickerBottomDialog(TagSettingActivity.this);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_tag_setting);
        addWifi = (LinearLayout) findViewById(R.id.ll_add_wifi_tag_setting);
        lv_wifi = (NoScrollListView) findViewById(R.id.lv_tag_setting_wifi_list);
        tv_range = (TextView) findViewById(R.id.tv_range_tag_setting);
        btn_save = (Button) findViewById(R.id.btn_save_tag_setting);
        ll_fast_sign = (LinearLayout) findViewById(R.id.ll_is_sign_tag_setting);
        iv_fast_sign = (ImageView) findViewById(R.id.iv_is_auto_sign_tag_setting);
        ll_add_address = (LinearLayout) findViewById(R.id.ll_add_area_tag_setting);
        ll_choose_range = (LinearLayout) findViewById(R.id.ll_choose_range_tag_setting);
        lv_location = (NoScrollListView) findViewById(R.id.lv_tag_location_wifi_list);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "requestCode:" + requestCode + ",resultCode:" + resultCode);
        switch (requestCode) {
            case 1:
                if (data != null && data.getExtras() != null) {
                    wifiSet.clear();
                    ArrayList<WifiInfo> list = (ArrayList<WifiInfo>) data.getExtras().getSerializable("selectWifiList");
                    for (int i = 0; i < list.size(); i++) {
                        wifiSet.add(list.get(i));
                    }
                    wifiList.clear();
                    wifiList.addAll(wifiSet);
                    if (wifiList != null) {
                        lv_wifi.setAdapter(getAdapter(wifiList));
                    }
                }
                break;

            case SelectLocationBiz.SELECT_LOCATION_CODE:
                BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
                        requestCode, data);
                if (place != null) {
                    String address = getLocationAddress("", "", place);
                    LogUtils.i(TAG, "address:" + address);
                    if (place.location != null) {
                        double mLatitude = place.location.lat;
                        double mLongitude = place.location.lng;

                        LocationInfo info = new LocationInfo();
                        info.Address = address;
                        info.Latitude = mLatitude;
                        info.Longitude = mLongitude;

                        boolean isRepeat = false;
                        for (LocationInfo item : locationList) {
                            if (info.Address.equals(item.Address)) {
                                isRepeat = true;
                            }
                        }
                        if (!isRepeat) {
                            locationList.add(info);
                        }

                        lv_location.setAdapter(getLocationAdapter(locationList));
                    }
                }
                break;
        }
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


        ll_fast_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastSign) {
                    iv_fast_sign.setImageResource(R.drawable.isfalse);
                    isFastSign = false;
                } else {
                    iv_fast_sign.setImageResource(R.drawable.istrue);
                    isFastSign = true;
                }
            }
        });

        /**
         * 添加WIFI
         */
        addWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagSettingActivity.this, AddTagWifiActivity.class);
                intent.putExtra("wifiList", wifiList);
                startActivityForResult(intent, REQUEST_SELECT_WIFI);
            }
        });

        /**
         * 添加地址列表
         */
        ll_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LocationListActivity.class);
                intent.putExtra(LocationListActivity.LATITUDE, 0);
                intent.putExtra(LocationListActivity.LONGITUDE, 0);

                intent.putExtra("isShowSearch", true);
                startActivityForResult(intent,
                        SELECT_LOCATION_CODE);
            }
        });


        /**
         * 选择有效范围
         */
        ll_choose_range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.show(new ArrayList<String>() {
                    {
                        add("100");
                        add("200");
                        add("500");
                        add("1000");
                    }
                });
                bottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        switch (index) {
                            case 0:
                                range = 100;
                                tv_range.setText("100米");
                                break;
                            case 1:
                                range = 200;
                                tv_range.setText("200米");
                                break;
                            case 2:
                                range = 500;
                                tv_range.setText("500米");
                                break;
                            case 3:
                                range = 1000;
                                tv_range.setText("1000米");
                                break;
                        }
                    }
                });
            }
        });


        /**
         * 保存考勤设置
         */
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
            }
        });
    }


    private CommanAdapter<WifiInfo> getAdapter(final List<WifiInfo> list) {
        return new CommanAdapter<WifiInfo>(list, mContext, R.layout.item_wifi_list) {
            @Override
            public void convert(final int position, WifiInfo item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_wifi_list, item.SSID);
                viewHolder.setTextValue(R.id.tv_bssid_item_wifi_list, item.BSSID);

                ImageView iv = viewHolder.getView(R.id.iv_select_item_wifi_list);

                iv.setVisibility(View.GONE);

                ImageView iv_delete = viewHolder.getView(R.id.iv_delete_item_wifi_list);

                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });

                if (item.isSelect) {
                    iv.setImageResource(R.drawable.select_on);
                } else {
                    iv.setImageResource(R.drawable.select_off);
                }
            }
        };
    }


    private CommanAdapter<LocationInfo> getLocationAdapter(final List<LocationInfo> list) {
        return new CommanAdapter<LocationInfo>(list, mContext, R.layout.item_wifi_list) {
            @Override
            public void convert(final int position, LocationInfo item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_wifi_list, item.Address);
                TextView tv = viewHolder.getView(R.id.tv_bssid_item_wifi_list);
                ImageView iv_delete = viewHolder.getView(R.id.iv_delete_item_wifi_list);
                tv.setVisibility(View.GONE);

                ImageView iv = viewHolder.getView(R.id.iv_select_item_wifi_list);

                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });

                iv.setVisibility(View.GONE);
            }
        };
    }


    /**
     * 是否极速考勤
     */
    private void isFastSign() {
        if (isFastSign) {
            iv_fast_sign.setImageResource(R.drawable.istrue);
        } else {
            iv_fast_sign.setImageResource(R.drawable.isfalse);
        }
    }


    /**
     * 获取AttendanceSetting
     */
    private void getSetting() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取企业配置 + "?name=考勤配置";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(TAG, "考勤信息：" + response);
                AttendanceSetting setting = null;
                try {
                    setting = JsonUtils.jsonToEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "value"), AttendanceSetting.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (setting != null) {
                    wifiList = setting.WifiList;
                    for (WifiInfo info : setting.WifiList) {
                        wifiSet.add(info);
                    }
                    lv_wifi.setAdapter(getAdapter(wifiList));
                    locationList = setting.LocationList;
                    lv_location.setAdapter(getLocationAdapter(locationList));
                    range = setting.Range;
                    tv_range.setText(range + "米");
                    isFastSign = setting.IsFastSign;
                    isFastSign();
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
     * 保存AttendanceSetting
     */
    private void saveSetting() {
        AttendanceSetting setting = new AttendanceSetting();
        setting.WifiList = wifiList;
        setting.IsFastSign = isFastSign;
        setting.LocationList = locationList;
        setting.Range = range;

        String value = JSON.toJSONString(setting);
        String url = Global.BASE_JAVA_URL + GlobalMethord.添加企业配置;//+ "?name=考勤配置&value=" + value

        JSONObject jo = new JSONObject();
        try {
            jo.put("name", "考勤配置");
            jo.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("保存成功");
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(result);
            }
        });
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
        return address;
    }
}
