package com.biaozhunyuan.tianyi.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.attendance.BaiduPlace;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.utils.EarthMapUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.helper.BaiduLocator;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.view.BoeryunDialog;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.helper.SelectLocationBiz.SELECT_LOCATION_CODE;

/**
 * 长汇客户详情 2016/01/27 10:47
 * <p/>
 * 长汇项目动态生成客户: 加载数据 解析，分类（基本字段,附加字段...），生成控件，绑定点击事件，提交校验
 */
public class ChClientInfoActivity extends BaseActivity implements BDLocationListener {

    public static final String TAG = "extra_ChClientInfoActivity";

    public static final String EXTRA_CLIENT_ID = "extra_client_id";
    private Context mContext;
    //    private ChClientBiz mClientBiz;
    private 客户 mClient;
    private String mClientId;
    private boolean isShowContactAndPhone = true;//是否显示联系人和手机号，默认为true
    private ScanCustomerModel scanCustomer;

    private BoeryunHeaderView mHeaderView;
    //    private Indicator mIndicator;
    private BoeryunViewpager mViewPager;
    private LinearLayout llContactList;
    private LinearLayout llAddContact;
    private Button btn_delete;
    private boolean isDelete = false;//是否有删除客户的权限
    private boolean isShowBaseInfo = false;

    private HashMap<String, ArrayList<表单字段>> mFormDataMap;
    private List<ChClientTabFragment> mFragments;
    private SimpleIndicator simpleindicator;
    private boolean readOnly;


    private List<BaiduPlace> bpList;
    /**
     * 上传到服务器的经纬度
     */
    double mLatitude;
    double mLongitude;
    private double Latitude;
    private double Longitude;
    private int radiusMap = 500;// 默认区域半径

