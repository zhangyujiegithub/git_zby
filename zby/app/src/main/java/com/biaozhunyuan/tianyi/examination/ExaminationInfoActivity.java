package com.biaozhunyuan.tianyi.examination;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.curriculum.Curriculum;
import com.biaozhunyuan.tianyi.curriculum.CurriculumlistActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.supportAndComment.SupportListPost;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BottomCommentView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import java.util.List;

import okhttp3.Request;

public class ExaminationInfoActivity extends BaseActivity {

    private WebView mWebView;
    private BoeryunHeaderView headerview;
    private String url;

    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS =
            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private LinearLayout ll_parent; //点赞评论父容器
    private LinearLayout ll_viewcount;
    private LinearLayout ll_support;  //评论人
    private NoScrollListView commen_listView;  //评论列表
    private TextView tv_nocomment;  //暂无评论
    private ImageView iv_support; //点赞按钮
    private ImageView iv_connment; //评论按钮
    private TextView tv_support; //点赞数量
    private TextView tv_supportuser; //点赞人
    private TextView tv_comment; //评论数量
    private TextView tv_viewcount; //浏览数量
    private BottomCommentView commentView; //父容器
    private DictionaryHelper helper;
    private String supportUser;
    private Curriculum mCurriculum;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private String title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_info);
        helper = new DictionaryHelper(this);
        initView();
        getIntentData();
        loadWebView();
        setOnTouch();
    }

    private void setOnTouch() {

        commentView.setOnSupportSuccessListener(new BottomCommentView.SupportSuccessListener() {
            @Override
            public void onSupportSuccess() { //点赞成功，重新加载点赞列表
//                fragment.reloadSupport();
                getSupportList(mCurriculum);
            }
        });

        commentView.setOnCommentListener(new BottomCommentView.CommentListener() {
            @Override
            public void onComment(String count) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mCurriculum.getUuid());
                post.setDataType("课程中心");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mCurriculum.getUuid());
                post.setContent(count);
                commentView.comment(post);
            }
        });


        commentView.setOnCommentSuccessListener(new BottomCommentView.CommentSuccessListener() {
            @Override
            public void onCommentSuccess() {
//                fragment.reloadComment();
                tv_comment.setText((mCurriculum.getCommentNumber() + 1) + "");
                getCommentList(mCurriculum);
            }
        });

        iv_connment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw(mCurriculum);
            }
        });

        iv_support.setOnClickListener(new View.OnClickListener() {
            private int likeNumber;

            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mCurriculum.getUuid());
                post.setDataType("课程中心");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mCurriculum.getUuid());
                if (mCurriculum.isLike()) { //取消点赞
                    likeNumber = mCurriculum.getLikeNumber() - 1;
                    tv_support.setText(likeNumber + "");
                    commentView.cancleSupport(post);
                    mCurriculum.setLike(false);
                } else {
                    likeNumber = (mCurriculum.getLikeNumber() + 1);
                    tv_support.setText(likeNumber + "");
                    mCurriculum.setLike(true);
                    commentView.support(post);
                }
                mCurriculum.setLikeNumber(likeNumber);
                if (mCurriculum.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
                CurriculumlistActivity.isResume = true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.reload();
    }

    /**
     * 浏览器模式加载表单
     */
    private void loadWebView() {
        mWebView.setInitialScale(25);
        // 得到浏览器设置
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);// 可以使用插件
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // 设置浏览器中javascript可执行
        webSettings.setJavaScriptEnabled(true);
        // 设置webview推荐使用的窗口
        webSettings.setUseWideViewPort(true);
        // 置webview加载的页面的
        webSettings.setLoadWithOverviewMode(true);
        // 设置缩放控制
        webSettings.setBuiltInZoomControls(true);
        // 支持缩放
        webSettings.setSupportZoom(true);
        // 缩放按钮
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);
        synCookies(ExaminationInfoActivity.this);
        //headers.put("Cookie",);
        mWebView.loadData("", "text/html", "UTF-8");


        WebViewClient wvc = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        };
        mWebView.setWebViewClient(wvc);

        mWebView.setWebChromeClient(new WebChromeClient() {

            /*** 视频播放相关的方法 **/
            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(ExaminationInfoActivity.this);
                frameLayout.setLayoutParams(new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                showCustomView(view, callback);
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }
        });

        mWebView.loadUrl(url);

        if(!TextUtils.isEmpty(title)){
            mCurriculum = (Curriculum) getIntent().getSerializableExtra("curriculun");
            setSupportAndCommentData(mCurriculum);
        }
    }


    private void getIntentData() {
        if(getIntent().getStringExtra("url")!=null){
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            if(!TextUtils.isEmpty(title)){
                headerview.setTitle(title);
            }
        }
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        mWebView = findViewById(R.id.webview);

        ll_parent = findViewById(R.id.ll_support_comment_parent);
        ll_viewcount = findViewById(R.id.ll_viewcount);
        ll_support = findViewById(R.id.ll_support_list);
        commen_listView = findViewById(R.id.contactinfo_listview);
        tv_nocomment = findViewById(R.id.tv_nocomment);
        iv_support = findViewById(R.id.iv_support);
        tv_support = findViewById(R.id.tv_support);
        tv_supportuser = findViewById(R.id.tv_support_user);
        tv_comment = findViewById(R.id.tv_comment);
        commentView = findViewById(R.id.comment_log_info);
        iv_connment = findViewById(R.id.iv_comment);
        tv_viewcount = findViewById(R.id.tv_viewcount);

        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });
    }

    /**
     * 设置点赞和评论数据
     */
    private void setSupportAndCommentData(Curriculum contact) {
        ll_parent.setVisibility(View.VISIBLE);
        if (contact.isLike()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
        }
        tv_viewcount.setText("浏览" + contact.getFavoriteNumber() + "次");
        commentView.setIsLike(contact.isLike());

        tv_comment.setText(contact.getCommentNumber() + "");
        tv_support.setText(contact.getLikeNumber() + "");

        getCommentList(contact);
        getSupportList(contact);
    }

    /**
     * 获取点赞列表
     *
     * @param contact
     */
    private void getSupportList(Curriculum contact) {
        SupportListPost post = new SupportListPost();
        post.setDataType("课程中心");
        post.setDataId(contact.getUuid());
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞列表;
        supportUser = "";
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    ll_support.setVisibility(View.VISIBLE);
                    for (int i = 0; i < list.size(); i++) {
                        supportUser += helper.getUserNameById(list.get(i).getFromId()) + "、";
                    }
                    String substring = supportUser.substring(0, supportUser.length() - 1);
                    tv_supportuser.setText(substring);
                } else {
                    ll_support.setVisibility(View.GONE);
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
     * 获取评论列表
     *
     * @param contact
     */
    private void getCommentList(Curriculum contact) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论列表;
        SupportListPost post = new SupportListPost();
        post.setDataType("课程中心");
        post.setDataId(contact.getUuid());

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    tv_nocomment.setVisibility(View.GONE);
                    commen_listView.setVisibility(View.VISIBLE);
                    commen_listView.setAdapter(getAdapter(list));
                    tv_comment.setText(list.size() + "");
                } else {
                    tv_nocomment.setVisibility(View.VISIBLE);
                    commen_listView.setVisibility(View.GONE);
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
     * 评论列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
                TextView tv_content = viewHolder.getView(R.id.tv_time_task_item);
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getFromId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNameById(item.getFromId()));//点赞人姓名
//                viewHolder.setTextValue(R.id.tv_time_task_item, ViewHelper.convertStrToFormatDateStr(item.getTime(), "MM月dd日 HH:mm"));//点赞时间
                tv_content.setText(item.getContent());  //评论内容
            }
        };
    }
            /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(ExaminationInfoActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /** 隐藏视频全屏 */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面 */
                if (customView != null) {
                    hideCustomView();
                } else if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.JSESSIONID + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.JSESSIONID));
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
    }


    private void popWiw(final Curriculum item) {

        popWiw = new BaseSelectPopupWindow(this, R.layout.edit_data);
        // popWiw.setOpenKeyboard(true);
        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWiw.setFocusable(true);
        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWiw.setShowTitle(false);
        popWiw.setBackgroundDrawable(new ColorDrawable(0));
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final TextView send = popWiw.getContentView().findViewById(
                R.id.btn_send);
        final TextEditTextView edt = popWiw.getContentView().findViewById(
                R.id.edt_content);

        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (TextUtils.isEmpty(edt.getText())) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                        String content = edt.getText().toString().trim();

                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("课程中心");
                        post.setDataId(item.getUuid());
                        post.setContent(content);
                        comment(post, item);
                        popWiw.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                    // /提交内容
                    String content = edt.getText().toString().trim();

                    SupportAndCommentPost post = new SupportAndCommentPost();
                    post.setFromId(Global.mUser.getUuid());
                    post.setToId(item.getCreatorId());
                    post.setDataType("课程中心");
                    post.setDataId(item.getUuid());
                    post.setContent(content);
                    comment(post, item);
                    popWiw.dismiss();
                }
            }
        });
        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.activity_add_record, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Curriculum space) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
//        et_comment.setText("");
//        InputSoftHelper.hiddenSoftInput(getActivity(), et_comment);
//        ll_bottom.setVisibility(View.GONE);
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("评论成功");
                space.setCommentNumber(space.getCommentNumber() + 1);
                getCommentList(mCurriculum);
                CurriculumlistActivity.isResume = true;
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
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
