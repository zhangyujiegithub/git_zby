package com.biaozhunyuan.tianyi.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.biaozhunyuan.tianyi.view.bragboard.DragBoardView;
import com.biaozhunyuan.tianyi.view.bragboard.model.DragColumn;
import com.biaozhunyuan.tianyi.view.bragboard.utils.AttrAboutPhone;
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

/**
 * 仿trello式,可拖拽item控制任务进度
 */
@SuppressLint("NewApi")
public class TaskLaneTrelloActivity extends BaseActivity {

    private static final int REQUEST_SELECT_BOARDSTAFF = 0x101;
    private ColumnAdapter mAdapter;
    public DragBoardView dragBoardView;
    private List<DragColumn> mData = new ArrayList<>();
    private OaWorkTaskPanelList mBoard; //看板条目对象,从看板列表传进来的
    private TaskBoard mTaskBoard; //包括该看板 列,列中条目等数据对象
    //    private DrawerLayout mDrawerUser; //侧拉菜单 成员列表
    private DrawerLayout mDrawerLayout;//侧拉菜单 过滤列表
    //    private RelativeLayout menu_right_user; //成员列表父容器
    private LinearLayout menu_right;//过滤列表父容器
    private LinearLayout ll_adduser;
    private ListView mMenuLv;
    private CommanAdapter<User> adapter;
    private TagFlowLayout tgf_executor; //过滤执行人
    private TagFlowLayout tgf_dept; //过滤部门
    private LinearLayout ll_user;//过滤执行人容器
    private LinearLayout ll_dept;//过滤部门容器
    private ImageView iv_user;
    private ImageView iv_dept;
    private TextView tv_confirm; //确定
    private TextView tv_reset; //重置
    private List<字典> typeList;
    private List<字典> deptList;
    private List<字典> statusList;
    private List<字典> projectList;
    private List<User> staffList;
    private String departmentId = "";
    private String executorId = "";
    private String projectId = "";
    private String beginTime = "";
    private String endTime = "";
    private String taskType = "";
    private String taskStatus = "";
    private LinearLayout ll_taskFilter; //显示列表过滤
    private RelativeLayout rl_staffList; //显示成员列表
    private LinearLayout ll_project;
    private LinearLayout ll_type;
    private LinearLayout ll_status;
    private ImageView iv_project;
    private ImageView iv_type;
    private ImageView iv_status;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (current == 0) {
                    mDrawerLayout.closeDrawers();
                    current = -1;
                } else {
                    current = 0;
                    ll_taskFilter.setVisibility(View.VISIBLE);
                    rl_staffList.setVisibility(View.GONE);
                    mDrawerLayout.openDrawer(menu_right);
                }

            } else if (msg.what == 1) {
                if (current == 1) {
                    mDrawerLayout.closeDrawers();
                    current = -1;
                } else {
                    current = 1;
                    ll_taskFilter.setVisibility(View.GONE);
                    rl_staffList.setVisibility(View.VISIBLE);
                    mDrawerLayout.openDrawer(menu_right);
                }
            }
        }
    };
    private TagFlowLayout tgf_status;
    private TagFlowLayout tgf_type;
    private TagFlowLayout tgf_project;
    private TextView tvTitle; //看板标题
    private EditText etTitle;
    private LinearLayout ll_parent;
    private int current = -1;
    private DictionaryHelper helper;
    private TagAdapter<User> userTagAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tasklane_trello);
        ImmersionBar.with(this).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).fitsSystemWindows(true).init();
        initView();
        getIntentData();
        initData();
