package com.biaozhunyuan.tianyi.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.DictionaryQueryDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.RESULT_SELECT_USER;

/**
 * Created by 王安民 on 2017/8/22.
 * <p>
 * 任务列表页面
 */

public class TaskListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;

    public static boolean isResume = false;

    private SimpleIndicator tabView;
    private ORMDataHelper dataHelper;
    private BoeryunViewpager viewpager;
    private List<Fragment> mFragments;
    private TaskDayViewFragment taskDayViewFragment;
    private DrawerLayout mDrawerLayout;
    private LinearLayout menu_right;
    private TextView confirm;
    private TextView reset;
    private User currentUser = Global.mUser;
    private TextView drawer_type;
    private TextView drawer_dept;
    private TextView drawer_project;
    private TagFlowLayout tgf_type;
    private TagFlowLayout tgf_dept;
    private TagFlowLayout tgf_project;
    private List<字典> typeList;
    private List<字典> deptList;
    private List<字典> projectList;
    private TaskListFragment taskListFragment;
    private TaskFilter mFilter;
    private ImageView arrow_project;
    private ImageView arrow_type;
    private ImageView arrow_dept;
    private DictionaryQueryDialogHelper dictionaryQueryDialogProject;
    private DictionaryQueryDialogHelper dictionaryQueryDialogDept;
    private TaskWeekFragment taskWeekViewFragment;
    private TaskBoardFragment taskBoardFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initViews();
        initData();
        setOnEvent();
    }

    private void initData() {
        dataHelper = ORMDataHelper.getInstance(this);
        typeList = new ArrayList<>();
        deptList = new ArrayList<>();
        projectList = new ArrayList<>();
        dictionaryQueryDialogProject = DictionaryQueryDialogHelper.getInstance(this);
        dictionaryQueryDialogDept = DictionaryQueryDialogHelper.getInstance(this);
        int screenWidth = ViewHelper.getScreenWidth(this);
        ViewGroup.LayoutParams params = menu_right.getLayoutParams();
        params.width = screenWidth / 5 * 3;

        menu_right.setLayoutParams(params);

        tabView.setTabItemTitles(new String[]{"日视图", "周视图", "列表", "看板", "周期任务"});
        taskDayViewFragment = new TaskDayViewFragment();
        taskListFragment = new TaskListFragment();
        taskWeekViewFragment = new TaskWeekFragment();
        taskBoardFragment = new TaskBoardFragment();
        mFragments = new ArrayList<Fragment>();
        mFragments.add(taskDayViewFragment);
        mFragments.add(taskWeekViewFragment);
        mFragments.add(taskListFragment);
//        mFragments.add(new TaskLaneFragment());
        mFragments.add(taskBoardFragment);
        mFragments.add(new TaskPeriodicFragment());
//        mFragments.add(new TaskLaneFragment());
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
        viewpager.setOffscreenPageLimit(4);//预加载
        tabView.setViewPager(viewpager, 0);

        getTaskType();
        getTaskDept();
        getTaskProject();
    }

    /**
     * 刷新各界面任务列表 同步数据
     */
    public void refreshList(boolean isRefreshWeek){
        taskDayViewFragment.pageIndex = 1;
        taskDayViewFragment.getTaskList();
        taskListFragment.pageIndex = 1;
        taskListFragment.getTaskList();
        if(isRefreshWeek){
            taskWeekViewFragment.getTaskList();
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
                projectList.add(new 字典("", "更多"));
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
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        final DictData dictData = new DictData();
        dictData.setDictionaryName("base_department");
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                int size = 5;
                if (字典s.size() <= 5) {
                    size = 字典s.size();
                }
                deptList.add(0, new 字典("", "全部"));
                for (int i = 0; i < size; i++) {
                    deptList.add(字典s.get(i));
                }
                deptList.add(new 字典("", "更多"));
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
                mFilter = new TaskFilter(typeList.get(0).getUuid(), "", "");
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
            super.onBackPressed();
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_task_list);
        tabView = (SimpleIndicator) findViewById(R.id.indicator_task_view);
        viewpager = (BoeryunViewpager) findViewById(R.id.viewpager_task_view);
        menu_right = (LinearLayout) findViewById(R.id.menu_right);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        confirm = findViewById(R.id.tv_drawer_confirm);
        reset = findViewById(R.id.tv_drawer_reset);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        drawer_type = findViewById(R.id.tv_drawer_customer);
        drawer_dept = findViewById(R.id.tv_drawer_dept);
        drawer_project = findViewById(R.id.tv_drawer_project);

        arrow_project = findViewById(R.id.iv_project);
        arrow_type = findViewById(R.id.iv_type);
        arrow_dept = findViewById(R.id.iv_dept);

        tgf_type = findViewById(R.id.tgf_task_type);
        tgf_dept = findViewById(R.id.tgf_task_dept);
        tgf_project = findViewById(R.id.tgf_task_project);

    }

    private void initTagFlowAdapter(List<字典> list, TagFlowLayout tgf) {
        TagAdapter<字典> tagAdapter = new TagAdapter<字典>(list) {
            @Override
            public View getView(FlowLayout parent, int position, 字典 t) {
                Logger.i("tagA" + position + "--" + t);
                TextView tv = (TextView) LayoutInflater.from(TaskListActivity.this).inflate(
                        R.layout.item_label_customer_list, tgf, false);
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

        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {

            }

            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {
                mDrawerLayout.openDrawer(menu_right);
            }

            @Override
            public void onClickSaveOrAdd() {
                Intent intent = new Intent(TaskListActivity.this, TaskNewActivity.class);
                startActivity(intent);
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
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(menu_right);
                taskListFragment.getFilterList(mFilter);
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
                if (position == 2) {
                    headerView.setFilterIconVisible(true);
                } else {
                    headerView.setFilterIconVisible(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        findViewById(R.id.ll_drawer_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_type.getVisibility() == View.GONE) {
                    tgf_type.setVisibility(View.VISIBLE);
                    arrow_type.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_type.setVisibility(View.GONE);
                    arrow_type.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        findViewById(R.id.ll_drawer_dept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_dept.getVisibility() == View.GONE) {
                    tgf_dept.setVisibility(View.VISIBLE);
                    arrow_dept.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_dept.setVisibility(View.GONE);
                    arrow_dept.setImageResource(R.drawable.arrow_right);
                }
            }
        });
        findViewById(R.id.ll_drawer_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tgf_project.getVisibility() == VideoView.GONE) {
                    tgf_project.setVisibility(View.VISIBLE);
                    arrow_project.setImageResource(R.drawable.arrow_down);
                } else {
                    tgf_project.setVisibility(View.GONE);
                    arrow_project.setImageResource(R.drawable.arrow_right);
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
                    mFilter.setDeparmentId(z.getUuid());
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
                mFilter.setDeparmentId(dict.getUuid());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS: //选择执行人
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        try {
                            List<User> users = userList.getUsers();
                            taskDayViewFragment.receiveSelectedUser(users.get(0));
                            taskListFragment.receiveSelectedUser(users.get(0));
                        } catch (IndexOutOfBoundsException e) {
                            showShortToast("没有选择执行人");
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
        drawer_project.setText(projectList.get(0).getName());
        drawer_dept.setText(deptList.get(0).getName());
        drawer_type.setText(typeList.get(0).getName());
        mFilter.setDictSchedulType(typeList.get(0).getUuid());
        mFilter.setProjectId("");
        mFilter.setDeparmentId("");
    }
}
