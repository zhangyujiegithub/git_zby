package com.biaozhunyuan.tianyi.log;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.supportAndComment.CommonFragment;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BottomCommentView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/15.
 * <p>
 * 日志详情页面
 */

public class LogInfoActivity extends FragmentActivity {


    private Context context;
    private WorkRecord mRecord;
    private Dynamic dynamic;


    private BoeryunHeaderView headerView;
    private CircleImageView iv_head;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_content;
    //    private TextView tv_attch_count;
    private TextView tv_support;
    private TextView tv_comment;
    private ImageView iv_support;
    private BottomCommentView commentView;
    private MultipleAttachView attach_view;
    private TextView tv_nocomment;
    private NoScrollListView lv_comment;
    private LinearLayout ll_support;
    private TextView support_name;

    private DictionaryHelper dictionaryHelper;
    private CommonFragment fragment;
    private ImageView iv_comment;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private TextView tv_viewcount;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_log_info);
        dictionaryHelper = new DictionaryHelper(getBaseContext());
        initViews();
        initIntentData();
        setOnEvent();
    }


    private void initViews() {
        tv_viewcount = findViewById(R.id.tv_viewcount);
        iv_comment = findViewById(R.id.iv_comment);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_log_info);
        tv_name = (TextView) findViewById(R.id.tv_name_log_info);
        tv_time = (TextView) findViewById(R.id.tv_time_log_info);
        tv_content = (TextView) findViewById(R.id.content_log_info);
        iv_head = (CircleImageView) findViewById(R.id.head_log_info);
        commentView = findViewById(R.id.comment_log_info);
        iv_support = findViewById(R.id.iv_support);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_support = (TextView) findViewById(R.id.tv_support);
        attach_view = findViewById(R.id.attach_view);
        tv_nocomment = findViewById(R.id.tv_nocomment);
        lv_comment = findViewById(R.id.lv_comment);
        ll_support = findViewById(R.id.ll_support);
        support_name = findViewById(R.id.support_name);
    }


    private void initIntentData() {
        context = LogInfoActivity.this;
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("logInfoExtras");
            if (bundle != null) {
                mRecord = (WorkRecord) bundle.getSerializable("logInfo");
                if (mRecord != null) {
                    initData();
                }
            }
            dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            if (dynamic != null) {
                getLogInfo();
            }
        }
    }

    private void initData() {
        tv_name.setText(dictionaryHelper.getUserNameById(mRecord.getCreatorId()));
        tv_time.setText(ViewHelper.convertStrToFormatDateStr(mRecord.getLastUpdateTime(), "yyyy-MM-dd HH:mm"));
        tv_content.setText(mRecord.getContent());
        ImageUtils.displyUserPhotoById(context, mRecord.getCreatorId(), iv_head);
        attach_view.loadImageByAttachIds(mRecord.getAttachmentIds());


        if (mRecord.isLikeNumber()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
            tv_support.setTextColor(getResources().getColor(R.color.statusbar_mine));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
            tv_support.setTextColor(Color.parseColor("#999999"));
        }
        tv_support.setText(mRecord.getLikeNumber() + "");
        tv_comment.setText(mRecord.getCommentNumber() + "");
        if (!TextUtils.isEmpty(mRecord.getLogType())) {
            if (mRecord.getLogType().contains("日志")) {
                headerView.setTitle("日志详情");
            } else {
                headerView.setTitle(mRecord.getLogType() + "详情");
            }
        }
        getCommentList();
        getSupportList();

        tv_viewcount.setText("浏览" + mRecord.getFavoriteNumber() + "次");
    }


    private void setOnEvent() {
        iv_support.setOnClickListener(new View.OnClickListener() {
            private int likeNumber;

            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mRecord.getUuid());
                post.setDataType("公告通知");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mRecord.getUuid());
                if (mRecord.isLikeNumber()) { //取消点赞
                    likeNumber = mRecord.getLikeNumber() - 1;
                    tv_support.setText(likeNumber + "");
                    support(post, mRecord);
                    mRecord.setLikeNumber(false);
                } else {
                    likeNumber = (mRecord.getLikeNumber() + 1);
                    tv_support.setText(likeNumber + "");
                    mRecord.setLikeNumber(true);
                    support(post, mRecord);
                }
                mRecord.setLikeNumber(likeNumber);
                if (mRecord.isLikeNumber()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
            }
        });
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw(mRecord);
            }
        });

        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                Intent intent = new Intent(LogInfoActivity.this, LogNewActivity.class);
                Bundle bundle = new Bundle();
                if (mRecord != null) {
                    bundle.putSerializable("logInfo", mRecord);
                }
                intent.putExtra("logInfomation", bundle);
                startActivity(intent);
                finish();
            }

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
//
        commentView.setOnCommentListener(new BottomCommentView.CommentListener() {
            @Override
            public void onComment(String count) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mRecord.getUuid());
                post.setDataType("日志");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mRecord.getUuid());
                post.setContent(count);
                commentView.comment(post);
            }
        });


        commentView.setOnCommentSuccessListener(new BottomCommentView.CommentSuccessListener() {
            @Override
            public void onCommentSuccess() {
                getCommentList();
                LogListActivity.isResume = true;
            }
        });
    }

    private void getCommentList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志评论列表 + "?dataId=" + mRecord.getUuid() + "&sortField=createTime&sort=desc";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                List<SupportAndCommentPost> list = null;
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(result), "data"), SupportAndCommentPost.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (list != null && list.size() > 0) {
                    tv_comment.setText(list.size() + "");
                    tv_nocomment.setVisibility(View.GONE);
                    lv_comment.setVisibility(View.VISIBLE);
                    lv_comment.setAdapter(getAdapter(list));
                } else {
                    tv_nocomment.setVisibility(View.VISIBLE);
                    lv_comment.setVisibility(View.GONE);
                }
            }
        });

    }


    /**
     * 获取点赞列表
     */
    public void getSupportList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志点赞列表 + "?dataId=" + mRecord.getUuid() + "&sortField=createTime";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    ll_support.setVisibility(View.VISIBLE);
                    String supportName = "";
                    for (SupportAndCommentPost post1 : list) {
                        supportName += dictionaryHelper.getUserNameById(post1.getCreatorId()) + ",";
                    }
                    if (supportName.length() > 0) {
                        supportName = supportName.substring(0, supportName.length() - 1);
                        support_name.setText(supportName);
                    }
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


    private void getLogInfo() {
        ProgressDialogHelper.show(context);
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态详情 + "?dataId=" + dynamic.getDataId() + "&dataType=" + dynamic.getDataType();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<WorkRecord> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), WorkRecord.class);
                    if (list != null && list.size() > 0) {
                        mRecord = list.get(0);
                    }
                    if (mRecord != null) {
                        initData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
                TextView tv_content = viewHolder.getView(R.id.tv_time_task_item);
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getCreatorId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, dictionaryHelper.getUserNameById(item.getCreatorId()));//点赞人姓名
                viewHolder.setTextValue(R.id.tv_time, ViewHelper.convertStrToFormatDateStr(item.getCreateTime(), "MM月dd日 HH:mm"));//点赞时间
                tv_content.setText(item.getContent());  //评论内容
            }
        };
    }

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 点赞
     *
     * @param post
     */
    private void support(SupportAndCommentPost post, final WorkRecord record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志点赞和取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                getSupportList();
                LogListActivity.isResume = true;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                getSupportList();
                LogListActivity.isResume = true;
            }
        });
    }


    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final WorkRecord record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志添加评论;
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                getCommentList();
                LogListActivity.isResume = true;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void popWiw(final WorkRecord item) {

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
                        post.setDataType("员工日志");
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
                    post.setDataType("员工日志");
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

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
}
