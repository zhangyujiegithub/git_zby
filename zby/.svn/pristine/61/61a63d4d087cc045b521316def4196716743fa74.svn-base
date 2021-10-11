package com.biaozhunyuan.tianyi.common.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class ImageUtils {

    /***
     * 获取附件文件下载的通用地址
     *
     * @return
     */
    public static String getDownloadUrlByAddress(String address) {
        return Global.BASE_JAVA_URL + address;
    }

    /***
     * 通过附件
     *
     * @return
     */
//    public static String getDownloadUrlById(String attachId) {
//        return Global.BASE_URL + "FileUpDownLoad/downloadAttach/" + attachId;
//    }

    /***
     * 根据附件id获取url
     *
     * @return
     */
    public static String getDownloadUrlById(String attachId) {
        return Global.BASE_JAVA_URL + GlobalMethord.显示附件 + attachId;
    }

    /***
     * 根据user的UUID获取url
     *
     * @return
     */
    public static String getUserPhotoURLById(Context context, String uuid) {
        String userPhoto = new DictionaryHelper(context).getUserPhoto(uuid);
        return Global.BASE_JAVA_URL + GlobalMethord.显示附件 + userPhoto;
    }

    /***
     * 根据文件后缀判断该文件是否是图片
     *
     * @param suffix
     * @return
     */
    public static boolean isImage(String suffix) {
        if (TextUtils.isEmpty(suffix)) {
            return false;
        }
        if (suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg")
                || suffix.equalsIgnoreCase("jpeg")
                || suffix.equalsIgnoreCase("bmp")) {
            return true;
        }
        return false;
    }

    /**
     * 打开可滑动的图片查看器
     *
     * @param position 位置
     * @param urls     图片路径相对路径集合
     */
    public static void startImageBrower(Context context, int position,
                                        ArrayList<String> urls) {

        ArrayList<String> urlList = new ArrayList<String>();
        for (int i = 0; i < urls.size(); i++) {
            urlList.add(urls.get(i));
        }

        ComponentName comp = new ComponentName(context, "com.biaozhunyuan.tianyi.attch.ImagePagerActivity");
        Intent intent = new Intent();
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra("image_urls", urlList);
        intent.putExtra("image_index", position);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    /**
     * 打开单张的图片查看器
     *
     * @param url 图片路径相对路径
     */
    public static void startSingleImageBrower(Context context, String url) {
        ArrayList<String> urls = new ArrayList<String>();
        urls.add(url);
        startImageBrower(context, 0, urls);
    }


    public static void displyImage(String url, ImageView iv) {
        displyImage(url, iv, -1);
    }

    /**
     * 显示图片z
     *
     * @param url
     * @param iv
     */
    public static void displyImage(final String url, final ImageView iv, final int failedImg) {
        String cookie = CookieUtils.cookieHeader(url);
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .extraForDownloader(cookie)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        iv.setTag(url);
        ImageLoader.getInstance().displayImage(url.toString(), iv, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (failedImg != -1) {
                    iv.setImageResource(failedImg);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (url.equals(view.getTag())) {
                    iv.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }


    /**
     * 根据图片id显示图片z
     *
     * @param id
     * @param view
     */
    public static void displyImageById(String id, ImageView view) {
        String url = getDownloadUrlById(id);
        String cookie = CookieUtils.cookieHeader(url);
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .extraForDownloader(cookie)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(url.toString(), view, options);
    }

    public static void displyImageById(String id, final ImageView view, final int defaultImg) {
        String url = getDownloadUrlById(id);
        String cookie = CookieUtils.cookieHeader(url);
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .extraForDownloader(cookie)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(url.toString(), view, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view1, FailReason failReason) {
                if (defaultImg != -1) {
                    view.setImageResource(defaultImg);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    /**
     * 根据用户id 显示用户头像 加载失败则显示姓名
     *
     * @param context
     * @param userId
     * @param iv
     */
    public static void displyUserPhotoById(Context context, String userId, final AvatarImageView iv) {
        displyUserPhotoById(context, userId, iv, AvatarImageView.COLORS[1]);
    }

    /**
     * 根据用户id 显示用户头像 加载失败则显示姓名
     *
     * @param context
     * @param userId
     * @param iv
     * @param color   头像背景颜色
     */
    public static void displyUserPhotoById(Context context, String userId, final AvatarImageView iv, final int color) {
        User user = new DictionaryHelper(context).getUser(userId);
        String userPhoto = user.getAvatar();
        final String name = user.getName();
        if (TextUtils.isEmpty(userPhoto)) {
            iv.setTextAndColor(name, color); //加载失败显示文字
        } else {
            final String url = getDownloadUrlById(userPhoto);
            String cookie = CookieUtils.cookieHeader(url);
            iv.setTag(url);
            DisplayImageOptions options = new DisplayImageOptions
                    .Builder()
                    .extraForDownloader(cookie)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoader.getInstance().displayImage(url.toString(), iv, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
//                    iv.setImageResource(R.drawable.default_head);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    iv.setTextAndColor(name, color); //加载失败显示文字
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (url.equals(iv.getTag())) {
                        iv.setImageBitmap(loadedImage);
                    }
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
        }
    }

    public static Bitmap getBitmapByUrl(String url) {
        String cookie = CookieUtils.cookieHeader(url);
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .extraForDownloader(cookie)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        return ImageLoader.getInstance().loadImageSync(url, options);
    }


    static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
            .showImageForEmptyUri(R.drawable.ico_default_user) //
            .showImageOnFail(R.drawable.ico_default_user).build();//

    /***
     * 根据员工编号显示头像
     * @param context 当前上下文
     * @param userId 员工编号
     * @param ivAvatar 头像ImageView
     */
    public static void displayAvatar(Context context, String userId, ImageView ivAvatar) {
        DictionaryHelper dictionaryHelper = new DictionaryHelper(context);
//		String name = dictionaryHelper.getUserNameById(userId);
        String avatarUrl = dictionaryHelper.getUserPhoto(userId);
        if (!TextUtils.isEmpty(avatarUrl)) {
            ImageLoader.getInstance().displayImage(Global.BASE_URL + avatarUrl,
                    ivAvatar, defaultOptions);
        } else {
            ivAvatar.setImageResource(R.drawable.ico_default_user);
        }
    }

//	/***
//	 * 根据员工编号显示图片信息
//	 * @param ivImg 头像ImageView
//	 */
//	public static void displayImage(String filepath, ImageView ivImg, int defalutImgRes) {
//		DisplayImageOptions options = new DisplayImageOptions.Builder() //
//				.showImageForEmptyUri(defalutImgRes) //
//				.showImageOnFail(defalutImgRes).build();//
//		if (!TextUtils.isEmpty(filepath)) {
//			ImageLoader.getInstance().displayImage(filepath,
//					ivImg, options);
//		} else {
//			ivImg.setImageResource(defalutImgRes);
//		}
//	}
}
