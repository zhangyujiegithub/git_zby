package com.biaozhunyuan.tianyi.apply;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.Audite;
import com.biaozhunyuan.tianyi.apply.model.WorkflowInstance;
import com.biaozhunyuan.tianyi.apply.model.WorkflowTemplate;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
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
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 待我审批页面
 *
 * @author kjx
 * @since 2015/03/12
 */
@SuppressLint("ValidFragment")
public class AskMeFragment extends Fragment {


    public static final String EXTRAS_ARG = "EXTRAS_ARG";
    public String mMethodName; // 根据方法显示（区分）具体的页面内容
    public String mFilter; // 过滤条件
    private Context context;
    private DictionaryHelper dictionaryHelper;
    private Demand demand;
    public static boolean isResume = false;

    private PullToRefreshAndLoadMoreListView lv;
    private String[] methodNames = new String[]{
            GlobalMethord.待我审批, GlobalMethord.我发起的, GlobalMethord.抄送列表, GlobalMethord.我审批的};
    public static boolean 待我审批已刷新 = false;
    public static boolean 我发起的已刷新 = false;
    public static boolean 抄送列表已刷新 = false;
    public static boolean 我审批的已刷新 = false;
    private int SEARCH_CANCLE = 123;
    private int list1Selection = 0;//当前选中的条目
    private List<WorkflowInstance> list;
    private CommanAdapter<WorkflowInstance> adapter;
    private List<WorkflowTemplate> templates;
    private CommonPopupWindow popupWindow;

    private int pageIndex = 1; //页码
    private BoeryunSearchView seachButton;
    //    private ImageView iv_filter;
    private LinearLayout ll_filter;
    private List<String> searchList;
    private GridView popup_gv;
    private String hint = "请输入申请单名称搜索";
    private ImageView iv_arrow;
    private TextView tv_filter;
    private boolean isShowAuditeBtnOnFlowList;


    public AskMeFragment() {
        super();
    }

    public AskMeFragment(String filter) {
        super();
        this.mFilter = filter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        //是否显示审批按钮
        isShowAuditeBtnOnFlowList = PreferceManager.getInsance().
                getValueBYkey("IsShowAuditeBtnOnFlowList", true);
//        Toast.makeText(context, mMethodName, Toast.LENGTH_SHORT).show();
        View view = View.inflate(context, R.layout.fragment_apply_list, null);
        initViews(view);
        initDemand();
        ProgressDialogHelper.show(context);
        getApplyList();
        setOnEvent();
        return view;
    }


    private void initViews(View view) {
        lv = (PullToRefreshAndLoadMoreListView) view.findViewById(R.id.lv_apply_list);
        seachButton = view.findViewById(R.id.seach_button);
//        iv_filter = view.findViewById(R.id.iv_filter_headerview);
        ll_filter = view.findViewById(R.id.ll_filter);
        tv_filter = view.findViewById(R.id.tv_filter);
        iv_arrow = view.findViewById(R.id.iv_arrow);
        seachButton.setHint(hint);
        searchList = new ArrayList<>();
        searchList.add("申请单名称");
        searchList.add("当前状态");
        searchList.add("申请人");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            if (mMethodName.equals(methodNames[0]) && !待我审批已刷新) {
                getApplyList();
                待我审批已刷新 = true;
            }
            if (mMethodName.equals(methodNames[1]) && !我发起的已刷新) {
                getApplyList();
                我发起的已刷新 = true;
            }
            if (mMethodName.equals(methodNames[2]) && !抄送列表已刷新) {
                getApplyList();
                抄送列表已刷新 = true;
            }
            if (mMethodName.equals(methodNames[3]) && !我审批的已刷新) {
                getApplyList();
                我审批的已刷新 = true;
            }
            isResume = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isResume = false;
    }

    public void reloadData() {
        pageIndex = 1;
        getApplyList();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    public void setValue(String value) {
        demand.src = Global.BASE_JAVA_URL + mMethodName + value;
        refreshListView();
    }

    private void setOnEvent() {
        ll_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_arrow.setImageResource(R.drawable.arrow_up);
                showPopupWindow();
            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                setSearchType(SEARCH_CANCLE, "");
                refreshListView();
            }

            @Override
            public void OnClick() {

            }
        });
        seachButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (list1Selection == SEARCH_CANCLE) {
                    setSearchType(0, str);
                    refreshListView();
                } else {
                    setSearchType(list1Selection, str);
                    refreshListView();
                }
            }
        });

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getApplyList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getApplyList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    WorkflowInstance template = adapter.getDataList().get(position - 1);
                    Intent intent = new Intent(context, FormInfoActivity.class);
                    intent.putExtra("formName", template.getFormName());
                    intent.putExtra("formDataId", template.getFormDataId());
                    intent.putExtra("createrId", template.getCreatorId());
                    intent.putExtra("workflowTemplateId", template.getWorkflowTemplate());
                    startActivity(intent);
                }
            }
        });

    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + mMethodName;
        demand = new Demand();
        demand.pageSize = 10;
