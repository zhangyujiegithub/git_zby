package com.biaozhunyuan.tianyi.contact;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.task.TasksCompletion;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.loonggg.weekcalendar.entity.CalendarData;
import com.loonggg.weekcalendar.view.WeekCalendar;

import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.utils.JsonUtils.ConvertJsonToList;

/**
 * Created by 王安民 on 2017/10/8.
 * 跟进记录周视图
 */

public class ContactWeekViewFragment extends Fragment {


    private final int[] mSateBgColors = new int[]{R.color.color_status_qidong, R.color.color_status_zanting, R.color.color_status_wancheng, R.color.color_status_gezhi, R.color.color_status_chongqi, R.color.color_status_tijiao};
    private static final String CONTACT_WEEK_VIEW_CHECK = "CONTACT_WEEK_VIEW_CHECK";

    private ViewHolder mViewHolder;

    private Demand<Contact> demand;
    private List<ContactStatus> status;

    private HashMap<Integer, TasksCompletion> mSelectCompletionMap = new HashMap<Integer, TasksCompletion>();
    private Contact mSelectTask;
    private List<TasksCompletion> users;
    private int lastPosition = -1;
    private boolean isSelect = false;
    private String currentId = Global.mUser.getUuid(); //按照员工过滤，默认是当前用户
    private String currentTime = "";  //按照时间过滤

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_week_view, null);
        mViewHolder = new ViewHolder(view);
        setOnEvent();
        return view;
    }

    @Override
    public void onStart() {
        getStatusName();
        initDemand();
        super.onStart();
    }

    private class ViewHolder {
        public View rootView;
        public HorizontalScrollView horizontalScrollView_task_week_view;
        public WeekCalendar weekcalendar_task_week_view;

        public LinearLayout ll_user_root_task_week_view;

        private LinearLayout ll_root_day1_week_view;
        private LinearLayout ll_root_day2_week_view;
        private LinearLayout ll_root_day3_week_view;
        private LinearLayout ll_root_day4_week_view;
        private LinearLayout ll_root_day5_week_view;
        private LinearLayout ll_root_day6_week_view;
        private LinearLayout ll_root_day7_week_view;

        private RelativeLayout rl_info_task_week_view;
        private CircleImageView avatarView;
        private TextView tvContent;
        private TextView tvName;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.horizontalScrollView_task_week_view = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView_task_week_view);
            this.weekcalendar_task_week_view = (WeekCalendar) rootView.findViewById(R.id.weekcalendar_task_week_view);
