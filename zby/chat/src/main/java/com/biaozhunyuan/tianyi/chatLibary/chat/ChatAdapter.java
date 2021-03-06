package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.Command;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.MessageSendStatusEnum;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.RecentMessage;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.attach.BoeryunDownloadManager;
import com.biaozhunyuan.tianyi.common.attach.DownloadFile;
import com.biaozhunyuan.tianyi.common.attach.OpenFilesIntent;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.AttachBiz;
import com.biaozhunyuan.tianyi.common.helper.DataCleanManager;
import com.biaozhunyuan.tianyi.common.helper.DateDeserializer;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.MediaManager;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.helper.UploadHelperListener;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ClickUtil;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.common.view.HorizontalRecentlySelectedStaffView;
import com.biaozhunyuan.tianyi.common.view.RoundImageView;
import com.example.chat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.andy.qpopuwindow.QPopuWindow;
import okhttp3.Request;

/**
 * Created by WangChang on 2016/4/28.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BaseAdapter> {

    private List<ChatMessage> dataList = new ArrayList<>();
    public transient HashMap<String, Bitmap> hashMaps = new HashMap<>();
    private Activity mContext;
    private DictionaryHelper helper;
    private SharedPreferencesHelper preferencesHelper;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;
    private DeleteItemListener deleteItemListener;
    private UploadFileFinshListener fileFinshListener;
    private ReplyMessageListener replyMessageListener;
    private ClickReplyMessageListener clickReplyMessageListener;
    private int rawX;
    private int rawY;
    private HorizontalRecentlySelectedStaffView staffView;
    private String managerId = "";
    private boolean isSending = false;


    public ChatAdapter(Activity context, List<ChatMessage> list) {
        mContext = context;
        dataList = list;
        helper = new DictionaryHelper(mContext);
        preferencesHelper = new SharedPreferencesHelper(mContext);
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public void replaceAll(ArrayList<ChatMessage> list) {
        dataList.clear();
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(ChatMessage list) {
        if (dataList != null && list != null) {
            dataList.add(list);
            notifyDataSetChanged();
        }

    }

    public void addAll(List<ChatMessage> list, boolean isClear) {
        if (dataList != null && list != null) {
            if (isClear) {
                dataList.clear();
            }
            dataList.addAll(list);
            notifyDataSetChanged();
        }

    }


    //?????????????????????????????????????????????????????????????????????????????????????????????????????????
    public void notifyItemStatus(String msgId) {
        if (dataList != null) {
            for (ChatMessage message : dataList) {
                if (message.getId().equals(msgId)) {
                    message.setSendStatus(MessageSendStatusEnum.????????????.getStatus());
                    HashMap<String, RecentMessage> hashMapData = preferencesHelper.getHashMapData
                            (Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
                    RecentMessage recentMessage = new RecentMessage();
                    recentMessage.setRecentMessages(dataList);
                    hashMapData.put(message.getChatId(), recentMessage);
                    preferencesHelper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", hashMapData);

                    List<GroupSession> listData = preferencesHelper.getListData
                            (Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);//????????????
                    for (GroupSession session : listData) {
                        if (session.getChatId().equals(message.getChatId())) {
                            session.setSendStatus(MessageSendStatusEnum.????????????.getStatus());
                            preferencesHelper.putListData(
                                    Global.mUser.getCorpId() + Global.mUser.getUuid(), listData);
                        }
                    }
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    public ChatMessage getItemAtPosition(int pos) {
        return dataList.get(pos);
    }

    public void clearAll() {
        dataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ChatAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ChatMessage.CHAT_Left:
                return new ChatAViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_left, parent, false));
            case ChatMessage.CHAT_RIGHT:
                return new ChatBViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_right, parent, false));
            case ChatMessage.CHAT_RIGHT_AUDIO:
                return new ChatRightAudioViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_send, parent, false));
            case ChatMessage.CHAT_LEFT_AUDIO:
                return new ChatRightAudioViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio_recive, parent, false));
            case ChatMessage.CHAT_RIGHT_FILE:
                return new ChatRightFileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_send, parent, false));
            case ChatMessage.CHAT_LEFT_FILE:
                return new ChatLeftFileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_recive, parent, false));
            case ChatMessage.CHAT_TIP:
                return new chatTipViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_tip, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatAdapter.BaseAdapter holder, int position) {
        holder.setData(position, dataList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getChatCategory();
    }


    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(int position, Object object) {

        }
    }

    /**
     * ??????????????????????????????????????????
     */
    private class ChatAViewHolder extends BaseAdapter {
        private AvatarImageView ic_user;
        private TextView tv;
        private TextView tvName;
        private TextView tvTime;
        private ImageView iv;
        private LinearLayout llRoot;
        private LinearLayout llTxt;

        //??????????????????
        private LinearLayout llReply;
        private TextView tvReplyName;
        private ImageView ivReplyType;
        private TextView tvReplyContent;

        public ChatAViewHolder(View view) {
            super(view);
            ic_user = itemView.findViewById(R.id.ic_user);
            tv = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv_chat_list);
            tvName = itemView.findViewById(R.id.tv_name_chat_activity);
            tvTime = itemView.findViewById(R.id.tv_time_chat);
            llRoot = itemView.findViewById(R.id.ll_root);
            llTxt = itemView.findViewById(R.id.ll_txt);

            llReply = itemView.findViewById(R.id.ll_reply);
            tvReplyName = itemView.findViewById(R.id.tv_name_reply);
            ivReplyType = itemView.findViewById(R.id.iv_type_reply);
            tvReplyContent = itemView.findViewById(R.id.tv_content_reply);
        }

        @Override
        void setData(final int position, Object object) {
            super.setData(position, object);
            final ChatMessage model = (ChatMessage) object;
            if (model.getKeyValues() != null) {
                String isSingle = model.getKeyValues().get("isSingle"); //???????????????
                if ("1".equals(isSingle)) { //????????????????????????????????????
                    tvName.setVisibility(View.GONE);
                } else {
                    tvName.setVisibility(View.VISIBLE);
                    tvName.setText(helper.getUserNameById(model.getFromUuid()));
                }
            } else {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(helper.getUserNameById(model.getFromUuid()));
            }

            ImageUtils.displyUserPhotoById(mContext, model.getFromUuid(), ic_user);
            if (Command.chat.equalsIgnoreCase(model.getCmd())) {
                if ("img".equalsIgnoreCase(model.getFormat())) {  //image??????
                    llTxt.setVisibility(View.GONE);
                    iv.setVisibility(View.VISIBLE);
                    ImageUtils.displyImageById(model.getBody(), iv);
                } else {  //txt??????
                    llTxt.setVisibility(View.VISIBLE);
                    iv.setVisibility(View.GONE);
                    tv.setText(model.getBody());
                }
            }


            ChatMessage message = model.getReplyMsg();
            if (message != null) {
                llReply.setVisibility(View.VISIBLE);
                tvReplyName.setText(helper.getUserNameById(message.getFromUuid()));


                ivReplyType.setVisibility(View.VISIBLE);
                if (ChatMessage.FORMAT_TXT.equals(message.getFormat())) {
                    ivReplyType.setVisibility(View.GONE);
                    tvReplyContent.setText(message.getBody());
                } else if (ChatMessage.FORMAT_IMG.equals(message.getFormat())) {
                    ivReplyType.setImageResource(R.drawable.icon_chat_pic);
                    tvReplyContent.setText("??????");
                } else if (ChatMessage.FORMAT_VOICE.equals(message.getFormat())) {
                    ivReplyType.setImageResource(R.drawable.ic_audio);
                    tvReplyContent.setText("??????");
                } else if (ChatMessage.FORMAT_FILE.equals(message.getFormat())) {
                    ivReplyType.setImageResource(R.drawable.icon_chat_file);
                    if (message.getKeyValues() != null) {
                        String fileName = message.getKeyValues().get("fileName");
                        tvReplyContent.setText(fileName);
                    }
                }
            } else {
                llReply.setVisibility(View.GONE);
            }

            if (dataList != null && dataList.size() > 0) {
                if (dataList.indexOf(model) == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                }
                if ((dataList.indexOf(model) - 1) > 0) {
                    ChatMessage lastMessage = dataList.get(dataList.indexOf(model) - 1); //???????????????????????????????????????
                    if (lastMessage != null && !StrUtils.isNullOrEmpty(lastMessage.getSendTime())) {
                        long dastance = (ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime() - ViewHelper.formatStrToDateAndTime(lastMessage.getSendTime()).getTime()) / (1000 * 60);
                        if (dastance >= 5) { //?????????????????????????????????????????????
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                        } else {
                            tvTime.setVisibility(View.GONE);
                        }
                    }
                }
            }


            llReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickReplyMessageListener != null) {
                        clickReplyMessageListener.onClickReplyMessageListener(model.getReplyMsg(),llRoot);
                    }
                }
            });

            ic_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserInfoActivity(model);
                }
            });


            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtils.startSingleImageBrower(mContext,
                            ImageUtils.getDownloadUrlById(model.getBody()));
                }
            });

            if (mItemClickListener != null) {
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(0);
                    }
                });
            }

            tv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    rawX = (int) event.getRawX();
                    rawY = (int) event.getRawY();
                    return false;
                }
            });

            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    rawX = (int) event.getRawX();
                    rawY = (int) event.getRawY();
                    return false;
                }
            });

            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Global.mUser.getUuid().equals(managerId)) {
                        showMenu(position, v, true);
                    } else {
                        showMenu(position, v, false);
                    }
                    return true;
                }
            });

            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Global.mUser.getUuid().equals(managerId)) {
                        showMenu(position, v, true, true);
                    } else {
                        showMenu(position, v, false, true);
                    }
                    return true;
                }
            });
        }
    }

    private void startUserInfoActivity(ChatMessage model) {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        intent.putExtra("userId", model.getFromUuid());
        mContext.startActivity(intent);
    }

    /**
     * ??????????????????????????????????????????
     */
    private class ChatBViewHolder extends BaseAdapter {
        private AvatarImageView ic_user;
        private TextView tvName;
        private TextView tvTime;
        private TextView tv;
        private RoundImageView iv;
        private LinearLayout llRoot;
        private LinearLayout rlTxt;
        private ProgressBar pb;
        private ImageView ivFail;

        //??????????????????
        private LinearLayout llReply;
        private TextView tvReplyName;
        private ImageView ivReplyType;
        private TextView tvReplyContent;

        public ChatBViewHolder(View view) {
            super(view);
            ic_user = itemView.findViewById(R.id.ic_user);
            tv = itemView.findViewById(R.id.tv);
            tvTime = itemView.findViewById(R.id.tv_time_chat);
            iv = itemView.findViewById(R.id.iv_chat_list);
            tvName = itemView.findViewById(R.id.tv_name_chat_activity);
            llRoot = itemView.findViewById(R.id.ll_root);
            rlTxt = itemView.findViewById(R.id.rl_txt);
            pb = itemView.findViewById(R.id.pb);
            ivFail = itemView.findViewById(R.id.iv_fail);

            llReply = itemView.findViewById(R.id.ll_reply);
            tvReplyName = itemView.findViewById(R.id.tv_name_reply);
            ivReplyType = itemView.findViewById(R.id.iv_type_reply);
            tvReplyContent = itemView.findViewById(R.id.tv_content_reply);
        }

        @Override
        void setData(final int position, Object object) {
            super.setData(position, object);
            final ChatMessage model = (ChatMessage) object;
            ImageUtils.displyUserPhotoById(mContext, Global.mUser.getUuid(), ic_user);
            if ("txt".equalsIgnoreCase(model.getFormat())) {
                rlTxt.setVisibility(View.VISIBLE);
                iv.setVisibility(View.GONE);
                tv.setText(model.getBody());
            } else if ("img".equalsIgnoreCase(model.getFormat())) {
                rlTxt.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);
                ImageUtils.displyImageById(model.getBody(), iv);
            }

            if (model.getSendStatus() == MessageSendStatusEnum.?????????.getStatus() && !isSending) {
                long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                        - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime())
                        / (1000 * 60);
                if (dastance > 1) {
                    model.setSendStatus(MessageSendStatusEnum.????????????.getStatus());
                }
            }

            if (model.getSendStatus() == MessageSendStatusEnum.?????????.getStatus()) {
                pb.setVisibility(View.VISIBLE);
                ivFail.setVisibility(View.GONE);
            } else if (model.getSendStatus() == MessageSendStatusEnum.????????????.getStatus()) {
                pb.setVisibility(View.GONE);
                ivFail.setVisibility(View.VISIBLE);
            } else if (model.getSendStatus() == MessageSendStatusEnum.????????????.getStatus()) {
                pb.setVisibility(View.GONE);
                ivFail.setVisibility(View.GONE);
            }


            ChatMessage message = model.getReplyMsg();
            if (message != null) {
                llReply.setVisibility(View.VISIBLE);
                tvReplyName.setText(helper.getUserNameById(message.getFromUuid()));


                ivReplyType.setVisibility(View.VISIBLE);
                if (message.getChatCategory() == ChatMessage.CHAT_Left ||
                        message.getChatCategory() == ChatMessage.CHAT_RIGHT) {
                    if ("img".equals(message.getFormat())) {
                        ivReplyType.setImageResource(R.drawable.icon_chat_pic);
                        tvReplyContent.setText("??????");
                    } else {
                        ivReplyType.setVisibility(View.GONE);
                        tvReplyContent.setText(message.getBody());
                    }
                } else if (message.getChatCategory() == ChatMessage.CHAT_LEFT_AUDIO ||
                        message.getChatCategory() == ChatMessage.CHAT_RIGHT_AUDIO) {
                    ivReplyType.setImageResource(R.drawable.ic_audio);
                    tvReplyContent.setText("??????");
                } else if (message.getChatCategory() == ChatMessage.CHAT_LEFT_FILE ||
                        message.getChatCategory() == ChatMessage.CHAT_RIGHT_FILE) {
                    ivReplyType.setImageResource(R.drawable.icon_chat_file);
                    if (message.getKeyValues() != null) {
                        String fileName = message.getKeyValues().get("fileName");
                        tvReplyContent.setText(fileName);
                    }
                }
            } else {
                llReply.setVisibility(View.GONE);
            }

            if (dataList != null && dataList.size() > 0) {
                if (dataList.indexOf(model) == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                }
                if ((dataList.indexOf(model) - 1) >= 0) {
                    ChatMessage lastMessage = dataList.get(dataList.indexOf(model) - 1); //???????????????????????????????????????
                    if (lastMessage != null && !StrUtils.isNullOrEmpty(lastMessage.getSendTime())) {
                        long dastance = (
                                ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()
                                        - ViewHelper.formatStrToDateAndTime(lastMessage.getSendTime()).getTime())
                                / (1000 * 60);
                        if (dastance >= 5) { //?????????????????????????????????????????????
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                        } else {
                            tvTime.setVisibility(View.GONE);
                        }
                    }
                }
            }

            llReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickReplyMessageListener != null) {
                        clickReplyMessageListener.onClickReplyMessageListener(model.getReplyMsg(),llRoot);
                    }
                }
            });

            ic_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserInfoActivity(model);
                }
            });


            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtils.startSingleImageBrower(mContext,
                            ImageUtils.getDownloadUrlById(model.getBody()));
                }
            });


            //??????????????????
            ClickUtil.clicks(ivFail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSending = true;
                    model.setSendStatus(MessageSendStatusEnum.?????????.getStatus());
                    notifyDataSetChanged();
                    if (ChartIntentUtils.socketService != null) {
                        ChartIntentUtils.socketService.sendMessage(model);
                    }
                }
            });


            if (mItemClickListener != null) {
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(0);
                    }
                });
            }

            llRoot.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    rawX = (int) event.getRawX();
                    rawY = (int) event.getRawY();
                    return false;
                }
            });

            iv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    rawX = (int) event.getRawX();
                    rawY = (int) event.getRawY();
                    return false;
                }
            });

            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Global.mUser.getUuid().equals(managerId)) {
                        showMenu(position, v, true, true);
                    } else {
                        //??????????????????,?????????????????????????????????????????????5?????????????????????
                        long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                                - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()) / (1000 * 60);
                        if (dastance > 5) {
                            showMenu(position, v, false, true);
                        } else {
                            showMenu(position, v, true, true);
                        }
                    }
                    return true;
                }
            });

            llRoot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Global.mUser.getUuid().equals(managerId)) {
                        showMenu(position, v, true);
                    } else {
                        //??????????????????,?????????????????????????????????????????????5?????????????????????
                        long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                                - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()) / (1000 * 60);
                        if (dastance > 5) {
                            showMenu(position, v, false);
                        } else {
                            showMenu(position, v, true);
                        }
                    }
                    return true;
                }
            });
        }
    }

    /**
     * ?????????????????????????????????
     */
    private class ChatRightAudioViewHolder extends BaseAdapter {
        private TextView tvAudioDuration;//????????????
        private TextView tvTime;
        private AvatarImageView imageView;
        private TextView tvName;
        private ImageView ivAudio;
        private RelativeLayout rlAudio;
        private BoeryunDownloadManager downloadManager;

        private ProgressBar pb;
        private ImageView ivFail;


        public ChatRightAudioViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_tv_time);
            tvAudioDuration = itemView.findViewById(R.id.tvDuration);
            imageView = itemView.findViewById(R.id.chat_item_header);
            ivAudio = itemView.findViewById(R.id.ivAudio);
            rlAudio = itemView.findViewById(R.id.rl_txt);
            pb = itemView.findViewById(R.id.pb);
            ivFail = itemView.findViewById(R.id.iv_fail);
            downloadManager = BoeryunDownloadManager.getInstance();
            if (itemView.findViewById(R.id.tv_name_chat_activity) != null) {
                tvName = itemView.findViewById(R.id.tv_name_chat_activity);
            }
        }

        @Override
        void setData(final int position, Object object) {
            super.setData(position, object);
            final ChatMessage model = (ChatMessage) object;
            if (model.getChatCategory() == ChatMessage.CHAT_LEFT_AUDIO) {
                ImageUtils.displyUserPhotoById(mContext, model.getFromUuid(), imageView);
            } else {
                ImageUtils.displyUserPhotoById(mContext, Global.mUser.getUuid(), imageView);
            }
            if (model.getChatCategory() == ChatMessage.CHAT_LEFT_AUDIO) {
                if (model.getKeyValues() != null) {
                    String isSingle = model.getKeyValues().get("isSingle"); //???????????????
                    String audioDuration = model.getKeyValues().get("audioDuration"); //????????????
                    tvAudioDuration.setText(audioDuration);
                    if ("1".equals(isSingle)) { //????????????????????????????????????
                        tvName.setVisibility(View.GONE);
                    } else {
                        tvName.setVisibility(View.VISIBLE);
                        tvName.setText(helper.getUserNameById(model.getFromUuid()));
                    }
                } else {
                    tvName.setVisibility(View.VISIBLE);
                    tvName.setText(helper.getUserNameById(model.getFromUuid()));
                }

            } else {
                tvAudioDuration.setText(model.getAudioDuration() + "\"");
            }


            if (model.getChatCategory() == ChatMessage.CHAT_RIGHT_AUDIO) {
                if (model.getSendStatus() == MessageSendStatusEnum.?????????.getStatus()) {
                    long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                            - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime())
                            / (1000 * 60);
                    if (dastance > 1) {
                        model.setSendStatus(MessageSendStatusEnum.????????????.getStatus());
                    }
                }

                if (model.getSendStatus() == MessageSendStatusEnum.?????????.getStatus()) {
                    pb.setVisibility(View.VISIBLE);
                    ivFail.setVisibility(View.GONE);
                } else if (model.getSendStatus() == MessageSendStatusEnum.????????????.getStatus()) {
                    pb.setVisibility(View.GONE);
                    ivFail.setVisibility(View.VISIBLE);
                } else if (model.getSendStatus() == MessageSendStatusEnum.????????????.getStatus()) {
                    pb.setVisibility(View.GONE);
                    ivFail.setVisibility(View.GONE);
                }
            }

            rlAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(model.getAudioLocalPath())) {
                        downloadAudio();
                    } else {
                        palyAudio();
                    }
                }

                private void palyAudio() {
                    MediaManager.reset();
                    if (model.getChatCategory() == ChatMessage.CHAT_RIGHT_AUDIO) {
                        ivAudio.setBackgroundResource(R.drawable.audio_animation_right_list);
                    } else {
                        ivAudio.setBackgroundResource(R.drawable.audio_animation_left_list);
                    }
                    AnimationDrawable drawable = (AnimationDrawable) ivAudio.getBackground();
                    drawable.start();

                    MediaManager.playSound(mContext, model.getAudioLocalPath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (model.getChatCategory() == ChatMessage.CHAT_RIGHT_AUDIO) {
                                ivAudio.setBackgroundResource(R.drawable.audio_animation_list_right_3);
                            } else {
                                ivAudio.setBackgroundResource(R.drawable.audio_animation_list_left_3);
                            }
                            MediaManager.release();
                        }
                    });
                }


                private void downloadAudio() {
                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.atttachId = model.getBody();
                    downloadFile.attachName = model.getBody() + ".mp3";
                    downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + model.getBody();
                    downloadManager.download(downloadFile);
                    downloadManager.setHandler(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            DownloadFile downloadFile = (DownloadFile) msg.obj;
                            if (downloadFile != null && downloadFile.downloadState
                                    == BoeryunDownloadManager.DOWNLOAD_STATE_FINISH) {
                                String localPath = FilePathConfig.getCacheDirPath() + File.separator
                                        + downloadFile.attachName;
                                model.setAudioLocalPath(localPath);
                                palyAudio();
                            }
                            return true;
                        }
                    }));
                }
            });

            if (dataList != null && dataList.size() > 0) {
                if (dataList.indexOf(model) == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                }
                if ((dataList.indexOf(model) - 1) >= 0) {
                    ChatMessage lastMessage = dataList.get(dataList.indexOf(model) - 1); //???????????????????????????????????????
                    if (lastMessage != null && !StrUtils.isNullOrEmpty(lastMessage.getSendTime())) {
                        long dastance = (ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime() - ViewHelper.formatStrToDateAndTime(lastMessage.getSendTime()).getTime()) / (1000 * 60);
                        if (dastance >= 5) { //?????????????????????????????????????????????
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                        } else {
                            tvTime.setVisibility(View.GONE);
                        }
                    }
                }
            }

            rlAudio.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    rawX = (int) event.getRawX();
                    rawY = (int) event.getRawY();
                    return false;
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserInfoActivity(model);
                }
            });

            rlAudio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Global.mUser.getUuid().equals(managerId)) {
                        showMenu(position, v, true, true);
                    } else {
                        if (model.getChatCategory() == ChatMessage.CHAT_RIGHT_AUDIO) {
                            //??????????????????,?????????????????????????????????????????????5?????????????????????
                            long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                                    - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()) / (1000 * 60);
                            if (dastance > 5) {
                                showMenu(position, v, false, true);
                            } else {
                                showMenu(position, v, true, true);
                            }
                        }
                    }
                    return true;
                }
            });
        }
    }

    /**
     * ???????????????????????????
     */
    private class ChatRightFileViewHolder extends BaseAdapter {

        private TextView tvTime;
        private AvatarImageView imageView;
        private TextView tvFileName; //????????????
        private TextView tvFileSize; //????????????
        private ImageView ivFileType; //????????????
        private ProgressBar bar;
        private RelativeLayout rlRoot;
        private LinearLayout llFile;
        private ProgressBar pb;
        private ImageView ivFail;
        private BoeryunDownloadManager downloadManager;


        public ChatRightFileViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_tv_time);
            imageView = itemView.findViewById(R.id.chat_item_header);
            tvFileName = itemView.findViewById(R.id.msg_tv_file_name);
            ivFileType = itemView.findViewById(R.id.rc_msg_iv_file_type_image);
            tvFileSize = itemView.findViewById(R.id.msg_tv_file_size);
            bar = itemView.findViewById(R.id.progressbar_upload_file);
            rlRoot = itemView.findViewById(R.id.rl_root);
            llFile = itemView.findViewById(R.id.rc_message);
            pb = itemView.findViewById(R.id.pb);
            ivFail = itemView.findViewById(R.id.iv_fail);
            downloadManager = BoeryunDownloadManager.getInstance();
        }

        @Override
        void setData(final int position, Object object) {
            super.setData(position, object);
            final ChatMessage model = (ChatMessage) object;

            if (model.getKeyValues() != null) {
                String isSingle = model.getKeyValues().get("isSingle"); //???????????????
                String fileName = model.getKeyValues().get("fileName"); //????????????
                String fileSize = model.getKeyValues().get("fileSize"); //????????????
                if (!TextUtils.isEmpty(fileName)) {
                    tvFileName.setText(fileName);
                    tvFileSize.setText(fileSize);
                }
            }

            final Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            int progress = (int) msg.obj;
                            bar.setProgress(progress);
                            model.setUploadState(ChatMessage.UPLOAD_STATE_UPLOADING);
                            if (progress == 100) {
                                bar.setVisibility(View.GONE);
                                model.setUploadState(ChatMessage.UPLOAD_STATE_FINISHED);
                            }
                            break;
                        case 2:
                            Attach attach = (Attach) msg.obj;
                            model.setBody(attach.getUuid());
                            if (fileFinshListener != null) {
                                fileFinshListener.onUploadFileFinsh(model);
                            }
                            break;
                    }
                    return true;
                }
            });
            if (dataList != null && dataList.size() > 0) {
                if (dataList.indexOf(model) == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                }
                if ((dataList.indexOf(model) - 1) >= 0) {
                    ChatMessage lastMessage = dataList.get(dataList.indexOf(model) - 1); //???????????????????????????????????????
                    if (lastMessage != null && !StrUtils.isNullOrEmpty(lastMessage.getSendTime())) {
                        long dastance = (ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime() - ViewHelper.formatStrToDateAndTime(lastMessage.getSendTime()).getTime()) / (1000 * 60);
                        if (dastance >= 5) { //?????????????????????????????????????????????
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                        } else {
                            tvTime.setVisibility(View.GONE);
                        }
                    }
                }
            }
            ImageUtils.displyUserPhotoById(mContext, Global.mUser.getUuid(), imageView);


            if (model.getSendStatus() == MessageSendStatusEnum.?????????.getStatus()) {
                long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                        - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime())
                        / (1000 * 60);
                if (dastance > 1) {
                    model.setSendStatus(MessageSendStatusEnum.????????????.getStatus());
                }
            }

            if (model.getSendStatus() == MessageSendStatusEnum.?????????.getStatus()) {
                pb.setVisibility(View.VISIBLE);
                ivFail.setVisibility(View.GONE);
            } else if (model.getSendStatus() == MessageSendStatusEnum.????????????.getStatus()) {
                pb.setVisibility(View.GONE);
                ivFail.setVisibility(View.VISIBLE);
            } else if (model.getSendStatus() == MessageSendStatusEnum.????????????.getStatus()) {
                pb.setVisibility(View.GONE);
                ivFail.setVisibility(View.GONE);
            }

            /**
             * ??????????????????????????????????????????????????????????????????
             */
            if (ChatMessage.UPLOAD_STATE_UPLOADING.equals(model.getUploadState())) {
                bar.setVisibility(View.VISIBLE);
            } else {
                bar.setVisibility(View.GONE);
            }


            downloadManager.setHandler(new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    DownloadFile downloadFile = (DownloadFile) msg.obj;
                    switch (downloadFile.downloadState) {
                        case BoeryunDownloadManager.DOWNLOAD_STATE_DOWNLOADING:
                            bar.setProgress(downloadFile.downloadSize / downloadFile.totalSize * 100);
                            break;
                        case BoeryunDownloadManager.DOWNLOAD_STATE_FINISH:
                            model.setDownloadState(ChatMessage.DOWNLOAD_STATE_FINISHED);
                            String filePath = FilePathConfig.getCacheDirPath() + File.separator
                                    + tvFileName.getText().toString();
                            model.setFileLocalPath(filePath);
                            OpenFilesIntent.open(mContext, filePath);
                            bar.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            }));

            llFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(model.getFileLocalPath())) {
                        OpenFilesIntent.open(mContext, model.getFileLocalPath());
                    } else {
                        String filePath = FilePathConfig.getCacheDirPath() + File.separator
                                + tvFileName.getText().toString();
                        File file = new File(filePath);
                        if (file.exists()) { //????????????????????????????????????????????????????????????????????????????????????
                            OpenFilesIntent.open(mContext, filePath);
                        } else {
                            DownloadFile downloadFile = new DownloadFile();
                            downloadFile.atttachId = model.getBody();
                            downloadFile.attachName = tvFileName.getText().toString();
                            downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + model.getBody();
                            bar.setVisibility(View.GONE);
                            downloadManager.download(downloadFile);
                        }
                    }
                }
            });


            /**
             * ????????????
             */
            if (!TextUtils.isEmpty(model.getFileLocalPath())) {
                final File file = new File(model.getFileLocalPath());
                if (file.exists()) {
                    try {
                        String fileName = file.getName();
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        int resoureIdBySuffix = AttachBiz.getImageResoureIdBySuffix(suffix);
                        ivFileType.setImageResource(resoureIdBySuffix);
                        tvFileName.setText(fileName);
                        tvFileSize.setText(DataCleanManager.getFormatSize(file.length()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    llFile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OpenFilesIntent.open(mContext, model.getFileLocalPath());
                        }
                    });

                    if (ChatMessage.UPLOAD_STATE_UN_UNLOAD.equals(model.getUploadState())) { //????????????????????????????????????????????????
                        bar.setVisibility(View.VISIBLE);
                        model.setUploadState(ChatMessage.UPLOAD_STATE_UPLOADING);
                        final UploadHelperListener listener = new UploadHelperListener();
                        listener.setOnProgressListener(new UploadHelperListener.ProgressListener() {
                            @Override
                            public void onProgressListener(int progress) {
                                Message message = new Message();
                                message.what = 1;
                                message.obj = progress;
                                handler.sendMessage(message);
                            }
                        });
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String methodName = GlobalMethord.????????????;
                                Attach attach = listener.uploadFile(file, methodName, "");
                                Message message = new Message();
                                message.what = 2;
                                message.obj = attach;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }
                }
            }

            llFile.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    rawX = (int) event.getRawX();
                    rawY = (int) event.getRawY();
                    return false;
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserInfoActivity(model);
                }
            });

            llFile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (Global.mUser.getUuid().equals(managerId)) {
                        showMenu(position, v, true, true);
                    } else {
                        if (model.getChatCategory() == ChatMessage.CHAT_RIGHT_FILE) {
                            //??????????????????,?????????????????????????????????????????????5?????????????????????
                            long dastance = (ViewHelper.formatStrToDateAndTime(ViewHelper.getCurrentFullTime()).getTime()
                                    - ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()) / (1000 * 60);
                            if (dastance > 5) {
                                showMenu(position, v, false, true);
                            } else {
                                showMenu(position, v, true, true);
                            }
                        }
                    }
                    return true;
                }
            });
        }
    }


    /**
     * ???????????????????????????
     */
    private class ChatLeftFileViewHolder extends BaseAdapter {
        private TextView tvTime;
        private TextView tvName;
        private AvatarImageView imageView;
        private TextView tvFileName; //????????????
        private TextView tvFileSize; //????????????
        private ImageView ivFileType; //????????????
        private RelativeLayout rlRoot;
        private ProgressBar bar;
        private BoeryunDownloadManager downloadManager;

        public ChatLeftFileViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_tv_time);
            tvName = itemView.findViewById(R.id.tv_name_chat_activity);
            imageView = itemView.findViewById(R.id.chat_item_header);
            tvFileName = itemView.findViewById(R.id.msg_tv_file_name);
            ivFileType = itemView.findViewById(R.id.rc_msg_iv_file_type_image);
            tvFileSize = itemView.findViewById(R.id.msg_tv_file_size);
            bar = itemView.findViewById(R.id.progressbar_upload_file);
            rlRoot = itemView.findViewById(R.id.rl_root);
            downloadManager = BoeryunDownloadManager.getInstance();
        }

        @Override
        void setData(int position, Object object) {
            super.setData(position, object);
            final ChatMessage model = (ChatMessage) object;
//            getAttachMent(model.getBody());//?????????????????????????????????
            ImageUtils.displyUserPhotoById(mContext, model.getFromUuid(), imageView);
            if (model.getKeyValues() != null) {
                String isSingle = model.getKeyValues().get("isSingle"); //???????????????
                String fileName = model.getKeyValues().get("fileName"); //????????????
                String fileSize = model.getKeyValues().get("fileSize"); //????????????
                tvFileName.setText(fileName);
                tvFileSize.setText(fileSize);
                if ("1".equals(isSingle)) { //????????????????????????????????????
                    tvName.setVisibility(View.GONE);
                } else {
                    tvName.setVisibility(View.VISIBLE);
                    tvName.setText(helper.getUserNameById(model.getFromUuid()));
                }
            } else {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(helper.getUserNameById(model.getFromUuid()));
            }
            downloadManager.setHandler(new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    DownloadFile downloadFile = (DownloadFile) msg.obj;
                    switch (downloadFile.downloadState) {
                        case BoeryunDownloadManager.DOWNLOAD_STATE_DOWNLOADING:
                            bar.setProgress(downloadFile.downloadSize / downloadFile.totalSize * 100);
                            break;
                        case BoeryunDownloadManager.DOWNLOAD_STATE_FINISH:
                            model.setDownloadState(ChatMessage.DOWNLOAD_STATE_FINISHED);
                            String filePath = FilePathConfig.getCacheDirPath() + File.separator
                                    + tvFileName.getText().toString();
                            model.setFileLocalPath(filePath);
                            OpenFilesIntent.open(mContext, filePath);
                            bar.setVisibility(View.GONE);
                            break;
                    }
                    return true;
                }
            }));

            rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filePath = FilePathConfig.getCacheDirPath() + File.separator
                            + tvFileName.getText().toString();
                    File file = new File(filePath);
                    if (file.exists()) { //????????????????????????????????????????????????????????????????????????????????????
                        OpenFilesIntent.open(mContext, filePath);
                    } else {
                        DownloadFile downloadFile = new DownloadFile();
                        downloadFile.atttachId = model.getBody();
                        downloadFile.attachName = tvFileName.getText().toString();
                        downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + model.getBody();
                        bar.setVisibility(View.GONE);
                        downloadManager.download(downloadFile);
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startUserInfoActivity(model);
                }
            });

        }


        /**
         * ??????????????????
         *
         * @param attachIds
         */
        private void getAttachMent(String attachIds) {
            JSONObject jo = new JSONObject();
            try {
                jo.put("attachIds", attachIds);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;
            StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    List<Attach> attaches = JsonUtils.ConvertJsonToList(response, Attach.class);
                    if (attaches != null) {
                        Attach attach = attaches.get(0);
                        if (attach != null) {
                            tvFileName.setText(attach.filename);
                            tvFileSize.setText(DataCleanManager.getFormatSize(attach.totalSize));
                            String suffix = attach.filename.substring(attach.filename.lastIndexOf(".") + 1);
                            int resoureIdBySuffix = AttachBiz.getImageResoureIdBySuffix(suffix);
                            ivFileType.setImageResource(resoureIdBySuffix);
                        }
                    }
                }

                @Override
                public void onFailure(Request request, Exception ex) {

                }

                @Override
                public void onResponseCodeErro(String result) {

                }
            });
        }
    }

    private class chatTipViewHolder extends BaseAdapter {
        private TextView tvTip;
        private TextView tvTime;

        public chatTipViewHolder(View itemView) {
            super(itemView);
            tvTip = itemView.findViewById(R.id.tv_tip);
            tvTime = itemView.findViewById(R.id.item_tv_time);
        }

        @Override
        void setData(int position, Object object) {
            super.setData(position, object);
            final ChatMessage model = (ChatMessage) object;
            String body = model.getBody();
            if (!TextUtils.isEmpty(body) && body.contains(Global.mUser.getName())) {
                body = body.replace(Global.mUser.getName(), "???");
            }
            tvTip.setText(body);

            if (dataList != null && dataList.size() > 0) {
                if (dataList.indexOf(model) == 0) {
                    tvTime.setVisibility(View.VISIBLE);
                    tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                }
                if ((dataList.indexOf(model) - 1) >= 0) {
                    ChatMessage lastMessage = dataList.get(dataList.indexOf(model) - 1); //???????????????????????????????????????
                    if (lastMessage != null && !StrUtils.isNullOrEmpty(lastMessage.getSendTime())) {
                        long dastance = (ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime() - ViewHelper.formatStrToDateAndTime(lastMessage.getSendTime()).getTime()) / (1000 * 60);
                        if (dastance >= 5) { //?????????????????????????????????????????????
                            tvTime.setVisibility(View.VISIBLE);
                            tvTime.setText(DateDeserializer.getTime(ViewHelper.formatStrToDateAndTime(model.getSendTime()).getTime()));
                        } else {
                            tvTime.setVisibility(View.GONE);
                        }
                    }
                }
            }

        }
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View v, Object object);
    }

    public void setOnItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

    public interface DeleteItemListener {
        void onDeleteItem(ChatMessage message);
    }

    public void setOnDeleteItemListener(DeleteItemListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public interface UploadFileFinshListener {
        void onUploadFileFinsh(ChatMessage message);
    }

    public void setOnUploadFileFinshListener(UploadFileFinshListener listener) {
        fileFinshListener = listener;
    }

    public interface ReplyMessageListener {
        void onReplyMessageListener(ChatMessage message);
    }

    public void setOnReplyMessageListener(ReplyMessageListener listener) {
        replyMessageListener = listener;
    }

    /**
     * ???????????????????????????
     */
    public interface ClickReplyMessageListener {
        void onClickReplyMessageListener(ChatMessage message,View view);
    }

    public void setOnClickReplyMessageListener(ClickReplyMessageListener listener) {
        clickReplyMessageListener = listener;
    }


    /**
     * ??????????????????
     *
     * @param view
     */
    private void showMenu(int itemPosition, View view, boolean isCanCancle) {
        showMenu(itemPosition, view, isCanCancle, false);
    }

    private void showMenu(final int itemPosition, View view, boolean isCanCancle, boolean isOnlyCanDelete) {
        final String[] showItems;
        if (isOnlyCanDelete) {
            if (isCanCancle) {
                showItems = new String[]{"??????", "??????", "??????", "??????"};
            } else {
                showItems = new String[]{"??????", "??????", "??????"};
            }
        } else {
            if (isCanCancle) {
                showItems = new String[]{"??????", "??????", "??????", "??????", "??????", "?????????"};
            } else {
                showItems = new String[]{"??????", "??????", "??????", "??????", "?????????"};
            }
        }
        QPopuWindow.getInstance(mContext).builder
                .bindView(view, 0)
                .setPopupItemList(showItems)
                .setPointers(rawX, rawY)
                .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                    /**
                     * @param anchorView ???pop?????????view
                     * @param anchorViewPosition  pop??????view???ListView???position
                     * @param position  pop??????item???position ????????????????????????0
                     */
                    @Override
                    public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                        String type = showItems[position];
                        ChatMessage chatMessage = dataList.get(itemPosition);
                        if ("??????".equals(type)) {
                            replyMessage(chatMessage);
                        } else if ("??????".equals(type)) {
                            onClickCopy(chatMessage.getBody());
                        } else if ("??????".equals(type)) {
                            reCallMessage(chatMessage);
                        } else if ("??????".equals(type)) {
                            deleteMessage(chatMessage);
                        } else if ("?????????".equals(type)) {
                            showSelectExecutor(chatMessage.getBody());
                        } else if ("??????".equals(type)) {
                            forwardMessage(chatMessage);
                        }
                    }
                }).show();

    }

    /**
     * ???????????????????????????????????????
     *
     * @param content
     */
    private void showSelectExecutor(final String content) {
        Display display;
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_chat_turntask_view, null);
        final Dialog alertDialog = new Dialog(mContext, R.style.AlertDialogStyle);
        alertDialog.setContentView(view);
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // ??????dialog????????????
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        staffView = view.findViewById(R.id.staff_view);
        staffView.setOnSelectedUserListener(new HorizontalRecentlySelectedStaffView.OnSelectedUserListener() {
            @Override
            public void onSelected(User user) {
                CommandManager.newTask(user.getUuid(), "", content, mContext);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * ?????????????????????
     */
    public void receiveSelectedUser(User user) {
        if (staffView != null) {
            staffView.reloadStaffList(user);
        }
    }

    /**
     * ??????????????????
     *
     * @param text
     */
    private void onClickCopy(String text) {
        // ???API11??????android????????????android.content.ClipboardManager
        // ????????????????????????????????????????????????android.text.ClipboardManager???????????????deprecated????????????????????????
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // ??????????????????????????????????????????
        cm.setText(text);
        Toast.makeText(mContext, "????????????", Toast.LENGTH_LONG).show();
    }


    /**
     * ????????????
     *
     * @param message ??????????????????
     */
    private void reCallMessage(final ChatMessage message) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;

        Map<String, String> map = new HashMap<>();
        map.put("messageId", message.getId());
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                dataList.remove(message);
                notifyDataSetChanged();
                if (deleteItemListener != null) {
                    deleteItemListener.onDeleteItem(message);
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * ????????????
     *
     * @param message ??????????????????
     */
    private void deleteMessage(final ChatMessage message) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;

        Map<String, String> map = new HashMap<>();
        map.put("messageId", message.getId());
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                dataList.remove(message);
                notifyDataSetChanged();
                if (deleteItemListener != null) {
                    deleteItemListener.onDeleteItem(message);
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ???????????????????????? ??????????????????
     *
     * @param message
     */
    private void forwardMessage(ChatMessage message) {
        Intent intent = new Intent(mContext, ForwardMessageActivity.class);
        String msgId = message.getId();
        message.setMessageId(UUID.randomUUID().toString().replaceAll("-", ""));
        intent.putExtra("ChatMessage", message);
        mContext.startActivity(intent);
    }


    /**
     * ????????????
     *
     * @param message ??????????????????
     */
    private void replyMessage(ChatMessage message) {
        if (replyMessageListener != null) {
            replyMessageListener.onReplyMessageListener(message);
        }
    }
}



