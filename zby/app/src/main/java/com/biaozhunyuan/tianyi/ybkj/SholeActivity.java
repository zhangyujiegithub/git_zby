package com.biaozhunyuan.tianyi.ybkj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.List;

public class SholeActivity extends BaseActivity {

    private BoeryunHeaderView header;

    private Context context;
    private String[] tabs = new String[]{"已办", "全部"};
    private String workflowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        //setContentView(R.layout.activity_shole);
        context = this;
        initViews();
        setOnEvent();
    }

    private void initViews() {
        int souceId = getIntent().getIntExtra("type", -1);
        LogUtils.e("TAG","TAG===>>"+souceId);
        header = findViewById(R.id.header);
        SimpleIndicator indicator = findViewById(R.id.indicator);
        ViewPager pager = findViewById(R.id.pager);
        indicator.setViewPager(pager, 0);
        List<Fragment> fragments = new ArrayList<>();
        showIdType(souceId);
        if(souceId == 100001) {
            fragments.add(SholeFragment.newInstance(true, getToJson(String.valueOf(souceId)), Global.KYYB));
            fragments.add(SholeFragment.newInstance(true, getToJson(""),""));
        }else if(souceId == 100002) {
            tabs= Global.HX;
            indicator.setVisibleTabCount(tabs.length);
            for (int i = 0; i < tabs.length; i++) {
                fragments.add(SholeFragment.newInstance(true
                        , getToJson(i==0?String.valueOf(souceId):""),tabs[i]+"横向"));
            }
        }else if(souceId == 100003) {
            tabs= Global.Y;
            indicator.setVisibleTabCount(3);
            for (int i = 0; i < tabs.length; i++) {
                fragments.add(SholeFragment.newInstance(true
                        , getToJson((i==6||i==7||i==8)?String.valueOf(souceId):""),tabs[i]+"院"));
            }
        }else if(souceId == 100004) {
            tabs= Global.KYWT;
            indicator.setVisibleTabCount(tabs.length);
            for (int i = 0; i < tabs.length; i++) {
                fragments.add(SholeFragment.newInstance(true
                        , getToJson((i==1)?String.valueOf(souceId):""),tabs[i]+"科研委托"));
            }
        }else if(souceId == 60001) {
            tabs= new String[]{Global.CG_XXHCG};
            indicator.setVisibleTabCount(tabs.length);
            fragments.add(SholeFragment.newInstance(true, getToJson(""),tabs[0]));
        }else{
            fragments.add(SholeFragment.newInstance(false, getToJson(String.valueOf(souceId)),""));
            fragments.add(SholeFragment.newInstance(true, getToJson(""),""));
        }
        indicator.setTabItemTitles(tabs);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                //Fragment fragment = ((Fragment) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
                return view == ((Fragment) obj).getView();
            }
        });
    }

    private void showIdType(int souceId) {
        if(souceId ==R.id.ll_renyuanzhaopin){
            header.setTitle("人员招聘审批");
            workflowId="42f74303d23746e5a14e907840779feb";
        }else if(souceId ==1433223){
            header.setTitle("阅件列表");
            workflowId="9c2558a510c042778e6ajg6c36bb4321";
        }else if(souceId ==R.id.ll_gangweipinyong){
            header.setTitle("岗位聘用合同审批");
            workflowId="3f90164fc6b84861be8ae4ad06a1e37c";
        }else if(souceId ==R.id.ll_qiyebianzhi){
            header.setTitle("企业编制招聘");
            workflowId="bdb62038bbe94710aa941c84b81ee09c";
        }else if(souceId ==R.id.ll_zhigong){
            header.setTitle("职工内部调动");
            workflowId="bb5c536620384093a9642b8e2d80abc0";
        }else if(souceId ==R.id.ll_renyuanwaijie){
            header.setTitle("人员外借审批");
            workflowId="bf12ce0e5f6c4c209f0faf8657b6730d";
        }else if(souceId ==R.id.ll_diaochushenpi){
            header.setTitle("调出人员审核");
            workflowId="1151aed25d4448ba9c1ebb46bf2acf05";
        }else if(souceId ==R.id.ll_qiyebeian) {
            header.setTitle("企业编工资变动备案");
            workflowId = "3e4a563e35964af6b65dce3c93125bf9";
        }else if(souceId ==R.id.ll_lingdaoyinsichuguo) {
            header.setTitle("领导因私出国");
            workflowId = "18326beeeeed455b8be3e003b6ca6e71";
        }else if(souceId ==R.id.ll_chuguoshenqing) {
            header.setTitle("员工出国申请");
            workflowId = "be80eee85dc544d886b3363f60e7efb9";
        }else if(souceId ==R.id.ll_zhaoshoushenqing) {
            header.setTitle("博士后招收申请");
            workflowId = "0e6365b8c7974ca782041339eb486778";
        }else if(souceId ==100001) {
            header.setTitle("科研事项审批");
            workflowId = "e86b872cd37544b3914325fd892481e3";
        }else if(souceId ==100002) {
            header.setRightIconVisible(false);
            header.setTitle("横向项目申报");
            workflowId = "e86b872ca37844b3914325fd892481e3";
        }else if(souceId ==100003) {
            header.setTitle("院长基金项目");
            workflowId = "n23p422cd67451b3914325fd892481e3";
        }else if(souceId ==100004) {
            header.setTitle("科研委托项目");
            workflowId = "n23p422cd67451b3914325fd89356yxc";
        }else if(souceId ==100005) {
            header.setTitle("技术委员会申请筹建");
            workflowId = "n23p422cd67451b3914325fd89paphhh";
        }else if(souceId ==100006) {
            header.setTitle("院科学技术奖申报");
            workflowId = "6ad4e86014ca4f528409a25fd7a614a4";
        }else if(souceId == 60001){
            header.setTitle("信息化采购");
            workflowId = "4363180c684143e88435c2062b88e272";
        }
    }

    private String getToJson(String souceId) {
        StringBuilder sb = new StringBuilder();
        if(TextUtils.isEmpty(souceId)) {
            return "{\"title\": \"" + header.getTitle() + "\"," +
                    "\"workflowTemplateId\": \"" + workflowId + "\"," +
                    "\"formName\": \"" + header.getTitle() + "\"," +
                    "\"status\": \"9\"," +
                    "\"showDelete\": \"1\"," +
                    "\"showCancel\": \"1\"," +
                    "\"jsFileId\": \"9\"," +
                    "\"sort\": \"desc\"," +
                    "\"sortField\": \"createTime\"}";
        }else{
            switch (souceId) {
                case "100001":
                    sb.append("{\"title\": \"")
                            .append(header.getTitle()).append("\",")
                            .append("\"workflowTemplateId\": \"").append(workflowId).append("\",")
                            .append("\"formName\": \"").append(header.getTitle()).append("\",")
                            .append("\"status\": \"5\",")
                            .append("\"type\": \"2\",")
                            .append("\"hideStatus\": \"1\",")
                            .append("\"showCancel\": \"1\",")
                            .append("\"sort\": \"desc\",")
                            .append("\"sortField\": \"createTime\"}");
                    break;
                case "100002":
                    sb.append("{\"status\": \"1\",")
                            .append("\"sort\": \"desc\",")
                            .append("\"sortField\": \"createTime\"}");
                    break;
                case "100003":
                    sb.append("{\"type\": \"1\",")
                            .append("\"sort\": \"desc\",")
                            .append("\"sortField\": \"createTime\"}");
                    break;
                case "100004":
                    sb.append("{\"tableName\": \"科研委托合同审批\",")
                            .append("\"sort\": \"desc\",")
                            .append("\"sortField\": \"createTime\"}");
                    break;
                default:
                    sb.append("{\"category\": \"33fb40edb10948e6a5442dd4141d1607\",")
                            .append("\"searchField_string_workflowTemplate\": \"1|").append(workflowId).append("\"}");
                    break;
            }
        }
        return sb.toString();
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
                Intent intent = new Intent();
                intent.setClass(context, FormInfoActivity.class);
                intent.putExtra("workflowTemplateId", workflowId);
                intent.putExtra("formDataId", "0");
                startActivity(intent);
            }
        });
    }
}