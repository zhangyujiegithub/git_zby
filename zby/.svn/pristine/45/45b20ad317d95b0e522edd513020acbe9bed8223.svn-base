package com.biaozhunyuan.tianyi.contact;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;
import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/10/23.
 * 跟进记录泳道图页面
 */
@SuppressLint("NewApi")
public class ConatactLaneFragment extends Fragment {


    private PagerContainer pagerContainer;
    private ViewPager viewPager;
    private Demand<Contact> demand;
    private String dictionary = "";
    private List<Contact> customerList = new ArrayList<>();
    private List<Contact> projectList = new ArrayList<>();
    public static boolean isReasume = false;

    private List<ContactLaneAdapter> adapterList = new ArrayList<>();
    private List<String> stageNameList = new ArrayList<>();

    private String[] colors = new String[]{"#67B5FE", "#FF7F66", "#00E0DF", "#8B76FF", "#F88546", "#FE4741", "#B7ACFF"};
    private MyPagerAdapter myPagerAdapter;
    private BoeryunSearchView seach_button;
    private ContactRecordListActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ContactRecordListActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_lane, null);
        isReasume = false;
        initViews(view);
        initDemand();
        getContactList();
        setOnTouch();
        return view;
    }

    private void setOnTouch() {
        seach_button.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                clearData();
                demand.fuzzySearch = str;
                demand.resetFuzzySearchField(true);
                getContactList();
            }
        });
        seach_button.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                clearData();
                demand.resetFuzzySearchField(false);
                getContactList();
            }

            @Override
            public void OnClick() {

            }
        });
    }

    @Override
    public void onStart() {
        if (isReasume) {
            getContactList();
            isReasume = false;
        }
        super.onStart();
    }

    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录列表 + "?from=&to=&advisorId=";
        demand = new Demand<>(Contact.class);
//        demand.pageSize = 999;
        demand.sort = "desc";
        demand.sortField = "contactTime";
        demand.dictionaryNames = "projectId.crm_project,customerId.crm_customer,stage.dict_contact_stage,contactWay.dict_contact_way";
        demand.src = url;
        demand.setFuzzySearch("crm_contact");
    }

    /**
     * 获取列表信息并展示
     */
    private void getContactList() {
        ProgressDialogHelper.show(getActivity());
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    customerList.clear();
                    projectList.clear();
                    List<Contact> data = demand.data;
                    if (data != null && data.size() > 0) {
                        for (Contact project : data) {
                            project.setCustomerName(demand.getDictName(project, "customerId"));
                            project.setProjectName(demand.getDictName(project, "projectId"));
                            project.setStageName(demand.getDictName(project, "stage"));
                            project.setContactWayName(demand.getDictName(project, "contactWay"));
                        }
                    }
                    //客户和项目的联系记录分类
                    for (Contact c : data) { //筛选客户的联系记录
                        if(TextUtils.isEmpty(c.getProjectId()) && !TextUtils.isEmpty(c.getCustomerId())){
                            if(customerList.size() > 0){ //判断当前集合是否为空 为空的话直接添加一条
                                boolean isExist = false; //判断是否添加数据
                                for (int i = 0;i<customerList.size();i++){ //遍历集合 判断集合中是否已经含有此条客户的联系记录
                                    if(customerList.get(i).getCustomerId().equals(c.getCustomerId())){
                                        isExist = true; //如果已经存在 把标志位设为true
                                        break;
                                    }
                                }
                                if(!isExist){ //如果不存在添加此条客户的联系记录
                                    customerList.add(c);
                                } else { //如果存在 标志位改为false继续走下一次循环
                                    isExist = false;
                                }
                            }else {
                                customerList.add(c);
                            }
                        }
//                        //筛选项目的联系记录
                        if(!TextUtils.isEmpty(c.getProjectId())){
                            if(projectList.size() > 0){ //判断当前集合是否为空 为空的话直接添加一条
                                boolean isExist = false; //判断是否添加数据
                                for (int i = 0;i<projectList.size();i++){ //遍历集合 判断集合中是否已经含有此条客户的联系记录
                                    if(projectList.get(i).getProjectId().equals(c.getProjectId())){
                                        isExist = true; //如果已经存在 把标志位设为true
                                        break;
                                    }
                                }
                                if(!isExist){ //如果不存在添加此条客户的联系记录
                                    projectList.add(c);
                                } else { //如果存在 标志位改为false继续走下一次循环
                                    isExist = false;
                                }
                            }else {
                                projectList.add(c);
                            }
                        }
//                        lamda表达式部分机型出现问题 暂时弃用
//                        boolean b = customerList.stream().anyMatch(o -> StrUtils.pareseNull(o.getCustomerId()).equals(StrUtils.pareseNull(c.getCustomerId())));
//                        if (!b) {
//                            if (TextUtils.isEmpty(c.getProjectId()) && !TextUtils.isEmpty(c.getCustomerId())) {
//                                customerList.add(c);
//                            }
//                        }
                    }
//
//                    for (Contact c : data) {
//
//                        boolean b = projectList.stream().anyMatch(o -> StrUtils.pareseNull(o.getProjectId()).equals(StrUtils.pareseNull(c.getProjectId())));
//                        if (!b) {
//                            if (!TextUtils.isEmpty(c.getProjectId()) && TextUtils.isEmpty(c.getCustomerId())) {
//                                projectList.add(c);
//                            }
//                        }
//                    }
//                    dictionary = JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "dictionary");
                    if(activity.getRightTitle().equals("客户泳道图")){
                        getStatusName(projectList);
                    } else {
                        getStatusName(customerList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void getStatusName(final List<Contact> taskList) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.跟进记录分类;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    adapterList.clear(); //清空旧数据
                    stageNameList.clear();
                    List<ContactStatus> data = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), ContactStatus.class);
                    int size = data.size();
                    if (size > 0) {
                        for (int i = 0; i < size; i++) {
                            List<Contact> list = new ArrayList<>();
                            for (int j = 0; j < taskList.size(); j++) {
                                if (data.get(i).getUuid().equals(taskList.get(j).getStage())) { //根据stage分类
                                    list.add(taskList.get(j));
                                }
                                //遍历到最后一条数据就初始化适配器添加到集合中 viewpager切换position会用到此集合 比直接切换数据源节省时间
                                if (j == taskList.size() - 1) {
                                    adapterList.add(new ContactLaneAdapter(list, getActivity(), dictionary));
                                }
                            }
                            //添加分类名称
                            stageNameList.add(data.get(i).getName());
                        }
                        initViewPager(stageNameList.size());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }


    private void initViewPager(int size) {
//        if(myPagerAdapter!=null){ //如果viewpager的适配器不为空 首先刷新适配器清空旧数据 防止加载数据阶段旧数据点击报空指针
//            for (int i = 0 ;i<adapterList.size();i++){
//                adapterList.get(i).notifyDataSetChanged();
//            }
//            myPagerAdapter.notifyDataSetChanged();
//        } else {
            myPagerAdapter = new MyPagerAdapter(getActivity());
            viewPager.setAdapter(myPagerAdapter);
            viewPager.setClipChildren(false);
            //
            viewPager.setOffscreenPageLimit(size + 2);

            new CoverFlow.Builder()
                    .with(viewPager)
                    .scale(0.2f)
                    .pagerMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin))
                    .spaceSize(0f)
                    .build();
