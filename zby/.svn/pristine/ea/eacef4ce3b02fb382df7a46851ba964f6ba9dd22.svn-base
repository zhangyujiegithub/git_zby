package com.biaozhunyuan.tianyi.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.user.Latest;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.common.view.HorizontalRecentlySelectedStaffView;
import com.biaozhunyuan.tianyi.view.SMNOScrollListView;
import com.biaozhunyuan.tianyi.view.VoiceInputView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.j256.ormlite.dao.Dao;
import com.loonggg.weekcalendar.view.WeekCalendar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;

/**
 * Created by 王安民 on 2017/10/8.
 * 任务日视图
 */

public class TaskDayViewFragment extends Fragment {
    private HorizontalScrollView horizontalScrollView_task_day_view;
    private ViewHolder mViewHolder;
    private Demand<Task> demand;
    private SMNOScrollListView lv;
    public int pageIndex = 1;
    private CommanAdapter<Task> adapter;
    private CommanAdapter<Task> doneAdapter;
    private String[] status = new String[]{"完成", "取消"}; //tab的标题
    private DictionaryHelper helper;
    public static boolean isResume = false;

    private Context mContext;
    private TaskListActivity activity;
    private List<TasksCompletion> users;
    private int lastPosition = -1;
    private boolean isSelect = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤
    private View view;
    //    ArrayList<TasksCompletion> itemList = new ArrayList<>();
    private static final String TASK_DAY_VIEW_CHECK = "TASK_DAY_VIEW_CHECK";
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private LinearLayout ll_comment_bottom;
    private VoiceInputView inputView;
    private TextEditTextView etContent;
    private ImageView ivSend;
    private View bgView;
    private Task currentItem;
    private View line;
    private SMNOScrollListView lv_done;
    private User currentUser = Global.mUser;
    private View addView;
    //    private LinearLayout ll_bottom;
    private HorizontalRecentlySelectedStaffView staffView;
    private ORMDataHelper dataHelper;
    private List<Latest> recentList; //最近选择的员工

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_day_view, null);
        mContext = getActivity();
        activity = (TaskListActivity) mContext;
        mViewHolder = new ViewHolder(view);
        helper = new DictionaryHelper(mContext);
        recentList = new ArrayList<>();
        dataHelper = ORMDataHelper.getInstance(mContext);
        Date date = new Date();
        currentTime = ViewHelper.getDateString(date);
        createAddView();
        initDemand();
        ProgressDialogHelper.show(mContext);
        getTaskList();
        initViews();
