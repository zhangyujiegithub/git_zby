package com.biaozhunyuan.tianyi.information;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;

import java.io.IOException;
import java.util.List;

import cn.droidlover.xrichtext.ImageLoader;
import cn.droidlover.xrichtext.XRichText;
import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-04-01 10:55
 * 描述：资讯列表页面
 */
public class InformationListActivity extends BaseActivity {

    private BoeryunHeaderView header;
    private PullToRefreshAndLoadMoreListView lv;


    private Context mContext;
    private Demand<InformationModel> demand;
    private CommanAdapter<InformationModel> adapter;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_list);
        initViews();
        initData();
        setOnEvent();
    }

    private void initViews() {
        header = findViewById(R.id.header_information_list);
        lv = findViewById(R.id.listview);
    }

    private void initData() {
        mContext = InformationListActivity.this;
        initDemand();
        getInformationList();
    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.资讯列表;
        demand = new Demand<>(InformationModel.class);
        demand.src = url;
        demand.pageIndex = pageIndex;
        demand.pageSize = 10;
        demand.sortField = "isTop desc,createTime desc";
        demand.dictionaryNames = "category.mall_information_category";
    }


    /**
     * 获取资讯列表
     */
    private void getInformationList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<InformationModel> list = demand.data;
                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(list);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(list, false);
                    lv.loadCompleted();
                    if (list != null && list.size() == 0) {
                        lv.loadAllData();
                    }
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


    private CommanAdapter<InformationModel> getAdapter(List<InformationModel> list) {
        return new CommanAdapter<InformationModel>(list, getBaseContext(), R.layout.item_information_list) {
            @Override
            public void convert(int position, final InformationModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_space_title, item.getTitle()); //文章标题
                XRichText tvContent = viewHolder.getView(R.id.tv_content_notice_item);
                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startInfoActivity(item);
                    }
                });
                tvContent.callback(new XRichText.BaseClickCallback() {
                    @Override
                    public void onImageClick(List<String> urlList, int position) {
                        super.onImageClick(urlList, position);
                        startInfoActivity(item);
                    }

                    @Override
                    public boolean onLinkClick(String url) {
                        return super.onLinkClick(url);
                    }

                    @Override
                    public void onFix(XRichText.ImageHolder holder) {
                        super.onFix(holder);
                    }
                }).imageDownloader(new ImageLoader() {
                    @Override
                    public Bitmap getBitmap(String url) throws IOException {
                        return ImageUtils.getBitmapByUrl(Global.BASE_JAVA_URL + url);
                    }
                }).text(item.getContent());
            }
        };
    }


    private void startInfoActivity(InformationModel item) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.资讯详情H5
                + "?informationId=" + item.getUuid()
                + "&corpId=" + Global.mUser.getCorpId()
                + "&advisorId=" + Global.mUser.getUuid()
                + "&customerId="
                + "&isClient=1";
        Intent intent = new Intent(mContext, WebviewNormalActivity.class);
        intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
        intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, "资讯详情");
        intent.putExtra("isShowShare", true);
        intent.putExtra("InformationInfo", item);
        startActivity(intent);
    }


    private void setOnEvent() {
        header.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getInformationList();
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getInformationList();
            }
        });
    }
}
