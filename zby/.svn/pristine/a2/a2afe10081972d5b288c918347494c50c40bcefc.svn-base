package com.biaozhunyuan.tianyi.common.helper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.activity.SelectPhotoActivity;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectPhotoBiz {
    public static final String THUMB_PHOTO_PATH = "thumb_photo_path";

    public static final int REQUESTCODE_TAKE_PHOTO = 0X901;
    public static final int REQUESTCODE_SELECT_PHOTO = 0X902;

    /**
     * 拍照获取图片
     */
    public static void doTakePhoto(Context context) {
        doTakePhoto(context, REQUESTCODE_TAKE_PHOTO);
    }

    /**
     * 拍照获取图片
     */
    public static void doTakePhoto(Context context, int requestCode) {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        PreferceManager spHelper = PreferceManager.getInsance();

        String photoPath = FilePathConfig.getAvatarDirPath() + File.separator
                + getPhotoFileName();
        spHelper.saveValueBYkey(THUMB_PHOTO_PATH, photoPath);

        File file = new File(photoPath);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Uri imageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//
        Log.i("RequestCode_take", requestCode + "");
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    /***
     * 选择图片
     *
     * @param context
     */
    public static void selectPhoto(Context context) {
        selectPhoto(context, REQUESTCODE_SELECT_PHOTO, 9);
    }

    /***
     * 选择图片
     *
     * @param context
     * @param maxPhotoCount 最大图片数量，默认9张
     */
    public static void selectPhoto(Context context, int maxPhotoCount) {
        selectPhoto(context, REQUESTCODE_SELECT_PHOTO, maxPhotoCount);
    }

    /***
     * 选择图片
     *
     * @param context
     * @param requestCode   请求码
     * @param maxPhotoCount 最大图片数量，默认9张
     */
    public static void selectPhoto(Context context, int requestCode,
                                   int maxPhotoCount) {
        Intent intent = new Intent(context, SelectPhotoActivity.class);
        intent.putExtra(SelectPhotoActivity.MAX_PHOTO_COUNT, maxPhotoCount);
        ((Activity) context).startActivityForResult(intent, requestCode);
        Log.i("RequestCode_select", requestCode + "");
    }

    /***
     * 获取拍照图片路径
     *
     * @param context
     * @return
     */
    public static String getPhotoPath(Context context) {
        return PreferceManager.getInsance().getValueBYkey(THUMB_PHOTO_PATH);
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private static String getPhotoFileName() {
        return "IMG"
                + DateFormat.format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA)) + ".jpg";
    }

    /***
     * 获取选择多图片路径
     *
     * @param data
     * @return
     */
    public static List<String> getSelectPathListOnActivityForResult(Intent data) {
        ArrayList<String> list = null;
        if (data != null) {
            list = data.getExtras().getStringArrayList(
                    SelectPhotoActivity.PHOTO_LIST);
        }
        return list;
    }


    /**
     * 按下拍照按钮,底部弹出 提示选择"拍照", "从相册中选择"
     * <p/>
     * 从相册中选择图片最多9张
     */
    public static void doPickOrTakePhotoiAction(final Context context) {
        String[] choices;
        choices = new String[]{"拍照", "从相册中选择"};
        DictIosPickerBottomDialog mDictIosPicker = new DictIosPickerBottomDialog(context);
        mDictIosPicker.show(choices);
        mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                onSelect(index, context, 9);
            }
        });
    }


    /**
     * 按下拍照按钮,底部弹出 提示选择"拍照", "从相册中选择"
     * 从相册中选择图片,可以指定选择照片个数
     */
    public static void doPickOrTakePhotoiAction(final Context context, final int maxCount) {
        String[] choices;
        choices = new String[]{"拍照", "从相册中选择"};
        DictIosPickerBottomDialog mDictIosPicker = new DictIosPickerBottomDialog(context);
        mDictIosPicker.show(choices);
        mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                onSelect(index, context, maxCount);
            }
        });
    }

    private static void onSelect(int which, final Context context, int maxCount) {
        switch (which) {
            case 0: {
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                    doTakePhoto(context);
                } else {
                    Toast.makeText(context, "没有SD卡", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 1:
                selectPhoto(context, maxCount);
                break;
        }
    }


}
