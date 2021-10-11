package com.biaozhunyuan.tianyi.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biaozhunyuan.tianyi.attendance.BaiduPlace;
import com.biaozhunyuan.tianyi.attendance.LocationListActivity;


/***
 * 选择地址
 * 
 * @author kjx 2016-03-10
 */
public class SelectLocationBiz {

	public static final int SELECT_LOCATION_CODE = 1101; // 选择地址

	public static final String ClientId = "id";
	public static final String ClientName = "clientName";

	public static final String REQUEST_CODE_LOCATION_LIST = "select_location_list";

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
	public static void selectLocation(Context context, double lat, double lng) {

		Intent intent = new Intent(context, LocationListActivity.class);
		if (lng != 0 && lat != 0) {
			intent.putExtra(LocationListActivity.LATITUDE, lat);
			intent.putExtra(LocationListActivity.LONGITUDE, lng);
		}

		((Activity) context).startActivityForResult(intent,
				SELECT_LOCATION_CODE);
	}

	/***
	 * 地址选择微调定位，打开定位列表，先定位，然后显示附近位置
	 * SelectLocationBiz.SELECT_LOCATION_CODE
	 *
	 * @param context
	 *            上下文
	 */
	public static void selectLocation(Context context) {

		selectLocation(context,0,0);
	}

	/***
	 * 地址选择微调定位，通过 {@link } 打开客户列表，请求结果码 为
	 * SelectLocationBiz.SELECT_LOCATION_CODE
	 * 
	 * @param fragment
	 *            所在fragment
	 * @param lat
	 *            经度
	 * @param lng
	 *            纬度
	 */
	public static void selectLocation(Fragment fragment, double lat, double lng) {
		Intent intent = new Intent(fragment.getActivity(),
				LocationListActivity.class);
		intent.putExtra(LocationListActivity.LATITUDE, lat);
		intent.putExtra(LocationListActivity.LONGITUDE, lng);
		fragment.startActivityForResult(intent, SELECT_LOCATION_CODE);
	}

	/****
	 * 选中定位地址完毕，显示
	 * 
	 * @param requestCode
	 * @param data
	 * @return 返回选中@BaiduPlace 对象实体，没有则返回null
	 */
	public static BaiduPlace onActivityGetPlace(int requestCode, Intent data) {
		BaiduPlace place = null;
		if (requestCode == SELECT_LOCATION_CODE) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					place = (BaiduPlace) bundle
							.getSerializable(LocationListActivity.RESULT);
				}
			}
		}
		return place;
	}
}
