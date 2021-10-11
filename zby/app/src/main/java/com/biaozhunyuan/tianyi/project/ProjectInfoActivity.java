package com.biaozhunyuan.tianyi.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.client.ChClientBiz;
import com.biaozhunyuan.tianyi.client.ChClientTabFragment;
import com.biaozhunyuan.tianyi.client.ClientProjectListFragment;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 项目详情
 */
@SuppressLint("NewApi")
public class ProjectInfoActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private BoeryunViewpager viewpager;
    private SimpleIndicator indeicatior;
    private List<Fragment> mFragments;
    private Context mContext;
    private ProjectInformationFragment projectInformationFragment;
    private int currentItem = 0;
    private Project mProject;
    public static String PRURL = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?id=0&workflowTemplateId=";
    public static String PRURL1 = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?workflowTemplateId=";
    public static String PRSTR = "暂无";
    public static String PRPROJECT_LISTDATA = "project_list_data";
    //    private String[] tableTitles = {"项目信息", Global.CONTACT_TITLE};
    private List<String> tableTitles;
    private List<Project> projectForm = new ArrayList<>();
    private boolean isCanNewForm = false;
    private boolean milestoneFlag = false;
    private int currentItemIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        initView();
        getIntentData();
        getShowContactRecordPermission();
        setOnTouch();
    }


    /**
     * 获取是否显示联系记录页面的权限
     */
    private void getShowContactRecordPermission() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.查询是否可直接新建
                + "?id=81511147b98c11e9b2a700155d465b14";

        StringRequest.getAsyn(url, new StringResponseCallBack() {

            @Override
            public void onResponse(String response) {

                getTableTiltle();
                try {
                    Project project = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Project.class);

                    if (project != null) {
                        if ("是否显示项目联系记录".equals(project.getName())
                                && "否".equals(project.getValue())) {
                            tableTitles.remove(Global.CONTACT_TITLE);
                            mFragments.remove(1);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

                getTableTiltle();
            }
        });
    }

    private void getTableTiltle() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.项目详情标签页;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                projectForm = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Project.class);
                if (projectForm.size() > 0) {
                    for (int i = 0; i < projectForm.size(); i++) {
                        tableTitles.add(projectForm.get(i).getFormName());
                        mFragments.add(ProjectCommanListFragment.newInstance(projectForm.get(i)));
                    }
                }
                initData();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                initData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getIsCanNewForm();
    }

    public void getIsCanNewForm() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.验证里程碑月份是否为空 + mProject.getUuid();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.getStringValue(response, "Data");
                    if (data.equals("true")) {
                        isCanNewForm = true;
                    } else {
                        isCanNewForm = false;
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

    private void getIntentData() {
        if (getIntent().getSerializableExtra("Project") != null) {
            mProject = (Project) getIntent().getSerializableExtra("Project");
        }
    }


    private void setOnTouch() {
        headerView.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {

                if (currentItem == 0) {  //项目信息
                    ArrayList<表单字段> list = getAllFormList();
                    String result = ChClientBiz.checkNull(list);
                    if (!TextUtils.isEmpty(result)) {
                        showShortToast(result);
                    } else {
                        String idCardReg = ChClientBiz.checkCardRegEx(list);
                        if (!TextUtils.isEmpty(idCardReg)) {
                            // 先校验身份证号
                            showShortToast(idCardReg);
                        } else {
                            if (!TextUtils.isEmpty(result)) {
                                showShortToast(result);
                            } else {
                                saveCustomerForm(list);
                            }
                        }
                    }

                } else if (currentItem == 1) {   //联系记录
                    Intent intent = new Intent(ProjectInfoActivity.this, AddRecordActivity.class);
                    intent.putExtra("project_add", mProject);
                    startActivity(intent);
                } else {
                    if (isCanNewForm) {
                        if (currentItem == 2 && milestoneFlag) {
                            //里程碑
                        } else {
                            if (projectForm.size() > 0) {
                                String url1 = Global.BASE_JAVA_URL + GlobalMethord.项目判断当前是否可以新建表单 +
                                        "?dynamicTabId=" + projectForm.get(currentItem - currentItemIndex).getUuid() +
                                        "&projectId=" + mProject.getUuid();
                                StringRequest.getAsyn(url1, new StringResponseCallBack() {
                                    @Override
                                    public void onResponse(String response) {
                                        final Project project = projectForm.get(currentItem - currentItemIndex);
                                        String url = Global.BASE_JAVA_URL + GlobalMethord.项目新建表单获取参数 +
                                                "?dynamicTabId=" + project.getUuid() + "&createFrom=&hostMajorKey=" + mProject.getUuid();
                                        StringRequest.getAsyn(url, new StringResponseCallBack() {
                                            @Override
                                            public void onResponse(String response) {
                                                String data = JsonUtils.pareseData(response);
                                                try {
                                                    String str = "";
                                                    if (!TextUtils.isEmpty(data)) {
                                                        String[] split = data.split("&");
                                                        for (int i = 0; i < split.length; i++) {
                                                            String[] split1 = split[i].split("=");
                                                            if (split1.length > 1) {
                                                                str += "&" + split1[0] + "=" + URLEncoder.encode(split1[1], "UTF-8");
                                                            }
                                                        }
                                                    }
                                                    String formUrl = PRURL + project.getWorkflowTemplateId() + "&projectId=" + mProject.getUuid() + str;
                                                    newFormMenu(formUrl);
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Request request, Exception ex) {

                                            }

                                            @Override
                                            public void onResponseCodeErro(String result) {
                                                Toast.makeText(mContext, "没有权限新建表单", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure(Request request, Exception ex) {

                                    }

                                    @Override
                                    public void onResponseCodeErro(String result) {
                                        Toast.makeText(mContext, "当前项目下最多新建" + JsonUtils.pareseData(result) + "个" + projectForm.get(currentItem - currentItemIndex).getFormName(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    } else {
                        showShortToast("请先完善里程碑预计完成月份");
                    }
                }
            }

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

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = viewpager.getCurrentItem();
//                if (mProject.isCanNewSamplepaint()) {
                if (currentItem == 0) { //项目信息
                    headerView.setRightTitle("保存");
                    headerView.setRightTitleVisible(true);
                } else if (currentItem == 1) {
                    headerView.setRightTitle("新建");
                    headerView.setRightTitleVisible(true);
                } else if (currentItem == 2 && milestoneFlag) { //里程碑
                    headerView.setRightTitleVisible(false);
                } else if (tableTitles.get(currentItem).equals("设备列表")) {
                    headerView.setRightTitleVisible(false);
                }
//                    else {
//                        headerView.setRightTitle("新建");
//                    }
//                } else {
//                    headerView.setRightTitleVisible(false);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setHeaderViewRight(boolean isHidden) {
//        if (mProject.isCanNewSamplepaint()) {
        if (isHidden) {
            headerView.setRightTitle("新建");
            headerView.setRightTitleVisible(true);
        } else {
            headerView.setRightTitleVisible(false);
        }
//        } else {
//            headerView.setRightTitleVisible(false);
//        }
    }

    private ArrayList<表单字段> getAllFormList() {
        ArrayList<表单字段> list = new ArrayList<表单字段>();
        for (ChClientTabFragment fragment : projectInformationFragment.mFragments) {
            list.addAll(fragment.getFormList());
        }
        return list;
    }

    private void saveCustomerForm(ArrayList<表单字段> formList) {
        for (int i = 0; i < formList.size(); i++) {
            if (formList.get(i).Name.equals("uuid")) {
                formList.get(i).Identify = true;
            }
            if (formList.get(i).Name.equals("lastUpdateTime")) {
                formList.get(i).Value = ViewHelper.getCurrentFullTime();
            }
        }
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.保存动态字段;
        StringRequest.postAsynNoMap(url, "crm_project", formList, new StringResponseCallBack() {
            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存失败");
                Logger.d(TAG + result + "");
            }

            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                showShortToast("保存成功");
                ProjectListActivity.isResume = true;
                ClientProjectListFragment.isResume = true;
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                Logger.e(TAG + ex + "");
                showShortToast("网络不给力，请稍后再试");
            }
        });
    }

    private void initData() {
        headerView.setRightTitleVisible(mProject.isCanNewSamplepaint() && !StrUtils.pareseNull(mProject.getStatus()).equals("7"));
        mContext = ProjectInfoActivity.this;
//        bottomDialog = new DictIosPickerBottomDialog(mContext);
        indeicatior.setTabItemTitles(tableTitles);

        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
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
        viewpager.setEnabled(true);
        indeicatior.setViewPager(viewpager, 0);
    }

    private void initView() {
        headerView = findViewById(R.id.boeryun_headerview);
        viewpager = findViewById(R.id.boeryun_viewpager);
        indeicatior = findViewById(R.id.simpleindicatior);
        mFragments = new ArrayList<>();
        projectInformationFragment = new ProjectInformationFragment();
        tableTitles = new ArrayList<>();
        tableTitles.add("项目信息");
        tableTitles.add(Global.CONTACT_TITLE);

        mFragments.add(projectInformationFragment); //项目信息
        mFragments.add(new ProjectRecordFragment()); //项目信息更新
        String jurisdictionList = PreferceManager.getInsance().getValueBYkey("JurisdictionList");
        if (jurisdictionList.contains("里程碑")) {
            milestoneFlag = true;
            currentItemIndex = 3;
            tableTitles.add("里程碑");
            mFragments.add(new ProjectMilestoneFragment()); //里程碑
        } else {
            currentItemIndex = 2;
        }
        if (jurisdictionList.contains("设备列表")) {
            tableTitles.add("设备列表");
            mFragments.add(new ProjectDeviceListFragment());
            currentItemIndex += 1;
        }
    }

    private void newFormMenu(String exturaUrl) {
        Intent intent = new Intent(ProjectInfoActivity.this, FormInfoActivity.class);
        intent.putExtra("exturaUrl", exturaUrl);
        intent.putExtra("formDataId", "0");
        intent.putExtra("projectId", mProject.getUuid());
        startActivity(intent);
    }


}
