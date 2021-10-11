package com.biaozhunyuan.tianyi.newuihome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.base.LazyFragment;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 *
 *院内共享
 * @author GaoB
 * @description:
 * @date : 2020/11/19 9:25
 */


@SuppressLint("ValidFragment")
public class ShareSubjectListFragment extends LazyFragment {


    private Context context;
    private int pageIndex = 1; //当前页
    private Demand demand = new Demand();//分页需上传实体类
    private ArrayList<String> datas = new ArrayList();
    private CommanAdapter<Notice> adapter;
    private List<Notice> list;

    private NoScrollListView lv;
    private RelativeLayout rl_nodata;
    private int fragmentID = 3;
    private AutoMaxHeightViewpager vp;
    private View v;


    public static boolean isResume = false;
    private User mUser = Global.mUser;


    public ShareSubjectListFragment(AutoMaxHeightViewpager vp, int fragmentID) {
        this.vp = vp;
        this.fragmentID = fragmentID;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_partysubject_list_new, null);
        initView(v);
        initData();
        initDemand();
        vp.setObjectForPosition(v, fragmentID);
        ProgressDialogHelper.show(getActivity());
        getNoticeList(null);
        setOnEvent();
        return v;
    }




    @Override
    public void onStart() {
        super.onStart();
        if (isResume) {
            pageIndex = 1;
            ProgressDialogHelper.show(getActivity());
            getNoticeList(null);
            isResume = false;
        }
    }



    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        rl_nodata = v.findViewById(R.id.rl_nodata);
    }


    private void setOnEvent() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notice item = adapter.getItem(position );
                Intent intent = new Intent(context, NoticeDetailActivity.class);
                intent.putExtra("Notice", item);
                intent.putExtra("NoticeType", "院内共享");
                startActivity(intent);
            }
        });
    }



    private void initData() {
        context = getActivity();
    }

    private void initDemand() {

        String url = Global.BASE_JAVA_URL + GlobalMethord.标准院首页栏目;
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "pushTime";
        Map<String, String> map =  new HashMap<>();
        map.put("type","4");
        demand.dictionaryNames = "type.dict_release_type,creatorId.base_staff,status.dict_release_status";
        demand.keyMap = map;
        demand.src = url;

    }


    public void loadMoreData(RefreshLayout refreshLayout) {
        getNoticeList(refreshLayout);
    }

    public void refreshData(RefreshLayout refreshLayout) {
        pageIndex = 1;
        getNoticeList(refreshLayout);
    }


    private void getNoticeList(final RefreshLayout refreshLayout) {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Notice.class);
                    if (list != null) {
                        if (refreshLayout != null) {
                            refreshLayout.finishRefresh();
                        }
                        if (pageIndex == 1) {
                            adapter = getAdapter(list);
                            lv.setAdapter(adapter);
                            if(list.size()>0){
                                rl_nodata.setVisibility(View.GONE);
                                lv.setVisibility(View.VISIBLE);
                            }else{
                                rl_nodata.setVisibility(View.VISIBLE);
                                lv.setVisibility(View.GONE);
                            }

                        } else {
                               adapter.addBottom(list, false);
                            if (list != null && list.size() == 0) {
                                Toast.makeText(context, "没有更多数据", Toast.LENGTH_SHORT).show();
                            }
                            if (refreshLayout != null) {
                                refreshLayout.finishLoadMore();
                            }
                        }
                        pageIndex += 1;
                        if (list.size()>0)
                        vp.resetHeight(fragmentID);
                    } else {
                        if (adapter != null) {
                            adapter.clearData();
                        }
                    }
                    ProgressDialogHelper.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (adapter != null) {
                        adapter.clearData();
                    }
                    ProgressDialogHelper.dismiss();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                if (adapter != null) {
                    adapter.clearData();
                }
            }
        });
    }


    private CommanAdapter<Notice> getAdapter(final List<Notice> list) {
        return new CommanAdapter<Notice>(list, context, R.layout.item_party_subject_new) {
            @Override
            public void convert(int position, final Notice item, BoeryunViewHolder viewHolder) {
                if (item != null) {
                    viewHolder.setTextValue(R.id.tv_title, item.getTitle()); //标题
                    viewHolder.setTextValue(R.id.tv_content, item.getPushTime().substring(0,10)); //标题
                    if (position == list.size() - 1) {
                        ProgressDialogHelper.dismiss();
                    }
                }
            }
        };
    }





}