//        initStaffHeader();
        setOnEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initViews() {
//        et_comments = view.findViewById(R.id.et_input_bottom_comment);
//        bt_send = view.findViewById(R.id.btn_share_commentt);
//        ll_bottom_comment = view.findViewById(R.id.ll_comment_share_bottom);

    }

    @Override
    public void onResume() {
        if (isResume) {
            pageIndex = 1;
            getTaskList();
            isResume = false;
//            initStaffHeader();
        }
        super.onResume();
    }

    private class ViewHolder {
        public View rootView;
        public HorizontalScrollView horizontalScrollView_task_day_view;
        public WeekCalendar weekcalendar_task_day_view;
        public LinearLayout ll_user_root_task_day_view, ll_item_log_comment, ll_item_log_support;
        public LinearLayout ll_list_task_day_view;
        public ImageView iv_item_log_comment, iv_item_log_support;
        public EditText et_content_task_day_view;
        public TextView tv_publish_task_day_view, tv_comment_count_log_item, tv_support_count_log_item;
        public SmartRefreshLayout refreshLayout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_content_task_day_view = (EditText) rootView.findViewById(R.id.et_content_task_day_view);
            this.tv_publish_task_day_view = (TextView) rootView.findViewById(R.id.tv_publish_task_day_view);
            this.ll_list_task_day_view = (LinearLayout) rootView.findViewById(R.id.ll_list_task_day_view);
            this.refreshLayout = rootView.findViewById(R.id.refreshLayout);

            line = rootView.findViewById(R.id.task_view_line);
            lv_done = rootView.findViewById(R.id.lv_done);

            refreshLayout.setReboundDuration(200);//回弹动画时长（毫秒）
            refreshLayout.setDragRate(0.7f);//显示下拉高度/手指真实下拉高度=阻尼效果
            refreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
            refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
            refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
            refreshLayout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
//            refreshLayout.setEnablePureScrollMode(true);//是否启用纯滚动模式
            lv =  rootView.findViewById(R.id.lv_task_day_list);
            ll_comment_bottom = (LinearLayout) rootView.findViewById(R.id.ll_home_add_task);
            inputView = rootView.findViewById(R.id.voice_input_view_home_add_task);
            etContent = rootView.findViewById(R.id.et_input_home_add_task);
            ivSend = rootView.findViewById(R.id.btn_home_add_task);
            bgView = rootView.findViewById(R.id.bg_view);
//            ll_bottom = rootView.findViewById(R.id.ll_bottom);
            staffView = rootView.findViewById(R.id.staff_view);
            inputView.setRelativeInputView(etContent);

            View header = View.inflate(mContext, R.layout.header_task_list, null);
            this.horizontalScrollView_task_day_view = (HorizontalScrollView) header.findViewById(R.id.horizontalScrollView_task_day_view);
            TaskDayViewFragment.this.horizontalScrollView_task_day_view = horizontalScrollView_task_day_view;
            this.weekcalendar_task_day_view = (WeekCalendar) header.findViewById(R.id.weekcalendar_task_day_view);
            this.ll_user_root_task_day_view = (LinearLayout) header.findViewById(R.id.ll_user_root_task_day_view);

//            View footer = LayoutInflater.from(mContext).inflate(R.layout.task_day_list_footerview, null);
//            line = footer.findViewById(R.id.task_view_line);
//            lv_done = footer.findViewById(R.id.lv_done);

            lv.addHeaderView(header);
//            lv.addFooterView(footer);
        }
    }

    private void setOnEvent() {
//        /**
//         * 查看更多
//         */
//        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                getTaskList();
//            }
//        });
//
//        /**
//         * 下拉刷新
//         */
//        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageIndex = 1;
//                getTaskList();
//                initStaffHeader();
//            }
//        });
        mViewHolder.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getTaskList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pageIndex = 1;
                getTaskList();
                initStaffHeader();
            }
        });

        mViewHolder.weekcalendar_task_day_view.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                currentTime = time;
//                String tommorrow = ViewHelper.ViewHelper.getTomorrow(new Date(currentTime));
                initStaffHeader();
                pageIndex = 1;
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?beginTime=" + currentTime + "&executorIds=" + currentId;
                getTaskList();
            }
        });


        /*inputView.setOnKeyBoardChangedListener(new VoiceInputView.OnKeyBoardChangedListener() {
            @Override
            public void onChanged(boolean isOpen) {
                if (!isOpen) {
                    ll_comment_bottom.setVisibility(View.GONE);
                    popWiw();
                } else {
                    ll_comment_bottom.setVisibility(View.VISIBLE);
                }
            }
        });*/
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputView.getVisibility() == View.GONE) {
                    inputView.setVisibility(View.VISIBLE);
                    staffView.setVisibility(View.VISIBLE);
                    bgView.setVisibility(View.VISIBLE);
                    inputView.openLastStatus();
                }
            }
        });
        etContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取当前界面可视部分
                ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = ((Activity) mContext).getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, heightDifference);
                inputView.setLayoutParams(params);
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask(etContent.getText().toString().trim(), currentUser);
            }
        });

