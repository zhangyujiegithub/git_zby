package com.biaozhunyuan.tianyi.attendance;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;

/**
 * 地图页面，传入经纬度用来显示传入的地点的定位
 */
public class MapViewActivity extends BaseActivity {

    private CardView back;
    private MapView mapView;
    private double mLongitude;
    private double mLatitude;
    private BaiduMap baiduMap;
    private BitmapDescriptor bitmapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        getIntentData();
        initViews();
        initData();
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mLongitude = (double) extras.get("longitude");
            mLatitude = (double) extras.get("latitude");
        }
    }

    private void initViews() {
        back = findViewById(R.id.back);
        mapView = findViewById(R.id.map_view);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        baiduMap = mapView.getMap();
        //设置是否显示比例尺控件
        mapView.showScaleControl(true);
        //设置是否显示缩放控件
        mapView.showZoomControls(true);
        // 删除百度地图LoGo
        // 设置marker图标
        bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker_map);
        // 定义Maker坐标点
        LatLng point = new LatLng(mLatitude, mLongitude);


        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);



        // 构建MarkerOption，用于在地图上添加Marker
        MarkerOptions options = new MarkerOptions().position(point)
                .icon(bitmapMarker);
        // 在地图上添加Marker，并显示
        baiduMap.addOverlay(options);
        //实例化一个地理编码查询对象
        GeoCoder geoCoder = GeoCoder.newInstance();
        //设置反地理编码位置坐标
        ReverseGeoCodeOption op = new ReverseGeoCodeOption();
        op.location(point);
        //发起反地理编码请求(经纬度->地址信息)
        geoCoder.reverseGeoCode(op);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
                //获取点击的坐标地址
                String address = arg0.getAddress();
                System.out.println("address=" + address);
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult arg0) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}
