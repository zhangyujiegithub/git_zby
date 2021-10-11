package com.biaozhunyuan.tianyi.chatLibary.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.biaozhunyuan.tianyi.chatLibary.chat.model.ChatMessage;
import com.biaozhunyuan.tianyi.chatLibary.chat.model.Command;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.utils.ByteUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

import static com.biaozhunyuan.tianyi.common.helper.PreferceManager.getInsance;


/**
 * Created by wangAnMin on 2018/12/26.
 */

public class SocketService extends Service {
    /*socket*/
    private Socket socket;
    /*连接线程*/
    private Thread connectThread;
    private Timer timer = new Timer();
    private OutputStream outputStream;
    private InputStream input;

    private SocketBinder sockerBinder = new SocketBinder();
    private String ip;
    private int port;
    private TimerTask task;
    private Gson gson = new Gson();

    private int connectStatus = 1; //1:socket连接，2:socket断开连接

    /*默认重连*/
    public boolean isReConnect = true;

    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public IBinder onBind(Intent intent) {
        return sockerBinder;
    }


    public class SocketBinder extends Binder {

        /*返回SocketService 在需要的地方可以通过ServiceConnection获取到SocketService  */
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*拿到传递过来的ip和端口号*/
        ip = Global.CHAT_IP;
        port = Global.CHAT_PORT;

        LogUtils.i("SocketService", "onCreate");
        /*初始化socket*/
        initSocket();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("SocketService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    /*初始化socket*/
    private void initSocket() {
        LogUtils.i("SocketService", "initSocket");
        if (socket == null && connectThread == null) {
            connectThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    socket = new Socket();
                    try {
                        socket.setKeepAlive(true);
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                    //当socket连接的时候 获取 离线消息,获取成功后发布通知
                    MsgCacheManager.getUnreadMessage(getApplicationContext());
                    try {
                        /*超时时间为5秒*/
                        socket.connect(new InetSocketAddress(ip, port), 5000);
                        /*连接成功的话  发送心跳包*/
                        if (socket.isConnected()) {


                            /*因为Toast是要运行在主线程的  这里是子线程  所以需要到主线程哪里去显示toast*/
                            toastMsg("socket已连接");

                            updateConnectStatus(2);
                            /*socket连接成功，登录聊天服务器*/
                            connectChat();

                            input = socket.getInputStream();
                            outputStream = socket.getOutputStream();


                            /*接收消息*/
                            receiveMsg();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            toastMsg("连接超时，正在重连");
                            releaseSocket();
                        } else if (e instanceof NoRouteToHostException) {
                            toastMsg("该地址不存在，请检查");
                            releaseSocket();
                        } else if (e instanceof ConnectException) {
                            toastMsg("连接异常或被拒绝，请检查");
                            updateConnectStatus(1);
                            releaseSocket();
                        }
                    }
                }
            });

            /*启动连接线程*/
            connectThread.start();

        }


    }


