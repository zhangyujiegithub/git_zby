package com.biaozhunyuan.tianyi.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.CustomDayView;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.helper.DictionaryQueryDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.HolidayUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.ListRecentlySelectedStaffView;
import com.biaozhunyuan.tianyi.view.OnViewGlobalLayoutListener;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.biaozhunyuan.tianyi.view.VoiceInputView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Request;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS_STAFF_VIEW;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

/**
 * Created by 王安民 on 2017/8/22.
 * <p>
 * 任务列表页面
 */

public class TaskListActivityNew extends BaseActivity {


    public static boolean isResume = false;
    private static String EXTRA_SELECTED_USER = "EXTRA_SELECTED_USER";

    private SimpleIndicator tabView;
    private ORMDataHelper dataHelper;
    private BoeryunViewpager viewpager;
    private List<Fragment> mFragments;
    //    private TaskTodayFragment taskDayViewFragment;
    private DrawerLayout mDrawerLayout;
    private LinearLayout menu_right;
    private TextView confirm;
    private TextView reset;
    private TextView drawer_type;
    private TextView drawer_dept;
    private TextView drawer_project;
    private TextView drawer_task;
    private TagFlowLayout tgf_type;
    private TagFlowLayout tgf_dept;
    private TagFlowLayout tgf_project;
    private TagFlowLayout tgf_task;
    private List<字典> typeList;
    private List<字典> taskList;
    private List<字典> deptList;
    private List<字典> projectList;
    private TaskListFragmentNew taskListFragment;
    private TaskFilter mFilter;
    private ImageView arrow_project;
    private ImageView arrow_type;
    private ImageView arrow_dept;
    private ImageView arrow_task;
    private DictionaryQueryDialogHelper dictionaryQueryDialogProject;
    private DictionaryQueryDialogHelper dictionaryQueryDialogDept;
    private TaskWeekFragment taskWeekViewFragment;
    private TaskBoardFragment taskBoardFragment;
    private CircleImageView iv_head;
    private ImageView iv_title;
    private ImageView iv_back;
    private ImageView iv_filter;
    private ImageView iv_search;
    private TextView tv_user;
    private TextView tv_back;
    private TextEditTextView etAddTask;
    private ImageView ivVoice;
    private LinearLayout llAddTask;
    private CommonPopupWindow staffPop; //展示成员的popupwindow
    private BaseSelectPopupWindow addTaskPop; //添加任务的popupwindow
    private BaseSelectPopupWindow calendarPop;//选择日期的popupwindow
    private BaseSelectPopupWindow executorPop;//选择执行人的popupwindow
    private String currentId = Global.mUser.getUuid(); //当前选中的任务执行人 默认为自己
    private VoiceInputView inputView;
    private View placeholderView;
    private ArrayList<Calendar> currentCalendars = new ArrayList<Calendar>();
    private ListRecentlySelectedStaffView staffView;
    private String selectDate = ViewHelper.getDateToday();
    private Context mContext;
    private LinearLayout llHead;
    private ImageView ivTitle;
    private RelativeLayout rlTitle;
    private TaskAssignFragment taskAssignFragment;
    public User currentUser = Global.mUser;
    private static boolean isOtherUserList = false; //当前页面是否为其他员工任务列表
    private int offscreenPageLimit = 4; //viewpager预加载fragment数量
    private int screenHeight;
    private LinearLayout ll_drawer_mytask;
    private View viewPop;
    private CalendarViewAdapter calendarAdapter;
    private CalendarDate selectCalendarDate;  //选中的日期
    private int pagerCurrentPosition = -1; //viewPager当前的位置
    private SharedPreferencesHelper preferencesHelper;
    private Handler handler = new Handler();
    private ListRecentlySelectedStaffView topStaffView;

