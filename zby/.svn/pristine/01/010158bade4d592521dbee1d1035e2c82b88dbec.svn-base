package com.biaozhunyuan.tianyi.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.project.ProjectInfoActivity.PRSTR;

/**
 * 项目信息更新
 */

public class ProjectRecordFragment extends LazyFragment {

    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Contact> demand;
    private int pageIndex = 1;
    private CommanAdapter<Contact> adapter;
    private Project mProject;
    private DictionaryHelper helper;
    private Context mContext;
    public static boolean isReasum = false;
    private TextView nullData;
    private List<Contact> projectList = new ArrayList<>();
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_project_info, null);
        isReasum = false;
        getIntentData();
        initView(v);
        initData();
        getList();
        setOnTouch();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isReasum){
            pageIndex = 1;
            getList();
        }
    }

    private void setOnTouch() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectList.clear();
                pageIndex = 1;
                getList();
            }
        });
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Contact project = adapter.getItem(position - 1);
                    Intent intent = new Intent(mContext, AddRecordActivity.class);
                    intent.putExtra("project", project);
                    startActivity(intent);
                }
            }
        });
    }

    private void getIntentData() {
        if (getActivity().getIntent().getSerializableExtra("Project") != null) {
            mProject = (Project) getActivity().getIntent().getSerializableExtra("Project");
        }
    }

    private void initData() {
        mContext = getActivity();
        helper = new DictionaryHelper(mContext);

        String url = Global.BASE_JAVA_URL + GlobalMethord.项目联系记录 +"?projectId=" + mProject.getUuid();
        demand = new Demand<>(Contact.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "projectId.crm_project,stage.crm_project_stage,contactWay.dict_contact_way";
        demand.src = url;
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Contact> data = demand.data;
                    if(data.size()>0) {
                        for(Contact p : data){
                            projectList.add(p);
                        }
                        for (Contact project : data) {
                            project.setName(demand.getDictName(project, "projectId"));
                            project.setStageName(demand.getDictName(project, "stage"));
                            project.setContactWayName(demand.getDictName(project, "contactWay"));
                        }
                        lv.onRefreshComplete();
                        if (pageIndex == 1) {
                            adapter = getAdapter(data);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.addBottom(data, false);
                            lv.loadCompleted();
                        }
                        pageIndex += 1;
                        showOrHiddenList(true);
                    }else {
                        if(projectList.size()>0){
                            showOrHiddenList(true);
                        }else {
                            showOrHiddenList(false);
                        }
                    }
                } catch (Exception e) {
                    showOrHiddenList(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showOrHiddenList(false);
            }
        });

    }


    private void showOrHiddenList(boolean isShowList){
        lv.loadCompleted();
        if(isShowList){
            nullData.setVisibility(View.GONE);
        }else {
            nullData.setVisibility(View.VISIBLE);
        }
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        nullData = v.findViewById(R.id.tv_null_data);
        nullData.setText(PRSTR + Global.CONTACT_TITLE);
    }


    private CommanAdapter<Contact> getAdapter(List<Contact> gridItems) {
        return new CommanAdapter<Contact>(gridItems, getActivity(), R.layout.item_project_record_list) {
            @SuppressLint("NewApi")
            public void convert(int position, final Contact item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_contact_item, helper.getUserNameById(item.getAdvisorId()));
                viewHolder.setUserPhotoById(R.id.head_item_contact_list, helper.getUser(item.getAdvisorId()));
                if(item.getContactTime().contains(" 00:00:00")){
                    viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd"));
                }else {
                    viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd HH:mm"));
                }
                viewHolder.setTextValue(R.id.content_contact_list, item.getContent());
                MultipleAttachView view = viewHolder.getView(R.id.attach_item_contact);
                if(TextUtils.isEmpty(item.getAttachment())){
                    view.setVisibility(View.GONE);
                }else {
                    view.setVisibility(View.VISIBLE);
                }
                view.loadImageByAttachIds(item.getAttachment());
                if(!TextUtils.isEmpty(item.getStageName())){
                    viewHolder.setTextValue(R.id.tv_status_item_contact, item.getStageName());
                    viewHolder.getView(R.id.tv_status_item_contact).setVisibility(View.VISIBLE);
                }else {
                    viewHolder.getView(R.id.tv_status_item_contact).setVisibility(View.GONE);
                }

                viewHolder.getView(R.id.ll_item_log_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWiw(item);
                    }
                });

                LinearLayout ll_support = viewHolder.getView(R.id.ll_item_log_support);//点赞
                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                final TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);
                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("联系记录");
                        post.setDataId(item.getUuid());
                        if (item.isLike()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });

                if (item.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(Color.parseColor("#01E0DF"));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(Color.parseColor("#999999"));
                }
                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");
            }
        };
    }

    private void popWiw(final Contact item) {

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
                        post.setToId(item.getCreatorId());
                        post.setDataType("联系记录");
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
                    post.setDataType("联系记录");
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
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_client_contact_list,null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Contact space) {
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
    private void support(SupportAndCommentPost post, final Contact record) {
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
    private void cancleSupport(SupportAndCommentPost post, final Contact record) {
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

}
