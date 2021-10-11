package com.biaozhunyuan.tianyi.wechat;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.attach.BoeryunDownloadManager;
import com.biaozhunyuan.tianyi.common.attach.DownloadFile;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.MediaManager;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.OpenIntentUtils;
import com.biaozhunyuan.tianyi.wechat.model.WeChatMsg;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.List;

import cn.andy.qpopuwindow.QPopuWindow;

/**
 * 作者： bohr
 * 日期： 2020-06-15 17:58
 * 描述：
 */
public class WeChatInfoAdapter extends BaseMultiItemQuickAdapter<WeChatMsg, BaseViewHolder> {
    private BoeryunDownloadManager downloadManager;
    private int rawX;
    private int rawY;
    private String[] showItems = new String[]{"使用扬声器播放"};

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public WeChatInfoAdapter(List<WeChatMsg> data) {
        super(data);
        downloadManager = BoeryunDownloadManager.getInstance();
        addItemType(1, R.layout.we_chat_text); //文字
        addItemType(3, R.layout.we_chat_img); //图片
        addItemType(34, R.layout.we_chat_audio); //语音
        addItemType(43, R.layout.we_chat_video); //视频
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WeChatMsg item) {
        switch (helper.getItemViewType()) {
            case 1://文字
                if (item.getDirection() == 0) { //如果不是自己发送的，直接显示对方头像
                    ImageUtils.displyImage(item.getCustomerIcon(), helper.getView(R.id.ic_user));
                } else {
                    if (!TextUtils.isEmpty(item.getSystemIcon())) {
                        ImageUtils.displyImage(ImageUtils.getDownloadUrlById
                                (item.getSystemIcon()), helper.getView(R.id.ic_user), R.drawable.default_head);
                    } else {
                        ImageUtils.displyUserPhotoById(mContext, item.getStaffId(), helper.getView(R.id.ic_user));
                    }
                }
                helper.setText(R.id.tv_name_chat_activity, item.getSender());
                helper.setText(R.id.tv_time_chat, ViewHelper.fromLongToDate(item.getCreateTime()));
                helper.setText(R.id.tv, item.getMsgContent());
                break;
            case 3://图片
                if (item.getDirection() == 0) {
                    ImageUtils.displyImage(item.getCustomerIcon(), helper.getView(R.id.ic_user));
                } else {
                    if (!TextUtils.isEmpty(item.getSystemIcon())) {
                        ImageUtils.displyImage(ImageUtils.getDownloadUrlById
                                (item.getSystemIcon()), helper.getView(R.id.ic_user), R.drawable.default_head);
                    } else {
                        ImageUtils.displyUserPhotoById(mContext, item.getStaffId(), helper.getView(R.id.ic_user));
                    }
                }
                helper.setText(R.id.tv_name_chat_activity, item.getSender());
                helper.setText(R.id.tv_time_chat, ViewHelper.fromLongToDate(item.getCreateTime()));
                ImageUtils.displyImageById(item.getAttachmentId(), helper.getView(R.id.iv_chat_list));
                helper.getView(R.id.iv_chat_list).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtils.startSingleImageBrower(mContext,
                                ImageUtils.getDownloadUrlById(item.getAttachmentId()));
                    }
                });
                break;
            case 34://语音
                if (item.getDirection() == 0) {
                    ImageUtils.displyImage(item.getCustomerIcon(), helper.getView(R.id.chat_item_header));
                } else {
                    if (!TextUtils.isEmpty(item.getSystemIcon())) {
                        ImageUtils.displyImage(ImageUtils.getDownloadUrlById
                                (item.getSystemIcon()), helper.getView(R.id.chat_item_header), R.drawable.default_head);
                    } else {
                        ImageUtils.displyUserPhotoById(mContext, item.getStaffId(), helper.getView(R.id.chat_item_header));
                    }
                }
                helper.setText(R.id.tv_name_chat_activity, item.getSender());
                helper.setText(R.id.tv_time_chat, ViewHelper.fromLongToDate(item.getCreateTime()));
                ImageView ivSound = helper.getView(R.id.ivAudio);
                RelativeLayout rlTxt = helper.getView(R.id.rl_txt);
                rlTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filePath = FilePathConfig.getCacheDirPath() + File.separator
                                + item.getAttachmentId() + ".mp3";
                        File file = new File(filePath);
                        if (!file.exists()) {
                            downloadAudio(item, ivSound);
                        } else {
                            palyAudio(item, ivSound, false);
                        }
                    }
                });

                rlTxt.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        rawX = (int) event.getRawX();
                        rawY = (int) event.getRawY();
                        return false;
                    }
                });

                rlTxt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        QPopuWindow.getInstance(mContext).builder
                                .bindView(v, 0)
                                .setPopupItemList(showItems)
                                .setPointers(rawX, rawY)
                                .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                                    /**
                                     * @param anchorView 为pop的绑定view
                                     * @param anchorViewPosition  pop绑定view在ListView的position
                                     * @param position  pop点击item的position 第一个位置索引为0
                                     */
                                    @Override
                                    public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                                        String type = showItems[position];
                                        if ("使用扬声器播放".equals(type)) {
                                            palyAudio(item, ivSound, true);
                                        }
                                    }
                                }).show();
                        return true;
                    }
                });
                break;
            case 43://视频
                if (item.getDirection() == 0) {
                    ImageUtils.displyImage(item.getCustomerIcon(), helper.getView(R.id.ic_user));
                } else {
                    if (!TextUtils.isEmpty(item.getSystemIcon())) {
                        ImageUtils.displyImage(ImageUtils.getDownloadUrlById
                                (item.getSystemIcon()), helper.getView(R.id.ic_user), R.drawable.default_head);
                    } else {
                        ImageUtils.displyUserPhotoById(mContext, item.getStaffId(), helper.getView(R.id.ic_user));
                    }
                }
                helper.setText(R.id.tv_name_chat_activity, item.getSender());
                helper.setText(R.id.tv_time_chat, ViewHelper.fromLongToDate(item.getCreateTime()));

                helper.getView(R.id.iv_chat_list).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filePath = FilePathConfig.getCacheDirPath() + File.separator
                                + item.getAttachmentId() + ".mp4";
                        File file = new File(filePath);
                        if (!file.exists()) {
                            downloadAudio(item, null);
                        } else {
                            OpenIntentUtils.openFile(mContext, filePath);
                        }
                    }
                });
                break;
        }
    }


    private void downloadAudio(WeChatMsg model, ImageView iv) {
        DownloadFile downloadFile = new DownloadFile();
        downloadFile.atttachId = model.getAttachmentId();
        downloadFile.attachName = model.getAttachmentId() + (iv == null ? ".mp4" : ".mp3");
        downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.显示附件 + model.getAttachmentId();
        downloadManager.download(downloadFile);
        downloadManager.setHandler(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                DownloadFile downloadFile = (DownloadFile) msg.obj;
                if (downloadFile != null && downloadFile.downloadState
                        == BoeryunDownloadManager.DOWNLOAD_STATE_FINISH) {
                    String localPath = FilePathConfig.getCacheDirPath() + File.separator
                            + downloadFile.attachName;
                    model.setLocalAttach(localPath);
                    if (iv == null) {
                        OpenIntentUtils.openFile(mContext, localPath);
                    } else {
                        palyAudio(model, iv, false);
                    }
                }
                return true;
            }
        }));
    }

    private void palyAudio(WeChatMsg model, ImageView ivAudio, boolean isLoud) {
        MediaManager.reset();
        ivAudio.setBackgroundResource(com.example.chat.R.drawable.audio_animation_left_list);
        AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
        drawable.start();

        String filePath = FilePathConfig.getCacheDirPath() + File.separator
                + model.getAttachmentId() + ".mp3";

        MediaManager.playSound(mContext, filePath, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivAudio.setBackgroundResource(com.example.chat.R.drawable.audio_animation_list_left_3);
                MediaManager.release();
            }
        }, isLoud);
    }
}
