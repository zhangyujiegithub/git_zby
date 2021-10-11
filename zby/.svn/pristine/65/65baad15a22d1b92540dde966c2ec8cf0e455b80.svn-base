package com.biaozhunyuan.tianyi.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;
import okhttp3.Request;

/**
 * 任务泳道图列表
 */

public class TaskLaneFragment extends Fragment {


    private PagerContainer pagerContainer;
    private List<Task> taskList;
    private ViewPager viewPager;
    private List<Task> taskDelayList = new ArrayList<Task>();
    private List<Task> taskDoingList = new ArrayList<Task>();
    private List<Task> taskPlanList = new ArrayList<Task>();
    public static boolean isReasume = false;
    private Demand<Task> demand;
    private String[] colors = new String[]{"#67B5FE", "#FF7F66", "#00E0DF"};
    private String[] tittles = new String[]{"已逾期", "本周在做", "计划中"};
    private List<TaskLaneAdapter> lists = new ArrayList<>();
    private TaskLaneViewPager taskLaneViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_lane_new, null);
        initView(view);
        initDemand();
        getTaskList();
        return view;
    }

    @Override
    public void onStart() {
        if (isReasume) {
            getTaskList();
            isReasume = false;
        }
        super.onStart();
    }

    private void initView(View view) {
        pagerContainer = view.findViewById(R.id.pager_container);
        viewPager = view.findViewById(R.id.view_pager);
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务过滤;
        demand = new Demand<>(Task.class);
        demand.pageSize = 50;
        demand.sort = "desc";
        demand.sortField = "creationTime";
        demand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        demand.src = url;
    }


    /**
     * 获取列表信息并展示
     */
    private void getTaskList() {
        ProgressDialogHelper.show(getActivity());
        StringRequest.postAsyn(demand.src, demand, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i(response);
                try {
                    taskList = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), Task.class);
                    if (taskList != null && taskList.size() > 0) {
                        lists.clear();
                        taskDelayList.clear();
                        taskDoingList.clear();
                        taskPlanList.clear();
                        for (Task task : taskList) {
                            Date date = DateTimeUtil.ConvertStringDateToDate(task.getEndTime());//获取任务的结束时间
                            List<String> list = ViewHelper.getDateThisWeeks();
                            Date date1 = DateTimeUtil.ConvertStringDateToDate(list.get(0));  //得到本周的周一
                            Date date2 = DateTimeUtil.ConvertStringDateToDate(list.get(list.size() - 1));//得到本周周末
                            if (date.before(date1) && TaskStatusEnum.已逾期.getName().equals(task.getStatus())) {       //如果任务的执行时间在本周一之前，属于延迟任务
                                taskDelayList.add(task);
                            } else if (date.after(date2)) {  //如果任务的执行时间在本周周末之后，属于计划任务
                                taskPlanList.add(task);
                            } else {        //否则属于正在做的任务
                                taskDoingList.add(task);
                            }
                        }

                        TaskLaneAdapter taskDelayAdapter = new TaskLaneAdapter(taskDelayList, getActivity());
                        TaskLaneAdapter taskDoingAdapter = new TaskLaneAdapter(taskDoingList, getActivity());
                        TaskLaneAdapter taskPlanAdapter = new TaskLaneAdapter(taskPlanList, getActivity());

                        lists.add(taskDelayAdapter);
                        lists.add(taskDoingAdapter);
                        lists.add(taskPlanAdapter);

                        initViewPager();

                        ProgressDialogHelper.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void initViewPager() {
        if (taskLaneViewPager == null) {
            taskLaneViewPager = new TaskLaneViewPager();
            viewPager.setAdapter(taskLaneViewPager);
            viewPager.setClipChildren(false);
            viewPager.setOffscreenPageLimit(3);
            if (isAdded()) {
                new CoverFlow.Builder()
                        .with(viewPager)
                        .scale(0.2f)
                        .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                        .spaceSize(0f)
                        .build();
            }
        } else {
            taskLaneViewPager = new TaskLaneViewPager();
            viewPager.setAdapter(taskLaneViewPager);
        }
    }

    private class TaskLaneViewPager extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_cover, null);
            TextView tvTittle = view.findViewById(R.id.tv_tittle);
            CardView cardView = view.findViewById(R.id.cardview);
            final ListView lv = view.findViewById(R.id.lv);
            cardView.setCardBackgroundColor(Color.parseColor(colors[position]));
            tvTittle.setText(tittles[position]);
            lv.setAdapter(lists.get(position));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
                    Task task = (Task) lv.getAdapter().getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtra("taskIntentInfo", bundle);
                    startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}
