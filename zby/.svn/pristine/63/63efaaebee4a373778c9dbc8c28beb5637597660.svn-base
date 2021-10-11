package com.biaozhunyuan.tianyi.notice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.newuihome.BackLog;
import com.biaozhunyuan.tianyi.supportAndComment.CommonFragment;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.supportAndComment.SupportListPost;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BottomCommentView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/10.
 * <p>
 * 通知详情
 */

public class NoticeInfoActivity extends FragmentActivity {

    private Notice mNotice;
    private BackLog backlog;
    private Dynamic dynamic;
    private Context context;


    private BoeryunHeaderView headerView;

    private ImageView iv_status;
    private AvatarImageView iv_head;
    private TextView tv_creator;
    private MultipleAttachView attachView;

    private TextView tv_dept_name;
    private TextView tv_time;
    private TextView tv_name;
    private TextView tv_title;
    private TextView tv_content;
    private BottomCommentView commentView;
    private CommonFragment fragment;
    private Notice backNotice;
    private boolean FLAG = false;
    private DictionaryHelper dictionaryHelper;

    private BaseSelectPopupWindow popWiw;// 回复的 编辑框

    private TextView tv_support;
    private TextView tv_comment;
    private ImageView iv_support;
    private ImageView iv_comment;
    private TextView tv_nocomment;
    private NoScrollListView lv_comment;
    private LinearLayout ll_support;
    private TextView support_name;
    private TextView tv_viewcount;


//    private NoScrollListView lv_attach;
//    private TextView attach_count;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice_info);
        context = NoticeInfoActivity.this;
        dictionaryHelper = new DictionaryHelper(context);
        initViews();
        initIntentData();
        setOnEvent();
    }


    /**
     * 初始化intent数据
     */
    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("noticeInfo");
            if (bundle != null) {
                mNotice = (Notice) bundle.getSerializable("noticeItem");
                if (mNotice != null) {
                    initData();
                }
            }
            dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            backlog = (BackLog) getIntent().getSerializableExtra("backlogInfo");
            if (backlog != null) {
                getBackLogInfo();
            }
            if (dynamic != null) {
                ProgressDialogHelper.show(context);
                getNoticeInfo();
            }
        }
    }


    private void initViews() {
        tv_viewcount = findViewById(R.id.tv_viewcount);
        iv_status = (ImageView) findViewById(R.id.imageView_status_notice_info);
        tv_dept_name = (TextView) findViewById(R.id.dept_notice_info);
        tv_time = (TextView) findViewById(R.id.time_notice_info);
        tv_name = (TextView) findViewById(R.id.from_notice_info);
        tv_title = (TextView) findViewById(R.id.title_notice_info);
        tv_content = (TextView) findViewById(R.id.content_notice_info);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_notice_list);
        iv_head = (AvatarImageView) findViewById(R.id.iv_head_item_notice_info);
        tv_creator = (TextView) findViewById(R.id.tv_creater_notice_info);
        attachView = (MultipleAttachView) findViewById(R.id.attach_notice_info);
        commentView = findViewById(R.id.comment_log_info);

        iv_support = findViewById(R.id.iv_support);
        iv_comment = findViewById(R.id.iv_comment);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_support = (TextView) findViewById(R.id.tv_support);
        tv_nocomment = findViewById(R.id.tv_nocomment);
        lv_comment = findViewById(R.id.lv_comment);
        ll_support = findViewById(R.id.ll_support);
        support_name = findViewById(R.id.support_name);
