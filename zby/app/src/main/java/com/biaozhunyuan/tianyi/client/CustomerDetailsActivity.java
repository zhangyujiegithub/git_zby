package com.biaozhunyuan.tianyi.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.business.BusinessAddActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Constant;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.NotificationUtil;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.contact.ContactNewActivity;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.helper.WebviewFragment;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectAddActivity;
import com.biaozhunyuan.tianyi.task.TaskInfoActivityNew;
import com.biaozhunyuan.tianyi.task.TaskStatusEnum;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.widget.BoeryunViewpager;
import com.leon.lfilepickerlibrary.LFilePicker;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

/**
 * ????????????
 */

public class CustomerDetailsActivity extends BaseActivity {

    private BoeryunViewpager viewpager;
    private BoeryunHeaderView headerview;
    private ImageView iv_add;
    private Context mContext;
    private Dynamic dynamic;
    private Client mClient = null;
    private DictionaryHelper helper;
    public final static String EXTRA_CLIENT = "extra_client_ClientInfoAndContactActivity";
    public final static String EXTRA_CLIENT_ID = "extra_client_CustomerId";
    public final static String PRPROJECT_LISTDATA = "project_list_data";
    private SimpleIndicator indicatior;
    private TextView tv_advisor;
    private CircleImageView iv_circle;
    private TextView tv_contactName;
    private TextView tv_contactNumber;
    private TextView tv_contactLocation;
    private boolean isShowAdd = false; //???????????????????????? ??????????????????
    private boolean isResume = false;
    private String[] tableTitles = {"????????????", "??????????????????", "????????????", "????????????"};
    private HashMap<Integer, Project> titleMap = new HashMap<>();
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<Project> projectForm = new ArrayList<>();
    private boolean taskFlag = false; //???????????????????????????
    private ClientAttachFragment clientAttachFragment;
    private Project currentProject;
    private boolean isCanShowPhone = true;