//        initDemand();
        initStaffListData(new ArrayList<>());
        getDataAndRefreshView();
        getTaskDept();
        getTaskStatus();
        getTaskType();
        getTaskProject();
        setOnTouch();
    }

    /**
     * 打开或者关闭 任务列表过滤和成员列表
     *
     * @param what 0 打开列表过滤 1 打开成员列表
     */
    private void openAndCloseDrawer(int what) {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawers();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        handler.sendEmptyMessage(what);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            handler.sendEmptyMessage(what);
        }
    }

    private void setOnTouch() {
        findViewById(R.id.iv_back_headerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(menu_right);
                } else {
                    finish();
                }
            }
        });
        findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(menu_right);
                } else {
                    finish();
                }
            }
        });
        findViewById(R.id.iv_filter_headerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAndCloseDrawer(0);
            }
        });
        findViewById(R.id.iv_add_headerview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAndCloseDrawer(1);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(menu_right);
            }
        });
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
        ll_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskLaneTrelloActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "添加看板成员");
                startActivityForResult(intent, REQUEST_SELECT_BOARDSTAFF);
            }
        });
        ll_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_executor.getVisibility() == View.GONE) {
                    tgf_executor.setVisibility(View.VISIBLE);
                    iv_user.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_executor.setVisibility(View.GONE);
                    iv_user.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        ll_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_dept.getVisibility() == View.GONE) {
                    tgf_dept.setVisibility(View.VISIBLE);
                    iv_dept.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_dept.setVisibility(View.GONE);
                    iv_dept.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        ll_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_project.getVisibility() == View.GONE) {
                    tgf_project.setVisibility(View.VISIBLE);
                    iv_project.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_project.setVisibility(View.GONE);
                    iv_project.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        ll_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_type.getVisibility() == View.GONE) {
                    tgf_type.setVisibility(View.VISIBLE);
                    iv_type.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_type.setVisibility(View.GONE);
                    iv_type.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        ll_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_status.getVisibility() == View.GONE) {
                    tgf_status.setVisibility(View.VISIBLE);
                    iv_status.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_status.setVisibility(View.GONE);
                    iv_status.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        tgf_executor.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                User z = new User();
                for (Integer pos : selectPosSet) {
                    z = staffList.get(pos);
                    tgf_executor.clearSelected();
                    tgf_executor.getAdapter().setSelectedList(pos);
                }
                executorId = z.getUuid();
            }
        });
        tgf_dept.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = deptList.get(pos);
                }
                departmentId = z.getUuid();
            }
        });
        tgf_project.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = projectList.get(pos);
                }
                projectId = z.getUuid();
            }
        });
        tgf_type.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = typeList.get(pos);
                }
                taskType = z.getUuid();
            }
        });
        tgf_status.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                字典 z = new 字典("", "");
                for (Integer pos : selectPosSet) {
                    z = statusList.get(pos);
                }
                taskStatus = z.getUuid();
            }
        });
        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(menu_right);
                getDataAndRefreshView(false);
            }
        });
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getVisibility() == View.GONE) {
                    etTitle.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.GONE);
                    etTitle.requestFocus();
                    InputSoftHelper.ShowKeyboard(etTitle);
                    etTitle.setHint(tvTitle.getText().toString());
                }
            }
        });

        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() { //监听输入框焦点变化
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //获得焦点不作处理
                } else {
                    //失去焦点隐藏输入框 自动保存标题 如果为空 不修改
                    etTitle.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.VISIBLE);
                    String title = etTitle.getText().toString().trim();
                    if (!TextUtils.isEmpty(title)) {
                        updateLaneColumnTitle(mTaskBoard.getUuid(), title, tvTitle);
                        etTitle.setText("");
                    }
                }
            }
        });

        etTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) { //点击软键盘回车键时 使EditText失去焦点
                    etTitle.clearFocus();
                    InputSoftHelper.hiddenSoftInput(TaskLaneTrelloActivity.this, etTitle);
                }
                return false;
            }
        });
        ll_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_parent.setFocusable(true);
                ll_parent.setFocusableInTouchMode(true);
                ll_parent.requestFocus();
                InputSoftHelper.hideKeyboard(etTitle);
            }
        });
    }

    /**
     * 获取看板uuid
     *
     * @return 看板uuid
     */
    public String getBoardUuid() {
        return mBoard.getUuid();
    }

    private void getIntentData() {
        if (getIntent().getSerializableExtra("TaskBoard") != null) {
            mBoard = (OaWorkTaskPanelList) getIntent().getSerializableExtra("TaskBoard");
            tvTitle.setText(mBoard.getTitle());
        }
    }

    private void initData() {
        helper = new DictionaryHelper(this);
//        mAdapter.setData(mData);
//        dragBoardView.setHorizontalAdapter(mAdapter);
        typeList = new ArrayList<>();
        deptList = new ArrayList<>();
        projectList = new ArrayList<>();
        statusList = new ArrayList<>();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AttrAboutPhone.saveAttr(this);
        AttrAboutPhone.initScreen(this);
        super.onWindowFocusChanged(hasFocus);
    }

    private void initView() {
        ll_parent = findViewById(R.id.ll_parent);
        etTitle = findViewById(R.id.et_title);
        tvTitle = findViewById(R.id.tv_title_headerview);
        dragBoardView = findViewById(R.id.layout_main);
//        mDrawerUser = findViewById(R.id.drawerlayout_user);
        mDrawerLayout = findViewById(R.id.drawerlayout_filter);
        menu_right = findViewById(R.id.menu_right);
//        menu_right_user = findViewById(R.id.menu_right_user);
//        mDrawerUser.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
//        mDrawerUser.setScrimColor(Color.TRANSPARENT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ll_adduser = findViewById(R.id.ll_adduser);
        mMenuLv = findViewById(R.id.drawerlayout_lv);
        tgf_executor = findViewById(R.id.tgf_task_executor);
        tgf_dept = findViewById(R.id.tgf_task_dept);
        tgf_status = findViewById(R.id.tgf_task_status);
        tgf_type = findViewById(R.id.tgf_task_type);
        tgf_project = findViewById(R.id.tgf_task_project);
        ll_user = findViewById(R.id.ll_drawer_executor);
        ll_dept = findViewById(R.id.ll_drawer_dept);
        ll_project = findViewById(R.id.ll_drawer_project);
        ll_type = findViewById(R.id.ll_drawer_type);
        ll_status = findViewById(R.id.ll_drawer_status);
        iv_user = findViewById(R.id.iv_executor);
        iv_dept = findViewById(R.id.iv_dept);
        iv_project = findViewById(R.id.iv_project);
        iv_type = findViewById(R.id.iv_type);
        iv_status = findViewById(R.id.iv_status);

        tv_confirm = findViewById(R.id.tv_drawer_confirm);
        tv_reset = findViewById(R.id.tv_drawer_reset);

        ll_taskFilter = findViewById(R.id.ll_taskfilter);
        rl_staffList = findViewById(R.id.rl_staffList);

        int screenWidth = ViewHelper.getScreenWidth(this);
        ViewGroup.LayoutParams params = menu_right.getLayoutParams();
        params.width = screenWidth / 5 * 3;
        menu_right.setLayoutParams(params);
    }

    /**
     * Drawerlayout back键关闭
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(menu_right)) {
            mDrawerLayout.closeDrawers();
        } else if (etTitle.getVisibility() == View.VISIBLE) {
            InputSoftHelper.hideKeyboard(etTitle);
            etTitle.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 获取部门字典
     */
    private void getTaskDept() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        final DictData dictData = new DictData();
        dictData.setDictionaryName("base_department");
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                deptList.add(0, new 字典("", "全部"));
                for (int i = 0; i < 字典s.size(); i++) {
                    deptList.add(字典s.get(i));
                }
                initTagFlowAdapter(deptList, tgf_dept);
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
     * 获取项目字典
     */
    private void getTaskProject() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        final DictData dictData = new DictData();
        dictData.setDictionaryName("crm_project");
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                projectList.add(0, new 字典("", "全部"));
                for (int i = 0; i < 字典s.size(); i++) {
                    projectList.add(字典s.get(i));
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

    /**
     * 获取任务类型字典
     */
    private void getTaskType() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        final DictData dictData = new DictData();
        dictData.setDictionaryName("dict_schedul_type");
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                typeList.add(0, new 字典("", "全部"));
                for (int i = 0; i < 字典s.size(); i++) {
                    typeList.add(字典s.get(i));
                }
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
     * 获取任务状态字典
     */
    private void getTaskStatus() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        final DictData dictData = new DictData();
        dictData.setDictionaryName("oa_work_scheduling_status");
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                statusList.add(0, new 字典("", "全部"));
                for (int i = 0; i < 字典s.size(); i++) {
                    statusList.add(字典s.get(i));
                }
                initTagFlowAdapter(statusList, tgf_status);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
            }
        });

    }

    private void initTagFlowAdapter(List<字典> list, TagFlowLayout tgf) {
        TagAdapter<字典> tagAdapter = new TagAdapter<字典>(list) {
            @Override
            public View getView(FlowLayout parent, int position, 字典 t) {
                Logger.i("tagA" + position + "--" + t);
                TextView tv = (TextView) LayoutInflater.from(TaskLaneTrelloActivity.this).inflate(
                        R.layout.item_label_customer_list, tgf, false);
                tv.setText(t.getName());
                return tv;
            }
        };
        tgf.setMaxSelectCount(1);
        tgf.setAdapter(tagAdapter);
        tagAdapter.setSelectedList(0);
    }

    /**
     * 获取看板详情 泳道数据和成员列表
     */
    private void getDataAndRefreshView() {
        getDataAndRefreshView(true);
    }

    /**
     * 获取看板详情 泳道数据和成员列表
     *
     * @param isReloadStaffList 是否重新加载成员列表
     */
    private void getDataAndRefreshView(boolean isReloadStaffList) {
        ProgressDialogHelper.show(this);
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情
                + mBoard.getUuid() + "&deparmentId=" + departmentId + "&executorIds=" + executorId
                + "&beginTime=" + beginTime + "&endTime=" + endTime + "";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    mTaskBoard = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), TaskBoard.class);
                    initLaneListData(mTaskBoard.getOaWorkLaneList());
                    if (isReloadStaffList) {
                        staffList = mTaskBoard.getStaffList();
                        if (staffList != null && staffList.size() > 0) {
                            User user = new User();
                            user.setName("全部");
                            user.setUuid("");
                            staffList.add(0, user);
                            initStaffListData(staffList);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ProgressDialogHelper.dismiss();
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
     * 看板泳道列表
     */
    private void initLaneListData(List<OaWorkLaneList> oaWorkLaneList) {
        mData.clear();
        for (int i = 0; i < oaWorkLaneList.size(); i++) {
            mData.add(new OaWorkLaneList(
                    oaWorkLaneList.get(i).getTitle(), oaWorkLaneList.get(i).getOaWorkTaskPanelIid(),
                    oaWorkLaneList.get(i).getType(), oaWorkLaneList.get(i).getUuid(),
                    oaWorkLaneList.get(i).getWorkScheduleList()));
        }
        if (mAdapter == null) {
            mAdapter = new ColumnAdapter(this);
            mAdapter.setData(mData);
            dragBoardView.setHorizontalAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 看板成员列表
     */
    private void initStaffListData(List<User> staffList) {
        adapter = getAdapter(staffList);
        mMenuLv.setAdapter(adapter);

        List<User> list = new ArrayList<>();
//        User user = new User();
//        user.setName("全部");
//        user.setUuid("");
//        list.add(user);
        if (staffList != null && staffList.size() > 0) {
            for (User user1 : staffList) {
                list.add(user1);
            }
        }
        userTagAdapter = new TagAdapter<User>(list) {
            @Override
            public View getView(FlowLayout parent, int position, User t) {
                Logger.i("tagA" + position + "--" + t);
                TextView tv = (TextView) LayoutInflater.from(TaskLaneTrelloActivity.this).inflate(
                        R.layout.item_label_customer_list, tgf_executor, false);
                tv.setText(t.getName());
                return tv;
            }
        };

        tgf_executor.setAdapter(userTagAdapter);
        tgf_executor.setMaxSelectCount(1);
        userTagAdapter.setSelectedList(0);
    }


    private CommanAdapter<User> getAdapter(List<User> userList) {
        return new CommanAdapter<User>(userList, this, R.layout.item_taskmenu_list) {

            @Override
            public void convert(int position, final User item, final BoeryunViewHolder viewHolder) {
                viewHolder.setUserPhotoById(R.id.iv_head, item);
                viewHolder.setTextValue(R.id.tv_username, item.getName());
                viewHolder.getView(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.remove(item);
                        removeBoardUser(item, position);
                    }
                });
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_SELECT_BOARDSTAFF:
                        if (userList != null) {
                            try {
                                List<User> users = userList.getUsers();
                                if (adapter != null) {
                                    adapter.addBottom(users, false);
                                }
                                String oaWorkTeamStaffId = "";
                                for (User uu : users) {
                                    if (!TextUtils.isEmpty(uu.getUuid())) {
                                        oaWorkTeamStaffId += uu.getUuid() + ",";
                                    }
                                }
                                addBoardUserList(users, oaWorkTeamStaffId.substring(0, oaWorkTeamStaffId.length() - 1));
                                userTagAdapter.addBottom(users, false);
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case REQUEST_SELECT_EXCUTORS:
                        if (userList != null) {
                            try {
                                List<User> users = userList.getUsers();
                                mAdapter.receiveSelectedUser(users.get(0));
                            } catch (IndexOutOfBoundsException e) {
                                showShortToast("没有选择执行人");
                            }
                        }
                        break;
                }
            }
        }
    }

    /**
     * 看板成员列表添加成员
     *
     * @param users 需要添加的成员集合,如果接口返回失败则从列表移除
     */
    private void addBoardUserList(List<User> users, String oaWorkTeamStaffId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情添加成员
                + "?oaWorkTaskPanel=" + mBoard.getUuid() + "&oaWorkTeamStaffId=" + oaWorkTeamStaffId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                adapter.removeList(users);
                showShortToast("添加失败");
            }

            @Override
            public void onResponseCodeErro(String result) {
                adapter.removeList(users);
                showShortToast("添加失败");
            }
        });
    }

    /**
     * 看板成员列表移除成员
     *
     * @param item 需要移除的成员
     */
    private void removeBoardUser(User item, int position) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情移除成员
                + "?oaWorkTaskPanel=" + mBoard.getUuid() + "&oaWorkTeamStaffId=" + item.getUuid();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                adapter.addData(item, position);
                showShortToast("移除失败");
            }

            @Override
            public void onResponseCodeErro(String result) {
                adapter.addData(item, position);
                showShortToast("移除失败");
            }
        });

    }

    /**
     * 重置过滤器
     */
    private void reset() {
        tgf_dept.getAdapter().setSelectedList(0);
        tgf_executor.getAdapter().setSelectedList(0);
        tgf_project.getAdapter().setSelectedList(0);
        tgf_status.getAdapter().setSelectedList(0);
        tgf_type.getAdapter().setSelectedList(0);
        departmentId = "";
        executorId = "";
        projectId = "";
        beginTime = "";
        endTime = "";
        taskType = "";
        taskStatus = "";
    }

    /**
     * 修改看板标题
     *
     * @param uuid  看板的uuid
     * @param title 修改后的内容
     */
    private void updateLaneColumnTitle(String uuid, String title, TextView textView) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.新建任务看板
                + "?uuid=" + uuid + "&title=" + title;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String title1 = JsonUtils.getStringValue(JsonUtils.pareseData(response), "title");
                    textView.setText(title1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(TaskLaneTrelloActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}