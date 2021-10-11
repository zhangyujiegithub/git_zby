package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.biaozhunyuan.tianyi.chatLibary.chat.group.GroupInfoActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.group.SelectGroupMembersActivity;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.CommandModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupMembers;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupModel;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.MessageSendStatusEnum;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.RecentMessage;
import com.biaozhunyuan.tianyi.common.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DataCleanManager;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.KeyboardChangeListener;
import com.biaozhunyuan.tianyi.common.helper.MediaManager;
import com.biaozhunyuan.tianyi.common.helper.SelectPhotoBiz;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Constant;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.InvokeUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.PublishContentTextClickSpan;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.HorizontalRecentlySelectedStaffView;
import com.biaozhunyuan.tianyi.common.view.RecordButton;
import com.example.chat.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.base.BoeryunApp.getInstance;
import static com.biaozhunyuan.tianyi.common.helper.ORMDataHelper.getInstance;
import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;


/**
 * Created by wangAnMin on 2018/4/19.
 * 聊天主界面
 */

public class ChatActivity extends AppCompatActivity {

    private final int REQUEST_CODE_UPDATE_NAME = 10;
    private final int REQUEST_CODE_SELECT_AT = 11; //选择要@的员工
    private Activity mContext;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private EditText et;
    private RelativeLayout bgView;
    private TextView tvSend;
    private String content;
    private TextView tvDepartment;
    private User currentUser;

    private ImageView ivBack;
    private TextView tvName;
    private TextView tvAt; //有人@的按钮，点击可以定位到@你的那条消息
    private TextView tvQuit; //被移除群聊
    private ImageView ivGroupInfo;//群聊详情
    private ImageView ivAddAttach;//选择图片
    private ImageView ivAudio;//语音按钮
    private ImageView ivNoDisturb;//消息免打扰
    private RecordButton btnAudio;//语音按钮
    private int audioDuration;//录音时长
    private String audioLocalPath;//录音本地地址
    private String fileLocalPath;//文件本地地址


    private LinearLayout llBottom;//底部功能区域
    private LinearLayout llChat;//底部功能区域
    private RelativeLayout llXiangce;//打开相册
    private RelativeLayout llTakePhoto;//照相
    private RelativeLayout rlSelectFile;//选择文件
//    private ListView lvCommand;//命令行列表
//    private CardView cardView;

    private LinearLayout llReply; //回复信息区域
    private TextView tvReplyName; //需要回复的信息的发送人的名称
    private ImageView ivReplyType; //需要回复的信息的类型
    private TextView tvReplyContent; //需要回复的信息的内容
    private ImageView ivReplyCancel; //取消回复