//        lv_attach = (NoScrollListView) findViewById(R.id.lv_attach_notice_info);
//        attach_count = (TextView) findViewById(R.id.attach_count_notice_info);

    }


    private void setOnEvent() {
        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw(mNotice);
            }
        });
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                if (FLAG) {
                    onBackData();
                } else {
                    finish();
                }
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {
            }
        });


        commentView.setOnSupportListener(new BottomCommentView.SupportListener() {
            @Override
            public void onSupport() {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mNotice.getUuid());
                post.setDataType("公告通知");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mNotice.getUuid());
                if (mNotice.isLike()) { //取消点赞
                    commentView.cancleSupport(post);
                    mNotice.setLike(false);
                } else {
                    mNotice.setLike(true);
                    commentView.support(post);
                }
            }
        });

        commentView.setOnSupportSuccessListener(new BottomCommentView.SupportSuccessListener() {
            @Override
            public void onSupportSuccess() { //点赞成功，重新加载点赞列表
//                fragment.reloadSupport();
                getSupportList();
                NoticeListActivity.isResume = true;
                backNotice = mNotice;
                FLAG = true;
            }
        });

        commentView.setOnCommentListener(new BottomCommentView.CommentListener() {
            @Override
            public void onComment(String count) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mNotice.getUuid());
                post.setDataType("公告通知");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mNotice.getUuid());
                post.setContent(count);
                commentView.comment(post);
            }
        });


        commentView.setOnCommentSuccessListener(new BottomCommentView.CommentSuccessListener() {
            @Override
            public void onCommentSuccess() { //评论成功，重新加载点赞列表
//                fragment.reloadComment();
                NoticeListActivity.isResume = true;
                backNotice = mNotice;
                tv_comment.setText((mNotice.getCommentNumber() + 1) + "");
                getCommentList();
                FLAG = true;
            }
        });

