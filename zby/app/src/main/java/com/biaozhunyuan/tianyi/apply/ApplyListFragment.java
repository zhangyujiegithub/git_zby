package com.biaozhunyuan.tianyi.apply;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * ??????????????????
 *
 * @author kjx
 * @since 2015/03/12
 */
@SuppressLint("ValidFragment")
public class ApplyListFragment extends Fragment {


    public String mMethodName; // ???????????????????????????????????????????????????
    public String mFilter; // ????????????
    private Context context;
    private DictionaryHelper dictionaryHelper;
    private Demand demand;
    public static boolean isResume = false;

    private PullToRefreshAndLoadMoreListView lv;
    private int SEARCH_CANCLE = 123;
    private int list1Selection = 0;//?????????????????????
    private List<WorkflowInstance> list;
    private CommanAdapter<WorkflowInstance> adapter;
    private List<WorkflowTemplate> templates;
    private CommonPopupWindow popupWindow;

    private int pageIndex = 1; //??????
    private BoeryunSearchView seachButton;
    //    private ImageView iv_filter;
    private LinearLayout ll_filter;
    private List<String> searchList;
    private GridView popup_gv;
    private String hint = "??????????????????????????????";
    private ImageView iv_arrow;
    private TextView tv_filter;
    private boolean isShowAuditeBtnOnFlowList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        dictionaryHelper = new DictionaryHelper(context);
        //????????????????????????
        isShowAuditeBtnOnFlowList = PreferceManager.getInsance().
                getValueBYkey("IsShowAuditeBtnOnFlowList", true);
//        Toast.makeText(context, mMethodName, Toast.LENGTH_SHORT).show();
        View view = View.inflate(context, R.layout.fragment_apply_list, null);
        EventBus.getDefault().register(this);
        initViews(view);
        initDemand();
        ProgressDialogHelper.show(context);
        getApplyList();
        setOnEvent();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            mMethodName = getArguments().getString("mMethodName");
        }
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
        searchList.add("???????????????");
        searchList.add("????????????");
        searchList.add("?????????");
    }

    public static ApplyListFragment getInstance(String mMethodName) {
        ApplyListFragment fragment = new ApplyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mMethodName", mMethodName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isResume = false;
        EventBus.getDefault().unregister(this);
    }

    public void reloadData() {
        pageIndex = 1;
        getApplyList();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(final String status) {
        if ("??????????????????".equals(status)) {
            reloadData();
            EventBus.getDefault().removeStickyEvent(status);
        }
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
        demand.sortField = "createTime desc";
        demand.src = url;
    }


    /**
     * ??????????????????
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


                viewHolder.setUserPhotoById(R.id.iv_head_item_apply_list, user); //????????????
                viewHolder.setTextValue(R.id.tv_creater_apply_item, user.getName()); //???????????????
                viewHolder.setTextValue(R.id.tv_state_apply_list, item.getFormName()); //????????????(????????????)
                viewHolder.setTextValue(R.id.tv_time_apply_item, DateTimeUtil.dateformatTime(ViewHelper.
                        formatStrToDateAndTime(item.getCreateTime()))); //????????????


                //????????????
                TextView tvStatus = viewHolder.getView(R.id.tv_status_item_apply);
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(item.getCurrentState());
                if (item.getCurrentState() != null) {
                    String currentState = item.getCurrentState();
                    if ("?????????".equals(currentState)) {
                        tvStatus.setTextColor(getResources().getColor(R.color.hanyaRed));
                    } else if ("?????????".equals(currentState)) {
                        tvStatus.setTextColor(getResources().getColor(R.color.text_tag));
                    } else if (currentState.contains("??????")) {
                        tvStatus.setTextColor(getResources().getColor(R.color.lightYellow));
                    } else if (currentState.equals("?????????") || currentState.equals("?????????")) {
                        tvStatus.setTextColor(getResources().getColor(R.color.apply_state_yifoujue));
                    }
                } else {
                    tvStatus.setTextColor(getResources().getColor(R.color.color_status_qidong));
                }


                LinearLayout llAudite = viewHolder.getView(R.id.linearLayout2);
                //????????????????????????????????????????????????????????????????????????
                llAudite.setVisibility(View.INVISIBLE);


                //????????????
                viewHolder.getView(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        audite(item.getUuid(), 2, "");
                    }
                });

                //????????????
                viewHolder.getView(R.id.tv_agree).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBeforeAudite(item.getUuid(), 1);
                    }
                });

                /**
                 * ????????????
                 */
                LinearLayout llRemark = viewHolder.getView(R.id.ll_mark_home_apply);
                llRemark.removeAllViews();

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
     * ??????
     *
     * @param type 1==?????? 2==??????
     */
    private void audite(String workFlowId, int type, String opinion) {
        ProgressDialogHelper.show(context, "?????????...");

        Audite audite = new Audite();
        audite.setWorkflowId(workFlowId);
        audite.setOpinion(opinion);
        audite.setType(type);

        StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.????????????, audite, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String data = JsonUtils.pareseData(response);
                if (!TextUtils.isEmpty(data) && data.contains("??????")) {
                    Toast.makeText(context, "????????????!", Toast.LENGTH_SHORT).show();
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
                //????????? ?????????????????????????????????6??????????????????????????????
                if ("6".equals(status)) {
                    Toast.makeText(context, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * ???????????????????????????????????????
     *
     * @param workflowId ??????id
     * @param type       1?????????/2?????????
     */
    private void checkBeforeAudite(String workflowId, int type) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.???????????????;

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
                                "?????????????????????????????????????????????????????????????????????????????????", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    /**
     * ????????????
     */
    private void refreshListView() {
        lv.startRefresh();
        pageIndex = 1;
        getApplyList();
    }


    private void showPopupWindow() {
        if (popupWindow == null) {
            popupWindow = new CommonPopupWindow.Builder(context)
                    //??????PopupWindow??????
                    .setView(R.layout.popup_applylist)
                    //????????????
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    //????????????
                    .setAnimationStyle(R.style.AnimDown)
                    //?????????????????????????????????0.0f-1.0f ??????????????? 1.0f?????????
                    .setBackGroundLevel(1.0f)
                    //??????PopupWindow?????????View???????????????
                    .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                        @Override
                        public void getChildView(View view, int layoutResId) {
                            popup_gv = view.findViewById(R.id.popup_gv);
                            popup_gv.setAdapter(getPopupListAdapter(searchList));
                            popup_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    list1Selection = position;
                                    adapter.notifyDataSetChanged();
                                    tv_filter.setText(searchList.get(list1Selection));
                                    hint = "?????????" + searchList.get(list1Selection) + "??????";
//                                    }
                                    seachButton.setHint(hint);
                                    popupWindow.dismiss();
                                }
                            });
                        }
                    })
                    //??????????????????????????? ?????????true
                    .setOutsideTouchable(true)
                    //????????????
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
     * ??????????????????
     *
     * @param i         i = 0:?????????????????????????????????. i = 1:?????????????????????????????? i = 2:?????????????????????????????????
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