    private BoeryunApp application;
    private GroupSession groupSession;
    private boolean isShowBottomArea = false; //是否显示底部区域,默认不显示
    private boolean isShowRecordBtn = false; //是否显示录音按钮,默认不显示
    private String chatId = "";
    private SharedPreferencesHelper preferencesHelper;
    private DictionaryHelper helper;
    private DictIosPickerBottomDialog dialog;
    private String talkers = "";
    private String talksUuid = "";
    private List<CommandModel> commandList;
    private InputMethodManager imm;
    private RecentMessage recentMessage;
    private ChatMessage replyMessage; //被回复的消息
    private List<ChatMessage> dataList;
    private HashMap<String, RecentMessage> personalChatMessage;
    private Dialog executorDialog;
    private InputMethodManager inputMethodManager;
    private HorizontalRecentlySelectedStaffView staffView;
    private String userIds;
    private List<User> allAtUsers = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String attach = (String) msg.obj;
                    ChatActivity.this.sendMessage("img", attach);
                    break;
                case 2:
                    String attach1 = (String) msg.obj;
                    Map<String, String> map = new HashMap<>();
                    map.put("audioDuration", audioDuration + "");
                    ChatActivity.this.sendMessage(ChatMessage.FORMAT_VOICE, map, attach1, true);
                    break;
            }
        }
    };
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);
        setStatusTextColor(true, this);
        mContext = ChatActivity.this;
        application = getInstance();
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        preferencesHelper = new SharedPreferencesHelper(ChatActivity.this);
        helper = new DictionaryHelper(ChatActivity.this);
        dialog = new DictIosPickerBottomDialog(ChatActivity.this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        commandList = CommandManager.getCommandList();
        initViews();
        initData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(final String status) {
        if ("502".equals(status)) {
            final AlertDialog dialog = new AlertDialog(this).builder();
            dialog.setCancelable(false)
                    .setMsg("您的账号在另外一台设备登录!")
                    .setTitle("提示")
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dissMiss();
                            EventBus.getDefault().removeStickyEvent(status); //移除事件
                            if (EventBus.getDefault().isRegistered(ChatActivity.this)) {
                                EventBus.getDefault().unregister(ChatActivity.this);
                            }
                            application.removeALLActivity();
                            getInsance().saveValueBYkey("isExit", true);
                            clearToken();
                            getInstance(getBaseContext()).deleteOldDb();
                            XGPushManager.unregisterPush(getBaseContext());

                            ComponentName comp = new ComponentName(getBaseContext(), "com.biaozhunyuan.tianyi.login.NavActivity");
                            Intent intent = new Intent();
                            intent.setComponent(comp);
                            intent.setAction("android.intent.action.VIEW");
                            startActivity(intent);
                            finish();
                        }
                    }).show();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onReceiveUnReadMsgEvent(String status) {
        if ("505".equals(status)) {
            if (adapter != null) {
                refreshData();
            }
        }
        if ("移除群成员成功".equals(status) || "添加群成员成功".equals(status)) {
            getGroupInfo();
        }
        if ("退出群聊成功".equals(status)) {
            finish();
            EventBus.getDefault().removeStickyEvent(status);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(ChatMessage message) {
        if (message != null) {
            //如果是回执消息,更新消息状态
            if ("sak".equalsIgnoreCase(message.getCmd())) {
                adapter.notifyItemStatus(message.getBody());
            } else {
                boolean isSaveMsg = true;
                if (ChatMessage.FORMAT_VOICE.equals(message.getFormat())) {
                    message.setChatCategory(ChatMessage.CHAT_LEFT_AUDIO);
                } else if (ChatMessage.FORMAT_FILE.equals(message.getFormat())) {
                    message.setChatCategory(ChatMessage.CHAT_LEFT_FILE);
                } else if (ChatMessage.FORMAT_TIP.equals(message.getFormat())) {
                    //修改群名称
                    if (!TextUtils.isEmpty(message.getBody())
                            && message.getBody().contains("修改")) {
                        String body = message.getBody();
                        String groupName = body.substring(body.indexOf("\"") + 1, body.lastIndexOf("\""));
                        groupSession.setName(groupName);
                        tvName.setText(groupName);
                    }

                    //撤回消息
                    if (!TextUtils.isEmpty(message.getBody())
                            && message.getBody().contains("撤回")) {
                        HashMap<String, RecentMessage> msgs = preferencesHelper.getHashMapData
                                (Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
                        RecentMessage message1 = msgs.get(message.getChatId());
                        for (ChatMessage msg : message1.getRecentMessages()) {
                            if (msg.getId().equals(message.getMessageId())) {
                                msg.setCmd(ChatMessage.FORMAT_TIP);
                                msg.setChatCategory(ChatMessage.CHAT_TIP);
                                msg.setBody(message.getBody());
                                isSaveMsg = false;
                                break;
                            }
                        }
                        preferencesHelper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", msgs);
                        if (groupSession.getChatId().equals(message.getChatId())) {
                            refreshData();
                        }
                    }
                } else {
                    message.setChatCategory(ChatMessage.CHAT_Left);
                }
                if (!TextUtils.isEmpty(message.getBody())) {
                    message.setBody(message.getBody().replaceAll("\n", ""));
                }
                if (!TextUtils.isEmpty(message.getKeyValues().get("@"))) {
                    if (groupSession.getChatId().equals(message.getChatId())) {
                        //如果是当前会话，并且有人@你不用显示
                        HashMap<String, String> keyValues = message.getKeyValues();
                        keyValues.remove("@");
                        message.setKeyValues(keyValues);
                    }

                }
                //不是单聊
                if (message.getKeyValues() != null && !"1".equals(message.getKeyValues().get("isSingle"))) {
                    if (!TextUtils.isEmpty(message.getGroupIcon())) {
                        message.setAvatar(message.getGroupIcon());
                    }
                }
                if (message.getChatId().equals(chatId) && isSaveMsg) {
                    adapter.addAll(message);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到最后一个条目
                }

                if (isSaveMsg) {
                    MsgCacheManager.saveMessage(mContext, message, !groupSession.isSetNoInterrupt());
                }
                MsgCacheManager.setMessageRead(message.getId(), chatId);
                SessionCacheManger.saveSession(mContext, chatId, message); //保存会话
            }
        }
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        et = findViewById(R.id.et);
        bgView = (RelativeLayout) findViewById(R.id.view_bg);
        tvSend = (TextView) findViewById(R.id.tvSend);
        tvDepartment = (TextView) findViewById(R.id.tv_tag);
        ivBack = findViewById(R.id.iv_back_chat);
        ivGroupInfo = findViewById(R.id.iv_group_info);
        tvName = findViewById(R.id.tv_name_chat);
        tvAt = findViewById(R.id.tv_at);
        tvQuit = findViewById(R.id.tv_quit);
        ivAddAttach = findViewById(R.id.iv_add_image);
        ivAudio = findViewById(R.id.ivAudio);
        ivNoDisturb = findViewById(R.id.iv_no_disturb);
        btnAudio = findViewById(R.id.btnAudio);
        llBottom = findViewById(R.id.ll_bottom_chat_activity);
        llChat = findViewById(R.id.ll_bottom);
        llXiangce = findViewById(R.id.ll_xiangce_chat_activity);
        llTakePhoto = findViewById(R.id.ll_tack_photo_chat_activity);
        rlSelectFile = findViewById(R.id.ll_select_file);

        llReply = findViewById(R.id.ll_reply);
        tvReplyName = findViewById(R.id.tv_name_reply);
        tvReplyContent = findViewById(R.id.tv_content_reply);
        ivReplyType = findViewById(R.id.iv_type_reply);
        ivReplyCancel = findViewById(R.id.iv_cancel_reply);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //设置EditText的显示方式为多行文本输入
        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //改变默认的单行模式
        et.setSingleLine(false);
        //水平滚动设置为False
        et.setHorizontallyScrolling(false);
    }

    private void initData() {
        if (getIntent().getExtras() != null) {
            groupSession = (GroupSession) getIntent().getSerializableExtra("chatUser");
            userIds = (String) getIntent().getSerializableExtra("userIds");
            boolean isPush = getIntent().getBooleanExtra("isPush", false);
            chatId = groupSession.getChatId();

            if (groupSession.isSingle() == 0) {
                getGroupMembers();
            }

            if (groupSession.isSetNoInterrupt()) {
                ivNoDisturb.setVisibility(View.VISIBLE);
            }
            if (groupSession.isDepartMent() == 1) {
                tvDepartment.setVisibility(View.VISIBLE);
            }
            if (groupSession.getAtType() != 0) {
                tvAt.setVisibility(View.VISIBLE);
                if (groupSession.getAtType() == 1) {
                    tvAt.setText("有人@你");
                } else if (groupSession.getAtType() == 2) {
                    tvAt.setText("有人@所有人");
                }
            }
            if (isPush) {
                MsgCacheManager.getUnreadMessage(mContext, new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1: //获取未读消息成功，刷新未读数量
                                /**
                                 * 获取聊天列表
                                 */
                                List<GroupSession> sessions = preferencesHelper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
                                for (GroupSession session : sessions) {
                                    if (chatId.equals(session.getChatId())) {
                                        groupSession.setName(session.getName());
                                        break;
                                    }
                                }
                                showMessage(groupSession);
                                break;
                        }
                        return true;
                    }
                }));
            } else {
                showMessage(groupSession);
            }
            if (groupSession.isSingle() == 0) {
                getGroupInfo();
            }
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputSoftHelper.hiddenSoftInput(ChatActivity.this, et);
                finish();
            }
        });

        tvAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ChatMessage message : recentMessage.getRecentMessages()) {
                    if (groupSession.getAtMessageId().equals(message.getId())) {
                        int index = recentMessage.getRecentMessages().indexOf(message);
                        moveToPosition(index);
                        tvAt.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < -50) {
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }
        });


        /**
         * 跳转群组详情页面
         */
        ivGroupInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupInfoActivity.class);
                intent.putExtra("GroupSession", groupSession);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_NAME);
            }
        });

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    isCommandOrSendMessage();
                }
                return false;
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
                //向前删除一个字符，@后的内容必须大于一个字符，可以在后面加一个空格
                if (lengthBefore == 1 && lengthAfter == 0) {
                    PublishContentTextClickSpan[] spans = et.getText().getSpans(0, et.getText().length(), PublishContentTextClickSpan.class);
                    for (PublishContentTextClickSpan publishContentTextSpan : spans) {
                        if (et.getText().getSpanEnd(publishContentTextSpan) == start && !text.toString().endsWith(publishContentTextSpan.getShowText())) {
                            et.getText().delete(et.getText().getSpanStart(publishContentTextSpan), et.getText().getSpanEnd(publishContentTextSpan));
                            User user = (User) publishContentTextSpan.getTag();
                            allAtUsers.remove(user);
                            break;
                        }
                    }
                }
                if (lengthAfter == 1) {
                    CharSequence changeStr = text.subSequence(text.length() - 1, text.length());
                    if ("@".equals(changeStr.toString()) && groupSession.isSingle() != 1) {
                        Intent intent = new Intent(mContext, SelectGroupMembersActivity.class);
                        intent.putExtra("GroupSession", groupSession);
                        startActivityForResult(intent, REQUEST_CODE_SELECT_AT);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                content = s.toString();
                if (TextUtils.isEmpty(s.toString().trim())) {
                    tvSend.setTextColor(getResources().getColor(R.color.text_info));
                    tvSend.setBackgroundResource(R.drawable.shape_bg_type);
                    tvSend.setClickable(false);
                } else {
                    tvSend.setTextColor(getResources().getColor(R.color.white));
                    tvSend.setBackgroundResource(R.drawable.shape_circle_5_send);
                    tvSend.setClickable(true);
                }
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCommandOrSendMessage();
            }
        });


        /**
         * 切换键盘和语音的形态
         */
        ivAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowRecordBtn) {
                    ivAudio.setImageResource(R.drawable.ic_audio);
                    et.setVisibility(View.VISIBLE);
                    btnAudio.setVisibility(View.GONE);
                    et.requestFocus();
                    showSoftInput();
                } else {
                    ivAudio.setImageResource(R.drawable.ic_keyboard);
                    et.setVisibility(View.GONE);
                    btnAudio.setVisibility(View.VISIBLE);
                    et.clearFocus();
                    hideKeyBorad(et);
                }
                isShowRecordBtn = !isShowRecordBtn;
            }
        });


        /**
         * 录音结束
         */
        btnAudio.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String audioPath, int time) {
                if (1 > time) {
                    Toast.makeText(mContext, "语音时间太短！", Toast.LENGTH_SHORT).show();
                    return;
                }
                audioDuration = time;
                audioLocalPath = audioPath;
                List<String> pathList = new ArrayList<>();
                pathList.add(audioPath);
                uploadFile(pathList, ChatMessage.FORMAT_VOICE);
            }
        });


        //选择图片
        ivAddAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.show(new String[]{"拍照", "从相册中选择"});
                if (isShowBottomArea) {
                    ivAddAttach.setImageResource(R.drawable.icon_add);
                    InputSoftHelper.hideShowSoft(mContext);
                    llBottom.setVisibility(View.GONE);
                    isShowBottomArea = false;
                } else {
                    ivAddAttach.setImageResource(R.drawable.chat_bottom_keyboard_nor);
//                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                    InputSoftHelper.hideKeyboard(et);
                    llBottom.setVisibility(View.VISIBLE);
                    isShowBottomArea = true;
                    bgView.setVisibility(View.VISIBLE);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到底部
                }
            }
        });

        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                if (index == 0) { //拍照
                    SelectPhotoBiz.doTakePhoto(mContext);// 用户点击了从照相机获取
                } else if (index == 1) { //选择图片
                    SelectPhotoBiz.selectPhoto(mContext, 1); //选择最大数量设置为1
                }
            }
        });

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && isShowBottomArea) {
                    llBottom.setVisibility(View.GONE);
                    isShowBottomArea = false;
                }
            }
        });

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowBottomArea) {
                    llBottom.setVisibility(View.GONE);
                    isShowBottomArea = false;
                }
            }
        });

        llXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoBiz.selectPhoto(mContext, 1); //选择最大数量设置为1
            }
        });

        llTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoBiz.doTakePhoto(mContext);// 用户点击了从照相机获取
            }
        });


        /**
         * 选择文件
         */
        rlSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(ChatActivity.this)
                        .withRequestCode(Constant.REQUESTCODE_FROM_ACTIVITY)
                        .withMutilyMode(false)
                        .withTitle("选择文件")
                        .start();
            }
        });

        new KeyboardChangeListener(this).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
            @Override
            public void onKeyboardChange(boolean isShow, int keyboardHeight) {
                if (isShow) {
                    bgView.setVisibility(View.VISIBLE);
                    if (adapter.getItemCount() > 0) {
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到底部
                    }
                } else {
                    bgView.setVisibility(View.GONE);
                }
            }
        });

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgView.setVisibility(View.GONE);
                if (llBottom.getVisibility() == View.VISIBLE) {
                    llBottom.setVisibility(View.GONE);
                }
                hideKeyBorad(et);
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bgView.getVisibility() == View.VISIBLE) {
                    bgView.setVisibility(View.GONE);
                }
            }
        });

        ivReplyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyMessage = null;
                llReply.setVisibility(View.GONE);
                et.setHint("");
            }
        });
    }


    /**
     * 是命令行还是发送消息
     */
    private void isCommandOrSendMessage() {
        /**
         * 创建一条消息，如果chatid为空，就重新生成一条,不为空就用返回来的
         */
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (content.startsWith("/rw @") && content.contains(",")) { //新建任务
            String str = content.substring(content.indexOf("@") + 1, content.indexOf(","));
            if (currentUser.getName().equals(str)) {
                String taskContent;
                taskContent = content.substring(content.indexOf(",") + 1, content.length());
                if (TextUtils.isEmpty(taskContent)) {
                    Toast.makeText(mContext, "请输入任务内容", Toast.LENGTH_SHORT).show();
                } else { //新建一条执行人为该对话人员的任务
                    CommandManager.newTask(currentUser.getUuid(), "", taskContent, mContext);
                }
            } else {
                sendMessage("txt", content);
            }
        } else if (content.startsWith("/rz ")) {
            String logContent;
            logContent = content.substring(content.indexOf(" "), content.length());
            if (TextUtils.isEmpty(logContent)) {
                Toast.makeText(mContext, "请输入日志内容", Toast.LENGTH_SHORT).show();
            } else { //新建一条执行人为该对话人员的任务
                CommandManager.saveLog(logContent, mContext);
            }
        } else {
            sendMessage("txt", content);
        }
        et.setText("");
    }

    public void selectAtUser() {
        Intent intent = new Intent(mContext, SelectGroupMembersActivity.class);
        intent.putExtra("GroupSession", groupSession);
        startActivityForResult(intent, REQUEST_CODE_SELECT_AT);
    }


    /**
     * 如果是单人聊天 获取对方的uuid
     */
    private String getChatUserUuid() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setFrom(groupSession.getFrom());
        return chatMessage.getFromUuid();
    }

    /**
     * @param user
     * @执行人
     */
    private void setSelectedExecutor(User user) {
        Editable text = et.getText();
        String atStr = "@" + user.getName() + " ";
        int start = text.length();
        text.append(atStr);
        SpannableString ss = new SpannableString(text);

        int end = text.length();
        //设置从@到,的字符颜色
        PublishContentTextClickSpan span = new PublishContentTextClickSpan(mContext, atStr, et);
        span.setTag(user);
        allAtUsers.add(user);
        ss.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et.setMovementMethod(LinkMovementMethod.getInstance());
        et.setText(ss);

        InputSoftHelper.openKeyBoard(et);
    }

    /**
     * 发送消息
     *
     * @param chatType 消息类型
     * @param content  消息内容
     */
    private void sendMessage(String chatType, String content) {
        sendMessage(chatType, null, content, true);
    }

    /**
     * 发送消息
     *
     * @param chatType      消息类型
     * @param map1
     * @param content       消息内容
     * @param isSaveMessage 是否保存消息
     */
    private void sendMessage(String chatType, Map<String, String> map1,
                             String content, boolean isSaveMessage) {
        ChatMessage message = new ChatMessage();
        message.setBody(content);
        chatId = TextUtils.isEmpty(chatId) ? UUID.randomUUID().toString().replaceAll("-", "") : chatId;
        message.setChatId(chatId);
        message.setSendStatus(MessageSendStatusEnum.发送中.getStatus());
        HashMap<String, String> map = new HashMap<>();
        map.put("name", groupSession.getName());
        map.put("isSingle", groupSession.isSingle() + "");
        map.put("members", groupSession.getMembers());
        map.put("isDepartment", groupSession.isDepartMent() + "");
        if (allAtUsers.size() > 0) {
            String atIds = "";
            for (User allAtUser : allAtUsers) {
                if ("所有人".equals(allAtUser.getName())) {
                    map.put("@", "all");
                    break;
                } else {
                    atIds += allAtUser.getUuid() + ",";
                }
            }

            if (map.get("@") == null) {
                if (atIds.length() > 0) {
                    atIds.substring(0, atIds.length() - 1);
                }
                map.put("@", atIds);
            }
        }
        if (map1 != null) {
            map.putAll(map1);
        }
        if (TextUtils.isEmpty(groupSession.getLastUpdateTime())) {
            message.setGroupUpdateTime(ViewHelper.getCurrentFullTime());
        } else {
            message.setGroupUpdateTime(groupSession.getLastUpdateTime());
        }
        message.setKeyValues(map);
        message.setFrom(Global.mUser.getUuid() + "@" + Global.mUser.getCorpId() + "/");
//                    message.setTalkers(talkers);
        message.setCmd("cht");
        message.setFormat(chatType);
        message.setSendTime(ViewHelper.getDateString());
        message.setName(groupSession.getName());
        message.setAvatar(groupSession.getAvatar());
        if (ChatMessage.FORMAT_VOICE.equals(chatType)) {
            message.setChatCategory(ChatMessage.CHAT_RIGHT_AUDIO);
            message.setAudioDuration(audioDuration);
            message.setAudioLocalPath(audioLocalPath);
        } else if (ChatMessage.FORMAT_FILE.equals(chatType)) {
            message.setChatCategory(ChatMessage.CHAT_RIGHT_FILE);
            message.setFileLocalPath(fileLocalPath);
        } else {
            message.setChatCategory(ChatMessage.CHAT_RIGHT);
        }
        if ("visitor".equalsIgnoreCase(groupSession.getChatType())) {
            message.setChatType("visitor");
            message.setFrom(groupSession.getFrom());
        } else {
            message.setChatType("staff");
        }

        if (replyMessage != null) {
            message.setReplyMsg(replyMessage);
            llReply.setVisibility(View.GONE);
            et.setHint("");
        }

        adapter.addAll(message);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到最后一个条目

        if (isSaveMessage) {
            saveMessage(message);
        }


        if (replyMessage != null) {
            replyMessage = null;
        }
        reSetData();
    }


    /**
     * 保存消息和保存会话
     *
     * @param message
     */
    private void saveMessage(ChatMessage message) {
        if (ChartIntentUtils.socketService != null) {
            ChartIntentUtils.socketService.sendMessage(message);
        }
        ChatMessage msg = new ChatMessage();
        try {
            InvokeUtils.reflectionAttr(message, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg.setId(message.getId());
        msg.setAvatar(message.getAvatar());
        if (msg.getKeyValues().get("@") != null) {
            msg.getKeyValues().remove("@");
        }
        allAtUsers.clear();
        MsgCacheManager.saveMessage(mContext, msg); //保存消息
        SessionCacheManger.saveSession(mContext, chatId, msg); //保存会话
    }

    private void refreshData() {
        reSetData();
//        adapter = new ChatAdapter(ChatActivity.this, recentMessage.getRecentMessages());
        if (recentMessage == null) {
            recentMessage = new RecentMessage();
        }
        adapter.addAll(recentMessage.getRecentMessages(), true);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到底部
    }

    private void reSetData() {
        personalChatMessage = preferencesHelper.getHashMapData(Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
        recentMessage = personalChatMessage.get(chatId);
    }


    /**
     * 显示消息
     *
     * @param chat
     */
    private void showMessage(GroupSession chat) {
        personalChatMessage = preferencesHelper.getHashMapData(Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
        if (chat != null) {
            tvName.setText(chat.getName());
            if (chat.isSingle() == 0) {
                ivGroupInfo.setVisibility(View.VISIBLE);
            }
//            talkers = chat.getTalkers();
//            talksUuid = chat.getStaffIds();

            recentMessage = personalChatMessage.get(chatId);
            if (recentMessage != null) {
                if (recentMessage.getRecentMessages() != null && recentMessage.getRecentMessages().size() > 0) { //设置最后一条消息为已读
                    MsgCacheManager.setMessageRead(recentMessage.getRecentMessages().get(recentMessage.getRecentMessages().size() - 1).getId(), chatId);
                }
                dataList = recentMessage.getRecentMessages();
                adapter = new ChatAdapter(ChatActivity.this, dataList);
            } else {
                dataList = new ArrayList<ChatMessage>();
                adapter = new ChatAdapter(ChatActivity.this, dataList);
            }

            recyclerView.setAdapter(adapter);
            recyclerView.scrollToPosition(adapter.getItemCount() - 1); //滑动到底部


            adapter.setOnItemClickListener(new ChatAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (llBottom.getVisibility() == View.VISIBLE) {
                        llBottom.setVisibility(View.GONE);
                    }
                    hideKeyBorad(et);
                }
            });

            adapter.setOnUploadFileFinshListener(new ChatAdapter.UploadFileFinshListener() {
                @Override
                public void onUploadFileFinsh(ChatMessage message) {
                    saveMessage(message);
                }
            });


            adapter.setOnDeleteItemListener(new ChatAdapter.DeleteItemListener() {
                @Override
                public void onDeleteItem(ChatMessage message) {
                    if (message != null) {
                        recentMessage.getRecentMessages().remove(message);//本地移除这条消息
                        ChatMessage chatMessage = null;
                        if (recentMessage.getRecentMessages().size() > 0) {
                            chatMessage = recentMessage.getRecentMessages().get(recentMessage.getRecentMessages().size() - 1); //获取最后一条消息
                        }
                        personalChatMessage.put(chatId, recentMessage);
                        /**
                         * 重新保存聊天记录
                         */
                        preferencesHelper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", personalChatMessage);
                        /**
                         * 获取聊天列表
                         */
                        List<GroupSession> sessions = preferencesHelper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
                        for (GroupSession session : sessions) {
                            if (session.getChatId() == null)
                                continue;
                            /**
                             * 设置会话列表的最后一条消息
                             */
                            if (session.getChatId().equals(chatId)) {
                                session.setLastMessage(chatMessage == null ? "" : chatMessage.getBody());
                                break;
                            }
                        }
                        preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
                    }
                }
            });


            //回复某条信息
            adapter.setOnReplyMessageListener(new ChatAdapter.ReplyMessageListener() {
                @Override
                public void onReplyMessageListener(ChatMessage message) {
                    if (message != null) {
                        replyMessage = new ChatMessage();
                        try {
                            InvokeUtils.reflectionAttr(message, replyMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        replyMessage.setReplyMsg(null);
                        replyMessage.setId(message.getId());
                        llReply.setVisibility(View.VISIBLE);

                        String name = helper.getUserNameById(message.getFromUuid());
                        tvReplyName.setText(name);
                        ivReplyType.setVisibility(View.VISIBLE);
                        if (message.getChatCategory() == ChatMessage.CHAT_Left ||
                                message.getChatCategory() == ChatMessage.CHAT_RIGHT) {
                            if ("img".equals(message.getFormat())) {
                                ivReplyType.setImageResource(R.drawable.icon_chat_pic);
                                tvReplyContent.setText("图片");
                            } else {
                                ivReplyType.setVisibility(View.GONE);
                                tvReplyContent.setText(message.getBody());
                            }
                        } else if (message.getChatCategory() == ChatMessage.CHAT_LEFT_AUDIO ||
                                message.getChatCategory() == ChatMessage.CHAT_RIGHT_AUDIO) {
                            ivReplyType.setImageResource(R.drawable.ic_audio);
                            tvReplyContent.setText("语音");
                        } else if (message.getChatCategory() == ChatMessage.CHAT_LEFT_FILE ||
                                message.getChatCategory() == ChatMessage.CHAT_RIGHT_FILE) {
                            ivReplyType.setImageResource(R.drawable.icon_chat_file);
                            if (message.getKeyValues() != null) {
                                String fileName = message.getKeyValues().get("fileName");
                                tvReplyContent.setText(fileName);
                            }
                        }
                        et.setHint("回复" + name + ":");
                    }
                }
            });


            //点击回复消息，需要 滑动到被回复的那条消息的位置，并且 展示一个背景由白色变为透明的动画效果
            adapter.setOnClickReplyMessageListener(new ChatAdapter.ClickReplyMessageListener() {
                @Override
                public void onClickReplyMessageListener(ChatMessage replyMsg, View view) {
                    if (replyMsg != null) {
                        for (ChatMessage message : recentMessage.getRecentMessages()) {
                            if (replyMsg.getId().equals(message.getId())) {
                                final int index = recentMessage.getRecentMessages().indexOf(message);
                                //滑动到被回复的消息的位置
                                moveToPosition(index);
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setAlphaAnimation(layoutManager.findViewByPosition(index));
                                    }
                                }, 500);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }


    private void hideKeyBorad(View v) {
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == SelectPhotoBiz.REQUESTCODE_TAKE_PHOTO) {
                String photoPath = SelectPhotoBiz.getPhotoPath(mContext);
                if (!TextUtils.isEmpty(photoPath)) {
                    final List<String> pathList = new ArrayList<>();
                    pathList.add(photoPath);
                    uploadFile(pathList, "img");
                }
            } else if (requestCode == SelectPhotoBiz.REQUESTCODE_SELECT_PHOTO) {
                final List<String> pathList = SelectPhotoBiz
                        .getSelectPathListOnActivityForResult(data);
                if (pathList != null && pathList.size() != 0) {
                    uploadFile(pathList, "img");
                }
            } else if (requestCode == REQUEST_CODE_UPDATE_NAME) {
                String groupName = data.getStringExtra("groupNames");
                boolean isClearAllMessage = data.getBooleanExtra("isClearAllMessage", false);
                if (!TextUtils.isEmpty(groupName)) {
                    tvName.setText(groupName);
                }
                if (isClearAllMessage) {
                    adapter.clearAll();
                }
            } else if (requestCode == 102) {//TaskNewActivity.REQUEST_SELECT_EXCUTORS
                //选择执行人
                Bundle bundle = data.getExtras();
                UserList userList = (UserList) bundle.getSerializable("RESULT_SELECT_USER");//TaskNewActivity.RESULT_SELECT_USER
                if (userList != null) {
                    try {
                        List<User> users = userList.getUsers();
                        currentUser = users.get(0);
                        adapter.receiveSelectedUser(currentUser);
                        if (executorDialog != null && executorDialog.isShowing()) {
                            executorDialog.dismiss();
                            staffView.reloadStaffList(currentUser);
                            setSelectedExecutor(currentUser);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(mContext, "没有选择执行人", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == Constant.REQUESTCODE_FROM_ACTIVITY) { //选择文件
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                if (list != null && list.size() > 0) {
                    fileLocalPath = list.get(0);
                    Map<String, String> map = null;
                    if (fileLocalPath != null) {
                        File file = new File(fileLocalPath);
                        if (file.exists()) {
                            String length = DataCleanManager.getFormatSize(file.length());
                            String name = file.getName();
                            map = new HashMap<>();
                            map.put("fileName", name);
                            map.put("fileSize", length);
                        }
                    }
                    sendMessage(ChatMessage.FORMAT_FILE, map, "", false);
                }
            } else if (requestCode == REQUEST_CODE_SELECT_AT) { //选择要@的员工
                List<GroupMembers> list = (List<GroupMembers>) data.getSerializableExtra("selectUser");
                if (list != null && list.size() > 0) {
                    et.setText(et.getText().subSequence(0, et.getText().length() - 1));
                    for (GroupMembers members : list) {
                        if (members != null) {
                            User user = new User();
                            user.setUuid(members.getUuid());
                            user.setName(members.getName());
                            setSelectedExecutor(user);
                        }
                    }
                }
            }
        }
    }


    /**
     * 上传文件
     *
     * @param pathList 文件地址集合
     * @param fileType 文件类型
     */
    private void uploadFile(final List<String> pathList, final String fileType) {
        new Thread() {
            @Override
            public void run() {
                boolean isImage = "img".equals(fileType);
                UploadHelper.getInstance().uploadMultipleFiles("chat", pathList, isImage, new IOnUploadMultipleFileListener() {
                    @Override
                    public void onStartUpload(int sum) {

                    }

                    @Override
                    public void onProgressUpdate(int completeCount) {

                    }

                    @Override
                    public void onComplete(String attachIds) {
                        Message msg = new Message();
                        if ("img".equals(fileType)) {
                            msg.what = 1;
                        } else if (ChatMessage.FORMAT_VOICE.equals(fileType)) {
                            msg.what = 2;
                        }
                        msg.obj = attachIds;
                        handler.sendMessage(msg);
                    }
                });
            }
        }.start();
    }


    private CommanAdapter<CommandModel> getAdapter(List<CommandModel> list) {
        return new CommanAdapter<CommandModel>(list, mContext, R.layout.item_command) {
            @Override
            public void convert(int position, CommandModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.command_short_cut, item.getShortCut());  //命令快捷键
                viewHolder.setTextValue(R.id.command_name, item.getName());  //命令
            }
        };
    }


    /**
     * 获取群组成员
     */
    private void getGroupMembers() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组成员
                + "?groupId=" + groupSession.getChatId();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                //如果群成员里面不包含当前员工，不能发送消息
                if (!response.contains(Global.mUser.getUuid())) {
                    tvQuit.setVisibility(View.VISIBLE);
                    llChat.setVisibility(View.GONE);
                    setQuiteStateConversition(true);
                } else {
                    setQuiteStateConversition(false);
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


    /**
     * 获取群组信息
     */
    private void getGroupInfo() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取群组信息
                + "?groupId=" + groupSession.getChatId();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    GroupModel groupInfo = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), GroupModel.class);
                    if (adapter != null && groupInfo != null) {
                        adapter.setManagerId(groupInfo.getGroupOwner());
                    }
                    if (groupInfo != null) {
                        if (groupSession.isQuite()) {
                            tvName.setText(groupSession.getName() + "(已退出)");
                        } else {
                            tvName.setText(groupSession.getName() + "(" + groupInfo.getStaffCount() + ")");
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
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

    /**
     * 清除用户的token
     */
    private void clearToken() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.清除设备;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.reset();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 设置状态栏文字色值为深色调
     *
     * @param useDart  是否使用深色调
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setStatusTextColor(boolean useDart, Activity activity) {
        if (Build.MANUFACTURER.equalsIgnoreCase("OPPO")) {
            //OPPO
            setOPPOStatusTextColor(useDart, activity);
        }
    }


    /**
     * 显示键盘
     */
    public void showSoftInput() {
        et.requestFocus();
        et.post(new Runnable() {
            @Override
            public void run() {
                inputMethodManager.showSoftInput(et, 0);
            }
        });
    }

    /**
     * 设置OPPO手机状态栏字体为黑色(colorOS3.0,6.0以下部分手机)
     *
     * @param lightStatusBar
     * @param activity
     */
    private static final int SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010;

    private static void setOPPOStatusTextColor(boolean lightStatusBar, Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int vis = window.getDecorView().getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightStatusBar) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (lightStatusBar) {
                vis |= SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            } else {
                vis &= ~SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT;
            }
        }
        window.getDecorView().setSystemUiVisibility(vis);
    }

    private void moveToPosition(int position) {
        if (position != -1) {
            recyclerView.scrollToPosition(position);
            LinearLayoutManager mLayoutManager =
                    (LinearLayoutManager) recyclerView.getLayoutManager();
            mLayoutManager.scrollToPositionWithOffset(position, 0);
        }
    }

    private void setAlphaAnimation(View view) {
        //对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
        ValueAnimator animator = ObjectAnimator.ofInt(view, "backgroundColor", 0xff77ffff, 0x00000000);
        animator.setDuration(1000);
        animator.setEvaluator(new ArgbEvaluator());//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
        animator.start();
    }


    /**
     * 设置当前会话退出状态
     *
     * @param isQuite 是否已退出
     */
    private void setQuiteStateConversition(boolean isQuite) {
        List<GroupSession> sessions = preferencesHelper.getListData
                (Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
        for (int i = 0; i < sessions.size(); i++) {
            GroupSession session = sessions.get(i);
            if (groupSession.getChatId().equals(session.getChatId())) {
                session.setQuite(isQuite);
                break;
            }
        }
        preferencesHelper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
    }
}
