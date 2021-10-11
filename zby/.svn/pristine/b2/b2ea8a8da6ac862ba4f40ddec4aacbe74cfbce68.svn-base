package com.biaozhunyuan.tianyi.attch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.helper.ZLServiceHelper;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddImageHelper implements OnClickListener {

    private final static String TAG = "AddImageHelper";
    public final int PHOTO_ID = 102;
    public final int CAMERA_TAKE_HELPER = 101;
    /* 用来标识请求gallery的activity */
    public final int PICKED_PHOTO_WITH_DATA = 3021;
    public StringBuilder sbAttachIds = new StringBuilder(); // 存储照片上传后返回的附件号
    private String mFieldNameStr;
    private int LOAD_IMAGE = 102;
    private int PHOTOS_ISNULL = 103; // 图片列表为空
    private String mPictureFile; // 照片文件名
    private Bitmap photo; // 头像照片
    private boolean isAdd;
    private View addImageView;
    private DictIosPickerBottomDialog mDictIosPicker;
    // 网络更新头像存放路径
    private String mAvatarPath = FilePathConfig.getAvatarDirPath();
    private ZLServiceHelper zlServiceHelper = new ZLServiceHelper();
    private String photoSerialNo; // 照片序列号，对应一组照片
    // public List<String> photoPathList = new ArrayList<String>();
    private List<String> photoList = new ArrayList<String>();// 所有图片
    private List<String> photoUploadList = new ArrayList<String>();// 存储要上传图片的路径
    private HandlerPic handler;
    private Context context;
    //	private ORMDataHelper helper;
//	private Dao<PhotoInfo, Integer> dao;
    private LinearLayout llImages; // 添加图片布局
    private ImageButton ibAdd; // 添加按钮
    private ProgressBar pBar;// 进度条
    private Activity activity;
    private int count; // 统计图片下载次数

    // RotateAnimation rotateAnimation;// 旋转动画
    private InOnSelectPhotoListener mOnSelectPhotoListener;

    /**
     * 构造函数
     *
     * @param activity      当前页面activity
     * @param context       上下文对象
     * @param view          添加图片布局
     * @param photoSerialNo 显示一组图片的附件号， 如果只显示图片，传入对应对象的序列号;如果要是添加图片功能 则为null;
     * @param isAdd         是否显示添加按钮
     */
    public AddImageHelper(Activity activity, final Context context,
                          LinearLayout view, final String photoSerialNo, Boolean isAdd) {
        this(activity, context, view, photoSerialNo, isAdd, null);
    }

    /**
     * 构造函数
     *
     * @param activity        当前页面activity
     * @param context         上下文对象
     * @param view            添加图片布局
     * @param photoSerialNo   显示一组图片的附件号， 如果只显示图片，传入对应对象的序列号;如果要是添加图片功能 则为null;
     * @param isAdd           是否显示添加按钮
     * @param defualtPathList 默认显示图片路径
     */
    public AddImageHelper(Activity activity, final Context context,
                          LinearLayout view, final String photoSerialNo, Boolean isAdd,
                          List<String> defualtPathList) {
        super();
        initViews(view);
        addImageView = view;
        this.activity = activity;
        this.context = context;
        this.isAdd = isAdd;
        mDictIosPicker = new DictIosPickerBottomDialog(context);
        if (isAdd) { // 添加功能
            ibAdd.setOnClickListener(this);
            pBar.setVisibility(View.GONE);
        } else {
            ibAdd.setVisibility(View.GONE);
            pBar.setVisibility(View.VISIBLE);
        }
//		try {
//			helper = ORMDataHelper.getInstance(context);
//			dao = helper.getDao(PhotoInfo.class);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}

        loadLocalImgs(defualtPathList);

        if (!TextUtils.isEmpty(photoSerialNo)) {
            // 如果附件号，先到数据库对应图片路径，找不到开启线程跟新
            List<PhotoInfo> photoInfos = new ArrayList<PhotoInfo>();
            PhotoInfo info = new PhotoInfo();
            String attachMent = photoSerialNo.replace("\"", ""); // 去除“”
            attachMent = attachMent.replace("'", ""); // 去除'
            info.setPhotoSerialNo(attachMent);
//			try {
////				photoInfos = dao.queryForMatching(info);
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
            if (photoInfos.size() > 0) {
                loadImage(photoInfos);
                Logger.i("photoInfos" + "photoInfos.size() > 0");
            } else {
                Logger.i("photoInfos" + "photoInfos.size() =0");
                // 从网络跟新数据库,有则修改，无则新建
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						List<PhotoInfo> picInfos = new ArrayList<PhotoInfo>();
//						picInfos = zlServiceHelper.getPhotoInfos(context,
//								photoSerialNo);
//						Logger.d("photoInfos"+ "photoInfos.size() = "
//								+ picInfos.size());
//						if (picInfos.size() > 0) {
//							for (PhotoInfo photoInfo : picInfos) {
//								try {
//									dao.create(photoInfo);
//								} catch (SQLException e) {
//									e.printStackTrace();
//								}
//							}
//							Message msg = handler.obtainMessage();
//							msg.what = LOAD_IMAGE;
//							msg.obj = picInfos;
//							handler.sendMessage(msg);
//						} else {
//							Message msg = handler.obtainMessage();
//							msg.what = PHOTOS_ISNULL;
//							handler.sendMessage(msg);
//						}
//					}
//				}).start();
            }
        } else {
            pBar.setVisibility(View.GONE);
            // view.setVisibility(View.GONE);
        }
    }

    // 封装请求Gallery的intent
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * Constructs an intent for image cropping. 调用图片剪辑程序
     */
    public static Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200); // 宽
        intent.putExtra("outputY", 200); // 高
        intent.putExtra("return-data", true);
        return intent;
    }

    private void initViews(LinearLayout view) {
        handler = new HandlerPic();
        llImages = (LinearLayout) view.findViewById(R.id.llImages_control);
        ibAdd = (ImageButton) view.findViewById(R.id.ibAdd_control);
        pBar = (ProgressBar) view.findViewById(R.id.pbar_add_control);
        // rotateAnimation = new RotateAnimation(0, 360,
        // RotateAnimation.RELATIVE_TO_SELF, 0.5f,
        // RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        // rotateAnimation.setDuration(1 * 1000);
        // rotateAnimation.setRepeatCount(-1); // -1表示无限循环
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ibAdd_control:
                doPickPhotoAction();
                break;
            case PHOTO_ID:
                int tagValue = 0;
                try {
                    tagValue = Integer.parseInt(v.getTag().toString());
                } catch (Exception e) {
                    // TODO: 有的时候出现崩溃
                    Logger.i(TAG + "===v.getTag().toString()="
                            + v.getTag().toString());
                }
                Logger.i("tag" + "tag:" + tagValue);
                showImgDialog(tagValue);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照后重新加载
     * <p>
     * 1.先显示本地图片
     */
    public void refresh(int requesCode, Intent data) {
        String filePath = "";
        Bitmap bitmap = null;
        // 获取屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // int value = ibAdd.getHeight(); // 得到的是像素
        int value = (int) ViewHelper.dip2px(context, 75);
        if (requesCode == CAMERA_TAKE_HELPER) {
            if (TextUtils.isEmpty(mPictureFile)) {
                SharedPreferences sp = context.getSharedPreferences("config",
                        Context.MODE_PRIVATE);
                mPictureFile = sp.getString("addpath", "");
                Logger.i("mPicturePath" + mPictureFile);
            }
            filePath = mAvatarPath + File.separator + mPictureFile;
            bitmap = BitmapHelper.zoomBitmap(filePath, value, value);
            // TODO 缩小图片
            // filePath = sampleBitmap(filePath);
        } else if (requesCode == PICKED_PHOTO_WITH_DATA) {
            // mPictureFile = getPhotoFileName();
            // filePath = avatarPath + File.separator + mPictureFile;
            // bitmap = data.getParcelableExtra("data");
            // // 保存头像缩略图
            // BitmapHelper.createThumBitmap(filePath, bitmap);
            filePath = getFilePath(data);
            bitmap = BitmapHelper.zoomBitmap(filePath, value, value);
        }
        final ImageView imageView = new ImageView(context);
        // 所有图片使用同一个id,图片序号作为存储tag用来区分不同图片
        imageView.setTag(photoList.size());
        imageView.setId(PHOTO_ID);
        imageView.setImageBitmap(bitmap);
        Logger.i("filePath" + filePath);
        photoList.add(filePath);
        photoUploadList.add(filePath);
        imageView.setOnClickListener(this);
        if (isAdd) { // 作为添加控件时，长按取消
            imageView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteDialog(imageView);
                    return true;
                }
            });
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(value,
                value);
        params.leftMargin = (int) ViewHelper.dip2px(context, 5);
        imageView.setLayoutParams(params);
        llImages.addView(imageView);
    }

    /**
     * 拍照后重新加载
     * <p>
     * 1.先显示本地图片
     */
    public void refresh() {
        int requesCode = CAMERA_TAKE_HELPER;
        Intent data = null;
        String filePath = "";
        // 获取屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int value = ibAdd.getHeight(); // 得到的是像素
        if (requesCode == CAMERA_TAKE_HELPER) {
            filePath = mAvatarPath + File.separator + mPictureFile;
            photo = BitmapHelper.zoomBitmap(filePath, value, value);
        } else if (requesCode == PICKED_PHOTO_WITH_DATA) {
            mPictureFile = getPhotoFileName();
            photo = data.getParcelableExtra("data");
            filePath = mAvatarPath + File.separator + mPictureFile;
            // filePath = getFilePath(data);
        }
        // TODO 缩小图片
        filePath = sampleBitmap(filePath);
        ImageView imageView = new ImageView(context);
        // 所有图片使用同一个id,图片序号作为存储tag用来区分不同图片
        imageView.setTag(photoList.size());
        imageView.setId(PHOTO_ID);
        imageView.setImageBitmap(photo);
        photoList.add(filePath);
        photoUploadList.add(filePath);
        imageView.setOnClickListener(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(value,
                value);
        params.leftMargin = (int) ViewHelper.dip2px(context, 5);
        imageView.setLayoutParams(params);
        llImages.addView(imageView);
    }

    /**
     * 获得文件绝对路径
     */
    private String getFilePath(Intent data) {
        Uri uri = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        // 获得对应列名的列号
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Logger.i("PATH" + picturePath);
        return picturePath;
    }

    /**
     * 上传图片缩小
     *
     * @param filePath
     * @return 返回缩略图路径
     */
    private String sampleBitmap(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        Logger.i("kjx9" + "picWidth --->" + picWidth + "*" + picHeight);
        // 如果图片需要压缩，则更改路径
        if ((picWidth > picHeight) && (picHeight > 600)) {
            // 采样图片（缩小）
            Bitmap uploadPhoto = BitmapHelper.decodeSampleBitmapFromFile(
                    filePath, 800, 600);
            // 更改路径:使用采样后的路径
            filePath = mAvatarPath + File.separator + "sf_" + mPictureFile;
            // 创建缩略图
            BitmapHelper.createThumBitmap(filePath, uploadPhoto);
            Logger.i("kjx9" + "(picHeight > 600)");
        } else if ((picWidth < picHeight) && (picWidth > 600)) {
            Bitmap uploadPhoto = BitmapHelper.decodeSampleBitmapFromFile(
                    filePath, 600, 800);
            filePath = mAvatarPath + File.separator + "sf_" + mPictureFile;
            BitmapHelper.createThumBitmap(filePath, uploadPhoto);
            Logger.i("kjx9" + "(picWidth > 600)");
        }
        return filePath;
    }

    private void loadImage(List<PhotoInfo> photoInfos) {
        count = photoInfos.size();
        Logger.i("attach" + "-->" + count);
        for (PhotoInfo photoInfo : photoInfos) {
            // 图片地址：下载方法中加上服务器地址
            String picUrl = photoInfo.Address;
            if (picUrl.endsWith(".png") || picUrl.endsWith(".PNG")
                    || picUrl.endsWith(".jpg") || picUrl.endsWith(".JPG")) {
                // 如果本地有，直接使用
                float width = ViewHelper.dip2px(context, 75);// 75dp转为px为单位
                float height = ViewHelper.dip2px(context, 75);
                ImageView imageView = new ImageView(context);
                imageView.setTag(photoList.size());
                imageView.setId(PHOTO_ID);
                // imageView.setImageResource(R.drawable.remain_img);
                imageView.setImageResource(R.drawable.frame_loading);
                // imageView.setImageResource(R.drawable.pro_loading);
                // imageView.startAnimation(rotateAnimation);
                photoList.add(picUrl);
                Logger.e("filepath" + picUrl);
                showPicture(imageView, picUrl); // 加载图片
                imageView.setOnClickListener(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        (int) width, (int) height);
                params.leftMargin = (int) ViewHelper.dip2px(context, 5);
                imageView.setLayoutParams(params);
                llImages.addView(imageView);
            }
        }
    }

    /**
     * 显示图片
     *
     * @param ivAvatar 要显示的图片控件
     * @param avataUrl 图片url地址
     */
    private void showPicture(final ImageView ivAvatar, final String avataUrl) {
        // 如果本地数据库中存有用户头像信息
        if (!TextUtils.isEmpty(avataUrl) && avataUrl.contains("\\")) {
            int index = avataUrl.lastIndexOf("\\");
            // 从url地址得到图片名字
            String avatarName = avataUrl
                    .substring(index + 1, avataUrl.length()); // 图片名
            // 本地图片文件
            if (!TextUtils.isEmpty(avatarName)) {
                File file = new File(new File(mAvatarPath), avatarName);
                if (file.exists()) { // 本地有图直接使用
                    Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                    if (bitmap != null) {
                        Bitmap avatar = BitmapHelper.zoomBitmap(bitmap,
                                ViewHelper.dip2px(context, 75),
                                ViewHelper.dip2px(context, 75));
                        count--;
                        if (count <= 0) {
                            pBar.setVisibility(View.GONE);
                        }
                        ivAvatar.setImageBitmap(avatar);
                    }
                } else {
                    // 开启线程从网络下载
//					final HttpUtils httpUtils = new HttpUtils();
//					new Thread(new Runnable() {
//						@Override
//						public void run() {
//							httpUtils.downloadData(avataUrl, handler, ivAvatar);
//						}
//					}).start();
                }
            } else {
                // 开启线程从网络下载
//				final HttpUtils httpUtils = new HttpUtils();
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						httpUtils.downloadData(avataUrl, handler, ivAvatar);
//					}
//				}).start();
            }
        }
    }

    /**
     * 显示本地图片
     *
     * @param ivAvatar   图片控件
     * @param path       文件路径
     * @param avatarName 图片名
     */
    private void showLocalImg(final ImageView ivAvatar, String path,
                              String avatarName) {
        if (!TextUtils.isEmpty(avatarName)) {
            File file = new File(new File(path), avatarName);
            if (file.exists()) { // 本地有图直接使用
                Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                count--;
                if (count <= 0) {
                    pBar.setVisibility(View.GONE);
                }
                if (bitmap != null) {
                    Bitmap avatar = BitmapHelper.zoomBitmap(bitmap,
                            ViewHelper.dip2px(context, 75),
                            ViewHelper.dip2px(context, 75));
                    ivAvatar.setImageBitmap(avatar);
                }
            }
        }
    }

    /**
     * 显示本地图片
     *
     * @param ivAvatar         图片控件
     * @param fileAbsolutePaht 图片路径
     */
    private void showLocalImg(final ImageView ivAvatar, String fileAbsolutePaht) {
        if (!TextUtils.isEmpty(fileAbsolutePaht)) {
            File file = new File(fileAbsolutePaht);
            if (file.exists()) { // 本地有图直接使用
                Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                count--;
                if (count <= 0) {
                    pBar.setVisibility(View.GONE);
                }
                if (bitmap != null) {
                    Bitmap avatar = BitmapHelper.zoomBitmap(bitmap,
                            ViewHelper.dip2px(context, 75),
                            ViewHelper.dip2px(context, 75));
                    ivAvatar.setImageBitmap(avatar);
                }
            }
        }
    }

    /***
     * 显示本地图片列表
     *
     * @param photoPathList
     */
    private void loadLocalImgs(List<String> photoPathList) {

        /** 加载默认图片 */
        if (photoPathList != null && photoPathList.size() > 0) {
            for (String path : photoPathList) {
                if (path.endsWith(".png") || path.endsWith(".PNG")
                        || path.endsWith(".jpg") || path.endsWith(".JPG")) {
                    // 如果本地有，直接使用
                    float width = ViewHelper.dip2px(context, 75);// 75dp转为px为单位
                    float height = ViewHelper.dip2px(context, 75);
                    ImageView imageView = new ImageView(context);
                    imageView.setImageResource(R.drawable.loading_bg);
                    photoUploadList.add(path); // 记录本地图片
                    Logger.e("filepath" + path);
                    showLocalImg(imageView, path);
                    imageView.setOnClickListener(this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            (int) width, (int) height);
                    params.leftMargin = (int) ViewHelper.dip2px(context, 5);
                    imageView.setLayoutParams(params);
                    llImages.addView(imageView);
                }
            }

        }
    }

    /**
     * 显示照片详细信息
     *
     * @param pos
     */
    private void showImgDialog(int pos) {
        String picName = photoList.get(pos);
        // 如果本地数据库中存有用户头像信息
        if (!TextUtils.isEmpty(picName) && picName.contains("\\")) {
            int index = picName.lastIndexOf("\\");
            // 从url地址得到图片名字
            String avatarName = picName.substring(index + 1, picName.length()); // 图片名
            // 本地图片文件
            if (!TextUtils.isEmpty(avatarName)) {
                picName = mAvatarPath + File.separator + avatarName;
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);// 调用系统的图片查看器
        Uri mUri = Uri.fromFile(new File(picName));// 图片的路径
        intent.setDataAndType(mUri, "image/*");// 设置数据和格式
        activity.startActivity(intent);
    }

    /**
     * 弹出对话框是否删除照片
     *
     * @param
     */
    private void showDeleteDialog(final ImageView iView) {
        int tagValue = 0;
        try {
            tagValue = Integer.parseInt(iView.getTag().toString());
        } catch (Exception e) {
            // TODO: 有的时候出现崩溃
            Logger.i(TAG + "===v.getTag().toString()="
                    + iView.getTag().toString());
        }
        Logger.i("tag" + "tag:" + tagValue);
        final int pos = tagValue;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("删除图片");
        builder.setMessage("是否删除该图片?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photoList.remove(pos);
                iView.setVisibility(View.GONE);
                Logger.i("pos" + "删除pos--->" + pos);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getPhotoSerialNo() {
        return photoSerialNo;
    }

    // /**
    // * 获得要上传照片路径
    // *
    // * @return
    // */
    // public List<String> getPhotoUploadList() {
    // return photoList;
    // }

    public void setPhotoSerialNo(String photoSerialNo) {
        this.photoSerialNo = photoSerialNo;
    }

    /**
     * 获得要上传照片路径
     *
     * @return
     */
    public List<String> getPhotoList() {
        // return photoList;
        return photoUploadList;
    }

    /**
     * 设置是否隐藏添加按钮
     *
     * @param isAdd 是否隐藏添加按钮，false表示隐藏
     */
    public void setIsAddButton(Boolean isAdd) {
        if (isAdd) {
            ibAdd.setOnClickListener(this);
        } else {
            ibAdd.setVisibility(View.GONE);
        }
    }

    /**
     * 按下拍照按钮
     */
    public void doPickPhotoAction() {
        final Context dialogContext = new ContextThemeWrapper(context,
                android.R.style.Theme_Light);
        String[] choices;
        choices = new String[]{"拍照", "从相册中选择"};

        mDictIosPicker.show(choices);
        mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                onSelect(index);

                if (mOnSelectPhotoListener != null) {
                    mOnSelectPhotoListener.onSelected();
                }
            }
        });

    }

    private void onSelect(int which) {
        switch (which) {
            case 0: {
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                    doTakePhoto();// 用户点击了从照相机获取
                } else {
                    Toast.makeText(context, "没有SD卡", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 1:
                // doPickPhotoFromGallery();// 从相册中去获取
                // 调用系统图库
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(intent, PICKED_PHOTO_WITH_DATA);
                break;
        }
    }

    /**
     * 拍照获取图片
     */
    protected void doTakePhoto() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        mPictureFile = getPhotoFileName();
        File file = new File(mAvatarPath, mPictureFile);
        Uri imageUri = Uri.fromFile(file);

        SharedPreferences sp = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        sp.edit().putString("addpath", mPictureFile).commit();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//

        // try {
        // file.createNewFile();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        // 把照片保存在sd卡中指定位置。
        activity.startActivityForResult(intent, CAMERA_TAKE_HELPER);
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        return "IMG"
                + DateFormat.format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA)) + ".jpg";
    }

    /**
     * 图片选择图片 请求Gallery程序
     */
    protected void doPickPhotoFromGallery() {
        try {
            final Intent intent = getPhotoPickIntent();
            activity.startActivityForResult(intent, PICKED_PHOTO_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "失败", Toast.LENGTH_LONG).show();
        }
    }

    protected void doCropPhoto(String fileName) {
        try {
            File file = new File(fileName);
            Uri uri = Uri.fromFile(file);
            // 启动gallery去剪辑这个照片
            final Intent intent = getCropImageIntent(uri);
            activity.startActivityForResult(intent, PICKED_PHOTO_WITH_DATA);
        } catch (Exception e) {
            Toast.makeText(context, "failure", Toast.LENGTH_LONG).show();
        }
    }

    public String getFieldName() {
        return mFieldNameStr;
    }

    /**
     * 记录动态字段名
     */
    public void setFieldName(String fieldName) {
        this.mFieldNameStr = fieldName;
    }

    public void setOnSelectPhotoListener(
            InOnSelectPhotoListener onSelectPhotoListener) {
        this.mOnSelectPhotoListener = onSelectPhotoListener;
    }

    public interface InOnSelectPhotoListener {
        void onSelected();
    }

    /**
     * 处理头像handler
     *
     * @author bohr
     */
    public class HandlerPic extends Handler {
        // 标示固定
        public final int SHOW_IMAGE_SUCCESS = 3;
        public final int SHOW_IMAGE_FAILUREE = 4;
        private ImageView ivAvatars;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == SHOW_IMAGE_SUCCESS) {
                ivAvatars = (ImageView) msg.obj;
                // Bitmap bitmap = (Bitmap) ivAvatars.getTag();
                // Bitmap avatar = BitmapHelper.zoomBitmap(bitmap,
                // ViewHelper.dip2px(context, 75),
                // ViewHelper.dip2px(context, 75));
                // ivAvatars.setImageBitmap(avatar);
                // ivAvatars.getAnimation().cancel();
//				String avatarName = (String) ivAvatars.getTag();
//				showLocalImg(ivAvatars, mAvatarPath, avatarName);

                int pos = msg.arg2;
                String avatarName = (String) ivAvatars.getTag();
                showLocalImg(ivAvatars, mAvatarPath, avatarName);
                if (pos >= 0) {
                    ivAvatars.setTag(pos);
                }
            } else if (what == SHOW_IMAGE_FAILUREE) {
                ivAvatars = (ImageView) msg.obj;
                // ivAvatars.setImageResource(R.drawable.remain_img);
                ivAvatars.setVisibility(View.GONE);
            } else if (what == LOAD_IMAGE) {
                List<PhotoInfo> picInfos = (List<PhotoInfo>) msg.obj;
                loadImage(picInfos);
            } else if (what == PHOTOS_ISNULL) {
                addImageView.setVisibility(View.GONE);
            }
        }
    }
}
