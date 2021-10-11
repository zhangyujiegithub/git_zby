package com.biaozhunyuan.tianyi.attch;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.AttachBiz;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.RoundImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.biaozhunyuan.tianyi.attch.EnumAttachType.附件号;

public class UploadAttachAdapter extends BaseAdapter {
    /***
     * 默认图片宽度 85dp
     */
    private static int mPicWidth = 85;
    private final String ATTACH_METHOD = Global.BASE_URL
            + "FileUpDownLoad/downloadAttach/";
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 是否新建
     */
    private boolean mIsAdd = true;
    public boolean mIsDelete;
    private List<AttachInfo> mAttachInfos = new ArrayList<AttachInfo>();
    private OnAdapterItemClickListener mOnItemClickListener;

    /***
     * @param ctx  当前上下文
     * @param urls 图片下载url地址
     */
    public UploadAttachAdapter(Context ctx, List<String> urls, boolean isAdd) {
        this.mContext = ctx;
        this.mIsAdd = isAdd;
        mAttachInfos.clear();
        for (String url : urls) {
            AttachInfo attachInfo = new AttachInfo();
            mAttachInfos.add(attachInfo);
        }

        if (mIsAdd) {
            initAddView();
        }
    }

    /***
     * @param ctx 当前上下文
     * @param
     */
    public UploadAttachAdapter(Context ctx, String attachIds, boolean isAdd) {
        this.mContext = ctx;
        this.mIsAdd = isAdd;
        this.mIsDelete = isAdd;
        mAttachInfos.clear();
        String[] mAttachArr = attachIds.split(",");
        for (String attachId : mAttachArr) {
            AttachInfo attachInfo = new AttachInfo();
            attachInfo.setIdAndUpdateType(attachId);
            mAttachInfos.add(attachInfo);
        }
        initAddView();
    }

    private void initAddView() {
        AttachInfo attachInfo = getAddAttachInfo();
        if (mIsAdd) {
            mAttachInfos.add(attachInfo);
        } else {
            mAttachInfos.remove(attachInfo);
        }
    }

    private AttachInfo getAddAttachInfo() {
        AttachInfo attachInfo = new AttachInfo();
        attachInfo.setType(EnumAttachType.新建);
        return attachInfo;
    }

    public void notifySetChangeIsAdd(boolean isAdd) {
        if (mIsAdd != isAdd) {
            mIsAdd = isAdd;
            // 如果原来不可新建，则刷新页面显示新建图标
            initAddView();
            notifyDataSetChanged();
        }
    }

    public void notifySetChangeIsDelete(boolean isDelete) {
        mIsDelete = isDelete;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAttachInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mAttachInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.item_attachinfo_grid, null);
        RoundImageView iv = view.findViewById(R.id.iv_attachinfo_item);
        TextView tvFileName = view.findViewById(R.id.tv_file_name);

        ImageView ivDelete = (ImageView) view
                .findViewById(R.id.iv_delte_attachinfo_item);

        RelativeLayout.LayoutParams params = new LayoutParams(mPicWidth,
                mPicWidth);
        iv.setLayoutParams(params);

        AttachInfo attachInfo = mAttachInfos.get(position);
        Attach attach = attachInfo.getAttach();
        tvFileName.setVisibility(View.INVISIBLE);


        if (mIsDelete && attachInfo.getType() != EnumAttachType.新建) {
            Logger.i("upload" + "显示删除按钮");
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            ivDelete.setVisibility(View.GONE);
        }

        ivDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mAttachInfos.remove(position);
                notifySetChangeIsDelete(false);
            }
        });

        iv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mIsAdd) {
                    notifySetChangeIsDelete(true);
                }
                return true;
            }
        });

        String url = "";
        switch (attachInfo.getType()) {
            case 附件号:
                if (attach != null) {
                    String suffix = "";
                    try {
                        suffix = attach.getName().substring(attach.getName().lastIndexOf(".") + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (ImageUtils.isImage(suffix)) {
                        ImageUtils.displyImageById(attach.getUuid(), iv);
                    } else {
                        iv.setImageResource(AttachBiz
                                .getImageResoureIdBySuffix(suffix));

                        tvFileName.setVisibility(View.VISIBLE);
                        tvFileName.setText(attach.filename);
                    }
                }

                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onOpenUrl(position, mAttachInfos);
                        }
                    }
                });
                break;
            case 本地路径:
                File file = new File(attachInfo.getLocalPath());
                if (file != null && file.exists()) {
                    if (checkEndsWithInStringArray(file.getName(), mContext.getResources()
                            .getStringArray(R.array.fileEndingImage))) {
                        Bitmap bitmap = BitmapHelper.zoomBitmap(attachInfo.getLocalPath(),
                                mPicWidth, mPicWidth);
                        iv.setImageBitmap(bitmap);
                    } else {
                        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        iv.setImageResource(AttachBiz
                                .getImageResoureIdBySuffix(suffix));
                    }
                }

                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onOpenUrl(position, mAttachInfos);
                        }
                    }
                });

                break;
            case 新建:
