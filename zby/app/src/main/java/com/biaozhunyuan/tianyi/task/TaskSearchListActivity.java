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
    private NoScrollListView lvHistory; //????????????listview
    private TextView tvMore; //??????????????????
    private TextView tvClear; //??????????????????
    private TextView tvSearchResult; //??????????????????
    private SimpleIndicator indicatior;
    private BoeryunViewpager viewPager;

    private ImageView myTaskIV;    //???????????? ?????????
    private ImageView myAssignIV; //???????????? ?????????
    private List<RelativeLayout> mListViewList; //?????????????????????????????????listview?????????
    private PullToRefreshAndLoadMoreListView myTaskListView; //????????????ListView
    private PullToRefreshAndLoadMoreListView myAssignListView; //????????????ListView
    private RelativeLayout myTaskRl;
    private RelativeLayout myAssignRl;
    private View viewMyTask;
    private View viewAssign;
    private LinearLayout include_result;
    private String currentContent;
    private int currentNum; //?????????????????????
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
                tvSearchResult.setText("????????????(???" + currentNum + "???)");
            }
        }
    };

    /**
     * ???????????????????????????View
     */
    private void initSearchResultView() {
        mListViewList = new ArrayList<>();
        myTaskListView = new PullToRefreshAndLoadMoreListView(this);
        myAssignListView = new PullToRefreshAndLoadMoreListView(this);

        initListView(); //?????????????????????

        initBgImageView();//??????????????????????????? ?????????

        initDataList(); //???????????????????????? ????????????????????????????????????

        initViewPager(); //?????????viewpager?????????

        initIndicator(); //??????????????????
    }

    /**
     * ??????????????????
     */
    private void initIndicator() {
        indicatior.setVisibleTabCount(2);
        indicatior.setTabItemTitles(new String[]{"????????????", "????????????"});
        indicatior.setViewPager(viewPager, 0);
    }

    /**
     * ?????????viewpager?????????
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
     * ???????????????????????? ????????????????????????????????????
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

        //???LinearLayout??????????????????
//        myTaskRl.setMinimumHeight((int) ViewHelper.dip2px(this, 225));
//        myAssignRl.setMinimumHeight((int) ViewHelper.dip2px(this, 225));

        mListViewList.add(myTaskRl);
        mListViewList.add(myAssignRl);
    }


    /**
     * ??????????????????????????? ?????????
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

//        myTaskIV.setVisibility(GONE); //????????????
//        myAssignIV.setVisibility(GONE);
    }


    /**
     * ?????????????????????
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
     * ??????????????????????????????????????????????????????
     */
    private void initDemand() {
        myTaskDemand = new Demand<>(Task.class);
        myTaskDemand.src = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?userId=" + Global.mUser.getUuid() + "&isMyTask=true&isShowAllHistory=true";
        myTaskDemand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        myTaskDemand.sort = "desc";
        myTaskDemand.pageSize = Integer.MAX_VALUE;
        myTaskDemand.sortField = "creationTime";
        myTaskDemand.pageIndex = myTaskPageIndex;

        myAssignDemand = new Demand<>(Task.class);
        myAssignDemand.src = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?creatorId=" + Global.mUser.getUuid() + "&isMyZhipai=true&isShowAllHistory=true";
        myAssignDemand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        myAssignDemand.sort = "desc";
        myAssignDemand.pageSize = Integer.MAX_VALUE;
        myAssignDemand.sortField = "creationTime";
        myAssignDemand.pageIndex = myAssignPageIndex;
    }

    private void setOnTouchEvent() {
        myAssignListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //??????????????????????????????
                Task task = (Task) myAssignListView.getItemAtPosition(position);
                skipActivity(task);
            }
        });
        myTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //??????????????????????????????
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
                    tvSearchResult.setText("????????????(???" + currentNum + "???)");
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
     * ????????????
     *
     * @param flag 1 : ????????????????????? 2 : ?????????????????????
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
     * ????????????????????????
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
                        if (t.getStatus().equals(TaskStatusEnum.?????????.getName())
                                || t.getStatus().equals(TaskStatusEnum.?????????.getName())) {
                            iterator.remove(); //?????????????????????????????????????????????
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
     * ??????????????????
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
                    //?????????????????????????????? ??????????????????
                    time.setTextColor(Color.parseColor("#000000"));
                } else {
                    //?????????????????????????????? ??????????????????
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
                 * ??????????????????????????????????????????
                 */
                if (TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_finish);
                } else if (TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                } else if (TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                } else if (TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.icon_status_);
                }
                ivStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getExecutorIds().equals(Global.mUser.getUuid())) {
                            if (TaskStatusEnum.?????????.getName().equals(item.getStatus())
                                    || TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                                saveTask(item, 1);
                            } else if (TaskStatusEnum.?????????.getName().equals(item.getStatus())) {
                                saveTask(item, 3);
                            } else {
                                showShortToast("?????????????????????????????????????????????!");
                            }
                        } else {
                            showShortToast("????????????????????????????????????????????????!");
                        }

                    }
                });
            }
        };
    }

    /**
     * ????????????
     *
     * @param status ???????????????1==????????????3==?????????
     */
    private void saveTask(Task task, int status) {

        String url = Global.BASE_JAVA_URL + GlobalMethord.?????????????????? + "?uuid=" + task.getUuid() + "&ticket=" + status;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                getTaskList(0);
                showShortToast("????????????????????????!");
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
     * ??????????????????
     *
     * @param task ??????????????????
     */
    private void skipActivity(Task task) {
        Intent intent = new Intent(this, TaskInfoActivityNew.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskInfo", task);
        intent.putExtra("taskIntentInfo", bundle);
        startActivity(intent);
    }

}
