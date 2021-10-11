package com.biaozhunyuan.tianyi.pushService.oppoPush;

import android.content.Context;

import com.coloros.mcssdk.PushService;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.CommandMessage;
import com.coloros.mcssdk.mode.SptDataMessage;

/**
 * Created by wangAnMin on 2018/12/12.
 */

public class OppoPushService extends PushService {
    /**
     * 命令消息，主要是服务端对客户端调用的反馈，一般应用不需要重写此方法
     *
     * @param context
     * @param commandMessage
     */
    @Override
    public void processMessage(Context context, CommandMessage commandMessage) {
        super.processMessage(context, commandMessage);
        //TestModeUtil.addLogString(PushMessageService.class.getSimpleName(), "Receive CommandMessage");
    }

    /**
     * 普通应用消息，视情况看是否需要重写
     *
     * @param context
     * @param appMessage
     */
    @Override
    public void processMessage(Context context, AppMessage appMessage) {
        super.processMessage(context, appMessage);
        String content = appMessage.getContent();
        OppoLogUtil.addLogString(OppoPushService.class.getSimpleName(), "Receive AppMessage:" + content);
        //MessageDispatcher.dispatch(context.getApplicationContext(), content);//统一处理
    }


    /**
     * 透传消息处理，应用可以打开页面或者执行命令,如果应用不需要处理透传消息，则不需要重写此方法
     *
     * @param context
     * @param sptDataMessage
     */
    @Override
    public void processMessage(Context context, SptDataMessage sptDataMessage) {
        super.processMessage(context.getApplicationContext(), sptDataMessage);
        String content = sptDataMessage.getContent();
        OppoLogUtil.addLogString(OppoPushService.class.getSimpleName(), "Receive SptDataMessage:" + content);
    }
}
