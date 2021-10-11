package com.biaozhunyuan.tianyi.common.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.model.form.ClientInfo;
import com.biaozhunyuan.tianyi.common.model.form.FormData;
import com.biaozhunyuan.tianyi.common.model.form.FormDetails;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.model.request.ReturnModel;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;


/**
 * 封装OkHttp异步请求网络方式*
 * <p/>
 * 通过二次封装，提供了get和post两种访问方式，把handler和子线程进行封装，调用时类似AsynTask
 * 可通过回调的方式在回调函数中直接进行UI绘制
 *
 * @author K 2015/09/16 23:48
 */
@SuppressLint("NewApi")
public class StringRequest {

    /**
     * 请求参数类型
     */
    public static final MediaType REQUEST_TYPE = MediaType
            .parse("application/x-www-form-urlencoded; charset=utf-8");

    /**
     * 请求参数类型
     */
    public static final MediaType REQUEST_TYPE2 = MediaType
            .parse("application/json; charset=UTF-8");

    /**
     * 连接超时10秒
     */
    public static final long TIME_OUT = 90;

    private static StringRequest mInstance;
    private static Handler mHandler;
    private OkHttpClient mOkHttpClient;

    private static Context context;

    @SuppressLint("NewApi")
    private StringRequest() {

        /**
         * 在OkHttpClient创建时，传入这个CookieJar的实现，完成对Cookie的自动管理
         */
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();// 设置连接超时
        // 设置连接超时
//        mOkHttpClient.setConnectTimeout(TIME_OUT, TimeUnit.SECONDS);

        // cookie enabled
//        mOkHttpClient.setCookieHandler(new CookieManager(null,
//                CookiePolicy.ACCEPT_ORIGINAL_SERVER));

        // 初始化和UI线程相关的Handlder
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static StringRequest getInstance() {
        if (mInstance == null) {
            synchronized (StringRequest.class) {
                if (mInstance == null) {
                    mInstance = new StringRequest();
                }
            }
        }
        return mInstance;
    }

    /***
     * http get请求，访问结果以String类型在UI线程在回调函数中触发
     *
     * @param url      访问地址
     * @param callBack 回调接口
     */
    public static void getAsyn(String url, final StringResponseCallBack callBack) {
        // 初始化一个请求
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = builder.url(url).build();
        Logger.i(url);
        requestFromServer(request, callBack);
    }

    /**
     * 同步GET请求
     *
     * @param url
     * @param callBack
     */
    public static void getSync(String url, final StringResponseCallBack callBack) {
        // 初始化一个请求
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = builder.url(url).build();
        Logger.i(url);
        try {
            Response response = getInstance().mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            Logger.i(request.url() + "::::" + result);
            ReturnModel<String> returnModel = JsonUtils.pareseResult(result);
            if (returnModel.getStatus() == 1) {
                callBack.onResponse(result);
            } else {
                callBack.onResponseCodeErro(result);
            }
        } catch (IOException e) {
            callBack.onFailure(request, e);
            e.printStackTrace();
        }
    }


    /**
     * 同步POST请求
     *
     * @param url
     * @param callBack
     */
    public static void postSync(String url, Object obj, final StringResponseCallBack callBack) {
        // 初始化一个请求
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Map<String, String> map = null;
        if (!(obj instanceof Map)) {
            try {
                map = JsonUtils.objectToMap(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            map = (Map<String, String>) obj;
        }
        String json = JsonUtils.convertStringParamter(map);
        RequestBody body = RequestBody.create(REQUEST_TYPE, json);
        Request request = builder.url(url).post(body).build();
        Logger.i(url);
        try {
            Response response = getInstance().mOkHttpClient.newCall(request).execute();
            String result = response.body().string();
            Logger.i(request.url() + "::::" + result);
            ReturnModel<String> returnModel = JsonUtils.pareseResult(result);
            if (returnModel.getStatus() == 1) {
                callBack.onResponse(result);
            } else {
                callBack.onResponseCodeErro(result);
            }
        } catch (IOException e) {
            callBack.onFailure(request, e);
            e.printStackTrace();
        }
    }


    /**
     * http get请求，访问结果以String类型在UI线程在回调函数中触发
     *
     * @param url      访问地址
     * @param obj      post实体对象
     * @param callBack 回调接口
     */
    public static void postAsyn(String url, Object obj,
                                final StringResponseCallBack callBack) {

        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = null;
        try {
//            String json = JsonUtils.initJsonString(obj);
            Map<String, String> map = null;
            if (!(obj instanceof Map)) {
                map = JsonUtils.objectToMap(obj);
            } else {
                map = (Map<String, String>) obj;
            }
            String json = JsonUtils.convertStringParamter(map);
            Logger.i(URLDecoder.decode(url + "\n" + json, "utf-8"));
            RequestBody body = RequestBody.create(REQUEST_TYPE, json);
            // 初始化一个请求
            request = builder.url(url).post(body).build();
            requestFromServer(request, callBack);
        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }


    /**
     * http get请求，访问结果以String类型在UI线程在回调函数中触发
     *
     * @param url      访问地址
     * @param obj      post实体对象
     * @param callBack 回调接口
     */
    public static void postAsynToMap(String url, Object obj,
                                     final StringResponseCallBack callBack) {
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = null;
        try {
            String json = "request=" + JsonUtils.initJsonString(obj);
            Logger.i("StringRequest_Post:::" +
                    url + "\n" + json);
            RequestBody body = RequestBody.create(REQUEST_TYPE, json);
            // 初始化一个请求
            request = builder.url(url).post(body).build();
            requestFromServer(request, callBack);
        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }


    /**
     * http get请求，访问结果以String类型在UI线程在回调函数中触发
     *
     * @param url      访问地址
     * @param obj      post实体对象
     * @param callBack 回调接口
     */
    public static void postAsynToJson(String url, Object obj,
                                      final StringResponseCallBack callBack) {
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = null;
        try {
            String json = JsonUtils.initJsonString(obj);
            Logger.i(url + "\n" + json);
            RequestBody body = RequestBody.create(REQUEST_TYPE2, json);
            // 初始化一个请求
            request = builder.url(url).post(body).build();
            requestFromServer(request, callBack);
        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }

    /**
     * http get请求，访问结果以String类型在UI线程在回调函数中触发
     *
     * @param url      访问地址
     * @param obj      post实体对象
     * @param callBack 回调接口
     */
    public static void postAsyn(String url, JSONObject obj,
                                final StringResponseCallBack callBack) {
        try {
            Logger.i(url + "\n" + JsonUtils.initJsonObject(obj));
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = null;
        try {
            String json = JsonUtils.initJsonObject(obj);
            RequestBody body = RequestBody.create(REQUEST_TYPE, json);
            // 初始化一个请求
            request = builder.url(url).post(body).build();
            requestFromServer(request, callBack);
        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }


    /**
     * http get请求，访问结果以String类型在UI线程在回调函数中触发,不转换为map类型key=value类型的json字符串
     *
     * @param url      访问地址
     * @param obj      post实体对象
     * @param callBack 回调接口
     */
    public static void postAsynNoMap(Context context, String url, FormData obj, List<List<FormDetails>> formDetailses,
                                     final StringResponseCallBack callBack) {
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = null;
        try {
            String jsonData = JsonUtils.initJsonString(obj);
            String details = JsonUtils.initJsonString(formDetailses);
            String encodeData = URLEncoder.encode(jsonData, "UTF-8");
            String encodeDetail = URLEncoder.encode(details, "UTF-8");
            String json = "jsonData=" + encodeData + "&detailData=" + encodeDetail;
            Logger.i("StringRequest_Post ::" +
                    url + "\n" + json);
            RequestBody body = RequestBody.create(REQUEST_TYPE, json);
            // 初始化一个请求
            request = builder.url(url).post(body).build();
            requestFromServer(request, callBack);

        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }

    public static void postAsynNoMap(String url, String obj, List<表单字段> formDetailses,
                                     final StringResponseCallBack callBack) {
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = null;
        try {
            ClientInfo info = new ClientInfo();
            info.setJsonData(formDetailses);
            String jsonData = JsonUtils.initJsonString(obj);
            String details = JsonUtils.initJsonString(info);
            String detailsData = JsonUtils.getStringValue(details, "jsonData");
            String encode = URLEncoder.encode(detailsData, "UTF-8");
            String json = "jsonData=" + encode + "&type=" + obj;
            Logger.i("StringRequest_Post ::" +
                    url + "\n" + json);
            RequestBody body = RequestBody.create(REQUEST_TYPE, json);
            // 初始化一个请求f
            request = builder.url(url).post(body).build();
            requestFromServer(request, callBack);
        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }

    public static void postAsynNoHeader(String url, Object obj,
                                        final StringResponseCallBack callBack) {
        try {
            LogUtils.i("StringRequest_Post",
                    url + "\n" + JsonUtils.initJsonString(obj));
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        Request request = null;
        try {
            String json = JsonUtils.initJsonString(obj);
            RequestBody body = RequestBody.create(REQUEST_TYPE, json);
            // 初始化一个请求
            request = new Request.Builder().url(url).post(body).build();
            requestFromServer(request, callBack);
        } catch (Exception e) {
            deliveryFailureResultToUI(request, e, callBack);
        }
    }


    /**
     * 向服务器发起请求
     *
     * @param request
     * @param callBack
     */
    private static void requestFromServer(final Request request,
                                          final StringResponseCallBack callBack) {
        // 初始化一个
        Call call = getInstance().mOkHttpClient.newCall(request);
        // 执行请求
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ProgressDialogHelper.dismiss();
                saveCookie(response.headers());
                String result = response.body().string();
                if (result.length() > 1000) {
                    LogUtils.i("result", request.url() + "\r\n" + result.substring(0, 1000));
                } else {
                    LogUtils.i("result", request.url() + "\r\n" + result);
                }
//                String resultStr = result.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
//                Logger.i(URLDecoder.decode(request.url() + "::::" + resultStr, "UTF-8"));
                try {
                    ReturnModel<String> returnModel = JsonUtils
                            .pareseResult(result);
                    // 解析状态码 1 表示成功
                    if (returnModel.Status == 1) {
                        deliverySuccessResultToUI(result, callBack);
                    } else {
                        deliveryResponseCodeErroResultToUI(result, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    deliveryFailureResultToUI(request, e, callBack);
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("网络请求失败：：：" + e.toString());
                e.printStackTrace();
                deliveryFailureResultToUI(request, e, callBack);
            }
        });
    }

    private static void deliverySuccessResultToUI(final String result,
                                                  final StringResponseCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onResponse(result);
            }
        });
    }

    private static void deliveryResponseCodeErroResultToUI(
            final String response, final StringResponseCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onResponseCodeErro(response);
            }
        });
    }

    private static void deliveryFailureResultToUI(final Request request,
                                                  final Exception ex, final StringResponseCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.i("网络访问失败：：：" + ex.toString());
                ex.printStackTrace();
                callBack.onFailure(request, ex);
            }
        });
    }

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }


    public static void saveCookie(Headers headers) {
        for (int i = 0; i < headers.size(); i++) {
            Logger.i("response.name   == " + headers.name(i) + "    response.value   == " + headers.value(i));
            if (StrUtils.equalsIgnoreCase("Set-Cookie", headers.name(i))) {
                String value = headers.value(i);
                value = StrUtils.substringBefore(value, ";");
                if (value.contains("rememberMe") && !value.contains("delete")) {
                    value = StrUtils.substringAfter(value, "=");
                    PreferceManager.getInsance().saveValueBYkey("rememberMe", value);
                    Logger.i("save rememberMe =" + value);
                } else if (value.contains("JSESSIONID") && !value.contains("delete")) {
                    value = StrUtils.substringAfter(value, "=");
                    PreferceManager.getInsance().saveValueBYkey("JSESSIONID", value);
                    Logger.i("save JSESSIONID =" + value);
                }
            }

        }
    }


    public static void addHeader(Request.Builder builder) {
        String rememberMe = PreferceManager.getInsance().getValueBYkey("rememberMe");
        String JSESSIONID = PreferceManager.getInsance().getValueBYkey("JSESSIONID");
        if (!TextUtils.isEmpty(rememberMe)) {
            builder.addHeader("Cookie", "rememberMe=" + rememberMe);
        }
        builder.addHeader("DEVICE-USER-AGENT", "android");
//        if (!TextUtils.isEmpty(JSESSIONID)) {
//            builder.addHeader("Cookie", "JSESSIONID=" + JSESSIONID);
//        }
        Logger.i("load rememberMe = " + rememberMe);
//        Logger.i("load JSESSIONID = " + JSESSIONID);

    }

    /**
     * delete 请求
     */
    public static void deleteAsyn(String url, final StringResponseCallBack callBack) {
        // 初始化一个请求
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = builder.url(url).delete().build();
        Logger.i(url);
        requestFromServer(request, callBack);
    }

    /**
     * 下载文件
     *
     * @param url
     * @param fileName
     * @param callBack
     */
    public static void downloadFile(String url, String fileName, final StringResponseCallBack callBack) {
        // 初始化一个请求
        Request.Builder builder = new Request.Builder();
        addHeader(builder);
        Request request = builder.url(url).build();
        Logger.i(url);
        requestFromServer(request, callBack, fileName);
    }

    /**
     * 向服务器发起请求
     *
     * @param request
     * @param callBack
     */
    private static void requestFromServer(final Request request,
                                          final StringResponseCallBack callBack, final String fileName) {
        // 初始化一个
        Call call = getInstance().mOkHttpClient.newCall(request);
        // 执行请求
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                saveCookie(response.headers());
                String file_name = getHeaderFileName(response);
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    String mSDCardPath = FilePathConfig.getCacheDirPath();
                    File dest;
                    if (TextUtils.isEmpty(file_name)) {
                        file_name = fileName;
                        dest = new File(mSDCardPath + "/" + fileName);
                    } else {
                        dest = new File(mSDCardPath + "/" + file_name);
                    }
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();
                    deliverySuccessResultToUI(file_name, callBack);
                    Log.i("DOWNLOAD", "download success");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("DOWNLOAD", "download failed");
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("网络请求失败：：：" + e.toString());
                e.printStackTrace();
                deliveryFailureResultToUI(request, e, callBack);
            }
        });
    }


    /**
     * 解析文件头
     * Content-Disposition:attachment;filename=FileName.txt
     * Content-Disposition: attachment; filename*="UTF-8''%E6%9B%BF%E6%8D%A2%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A.pdf"
     */
    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header("Content-Disposition");
        if (!TextUtils.isEmpty(dispositionHeader)) {
            dispositionHeader.replace("attachment;filename=", "");
            dispositionHeader.replace("filename*=utf-8", "");
            String[] strings = dispositionHeader.split("; ");
            if (strings.length > 1) {
                dispositionHeader = strings[1].replace("filename=", "");
                dispositionHeader = dispositionHeader.replace("\"", "");
                return dispositionHeader;
            }
            return "";
        }
        return "";
    }

}
