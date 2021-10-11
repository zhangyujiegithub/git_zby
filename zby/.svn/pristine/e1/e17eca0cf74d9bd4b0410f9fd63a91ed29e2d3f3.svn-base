package com.biaozhunyuan.tianyi.notice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import java.util.List;

import okhttp3.Request;

/**
 * Created by 王安民 on 2017/8/7.
 * <p>
 * 通知列表
 */

public class NoticeListActivity extends Activity {

    private PullToRefreshAndLoadMoreListView lv;
    private BoeryunHeaderView headerView;
    private BoeryunSearchView searchView;

    public static boolean isResume; // 是否在Resume中刷新
    private List<Notice> noticeList;
    private Context context;
    //    private CommanAdapter adapter;
    private MyAdapter myAdapter;
    private int pageIndex = 1; //当前页
    private Demand<Notice> demand;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框

//    private LinearLayout ll_bottom_comment;

    //    private List<ReturnDict> nameList = new ArrayList<ReturnDict>();
//    private List<ReturnDict> deptList = new ArrayList<ReturnDict>();
//    private EditText et_comments;
//    private LinearLayout ll_comment;
//    private Button bt;
    private DictionaryHelper helper;
    private int mPosition;

    private Notice currentItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        helper = new DictionaryHelper(context);
        initViews();
        getNoticeManager();
        initDemand();
        initData();
        setOnEvent();
    }

    private void getNoticeManager() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.功能模块权限;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (response.contains("公告通知管理")) {
                    headerView.setRightIconVisible(true);
                } else {
                    headerView.setRightIconVisible(false);
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

    @Override
    protected void onResume() {
        if (isResume) {
            ProgressDialogHelper.show(NoticeListActivity.this);
            pageIndex = 1;
            getWorkList();
            isResume = false;
        }
        super.onResume();
    }

    private void initData() {
        context = getBaseContext();
        ProgressDialogHelper.show(NoticeListActivity.this);
        getWorkList();
    }

    private void initViews() {
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.lv_notice_list);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_notice_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_notice_list);
//        bt = findViewById(R.id.btn_share_commentt);
//        ll_bottom_comment = findViewById(R.id.ll_comment_share_bottom);
//        et_comments = findViewById(R.id.et_input_bottom_comment);

        searchView.setHint("搜索标题、内容关键字");
    }

    private void setOnEvent() {
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
                startActivity(new Intent(context, NoticeNewActivity.class));
            }
        });

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ProgressDialogHelper.show(NoticeListActivity.this);
                pageIndex = 1;
                getWorkList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                ProgressDialogHelper.show(NoticeListActivity.this);
                getWorkList();
            }
        });

        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                lv.startRefresh();
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.通知列表;
                getWorkList();
            }

            @Override
            public void OnClick() {

            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                lv.startRefresh();
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.通知过滤 + "?whereData=" + str;
                getWorkList();
            }
        });


