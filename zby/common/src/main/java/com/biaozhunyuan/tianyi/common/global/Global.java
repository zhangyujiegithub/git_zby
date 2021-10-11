package com.biaozhunyuan.tianyi.common.global;


import com.biaozhunyuan.tianyi.common.model.user.User;

/**
 * Created by 王安民 on 2017/8/4.
 * <p>
 * 全局文件
 */

public class Global {

    //sdfsdf98894d8f4s5d4f1e98w1r84dz(科研事项审批)
    //e86b272ca37844b3516722fd812481e3(横向——项目变更)
    //e86b872ca37844b3914325fd89790ioa(横向——项目验收)
    //category
    public static final String CATEGORY = "f4c2c84c0b90418fae46ff5915b63c0x,f4c2c84c0b90418fae46ff5915b63c0m," +
            "f4c2c84c0b90418fae46ff5915b63c0p,f4c2c84c0b90418fae46ff5915b63c6c," +
            "sdfsdf98894d8f4s5d4f1e98w1r84d1,33fb40edb10948e6a5442dd4141d1607," +
            "sdfsdf98894d8f4s5d4f1e98w1r84dz,e86b272ca37844b3516722fd812481e3";
    public static final String KYYB="科研项目已办";

    public static final String[] HX ={"项目申报","项目变更","项目验收","合同列表"};
    public static final String CG_XXHCG="信息化采购列表";
    public static final String[] Y ={"项目申报","任务下达","项目变更","阶段检查","项目验收"
            ,"项目跟踪","评审委托书","阶段检查委托书","验收委托书"};
    public static final String[] KYWT ={"项目立项","合同审批","项目变更","项目验收"};

    /**
     * 小米推送的id和key
     */
    public static final String xmAppId = "2882303761518958572";
    public static final String xmAppKey = "5531895877572";

    /**
     * oppo推送的id和key
     */
    public static final String oppoAppKey = "d773136efda34886ac910aeb3b136372";
    public static final String oppoAppSecret = "9a9090ad0e1449b58772ee0fe92a3f0c";


    /**
     * 聊天的ip和端口
     */
    public static final String CHAT_IP = "39.105.96.34"; //正式
    //    public static final String CHAT_IP = "39.106.166.59"; //测试
//    public static final String CHAT_IP = "192.168.1.67"; //测试
    public static final int CHAT_PORT = 8026;

    /**
     * 企业的资源文件配置
     */
    public static String CONTACT_TIME = "联系时间";
    public static String CONTACT_TITLE = "联系记录";
    public static String CONTACT_CONTENT = "联系内容";
    public static String CONTACT_STAGE = "阶段";
    public static String CONTACT_NEXTCONTENT = "下次联系内容";
    public static String CONTACT_NEXTCONTACTTIME = "下次联系时间";
    public static String FORM_PAPER = "套打";

    /**
     * 个人电脑ip
     */
//    public static final String JAVA_IP = "192.168.2.106";
//    public static String BASE_JAVA_URL = "http://" + JAVA_IP + ":8081/";

    /**
     * 当前登录的企业名
     */
    public static String CURRENT_CROP_NAME = "新起点";

    /**
     * 外网ip
     */
//    public static String BASE_JAVA_URL = "http://zhyw.cnis.ac.cn/"; //正式地址
    //public static String BASE_JAVA_URL = "http://39.96.78.45:8082/"; //测试地址
    public static String BASE_JAVA_URL = "http://192.168.1.100:8094/"; //本地测试地址
   // public static String BASE_JAVA_URL = "http://192.168.103.158:8094/"; //本地测试地址
//
//    public static String BASE_JAVA_URL = "http://111.204.220.230:12081/";//发布地址
//    public static String BASE_JAVA_URL = "http://ngrok.tysoft.com:45643/";//测试地址
//    public static String BASE_JAVA_URL = "http://10.2.1.31/";//测试地址

//    public static String BASE_JAVA_URL = "http://myth.ngrok.tysoft.com:8123/";//测试地址


    public static final String IP = "www.boeryun.com";
    public static final String BASE_URL = "http://" + IP + ":8076/";
    public static final String BASE_URL_PROCESS = "http://" + IP + ":8076/";
    public static final String EXTENSION = "";
    public static User mUser = new User();
}
