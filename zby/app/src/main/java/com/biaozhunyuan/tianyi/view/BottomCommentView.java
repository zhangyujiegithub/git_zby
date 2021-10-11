package com.biaozhunyuan.tianyi.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/3/12.
 * 通用的底部评论 布局
 */

public class BottomCommentView extends LinearLayout {

    private EditText et_comment;
    private ImageView iv_support;
    private LinearLayout ll_comment;
    private CommentListener commentListener;
    private SupportListener supportListener;
    private SupportSuccessListener successListener;
    private CommentSuccessListener commentSuccessListener;
    private Context context;


    public BottomCommentView(Context context) {
        this(context, null);
    }

    public BottomCommentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomCommentView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_input_and_support, this, true);
        this.context = context;
        initViews(view);
        setOnEvent();
    }


    private void initViews(View view) {
        et_comment = view.findViewById(R.id.et_input_and_support);
        iv_support = view.findViewById(R.id.iv_support_input_and_support);
        ll_comment = view.findViewById(R.id.ll_send_input_and_support);

        et_comment.clearFocus();
    }


    private void setOnEvent() {

        et_comment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = ((Activity) getContext()).getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
//                    Toast.makeText(getContext(), "addOnGlobalLayoutListener=" + heightDifference, Toast.LENGTH_SHORT).show();
                if (heightDifference <= 0) {
                    //键盘隐藏,显示语音输入
                    ll_comment.setVisibility(GONE);
//                    iv_support.setVisibility(VISIBLE);
                } else {
                    ll_comment.setVisibility(VISIBLE);
//                    iv_support.setVisibility(GONE);
                }
                et_comment.requestLayout();
            }
        });

        ll_comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_comment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(context, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (commentListener != null) {
                    commentListener.onComment(comment);
                }
            }
        });

        iv_support.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (supportListener != null) {
                    supportListener.onSupport();
                }
            }
        });
    }


    public interface CommentListener {
        void onComment(String content);
    }

    public void setOnCommentListener(CommentListener listener) {
        this.commentListener = listener;
    }

    public interface SupportListener {
        void onSupport();
    }

    public void setOnSupportListener(SupportListener listener) {
        this.supportListener = listener;
    }

    public interface SupportSuccessListener {
        void onSupportSuccess();
    }

    public void setOnSupportSuccessListener(SupportSuccessListener listener) {
        this.successListener = listener;
    }


    public interface CommentSuccessListener {
        void onCommentSuccess();
    }

    public void setOnCommentSuccessListener(CommentSuccessListener listener) {
        this.commentSuccessListener = listener;
    }


    /**
     * 设置是否已经点赞，更新view
     *
     * @param isLike
     */
    public void setIsLike(boolean isLike) {
        if (isLike) {
            iv_support.setImageResource(R.drawable.ico_support_like);
        } else {
            iv_support.setImageResource(R.drawable.ico_list_support_new);
        }
    }


    /**
     * 点赞
     *
     * @param post
     */
    public void support(SupportAndCommentPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                setIsLike(true);
                if (successListener != null) {
                    successListener.onSupportSuccess();
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
     * 取消点赞
     *
     * @param post 要取消点赞的实体的ID
     */
    public void cancleSupport(SupportAndCommentPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                setIsLike(false);
                Toast.makeText(context, "取消点赞成功", Toast.LENGTH_SHORT).show();
                if (successListener != null) {
                    successListener.onSupportSuccess();
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
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
        et_comment.setText("");
        InputSoftHelper.hiddenSoftInput(context, et_comment);

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                if (commentSuccessListener != null) {
                    commentSuccessListener.onCommentSuccess();
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

}
