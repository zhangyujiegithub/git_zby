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
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.newuihome.Notice;
import com.biaozhunyuan.tianyi.newuihome.NoticeDetailActivity;

import java.util.List;

import okhttp3.Request;

/**
 * 院内共享页面
 *
 * @author GaoB
 * @description:
 * @date : 2020/11/23 9:44
 */
public class ShareActivity extends BaseActivity {


    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private RelativeLayout rl_nodata;
    private CommanAdapter<Notice> adapter;
    private List<Notice> list;
    private Demand<Notice> demand;
    private int pageIndex = 1; //页码
    private DictionaryHelper dictionaryHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initView();
        initData();
        setOnEvent();
        ProgressDialogHelper.show(ShareActivity.this);
        getShareList();
    }


    private void initView() {
        headerView = findViewById(R.id.header_share_list);
        lv = findViewById(R.id.lv_share_list);
        rl_nodata = findViewById(R.id.rl_nodata);
    }

    private void initData() {
        dictionaryHelper = new DictionaryHelper(this);
        initDemand();
    }


    private void initDemand() {
        demand = new Demand<>(Notice.class);
        String url = Global.BASE_JAVA_URL + GlobalMethord.院内共享列表;
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "createTime";
//        Map<String, String> map = new HashMap<>();
//        map.put("type", "4");
//        demand.dictionaryNames = "type.dict_release_type,creatorId.base_staff,status.dict_release_status";
//        demand.keyMap = map;

        demand.dictionaryNames = "栏目.dict_shar_detailtype,creatorId.base_staff,status.dict_release_status";


        demand.src = url;
    }


    private void setOnEvent() {

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getShareList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getShareList();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Notice item = adapter.getItem(position - 1);
                    Intent intent = new Intent(ShareActivity.this, NoticeDetailActivity.class);
                    item.setTitle("院内共享");
                    intent.putExtra("Notice", item);
                    intent.putExtra("NoticeType", "院内共享");
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
    }


    private void getShareList() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                list = demand.data;


                    for (Notice notice : list) {
                        notice.setStatusName(demand.getDictName(notice, "status"));
                        notice.setTypeName(demand.getDictName(notice, "栏目"));

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

                    viewHolder.setTextValue(R.id.content_seal_receive_list, item.get标题()); //标题

                    viewHolder.setTextValue(R.id.tv_status_item_seal_receive, item.getStatusName()); //当前节点

                    ImageUtils.displyUserPhotoById(ShareActivity.this, item.getCreatorId(),
                            viewHolder.getView(R.id.head_item_seal_receive_list), Color.parseColor("#3366CC"));

                }

            }
        };
    }


}
