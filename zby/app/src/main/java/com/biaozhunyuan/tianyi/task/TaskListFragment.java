package com.biaozhunyuan.tianyi.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.utils.ShapeUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.j256.ormlite.dao.Dao;
import com.loonggg.weekcalendar.view.WeekCalendar;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;

/**
 * Created by 王安民 on 2017/10/8.
 * 任务列表页面
 */

public class TaskListFragment extends Fragment {

    private PullToRefreshAndLoadMoreListView lv;
    private List<Task> taskList;
    private CommanAdapter<Task> adapter;
    private Demand<Task> demand;
    public int pageIndex = 1;
    public static boolean isReasume = false;
    private Task currentItem;
    private int lastPosition = -1;
    private static final String TASK_LIST_VIEW_CHECK = "TASK_LIST_VIEW_CHECK";
    private HorizontalScrollView scrollView;
    private WeekCalendar weekCalendar;
    private BoeryunSearchView searchView;
    private RelativeLayout rl_back_calander;
    private RelativeLayout rl_show_calander;
    private RelativeLayout rl_hide_calander;
    private ImageView iv_show_calander;
    private ImageView iv_hide_calander;
    private TextView tv_current_time;
    private ImageView iv_hide_head;
    private int calanderType = 0;//日历的状态：0=显示，1=隐藏   默认为显示状态
    private boolean isHeadShow = false;
    private User currentUser = Global.mUser; //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤
    private String currentProject = "";
    private String currentdeparmentId = "";
    private String currentdictSchedulType = "";
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private LinearLayout ll_user_root_task_day_view;
    private View addView;
    private ORMDataHelper dataHelper;
    private List<Latest> recentList; //最近选择的员工
    private String currentUrl = Global.BASE_JAVA_URL + GlobalMethord.任务列表
            + "?userId=" + currentUser.getUuid() + "&projectId=" + currentProject + "&deparmentId=" + currentdeparmentId
            + "&dictSchedulType=" + currentdictSchedulType + "&beginTime=" + currentTime + " 00:00:00"
            + "&endTime=" + currentTime + " 23:59:59";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, null);
        initViews(view);
        recentList = new ArrayList<>();
        dataHelper = ORMDataHelper.getInstance(getActivity());
        ProgressDialogHelper.show(getActivity());
        initDemand();
        getTaskList();
        createAddView();
        setOnEvent();
        return view;
    }


    private void initViews(View view) {
        lv = (PullToRefreshAndLoadMoreListView) view.findViewById(R.id.lv_task_list);
        View view1 = View.inflate(getActivity(), R.layout.header_worklog, null);
        RelativeLayout rl_time = (RelativeLayout) view1.findViewById(R.id.rl_time_header);
        scrollView = (HorizontalScrollView) view1.findViewById(R.id.horizontalScrollView_task_day_view);
        weekCalendar = (WeekCalendar) view1.findViewById(R.id.weekcalendar_task_day_view);
        searchView = view1.findViewById(R.id.search_view);
        tv_current_time = (TextView) view1.findViewById(R.id.tv_current_time);
        iv_hide_calander = (ImageView) view1.findViewById(R.id.iv_hide_calander);
        iv_show_calander = (ImageView) view1.findViewById(R.id.iv_home_show_calander);
        rl_show_calander = (RelativeLayout) view1.findViewById(R.id.rl_home_calander);
        rl_hide_calander = (RelativeLayout) view1.findViewById(R.id.rl_home_yearmonth);
        rl_back_calander = (RelativeLayout) view1.findViewById(R.id.rl_calendar_month);

        iv_hide_head = (ImageView) view1.findViewById(R.id.iv_hide_head_log_List);
        ll_user_root_task_day_view = view1.findViewById(R.id.ll_user_root_task_day_view);
        weekCalendar.setVisibility(View.VISIBLE);
        lv.addHeaderView(view1);
        rl_time.setVisibility(View.VISIBLE);

        calanderType = 1;
        showCanlender();

        String time = ViewHelper.getDateTodayStr();

        currentTime = time;
        String yue = time.substring(5, 7);
        String ri = time.substring(8, 10);
        if (yue.substring(0, 1).equals("0")) {
            yue = yue.substring(1, 2);
        }
        if (ri.substring(0, 1).equals("0")) {
            ri = ri.substring(1, 2);
        }
        tv_current_time.setText(currentTime);
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


    @Override
    public void onResume() {
        super.onResume();
        if (isReasume) {
            pageIndex = 1;
            getTaskList();
            isReasume = false;
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
//                    scrollView.setVisibility(View.GONE);
                    isHeadShow = false;
                    iv_hide_head.setImageResource(R.drawable.icon_show_head);
                } else {
//                    scrollView.setVisibility(View.VISIBLE);
                    isHeadShow = true;
                    iv_hide_head.setImageResource(R.drawable.icon_hide_head);
                }
            }
        });
        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                demand.key = "searchField_string_content";
                demand.value = "1|" + str;
                pageIndex = 1;
                getTaskList();
            }
        });

        searchView.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                demand.key = "searchField_string_content";
                demand.value = "";
                pageIndex = 1;
                lv.startRefresh();
                getTaskList();
            }

            @Override
            public void OnClick() {

            }
        });

        weekCalendar.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String time) {
                currentTime = time;
                String times = ViewHelper.convertStrToFormatDateStr(time + " 00:00:00", "yyyy-MM-dd");
                tv_current_time.setText(times);
                currentUrl = Global.BASE_JAVA_URL + GlobalMethord.任务列表 + "?userId=" + currentUser.getUuid()
                        + "&projectId=" + currentProject + "&deparmentId=" + currentdeparmentId
                        + "&dictSchedulType=" + currentdictSchedulType + "&beginTime=" + currentTime + " 00:00:00"
                        + "&endTime=" + currentTime + " 23:59:59";
                ;//
                pageIndex = 1;
                getTaskList();
            }
        });
    }

    /**
     * 1为我执行的任务
     * 2为我委派的任务
     * 3为我参与的任务
     */
    private void initDemand() {
        demand = new Demand<Task>(Task.class);
        demand.pageSize = 10;
        demand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
    }

    /**
     * 获取列表信息并展示
     */
    public void getTaskList() {
        getTaskList(null);
    }

    /**
     * 获取列表信息并展示
     */
    private void getTaskList(Latest latest) {
        demand.src = currentUrl;
        ProgressDialogHelper.show(getActivity());
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                taskList = demand.data;

                for (final Task task : demand.data) {
                    try {
                        task.setCreatorName(demand.getDictName(task, "creatorId"));
                        task.setExecutorNames(demand.getDictName(task, "executorIds"));
                        task.setParticipantNames(new DictionaryHelper(getActivity()).getUserNamesById(task.getParticipantIds()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(taskList);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(taskList, false);
                    if (taskList != null && taskList.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;


                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 1) {
                            Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
                            Task task = adapter.getDataList().get(position - 2);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("taskInfo", task);
                            intent.putExtra("taskIntentInfo", bundle);
                            startActivity(intent);
                        }
                    }
                });
                if (latest != null) {
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
                if (latest != null) {
                    initStaffHeader(latest);
                }
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
                        if (lat.getUuid().equals(mine.getUuid())) {
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

    public void generationTaskCompletionView(final List<Latest> mList, Latest latest) {

        ll_user_root_task_day_view.removeAllViews();
//        itemList.clear();
        for (int i = 0; i < mList.size(); i++) {
            final Latest item = mList.get(i);
            final int pos = i;
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_user_and_progress, null);
            final View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_user_and_progress_transpant, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name_avatar);
            CircleImageView view2 = (CircleImageView) view.findViewById(R.id.circularAvatar_task_user);
            String valueBYkey = PreferceManager.getInsance().getValueBYkey(TASK_LIST_VIEW_CHECK);

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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialogHelper.show(getActivity());
                    PreferceManager.getInsance().saveValueBYkey(TASK_LIST_VIEW_CHECK, item.getUuid());
                    if (lastPosition != -1 && lastPosition != pos) {
                        ll_user_root_task_day_view.getChildAt(lastPosition).setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                    }
                    lastPosition = pos;
                    pageIndex = 1;
                    currentUser = ViewHelper.turnUser(item);
                    currentUrl = Global.BASE_JAVA_URL + GlobalMethord.任务列表 + "?userId=" + currentUser.getUuid()
                            + "&projectId=" + currentProject + "&deparmentId=" + currentdeparmentId
                            + "&dictSchedulType=" + currentdictSchedulType + "&beginTime=" + currentTime + " 00:00:00"
                            + "&endTime=" + currentTime + " 23:59:59";
                    getTaskList();
                }
            });
            ll_user_root_task_day_view.addView(view);
            ll_user_root_task_day_view.addView(view1); //分割线

            if (i == mList.size() - 1) {
                ll_user_root_task_day_view.addView(addView);
            }
        }
    }

    private void createAddView() {
        addView = LayoutInflater.from(getActivity()).inflate(R.layout.item_input_add, null);
        ImageView ivAdd = addView.findViewById(R.id.iv_item_input_add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", true);
                intent.putExtra("title", "选择执行人");
                ((Activity) getActivity()).startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
            }
        });
    }

    private CommanAdapter<Task> getAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, getActivity(), R.layout.item_tasklist_list) {
            private DictIosPickerBottomDialog dialog;

            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                if (!TextUtils.isEmpty(item.getBeginTime()) && item.getBeginTime().length() >= 10) {
                    viewHolder.setTextValue(R.id.time_item_task_list, item.getBeginTime().substring(0, 10));
                }
                viewHolder.setTextValue(R.id.textViewTitle_tasklist, item.getContent());
                viewHolder.setTextValue(R.id.name_item_task_list, item.getExecutorNames());
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getCreatorId());//创建人头像


                TextView tvStatus = viewHolder.getView(R.id.tv_state_tasklist);
                tvStatus.setVisibility(View.VISIBLE);
                /**
                 * 根据任务状态枚举类型显示状态
                 *
                 */
                if (isAdded()) {
                    if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
                                R.color.fuzhuselan), 5, 0));
                        tvStatus.setText("已完成");
                    } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
                                R.color.apply_state_daishenhe), 5, 0));
                        tvStatus.setText("进行中");
                    } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
                                R.color.fuzhuselan), 5, 0));
                        tvStatus.setText("已取消");
                    } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
                                R.color.apply_state_yifoujue), 5, 0));
                        tvStatus.setText("已逾期");
                    }
                }

