/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.biaozhunyuan.tianyi.barcode;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.activity.Inventory;
import com.biaozhunyuan.tianyi.activity.InventoryList;
import com.biaozhunyuan.tianyi.barcode.utils.Constant;
import com.biaozhunyuan.tianyi.barcode.zxing.ScanListener;
import com.biaozhunyuan.tianyi.barcode.zxing.ScanManager;
import com.biaozhunyuan.tianyi.barcode.zxing.decode.DecodeThread;
import com.biaozhunyuan.tianyi.barcode.zxing.decode.Utils;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.ScanImageView;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


/**
 * 进销存出入库扫码页面（表单跳转）
 */
public final class InvoiceScanActivity extends BaseActivity implements ScanListener, View.OnClickListener {
    private SurfaceView scanPreview = null;
    private View scanContainer;
    private View scanCropView;
    private ImageView scanLine;
    private ScanManager scanManager;
    private TextView iv_light;
    private TextView qrcode_g_gallery;
    private TextView qrcode_ic_back;
    final int PHOTOREQUESTCODE = 1111;

    private Button rescan;
    private ScanImageView scan_image;
    private int scanMode;//扫描模型（条形，二维码，全部）
    private TextView scan_hint;
    private ListView shopList;
    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    private RelativeLayout rlContainer;
    private int existPosition = -1; //记录已存在列表的物品条目位置
    private List<Inventory> inventoryList;//货物详细信息的集合
    private InventoryList returnInventoryList;//货物详细信息的集合
    private CommanAdapter<Inventory> inventoryAdapter;
    private Bitmap mBitmap = null; //扫描后的条形码图片
    private boolean isSingle = true; //连续扫描或者单次扫描
    public static String IS_SINGLE_SWEEP = "IS_SINGLE_SWEEP";
    private static String FORM_NAME = "formName";
    private static String RESULT_BAR_CODE = "RESULT_BAR_CODE";
    private BoeryunHeaderView headerView;
    private String formName = "";
    private String scanBarcodeUrl = "";//进销存扫描条形码根据barcode获取商品详情的接口
    private String queryStorageUrl = "";//进销存扫描条形码根据skuid获取库存的接口


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan_code);
        scanMode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        formName = getIntent().getStringExtra(FORM_NAME);
        scanBarcodeUrl = getIntent().getStringExtra("scanBarcodeUrl");
        queryStorageUrl = getIntent().getStringExtra("queryStorageUrl");
        isSingle = getIntent().getBooleanExtra(IS_SINGLE_SWEEP, true);
        returnInventoryList = (InventoryList) getIntent().getSerializableExtra("inventoryList");
        inventoryList = new ArrayList<>();
        initView();
        setOnTouchEvent();
    }

    /**
     * 点击事件
     */
    private void setOnTouchEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                //返回扫描结果
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                for (int i = 0; i < inventoryList.size(); i++) {
                    Inventory inventory = inventoryList.get(i);
                    for (Inventory inventory1 : returnInventoryList.getList()) {
                        if (inventory.getUuid().equals(inventory1.getUuid())) {
                            inventory1.setNum(inventory1.getNum() + inventory.getNum());
                            inventoryList.remove(i);
                            break;
                        }
                    }
                }
                returnInventoryList.getList().addAll(inventoryList);
                bundle.putSerializable(RESULT_BAR_CODE, returnInventoryList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
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
        switch (scanMode) {
            case DecodeThread.BARCODE_MODE:
                scan_hint.setText(R.string.scan_barcode_hint);
                break;
            case DecodeThread.QRCODE_MODE:
                scan_hint.setText(R.string.scan_qrcode_hint);
                break;
            case DecodeThread.ALL_MODE:
                scan_hint.setText(R.string.scan_allcode_hint);
                break;
        }
    }

    void initView() {
        shopList = findViewById(R.id.list_shop);
        rescan = findViewById(R.id.service_register_rescan);
        scan_image = findViewById(R.id.scan_image);
        scan_hint = findViewById(R.id.scan_hint);
        headerView = findViewById(R.id.boeryun_headerview);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        rlContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        qrcode_g_gallery = (TextView) findViewById(R.id.qrcode_g_gallery);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back = (TextView) findViewById(R.id.qrcode_ic_back);
        qrcode_ic_back.setOnClickListener(this);
        iv_light = (TextView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(this);
        rescan.setOnClickListener(this);


        if (isSingle) { //如果是连续多次扫描 显示头部布局
            headerView.setVisibility(View.VISIBLE);
        } else {
            headerView.setVisibility(View.GONE);
        }
        initData();
    }

    private void initData() {
        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode, this);

        inventoryAdapter = getAdapter(inventoryList);
        shopList.setAdapter(inventoryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
//        rescan.setVisibility(View.INVISIBLE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    /**
     * 扫描成功后处理扫描结果
     */
    public void scanResult(Result rawResult, Bundle bundle) {
        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            scan_image.setVisibility(View.VISIBLE);
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            Bitmap barcode = null;
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
            mBitmap = BitmapHelper.zoomBitmap(barcode, ViewHelper.px2dip(this, 65), ViewHelper.px2dip(this, 65));
        }
        scan_image.setVisibility(View.VISIBLE);
        shopList.setVisibility(View.VISIBLE);
        Inventory inventory = new Inventory("", rawResult.getText(), 1);
        boolean isExist = false;//列表中是否已经存在
        existPosition = -1;
        for (int i = 0; i < inventoryList.size(); i++) {
            Inventory in = inventoryList.get(i);
            if (in.getUuid().equals(inventory.getUuid())) {
                isExist = true;
                in.setNum(in.getNum() + 1);
                existPosition = i;
            }
        }
        Message message = handler.obtainMessage();
        message.obj = inventory;
        message.what = 0;
        if (!isExist) {
            inventoryAdapter.addTop(inventory);
            shopList.post(new Runnable() {
                @Override
                public void run() {
                    existPosition = 0;
                    shopList.setSelection(0);
                    handler.sendMessage(message);
                }
            });
        } else {
            if (existPosition != -1) {
                shopList.post(new Runnable() {
                    @Override
                    public void run() {
                        shopList.setSelection(existPosition);
                        handler.sendMessage(message);
                    }
                });
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Inventory inventory = (Inventory) msg.obj;
                getCargoInfo(inventory); //查询商品详细信息
            }
        }
    };

    void startScan() {
        if (rescan.getVisibility() == View.VISIBLE) {
//            rescan.setVisibility(View.INVISIBLE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            scanPreview.setVisibility(View.INVISIBLE);
        }
    }

    public void showPictures(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTOREQUESTCODE:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(colum_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(), data.getData());
                        }
                        scanManager.scanningImage(photo_path);
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_g_gallery:
                showPictures(PHOTOREQUESTCODE);
                break;
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            case R.id.qrcode_ic_back:
                finish();
                break;
            case R.id.service_register_rescan://再次开启扫描
                startScan();
                break;
            default:
                break;
        }
    }

    private CommanAdapter<Inventory> getAdapter(final List<Inventory> list) {
        return new CommanAdapter<Inventory>(list, this, R.layout.item_inventory_list) {

            @Override
            public void convert(int position, final Inventory item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name, item.getName());
                viewHolder.setTextValue(R.id.tv_num, item.getNum() + "");
                viewHolder.setTextValue(R.id.tv_guige, item.getItemstyle());
                viewHolder.setTextValue(R.id.tv_barcode, item.getBarcode());
                viewHolder.setTextValue(R.id.tv_balance, item.getBalance());
            }
        };
    }

    /**
     * 添加列表中
     *
     * @param view 终点位置的view
     */
    private void addCargoList(View view) {

        // 创建执行动画的主题---ImageView(该图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线）。)
        final ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(mBitmap);
        // 将执行动画的图片添加到开始位置。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) ViewHelper.dip2px(this, 38), (int) ViewHelper.dip2px(this, 38));
        rlContainer.addView(imageView, params);


        // 计算动画开始/结束点的坐标的准备工作
        // 得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        rlContainer.getLocationInWindow(parentLocation);
        // 得到商品图片的坐标（用于计算动画开始的坐标）
        int[] startLoc = new int[2];
        scan_image.getLocationInWindow(startLoc);
        // 得到购物车图片的坐标(用于计算动画结束后的坐标)
        int[] endLoc = new int[2];
        view.getLocationInWindow(endLoc);

        // 计算动画开始结束的坐标
        // 开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + scan_image.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + scan_image.getHeight() / 2;
        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + view.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        //计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        PathMeasure mPathMeasure = new PathMeasure(path, false);

        //属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(400);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        scan_image.setVisibility(View.GONE);
        // 开始执行动画
        valueAnimator.start();
        // 动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                rlContainer.removeView(imageView);
                inventoryAdapter.notifyDataSetChanged();
                //重启扫描器
                scanManager.reScan();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * 根据商品的barcode获取商品的详情
     *
     * @param inventory
     */
    private void getProductInfoAndBalance(Inventory inventory) {
        String url = Global.BASE_JAVA_URL + queryStorageUrl;

        Map<String, String> map = new HashMap<>();
        map.put("skuId", inventory.getUuid());

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Inventory inventory1 = null;
                try {
                    inventory1 = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Inventory.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (inventory1 != null) {
                    for (int i = 0; i < inventoryList.size(); i++) {
                        Inventory in = inventoryList.get(i);
                        if (in.getBarcode().equals(inventory1.getBarcode())) {
                            in.setBalance(inventory1.getBalance());
                            break;
                        }
                    }
                    inventoryAdapter.notifyDataSetChanged();
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
     * 查询商品详细信息
     *
     * @param inventory
     */
    private void getCargoInfo(Inventory inventory) {
        String url = Global.BASE_JAVA_URL + scanBarcodeUrl;

        Map<String, String> map = new HashMap<>();
        map.put("barcode", inventory.getUuid());

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Map<String, String> map = new HashMap<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);//json数据
                    JSONObject data = jsonObject.getJSONObject("Data");
                    // 动态获取key值
                    Iterator<String> iterator = data.keys();//使用迭代器
                    while (iterator.hasNext()) {
                        String key = iterator.next();//获取key
                        String value = data.getString(key);//获取value
                        if (key.equals("invCategoryId")) {

                        } else if (key.equals("uuid")) {
                            map.put("skuId", value);
                            map.put("invCategoryId", value);
                        } else if (key.equals("purchasePrice")) {
                            map.put(key, value);
                            map.put("单价", value);
                        } else if (key.equals("price")) {
                            map.put(key, value);
                            map.put("单价", value);
                        } else if (key.equals("measureUnit")) {
                            map.put(key, value);
                        } else if (key.equals("specs")) {
                            map.put(key, value);
                            map.put("skuStyle", value);
                        } else {
                            map.put(key, value);
                        }

                    }
                    if (map.size() > 0) {
                        Inventory inventory1 = null;
                        try {
                            inventory1 = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Inventory.class);
                            inventory1.setCargoList(map);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < inventoryList.size(); i++) {
                            Inventory in = inventoryList.get(i);
                            if (in.getUuid().equals(inventory.getUuid())) {
                                in.setBalance(inventory1.getBalance());
                                in.setName(inventory1.getName());
                                in.setBarcode(inventory1.getBarcode());
                                in.setItemstyle(inventory1.getItemstyle());
                                in.setCargoList(map);
                                break;
                            }
                        }
                        inventoryAdapter.notifyDataSetChanged();
                        getProductInfoAndBalance(inventory1);
                        View view = shopList.getChildAt(existPosition - shopList.getFirstVisiblePosition());
                        addCargoList(view); //执行属性动画添加列表中
                    } else {
                        scan_image.setVisibility(View.GONE);
                        inventoryAdapter.remove(inventory);
                        //重启扫描器
                        scanManager.reScan();
                        showShortToast("暂无库存信息");
                    }
                } catch (JSONException e) {
                    showShortToast("暂无库存信息");
                    scan_image.setVisibility(View.GONE);
                    inventoryAdapter.remove(inventory);
                    //重启扫描器
                    scanManager.reScan();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("暂无库存信息");
                scan_image.setVisibility(View.GONE);
                inventoryAdapter.remove(inventory);
                //重启扫描器
                scanManager.reScan();
            }
        });
    }


}
