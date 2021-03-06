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
 * Created by ????????? on 2017/9/8.
 * ??????????????????
 */

public class ShouWenInfoActivity extends BaseActivity {


    /**
     * ????????????
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
    private String createrId;//?????????id
    private String formName = "";//????????????
    private String formDataId = "0";//??????????????????,??????????????????????????????0
    private String workflowTemplateId = "";//????????????????????????
    private String workflowTemplateVersion = "";//????????????
    private String workFlowId;//??????????????????
    private boolean auditable = false; //???????????????
    private boolean editable = false; //????????????????????????(?????????????????????)
    private boolean attredit = false; //?????????????????????
    private String isShowForwardRecallFormBtn = "false"; //?????????????????????????????????
    private String formType = ""; //???????????????"type": "vsheet_leave_application"
    private String detailType = ""; //?????????????????????"detailType": "vsheet_out_application_detail"
    private String intentUrl = ""; //????????????url????????????????????????????????????????????????url
    private List<WorkflowNodeVersion> nodeVersions; //???????????????
    private List<String> nodeNames = new ArrayList<String>(); //?????????????????????
    private boolean FLAG;
    private String shenpiUrl;
    private String errorMessage = ""; //????????????????????????????????????????????????
    private List<EditText> mEditList = new ArrayList<EditText>();
    private List<EditText> mDetailsEdits = new ArrayList<EditText>();
    private HashMap<Integer, List<EditText>> detailMap = new HashMap<Integer, List<EditText>>();
    private List<TextView> mDetailsTitles = new ArrayList<TextView>(); //????????????????????????
    public HashMap<String, Map<String, String>> mDictionaries;
    private boolean isAttachView = false; //?????????????????????????????????,?????????false
    /***
     * ????????????????????? ??????
     */
    private List<MultipleAttachView> mAttachViews = new ArrayList<MultipleAttachView>();
    /***
     * ?????????????????????????????????
     */
    public static String mMultipleAttachFieldName = "";
    private int formDetailsCount = 0; //???????????????
    private String startFieldValue = "";//??????????????????????????????Value???
    private String endFieldValue = "";//??????????????????????????????Value???
    private CellInfo startField = null; //?????????????????????
    private CellInfo endField = null;//?????????????????????
    private String startOutFieldValue = "";//??????????????????????????????Value???
    private String endOutFieldValue = "";//??????????????????????????????Value???
    private CellInfo startOutField = null; //???????????????????????????
    private CellInfo endOutField = null;//???????????????????????????
    private EditText etTotalDays; //?????????????????????????????????
    /**
     * ????????????
     */
    private final String[] checkStrs = {"???", "???"};

    /**
     * ???????????????
     */
    private DictionaryHelper dictionaryHelper;
    private TimePickerView pickerView;
    private DictIosPickerBottomDialog mDictIosPicker;
    private DictIosPickerBottomDialog mNodeIosPicker; //?????????????????????
    private DictIosMultiPicker dictIosMultiPicker;
    private DictionaryQueryDialog dictionaryQueryDialog;
    private BoeryunProgressBar progressBar;

    /**
     * view ??????
     */
//    private BoeryunHeaderView headerView;
    private ImageView iv_back;
    private TextView tv_title;
    private TextView tv_save;
    private TextView tv_submit;
    private CircleImageView iv_head; //??????
    private TextView tv_name;//???????????????
    private TextView tv_dept;//?????????????????????
    private TextView tv_add_details;//??????????????????
    private ScrollView scrollView;
    private LinearLayout root_attach;//?????????????????????
    private MultipleAttachView attachView;//??????????????????
    private LinearLayout root_audite;//??????????????????
    private NoScrollListView lv_audite;//???????????????
    private LinearLayout ll_audite; //?????????????????????
    private TextView tv_refuse; //????????????
    private TextView tv_copy; //??????
    private TextView tv_agree; //??????
    private TextView tv_back; //??????
    private TextView tv_recall; //??????

    private LinearLayout ll_show_form; //??????????????????
    private LinearLayout ll_show_audite; //????????????????????????
    private LinearLayout ll_zhengwen; //??????
    private BoeryunDownloadManager downloadHelper;
    public static final int REQUEST_SELECT_PARTICIPANT = 101;
    public static final int REQUEST_SELECT_AUDITOR = 102;//?????????????????????


    private LinearLayout ll_root;//?????????????????????
    private TextView tv_turn;//?????????????????????
    private String auditeMessage; //????????????
    private String auiteMessage;
    private AlertDialog dialog;
    private String isShowForwardTurn; //??????????????????????????????

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
                case REQUEST_SELECT_AUDITOR://?????????????????????
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
                            shenpiUrl = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?auditorIds=" + list_ids;
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
     * ?????????view
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