    private boolean isCanNewContactRecord = false; //??????????????????????????????
    private boolean isCanNewCustomerChance = false; //????????????????????????
    private boolean isCanNewCustomerProject = false; //????????????????????????
    private String canNewPermissions = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        mContext = CustomerDetailsActivity.this;
        initView();
        extractIntent();
        setOnTouchEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume) {
            try {
                WebviewFragment fragment = (WebviewFragment) mFragmentList.get(viewpager.getCurrentItem());
                fragment.reLoad();
            } catch (Exception e) {
                e.printStackTrace();
            }
            isResume = false;
        }
    }

    /**
     * ????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * ?????????????????????????????????????????????????????????????????????????????????
     *
     * @param isShareClient
     */
    private void getProjectNewMoudle(boolean isShareClient) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.??????????????????????????? + "?id=ab77c83744464c30a32d15df68c8ef23";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    Project project = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Project.class);
                    if (project != null) {
                        if (isShareClient) {
                            if (isCanNewCustomerProject && project.getValue().equals("???????????????")) {
                                isCanNewCustomerProject = true;
                            } else {
                                isCanNewCustomerProject = false;
                            }
                        } else {
                            if (project.getValue().equals("???????????????")) {
                                isCanNewCustomerProject = true;
                            } else {
                                isCanNewCustomerProject = false;
                            }
                        }
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


    /**
     * ?????????????????????????????????
     */
    private void getCanNewPermission() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.???????????????????????? +
                "?staffId=" + Global.mUser.getUuid() + "&customerId=" + mClient.getUuid();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    canNewPermissions = JsonUtils.pareseData(response);
                    if (!TextUtils.isEmpty(canNewPermissions)) {
                        if (canNewPermissions.contains("contactRecord")) {
                            isCanNewContactRecord = true;
                            iv_add.setVisibility(View.VISIBLE);
                        }
                        if (canNewPermissions.contains("customerChance")) {
                            isCanNewCustomerChance = true;
                        }
                        if (canNewPermissions.contains("customerProject")) {
                            isCanNewCustomerProject = true;
                            getProjectNewMoudle(true);
                        }
                    }
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

    /**
     * ?????????????????????
     */
    private void getTableTitle() {
        //?????????????????????????????????????????????  ????????????????????????????????????
        if (Global.mUser.getUuid().equals(mClient.getAdvisorId())) {
            iv_add.setVisibility(View.VISIBLE);
            getProjectNewMoudle(false);
        } else {
            getCanNewPermission();
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.?????????????????????;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                projectForm = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Project.class);
                if (projectForm.size() > 0) {
                    int length = tableTitles.length;
                    for (int i = 0; i < projectForm.size(); i++) {
                        tableTitles = Arrays.copyOf(tableTitles, length += 1);
                        tableTitles[length - 1] = projectForm.get(i).getFormName();
                        titleMap.put(tableTitles.length - 1, projectForm.get(i));
//                        mFragmentList.add(ClientCommanListFragment.newInstance(mClient.getUuid(), projectForm.get(i)));
                        String url = "http://crm.tysoft.com/" + GlobalMethord.??????????????????H5 + "?customerId="
                                + mClient.getUuid() + "&dynamicTabId=" + projectForm.get(i).getUuid();
                        mFragmentList.add(WebviewFragment.getInstance(url));

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

    private void setOnTouchEvent() {

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int currentItem = viewpager.getCurrentItem();
                if (currentItem == 0) { //??????????????????
                    Intent intent = new Intent(mContext,
                            AddRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ContactNewActivity.EXTRA_CLIENT_NAME, mClient.getName());
                    bundle.putString(ContactNewActivity.EXTRA_CLIENT_ID, mClient.getUuid());
                    bundle.putString("advisorId", Global.mUser.getUuid());//mClient.getAdvisorId()
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (currentItem == 1) { //???????????????
                    Intent intent = new Intent(mContext, ClientNewContactWayActivity.class);
                    intent.putExtra("customerId", mClient.getUuid());
                    startActivity(intent);
                } else if (tableTitles[currentItem].equals("????????????")) { //????????????
                    Intent intent = new Intent(mContext, TaskInfoActivityNew.class);
                    Bundle bundle = new Bundle();
                    Task task = new Task();
                    task.setBeginTime(ViewHelper.getCurrentTime() + " 00:00:00");
                    task.setEndTime(ViewHelper.getCurrentTime() + " 23:59:59");
                    task.setExecutorIds(Global.mUser.getUuid());
                    task.setCreatorId(Global.mUser.getUuid());
                    task.setCreationTime(ViewHelper.getCurrentFullTime());
                    task.setStatus(TaskStatusEnum.?????????.getName());
                    task.setCustomerId(mClient.getUuid());
                    task.setCustomerName(mClient.getName());
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (tableTitles[currentItem].equals("??????")) { //????????????
                    Intent intent = new Intent(mContext, BusinessAddActivity.class);
                    intent.putExtra("Customer", mClient);
                    startActivity(intent);
                } else if (tableTitles[currentItem].equals("??????")) { //????????????
                    Intent intent = new Intent(mContext, ProjectAddActivity.class);
                    intent.putExtra("Customer", mClient);
                    startActivity(intent);
                } else if (tableTitles[currentItem].equals("????????????")) {
                    new LFilePicker()
                            .withActivity(CustomerDetailsActivity.this)
                            .withRequestCode(Constant.REQUESTCODE_FROM_ACTIVITY)
                            .withTitle("????????????")
                            .withIconStyle(Constant.ICON_STYLE_YELLOW)
                            .withMutilyMode(true)
                            .withMaxNum(9)
//                            .withStartPath("/storage/emulated/0/Download")//????????????????????????
                            .withNotFoundBooks("????????????????????????")
//                            .withIsGreater(false)//?????????????????? ???????????????????????????
//                            .withFileSize(500 * 1024)//?????????????????????500K
                            .withChooseMode(true)//??????????????????
                            //.withFileFilter(new String[]{"txt", "png", "docx"})
                            .start();
                }
//                else if (tableTitles[currentItem].contains("??????")){ //????????????
//                    Intent intent = new Intent(mContext,StandingBookInfoActivity.class);
//                    intent.putExtra("tableName",tableTitles[currentItem]);
//                    intent.putExtra("customerId",mClient.getUuid());
//                    startActivity(intent);
//                }
                else {
                    for (int i = 0; i < projectForm.size(); i++) {
                        if (projectForm.get(i).getFormName().equals(tableTitles[currentItem])) {
                            currentProject = projectForm.get(i);
                            break;
                        }
                    }
                    String url1 = Global.BASE_JAVA_URL + GlobalMethord.?????????????????????????????????????????? +
                            "?dynamicTabId=" + currentProject.getUuid() +
                            "&customerId=" + mClient.getUuid();
                    StringRequest.getAsyn(url1, new StringResponseCallBack() {
                        @Override
                        public void onResponse(String response) {
                            String url = Global.BASE_JAVA_URL + GlobalMethord.?????????????????????????????? +
                                    "?dynamicTabId=" + currentProject.getUuid() + "&createFrom=&hostMajorKey=" + mClient.getUuid();
                            StringRequest.getAsyn(url, new StringResponseCallBack() {
                                @Override
                                public void onResponse(String response) {
                                    String data = JsonUtils.pareseData(response);
                                    newFormMenu(data, currentItem);
                                }

                                @Override
                                public void onFailure(Request request, Exception ex) {

                                }

                                @Override
                                public void onResponseCodeErro(String result) {
                                    showShortToast(JsonUtils.pareseData(result));
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, Exception ex) {

                        }

                        @Override
                        public void onResponseCodeErro(String result) {
                            Toast.makeText(mContext, "???????????????????????????" + JsonUtils.pareseData(result) + "???" + currentProject.getFormName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                Intent intent = new Intent(mContext, ChClientInfoActivity.class);
                intent.putExtra("isReadOnly", isShowAdd);
                intent.putExtra(ChClientInfoActivity.EXTRA_CLIENT_ID, mClient.getUuid());
                intent.putExtra("isShowContactAndPhone", isCanShowPhone);
                startActivity(intent);
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
                //?????????????????????????????????????????????
                if (Global.mUser.getUuid().equals(mClient.getAdvisorId())) {
                    if (tableTitles[position].equals("????????????")
                            || tableTitles[position].equals("????????????")
                            || tableTitles[position].equals("??????")
                            || tableTitles[position].equals("??????????????????")
                            || tableTitles[position].equals("????????????")
                            || (tableTitles[position].equals("??????") && !isCanNewCustomerProject)) {
                        iv_add.setVisibility(View.GONE);
                    } else {
                        iv_add.setVisibility(View.VISIBLE);
                    }
                    //?????????????????????,?????????????????????????????????????????????
                } else {
                    //??????????????????????????????
                    if ((titleMap.get(position) != null
                            && !TextUtils.isEmpty(titleMap.get(position).getWorkflowTemplateId())
                            && canNewPermissions.contains(titleMap.get(position).getWorkflowTemplateId())
                            && titleMap.get(position).getCreateFrom().contains("host"))
                            || (tableTitles[position].equals("????????????") && isCanNewContactRecord)
                            || (tableTitles[position].equals("??????") && isCanNewCustomerChance)
                            || (tableTitles[position].equals("??????") && isCanNewCustomerProject)) {
                        iv_add.setVisibility(View.VISIBLE);
                    } else {
                        iv_add.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setHeaderViewRight(boolean isHidden) {
        if (isShowAdd) {
            if (isHidden) {
                iv_add.setVisibility(View.VISIBLE);
            } else {
                iv_add.setVisibility(View.GONE);
            }
        }
    }

    private void newFormMenu(String data, int currentItem) {
        Intent intent = new Intent(mContext, FormInfoActivity.class);
        String workflowTemplateId = "";
        for (int i = 0; i < projectForm.size(); i++) {
            if (projectForm.get(i).getFormName().equals(tableTitles[currentItem])) {
                workflowTemplateId = projectForm.get(i).getWorkflowTemplateId();
            }
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.???????????? +
                "?id=0&workflowTemplateId=" + workflowTemplateId +
                "&customerId=" + mClient.getUuid() + data;
        if ("?????????".equals(tableTitles[currentItem])) {
            isResume = true;
        }
        if (tableTitles[currentItem].contains("??????")
                || tableTitles[currentItem].contains("??????????????????")
                || tableTitles[currentItem].contains("?????????")) {
            intent.putExtra("customerId", mClient.getUuid());
        }
        intent.putExtra("advisorId", mClient.getAdvisorId());
        intent.putExtra("exturaUrl", url);
        startActivity(intent);
    }

    private void initView() {
        indicatior = findViewById(R.id.simpleindicatior);
        viewpager = findViewById(R.id.vp_client_inf_and_contact);
        headerview = findViewById(R.id.boeryun_headerview);
        iv_add = findViewById(R.id.iv_add_relate);
        tv_advisor = findViewById(R.id.advisor_name); //???????????????
        iv_circle = findViewById(R.id.circleImageView); // ???????????????
        tv_contactName = findViewById(R.id.contact_name);//???????????????
        tv_contactNumber = findViewById(R.id.contact_number);//???????????????
        tv_contactLocation = findViewById(R.id.contact_location); //??????
        helper = new DictionaryHelper(this);
    }

    private void extractIntent() {
        Bundle bundle = getIntent().getExtras();
        dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
        if (dynamic != null) {
            getClientInfo();
        }
        if (bundle == null) {
            return;
        }

        Client client = (Client) bundle.getSerializable(EXTRA_CLIENT);
        isCanShowPhone = bundle.getBoolean("isCanShowPhone", true);
        if (client != null) {
            initClinetViews(client);
            loadRelatedInfos(client.getUuid());
            mClient = client;
            if (StrUtils.pareseNull(mClient.getAdvisor()).equals(Global.mUser.getUuid())) {
                isShowAdd = true;
            }
            mClient.setMineCustomer(isShowAdd);
            getTableTitle();
        }
        String customerId = bundle.getString(EXTRA_CLIENT_ID);
        if (!TextUtils.isEmpty(customerId)) {
            getCustomerInfo(customerId);
        }
    }

    /**
     * ????????????????????????
     *
     * @param uuid ??????uuid
     */
    private void getCustomerInfo(String uuid) {
        StringRequest.getAsyn(Global.BASE_JAVA_URL + GlobalMethord.?????????????????? + uuid,
                new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Client client = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Client.class);
                            initClinetViews(client);
                            loadRelatedInfos(client.getUuid());
                            mClient = client;
                            if (StrUtils.pareseNull(mClient.getAdvisor()).equals(Global.mUser.getUuid())) {
                                isShowAdd = true;
                            }
                            mClient.setMineCustomer(isShowAdd);
                            if (mClient.isMineCustomer()) {
                                iv_add.setVisibility(View.VISIBLE);
                            } else {
                                iv_add.setVisibility(View.GONE);
                            }
                            getTableTitle();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            showShortToast("????????????");
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

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */
    public boolean isMineCustomer() {
        return mClient.isMineCustomer();
    }

    private void getClientInfo() {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?dataId=" + dynamic.getDataId() + "&dataType=" + dynamic.getDataType();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    List<Client> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), Client.class);
                    if (list != null && list.size() > 0) {
                        mClient = list.get(0);
                    }
                    if (mClient != null) {
                        initClinetViews(mClient);
                        loadRelatedInfos(mClient.getUuid());
                        if (mClient.getAdvisor().equals(Global.mUser.getUuid())) {
                            isShowAdd = true;
                        }
                        mClient.setMineCustomer(isShowAdd);
                    }
                } catch (Exception e) {
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
     * ?????????????????????????????????
     *
     * @param client ????????????
     */
    private void initClinetViews(Client client) {
        headerview.setTitle(StrUtils.pareseNull(client.getName()));
        ImageUtils.displyUserPhotoById(mContext, client.getAdvisor(), iv_circle);
        tv_advisor.setText(helper.getUserNameById(client.getAdvisor()));//?????????
        if (isCanShowPhone) {
            tv_contactName.setText(TextUtils.isEmpty(client.getContact()) ? "???" : client.getContact());//???????????????
            tv_contactNumber.setText(TextUtils.isEmpty(client.getMobile()) ? "???" : client.getMobile());//???????????????
        } else {
            tv_contactName.setText("***");//???????????????
            tv_contactNumber.setText("******");//???????????????
        }
        tv_contactLocation.setText(TextUtils.isEmpty(client.getAddress()) ? "???" : client.getAddress()); //????????????
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param clientId
     */
    private void loadRelatedInfos(String clientId) {
        mFragmentList.add(ClientContactListFragment.newInstance(clientId)); //????????????
        mFragmentList.add(ClientContactWayListFragment.newInstance(clientId)); //??????????????????
        mFragmentList.add(ClinetAmendantListFragment.newInstance(clientId)); //????????????
        String url = "http://crm.tysoft.com/" + GlobalMethord.????????????????????????H5 + "?customerId=" + clientId;
        WebviewFragment instance = WebviewFragment.getInstance(url);
        instance.setCustomerId(clientId);
        mFragmentList.add(instance); //????????????

        int length = tableTitles.length;
        String jurisdictionList = PreferceManager.getInsance().getValueBYkey("JurisdictionList");
        if (jurisdictionList.contains("??????????????????")) {
            taskFlag = true;
            tableTitles = Arrays.copyOf(tableTitles, length += 1);
            tableTitles[length - 1] = "????????????";
            mFragmentList.add(ClientTaskListFragment.newInstance(clientId)); //????????????
        }
        if (jurisdictionList.contains("??????????????????")) {
            tableTitles = Arrays.copyOf(tableTitles, length += 1);
            tableTitles[length - 1] = "??????";
            mFragmentList.add(ClientProjectListFragment.newInstance(clientId)); //????????????
        }
        if (jurisdictionList.contains("????????????")) {
            tableTitles = Arrays.copyOf(tableTitles, length += 1);
            tableTitles[length - 1] = "??????";
            mFragmentList.add(ClientBusinessListFragment.newInstance(clientId)); //??????
        }
        if (jurisdictionList.contains("??????????????????")) {
            tableTitles = Arrays.copyOf(tableTitles, length += 1);
            tableTitles[length - 1] = "????????????";
            mFragmentList.add(ClientAllotListFragment.newInstance(clientId)); //????????????
        }
        if (jurisdictionList.contains("??????????????????")) {
            tableTitles = Arrays.copyOf(tableTitles, length += 1);
            tableTitles[length - 1] = "????????????";
            clientAttachFragment = ClientAttachFragment.newInstance(clientId);
            mFragmentList.add(clientAttachFragment);  //????????????
        }
        if (jurisdictionList.contains("????????????")) {
//            isCanNewWorkOrder = true;
        }
    }

    private void initData() {
//        mFragmentList.add(ClientStandingBookListFragment.newInstance(mClient.getUuid())); //??????
//        tableTitles = Arrays.copyOf(tableTitles, tableTitles.length + 1);
//        tableTitles[tableTitles.length - 1] = "??????";
        indicatior.setTabItemTitles(tableTitles);
        viewpager.setEnabled(true);
        viewpager.setAdapter(new ClientDetailFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        indicatior.setViewPager(viewpager, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);  //???????????????????????????????????????
                if (list.size() > 0) {
                    List<String> fileNames = new ArrayList<>(); //??????????????????????????????
                    for (int i = 0; i < list.size(); i++) {
                        fileNames.add(list.get(i).substring(list.get(i).lastIndexOf("/"), list.get(i).length()));
                    }
                    AlertDialog alertDialog = new AlertDialog(mContext).builder();
                    alertDialog.setListMsg(fileNames).setPositiveButton("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NotificationUtil mNotificationUtil = new NotificationUtil(mContext, list.size()); //??????????????????????????????????????????
                            UploadHelper uploadHelper = UploadHelper.getInstance();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (list.size() > 1) {
                                        uploadHelper.uploadMultipleFiles("file", list, false, new IOnUploadMultipleFileListener() {
                                            @Override
                                            public void onStartUpload(int sum) {
                                                mNotificationUtil.showNotification(Constant.NOTIFICATION_TAG);
                                                showShortToast("??????????????????");
                                            }

                                            @Override
                                            public void onProgressUpdate(int completeCount) {
                                                mNotificationUtil.updateProgress(Constant.NOTIFICATION_TAG, completeCount, list.size());//????????????
                                            }

                                            @Override
                                            public void onComplete(String attachIds) {
                                                if (!TextUtils.isEmpty(attachIds)) {
                                                    mNotificationUtil.cancel(Constant.NOTIFICATION_TAG);
                                                    String toast = "????????????";
                                                    if (attachIds.contains("null")) {
                                                        int aNull = StrUtils.getStrCountInString(attachIds, "null");
                                                        toast = "????????????" + (list.size() - aNull) + "???,??????" + aNull + "???";
                                                    }
                                                    saveClientAttach(attachIds, toast);
                                                } else {
                                                    showShortToast("????????????");
                                                }
                                            }
                                        });
                                    } else {
                                        File file = new File(list.get(0));
                                        String attachIds = uploadHelper.uploadFileGetAttachId("file", file);
                                        if (!TextUtils.isEmpty(attachIds)) {
                                            saveClientAttach(attachIds, "");
                                        } else {
                                            showShortToast("????????????");
                                        }
                                    }
                                }
                            }).start();
                            alertDialog.dissMiss();
                        }
                    }).setNegativeButton("??????", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dissMiss();
                        }
                    }).setTitle("?????????????").show();
                } else {
                    showShortToast("????????????????????????");
                }
            }
        }
    }

    private void saveClientAttach(String attachments, String toast) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????????????????;
        JSONObject js = new JSONObject();
        try {
            js.put("attachments", attachments);
            js.put("customerId", mClient.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, js, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast(toast);
                if (clientAttachFragment != null) {
                    clientAttachFragment.getAttachList();
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
}
