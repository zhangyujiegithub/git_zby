package com.biaozhunyuan.tianyi.space;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 部门空间
 */
@SuppressLint("NewApi")
public class DepartmentListFragment extends Fragment {
    public static boolean isResume;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1;
    private Demand<Space> demand;
//    private LinearLayout ll_bottom;
//    private EditText et_comment; //评论内容
//    private TextView btn_comment; //发表评论
    private List<Space> spaceList = new ArrayList<>();
    private CommanAdapter adapter;
    private LinearLayout ll_comment;
    private DictionaryHelper helper;
    private DictionaryHelper dictionaryHelper;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_departmentlist,null);
        dictionaryHelper = new DictionaryHelper(getActivity());
        initView(v);
        initDemand();
        initData();
        setTouchEvent();
        return v;
    }


    @Override
    public void onResume() {
        if(isResume){
            pageIndex = 1;
            getSpaceList();
            isResume = false;
        }
        super.onResume();
    }

    private void setTouchEvent() {

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getSpaceList();
            }
        });


        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSpaceList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SpaceListInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("spaceitem", (Space) adapter.getDataList().get(position - 1));
                intent.putExtra("spaceinfo", bundle);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        getSpaceList();
    }

    /**
     * 获取列表信息并展示
     */
    public void getSpaceList(){
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                spaceList = demand.data;
                lv.onRefreshComplete();
                if(pageIndex == 1){
                    adapter = getAdapter(spaceList);
                    lv.setAdapter(adapter);
                    pageIndex +=1;
                }else{
                    adapter.addBottom(spaceList, false);
                    lv.loadCompleted();
                    pageIndex +=1;
                }

            }
            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                lv.loadCompleted();
                try {
                    String status = JsonUtils.getStringValue(result, "Status");
                    String data = JsonUtils.getStringValue(result, "Data");
                    Toast.makeText(getActivity(),data,Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.部门空间;
        demand = new Demand<>(Space.class);
        demand.pageSize = 10;
//        demand.sort = "desc";
//        demand.sortField = "creationTime";
        demand.dictionaryNames = "postTypeChild.dict_zone_post_type,creatorId.base_staff,creatorDepartmentId.base_department";
        demand.src = url;
    }

    private void initView(View v) {
        helper = new DictionaryHelper(getActivity());
        lv = v.findViewById(R.id.lv_task_list);
//        ll_bottom = v.findViewById(R.id.ll_comment_share_bottom_pl);
//        et_comment = v.findViewById(R.id.et_input_bottom_comment);
//        btn_comment = v.findViewById(R.id.btn_share_comment);
    }

    private CommanAdapter<Space> getAdapter(List<Space> list) {
        return new CommanAdapter<Space>(list,getActivity(),R.layout.item_spacelist_new1){

            @Override
            public void convert(int position, final Space item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_creater_notice_item, dictionaryHelper.getUserNameById(item.getCreatorId())); //创建人名称
                viewHolder.setTextValue(R.id.tv_time_notice_item, ViewHelper.getDateStringFormat(item.getCreateTime())); //创建时间
                viewHolder.setTextValue(R.id.tv_content_notice_item, item.getTextPart()); //通知内容
                viewHolder.setUserPhotoById(R.id.iv_head_item_notice_list, helper.getUser(item.getCreatorId()));//创建人头像
                MultipleAttachView attachView = viewHolder.getView(R.id.multi_attach_notice_item);
                attachView.loadImageByAttachIds(item.getFileAttachmentIds());
                //新加: 点赞和评论
                LinearLayout ll_support = viewHolder.getView(R.id.nl_item_log_support);//点赞
                ll_comment = viewHolder.getView(R.id.nl_item_log_comment);
                ll_comment.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
//                        ll_bottom.setVisibility(View.VISIBLE);
//                        et_comment.requestFocus();
//                        hideShowSoft();
                        popWiw(item);
                    }
                });
                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("空间");
                        post.setDataId(item.getUuid());
                        if (item.isLike()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });

                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);

                if (item.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getActivity().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getActivity().getColor(R.color.color_support_text));
                }
                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");
//
//                btn_comment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String content = et_comment.getText().toString().trim();
//                        if (TextUtils.isEmpty(content)) {
//                            Toast.makeText(getActivity(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getCreatorId());
//                        post.setDataType("空间");
//                        post.setDataId(item.getUuid());
//                        post.setContent(content);
//                        comment(post, item);
//                    }
//                });
            }
        };
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
                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                space.setCommentNumber(space.getCommentNumber() + 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Log.e("tag","评论失败");
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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 点赞
     *
     * @param post
     * @param record
     */
    private void support(SupportAndCommentPost post, final Space record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLike(true);
                adapter.notifyDataSetChanged();
//                ll_bottom.setVisibility(View.GONE);
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
     * @param record
     */
    private void cancleSupport(SupportAndCommentPost post, final Space record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLike(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void popWiw(final Space space) {

        popWiw = new BaseSelectPopupWindow(getActivity(), R.layout.edit_data);
        // popWiw.setOpenKeyboard(true);
        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWiw.setFocusable(true);
        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWiw.setShowTitle(false);
        popWiw.setBackgroundDrawable(new ColorDrawable(0));
        InputMethodManager im = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final TextView send =  popWiw.getContentView().findViewById(
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
                        post.setToId(space.getCreatorId());
                        post.setDataType("空间");
                        post.setDataId(space.getUuid());
                        post.setContent(content);
                        comment(post, space);

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
                    post.setToId(space.getCreatorId());
                    post.setDataType("空间");
                    post.setDataId(space.getUuid());
                    post.setContent(content);
                    comment(post, space);

                    popWiw.dismiss();
                }
            }
        });

        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist,null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }




}