        //????????????
        ll_zhengwen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (EditText et : mEditList) {
                    CellInfo info = (CellInfo) et.getTag();
                    if ("??????".equals(info.getLabelText())) {
                        if (!TextUtils.isEmpty(info.getValue())) {
//                            showShortToast(info.getValue());
                            getAttact(info.getValue());
                        } else {
                            showShortToast("????????????");
                        }
                        break;
                    }

                }
            }
        });

    }


    /**
     * ????????????id??????????????????
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
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;
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
                        downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + attach.getUuid();
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
        mNodeIosPicker.setTitle("??????????????????");
        dictIosMultiPicker = new DictIosMultiPicker(context);
        mDictionaries = new HashMap<String, Map<String, String>>();
        dictionaryQueryDialog = new DictionaryQueryDialog(context);
    }


    /**
     * ??????formid?????????
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

            if (!formDataId.equals("0")) { //???????????????????????????????????????????????????

            } else { //??????????????????????????????
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
     * ??????????????????
     */
    private void getFormInfo() {
        ProgressDialogHelper.show(context);
        String url = "";
        if (!TextUtils.isEmpty(intentUrl)) {
            url = intentUrl;
        } else {
            url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?id=" + formDataId + "&workflowTemplateId=" + workflowTemplateId;
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
                    final List<CellInfo> list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "masterAreas"), "?????????????????????"), CellInfo.class);
                    Map<String, LinkedTreeMap<String, String>> dictMap = null;
                    dictMap = GsonTool.jsonToHas(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "dictionary"));
                    String detailInfo = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "detailArea"));
                    try {
                        workFlowId = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "?????????"));
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
                            if (!"??????".equals(nodeVersions.get(i).getTitle()) ||
                                    !"??????".equals(nodeVersions.get(i).getTitle()) || nodeVersions.get(i).getStatus() == 1) {
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

                    if (auditable) { //??????????????????
                        ll_audite.setVisibility(View.VISIBLE);
                    }

                    if (editable && auditable) { //???????????????????????????????????????????????????????????????????????????????????????z
                        tv_submit.setVisibility(View.INVISIBLE);
                    }

                    if ("false".equalsIgnoreCase(isShowForwardRecallFormBtn)) { //?????????????????????
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
                     *??????????????????
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
                        if (!TextUtils.isEmpty(attach)) {//????????????
                            attachView.loadImageByAttachIds(attach);
                            root_attach.setVisibility(View.VISIBLE);
                        } else {
                            attachView.loadImageByAttachIds("");
                            if (!editable) { //???????????????????????????????????????????????????????????????
                                root_attach.setVisibility(View.GONE);
                            } else {
                                root_attach.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        attachView.loadImageByAttachIds("");
                        if (!editable) { //???????????????????????????????????????????????????????????????
                            root_attach.setVisibility(View.GONE);
                        } else {
                            root_attach.setVisibility(View.VISIBLE);
                        }
                    }

                    initSettings(); //???????????????

                    if ("true".equals(attredit1)) {
                        attredit = true;
                    } else {
                        attredit = false;
                    }

                    if (dictMap != null) { //?????????????????????
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
                        for (int i = 0; i < list.size(); i++) { //??????binding??????????????????
                            if (TextUtils.isEmpty(list.get(i).getBinding())) {
                                list.remove(list.get(i));
                            }
                        }
                        createUI(list, ll_root);

                        setExpression(); //????????????????????????
                    }

                    if (!TextUtils.isEmpty(detailInfo)) {//????????????????????????
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


                        if (list2.size() == 0) {//???????????????????????????????????????????????????
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

                        tv_add_details.setOnClickListener(new View.OnClickListener() { //????????????
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
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//???????????????
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
     * ???????????????
     *
     * @param ids ???????????????id
     */
    private void copyTo(String ids) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.?????? + "?lid=" + formDataId + "&cropIds=" + ids;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
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
     * ??????????????????
     */
    public void setExpression() {
        if (mEditList != null && mEditList.size() > 0) {
            for (int i = 0; i < mEditList.size(); i++) {
                // ?????????????????????EditText
                final EditText etExpression = mEditList.get(i);
                CellInfo fieldInfoExpression = (CellInfo) etExpression
                        .getTag();
                String expression = fieldInfoExpression.getExpression();
                if (!TextUtils.isEmpty(expression)) {
//                    expression = expression.toLowerCase();
                    expression = expression.toLowerCase();
                    if (expression.contains("rmb(")) {
                        // ??????????????????????????????
                        setMoneyConvert(etExpression, expression);
                    } else if (expression.contains("*")
                            || expression.contains("-")
                            || expression.contains("+")
                            || expression.contains("/")) {
                        setOperatorConvert(etExpression, expression);
                    } else if (expression.contains("thousand(sum(")) { //??????????????????????????????????????????????????????????????????????????????????????????????????????
                        setSameDataOperator(etExpression, expression);
                    }
                }
            }
        }
    }


    /**
     * ????????????????????????
     */

    private void getAuditeList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?id=" + formDataId + "&workflowTemplateId=" + workflowTemplateId + "&workflowId=" + workFlowId;

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
     * ???????????????
     */
    private void initSettings() {
        attachView.setIsAdd(editable);
    }


    /**
     * ????????????
     */
    private void setOnEvent() {


        //????????????????????????
        tv_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "?????????????????????");
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
         * ????????????
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
         * ????????????
         */
        tv_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editable) {  //??????????????????????????????????????????
//                    saveForm(false, 2);
                    uploadMulipleFile(false, 2);
                } else {
                    showAuditeDialog(2);
                }
            }
        });


        /**
         *  ????????????
         */
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNodeIosPicker.show(nodeNames);
            }
        });


        /**
         *  ????????????
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
         * ????????????
         */
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isCanSave()) {
//                    saveForm(false, 0);
                ProgressDialogHelper.show(context, "?????????...");
                uploadMulipleFile(false, 0);
//                }
            }
        });


        /**
         * ????????????
         */
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isCanSave()) {
//                    saveForm(true, 0);
                ProgressDialogHelper.show(context, "?????????...");
                uploadMulipleFile(true, 0);
//                }
            }
        });


        //??????
        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                intent.putExtra("isSingleSelect", false);
                intent.putExtra("title", "???????????????");
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
     * ??????????????????-??????
     *
     * @param workflowId   ??????id
     * @param staffIds     ??????ids
     * @param auditOpinion ????????????
     * @return
     */
    public void forward(String workflowId, String staffIds, String auditOpinion) {
        ProgressDialogHelper.show(context, "?????????...");
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????????????? + "?workflowId=" + workflowId + "&staffIds=" + staffIds + "&auditOpinion=" + auditOpinion;
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
                Toast.makeText(ShouWenInfoActivity.this, "??????", Toast.LENGTH_SHORT).show();
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
     * ??????
     *
     * @param type 1==?????? 2==??????
     */
    private void audite(String url, int type, String opinion) {
        ProgressDialogHelper.show(context, "?????????...");

        Audite audite = new Audite();
        audite.setWorkflowId(workFlowId);
        audite.setOpinion(opinion);
        audite.setType(type);

        StringRequest.postAsyn(url, audite, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                String data = JsonUtils.pareseData(response);

                if (!TextUtils.isEmpty(data) && data.contains("??????")) {
                    Toast.makeText(context, "????????????!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "???????????????????????????", Toast.LENGTH_LONG).show();
                    FLAG = true;
                    Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                } else if (data != null && "????????????:??????????????????".equals(data)) {
                    Toast.makeText(context, "???????????????????????????", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShouWenInfoActivity.this, SelectedNotifierActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                } else {
                    Toast.makeText(context, "????????????", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * ??????????????????
     *
     * @param type
     */
    private void showAuditeDialog(final int type) {
        dialog.setTitle("????????????");
        dialog.setPositiveButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auditeMessage = dialog.spinnerEditText.getText().toString().trim();
                shenpiUrl = Global.BASE_JAVA_URL + GlobalMethord.????????????;
                audite(shenpiUrl, type, auditeMessage);
            }
        });
        dialog.setNegativeButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();
    }
//        LayoutInflater factory = LayoutInflater.from(ShouWenInfoActivity.this);//?????????
//        final View view = factory.inflate(R.layout.dialog_audite, null);//???????????????final???
//        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//?????????????????????
//        edit.requestFocus();
//
//        new AlertDialog.Builder(ShouWenInfoActivity.this)
//                .setTitle("??????????????????")//???????????????
//                .setView(view)
//                .setPositiveButton("??????",//????????????????????????
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                String auditeMessage = edit.getText().toString().trim();
//                                audite(type, auditeMessage);
//                            }
//                        }).setNegativeButton("??????", null).create().show();


    /**
     * ??????????????????
     *
     * @param
     */
    private void showAuditorDialog(final String list_ids) {
        dialog.setTitle("????????????");
        dialog.setPositiveButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auiteMessage = dialog.spinnerEditText.getText().toString().trim();
                forward(workFlowId, list_ids, auiteMessage);
            }
        });
        dialog.setNegativeButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();

//        LayoutInflater factory = LayoutInflater.from(ShouWenInfoActivity.this);//?????????
//        final View view = factory.inflate(R.layout.dialog_audite, null);//???????????????final???
//        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//?????????????????????
//        edit.requestFocus();
//        new AlertDialog.Builder(ShouWenInfoActivity.this)
//                .setTitle("????????????")//???????????????
//                .setView(view)
//                .setEditTextEnable(true)
//                .setPositiveButton("??????",//????????????????????????
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                String auditeMessage = edit.getText().toString().trim();
//                                forward(workFlowId,list_ids,auditeMessage);
//                            }
//                        }).setNegativeButton("??????", null).create().show();
    }

    public void initDialog() {
        dialog = new AlertDialog(context);
        dialog.builder();
        dialog.setCancelable(false);
        ;
        dialog.setDropEditTextEnable(true);

    }

    /**
     * ?????????????????????
     *
     * @param uuid
     */
    private void showBackDialog(final String uuid) {
        dialog.setTitle("????????????");
        dialog.setPositiveButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auditeMessage = dialog.spinnerEditText.getText().toString().trim();
                backForm(uuid, auditeMessage);
            }
        });
        dialog.setNegativeButton("??????", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();
        //        LayoutInflater factory = LayoutInflater.from(ShouWenInfoActivity.this);//?????????
//        final View view = factory.inflate(R.layout.dialog_audite, null);//???????????????final???
//        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//?????????????????????
//        edit.requestFocus();
//
//        new AlertDialog.Builder(ShouWenInfoActivity.this)
//                .setTitle("????????????")//???????????????
//                .setView(view)
//                .setPositiveButton("??????",//????????????????????????
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                String auditeMessage = edit.getText().toString().trim();
//                                backForm(uuid, auditeMessage);
//                            }
//                        }).setNegativeButton("??????", null).create().show();

    }


    /**
     * ???????????????????????????
     *
     * @param uuid
     */
    private void backForm(String uuid, String Message) {
        ProgressDialogHelper.show(context, "???????????????...");
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;

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
                Toast.makeText(context, "???????????????", Toast.LENGTH_SHORT).show();
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
     * ??????????????????????????????
     *
     * @return
     */
    private boolean isCanSave() {
        if (!TextUtils.isEmpty(errorMessage)) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            ProgressDialogHelper.dismiss();
            return false;
        }
        for (EditText text : mEditList) { //???????????????
            CellInfo info = (CellInfo) text.getTag();

            String fieldDict = info.getDict();
            String fieldStyle = info.getCellStyle();

            // ????????????????????????
            // ???????????????????????????????????????????????????????????????????????????
            if (("textbox".equalsIgnoreCase(fieldStyle))) {//&& TextUtils.isEmpty(fieldDict)
                // ???????????????
                String content = text.getText().toString().trim();
                info.setValue(content);
            }

            if (!info.getReadOnly() && info.getRequired() && TextUtils.isEmpty(info.getValue())) {
                Toast.makeText(context, info.getLabelText() + "????????????", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
                return false;
            }

//            if (info.getCellStyle().equals("datepicker")) {
//                if (!TextUtils.isEmpty(info.getMaxDate())) {
//                    for (EditText text1 : mEditList) { //???????????????
//                        CellInfo info1 = (CellInfo) text1.getTag();
//                        if (info1.getBinding().equals(info.getMaxDate())) {
//                            if (!isBigTime(ViewHelper.formatStrToDateAndTime(info1.getValue()), ViewHelper.formatStrToDateAndTime(info.getValue()))) {
//                                Toast.makeText(context, info.getLabelText() + "????????????" + info1.getLabelText(), Toast.LENGTH_SHORT).show();
//                                return false;
//                            }
//                        }
//                    }
//                }
//            }
        }

        if (detailMap.size() > 0) {  //????????????????????????
            Set<Map.Entry<Integer, List<EditText>>> entries = detailMap.entrySet();

            for (Map.Entry<Integer, List<EditText>> m : entries) {
                List<EditText> editTexts = m.getValue();
                for (EditText text1 : editTexts) {
                    CellInfo fieldInfo = (CellInfo) text1.getTag();

                    String fieldDict1 = fieldInfo.getDict();
                    String fieldStyle1 = fieldInfo.getCellStyle();
                    if (("textbox".equalsIgnoreCase(fieldStyle1) && TextUtils.isEmpty(fieldDict1))) {
                        // ???????????????
                        String content = text1.getText().toString().trim();
                        fieldInfo.setValue(content);
                    }

                    if (!fieldInfo.getReadOnly() && fieldInfo.getRequired() && TextUtils.isEmpty(fieldInfo.getValue())) {
                        ProgressDialogHelper.dismiss();
                        Toast.makeText(context, "?????????" + fieldInfo.getLabelText() + "????????????", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(context, "?????????" + fieldInfo.getLabelText() + "????????????" + info1.getLabelText(), Toast.LENGTH_SHORT).show();
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
     * ????????????
     *
     * @param isSubmit ????????????
     * @param isAudite ???????????? 1???????????????2????????????  ???????????????0
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
                String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;

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

                    // ????????????????????????
                    // ???????????????????????????????????????????????????????????????????????????
                    if (("textbox".equalsIgnoreCase(fieldStyle) && TextUtils.isEmpty(fieldDict))) {
                        // ???????????????
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
                if (detailMap.size() > 0) {  //??????????????????
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
                                // ???????????????
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
                                Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
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
     * ????????????
     *
     * @param formId
     */
    private void submitForm(String formId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.????????????;

        JSONObject object = new JSONObject();
        try {
            object.put("id", formId);
            object.put("name", formName);
            object.put("version", "??????");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest.postAsyn(url, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                if (JsonUtils.parseStatus(response).contains("1")) {
                    Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
                    AskMeFragment.isResume = true;
                    ProductListActivity.isResume = true;
                    finish();
                } else {
                    Toast.makeText(context, "?????????????????????", Toast.LENGTH_SHORT).show();
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
     * ?????????????????????UI??????,?????????FieldInfo??????????????????,?????????(????????????EditText??????),?????????????????????
     */

    private void createUI(List<CellInfo> mFields, LinearLayout ll_root) {
        boolean isDetails = false; //?????????????????????
        if (!ll_root.equals(this.ll_root)) {
            isDetails = true;
        }

        // ???dp?????????px
        /** ?????????????????????????????????????????????????????????????????? */
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (120 * scale + 0.5f);
        int height = (int) (45 * scale + 0.5f);
        int leftPadding = (int) (5 * scale + 0.5f);
        ll_root.setVisibility(View.VISIBLE);
        if (mFields == null || mFields.size() < 0) {
            return;
        }

        // ???????????????UI??????
        for (int i = 0; i < mFields.size(); i++) {
            String fieldStyle = mFields.get(i).getCellStyle();
            String fieldName = mFields.get(i).getLabelText();
            if (!TextUtils.isEmpty(fieldStyle) && !TextUtils.isEmpty(fieldName)
                    && !fieldName.contains("??????")) {
                String dict = mFields.get(i).getDict(); // ???????????????
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


                // ????????????????????????
                addTextView(width, height, leftPadding, fieldInfo, linearLayout);

                if ("0".equals(formDataId) && fieldInfo.getReadOnly() && formName.contains("??????")) { //????????????????????????????????????????????????????????????
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
//                    mEditList.get(mEditList.size() - 1).setHint("??????");
//                }
                ll_root.addView(linearLayout);
                // ????????????
                if (i != mFields.size() - 1) {
                    if ("0".equals(formDataId) && fieldInfo.getReadOnly()) { //?????????????????????????????????????????????????????????

                    } else {
                        addHorionzalLine(ll_root);
                    }
                }
            }
        }
    }


    /**
     * @param fieldInfo    ??????????????????<FieldInfo>?????????,??????????????????????????????
     * @param linearLayout ??????????????????????????????
     * @param isDetails    ????????????????????????
     */
    private void addEditTextView(CellInfo fieldInfo,
                                 LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        String fieldValue = fieldInfo.getText();
        String defalutValue = fieldInfo.getDefaultValue();

        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        setEditEnable(fieldInfo, editText);
//        setHiddenFields(fieldInfo, editText);
        if (!TextUtils.isEmpty(fieldInfo.getBinding()) && "totaldays".equalsIgnoreCase(fieldInfo.getBinding())) { //???????????????????????????
            etTotalDays = editText;
        }

        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("??????");
        }

        // editText.setHint("????????????");
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);


        if (!TextUtils.isEmpty(fieldValue)) {// ??????????????????,?????????
            Logger.i(editText.getLineCount() + "--" + fieldValue);

//            if (fieldValue.length() > 15) {
//                editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            }
            editText.setText(fieldValue);
            if (fieldInfo.getEncrypted()) {
                editText.setText("******");
            }
        } else if (!TextUtils.isEmpty(fieldInfo.getDataType())) {
            // ????????????fieldValue?????????????????????????????????0
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
                // ?????????????????????
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

//        if (isEditThisField(fieldInfo)) { // ????????????????????????????????????????????????
//            editText.setEnabled(true);
//        }

        if (fieldInfo.getRequired()) {
            editText.setHint("??????");
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
     * ??????TextView??????
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

        if (fieldInfo.getRequired()) {  //?????????????????????????????????
            linearLayout.addView(tvXing);
        }
        linearLayout.addView(textView);
    }


    /**
     * @param fieldInfo    ??????????????????<FieldInfo>?????????,??????????????????????????????
     * @param linearLayout ??????????????????????????????
     * @param params
     * @param isDetails
     */
    private void addComboxView(final CellInfo fieldInfo,
                               LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {


        final String fieldValue = fieldInfo.getValue();
        // new??????????????????
        final EditText editText = new EditText(context);

        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("??????");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("????????????");
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
        // ???????????????
        if (!TextUtils.isEmpty(dict)) {
            if (!TextUtils.isEmpty(fieldValue)) {// ??????????????????????????????
                if ("??????".equals(dict)) {
                    // ???????????????????????????????????????
                    String userName = dictionaryHelper
                            .getUserNameById(fieldValue);
                    editText.setText(userName + "");
                } else {// ?????????????????????????????? ????????????????????????????????????????????????
                    if (dictHashMap != null) {
                        String defaultStrValue = dictHashMap.get(fieldInfo.getValue());
                        editText.setText(defaultStrValue);
                    }
                }
            } else if (!TextUtils.isEmpty(defaultValue)) {// ???????????????DefaultValue??????
//                defaultValue = defaultValue.toLowerCase();
                if (defaultValue.contains("user")) {
                    // ?????????????????????
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
                } else if (defaultValue.contains("??????")) {
//                    HashMap<Integer, String> dictHashMap = mDictionaries
//                            .get(dict);
                    String defaultPosition = dictionaryHelper.getDepartNameById(Global.mUser.getPostCategory());
                    editText.setText(defaultPosition);
                    fieldInfo.setValue(Global.mUser.getPostCategory() + "");
                } else if (defaultValue.contains("post")) { //??????
//                    HashMap<Integer, String> dictHashMap = mDictionaries
//                            .get(dict);
//                    String ?????? = dictHashMap
//                            .get(dictionaryHelper.getUser(Global.mUser.Id).PostId);
                    editText.setText(dictionaryHelper.getUser(Global.mUser.getUuid()).getPost());
                    fieldInfo.setValue(dictionaryHelper.getUser(Global.mUser.getUuid()).getPostCategory());
                } else {
                    // ?????????????????? ?????????????????????????????????key?????????value
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

        if (!TextUtils.isEmpty(fieldInfo.getText())) {//??????text??????????????????text
            editText.setText(fieldInfo.getText());
        }

        if (!readOnly) {
            editText.setEnabled(true); //
            // ???????????????????????????????????????????????????
            if ("??????".equals(dict)) {
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
            } else if ("??????".equals(dict)) {
                // if (binding.equals("??????")) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO ???????????????
//                        InputSoftHelper.hiddenSoftInput(mContext, editText);
//                        // ????????????????????????????????????
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
                        // ???????????????
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

        //??????????????????
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

        //??????????????????
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
            editText.setHint("??????");
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


        // ?????????????????????format ?????????????????????
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String format = (!TextUtils.isEmpty(formatStr) && formatStr
                .contains("yyyy-mm-dd")) ? formatStr.replaceAll("yyyy-mm-dd",
                "yyyy-MM-dd") : formatStr;

        String fieldVlue = fieldInfo.getText();

        boolean readOnly = fieldInfo.getReadOnly();
        if (!readOnly) {
            editText.setEnabled(false);
        }
        if (!TextUtils.isEmpty(fieldInfo.getDefaultValue()) && TextUtils.isEmpty(fieldVlue)) {
            // ???????????? ???????????????
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

        if (!fieldInfo.getReadOnly()) { // ????????????????????????????????????????????????
            editText.setEnabled(true);
            if (TextUtils.isEmpty(format)) {
                //??????????????????????????????????????????????????????
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
                                if (!TextUtils.isEmpty(endFieldValue)) { //??????????????????????????????????????????????????????.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startFieldValue), ViewHelper.formatStrToDateAndTime(endFieldValue))) {
                                        Toast.makeText(context, startField.getLabelText() + "????????????" + endField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startField.getLabelText() + "????????????" + endField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startFieldValue, endFieldValue, false);
                                    }
                                }
                            }
                            if ("endTime".equals(fieldInfo.getBinding())) {
                                endFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(startFieldValue)) { //??????????????????????????????????????????????????????.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startFieldValue), ViewHelper.formatStrToDateAndTime(endFieldValue))) {
                                        Toast.makeText(context, startField.getLabelText() + "????????????" + endField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startField.getLabelText() + "????????????" + endField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startFieldValue, endFieldValue, false);
                                    }
                                }
                            }

                            if ("onbusinessStartDate".equals(fieldInfo.getBinding())) {
                                startOutFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(endOutFieldValue)) { //??????????????????????????????????????????????????????.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startOutFieldValue), ViewHelper.formatStrToDateAndTime(endOutFieldValue))) {
                                        Toast.makeText(context, startOutField.getLabelText() + "????????????" + endOutField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startOutField.getLabelText() + "????????????" + endOutField.getLabelText();
                                    } else {
                                        errorMessage = "";
                                        caculateLeaveDays(startOutFieldValue, endOutFieldValue, true);
                                    }
                                }
                            }
                            if ("onbusinessEndDate".equals(fieldInfo.getBinding())) {
                                endOutFieldValue = ViewHelper.formatDateToStr(date, "yyyy-MM-dd HH:mm:ss");
                                if (!TextUtils.isEmpty(startOutFieldValue)) { //??????????????????????????????????????????????????????.
                                    if (isBigTime(ViewHelper.formatStrToDateAndTime(startOutFieldValue), ViewHelper.formatStrToDateAndTime(endOutFieldValue))) {
                                        Toast.makeText(context, startOutField.getLabelText() + "????????????" + endOutField.getLabelText(), Toast.LENGTH_SHORT).show();
                                        errorMessage = startOutField.getLabelText() + "????????????" + endOutField.getLabelText();
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
     * ????????????
     */
    private void addCheckedBox(final CellInfo fieldInfo,
                               LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("??????");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("????????????");
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

        if (!fieldInfo.getReadOnly()) { // ????????????????????????????????????????????????
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
     * @param fieldInfo    ??????????????????<FieldInfo>?????????,??????????????????????????????
     * @param linearLayout ??????????????????????????????
     * @param params
     * @param isDetails
     */
    private void addComboxListView(final CellInfo fieldInfo,
                                   LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails) {
        String fieldValue = fieldInfo.getText();
        // new??????????????????
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("??????");
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("????????????");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        String defaultValue = fieldInfo.getText();
        final String dict = fieldInfo.getDict();

        boolean readOnly = fieldInfo.getReadOnly();
        if (readOnly) {
            editText.setEnabled(false);
        }

        final Map<String, String> dictHashMap = mDictionaries.get(dict);

        // ???????????????
        if (!TextUtils.isEmpty(dict)) {
            Logger.i("checklist" + dict + "--" + fieldInfo.getValue());
            if (!TextUtils.isEmpty(fieldInfo.getValue())) {// ??????????????????????????????
                // // ?????????????????????????????? ????????????????????????????????????????????????
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
                    // ???????????????
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
            editText.setHint("??????");
        }
//        setHiddenFields(fieldInfo, editText);

        // editText.setHint("????????????");
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
     * ???????????????
     *
     * @param fieldInfo    ??????????????????<FieldInfo>?????????,??????????????????????????????
     * @param linearLayout ??????????????????????????????
     * @param params
     */
    private void addMultiImageView(final CellInfo fieldInfo,
                                   LinearLayout linearLayout, ViewGroup.LayoutParams params) {

        String fieldValue = fieldInfo.getValue();
        // fieldValue = "13288";
        // new??????????????????
        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        editText.setFocusable(false);
        editText.setVisibility(View.GONE);
        editText.setTextColor(context.getResources().getColor(
                R.color.color_text_item_content));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("????????????");
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
        //??????????????????????????????????????????????????????????????????
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
     * ???????????????????????????
     *
     * @param etExpression ????????????????????????
     * @param expression   ??????
     */
    private void setMoneyConvert(final EditText etExpression, String expression) {
        // ?????????????????????????????????
        Logger.i(TAG + expression);
        for (int j = 0; j < mEditList.size(); j++) {
            final EditText eText = mEditList.get(j);// ????????????????????????????????????
            CellInfo fieldInfo = (CellInfo) eText.getTag();
            String fieldName = fieldInfo.getBinding();
            if (!TextUtils.isEmpty(fieldName)
                    && expression.contains("(" + fieldName.toLowerCase() + ")")) {
                Logger.i(TAG + "???????????????" + fieldName);
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
                            Logger.i(TAG + "?????????????????????" + moneyUp);
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
     * ???????????????????????????
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
                        url = Global.BASE_JAVA_URL + GlobalMethord.????????????;
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
     * ?????????????????????????????????????????????????????????????????????????????????????????????
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
                        Logger.i("upload_All:::" + "????????????????????????");
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
     * ??????????????????????????????
     */
    private void setEditEnable(CellInfo fieldInfo, EditText editText) {
        if (fieldInfo.getReadOnly()) {
            // ????????????????????????
            editText.setEnabled(false);
        } else {
            editText.setEnabled(true);
        }
    }


    /**
     * ?????????????????????
     */
    private void addHorionzalLine(LinearLayout ll_root) {
        View view = new View(context);
        view.setMinimumHeight(1);
        view.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        view.setBackgroundColor(0xFFdddddd);
        ll_root.addView(view);
    }


    /**
     * ??????????????????????????? ??????????????????
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
     * ?????????????????????
     *
     * @param
     */
    private void createDetailsHeader(final LinearLayout root) {
        formDetailsCount += 1;
        final int detailsIndex = formDetailsCount;
        detailMap.put(formDetailsCount, new ArrayList<EditText>());
        // ???dp?????????px
        /** ?????????????????????????????????????????????????????????????????? */
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
        textView.setText("???????????????" + formDetailsCount);
        textView.setPadding(leftPadding * 2, leftPadding * 2, leftPadding * 2, leftPadding * 2);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        TextView tv_delete = new TextView(context);
        tv_delete.setTextSize(17);
        tv_delete.setTextColor(Color.RED);
        tv_delete.setText("??????");
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_delete.setPadding(leftPadding * 2, leftPadding * 2, leftPadding * 2, leftPadding * 2);
        tv_delete.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        tv_delete.setLayoutParams(params1);

        relativeLayout.addView(textView);

        if (editable) {  //??????????????????????????????????????????
            relativeLayout.addView(tv_delete);
        }


        mDetailsTitles.add(textView);
        root.addView(relativeLayout);

        tv_delete.setOnClickListener(new View.OnClickListener() {  //??????????????????
            @Override
            public void onClick(View v) {
                if (formDetailsCount == 1) {
                    Toast.makeText(context, "????????????????????????!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ll_root.removeView(root);
                mDetailsTitles.remove(textView);
                detailMap.remove(detailsIndex);
                formDetailsCount -= 1;

                for (int i = 0; i < mDetailsTitles.size(); i++) {
                    TextView tv = mDetailsTitles.get(i);
                    tv.setText("???????????????" + (i + 1));
                }
            }
        });
    }


    /**
     * ???????????????
     */
    private void recallForm() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.??????????????? + "?workflowId=" + workFlowId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ShouWenInfoActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(ShouWenInfoActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(ShouWenInfoActivity.this, "????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CommanAdapter<AuditeInfo> getAuditeAdapter(List<AuditeInfo> auto) {
        return new CommanAdapter<AuditeInfo>(auto, context, R.layout.item_audite) {
            @Override
            public void convert(int position, AuditeInfo item, BoeryunViewHolder viewHolder) {
                TextView mess = viewHolder.getView(R.id.message_item_audite);

                String option = TextUtils.isEmpty(item.getOpinion()) ? "?????????" : item.getOpinion();


//                viewHolder.setUserPhotoById(R.id.head_item_audite, dictionaryHelper.getUserPhoto(item.getUserId()));
                viewHolder.setTextValue(R.id.name_item_audite,
                        TextUtils.isEmpty(item.getUserName()) ? dictionaryHelper.getUserNameById(item.getUserId()) : item.getUserName());

                if (item.getResult().contains("??????")) {
                    mess.setTextColor(getResources().getColor(R.color.color_ff87c624));
                    mess.setText("??????");
                } else {
                    mess.setTextColor(Color.RED);
                    mess.setText("??????");
                }

                viewHolder.setTextValue(R.id.audite_message_item, option);

                viewHolder.setTextValue(R.id.time_item_audite, ViewHelper.convertStrToFormatDateStr(item.getProcessTime(), "yyyy/MM/dd HH:mm"));

            }
        };
    }


    /**
     * ???????????????????????????
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
     * ??????????????????????????????????????????
     *
     * @param etExpression ????????????????????????
     * @param expression   ??????
     */
    private void setOperatorConvert(final EditText etExpression,
                                    String expression) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        // ?????????????????????????????????

        final String operatorStr = expression.substring(expression.indexOf("(") + 1, expression.indexOf(","));
        Logger.i(TAG + "::setOperatorConvert--" + operatorStr);
        for (int j = 0; j < mEditList.size(); j++) {
            final EditText eText = mEditList.get(j);// ????????????????????????????????????
            final CellInfo fieldInfo = (CellInfo) eText.getTag();
            final String fieldName = fieldInfo.getBinding();
            final String fieldValue = fieldInfo.getValue();


            if (!TextUtils.isEmpty(fieldName) && operatorStr.indexOf(fieldName) != -1 && !TextUtils.isEmpty(fieldInfo.getExpression())) {
                Logger.i(TAG + "::???????????????" + fieldName);
                if (!TextUtils.isEmpty(fieldValue)) {
                    // ???????????????
                    hashMap.put(fieldName, fieldValue);
                }
                eText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        try {
                            // etExpression.setText(moneyUp + "");
                            // ???????????????????????? ?????????????????????????????????hashmap
                            hashMap.put(fieldName, s.toString());
                            if (fieldInfo.getCellStyle().contains("datepicker")) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                Date date = sdf.parse(s.toString());
                                long time = date.getTime() / 1000;
                                hashMap.put(fieldName, time + "");
                            }
                            Logger.i(TAG + "::?????????????????????onTextChanged");
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
                        Logger.i(TAG + "::afterTextChanged???");
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
                            // ????????????????????????
                            Expression parsiiExpression = Parser.parse(str);
                            double result = parsiiExpression.evaluate();
                            Logger.i("out" + "::result =" + result);
                            Logger.i(TAG + "::???????????????" + result);
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
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param etExpression
     * @param expression
     */
    private void setSameDataOperator(final EditText etExpression,
                                     String expression) {
        Logger.i(TAG + expression);
        String binding = expression.substring(expression.indexOf("sum(") + 4, expression.indexOf(")"));
        Logger.i("binding?????????" + expression);
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
     * ???????????????????????????
     *
     * @param dt1 ????????????
     * @param dt2 ????????????
     * @return ?????????????????????????????????????????? ??????true???????????????false
     */
    private boolean isBigTime(Date dt1, Date dt2) {

        if (dt1.getTime() > dt2.getTime())//??????????????????,??????dt1??????dt2
        {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ??????????????????
     *
     * @param startTime ??????????????????
     * @param endTime   ??????????????????
     * @param isOut     ?????????????????????
     */
    private void caculateLeaveDays(String startTime, String endTime, boolean isOut) {

        String methord = "";
        if (isOut) {
            methord = GlobalMethord.?????????????????????;
        } else {
            methord = GlobalMethord.??????????????????????????????;
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