//        demand.sort = "desc";
        demand.sortField = "createTime desc";
        demand.src = url;
    }


    /**
     * 获取新建申请列表
     */
    private void getAppList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.新建申请列表;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                try {
                    templates = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(result, "考勤类"), WorkflowTemplate.class);

                } catch (JSONException e) {
                    getApplyList();
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取申请列表
     */
    private void getApplyList() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), WorkflowInstance.class);

                    lv.onRefreshComplete();
                    if (list != null) {
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.addBottom(list, false);
                            if (list != null && list.size() == 0) {
                                lv.loadAllData();
                            }
                            lv.loadCompleted();
                        }
                    }

                    pageIndex += 1;
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

    private CommanAdapter<WorkflowInstance> getAdapter(List<WorkflowInstance> list) {
        return new CommanAdapter<WorkflowInstance>(list, context, R.layout.item_home_apply_list) {
            @Override
            public void convert(int position, WorkflowInstance item, BoeryunViewHolder viewHolder) {
                User user = dictionaryHelper.getUser(item.getCreatorId());


                viewHolder.setUserPhotoById(R.id.iv_head_item_apply_list, user); //头像显示
                viewHolder.setTextValue(R.id.tv_creater_apply_item, user.getName()); //表单创建人
                viewHolder.setTextValue(R.id.tv_state_apply_list, item.getFormName()); //表单名称(表单类型)
                viewHolder.setTextValue(R.id.tv_time_apply_item, DateTimeUtil.dateformatTime(ViewHelper.
                        formatStrToDateAndTime(item.getCreateTime()))); //创建时间


                //审批状态
                TextView tvStatus = viewHolder.getView(R.id.tv_status_item_apply);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(item.getCurrentState());
                if (item.getCurrentState() != null) {
                    String currentState = item.getCurrentState();
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


                LinearLayout llAudite = viewHolder.getView(R.id.linearLayout2);
                //待我审批列表显示审批按钮，别的列表不显示审批按钮
                if (mMethodName.equals(methodNames[0])) {
                    if (isShowAuditeBtnOnFlowList) {
                        llAudite.setVisibility(View.VISIBLE);
                    } else {
                        llAudite.setVisibility(View.INVISIBLE);
                    }
                } else {
                    llAudite.setVisibility(View.INVISIBLE);
                }


                //拒绝按钮
                viewHolder.getView(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audite(item.getUuid(), 2, "");
                    }
                });

                //同意按钮
                viewHolder.getView(R.id.tv_agree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBeforeAudite(item.getUuid(), 1);
                    }
                });

                /**
                 * 显示摘要
                 */
                LinearLayout llRemark = viewHolder.getView(R.id.ll_mark_home_apply);
                llRemark.removeAllViews();
               /* if (!TextUtils.isEmpty(item.getSummary())) {
                    String summary = item.getSummary();
                    List<FormSummary> formSummaries = JsonUtils.jsonToArrayEntity(summary, FormSummary.class);

                    if (formSummaries != null && formSummaries.size() > 0) {
                        for (FormSummary formSummary : formSummaries) {
                            TextView tv = new TextView(context);
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                            tv.setTextColor(getResources().getColor(R.color.text_tag));
                            tv.setSingleLine(true);
                            tv.setEllipsize(TextUtils.TruncateAt.END);
                            tv.setText(StrUtils.pareseNull(formSummary.getFieldName()) + " : "
                                    + StrUtils.pareseNull(formSummary.getText()));
                            tv.setPadding(8, 8, 8, 8);

                            llRemark.addView(tv);
                        }
                    } else {
                        llRemark.removeAllViews();
                    }
                } else {
                    llRemark.removeAllViews();
                }*/

                String remark = item.getRemark();
                llRemark.removeAllViews();
                if (!TextUtils.isEmpty(remark)) {
                    String[] remarkArr = remark.split("&&");
                    if (remarkArr.length > 0) {
                        for (String s : remarkArr) {
                            TextView tv = new TextView(context);
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

            }
        };
    }


    /**
     * 审批
     *
     * @param type 1==通过 2==拒绝
     */
    private void audite(String workFlowId, int type, String opinion) {
        ProgressDialogHelper.show(context, "审批中...");

        Audite audite = new Audite();
        audite.setWorkflowId(workFlowId);
        audite.setOpinion(opinion);
        audite.setType(type);

        StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.审批申请, audite, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String data = JsonUtils.pareseData(response);
                if (!TextUtils.isEmpty(data) && data.contains("成功")) {
                    Toast.makeText(context, "审批成功!", Toast.LENGTH_SHORT).show();
                    refreshListView();
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
                    Toast.makeText(context, "请进入申请单详情审批", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context,
                                "该申请中有需您填写或修改内容，请进入详情页面填写并审批", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    /**
     * 删除申请
     *
     * @param applyId 要删除申请的id
     */
    private void deleteApply(String applyId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.删除申请;

        JSONObject object = new JSONObject();
        try {
            object.put("ids", applyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String status = JsonUtils.parseStatus(response);

                if (status.equals("1")) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    refreshListView();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, "删除失败" + JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 刷新列表
     */
    private void refreshListView() {
        lv.startRefresh();
        pageIndex = 1;
        getApplyList();
    }


    private void showPopupWindow() {
        if (popupWindow == null) {
            popupWindow = new CommonPopupWindow.Builder(context)
                    //设置PopupWindow布局
                    .setView(R.layout.popup_applylist)
                    //设置宽高
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    //设置动画
                    .setAnimationStyle(R.style.AnimDown)
                    //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                    .setBackGroundLevel(1.0f)
                    //设置PopupWindow里的子View及点击事件
                    .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                        @Override
                        public void getChildView(View view, int layoutResId) {
                            popup_gv = view.findViewById(R.id.popup_gv);
                            popup_gv.setAdapter(getPopupListAdapter(searchList));
                            popup_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    if(list1Selection == position){
//                                        list1Selection = SEARCH_CANCLE; //条目反选
//                                    } else {
                                    list1Selection = position;
//                                    }
//                                    for (int i = 0; i < parent.getMsgTotalCount(); i++) { //给选中的条目设置背景色
//                                        View v = parent.getChildAt(i);
//                                        if (list1Selection == i) {
//                                            v.setBackgroundColor(Color.parseColor("#cccccc"));
//                                        } else {
//                                            v.setBackgroundColor(Color.WHITE);
//                                        }
//                                    }
//                                    if(list1Selection == SEARCH_CANCLE){ //如果没有选中默认选中第一项
//                                        tv_filter.setText(searchList.get(0));
//                                        hint = "请输入申请单名称搜索";
//                                    }else {
                                    adapter.notifyDataSetChanged();
                                    tv_filter.setText(searchList.get(list1Selection));
                                    hint = "请输入" + searchList.get(list1Selection) + "搜索";
//                                    }
                                    seachButton.setHint(hint);
                                    popupWindow.dismiss();
                                }
                            });
                        }
                    })
                    //设置外部是否可点击 默认是true
                    .setOutsideTouchable(true)
                    //开始构建
                    .create();
        } else {
            popup_gv.setSelection(list1Selection);
        }
        popupWindow.showAsDropDown(seachButton);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_arrow.setImageResource(R.drawable.arrow_down);
            }
        });
    }

    private CommanAdapter<String> getPopupListAdapter(List<String> list) {
        return new CommanAdapter<String>(list, context, R.layout.item_dictionary2) {
            @Override
            public void convert(int position, String item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, item);
                if (item.equals(searchList.get(list1Selection))) {
                    viewHolder.getView(R.id.ll_parent).setBackgroundColor(Color.parseColor("#cccccc"));
                } else {
                    viewHolder.getView(R.id.ll_parent).setBackgroundColor(Color.WHITE);
                }
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        seachButton.setOnCancleSearch(false);
    }

    /**
     * 设置搜索参数
     *
     * @param i         i = 0:根据申请类型关键字搜索. i = 1:根据申请人关键字搜索 i = 2:根据当前状态关键字搜索
     * @param searchStr
     */
    public void setSearchType(int i, String searchStr) {
        Map<String, String> map = new HashMap<>();
        switch (i) {
            case 0:
                map.put("searchField_string_formName", "1|" + searchStr);
                map.put("searchField_string_currentState", "");
                map.put("searchField_string_creatorName", "");
                demand.keyMap = map;
                break;
            case 1:
                map.put("searchField_string_formName", "");
                map.put("searchField_string_currentState", "1|" + searchStr);
                map.put("searchField_string_creatorName", "");
                demand.keyMap = map;
                break;
            case 2:
                map.put("searchField_string_formName", "");
                map.put("searchField_string_currentState", "");
                map.put("searchField_string_creatorName", "1|" + searchStr);
                demand.keyMap = map;
                break;
            default:
                map.put("searchField_string_formName", "");
                map.put("searchField_string_currentState", "");
                map.put("searchField_string_creatorName", "");
                demand.keyMap = map;
                break;
        }
    }
}
