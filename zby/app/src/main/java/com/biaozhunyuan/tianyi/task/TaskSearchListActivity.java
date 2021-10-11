package com.biaozhunyuan.tianyi.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@SuppressLint("NewApi")
public class TaskSearchListActivity extends BaseActivity {

    private TextView tvCancle;
    private EditText etSearch;
    private Demand<Task> myTaskDemand;
    private Demand<Task> myAssignDemand;
    private ImageView ivClear;
    private NoScrollListView lvHistory; //历史记录listview
    private TextView tvMore; //更多历史记录
    private TextView tvClear; //清楚历史记录
    private TextView tvSearchResult; //搜索结果数量
    private SimpleIndicator indicatior;
    private BoeryunViewpager viewPager;

    private ImageView myTaskIV;    //我的任务 背景板
    private ImageView myAssignIV; //我的指派 背景板
    private List<RelativeLayout> mListViewList; //存放我的任务和我的指派listview的集合
    private PullToRefreshAndLoadMoreListView myTaskListView; //我的任务ListView
    private PullToRefreshAndLoadMoreListView myAssignListView; //我的指派ListView
    private RelativeLayout myTaskRl;
    private RelativeLayout myAssignRl;
    private View viewMyTask;
    private View viewAssign;
    private LinearLayout include_result;
    private String currentContent;
    private int currentNum; //搜索结果总数量
    private int myTaskPageIndex = 1;
    private int myAssignPageIndex = 1;
    private CommanAdapter<Task> myTaskAdapter;
    private CommanAdapter<Task> myAssignAdapter;
    private DictionaryHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_search_list);
        helper = new DictionaryHelper(this);
        initView();
//        initData();
        initSearchResultView();
        initDemand();
        setOnTouchEvent();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x111) {
                currentNum += (int) msg.obj;
                tvSearchResult.setText("搜索结果(共" + currentNum + "条)");
            }
        }
    };

    /**
     * 初始化搜索结果中的View
     */
    private void initSearchResultView() {
        mListViewList = new ArrayList<>();
        myTaskListView = new PullToRefreshAndLoadMoreListView(this);
        myAssignListView = new PullToRefreshAndLoadMoreListView(this);

        initListView(); //初始化列表控件

        initBgImageView();//初始化列表空数据时 背景板

        initDataList(); //初始化列表的容器 及添加列表和背景到容器中

        initViewPager(); //初始化viewpager适配器

        initIndicator(); //初始化指示器
    }

    /**
     * 初始化指示器
     */
    private void initIndicator() {
        indicatior.setVisibleTabCount(2);
        indicatior.setTabItemTitles(new String[]{"我的任务", "我指派的"});
        indicatior.setViewPager(viewPager, 0);
    }

    /**
     * 初始化viewpager适配器
     */
    private void initViewPager() {
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mListViewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RelativeLayout view = mListViewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mListViewList.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    /**
     * 初始化列表的容器 及添加列表和背景到容器中
     */
    private void initDataList() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        myTaskRl = new RelativeLayout(this);
        myTaskRl.setLayoutParams(layoutParams);
//        myTaskRl.setOrientation(VERTICAL);
        myTaskRl.addView(myTaskListView);
        myTaskRl.addView(myTaskIV);

        myAssignRl = new RelativeLayout(this);
//        myAssignRl.setOrientation(VERTICAL);
        myAssignRl.setLayoutParams(layoutParams);
        myAssignRl.addView(myAssignListView);
        myAssignRl.addView(myAssignIV);

        //给LinearLayout设置最小高度
//        myTaskRl.setMinimumHeight((int) ViewHelper.dip2px(this, 225));
//        myAssignRl.setMinimumHeight((int) ViewHelper.dip2px(this, 225));

        mListViewList.add(myTaskRl);
        mListViewList.add(myAssignRl);
    }


    /**
     * 初始化列表空数据时 背景板
     */
    private void initBgImageView() {
        myTaskIV = new ImageView(this);
        myAssignIV = new ImageView(this);
        myTaskIV.setBackground(getResources().getDrawable(R.drawable.bg_task_nulldata));
        myAssignIV.setBackground(getResources().getDrawable(R.drawable.bg_task_nulldata));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        myTaskIV.setLayoutParams(params);
        myAssignIV.setLayoutParams(params);
//        myTaskIV.setMinimumHeight((int) ViewHelper.dip2px(mContext, 225));
//        myAssignIV.setMinimumHeight((int) ViewHelper.dip2px(mContext, 225));

//        myTaskIV.setVisibility(GONE); //默认隐藏
//        myAssignIV.setVisibility(GONE);
    }


    /**
     * 初始化列表控件
     */
    private void initListView() {
        myTaskListView.setDivider(getDrawable(R.drawable.list_divider));
        myAssignListView.setDivider(getDrawable(R.drawable.list_divider));
        myTaskListView.setDividerHeight(1);
        myAssignListView.setDividerHeight(1);
        myTaskListView.setIsCanRefresh(false);
        myAssignListView.setIsCanRefresh(false);
    }