    private boolean isTopSelectUser = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list_new);
        initViews();
        getIntentData();
        setOnEvent();
    }

    /**
     * 如果是从任务界面选择执行人控件跳转进来的 获取携带参数
     */
    private void getIntentData() {
        if (getIntent().getSerializableExtra(EXTRA_SELECTED_USER) != null) {
            currentUser = (User) getIntent().getSerializableExtra(EXTRA_SELECTED_USER);
            offscreenPageLimit = 1;
            hiddenList();
        }
        initData();
    }

    private void initData() {
        mContext = this;
        preferencesHelper = new SharedPreferencesHelper(mContext);
        dataHelper = ORMDataHelper.getInstance(this);
        mFragments = new ArrayList<Fragment>();
        typeList = new ArrayList<>();
        taskList = new ArrayList<>();
        deptList = new ArrayList<>();
        projectList = new ArrayList<>();
        //屏幕高度
        screenHeight = ViewHelper.getScreenHeight(mContext) / 2;
        dictionaryQueryDialogProject = DictionaryQueryDialogHelper.getInstance(this);
        dictionaryQueryDialogDept = DictionaryQueryDialogHelper.getInstance(this);
        int screenWidth = ViewHelper.getScreenWidth(this);
        ViewGroup.LayoutParams params = menu_right.getLayoutParams();
        params.width = screenWidth / 10 * 9;
        setBottomKeyBoardStatus();

        menu_right.setLayoutParams(params);
        if (currentUser.getUuid().equals(Global.mUser.getUuid())) { //如果当前查看员工任务是自己
            pagerCurrentPosition = 0;
            tabView.setTabItemTitles(new String[]{"我的任务", "我指派的", "周期任务", "周视图", "看板"});
//        taskDayViewFragment = new TaskDayViewFragment();
//            taskDayViewFragment = new TaskTodayFragment();
            taskListFragment = new TaskListFragmentNew();
            taskAssignFragment = new TaskAssignFragment();
            taskWeekViewFragment = new TaskWeekFragment();
            taskBoardFragment = new TaskBoardFragment();
//            mFragments.add(taskDayViewFragment);
            mFragments.add(taskListFragment);
            mFragments.add(taskAssignFragment);
            mFragments.add(new TaskPeriodicFragment());
            mFragments.add(taskWeekViewFragment);
//        mFragments.add(new TaskLaneFragment());
            mFragments.add(taskBoardFragment);
//        mFragments.add(new TaskLaneFragment());
            tabView.setViewPager(viewpager, 0);
        } else {
            taskListFragment = new TaskListFragmentNew();
            mFragments.add(taskListFragment);
        }
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == ((Fragment) obj).getView();
            }
        });
        viewpager.setOffscreenPageLimit(offscreenPageLimit);//预加载

