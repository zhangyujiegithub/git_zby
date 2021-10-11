package com.biaozhunyuan.tianyi.helper;

/**
 * 向服务器存储和获取数据 请求响应回调接口
 */

public interface ParamCallback {

    /**
     * 请求成功回调函数value
     * @param value
     *  响应结果：key对应的value
     */
    public void onParam(String value);

    /***
     * 执行操作失败回调函数
     */
    public void onFailure();
}