//        inputView.setOnOpenListener(new VoiceInputView.onOpenListener() {
//            @Override
//            public void onOpen() {
////                ll_bottom.setVisibility(View.VISIBLE);
//                inputView.setVisibility(View.VISIBLE);
//                staffView.setVisibility(View.VISIBLE);
//                bgView.setVisibility(View.VISIBLE);
//            }
//        });

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffView.setVisibility(View.GONE);
                inputView.setVisibility(View.GONE);
                bgView.setVisibility(View.GONE);
                hideKeyBorad(etContent);
            }
        });
        staffView.setOnSelectedUserListener(new HorizontalRecentlySelectedStaffView.OnSelectedUserListener() {
            @Override
            public void onSelected(User user) {
                currentUser = user;
                PreferceManager.getInsance().saveValueBYkey(TASK_DAY_VIEW_CHECK,user.getUuid());
            }
        });



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(mContext, TaskInfoActivity.class);
                    Task task = adapter.getDataList().get(position - 1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtra("taskIntentInfo", bundle);
                    startActivity(intent);
                }
            }
        });
        lv_done.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, TaskInfoActivity.class);
                Task task = doneAdapter.getDataList().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskInfo", task);
                intent.putExtra("taskIntentInfo", bundle);
                startActivity(intent);
            }
        });
    }

    private void initStaffHeader() {
        initStaffHeader(null);
    }

    /**
     * 初始化头布局过滤员工
     */
    private void initStaffHeader(Latest latest) {
//        final String url = Global.BASE_JAVA_URL + GlobalMethord.任务查看员工 + "?from=" + currentTime + " 00:00:00&to=" + currentTime + " 23:59:59";
//        StringRequest.getAsyn(url, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                users = ConvertJsonToList(response, TasksCompletion.class);
//                generationTaskCompletionView(users);
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//        Latest currentLatest = ViewHelper.turnLatest(currentUser);
        Latest mine = ViewHelper.turnLatest(Global.mUser);
        recentList.clear();
        try {
            Dao<Latest, Integer> latestDao = dataHelper.getLatestDao();
            List<Latest> latests = latestDao.queryForAll();
            for (int i = 0; i < latests.size(); i++) { //先插入最近联系人
                Latest lat = latests.get(i);
                if (!removeDuplicate(lat)) {
                    if (latest != null && latest.getUuid().equals(lat.getUuid())) {
                        //如果有传进来一个latest对象 放到下面添加 添加到第一位
                    }
//                    else if (lat.getUuid().equals(currentUser.getUuid())){
//                        //当前选中的员工 放到下面添加 添加到第一位
//                    }
                    else {
                        if(lat.getUuid().equals(mine.getUuid())){
                            //自己放到下面添加 添加到第一位
                        } else {
                            recentList.add(lat);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (latest != null) { //添加到第一个位置
            recentList.add(0, latest);
        }
//        if (!removeDuplicate(currentLatest)) {
//            recentList.add(0, currentLatest);
//        }
        if (!removeDuplicate(mine)) {
            recentList.add(0, mine);
        }
        generationTaskCompletionView(recentList, latest);
    }

    //把list里的对象遍历一遍，用list.contain()，如果不存在就放入到另外一个list集合中
    public boolean removeDuplicate(Latest user) {
        boolean isRePeat = false;
        for (Latest user1 : recentList) {
            if (user.getUuid().equals(user1.getUuid())) {
                isRePeat = true;
                break;
            }
        }
        return isRePeat;
    }

    private void createAddView() {
        addView = LayoutInflater.from(mContext).inflate(R.layout.item_input_add, null);
        ImageView ivAdd = addView.findViewById(R.id.iv_item_input_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", true);
                intent.putExtra("title", "选择执行人");
                ((Activity) mContext).startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
            }
        });
    }

    public void generationTaskCompletionView(final List<Latest> mList, Latest latest) {

        mViewHolder.ll_user_root_task_day_view.removeAllViews();
//        itemList.clear();
        for (int i = 0; i < mList.size(); i++) {
            final Latest item = mList.get(i);
            final int pos = i;
            final View view = LayoutInflater.from(mContext).inflate(R.layout.item_task_user_and_progress, null);
            final View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_task_user_and_progress_transpant, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name_avatar);
            CircleImageView view2 = (CircleImageView) view.findViewById(R.id.circularAvatar_task_user);
            String valueBYkey = PreferceManager.getInsance().getValueBYkey(TASK_DAY_VIEW_CHECK);

            if (latest != null) { //如果传过来一个latest对象 则选中
                if (item.getUuid().equals(latest.getUuid())) {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg));
                }
            } else {
                if (TextUtils.isEmpty(valueBYkey)) {
                    if (item.getUuid().equals(Global.mUser.getUuid())) {
                        view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                    }
                } else {
                    if (valueBYkey.equals(item.getUuid())) {
                        view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                    } else {
                        view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg));
                    }
                }
            }

            ImageUtils.displyImageById(item.getAvatar(), view2);
            if (TextUtils.isEmpty(item.getUuid())) {
                tvName.setText(item.getName());
            } else {
                tvName.setText(item.getName());
            }