//                final ImageView iv_support = viewHolder.getView(R.id.iv_item_task_support);
//                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_task_item);
//                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_task_item);

//                if (item.isLike()) {
//                    iv_support.setImageResource(R.drawable.icon_support_select);
//                } else {
//                    iv_support.setImageResource(R.drawable.icon_support);
//                }
//
//                tv_support.setText(item.getLikeNumber() + "");
//                tv_comment.setText(item.getCommentNumber() + "");

                //新加: 点赞和评论

//                LinearLayout ll_support = viewHolder.getView(R.id.ll_task_item_support);//dianzan
//                LinearLayout ll_comment = viewHolder.getView(R.id.ll_task_item_comment);

//                ll_comment.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        currentItem = item;
////                        ll_bottom_comment.setVisibility(View.VISIBLE);
////                        et_comments.requestFocus();
////                        hideShowSoft();
//                        popWiw(currentItem);
//                    }
//                });

//                //点赞/取消赞
//                ll_support.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getExecutorIds());
//                        post.setDataType("任务计划");
//                        post.setDataId(item.getUuid());
//                        if (item.isLike()) { //取消点赞
//                            cancleSupport(post, item);
//                        } else { //点赞
//                            support(post, item);
//                        }
//                    }
//                });

            }
        };
    }