//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = et_comments.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    Toast.makeText(NoticeListActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                SupportAndCommentPost post = new SupportAndCommentPost();
//                post.setFromId(Global.mUser.getUuid());
//                post.setToId(currentItem.getCreatorId());
//                post.setDataType("公告通知");
//                post.setDataId(currentItem.getUuid());
//                post.setContent(content);
//                comment(post, currentItem);
//            }
//        });
    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.通知列表;
        demand = new Demand<>(Notice.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "creationTime";
        demand.dictionaryNames = "creatorId.base_staff,categoryId.oa_notice_category";
        demand.src = url;
    }


    /**
     * 获取列表信息并展示
     */
    private void getWorkList() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                noticeList = demand.data;

                for (Notice notice : demand.data) {
                    try {
                        notice.setCreatorName(demand.getDictName(notice, notice.getCreatorIDName()));
                        notice.setCreationDepartmentName(demand.getDictName(notice, notice.getcategoryIdName()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                lv.onRefreshComplete();
                if (pageIndex == 1) {
//                    adapter = getAdapter(noticeList);
//                    lv.setAdapter(adapter);
                    myAdapter = new MyAdapter(noticeList);
                    lv.setAdapter(myAdapter);
                } else {
//                    adapter.addBottom(noticeList, false);
                    myAdapter.addBottom(noticeList);
                    if (noticeList != null && noticeList.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            Intent intent = new Intent(context, NoticeInfoActivity.class);
                            Bundle bundle = new Bundle();
                            mPosition = position - 1;
                            Notice mNotice = (Notice) myAdapter.getItem(mPosition);
                            bundle.putSerializable("noticeItem", mNotice);
                            intent.putExtra("noticeInfo", bundle);
                            startActivityForResult(intent, RESULT_OK);
                        }
                    }
                });

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


    class MyAdapter extends BaseAdapter {

        private List<Notice> list;

        public MyAdapter(List<Notice> notices) {
            this.list = notices;
        }

        public void addBottom(List<Notice> list) {
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            final Notice item = list.get(position);
            if (convertView == null) {
                holder = new Holder();
                convertView = View.inflate(context, R.layout.item_notice_list,
                        null);
                holder.tv_name = convertView.findViewById(R.id.tv_creater_notice_item);
                holder.tv_tittle = convertView.findViewById(R.id.tv_title_notice_item);
                holder.tv_time = convertView.findViewById(R.id.tv_time_notice_item);
                holder.tv_content = convertView.findViewById(R.id.tv_content_notice_item);
                holder.tv_support = convertView.findViewById(R.id.tv_support_count_log_item);
                holder.tv_comment = convertView.findViewById(R.id.tv_comment_count_log_item);
                holder.iv_head = convertView.findViewById(R.id.iv_head_item_notice_list);
                holder.attachView = convertView.findViewById(R.id.multi_attach_notice_item);
                holder.ll_support = convertView.findViewById(R.id.nl_item_log_support);
                holder.ll_comment = convertView.findViewById(R.id.nl_item_log_comment);
                holder.iv_support = convertView.findViewById(R.id.iv_item_log_support);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.tv_tittle.setText(item.getTitle());
            holder.tv_name.setText(item.getCreatorName());
            holder.tv_time.setText(ViewHelper.getDateStringFormat(item.getCreationTime()));
            if (Build.VERSION.SDK_INT >= 24)
                holder.tv_content.setText(Html.fromHtml(item.getContent(), Html.FROM_HTML_MODE_COMPACT));
            else
                holder.tv_content.setText(Html.fromHtml(item.getContent()));
            ImageUtils.displyUserPhotoById(context, item.getCreatorId(), holder.iv_head);

            if (!StrUtils.isNullOrEmpty(item.getAttachmentIds())) {
                holder.attachView.setVisibility(View.VISIBLE);
                holder.attachView.loadImageByAttachIds(item.getAttachmentIds());
            } else {
                holder.attachView.setVisibility(View.GONE);
                holder.attachView.loadImageByAttachIds("");
            }

            if (item.isLike()) {
                holder.iv_support.setImageResource(R.drawable.icon_support_select);
            } else {
                holder.iv_support.setImageResource(R.drawable.icon_support);
            }
            holder.tv_support.setText(item.getLikeNumber() + "");
            holder.tv_comment.setText(item.getCommentNumber() + "");

            holder.ll_comment.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    currentItem = item;
                    popWiw(currentItem);
//                    ll_bottom_comment.setVisibility(View.VISIBLE);
//                    et_comments.requestFocus();
//                    hideShowSoft();
                }
            });

            //点赞/取消赞
            holder.ll_support.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SupportAndCommentPost post = new SupportAndCommentPost();
                    post.setFromId(Global.mUser.getUuid());
                    post.setToId(item.getCreatorId());
                    post.setDataType("公告通知");
                    post.setDataId(item.getUuid());
                    if (item.isLike()) { //取消点赞
                        cancleSupport(post, item);
                    } else { //点赞
                        support(post, item);
                    }
                }
            });
            return convertView;
        }

        class Holder {
            TextView tv_name;
            TextView tv_tittle;
            TextView tv_time;
            TextView tv_content;
            TextView tv_support;
            TextView tv_comment;
            CircleImageView iv_head;
            ImageView iv_support;
            MultipleAttachView attachView;
            LinearLayout ll_support;
            LinearLayout ll_comment;
        }
    }


