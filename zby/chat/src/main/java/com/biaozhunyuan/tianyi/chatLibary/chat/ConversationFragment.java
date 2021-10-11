package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.animation.Animator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.GroupSession;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.MessageSendStatusEnum;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.RecentMessage;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.AnimUtil;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.example.chat.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/4/19.
 * 会话页面
 */

public class ConversationFragment extends Fragment {

    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    public static final int REQUEST_SELECT_PARTICIPANT = 101; //选择参与人
    private Context mContext;
    private NewsAdapter adapter;
    private SharedPreferencesHelper helper;
    private DictionaryHelper dictionaryHelper;
    private List<GroupSession> list;
    private DictIosPickerBottomDialog dialog;


    private ListView lv;
    private RelativeLayout rlSearch;
    private LinearLayout llDisconnect;
    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;
    private ImageView ivDynamic; //动态
    private ImageView ivMutiChat; //创建群聊

    private static final long DURATION = 100;
    private static final float START_ALPHA = 1f;
    private static final float END_ALPHA = 1f;
    private float bgAlpha = 1f;
    private boolean bright = false;
    private TextView tv_talk_all;
    private TextView tv_talk_only;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_list, null);
        initViews(view);
        initData();
//        getSessionList();
        setOnEvent();
        return view;
    }

    private void initViews(View view) {
        mPopupWindow = new PopupWindow(getActivity());
        animUtil = new AnimUtil();
        lv = view.findViewById(R.id.lv_conversation_list);
        rlSearch = view.findViewById(R.id.rl_search_ico);
        llDisconnect = view.findViewById(R.id.ll_disconnect);
        ivDynamic = view.findViewById(R.id.iv_show_dynamic);
        ivMutiChat = view.findViewById(R.id.iv_create_muti_chat);
    }

    private void initData() {
        mContext = getActivity();
        dialog = new DictIosPickerBottomDialog(mContext);
        helper = new SharedPreferencesHelper(mContext);
        dictionaryHelper = new DictionaryHelper(mContext);
        updateLocalSessionList();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(String message) {
        if ("断开连接".equals(message)) {
            llDisconnect.setVisibility(View.VISIBLE);
        } else if ("重新连接".equals(message)) {
            llDisconnect.setVisibility(View.GONE);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(ChatMessage message) {
        if (message != null) {
            //消息回执，将本地的消息状态更新为发送成功，并且重新保存
            if ("sak".equalsIgnoreCase(message.getCmd())) {
                updateMsgStatusRecoverd(message);
            } else {
                boolean isSaveMsg = true;
                if (ChatMessage.FORMAT_VOICE.equals(message.getFormat())) {
                    message.setChatCategory(ChatMessage.CHAT_LEFT_AUDIO);
                } else if (ChatMessage.FORMAT_FILE.equals(message.getFormat())) {
                    message.setChatCategory(ChatMessage.CHAT_LEFT_FILE);
                } else if (ChatMessage.FORMAT_TIP.equals(message.getFormat())) {
                    //撤回消息
                    if (!TextUtils.isEmpty(message.getBody())
                            && message.getBody().contains("撤回")) {
                        HashMap<String, RecentMessage> msgs = helper.getHashMapData
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
                        helper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", msgs);
                    }
                } else {
                    message.setChatCategory(ChatMessage.CHAT_Left);
                }
                if (!TextUtils.isEmpty(message.getBody())) {
                    message.setBody(message.getBody().replaceAll("\n", ""));
                }
                if (TextUtils.isEmpty(message.getChatId())) {
                    message.setChatId(UUID.randomUUID().toString().replaceAll("-", ""));
                }

                //不是单聊,保存头像
                if (message.getKeyValues() != null && !"1".equals(message.getKeyValues().get("isSingle"))) {
                    if (!TextUtils.isEmpty(message.getGroupIcon())) {
                        message.setAvatar(message.getGroupIcon());
                    }
                }
                if (isSaveMsg) {
                    boolean isShock = true;
                    //群组是否设置免打扰，如果设置免打扰，不震动
                    for (GroupSession session : list) {
                        if (session.getChatId().equals(message.getChatId())) {
                            isShock = session.isSetNoInterrupt();
                            break;
                        }
                    }
                    MsgCacheManager.saveMessage(mContext, message, !isShock);
                }
                MsgCacheManager.setMessageRead(message.getId(), message.getChatId());
                SessionCacheManger.saveSession(mContext, "", message);
                refreshData();
                refreshUnreadCount();
            }
        }
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onReceiveUnReadMsgEvent(String status) {
        if ("505".equals(status)) {
            if (adapter != null) {
                refreshData();
                refreshUnreadCount();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        refreshData();
        refreshUnreadCount();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            refreshData();
        }
    }


    private void showPop() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.pop_add, null));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAsDropDown(ivMutiChat, 40, 0);
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });

        tv_talk_all = mPopupWindow.getContentView().findViewById(R.id.tv_talk_all);//发起群聊
        tv_talk_only = mPopupWindow.getContentView().findViewById(R.id.tv_talk_only);//选择单聊

        tv_talk_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName comp = new ComponentName(mContext, "com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity");
                Intent intent = new Intent();
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("intent_tag", "chatActivity");
                List<User> list = new ArrayList<>();
                list.add(Global.mUser);
                //创建群聊的时候 不能选择自己
                intent.putExtra("unClickAbleUsers", (Serializable) list);
                intent.putExtra("title", "创建群聊");
                intent.setComponent(comp);
                intent.setAction("android.intent.action.VIEW");
                startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                mPopupWindow.dismiss();
            }
        });

        tv_talk_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupSessionActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                mPopupWindow.dismiss();
            }
        });

    }

    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private void refreshData() {
        updateLocalSessionList();
    }


    /**
     * 获取会话列表，本地列表已经存在的，不做处理，本地列表不存在的，加入本地缓存
     */
    private void getSessionList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取会话列表;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<GroupSession> groupSessions = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), GroupSession.class);
                if (groupSessions != null) {
                    for (GroupSession groupSession : groupSessions) {
                        for (GroupSession session : list) {
                            if (session.getChatId().equals(groupSession.getUuid())) {
                                if (TextUtils.isEmpty(session.getAvatar())) {
                                    session.setAvatar(groupSession.getIcon());
                                }
                            }
                        }
                    }
                    helper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), list);
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
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


    private void setOnEvent() {

        adapter.setOnItemClickListener(new NewsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(GroupSession item) {
                item.setUnreadCount(0);
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("chatUser", item);


                //设置未读数量为0
                List<GroupSession> sessions = helper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
                for (GroupSession session : sessions) {
                    if (session.getChatId() == null)
                        continue;
                    if (session.getChatId().equals(item.getChatId())) {
                        session.setUnreadCount(0);
                        session.setAtType(0);
                        break;
                    }
                }
                refreshUnreadCount();
                //如果是跳转到聊天页面，聊天页面有接受消息的设置，这里需要取消注册，防止重复接收消息
                if (EventBus.getDefault().isRegistered(ConversationFragment.this)) {
                    EventBus.getDefault().unregister(ConversationFragment.this);
                }
                helper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), sessions);
                startActivity(intent);
            }
        });

        adapter.setOnDeleteItemListener(new NewsAdapter.DeleteItemListener() {
            @Override
            public void onDeleteItem(GroupSession session) {
                list.remove(session);
                HashMap<String, RecentMessage> hashMapData = helper.getHashMapData
                        (Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);
                RecentMessage recentMessage = hashMapData.get(session.getChatId());
                if (recentMessage != null) {
                    hashMapData.remove(session.getChatId());
                }
                helper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), list);
                helper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", hashMapData);
                updateLocalSessionList();
                refreshUnreadCount();
            }
        });

        ivDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ComponentName comp = new ComponentName(mContext, "com.biaozhunyuan.tianyi.dynamic.DynamicActivity");
                Intent intent = new Intent();
                intent.setComponent(comp);
                startActivity(intent);
            }
        });

        ivMutiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
                toggleBright();
            }
        });

        /**
         * 搜索 人员、群组、聊天记录
         */
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SearchAllActivity.class));
            }
        });
    }


    UnRedCountChangeListener redCountChangeListener;

    public interface UnRedCountChangeListener {
        void UnRedCountChange(int count);
    }

    public void setOnUnRedCountChangeListener(UnRedCountChangeListener listener) {
        redCountChangeListener = listener;
    }


    //刷新未读数量
    private void refreshUnreadCount() {
        List<GroupSession> messages = helper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);

        int unreadCount = 0;
        for (GroupSession message : messages) {
            if (message.getUnreadCount() > 0 && !message.isSetNoInterrupt()) {
                unreadCount += message.getUnreadCount();
            }
        }
        if (redCountChangeListener != null)
            redCountChangeListener.UnRedCountChange(unreadCount);
    }


    /**
     * 消息回执，将本地的消息状态更新为发送成功，并且重新保存
     *
     * @param msg
     */
    private void updateMsgStatusRecoverd(ChatMessage msg) {
        HashMap<String, RecentMessage> hashMapData = helper.getHashMapData
                (Global.mUser.getUuid() + "ChatRecord", RecentMessage.class);

        for (Map.Entry<String, RecentMessage> entry : hashMapData.entrySet()) {
            for (ChatMessage message : entry.getValue().getRecentMessages()) {
                if (message.getId().equals(msg.getId())) {
                    message.setSendStatus(MessageSendStatusEnum.发送成功.getStatus());
                    helper.putHashMapData(Global.mUser.getUuid() + "ChatRecord", hashMapData);
                    break;
                }
            }
        }
    }


    /**
     * 设置本地数据 群组为置顶/取消置顶
     */
    private void updateLocalSessionList() {
        list = helper.getListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), GroupSession.class);
        List<GroupSession> topList = new ArrayList<>(); //取到置顶的数据
        List<GroupSession> unTopList = new ArrayList<>(); //取到未置顶的数据
        for (GroupSession list1 : list) {
            if (list1.isTop()) {
                topList.add(list1);
            } else {
                unTopList.add(list1);
            }
        }
        //将置顶的会话按照置顶时间排序
        sortListByTopTime(topList);
        //将未置顶的会话按照最后一条消息的发送时间排序
        sortListByLastSendTime(unTopList);
        list.clear();
        list.addAll(topList);
        list.addAll(unTopList);
//        helper.putListData(Global.mUser.getCorpId() + Global.mUser.getUuid(), lists);

        if (adapter == null) {
            adapter = new NewsAdapter(mContext, list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }

    /**
     * 根据置顶时间正序排列
     *
     * @param list
     */
    private void sortListByTopTime(List<GroupSession> list) {
        Collections.sort(list, new Comparator<GroupSession>() {
            @Override
            public int compare(GroupSession o1, GroupSession o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getTopTime());
                    Date dt2 = format.parse(o2.getTopTime());
                    if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    /**
     * 根据最后一条消息发送时间倒序排序
     *
     * @param list
     */
    private void sortListByLastSendTime(List<GroupSession> list) {
        Collections.sort(list, new Comparator<GroupSession>() {
            @Override
            public int compare(GroupSession o1, GroupSession o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getLastMessageSendTime());
                    Date dt2 = format.parse(o2.getLastMessageSendTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