//                iv.setImageResource(R.drawable.ico_add_photo);
                iv.setImageResource(R.drawable.ic_add_attch);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onAdd();
                        }
                    }
                });
                break;
            default:
                break;
        }
        return view;
    }

    public int getPicWidth() {
        return mPicWidth;
    }

    public void setPicWidth(int mPicWidth) {
        this.mPicWidth = mPicWidth;
//        this.mPicWidth = (int) ViewHelper.dip2px(mContext, 60);
    }

    /**
     * 添加一个附件实体
     *
     * @param attachInfo
     */
    public void addItem(AttachInfo attachInfo) {
        if (mIsAdd) {
            if (mAttachInfos.size() >= 1) {
                mAttachInfos.add(mAttachInfos.size() - 1, attachInfo);
            }
        } else {
            mAttachInfos.add(attachInfo);
        }
        notifyDataSetChanged();
    }

    /**
     * 添加一个附件实体
     *
     * @param
     */
    public void addItems(List<AttachInfo> addAttachInfos, boolean isClearOldData) {
        if (isClearOldData) {
            mAttachInfos.clear();
            initAddView();
        }

        if (mIsAdd && mAttachInfos != null && mAttachInfos.size() > 0) {
            mAttachInfos.addAll(mAttachInfos.size() - 1, addAttachInfos);
        } else {
            mAttachInfos.addAll(addAttachInfos);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取指定类型的附件集合
     */
    private List<AttachInfo> getAttachInfoListByType(EnumAttachType type) {
        List<AttachInfo> pathList = new ArrayList<AttachInfo>();
        for (AttachInfo attachInfo : mAttachInfos) {
            if (attachInfo != null && attachInfo.getType() == type) {
                pathList.add(attachInfo);
            }
        }
        return pathList;
    }

    /**
     * 获取当地文件路径
     */
    public List<String> getLocalPathList() {
        List<String> pathList = new ArrayList<String>();
        List<AttachInfo> attachInfos = getAttachInfoListByType(EnumAttachType.本地路径);
        for (AttachInfo attachInfo : attachInfos) {
            if (attachInfo != null) {
                pathList.add(attachInfo.getLocalPath());
            }
        }
        return pathList;
    }

    /**
     * 获取不包括新建 数据源
     */
    public List<AttachInfo> getAttachDataList() {
        List<AttachInfo> pathList = new ArrayList<AttachInfo>();
        for (AttachInfo attachInfo : mAttachInfos) {
            if (attachInfo != null && attachInfo.getType() != EnumAttachType.新建) {
                pathList.add(attachInfo);
            }
        }
        return pathList;
    }

    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }


    /**
     * 获取已有附件号编号 1,2,3
     */
    public String getAttachIds() {
        StringBuilder sBuilder = new StringBuilder();
        List<AttachInfo> attachInfos = getAttachInfoListByType(附件号);
        for (AttachInfo attachInfo : attachInfos) {
            if (attachInfo != null && !TextUtils.isEmpty(attachInfo.getUuid())) {
                sBuilder.append(attachInfo.getUuid() + ",");
            }
        }
        String attachIds = sBuilder.toString();
        if (attachIds.endsWith(",")) {
            attachIds = attachIds.substring(0, attachIds.length() - 1);
        }
        return attachIds;
    }

    public void setOnAdapterItemClickListener(
            OnAdapterItemClickListener onAdapterItemClickListener) {
        this.mOnItemClickListener = onAdapterItemClickListener;
    }

    public interface OnAdapterItemClickListener {
        void onAdd();

        void onOpenLocal(int pos, List<AttachInfo> attachInfos);

        void onOpenUrl(int pos, List<AttachInfo> attachInfos);
    }
}
