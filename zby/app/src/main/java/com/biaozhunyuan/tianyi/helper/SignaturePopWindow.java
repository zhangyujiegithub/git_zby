package com.biaozhunyuan.tianyi.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.helper.signaturepad.SignaturePad;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/***
 * 手写签名窗体
 * 
 * @author K
 * 
 */
public class SignaturePopWindow {

	private Context mContext;
	private String mPath;

	public SignaturePopWindow(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**
	 * 弹出IOS风格的底部字典选择
	 * 
	 * @param mainLayoutId
	 *            layout文件的根节点id
	 * @param
	 */
	public void show(int mainLayoutId) {
		View parentView = ((Activity) mContext).findViewById(mainLayoutId);

		View view = View.inflate(mContext, R.layout.pop_signature, null);

		int height = ViewHelper.getScreenHeight(mContext)
				- ViewHelper.getStatusBarHeight(mContext);
		final PopupWindow popupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, height);
		initViews(view, popupWindow);
		// RotateAnimation rotateAnimation = new RotateAnimation(0, 90,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// rotateAnimation.setDuration(10);
		// rotateAnimation.setFillAfter(true);
		// view.setAnimation(rotateAnimation);
		initPopupWindow(parentView, popupWindow);
	}

	private void initViews(View view, final PopupWindow popupWindow) {
		BoeryunHeaderView headerView = (BoeryunHeaderView) view
				.findViewById(R.id.header_pop_signature);
		final SignaturePad signaturePad = (SignaturePad) view
				.findViewById(R.id.signature_pop_signature);
		ImageView ivClear = (ImageView) view
				.findViewById(R.id.iv_clear_pop_signature);

		ivClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				signaturePad.clear();
			}
		});

		headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
			@Override
			public void onClickSaveOrAdd() {
				Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
				if (addJpgSignatureToGallery(signatureBitmap)) {
					// Toast.makeText(mContext,
					// "Signature saved into the Gallery",
					// Toast.LENGTH_SHORT).show();
					if (mOnSaveSuccessedListener != null) {
						mOnSaveSuccessedListener.onSaved(mPath);
					}
					popupWindow.dismiss();
				} else {
					Toast.makeText(mContext, "Unable to store the signature",
							Toast.LENGTH_SHORT).show();
				}

				// Toast.makeText(mContext, "保存图片：" + mPath, Toast.LENGTH_SHORT)
				// .show();
			}

			@Override
			public void onClickFilter() {

			}

			@Override
			public void onClickBack() {
				popupWindow.dismiss();
			}
		});

	}

	private void initPopupWindow(View parentView, final PopupWindow popupWindow) {
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				setBackgroundAlpha(1);
			}
		});

		popupWindow.setAnimationStyle(R.style.AnimationFadeBottom);
		setBackgroundAlpha(0.5f);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);

		popupWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.LEFT,
				0, 0);

	}

	public File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(FilePathConfig.getSignatureDirPath(), albumName);
		if (!file.mkdirs()) {
			LogUtils.e("SignaturePad", "Directory not created");
		}
		return file;
	}

	public boolean addJpgSignatureToGallery(Bitmap signature) {
		boolean result = false;
		try {
			File photo = new File(getAlbumStorageDir("SignaturePad"),
					String.format("Signature_%d.jpg",
							System.currentTimeMillis()));
			mPath = photo.getAbsolutePath();
			saveBitmapToJPG(signature, photo);
			scanMediaFile(photo);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void scanMediaFile(File photo) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(photo);
		mediaScanIntent.setData(contentUri);
		mContext.sendBroadcast(mediaScanIntent);
	}

	public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
		OutputStream stream = new FileOutputStream(photo);
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);////注意是PNG格式的。若设置为JPG格式，背景色会变黑
		stream.close();
	}

	/**
	 * 设置位图的背景色
	 * @param bitmap 需要设置的位图
	 * @param color 背景色
	 */
	public void setBitmapBGColor(Bitmap bitmap,int color){
		for(int i=0;i<bitmap.getWidth();i++){
			for(int j=0;j<bitmap.getHeight();j++){
				bitmap.setPixel(i,j,color);//将bitmap的每个像素点都设置成相应的颜色
			}
		}
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	public void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
				.getAttributes();
		lp.alpha = bgAlpha;// 0.0-1.0
		((Activity) mContext).getWindow().setAttributes(lp);
	}

	private OnSaveSuccessedListener mOnSaveSuccessedListener;

	public interface OnSaveSuccessedListener {
		void onSaved(String path);

	}

	public void setOnSaveSuccessedListener(
			OnSaveSuccessedListener onSaveSuccessedListener) {
		this.mOnSaveSuccessedListener = onSaveSuccessedListener;
	}
}
