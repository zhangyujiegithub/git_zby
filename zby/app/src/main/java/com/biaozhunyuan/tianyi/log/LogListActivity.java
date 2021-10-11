package com.biaozhunyuan.tianyi.log;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.WorkRecord;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.gyf.barlibrary.ImmersionBar;
import com.loonggg.weekcalendar.view.WeekCalendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.utils.JsonUtils.ConvertJsonToList;

/**
 * Created by 王安民 on 2017/8/8.
 * <p>
 * 日志列表
 */

public class LogListActivity extends BaseActivity {

    public static boolean isResume = false;
    private Context context;
    private CommanAdapter adapter;
    private List<WorkRecord> recordList;
    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1; //当前页
    private Demand<WorkRecord> demand;
    private BoeryunSearchView searchView;
    private WorkRecord currentItem;

    /**
     * 过滤日志类型
     */
    private PopupWindow popupWindow;
    private LinearLayout llLog;
    private LinearLayout llWeekLog;
    private LinearLayout llMonthLog;
    private LinearLayout llNextWeekPlane;
    private LinearLayout llNextMonthPlane;

    /**
     * 顶部过滤
     */
    private HorizontalScrollView scrollView;
    private LinearLayout ll_staff;
    private WeekCalendar weekCalendar;
    //    private List<User> userList;

    private RelativeLayout rl_back_calander;
    private RelativeLayout rl_show_calander;
    private RelativeLayout rl_hide_calander;
    private ImageView iv_show_calander;
    private ImageView iv_hide_calander;
    private TextView tv_current_time;
    private ImageView iv_hide_head;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private String[] categorys = new String[]{"普通日志", "周总结", "月总结", "下周目标", "下月目标"};
//    private LinearLayout ll_bottom_comment;
//    private EditText et_comment;
//    private Button btn_comment;

    private int calanderType = 0;//日历的状态：0=显示，1=隐藏   默认为显示状态
    private boolean isHeadShow = false;
    private List<User> users;
    private int lastPosition = -1;
    private boolean isSelect = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤
    private DictionaryHelper dictionaryHelper;
    private String userNameById;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).fitsSystemWindows(true).init();
        setContentView(R.layout.activity_log_list);
        initData();
        initViews();
        initStaffHeader();
        initDemand();
        initPopwindow();
        getLogList();
        setEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            getLogList();
            isResume = false;
        }
    }

    private void initData() {
        dictionaryHelper = new DictionaryHelper(this);
        userNameById = dictionaryHelper.getUserNameById(Global.mUser.getUuid());
        context = getBaseContext();
        recordList = new ArrayList<WorkRecord>();
    }


    private void initPopwindow() {
        View view = View.inflate(this, R.layout.popup_select_product_status, null);
        popupWindow = new PopupWindow(view, (int) ViewHelper.dip2px(context, 150), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        llLog = view.findViewById(R.id.ll_log);
        llWeekLog = view.findViewById(R.id.ll_week_log);
        llMonthLog = view.findViewById(R.id.ll_month_log);
        llNextWeekPlane = view.findViewById(R.id.ll_next_week_plane);
        llNextMonthPlane = view.findViewById(R.id.ll_next_month_plane);

        llLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogType(0);
            }
        });

        llWeekLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogType(1);
            }
        });

        llMonthLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogType(2);
            }
        });

        llNextWeekPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogType(3);
            }
        });

        llNextMonthPlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogType(4);
            }
        });
    }

    private void setLogType(int position) {
        popupWindow.dismiss();
        pageIndex = 1;
        demand.key = "logType";
        demand.value = categorys[position];
        if (position == 0) {
            headerView.setTitle("普通日志");
        } else {
            headerView.setTitle(categorys[position]);
        }
        getLogList();
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_log_list);
        lv = (PullToRefreshAndLoadMoreListView) findViewById(R.id.lv_log_list);
        View view = View.inflate(context, R.layout.header_worklog, null);
        ll_staff = (LinearLayout) view.findViewById(R.id.ll_user_root_task_day_view);
        searchView = (BoeryunSearchView) view.findViewById(R.id.search_view);
        searchView.setHint("请输入员工名称搜索");
        scrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView_task_day_view);
        weekCalendar = (WeekCalendar) view.findViewById(R.id.weekcalendar_task_day_view);
        tv_current_time = (TextView) view.findViewById(R.id.tv_current_time);
        iv_hide_calander = (ImageView) view.findViewById(R.id.iv_hide_calander);
        iv_show_calander = (ImageView) view.findViewById(R.id.iv_home_show_calander);
        rl_show_calander = (RelativeLayout) view.findViewById(R.id.rl_home_calander);
        rl_hide_calander = (RelativeLayout) view.findViewById(R.id.rl_home_yearmonth);
        rl_back_calander = (RelativeLayout) view.findViewById(R.id.rl_calendar_month);
        iv_hide_head = (ImageView) view.findViewById(R.id.iv_hide_head_log_List);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        iv_hide_head.setVisibility(View.GONE);
        lv.addHeaderView(view);

        calanderType = 0;
        showCanlender();

        scrollView.setVisibility(View.GONE);
        String time = ViewHelper.getDateToday("yyyy年MM月dd日");

