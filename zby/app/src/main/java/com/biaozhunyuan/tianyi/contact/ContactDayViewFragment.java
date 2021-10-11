package com.biaozhunyuan.tianyi.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.task.TasksCompletion;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.utils.ShapeUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.loonggg.weekcalendar.view.WeekCalendar;

import java.util.Date;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.utils.JsonUtils.ConvertJsonToList;

/**
 * Created by 王安民 on 2017/10/8.
 * 任务日视图
 */

public class ContactDayViewFragment extends Fragment {
    private ViewHolder mViewHolder;
    private Demand<Contact> demand;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1;
    private CommanAdapter<Contact> adapter;
    private String[] status = new String[]{"完成", "取消"}; //tab的标题
    private List<Contact> recordList;
    private DictionaryHelper helper;
    public static boolean isResume = false;
    private static final String CONTACT_DAY_VIEW_CHECK = "CONTACT_DAY_VIEW_CHECK";


    private List<TasksCompletion> users;
    private int lastPosition = -1;
    private boolean isSelect = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_day_view, null);
        mViewHolder = new ViewHolder(view);
        Date date = new Date();
        helper = new DictionaryHelper(getActivity());
        currentTime = ViewHelper.formatDateToStr(date, "yyyy-MM-dd");
        ProgressDialogHelper.show(getActivity());
        initDemand();
        getTaskList();

        setOnEvent();
        return view;
    }

    @Override
    public void onResume() {
        if (isResume) {
            lv.startRefresh();
            pageIndex = 1;
            getTaskList();
            isResume = false;
        }
        super.onResume();
    }

    private class ViewHolder {
        public View rootView;
        public HorizontalScrollView horizontalScrollView_task_day_view;
        public WeekCalendar weekcalendar_task_day_view;
        public LinearLayout ll_user_root_task_day_view;
        public LinearLayout ll_list_task_day_view;
        public LinearLayout ll_add_task;

        public EditText et_content_task_day_view;
        public TextView tv_publish_task_day_view;


        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_content_task_day_view = (EditText) rootView.findViewById(R.id.et_content_task_day_view);
            this.tv_publish_task_day_view = (TextView) rootView.findViewById(R.id.tv_publish_task_day_view);
            this.ll_list_task_day_view = (LinearLayout) rootView.findViewById(R.id.ll_list_task_day_view);
            this.ll_add_task = (LinearLayout) rootView.findViewById(R.id.ll_add_task_list);
            lv = (PullToRefreshAndLoadMoreListView) rootView.findViewById(R.id.lv_task_day_list);
            ll_add_task.setVisibility(View.GONE);

            View header = View.inflate(getActivity(), R.layout.header_task_list, null);
            this.horizontalScrollView_task_day_view = (HorizontalScrollView) header.findViewById(R.id.horizontalScrollView_task_day_view);
            this.weekcalendar_task_day_view = (WeekCalendar) header.findViewById(R.id.weekcalendar_task_day_view);
            this.ll_user_root_task_day_view = (LinearLayout) header.findViewById(R.id.ll_user_root_task_day_view);
            lv.addHeaderView(header);


        }
    }

    private void setOnEvent() {
        /**
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getTaskList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getTaskList();
            }
        });

        mViewHolder.weekcalendar_task_day_view.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                currentTime = time;
//                initStaffHeader();
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表 + "?from=" + currentTime + "&to=" + currentTime + "&advisorId=";
                getTaskList();
            }
        });
    }


    /**
     * 初始化头布局过滤员工
     */
    private void initStaffHeader() {
        final String url = Global.BASE_JAVA_URL + GlobalMethord.任务查看员工 + "?from=";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                users = ConvertJsonToList(response, TasksCompletion.class);
                generationTaskCompletionView(users);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }


    public void generationTaskCompletionView(final List<TasksCompletion> mList) {
        ProgressDialogHelper.dismiss();
        mViewHolder.ll_user_root_task_day_view.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            final TasksCompletion item = mList.get(i);
            final int pos = i;
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_user_and_progress, null);
            final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_user_and_progress_transpant, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name_avatar);
            CircleImageView view2 = (CircleImageView) view.findViewById(R.id.circularAvatar_task_user);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_task_progress);

            String valueBYkey = PreferceManager.getInsance().getValueBYkey(CONTACT_DAY_VIEW_CHECK);

            if (TextUtils.isEmpty(valueBYkey)) {
                if (item.getStaffId().equals(Global.mUser.getUuid())) {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                }
            } else {
                if (valueBYkey.equals(item.getStaffId())) {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg));
                }
            }

            rl.setVisibility(View.GONE);
            ImageUtils.displyImageById(item.getAvatar() + "", view2);
