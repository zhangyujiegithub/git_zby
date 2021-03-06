package com.biaozhunyuan.tianyi.newuihome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.BacklogListActivity;
import com.biaozhunyuan.tianyi.cnis.BusinessActivity;
import com.biaozhunyuan.tianyi.cnis.GuideActivity;
import com.biaozhunyuan.tianyi.cnis.InformationActivity;
import com.biaozhunyuan.tianyi.cnis.LaoWuActivity;
import com.biaozhunyuan.tianyi.cnis.LeaveActivity;
import com.biaozhunyuan.tianyi.cnis.SealUseActivity;
import com.biaozhunyuan.tianyi.cnis.SendDocManageActivity;
import com.biaozhunyuan.tianyi.cnis.ShareActivity;
import com.biaozhunyuan.tianyi.cnis.SignDocManageActivity;
import com.biaozhunyuan.tianyi.cnis.YiQingActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.models.CategoryBean;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.ybkj.HomeAdapter;
import com.biaozhunyuan.tianyi.ybkj.SholeActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Request;


/**
 * ????????? bohr
 * ????????? 2020-07-03 10:08
 * ?????????????????????
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    //fields
    private final String FIELDS = "??????";
    //templateids
    private final String TEMPLATEIDS = "dbfdf689c48c41e5ab8bc6db2ed4bdd8,e79613f127aa42cdbaa4b38d0d5d6c72";

    private SimpleIndicator indicator;
    private TextView tv_unread_new, tv_unread_old;//????????????
    private LinearLayout ll_information,ll_guide,ll_xingzhenggongwen
            ,ll_renliziyuan,ll_keyanguanli,ll_guojigongzuo
            ,ll_caigouguanli,ll_yiqing;
    private String[] titles = new String[]{"????????????", "????????????"};//, "???      ???"
    private AutoMaxHeightViewpager pager;
    private List<Fragment> fragments;

    private RefreshLayout refreshLayout;
    NewBacklogListFragment newBacklogListFragment;
    OldBacklogListFragment oldBacklogListFragment;


    private Demand demand, demand_old;
    private int selectPosition = 0;
    private HomeFragment mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        mContext=this;
        initView(view);
        setOnEvent();
        return view;
    }

    private void initView(View view) {
        indicator = view.findViewById(R.id.indicator);
        tv_unread_new = view.findViewById(R.id.tv_unread_new);
        tv_unread_old = view.findViewById(R.id.tv_unread_old);
        ll_information = view.findViewById(R.id.ll_information);
        ll_guide = view.findViewById(R.id.ll_guide);
        ll_xingzhenggongwen = view.findViewById(R.id.ll_xingzhenggongwen);
        ll_renliziyuan = view.findViewById(R.id.ll_renliziyuan);
        ll_keyanguanli = view.findViewById(R.id.ll_keyanguanli);
        ll_guojigongzuo = view.findViewById(R.id.ll_guojigongzuo);
        ll_caigouguanli = view.findViewById(R.id.ll_caigouguanli);
        ll_yiqing = view.findViewById(R.id.ll_yiqing);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        pager = view.findViewById(R.id.view_pager);
        ll_information.setOnClickListener(this);
        ll_guide.setOnClickListener(this);
        ll_xingzhenggongwen.setOnClickListener(this);
        ll_renliziyuan.setOnClickListener(this);
        ll_keyanguanli.setOnClickListener(this);
        ll_guojigongzuo.setOnClickListener(this);
        ll_caigouguanli.setOnClickListener(this);
        ll_yiqing.setOnClickListener(this);
        refreshLayout.setReboundDuration(300);//??????????????????????????????
        refreshLayout.setDragRate(0.5f);//??????????????????/????????????????????????=????????????
        refreshLayout.setEnableAutoLoadMore(false);//????????????????????????????????????????????????????????????
        refreshLayout.setEnableScrollContentWhenLoaded(false); //??????????????????????????????????????????????????????
        refreshLayout.setEnableScrollContentWhenRefreshed(false);//??????????????????????????????????????????????????????
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);//?????????????????????????????????Footer????????????1.0.4
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        newBacklogListFragment = new NewBacklogListFragment(pager, 0);
        oldBacklogListFragment = new OldBacklogListFragment(pager, 1);
        fragments.add(newBacklogListFragment);
        fragments.add(oldBacklogListFragment);
        pager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragment fragment = ((Fragment) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object obj) {
                return view == ((Fragment) obj).getView();
            }
        });
        indicator.setTabItemTitles(titles);
        pager.setOffscreenPageLimit(3);
        indicator.setViewPager(pager, 0);

    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.?????????????????????;
        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("category", Global.CATEGORY);
        keyMap.put("templateIds", TEMPLATEIDS);

        demand = new Demand();
        demand.pageSize = 10;
        demand.pageIndex = 1;
        demand.sort = "desc";
        demand.sortField = "lastUpdateTime";
        demand.dictionaryNames = "creatorId.base_staff,prevStepAuditorId.base_staff";
        demand.keyMap = keyMap;
        demand.src = url;
    }

    private void initDemand_old() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.?????????????????????;
        demand_old = new Demand();
        demand_old.src = url;
        demand_old.pageSize = 10;
        Map<String, String> map = new HashMap<>();
        map.put("category", "f4c2c84c0b90418fae46ff5915b63c0x," +
                "f4c2c84c0b90418fae46ff5915b63c0m,f4c2c84c0b90418fae46ff5915b63c0p," +
                "f4c2c84c0b90418fae46ff5915b63c6c");
        map.put("fields", "??????");
        demand_old.keyMap = map;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != refreshLayout) {
            if (selectPosition == 0) {
                newBacklogListFragment.refreshData(refreshLayout);
            }
        }
        getBackLogNum_new();
        getBackLogNum_old();
    }

    private void setOnEvent() {

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (pager.getCurrentItem() == 0) {
                    newBacklogListFragment.loadMoreData(refreshLayout);
                } else if (pager.getCurrentItem() == 1) {
                    oldBacklogListFragment.loadMoreData(refreshLayout);
                    refreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {

                refreshLayout.setNoMoreData(false);
                if (pager.getCurrentItem() == 0) {
                    newBacklogListFragment.refreshData(refreshLayout);
                    getBackLogNum_new();
                } else if (pager.getCurrentItem() == 1) {
                    oldBacklogListFragment.refreshData(refreshLayout);
                    getBackLogNum_old();
                }
                refreshLayout.finishRefresh();
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pager.resetHeight(position);
            }

            @Override
            public void onPageSelected(int position) {
                refreshLayout.setNoMoreData(false);//???????????????????????????????????????
                selectPosition = position;
                pager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * ??????????????????
     */
    private void getBackLogNum_new() {
        initDemand();
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(JsonUtils.getStringValue(response, "Data"));
                    int backlogNum = jo.getInt("total");
                    showOrHideUnread_new(backlogNum);
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

    /**
     * ?????????????????????
     */
    private void getBackLogNum_old() {

        initDemand_old();
        demand_old.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    int backlogNum = Integer.valueOf(JsonUtils.getStringValue(JsonUtils.pareseData(response), "total"));
//                    showOrHideUnread_old(backlogNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {

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

    private void showOrHideUnread_new(int unReadNum) {

        if (unReadNum > 0) {
            tv_unread_new.setVisibility(View.VISIBLE);
            if (unReadNum > 99) {
                tv_unread_new.setText("99+");
            } else {
                tv_unread_new.setText(unReadNum + "");
            }
        } else {
            tv_unread_new.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_xingzhenggongwen: //????????????
            case R.id.ll_renliziyuan: //????????????
            case R.id.ll_keyanguanli: //????????????
            case R.id.ll_guojigongzuo: //????????????
            case R.id.ll_caigouguanli: //????????????
            case R.id.ll_guide://????????????
                alertVerifyDialog(v.getId());
                break;
            case R.id.ll_information://????????????
                intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_yiqing://????????????
                intent = new Intent(getActivity(), YiQingActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

    private void window_loadData(Map<Integer,CategoryBean> map) {
        homeList.clear();
        for (Integer key : map.keySet()) {
            String iconName= Objects.requireNonNull(map.get(key)).getIconName();
            int viewId= Objects.requireNonNull(map.get(key)).getViewId();
            homeList.add(new CategoryBean(iconName,key,viewId));
        }
        homeAdapter.notifyDataSetChanged();
    }

    private void window_initView(AlertDialog view,Context context) {
        gridView = view.findViewById(R.id.gridView);
        homeAdapter = new HomeAdapter(context, homeList);
        gridView.setAdapter(homeAdapter);
        gridView.setOnItemClickListener((parent, view12, position, id) -> {
            Intent intent;
            if(homeList.get(position).getViewId()==R.id.ll_send_doc){
                intent = new Intent(getActivity(), SendDocManageActivity.class);
            }else if(homeList.get(position).getViewId()==R.id.ll_sign){
                intent = new Intent(getActivity(), SignDocManageActivity.class);
            }else if(homeList.get(position).getViewId()==R.id.ll_seal_use){
                intent = new Intent(getActivity(), SealUseActivity.class);
            }else if(homeList.get(position).getViewId()==R.id.ll_take_break){
                intent = new Intent(getActivity(), LeaveActivity.class);
            }else if(homeList.get(position).getViewId()==R.id.ll_business){
                intent = new Intent(getActivity(), BusinessActivity.class);
            }else if(homeList.get(position).getViewId()==R.id.ll_laowu){
                intent = new Intent(getActivity(), LaoWuActivity.class);
            }else if(homeList.get(position).getViewId()==7001){
                intent = new Intent(getActivity(), GuideActivity.class);
            }else {
                intent = new Intent(getActivity(), SholeActivity.class);
                intent.putExtra("type", homeList.get(position).getViewId());
            }
            startActivity(intent);
            alert.dismiss();
        });
    }

    private final List<CategoryBean> homeList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private GridView gridView;
    //???????????????????????????id
    private int[] send_doc_icon={R.drawable.new_icon_send_doc,R.drawable.new_icon_sign
            ,R.drawable.new_icon_seal_use,R.drawable.yuejian};
    //???????????????????????????id
    private int[] send_doc_id={R.id.ll_send_doc,R.id.ll_sign,R.id.ll_seal_use,1433223};
    //?????????????????????????????????
    private String[] send_doc_name={"????????????","????????????","????????????","????????????"};
    //???????????????????????????id
    private int[] sign_icon={R.drawable.new_icon_take_break,R.drawable.new_icon_leave
            ,R.drawable.ll_laowu,R.drawable.ll_renyuanzhaopin,R.drawable.ll_gangweipinyong
            ,R.drawable.ll_qiyebianzhi,R.drawable.ll_zhigong,R.drawable.ll_renyuanwaijie
            ,R.drawable.ll_diaochushenpi,R.drawable.ll_qiyebeian,R.drawable.ll_lingdaoyinsichuguo
            ,R.drawable.ll_chuguoshenqing,R.drawable.ll_zhaoshoushenqing};
    //???????????????????????????id
    private int[] sign_id={R.id.ll_take_break,R.id.ll_business,R.id.ll_laowu,R.id.ll_renyuanzhaopin
            ,R.id.ll_gangweipinyong,R.id.ll_qiyebianzhi,R.id.ll_zhigong
            ,R.id.ll_renyuanwaijie,R.id.ll_diaochushenpi,R.id.ll_qiyebeian
            ,R.id.ll_lingdaoyinsichuguo,R.id.ll_chuguoshenqing,R.id.ll_zhaoshoushenqing};
    //?????????????????????????????????
    private String[] sign_name={"????????????","????????????","????????????","????????????"
            ,"????????????","????????????","????????????","????????????"
            ,"????????????","????????????","????????????","????????????"
            ,"????????????"};
    //???????????????????????????id
    private int[] keyan_icon={R.drawable.keyanshixiangshengpi,R.drawable.hengxiangshengbao
            ,R.drawable.jijinxiangmushengbao,R.drawable.keyanweituolixiang
            ,R.drawable.jishuweiyuanhui,R.drawable.kexuijishushengbao};
    //???????????????????????????id
    private int[] keyan_id={100001,100002,100003,100004,100005,100006};
    //?????????????????????????????????
    private String[] keyan_name={"??????????????????","??????????????????","????????????????????????","????????????????????????"
            ,"???????????????????????????","????????????????????????"};
    //???????????????????????????id
    private int[] guoji_icon={R.drawable.bzhhd,R.drawable.jhsb
            ,R.drawable.cfzx,R.drawable.xsgjhy
            ,R.drawable.jdlf,R.drawable.gjbzhpx};
    //?????????????????????????????????
    private String[] guoji_name={"???????????????","????????????","????????????","??????????????????"
            ,"????????????","?????????????????????"};
    //???????????????????????????id
    private int[] caigou_icon={R.drawable.xxhcg,R.drawable.bgyp
            ,R.drawable.bgjj,R.drawable.yqsb};
    private int[] caigou_id={60001,60002,60003,60004};
    //?????????????????????????????????
    private String[] caigou_name={"???????????????","??????????????????","??????????????????","??????????????????"};
    //???????????????????????????id
    private int[] myoa_icon={R.drawable.new_icon_guide};
    //???????????????????????????id
    private int[] myoa_id={7001};
    //?????????????????????????????????
    private String[] myoa_name={"????????????"};
    AlertDialog alert;
    public void alertVerifyDialog(int viewId) {
        Context context= mContext.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        alert = builder.create();
        alert.show();
        alert.setContentView(R.layout.window_category);
        window_initView(alert,context);
        Map<Integer,CategoryBean> map=new LinkedHashMap<>();
        if(viewId == R.id.ll_xingzhenggongwen){
            for (int i = 0; i < send_doc_id.length; i++) {
                map.put(send_doc_icon[i],new CategoryBean(send_doc_name[i],send_doc_icon[i],send_doc_id[i]));
            }
        }else if(viewId == R.id.ll_renliziyuan){
            for (int i = 0; i < sign_id.length; i++) {
                map.put(sign_icon[i],new CategoryBean(sign_name[i],sign_icon[i],sign_id[i]));
            }
        }else if(viewId == R.id.ll_keyanguanli){
            for (int i = 0; i < keyan_icon.length; i++) {
                map.put(keyan_icon[i],new CategoryBean(keyan_name[i],keyan_icon[i],keyan_id[i]));
            }
        }else if(viewId == R.id.ll_guojigongzuo){
            for (int i = 0; i < guoji_icon.length; i++) {
                map.put(guoji_icon[i],new CategoryBean(guoji_name[i],guoji_icon[i],0));
            }
        }else if(viewId == R.id.ll_caigouguanli){
            for (int i = 0; i < caigou_icon.length; i++) {
                map.put(caigou_icon[i],new CategoryBean(caigou_name[i],caigou_icon[i],caigou_id[i]));
            }
        }else if(viewId == R.id.ll_guide){
            for (int i = 0; i < myoa_icon.length; i++) {
                map.put(myoa_icon[i],new CategoryBean(myoa_name[i],myoa_icon[i],myoa_id[i]));
            }
        }
        window_loadData(map);
        Objects.requireNonNull(alert.getWindow()).clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }
}