    /**
     * 接收消息
     */
    private void receiveMsg() {
        try {
            /**
             * 截取前四个字节，代表消息字节的长度，从消息字节中截取对应长度的字节
             */
            while (true) {
                if (input == null) {
                    continue;
                }
                if (input.available() <= 0) {
                    continue;
                }
                byte[] bodyLen = new byte[4];
                byte[] blobLen = new byte[4];
                input.read(bodyLen);
                input.read(blobLen);
                byte[] bytes = new byte[input.available()];//消息体字节数组
                input.read(bytes);
                String json = null;
                try {
                    json = ByteUtils.toString(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LogUtils.i("SocketService", "内容 : " + json);
                try {
                    ChatMessage chatMessage = JsonUtils.jsonToEntity(json, ChatMessage.class);
                    if (chatMessage != null) {
                        if ("logout".equals(chatMessage.getCmd())) {
                            isReConnect = false;
                            stopSelf();
                            EventBus.getDefault().postSticky("502");
                        }
                        if (Command.login.equals(chatMessage.getCmd())) { //登录成功
                            /*连接成功以后，发送心跳数据*/
                            sendBeatData();
                            toastMsg("聊天服务器登录成功");
//                            String cropId = JsonUtils.getStringValue(chatMessage.getBody(), "corpId");
//                            Global.mUser.setCorpId(cropId);
                        }
//                        if (TextUtils.isEmpty(chatMessage.getChatId())) //返回成功发送消息回执，chatId为空
//                            continue;
                        EventBus.getDefault().post(chatMessage);
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /*因为Toast是要运行在主线程的   所以需要到主线程哪里去显示toast*/
    private void toastMsg(final String msg) {

        LogUtils.i("SocketService", msg);
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 连接聊天服务器
     */
    private void connectChat() {
        String userName = getInsance().getValueBYkey("userNmae");
        String passWord = getInsance().getValueBYkey("passWord");
        String cropName = getInsance().getValueBYkey("cropNmae");
        String content = "{\"c\":\"" + cropName + "\"," +
                "\"u\":\"" + userName + "\"," +
                "\"p\":\"" + passWord + "\"," + "\"device\":\"m\"}";
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setBody(content);
        chatMessage.setCmd(Command.login);
        sendMessage(chatMessage);
    }

    /**
     * 发送消息
     *
     * @param chatMessage
     */
    public void sendMessage(final ChatMessage chatMessage) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String messageJson = gson.toJson(chatMessage);

                    /**
                     * 需要把字符串的字节长度转换为字节数组写入到流中
                     */
                    LogUtils.i("chatMessage::::", messageJson);
                    byte[] buffer = messageJson.getBytes("utf-8"); //获取到数据的字节数组
                    int bufLen = buffer.length;
                    byte[] lenBuffer = ByteUtils.intToBytes(bufLen);

                    byte version = 2;  //版本号
                    byte blobLen = 0;  //版本号
                    outputStream.write(version); //流中写入 版本号
                    outputStream.write(int2ByteArray(buffer.length)); //流中写入 消息体字节长度
                    outputStream.write(int2ByteArray(blobLen));//流中写入 blob字节长度
                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.flush(); // 刷新输出流，使Server马上收到该字符串
                } catch (Exception e) {
                    e.printStackTrace();

                    releaseSocket();
                    Log.e("test", "错误：" + e.toString());
                }
            }
        }.start();

    }

    private byte[] int2ByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /*定时发送数据*/
    private void sendBeatData() {
        if (timer == null) {
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setCmd(Command.heartbeat);
                        sendMessage(chatMessage);
                    } catch (Exception e) {
                        /*发送失败说明socket断开了或者出现了其他错误*/
                        toastMsg("连接断开，正在重连");
                        /*重连*/
                        releaseSocket();
                        e.printStackTrace();
                    }
                }
            };
        }

        timer.schedule(task, 0, 15000);
    }

    private void releaseSocket() {
        if (isReConnect) {  //如果手动离线重连不间隔，立马掉线
            releaseSocket(15000);
        } else {
            releaseSocket(0);
        }
    }


    /*释放资源*/
    public void releaseSocket(long second) {
        if (isReConnect) {  //如果手动离线重连不间隔，立马掉线
            try {
                Thread.sleep(second);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if (input != null) {
            try {
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            input = null;
        }

        if (socket != null) {
            try {
                socket.close();

            } catch (IOException e) {
            }
            socket = null;
        }

        if (connectThread != null) {
            connectThread = null;
        }

        /*重新初始化socket*/
        if (isReConnect) {
            initSocket();
        }

    }

    private void updateConnectStatus(int status) {
        if (status == 1) {
            EventBus.getDefault().postSticky("断开连接");
        } else if (status == 2) {
            EventBus.getDefault().postSticky("重新连接");
        }

    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     * @param socket
     * @return
     */
    public Boolean isServerClose(Socket socket){
        try{
            socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            EventBus.getDefault().postSticky("重新连接");
            return false;
        }catch(Exception se){
            EventBus.getDefault().postSticky("断开连接");
            return true;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SocketService", "onDestroy");
        isReConnect = false;
        releaseSocket();
    }
}