//    private CommanAdapter<Notice> getAdapter(final List<Notice> list) {
//        return new CommanAdapter<Notice>(list, context, R.layout.item_notice_list) {
//
//            @Override
//            public void convert(int position, final Notice item, BoeryunViewHolder viewHolder) {
//                viewHolder.setTextValue(R.id.tv_title_notice_item, item.getTitle()); //通知标题
//                viewHolder.setTextValue(R.id.tv_creater_notice_item, item.getCreatorName()); //创建人名称
//                viewHolder.setTextValue(R.id.tv_time_notice_item, ViewHelper.getDateStringFormat(item.getCreationTime())); //创建时间
//                viewHolder.setTextValue(R.id.tv_content_notice_item, item.getContent()); //通知内容
//                viewHolder.setUserPhotoById(R.id.iv_head_item_notice_list, helper.getUserPhoto(item.getCreatorId()));
//                MultipleAttachView attachView = viewHolder.getView(R.id.multi_attach_notice_item);
//
//                if (!StrUtils.isNullOrEmpty(item.getAttachmentIds())) {
//                    attachView.loadImageByAttachIds(item.getAttachmentIds());
//                    attachView.setVisibility(View.VISIBLE);
//                } else {
//                    attachView.setVisibility(View.GONE);
//                    attachView.loadImageByAttachIds("");
//                }
//
//
//                LinearLayout ll_support = viewHolder.getView(R.id.nl_item_log_support);//dianzan
////                ll_comment = viewHolder.getView(R.id.nl_item_log_comment);
//
////                ll_comment.setOnClickListener(new View.OnClickListener() {
////                    public void onClick(View v) {
////                        currentItem = item;
////                        ll_bottom_comment.setVisibility(View.VISIBLE);
////                        et_comments.requestFocus();
////                        hideShowSoft();
////                    }
////                });
//
//                //点赞/取消赞
//                ll_support.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getCreatorId());
//                        post.setDataType("公告通知");
//                        post.setDataId(item.getUuid());
//                        if (item.isLike()) { //取消点赞
//                            cancleSupport(post, item);
//                        } else { //点赞
//                            support(post, item);
//                        }
//                    }
//                });
//
//
//                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
//                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
//                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);
//
//                if (item.isLike()) {
//                    iv_support.setImageResource(R.drawable.icon_support_select);
//                } else {
//                    iv_support.setImageResource(R.drawable.icon_support);
//                }
//                tv_support.setText(item.getLikeNumber() + "");
//                tv_comment.setText(item.getCommentNumber() + "");
//
//            }
//        };
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK){
//            Bundle bundle = data.getExtras();
//            Notice backNotice = (Notice) bundle.getSerializable("BACK_NOTICE");
//            if(backNotice!=null){
//
//
//            }
//
//
//        }
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
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                notice.setCommentNumber(notice.getCommentNumber() + 1);
                myAdapter.notifyDataSetChanged();
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
     * @param record
     */
    private void support(SupportAndCommentPost post, final Notice record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLike(true);
                myAdapter.notifyDataSetChanged();
//                ll_bottom_comment.setVisibility(View.GONE);
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
     * @param post   要取消点赞的实体的ID
     * @param record
     */
    private void cancleSupport(SupportAndCommentPost post, final Notice record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLike(false);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

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
        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setOnCancleSearch(false);
    }
}