//            ImageUtils.displayImage(item.get头像图片(),view2,R.drawable.ico_default_user);
            if (TextUtils.isEmpty(item.getStaffName())) {
//                        view2.displayUserName(new DictionaryHelper(context).getUserNameById(user.get编号()));
                tvName.setText(new DictionaryHelper(getActivity()).getUserNameById(item.getStaffName()));
            } else {
                tvName.setText(item.getStaffName());
            }
            ProgressBar progressBar = (ProgressBar) view
                    .findViewById(R.id.pbar_task_completion);
            TextView tvCount = (TextView) view
                    .findViewById(R.id.tv_count_completion);

//            circularAvatarView.displayAvatar(item.get员工编号());
//            if (TextUtils.isEmpty(item.get员工姓名())) {
//                circularAvatarView.displayNameByUserId(item.get员工编号());
//            } else {
//                circularAvatarView.displayUserName(item.get员工姓名());
//            }
            progressBar.setMax(item.getTotal());
            progressBar.setProgress(item.getCompleted());
            tvCount.setText(item.getCompleted() + "/" + item.getTotal());


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialogHelper.show(getActivity());
                    PreferceManager.getInsance().saveValueBYkey(CONTACT_DAY_VIEW_CHECK,item.getStaffId());
//                    item.isChecked = !item.isChecked;
//                    if (item.isChecked) {
//                        view.setBackgroundColor(getResources().getColor(R.color.bg_list));
//                        mSelectCompletionMap.put(pos, item);
//                    } else {
//                        view.setBackgroundColor(getResources().getColor(R.color.transparent));
//                        mSelectCompletionMap.remove(pos);
//                    }
                    if (lastPosition != -1 && lastPosition != pos) {
                        mViewHolder.ll_user_root_task_day_view.getChildAt(lastPosition).setBackground(getResources().getDrawable(R.drawable.round_corners_5dp_blue));
                    }