//            this.lv_task_week_view = (ListView) rootView.findViewById(R.id.lv_task_week_view);
            this.ll_user_root_task_week_view = (LinearLayout) rootView.findViewById(R.id.ll_user_root_task_week_view);
            this.ll_root_day1_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day1_week_view);
            this.ll_root_day2_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day2_week_view);
            this.ll_root_day3_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day3_week_view);
            this.ll_root_day4_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day4_week_view);
            this.ll_root_day5_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day5_week_view);
            this.ll_root_day6_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day6_week_view);
            this.ll_root_day7_week_view = (LinearLayout) rootView.findViewById(R.id.ll_root_day7_week_view);
            this.rl_info_task_week_view = (RelativeLayout) rootView.findViewById(R.id.rl_info_task_week_view);

            this.avatarView = (CircleImageView) rootView.findViewById(R.id.circularAvatar_info_task_week_view);
            this.tvContent = (TextView) rootView.findViewById(R.id.tv_info_content_task_week_view);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name_task_week_view);
        }
    }

    private void setOnEvent() {
        mViewHolder.weekcalendar_task_week_view.setOnPageChangedListener(new WeekCalendar.OnPageChangedListener() {
            @Override
            public void onPageChange(List<CalendarData> dataList) {
//                mSelectCompletionMap.clear();
                getTaskList(currentId);
            }
        });

        mViewHolder.rl_info_task_week_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectTask != null) {
                    Contact task = mSelectTask;
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contactInfo", task);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void clearAllTaskView() {
        mViewHolder.ll_root_day1_week_view.removeAllViews();
        mViewHolder.ll_root_day2_week_view.removeAllViews();
        mViewHolder.ll_root_day3_week_view.removeAllViews();
        mViewHolder.ll_root_day4_week_view.removeAllViews();
        mViewHolder.ll_root_day5_week_view.removeAllViews();
        mViewHolder.ll_root_day6_week_view.removeAllViews();
        mViewHolder.ll_root_day7_week_view.removeAllViews();
    }


    private void initDemand() {
        demand = new Demand<>(Contact.class);
        demand.pageSize = 1000;
        demand.sort = "desc";
        demand.sortField = "contactTime";
        demand.dictionaryNames = "customerId.crm_customer,stage.dict_contact_stage,contactWay.dict_contact_way";
    }


    private void getStatusName() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录状态;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                status = JsonUtils.jsonToArrayEntity(result, ContactStatus.class);
                getTaskList(currentId);
            }
        });
    }


    private void getTaskList(String executorIds) {
        if (mViewHolder != null) {
            clearAllTaskView();
        }
        initStaffHeader();
        final List<CalendarData> calendarDatas = mViewHolder.weekcalendar_task_week_view.getCurrentWeekDatas();
        for (int i = 0; i < calendarDatas.size(); i++) {
            CalendarData data = calendarDatas.get(i);
            demand.src = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表 + "?from=" + data.getDateStr() + "&to=" + data.getDateStr() + "&advisorId=" + executorIds;
            final int finalI = i;
            demand.init(new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    try {
                        List<Contact> list = demand.data;
//                        List<Contact> list = GsonTool.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), Contact.class);
//                        String dictionay = JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "dictionary");
                        for (Contact project : list) {
                            project.setCustomerName(demand.getDictName(project,"customerId"));
                            project.setStageName(demand.getDictName(project,"stage"));
                            project.setContactWayName(demand.getDictName(project,"contactWay"));
                        }
                        if (list != null && list.size() > 0) {
                            LinearLayout llRoot = mViewHolder.ll_root_day1_week_view;
                            String date = calendarDatas.get(finalI).getDateStr();
                            switch (finalI) {
                                case 0:
                                    llRoot = mViewHolder.ll_root_day1_week_view;
                                    break;
                                case 1:
                                    llRoot = mViewHolder.ll_root_day2_week_view;
                                    break;
                                case 2:
                                    llRoot = mViewHolder.ll_root_day3_week_view;
                                    break;
                                case 3:
                                    llRoot = mViewHolder.ll_root_day4_week_view;
                                    break;
                                case 4:
                                    llRoot = mViewHolder.ll_root_day5_week_view;
                                    break;
                                case 5:
                                    llRoot = mViewHolder.ll_root_day6_week_view;
                                    break;
                                case 6:
                                    llRoot = mViewHolder.ll_root_day7_week_view;
                                    break;
                            }

//                            List<ReturnDict> dictList = JsonUtils.getDictByName(dictionay, "customerId.crm_customer");
                            for (Contact task : list) {
                                Logger.i("CalendarData_date:::" + "ssignTime=" + task.getContactTime());
//                                task.setCustomerName(JsonUtils.getDictValueById(dictList, task.getCustomerId()));
                                if (!TextUtils.isEmpty(task.getContactTime()) && task.getContactTime().contains(date)) {
                                    generateTaskItem(llRoot, task);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
    }


    public void generateTaskItem(LinearLayout llRoot, final Contact item) {
        View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_week_view, null);
        final TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content_task_week_view);
        String content = item.getContent();
        if (!TextUtils.isEmpty(content)) {
            content = content.replaceAll(" ", "") + "    ";

            content = content.length() >= 4 ? content.substring(0, 4) : content;
            if (content.length() > 2) {
                content = content.substring(0, 2) + "\n" + content.substring(2, content.length());
            }
            tvContent.setText(content);
            for (ContactStatus statu : status) {
                if (statu.getUuid().equals(item.getStage())) {
                    tvContent.setBackgroundColor(Color.parseColor(statu.getColor()));
//                    if (statu.getName().contains("首次拜访")) {
//                        tvContent.setBackgroundColor(getResources().getColor(R.color.color_status_zanting));
//                    } else if (statu.getName().contains("成功邀约")) {
//                        tvContent.setBackgroundColor(getResources().getColor(R.color.color_status_chongzhi));
//                    } else if (statu.getName().contains("方案制定评估")) {
//                        tvContent.setBackgroundColor(getResources().getColor(R.color.color_status_wancheng));
//                    } else if (statu.getName().contains("成交")) {
//                        tvContent.setBackgroundColor(getResources().getColor(R.color.color_status_chongqi));
//                    } else if (statu.getName().contains("商务谈判促成")) {
//                        tvContent.setBackgroundColor(getResources().getColor(R.color.color_status_zanting));
//                    }
                }
            }

            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectTask = item;
                    if (mViewHolder.rl_info_task_week_view.getVisibility() == View.GONE) {
                        mViewHolder.rl_info_task_week_view.setVisibility(View.VISIBLE);
                    }

                    //顯示底部任務詳情
                    mViewHolder.tvContent.setText(StrUtils.pareseNull(item.getContent()));
                    mViewHolder.tvName.setText(StrUtils.pareseNull(new DictionaryHelper(getActivity()).getUserNameById(item.getAdvisorId())));
                    ImageUtils.displyImageById(item.getAdvisorId(), mViewHolder.avatarView);
//                    mViewHolder.avatarView.displayNameByUserId(item.getExecutor());
                }
            });
            llRoot.addView(itemView);
        }
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
        mViewHolder.ll_user_root_task_week_view.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            final TasksCompletion item = mList.get(i);
            final int pos = i;
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_task_user_and_progress, null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name_avatar);
            CircleImageView view2 = (CircleImageView) view.findViewById(R.id.circularAvatar_task_user);
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl_task_progress);

            String valueBYkey = PreferceManager.getInsance().getValueBYkey(CONTACT_WEEK_VIEW_CHECK);

            if(TextUtils.isEmpty(valueBYkey)){
                if(item.getStaffId().equals(Global.mUser.getUuid())){
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                }

            }else{
                if(valueBYkey.equals(item.getStaffId())){
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg_selected));
                }else {
                    view.setBackground(getResources().getDrawable(R.drawable.task_progress_item_bg));
                }
            }

            ImageUtils.displyImageById(new DictionaryHelper(getActivity()).getUserPhoto(item.getStaffId()), view2);
            rl.setVisibility(View.GONE);
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

            progressBar.setMax(item.getTotal());
            progressBar.setProgress(item.getCompleted());
            tvCount.setText(item.getCompleted() + "/" + item.getTotal());



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialogHelper.show(getActivity());
                    PreferceManager.getInsance().saveValueBYkey(CONTACT_WEEK_VIEW_CHECK,item.getStaffId());
//                    item.isChecked = !item.isChecked;
                    if (lastPosition != -1 && lastPosition != pos) {
                        mViewHolder.ll_user_root_task_week_view.getChildAt(lastPosition).setBackground(getResources().getDrawable(R.drawable.round_corners_5dp_blue));
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
                    currentId = item.getStaffId();
                    getTaskList(currentId);
                }
            });
            mViewHolder.ll_user_root_task_week_view.addView(view);
        }
    }
}
