package com.biaozhunyuan.tianyi.newuihome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.AllFormFragment;
import com.biaozhunyuan.tianyi.apply.ApplylistActivity;
import com.biaozhunyuan.tianyi.apply.model.Audite;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.apply.model.WorkflowInstance;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.DateTimeUtil;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.view.RollViewPager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 首页用户自定义排版: 待我审批
 */
@SuppressLint("NewApi")
public class HomeApplyLayout extends LinearLayout {

    private Context mContext;
    private Activity activity;
    private AuditSuccessListener listener;
    //    private NoScrollListView lv;
    private Demand demand;
    private List<WorkflowInstance> approvalList;
    private List<WorkflowInstance> copeToList;
    private DictionaryHelper helper;
    private ImageView ivAdd;


    private RollViewPager viewPager;
    private LinearLayout ll_approval_select; //待我审批 选中
    private LinearLayout ll_copyto_select; //抄送我的 选中
    private TextView tv_approval; //待我审批 未选中
    private TextView tv_copyto;   //抄送我的 未选中
    private TextView tv_approval_num; //待我审批 数量
    private TextView tv_copyto_num;   //抄送我的 数量
    private List<RelativeLayout> mListViewList; //存放待我审批和抄送我的listview的集合
    private LinearLayout llApproval; //待我审批
    private LinearLayout llCopyTo; //抄送给我
    //    private ImageView approvalIV; //待我审批背景占位图
//    private ImageView copyToIV;//抄送给我背景占位图
    private CommanAdapter<WorkflowInstance> approvalAdapter;
    private CommanAdapter<WorkflowInstance> copyToAdapter;
    private RelativeLayout approvalll;
    private RelativeLayout copyToll;
    private TextView tv_more;//查看更多
    private View viewCopy;
    private View viewAsk;

    public HomeApplyLayout(Context context) {
        super(context);
        init(context);
    }

    public HomeApplyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HomeApplyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.activity = (Activity) mContext;

        View mParentView = LayoutInflater.from(context).
                inflate(R.layout.include_home_apply_new, this, true);

        initView(mParentView);

        initData();

