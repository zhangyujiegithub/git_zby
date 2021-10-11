package com.biaozhunyuan.tianyi.buglist;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

/**
 * bug列表
 */

public class BugListActivity extends BaseActivity {


    private BoeryunHeaderView headerview;
    private BoeryunSearchView seachButton;
    private PullToRefreshAndLoadMoreListView lv;
    private Demand<Bug> demand;
    private int pageIndex = 1;
    private CommanAdapter<Bug> adapter;
    public static boolean isResume = false;
    private Context mContext;
    private CommonPopupWindow popupWindow;
    private TagAdapter<String> tagAdapter;
    private LayoutInflater mInflater;
    private Set<Integer> mSelectPosSet = new HashSet<Integer>();
    private List<字典> 字典s = new ArrayList<>();
    private ArrayList<String> datas = new ArrayList();
    private int currentSelected = 0;
    private DictionaryHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_list);
        initData();
        initView();
        initDemand();
        getCompanyProject();
        setOnTouch();
    }

    /**
     * 获取项目列表
     */
    private void getCompanyProject() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取可选择的项目;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                if (字典s.size() > 0) {
                    for (字典 z : 字典s) {
                        datas.add(z.getName());
                    }
                }
                demand.src = Global.BASE_JAVA_URL + GlobalMethord.BUG列表 + 字典s.get(0).getUuid();
                getBugList();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void initData() {
        mContext = this;
        mInflater = LayoutInflater.from(mContext);
        helper = new DictionaryHelper(mContext);
    }

    private void setOnTouch() {
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {
                showPopupWindow();
            }

            @Override
            public void onClickSaveOrAdd() {
                skip(BugInfoActivity.class);
            }
        });
        seachButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                searchList(str, true);
            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                searchList("", false);
            }

            @Override
            public void OnClick() {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Bug item = adapter.getItem(position - 1);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bugInfo", item);
                    skip(BugInfoActivity.class, bundle);
                }
            }
        });
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getBugList();
            }
        });
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBugList();
            }
        });
    }

    /**
     * 搜索
     *
     * @param searchStr 搜索关键字
     * @param isSearch  true:开始搜索 false:取消搜索
     */
    private void searchList(String searchStr, boolean isSearch) {
        Map<String,String> searchMap = new HashMap<>();
        searchMap.put("searchField_string_content","1|" + searchStr);
        demand.keyMap = searchMap;
        pageIndex = 1;
        getBugList();
    }

    /**
     * 选择项目
     */
    private void showPopupWindow() {
        popupWindow = new CommonPopupWindow.Builder(mContext)
                //设置PopupWindow布局
                .setView(R.layout.popup_bug)
                //设置宽高
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                        750)
                //设置动画
                .setAnimationStyle(R.style.AnimDown)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                .setBackGroundLevel(1.0f)
                //设置PopupWindow里的子View及点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        TagFlowLayout flowlayout = view.findViewById(R.id.tgflowlayout_multi_picker);
                        flowlayout.setMaxSelectCount(1);
                        tagAdapter = new TagAdapter<String>(datas) {
                            @Override
                            public View getView(FlowLayout parent, int position, String t) {
                                Logger.i("tagA" + position + "--" + t);
                                TextView tv = (TextView) mInflater.inflate(
                                        R.layout.item_label_customer_list, flowlayout, false);
                                tv.setText(t);
                                return tv;
                            }
                        };
                        flowlayout.setAdapter(tagAdapter);
                        tagAdapter.setSelectedList(currentSelected);

                        flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
                            @Override
                            public void onSelected(Set<Integer> selectPosSet) {
                                mSelectPosSet = selectPosSet;
                                String companyType = "";
                                for (Integer pos : mSelectPosSet) {
                                    currentSelected = pos;
                                    companyType = 字典s.get(pos).getUuid();
                                }
                                if (!TextUtils.isEmpty(companyType)) {
                                    demand.src = Global.BASE_JAVA_URL + GlobalMethord.BUG列表 + companyType;
                                    pageIndex = 1;
                                    getBugList();
                                }
                                popupWindow.dismiss();
                            }
                        });
                    }
                })
                //设置外部是否可点击 默认是true
                .setOutsideTouchable(true)
                //开始构建
                .create();
        popupWindow.showAsDropDown(headerview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            getBugList();
            isResume = true;
        }
    }

    /**
     * 获取bug列表
     */
    private void getBugList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<Bug> data = demand.data;
                try {
                    for (Bug bug : data) {
                        bug.setStatusName(demand.getDictName(bug, "status"));
                        for (字典 s : 字典s){
                            if(bug.getProjectMange().equals(s.getUuid())){
                                bug.setProjectManagementName(s.getName());
                                bug.setProjectManagement(s.getUuid());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(data);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(data, false);
                    if (data != null && data.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }

    private CommanAdapter<Bug> getAdapter(List<Bug> bugList) {
        return new CommanAdapter<Bug>(bugList, this, R.layout.item_bug_list) {
            @Override
            public void convert(int position, final Bug item, final BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.content_contact_list, item.getContent());
                viewHolder.setTextValue(R.id.tv_time_contact_item, item.getCreateTime());
                viewHolder.setTextValue(R.id.tv_advisor_contact_item, helper.getUserNameById(item.getCreatorId()));
                viewHolder.setUserPhoto(R.id.head_item_contact_list, item.getCreatorId());
                viewHolder.setTextValue(R.id.tv_status_item_contact, item.getStatusName());
                MultipleAttachView attachView = viewHolder.getView(R.id.attach_item_contact);
                if(!TextUtils.isEmpty(item.getAttachmentIds())){
                    attachView.setVisibility(View.VISIBLE);
                } else {
                    attachView.setVisibility(View.GONE);
                }
                attachView.loadImageByAttachIds(item.getAttachmentIds());
            }
        };
    }

    private void initDemand() {
        demand = new Demand<>(Bug.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.dictionaryNames = "status.dict_oa_bug";
//        demand.setFuzzySearch("oa_bug");
    }

    private void initView() {
        headerview = findViewById(R.id.boeryun_headerview);
        seachButton = findViewById(R.id.seach_button);
        lv = findViewById(R.id.lv);
    }

    @Override
    protected void onStop() {
        super.onStop();
        seachButton.setOnCancleSearch(false);
    }
}