//        }
    }

    private void initViews(View view) {
        pagerContainer = view.findViewById(R.id.pager_container);
        viewPager = view.findViewById(R.id.view_pager);
        seach_button = view.findViewById(R.id.seach_button);
    }

    /**
     * 切换数据源
     */
    public void updateData(BoeryunHeaderView headerView) {
        if(headerView.getRightTitleText().equals("客户泳道图")){
            if (customerList.size() > 0) {
                clearData();
                headerView.setRightTitle("项目泳道图");
                getStatusName(customerList);
            } else {
                Toast.makeText(getActivity(), "没有客户的联系记录", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        } else {
            if (projectList.size() > 0) {
                clearData();
                headerView.setRightTitle("客户泳道图");
                getStatusName(projectList);
            } else {
                Toast.makeText(getActivity(), "没有项目的联系记录", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        private LayoutInflater mInflater;

        public MyPagerAdapter(Context context) {
            if (context != null) {
                mInflater = LayoutInflater.from(context);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.item_cover, null);
            TextView tvTittle = view.findViewById(R.id.tv_tittle);
            CardView cardView = view.findViewById(R.id.cardview);
            final ListView lv = view.findViewById(R.id.lv);
            cardView.setCardBackgroundColor(Color.parseColor(colors[position % colors.length]));
            tvTittle.setText(stageNameList.get(position % (stageNameList.size() == 0 ? 1 : stageNameList.size())));
            if (adapterList.size() > 0) {
                lv.setAdapter(adapterList.get(position % adapterList.size()));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Contact item = (Contact) lv.getAdapter().getItem(position);
                        Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("contactInfo", item);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return adapterList.size() + 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        /**
         * 第五这里获得当前的positon然后对其setCurrentItem进行变换
         * 这里设置当position=0时把position设置为图片列表的最大值
         * 是为了position=0时左滑显示最后一张，我举个例子这里ImageSize是5
         * 当position==0时设置为5，左滑就是position=4，也就是第五张图片，
         * if (position == (ImageSize+2) - 1)
         * 这个判断 (ImageSize+2)这个是给viewpager设置的页面数，这里是7
         * 当position==7-1=6时，这时viewpager就滑到头了，所以把currentItem设置为1
         * 这里设置为1还是为了能够左滑，这时左滑position=0又执行了第一个判断又设置为5，
         * 这样就实现了无限轮播的效果
         * setCurrentItem(position,false);
         * 这里第二个参数false是消除viewpager设置item时的滑动动画
         */
        @Override
        public void finishUpdate(ViewGroup container) {
            int position = viewPager.getCurrentItem();
            if (position == 0) {
                position = adapterList.size();
                viewPager.setCurrentItem(position, false);
            } else if (position == (adapterList.size() + 2) - 1) {
                position = 1;
                viewPager.setCurrentItem(position, false);
            }
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private void clearData(){
        for(int i =0;i<adapterList.size();i++){
            adapterList.get(i).clearData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        seach_button.setOnCancleSearch(false);
    }
}
