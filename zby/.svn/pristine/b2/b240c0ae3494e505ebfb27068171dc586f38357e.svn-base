package com.biaozhunyuan.tianyi.space;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
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
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;

/**
 * 空间列表详情页
 */

public class SpaceListInfoActivity extends AppCompatActivity {

    private Space space;
    private BoeryunHeaderView headerView;
    private DictionaryHelper helper;
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
    private NoScrollListView lv;

    private String supportUser = "";
    private TextView tv_supportuser;
    private LinearLayout ll_support;
    private TextView tv_comment;
    private ImageView iv_support;
    private TextView tv_support;
    private TextView tv_nocomment;
    private MultipleAttachView attach;
    private ImageView iv_connment;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private TextView tv_viewcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_list_info);
        ImmersionBar.with(this).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).fitsSystemWindows(true).init();
        helper = new DictionaryHelper(this);
        initViews();
        initIntentData();
        setOnEvent();
        getCommentList();
        getSupportList();
    }

    private void getSupportList() {
        SupportListPost post = new SupportListPost();
        post.setDataType("帖子");
        post.setDataId(space.getUuid());
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

    private void getCommentList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志评论列表 + "?dataId=" + space.getUuid() + "&sortField=createTime&sort=desc";
        ;

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
                    tv_nocomment.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setAdapter(getAdapter(list));
                } else {
                    tv_nocomment.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
                }
            }
        });

    }

    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
//                WebView webView = viewHolder.getView(R.id.webview);
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getCreatorId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNameById(item.getCreatorId()));//点赞人姓名
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.getSettings().setSupportZoom(false);
//                webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        webView.loadUrl(url);
//                        return true;
//                    }
//                });
//                String htmlContent = "";//item.getContent();
//                htmlContent = htmlContent + "<script type=\"text/javascript\">" +
//                        "var imgs = document.getElementsByTagName('img');" + // 找到table标签
//                        "for(var i = 0; i<imgs.length; i++){" +  // 逐个改变
//                        "imgs[i].style.width = '100%';" +  // 宽度改为100%
//                        "imgs[i].style.height = 'auto';" +
//                        "}" +
//                        "</script>";
//                htmlContent = getHtmlData(item.getContent());
//                webView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);

                TextView tv_content = viewHolder.getView(R.id.tv_time_task_item);
                viewHolder.setTextValue(R.id.tv_time_task_item, ViewHelper.convertStrToFormatDateStr(item.getTime(), "MM月dd日 HH:mm"));//点赞时间
                if (Build.VERSION.SDK_INT >= 24)
                    tv_content.setText(Html.fromHtml(item.getContent(), Html.FROM_HTML_MODE_COMPACT));
                else
                    tv_content.setText(Html.fromHtml(item.getContent()));

            }
        };
    }

    private void setOnEvent() {
        iv_connment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw(space);
            }
        });

        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        commentView.setOnSupportSuccessListener(new BottomCommentView.SupportSuccessListener() {
            @Override
            public void onSupportSuccess() { //点赞成功，重新加载点赞列表
                getSupportList();
                PersonalListFragment.isResume = true;
            }
        });

        commentView.setOnCommentListener(new BottomCommentView.CommentListener() {
            @Override
            public void onComment(String count) {
                comment(count);
            }
        });

        iv_support.setOnClickListener(new View.OnClickListener() {
            private int likeNumber;

            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(space.getUuid());
                post.setDataType("帖子");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(space.getUuid());
                if (space.isLike()) { //取消点赞
                    likeNumber = space.getLikeNumber() - 1;
                    tv_support.setText(likeNumber + "");
                    commentView.cancleSupport(post);
                    space.setLike(false);
                } else {
                    likeNumber = (space.getLikeNumber() + 1);
                    tv_support.setText(likeNumber + "");
                    space.setLike(true);
                    commentView.support(post);
                }
                space.setLikeNumber(likeNumber);
                if (space.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
                PersonalListFragment.isResume = true;
                DepartmentListFragment.isResume = true;
                ComponyListFragment.isResume = true;
            }
        });


    }

    private void initViews() {
        tv_viewcount = findViewById(R.id.tv_viewcount);
        iv_connment = findViewById(R.id.iv_comment);
        attach = findViewById(R.id.multi_attach_notice_item);
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
        lv = findViewById(R.id.spaceinfo_listview);
        tv_supportuser = findViewById(R.id.tv_support_user);
        ll_support = findViewById(R.id.ll_support_list);

        iv_support = findViewById(R.id.iv_support);
        tv_support = findViewById(R.id.tv_support);
        tv_comment = findViewById(R.id.tv_comment);
        tv_nocomment = findViewById(R.id.tv_nocomment);

    }

    /**
     * 评论
     *
     * @param content
     */
    public void comment(String content) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志添加评论;
        hideShowSoft();

        JSONObject ob = new JSONObject();
        try {
            ob.put("dataType", "帖子");
            ob.put("dataId", space.getUuid());
            ob.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, ob, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                tv_comment.setText((space.getCommentNumber() + 1) + "");
                getCommentList();
                SpaceListActivity.isResume = true;
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

    /**
     * 初始化intent数据
     */
    private void initIntentData() {
        if (getIntent().getExtras() != null) {
            space = (Space) getIntent().getBundleExtra("spaceinfo").getSerializable("spaceitem");
            if (space != null) {
                ininData();
            }
        }
    }

    private void ininData() {

        tv_time.setText(ViewHelper.getDateStringFormat(space.getCreateTime()));
        if (Build.VERSION.SDK_INT >= 24)
            tv_content.setText(Html.fromHtml(space.getTextPart(), Html.FROM_HTML_MODE_COMPACT));
        else
            tv_content.setText(Html.fromHtml(space.getTextPart()));
        tv_creator.setText(helper.getUserNameById(space.getCreatorId()));
        ImageUtils.displyUserPhotoById(getBaseContext(), space.getCreatorId(), iv_head);
//        attachView.loadImageByAttachIds(space.getFileAttachmentIds());

        commentView.setIsLike(space.isLike());

        tv_comment.setText(space.getCommentNumber() + "");
        tv_support.setText(space.getLikeNumber() + "");

        if (space.isLike()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
        }
        if (TextUtils.isEmpty(space.getFileAttachmentIds())) {
            attach.setVisibility(View.GONE);
        } else {
            attach.setVisibility(View.VISIBLE);
            attach.loadImageByAttachIds(space.getFileAttachmentIds());
        }

        tv_viewcount.setText("浏览" + space.getFavoriteNumber() + "次");
//        SupportListPost post = new SupportListPost();
//        post.setDataType("帖子");
//        post.setDataId(space.getUuid());
//        fragment = CommonFragment.newInstance(post);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.add(R.id.frame_comment_log, fragment);
//        transaction.commit();

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
    public void comment(SupportAndCommentPost post, final Space space) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
//        et_comment.setText("");
//        InputSoftHelper.hiddenSoftInput(getActivity(), et_comment);
//        ll_bottom.setVisibility(View.GONE);
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(SpaceListInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                space.setCommentNumber(space.getCommentNumber() + 1);
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

    private void popWiw(final Space item) {

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
                        post.setDataType("帖子");
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
                    post.setDataType("帖子");
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

    /**
     * WebView.loadData图片适配
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
