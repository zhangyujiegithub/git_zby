package com.biaozhunyuan.tianyi.pushService.vivoPush;

import android.content.Context;
import android.util.Log;

import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class VivoPushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = VivoPushMessageReceiverImpl.class.getSimpleName();

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage msg) {
        String customContentString = msg.getSkipContent();
//        Intent intent = new Intent(context, NavActivity.class);
//        intent.putExtra("pushDataType",msg.getParams().get("dataType"));
//        intent.putExtra("pushDataId",msg.getParams().get("dataId"));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
        String notifyString = "通知点击 msgId " + msg.getMsgId() + " ;customContent=" + customContentString;
        Log.d(TAG, notifyString);
    }

    @Override
    public void onReceiveRegId(Context context, String regId) {
        String responseString = "onReceiveRegId regId = " + regId;
        Log.d(TAG, responseString);
    }
}
