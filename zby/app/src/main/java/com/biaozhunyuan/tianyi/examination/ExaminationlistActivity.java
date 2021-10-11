package com.biaozhunyuan.tianyi.examination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 考试中心列表
 */

public class ExaminationlistActivity extends BaseActivity {

    private PullToRefreshAndLoadMoreListView lv;
    private BoeryunHeaderView headerView;
    private int pageIndex = 1;
    private Demand<Examination> demand;
    private CommanAdapter<Examination> adapter;
    private DictionaryHelper helper;
    private SimpleDateFormat format;
    private Date date;
    private CommonPopupWindow popupWindow;
    private BoeryunSearchView seach_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinationlist);
        initView();
        initDemand();
        getList();
        setOnTouch();
    }

    private void setOnTouch() {
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });
        seach_button.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                lv.startRefresh();
                pageIndex = 1;
                Map<String,String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_name","");
                demand.keyMap = searchMap;
                getList();
            }

            @Override
            public void OnClick() {

            }
        });

        seach_button.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                lv.startRefresh();
                pageIndex = 1;
                Map<String,String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_name","1|" + str);
                demand.keyMap = searchMap;
                getList();
            }
        });
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position>0){
//                    Examination item = adapter.getItem(position - 1);
//                    final Intent intent = new Intent(ExaminationlistActivity.this,ExaminationInfoActivity.class);
//                    String url = Global.BASE_JAVA_URL + GlobalMethord.查看答卷 + item.getUuid() + "&name=" + item.getName();
//                    intent.putExtra("url",url);
//                    startActivity(intent);
//                }
//            }
//        });
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                date = new Date();
                List<Examination> list = demand.data;
                lv.onRefreshComplete();
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

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.考试中心列表;
        demand = new Demand<>(Examination.class);
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.src = url;
    }

    private void initView() {
        helper = new DictionaryHelper(this);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        headerView = findViewById(R.id.boeryun_headerview);
        lv = findViewById(R.id.lv);
        seach_button = findViewById(R.id.seach_button);
        seach_button.setHint("搜索关键字");
    }

    private CommanAdapter<Examination> getAdapter(List<Examination> gridItems) {

        return new CommanAdapter<Examination>(gridItems, this, R.layout.item_exanination_list) {
            public void convert(int position, final Examination item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_name,item.getName());
                viewHolder.setTextValue(R.id.tv_time,item.getCreateTime());
                viewHolder.setTextValue(R.id.tv_customer,helper.getUserNameById(item.getCreatorId()));
                final TextView score = viewHolder.getView(R.id.tv_score);
                boolean showreform = false;

                try {
                    Date endTime = format.parse(item.getEndTime());
                    if(item.getIsFilled().equals("0")){
                        if(endTime.getTime()<date.getTime()){
                            score.setText("已关闭");
                        }else {
                            score.setText("开始答卷");
                        }
                    }else {
                        if(endTime.getTime()<date.getTime()){
                            showreform = false;
                        }else {
                            showreform = true;
                        }
                        score.setText(item.getScore() + "分 ∨");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final boolean finalShowreform = showreform;
                score.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(score.getText().toString().equals("开始答卷")){
                            final Intent intent = new Intent(ExaminationlistActivity.this,ExaminationInfoActivity.class);
                            String url = Global.BASE_JAVA_URL + GlobalMethord.重做答卷 + item.getUuid() + "&name=" + item.getName();
                            intent.putExtra("url",url);
                            startActivity(intent);
                        }else if(score.getText().toString().contains("∨")){
                            showPopup(score,item, finalShowreform);
                        }
                    }
                });
            }
        };
    }
    private void showPopup(TextView view, final Examination item, final boolean showreform){
        popupWindow = new CommonPopupWindow.Builder(ExaminationlistActivity.this)
                .setView(R.layout.popup_score)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        TextView lookover = view.findViewById(R.id.tv_lookover);
                        TextView reform = view.findViewById(R.id.tv_reform);
                        View view1 = view.findViewById(R.id.view);
                        if(showreform){
                            view1.setVisibility(View.VISIBLE);
                            reform.setVisibility(View.VISIBLE);
                        }else {
                            view1.setVisibility(View.GONE);
                            reform.setVisibility(View.GONE);
                        }
                        final Intent intent = new Intent(ExaminationlistActivity.this,ExaminationInfoActivity.class);
                        lookover.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = Global.BASE_JAVA_URL + GlobalMethord.查看答卷 + item.getUuid() + "&name=" + item.getName() + "&advisorId=" + Global.mUser.getUuid();
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        });
                        reform.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = Global.BASE_JAVA_URL + GlobalMethord.重做答卷 + item.getUuid() + "&name=" + item.getName();
                                intent.putExtra("url",url);
                                startActivity(intent);
                            }
                        });
                    }
                })
                .setOutsideTouchable(true)
                //开始构建
                .create();
        popupWindow.showAsDropDown(view, 40, 0);
    }

}