//                    if (item.isChecked) {
//                        view.setBackground(getResources().getDrawable(R.drawable.round_corners_5dp_blue));
//                        currentId = item.getStaffId();
//                    } else {
//                        view.setBackground(getResources().getDrawable(R.drawable.round_corners_5dp_white));
//                        currentId = "";
//                    }
                    PreferceManager.getInsance().saveValueBYkey("selectUserID", item.getStaffId() + "");
                    lastPosition = pos;
                    pageIndex = 1;
                    currentId = item.getStaffId();
                    demand.src = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表 + "?from=" + currentTime + "&to= " + currentTime + "&advisorId=" + currentId;
                    getTaskList();
                }
            });
            mViewHolder.ll_user_root_task_day_view.addView(view);
            mViewHolder.ll_user_root_task_day_view.addView(view1);
        }
    }

    /**
     * 1为我执行的任务
     * 2为我委派的任务
     * 3为我参与的任务
     */
    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表 + "?from="+ currentTime + "&to=" + currentTime + "+&advisorId=";
        demand = new Demand<Contact>(Contact.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "contactTime";
        demand.dictionaryNames = "projectId.crm_project,customerId.crm_customer,stage.dict_contact_stage,contactWay.dict_contact_way";
        demand.src = url;
    }


    /**
     * 获取列表信息并展示
     */
    private void getTaskList() {
        ProgressDialogHelper.show(getActivity());
        initStaffHeader();
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                recordList = demand.data;

//
//                List<ReturnDict> dictList = JsonUtils.getDictByName(demand.dictionary, "customerId.crm_customer");
//                List<ReturnDict> stageList = JsonUtils.getDictByName(demand.dictionary, "stage.crm_stage");

//                for (Contact contact : recordList) {
//                    contact.setCustomerName(JsonUtils.getDictValueById(dictList, contact.getCustomerId()));
//                    contact.setStageName(JsonUtils.getDictValueById(stageList, contact.getStage()));
//                }
                try {
                    for (Contact project : recordList) {
                        project.setCustomerName(demand.getDictName(project,"customerId"));
                        project.setProjectName(demand.getDictName(project,"projectId"));
                        project.setStageName(demand.getDictName(project,"stage"));
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


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position>1){
                            Contact record = (Contact) adapter.getDataList().get(position - 2);

                            Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("contactInfo", record);
                            intent.putExtras(bundle);
                            startActivity(intent);
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
                lv.onRefreshComplete();
                lv.loadCompleted();
            }
        });


    }

    private CommanAdapter<Contact> getAdapter(List<Contact> gridItems) {
        return new CommanAdapter<Contact>(gridItems, getActivity(),
                R.layout.item_contract_list) {
            @SuppressLint("NewApi")
            @Override
            public void convert(int position, final Contact item,
                                BoeryunViewHolder viewHolder) {

                String name = helper.getUserNameById(item.getAdvisorId());
//                if (!TextUtils.isEmpty(item.getCustomerName())) {
//                    name += "(" + item.getCustomerName() + ")";
//                }
                viewHolder.setTextValue(R.id.tv_name_contact_item, item.getCustomerName());
                viewHolder.setTextValue(R.id.tv_advisor_contact_item, name);
                if(StrUtils.pareseNull(item.getContactTime()).contains(" 00:00:00")){
                    viewHolder.setTextValue(R.id.tv_time_contact_item,
                            ViewHelper.getDateStringFormat(item.getContactTime(),"yyyy-MM-dd"));
                }else {
                    viewHolder.setTextValue(R.id.tv_time_contact_item,
                            ViewHelper.getDateStringFormat(StrUtils.pareseNull(item.getContactTime()),"yyyy-MM-dd HH:mm"));
                }
                viewHolder.setTextValue(R.id.content_contact_list, item.getContent());
                viewHolder.setUserPhoto(R.id.head_item_contact_list, item.getAdvisorId());

                MultipleAttachView attachView = viewHolder.getView(R.id.attach_item_contact);

                if (!TextUtils.isEmpty(item.getAttachment())) {
                    attachView.setVisibility(View.VISIBLE);
                    attachView.loadImageByAttachIds(item.getAttachment());
                } else {
                    attachView.setVisibility(View.GONE);
                    attachView.loadImageByAttachIds("");
                }


                TextView tvStage = viewHolder.getView(R.id.tv_status_item_contact);

                if (!TextUtils.isEmpty(item.getStageName())) {
                    tvStage.setVisibility(View.VISIBLE);
                    tvStage.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getResources().getColor(R.color.hanyaRed), ViewHelper.dip2px(getActivity(), 5), 0));
                    tvStage.setText(item.getStageName());
                } else {
                    tvStage.setVisibility(View.GONE);
                }
                //评论
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
//                    tv_support.setTextColor(getActivity().getResources().getColor(R.color.color_support_text_like));
                    tv_support.setTextColor(Color.parseColor("#01E0DF"));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
//                    tv_support.setTextColor(getActivity().getResources().getColor(R.color.color_support_text));
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

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_contact_day_view,null), Gravity.BOTTOM
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