//    private void initData() {
//        View view = LayoutInflater.from(this).inflate(R.layout.tv_task_search, null);
//
//    }

    /**
     * 初始化我的任务和我指派的网络请求参数
     */
    private void initDemand() {
        myTaskDemand = new Demand<>(Task.class);
        myTaskDemand.src = Global.BASE_JAVA_URL + GlobalMethord.任务列表 + "?userId=" + Global.mUser.getUuid() + "&isMyTask=true&isShowAllHistory=true";
        myTaskDemand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        myTaskDemand.sort = "desc";
        myTaskDemand.pageSize = Integer.MAX_VALUE;
        myTaskDemand.sortField = "creationTime";
        myTaskDemand.pageIndex = myTaskPageIndex;

        myAssignDemand = new Demand<>(Task.class);
        myAssignDemand.src = Global.BASE_JAVA_URL + GlobalMethord.任务列表 + "?creatorId=" + Global.mUser.getUuid() + "&isMyZhipai=true&isShowAllHistory=true";
        myAssignDemand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        myAssignDemand.sort = "desc";
        myAssignDemand.pageSize = Integer.MAX_VALUE;
        myAssignDemand.sortField = "creationTime";
        myAssignDemand.pageIndex = myAssignPageIndex;
    }

    private void setOnTouchEvent() {
        myAssignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //我的指派列表点击事件
                Task task = (Task) myAssignListView.getItemAtPosition(position);
                skipActivity(task);
            }
        });
        myTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //我的任务列表点击事件
                Task task = (Task) myTaskListView.getItemAtPosition(position);
                skipActivity(task);
            }
        });
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentNum = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    currentNum = 0;
                    currentContent = etSearch.getText().toString();
                    Map<String, String> searchMap = new HashMap<>();
                    searchMap.put("searchField_string_content", "1|" + currentContent);
                    myTaskDemand.keyMap = searchMap;
                    myTaskDemand.pageIndex = myTaskPageIndex = 1;
                    myAssignDemand.keyMap = searchMap;
                    myAssignDemand.pageIndex = myAssignPageIndex = 1;
                    getTaskList(0);
                } else {
                    currentContent = "";
                    currentNum = 0;
                    include_result.setVisibility(GONE);
                    tvSearchResult.setText("搜索结果(共" + currentNum + "条)");
                    myAssignDemand.pageIndex = myAssignPageIndex = 1;
                    myTaskDemand.pageIndex = myTaskPageIndex = 1;
                }
            }
        });
        myTaskListView.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                myTaskDemand.pageIndex = myTaskPageIndex;
                getTaskList(1);
            }
        });
        myAssignListView.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                myAssignDemand.pageIndex = myAssignPageIndex;
                getTaskList(2);
            }
        });
    }


    /**
     * 任务列表
     *
     * @param flag 1 : 只加载我的任务 2 : 只加载我的指派
     */
    private void getTaskList(int flag) {
        include_result.setVisibility(VISIBLE);
        if (flag == 1) {
            getMyTaskList();
        } else if (flag == 2) {
            getAssignList();
        } else {
            getAssignList();
            getMyTaskList();
        }
    }

    /**
     * 我指派的任务列表
     */
    private void getAssignList() {
        currentNum = 0;
        myAssignDemand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String total = JsonUtils.getStringValue(JsonUtils.pareseData(response), "total");
                    Message message = handler.obtainMessage();
                    message.obj = Integer.parseInt(total);
                    message.what = 0x111;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<Task> data = myAssignDemand.data;
                for (final Task task : data) {
                    try {
                        task.setCreatorName(myAssignDemand.getDictName(task, "creatorId"));
                        task.setExecutorNames(myAssignDemand.getDictName(task, "executorIds"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (data != null) {
                    myAssignIV.setVisibility(GONE);
                    myTaskListView.setVisibility(VISIBLE);
                    Iterator<Task> iterator = data.iterator();
                    while (iterator.hasNext()) {
                        Task t = iterator.next();
                        if (t.getStatus().equals(TaskStatusEnum.已完成.getName())
                                || t.getStatus().equals(TaskStatusEnum.已取消.getName())) {
                            iterator.remove(); //把已完成和已取消的任务先拿出去
                        }
                    }
                    myAssignAdapter = getTaskAdapter(data);
                    myAssignListView.setAdapter(myAssignAdapter);
                } else {
                    myAssignIV.setVisibility(VISIBLE);
                    myAssignListView.setVisibility(GONE);
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

    /**
     * 我的任务列表
     */
    private void getMyTaskList() {
        currentNum = 0;
        myTaskDemand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String total = JsonUtils.getStringValue(JsonUtils.pareseData(response), "total");
                    Message message = handler.obtainMessage();
                    message.obj = Integer.parseInt(total);
                    message.what = 0x111;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<Task> data = myTaskDemand.data;
                if (data != null) {
                    myTaskIV.setVisibility(GONE);
                    myTaskListView.setVisibility(VISIBLE);

                    myTaskAdapter = getTaskAdapter(data);
                    myTaskListView.setAdapter(myTaskAdapter);
                } else {
                    myTaskIV.setVisibility(VISIBLE);
                    myTaskListView.setVisibility(GONE);
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

    private void initView() {
        tvCancle = findViewById(R.id.tv_cancle);
        etSearch = findViewById(R.id.et_search);
        ivClear = findViewById(R.id.iv_clear);
        lvHistory = findViewById(R.id.lv_history);
        tvMore = findViewById(R.id.tv_more_history);
        tvClear = findViewById(R.id.tv_clear_history);
        include_result = findViewById(R.id.include_result);
        tvSearchResult = findViewById(R.id.tv_search_result);
        indicatior = findViewById(R.id.simpleindicatior);
        viewPager = findViewById(R.id.boeryun_viewpager);
    }

    private CommanAdapter<Task> getTaskAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, this, R.layout.item_task_list_new) {

            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                viewHolder.getView(R.id.ll_task_time).setVisibility(View.VISIBLE);
                TextView time = viewHolder.getView(R.id.time_item_task_list);
                time.setText(DateTimeUtil.dateformatTime(ViewHelper.formatStrToDateAndTime(item.getBeginTime()), false));
                if (DateTimeUtil.getBetweenDays(ViewHelper.getDateToday(),
                        ViewHelper.formatStrToStr(item.getEndTime(), "yyyy-MM-dd")) >= 0) {
                    //结束时间大于今天时间 说明没有预期
                    time.setTextColor(Color.parseColor("#000000"));
                } else {
                    //结束时间小于今天时间 说明任务逾期
                    time.setTextColor(Color.parseColor("#dc1414"));
                }
                if (!item.getCreatorId().equals(item.getExecutorIds())) {
                    viewHolder.setTextValue(R.id.assign_item_task_list, helper.getUserNameById(item.getCreatorId()));
                } else {
                    viewHolder.setTextValue(R.id.assign_item_task_list, "");
                }
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNameById(item.getCreatorId()));
                TextView content = viewHolder.getView(R.id.task_content);
                if (item.getContent().contains(currentContent)) {
                    int startIndex = item.getContent().indexOf(currentContent);
                    int endIndex = startIndex + currentContent.length();
                    SpannableStringBuilder spannable = new SpannableStringBuilder(item.getContent());
                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#2D7FC7")),
                            startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    content.setText(spannable);
                } else {
                    content.setText(item.getContent());
                }


                final ImageView ivStatus = viewHolder.getView(R.id.task_status);
                /**
                 * 根据任务状态枚举类型显示状态
                 */
                if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_finish);
                } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                }
                ivStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getExecutorIds().equals(Global.mUser.getUuid())) {
                            if (TaskStatusEnum.进行中.getName().equals(item.getStatus())
                                    || TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                                saveTask(item, 1);
                            } else if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
                                saveTask(item, 3);
                            } else {
                                showShortToast("当前任务状态下不能修改任务状态!");
                            }
                        } else {
                            showShortToast("不是自己的任务不可以修改任务状态!");
                        }

                    }
                });
            }
        };
    }

    /**
     * 完成任务
     *
     * @param status 任务状态：1==已完成，3==未完成
     */
    private void saveTask(Task task, int status) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.改变任务状态 + "?uuid=" + task.getUuid() + "&ticket=" + status;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                getTaskList(0);
                showShortToast("修改任务状态成功!");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast(JsonUtils.pareseMessage(result));
            }
        });
    }

    /**
     * 跳转任务详情
     *
     * @param task 跳转携带参数
     */
    private void skipActivity(Task task) {
        Intent intent = new Intent(this, TaskInfoActivityNew.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskInfo", task);
        intent.putExtra("taskIntentInfo", bundle);
        startActivity(intent);
    }

}