    /**
     * 记录初次定位的经纬度
     */
    private double mLat;
    private double mLog;
    /**
     * 定位所需字段
     */
    String mLocation = null;
    private String mCity;
    private String mCountry;
    private String mProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch_client_info);
        initViews();
        permission();
        initData();
        try {
            requestLocating();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setOnEvent();

    }

    private void requestLocating() throws Exception {
        BaiduLocator.requestLocation(getApplicationContext(), ChClientInfoActivity.this);
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
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            mLongitude = location.getLongitude();
            Longitude = location.getLongitude();

            mLog = location.getLongitude();


//            BaiduLocator.stop();
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
            mProvince = location.getProvince();

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

                                // 根据距离由近到远排序
                                for (int i = 0; i < bpList.size(); i++) {
                                    BaiduPlace temp = bpList.get(i);
                                    Logger.i("distance::" + temp.name
                                            + temp.address + "---"
                                            + temp.location.lat + ","
                                            + temp.location.lng);
                                }
                            }
                            BaiduPlace bPlace = bpList.get(0);

                            mLatitude = bPlace.location.lat;
                            mLongitude = bPlace.location.lng;
                            mLocation = getLocationAddress(country, city, bPlace);

                            String lat = String.valueOf(mLatitude);
                            String log = String.valueOf(mLongitude);
                            PreferceManager.getInsance().saveValueBYkey("add_business_lat", lat);
                            PreferceManager.getInsance().saveValueBYkey("add_business_lng", log);
                        } else {
                        }
                    }
                } catch (Exception e) {
                    Logger.e("" + e.getMessage());
                }
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
        if (!TextUtils.isEmpty(city) && !address.contains(city)) {
            address = city + " " + address;
        }
        return address;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_LOCATION_CODE) {
                BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
                        requestCode, data);
                if (place != null) {
                    String address = place.name + " (" + place.address + ")";
                    mProvince = place.province;
                    mCity = place.city;
                    mLatitude = place.location.lat;
                    mLongitude = place.location.lng;
//                        if(address.contains("省")){
//                            mProvince = address.substring(0,address.indexOf("省"));
//                        }else {
//                            mProvince = "";
//                        }
//                        if(place.address.contains("市")){
//                            mCity = place.address.substring(0,place.address.indexOf("市"));
//                        }else {
//                            mCity = "";
//                        }
                    for (ChClientTabFragment fragment : mFragments) {
                        fragment.setFormListAddress(address, mProvince, mCity);
                    }
                    if (place.location != null) {
                        String lat = String.valueOf(place.location.lat);
                        String log = String.valueOf(place.location.lng);
                        PreferceManager.getInsance().saveValueBYkey("add_business_lat", lat);
                        PreferceManager.getInsance().saveValueBYkey("add_business_lng", log);
                    }
                }
            }
        }
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void initViews() {
//        mIndicator = (Indicator) findViewById(R.id.indicator_ch_client_info);
        simpleindicator = findViewById(R.id.simpleindicator_ch_client_info);
        mViewPager = (BoeryunViewpager) findViewById(R.id.vp_ch_client_info);
        mHeaderView = (BoeryunHeaderView) findViewById(R.id.header_ch_client_info);
        llContactList = (LinearLayout) findViewById(R.id.ll_contact_list_ch_client_info);
        llAddContact = (LinearLayout) findViewById(R.id.ll_contact_add_ch_client_info);
        btn_delete = (Button) findViewById(R.id.btn_client_info_delete_client);
        mViewPager.setEnabled(true);
        simpleindicator.setVisibleTabCount(3);

    }

    private void initData() {
        mContext = this;
        mFragments = new ArrayList<ChClientTabFragment>();
        mFormDataMap = new HashMap<String, ArrayList<表单字段>>();
        mClientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
        isShowBaseInfo = getIntent().getBooleanExtra("isShowBaseInfo", false);
        isShowContactAndPhone = getIntent().getBooleanExtra("isShowContactAndPhone", true);
        readOnly = getIntent().getBooleanExtra("isReadOnly", false);
        scanCustomer = (ScanCustomerModel) getIntent().getSerializableExtra("ScanCustomer");
        if (isShowBaseInfo) {
//            mIndicator.setVisibility(View.GONE);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mClient = (客户) bundle.getSerializable(TAG);
            if (mClient != null && mClientId.equals("0")) {
                mClientId = mClient.编号 + "";
            }
        }
        getCustomerFormById(mClientId);

        if (mClientId.equals("0")) {
//            llAddContact.setVisibility(View.GONE);
//            llContactList.setVisibility(View.GONE);
            findViewById(R.id.ll_bottom_ch_client).setVisibility(View.GONE);
            mHeaderView.setTitle("新建客户");
            mHeaderView.setRightTitle("保存");
            mHeaderView.setRightTitleVisible(true);
        }
        if (!readOnly) {
            mHeaderView.setRightTitleVisible(false);
        }
    }

    private void setOnEvent() {
//        mIndicator.setRelateViewPager(mViewPager);
        simpleindicator.setViewPager(mViewPager, 1);

        // 查看本客户的联系记录
        llContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,
//                        ClientContactListActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("ClientInfoActivity_clientId", mClientId);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        // 新建联系记录
        llAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,
//                        ClientConstactNewActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(ClientConstactNewActivity.EXTRA_CLIENT_NAME, getValueByFiled("名称"));
//                bundle.putInt(ClientConstactNewActivity.EXTRA_CLIENT_ID, mClientId);
//
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        mHeaderView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                ArrayList<表单字段> list = getAllFormList();
                String result = ChClientBiz.checkNull(list);
                if (!TextUtils.isEmpty(result)) {
                    showShortToast(result);
                } else {
                    String idCardReg = ChClientBiz.checkCardRegEx(list);
                    if (!TextUtils.isEmpty(idCardReg)) {
                        // 先校验身份证号
                        showShortToast(idCardReg);
                    } else {
//                        result = ChClientBiz.checkRegEx(list);
                        if (!TextUtils.isEmpty(result)) {
                            showShortToast(result);
                        } else {
                            saveCustomerForm(list);
                        }
                    }
                }
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


        //删除客户
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BoeryunDialog dialog = new BoeryunDialog(mContext, false, "提示", "确认删除客户？", "确认", "取消");
                dialog.setBoeryunDialogClickListener(
                        new BoeryunDialog.OnBoeryunDialogClickListner() {
                            @Override
                            public void onClick() {
                                String url = Global.BASE_URL + "Customer/DeleteCustomer/" + mClientId;
                                StringRequest.getAsyn(url, new StringResponseCallBack() {
                                    @Override
                                    public void onResponse(String response) {
                                        showShortToast("删除成功");
//                                        ClientListActivity.isResume = true;
//                                        ClientRelatedInfoActivity.isFinish = true;
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Request request, Exception ex) {

                                    }

                                    @Override
                                    public void onResponseCodeErro(String result) {
                                        showShortToast("删除失败");
                                    }
                                });
                                dialog.dismiss();
                            }
                        }, new BoeryunDialog.OnBoeryunDialogClickListner() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });

    }


    /**
     * 进入购物车 的权限码：99
     */
    private void permission() {
        String url = Global.BASE_URL + "权限/Get员工所有权限";
        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponseCodeErro(String result) {
            }

            @Override
            public void onResponse(String response) {
                // Toast.makeText(mContext, "获取成功", Toast.LENGTH_LONG).show();
//                Gson gson = new Gson();
//                PermissionBean status = gson.fromJson(response,
//                        PermissionBean.class);
//                List<Integer> result = status.getData();
//                for (int i = 0; i < result.size(); i++) {
//                    if (result.get(i) == 99) {
//                        isDelete = true;
////                        return;
//                    } else {
//                        isDelete = false;
//                    }
//                }
                if (response.contains(",99,")) {
                    isDelete = true;
                } else {
                    isDelete = false;
                }

                if (isDelete) {
                    btn_delete.setVisibility(View.VISIBLE);
                } else {
                    btn_delete.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                // TODO Auto-generated method stub

            }
        });
    }

    /***
     * 根据客户编号获取客户表单
     *
     * @param clientId 客户编号，0代表新建
     */
    private void getCustomerFormById(String clientId) {
        ProgressDialogHelper.show(mContext);
        String url = "";
        if (isShowBaseInfo) {
            url = Global.BASE_JAVA_URL + GlobalMethord.动态字段 + "?category=基本信息";
        } else {
            url = Global.BASE_JAVA_URL + GlobalMethord.动态字段;
        }
        JSONObject object = new JSONObject();
        try { //crm_contact
            object.put("type", "crm_customer");
            object.put("id", clientId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String result) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String response) {
                Logger.i(TAG + response);
                ProgressDialogHelper.dismiss();
                动态表单ViewModel formViewModel = null;
                try {
                    formViewModel = JsonUtils.jsonToEntity(
                            response, 动态表单ViewModel.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (formViewModel != null) {
                    List<String> titles = new ArrayList<String>();
                    for (动态表单分类 categray : formViewModel.动态表单分类s) {
                        titles.add(categray.分类名称);
                        Logger.i("CategrayTag::" + categray.分类名称 + "");
                        mFormDataMap.put(categray.分类名称, new ArrayList<表单字段>());
                    }
                    if (formViewModel.表单字段s != null
                            && formViewModel.表单字段s.size() > 0) {
                        // 根据分类Tab
                        for (表单字段 form : formViewModel.表单字段s) {
                            if (form.Name.equals("uuid")) {
                                form.TypeName = "基本信息";
                                mFormDataMap.put(form.TypeName,
                                        new ArrayList<表单字段>());
                                mFormDataMap.get(form.TypeName).add(form);
                                break;
                            }
                        }
                    }

                    if (formViewModel.表单字段s != null
                            && formViewModel.表单字段s.size() > 0) {
                        // 根据分类Tab
                        for (表单字段 form : formViewModel.表单字段s) {
                            if (!mFormDataMap.containsKey(form.TypeName)) {
                                mFormDataMap.put(form.TypeName,
                                        new ArrayList<表单字段>());
                            }
//							if(!"编号".equals(form.Name))
//							{   //完成分类，编号字段不显示
//								mFormDataMap.get(form.TypeName).add(form);
//							}
                            if (!form.Name.equals("uuid")) {
                                mFormDataMap.get(form.TypeName).add(form);
                            }

                            if (scanCustomer != null) {
                                if ("address".equals(form.Name)) {
                                    form.Value = scanCustomer.getAddr();
                                } else if ("name".equals(form.Name)) {
                                    form.Value = scanCustomer.getCompany();
                                } else if ("email".equals(form.Name)) {
                                    form.Value = scanCustomer.getEmail();
                                } else if ("fax".equals(form.Name)) {
                                    form.Value = scanCustomer.getFax();
                                } else if ("mobile".equals(form.Name)) {
                                    form.Value = scanCustomer.getMobile();
                                } else if ("contact".equals(form.Name)) {
                                    form.Value = scanCustomer.getName();
                                } else if ("phone".equals(form.Name)) {
                                    form.Value = scanCustomer.getTel();
                                } else if ("title".equals(form.Name)) {
                                    form.Value = scanCustomer.getPostion();
                                } else if ("remark".equals(form.Name)) {
                                    form.Value = scanCustomer.getUrl();
                                }
                            }
                        }

                        表单字段 tempIdForm = null;
                        String firstKey = "";
                        Iterator<Entry<String, ArrayList<表单字段>>> item = mFormDataMap.entrySet().iterator();
                        while (item.hasNext()) {
                            Entry<String, ArrayList<表单字段>> entry = item.next();
                            List<表单字段> formList = entry.getValue();
                            String formName = entry.getKey();

                            //特殊处理，当分类下没有内容 或只有一个编号字段则去除tab
                            if (formList == null || formList.size() == 0) {
//                                ||
                                titles.remove(formName);
                            } else if ((formList.size() == 1 && "编号".equals(formList.get(0).Name))) {
                                titles.remove(formName);
                                tempIdForm = formList.get(0);
                            }

                            if (TextUtils.isEmpty(firstKey)) {
                                firstKey = formName;
                            }
                        }


                        if (tempIdForm != null) {
                            //特殊处理id字段到第一分类
                            mFormDataMap.get(firstKey).add(tempIdForm);
                        }
                        for (int i = 0; i < titles.size(); i++) {
                            String s = titles.get(i);
                            if (TextUtils.isEmpty(s)) {
//                                titles.set(titles.indexOf(s), "其他");
                                titles.remove(i);
                                titles.add("其他");
                            }
                        }
//                        mIndicator.setTabItemTitles(titles);

                        for (String title : titles) {
                            if (title.equals("其他")) {
                                title = "";
                            }
                            Iterator<Entry<String, ArrayList<表单字段>>> it = mFormDataMap
                                    .entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<String, ArrayList<表单字段>> entry = it
                                        .next();
                                String keyStr = entry.getKey();
                                Logger.i("EQU" + keyStr + "---" + title);
                                if (keyStr.equals(title)) {
                                    ArrayList<表单字段> formList = entry.getValue();

                                    if (mClient != null && mClient.编号 == 0) {
                                        for (表单字段 field : formList) {
                                            if ("电话".equals(field.Name)) {
                                                field.Value = mClient.电话;
                                            }

                                            if ("手机".equals(field.Name)) {
                                                field.Value = mClient.手机;
                                            }
                                        }
                                    }


                                    ChClientTabFragment fragment = ChClientTabFragment
                                            .newInstance(formList, readOnly, "crm_customer", clientId);
                                    fragment.setShowContactAndPhone(isShowContactAndPhone);
                                    mFragments.add(fragment);
                                }
                            }
                        }

                        for (int i = 0; i < titles.size(); i++) {
                            String s = titles.get(i);
                            if ("其他".equals(s)) {
//                                titles.set(titles.indexOf(s), "其他");
                                titles.remove(s);
                            }
                        }
//                        mIndicator.setTabItemTitles(titles);
                        simpleindicator.setTabItemTitles(titles);
                        if (titles == null || titles.size() == 0 || (titles.size() == 1 && TextUtils.isEmpty(titles.get(0)))) {
//                            mIndicator.setVisibility(View.GONE);
                            simpleindicator.setVisibility(View.GONE);
                        }
                        mViewPager.setAdapter(new FragmentPagerAdapter(
                                getSupportFragmentManager()) {

                            @Override
                            public int getCount() {
                                // TODO Auto-generated method stub
                                return mFragments.size();
                            }

                            @Override
                            public Fragment getItem(int position) {
                                return mFragments.get(position);
                            }
                        });
                        mViewPager.setOffscreenPageLimit(mFragments.size());


                        //如果是 扫描名片新建客户，进入页面赋值之后保存客户
                        if (scanCustomer != null) {
                            saveCustomerForm(getAllFormList());
                        }
                    } else {
//                        mIndicator.setVisibility(View.GONE);
                        simpleindicator.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Logger.e(TAG + ex + "");
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private void saveCustomerForm(ArrayList<表单字段> formList) {
//        ClientInfo info = new ClientInfo();
//        info.setJsonData(formList);
//        info.setType("crm_customer");
        for (int i = 0; i < formList.size(); i++) {
            if (formList.get(i).Name.equals("uuid")) {
                formList.get(i).Identify = true;
                break;
            }
        }
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.保存动态字段;
        StringRequest.postAsynNoMap(url, "crm_customer", formList, new StringResponseCallBack() {
            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存失败:" + JsonUtils.pareseMessage(result));
                Logger.d(TAG + result + "");
            }

            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存成功");
                setResult(RESULT_OK);
                ClientlistCustomerFragment.isResume = true;
                ClientlistpublicFragment.isResume = true;
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                Logger.e(TAG + ex + "");
                showShortToast("网络不给力，请稍后再试");
            }
        });
    }

    private ArrayList<表单字段> getAllFormList() {
        ArrayList<表单字段> list = new ArrayList<表单字段>();
        for (ChClientTabFragment fragment : mFragments) {
            list.addAll(fragment.getFormList());
        }
        return list;
    }

    /**
     * 获取指定字段的属性值
     *
     * @param fieldName
     * @return
     */
    private String getValueByFiled(String fieldName) {
        ArrayList<表单字段> list = getAllFormList();
        for (表单字段 field : list) {
            Logger.i("fieldDe" + field.Name + "=" + field.Value);
            if (fieldName.equals(field.Name)) {
                return field.Value;
            }
        }
        return "";
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == SELECT_LOCATION_CODE) {
//                BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
//                        requestCode, data);
//                if (place != null) {
//                    String address = place.name + " (" + place.address + ")";
//                    String mProvince = place.province;
//                    String mCity = place.city;
//                    Double mLatitude = place.location.lat;
//                    Double mLongitude = place.location.lng;
////                        if(address.contains("省")){
////                            mProvince = address.substring(0,address.indexOf("省"));
////                        }else {
////                            mProvince = "";
////                        }
////                        if(place.address.contains("市")){
////                            mCity = place.address.substring(0,place.address.indexOf("市"));
////                        }else {
////                            mCity = "";
////                        }
//                    for (ChClientTabFragment fragment : mFragments) {
//                        fragment.setFormListAddress(address, mProvince, mCity);
//                    }
//                    if (place.location != null) {
//                        String lat = String.valueOf(place.location.lat);
//                        String log = String.valueOf(place.location.lng);
//                        PreferceManager.getInsance().saveValueBYkey("add_business_lat", lat);
//                        PreferceManager.getInsance().saveValueBYkey("add_business_lng", log);
//                    }
//                }
//            }
//        }
//    }

}