//        ImageUtils.displyUserPhotoById(this, currentUser.getUuid(), iv_head);
        tv_user.setText(new DictionaryHelper(this).getUserNameById(currentUser.getUuid()));

        getTaskType();
        getTaskDept();
        getTaskMyTask();
        getTaskProject();
    }

    private void getTaskMyTask() {
        taskList.add(new 字典("", "全部"));
        if (offscreenPageLimit == 1) {
            taskList.add(new 字典(currentUser.getUuid(), "他创建的"));
            taskList.add(new 字典(currentUser.getUuid(), "指派给他"));
        } else {
            taskList.add(new 字典(Global.mUser.getUuid(), "我创建的"));
            taskList.add(new 字典(Global.mUser.getUuid(), "指派给我"));
        }
        initTagFlowAdapter(taskList, tgf_task);
    }

    /**
     * 如果当前查看的不是自己的任务列表 只展示列表,并且隐藏新建任务
     */
    private void hiddenList() {
        tabView.setVisibility(GONE);
        iv_search.setVisibility(GONE);
        llAddTask.setVisibility(GONE);
    }

    /**
     * 刷新各界面任务列表 同步数据
     */
    public void refreshList(boolean isRefreshWeek) {
        /*if (taskDayViewFragment != null) {
            taskDayViewFragment.pageIndex = 1;
            taskDayViewFragment.getTaskList();
        }*/
        if (taskListFragment != null) {
            taskListFragment.pageIndex = 1;
            taskListFragment.getTaskList();
            taskListFragment.getTaskData("", "");
        }
        if (taskAssignFragment != null) {
            taskAssignFragment.pageIndex = 1;
            taskAssignFragment.getTaskList();
            taskAssignFragment.getTaskData("", "");
        }
        if (taskWeekViewFragment != null) {
            taskWeekViewFragment.pageIndex = 1;
            taskWeekViewFragment.getTaskList();
            taskWeekViewFragment.getTaskData("", "");
        }
    }

    private void getTaskProject() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务过滤项目;
        StringRequest.postAsyn(url, "name=", new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                int size = 5;
                if (字典s.size() <= 5) {
                    size = 字典s.size();
                }
                projectList.add(0, new 字典("", "全部"));
                for (int i = 0; i < size; i++) {
                    projectList.add(字典s.get(i));
                }
                if (size > 5) {
                    projectList.add(new 字典("", "更多"));
                }
                initTagFlowAdapter(projectList, tgf_project);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void getTaskDept() {
        deptList.add(new 字典("", "全部"));
        deptList.add(new 字典(TaskStatusEnum.进行中.getName() + "," + TaskStatusEnum.已逾期.getName(), "未完成"));
        deptList.add(new 字典(TaskStatusEnum.已完成.getName(), "已完成"));
        initTagFlowAdapter(deptList, tgf_dept);

    }

    private void getTaskType() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        final DictData dictData = new DictData();
        dictData.setDictionaryName("dict_schedul_type");
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                typeList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                for (int i = 0; i < typeList.size(); i++) {
                    if (typeList.get(i).getName().equals("日计划")) {
                        字典 日计划 = typeList.get(i);
                        typeList.remove(i);
                        typeList.add(0, 日计划);
                    }
                }
                mFilter = new TaskFilter("", "", "");
                initTagFlowAdapter(typeList, tgf_type);
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
     * Drawerlayout back键关闭
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(menu_right))
            mDrawerLayout.closeDrawers();
        else
            isOtherUserList = false;
        super.onBackPressed();
    }

    private void initViews() {
//        headerView = (BoeryunHeaderView) findViewById(R.id.header_task_list);
        tabView = (SimpleIndicator) findViewById(R.id.indicator_task_view);
        viewpager = (BoeryunViewpager) findViewById(R.id.viewpager_task_view);
        menu_right = (LinearLayout) findViewById(R.id.menu_right);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        confirm = findViewById(R.id.tv_drawer_confirm);
        reset = findViewById(R.id.tv_drawer_reset);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ll_drawer_mytask = findViewById(R.id.ll_drawer_mytask);
        drawer_type = findViewById(R.id.tv_drawer_customer);
        drawer_dept = findViewById(R.id.tv_drawer_dept);
        drawer_project = findViewById(R.id.tv_drawer_project);
        drawer_task = findViewById(R.id.tv_drawer_mytask);

        arrow_project = findViewById(R.id.iv_project);
        arrow_type = findViewById(R.id.iv_type);
        arrow_dept = findViewById(R.id.iv_dept);
        arrow_task = findViewById(R.id.iv_mytask);

        tgf_type = findViewById(R.id.tgf_task_type);
        tgf_dept = findViewById(R.id.tgf_task_dept);
        tgf_project = findViewById(R.id.tgf_task_project);
        tgf_task = findViewById(R.id.tgf_task_mytask);

        tgf_type.setCanCancelSelect(false);
        tgf_dept.setCanCancelSelect(false);
        tgf_project.setCanCancelSelect(false);
        tgf_task.setCanCancelSelect(false);


        iv_head = findViewById(R.id.iv_head);
        iv_title = findViewById(R.id.iv_title);
        iv_back = findViewById(R.id.iv_back_headerview);
        iv_filter = findViewById(R.id.iv_filter_headerview);
        iv_search = findViewById(R.id.iv_search_headerview);
        tv_user = findViewById(R.id.tv_user);
        tv_back = findViewById(R.id.tv_back);

        etAddTask = findViewById(R.id.et_input_home_add_task);
        ivVoice = findViewById(R.id.btn_voice);
        llAddTask = findViewById(R.id.ll_comment_share_bottom);

        viewPop = findViewById(R.id.view_pop);

        rlTitle = findViewById(R.id.rl_title);
        llHead = findViewById(R.id.ll_head);
        ivTitle = findViewById(R.id.iv_title);
    }

    private void initTagFlowAdapter(List<字典> list, TagFlowLayout tgf) {
        TagAdapter<字典> tagAdapter = new TagAdapter<字典>(list) {
            @Override
            public View getView(FlowLayout parent, int position, 字典 t) {
                Logger.i("tagA" + position + "--" + t);
                TextView tv = (TextView) LayoutInflater.from(TaskListActivityNew.this).inflate(
                        R.layout.item_label_task_filter_list, tgf, false);
                tv.setText(t.getName());
                return tv;
            }
        };
        tgf.setMaxSelectCount(1);
        tgf.setAdapter(tagAdapter);
        tagAdapter.setSelectedList(0);
//        tgf.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//            @Override
//            public void onSelected(Set<Integer> selectPosSet) {
//                字典 z = new 字典();
//                for (Integer pos : selectPosSet) {
//                    z = childs.get(groupPosition).get(pos);
//                }
//                groups.get(groupPosition).setText(z.getName());
//                notifyDataSetChanged();
//            }
//        });
    }


    private void setOnEvent() {
        llHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStaffPop();
            }
        });
        ivTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStaffPop();
            }
        });
        ivVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewTaskCardPopWindow(true);
            }
        });
        etAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewTaskCardPopWindow(false);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtherUserList = false;
                finish();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtherUserList = false;
                finish();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskListActivityNew.this, TaskSearchListActivity.class));
            }
        });
        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(menu_right);
            }
        });


        taskListFragment.setOnSelectDateListener(new TaskListFragmentNew.onSelectDateListener() {
            @Override
            public void onSelectDate(String date) {
                selectDateOnCalendar(date);
            }
        });

        if (taskAssignFragment != null) {
            taskAssignFragment.setOnSelectDateListener(new TaskAssignFragment.onSelectDateListener() {
                @Override
                public void onSelectDate(String date) {
                    selectDateOnCalendar(date);
                }
            });

            taskAssignFragment.setOnSelectUserListener(new TaskAssignFragment.onSelectUserListener() {
                @Override
                public void onSelectUser(User user) {
                    if (user != null) {
                        currentId = user.getUuid();
                    }
                }
            });
        }


        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //开启手势滑动
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(menu_right);
                if (pagerCurrentPosition == -1 && taskListFragment != null) {
                    taskListFragment.getFilterList(mFilter);
                }

                /*if (pagerCurrentPosition == 0) {
                    taskDayViewFragment.getFilterList(mFilter);
                }*/
                if (pagerCurrentPosition == 0) {
                    taskListFragment.getFilterList(mFilter);
                }
                if (pagerCurrentPosition == 1) {
                    taskAssignFragment.getFilterList(mFilter);
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagerCurrentPosition = position;
                //在重新回到今日任务页面，默认执行人为当前员工，默认日期为今天
                setTodayAndCurrentUser();
                if (position == 1 || position == 0) {
                    llAddTask.setVisibility(VISIBLE);
                    iv_filter.setVisibility(VISIBLE);
                }
                if (position == 2) {
                    llAddTask.setVisibility(GONE);
                    iv_filter.setVisibility(GONE);
                }
                if (position == 3) {
                    llAddTask.setVisibility(VISIBLE);
                    iv_filter.setVisibility(GONE);
                }
                if (position == 1) {
                    ll_drawer_mytask.setVisibility(GONE);
                    tgf_task.setVisibility(GONE);
                    etAddTask.setHint("指派任务");
                } else {
                    ll_drawer_mytask.setVisibility(VISIBLE);
                    tgf_task.setVisibility(VISIBLE);
                    etAddTask.setHint("添加任务");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.ll_drawer_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_type.getVisibility() == GONE) {
                    tgf_type.setVisibility(VISIBLE);
                    arrow_type.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_type.setVisibility(GONE);
                    arrow_type.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        findViewById(R.id.ll_drawer_dept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_dept.getVisibility() == GONE) {
                    tgf_dept.setVisibility(VISIBLE);
                    arrow_dept.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_dept.setVisibility(GONE);
                    arrow_dept.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        findViewById(R.id.ll_drawer_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_project.getVisibility() == GONE) {
                    tgf_project.setVisibility(VISIBLE);
                    arrow_project.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_project.setVisibility(GONE);
                    arrow_project.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        ll_drawer_mytask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_task.getVisibility() == GONE) {
                    tgf_task.setVisibility(VISIBLE);
                    arrow_task.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_task.setVisibility(GONE);
                    arrow_task.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        tgf_task.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = taskList.get(pos);
                }
                drawer_task.setText(z.getName());
//                mFilter.setDictSchedulType(z.getUuid());
                if (z.getName().equals("我创建的")) {
                    mFilter.setUserId("我创建的");
                } else if (z.getName().equals("指派给我")) {
                    mFilter.setUserId("指派给我");
                } else if (z.getName().equals("他创建的")) {
                    mFilter.setUserId("他创建的");
                } else if (z.getName().equals("指派给他")) {
                    mFilter.setUserId("指派给他");
                } else {
                    mFilter.setUserId("全部");
                }
            }
        });

        tgf_type.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = typeList.get(pos);
                }
                drawer_type.setText(z.getName());
                mFilter.setDictSchedulType(z.getUuid());
            }
        });
        tgf_dept.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = deptList.get(pos);
                }
                if (!z.getName().equals("更多")) {
                    drawer_dept.setText(z.getName());
//                    mFilter.setDeparmentId(z.getUuid());
                    mFilter.setStatus(z.getUuid());
                } else {
                    dictionaryQueryDialogDept.show("base_department");
                }
            }
        });
        dictionaryQueryDialogDept.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
            @Override
            public void onSelected(字典 dict) {
                boolean isExist = false;
                for (int i = 0; i < deptList.size(); i++) {
                    if (deptList.get(i).getUuid().equals(dict.getUuid())) {
                        isExist = true;
                        tgf_dept.clearSelected();
                        tgf_dept.getAdapter().setSelectedList(i);
                    }
                }
                if (!isExist) {
                    deptList.add(1, dict);
                    tgf_dept.clearSelected();
                    tgf_dept.getAdapter().setSelectedList(1);
                }
//                mFilter.setDeparmentId(dict.getUuid());
                mFilter.setStatus(dict.getUuid());
                drawer_dept.setText(dict.getName());
            }
        });
        tgf_project.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = projectList.get(pos);
                }
                if (!z.getName().equals("更多")) {
                    drawer_project.setText(z.getName());
                    mFilter.setProjectId(z.getUuid());
                } else {
                    dictionaryQueryDialogProject.showDialogByUrl(Global.BASE_JAVA_URL + GlobalMethord.任务过滤项目);
                }
            }
        });
        dictionaryQueryDialogProject.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
            @Override
            public void onSelected(字典 dict) {
                boolean isExist = false;
                for (int i = 0; i < projectList.size(); i++) {
                    if (projectList.get(i).getUuid().equals(dict.getUuid())) {
                        isExist = true;
                        tgf_project.clearSelected();
                        tgf_project.getAdapter().setSelectedList(i);
                    }
                }
                if (!isExist) {
                    projectList.add(1, dict);
                    tgf_project.clearSelected();
                    tgf_project.getAdapter().setSelectedList(1);
                }
                mFilter.setProjectId(dict.getUuid());
                drawer_project.setText(dict.getName());
            }
        });
    }


    /**
     * 更新日历上的选中日期
     *
     * @param date 日期
     */
    private void selectDateOnCalendar(String date) {
        selectCalendarDate = new CalendarDate(
                ViewHelper.getYearByDate(date),
                ViewHelper.getMonthByDate(date),
                ViewHelper.getDayByDate(date));
        selectDate = date;
        if (calendarAdapter != null)
            calendarAdapter.notifyDataChanged(selectCalendarDate);
    }

    /**
     * 展示成员的pop
     */
    private void showStaffPop() {
        if (staffPop == null) {
            staffPop = new CommonPopupWindow.Builder(mContext)
                    //设置PopupWindow布局
                    .setView(R.layout.popup_task_staff)
                    //设置宽高
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    //设置动画
                    .setAnimationStyle(R.style.AnimDown)
                    //设置PopupWindow里的子View及点击事件
                    .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                        @Override
                        public void getChildView(View view, int layoutResId) {
                            LinearLayout llParent = view.findViewById(R.id.ll_parent);
                            llParent.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(llParent, screenHeight));
                            topStaffView = view.findViewById(R.id.staff_view);
                            if (!currentUser.getUuid().equals(Global.mUser.getUuid())) {
                                topStaffView.reloadStaffList(currentUser);
                            }
                            topStaffView.setOnKeyBoardSelectedUserListener(new ListRecentlySelectedStaffView.OnKeyBoardSelectedUserListener() {
                                @Override
                                public void onSelected(User user) {
                                    checkUserTask(user);
                                }
                            });
                        }
                    })
                    //开始构建
                    .create();
        }
        topStaffView.reloadStaffList(currentUser);
        staffPop.showAsDropDown(rlTitle);
        isTopSelectUser = true;
        viewPop.setVisibility(VISIBLE);
        staffPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewPop.setVisibility(GONE);
            }
        });
    }

    /**
     * 顶部员工切换
     *
     * @param user
     */
    private void checkUserTask(User user) {
        if (user.getUuid().equals(Global.mUser.getUuid()) //如果当前查看员工任务不是自己
                && !currentUser.getUuid().equals(Global.mUser.getUuid())) {
            isOtherUserList = false;
            finish();
        } else if (user.getUuid().equals(currentUser.getUuid())) {
            if (staffPop != null)
                staffPop.dismiss();
        } else {
            if (!user.getUuid().equals(Global.mUser.getUuid())) {
                if (!isOtherUserList) { //如果当前在查看我的任务界面 跳转一个新的activity
                    isOtherUserList = true;
                    Intent intent = new Intent(mContext, TaskListActivityNew.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(EXTRA_SELECTED_USER, user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {//如果当前已经启动新的activity刷新当前页面数据
                    currentUser = user;
                    tv_user.setText(new DictionaryHelper(TaskListActivityNew.this)
                            .getUserNameById(currentUser.getUuid()));
                    taskListFragment.refreshUserList(currentUser);
                }
            }
            if (staffPop != null)
                staffPop.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS_STAFF_VIEW: //选择执行人
                    if (staffPop != null) {
                        staffPop.dismiss();
                    }
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        User user = userList.getUsers().get(0);
                        if (topStaffView != null) {
                            topStaffView.reloadStaffList(user);
                        }
                        if (staffView != null) {
                            staffView.reloadStaffList(user);
                        }
                        if (isTopSelectUser) {
                            checkUserTask(user);
                        }
                    }
            }
        }
    }

    /**
     * 重置选项
     */
    private void reset() {
        tgf_project.getAdapter().setSelectedList(0);
        tgf_dept.getAdapter().setSelectedList(0);
        tgf_type.getAdapter().setSelectedList(0);
        tgf_task.getAdapter().setSelectedList(0);
        tgf_project.clearSelected();
        tgf_dept.clearSelected();
        tgf_type.clearSelected();
        tgf_task.clearSelected();
        drawer_project.setText(projectList.get(0).getName());
        drawer_dept.setText(deptList.get(0).getName());
        drawer_type.setText(typeList.get(0).getName());
        drawer_task.setText(taskList.get(0).getName());
        mFilter.setDictSchedulType(typeList.get(0).getUuid());
        mFilter.setProjectId("");
        mFilter.setDeparmentId("");
        mFilter.setUserId("");
        mFilter.setStatus("");
    }


    /**
     * 添加卡片的对话框
     */
    private void showNewTaskCardPopWindow(boolean isShowVoice) {
        addTaskPop = new BaseSelectPopupWindow(mContext, R.layout.pop_add_task);
        // popWiw.setOpenKeyboard(true);
        addTaskPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        addTaskPop.setFocusable(true);
        addTaskPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        addTaskPop.setShowTitle(false);
        addTaskPop.setBackgroundDrawable(new ColorDrawable(0));

        WindowManager.LayoutParams lp = getWindow().getAttributes();

        TextEditTextView edt = addTaskPop.getContentView().findViewById(R.id.et_input_home_add_task);
        LinearLayout addBtn = addTaskPop.getContentView().findViewById(R.id.btn_home_add_task);
        ImageView tvDate = addTaskPop.getContentView().findViewById(R.id.tv_tomorrow);
        ImageView voiceBtn = addTaskPop.getContentView().findViewById(R.id.btn_voice);
        ImageView selectexBtn = addTaskPop.getContentView().findViewById(R.id.select_executor);
        placeholderView = addTaskPop.getContentView().findViewById(R.id.placeholderView);
        inputView = addTaskPop.getContentView().findViewById(R.id.voice_input_view_home_add_task);
        inputView.setSwitvhKeyBoardandVoiceVisible(false);
        inputView.setRelativeInputView(edt);

        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);
        //是否打开语音键盘
        boolean isVoiceOpen = preferencesHelper.getBooleanValue("isVoiceOpen", false);

        if (isVoiceOpen) {
            voiceBtn.setBackground(getResources().getDrawable(R.drawable.ico_add_task_keyboard));
            inputView.setVisibility(View.VISIBLE);
            InputSoftHelper.hideKeyboard(edt);
        } else {
            voiceBtn.setBackground(getResources().getDrawable(R.drawable.ico_add_task_voice));
            inputView.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputSoftHelper.ShowKeyboard(edt);
                }
            }, 100);
        }

        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCurrentInputType()) {
                    preferencesHelper.putBooleanValue("isVoiceOpen", false);
                    voiceBtn.setBackground(getResources().getDrawable(R.drawable.ico_add_task_voice));
                    inputView.setVisibility(View.GONE);
                    InputSoftHelper.ShowKeyboard(edt);
                } else {
                    preferencesHelper.putBooleanValue("isVoiceOpen", true);
                    voiceBtn.setBackground(getResources().getDrawable(R.drawable.ico_add_task_keyboard));
                    inputView.setVisibility(View.VISIBLE);
                    InputSoftHelper.hideKeyboard(edt);
                }

            }
        });
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceBtn.setBackground(getResources().getDrawable(R.drawable.icon_add_task_voice));
                inputView.setVisibility(View.GONE);
                InputSoftHelper.ShowKeyboard(edt);
            }
        });

        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                        String content = edt.getText().toString().trim();
                        addTask(content);
                        edt.setText("");
                        addTaskPop.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        selectexBtn.setOnClickListener(new View.OnClickListener() { //选择执行人
            @Override
            public void onClick(View v) {
                InputSoftHelper.hideKeyboard(edt);
                showSelectExecutor();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() { //添加卡片

            public void onClick(View v) {
                String content = edt.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    addTask(content);
                    edt.setText("");
                    addTaskPop.dismiss();
                } else {
                    Toast.makeText(mContext, "任务内容不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //选择日期
                InputSoftHelper.hideKeyboard(tvDate);
                showSelectCalendar();
            }
        });

        addTaskPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                InputSoftHelper.hideKeyboard(tvDate);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                llAddTask.setVisibility(VISIBLE);

                setBottomKeyBoardStatus();
            }
        });
        lp.alpha = 0.65f;
        getWindow().setAttributes(lp);
        llAddTask.setVisibility(GONE);
        addTaskPop.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_menu_home, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
        isTopSelectUser = false;
    }


    /**
     * 判断键盘形态并设置底部的键盘图标
     */
    private void setBottomKeyBoardStatus() {
        boolean isVoiceOpen1 = preferencesHelper.getBooleanValue("isVoiceOpen", false);
        if (isVoiceOpen1) {
            ivVoice.setBackground(getResources().getDrawable(R.drawable.ico_add_task_keyboard));
        } else {
            ivVoice.setBackground(getResources().getDrawable(R.drawable.ico_add_task_voice));
        }
    }

    /**
     * 显示执行人列表 选择执行人
     */
    private void showSelectExecutor() {
        if (executorPop == null) {
            executorPop = new BaseSelectPopupWindow(mContext, R.layout.pop_add_task_executor);
            executorPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            executorPop.setFocusable(true);
            executorPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            executorPop.setShowTitle(false);
            executorPop.setBackgroundDrawable(new ColorDrawable(0));

            staffView = executorPop.getContentView().findViewById(R.id.staff_view);

            staffView.setOnKeyBoardSelectedUserListener(new ListRecentlySelectedStaffView.OnKeyBoardSelectedUserListener() {
                @Override
                public void onSelected(User user) {
                    currentId = user.getUuid();
                    executorPop.dismiss();
                    if (InputSoftHelper.isSoftShowing(TaskListActivityNew.this)) {
                        InputSoftHelper.hideShowSoft(mContext);
                    }
                }
            });

            executorPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (!getCurrentInputType()) {
                        placeholderView.setVisibility(GONE);
                        if (InputSoftHelper.isSoftShowing(TaskListActivityNew.this)) {
                            InputSoftHelper.hideShowSoft(mContext);
                        }
                    }
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
        }
        if (!getCurrentInputType()) {
            placeholderView.setVisibility(VISIBLE);
        }
        executorPop.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_menu_home, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 判断当前是语音输入模式还是键盘输入模式
     *
     * @return true 语音输入模式 false 键盘输入模式
     */
    public boolean getCurrentInputType() {
        return inputView.getVisibility() == VISIBLE ? true : false;
    }

    /**
     * 显示日历 选择日期
     */
    @SuppressLint("WrongViewCast")
    private void showSelectCalendar() {
        if (calendarPop == null) {
            calendarPop = new BaseSelectPopupWindow(mContext, R.layout.pop_add_task_calendar);
            calendarPop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            calendarPop.setFocusable(true);
            calendarPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            calendarPop.setShowTitle(false);
            calendarPop.setBackgroundDrawable(new ColorDrawable(0));
            TextView currentData = calendarPop.getContentView().findViewById(R.id.tv_currentData);
            ImageView ivLeft = calendarPop.getContentView().findViewById(R.id.iv_left);
            ImageView ivRight = calendarPop.getContentView().findViewById(R.id.iv_right);
            TextView tvToday = calendarPop.getContentView().findViewById(R.id.tv_toady);
            TextView tvTomorrow = calendarPop.getContentView().findViewById(R.id.tv_tomorrow);

            currentData.setText(ViewHelper.formatDateToStr(new Date(), "yyyy-MM"));
            MonthPager monthPager = calendarPop.getContentView().findViewById(R.id.calendar_view);
            CustomDayView customDayView = new CustomDayView(mContext, R.layout.custom_day);
            OnSelectDateListener onSelectDateListener = new OnSelectDateListener() {
                @Override
                public void onSelectDate(CalendarDate date) {
                    selectDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                    calendarPop.dismiss();
                }

                @Override
                public void onSelectOtherMonth(int offset) {
                    //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                    monthPager.selectOtherMonth(offset);
                }
            };
            monthPager.setViewheight(Utils.dpi2px(mContext, 270));
            calendarAdapter = new CalendarViewAdapter(
                    mContext,
                    onSelectDateListener,
                    CalendarAttr.CalendayType.MONTH,
                    customDayView);
            HashMap<String, String> holidayMap = HolidayUtils.getHolidayMap();
            calendarAdapter.setMarkData(holidayMap);


            monthPager.setAdapter(calendarAdapter);
            monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);


            monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
                @Override
                public void transformPage(View page, float position) {
                    position = (float) Math.sqrt(1 - Math.abs(position));
                    page.setAlpha(position);
                }
            });
            monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentCalendars = calendarAdapter.getPagers();
                    if (currentCalendars.get(position % currentCalendars.size()) instanceof Calendar) {
                        CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                        currentData.setText(date.getYear() + "年" + date.getMonth() + "月");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //向前翻页
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
                }
            });

            ivRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //向后翻页
                    monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
                }
            });

            tvToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //今天
                    selectDate = ViewHelper.getDateToday();
                    tvToday.setTextColor(Color.parseColor("#5B8AF2"));
                    tvToday.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_left_select));
                    tvTomorrow.setTextColor(Color.parseColor("#78787C"));
                    tvTomorrow.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_right));
                    calendarPop.dismiss();
                    cancelSelectState(calendarAdapter);
                    calendarAdapter.notifyDataChanged(new CalendarDate());
                }
            });
            tvTomorrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //明天
                    selectDate = ViewHelper.getDateTomorrow();
                    tvTomorrow.setTextColor(Color.parseColor("#5B8AF2"));
                    tvTomorrow.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_right_select));
                    tvToday.setTextColor(Color.parseColor("#78787C"));
                    tvToday.setBackground(getResources().getDrawable(R.drawable.tv_bg_home_task_left));
                    calendarPop.dismiss();
                    cancelSelectState(calendarAdapter);
                    //获取明天的日期
                    String date = ViewHelper.getDateTomorrow();
                    CalendarDate calendarDate = new CalendarDate(
                            ViewHelper.getYearByDate(date),
                            ViewHelper.getMonthByDate(date),
                            ViewHelper.getDayByDate(date));
                    calendarAdapter.notifyDataChanged(calendarDate);
                }
            });
            calendarPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
            calendarPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (!getCurrentInputType()) {
                        if (InputSoftHelper.isSoftShowing(TaskListActivityNew.this)) {
                            InputSoftHelper.hideShowSoft(mContext);
                        }
                    }
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
        }
        calendarPop.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_tasklane_trello, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);


        //如果任务列表已选中日期，打开新建的日历控件，默认选中这天日期
        if (selectCalendarDate != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    calendarAdapter.notifyDataChanged(selectCalendarDate);
                    selectCalendarDate = null;
                }
            });
        }
    }

    /**
     * 日历上选中的日期取消选中
     *
     * @param calendarAdapter
     */
    private void cancelSelectState(CalendarViewAdapter calendarAdapter) {
        ArrayList<Calendar> calendars = calendarAdapter.getPagers();
        for (int i = 0; i < calendars.size(); ++i) {
            Calendar calendar = calendars.get(i);
            calendar.cancelSelectState();
        }
    }


    /**
     * 添加任务
     *
     * @param content
     */
    private void addTask(String content) {
        ProgressDialogHelper.show(mContext, "保存中...");
        Task task = new Task();
        task.setContent(content);
        task.setExecutorIds(currentId);
        task.setBeginTime(selectDate);
        String endTime;
        if (!TextUtils.isEmpty(selectDate) && selectDate.length() >= 10) {
            endTime = selectDate.substring(0, 10);
            task.setEndTime(endTime + " 23:59:59");
        }
        if (InputSoftHelper.isSoftShowing(this)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputSoftHelper.hideShowSoft(mContext);
                }
            }, 100);

        }
        task.setCreatorId(Global.mUser.getUuid());

        String url = Global.BASE_JAVA_URL + GlobalMethord.任务保存;

        StringRequest.postAsyn(url, task, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                setTodayAndCurrentUser();
                Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                refreshList(true);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        });
    }

    private void setTodayAndCurrentUser() {
        //我的任务
        if (viewpager.getCurrentItem() == 0) {
            currentId = Global.mUser.getUuid();
            if (staffView != null) {
                staffView.reloadStaffList(currentId);
            }
        }

        /*//今日任务
        if (viewpager.getCurrentItem() == 0) {
            selectDate = ViewHelper.getDateToday();
            if (calendarAdapter != null)
                calendarAdapter.notifyDataChanged(new CalendarDate());
        }*/
        if (viewpager.getCurrentItem() == 0) {
            if (!TextUtils.isEmpty(taskListFragment.currentDate)) {
                selectDate = taskListFragment.currentDate;
            }
            if (calendarAdapter != null)
                calendarAdapter.notifyDataChanged(new CalendarDate());
        }

        //我的指派
        if (viewpager.getCurrentItem() == 1) {
            if (taskAssignFragment != null) {
                if (!TextUtils.isEmpty(taskAssignFragment.currentUserId)) {
                    currentId = taskAssignFragment.currentUserId;
                }
                if (!TextUtils.isEmpty(taskAssignFragment.currentDate)) {
                    selectDate = taskAssignFragment.currentDate;
                }
                if (staffView != null) {
                    staffView.reloadStaffList(currentId);
                }
                if (calendarAdapter != null)
                    calendarAdapter.notifyDataChanged(new CalendarDate());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOtherUserList = false;
    }


}
