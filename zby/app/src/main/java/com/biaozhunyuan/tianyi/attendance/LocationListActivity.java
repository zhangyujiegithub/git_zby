package com.biaozhunyuan.tianyi.attendance;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.EarthMapUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

/***
 * ?????? ????????????????????????
 *
 * @author K
 */
public class LocationListActivity extends BaseActivity implements
        OnGetGeoCoderResultListener {

    /**
     * ??????
     */
    public final static String ADDRESS = "address";

    // ???????????????????????????????????????????????????
    GeoCoder mSearch = null;
    BaiduMap mBaiduMap = null;
    TextureMapView mMapView = null;
    // ????????????
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // ??????????????????
    private int mSelectPos = -1; // ????????????
    private CommanAdapter<BaiduPlace> mAdapter;

    public static final String LONGITUDE = "LocationLongitude";
    public static final String LATITUDE = "LocationLatitude";
    public static final String RESULT = "LocationResult";

    public static final String TIME = "12:00:00";

    private CommonPopupWindow popupWindow;//

    private final int SUCCESS_GET_LOCATION_LIST = 1;
    private final int FAILURE_GET_LOCATION_LIST = 2;

    private int radiusMap = 150;// ??????????????????
    private int Max_RadiusMap = 500;// ??????????????????

    private Context mContext;
    private List<BaiduPlace> mList;
    private List<BaiduPlace> allMyList = new ArrayList<BaiduPlace>();

    private ImageView ivBack;
    private ListView lv;
    private TextView tvSort;

    private Double mLat;
    private Double mLng;
    private Double mRange;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUCCESS_GET_LOCATION_LIST:
                    ProgressDialogHelper.dismiss();
                    mAdapter = getAdapter(mList);
                    lv.setAdapter(mAdapter);
                    break;
                case FAILURE_GET_LOCATION_LIST:
                    ProgressDialogHelper.dismiss();
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private LatLng end = null;
    private String currentTime;
    private TextView tv_search;
    private RelativeLayout rl;
    private SuggestionSearch suggestionSearch;
    private List<BaiduPlace> seachList;
    private Boolean isShowSearch;
    private Boolean isShowOutSide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);
        try {
            mLat = Double.parseDouble(PreferceManager.getInsance().getValueBYkey("lat"));
            mLng = Double.parseDouble(PreferceManager.getInsance().getValueBYkey("lng"));
            mRange = Double.parseDouble(PreferceManager.getInsance().getValueBYkey("range"));
            currentTime = ViewHelper.formatDateToStr(new Date(), "kk:mm:ss");
            if (mLat != 0.0 && mLng != 0.0 && mRange != 0.0) {
                end = new LatLng(mLat, mLng);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        initViews();
        init();
        setEventsListener();
        initMap();
    }

    private void initMap() {
        // ???????????????
        mMapView = (TextureMapView) findViewById(R.id.map_location_list);
        mBaiduMap = mMapView.getMap();
        // ?????????????????????
        mMapView.showZoomControls(false);
        // ???????????????????????? [3-21]
        mBaiduMap.setMaxAndMinZoomLevel(18, 18);
        // ??????????????????
        mBaiduMap.setMyLocationEnabled(true);
        // ???????????????
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // ??????gps
        option.setCoorType("bd09ll"); // ??????????????????
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        // ??????????????????????????????????????????
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        if (mLatitude == 0 || mLongitude == 0) {

        } else {
            isFirstLoc = false;
            reloadLocation(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ???activity??????onResume?????????mMapView.onResume()?????????????????????????????????
        mMapView.onResume();

    }

    @Override
    protected void onDestroy() {

        // ?????????????????????
        mLocClient.stop();
        // ??????????????????
        mBaiduMap.setMyLocationEnabled(false);
        try {
            if (mMapView != null) {
                // ???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
                mMapView.onDestroy();
                mMapView = null;
            }
            if (mSearch != null) {
                mSearch.destroy();
            }
        } catch (Exception e) {
            Logger.e(TAG + "" + e.getLocalizedMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // ???activity??????onPause?????????mMapView.onPause()?????????????????????????????????
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_LONG).show();
            return;
        }

        // ?????????????????????
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("?????????%f ?????????%f",
                result.getLocation().latitude, result.getLocation().longitude);
//		Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        Logger.i(TAG + result.getAddress() + "");
    }

    private double mLatitude;
    private double mLongitude;

    private void init() {
        seachList = new ArrayList<>();
        mContext = this;
        mLatitude = getIntent().getDoubleExtra(LATITUDE, 0);
        mLongitude = getIntent().getDoubleExtra(LONGITUDE, 0);
        isShowSearch = getIntent().getBooleanExtra("isShowSearch", false);
        isShowOutSide = getIntent().getBooleanExtra("isShowOutSide", false);
        // mLatitude = 40.0725815874954;
        // mLongitude = 116.256290671531;
        if (isShowSearch) {
            tv_search.setVisibility(View.VISIBLE);
        } else {
            tv_search.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back_loaction_list);
        tvSort = (TextView) findViewById(R.id.tv_sort_location_list);
        lv = (ListView) findViewById(R.id.lv_locationlist);
        tv_search = findViewById(R.id.tv_search_location_list);
        rl = findViewById(R.id.rl);
    }

    private void setEventsListener() {
        tv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw();
            }
        });

        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSort.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (radiusMap <= 100) {
                // radiusMap = 200;
                // tvSort.setText("200??????");
                // } else {
                // radiusMap = 100;
                // tvSort.setText("100??????");
                // }
                // reloadLocationList(mLongitude, mLatitude);

                if (mSelectPos == -1) {
                    showShortToast("?????????????????????");
                } else {
                    BaiduPlace place = mList.get(mSelectPos);
                    int i = ViewHelper.getTimeCompareSize(TIME, currentTime);
                    if (i == 1) {  //?????? ??????????????????
                        PreferceManager.getInsance().saveValueBYkey("SigninAddressplace" + Global.mUser.getUuid(), place.name);
                    } else {   //?????? ??????????????????
                        PreferceManager.getInsance().saveValueBYkey("SignoutAddressplace" + Global.mUser.getUuid(), place.name);
                    }
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(RESULT, place);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectPos = position;
                final BaiduPlace place = mAdapter.getItem(position);
//                mAdapter.notifyDataSetChanged();
                // Intent intent = new Intent();
                // Bundle bundle = new Bundle();
                // bundle.putSerializable(RESULT, place);
                // intent.putExtras(bundle);
                // setResult(RESULT_OK, intent);
                // finish();
                reReverseGeoCode(place.location.lat, place.location.lng);

                String url = "http://api.map.baidu.com/geocoder?location=" +
                        place.location.lat + "," + place.location.lng + "&output=json&key=" +
                        "1lefPqdAWokm2tpRg3o9IhUfwjxvk1ci";
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
                            String addressComponent = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "result"), "addressComponent");
                            String district = JsonUtils.getStringValue(addressComponent, "district");
                            String province = JsonUtils.getStringValue(addressComponent, "province");
                            String city = JsonUtils.getStringValue(addressComponent, "city");
                            place.city = city;
                            place.province = province;
                            place.district = district;
                            int i = ViewHelper.getTimeCompareSize(TIME, currentTime);
                            if (i != 0) {
                                if (i == 1) {  //?????? ??????????????????
                                    PreferceManager.getInsance().saveValueBYkey("SigninAddressplace" + Global.mUser.getUuid(), place.name);
                                } else {   //?????? ??????????????????
                                    PreferceManager.getInsance().saveValueBYkey("SignoutAddressplace" + Global.mUser.getUuid(), place.name);
                                }
                            }
                            resultIntent(place);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            int i = ViewHelper.getTimeCompareSize(TIME, currentTime);
                            if (i != 0) {
                                if (i == 1) {  //?????? ??????????????????
                                    PreferceManager.getInsance().saveValueBYkey("SigninAddressplace" + Global.mUser.getUuid(), place.name);
                                } else {   //?????? ??????????????????
                                    PreferceManager.getInsance().saveValueBYkey("SignoutAddressplace" + Global.mUser.getUuid(), place.name);
                                }
                            }
                            resultIntent(place);
                        }

                    }
                });

            }
        });


    }

    private void resultIntent(BaiduPlace bp) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESULT, bp);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void getLocationList(final String url) {
        ProgressDialogHelper.show(mContext, "?????????..");
        Logger.i(TAG + url);
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
                        mList = JsonUtils.pareseJsonToList(results,
                                BaiduPlace.class);
                        if (mList == null || mList.size() == 0) {
                            if (radiusMap == Max_RadiusMap) {  //????????????????????????????????? ????????????????????? ???????????????
                                if (getIntent().getExtras().getSerializable("location_place") != null) {
                                    BaiduPlace bp = (BaiduPlace) getIntent().getExtras().getSerializable("location_place");
                                    mList.add(bp);
                                    sortAndSetAdapter();
                                } else {
                                    showShortToast("???????????????????????????");
                                }
                            } else { //???????????????150????????????????????????????????????????????????????????????????????????
                                radiusMap = Max_RadiusMap;
                                reloadLocationList(mLongitude, mLatitude, true);
                            }
                        } else {
                            sortAndSetAdapter();
                        }
                    } else {
                        handler.sendEmptyMessage(FAILURE_GET_LOCATION_LIST);
                    }
                } catch (Exception e) {
                    Logger.e(TAG + "::" + e.getMessage());
                    handler.sendEmptyMessage(FAILURE_GET_LOCATION_LIST);
                }
            }
        });
    }

    /**
     * ??????????????????????????????????????? ????????????????????????
     */
    private void sortAndSetAdapter(){
        allMyList = mList;
        if (end != null) {
            for (int i = 0; i < mList.size(); i++) { //?????? ?????????????????????????????? ?????????????????????????????????????????????
                for (int j = 0; j < mList.size(); j++) {
                    BaiduPlace temp = null;
                    LatLng start1 = new LatLng(mList.get(i).location.lat, mList.get(i).location.lng);
                    LatLng start2 = new LatLng(mList.get(j).location.lat, mList.get(j).location.lng);
                    double distance1 = ViewHelper.getDistance(end, start1);
                    double distance2 = ViewHelper.getDistance(end, start2);
                    if (distance1 < distance2) {
                        temp = mList.get(i);
                        mList.set(i, mList.get(j));
                        mList.set(j, temp);
                    }
                }
            }
        }
        Logger.i(TAG + "????????????" + mList.size());
        handler.sendEmptyMessage(SUCCESS_GET_LOCATION_LIST);
    }


    private CommanAdapter<BaiduPlace> getAdapter(final List<BaiduPlace> list) {
        return new CommanAdapter<BaiduPlace>(list, mContext,
                R.layout.item_baiduplace) {
            @Override
            public void convert(int position, BaiduPlace item, BoeryunViewHolder viewHolder) {
                TextView tvName = viewHolder.getView(R.id.tv_name_location_item);
                TextView tvAddress = viewHolder.getView(R.id.tv_address_location_item);
                ImageView ivCheck = viewHolder.getView(R.id.iv_checked_location_item);
                tvAddress.setText(item.address + "");
                if (mSelectPos == position) {
                    ivCheck.setVisibility(View.VISIBLE);
                } else {
                    ivCheck.setVisibility(View.INVISIBLE);
                }
                if (end != null) {
                    double lat = item.location.lat;
                    double lng = item.location.lng;
                    LatLng start = new LatLng(lat, lng);
                    double distance = ViewHelper.getDistance(start, end);
                    if ((distance > mRange) && isShowOutSide) {  //??????????????????????????????????????????????????? ???????????????
                        tvName.setText(item.name + "   (????????????)");
                        tvName.setTextColor(Color.parseColor("#00aeef"));
                    } else {
                        tvName.setText(item.name + "");
                        tvName.setTextColor(Color.parseColor("#000000"));
                    }
                } else {
                    tvName.setText(item.name + "");
                }
            }
        };
    }

    /**
     * ??????????????????
     */
    private void reloadLocationList(double mLong, double mLati, boolean isLocation) {
        String locationRect = EarthMapUtils.getLocationRect(mLati, mLong,
                radiusMap);
        String url = "http://api.map.baidu.com/place/v2/search?query=???$??????$??????$??????$??????$??????$??????$??????$??????$???"
                + "&bounds=" + locationRect
//                + "&location=" + mLati + "," + mLong
//                + "&radius=" + radiusMap
                + "&output=json&ak=1lefPqdAWokm2tpRg3o9IhUfwjxvk1ci"
                + "&tag=??????,??????,??????,????????????,??????,????????????,????????????,????????????," +
                "????????????,????????????,??????,????????????,????????????,??????,?????????,????????????,????????????,?????????,????????????"
                + "&scope=2";
        if (isLocation) {
            getLocationList(url);
        }
    }

    /***
     * ??????????????????
     */
    private void reloadLocation(boolean isLocation) {
        if (mLatitude != 0 && mLongitude != 0) {
            reloadLocationList(mLongitude, mLatitude, isLocation);
            reReverseGeoCode(mLatitude, mLongitude);
        }
    }

    private void reReverseGeoCode(double lat, double lng) {
        LatLng ptCenter = new LatLng(lat, lng);
        // ???Geo??????
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
    }

    /**
     * ??????SDK????????????
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view ???????????????????????????????????????
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // ?????????????????????????????????????????????????????????0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                        .newMapStatus(builder.build()));

//				Toast.makeText(
//						mContext,
//						location.getLongitude() + "-????????????-"
//								+ location.getLatitude(), Toast.LENGTH_LONG)
//						.show();
                mLatitude = ll.latitude;
                mLongitude = ll.longitude;
                reloadLocation(true);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void popWiw() {

        popupWindow = new CommonPopupWindow.Builder(LocationListActivity.this)
                //??????PopupWindow??????
                .setView(R.layout.popup_search_location)
                //????????????
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
//                //????????????
//                .setAnimationStyle(R.style.AnimDown)
                //?????????????????????????????????0.0f-1.0f ??????????????? 1.0f?????????
//                .setBackGroundLevel(0.65f)

                //??????PopupWindow?????????View???????????????
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        BoeryunSearchView seach = view.findViewById(R.id.seach_button);
                        seach.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
                            @Override
                            public void OnSearched(String str) {
//                                ProgressDialogHelper.show(LocationListActivity.this);
                                if (suggestionSearch == null) {
                                    suggestionSearch = SuggestionSearch.newInstance();
                                }
                                //????????????????????????
                                suggestionSearch.requestSuggestion((new SuggestionSearchOption())
                                        .keyword(str)
                                        .city("??????"));
                            }
                        });
                        seach.setOnEditorActionListener(new BoeryunSearchView.OnEditorActionListener() {
                            @Override
                            public void OnDone() {
                                popupWindow.dismiss();
                            }
                        });
                    }
                })
                //??????????????????????????? ?????????true
                .setOutsideTouchable(true)
                //????????????
                .create();
        popupWindow.showAsDropDown(rl);

        //?????????????????????????????????
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    return;
                } else {
                    List<SuggestionResult.SuggestionInfo> allSuggestions = res.getAllSuggestions();
                    seachList.clear();
                    for (SuggestionResult.SuggestionInfo ss : allSuggestions) {
                        BaiduPlace baiduPlace = new BaiduPlace();
                        BaiduPlace.Location location = new BaiduPlace.Location();
                        baiduPlace.location = location;
                        baiduPlace.name = ss.key;
                        baiduPlace.address = ss.city + ss.district;
                        if (ss.pt != null) {
                            mLatitude = ss.pt.latitude;
                            mLongitude = ss.pt.longitude;
//                            reloadLocation(true);
                            baiduPlace.location.lat = ss.pt.latitude;
                            baiduPlace.location.lng = ss.pt.longitude;
//                            break;
                        }
                        seachList.add(baiduPlace);
                    }
                    if (seachList.size() > 0) {
                        if (mAdapter == null) {
                            mAdapter = getAdapter(seachList);
                        } else {
                            mAdapter.changeData(seachList);
                        }
                        mLatitude = seachList.get(0).location.lat;
                        mLongitude = seachList.get(0).location.lng;
                        reloadLocation(false);
                    } else {
                        showShortToast("???????????????????????????");
                    }
//
                }
                ProgressDialogHelper.dismiss();
            }
        };
        //??????????????????????????????
        suggestionSearch = SuggestionSearch.newInstance();
        //?????????????????????????????????
        suggestionSearch.setOnGetSuggestionResultListener(listener);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //??????????????????????????????
                suggestionSearch.destroy();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
    }

}