//            ProgressBar progressBar = (ProgressBar) view
//                    .findViewById(R.id.pbar_task_completion);
//            TextView tvCount = (TextView) view
//                    .findViewById(R.id.tv_count_completion);

//            progressBar.setMax(item.getTotal());
//            progressBar.setProgress(item.getCompleted());
//            tvCount.setText(item.getCompleted() + "/" + item.getTotal());


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialogHelper.show(getActivity());
                    PreferceManager.getInsance().saveValueBYkey(TASK_DAY_VIEW_CHECK, item.getUuid());
                    if (lastPosition != -1 && lastPosition != pos) {
                        mViewHolder.ll_user_root_task_day_view.getChildAt(lastPosition).setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                    }
                    currentUser = ViewHelper.turnUser(item);
                    lastPosition = pos;
                    pageIndex = 1;
//                    demand.src = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?beginTime=" + currentTime + "&executorIds=" + item.getUuid();
                    getTaskList();
                }
            });
//            itemList.add(item);
            mViewHolder.ll_user_root_task_day_view.addView(view);
            mViewHolder.ll_user_root_task_day_view.addView(view1); //分割线

            if (i == mList.size() - 1) {
                mViewHolder.ll_user_root_task_day_view.addView(addView);
            }
        }
    }

    /**
     * 1为我执行的任务
     * 2为我委派的任务
     * 3为我参与的任务
     */
    private void initDemand() {
        ;//+ "?ticket=1"
        demand = new Demand<Task>(Task.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "beginTime";
        demand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff,customerId.crm_customer";
    }

    /**
     * 获取列表信息并展示
     */
    private void getTaskList(Latest latest) {
        if (latest != null) {
            currentId = latest.getUuid();
            demand.src = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?beginTime=" + currentTime + "&executorIds=" + currentId;
        }
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?beginTime=" + currentTime + "&executorIds=" + currentUser.getUuid();
        demand.pageIndex = pageIndex;
        if(pageIndex == 1){
            ProgressDialogHelper.show(mContext);
        }
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                mViewHolder.refreshLayout.finishRefresh();
                mViewHolder.refreshLayout.finishLoadMore();
                List<Task> doneTasks = new ArrayList<>(); //已完成任务
                List<Task> tasks = new ArrayList<>();
                List<Task> taskList = demand.data;
                for (final Task task : demand.data) {
                    try {
                        task.setCreatorName(demand.getDictName(task, "creatorId"));
                        task.setExecutorNames(demand.getDictName(task, "executorIds"));
//                        task.setCustomerName(demand.getDictName(task, "customerId"));
                        task.setParticipantNames(new DictionaryHelper(mContext).getUserNamesById(task.getParticipantIds()));
                        task.setLike(task.isLike());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (taskList.size() > 0) {
                    for (Task task : taskList) {
                        if (TaskStatusEnum.已完成.getName().equals(task.getStatus())
                                ||TaskStatusEnum.已取消.getName().equals(task.getStatus()) ) {
                            doneTasks.add(task);
                        } else {
                            tasks.add(task);
                        }
                    }
                    if (doneTasks.size() > 0) {
                        line.setVisibility(View.VISIBLE);
                    } else {
                        line.setVisibility(View.GONE);
                    }
                } else {
                    line.setVisibility(View.GONE);
                }

//                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    doneAdapter = getDoneTaskAdapter(doneTasks);
                    adapter = getTaskAdapter(tasks);
                    lv.setAdapter(adapter);
                    lv_done.setAdapter(doneAdapter);
                    mViewHolder.refreshLayout.resetNoMoreData();
                } else {
                    adapter.addBottom(tasks, false);
                    doneAdapter.addBottom(doneTasks, false);
                    if (taskList != null && taskList.size() == 0) {
                        mViewHolder.refreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据
                        Toast.makeText(mContext, "已经加载了全部数据", Toast.LENGTH_SHORT).show();
                    }
//                    lv.loadCompleted();
                }
                pageIndex += 1;

                if (latest != null) {
                    horizontalScrollView_task_day_view.scrollTo(0,0);
                    initStaffHeader(latest);
                } else {
                    initStaffHeader();
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
     * 获取列表信息并展示
     */
    public void getTaskList() {
        getTaskList(null);
    }

    /**
     * 已完成任务适配器
     *
     * @param taskList 数据源
     * @return
     */
    private CommanAdapter<Task> getDoneTaskAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, getActivity(), R.layout.item_done_task) {

            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                TextView tvContent = viewHolder.getView(R.id.task_content);
                tvContent.setText(item.getContent());
                tvContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //已完成任务字体中间加横线

                viewHolder.getView(R.id.task_status).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "当前任务状态下不能修改任务状态!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }

    private CommanAdapter<Task> getTaskAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, getActivity(), R.layout.item_task_home) {

            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_creater_task_item, item.getCreatorName());
                viewHolder.setTextValue(R.id.task_content, item.getContent());
                final ImageView ivStatus = viewHolder.getView(R.id.task_status);
                /**
                 * 根据任务状态枚举类型显示状态
                 */
                if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_selected);
                } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status);
                } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status);
                } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status);
                }
                ivStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog(getActivity()).builder()
                                .setTitle("是否提交")
                                .setMsg("确认完成?")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        item.setStatus(TaskStatusEnum.已完成.getName());
                                        saveTask(item);
                                    }
                                })
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                            dialog.show();
                        } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                            dialog.show();
                        } else {
                            Toast.makeText(getActivity(), "当前任务状态下不能修改任务状态!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }


    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Task task) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
//        et_comments.setText("");
//        InputSoftHelper.hiddenSoftInput(getActivity(), et_comments);
//        ll_bottom_comment.setVisibility(View.GONE);
        hideShowSoft();

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                task.setCommentNumber(task.getCommentNumber() + 1);
                adapter.notifyDataSetChanged();
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
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 完成任务
     */
    private void saveTask(Task task) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.改变任务状态 + "?uuid=" + task.getUuid() + "&ticket=1";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "修改任务状态成功!", Toast.LENGTH_SHORT).show();
                pageIndex = 1;
                getTaskList();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(getActivity(), JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onDestroy() {
        PreferceManager.getInsance().saveValueBYkey(TASK_DAY_VIEW_CHECK, "");
        super.onDestroy();
    }

//
//    private void popWiw() {
//
//        popWiw = new BaseSelectPopupWindow(getActivity(), R.layout.edit_data);
//        // popWiw.setOpenKeyboard(true);
//        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popWiw.setFocusable(true);
//        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popWiw.setShowTitle(false);
//        popWiw.setBackgroundDrawable(new ColorDrawable(0));
//        InputMethodManager im = (InputMethodManager) getActivity()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//        final TextView send = popWiw.getContentView().findViewById(
//                R.id.btn_send);
//        final TextEditTextView edt = popWiw.getContentView().findViewById(
//                R.id.edt_content);
//
//        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
//        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);
//        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND
//                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    saveTask(edt.getText().toString().trim(), currentId);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                ll_comment_bottom.setVisibility(View.VISIBLE);
//                inputView.setVisibility(View.GONE);
//                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            }
//        });
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveTask(edt.getText().toString().trim(), currentId);
//            }
//        });
//
//        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
//                | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }


    /**
     * 保存任务
     *
     * @param content 任务内容
     */
    private void saveTask(String content, User user) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressDialogHelper.show(getActivity(), "保存中...");
        Task task = new Task();
        task.setContent(content);
        task.setExecutorIds(user.getUuid());
        task.setBeginTime(currentTime + " 00:00:00");
        task.setEndTime(currentTime + " 23:59:59");
        task.setCreatorId(Global.mUser.getUuid());

        String url = Global.BASE_JAVA_URL + GlobalMethord.任务保存;

        StringRequest.postAsyn(url, task, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                etContent.setText("");
                inputView.setVisibility(View.GONE);
                staffView.setVisibility(View.GONE);
                bgView.setVisibility(View.GONE);
                helper.insertLatest(user);
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                activity.refreshList(true);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        });
    }

    /**
     * 收取选择的员工
     */
    public void receiveSelectedUser(User user) {
        if (staffView != null) {
            staffView.reloadStaffList(user);
            pageIndex = 1;
            getTaskList(ViewHelper.turnLatest(user));
        }
    }
}
