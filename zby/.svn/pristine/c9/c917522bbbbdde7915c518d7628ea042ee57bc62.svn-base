package com.biaozhunyuan.tianyi.chatLibary.chat.model;

/**
 * 客户端请求服务端处理的命令
 */
public abstract class Command{
    //由IOServer或server填写，表示网络故障或登陆失败
    public final static String error = "err"; //error
    //回执，以确认消息服务器已接受
    public final static String serverAck = "sak"; //ok
    //回执，以确认消息接收人已接受
    public final static String recepientAck = "rak"; //ok
    //消息，server端只负责转发而不对Msg做处理
    public final static String chat = "cht"; //chat
    //登陆信息，由server处理并返回登陆状态
    public final static String login = "lin"; //login
    //请求加载聊天记录
    public final static String fetchChatHistory = "his"; //history
    //网络状态检测（心跳包），server收到后回复ok
    public final static String heartbeat = "hbt"; //netCheck
    //系统提醒
    public final static String warning = "wrn"; //warning
    //查询在线用户
    public final static String checkTalkerOnlineStatus = "olu"; //onlineUser
}
