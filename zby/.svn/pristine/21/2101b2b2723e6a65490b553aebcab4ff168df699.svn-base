package com.biaozhunyuan.tianyi.cnis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.newuihome.Notice;
import com.biaozhunyuan.tianyi.newuihome.NoticeDetailActivity;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 规章制度页面
 *
 * @author GaoB
 * @description:
 * @date : 2020/11/23 9:44
 */
public class GuideActivity extends BaseActivity {


    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private RelativeLayout rl_nodata;
    private BoeryunSearchViewNoButton searchView;
    private View view;

    private CommanAdapter<Notice> adapter;
    private List<Notice> list;
    private Demand demand;
    private String key = "";
    private int pageIndex = 1; //页码
    private DictionaryHelper dictionaryHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
        setOnEvent();
//        ProgressDialogHelper.show(this);
        getGuideList();
    }


    private void initView() {
        headerView = findViewById(R.id.header_guide_list);
        lv = findViewById(R.id.lv_guide_list);
        rl_nodata = findViewById(R.id.rl_nodata);
        searchView = findViewById(R.id.search_view);
        view = findViewById(R.id.progress);
    }

    private void initData() {
        dictionaryHelper = new DictionaryHelper(this);
        initDemand();
    }


    private void initDemand() {
        demand = new Demand();
        String url = Global.BASE_JAVA_URL + GlobalMethord.标准院首页栏目;
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "pushTime";
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        demand.keyMap = map;
        demand.dictionaryNames = "type.dict_release_type,creatorId.base_staff,status.dict_release_status";
        demand.src = url;
    }


    private void setOnEvent() {

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getGuideList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getGuideList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Notice item = adapter.getItem(position - 1);
                    Intent intent = new Intent(GuideActivity.this, NoticeDetailActivity.class);
                    intent.putExtra("Notice", item);
                    intent.putExtra("NoticeType", "规章制度");
                    startActivity(intent);
                }
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

        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                key = str;
                pageIndex = 1;
                getGuideList();
            }
        });

        searchView.setOnButtonClickListener(new BoeryunSearchViewNoButton.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                key = "";
                pageIndex = 1;
                getGuideList();
            }

            @Override
            public void OnClick() {

            }
        });
    }


    private void getGuideList() {
        view.setVisibility(View.VISIBLE);
        demand.pageIndex = pageIndex;
        demand.keyMap.put("searchField_string_title", "1|" + key);
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                view.setVisibility(View.GONE);
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Notice.class);

                    for (Notice notice : list) {
                        notice.setStatusName(demand.getDictName(notice, "status"));
                        notice.setTypeName(demand.getDictName(notice, "type"));

                    }


                    lv.onRefreshComplete();
                    if (list != null) {
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                            if (list.size() > 0) {
                                rl_nodata.setVisibility(View.GONE);
                                lv.setVisibility(View.VISIBLE);
                            } else {
                                rl_nodata.setVisibility(View.VISIBLE);
                                lv.setVisibility(View.GONE);
                            }
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
                view.setVisibility(View.GONE);
            }

            @Override
            public void onResponseCodeErro(String result) {
                view.setVisibility(View.GONE);
            }
        });
    }

    private CommanAdapter<Notice> getAdapter(final List<Notice> list) {
        return new CommanAdapter<Notice>(list, this, R.layout.item_seal_receive_list) {
            @Override
            public void convert(int position, final Notice item, BoeryunViewHolder viewHolder) {
                if (item != null) {


                    viewHolder.setTextValue(R.id.tv_creator_seal_receive_item, dictionaryHelper.getUserNameById(item.getCreatorId())); //创建人
                    viewHolder.setTextValue(R.id.tv_item_seal_receive_type, item.getTypeName()); //类型

                    viewHolder.setTextValue(R.id.tv_creatTime_seal_receive_text_item, "创建时间"); //时间标题
                    viewHolder.setTextValue(R.id.tv_creatTime_seal_receive_item, item.getCreateTime()); //时间

                    viewHolder.getView(R.id.tv_handleTime_text_item).setVisibility(View.GONE);//
                    viewHolder.getView(R.id.tv_handleTime_seal_receive_item).setVisibility(View.GONE);//

                    viewHolder.getView(R.id.tv_no_seal_receive_item).setVisibility(View.GONE);//文号

                    viewHolder.setTextValue(R.id.content_seal_receive_list, item.getTitle()); //标题

                    viewHolder.setTextValue(R.id.tv_status_item_seal_receive, item.getStatusName()); //当前节点

                    ImageUtils.displyUserPhotoById(GuideActivity.this, item.getCreatorId(),
                            viewHolder.getView(R.id.head_item_seal_receive_list), Color.parseColor("#3366CC"));

                }

            }
        };
    }


}
