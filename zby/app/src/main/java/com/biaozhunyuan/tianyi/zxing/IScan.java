package com.biaozhunyuan.tianyi.zxing;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import com.google.zxing.Result;
import com.biaozhunyuan.tianyi.zxing.camera.CameraManager;

/** Zxin扫描需要实现的接口 */
public interface IScan {

	void handleDecode(Result rawResult, Bundle bundle);

	Handler getHandler();

	void setResult(int resultCode, Intent data);

	Rect getCropRect();

	CameraManager getCameraManager();
}