//        currentTime = time;

//        String yue = time.substring(5, 7);
//        String ri = time.substring(8, 10);
//        if (yue.substring(0, 1).equals("0")) {
//            yue = yue.substring(1, 2);
//        }
//        if (ri.substring(0, 1).equals("0")) {
//            ri = ri.substring(1, 2);
//        }
//        time = time.substring(0, 4) + "年" + yue + "月" + ri + "日";
        tv_current_time.setText(time);
    }


    private void showCanlender() {
        if (calanderType == 0) {
            rl_show_calander.setVisibility(View.VISIBLE);
            rl_hide_calander.setVisibility(View.GONE);
            iv_hide_calander.setImageResource(R.drawable.arrow_up);
            calanderType = 1;
        } else if (calanderType == 1) {
            rl_show_calander.setVisibility(View.GONE);
            rl_hide_calander.setVisibility(View.VISIBLE);
            iv_hide_calander.setImageResource(R.drawable.arrow_down);
            calanderType = 0;
        }
    }


    private void setEvent() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                if (!TextUtils.isEmpty(currentTime)) {
                    demand.starttime = currentTime + " 00:00:00";
                    demand.endtime = currentTime + " 23:59:59";
                } else {
                    demand.starttime = "";
                    demand.endtime = "";
                }
                getLogList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!TextUtils.isEmpty(currentTime)) {
                    demand.starttime = currentTime + " 00:00:00";
                    demand.endtime = currentTime + " 23:59:59";
                } else {
                    demand.starttime = "";
                    demand.endtime = "";
                }
                demand.pageIndex = pageIndex;
                getLogList();
            }
        });
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {
                setBackgroundAlpha(0.5f);
                popupWindow.showAsDropDown(headerView.ivFilter);
            }

            @Override
            public void onClickSaveOrAdd() {  //新建日志
                Intent intent = new Intent(LogListActivity.this, LogNewActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                lv.startRefresh();
                pageIndex = 1;
                demand.keyword = "";
                getLogList();
            }

            @Override
            public void OnClick() {

            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });

        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                lv.startRefresh();
                pageIndex = 1;
                if (!TextUtils.isEmpty(currentTime)) {
                    demand.starttime = currentTime + " 00:00:00";
                    demand.endtime = currentTime + " 23:59:59";
                } else {
                    demand.starttime = "";
                    demand.endtime = "";
                }
                demand.keyword = str;
                getLogList(false);
            }
        });


        /**
         * 隐藏/展示日历
         */
        rl_back_calander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCanlender();
            }
        });

        iv_hide_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHeadShow) {
                    scrollView.setVisibility(View.GONE);
                    isHeadShow = false;
                    iv_hide_head.setImageResource(R.drawable.icon_show_head);
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    isHeadShow = true;
                    iv_hide_head.setImageResource(R.drawable.icon_hide_head);
                }
            }
        });

        weekCalendar.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                currentTime = time;
                pageIndex = 1;
                demand.starttime = currentTime + " 00:00:00";
                demand.endtime = currentTime + " 23:59:59";
                getLogList();
            }
        });
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志过滤 + "?compatible=1";
        demand = new Demand<>(WorkRecord.class);
        demand.pageSize = 10;
        demand.pageIndex = pageIndex;
        demand.sort = "desc";
        demand.sortField = "lastUpdateTime";
        demand.dictionaryNames = "creatorId.base_staff";
        demand.starttime = "";
        demand.endtime = "";
        demand.keyword = "";
        demand.src = url;
    }


    /**
     * 加载列表
     */
    private void getLogList() {
        getLogList(true);
    }

    /**
     * 加载列表
     *
     * @param isShowPro 是否显示加载框
     */
    private void getLogList(boolean isShowPro) {
        demand.pageIndex = pageIndex;
        if (isShowPro) {
            ProgressDialogHelper.show(LogListActivity.this);
        }
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                recordList = demand.data;

                for (WorkRecord task : demand.data) {
                    try {
                        task.setCreatorName(demand.getDictName(task, "creatorId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                        if (position > 1) {
                            WorkRecord record = (WorkRecord) adapter.getDataList().get(position - 2);

                            Intent intent = new Intent(LogListActivity.this, LogInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("logInfo", record);
                            intent.putExtra("logInfoExtras", bundle);
                            startActivity(intent);
                        }
                    }
                });
                ProgressDialogHelper.dismiss();
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
     * 初始化头布局过滤员工
     */
    private void initStaffHeader() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.下属员工 + "?staffId=" + Global.mUser.getUuid();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                users = ConvertJsonToList(response, User.class);

                if (users != null) {
                    for (int i = 0; i < users.size(); i++) {
                        View view1 = View.inflate(context, R.layout.item_head_view, null);
                        TextView tvName = (TextView) view1.findViewById(R.id.tv_name_header_view);
                        CircleImageView view2 = (CircleImageView) view1.findViewById(R.id.head_header_view);
                        tvName.setText(users.get(i).getName());
                        ImageUtils.displyImageById(new DictionaryHelper(context).getUserPhoto(users.get(i).getUuid()), view2);
                        ll_staff.addView(view1);
                    }

                    for (int i = 0; i < users.size(); i++) {
                        final int finalI = i;
                        ll_staff.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lastPosition != -1) {
                                    ll_staff.getChildAt(lastPosition).setBackgroundColor(Color.TRANSPARENT);
                                }

                                if (!isSelect) {  //未选中状态，点击选中，设置背景为灰色，条件为选中的员工
                                    isSelect = true;
                                    ll_staff.getChildAt(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                                } else {    //选中状态下，点击分两种情况，点击别的位置，取消背景，点击的位置设为灰色，条件为选中的员工；点击原来的位置，取消背景，条件为空。
                                    if (lastPosition != -1) {
                                        if (lastPosition == finalI) {
                                            isSelect = false;
                                            ll_staff.getChildAt(finalI).setBackgroundColor(Color.TRANSPARENT);
                                        } else {
                                            isSelect = true;
                                            ll_staff.getChildAt(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                                        }
                                    }
                                }

                                /**
                                 * 点击头像过滤
                                 */
                                if (isSelect) { //选中状态
                                    currentId = users.get(finalI).getUuid();
                                } else { //未选中状态默认为当前用户id
                                    currentId = Global.mUser.getUuid();
                                }
                                lastPosition = finalI;
                                demand.src = Global.BASE_JAVA_URL + GlobalMethord.日志过滤 + "?date=" + currentTime + "&uuid=" + currentId + "&compatible=1";
                                pageIndex = 1;
                                getLogList();
                            }
                        });
                    }
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

    private CommanAdapter<WorkRecord> getAdapter(
            List<WorkRecord> gridItems) {
        return new CommanAdapter<WorkRecord>(gridItems, context,
                R.layout.item_log_list) {

            @Override
            public void convert(int position, final WorkRecord item,
                                BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name_log_item, item.getCreatorName());
                Date date = ViewHelper.formatStrToDateAndTime(item.getLastUpdateTime(), "yyyy-MM-dd kk:mm:ss");
                viewHolder.setTextValue(R.id.tv_time_log_item, ViewHelper.convertStrToFormatDateStr(item.getLastUpdateTime(), "yyyy-MM-dd HH:mm"));
                viewHolder.setTextValue(R.id.content_log_list, item.getContent());
                MultipleAttachView attach = viewHolder.getView(R.id.attach_view);
                if (!StrUtils.isNullOrEmpty(item.getAttachmentIds())) {
                    attach.setVisibility(View.VISIBLE);
                    attach.loadImageByAttachIds(item.getAttachmentIds());
                } else {
                    attach.setVisibility(View.GONE);
                    attach.loadImageByAttachIds("");
                }
                viewHolder.setUserPhoto(R.id.head_item_log_list, item.getCreatorId());//创建人头像


                LinearLayout ll_support = viewHolder.getView(R.id.ll_item_log_support);
                final LinearLayout ll_comment = viewHolder.getView(R.id.ll_item_log_comment);


                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);

                if (item.isLikeNumber()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.statusbar_mine));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(Color.parseColor("#999999"));
                }

                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");


                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("日志");
                        post.setDataId(item.getUuid());
                        if (item.isLikeNumber()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });


                ll_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentItem = item;
                        popWiw(currentItem);
                    }
                });
            }
        };
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
                Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLikeNumber(true);
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


    /**
     * 取消点赞
     *
     * @param post 要取消点赞的实体的ID
     */
    private void cancleSupport(SupportAndCommentPost post, final WorkRecord record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志点赞和取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLikeNumber(false);
                adapter.notifyDataSetChanged();
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
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                record.setCommentNumber(record.getCommentNumber() + 1);
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

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void popWiw(final WorkRecord log) {

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
                        post.setToId(log.getCreatorId());
                        post.setDataType("日志");
                        post.setDataId(log.getUuid());
                        post.setContent(content);
                        comment(post, log);

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
                    post.setToId(log.getCreatorId());
                    post.setDataType("日志");
                    post.setDataId(log.getUuid());
                    post.setContent(content);
                    comment(post, log);

                    popWiw.dismiss();
                }
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setOnCancleSearch(false);
    }
}