//        tv_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), CalendarMainActivity.class);
//                startActivity(intent);
//            }
//        });
        iv_support.setOnClickListener(new View.OnClickListener() {
            private int likeNumber;

            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mNotice.getUuid());
                post.setDataType("公告通知");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mNotice.getUuid());
                if (mNotice.isLike()) { //取消点赞
                    likeNumber = mNotice.getLikeNumber() - 1;
                    tv_support.setText(likeNumber + "");
                    commentView.cancleSupport(post);
                    mNotice.setLike(false);
                } else {
                    likeNumber = (mNotice.getLikeNumber() + 1);
                    tv_support.setText(likeNumber + "");
                    mNotice.setLike(true);
                    commentView.support(post);
                }
                mNotice.setLikeNumber(likeNumber);
                if (mNotice.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.hanyaRed));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
            }
        });

    }

    private void initData() {
        String deptName = mNotice.getCreationDepartmentName();
        if (!TextUtils.isEmpty(deptName)) {
            if (deptName.contains("行政")) {
                iv_status.setBackgroundColor(getResources().getColor(R.color.notice_xingzheng));
            } else if (deptName.contains("财务")) {
                iv_status.setBackgroundColor(getResources().getColor(R.color.notice_caiwu));
            } else if (deptName.contains("人事")) {
                iv_status.setBackgroundColor(getResources().getColor(R.color.notice_renshi));
            } else {
                iv_status.setVisibility(View.INVISIBLE);
            }
        } else {
            iv_status.setVisibility(View.INVISIBLE);
        }


        tv_dept_name.setText(mNotice.getCreationDepartmentName());
        tv_time.setText(ViewHelper.getDateStringFormat(mNotice.getCreationTime()));
        tv_name.setText(dictionaryHelper.getUserNameById(mNotice.getCreatorId()));
        tv_title.setText(mNotice.getTitle());
        if (Build.VERSION.SDK_INT >= 24)
           tv_content.setText(Html.fromHtml(mNotice.getContent(), Html.FROM_HTML_MODE_COMPACT));
        else
            tv_content.setText(Html.fromHtml(mNotice.getContent()));


        tv_creator.setText(dictionaryHelper.getUserNameById(mNotice.getCreatorId()));
        ImageUtils.displyUserPhotoById(getBaseContext(), mNotice.getCreatorId(), iv_head);
        attachView.loadImageByAttachIds(mNotice.getAttachmentIds());

        if (mNotice.isLike()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
            tv_support.setTextColor(getResources().getColor(R.color.statusbar_mine));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
            tv_support.setTextColor(Color.parseColor("#999999"));
        }
        tv_support.setText(mNotice.getLikeNumber() + "");
        tv_comment.setText(mNotice.getCommentNumber() + "");
        getCommentList();
        getSupportList();

        if (!TextUtils.isEmpty(mNotice.getFavoriteNumber())) {
            tv_viewcount.setText("浏览" + mNotice.getFavoriteNumber() + "次");
        }
//        if (TextUtils.isEmpty(mNotice.getAttachmentIds())) {
//            attach_count.setText("暂无附件");
//        } else {
//            new AttachPlayHelper(NoticeInfoActivity.this, mNotice.getAttachmentIds(), lv_attach); //显示附件列表
//        }

//        commentView.setIsLike(mNotice.isLike());
//        SupportListPost post = new SupportListPost();
//        post.setDataId(mNotice.getUuid());
//        post.setDataType("公告通知");
//        fragment = CommonFragment.newInstance(post);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.frame_comment_log, fragment);
//        transaction.commit();

    }


    private void getNoticeInfo() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.动态详情 + "?dataId=" + dynamic.getDataId() + "&dataType=" + dynamic.getDataType();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Notice> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Notice.class);
                    if (list != null && list.size() > 0) {
                        mNotice = list.get(0);
                    }
                    if (mNotice != null) {
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

    public void getBackLogInfo() {
        mNotice = new Notice();
        mNotice.setCreationTime(backlog.getData().getLastUpdateTime());
        mNotice.setCreatorName(dictionaryHelper.getUserNameById(backlog.getData().getCreatorId()));
        mNotice.setTitle(backlog.getData().getTitle());
        mNotice.setContent(backlog.getData().getContent());
        mNotice.setCreatorId(backlog.getData().getCreatorId());
        mNotice.setAttachmentIds(backlog.getData().getAttachmentIds());
        mNotice.setLike(backlog.getData().isLike());
        mNotice.setLikeNumber(backlog.getData().getLikeNumber());
        mNotice.setCommentNumber(backlog.getData().getCommentNumber());

        initData();
    }


    private void getCommentList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论列表;
        SupportListPost post = new SupportListPost();
        post.setDataType("公告通知");
        if (mNotice != null) {
            post.setDataId(mNotice.getUuid());
        } else {
            post.setDataId(backlog.getData().getUuid());
        }

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
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


    /**
     * 获取点赞列表
     */
    public void getSupportList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞列表;
        SupportListPost post2 = new SupportListPost();
        post2.setDataType("公告通知");
        if (mNotice != null) {
            post2.setDataId(mNotice.getUuid());
        } else {
            post2.setDataId(backlog.getData().getUuid());
        }
        StringRequest.postAsyn(url, post2, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    ll_support.setVisibility(View.VISIBLE);
                    String supportName = "";
                    for (SupportAndCommentPost post1 : list) {
                        supportName += dictionaryHelper.getUserNameById(post1.getFromId()) + ",";
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
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
            }
        });
    }

    /**
     * 重写返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FLAG) {
                onBackData();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackData() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BACK_NOTICE", mNotice);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
                TextView tv_content = viewHolder.getView(R.id.tv_time_task_item);
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getFromId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, dictionaryHelper.getUserNameById(item.getFromId()));//点赞人姓名
                viewHolder.setTextValue(R.id.tv_time, ViewHelper.convertStrToFormatDateStr(item.getTime(), "MM月dd日 HH:mm"));//点赞时间
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
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Notice notice) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(NoticeInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                notice.setCommentNumber(notice.getCommentNumber() + 1);
                NoticeListActivity.isResume = true;
                getCommentList();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Log.e("tag", "评论失败");
            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void popWiw(final Notice item) {

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
                        post.setDataType("公告通知");
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
                    post.setDataType("公告通知");
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
