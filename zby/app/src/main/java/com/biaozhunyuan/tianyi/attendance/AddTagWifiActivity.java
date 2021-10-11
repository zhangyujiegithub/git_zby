package com.biaozhunyuan.tianyi.attendance;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wam on 2017/5/5.
 * <p/>
 * 添加wifi列表  根据周围的wifi列表添加WiFi
 */
public class AddTagWifiActivity extends BaseActivity {

    private Context mContext;
    private BoeryunHeaderView headerView;
    private NoScrollListView lv_wifi;
    private Button btn_sure;
    private CommanAdapter<WifiInfo> adapter;
    private List<ScanResult> scanResults;
    private List<WifiInfo> wifiList = new ArrayList<>();
    private List<WifiInfo> returnWifiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi);
        initViews();
        initData();
        setOnEvent();
    }

    private void initData() {
        mContext = getBaseContext();
        scanResults = getWifiList();
        for (int i = 0; i < scanResults.size(); i++) {
            ScanResult result = scanResults.get(i);
            WifiInfo info = new WifiInfo();
            info.BSSID = result.BSSID;
            info.SSID = result.SSID;
            wifiList.add(info);
        }
        getIntentData();
        adapter = getAdapter(wifiList);
        lv_wifi.setAdapter(adapter);
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            returnWifiList = (List<WifiInfo>) getIntent().getSerializableExtra("wifiList");
            if (returnWifiList != null) {
                for (WifiInfo info : wifiList) {
                    for (WifiInfo info1 : returnWifiList) {
                        if (info.BSSID.equals(info1.BSSID)) {
                            info.isSelect = true;
                        }
                    }
                }
            }
        }
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_add_tag_setting);
        lv_wifi = (NoScrollListView) findViewById(R.id.lv_wifi_tag_setting);
        btn_sure = (Button) findViewById(R.id.btn_save_tag_setting);
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

        lv_wifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WifiInfo info = wifiList.get(position);
                if (returnWifiList == null) {
                    returnWifiList = new ArrayList<>();
                }
                info.isSelect = !info.isSelect;
                if (info.isSelect) {
                    returnWifiList.add(info);
                } else {
                    returnWifiList.remove(info);
                }
                adapter.notifyDataSetChanged();
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTagWifiActivity.this, TagSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("selectWifiList", (Serializable) returnWifiList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private List<ScanResult> getWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        List<ScanResult> scanResults = wifiManager.getScanResults();
        return scanResults;
    }


    private CommanAdapter<WifiInfo> getAdapter(List<WifiInfo> list) {
        return new CommanAdapter<WifiInfo>(list, mContext, R.layout.item_wifi_list) {
            @Override
            public void convert(int position, WifiInfo item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_item_wifi_list, item.SSID);
                viewHolder.setTextValue(R.id.tv_bssid_item_wifi_list, item.BSSID);
                ImageView iv_delete = viewHolder.getView(R.id.iv_delete_item_wifi_list);
                ImageView iv = viewHolder.getView(R.id.iv_select_item_wifi_list);

                iv_delete.setVisibility(View.GONE);

                if (item.isSelect) {
                    iv.setImageResource(R.drawable.select_on);
                } else {
                    iv.setImageResource(R.drawable.select_off);
                }
            }
        };
    }
}
