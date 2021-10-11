package com.biaozhunyuan.tianyi.common.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import static com.biaozhunyuan.tianyi.common.utils.ImageUtils.getDownloadUrlById;

/**
 * 通用的ViewHolder
 */
public class BoeryunViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context context;
    private DictionaryHelper helper;

    private BoeryunViewHolder(int position, ViewGroup parent, Context context,
                              int layoutId) {
        mViews = new SparseArray<View>();
        this.context = context;
        helper = new DictionaryHelper(context);
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mConvertView.setTag(this);
    }

    /**
     * 获得持有convertView和其中各个控件 的实体类ViewHolder
     *
     * @param position    数据源的序号（位置）
     * @param convertView 由xml加载的view
     * @param parent      父控件
     * @param context
     * @param layoutId    item的xml资源ID
     * @return
     */
    public static BoeryunViewHolder getInstance(int position, View convertView,
                                                ViewGroup parent, Context context, int layoutId) {
        if (convertView == null) {
            return new BoeryunViewHolder(position, parent, context, layoutId);
        } else {
            return (BoeryunViewHolder) convertView.getTag();
        }
    }

    /***
     * 获得Item中指定编号的一个View
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
//        AnimateUtil.setFillingAnimate(view);
        return (T) view;
    }

    /***
     * 设置指定id的textView显示指定内容
     *
     * @param viewId  控件布局编号
     * @param content 显示内容
     * @return
     */
    public void setTextValue(int viewId, String content) {
        TextView textView = getView(viewId);
        if (textView != null
                && (textView instanceof TextView || textView instanceof EditText)) {
            textView.setText(StrUtils.pareseNull(content));
        }
    }
    /***
     * 设置指定id的textView显示指定内容
     *
     * @param viewId  控件布局编号
     * @param Color 颜色
     * @return
     */
    public void setTextColor(int viewId, String Color) {
        TextView textView = getView(viewId);
        textView.setTextColor(android.graphics.Color.parseColor(Color));
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param resourceId 图片资源编号
     * @return
     */
    public BoeryunViewHolder setImageResoure(int viewId, int resourceId) {
        ImageView imageView = getView(viewId);
        if (imageView != null && (imageView instanceof ImageView)) {
            imageView.setImageResource(resourceId);
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param
     * @return
     */
    public BoeryunViewHolder setImageByUrl(int viewId, String url) {
        PhotoImageLoader.getInstance(3, PhotoImageLoader.Type.LIFO).loadImage(url,
                (ImageView) getView(viewId));
        return this;
    }

    /**
     * 根据图片id为ImageView设置图片
     *
     * @param viewId
     * @param
     * @return
     */
    public BoeryunViewHolder setImageById(int viewId, String uuid) {
        setUserPhotoById(uuid, "", viewId);
        return this;
    }

    /**
     * 根据图片id为ImageView设置图片
     *
     * @param corlor
     * @param
     * @return
     */
    public BoeryunViewHolder setBackgroundColor(int corlor) {
        mConvertView.setBackgroundColor(corlor);
        return this;
    }

    /**
     * 根据图片id显示图片z
     *
     * @param id
     * @param viewId
     */
    public BoeryunViewHolder setImageById(String id, int viewId) {
        ImageView view = getView(viewId);
        String url = getDownloadUrlById(id);
        String cookie = CookieUtils.cookieHeader(url);
        DisplayImageOptions options = new DisplayImageOptions
                .Builder()
                .extraForDownloader(cookie)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(url.toString(), view, options);
        return this;
    }

    /**
     * 根据用户id 显示用户头像 加载失败则显示姓名
     */
    public BoeryunViewHolder setUserPhotoById(int viewId, User user) {
        return setUserPhotoById(user.getAvatar(), user.getName(), viewId);
    }

    /**
     * 根据用户id 显示用户头像 加载失败则显示姓名
     *
     * @param userAvatar 用户头像id
     */
    public BoeryunViewHolder setUserPhotoById(String userAvatar, final String userName, int viewId) {

        final ImageView iv = getView(viewId);
        if (!TextUtils.isEmpty(userAvatar)) {
            final String url = getDownloadUrlById(userAvatar);
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
                    if (iv instanceof AvatarImageView) {
                        AvatarImageView imageView = (AvatarImageView) iv;
                        imageView.setTextAndColor(userName, AvatarImageView.COLORS[1]); //加载失败显示文字
                    } else {
                        iv.setImageResource(R.drawable.default_error);
                    }
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
        } else {
            if (iv instanceof AvatarImageView) {
                AvatarImageView imageView = (AvatarImageView) iv;
                imageView.setTextAndColor(userName, AvatarImageView.COLORS[1]); //加载失败显示文字
            } else {
                iv.setImageResource(R.drawable.default_error);
            }
        }
        return this;
    }

    /**
     * 为控件显示员工头像
     *
     * @param viewId
     * @param uuid   员工的uuid
     * @return
     */
    public BoeryunViewHolder setUserPhoto(int viewId, String uuid) {
        User user = helper.getUser(uuid);
        setUserPhotoById(user.getAvatar(), user.getName(), viewId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param
     * @return
     */
    public BoeryunViewHolder setImageByCache(int viewId, String url) {
        PhotoImageLoader.getInstance(3, PhotoImageLoader.Type.LIFO).loadImage(url,
                (ImageView) getView(viewId));
        return this;
    }

    public View getConvertView() {
        return mConvertView;
    }

}