//    /**
//     * 评论
//     *
//     * @param post
//     */
//    public void comment(SupportAndCommentPost post, final Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
////        et_comments.setText("");
////        InputSoftHelper.hiddenSoftInput(getActivity(), et_comments);
////        ll_bottom_comment.setVisibility(View.GONE);
//        hideShowSoft();
//        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
//                task.setCommentNumber(task.getCommentNumber() + 1);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//                Log.e("tag", "评论失败");
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//    }
//
//    /**
//     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
//     */
//    private void hideShowSoft() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }

//    /**
//     * 点赞
//     *
//     * @param post
//     * @param task
//     */
//    private void support(SupportAndCommentPost post, final Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;
//
//        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
//                task.setLikeNumber(task.getLikeNumber() + 1);
//                task.setLike(true);
//                adapter.notifyDataSetChanged();
////                ll_bottom_comment.setVisibility(View.GONE);
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
//    }
//
//    /**
//     * 取消点赞
//     *
//     * @param post 要取消点赞的实体的ID
//     * @param task
//     */
//    private void cancleSupport(SupportAndCommentPost post, final Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;
//
//        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "取消点赞成功", Toast.LENGTH_SHORT).show();
//                task.setLikeNumber(task.getLikeNumber() - 1);
//                task.setLike(false);
//                adapter.notifyDataSetChanged();
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
//    }
//
//
//    /**
//     * 保存任务
//     */
//    private void saveTask(Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.任务保存;
//
//        StringRequest.postAsyn(url, task, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "修改任务状态成功!", Toast.LENGTH_SHORT).show();
//                lv.startRefresh();
//                pageIndex = 1;
//                getTaskList();
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//                Toast.makeText(getActivity(), JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT);
//            }
//        });
//    }

