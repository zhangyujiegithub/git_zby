package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.Audite;
import com.biaozhunyuan.tianyi.apply.model.AuditeInfo;
import com.biaozhunyuan.tianyi.apply.model.CellInfo;
import com.biaozhunyuan.tianyi.common.model.form.FormData;
import com.biaozhunyuan.tianyi.common.model.form.FormDetails;
import com.biaozhunyuan.tianyi.apply.model.LoadRelatedData;
import com.biaozhunyuan.tianyi.apply.model.RelatedData;
import com.biaozhunyuan.tianyi.common.model.form.TabCell;
import com.biaozhunyuan.tianyi.apply.model.WorkflowNodeVersion;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.attach.BoeryunDownloadManager;
import com.biaozhunyuan.tianyi.common.attach.DownloadFile;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.helper.DictionaryQueryDialog;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.product.ProductListActivity;
import com.biaozhunyuan.tianyi.wps.WPSBroadCastReciver;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.ExpandAnimation;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.MoneyUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.view.BoeryunProgressBar;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.view.DictIosMultiPicker;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import parsii.eval.Expression;
import parsii.eval.Parser;


/**
 * Created by 王安民 on 2017/9/8.
 * 表单详情页面
 */

public class ShouWenInfoActivity extends BaseActivity {


    /**
     * 数据定义
     */
    private String TAG = "FormInfoActivity";
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DownloadFile downloadFile = (DownloadFile) msg.obj;
            if (BoeryunDownloadManager.DOWNLOAD_STATE_FINISH == downloadFile.downloadState) {
                progressBar.dissMiss();
                downloadHelper.open(context, downloadFile.attachName);
            }
        }
    };
    private String createrId;//申请人id
    private String formName = "";//表单名称
    private String formDataId = "0";//表单数据编号,默认新建表单，编号为0
    private String workflowTemplateId = "";//流程分类数据编号
    private String workflowTemplateVersion = "";//流程版本
    private String workFlowId;//流程分类编号
    private boolean auditable = false; //是否可审核
    private boolean editable = false; //单元格是否可编辑(是否可添加明细)
    private boolean attredit = false; //是否可删除附件
    private String isShowForwardRecallFormBtn = "false"; //是否显示子流程撤回按钮
    private String formType = ""; //表单类型，"type": "vsheet_leave_application"
    private String detailType = ""; //表单详细类型，"detailType": "vsheet_out_application_detail"
    private String intentUrl = ""; //传进来的url，如果不为空。查看表单详情用这个url
    private List<WorkflowNodeVersion> nodeVersions; //节点的集合
    private List<String> nodeNames = new ArrayList<String>(); //节点名称的集合
    private boolean FLAG;
    private String shenpiUrl;
    private String errorMessage = ""; //错误信息，保存的时候判断是否为空
    private List<EditText> mEditList = new ArrayList<EditText>();
    private List<EditText> mDetailsEdits = new ArrayList<EditText>();
    private HashMap<Integer, List<EditText>> detailMap = new HashMap<Integer, List<EditText>>();
    private List<TextView> mDetailsTitles = new ArrayList<TextView>(); //申请明细头的集合
    public HashMap<String, Map<String, String>> mDictionaries;
    private boolean isAttachView = false; //是否是底部上传图片控件,默认为false
    /***
     * 多图片上传控件 集合
     */
    private List<MultipleAttachView> mAttachViews = new ArrayList<MultipleAttachView>();
    /***
     * 记录选择附件的字段名称
     */
    public static String mMultipleAttachFieldName = "";
    private int formDetailsCount = 0; //明细表个数
    private String startFieldValue = "";//请假开始日期单元格的Value值
    private String endFieldValue = "";//请假结束日期单元格的Value值
    private CellInfo startField = null; //开始时间单元格
    private CellInfo endField = null;//结束时间单元格
    private String startOutFieldValue = "";//出差开始日期单元格的Value值
    private String endOutFieldValue = "";//出差结束日期单元格的Value值
    private CellInfo startOutField = null; //出差开始时间单元格
    private CellInfo endOutField = null;//出差结束时间单元格
    private EditText etTotalDays; //显示请假总天数的输入框
    /**
     * 是否选择
     */
    private final String[] checkStrs = {"否", "是"};

    /**
     * 初始化数据
     */
    private DictionaryHelper dictionaryHelper;
    private TimePickerView pickerView;
    private DictIosPickerBottomDialog mDictIosPicker;
    private DictIosPickerBottomDialog mNodeIosPicker; //节点底部弹出框
    private DictIosMultiPicker dictIosMultiPicker;
    private DictionaryQueryDialog dictionaryQueryDialog;
    private BoeryunProgressBar progressBar;

    /**
     * view 控件
     */
//    private BoeryunHeaderView headerView;
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_save;
    private TextView tv_submit;
    private CircleImageView iv_head; //头像
    private TextView tv_name;//申请人名称
    private TextView tv_dept;//申请人部门名称
    private TextView tv_add_details;//添加明细按钮
    private ScrollView scrollView;
    private LinearLayout root_attach;//上传附件根布局
    private MultipleAttachView attachView;//上传附件控件
    private LinearLayout root_audite;//审批人根布局
    private NoScrollListView lv_audite;//审批人列表
    private LinearLayout ll_audite; //审批意见根布局
    private TextView tv_refuse; //审批拒绝
    private TextView tv_copy; //抄送
    private TextView tv_agree; //同意
    private TextView tv_back; //退回
    private TextView tv_recall; //撤回

    private LinearLayout ll_show_form; //展示表单详情
    private LinearLayout ll_show_audite; //展示表单审批意见
    private LinearLayout ll_zhengwen; //正文
    private BoeryunDownloadManager downloadHelper;
    public static final int REQUEST_SELECT_PARTICIPANT = 101;
    public static final int REQUEST_SELECT_AUDITOR = 102;//转下一位审核人


    private LinearLayout ll_root;//线性布局根布局
    private TextView tv_turn;//转下一步审核人
    private String auditeMessage; //审批意见
    private String auiteMessage;
    private AlertDialog dialog;
    private String isShowForwardTurn; //是否可转下一步审核人

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouwen_info);
        FLAG = false;
        initData();
        initDialog();
        initViews();
        initIntentData();
        getFormInfo();
        setOnEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PARTICIPANT:
