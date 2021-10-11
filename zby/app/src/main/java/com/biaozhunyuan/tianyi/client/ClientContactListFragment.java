package com.biaozhunyuan.tianyi.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.helper.ZLServiceHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import java.util.List;

import okhttp3.Request;

/**
 * 客户联系记录列表Fragment
 */
public class ClientContactListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    // TODO: Rename and change types of parameters
    private String mClientId = "";

    private Context context;
    private CommanAdapter adapter;
    private List<Contact> recordList;
    private String dictionary = "";
    private DictionaryHelper helper;
    //    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1; //当前页
    private Demand<Contact> demand;
    public static boolean isResume = false;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private ZLServiceHelper zlServiceHelper = new ZLServiceHelper();


    public ClientContactListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Id 编号
     * @return A new instance of fragment ClientContactListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientContactListFragment newInstance(String Id) {
        ClientContactListFragment fragment = new ClientContactListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_contact_list, container, false);
        context = getActivity();
        helper = new DictionaryHelper(context);
        isResume = false;
        initViews(view);
        initDemand();
        getWorkList();
        setOnEvent();
        return view;
    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.项目联系记录 + "?customerId=" + mClientId;
        demand = new Demand(Contact.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "projectId.crm_project,customerId.crm_customer,stage.dict_contact_stage,contactWay.dict_contact_way";
        demand.src = url;
    }

    @Override
    public void onResume() {
        if (isResume) {
            lv.startRefresh();
            pageIndex = 1;
            getWorkList();
            isResume = false;
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initViews(View view) {
        lv = (PullToRefreshAndLoadMoreListView) view.findViewById(R.id.lv_fragment_client_contact_list);
    }

    private void getWorkList() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                recordList = demand.data;
                try {
                    for (Contact project : recordList) {
                        project.setStageName(demand.getDictName(project,"stage"));
                        project.setCustomerName(demand.getDictName(project,"customerId"));
                        project.setProjectName(demand.getDictName(project,"projectId"));
                        project.setContactWayName(demand.getDictName(project,"contactWay"));
                    }
                }catch (Exception e){

                }
                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(recordList);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(recordList, false);
                    if (recordList != null && recordList.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;

                dictionary = demand.dictionary;

                if (recordList != null) {
                    for (Contact contact : recordList) {
                        try {
                            contact.setCustomerName(demand.getDictName(contact,"customerId"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

//
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        if (position > 1) {
//                            Contact record = (Contact) adapter.getDataList().get(position - 1);
//                            Intent intent = new Intent(getActivity(), ContactNewActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("contactInfo", record);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                lv.onRefreshComplete();
                lv.loadCompleted();
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
        }
    }

    private void setOnEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Contact record = (Contact) adapter.getDataList().get(position - 1);
                    record.setHiddenMore(true);
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contactInfo", record);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        /**
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getWorkList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getWorkList();
            }
        });
    }

    private CommanAdapter<Contact> getAdapter(
            List<Contact> gridItems) {
        return new CommanAdapter<Contact>(gridItems, context,
                R.layout.item_contract_client_list) {
            @SuppressLint("NewApi")
            @Override
            public void convert(int position, final Contact item,
                                BoeryunViewHolder viewHolder) {
                MultipleAttachView view = viewHolder.getView(R.id.attach_item_contact);
                if(TextUtils.isEmpty(item.getAttachment())){
                    view.setVisibility(View.GONE);
                }else{
                    view.setVisibility(View.VISIBLE);
                }
                view.loadImageByAttachIds(item.getAttachment());
                String name = helper.getUserNameById(item.getAdvisorId());
//                if (!TextUtils.isEmpty(item.getCustomerName())) {
//                    name += "(" + item.getCustomerName() + ")";
//                }
                viewHolder.setTextValue(R.id.tv_name_contact_item, name);
                viewHolder.setUserPhotoById(R.id.head_item_contact_list,  helper.getUser(item.getAdvisorId()));
                if(!TextUtils.isEmpty(item.getContactTime()) && item.getContactTime().contains(" 00:00:00")){
                    viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd"));
                }else {
                    viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd HH:mm"));
                }
                viewHolder.setTextValue(R.id.content_contact_list, item.getContent());
                TextView tv_status = viewHolder.getView(R.id.tv_status_item_contact);
                if(!TextUtils.isEmpty(item.getStageName())){
                    tv_status.setVisibility(View.VISIBLE);
                    viewHolder.setTextValue(R.id.tv_status_item_contact,item.getStageName());
                } else {
                    tv_status.setVisibility(View.GONE);
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
                    tv_support.setTextColor(getActivity().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getActivity().getColor(R.color.color_support_text));
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