//    private void popWiw(final Task item) {
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
//        edt.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                if (TextUtils.isEmpty(edt.getText())) {
//                    send.setEnabled(false);
//                } else {
//                    send.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND
//                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
//                        String content = edt.getText().toString().trim();
//
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getExecutorIds());
//                        post.setDataType("任务计划");
//                        post.setDataId(item.getUuid());
//                        post.setContent(content);
//                        comment(post, item);
//
//                        popWiw.dismiss();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
//        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            }
//        });
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
//                    // /提交内容
//                    String content = edt.getText().toString().trim();
//
//                    SupportAndCommentPost post = new SupportAndCommentPost();
//                    post.setFromId(Global.mUser.getUuid());
//                    post.setToId(item.getExecutorIds());
//                    post.setDataType("任务计划");
//                    post.setDataId(item.getUuid());
//                    post.setContent(content);
//                    comment(post, item);
//
//                    popWiw.dismiss();
//                }
//            }
//        });
//
//        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
//                | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }


    public void getFilterList(TaskFilter filter) {
        currentProject = filter.getProjectId();
        currentdeparmentId = filter.getDeparmentId();
        currentdictSchedulType = filter.getDictSchedulType();
        currentUrl = Global.BASE_JAVA_URL + GlobalMethord.任务列表
                + "?userId=" + currentUser.getUuid() + "&projectId=" + currentProject + "&deparmentId=" + currentdeparmentId
                + "&dictSchedulType=" + currentdictSchedulType + "&beginTime=" + currentTime + " 00:00:00"
                + "&endTime=" + currentTime + " 23:59:59";
        pageIndex = 1;
        getTaskList();
    }

    /**
     * 收取选择的员工
     */
    public void receiveSelectedUser(User user) {
        scrollView.scrollTo(0, 0);
        currentUser = user;
        currentUrl = Global.BASE_JAVA_URL + GlobalMethord.任务列表
                + "?userId=" + currentUser.getUuid() + "&projectId=" + currentProject + "&deparmentId=" + currentdeparmentId
                + "&dictSchedulType=" + currentdictSchedulType + "&beginTime=" + currentTime + " 00:00:00"
                + "&endTime=" + currentTime + " 23:59:59";
        pageIndex = 1;
        getTaskList(ViewHelper.turnLatest(user));
    }

    @Override
    public void onDestroy() {
        PreferceManager.getInsance().saveValueBYkey(TASK_LIST_VIEW_CHECK, "");
        super.onDestroy();
    }
}