        setOnTouch();
    }

    private void setOnTouch() {

        tv_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ApplylistActivity.class));
            }
        });

        tv_approval.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setHiddenAndShowTitle(0);
                viewPager.setCurrentItem(0);
            }
        });
        tv_copyto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setHiddenAndShowTitle(1);
                viewPager.setCurrentItem(1);
            }
        });

        ivAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, AllFormFragment.class));
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1 && copeToList == null) {
                    demand.src = Global.BASE_JAVA_URL + GlobalMethord.抄送列表;
                    ProgressDialogHelper.show(mContext);
                    getApplyList();
                }
            }

            @Override
            public void onPageSelected(int position) {
                setHiddenAndShowTitle(position);
                if (position == 1) {
                    viewCopy.setVisibility(VISIBLE);
                    viewAsk.setVisibility(GONE);
                } else {
                    viewAsk.setVisibility(VISIBLE);
                    viewCopy.setVisibility(GONE);
                }
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置我的任务和我的指派标签显示隐藏
     *
     * @param current 0: 当前选中我的任务 1:当前选中我的指派
     */
    private void setHiddenAndShowTitle(int current) {
        if (current == 0) {
            if (ll_approval_select.getVisibility() == GONE) {
                ll_copyto_select.setVisibility(GONE);
                ll_approval_select.setVisibility(VISIBLE);
                tv_copyto.setVisibility(VISIBLE);
                tv_approval.setVisibility(GONE);
            }
        } else {
            if (ll_copyto_select.getVisibility() == GONE) {
                ll_copyto_select.setVisibility(VISIBLE);
                ll_approval_select.setVisibility(GONE);
                tv_copyto.setVisibility(GONE);
                tv_approval.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 初始化viewpager适配器
     */
    private void initViewPager() {
        viewPager.setPagingEnabled(true);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mListViewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RelativeLayout view = mListViewList.get(position);
                container.addView(view);
                viewPager.setObjectForPosition(view, position);
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

        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        approvalll = new RelativeLayout(mContext);
        approvalll.setLayoutParams(layoutParams);

        llApproval = new LinearLayout(mContext);
        llApproval.setOrientation(VERTICAL);
        llApproval.setLayoutParams(llParams);

        approvalll.addView(llApproval);

        copyToll = new RelativeLayout(mContext);
        copyToll.setLayoutParams(layoutParams);

        llCopyTo = new LinearLayout(mContext);
        llCopyTo.setOrientation(VERTICAL);
        llCopyTo.setLayoutParams(llParams);

        copyToll.addView(llCopyTo);

        mListViewList.add(approvalll);
        mListViewList.add(copyToll);
    }

    private void initData() {
        mListViewList = new ArrayList<>();

        initDataList(); //初始化列表的容器 及添加列表和背景到容器中

        initViewPager(); //初始化viewpager适配器

        initDamend(); //网络请求列表

    }

    private void initDamend() {
        helper = new DictionaryHelper(mContext);
        demand = new Demand();
        demand.sortField = "createTime desc";
        demand.pageSize = 3;
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.待我审批;
        getApplyList();
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.抄送列表;
        getApplyList();
    }

    public void refreshDataList() {
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.待我审批;
        getApplyList();
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.抄送列表;
        getApplyList();
    }

    private void getApplyList() {
        String url = demand.src;
        boolean isMyApproval; //是否是待我审批
        if (demand.src.contains(GlobalMethord.待我审批)) {
            isMyApproval = true;
        } else {
            demand.src += "?lookStatus=未查看";
            isMyApproval = false;
        }
        boolean finalIsMyApproval = isMyApproval;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<WorkflowInstance> listData = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), WorkflowInstance.class);
                    if (listData != null) {
                        if (finalIsMyApproval) { //待我审批
                            Message message = handler.obtainMessage();
                            message.what = 0x111;
                            message.obj = listData;
                            handler.sendMessage(message);
                            String total = JsonUtils.getStringValue(JsonUtils.pareseData(response), "total");
                            try {
                                if (Integer.parseInt(total) > 0 && listData.size() > 0) {
                                    tv_approval_num.setVisibility(VISIBLE);
                                } else {
                                    tv_approval_num.setVisibility(GONE);
                                }
                                tv_approval_num.setText(total);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else { //抄送我的
                            Message message = handler.obtainMessage();
                            message.what = 0x112;
                            message.obj = listData;
                            handler.sendMessage(message);
                            String total = JsonUtils.getStringValue(JsonUtils.pareseData(response), "total");
                            try {
                                if (Integer.parseInt(total) > 0 && listData.size() > 0) {
                                    tv_copyto_num.setVisibility(VISIBLE);
                                } else {
                                    tv_copyto_num.setVisibility(GONE);
                                }
                                tv_copyto_num.setText(total);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
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

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x111) {
                approvalList = (List<WorkflowInstance>) msg.obj;
                if (approvalList.size() > 0) {
                    llApproval.setVisibility(VISIBLE);
                    createView(approvalList, llApproval, true);
                } else {
                    llApproval.setVisibility(GONE);
                }
                setSelfVisibility();
            } else if (msg.what == 0x112) {
                copeToList = (List<WorkflowInstance>) msg.obj;
                if (copeToList.size() > 0) {
                    llCopyTo.setVisibility(VISIBLE);
                    createView(copeToList, llCopyTo, false);
                } else {
                    llCopyTo.setVisibility(GONE);
                }
                setSelfVisibility();
            }
        }
    };

    //设置自己是否可见，如果是没有数据，隐藏，有数据，显示
    private void setSelfVisibility() {
        boolean isVisible = false;
        if (approvalList != null && copeToList != null) {
            if (approvalList.size() > 0 || copeToList.size() > 0) {
                isVisible = true;
            }

            if (isVisible) {
                setVisibility(VISIBLE);
            } else {
                setVisibility(GONE);
            }
        } else {
            setVisibility(GONE);
        }
    }


    /**
     * 生成itemview
     *
     * @param list
     * @param ll
     */
    private void createView(List<WorkflowInstance> list, LinearLayout ll, boolean isShowAudite) {
        boolean isShowAuditeBtnOnFlowList = PreferceManager.getInsance().
                getValueBYkey("IsShowAuditeBtnOnFlowList", true); //是否显示审批按钮
        ll.removeAllViews();
        for (WorkflowInstance workflow : list) {
            View view = inflate(mContext, R.layout.item_home_apply_list, null);

            AvatarImageView ivHead = view.findViewById(R.id.iv_head_item_apply_list);
            TextView tvName = view.findViewById(R.id.tv_creater_apply_item);
            TextView tvTime = view.findViewById(R.id.tv_time_apply_item);
            TextView tvReject = view.findViewById(R.id.tv_reject);
            TextView tvAgree = view.findViewById(R.id.tv_agree);
            TextView tvStyle = view.findViewById(R.id.tv_state_apply_list);
            LinearLayout llAudite = view.findViewById(R.id.linearLayout2);
            View divider = view.findViewById(R.id.divider_line);

            User user = helper.getUser(workflow.getCreatorId());
            ImageUtils.displyUserPhotoById(mContext, workflow.getCreatorId(), ivHead);//申请人头像

            tvStyle.setText(workflow.getFormName()); //申请单名称
            tvName.setText(helper.getUserNameById(workflow.getCreatorId())); //申请人名称
            tvTime.setText(DateTimeUtil.dateformatTime(ViewHelper.
                    formatStrToDateAndTime(workflow.getCreateTime())));//申请时间

            //审批状态
            TextView tvStatus = view.findViewById(R.id.tv_status_item_apply);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(workflow.getCurrentState());
            if (workflow.getCurrentState() != null) {
                String currentState = workflow.getCurrentState();
                if ("已完成".equals(currentState)) {
                    tvStatus.setTextColor(getResources().getColor(R.color.hanyaRed));
                } else if ("已保存".equals(currentState)) {
                    tvStatus.setTextColor(getResources().getColor(R.color.text_tag));
                } else if (currentState.contains("审核")) {
                    tvStatus.setTextColor(getResources().getColor(R.color.lightYellow));
                } else if (currentState.equals("已否决") || currentState.equals("已退回")) {
                    tvStatus.setTextColor(getResources().getColor(R.color.apply_state_yifoujue));
                }
            } else {
                tvStatus.setTextColor(getResources().getColor(R.color.color_status_qidong));
            }

            String remark = workflow.getRemark();
            LinearLayout llRemark = view.findViewById(R.id.ll_mark_home_apply);
            if (!TextUtils.isEmpty(remark)) {
                String[] remarkArr = remark.split("&&");
                if (remarkArr.length > 0) {
                    for (String s : remarkArr) {
                        TextView tv = new TextView(mContext);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                        tv.setTextColor(getResources().getColor(R.color.text_tag));
                        tv.setSingleLine(true);
                        tv.setEllipsize(TextUtils.TruncateAt.END);
                        tv.setText(s);
                        tv.setPadding(8, 8, 8, 8);

                        llRemark.addView(tv);
                    }
                } else {
                    llRemark.removeAllViews();
                }
            } else {
                llRemark.removeAllViews();
            }

            ll.addView(view);


            //是否显示审批按钮
            if (isShowAudite) {
                if (isShowAuditeBtnOnFlowList) {
                    llAudite.setVisibility(VISIBLE);
                } else {
                    llAudite.setVisibility(INVISIBLE);
                }
            } else {
                llAudite.setVisibility(INVISIBLE);
            }

            /**
             * 拒绝
             */
            tvReject.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    audite(workflow.getUuid(), 2, "");
                }
            });

            /**
             * 同意
             */
            tvAgree.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBeforeAudite(workflow.getUuid(), 1);
                }
            });


            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkflowInstance item = workflow;
                    //设置已读
                    if (!isShowAudite && "未查看".equals(item.getLookStatus())) {
                        setFormRead(item);
                        changeCopyStatus(item);
                    }
                    Intent intent = new Intent();
                    intent.setClass(mContext, FormInfoActivity.class);
                    String url1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowId=" + item.getUuid();
                    intent.putExtra("exturaUrl", url1);
                    intent.putExtra("formDataId", item.getFormDataId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void initView(View mParentView) {
//        lv = mParentView.findViewById(R.id.lv_apply);
        tv_more = mParentView.findViewById(R.id.tv_view_more);
        ivAdd = mParentView.findViewById(R.id.iv_add_apply);
        viewPager = mParentView.findViewById(R.id.viewpager_home_view);
        ll_approval_select = mParentView.findViewById(R.id.ll_approval_apply_select);
        ll_copyto_select = mParentView.findViewById(R.id.ll_copyto_apply_select);
        tv_approval = mParentView.findViewById(R.id.tv_approval_apply);
        tv_copyto = mParentView.findViewById(R.id.tv_copyto_apply);
        tv_approval_num = mParentView.findViewById(R.id.tv_approval_num);
        tv_copyto_num = mParentView.findViewById(R.id.tv_copyto_num);
        viewCopy = mParentView.findViewById(R.id.view_copyme);
        viewAsk = mParentView.findViewById(R.id.view_askme);
    }

    private void setFormRead(WorkflowInstance form) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.设置申请已读 + "?workflowId=" + form.getUuid();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void changeCopyStatus(WorkflowInstance form) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.改变抄送申请状态 + "?formDataId=" + form.getFormDataId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

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
     * 审批
     *
     * @param type 1==通过 2==拒绝
     */
    private void audite(String workFlowId, int type, String opinion) {
        ProgressDialogHelper.show(mContext, "审批中...");

        Audite audite = new Audite();
        audite.setWorkflowId(workFlowId);
        audite.setOpinion(opinion);
        audite.setType(type);

        StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.审批申请, audite, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    listener.onAuditSuccess();
                }
                String data = JsonUtils.pareseData(response);
                if (!TextUtils.isEmpty(data) && data.contains("成功")) {
                    Toast.makeText(mContext, "审批成功!", Toast.LENGTH_SHORT).show();
                    if (viewPager.getCurrentItem() == 0) {
                        demand.src = Global.BASE_JAVA_URL + GlobalMethord.待我审批;
                    } else {
                        demand.src = Global.BASE_JAVA_URL + GlobalMethord.抄送列表;
                    }
                    getApplyList();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                String status = JsonUtils.parseStatus(result);
                //如果是 自由审批流程，状态码为6，提示进入详情页审批
                if ("6".equals(status)) {
                    Toast.makeText(mContext, "请进入申请单详情审批", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 审批前检查是否满足审批条件
     *
     * @param workflowId 流程id
     * @param type       1是通过/2是否决
     */
    private void checkBeforeAudite(String workflowId, int type) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.审批前检查;

        Map<String, String> map = new HashMap<>();
        map.put("workflowId", workflowId);
        map.put("type", type + "");
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                audite(workflowId, 1, "");
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                if ("{}".equals(result)) {
                    audite(workflowId, 1, "");
                } else {
                    if ("0".equals(JsonUtils.parseStatus(result))) {
                        Toast.makeText(mContext,
                                "该申请中有需您填写或修改内容，请进入详情页面填写并审批", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, JsonUtils.pareseMessage(result), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    interface AuditSuccessListener {
        void onAuditSuccess();
    }

    public void setOnAuditSuccessListener(AuditSuccessListener auditSuccessListener) {
        listener = auditSuccessListener;
    }
}