//                    Bundle bundle1 = data.getExtras();
//                    UserList userList = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
//                    if (userList != null) {
//                        List<User> users = userList.getUsers();
//                        String copyIds = "";
//                        for (User user : users) {
//                            copyIds += user.getUuid() + ",";
//                        }
//                        if (copyIds.length() > 0) {
//                            copyIds = copyIds.substring(0, copyIds.length() - 1);
//                        }
//                        copyTo(copyIds);
//                    }
                    Bundle bundle1 = data.getExtras();
                    UserList select_auditor1 = (UserList) bundle1.getSerializable("RESULT_SELECT_USER");
                    if (select_auditor1 != null) {
                        List<User> users_auditor = select_auditor1.getUsers();
                        String list_ids = "";
                        for (User user : users_auditor) {
                            list_ids += user.getUuid() + ",";
                        }
                        if (list_ids.length() > 0) {
                            list_ids = list_ids.substring(0, list_ids.length() - 1);
                        }
                        copyTo(list_ids);
                    }
                    break;
                case REQUEST_SELECT_AUDITOR://转下一步审核人
                    Bundle bundle = data.getExtras();
                    UserList select_auditor = (UserList) bundle.getSerializable("RESULT_SELECT_USER");
                    if (select_auditor != null) {
                        List<User> users_auditor = select_auditor.getUsers();
                        String list_ids = "";
                        for (User user : users_auditor) {
                            list_ids += user.getUuid() + ",";
                        }
                        if (list_ids.length() > 0) {
                            list_ids = list_ids.substring(0, list_ids.length() - 1);
                        }
                        if (FLAG) {
                            shenpiUrl = Global.BASE_JAVA_URL + GlobalMethord.审批申请 + "?auditorIds=" + list_ids;
                            audite(shenpiUrl, 1, auditeMessage);
                        } else {
                            showAuditorDialog(list_ids);
//                            forward(workFlowId, list_ids, auiteMessage);
                        }
                    }

                    break;

                default:
                    if (isAttachView) {
                        attachView.onActivityiForResultImage(requestCode,
                                resultCode, data);
                        isAttachView = false;
                    } else {
                        updateMultipeAttachViewOnActivityForResult(requestCode,
                                resultCode, data);
                    }
                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化view
     */
    private void initViews() {
        tv_turn = findViewById(R.id.tv_turn_form_info);
//        headerView = (BoeryunHeaderView) findViewById(R.id.header_form_info);
        iv_back = (ImageView) findViewById(R.id.iv_back_form_info);
        tv_title = (TextView) findViewById(R.id.tv_title_form_info);
        tv_save = (TextView) findViewById(R.id.iv_save_form_info);
        tv_submit = (TextView) findViewById(R.id.iv_submit_form_info);
        iv_head = (CircleImageView) findViewById(R.id.iv_head_item_apply_info);
        tv_name = (TextView) findViewById(R.id.tv_creater_apply_info);
        tv_dept = (TextView) findViewById(R.id.tv_dept_apply_info);
        ll_root = (LinearLayout) findViewById(R.id.ll_form_info_root);
        tv_add_details = (TextView) findViewById(R.id.tv_add_details_apply_info);
        scrollView = (ScrollView) findViewById(R.id.scrollview_form_info);
        attachView = (MultipleAttachView) findViewById(R.id.attach_form_info);
        root_audite = (LinearLayout) findViewById(R.id.ll_root_audite_form_info);
        lv_audite = (NoScrollListView) findViewById(R.id.lv_audite_list_form_info);
        root_attach = (LinearLayout) findViewById(R.id.root_attach_form_info);
        tv_refuse = (TextView) findViewById(R.id.tv_refuse_form_info);
        tv_copy = (TextView) findViewById(R.id.tv_copy_form_info);
        tv_agree = (TextView) findViewById(R.id.tv_agree_form_info);
        tv_back = (TextView) findViewById(R.id.tv_back_form_info);
        tv_recall = (TextView) findViewById(R.id.tv_recall_form_info);
        ll_audite = (LinearLayout) findViewById(R.id.ll_audite_form_info);
        ll_show_audite = findViewById(R.id.ll_form_show_audite);
        ll_zhengwen = findViewById(R.id.ll_form_zhengwen);
        ll_show_form = findViewById(R.id.ll_form_show_info);

        ll_show_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandAnimation animation = new ExpandAnimation(ll_root, 300);
                ll_root.startAnimation(animation);
            }
        });

        ll_show_audite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandAnimation animation = new ExpandAnimation(root_audite, 300);
                root_audite.startAnimation(animation);
            }
        });


        //查看正文
        ll_zhengwen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText et : mEditList) {
                    CellInfo info = (CellInfo) et.getTag();
                    if ("正文".equals(info.getLabelText())) {
                        if (!TextUtils.isEmpty(info.getValue())) {
//                            showShortToast(info.getValue());
                            getAttact(info.getValue());
                        } else {
                            showShortToast("没有正文");
                        }
                        break;
                    }

                }
            }
        });

    }


    /**
     * 根据附件id获取附件信息
     *
     * @param attachIds
     */
    private void getAttact(final String attachIds) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("attachIds", attachIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.附件列表;
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Logger.i(response);
                progressBar.show();
                final List<Attach> attaches = JsonUtils.ConvertJsonToList(response, Attach.class);
                if (attaches.size() > 0) {
                    Attach attach = attaches.get(0);
                    String fileName = FilePathConfig.getCacheDirPath() + File.separator
                            + attach.getUuid() + "_" + attach.filename;
                    WPSBroadCastReciver.fieldName = fileName;
                    File currentPath = new File(fileName);
                    if (currentPath != null && currentPath.exists()) {
                        progressBar.dissMiss();
                        downloadHelper.open(context, attach.getUuid() + "_" + attach.filename);
                    } else {
                        DownloadFile downloadFile = new DownloadFile();
                        downloadFile.atttachId = attach.getUuid();
                        downloadFile.attachName = attach.getUuid() + "_" + attach.filename;
                        downloadFile.totalSize = attach.totalSize;
                        downloadFile.downloadSize = attach.downloadSize;
                        downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.显示附件 + attach.getUuid();
                        downloadHelper.download(downloadFile);
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

    private void initData() {
        context = ShouWenInfoActivity.this;
        downloadHelper = BoeryunDownloadManager.getInstance();
        progressBar = new BoeryunProgressBar(context);
        downloadHelper.setHandler(handler);
        dictionaryHelper = new DictionaryHelper(context);
        mDictIosPicker = new DictIosPickerBottomDialog(context);
        mNodeIosPicker = new DictIosPickerBottomDialog(context);
        mNodeIosPicker.setTitle("选择退回节点");
        dictIosMultiPicker = new DictIosMultiPicker(context);
        mDictionaries = new HashMap<String, Map<String, String>>();
        dictionaryQueryDialog = new DictionaryQueryDialog(context);
    }


    /**
     * 获取formid和名称
     */
    private void initIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            workFlowId = getIntent().getExtras().getString("workflowId");
            intentUrl = getIntent().getExtras().getString("exturaUrl");
            formName = bundle.getString("formName");
            formDataId = bundle.getString("formDataId");
            workflowTemplateId = bundle.getString("workflowTemplateId");
//            workFlowId = bundle.getString("workflowId");
            createrId = bundle.getString("createrId");

//            headerView.setTitle(StrUtils.pareseNull(formName));
            tv_title.setText(StrUtils.pareseNull(formName));

            if (!formDataId.equals("0")) { //如果不是新建申请，获取审批意见列表

            } else { //新建申请审批列表隐藏
                root_audite.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(createrId)) {
                createrId = Global.mUser.getUuid();
            }

            WPSBroadCastReciver.formID = formDataId;

//            ImageUtils.displyImageById(dictionaryHelper.getUserPhoto(createrId), iv_head);
            tv_name.setText(dictionaryHelper.getUserNameById(createrId));
            tv_dept.setText(dictionaryHelper.getDepartNameById(dictionaryHelper.getUser(createrId).getDepartmentId()));
        }

    }

// + "&workflowId=" + workFlowId

    /**
     * 获取表单数据
     */
    private void getFormInfo() {
        ProgressDialogHelper.show(context);
        String url = "";
        if (!TextUtils.isEmpty(intentUrl)) {
            url = intentUrl;
        } else {
            url = Global.BASE_JAVA_URL + GlobalMethord.表单详情 + "?id=" + formDataId + "&workflowTemplateId=" + workflowTemplateId;
        }

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                if (!TextUtils.isEmpty(JsonUtils.pareseMessage(result))) {
                    Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
                }
                try {
                    final List<CellInfo> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "masterAreas"), "数据单元格列表"), CellInfo.class);
                    Map<String, LinkedTreeMap<String, String>> dictMap = null;
                    dictMap = GsonTool.jsonToHas(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "dictionary"));
                    String detailInfo = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "detailArea"));
                    try {
                        workFlowId = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "工作流"));
                        getAuditeList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        isShowForwardRecallFormBtn = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "isShowForwardRecallFormBtn"));
                        isShowForwardTurn = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "nextStepAuditType"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        List<WorkflowNodeVersion> versions = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "nodes"), WorkflowNodeVersion.class);
                        for (int i = 0; i < versions.size(); i++) {
                            if (!"提交".equals(nodeVersions.get(i).getTitle()) ||
                                    !"完成".equals(nodeVersions.get(i).getTitle()) || nodeVersions.get(i).getStatus() == 1) {
                                nodeVersions.add(versions.get(i));
                            }
                        }
                        for (int i = 0; i < nodeVersions.size(); i++) {
                            nodeNames.add(nodeVersions.get(i).getTitle());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        createrId = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "creatorId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        workflowTemplateId = JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "setting"), "uuid");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        formName = JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "setting"), "formName");
                        tv_title.setText(formName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        workflowTemplateVersion = JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "setting"), "workflowVersion");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String auditable1 = JsonUtils.getStringValue(result, "auditable");
                    String editable1 = JsonUtils.getStringValue(result, "editable");
                    String attredit1 = JsonUtils.getStringValue(result, "attredit");

                    if ("true".equals(auditable1)) {
                        auditable = true;
                    } else {
                        auditable = false;
                    }

                    if ("true".equals(editable1)) {
                        editable = true;
                    } else {
                        editable = false;
                    }

                    if (editable) {
                        tv_save.setVisibility(View.VISIBLE);
                        tv_submit.setVisibility(View.VISIBLE);
                    }

                    if (auditable) { //显示审批按钮
                        ll_audite.setVisibility(View.VISIBLE);
                    }

                    if (editable && auditable) { //如果审批过程中，表单可编辑，只显示保存按钮，隐藏提交按钮。z
                        tv_submit.setVisibility(View.INVISIBLE);
                    }

                    if ("false".equalsIgnoreCase(isShowForwardRecallFormBtn)) { //撤回子流程按钮
                        tv_recall.setVisibility(View.GONE);
                    } else {
                        tv_recall.setVisibility(View.VISIBLE);
                    }
                    int isShowTurn = Integer.parseInt(isShowForwardTurn);
                    if (isShowTurn == 35) {
                        tv_turn.setVisibility(View.VISIBLE);
                    } else {
                        tv_turn.setVisibility(View.GONE);
                    }

                    /**
                     *获取表单类型
                     */
                    formType = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "type");

                    try {
                        detailType = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "detailType");
                    } catch (JSONException e) {
                        detailType = "";
                        e.printStackTrace();
                    }

                    try {
                        String attach = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "attachmentIds");
                        if (!TextUtils.isEmpty(attach)) {//显示附件
                            attachView.loadImageByAttachIds(attach);
                            root_attach.setVisibility(View.VISIBLE);
                        } else {
                            attachView.loadImageByAttachIds("");
                            if (!editable) { //如果没有附件，并且不可编辑。不显示附件布局
                                root_attach.setVisibility(View.GONE);
                            } else {
                                root_attach.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        attachView.loadImageByAttachIds("");
                        if (!editable) { //如果没有附件，并且不可编辑。不显示附件布局
                            root_attach.setVisibility(View.GONE);
                        } else {
                            root_attach.setVisibility(View.VISIBLE);
                        }
                    }

                    initSettings(); //初始化配置

                    if ("true".equals(attredit1)) {
                        attredit = true;
                    } else {
                        attredit = false;
                    }

                    if (dictMap != null) { //拿到返回的字典
                        for (Map.Entry<String, LinkedTreeMap<String, String>> entry : dictMap.entrySet()) {
                            LinkedTreeMap<String, String> map = entry.getValue();
                            if (map != null) {
                                for (Map.Entry<String, String> m : map.entrySet()) {
                                    map.put(m.getKey(), m.getValue());
                                }
                            }
                            mDictionaries.put(entry.getKey(), map);
                        }
                    }

                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) { //去掉binding为空的单元格
                            if (TextUtils.isEmpty(list.get(i).getBinding())) {
                                list.remove(list.get(i));
                            }
                        }
                        createUI(list, ll_root);

                        setExpression(); //为单元格绑定公式
                    }

                    if (!TextUtils.isEmpty(detailInfo)) {//得到表单明细数据
                        final List<List<CellInfo>> list1 = JsonUtils.ConvertJsonToListModle(JsonUtils.getStringValue(detailInfo, "content"), CellInfo.class);
                        final List<List<CellInfo>> list2 = new ArrayList<List<CellInfo>>();
                        for (int i = 0; i < list1.size(); i++) {
                            for (int j = 0; j < list1.get(i).size(); j++) {
                                if (!TextUtils.isEmpty(list1.get(i).get(j).getValue())) {
                                    list2.add(list1.get(i));
                                    break;
                                }
                            }
                        }


                        if (list2.size() == 0) {//如果明细表没有值，默认展示一条明细
                            list2.add(list1.get(2));
                        }

                        if (editable) {
                            tv_add_details.setVisibility(View.VISIBLE);
                        } else {
                            tv_add_details.setVisibility(View.GONE);
                        }

                        for (int i = 0; i < list2.size(); i++) {
                            CreateDetailLayout(list2, i);
                        }

                        tv_add_details.setOnClickListener(new View.OnClickListener() { //添加明细
                            @Override
                            public void onClick(View v) {
                                List<List<CellInfo>> list3 = list1;

                                for (List<CellInfo> list4 : list3) {
                                    for (CellInfo cellInfo : list4) {
                                        cellInfo.setText("");
                                        cellInfo.setValue("");
                                    }
                                }
                                CreateDetailLayout(list3, 2);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                                    }
                                });

                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 抄送给别人
     *
     * @param ids 要抄送人的id
     */
    private void copyTo(String ids) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.抄送 + "?lid=" + formDataId + "&cropIds=" + ids;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "抄送成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /***
     * 绑定公式计算
     */
    public void setExpression() {
        if (mEditList != null && mEditList.size() > 0) {
            for (int i = 0; i < mEditList.size(); i++) {
                // 遍历带有公式的EditText
                final EditText etExpression = mEditList.get(i);
                CellInfo fieldInfoExpression = (CellInfo) etExpression
                        .getTag();
                String expression = fieldInfoExpression.getExpression();
                if (!TextUtils.isEmpty(expression)) {
//                    expression = expression.toLowerCase();
                    expression = expression.toLowerCase();
                    if (expression.contains("rmb(")) {
                        // 计算人民币大小写转换
                        setMoneyConvert(etExpression, expression);
                    } else if (expression.contains("*")
                            || expression.contains("-")
                            || expression.contains("+")
                            || expression.contains("/")) {
                        setOperatorConvert(etExpression, expression);
                    } else if (expression.contains("thousand(sum(")) { //绑定两个单元格，一个单元格数据变化，另外一个单元格设置为同样的数据。
                        setSameDataOperator(etExpression, expression);
                    }
                }
            }
        }
    }


    /**
     * 获取审批意见列表
     */

    private void getAuditeList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.审批意见 + "?id=" + formDataId + "&workflowTemplateId=" + workflowTemplateId + "&workflowId=" + workFlowId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                List<AuditeInfo> auditeInfos = JsonUtils.jsonToArrayEntity(result, AuditeInfo.class);

                if (auditeInfos != null && auditeInfos.size() > 0) {
                    root_audite.setVisibility(View.VISIBLE);
                    lv_audite.setAdapter(getAuditeAdapter(auditeInfos));
                } else {
                    root_audite.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 初始化配置
     */
    private void initSettings() {
        attachView.setIsAdd(editable);
    }


    /**
     * 点击事件
     */
    private void setOnEvent() {


        //选择下一步审核人
        tv_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "转下一步审核人");
                startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 审批通过
         */
        tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editable) {
//                    if (isCanSave()) {
//                        saveForm(false, 1);
                    uploadMulipleFile(false, 1);
//                    }
                } else {
                    showAuditeDialog(1);
                }
            }
        });


        /**
         * 审批拒绝
         */
        tv_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editable) {  //如果表单可编辑，先保存再审批
//                    saveForm(false, 2);
                    uploadMulipleFile(false, 2);
                } else {
                    showAuditeDialog(2);
                }
            }
        });


        /**
         *  退回表单
         */
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNodeIosPicker.show(nodeNames);
            }
        });


        /**
         *  撤回表单
         */
        tv_recall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recallForm();
            }
        });


        mNodeIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                if (index > 0) {
                    WorkflowNodeVersion version = nodeVersions.get(index - 1);
                    showBackDialog(version.getUuid());
                }
            }
        });


        /**
         * 保存表单
         */
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isCanSave()) {
//                    saveForm(false, 0);
                ProgressDialogHelper.show(context, "保存中...");
                uploadMulipleFile(false, 0);
//                }
            }
        });


        /**
         * 提交表单
         */
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isCanSave()) {
//                    saveForm(true, 0);
                ProgressDialogHelper.show(context, "提交中...");
                uploadMulipleFile(true, 0);
//                }
            }
        });


        //抄送
        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "选择抄送人");
                startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
            }
        });

        attachView.setOnAddImageListener(new MultipleAttachView.OnAddImageListener() {
            @Override
            public void onAddImageListener() {
                isAttachView = true;
            }
        });
    }

    /**
     * 自由选择节点-转发
     *
     * @param workflowId   流程id
     * @param staffIds     员工ids
     * @param auditOpinion 审核意见
     * @return
     */
    public void forward(String workflowId, String staffIds, String auditOpinion) {
        ProgressDialogHelper.show(context, "审批中...");
        String url = Global.BASE_JAVA_URL + GlobalMethord.转下一步审核人 + "?workflowId=" + workflowId + "&staffIds=" + staffIds + "&auditOpinion=" + auditOpinion;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String s = JsonUtils.pareseMessage(response);
                Toast.makeText(ShouWenInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(ShouWenInfoActivity.this, "异常", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                String s = JsonUtils.pareseMessage(result);
                Toast.makeText(ShouWenInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        });

    }


    /**
     * 审批
     *
     * @param type 1==通过 2==拒绝
     */
    private void audite(String url, int type, String opinion) {
        ProgressDialogHelper.show(context, "审批中...");

        Audite audite = new Audite();
        audite.setWorkflowId(workFlowId);
        audite.setOpinion(opinion);
        audite.setType(type);

        StringRequest.postAsyn(url, audite, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                String data = JsonUtils.pareseData(response);

                if (!TextUtils.isEmpty(data) && data.contains("成功")) {
                    Toast.makeText(context, "审批成功!", Toast.LENGTH_SHORT).show();
                    AskMeFragment.isResume = true;
                    finish();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                String status = JsonUtils.parseStatus(result);
                String data = JsonUtils.pareseData(result);
                if (status != null && "6".equals(status)) {
                    Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                    FLAG = true;
                    Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                } else if (data != null && "提交失败:审核人未找到".equals(data)) {
                    Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                } else {
                    Toast.makeText(context, "审批失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * 审批意见弹窗
     *
     * @param type
     */
    private void showAuditeDialog(final int type) {
        dialog.setTitle("审批意见");
        dialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auditeMessage = dialog.spinnerEditText.getText().toString().trim();
                shenpiUrl = Global.BASE_JAVA_URL + GlobalMethord.审批申请;
                audite(shenpiUrl, type, auditeMessage);
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();
    }
//        LayoutInflater factory = LayoutInflater.from(ShouWenInfoActivity.this);//提示框
//        final View view = factory.inflate(R.layout.dialog_audite, null);//这里必须是final的
//        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//获得输入框对象
//        edit.requestFocus();
//
//        new AlertDialog.Builder(ShouWenInfoActivity.this)
//                .setTitle("填写审批意见")//提示框标题
//                .setView(view)
//                .setPositiveButton("确定",//提示框的两个按钮
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                String auditeMessage = edit.getText().toString().trim();
//                                audite(type, auditeMessage);
//                            }
//                        }).setNegativeButton("取消", null).create().show();


    /**
     * 审批意见弹窗
     *
     * @param
     */
    private void showAuditorDialog(final String list_ids) {
        dialog.setTitle("审批意见");
        dialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auiteMessage = dialog.spinnerEditText.getText().toString().trim();
                forward(workFlowId, list_ids, auiteMessage);
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();

//        LayoutInflater factory = LayoutInflater.from(ShouWenInfoActivity.this);//提示框
//        final View view = factory.inflate(R.layout.dialog_audite, null);//这里必须是final的
//        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//获得输入框对象
//        edit.requestFocus();
//        new AlertDialog.Builder(ShouWenInfoActivity.this)
//                .setTitle("审批意见")//提示框标题
//                .setView(view)
//                .setEditTextEnable(true)
//                .setPositiveButton("确定",//提示框的两个按钮
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                String auditeMessage = edit.getText().toString().trim();
//                                forward(workFlowId,list_ids,auditeMessage);
//                            }
//                        }).setNegativeButton("取消", null).create().show();
    }

    public void initDialog() {
        dialog = new AlertDialog(context);
        dialog.builder();
        dialog.setCancelable(false);
        ;
        dialog.setDropEditTextEnable(true);

    }

    /**
     * 退回表单弹出框
     *
     * @param uuid
     */
    private void showBackDialog(final String uuid) {
        dialog.setTitle("填写意见");
        dialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auditeMessage = dialog.spinnerEditText.getText().toString().trim();
                backForm(uuid, auditeMessage);
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();
        //        LayoutInflater factory = LayoutInflater.from(ShouWenInfoActivity.this);//提示框
//        final View view = factory.inflate(R.layout.dialog_audite, null);//这里必须是final的
//        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//获得输入框对象
//        edit.requestFocus();
//
//        new AlertDialog.Builder(ShouWenInfoActivity.this)
//                .setTitle("填写意见")//提示框标题
//                .setView(view)
//                .setPositiveButton("确定",//提示框的两个按钮
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                String auditeMessage = edit.getText().toString().trim();
//                                backForm(uuid, auditeMessage);
//                            }
//                        }).setNegativeButton("取消", null).create().show();

    }


    /**
     * 退回表单到一个节点
     *
     * @param uuid
     */
    private void backForm(String uuid, String Message) {
        ProgressDialogHelper.show(context, "退回申请中...");
        String url = Global.BASE_JAVA_URL + GlobalMethord.退回申请;

        JSONObject jo = new JSONObject();
        try {
            jo.put("workflowId", workFlowId);
            jo.put("nodeId", uuid);
            jo.put("opinion", Message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                Toast.makeText(context, "退回成功！", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 判断是否可以提交表单
     *
     * @return
     */
    private boolean isCanSave() {
        if (!TextUtils.isEmpty(errorMessage)) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            ProgressDialogHelper.dismiss();
            return false;
        }
        for (EditText text : mEditList) { //判断必填项
            CellInfo info = (CellInfo) text.getTag();

            String fieldDict = info.getDict();
            String fieldStyle = info.getCellStyle();

            // 仅仅对必填项校验
            // 判断用于选择的项，如果不是文本类型并且字典项不为空
            if (("textbox".equalsIgnoreCase(fieldStyle))) {//&& TextUtils.isEmpty(fieldDict)
                // 判断字典项
                String content = text.getText().toString().trim();
                info.setValue(content);
            }

            if (!info.getReadOnly() && info.getRequired() && TextUtils.isEmpty(info.getValue())) {
                Toast.makeText(context, info.getLabelText() + "为必填项", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
                return false;
            }

//            if (info.getCellStyle().equals("datepicker")) {
//                if (!TextUtils.isEmpty(info.getMaxDate())) {
//                    for (EditText text1 : mEditList) { //判断必填项
//                        CellInfo info1 = (CellInfo) text1.getTag();
//                        if (info1.getBinding().equals(info.getMaxDate())) {
//                            if (!isBigTime(ViewHelper.formatStrToDateAndTime(info1.getValue()), ViewHelper.formatStrToDateAndTime(info.getValue()))) {
//                                Toast.makeText(context, info.getLabelText() + "不能大于" + info1.getLabelText(), Toast.LENGTH_SHORT).show();
//                                return false;
//                            }
//                        }
//                    }
//                }
//            }
        }

        if (detailMap.size() > 0) {  //判断明细表必填项
            Set<Map.Entry<Integer, List<EditText>>> entries = detailMap.entrySet();

            for (Map.Entry<Integer, List<EditText>> m : entries) {
                List<EditText> editTexts = m.getValue();
                for (EditText text1 : editTexts) {
                    CellInfo fieldInfo = (CellInfo) text1.getTag();

                    String fieldDict1 = fieldInfo.getDict();
                    String fieldStyle1 = fieldInfo.getCellStyle();
                    if (("textbox".equalsIgnoreCase(fieldStyle1) && TextUtils.isEmpty(fieldDict1))) {
                        // 判断字典项
                        String content = text1.getText().toString().trim();
                        fieldInfo.setValue(content);
                    }

                    if (!fieldInfo.getReadOnly() && fieldInfo.getRequired() && TextUtils.isEmpty(fieldInfo.getValue())) {
                        ProgressDialogHelper.dismiss();
                        Toast.makeText(context, "明细表" + fieldInfo.getLabelText() + "为必填项", Toast.LENGTH_SHORT).show();
                        return false;
                    }


                    if (fieldInfo.getCellStyle().equals("datepicker")) {
                        if (!TextUtils.isEmpty(fieldInfo.getMaxDate())) {
                            for (Map.Entry<Integer, List<EditText>> m1 : entries) {
                                List<EditText> editTexts1 = m1.getValue();
                                for (EditText text2 : editTexts1) {
                                    CellInfo info1 = (CellInfo) text2.getTag();
                                    if (info1.getBinding().equals(fieldInfo.getMaxDate())) {
                                        if (!isBigTime(ViewHelper.formatStrToDateAndTime(info1.getValue()), ViewHelper.formatStrToDateAndTime(fieldInfo.getValue()))) {
                                            Toast.makeText(context, "明细表" + fieldInfo.getLabelText() + "不能大于" + info1.getLabelText(), Toast.LENGTH_SHORT).show();
                                            ProgressDialogHelper.dismiss();
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * 保存表单
     *
     * @param isSubmit 是否提交
     * @param isAudite 审批类型 1审批通过，2审批拒绝  不审批可传0
     */
    private void saveForm(final boolean isSubmit, final int isAudite) {

        attachView.uploadImage("apply", new IOnUploadMultipleFileListener() {
            @Override
            public void onStartUpload(int sum) {

            }

            @Override
            public void onProgressUpdate(int completeCount) {

            }

            @Override
            public void onComplete(String attachIds) {
                String url = Global.BASE_JAVA_URL + GlobalMethord.保存表单;

                FormData formData = new FormData();
                List<TabCell> list = new ArrayList<>();

                for (EditText text : mEditList) {
                    TabCell cell = new TabCell();
                    CellInfo fieldInfo = (CellInfo) text.getTag();
                    cell.setFieldName(fieldInfo.getBinding());
                    cell.setFieldType(fieldInfo.getDataType());
                    cell.setFieldValue(fieldInfo.getValue());

                    String fieldDict = fieldInfo.getDict();
                    String fieldStyle = fieldInfo.getCellStyle();

                    // 仅仅对必填项校验
                    // 判断用于选择的项，如果不是文本类型并且字典项不为空
                    if (("textbox".equalsIgnoreCase(fieldStyle) && TextUtils.isEmpty(fieldDict))) {
                        // 判断字典项
                        String content = text.getText().toString().trim();
                        cell.setFieldValue(content);
                    }


                    list.add(cell);
                }

                list.add(new TabCell("creatorId", "int", createrId));
                list.add(new TabCell("createTime", "date", ViewHelper.getCurrentFullTime()));
                if (!TextUtils.isEmpty(attachIds)) {
                    list.add(new TabCell("attachmentIds", "string", attachIds));
                }

                formData.setFields(list);
                formData.setId(formDataId);
                formData.setForm(formName);
                formData.setTableName(formType);
                formData.setDetailName(detailType);
                formData.setWorkflowTemplateId(workflowTemplateId);
                formData.setVersion(workflowTemplateVersion);


                List<FormDetails> formDetailses = new ArrayList<>();
                if (detailMap.size() > 0) {  //添加明细信息
                    Set<Map.Entry<Integer, List<EditText>>> entries = detailMap.entrySet();

                    for (Map.Entry<Integer, List<EditText>> m : entries) {
                        FormDetails details = new FormDetails();
                        List<TabCell> list1 = new ArrayList<>();
                        details.setId("0");
                        details.setDetailName(detailType);
                        List<EditText> editTexts = m.getValue();
                        for (EditText text : editTexts) {
                            TabCell cell = new TabCell();
                            CellInfo fieldInfo = (CellInfo) text.getTag();
                            cell.setFieldName(fieldInfo.getBinding());
                            cell.setFieldType(fieldInfo.getDataType());
                            cell.setFieldValue(fieldInfo.getValue());


                            String fieldDict = fieldInfo.getDict();
                            String fieldStyle = fieldInfo.getCellStyle();
                            if (("textbox".equalsIgnoreCase(fieldStyle) && TextUtils.isEmpty(fieldDict))) {
                                // 判断字典项
                                String content = text.getText().toString().trim();
                                cell.setFieldValue(content);
                            }
                            list1.add(cell);
                        }
                        details.setFields(list1);
                        formDetailses.add(details);
                    }
                    Logger.i(formDetailses.size() + "");
                }
                List<List<FormDetails>> lists = new ArrayList<>();
                lists.add(formDetailses);

                StringRequest.postAsynNoMap(context, url, formData, lists, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        ProgressDialogHelper.dismiss();
                        if (JsonUtils.parseStatus(response).equals("1")) {
                            formDataId = JsonUtils.pareseData(response);
                            if (!TextUtils.isEmpty(formDataId)) {
                                Toast.makeText(context, "表单保存成功！", Toast.LENGTH_SHORT).show();
                                if (isSubmit) {
                                    submitForm(formDataId);
                                }
                                if (isAudite == 1) {
                                    showAuditeDialog(1);
                                }
                                if (isAudite == 2) {
                                    showAuditeDialog(2);
                                }
                            } else {
                                Toast.makeText(context, "表单保存失败！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "表单保存失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {
                        ProgressDialogHelper.dismiss();
                    }

                    @Override
                    public void onResponseCodeErro(String result) {
                        ProgressDialogHelper.dismiss();
                        Toast.makeText(context, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    /**
     * 提交表单
     *
     * @param formId
     */
    private void submitForm(String formId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.提交表单;

        JSONObject object = new JSONObject();
        try {
            object.put("id", formId);
            object.put("name", formName);
            object.put("version", "默认");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                if (JsonUtils.parseStatus(response).contains("1")) {
                    Toast.makeText(context, "表单提交成功！", Toast.LENGTH_SHORT).show();
                    AskMeFragment.isResume = true;
                    ProductListActivity.isResume = true;
                    finish();
                } else {
                    Toast.makeText(context, "表单提交失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                Toast.makeText(context, JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 生成主表相应的UI界面,并返回FieldInfo对象的字段名,字段值(字段值是EditText类型),字段类型的集合
     */

    private void createUI(List<CellInfo> mFields, LinearLayout ll_root) {
        boolean isDetails = false; //是否是表单明细
        if (!ll_root.equals(this.ll_root)) {
            isDetails = true;
        }

        // 将dp转化为px
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (120 * scale + 0.5f);
        int height = (int) (45 * scale + 0.5f);
        int leftPadding = (int) (5 * scale + 0.5f);
        ll_root.setVisibility(View.VISIBLE);
        if (mFields == null || mFields.size() < 0) {
            return;
        }

        // 生成相应的UI界面
        for (int i = 0; i < mFields.size(); i++) {
            String fieldStyle = mFields.get(i).getCellStyle();
            String fieldName = mFields.get(i).getLabelText();
            if (!TextUtils.isEmpty(fieldStyle) && !TextUtils.isEmpty(fieldName)
                    && !fieldName.contains("附件")) {
                String dict = mFields.get(i).getDict(); // 绑定字典项
                Logger.d("Dict=" + dict);
                CellInfo fieldInfo = mFields.get(i);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(params);
                linearLayout.setPadding(leftPadding * 2, leftPadding, leftPadding * 2, leftPadding);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                if (fieldInfo.getReadOnly()) {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
                }


                // 添加左边提示文字
                addTextView(width, height, leftPadding, fieldInfo, linearLayout);

                if ("0".equals(formDataId) && fieldInfo.getReadOnly() && formName.contains("合同")) { //新建合同表单，并且是只读属性，隐藏单元格
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }

                if (("textbox".equalsIgnoreCase(fieldStyle))
                        && TextUtils.isEmpty(dict)) {
                    addEditTextView(fieldInfo, linearLayout, params, isDetails);
                } else if ("combobox".equalsIgnoreCase(fieldStyle)
                        || "dropdownlist".equalsIgnoreCase(fieldStyle)) {
                    addComboxView(fieldInfo, linearLayout, params, isDetails);
                } else if ("datepicker".equalsIgnoreCase(fieldStyle)) {
                    addDateTimeEditView(fieldInfo, linearLayout, params, isDetails);
                } else if ("checkbox".equalsIgnoreCase(fieldStyle)) {
                    addCheckedBox(fieldInfo, linearLayout, params, isDetails);
                } else if ("checklistbox".equalsIgnoreCase(fieldStyle)) {
                    addComboxListView(fieldInfo, linearLayout, params, isDetails);
                } else if ("signature".equalsIgnoreCase(fieldStyle)) {
//                    addSignatureView(fieldInfo, linearLayout, params);
                } else if ("image".equalsIgnoreCase(fieldStyle)) {
                    addMultiImageView(fieldInfo, linearLayout, params);
                } else {
                    addOtherEditView(fieldInfo, linearLayout, params, isDetails);
                }

//                if (fieldInfo.getRequired()) {
//                    mEditList.get(mEditList.size() - 1).setHint("必填");
//                }
                ll_root.addView(linearLayout);
                // 加分割线
                if (i != mFields.size() - 1) {
                    if ("0".equals(formDataId) && fieldInfo.getReadOnly()) { //新建表单，并且是只读属性，不添加分割线

                    } else {
                        addHorionzalLine(ll_root);
                    }
                }
            }
        }
    }


    /**
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param isDetails    是否是表单明细行
     */
    private void addEditTextView(CellInfo fieldInfo,
                                 LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        String fieldValue = fieldInfo.getText();
        String defalutValue = fieldInfo.getDefaultValue();

        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        setEditEnable(fieldInfo, editText);
//        setHiddenFields(fieldInfo, editText);
        if (!TextUtils.isEmpty(fieldInfo.getBinding()) && "totaldays".equalsIgnoreCase(fieldInfo.getBinding())) { //显示总天数的编辑框
            etTotalDays = editText;
        }

        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }

        // editText.setHint("点击填写");
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);


        if (!TextUtils.isEmpty(fieldValue)) {// 如果携带数据,则显示
            Logger.i(editText.getLineCount() + "--" + fieldValue);

//            if (fieldValue.length() > 15) {
//                editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            }
            editText.setText(fieldValue);
            if (fieldInfo.getEncrypted()) {
                editText.setText("******");
            }
        } else if (!TextUtils.isEmpty(fieldInfo.getDataType())) {
            // 既没有值fieldValue类型的整数，设置默认为0
            if (fieldInfo.getDataType().equalsIgnoreCase("int")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                fieldInfo.setValue("0");
            } else if (fieldInfo.getDataType().equalsIgnoreCase("double") || fieldInfo.getDataType().equalsIgnoreCase("decimal")) {
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                fieldInfo.setValue("0");
            }
        } else if (!TextUtils.isEmpty(defalutValue)) {
            editText.setText(defalutValue);

            if (defalutValue.contains("user")) {
                // 设置用户默认值
                editText.setText(Global.mUser.getName());
                // editText.setTag(Global.mUser.Id);
                fieldInfo.setValue(Global.mUser.getUuid() + "");
            } else if (defalutValue.startsWith("[") && defalutValue.endsWith("]")) {
                editText.setText("");
            }
        }


        if (fieldInfo.getReadOnly()) {
            editText.setEnabled(false);
            editText.setHint("");
        }

//        if (isEditThisField(fieldInfo)) { // 如果控件可编辑，为其绑定监听事件
//            editText.setEnabled(true);
//        }

        if (fieldInfo.getRequired()) {
            editText.setHint("必填");
        }


        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            detailMap.get(formDetailsCount).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
    }


    /**
     * 添加TextView控件
     */
    private void addTextView(int width, int height, int leftPadding,
                             CellInfo fieldInfo, LinearLayout linearLayout) {
        String fieldName = fieldInfo.getLabelText();

        TextView tvXing = new TextView(context);
        tvXing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvXing.setTextColor(Color.RED);
        tvXing.setText("*");

        TextView textView = new TextView(context);
        textView.setWidth(width);
        textView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setMinHeight(height);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(context.getResources().getColor(
                R.color.text_black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textView.setText(fieldName);

        if (fieldInfo.getRequired()) {  //如果是必填，加红色星号
            linearLayout.addView(tvXing);
        }
        linearLayout.addView(textView);
    }


    /**
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param params
     * @param isDetails
     */
    private void addComboxView(final CellInfo fieldInfo,
                               LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {


        final String fieldValue = fieldInfo.getValue();
        // new一个线性布局
        final EditText editText = new EditText(context);

        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("点击选择");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        String defaultValue = fieldInfo.getDefaultValue();
        final String dict = fieldInfo.getDict();

        boolean readOnly = fieldInfo.getReadOnly();
        if (!readOnly) {
            editText.setEnabled(false);
        }

        Map<String, String> dictHashMap = mDictionaries
                .get(fieldInfo.getDict());

        final List<ReturnDict> returnDicts = new ArrayList<>();

        if (dictHashMap != null) {
            for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
                returnDicts.add(dict1);
            }
        }
        // 设置默认值
        if (!TextUtils.isEmpty(dict)) {
            if (!TextUtils.isEmpty(fieldValue)) {// 字段有值有字典则显示
                if ("员工".equals(dict)) {
                    // 根据员工编号，查询员工姓名
                    String userName = dictionaryHelper
                            .getUserNameById(fieldValue);
                    editText.setText(userName + "");
                } else {// 除了客户和员工以外的 选择项，根据字典编号设置字典名称
                    if (dictHashMap != null) {
                        String defaultStrValue = dictHashMap.get(fieldInfo.getValue());
                        editText.setText(defaultStrValue);
                    }
                }
            } else if (!TextUtils.isEmpty(defaultValue)) {// 判断是否有DefaultValue字段
//                defaultValue = defaultValue.toLowerCase();
                if (defaultValue.contains("user")) {
                    // 设置用户默认值
                    editText.setText(Global.mUser.getName());
                    // editText.setTag(Global.mUser.Id);
                    fieldInfo.setValue(Global.mUser.getUuid() + "");
                } else if (defaultValue.contains("department")) {
//                    HashMap<Integer, String> dictHashMap = mDictionaries
//                            .get(dict);
//                    String defaultDepartment = dictHashMap
//                            .get(Global.mUser.getDepartmentId());
                    editText.setText(dictionaryHelper.getDepartNameById(Global.mUser.getDepartmentId()));
                    fieldInfo.setValue(Global.mUser.getDepartmentId() + "");
                } else if (defaultValue.contains("职务")) {
//                    HashMap<Integer, String> dictHashMap = mDictionaries
//                            .get(dict);
                    String defaultPosition = dictionaryHelper.getDepartNameById(Global.mUser.getPostCategory());
                    editText.setText(defaultPosition);
                    fieldInfo.setValue(Global.mUser.getPostCategory() + "");
                } else if (defaultValue.contains("post")) { //岗位
//                    HashMap<Integer, String> dictHashMap = mDictionaries
//                            .get(dict);
//                    String 岗位 = dictHashMap
//                            .get(dictionaryHelper.getUser(Global.mUser.Id).PostId);
                    editText.setText(dictionaryHelper.getUser(Global.mUser.getUuid()).getPost());
                    fieldInfo.setValue(dictionaryHelper.getUser(Global.mUser.getUuid()).getPostCategory());
                } else {
                    // 如果有默认值 根据字典项查询该默认值key对应的value
                    try {
                        String value = fieldValue;
                        if (dictHashMap != null) {
                            String defaultStrValue = dictHashMap.get(value);
                            editText.setText(defaultStrValue);
                            fieldInfo.setValue(value);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(fieldInfo.getText())) {//如果text不为空设置为text
            editText.setText(fieldInfo.getText());
        }

        if (!readOnly) {
            editText.setEnabled(true); //
            // 如果是字段为客户，就跳转到客户列表
            if ("客户".equals(dict)) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        ((InputMethodManager) context
                                .getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(
                                        editText.getWindowToken(), 0);
                        Intent intent = new Intent(context,
                                ClientListActivity.class);
//                        intent.putExtra(ClientListActivity.SELECT_CLIENT, true);
//                        context.startActivityForResult(intent,
//                                CreateVmFormActivity.SELECT_CLIENT_CODE);
                    }
                });
            } else if ("员工".equals(dict)) {
                // if (binding.equals("客户")) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO 取消软键盘
//                        InputSoftHelper.hiddenSoftInput(mContext, editText);
//                        // 记录当前选择员工的字段名
//                        mUserFieldName = fieldInfo.fieldName;
//
//                        if (mSelectUserPopupWindow != null) {
//                            mSelectUserPopupWindow.show(true);
//                            mSelectUserPopupWindow.setOnSelectUsersListener(new SelectUserPopupWindow.SelectUsersListener() {
//                                @Override
//                                public void onSelectUsersListener(User user) {
//                                    String mUserSelectId = user.getId();
//                                    String mUserSelectName = user.getUserName();
//                                    LogUtils.i("selectUser", mUserSelectId + "--" + mUserSelectName);
//                                    if (!TextUtils.isEmpty(mUserSelectName)) {
//                                        updateUserOnActivityForResult(
//                                                mEditList, mUserSelectName, mUserSelectId);
//                                    }
//                                }
//                            });
//                        }
//
                    }
                });
            } else if ("product".equalsIgnoreCase(dict)) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                    }
                });
            } else {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // 取消软键盘
                        InputSoftHelper.hiddenSoftInput(context, editText);

                        dictionaryQueryDialog.show(dict, returnDicts);
                        dictionaryQueryDialog
                                .setOnSelectedListener(new DictionaryQueryDialog.OnSelectedListener() {
                                    @Override
                                    public void onSelected(ReturnDict dict) {
                                        fieldInfo.setValue(dict.value);
                                        editText.setText(dict.text + "");
                                    }
                                });
                    }
                });
            }
        }

        //处理表单联动
//        final List<FormRelatedData> loaddetailrelatedfields = fieldInfo.loaddetailrelatedfields;
//        if (loaddetailrelatedfields != null && loaddetailrelatedfields.size() > 0) {
//            editText.addTextChangedListener(new TextWatcher() {
//                private FormRelatedDataFilter filter;
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    LogUtils.i("loaddetailrelatedfields", fieldInfo.fieldName + ":" + fieldInfo.fieldValue + "\t" + s.toString());
//                    if (loaddetailrelatedfields == null) {
//                        return;
//                    }
//
//                    if (loaddetailrelatedfields.size() > 0 && loaddetailrelatedfields.get(0).Filters != null) {
//                        loaddetailrelatedfields.get(0).Filters.get(0).Value = fieldInfo.fieldValue;
//
//                        GetFormRelatedData(loaddetailrelatedfields);
//                    }
//
//                }
//            });
//        }

        //处理表单联动
        if (!TextUtils.isEmpty(fieldInfo.getLoadRelated())) {
            getLoadRelatedData(fieldInfo);
        }

        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            detailMap.get(formDetailsCount).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
    }


    private void addDateTimeEditView(final CellInfo fieldInfo,
                                     LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        final EditText editText = new EditText(context);
        if (fieldInfo.getBinding().equals("startTime")) {
            startField = fieldInfo;
        }
        if (fieldInfo.getBinding().equals("endTime")) {
            endField = fieldInfo;
        }
        if (fieldInfo.getBinding().equals("onbusinessStartDate")) {
            startOutField = fieldInfo;
        }
        if (fieldInfo.getBinding().equals("onbusinessEndDate")) {
            endOutField = fieldInfo;
        }
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        String formatStr = fieldInfo.getFormat();


        // 服务器表单配置format 没有区分大小写
        // 导致格式转换报错，若时间格式不对，会导致所有表单时间都会默认当前时间值，以及新建周报评论，申请单时保存不了
        String format = (!TextUtils.isEmpty(formatStr) && formatStr
                .contains("yyyy-mm-dd")) ? formatStr.replaceAll("yyyy-mm-dd",
                "yyyy-MM-dd") : formatStr;

        String fieldVlue = fieldInfo.getText();

        boolean readOnly = fieldInfo.getReadOnly();
        if (!readOnly) {
            editText.setEnabled(false);
        }
        if (!TextUtils.isEmpty(fieldInfo.getDefaultValue()) && TextUtils.isEmpty(fieldVlue)) {
            // 没有值则 设置默认值
            if (fieldInfo.getDefaultValue().toLowerCase().contains("now")) {
                fieldVlue = ViewHelper.getDateString();
                if (!TextUtils.isEmpty(format) && format.endsWith(":ss")) {
                    format.replaceAll(":ss", "");
                }
                fieldVlue = ViewHelper.convertStrToFormatDateStr(fieldVlue,
                        format);
                editText.setText(fieldVlue);
                fieldInfo.setValue(fieldVlue);
            }
        }

        if (!TextUtils.isEmpty(fieldVlue)) {
            if (!TextUtils.isEmpty(format)) {
                Logger.i("formatDate" + format + "---" + fieldVlue);
                if (fieldVlue.contains("/")) {
                    fieldVlue = fieldVlue.replaceAll("/", "-");
                }
                if (format.endsWith(":ss")) {
                    format.replaceAll(":ss", "");
                }
//                fieldVlue = ViewHelper.convertStrToFormatDateStr(fieldVlue,
//                        format);
                Logger.d("formatDate" + "" + fieldVlue);
            }
            editText.setText(fieldVlue);
        }

        if (!readOnly) {
            editText.setEnabled(true);
        }

        if (!fieldInfo.getReadOnly()) { // 如果控件可编辑，为其绑定监听事件
            editText.setEnabled(true);
            if (TextUtils.isEmpty(format)) {
                //如果时间格式为空，默认为年月日，时分
                format = "yyyy-MM-dd HH:mm";
            }
            final String finalFormat = format;
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    boolean isSelectTime = true;
                    if ("yyyy-MM-dd".equals(finalFormat)) {
                        isSelectTime = false;
                    }
                    if (isSelectTime) {
                        pickerView = new TimePickerView(context, TimePickerView.Type.ALL);
                    } else {
                        pickerView = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY);
                    }
                    pickerView.setTime(new Date());
                    pickerView.setCyclic(true);
                    pickerView.setCancelable(true);
                    pickerView.show();
                    pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date) {
                            if (!TextUtils.isEmpty(finalFormat)) {
                                String time = ViewHelper.formatDateToStr(
                                        date, finalFormat);
                                editText.setText(time);
                                fieldInfo.setValue(ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss"));
                            }

                            if ("startTime".equals(fieldInfo.getBinding())) {
                                startFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(endFieldValue)) { //调用计算请假天数的接口，计算请假天数.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startFieldValue), ViewHelper.formatStrToDateAndTime(endFieldValue))) {
                                        Toast.makeText(context, startField.getLabelText() + "不能大于" + endField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startField.getLabelText() + "不能大于" + endField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startFieldValue, endFieldValue, false);
                                    }
                                }
                            }
                            if ("endTime".equals(fieldInfo.getBinding())) {
                                endFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(startFieldValue)) { //调用计算请假天数的接口，计算请假天数.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startFieldValue), ViewHelper.formatStrToDateAndTime(endFieldValue))) {
                                        Toast.makeText(context, startField.getLabelText() + "不能大于" + endField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startField.getLabelText() + "不能大于" + endField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startFieldValue, endFieldValue, false);
                                    }
                                }
                            }

                            if ("onbusinessStartDate".equals(fieldInfo.getBinding())) {
                                startOutFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(endOutFieldValue)) { //调用计算请假天数的接口，计算请假天数.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startOutFieldValue), ViewHelper.formatStrToDateAndTime(endOutFieldValue))) {
                                        Toast.makeText(context, startOutField.getLabelText() + "不能大于" + endOutField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startOutField.getLabelText() + "不能大于" + endOutField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startOutFieldValue, endOutFieldValue, true);
                                    }
                                }
                            }
                            if ("onbusinessEndDate".equals(fieldInfo.getBinding())) {
                                endOutFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(startOutFieldValue)) { //调用计算请假天数的接口，计算请假天数.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startOutFieldValue), ViewHelper.formatStrToDateAndTime(endOutFieldValue))) {
                                        Toast.makeText(context, startOutField.getLabelText() + "不能大于" + endOutField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startOutField.getLabelText() + "不能大于" + endOutField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startOutFieldValue, endOutFieldValue, true);
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            detailMap.get(formDetailsCount).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
    }


    /**
     * 选择是否
     */
    private void addCheckedBox(final CellInfo fieldInfo,
                               LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("点击选择");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        boolean readOnly = fieldInfo.getReadOnly();


        if (TextUtils.isEmpty(fieldInfo.getValue())) {
            fieldInfo.setValue("0");
        }

        if (!TextUtils.isEmpty(fieldInfo.getValue())) {
            try {
                int index = Integer.parseInt(fieldInfo.getValue());
                if (index < checkStrs.length) {
                    editText.setText(checkStrs[index]);
                }
            } catch (Exception e) {
                Log.e(getClass().getName(), e + "");
            }
        }


        if (readOnly) {
            editText.setEnabled(false);
        }

        if (!fieldInfo.getReadOnly()) { // 如果控件可编辑，为其绑定监听事件
            editText.setEnabled(true);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mDictIosPicker.show(checkStrs);
                    mDictIosPicker
                            .setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                @Override
                                public void onSelected(int index) {
                                    editText.setText(checkStrs[index]);
                                    fieldInfo.setValue(index + "");
                                }
                            });
                }
            });
        }

        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            detailMap.get(formDetailsCount).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
    }


    /**
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param params
     * @param isDetails
     */
    private void addComboxListView(final CellInfo fieldInfo,
                                   LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        String fieldValue = fieldInfo.getText();
        // new一个线性布局
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("点击选择");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        String defaultValue = fieldInfo.getText();
        final String dict = fieldInfo.getDict();

        boolean readOnly = fieldInfo.getReadOnly();
        if (readOnly) {
            editText.setEnabled(false);
        }

        final Map<String, String> dictHashMap = mDictionaries.get(dict);

        // 设置默认值
        if (!TextUtils.isEmpty(dict)) {
            Logger.i("checklist" + dict + "--" + fieldInfo.getValue());
            if (!TextUtils.isEmpty(fieldInfo.getValue())) {// 字段有值有字典则显示
                // // 除了客户和员工以外的 选择项，根据字典编号设置字典名称
                String[] dictIds = fieldInfo.getValue().split(",");
                StringBuilder dictNamesBuilder = new StringBuilder();
                for (String dictId : dictIds) {
                    try {
                        dictNamesBuilder.append(dictHashMap.get(dictId))
                                .append(",");
                    } catch (Exception e) {
                    }
                }
                if (dictNamesBuilder.length() > 0) {
                    editText.setText(dictNamesBuilder.substring(0,
                            dictNamesBuilder.length() - 1).toString());
                    fieldInfo.setValue(fieldInfo.getValue());
                }
            }
        }

        if (!readOnly) {
            editText.setEnabled(true); //
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 取消软键盘
                    InputSoftHelper.hiddenSoftInput(context, editText);

                    dictIosMultiPicker.show(R.id.ll_form_info_root, dictHashMap);
                    dictIosMultiPicker
                            .setOnMultiSelectedListener(new DictIosMultiPicker.OnMultiSelectedListener() {
                                @Override
                                public void onSelected(String selectedIds,
                                                       String selectedNames) {
                                    fieldInfo.setValue(selectedIds);
                                    editText.setText(StrUtils
                                            .pareseNull(selectedNames));
                                }
                            });
                }
            });
        }

        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            detailMap.get(formDetailsCount).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
    }


    private void addOtherEditView(CellInfo fieldInfo,
                                  LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        EditText editText = new EditText(context);
        setEditEnable(fieldInfo, editText);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
//        setHiddenFields(fieldInfo, editText);

        // editText.setHint("点击填写");
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        editText.setPadding(5, 0, 0, 0);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        boolean readOnly = fieldInfo.getReadOnly();

        if (!TextUtils.isEmpty(fieldInfo.getText())) {
            editText.setText(fieldInfo.getText());
        }


        if (readOnly) {
            editText.setEnabled(false);
        } else {
            editText.setEnabled(true);
        }
        editText.setTag(fieldInfo);

        if (isDetails) {
            detailMap.get(formDetailsCount).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
    }


    /**
     * 多图片类型
     *
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param params
     */
    private void addMultiImageView(final CellInfo fieldInfo,
                                   LinearLayout linearLayout, ViewGroup.LayoutParams params) {

        String fieldValue = fieldInfo.getValue();
        // fieldValue = "13288";
        // new一个线性布局
        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        editText.setFocusable(false);
        editText.setVisibility(View.GONE);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("点击选择");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        LinearLayout llOther = new LinearLayout(context);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        llOther.setLayoutParams(llParams);
        llOther.setPadding(0, 5, 5, 5);
        llOther.setGravity(Gravity.RIGHT);

        final MultipleAttachView gView = MultipleAttachView.getInstance(
                context, 4);
        gView.setLayoutParams(llParams);
        //判断单元格是否可编辑，如果是则可以添加照片、
        boolean isedit = fieldInfo.getReadOnly();
        gView.setIsAdd(!isedit);
        gView.setTag(fieldInfo);
        gView.loadImageByAttachIds(fieldValue);
        gView.setOnAddImageListener(new MultipleAttachView.OnAddImageListener() {
            @Override
            public void onAddImageListener() {
                mMultipleAttachFieldName = fieldInfo.getBinding();
            }
        });

        gView.setLayoutParams(llParams);
        llOther.addView(gView);

        editText.setTag(fieldInfo);
        mEditList.add(editText);
        mAttachViews.add(gView);
        linearLayout.addView(llOther);
        linearLayout.addView(editText);
    }


    /***
     * 设置大小写公式绑定
     *
     * @param etExpression 计算公式的文本框
     * @param expression   公式
     */
    private void setMoneyConvert(final EditText etExpression, String expression) {
        // 包含公式数字大小写转换
        Logger.i(TAG + expression);
        for (int j = 0; j < mEditList.size(); j++) {
            final EditText eText = mEditList.get(j);// 绑定公式计算的文本框监听
            CellInfo fieldInfo = (CellInfo) eText.getTag();
            String fieldName = fieldInfo.getBinding();
            if (!TextUtils.isEmpty(fieldName)
                    && expression.contains("(" + fieldName.toLowerCase() + ")")) {
                Logger.i(TAG + "涉及字段：" + fieldName);
                if (!TextUtils.isEmpty(fieldInfo.getText())) {
                    etExpression.setText(MoneyUtils.change(Double.valueOf(fieldInfo.getText())));
                }
                eText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        try {
                            int money = Integer.parseInt(s.toString());
                            String moneyUp = MoneyUtils.change(money);
                            Logger.i(TAG + "文字发生变化：" + moneyUp);
                            etExpression.setText(moneyUp + "");
                        } catch (Exception e) {
                            Logger.e(TAG + e + "");
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            }
        }
    }


    /**
     * 获取表单联动的信息
     *
     * @param info
     */
    private void getLoadRelatedData(CellInfo info) {
        String loadRelated = info.getLoadRelated();
        if (!TextUtils.isEmpty(loadRelated)) {
            final String value = info.getValue();
            final String dict = info.getDict();

            try {
                LoadRelatedData data = JsonUtils.jsonToEntity(loadRelated, LoadRelatedData.class);

                if (data != null) {
                    for (RelatedData datas : data.getRequestFieldMaps()) {
                        datas.setValue(value);
                    }

                    String url = "";
                    if (!TextUtils.isEmpty(data.getSpecialUrl()) && data.getSpecialUrl().length() > 0) {
                        url = Global.BASE_JAVA_URL + data.getSpecialUrl().substring(1, data.getSpecialUrl().length());
                    } else {
                        url = Global.BASE_JAVA_URL + GlobalMethord.表单联动;
                    }

                    StringRequest.postAsynToMap(url, data, new StringResponseCallBack() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                LoadRelatedData relatedData = JsonUtils.jsonToEntity(JsonUtils.getStringValue(response, "Data"), LoadRelatedData.class);
                                if (relatedData != null) {
                                    for (RelatedData data1 : relatedData.getResultFieldMaps()) {
                                        for (EditText et : mEditList) {
                                            CellInfo info = (CellInfo) et.getTag();
                                            if (info.getBinding().equals(data1.getvSheetFieldName())) {
                                                info.setValue(data1.getValue());
                                                if (!TextUtils.isEmpty(StrUtils.removeSpace(info.getDict()))) {
                                                    Map<String, String> map = mDictionaries.get(info.getDict());
                                                    if (map != null) {
                                                        String text = map.get(data1.getValue());
                                                        et.setText(text);
                                                    }
                                                } else {
                                                    et.setText(data1.getValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 判断是否有图片的单元格，如果有上传图片在保存表单，没有直接保存
     *
     * @param isSubmit
     * @param type
     */
    private void uploadMulipleFile(final boolean isSubmit, final int type) {

        final int[] uploadCount = {0};
        if (mAttachViews == null || mAttachViews.size() <= 0) {
            if (isCanSave()) {
                saveForm(isSubmit, type);
            }
            return;
        }

        final int size = mAttachViews.size();
        for (MultipleAttachView attachView : mAttachViews) {
            final CellInfo fieldInfo = (CellInfo) attachView.getTag();
            attachView.uploadImage("FromInfo", new IOnUploadMultipleFileListener() {
                @Override
                public void onStartUpload(int sum) {
                    Logger.i("upload_sum:::" + fieldInfo.getBinding() + "--sum="
                            + sum);
                }

                @Override
                public void onProgressUpdate(int completeCount) {
                    Logger.i("upload_progress::" + "progress=" + completeCount);
                }

                @Override
                public void onComplete(String attachIds) {
                    uploadCount[0]++;
                    Logger.i("upload_com:::" + uploadCount[0] + "\tonComplete:"
                            + attachIds);
                    fieldInfo.setValue(attachIds);
                    if (uploadCount[0] >= size) {
                        Logger.i("upload_All:::" + "所有文件上传完毕");
                        if (isCanSave()) {
                            saveForm(isSubmit, type);
                        }
                        ProgressDialogHelper.dismiss();
                    }
                }
            });
        }
    }


    /**
     * 设置文本框是否可编辑
     */
    private void setEditEnable(CellInfo fieldInfo, EditText editText) {
        if (fieldInfo.getReadOnly()) {
            // 可编辑单元格为空
            editText.setEnabled(false);
        } else {
            editText.setEnabled(true);
        }
    }


    /**
     * 绘制横向分割线
     */
    private void addHorionzalLine(LinearLayout ll_root) {
        View view = new View(context);
        view.setMinimumHeight(1);
        view.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        view.setBackgroundColor(0xFFdddddd);
        ll_root.addView(view);
    }


    /**
     * 创建申请表明细布局 添加一行明细
     *
     * @param list2
     * @param i
     */

    private void CreateDetailLayout(List<List<CellInfo>> list2, int i) {
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);


        addHorionzalLine(linearLayout);
        createDetailsHeader(linearLayout);
        addHorionzalLine(linearLayout);
        createUI(list2.get(i), linearLayout);


        ll_root.addView(linearLayout);
    }


    /**
     * 创建申请明细头
     *
     * @param
     */
    private void createDetailsHeader(final LinearLayout root) {
        formDetailsCount += 1;
        final int detailsIndex = formDetailsCount;
        detailMap.put(formDetailsCount, new ArrayList<EditText>());
        // 将dp转化为px
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (100 * scale + 0.5f);
        int height = (int) (45 * scale + 0.5f);
        int leftPadding = (int) (5 * scale + 0.5f);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setGravity(RelativeLayout.CENTER_VERTICAL);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.bg_list));
        relativeLayout.setLayoutParams(params);


        final TextView textView = new TextView(context);
        textView.setTextSize(17);
        textView.setTextColor(getResources().getColor(R.color.notice_renshi));
        textView.setText("申请单明细" + formDetailsCount);
        textView.setPadding(leftPadding * 2, leftPadding * 2, leftPadding * 2, leftPadding * 2);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv_delete = new TextView(context);
        tv_delete.setTextSize(17);
        tv_delete.setTextColor(Color.RED);
        tv_delete.setText("删除");
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_delete.setPadding(leftPadding * 2, leftPadding * 2, leftPadding * 2, leftPadding * 2);
        tv_delete.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        tv_delete.setLayoutParams(params1);

        relativeLayout.addView(textView);

        if (editable) {  //如果可编辑，添加明细删除按钮
            relativeLayout.addView(tv_delete);
        }


        mDetailsTitles.add(textView);
        root.addView(relativeLayout);

        tv_delete.setOnClickListener(new View.OnClickListener() {  //删除一行明细
            @Override
            public void onClick(View v) {
                if (formDetailsCount == 1) {
                    Toast.makeText(context, "至少保留一条明细!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ll_root.removeView(root);
                mDetailsTitles.remove(textView);
                detailMap.remove(detailsIndex);
                formDetailsCount -= 1;

                for (int i = 0; i < mDetailsTitles.size(); i++) {
                    TextView tv = mDetailsTitles.get(i);
                    tv.setText("申请单明细" + (i + 1));
                }
            }
        });
    }


    /**
     * 撤回子流程
     */
    private void recallForm() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.撤回子流程 + "?workflowId=" + workFlowId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ShouWenInfoActivity.this, "撤回成功", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(ShouWenInfoActivity.this, "撤回失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(ShouWenInfoActivity.this, "撤回失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CommanAdapter<AuditeInfo> getAuditeAdapter(List<AuditeInfo> auto) {
        return new CommanAdapter<AuditeInfo>(auto, context, R.layout.item_audite) {
            @Override
            public void convert(int position, AuditeInfo item, BoeryunViewHolder viewHolder) {
                TextView mess = viewHolder.getView(R.id.message_item_audite);

                String option = TextUtils.isEmpty(item.getOpinion()) ? "无意见" : item.getOpinion();


//                viewHolder.setUserPhotoById(R.id.head_item_audite, dictionaryHelper.getUserPhoto(item.getUserId()));
                viewHolder.setTextValue(R.id.name_item_audite,
                        TextUtils.isEmpty(item.getUserName()) ? dictionaryHelper.getUserNameById(item.getUserId()) : item.getUserName());

                if (item.getResult().contains("通过")) {
                    mess.setTextColor(getResources().getColor(R.color.color_ff87c624));
                    mess.setText("通过");
                } else {
                    mess.setTextColor(Color.RED);
                    mess.setText("否决");
                }

                viewHolder.setTextValue(R.id.audite_message_item, option);

                viewHolder.setTextValue(R.id.time_item_audite, ViewHelper.convertStrToFormatDateStr(item.getProcessTime(), "yyyy/MM/dd HH:mm"));

            }
        };
    }


    /**
     * 给多附件的控件赋值
     */
    public void updateMultipeAttachViewOnActivityForResult(int requestCode,
                                                           int resultCode, Intent data) {
        CellInfo fieldInfo = null;
        for (MultipleAttachView attachView : mAttachViews) {
            fieldInfo = (CellInfo) attachView.getTag();
            String fieldName = fieldInfo.getBinding();
            if (mMultipleAttachFieldName.equals(fieldName)) {
                attachView.onActivityiForResultImage(requestCode, resultCode,
                        data);
                mMultipleAttachFieldName = "";
                break;
            }
        }
    }


    /***
     * 设置运算符公式绑定，设置监听
     *
     * @param etExpression 计算公式的文本框
     * @param expression   公式
     */
    private void setOperatorConvert(final EditText etExpression,
                                    String expression) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        // 包含公式数字大小写转换

        final String operatorStr = expression.substring(expression.indexOf("(") + 1, expression.indexOf(","));
        Logger.i(TAG + "::setOperatorConvert--" + operatorStr);
        for (int j = 0; j < mEditList.size(); j++) {
            final EditText eText = mEditList.get(j);// 绑定公式计算的文本框监听
            final CellInfo fieldInfo = (CellInfo) eText.getTag();
            final String fieldName = fieldInfo.getBinding();
            final String fieldValue = fieldInfo.getValue();


            if (!TextUtils.isEmpty(fieldName) && operatorStr.indexOf(fieldName) != -1 && !TextUtils.isEmpty(fieldInfo.getExpression())) {
                Logger.i(TAG + "::涉及字段：" + fieldName);
                if (!TextUtils.isEmpty(fieldValue)) {
                    // 如果有值则
                    hashMap.put(fieldName, fieldValue);
                }
                eText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        try {
                            // etExpression.setText(moneyUp + "");
                            // 以键值对的形式把 涉及公式字段和值保存到hashmap
                            hashMap.put(fieldName, s.toString());
                            if (fieldInfo.getCellStyle().contains("datepicker")) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date date = sdf.parse(s.toString());
                                long time = date.getTime() / 1000;
                                hashMap.put(fieldName, time + "");
                            }
                            Logger.i(TAG + "::文字发生变化：onTextChanged");
                        } catch (Exception e) {
                            Logger.e(TAG + e + "");
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Logger.i(TAG + "::afterTextChanged：");
                        String str = operatorStr;
                        Iterator<Map.Entry<String, String>> iter = hashMap
                                .entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
                                    .next();
                            String key = entry.getKey();
                            String value = entry.getValue();
                            Logger.i(TAG + key + "--" + value);
                            str = str.replace(key, value);
                        }
                        try {
                            // 表达式转为运算符
                            Expression parsiiExpression = Parser.parse(str);
                            double result = parsiiExpression.evaluate();
                            Logger.i("out" + "::result =" + result);
                            Logger.i(TAG + "::運算結果：" + result);
                            etExpression.setText(result + "");
                        } catch (Exception e) {
                            Logger.e(TAG + str);
                        }
                        Logger.i(TAG + str);
                    }
                });
            }
        }
    }


    /**
     * 绑定两个单元格，一个单元格数据变化，另外一个单元格设置为同样的数据。
     *
     * @param etExpression
     * @param expression
     */
    private void setSameDataOperator(final EditText etExpression,
                                     String expression) {
        Logger.i(TAG + expression);
        String binding = expression.substring(expression.indexOf("sum(") + 4, expression.indexOf(")"));
        Logger.i("binding：：：" + expression);
        for (int j = 0; j < mEditList.size(); j++) {
            final EditText etPress = mEditList.get(j);
            CellInfo info = (CellInfo) etPress.getTag();

            if (!TextUtils.isEmpty(info.getBinding()) && binding.toLowerCase().equalsIgnoreCase(info.getBinding())) {
                etPress.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        etExpression.setText(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }

        }
    }


    /**
     * 判断两个时间的大小
     *
     * @param dt1 开始时间
     * @param dt2 结束时间
     * @return 如果第一个时间大于第二个时间 返回true。否则返回false
     */
    private boolean isBigTime(Date dt1, Date dt2) {

        if (dt1.getTime() > dt2.getTime())//比较时间大小,如果dt1大于dt2
        {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算请假天数
     *
     * @param startTime 请假开始时间
     * @param endTime   请假结束时间
     * @param isOut     是否是出差申请
     */
    private void caculateLeaveDays(String startTime, String endTime, boolean isOut) {

        String methord = "";
        if (isOut) {
            methord = GlobalMethord.出差的时间计算;
        } else {
            methord = GlobalMethord.调休和请假的时间计算;
        }
        String url = Global.BASE_JAVA_URL + methord + "?startTime=" + startTime + "&endTime=" + endTime;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if (etTotalDays != null) {
                    etTotalDays.setText(JsonUtils.pareseData(response));
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
