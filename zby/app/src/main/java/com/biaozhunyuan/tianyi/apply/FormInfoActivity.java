package com.biaozhunyuan.tianyi.apply;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.activity.Inventory;
import com.biaozhunyuan.tianyi.activity.InventoryList;
import com.biaozhunyuan.tianyi.apply.model.Audite;
import com.biaozhunyuan.tianyi.apply.model.AuditeInfo;
import com.biaozhunyuan.tianyi.apply.model.CellInfo;
import com.biaozhunyuan.tianyi.apply.model.FlowNode;
import com.biaozhunyuan.tianyi.apply.model.FormOfPlay;
import com.biaozhunyuan.tianyi.apply.model.LoadRelatedData;
import com.biaozhunyuan.tianyi.apply.model.MuitiDetails;
import com.biaozhunyuan.tianyi.apply.model.OpinionModel;
import com.biaozhunyuan.tianyi.apply.model.RelatedData;
import com.biaozhunyuan.tianyi.apply.model.TabCellsController;
import com.biaozhunyuan.tianyi.apply.model.WorkflowNodeVersion;
import com.biaozhunyuan.tianyi.barcode.InvoiceScanActivity;
import com.biaozhunyuan.tianyi.barcode.utils.Constant;
import com.biaozhunyuan.tianyi.business.BusinessViewerActivity;
import com.biaozhunyuan.tianyi.cnis.model.FormComment;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.form.FormData;
import com.biaozhunyuan.tianyi.common.model.form.FormDetails;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.common.model.form.TabCell;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.utils.ClickUtil;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.DisplayUtils;
import com.biaozhunyuan.tianyi.common.utils.DoubleUtil;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.InvokeUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.LongLogUtil;
import com.biaozhunyuan.tianyi.common.utils.MoneyUtils;
import com.biaozhunyuan.tianyi.common.utils.OpenIntentUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.expenseaccount.Invoice;
import com.biaozhunyuan.tianyi.helper.DictionaryQueryDialog;
import com.biaozhunyuan.tianyi.helper.DictionaryQueryDialogHelper;
import com.biaozhunyuan.tianyi.helper.SignaturePopWindow;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.notice.SelectedUserListActivity;
import com.biaozhunyuan.tianyi.project.FormSelectPartActivity;
import com.biaozhunyuan.tianyi.project.FormSelectProjectNumberActivity;
import com.biaozhunyuan.tianyi.project.FormSelectPurchaseActivity;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectList;
import com.biaozhunyuan.tianyi.project.ProjectViewerActivity;
import com.biaozhunyuan.tianyi.view.ApplyOpinionDialog;
import com.biaozhunyuan.tianyi.view.DictIosMultiPicker;
import com.biaozhunyuan.tianyi.view.HorizontalListView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.JavaVoidCallback;
import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import okhttp3.Request;
import parsii.eval.Expression;
import parsii.eval.Parser;

import static com.biaozhunyuan.tianyi.helper.WebviewNormalActivity.EXTRA_IS_INTERCEPT;
import static com.biaozhunyuan.tianyi.helper.WebviewNormalActivity.EXTRA_IS_ROOM;
import static com.biaozhunyuan.tianyi.helper.WebviewNormalActivity.EXTRA_TITLE;
import static com.biaozhunyuan.tianyi.helper.WebviewNormalActivity.EXTRA_URL;


/**
 * Created by 王安民 on 2017/9/8.
 * 表单详情页面
 */
@SuppressLint("NewApi")
public class FormInfoActivity extends BaseActivity {


    /**
     * 数据定义
     */
    private String TAG = "FormInfoActivity";
    public static final int REQUEST_SELECT_PARTICIPANT = 101;
    public static final int REQUEST_SELECT_AUDITOR = 102;//转下一位审核人
    public static final int REQUEST_SELECT_PURCHASE = 103;
    public static final int REQUEST_SELECT_NEXT_AUDITOR = 104;//转下一位审核人,单选
    public static final int REQUEST_SELECT_FIRST_AUDITOR = 105;//选择第一步审核人
    public static final int REQUEST_SELECT_PART = 106; //选择零部件
    public static final int REQUEST_SELECT_SELL_FORMNUMBER = 108; //选择销售出差报告
    public static final int RESULT_BARCODE_RISHSCAN = 109; //进销存出入库扫描条形码
    public static final int RESULT_JS_FORM_SINGLE_SELECT = 110; //表单引擎单选
    public static final int RESULT_JS_FORM_MUTI_SELECT = 111; //表单引擎多选
    public static final int RESULT_SELECT_NEXT_NODE = 112; //选择下一个节点
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private Context context;
    private Handler handler = new Handler();
    private String createrId;//申请人id
    private String projectId;//项目id
    private String productId;//当前选中产品id
    private String customerId;//客户id
    private String formName = "";//表单名称
    private String formDataId = "0";//表单数据编号,默认新建表单，编号为0
    private String workflowTemplateId = "";//流程分类数据编号
    private String workflowTemplateVersion = "";//流程版本
    private String workFlowId;//流程分类编号
    private String mFormJson;
    private String submitTime;//表单提交时间
    private boolean approvalCostTime = false;//是否显示审批耗时
    private String status = "";//表单状态：枚举，1:未提交、2:审批中、3:已退回、4:已否决、5:已通过、6:已终止、7:已作废、8:附件错误、9:待完善
    private int nextStepId;//下一步节点id
    private boolean isfreeChoice = false; //是否是灵活审批
    private boolean auditable = false; //是否可审核
    private boolean editable = false; //单元格是否可编辑(是否可添加明细)
    private boolean enableBarcode = false; //扫码出入库扫描是否可用
    private boolean attredit = false; //是否可删除附件
    private String scanBarcodeUrl = ""; //进销存扫描条形码根据barcode获取商品详情的接口
    private String queryStorageUrl = "";//进销存扫描条形码根据skuid获取库存的接口
    private String formType = ""; //表单类型，"type": "vsheet_leave_application"
    private String checkerNames = ""; //当前审批人类型
    private String detailType = ""; //表单详细类型，"detailType": "vsheet_out_application_detail"
    private String Type = ""; //表单详细类型，"detailType": "vsheet_out_application"
    private String intentUrl = ""; //传进来的url，如果不为空。查看表单详情用这个url
    private boolean isShowCancelPush = false; //是否显示
    private List<WorkflowNodeVersion> nodeVersions; //节点的集合
    private List<FlowNode> flowNodes; //节点的集合
    private List<Invoice> invoiceList; //发票扫描的单据集合
    private List<String> nodeNames = new ArrayList<String>(); //节点名称的集合


    private String errorMessage = ""; //错误信息，保存的时候判断是否为空
    private List<EditText> mEditList = new ArrayList<EditText>();
    private HashMap<CellInfo, LinearLayout> allCellMap = new HashMap<>(); //所有单元格的map，以cellinfo为key
    private HashMap<CellInfo, String> changedCellMap = new HashMap<>(); //单元格属性（隐藏、只读、必填）发生变化的单元格
    private List<EditText> mDetailsEdits = new ArrayList<EditText>();
    private String[] hideCells; //需要隐藏值的单元格
    private HashMap<View, Map<Integer, List<EditText>>> detailsMap = new HashMap<>(); //多明细的集合
    private HashMap<LinearLayout, List<TextView>> detailsCTitles = new HashMap<>(); //多明细的标题
    private HashMap<LinearLayout, String> detailTitles = new HashMap<>(); //多明细的标题
    public HashMap<String, Map<String, String>> mDictionaries;
    private boolean isAttachView = false; //是否是底部上传图片控件,默认为false

    /***
     * 多图片上传控件 集合
     */
    private List<MultipleAttachView> mAttachViews = new ArrayList<MultipleAttachView>();
    private HashMap<CellInfo, MultipleAttachView> attachViewHashMap = new HashMap<>();
    private CellInfo selectAttachView;
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
    private DictIosPickerBottomDialog mFormOfPlayPicker;
    private DictIosPickerBottomDialog mNodeIosPicker; //节点底部弹出框
    private DictIosMultiPicker dictIosMultiPicker;
    private DictionaryQueryDialog dictionaryQueryDialog;
    private DictionaryQueryDialogHelper dictionaryQueryDialogHelper;

    /**
     * view 控件
     */
//    private BoeryunHeaderView headerView

    private LinearLayout ll_form_comment;
    private ListView lv_comment;//意见列表
    private RelativeLayout rl_nodata;//无意见时显示
    private ImageView iv_back;   //返回
    private TextView tv_title;  //表单名称
    private TextView tv_save;   //保存
    private TextView tv_submit; //提交
    private TextView tv_paper; //套打
    private Button btnSavePdf; //存为PDF
    private TextView tvTestBtn; //表单引擎生成按钮
    private TextView tv_totalTime;//审批总耗时
    private AvatarImageView iv_head; //头像
    private SimpleIndicator indicator;
    private LinearLayout ll_form;
    private LinearLayout llCancelPush;
    private TextView tvCancelPush;
    private TextView tvCancelPushText;
    private TextView tv_name;//申请人名称
    private TextView tv_dept;//申请人部门名称
    private TextView tv_add_details;//添加明细按钮
    private ScrollView scrollView;
    private LinearLayout root_attach;//上传附件根布局
    private MultipleAttachView attachView;//上传附件控件
    private LinearLayout root_audite;//审批人根布局
    private NoScrollListView lv_audite;//审批人列表
    private LinearLayout ll_audite; //审批意见根布局
    //    private LinearLayout llNode; //流程节点
    private HorizontalListView lvNode;//流程节点
    private LinearLayout llNodeRoot; //流程节点根布局
    private TextView tvCurrentAuditor; //当前审批人
    private TextView tv_refuse; //审批拒绝
    private TextView tv_copy; //抄送
    private TextView tv_agree; //同意
    private TextView tv_disagree;//不同意
    private TextView tv_back; //退回
    private PopupWindow opinionPop;
    private ApplyOpinionDialog applyOpinionDialog;
    private String opinion = "同意。";//意见, 默认为同意
    private String yiJianType = "yes";//意见类型 yes,no,默认为yes
    private Boolean isNeedInputAuditeMessage = false; //是否需要输入审批意见
    private LinearLayout ll_root;//线性布局根布局
    private LinearLayout ll_details;//明细根布局
    private LinearLayout ll_examine;
    private TextView et_input_examine;
    private TextView tv_tongyi;
    private String shenpiUrl;
    private String message;
    private String saveUrl = ""; // 保存表单接口
    private String submitUrl = ""; // 提交表单接口
    private String businessCode; //
    //选择厂内样板 需要隐藏的单元格
    private String[] Within_The_Production_Model = {"plate_text_13", "plate_text_14", "plate_text_15", "plate_text_16", "plate_datetime_2", "plate_text_17", "plate_text_18", "plate_text_19", "plate_text_20"};
    //选择现场服务 需要隐藏的单元格
    private String[] Field_Service = {"productName", "shine", "color", "addOrder", "specificationType", "quantity", "plate_text_7", "remark,plate_number_1", "plate_text_8", "plate_text_9", "plate_datetime_1", "plate_text_10"};
    //是否需要现场服务 是 需要隐藏的单元格
    private String[] The_Scene_Model_YES = {""};
    //是否需要现场服务 否 需要隐藏的单元格
    private String[] The_Scene_Model_NO = {"paint_text_11", "paint_text_12", "paint_text_13", "paint_text_14", "paint_datetime_1", "paint_text_15"};
    //合同类型 B1- 需要显示的单元格
    private String[] Contract_B1 = {"guaranteeName"};
    //合同类型 B2-非全款合同 需要显示的单元格
    private String[] Contract_B2 = {"actualPurchasePerson"};
    //合同类型 C-非全款合同 需要显示的单元格
    private String[] Contract_C = {"ownerName", "constructionContractAmount", "constructionStartDate", "constructionEndDate"};
    //合同类型 B1- B2- C-非全款合同 需要隐藏的单元格
    private String[] Contract_ALL1 = {"actualPurchasePerson", "guaranteeName", "ownerName", "constructionContractAmount", "constructionStartDate", "constructionEndDate"};
    private String apply; //判断使用oa或crm接口
    private LinearLayout detailsLinear;
    private Integer currentDetail;
    private String departmentId = ""; //支出凭证单部门id
    private List<FormOfPlay> mFormOfPlayList; //表单套打
    private List<String> mFormOfPlayListStr; //表单套打
    private ImageView iv_title;
    private String advisorId; //业务员id
    private String mainBody;
    private String 手签章;
    private String 其他;
    private String[] editableField; //当前节点需要可编辑的单元格
    private String[] invisibleField; //当前节点需要隐藏的单元格
    private LinearLayout llTitleRight; //标题左边的部分
    private LinearLayout llTitleLeft;  //标题右边的部分
    private Button btn_zhengwen;
    private WebView webView;
    private int partQtyRepertory = 0; //大正电气 零部件库存
    private int quantityNumber;//大正电气 当前领用数量
    private int etExpreInputType; //对进行公式计算的edittext的inputtype处理 防止出现TextWatcher无限迭代
    private List<MuitiDetails> muitiDetails;
    private InventoryList inventoryList = new InventoryList(new ArrayList<>());
    private Map<String, String> extentMap;
    private List<String> detailIds;

    private V8 v8;
    private V8Object v8Object;
    private V8Object v8Value;
    private String JSFunction = "";
    private String[] jsFunctionArrs;
    private Map<String, String> jsFunctionMap = new HashMap<>();
    private String JSSelectFunction = "";
    private String saveCallBack = "";
    private int currentDetailIndex = 1;


    private RadioGroup rg_audite;
    private RadioButton rb_agree_form_info, rb_disagree_form_info;//表单同意,不同意
    private TextView btn_next;//下一步


    /**
     * 表单审核意见
     */
    private List<FormComment> list;
    private CommanAdapter<FormComment> adapter;

    /**
     * 明细表省市县级联
     */
    private String privinceValue = "";
    private String cityValue = "";

    private String privinceValue_back = "";
    private String cityValue_back = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_info);
        initData();
        initViews();
        initWebView();
        initIntentData();
        getFormInfo();
        setOnEvent();
    }

    @Override
    protected void onDestroy() {
        if (v8Object != null) {
            v8Object.release();
        }
        if (v8Value != null) {
            v8Value.release();
        }
        try {
            v8.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PARTICIPANT:
                    Bundle bundle1 = data.getExtras();
                    UserList userList = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        List<User> users = userList.getUsers();
                        String copyIds = "";
                        for (User user : users) {
                            copyIds += user.getUuid() + ",";
                        }
                        if (copyIds.length() > 0) {
                            copyIds = copyIds.substring(0, copyIds.length() - 1);
                        }
                        copyTo(copyIds);
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

                        if (isNeedInputAuditeMessage) {
                            shenpiUrl = Global.BASE_JAVA_URL + GlobalMethord.审批申请 + "?auditorIds=" + list_ids;
                            audite(1, message);
                        } else {
                            showshenpi(list_ids);
                        }
                    }
                    break;
                case REQUEST_SELECT_NEXT_AUDITOR: //选择下一步审核人，单选
                    Bundle bundle3 = data.getExtras();
                    UserList list = (UserList) bundle3.getSerializable("RESULT_SELECT_USER");
                    if (list != null) {
                        List<User> users_auditor = list.getUsers();
                        String list_ids = "";
                        for (User user : users_auditor) {
                            list_ids += user.getUuid() + ",";
                        }
                        if (list_ids.length() > 0) {
                            list_ids = list_ids.substring(0, list_ids.length() - 1);
                        }

                        addFreeFlowNode(list_ids);
                    }
                    break;
                case REQUEST_SELECT_FIRST_AUDITOR: //选择下一步审核人，单选
                    Bundle bundle4 = data.getExtras();
                    UserList list1 = (UserList) bundle4.getSerializable("RESULT_SELECT_USER");
                    if (list1 != null) {
                        List<User> users_auditor = list1.getUsers();
                        String list_ids = "";
                        for (User user : users_auditor) {
                            list_ids += user.getUuid() + ",";
                        }
                        if (list_ids.length() > 0) {
                            list_ids = list_ids.substring(0, list_ids.length() - 1);
                        }

                        submitForm(formDataId, list_ids, null);
                    }
                    break;
                case REQUEST_SELECT_PURCHASE:
                    Bundle bundle2 = data.getExtras();
                    ProjectList select_product = (ProjectList) bundle2.getSerializable("REQUEST_SELECT_PURCHASE");
                    if (select_product != null) {
                        List<Project> projects = select_product.getProjects();
                        String assetName = projects.get(0).getAssetName();
                        String specificationType = projects.get(0).getSpecificationType();
                        String unit = projects.get(0).getUnit();
                        String price = projects.get(0).getPrice();
                        String uuid = projects.get(0).getUuid();
                        Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                        for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                            Map<Integer, List<EditText>> map1 = map.getValue();
                            Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                            if (map.getKey() == detailsLinear) {
                                for (Map.Entry<Integer, List<EditText>> m : entries) {
                                    if (currentDetail == m.getKey()) {
                                        List<EditText> value = m.getValue();
                                        for (EditText et : value) {
                                            CellInfo tag = (CellInfo) et.getTag();
                                            if (tag.getBinding().equals("assetName")) {
                                                et.setText(assetName);
                                            }
                                            if (tag.getBinding().equals("specificationsType")) {
                                                et.setText(specificationType);
                                                tag.setValue(uuid);
                                            }
                                            if (tag.getBinding().equals("unit")) {
                                                et.setText(unit);
                                            }
                                            if (tag.getBinding().equals("price")) {
                                                et.setText(price);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                    break;

                case REQUEST_SELECT_PART:
                    Bundle partBundle = data.getExtras();
                    ProjectList select_part = (ProjectList) partBundle.getSerializable("REQUEST_SELECT_PART");
                    if (select_part != null) {
                        List<Project> projects = select_part.getProjects();
                        String code = projects.get(0).getCode();
                        String name = projects.get(0).getName();
                        String model = projects.get(0).getModel();
                        String uuid = projects.get(0).getUuid();
                        partQtyRepertory = projects.get(0).getQtyRepertory();
                        Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                        for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                            Map<Integer, List<EditText>> map1 = map.getValue();
                            Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                            if (map.getKey() == detailsLinear) {
                                for (Map.Entry<Integer, List<EditText>> m : entries) {
                                    if (currentDetail == m.getKey()) {
                                        List<EditText> value = m.getValue();
                                        for (EditText et : value) {
                                            CellInfo tag = (CellInfo) et.getTag();
                                            if (tag.getBinding().equals("productCode")) {
                                                et.setText(code);
                                                tag.setValue(uuid);
                                            }
                                            if (tag.getBinding().equals("productName")) {
                                                et.setText(name);
                                                tag.setValue(name);
                                            }
                                            if (tag.getBinding().equals("productModel")) {
                                                et.setText(model);
                                                tag.setValue(model);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                case REQUEST_SELECT_SELL_FORMNUMBER:
                    Bundle sellBundle = data.getExtras();
                    ProjectList select_sell = (ProjectList) sellBundle.getSerializable("REQUEST_SELECT_PROJECT_FORMNUMBER");
                    if (select_sell != null) {
                        List<Project> projects = select_sell.getProjects();
                        String travelingReason = projects.get(0).getReporterName() + "-" + projects.get(0).getCompanyName() + "出差报告";
                        String travelingReportSubmitDate = projects.get(0).getCompleteTime();
                        String uuid = projects.get(0).getUuid();
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("travelingReportSubmitDate")) {
                                et.setText(travelingReportSubmitDate);
                                tag.setValue(travelingReportSubmitDate);
                            } else if (tag.getBinding().equals("travelingReason")) {
                                et.setText(travelingReason);
                                tag.setValue(uuid);
                            }
                        }
                    }

                    break;
                case RESULT_BARCODE_RISHSCAN:  //进销存出入库集合
                    Bundle barCodeBundle = data.getExtras();
                    inventoryList = (InventoryList) barCodeBundle.getSerializable("RESULT_BAR_CODE");
                    if (inventoryList != null) {
                        List<Inventory> inventories = inventoryList.getList();
                        int index = 1;
                        LinearLayout linearLayout = null;
                        for (int i = 0; i < ll_details.getChildCount(); i++) {
                            View view = ll_details.getChildAt(i);
                            if (view instanceof LinearLayout) {
                                if (index == 1) { //第一个LinearLayout是明细头布局 跳过
                                    index += 1;
                                } else {
                                    linearLayout = (LinearLayout) view;
                                    break;
                                }
                            }
                        }
                        detailsCTitles.clear();
                        detailsCTitles.put(linearLayout, new ArrayList<>());
                        linearLayout.removeAllViews();


                        MuitiDetails muitidetails = FormInfoActivity.this.muitiDetails.get(0);
                        final List<List<CellInfo>> list2 = new ArrayList<List<CellInfo>>();
                        for (int i = 0; i < muitidetails.getContent().size(); i++) { //判断如果明细有值才显示
                            for (int j = 0; j < muitidetails.getContent().get(i).size(); j++) {
                                if (!TextUtils.isEmpty(muitidetails.getContent().get(i).get(j).getValue())) {
                                    list2.add(muitidetails.getContent().get(i));
                                    break;
                                }
                            }
                        }
                        final List<CellInfo> cellInfos = new ArrayList<>();
                        for (CellInfo info : muitidetails.getContent().get(1)) {  //一个空的明细行，添加的时候添加空的明细行
                            CellInfo infoNew = new CellInfo();
                            try {
                                InvokeUtils.reflectionAttr(info, infoNew);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            infoNew.setText("");
                            infoNew.setValue("");
                            cellInfos.add(infoNew);
                        }

                        if (list2.size() == 0) {
                            list2.add(cellInfos);
                        }
                        Iterator<Inventory> iterator1 = inventories.iterator();
                        int detailIndex = 1;
                        while (iterator1.hasNext()) {
                            Inventory inventory = iterator1.next();
                            Map<String, String> cargoList = inventory.getCargoList();

                            //如果 第一条明细是空，不创建新的布局
//                            if (!isFirstDetailEmpty) {
                            detailTitles.put(linearLayout, muitidetails.getDetailName());
                            CreateDetailLayout(cellInfos, linearLayout, detailIndex, true);
                            detailIndex += 1;
//                            }

//                            isFirstDetailEmpty = false;


                            List<EditText> editTexts = detailsMap.get(linearLayout).get(detailsCTitles.get(linearLayout).size());
                            for (EditText editText : editTexts) {
                                CellInfo tag = (CellInfo) editText.getTag();
                                if (inventory.getCargoList() != null) {
                                    for (Map.Entry<String, String> map : cargoList.entrySet()) {
                                        if (map.getKey().equals(tag.getBinding())) {
                                            if (TextUtils.isEmpty(tag.getDict())) {
                                                tag.setValue(map.getValue());
                                                editText.setText(map.getValue());
                                            } else {
                                                Map<String, String> dictHashMap;
                                                if (!TextUtils.isEmpty(tag.getDisplayMemberPath())) {
                                                    dictHashMap = mDictionaries
                                                            .get(tag.getDict() + "." + tag.getDisplayMemberPath());
                                                } else {
                                                    dictHashMap = mDictionaries
                                                            .get(tag.getDict());
                                                }
                                                String s = dictHashMap.get(map.getValue());
                                                editText.setText(s);
                                                tag.setValue(map.getValue());
                                                if (!TextUtils.isEmpty(tag.getOnChange())) {
                                                    int finalDetailIndex = detailIndex;
                                                    editText.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String js = "var rowIndex = " + finalDetailIndex + ";\r\n var isPostBack = true;";
                                                            v8.executeScript(js);
                                                            try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                                                v8.executeScript(tag.getOnChange() + "();");
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }, 500);
                                                }
                                            }
                                        } else if (tag.getBinding().equals("数量")
                                                || "quantity".equals(tag.getBinding())
                                                || "count".equals(tag.getBinding())) {
                                            tag.setValue(inventory.getNum() + "");
                                            editText.setText(inventory.getNum() + "");
                                        }
                                    }
                                }
                            }
                        }

                        setExpression(false, null);
                    }
                    break;

                case RESULT_JS_FORM_SINGLE_SELECT: //js选择项单选
                case RESULT_JS_FORM_MUTI_SELECT://js选择项多选
                    List<Map<String, String>> maps = (List<Map<String, String>>)
                            data.getSerializableExtra("selectData");
                    if (maps != null) {
                        String selectDataJs = "";
                        try {
                            selectDataJs = "var selectData =" + JsonUtils.initJsonString(maps) + ";\r\n var isPostBack = true;";
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String js = selectDataJs + JSSelectFunction;
                        v8.executeScript(js);
                    }
                    break;
                case RESULT_SELECT_NEXT_NODE: //选择下一个审核节点
                    String auditorIds = data.getStringExtra("auditorIds");
                    String NodeSelectId = data.getStringExtra("NodeSelectId");
                    String NodeName = data.getStringExtra("NodeName");
                    String shenPiShunXu = data.getStringExtra("shenPiShunXu");
                    int nodeType = data.getIntExtra("nodeType", 0);
                    boolean isAudite = data.getBooleanExtra("isAudite", false);

                    if (isAudite) {
                        Audite audite = new Audite();
                        audite.setWorkflowId(workFlowId);
                        audite.setOpinion(opinion);
                        audite.setType(1); //同意
                        audite.setAuditorIds(auditorIds);
                        audite.setNodeSelectId(NodeSelectId);
                        audite.setNodeName(NodeName);
                        audite.setShenPiShunXu(shenPiShunXu);
                        audite.setNodeType(nodeType + "");
                        auditFlow(audite);
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("NodeSelectId", NodeSelectId);
                        map.put("NodeName", NodeName);
                        map.put("NodeType", "2");
                        submitForm(formDataId, auditorIds, map);
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

    private void initWebView() {
        webView.setInitialScale(25);
        // 得到浏览器设置
        WebSettings webSettings = webView.getSettings();
        // 设置浏览器中javascript可执行
        webSettings.setJavaScriptEnabled(true);
        // 设置webview推荐使用的窗口
        webSettings.setUseWideViewPort(true);
        // 置webview加载的页面的
        webSettings.setLoadWithOverviewMode(true);
        // 设置缩放控制
        webSettings.setBuiltInZoomControls(true);
        // 支持缩放
        webSettings.setSupportZoom(false);
        // 缩放按钮
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(false);
        synCookies(FormInfoActivity.this);
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
        String newCookie = cookieManager.getCookie("http://crm.tysoft.com");
        Logger.d(newCookie);
    }

    /**
     * 初始化view
     */
    private void initViews() {
//        headerView = (BoeryunHeaderView) findViewById(R.id.header_form_info);
        llTitleRight = findViewById(R.id.ll_title_right);
        llTitleLeft = findViewById(R.id.ll_title_left);
        btn_zhengwen = findViewById(R.id.btn_zhengwen);
        ll_examine = findViewById(R.id.ll_home_add_task);
        tv_tongyi = findViewById(R.id.btn_home_add_task);
        et_input_examine = findViewById(R.id.et_input_home_add_task);
        iv_back = (ImageView) findViewById(R.id.iv_back_form_info);
        tv_title = (TextView) findViewById(R.id.tv_title_form_info);
        tv_save = (TextView) findViewById(R.id.iv_save_form_info);
        tv_submit = (TextView) findViewById(R.id.iv_submit_form_info);
        tv_paper = (TextView) findViewById(R.id.iv_paper_form_info);
        btnSavePdf = findViewById(R.id.btn_save_pdf);
        tvTestBtn = (TextView) findViewById(R.id.tv_js_btn);
        tv_totalTime = (TextView) findViewById(R.id.tv_total_time);
        iv_head = (AvatarImageView) findViewById(R.id.iv_head_item_apply_info);
        indicator = findViewById(R.id.indicator);
        ll_form = findViewById(R.id.ll_form);
        llCancelPush = findViewById(R.id.ll_cancel_push);
        tvCancelPush = findViewById(R.id.tv_cancel_push);
        tvCancelPushText = findViewById(R.id.tv_cancel_push_text);
        tv_name = (TextView) findViewById(R.id.tv_creater_apply_info);
        tv_dept = (TextView) findViewById(R.id.tv_dept_apply_info);
        ll_root = (LinearLayout) findViewById(R.id.ll_form_info_root);
        ll_details = (LinearLayout) findViewById(R.id.ll_form_details_root);
        tv_add_details = (TextView) findViewById(R.id.tv_add_details_apply_info);
        scrollView = (ScrollView) findViewById(R.id.scrollview_form_info);
        attachView = (MultipleAttachView) findViewById(R.id.attach_form_info);
        root_audite = (LinearLayout) findViewById(R.id.ll_root_audite_form_info);
        lv_audite = (NoScrollListView) findViewById(R.id.lv_audite_list_form_info);
        root_attach = (LinearLayout) findViewById(R.id.root_attach_form_info);
        tv_refuse = (TextView) findViewById(R.id.tv_refuse_form_info);
        tv_copy = (TextView) findViewById(R.id.tv_copy_form_info);
        tv_agree = (TextView) findViewById(R.id.tv_agree_form_info);
        tv_disagree = (TextView) findViewById(R.id.tv_disagree_form_info);
        tv_back = (TextView) findViewById(R.id.tv_back_form_info);
        ll_audite = (LinearLayout) findViewById(R.id.ll_audite_form_info);
        tvCurrentAuditor = (TextView) findViewById(R.id.tv_current_audite);
        iv_title = findViewById(R.id.iv_title);
        lvNode = findViewById(R.id.lv_node);
        webView = findViewById(R.id.webview);
        ll_form_comment = findViewById(R.id.ll_form_comment);
        lv_comment = findViewById(R.id.lv_comment);
        rl_nodata = findViewById(R.id.rl_nodata);
        rg_audite = (RadioGroup) findViewById(R.id.rg_audite);
        rb_agree_form_info = (RadioButton) findViewById(R.id.rb_agree_form_info);
        rb_disagree_form_info = (RadioButton) findViewById(R.id.rb_disagree_form_info);
        btn_next = (TextView) findViewById(R.id.btn_next);

        llNodeRoot = (LinearLayout) findViewById(R.id.ll_root_flow_node_form_info);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        indicator.setTabItemTitles(new String[]{"表单", "审批意见", "检视流转"});
        et_input_examine.setText(opinion);
    }

    private void initData() {
        context = FormInfoActivity.this;
        v8 = V8.createV8Runtime();
        v8.registerJavaMethod(FormInfoActivity.this,
                "hide", "hide",
                new Class<?>[]{V8Array.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "hideDetail", "hideDetail",
                new Class<?>[]{V8Array.class, Integer.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "getValueDetail", "getValueDetail",
                new Class<?>[]{String.class, Integer.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "getValue", "getValue",
                new Class<?>[]{String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "getQueryString", "getQueryString",
                new Class<?>[]{String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "setReadonlyDetail", "setReadonlyDetail",
                new Class<?>[]{V8Array.class, Boolean.class, Integer.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "setReadonly", "setReadonly",
                new Class<?>[]{V8Array.class, Boolean.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "setRequiredDetail", "setRequiredDetail",
                new Class<?>[]{V8Array.class, Boolean.class, Integer.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "setRequired", "setRequired",
                new Class<?>[]{V8Array.class, Boolean.class});
        JavaVoidCallback setValueCallback = (v8Object, v8Array) -> {
            if (v8Array.length() == 2) {
                String param1 = (String) v8Array.get(0);
                String param2 = String.valueOf(v8Array.get(1));
                setValue(param1, param2);
                if (v8Array.get(0) instanceof Releasable) {
                    ((Releasable) v8Array.get(0)).release();
                }
                if (v8Array.get(1) instanceof Releasable) {
                    ((Releasable) v8Array.get(1)).release();
                }
            }
        };
        v8.registerJavaMethod(setValueCallback, "setValue");
        JavaVoidCallback setValueDetailCallback = (v8Object, v8Array) -> {
            String param1 = (String) v8Array.get(0);
            String param2 = String.valueOf(v8Array.get(1));
            Integer param3 = v8Array.getInteger(2);
            String param4 = String.valueOf(v8Array.get(3));
            setValueDetail(param1, param2, param3, param4);
            if (v8Array.get(0) instanceof Releasable) {
                ((Releasable) v8Array.get(0)).release();
            }
            if (v8Array.get(1) instanceof Releasable) {
                ((Releasable) v8Array.get(1)).release();
            }
            if (v8Array.get(2) instanceof Releasable) {
                ((Releasable) v8Array.get(2)).release();
            }
            if (v8Array.get(3) instanceof Releasable) {
                ((Releasable) v8Array.get(3)).release();
            }
        };
        v8.registerJavaMethod(setValueDetailCallback, "setValueDetail");
        v8.registerJavaMethod(FormInfoActivity.this,
                "show", "show",
                new Class<?>[]{V8Array.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "showDetail", "showDetail",
                new Class<?>[]{V8Array.class, Integer.class, String.class});
        JavaVoidCallback setDictionaryCallback = (v8Object, v8Array) -> {
            String param1 = (String) v8Array.get(0);
            if (v8Array.get(1) instanceof V8Array) {
                V8Array param2 = (V8Array) v8Array.get(1);
                setDictionary(param1, param2);
            }
            if (v8Array.get(0) instanceof Releasable) {
                ((Releasable) v8Array.get(0)).release();
            }
            if (v8Array.get(1) instanceof Releasable) {
                ((Releasable) v8Array.get(1)).release();
            }
        };
        v8.registerJavaMethod(setDictionaryCallback, "setDictionary");
        JavaVoidCallback setDictionaryDetailCallback = (v8Object, v8Array) -> {
            String param1 = (String) v8Array.get(0);
            Integer param3 = v8Array.getInteger(2);
            String param4 = String.valueOf(v8Array.get(3));
            if (v8Array.get(1) instanceof V8Array) {
                V8Array param2 = (V8Array) v8Array.get(1);
                setDictionaryDetail(param1, param2, param3, param4);
            }
            if (v8Array.get(0) instanceof Releasable) {
                ((Releasable) v8Array.get(0)).release();
            }
            if (v8Array.get(1) instanceof Releasable) {
                ((Releasable) v8Array.get(1)).release();
            }
            if (v8Array.get(2) instanceof Releasable) {
                ((Releasable) v8Array.get(2)).release();
            }
            if (v8Array.get(3) instanceof Releasable) {
                ((Releasable) v8Array.get(3)).release();
            }
        };
        v8.registerJavaMethod(setDictionaryDetailCallback, "setDictionaryDetail");
        v8.registerJavaMethod(FormInfoActivity.this,
                "add", "add",
                new Class<?>[]{String.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "request", "request",
                new Class<?>[]{String.class, String.class, V8Object.class, V8Object.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "asyncRpc", "asyncRpc",
                new Class<?>[]{String.class, V8Object.class, V8Object.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "onsave", "onsave",
                new Class<?>[]{V8Object.class, V8Object.class});
        v8.registerJavaMethod(FormInfoActivity.this,
                "getFormData", "getFormData",
                new Class<?>[]{});
        v8.registerJavaMethod(FormInfoActivity.this, "message",
                "message", new Class[]{String.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this, "createButton",
                "createButton", new Class[]{String.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this, "multipleSelectList",
                "multipleSelectList", new Class[]{String.class, String.class});
        v8.registerJavaMethod(FormInfoActivity.this, "singleSelectList",
                "singleSelectList", new Class[]{String.class, String.class});
        mFormOfPlayList = new ArrayList<>();
        mFormOfPlayListStr = new ArrayList<>();
        dictionaryHelper = new DictionaryHelper(context);
        mDictIosPicker = new DictIosPickerBottomDialog(context);
        mFormOfPlayPicker = new DictIosPickerBottomDialog(context);
        mNodeIosPicker = new DictIosPickerBottomDialog(context);
        mNodeIosPicker.setTitle("选择退回节点");
        dictIosMultiPicker = new DictIosMultiPicker(context);
        mDictionaries = new HashMap<>();
        dictionaryQueryDialog = new DictionaryQueryDialog(context);
        dictionaryQueryDialogHelper = DictionaryQueryDialogHelper.getInstance(context);
        apply = PreferceManager.getInsance().getValueBYkey(Global.mUser.getUuid() + "APPLY");
        etExpreInputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    }

    /**
     * 自由选择节点-转发
     *
     * @param workflowId   流程id
     * @param staffIds     员工ids
     * @param auditOpinion 审核意见
     * @return
     */
    public void forward(final String workflowId, final String staffIds,
                        final String auditOpinion) {
        ProgressDialogHelper.show(context, "审批中...");
        String url = Global.BASE_JAVA_URL + GlobalMethord.转下一步审核人 + "?workflowId=" + workflowId + "&staffIds=" + staffIds + "&auditOpinion=" + auditOpinion;
//        if (apply.equals("21fe612a54d842238c7cc2fbe58c4ed4")) {
//            url = Global.BASE_JAVA_URL + GlobalMethord.CRM审批申请;// + "?workflowId=" + workflowId + "&staffIds=" + staffIds + "&auditOpinion=" + auditOpinion;
//        }
//        JSONObject object = new JSONObject();
//        try {
//            object.put("workflowId", workflowId);
//            object.put("auditorIds", staffIds);
//            object.put("auditOpinion", auditOpinion);
//            object.put("type", "1");
//        } catch (Exception e) {
//
//        }

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String s = JsonUtils.pareseMessage(response);
                Toast.makeText(FormInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(FormInfoActivity.this, "异常", Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                String s = JsonUtils.pareseMessage(result);
                Toast.makeText(FormInfoActivity.this, s, Toast.LENGTH_SHORT).show();
                ProgressDialogHelper.dismiss();
            }
        });

    }

    /**
     * 转下一步审核人后审批
     *
     * @param workflowId
     * @param opinion
     * @param auditorIds
     */
    private void approveForm(String workflowId, String opinion, String auditorIds) {
        JSONObject object = new JSONObject();
        try {
            object.put("workflowId", workflowId);
            object.put("auditorIds", auditorIds);
            object.put("auditOpinion", opinion);
            object.put("type", "1");
            object.put("version", "默认");
        } catch (Exception e) {

        }
        StringRequest.postAsyn(shenpiUrl, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                String data = JsonUtils.pareseData(response);
                if (!TextUtils.isEmpty(data) && data.contains("成功")) {
                    Toast.makeText(context, "审批成功!", Toast.LENGTH_SHORT).show();
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
                List<User> userList;
                try {
                    userList = JsonUtils.jsonToArrayEntity(data, User.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    userList = new ArrayList<>();
                }
                if ("6".equals(status)) {
                    if (userList.size() > 0) {
                        isNeedInputAuditeMessage = true;
                        UserList userList1 = new UserList();
                        userList1.setUsers(userList);
                        Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FormInfoActivity.this, SelectedUserListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("auditorList", userList1);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                    } else {
                        Intent intent = new Intent(context, AuditSelectNextNodeActivity.class);
                        intent.putExtra("nodeId", nextStepId);
                        intent.putExtra("workflowId", workflowId);
                        intent.putExtra("workflowTemplateId", workflowTemplateId);
                        intent.putExtra("yiJianType", yiJianType);
                        intent.putExtra("isAudite", true);
                        startActivity(intent);
                    }
                } else if ("提交失败:审核人未找到".equals(data)) {
                    Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FormInfoActivity.this, SelectedNotifierActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                } else {
                    showShortToast(data);
                }
            }
        });
    }


    /**
     * 获取formid和名称
     */
    private void initIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            intentUrl = bundle.getString("exturaUrl");
            isShowCancelPush = bundle.getBoolean("isShowCancelPush", false);
            formName = bundle.getString("formName");
            invoiceList = (List<Invoice>) bundle.getSerializable("invoiceList");
            formDataId = bundle.getString("formDataId");
            workflowTemplateId = bundle.getString("workflowTemplateId");
//            workFlowId = bundle.getString("workflowId");
            createrId = bundle.getString("createrId");
            projectId = bundle.getString("projectId");
            customerId = bundle.getString("customerId");
            advisorId = bundle.getString("advisorId");
            extentMap = (Map<String, String>) bundle.getSerializable("extentMap");
            detailIds = (List<String>) bundle.getSerializable("detailIds");
//            headerView.setTitle(StrUtils.pareseNull(formName));
            tv_title.setText(StrUtils.pareseNull(formName));

            if (null == formDataId) {
                formDataId = "0";
            }

            if (!"0".equals(formDataId)) { //如果不是新建申请，获取审批意见列表
//                getAuditeList();
            } else { //新建申请审批列表隐藏
                root_audite.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(createrId)) {
                createrId = Global.mUser.getUuid();
            }
            if (isShowCancelPush) {
                llCancelPush.setVisibility(View.VISIBLE);
            }

            ImageUtils.displyUserPhotoById(context, createrId, iv_head);
            tv_name.setText(dictionaryHelper.getUserNameById(createrId));
            tv_dept.setText(dictionaryHelper.getDepartNameById(dictionaryHelper.getUser(createrId).getDepartmentId()));
        }
        saveUrl = Global.BASE_JAVA_URL + GlobalMethord.保存表单;
        submitUrl = Global.BASE_JAVA_URL + GlobalMethord.提交表单;
        shenpiUrl = Global.BASE_JAVA_URL + GlobalMethord.审批申请;
        tv_paper.setText(Global.FORM_PAPER);
    }

//accommodationsPrice

    /**
     * 获取表单数据
     */
    private void getFormInfo() {
        ProgressDialogHelper.show(context);
        String url ;
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
               // LongLogUtil.e("Doc_result",result);
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
                        getFormComment(workFlowId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mFormJson = JsonUtils.getStringValue(result, "form");
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
                        checkerNames = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "checkerNames");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        createrId = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "creatorId"));
                        ImageUtils.displyUserPhotoById(context, createrId, iv_head);
                        tv_name.setText(dictionaryHelper.getUserNameById(createrId));
                        tv_dept.setText(dictionaryHelper.getDepartNameById(dictionaryHelper.getUser(createrId).getDepartmentId()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String editable = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "editableField"));
                        if (!TextUtils.isEmpty(editable)) {
                            editableField = editable.split(",");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String invisible = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "invisibleField"));
                        if (!TextUtils.isEmpty(invisible)) {
                            invisibleField = invisible.split(",");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String vSheetDataJson = StrUtils.removeRex(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "vSheetDataJson"));
                        Map<String, String> stringStringHashMap = ((Map<String, String>) JSON.parse(vSheetDataJson));
                        mainBody = stringStringHashMap.get("mainBody");
                        手签章 = stringStringHashMap.get("手签章");
                        其他 = stringStringHashMap.get("其他");
                        if (!TextUtils.isEmpty(mainBody)) {
                            btn_zhengwen.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        flowNodes = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "nodes"), FlowNode.class);
//                        getAuditeList();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        isfreeChoice = Boolean.valueOf(JsonUtils.getStringValue(result, "isfreeChoice"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        submitTime = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "submissionTime");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        status = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "status");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        approvalCostTime = Boolean.valueOf(JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "setting"), "approvalCostTime"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        nextStepId = Integer.valueOf(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "nextStepId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        enableBarcode = Boolean.valueOf(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "enableBarcode"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        scanBarcodeUrl = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "scanBarcodeUrl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        queryStorageUrl = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "queryStorageUrl");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        workflowTemplateId = JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "setting"), "uuid");
                        getFormJs();
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
                        String hideCell = JsonUtils.getStringValue(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "returnNode"), "hideCells");
                        hideCells = hideCell.split(",");
                        if (hideCell == null) {
                            hideCells = new String[]{};
                        }
                    } catch (Exception e) {
                        hideCells = new String[]{};
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
                        ll_examine.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.VISIBLE);
                    }

                    if (editable && auditable) { //如果审批过程中，表单可编辑，只显示保存按钮，隐藏提交按钮。z
                        tv_submit.setVisibility(View.GONE);
                    }

                    checkModelFileExist();

                    /**
                     *获取表单类型
                     */
                    formType = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "type");

                    try {
                        detailType = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "detailType");
                        Type = JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "type");
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
                        createUI(list, ll_root, 0, ll_root);
                    }
                    if (mEditList != null && mEditList.size() > 0) {  //表单联动
                        for (int i = 0; i < mEditList.size(); i++) {
                            final EditText etExpression = mEditList.get(i);
                            CellInfo fieldInfoExpression = (CellInfo) etExpression
                                    .getTag();
                            getLoadRelatedData(fieldInfoExpression);
                        }
                    }

                    //明细数据
                    FormInfoActivity.this.muitiDetails = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(result, "form"), "detailArea"), MuitiDetails.class);
                    if (FormInfoActivity.this.muitiDetails != null && FormInfoActivity.this.muitiDetails.size() > 0) {
                        for (int k = 0; k < FormInfoActivity.this.muitiDetails.size(); k++) {
                            MuitiDetails details = FormInfoActivity.this.muitiDetails.get(k);
                            final boolean[] showMore = {false};
                            final LinearLayout linearLayout = new LinearLayout(context); //创建一个明细的线性布局
                            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setLayoutParams(params);


                            View view = View.inflate(context, R.layout.header_multiple_form_details, null);
                            ImageView ivRishScan = view.findViewById(R.id.richscan); //进销存的扫一扫功能
                            TextView tvDetailName = (TextView) view.findViewById(R.id.tv_name_form_detail); //明细表名称
                            TextView ivAddDetail = (TextView) view.findViewById(R.id.iv_add_form_detail); //明细表 添加明细按钮
                            final ImageView ivShowMore = (ImageView) view.findViewById(R.id.iv_show_more_form_detail); //明细表 展示表单明细按钮
                            if (formName.equals("入库单") || formName.equals("出库单") || formName.equals("调拨单")) {
                                ivRishScan.setVisibility(View.VISIBLE);
                            }
                            if (enableBarcode && editable) {
                                ivRishScan.setVisibility(View.VISIBLE);
                            }
                            if (editable) {
                                ivAddDetail.setVisibility(View.VISIBLE);
                            } else {
                                ivAddDetail.setVisibility(View.GONE);
                            }


                            boolean IsShowFormAddDetailBtn = PreferceManager.getInsance().getValueBYkey("IsShowFormAddDetailBtn", true);
                            if (IsShowFormAddDetailBtn) {
                                ivAddDetail.setVisibility(View.VISIBLE);
                            } else {
                                ivAddDetail.setVisibility(View.GONE);
                            }

                            detailsCTitles.put(linearLayout, new ArrayList<TextView>());
                            tvDetailName.setText(TextUtils.isEmpty(details.getDetailTitle())
                                    ? details.getDetailName() : details.getDetailTitle());
                            addHorionzalLine(ll_details);
                            ll_details.addView(view); //明细线性布局里添加明细头
                            addHorionzalLine(ll_details);


                            ll_details.addView(linearLayout);
                            final List<List<CellInfo>> list2 = new ArrayList<List<CellInfo>>();
                            for (int i = 0; i < details.getContent().size(); i++) { //判断如果明细有值才显示
                                for (int j = 0; j < details.getContent().get(i).size(); j++) {
                                    if (!TextUtils.isEmpty(details.getContent().get(i).get(j).getValue())) {
                                        list2.add(details.getContent().get(i));
                                        break;
                                    }
                                }
                            }


                            final List<CellInfo> cellInfos = new ArrayList<>();

                            for (CellInfo info : details.getContent().get(1)) {  //一个空的明细行，添加的时候添加空的明细行
                                CellInfo infoNew = new CellInfo();
                                try {
                                    InvokeUtils.reflectionAttr(info, infoNew);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                infoNew.setText("");
                                infoNew.setValue("");
                                cellInfos.add(infoNew);
                            }

                            if (list2.size() == 0) {
                                list2.add(cellInfos);
                            }

                            int index = 0;
                            for (int i = 0; i < list2.size(); i++) {
                                List<CellInfo> list1 = list2.get(i);
                                index += 1;
                                detailTitles.put(linearLayout, details.getDetailName());
                                boolean isLastDetail = false;
                                if (k == FormInfoActivity.this.muitiDetails.size() - 1 && i == list2.size() - 1) {
                                    isLastDetail = true;
                                }
                                CreateDetailLayout(list1, linearLayout, index, true);
                            }

                            linearLayout.setVisibility(View.VISIBLE);
                            ivShowMore.setOnClickListener(new View.OnClickListener() {  //展开/收缩明细表
                                @Override
                                public void onClick(View v) {
                                    showMore[0] = !showMore[0];

                                    if (showMore[0]) {
                                        linearLayout.setVisibility(View.VISIBLE);
                                        ivShowMore.setImageResource(R.drawable.arrow_down);
                                    } else {
                                        ivShowMore.setImageResource(R.drawable.arrow_right);
                                        linearLayout.setVisibility(View.GONE);
                                    }
                                }
                            });
                            ivRishScan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, InvoiceScanActivity.class);
                                    intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
                                    intent.putExtra("formName", formName);//表单名称
                                    intent.putExtra("inventoryList", inventoryList);//已经扫码的商品
                                    intent.putExtra("scanBarcodeUrl", scanBarcodeUrl);//进销存扫描条形码根据barcode获取商品详情的接口
                                    intent.putExtra("queryStorageUrl", queryStorageUrl);//进销存扫描条形码根据skuid获取库存的接口
                                    startActivityForResult(intent, RESULT_BARCODE_RISHSCAN);
                                }
                            });

                            final int[] finalIndex = {index};
                            ivAddDetail.setOnClickListener(new View.OnClickListener() { //添加一个明细
                                @Override
                                public void onClick(View v) {
                                    finalIndex[0] += 1;
                                    detailTitles.put(linearLayout, details.getDetailName());
                                    CreateDetailLayout(cellInfos, linearLayout,
                                            detailsCTitles.get(linearLayout).size() + 1, true);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                                        }
                                    });
                                }
                            });
                            /*if (formDataId.equals("0") && editable) { //默认添加一条明细
                                finalIndex[0] += 1;
                                detailTitles.put(linearLayout, details.getDetailName());
                                CreateDetailLayout(cellInfos, linearLayout,
                                        detailsCTitles.get(linearLayout).size() + 1, true);
                            }*/
                        }
                    }

                    if (formName.equals("退换货申请单")) {
                        for (EditText et : mEditList) {
                            CellInfo fieldInfo = (CellInfo) et.getTag();
                            if (fieldInfo.getBinding().equals("oweAmount")) {
                                LinearLayout parent = (LinearLayout) et.getParent();
                                for (EditText et1 : mEditList) {
                                    CellInfo tag = (CellInfo) et1.getTag();
                                    if (tag.getBinding().equals("isOweMoney")) {
                                        if (!TextUtils.isEmpty(et1.getText().toString())) {
                                            if (et1.getText().toString().equals("是")) {
                                                parent.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
                                                et.setEnabled(false);
                                            } else {
                                                parent.setBackgroundColor(getResources().getColor(R.color.white));
                                                et.setEnabled(true);
                                            }
                                        } else {
                                            parent.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
                                            et.setEnabled(false);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (formName.equals("样板及现场服务")) {
                        hiddenMasterAreas(Field_Service, false);//现场服务
                    }
                    if (formName.equals("样漆及现场样板")
                            || formName.equals("授信申请及合同")) {
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("plate_text_2")) {
                                String s1 = tag.getText();
                                if (!TextUtils.isEmpty(s1)) {
                                    if (s1.equals("厂内样板")) {
                                        hiddenMasterAreas(Within_The_Production_Model, false);
                                    }
                                }
                            }
                            if (tag.getBinding().equals("paint_text_20")) {
                                String s = tag.getText();
                                if (s.equals("是")) {
                                    hiddenMasterAreas(The_Scene_Model_YES, false);
                                } else if (s.equals("否")) {
                                    hiddenMasterAreas(The_Scene_Model_NO, false);
                                }
                            }
                            if (tag.getBinding().equals("contractType")) {
                                String s = tag.getText();
                                if (!TextUtils.isEmpty(s)) {
                                    hiddenMasterAreas(Contract_ALL1);
                                    if (s.equals("B2-外借资产")) {
                                        hiddenMasterAreas(Contract_B2, true);
                                    } else if (s.equals("C-内借建设资质")) {
                                        hiddenMasterAreas(Contract_C, true);
                                    } else if (s.equals("B1-非全款合同,B2-外借资产,C-内借建设资质")
                                            || s.equals("B1-非全款合同,B2-外借资产")
                                            || s.equals("B1-非全款合同")) {
                                        hiddenMasterAreas(Contract_B1, true);
                                    }
                                    if (s.equals("F-其他")) {
                                        for (EditText et1 : mEditList) {
                                            CellInfo tag1 = (CellInfo) et1.getTag();
                                            if (tag1.getBinding().equals("otherContractType")) {
                                                setEditTextEnabled(et1, tag1, false);
                                            }
                                        }
                                    }
                                } else {
                                    hiddenMasterAreas(Contract_ALL1, false);
                                }
                            }
                            if (tag.getBinding().equals("customerCompanyNature")) {
                                String s = tag.getText();
                                if (s.equals("其他")) {
                                    for (EditText et1 : mEditList) {
                                        CellInfo tag1 = (CellInfo) et1.getTag();
                                        if (tag1.getBinding().equals("otherCustomerCompanyNature")) {
                                            setEditTextEnabled(et1, tag1, false);
                                        }
                                    }
                                }
                            }
                            if (tag.getBinding().equals("signedContract")) {
                                String s = tag.getText();
                                if (s.equals("其他")) {
                                    for (EditText et1 : mEditList) {
                                        CellInfo tag1 = (CellInfo) et1.getTag();
                                        if (tag1.getBinding().equals("otherSignedContract")) {
                                            setEditTextEnabled(et1, tag1, false);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isfreeChoice && flowNodes != null && flowNodes.size() > 0) { //显示流程节点布局
                        showFlowNodes();
                    }
                    setExpression(false, null); //为单元格绑定公式
                    setFormCellsStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取表单评论
     */
    private void getFormComment(String flowId) {
        ProgressDialogHelper.show(context);
        String url = Global.BASE_JAVA_URL + GlobalMethord.标准院表单审批意见;
        JSONObject jo = new JSONObject();
        try {
            jo.put("pageSize", "0");
            jo.put("pageIndex", "0");
            jo.put("workflowId", flowId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    list = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), FormComment.class);
                    if (list != null) {
                        adapter = getCommentAdapter(list);
                        lv_comment.setAdapter(adapter);
                        if (list.size() > 0) {
                            rl_nodata.setVisibility(View.GONE);
                            lv_comment.setVisibility(View.VISIBLE);
                        } else {
                            rl_nodata.setVisibility(View.VISIBLE);
                            lv_comment.setVisibility(View.GONE);
                        }
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


    /**
     * 获取表单的js
     */
    private void getFormJs() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取表单引擎JS代码 + workflowTemplateId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                JSFunction = result;
                LogUtils.e("result","result==>>"+result);
                v8.executeObjectScript("var isMobile = true;");
                try {
                    v8.executeObjectScript(result);
                    v8.executeObjectScript("onload();");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*jsFunctionArrs = JSFunction.split("\n\n");
                if (jsFunctionArrs.length > 0) {
                    for (String functionArr : jsFunctionArrs) {
                        //如果有onload()函数，将onload方法体取出并执行
                        *//*if (functionArr.contains("onload()")) {
                            try {
                                int beginIndex = functionArr.indexOf("{");
                                int endIndex = functionArr.indexOf("}");

                                String str = functionArr.substring(beginIndex + 1, endIndex - 1);
                                String[] split = str.split("\n\t");
                                for (String s : split) {
                                    v8.executeVoidScript(s);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*//*
                        try {
                            String functionName = functionArr.substring(functionArr.indexOf("function ") + 9, functionArr.indexOf("("));
                            String functionBody = functionArr.substring(functionArr.indexOf("{") + 1, functionArr.lastIndexOf("}"));
                            jsFunctionMap.put(functionName, functionBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }*/
            }
        });
    }

    /**
     * 判断表单套打 按钮显示或隐藏
     */
    private void checkModelFileExist() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取表单套打模板 + "?formName=" + formName;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
//                int width = llTitleRight.getWidth();
                mFormOfPlayList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), FormOfPlay.class);
                if (mFormOfPlayList.size() > 0) {
                    if (formDataId != null && formDataId.equals("0")) { //如果是新建表单 不可以套打
                        tv_paper.setVisibility(View.GONE);
                    } else {
                        tv_paper.setVisibility(View.VISIBLE);
//                        if(editable){
//                            width = llTitleRight.getWidth() / 2 * 3;
//                        }
                    }
                    for (FormOfPlay formOfPlay : mFormOfPlayList) {
                        mFormOfPlayListStr.add(formOfPlay.getName());
                    }
                }
                int width = llTitleRight.getWidth();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llTitleLeft.getLayoutParams();
                layoutParams.width = width;
                llTitleLeft.setLayoutParams(layoutParams);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
//                showShortToast(JsonUtils.pareseMessage(result));
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
                AskMeFragment.抄送列表已刷新 = true;
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
    public void setExpression(boolean isAddDetail, List<EditText> mDetailsEdits) {
        if (mEditList != null && mEditList.size() > 0) {
            List<EditText> editTexts = new ArrayList<>();
            if (detailsMap != null && detailsMap.size() > 0) {
                //申请单明细
                Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                    Map<Integer, List<EditText>> map1 = map.getValue();
                    Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                    for (Map.Entry<Integer, List<EditText>> m : entries) {
                        editTexts.addAll(m.getValue());
                    }
                }
            }
            for (int i = 0; i < mEditList.size(); i++) {
                // 遍历带有公式的EditText
                final EditText etExpression = mEditList.get(i);
                CellInfo fieldInfoExpression = (CellInfo) etExpression
                        .getTag();
                String expression = fieldInfoExpression.getExpression();
                if (!TextUtils.isEmpty(expression)) {
//                    expression = expression.toLowerCase();
//                    expression = expression;//.toLowerCase();
                    LogUtils.i("exp ression::::", expression);
                    if (expression.contains("rmb(")) {
                        // 计算人民币大小写转换
                        setMoneyConvert(etExpression, expression, editTexts.size() > 0 ? editTexts : mEditList);
                    } else if (expression.contains("*")
                            || expression.contains("-")
                            || expression.contains("+")
                            || expression.contains("/")) {
                        //解决sum(sumTotalAmounta+sumTotalAmountb) 这样的公式，只保留运算公式的部分
                        if (expression.contains("sum(")) {
                            expression.replace("sum(", "");
                        }
                        setOperatorConvert(etExpression, expression, editTexts.size() > 0 ? editTexts : mEditList);
                    } else if (expression.contains("sum(")) { //绑定两个单元格，一个单元格数据变化，另外一个单元格设置为同样的数据。
                        setSameDataOperator(etExpression, expression, editTexts.size() > 0 ? editTexts : mEditList);
                    }
                }
            }
        }

        //如果是 添加明细,只处理明细里面的公式
        if (isAddDetail && mDetailsEdits != null) {
            bindExpression(mDetailsEdits);
        }
    }


    /**
     * 为单元格绑定表达式
     *
     * @param editTexts
     */
    private void bindExpression(List<EditText> editTexts) {
        for (EditText et : editTexts) {
            CellInfo info = (CellInfo) et.getTag();
            String expression = info.getExpression();
            if (!TextUtils.isEmpty(expression)) {
                expression = expression;//.toLowerCase();
                if (expression.contains("rmb(")) {
                    // 计算人民币大小写转换
                    setMoneyConvert(et, expression, editTexts);
                } else if (expression.contains("*")
                        || expression.contains("-")
                        || expression.contains("+")
                        || expression.contains("/")) {
                    //解决sum(sumTotalAmounta+sumTotalAmountb) 这样的公式，只保留运算公式的部分
                    if (expression.contains("sum(")) {
                        expression.replace("sum(", "");
                    }
                    setOperatorConvert(et, expression, editTexts);
                } else if (expression.contains("sum(")) { //绑定两个单元格，一个单元格数据变化，另外一个单元格设置为同样的数据。
                    setSameDataOperator(et, expression, editTexts);
                }
            }
        }
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
        tv_title.setOnClickListener(v -> showSwitchWebFormPop());
        /**
         * 表单套打
         */
        tv_paper.setOnClickListener(v -> {
            if (mFormOfPlayList.size() > 0) {
                mFormOfPlayPicker.setTitle("");
                mFormOfPlayPicker.show(mFormOfPlayListStr);
                mFormOfPlayPicker.setOnSelectedListener(index -> {
                    ProgressDialogHelper.show(context);
                    saveForm(false, 3, index);
                });
            } else {
                showShortToast("获取套打模板失败");
            }
        });

        iv_back.setOnClickListener(v -> finish());

        /**
         * 审批通过
         */
        tv_agree.setOnClickListener(v -> {
            yiJianType = "yes";
            if (editable) {
                uploadMulipleFile(false, 1);
            } else {
//                    showAuditeDialog(1);
                showshenpi(1);
            }
        });


        /**
         * 审批不同意
         */
        tv_disagree.setOnClickListener(v -> {
            yiJianType = "no";
            if (editable) {
                uploadMulipleFile(false, 1);
            } else {
//                    showAuditeDialog(1);
                showshenpi(1);
            }
        });


        /**
         * 审批拒绝
         */
        tv_refuse.setOnClickListener(v -> {
            if (editable) {  //如果表单可编辑，先保存再审批
//                    saveForm(false, 2);
                uploadMulipleFile(false, 2);
            } else {
//                    showAuditeDialog(2);
                showshenpi(2);
            }
        });


        /**
         *  退回表单
         */
        et_input_examine.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            applyOpinionDialog = new ApplyOpinionDialog();
            Bundle bundle = new Bundle();
            bundle.putString("text", et_input_examine.getText().toString());
            applyOpinionDialog.setArguments(bundle);
            applyOpinionDialog.show(fragmentManager, "applyOpinionDialog");
            applyOpinionDialog.setDismissListener(opinion -> {
                if (!TextUtils.isEmpty(opinion)) {
                    FormInfoActivity.this.opinion = opinion;
                    et_input_examine.setText(opinion);
                }
            });
            /*if (opinionPop == null) {
                initOpinionPop();
                if (!TextUtils.isEmpty(opinion)) {
                    EditText etOpinion = opinionPop.getContentView().findViewById(R.id.et_opinion);
                    etOpinion.setText(opinion);
                }
//                    initOpinionPop();
            } else {
                int[] location = new int[2];
                tv_agree.getLocationOnScreen(location);
                opinionPop.showAtLocation(tv_agree, Gravity.BOTTOM, 0, -location[1]);
                setBackgroundAlpha(0.5f);
                if (!TextUtils.isEmpty(opinion)) {
                    EditText etOpinion = opinionPop.getContentView().findViewById(R.id.et_opinion);
                    etOpinion.setText(opinion);
                }
            }*/
        });


        mNodeIosPicker.setOnSelectedListener(index -> {
            if (index > 0) {
                WorkflowNodeVersion version = nodeVersions.get(index - 1);
                showBackDialog(version.getUuid());
            }
        });


        /**
         * 保存表单
         */
        tv_save.setOnClickListener(v -> {
            /*if (!TextUtils.isEmpty(saveCallBack)) {
                V8Object object = v8.executeObjectScript(saveCallBack);
                if (object.get("Status").toString().equals("1")) {
                    ProgressDialogHelper.show(context, "保存中...");
                    uploadMulipleFile(false, 0);
                } else {
                    showShortToast("保存不了。。。。");
                }
            } else {
                ProgressDialogHelper.show(context, "保存中...");
                uploadMulipleFile(false, 0);
            }*/
            ProgressDialogHelper.show(context, "保存中...");
            uploadMulipleFile(false, 0);
        });


        /**
         * 提交表单
         */
        tv_submit.setOnClickListener(v -> {
//                if (isCanSave()) {
//                    saveForm(true, 0);
            ProgressDialogHelper.show(context, "提交中...");
            uploadMulipleFile(true, 0);
//                }
        });


        //抄送
        tv_copy.setOnClickListener(v -> {
            Intent intent = new Intent(FormInfoActivity.this, SelectedNotifierActivity.class);
            intent.putExtra("isSingleSelect", false);
            intent.putExtra("title", "选择抄送人");
            startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
        });

        attachView.setOnAddImageListener(() -> isAttachView = true);


        /**
         * 存为PDF
         */
        btnSavePdf.setOnClickListener(v -> getFormPdf());

        tvCancelPush.setOnClickListener(v -> cancelPush());

        btn_zhengwen.setOnClickListener(v -> {
            String downUrl = Global.BASE_JAVA_URL + GlobalMethord.显示附件
                    + mainBody;
            String fileName = mainBody + ".doc";
            StringRequest.downloadFile(downUrl, fileName, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    OpenIntentUtils.openFile(context, FilePathConfig.getCacheDirPath() + "/" + fileName);
                }

                @Override
                public void onFailure(Request request, Exception ex) {

                }

                @Override
                public void onResponseCodeErro(String result) {

                }
            });
        });

        indicator.setClickListener(position -> {
            if (position == 0) {
                ll_form.setVisibility(View.VISIBLE);
                ll_form_comment.setVisibility(View.GONE);
                webView.setVisibility(View.GONE);
            } else if (position == 1) {
                ll_form.setVisibility(View.GONE);
                ll_form_comment.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
//                    webView.setVisibility(View.VISIBLE);
//                    String url = Global.BASE_JAVA_URL + GlobalMethord.表单审批意见H5
//                            + "?workflowTemplateId=" + workflowTemplateId + "&workflowId=" + workFlowId;
//                    webView.loadUrl(url);
            } else if (position == 2) {
                ll_form.setVisibility(View.GONE);
                ll_form_comment.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                String url = Global.BASE_JAVA_URL + GlobalMethord.表单检视流转H5
                        + "?checkerNames=" + checkerNames + "&workflowId=" + workFlowId;
                webView.loadUrl(url);
            }
        });

        /**
         * 审批流程
         */
        rg_audite.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 选中状态改变时被触发
                switch (checkedId) {
                    case R.id.rb_agree_form_info:
                        // 审批通过
                        opinion = "同意。";
                        yiJianType = "yes";
                        et_input_examine.setText(opinion);
                        break;
                    case R.id.rb_disagree_form_info:
                        // 审批不通过
                        opinion = "不同意。";
                        yiJianType = "no";
                        et_input_examine.setText(opinion);
                        break;
                }
            }
        });

        ClickUtil.clicks(btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editable) {
                    uploadMulipleFile(false, 1);
                } else {
                    showshenpi(1);
                }

            }
        });


    }

    private void initOpinionPop() {
        View view = View.inflate(this, R.layout.pop_flow_opinion, null);
        EditText etOpinion = view.findViewById(R.id.et_opinion);
        ListView lvOpinion = view.findViewById(R.id.lv_opinion);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSure = view.findViewById(R.id.btn_sure);
        opinionPop = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        opinionPop.setFocusable(true);
        opinionPop.setTouchable(true);
        opinionPop.setOutsideTouchable(false);
        opinionPop.setAnimationStyle(R.style.AnimBottom);
        opinionPop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int[] location = new int[2];
        tv_agree.getLocationOnScreen(location);
        opinionPop.showAtLocation(tv_agree, Gravity.BOTTOM, 0, -location[1]);
        setBackgroundAlpha(0.5f);
        getOpinionList(lvOpinion);
        opinionPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                et_input_examine.setText(opinion);
                setBackgroundAlpha(1.0f);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opinionPop.dismiss();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opinion = et_input_examine.getText().toString().trim();
                opinionPop.dismiss();
            }
        });

        lvOpinion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpinionModel item = (OpinionModel) lvOpinion.getItemAtPosition(position);
//                etOpinion.setText(etOpinion.getText().toString() + item.getCommonOption());
                etOpinion.setText(item.getCommonOption());
            }
        });
    }

    private void getOpinionList(ListView lv) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取审批意见;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<OpinionModel> list = GsonTool.jsonToArrayEntity(
                            JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), OpinionModel.class);
                    lv.setAdapter(getAdapter(list));
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

    private CommanAdapter<OpinionModel> getAdapter(List<OpinionModel> list) {
        return new CommanAdapter<OpinionModel>(list, context, R.layout.item_common) {
            @Override
            public void convert(int position, OpinionModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv, StrUtils.pareseNull(item.getCommonOption()));
            }
        };
    }

    /**
     * 切换webview展示表单的popupwindow
     */
    private void showSwitchWebFormPop() {
        CommonPopupWindow titlePop = new CommonPopupWindow.Builder(context)
                //设置PopupWindow布局
                .setView(R.layout.popup_forminfo)
                //设置宽高
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                .setBackGroundLevel(0.65f)
                //设置PopupWindow里的子View及点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        TextView tvSwitch = view.findViewById(R.id.tv_switch_form);
                        tvSwitch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = Global.BASE_JAVA_URL + GlobalMethord.web表单详情
                                        + "?workflowTemplateId=" + workflowTemplateId + "&id=" + formDataId;
                                Intent intent = new Intent(context, WebviewNormalActivity.class);
                                intent.putExtra(EXTRA_IS_ROOM, true);
                                intent.putExtra(EXTRA_IS_INTERCEPT, false);
                                intent.putExtra(EXTRA_URL, url);
                                intent.putExtra(EXTRA_TITLE, formName);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                })
                //设置外部是否可点击 默认是true
                .setOutsideTouchable(true)
                //开始构建
                .create();
        titlePop.showAsDropDown(iv_title);
    }


    /**
     * 检查需要套打的文件是否是多个
     */
    private void checkPaperIsMuti(String uuid, String fileName) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.表单套打模板下载
                + "?formName=" + formName + "&id=" + formDataId + "&printTemplateId=" + uuid;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                downLoadPaperType(uuid, fileName);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                if ("0".equals(JsonUtils.parseStatus(result))) {
                    List<Attach> attaches = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(result), Attach.class);
                    if (attaches != null && attaches.size() > 0) {
                        List<String> names = new ArrayList<>();
                        for (Attach attach : attaches) {
                            names.add(attach.filename);
                        }
                        mFormOfPlayPicker.setTitle("请选择需要查阅的文件");
                        mFormOfPlayPicker.show(names);

                        mFormOfPlayPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {
                                downLoadPaperType(uuid, attaches.get(index - 1).getUuid(), attaches.get(index - 1).filename);
                            }
                        });
                    }
                } else {
                    downLoadPaperType(uuid, fileName);
                }
            }
        });
    }


    private void downLoadPaperType(String templateId, String fileName) {
        downLoadPaperType(templateId, "", fileName);
    }


    private void calculateLeaveDay() {
        String start = getValue("请假开始时间");
        String end = getValue("请假结束时间");
        String type = getValue("请假类型");

        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
            String startDate = ViewHelper.convertStrToFormatDateStr(start, "yyyy-MM-dd kk:mm:ss");
            String endDate = ViewHelper.convertStrToFormatDateStr(end, "yyyy-MM-dd kk:mm:ss");

            if ("da454fsa555551fsa356wqr4".equals(type)) {
                final int[] results = {0};
                if (startDate.equals(endDate)) {
                    setValue("共计几天", "1");
                } else {
                    long diff = Math.abs(ViewHelper.formatStrToDateAndTime(endDate).getTime()
                            - ViewHelper.formatStrToDateAndTime(startDate).getTime());
                    int day = (int) Math.floor(diff / (1000 * 60 * 60 * 24));
                    if (ViewHelper.formatStrToDateAndTime(endDate).getTime()
                            >= ViewHelper.formatStrToDateAndTime(startDate).getTime()) {
                        final int[] count = {0};
                        for (int i = 0; i <= day; i++) {
                            String tempDate = ViewHelper.getFetureDateStr(startDate, i);

                            String url = Global.BASE_JAVA_URL + GlobalMethord.获取工作日;
                            Map<String, String> map = new HashMap<>();
                            map.put("tempStart", tempDate);
                            StringRequest.postAsyn(url, map, new StringResponseCallBack() {
                                @Override
                                public void onResponse(String response) {
                                    count[0] += 1;
                                    if (TextUtils.isEmpty(StrUtils.removeRex(JsonUtils.pareseData(response)))) {
                                        results[0] += 1;
                                        Logger.e("请假" + results[0] + "天");
                                    }
                                    if (count[0] == day + 1) {
                                        setValue("共计几天", results[0] + "");
                                    }
                                }

                                @Override
                                public void onFailure(Request request, Exception ex) {

                                }

                                @Override
                                public void onResponseCodeErro(String result) {
                                    count[0] += 1;
                                    if (count[0] == day) {
                                        setValue("共计几天", results[0] + "");
                                    }
                                }
                            });
                        }

                    }
                }
            } else {
                int result = 0;
                if (startDate.equals(endDate)) {
                    result = 1;
                } else {
                    long diff = Math.abs(ViewHelper.formatStrToDateAndTime(endDate).getTime()
                            - ViewHelper.formatStrToDateAndTime(startDate).getTime());
                    int day = (int) Math.floor(diff / (1000 * 60 * 60 * 24));
                    result = day + 1;
                }
                setValue("共计几天", result + "");
            }
        }
    }

    /**
     * 下载表单套打模板到手机sd卡
     */
    private void downLoadPaperType(String templateId, String attachId, String fileName) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.表单套打模板下载
                + "?formName=" + formName
                + "&id=" + formDataId
                + "&printTemplateId=" + templateId
                + "&printMethod=download"
                + "&formPrintTemplateId=" + attachId;
        StringRequest.downloadFile(url, fileName, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                OpenIntentUtils.openFile(context,
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName);
                showShortToast("表单套打已下载到文件管理sd卡根目录中");
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
     * 审批
     *
     * @param type 1==通过 2==拒绝
     */
    private void audite(int type, String opinion) {
        Audite audite = new Audite();
        audite.setWorkflowId(workFlowId);
        audite.setOpinion(this.opinion);
        audite.setType(type);

        auditFlow(audite);
    }

    private void auditFlow(Audite audite) {


        if (TextUtils.isEmpty(yiJianType)) {
            Toast.makeText(context, "请选择意见类型!", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(audite.getOpinion())) {
            Toast.makeText(context, "请填写审批意见!", Toast.LENGTH_SHORT).show();
            return;
        }


        audite.setYiJianType(yiJianType);

        ProgressDialogHelper.show(context, "审批中...");
        StringRequest.postAsyn(shenpiUrl, audite, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                String data = JsonUtils.pareseData(response);
                if (!TextUtils.isEmpty(data) && data.contains("成功")) {
                    Toast.makeText(context, "审批成功!", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky("表单审批成功");
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
                List<User> userList;
                try {
                    userList = JsonUtils.jsonToArrayEntity(data, User.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    userList = new ArrayList<>();
                }
                if ("6".equals(status)) {
                    if (userList != null && userList.size() > 0) {
                        isNeedInputAuditeMessage = true;
                        UserList userList1 = new UserList();
                        userList1.setUsers(userList);
                        Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FormInfoActivity.this, SelectedUserListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("auditorList", userList1);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                    } else {
                        Intent intent = new Intent(context, AuditSelectNextNodeActivity.class);
                        intent.putExtra("nodeId", nextStepId);
                        intent.putExtra("workflowId", workFlowId);
                        intent.putExtra("workflowTemplateId", workflowTemplateId);
                        intent.putExtra("yiJianType", yiJianType);
                        intent.putExtra("isAudite", true);
                        startActivityForResult(intent, RESULT_SELECT_NEXT_NODE);
                    }
                } else if (data != null && "提交失败:审核人未找到".equals(data)) {
                    Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FormInfoActivity.this, SelectedNotifierActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_AUDITOR);
                } else {
                    showShortToast(data);
                }
            }
        });
    }


    /**
     * 显示流程节点
     */
    private void showFlowNodes() {
        llNodeRoot.setVisibility(View.VISIBLE);
        for (int i = 0; i < flowNodes.size(); i++) {
            FlowNode node = flowNodes.get(i);
            if (node.getStartNode() || node.getEndNode()) {
                flowNodes.remove(node);
            }
        }
        if (Global.mUser.getUuid().equals(createrId)) {
            flowNodes.add(new FlowNode());
        }
        lvNode.setAdapter(getNodeAdapter());
    }

    private CommanAdapter<FlowNode> getNodeAdapter() {
        return new CommanAdapter<FlowNode>(flowNodes, context, R.layout.item_flow_node) {
            @Override
            public void convert(int position, FlowNode item, BoeryunViewHolder viewHolder) {

                ImageView ivDelete = viewHolder.getView(R.id.iv_delete);
                ImageView ivConnect = viewHolder.getView(R.id.iv_connect);
                ImageView ivHead = viewHolder.getView(R.id.iv_head);
                TextView tvName = viewHolder.getView(R.id.tv_name);

                if (Global.mUser.getUuid().equals(createrId)) {
                    if (position == flowNodes.size() - 1) {  //最后一个，显示为加号
                        ivDelete.setVisibility(View.GONE);
                        tvName.setVisibility(View.GONE);
                        ivConnect.setVisibility(View.GONE);
                        ivHead.setImageResource(R.drawable.ic_add);
                    } else {
                        if (item.getStatus() == 1) { //审批已通过
                            ivDelete.setVisibility(View.GONE);
                        } else {
                            ivDelete.setVisibility(View.VISIBLE);
                        }
                        tvName.setVisibility(View.VISIBLE);
                        ivConnect.setVisibility(View.VISIBLE);
                        viewHolder.setUserPhoto(R.id.iv_head, item.getAuditorId()); //审核节点员工头像
                        tvName.setText(dictionaryHelper.getUserNameById(item.getAuditorId()));
                    }
                } else {
                    if (position == flowNodes.size() - 1) {
                        ivConnect.setVisibility(View.GONE);
                    } else {
                        ivConnect.setVisibility(View.VISIBLE);
                    }
                    ivDelete.setVisibility(View.GONE);
                    tvName.setVisibility(View.VISIBLE);
                    viewHolder.setUserPhoto(R.id.iv_head, item.getAuditorId()); //审核节点员工头像
                    tvName.setText(dictionaryHelper.getUserNameById(item.getAuditorId()));
                }


                /**
                 * 删除当前节点
                 */
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (flowNodes.size() == 2) {
                            com.biaozhunyuan.tianyi.common.view.AlertDialog dialog = new com.biaozhunyuan.tianyi.common.view.AlertDialog(context).builder()
                                    .setTitle("删除节点")
                                    .setMsg("删掉此节点流程将自动设置为完成，是否继续")
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            deleteFreeFlowNode(true, item.getNodeId() + "");
                                        }
                                    })
                                    .setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    });
                            dialog.show();
                        } else {
                            deleteFreeFlowNode(false, item.getNodeId() + "");
                        }

                    }
                });

                ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Global.mUser.getUuid().equals(createrId) && position == flowNodes.size() - 1) { //如果是加号，选择员工
                            Intent intent = new Intent(context, SelectedNotifierActivity.class);
                            intent.putExtra("isSingleSelect", true);
                            intent.putExtra("title", "选择下一步审核人");
                            startActivityForResult(intent, REQUEST_SELECT_NEXT_AUDITOR);
                        }
                    }
                });
            }
        };
    }

    /**
     * 取消表单提醒
     */
    private void cancelPush() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.设置表单不再提醒 + workFlowId;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("设置成功");
                tvCancelPushText.setText("应付推送提醒已取消，不再发送相关通知");
                tvCancelPush.setVisibility(View.GONE);
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
     * 审批意见弹窗
     *
     * @param type
     */
    private void showAuditeDialog(final int type) {
        LayoutInflater factory = LayoutInflater.from(FormInfoActivity.this);//提示框
        final View view = factory.inflate(R.layout.dialog_audite, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//获得输入框对象
        edit.requestFocus();

        new AlertDialog.Builder(FormInfoActivity.this)
                .setTitle("填写审批意见")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String auditeMessage = edit.getText().toString().trim();
                                audite(type, auditeMessage);
                            }
                        }).setNegativeButton("取消", null).create().show();
    }


    /**
     * 删除自由审批流程节点
     *
     * @param nodeId
     */
    private void deleteFreeFlowNode(boolean isClear, String nodeId) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.自由审批删除节点 + "?workflowInstanceId=" + workFlowId + "&nodeId=" + nodeId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("删除成功");
                if (isClear) {
                    llNodeRoot.setVisibility(View.GONE);
                } else {
                    getFreeFlowNode();
                }
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

    /**
     * 添加自由审批流程节点
     *
     * @param auditorIds 审核人
     */
    private void addFreeFlowNode(String auditorIds) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.自由审批添加节点 + "?workflowInstanceId=" + workFlowId + "&auditorIds=" + auditorIds;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("添加成功");
                getFreeFlowNode();
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


    /**
     * 获取自由流程节点数据
     */
    private void getFreeFlowNode() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.流程节点 + "?workflowTemplateId="
                + workflowTemplateId + "&id=" + formDataId + "&workflowId=" + workFlowId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                flowNodes = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), FlowNode.class);
                if (flowNodes != null) {
                    showFlowNodes();
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
     * 退回表单弹出框
     *
     * @param uuid
     */
    private void showBackDialog(final String uuid) {
        LayoutInflater factory = LayoutInflater.from(FormInfoActivity.this);//提示框
        final View view = factory.inflate(R.layout.dialog_audite, null);//这里必须是final的
        final EditText edit = (EditText) view.findViewById(R.id.edit_audite_message);//获得输入框对象
        edit.requestFocus();

        new AlertDialog.Builder(FormInfoActivity.this)
                .setTitle("填写意见")//提示框标题
                .setView(view)
                .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String auditeMessage = edit.getText().toString().trim();
                                backForm(uuid, auditeMessage);
                            }
                        }).setNegativeButton("取消", null).create().show();
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
            LinearLayout parent = (LinearLayout) text.getParent();
            if (parent.getVisibility() == View.GONE) {
                info.setRequired(false);
            }

            // 仅仅对必填项校验
            // 判断用于选择的项，如果不是文本类型并且字典项不为空
            if (("label".equalsIgnoreCase(fieldStyle) || "textbox".equalsIgnoreCase(fieldStyle) || "textarea".equalsIgnoreCase(fieldStyle))) {//&& TextUtils.isEmpty(fieldDict)
                // 判断字典项
                String content = text.getText().toString().trim();
                info.setValue(content);
            }

            //字典类型，如果默认value为0，text为空，判断必填项
            //但是要去除image和signature类型的，因为他们的text为空
            if (!info.getReadOnly() && info.getRequired()) {
                if (TextUtils.isEmpty(info.getValue())) {
                    Toast.makeText(context, info.getLabelText() + "为必填项", Toast.LENGTH_SHORT).show();
                    ProgressDialogHelper.dismiss();
                    return false;
                } else if (!("image".equalsIgnoreCase(fieldStyle)
                        || "signature".equalsIgnoreCase(fieldStyle))
                        && TextUtils.isEmpty(text.getText().toString())) {
                    Toast.makeText(context, info.getLabelText() + "为必填项", Toast.LENGTH_SHORT).show();
                    ProgressDialogHelper.dismiss();
                    return false;
                }
            }
        }

        if (detailsMap.size() > 0) {  //判断明细表必填项

            //申请单明细
            Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
            for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                Map<Integer, List<EditText>> map1 = map.getValue();
                Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                for (Map.Entry<Integer, List<EditText>> m : entries) {
                    List<EditText> editTexts = m.getValue();
                    for (EditText text1 : editTexts) {
                        CellInfo fieldInfo = (CellInfo) text1.getTag();

                        String fieldDict1 = fieldInfo.getDict();
                        String fieldStyle1 = fieldInfo.getCellStyle();
                        if ((("textbox".equalsIgnoreCase(fieldStyle1) || "textarea".equalsIgnoreCase(fieldStyle1)) && TextUtils.isEmpty(fieldDict1))) {
                            // 判断字典项
                            String content = text1.getText().toString().trim();
                            fieldInfo.setValue(content);
                        }


                        //字典类型，如果默认value为0，text为空，判断必填项
                        //但是要去除image和signature类型的，因为他们的text为空
                        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
                            if (TextUtils.isEmpty(fieldInfo.getValue())) {
                                Toast.makeText(context, "明细表" + fieldInfo.getLabelText() + "为必填项", Toast.LENGTH_SHORT).show();
                                ProgressDialogHelper.dismiss();
                                return false;
                            } else if (!("image".equalsIgnoreCase(fieldStyle1)
                                    || "signature".equalsIgnoreCase(fieldStyle1))
                                    && TextUtils.isEmpty(text1.getText().toString())) {
                                Toast.makeText(context, "明细表" + fieldInfo.getLabelText() + "为必填项", Toast.LENGTH_SHORT).show();
                                ProgressDialogHelper.dismiss();
                                return false;
                            }
                        }


                        if (fieldInfo.getCellStyle().equals("datepicker")) {
                            if (!TextUtils.isEmpty(fieldInfo.getMaxDate())) {
                                for (Map.Entry<Integer, List<EditText>> m1 : entries) {
                                    List<EditText> editTexts1 = m1.getValue();
                                    for (EditText text2 : editTexts1) {
                                        CellInfo info1 = (CellInfo) text2.getTag();
                                        if (info1.getBinding().equals(fieldInfo.getMaxDate()) && !TextUtils.isEmpty(info1.getValue())) {
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
        }
        return true;
    }

    public void showshenpi(final int type) {
//        et_input_examine.requestFocus();
//        InputSoftHelper.hideShowSoft(context);
//        InputSoftHelper.hiddenSoftInput(context, et_input_examine);
//        ll_examine.setVisibility(View.VISIBLE);
        if (type == 1) {
            tv_tongyi.setText("同意");
        } else if (type == 2) {
            tv_tongyi.setText("否决");
        }

//        message = et_input_examine.getText().toString().trim();
        audite(type, message);
    }

    public void showshenpi(final String list_ids) {
//        et_input_examine.requestFocus();
//        ll_examine.setVisibility(View.VISIBLE);
//        message = et_input_examine.getText().toString().trim();
        approveForm(workFlowId, message, list_ids);
    }

    /**
     * 保存表单
     *
     * @param isSubmit 是否提交
     * @param isAudite 审批类型 1审批通过，2审批拒绝  不审批可传0
     */
    private void saveForm(final boolean isSubmit, final int isAudite) {
        saveForm(isSubmit, isAudite, -1);
    }

    /**
     * 保存表单
     *
     * @param isSubmit 是否提交
     * @param isAudite 审批类型 1审批通过，2审批拒绝,3仅仅保存 不审批可传0
     * @param index    默认为-1, index>0则判断为表单保存后下载套打文件
     */
    private void saveForm(final boolean isSubmit, final int isAudite, final int index) {

        attachView.uploadImage("apply", new IOnUploadMultipleFileListener() {
            @Override
            public void onStartUpload(int sum) {

            }

            @Override
            public void onProgressUpdate(int completeCount) {

            }

            @Override
            public void onComplete(String attachIds) {
                FormData formData = new FormData();
                List<TabCell> list = new ArrayList<>();

                for (EditText text : mEditList) {
                    TabCell cell = new TabCell();
                    CellInfo fieldInfo = (CellInfo) text.getTag();
                    cell.setFieldName(fieldInfo.getBinding());
                    cell.setFieldType(fieldInfo.getDataType());
                    cell.setFieldValue(fieldInfo.getValue() == null ? "" : fieldInfo.getValue());

                    String fieldDict = fieldInfo.getDict();
                    String fieldStyle = fieldInfo.getCellStyle();


                    if ("bool".equals(fieldInfo.getDataType()) && TextUtils.isEmpty(cell.getFieldValue())) {
                        cell.setFieldValue("false");
                    }
                    // 仅仅对必填项校验
                    // 判断用于选择的项，如果不是文本类型并且字典项不为空
                    if ((("textbox".equalsIgnoreCase(fieldStyle) || "textarea".equalsIgnoreCase(fieldStyle)) && TextUtils.isEmpty(fieldDict))) {
                        // 判断字典项
                        String content = text.getText().toString().trim();
                        cell.setFieldValue(content);
                    }
                    list.add(cell);
                }

                if (!TextUtils.isEmpty(customerId)) {
                    list.add(new TabCell("customerId", "string", customerId));
                }
                list.add(new TabCell("creatorId", "string", createrId));
                list.add(new TabCell("createTime", "date", ViewHelper.getCurrentFullTime()));
                list.add(new TabCell("version", "string", workflowTemplateVersion));
                if (!TextUtils.isEmpty(attachIds)) {
                    list.add(new TabCell("attachmentIds", "string", attachIds));
                }
                //判断是否添加业务员ID
                boolean isAddAdvisor = true;
                for (EditText et : mEditList) {
                    CellInfo info = (CellInfo) et.getTag();
                    if (info.getBinding().equals("advisorId")) {
                        isAddAdvisor = false; //如果主表中已经有业务员id单元格 就不再添加
                        break;
                    }
                }
                if (extentMap != null) {
                    Set<Map.Entry<String, String>> entries = extentMap.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        boolean isHaveSameData = false;
                        for (EditText et : mEditList) {
                            CellInfo info = (CellInfo) et.getTag();
                            if (info.getBinding().equals(entry.getKey())) {
                                isHaveSameData = true;
                            }
                        }
                        if (!isHaveSameData) {
                            list.add(new TabCell(entry.getKey(), "string", entry.getValue()));
                        }
                    }
                }
                if (!TextUtils.isEmpty(advisorId) && isAddAdvisor) {
                    list.add(new TabCell("advisorId", "string", advisorId));
                }

                formData.setFields(list);
                formData.setId(formDataId);
                formData.setForm(formName);
                formData.setTableName(formType);
                formData.setDetailName(detailType);
                formData.setWorkflowTemplateId(workflowTemplateId);
                formData.setVersion(workflowTemplateVersion);


                List<List<FormDetails>> lists = new ArrayList<>();
                if (detailsMap.size() > 0) {  //添加明细信息
                    Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                    for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                        Map<Integer, List<EditText>> map1 = map.getValue();
                        Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();


                        List<FormDetails> formDetailses = new ArrayList<>();
                        int detailCount = 0;
                        for (Map.Entry<Integer, List<EditText>> m : entries) {
                            FormDetails details = new FormDetails();
                            List<TabCell> list1 = new ArrayList<>();
                            details.setId("0");
                            details.setDetailName(detailTitles.get(map.getKey()));
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
                            if (detailIds != null && detailIds.size() > detailCount) {
                                TabCell cell = new TabCell();
                                cell.setFieldName("detailId");
                                cell.setFieldType("String");
                                cell.setFieldValue(detailIds.get(detailCount));
                                list1.add(cell);
                            }
                            detailCount++;
                            details.setFields(list1);
                            formDetailses.add(details);
                        }
                        lists.add(formDetailses);
                    }

                }

                StringRequest.postAsynNoMap(context, saveUrl, formData, lists, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        ProgressDialogHelper.dismiss();
                        if (JsonUtils.parseStatus(response).equals("1")) {
                            if (isAudite == 3) {
                                if (index >= 0) {
                                    String str = mFormOfPlayListStr.get(index);
                                    String fileDirectory = mFormOfPlayList.get(index).getFileDirectory();
                                    String fileType = fileDirectory.substring(fileDirectory.lastIndexOf("."), fileDirectory.length());

                                    checkPaperIsMuti(mFormOfPlayList.get(index).getUuid(), formName + "_" + str + fileType);
                                } else {
                                    showShortToast("表单保存成功");
                                }
                            } else {
                                formDataId = JsonUtils.pareseData(response);
                                if (!TextUtils.isEmpty(formDataId)) {
                                    Toast.makeText(context, "表单保存成功！", Toast.LENGTH_SHORT).show();
                                    if (isSubmit) {
                                        submitForm(formDataId, "", null);
                                    }
                                    if (isAudite == 1) {
                                        showshenpi(1);
                                    }
                                    if (isAudite == 2) {
//                                    showAuditeDialog(2);
                                        showshenpi(2);
                                    }
                                } else {
                                    Toast.makeText(context, "表单保存失败！", Toast.LENGTH_SHORT).show();
                                }
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
     * @param map
     */
    private void submitForm(String formId, String auditors, Map<String, String> map) {


        Map<String, String> paraMap = new HashMap();
        paraMap.put("id", formId);
        paraMap.put("name", formName);
        if (!TextUtils.isEmpty(auditors)) {
            paraMap.put("auditorIds", auditors);
        }
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                paraMap.put(entry.getKey(), entry.getValue());
            }
        }

        StringRequest.postAsyn(submitUrl, paraMap, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                /**
                 * {"Status":"1"} 返回数据格式是这样的，代表普通表单提交成功
                 * {"Status":"1","Data":[]} 返回数据格式是这样的，代表自由审批流程没有下一步审核人，需要选择下一步审核人。
                 */
                try {
                    String data = JsonUtils.getStringValue(response, "Data");
                    List<User> userList = JsonUtils.jsonToArrayEntity(data, User.class);
                    if (userList.size() > 0) {
                        UserList userList1 = new UserList();
                        userList1.setUsers(userList);
                        Toast.makeText(context, "请选择下一位审批人", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FormInfoActivity.this, SelectedUserListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("auditorList", userList1);
                        intent.putExtra("isSingleSelect", true);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_SELECT_FIRST_AUDITOR);
                    } else if ("[]".equals(data)) {
                        showShortToast("请选择下一步审核人");
                        Intent intent = new Intent(context, SelectedNotifierActivity.class);
                        intent.putExtra("isSingleSelect", true);
                        intent.putExtra("title", "选择下一步审核人");
                        startActivityForResult(intent, REQUEST_SELECT_FIRST_AUDITOR);
                    }
                    if (!TextUtils.isEmpty(data) && data.contains("自由选择")) {
                        Intent intent = new Intent(context, AuditSelectNextNodeActivity.class);
                        intent.putExtra("nodeId", nextStepId);
                        intent.putExtra("workflowId", workFlowId);
                        intent.putExtra("formDataId", formDataId);
                        intent.putExtra("workflowTemplateId", workflowTemplateId);
                        intent.putExtra("yiJianType", yiJianType);
                        startActivityForResult(intent, RESULT_SELECT_NEXT_NODE);
                    }
                } catch (JSONException e) { //没有data，提交成功
                    e.printStackTrace();
                    Toast.makeText(context, "表单提交成功！", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().postSticky("表单提交成功！");
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
                try {
                    showShortToast(JsonUtils.getStringValue(result, "Data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 生成主表相应的UI界面,并返回FieldInfo对象的字段名,字段值(字段值是EditText类型),字段类型的集合
     */

    private void createUI(List<CellInfo> mFields, LinearLayout ll_root,
                          int index, LinearLayout lin) {
        boolean isDetails = false; //是否是表单明细
        if (!ll_root.equals(this.ll_root)) {
            isDetails = true;
        }

        if (!TextUtils.isEmpty(手签章) || !TextUtils.isEmpty(其他)) {
            int fieldIndex = mFields.size();
            for (CellInfo mField : mFields) {
                if ("印章种类".equals(mField.getLabelText())) {
                    fieldIndex = mFields.indexOf(mField);
                    break;
                }
            }
            if (!TextUtils.isEmpty(其他)) {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setDataType("String");
                cellInfo.setCellStyle("textbox");
                cellInfo.setText(其他);
                cellInfo.setLabelText("其他");
                cellInfo.setBinding("其他");
                cellInfo.setReadOnly(true);
                cellInfo.setHidden(false);
                cellInfo.setRequired(false);
                mFields.add(fieldIndex + 1, cellInfo);
            }
            if (!TextUtils.isEmpty(手签章)) {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setDataType("String");
                cellInfo.setCellStyle("textbox");
                cellInfo.setText(手签章);
                cellInfo.setLabelText("手签章");
                cellInfo.setBinding("手签章");
                cellInfo.setReadOnly(true);
                cellInfo.setHidden(false);
                cellInfo.setRequired(false);
                mFields.add(fieldIndex + 1, cellInfo);
            }
        }

        // 将dp转化为px
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = (int) (120 * scale + 0.5f);
        int height = (int) (35 * scale + 0.5f);
        int leftPadding = (int) (5 * scale + 0.5f);
        ll_root.setVisibility(View.VISIBLE);
        if (mFields == null || mFields.size() < 0) {
            return;
        }

        // 生成相应的UI界面
        for (int i = 0; i < mFields.size(); i++) {
            String fieldStyle = mFields.get(i).getCellStyle();
            String fieldName = mFields.get(i).getLabelText();
            if (!TextUtils.isEmpty(fieldStyle) //&& !fieldName.contains("附件")
                    && !TextUtils.isEmpty(fieldName)) {
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
                boolean isVisible = false;//是否隐藏
                if (invisibleField != null && invisibleField.length > 0 && !isDetails) { //如果当前单元格不是明细表,并且有需要只读改为可编辑单元格
                    for (int a = 0; a < invisibleField.length; a++) {
                        if (fieldInfo.getBinding().equals(invisibleField[a])) {
                            isVisible = true; //需要隐藏
                        }
                    }
                }
                linearLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                boolean isEditable = true;//是否只读
                if (editableField != null && editableField.length > 0 && !isDetails) { //如果当前单元格不是明细表,并且有需要只读改为可编辑单元格
                    for (int a = 0; a < editableField.length; a++) {
                        if (fieldInfo.getBinding().equals(editableField[a])) {
                            isEditable = false; //需要只读改为可编辑
                        }
                    }
                }
                if (fieldInfo.getReadOnly() && isEditable) {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.chat_back));
                } else {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                }
                // 添加左边提示文字
                addTextView(width, height, leftPadding, fieldInfo, linearLayout);
                if (("textbox".equalsIgnoreCase(fieldStyle))
                        && TextUtils.isEmpty(dict)) {
                    addEditTextView(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                } else if ("combobox".equalsIgnoreCase(fieldStyle)
                        || "dropdownlist".equalsIgnoreCase(fieldStyle)) {
                    addComboxView(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                } else if ("datepicker".equalsIgnoreCase(fieldStyle)) {
                    addDateTimeEditView(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                } else if ("checkbox".equalsIgnoreCase(fieldStyle)
                        || "radiolist".equalsIgnoreCase(fieldStyle)) {
                    addCheckedBox(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                } else if ("checklistbox".equalsIgnoreCase(fieldStyle)) {
                    addComboxListView(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                } else if ("multcombobox".equals(fieldStyle)) {
                    addMutiComobox(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                } else if ("signature".equalsIgnoreCase(fieldStyle)) {
                    addSignatureView(fieldInfo, isDetails, index, linearLayout, params, isEditable);
                } else if ("image".equalsIgnoreCase(fieldStyle)) {
                    addMultiImageView(fieldInfo, linearLayout, isDetails, index, params, lin, isEditable);
                } else if ("file".equalsIgnoreCase(fieldStyle)) {
                    addMultiImageView(fieldInfo, linearLayout, isDetails, index, params, lin, isEditable);
                } else {
                    addOtherEditView(fieldInfo, linearLayout, params, isDetails, index, lin, isEditable);
                }

                allCellMap.put(fieldInfo, linearLayout);
                ll_root.addView(linearLayout);
                addHorionzalLine(ll_root);
            }
        }
    }


    /**
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param isDetails    是否是表单明细行
     * @param index
     * @param ll_root
     */
    private void addEditTextView(final CellInfo fieldInfo,
                                 LinearLayout linearLayout, ViewGroup.LayoutParams params,
                                 boolean isDetails, int index, final LinearLayout ll_root, final boolean isEditable) {
        final String fieldValue = fieldInfo.getText();
        String defalutValue = fieldInfo.getDefaultValue();

        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        setEditEnable(fieldInfo, editText, isEditable);
//        setHiddenFields(fieldInfo, editText);
        if (!TextUtils.isEmpty(fieldInfo.getBinding()) && "totaldays".equalsIgnoreCase(fieldInfo.getBinding())) { //显示总天数的编辑框
            etTotalDays = editText;
        }

        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                fieldInfo.setValue("0");
                editText.addTextChangedListener(new TextWatcher() {
                    int selectionStart;
                    int selectionEnd;
                    CharSequence temp;

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // TODO Auto-generated method stub
                        temp = s;

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (!TextUtils.isEmpty(s.toString().trim())) {
                            selectionStart = editText.getSelectionStart();
                            selectionEnd = editText.getSelectionEnd();
                            if (DoubleUtil.isNumeric(editText.getText().toString()) &&
                                    !ViewHelper.isOnlyPointNumber(editText.getText().toString())) {
                                if (selectionStart == 0) { //表单联动显示的小数
                                    String str = MoneyUtils.formatFloatNumberNoSplit(
                                            Double.valueOf(editText.getText().toString()), true);
                                    editText.setText(str);
                                } else {
                                    showShortToast("您输入的数字保留在小数点后两位");
                                    //删除多余输入的字（不会显示出来）
                                    s.delete(selectionStart - 1, selectionEnd);
                                    editText.setText(s);
                                }
                            }
                        }
                    }
                });
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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;
                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                    v8.executeScript(js);
                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                        v8.executeScript(fieldInfo.getOnChange() + "();");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!TextUtils.isEmpty(fieldInfo.getOnClick())) {
                        String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                        v8.executeScript(js);
                        try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                            v8.executeScript(fieldInfo.getOnClick() + "();");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(fieldInfo.getOnClick())) {
                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                    v8.executeScript(js);
                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                        v8.executeScript(fieldInfo.getOnClick() + "();");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


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
        if (fieldInfo.getBinding().equals("code")) {
            businessCode = fieldInfo.getValue();
        }
        if (fieldInfo.getBinding().equals("travelAmount") && formName.equals("差旅费报销单") && isDetails) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    double amount = 0;
                    List<EditText> editTexts = detailsMap.get(ll_root).get(index);
                    for (EditText et : editTexts) {
                        CellInfo tag = (CellInfo) et.getTag();
                        if (tag.getBinding().equals("trafficTools") && !TextUtils.isEmpty(s1)) {
                            double d = Double.parseDouble(s1);
                            String s2 = et.getText().toString();
                            if (s2.equals("航空")) {
                                amount = (d + 50) / (1 + 0.09) * 0.09;
                            } else if (s2.equals("铁路")) {
                                amount = d / (1 + 0.09) * 0.09;
                            } else if (s2.equals("公路水路")) {
                                amount = d / (1 + 0.03) * 0.03;
                            }
                        } else if (tag.getBinding().equals("taxAmount")) {
                            if (!TextUtils.isEmpty(s1)) {
                                et.setText(ViewHelper.stringToDouble(amount + "", "00"));
                            } else {
                                et.setText("");
                            }
                        }
                    }
                }
            });
        }


        if (fieldInfo.getBinding().equals("projectName") && formName.equals("项目报备申请")) {
            editText.setTextColor(context.getResources().getColor(
                    R.color.color_theme));
            editText.setCursorVisible(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setEnabled(true);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BusinessViewerActivity.class);
                    intent.putExtra("code", businessCode);
                    startActivity(intent);
                }
            });
        }
        if (fieldInfo.getBinding().equals("assetName") && formName.equals("支出凭证")) {
            setEditTextEnabled(editText);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDetail = index;
                    detailsLinear = ll_root;
                    String url1 = Global.BASE_JAVA_URL +
                            "crm/purchase/getAssetPurchaseFormDetailData?departmentId=" + departmentId;
                    Intent intent = new Intent(context, FormSelectPurchaseActivity.class);
                    intent.putExtra("url", url1);
                    startActivityForResult(intent, REQUEST_SELECT_PURCHASE);
                }
            });

        }
        if (isDetails) {
            if (fieldInfo.getBinding().equals("单价") && formName.equals("订单")) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String s1 = s.toString();
                        if (!TextUtils.isEmpty(s1)) {
                            Double 小计 = 0.0;
                            Double 单价 = Double.valueOf(s1);

                            Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                            for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                Map<Integer, List<EditText>> map1 = map.getValue();
                                Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                if (ll_root == map.getKey()) {
                                    for (Map.Entry<Integer, List<EditText>> m : entries) {
                                        if (index == m.getKey()) {
                                            List<EditText> editTexts = m.getValue();
                                            for (EditText et : editTexts) {
                                                CellInfo tag = (CellInfo) et.getTag();
                                                if (tag.getBinding().equals("数量")) {
                                                    String s2 = et.getText().toString();
                                                    if (!TextUtils.isEmpty(s2)) {
                                                        Double 数量 = Double.valueOf(s2);
                                                        小计 = ViewHelper.mul(单价, 数量);
                                                    }
                                                }
                                                if (tag.getBinding().equals("小计")) {
                                                    if (!TextUtils.isEmpty(小计 + "")) {
                                                        et.setText(小计 + "");
                                                        tag.setValue(小计 + "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        if (isDetails) {
            if (fieldInfo.getBinding().equals("数量") && formName.equals("订单")) {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String s1 = s.toString();
                        if (!TextUtils.isEmpty(s1)) {
                            Double 小计 = 0.0;
                            Double 数量 = Double.valueOf(s1);

                            //申请单明细
                            Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                            for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                Map<Integer, List<EditText>> map1 = map.getValue();
                                Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                if (ll_root == map.getKey()) {
                                    for (Map.Entry<Integer, List<EditText>> m : entries) {
                                        if (index == m.getKey()) {
                                            List<EditText> editTexts = m.getValue();
                                            for (EditText et : editTexts) {
                                                CellInfo tag = (CellInfo) et.getTag();
                                                if (tag.getBinding().equals("单价")) {
                                                    String s2 = et.getText().toString();
                                                    if (!TextUtils.isEmpty(s2)) {
                                                        Double 单价 = Double.valueOf(s2);
                                                        小计 = ViewHelper.mul(单价, 数量);
                                                    }
                                                }
                                                if (tag.getBinding().equals("小计")) {
                                                    if (!TextUtils.isEmpty(小计 + "")) {
                                                        et.setText(小计 + "");
                                                        tag.setValue(小计 + "");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
        if (!fieldInfo.getReadOnly() && (formName.equals("工程差旅报销单") || formName.equals("销售差旅报销单"))
                && fieldInfo.getBinding().equals("travelingReason")) {
            setEditTextEnabled(editText);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDetail = index;
                    detailsLinear = ll_root;
                    Intent intent = new Intent();
                    intent.putExtra("projectId", projectId);
                    intent.setClass(context, FormSelectProjectNumberActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_SELL_FORMNUMBER);
                }
            });
        }

        if (isDetails && !fieldInfo.getReadOnly() && (formName.equals("维修工单") || formName.equals("维保工单"))
                && fieldInfo.getBinding().equals("productName")) {
            setEditTextEnabled(editText);
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDetail = index;
                    detailsLinear = ll_root;
                    startActivityForResult(new Intent(context, FormSelectPartActivity.class), REQUEST_SELECT_PART);
                }
            });
        }

        if (formName.equals("维修工单") && fieldInfo.getBinding().equals("quantity")) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    if (!TextUtils.isEmpty(s1)) {
                        int i = Integer.parseInt(s1);
                        if (partQtyRepertory < i) {
                            editText.setText("");
                            showShortToast("当前库存数量:" + partQtyRepertory);
                        } else {
                            quantityNumber = Integer.parseInt(s1);
                        }
                    }
                }
            });
        }
        if (formName.equals("维修工单") && fieldInfo.getBinding().equals("actualUsed")) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    if (!TextUtils.isEmpty(s1)) {
                        int i = Integer.parseInt(s1);
                        if (quantityNumber < i) {
                            editText.setText("");
                            showShortToast("不能大于领用数量");
                        }
                    }
                }
            });
        }

        if ((formName.equals("采购平台合同") || formName.equals("维修服务合同") || formName.equals("维保合同") || formName.equals("备件销售合同")) && fieldInfo.getBinding().equals("qualityAssuredAmount")) {
            addRequired(editText, "assuredCollectionDate");
        }


        if (isDetails) {
            getLoadRelatedDataDetail(fieldInfo, ll_root, index);
        } else {
            getLoadRelatedData(fieldInfo);
        }

        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }

    /**
     * 修改必填项
     */
    private void addRequired(EditText et, String binding) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (EditText et : mEditList) {
                    CellInfo tag = (CellInfo) et.getTag();
                    if (tag.getBinding().equals(binding)) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            tag.setRequired(true);
                        } else {
                            tag.setRequired(false);
                        }
                    }
                }

            }
        });
    }


    /**
     * 添加TextView控件
     */
    private void addTextView(int width, int height, int leftPadding,
                             CellInfo fieldInfo, LinearLayout linearLayout) {
        String fieldName = fieldInfo.getLabelText();

        TextView tvXing = new TextView(context);
        tvXing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvXing.setTextColor(context.getResources().getColor(R.color.color_red));
        tvXing.setText("*");

        TextView textView = new TextView(context);
//        textView.setWidth(width);
//        textView.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        textView.setMinHeight(height);
//        textView.setMinimumWidth(height);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setText(fieldName);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                width, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);

        if (fieldInfo.getRequired()) {  //如果是必填，加红色星号
            linearLayout.addView(tvXing);
        }
        linearLayout.addView(textView);
    }


    /**
     * @param origFieldInfo 一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout  用于生成表单的父布局
     * @param params
     * @param isDetails
     * @param index
     * @param ll_root
     */
    private void addComboxView(final CellInfo origFieldInfo,
                               final LinearLayout linearLayout, ViewGroup.LayoutParams params,
                               final boolean isDetails, int index, final LinearLayout ll_root, final boolean isEditable) {
        final CellInfo fieldInfo;

        if (isDetails) {
            fieldInfo = new CellInfo();
            for (Field field : fieldInfo.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    field.set(fieldInfo, field.get(origFieldInfo));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            fieldInfo = origFieldInfo;
        }
        final String fieldValue = fieldInfo.getValue();
        // new一个线性布局
        final EditText editText = new EditText(context);

        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));

        if (fieldInfo.getDataType().equals("int")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (fieldInfo.getDataType().equals("double")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText, isEditable);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("点击选择");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        String defaultValue = fieldInfo.getDefaultValue();
        final String dict = fieldInfo.getDict();
        final String remoteUrl = fieldInfo.getRemoteurl();

        boolean readOnly = fieldInfo.getReadOnly();
        if (!readOnly) {
            editText.setEnabled(false);
        }
//        fieldInfo.getDict().split(",");

        //先获取dict.DisplayMemberPath
        // 看字典中是否有值，如果没有字典，获取dict
        Map<String, String> dictHashMap;
        if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
            dictHashMap = mDictionaries.get(fieldInfo.getDict()
                    + "." + fieldInfo.getDisplayMemberPath());
            if (dictHashMap == null
                    || (dictHashMap != null && dictHashMap.size() == 0)) {
                dictHashMap = mDictionaries
                        .get(fieldInfo.getDict());
            }
        } else {
            dictHashMap = mDictionaries
                    .get(fieldInfo.getDict());
        }

        if (fieldInfo.getBinding().equals("productIdNew")) {
            productId = fieldValue;
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

        if ("colorNumber".equals(fieldInfo.getBinding()) && "1".equals(fieldInfo.getValue())) {
            editText.setText("无色号");
        }

        if (!readOnly) {
            editText.setEnabled(true); //


            if (isPrivinceType(fieldInfo.getLabelText())) {


                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Map<String, String> dictHashMap;
                        if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
                            dictHashMap = mDictionaries.get(fieldInfo.getDict()
                                    + "." + fieldInfo.getDisplayMemberPath());
                            if (dictHashMap == null
                                    || (dictHashMap != null && dictHashMap.size() == 0)) {
                                dictHashMap = mDictionaries
                                        .get(fieldInfo.getDict());
                            }
                        } else {
                            dictHashMap = mDictionaries
                                    .get(fieldInfo.getDict());
                        }


                        final List<ReturnDict> returnDicts = new ArrayList<>();

                        if (dictHashMap != null) {
                            for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                                ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
                                returnDicts.add(dict1);
                            }
                        }
                        // 取消软键盘
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                        if (!TextUtils.isEmpty(remoteUrl)) {
                            dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
                            dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
                                @Override
                                public void onSelected(字典 dict) {
                                    controlCellStatus(fieldInfo, dict.uuid, true);
                                    fieldInfo.setValue(dict.uuid);
                                    editText.setText(dict.getName() + "");
                                    //处理表单联动
                                    if (isDetails) {
                                        getLoadRelatedDataDetail(fieldInfo, ll_root, index);
                                    } else {
                                        getLoadRelatedData(fieldInfo);
                                    }
                                    if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                        String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                        v8.executeScript(js);
                                        try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                            v8.executeScript(fieldInfo.getOnChange() + "();");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        } else {
                            if (returnDicts.size() > 0) {
                                if (formName.equals("订单") && fieldInfo.getBinding().equals("contractId")) {
                                    mDictIosPicker.show(dict, true, "projectId='" + projectId + "'", "contractNumber");
                                    mDictIosPicker.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                        @Override
                                        public void onSelectedDict(字典 dict) {
                                            ReturnDict rd = new ReturnDict(dict.getUuid(), dict.getName());
                                            String url = Global.BASE_JAVA_URL + GlobalMethord.莱恩斯合同表单数据 + rd.value;
                                            StringRequest.getAsyn(url, new StringResponseCallBack() {
                                                @Override
                                                public void onResponse(String response) {
                                                    String data = JsonUtils.pareseData(response);
                                                    try {
                                                        String receivePerson = JsonUtils.getStringValue(data, "consigneeName");
                                                        String receiveMobile = JsonUtils.getStringValue(data, "consigneeMobile");
                                                        String order_text_6 = JsonUtils.getStringValue(data, "payMenthod");
                                                        for (EditText et : mEditList) {
                                                            CellInfo tag = (CellInfo) et.getTag();
                                                            if (tag.getBinding().equals("receivePerson")) {
                                                                et.setText(receivePerson);
                                                                tag.setValue(receivePerson);
                                                            }
                                                            if (tag.getBinding().equals("receiveMobile")) {
                                                                et.setText(receiveMobile);
                                                                tag.setValue(receiveMobile);
                                                            }
                                                            if (tag.getBinding().equals("order_text_6")) {
                                                                Map<String, String> dictHashMap = mDictionaries
                                                                        .get(tag.getDict());

                                                                final List<ReturnDict> returnDicts = new ArrayList<>();

                                                                if (dictHashMap != null) {
                                                                    for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                                                                        ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
                                                                        returnDicts.add(dict1);
                                                                        if (dict1.value.equals(order_text_6)) {
                                                                            et.setText(dict1.text);
                                                                            tag.setValue(order_text_6);
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    setEdittextTag(editText, ll_root, rd, fieldInfo, isDetails, index);
                                                }

                                                @Override
                                                public void onFailure(Request request, Exception ex) {

                                                }

                                                @Override
                                                public void onResponseCodeErro(String result) {

                                                }
                                            });
                                        }
                                    });
                                } else if (fieldInfo.getBinding().equals("仓库")) {
                                    if (!TextUtils.isEmpty(remoteUrl)) {
                                        dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
                                    } else if (!TextUtils.isEmpty(fieldInfo.getDict())) {
                                        dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
                                    }
                                    dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
                                        @Override
                                        public void onSelected(字典 dict) {
                                            fieldInfo.setValue(dict.uuid);
                                            editText.setText(dict.getName() + "");
                                        }
                                    });
                                } else {
                                    Map<String, String> map = new HashMap<>();
                                    if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
                                        map = mDictionaries.get(fieldInfo.getDict() + "." + fieldInfo.getDisplayMemberPath());
                                        if (map == null || map.size() <= 0) {
                                            map = mDictionaries.get(fieldInfo.getDict());
                                        }
                                    } else {
                                        map = mDictionaries.get(fieldInfo.getDict());
                                    }
                                    dictionaryQueryDialog.show(fieldInfo.getDict(), map);
                                }
                                dictionaryQueryDialog
                                        .setOnSelectedListener(new DictionaryQueryDialog.OnSelectedListener() {
                                            @Override
                                            public void onSelected(final ReturnDict dict) {
                                                setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
                                                if (((formName.equals("订单") && fieldInfo.getBinding().equals("productId")) ||
                                                        formName.equals("特价申请") && fieldInfo.getBinding().equals("productIdNew")) && isDetails) {
                                                    productId = dict.value;
                                                    String specialUrl = Global.BASE_JAVA_URL +
                                                            "crm/order/getSpecialPriceInfo?productId=" + productId + "&projectId=" + projectId;
                                                    StringRequest.getAsyn(specialUrl, new StringResponseCallBack() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            String data = JsonUtils.pareseData(response);
                                                            if (!TextUtils.isEmpty(data)) { //不为空说明该产品没有走过特价申请
                                                                try {
                                                                    String productName = JsonUtils.getStringValue(data, "productName");
                                                                    String specificationType = JsonUtils.getStringValue(data, "specificationType");
                                                                    String discountPrice = JsonUtils.getStringValue(data, "discountPrice");

                                                                    //申请单明细
                                                                    Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                                                                    for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                                                        Map<Integer, List<EditText>> map1 = map.getValue();
                                                                        Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                                                        if (ll_root == map.getKey()) {
                                                                            for (Map.Entry<Integer, List<EditText>> m : entries) {
                                                                                if (index == m.getKey()) {
                                                                                    List<EditText> editTexts = m.getValue();
                                                                                    for (EditText et : editTexts) {
                                                                                        CellInfo tag = (CellInfo) et.getTag();
                                                                                        if (tag.getBinding().equals("productName")) {
                                                                                            et.setText(productName);
                                                                                            tag.setValue(productName);
                                                                                        } else if (tag.getBinding().equals("specificationType")) {
                                                                                            et.setText(specificationType);
                                                                                            tag.setValue(specificationType);
                                                                                        } else if (tag.getBinding().equals("price")) {
                                                                                            et.setText(discountPrice);
                                                                                            tag.setValue(discountPrice);
                                                                                        } else if (tag.getBinding().equals("colorNumber")) {
                                                                                            et.setText("");
                                                                                            tag.setValue("");
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                StringRequest.getAsyn(Global.BASE_JAVA_URL + "psi/sku/getSkuByProductId?productId=" + productId, new StringResponseCallBack() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        String data1 = JsonUtils.pareseData(response);
                                                                        try {
                                                                            String name = JsonUtils.getStringValue(data1, "name");
                                                                            String price = JsonUtils.getStringValue(data1, "price");
                                                                            String itemstyle = JsonUtils.getStringValue(data1, "itemstyle");

                                                                            //申请单明细
                                                                            Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                                                                            for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                                                                Map<Integer, List<EditText>> map1 = map.getValue();
                                                                                Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                                                                if (ll_root == map.getKey()) {
                                                                                    for (Map.Entry<Integer, List<EditText>> m : entries) {
                                                                                        if (index == m.getKey()) {
                                                                                            List<EditText> editTexts = m.getValue();
                                                                                            for (EditText et : editTexts) {
                                                                                                CellInfo tag = (CellInfo) et.getTag();
                                                                                                if (tag.getBinding().equals("productName")) {
                                                                                                    et.setText(name);
                                                                                                    tag.setValue(name);
                                                                                                } else if (tag.getBinding().equals("specificationType")) {
                                                                                                    et.setText(itemstyle);
                                                                                                    tag.setValue(itemstyle);
                                                                                                } else if (tag.getBinding().equals("price")) {
                                                                                                    et.setText(price);
                                                                                                    tag.setValue(price);
                                                                                                } else if (tag.getBinding().equals("lionsAddOrder")) {
                                                                                                    et.setText("");
                                                                                                    tag.setValue("");
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    break;
                                                                                }
                                                                            }
                                                                        } catch (JSONException e1) {
                                                                            e1.printStackTrace();
                                                                            setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
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

                                                        @Override
                                                        public void onFailure(Request request, Exception ex) {

                                                        }

                                                        @Override
                                                        public void onResponseCodeErro(String result) {

                                                        }
                                                    });
                                                } else {
                                                    setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
                                                }

                                                currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;
                                                if ("请假类型".equals(fieldInfo.getBinding())) {
                                                    calculateLeaveDay();
                                                }
                                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                                    v8.executeScript(js);
                                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                                        v8.executeScript(fieldInfo.getOnChange() + "();");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                if (formName.equals("特价申请") && fieldInfo.getBinding().equals("colorNumber")) {
                                    try {
                                        JSONObject jo = new JSONObject();
                                        jo.put("projectId", projectId);
                                        jo.put("product", productId);
                                        StringRequest.postAsyn(Global.BASE_JAVA_URL +
                                                "crm/crm/project/getSampleColorNumber", jo, new StringResponseCallBack() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    String data = JsonUtils.getStringValue(response, "Data");
                                                    if (!TextUtils.isEmpty(data)) {
                                                        final List<字典> 字典s = JsonUtils.jsonToArrayEntity(data, 字典.class);
                                                        final List<String> ls = new ArrayList<>();
                                                        if (字典s.size() > 0) {
                                                            for (int i = 0; i < 字典s.size(); i++) {
                                                                ls.add(字典s.get(i).getName());
                                                            }
                                                        }
                                                        if (ls.size() > 0) {
                                                            mDictIosPicker.show(ls);
                                                            mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                                                @Override
                                                                public void onSelected(int index) {
                                                                    editText.setText(字典s.get(index).getName());
                                                                    fieldInfo.setValue(字典s.get(index).getUuid());
                                                                }
                                                            });
                                                        } else {
                                                            showShortToast("没有可使用的色号/追单");
                                                        }
                                                    } else {
                                                        showShortToast("没有可使用的色号/追单");
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
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (fieldInfo.getBinding().equals("lionsAddOrder") && formName.equals("订单")) {
                                    try {
                                        JSONObject jo = new JSONObject();
                                        jo.put("projectId", projectId);
                                        jo.put("product", productId);
                                        StringRequest.postAsyn(Global.BASE_JAVA_URL +
                                                "crm/crm/project/getSpecialPriceColorNumber", jo, new StringResponseCallBack() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    String data = JsonUtils.getStringValue(response, "Data");
                                                    if (!TextUtils.isEmpty(data)) {
                                                        final List<字典> 字典s = JsonUtils.jsonToArrayEntity(data, 字典.class);
                                                        final List<String> ls = new ArrayList<>();
                                                        if (字典s.size() > 0) {
                                                            for (int i = 0; i < 字典s.size(); i++) {
                                                                ls.add(字典s.get(i).getName());
                                                            }
                                                        }
                                                        if (ls.size() > 0) {
                                                            mDictIosPicker.show(ls);
                                                            mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                                                @Override
                                                                public void onSelected(int index) {
                                                                    editText.setText(字典s.get(index).getName());
                                                                    fieldInfo.setValue(字典s.get(index).getUuid());
                                                                }
                                                            });
                                                        } else {
                                                            showShortToast("没有可使用的色号/追单");
                                                        }
                                                    } else {
                                                        showShortToast("没有可使用的色号/追单");
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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (fieldInfo.getBinding().equals("仓库")) {
                                    if (!TextUtils.isEmpty(remoteUrl)) {
                                        dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
                                    } else if (!TextUtils.isEmpty(dict)) {
                                        dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
                                    }
                                    dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
                                        @Override
                                        public void onSelected(字典 dict) {
                                            fieldInfo.setValue(dict.uuid);
                                            editText.setText(dict.getName() + "");
                                        }
                                    });
                                } else {

                                    if (!TextUtils.isEmpty(dict)) {

                                        currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;

                                        if (!TextUtils.isEmpty(fieldInfo.getParentFieldName())) {

                                            String parentId = "";

                                            if (fieldInfo.getLabelText().equals("市") && fieldInfo.getBinding().equals("市")) {
                                                parentId = privinceValue;
                                            } else if (fieldInfo.getLabelText().equals("县") && fieldInfo.getBinding().equals("县")) {
                                                parentId = cityValue;
                                            } else if (fieldInfo.getLabelText().equals("市") && fieldInfo.getBinding().equals("市2")) {
                                                parentId = privinceValue_back;
                                            } else if (fieldInfo.getLabelText().equals("县") && fieldInfo.getBinding().equals("县2")) {
                                                parentId = cityValue_back;
                                            }

                                            mDictIosPicker.show(dict, true, "parent='" + parentId + "'", "");
                                        } else {
                                            mDictIosPicker.show(dict, true, fieldInfo.getFilter(), "");
                                        }

                                        mDictIosPicker.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                            @Override
                                            public void onSelectedDict(字典 dict) {

                                                controlCellStatus(fieldInfo, dict.uuid, true);
                                                fieldInfo.setValue(dict.uuid);
                                                editText.setText(dict.getName() + "");


                                                String detailName = "";

                                                if (formName.contains("请假")) {
                                                    detailName += "请假地点";
                                                } else if (formName.contains("出差")) {
                                                    detailName += "出差地点";
                                                } else if (formName.contains("疫情防控情况报备表")) {
                                                    detailName += "疫情防控地点";
                                                }

                                                if (fieldInfo.getBinding().equals("省") || fieldInfo.getBinding().equals("市")) {
                                                    detailName += "明细";

                                                } else if (fieldInfo.getBinding().equals("省2") || fieldInfo.getBinding().equals("市2")) {
                                                    detailName += "明细1";
                                                }


                                                if (fieldInfo.getLabelText().equals("省") && fieldInfo.getBinding().equals("省")) {
                                                    privinceValue = dict.uuid;
                                                    setValueDetail("市", "", 1, detailName);
                                                    setValueDetail("县", "", 1, detailName);

                                                } else if (fieldInfo.getLabelText().equals("市") && fieldInfo.getBinding().equals("市")) {
                                                    cityValue = dict.uuid;
                                                    setValueDetail("县", "", 1, detailName);


                                                } else if (fieldInfo.getLabelText().equals("省") && fieldInfo.getBinding().equals("省2")) {
                                                    privinceValue_back = dict.uuid;
                                                    setValueDetail("市2", "", 1, detailName);
                                                    setValueDetail("县2", "", 1, detailName);


                                                } else if (fieldInfo.getLabelText().equals("市") && fieldInfo.getBinding().equals("市2")) {
                                                    cityValue_back = dict.uuid;
                                                    setValueDetail("县2", "", 1, detailName);

                                                }


                                                //处理表单联动
                                                if (isDetails) {
                                                    getLoadRelatedDataDetail(fieldInfo, ll_root, index);
                                                } else {
                                                    getLoadRelatedData(fieldInfo);
                                                }
                                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                                    v8.executeScript(js);
                                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                                        v8.executeScript(fieldInfo.getOnChange() + "();");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }


                                            }
                                        });

//                                        dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
//                                        dictionaryQueryDialogHelper.setOnSelectedListener1(new DictionaryQueryDialogHelper.OnSelectedListener1() {
//                                            @Override
//                                            public void onSelected1(字典 dict) {
//                                                controlCellStatus(fieldInfo, dict.uuid, true);
//                                                fieldInfo.setValue(dict.uuid);
//                                                editText.setText(dict.getName() + "");
//                                                //处理表单联动
//                                                if (isDetails) {
//                                                    getLoadRelatedDataDetail(fieldInfo, ll_root, index);
//                                                } else {
//                                                    getLoadRelatedData(fieldInfo);
//                                                }
//                                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
//                                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
//                                                    v8.executeScript(js);
//                                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
//                                                        v8.executeScript(fieldInfo.getOnChange() + "();");
//                                                    } catch (Exception e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }
//                                        });


                                    }

                                }
                            }
                        }
                    }
                });


            } else {

                // 如果是字段为客户，就跳转到客户列表
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Map<String, String> dictHashMap;
                        if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
                            dictHashMap = mDictionaries.get(fieldInfo.getDict()
                                    + "." + fieldInfo.getDisplayMemberPath());
                            if (dictHashMap == null
                                    || (dictHashMap != null && dictHashMap.size() == 0)) {
                                dictHashMap = mDictionaries
                                        .get(fieldInfo.getDict());
                            }
                        } else {
                            dictHashMap = mDictionaries
                                    .get(fieldInfo.getDict());
                        }


                        final List<ReturnDict> returnDicts = new ArrayList<>();

                        if (dictHashMap != null) {
                            for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                                ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
                                returnDicts.add(dict1);
                            }
                        }
                        // 取消软键盘
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


                        if (!TextUtils.isEmpty(remoteUrl)) {
                            dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
                            dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
                                @Override
                                public void onSelected(字典 dict) {
                                    controlCellStatus(fieldInfo, dict.uuid, true);
                                    fieldInfo.setValue(dict.uuid);
                                    editText.setText(dict.getName() + "");
                                    //处理表单联动
                                    if (isDetails) {
                                        getLoadRelatedDataDetail(fieldInfo, ll_root, index);
                                    } else {
                                        getLoadRelatedData(fieldInfo);
                                    }
                                    if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                        String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                        v8.executeScript(js);
                                        try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                            v8.executeScript(fieldInfo.getOnChange() + "();");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        } else {
                            if (returnDicts.size() > 0) {
                                if (formName.equals("订单") && fieldInfo.getBinding().equals("contractId")) {
                                    mDictIosPicker.show(dict, true, "projectId='" + projectId + "'", "contractNumber");
                                    mDictIosPicker.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                                        @Override
                                        public void onSelectedDict(字典 dict) {
                                            ReturnDict rd = new ReturnDict(dict.getUuid(), dict.getName());
                                            String url = Global.BASE_JAVA_URL + GlobalMethord.莱恩斯合同表单数据 + rd.value;
                                            StringRequest.getAsyn(url, new StringResponseCallBack() {
                                                @Override
                                                public void onResponse(String response) {
                                                    String data = JsonUtils.pareseData(response);
                                                    try {
                                                        String receivePerson = JsonUtils.getStringValue(data, "consigneeName");
                                                        String receiveMobile = JsonUtils.getStringValue(data, "consigneeMobile");
                                                        String order_text_6 = JsonUtils.getStringValue(data, "payMenthod");
                                                        for (EditText et : mEditList) {
                                                            CellInfo tag = (CellInfo) et.getTag();
                                                            if (tag.getBinding().equals("receivePerson")) {
                                                                et.setText(receivePerson);
                                                                tag.setValue(receivePerson);
                                                            }
                                                            if (tag.getBinding().equals("receiveMobile")) {
                                                                et.setText(receiveMobile);
                                                                tag.setValue(receiveMobile);
                                                            }
                                                            if (tag.getBinding().equals("order_text_6")) {
                                                                Map<String, String> dictHashMap = mDictionaries
                                                                        .get(tag.getDict());

                                                                final List<ReturnDict> returnDicts = new ArrayList<>();

                                                                if (dictHashMap != null) {
                                                                    for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                                                                        ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
                                                                        returnDicts.add(dict1);
                                                                        if (dict1.value.equals(order_text_6)) {
                                                                            et.setText(dict1.text);
                                                                            tag.setValue(order_text_6);
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    setEdittextTag(editText, ll_root, rd, fieldInfo, isDetails, index);
                                                }

                                                @Override
                                                public void onFailure(Request request, Exception ex) {

                                                }

                                                @Override
                                                public void onResponseCodeErro(String result) {

                                                }
                                            });
                                        }
                                    });
                                } else if (fieldInfo.getBinding().equals("仓库")) {
                                    if (!TextUtils.isEmpty(remoteUrl)) {
                                        dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
                                    } else if (!TextUtils.isEmpty(fieldInfo.getDict())) {
                                        dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
                                    }
                                    dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
                                        @Override
                                        public void onSelected(字典 dict) {
                                            fieldInfo.setValue(dict.uuid);
                                            editText.setText(dict.getName() + "");
                                        }
                                    });
                                } else {
                                    Map<String, String> map = new HashMap<>();
                                    if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
                                        map = mDictionaries.get(fieldInfo.getDict() + "." + fieldInfo.getDisplayMemberPath());
                                        if (map == null || map.size() <= 0) {
                                            map = mDictionaries.get(fieldInfo.getDict());
                                        }
                                    } else {
                                        map = mDictionaries.get(fieldInfo.getDict());
                                    }
                                    dictionaryQueryDialog.show(fieldInfo.getDict(), map);
                                }
                                dictionaryQueryDialog
                                        .setOnSelectedListener(new DictionaryQueryDialog.OnSelectedListener() {
                                            @Override
                                            public void onSelected(final ReturnDict dict) {
                                                setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
                                                if (((formName.equals("订单") && fieldInfo.getBinding().equals("productId")) ||
                                                        formName.equals("特价申请") && fieldInfo.getBinding().equals("productIdNew")) && isDetails) {
                                                    productId = dict.value;
                                                    String specialUrl = Global.BASE_JAVA_URL +
                                                            "crm/order/getSpecialPriceInfo?productId=" + productId + "&projectId=" + projectId;
                                                    StringRequest.getAsyn(specialUrl, new StringResponseCallBack() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            String data = JsonUtils.pareseData(response);
                                                            if (!TextUtils.isEmpty(data)) { //不为空说明该产品没有走过特价申请
                                                                try {
                                                                    String productName = JsonUtils.getStringValue(data, "productName");
                                                                    String specificationType = JsonUtils.getStringValue(data, "specificationType");
                                                                    String discountPrice = JsonUtils.getStringValue(data, "discountPrice");

                                                                    //申请单明细
                                                                    Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                                                                    for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                                                        Map<Integer, List<EditText>> map1 = map.getValue();
                                                                        Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                                                        if (ll_root == map.getKey()) {
                                                                            for (Map.Entry<Integer, List<EditText>> m : entries) {
                                                                                if (index == m.getKey()) {
                                                                                    List<EditText> editTexts = m.getValue();
                                                                                    for (EditText et : editTexts) {
                                                                                        CellInfo tag = (CellInfo) et.getTag();
                                                                                        if (tag.getBinding().equals("productName")) {
                                                                                            et.setText(productName);
                                                                                            tag.setValue(productName);
                                                                                        } else if (tag.getBinding().equals("specificationType")) {
                                                                                            et.setText(specificationType);
                                                                                            tag.setValue(specificationType);
                                                                                        } else if (tag.getBinding().equals("price")) {
                                                                                            et.setText(discountPrice);
                                                                                            tag.setValue(discountPrice);
                                                                                        } else if (tag.getBinding().equals("colorNumber")) {
                                                                                            et.setText("");
                                                                                            tag.setValue("");
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                StringRequest.getAsyn(Global.BASE_JAVA_URL + "psi/sku/getSkuByProductId?productId=" + productId, new StringResponseCallBack() {
                                                                    @Override
                                                                    public void onResponse(String response) {
                                                                        String data1 = JsonUtils.pareseData(response);
                                                                        try {
                                                                            String name = JsonUtils.getStringValue(data1, "name");
                                                                            String price = JsonUtils.getStringValue(data1, "price");
                                                                            String itemstyle = JsonUtils.getStringValue(data1, "itemstyle");

                                                                            //申请单明细
                                                                            Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                                                                            for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                                                                Map<Integer, List<EditText>> map1 = map.getValue();
                                                                                Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                                                                if (ll_root == map.getKey()) {
                                                                                    for (Map.Entry<Integer, List<EditText>> m : entries) {
                                                                                        if (index == m.getKey()) {
                                                                                            List<EditText> editTexts = m.getValue();
                                                                                            for (EditText et : editTexts) {
                                                                                                CellInfo tag = (CellInfo) et.getTag();
                                                                                                if (tag.getBinding().equals("productName")) {
                                                                                                    et.setText(name);
                                                                                                    tag.setValue(name);
                                                                                                } else if (tag.getBinding().equals("specificationType")) {
                                                                                                    et.setText(itemstyle);
                                                                                                    tag.setValue(itemstyle);
                                                                                                } else if (tag.getBinding().equals("price")) {
                                                                                                    et.setText(price);
                                                                                                    tag.setValue(price);
                                                                                                } else if (tag.getBinding().equals("lionsAddOrder")) {
                                                                                                    et.setText("");
                                                                                                    tag.setValue("");
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    break;
                                                                                }
                                                                            }
                                                                        } catch (JSONException e1) {
                                                                            e1.printStackTrace();
                                                                            setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
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

                                                        @Override
                                                        public void onFailure(Request request, Exception ex) {

                                                        }

                                                        @Override
                                                        public void onResponseCodeErro(String result) {

                                                        }
                                                    });
                                                } else {
                                                    setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
                                                }

                                                currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;
                                                if ("请假类型".equals(fieldInfo.getBinding())) {
                                                    calculateLeaveDay();
                                                }
                                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                                    v8.executeScript(js);
                                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                                        v8.executeScript(fieldInfo.getOnChange() + "();");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                if (formName.equals("特价申请") && fieldInfo.getBinding().equals("colorNumber")) {
                                    try {
                                        JSONObject jo = new JSONObject();
                                        jo.put("projectId", projectId);
                                        jo.put("product", productId);
                                        StringRequest.postAsyn(Global.BASE_JAVA_URL +
                                                "crm/crm/project/getSampleColorNumber", jo, new StringResponseCallBack() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    String data = JsonUtils.getStringValue(response, "Data");
                                                    if (!TextUtils.isEmpty(data)) {
                                                        final List<字典> 字典s = JsonUtils.jsonToArrayEntity(data, 字典.class);
                                                        final List<String> ls = new ArrayList<>();
                                                        if (字典s.size() > 0) {
                                                            for (int i = 0; i < 字典s.size(); i++) {
                                                                ls.add(字典s.get(i).getName());
                                                            }
                                                        }
                                                        if (ls.size() > 0) {
                                                            mDictIosPicker.show(ls);
                                                            mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                                                @Override
                                                                public void onSelected(int index) {
                                                                    editText.setText(字典s.get(index).getName());
                                                                    fieldInfo.setValue(字典s.get(index).getUuid());
                                                                }
                                                            });
                                                        } else {
                                                            showShortToast("没有可使用的色号/追单");
                                                        }
                                                    } else {
                                                        showShortToast("没有可使用的色号/追单");
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
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                } else if (fieldInfo.getBinding().equals("lionsAddOrder") && formName.equals("订单")) {
                                    try {
                                        JSONObject jo = new JSONObject();
                                        jo.put("projectId", projectId);
                                        jo.put("product", productId);
                                        StringRequest.postAsyn(Global.BASE_JAVA_URL +
                                                "crm/crm/project/getSpecialPriceColorNumber", jo, new StringResponseCallBack() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    String data = JsonUtils.getStringValue(response, "Data");
                                                    if (!TextUtils.isEmpty(data)) {
                                                        final List<字典> 字典s = JsonUtils.jsonToArrayEntity(data, 字典.class);
                                                        final List<String> ls = new ArrayList<>();
                                                        if (字典s.size() > 0) {
                                                            for (int i = 0; i < 字典s.size(); i++) {
                                                                ls.add(字典s.get(i).getName());
                                                            }
                                                        }
                                                        if (ls.size() > 0) {
                                                            mDictIosPicker.show(ls);
                                                            mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                                                @Override
                                                                public void onSelected(int index) {
                                                                    editText.setText(字典s.get(index).getName());
                                                                    fieldInfo.setValue(字典s.get(index).getUuid());
                                                                }
                                                            });
                                                        } else {
                                                            showShortToast("没有可使用的色号/追单");
                                                        }
                                                    } else {
                                                        showShortToast("没有可使用的色号/追单");
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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (fieldInfo.getBinding().equals("仓库")) {
                                    if (!TextUtils.isEmpty(remoteUrl)) {
                                        dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
                                    } else if (!TextUtils.isEmpty(dict)) {
                                        dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
                                    }
                                    dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
                                        @Override
                                        public void onSelected(字典 dict) {
                                            fieldInfo.setValue(dict.uuid);
                                            editText.setText(dict.getName() + "");
                                        }
                                    });
                                } else {

                                    if (!TextUtils.isEmpty(dict)) {
                                        currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;
                                        dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
                                        dictionaryQueryDialogHelper.setOnSelectedListener1(new DictionaryQueryDialogHelper.OnSelectedListener1() {
                                            @Override
                                            public void onSelected1(字典 dict) {
                                                controlCellStatus(fieldInfo, dict.uuid, true);
                                                fieldInfo.setValue(dict.uuid);
                                                editText.setText(dict.getName() + "");
                                                //处理表单联动
                                                if (isDetails) {
                                                    getLoadRelatedDataDetail(fieldInfo, ll_root, index);
                                                } else {
                                                    getLoadRelatedData(fieldInfo);
                                                }
                                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                                    v8.executeScript(js);
                                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                                        v8.executeScript(fieldInfo.getOnChange() + "();");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });
                                    }

                                }
                            }
                        }
                    }
                });
            }


            // 如果是字段为客户，就跳转到客户列表
//            editText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    Map<String, String> dictHashMap;
//                    if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
//                        dictHashMap = mDictionaries.get(fieldInfo.getDict()
//                                + "." + fieldInfo.getDisplayMemberPath());
//                        if (dictHashMap == null
//                                || (dictHashMap != null && dictHashMap.size() == 0)) {
//                            dictHashMap = mDictionaries
//                                    .get(fieldInfo.getDict());
//                        }
//                    } else {
//                        dictHashMap = mDictionaries
//                                .get(fieldInfo.getDict());
//                    }
//
//
//                    final List<ReturnDict> returnDicts = new ArrayList<>();
//
//                    if (dictHashMap != null) {
//                        for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
//                            ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
//                            returnDicts.add(dict1);
//                        }
//                    }
//                    // 取消软键盘
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//
//                    if (!TextUtils.isEmpty(remoteUrl)) {
//                        dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
//                        dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
//                            @Override
//                            public void onSelected(字典 dict) {
//                                controlCellStatus(fieldInfo, dict.uuid, true);
//                                fieldInfo.setValue(dict.uuid);
//                                editText.setText(dict.getName() + "");
//                                //处理表单联动
//                                if (isDetails) {
//                                    getLoadRelatedDataDetail(fieldInfo, ll_root, index);
//                                } else {
//                                    getLoadRelatedData(fieldInfo);
//                                }
//                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
//                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
//                                    v8.executeScript(js);
//                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
//                                        v8.executeScript(fieldInfo.getOnChange() + "();");
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        });
//                    } else {
//                        if (returnDicts.size() > 0) {
//                            if (formName.equals("订单") && fieldInfo.getBinding().equals("contractId")) {
//                                mDictIosPicker.show(dict, true, "projectId='" + projectId + "'", "contractNumber");
//                                mDictIosPicker.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
//                                    @Override
//                                    public void onSelectedDict(字典 dict) {
//                                        ReturnDict rd = new ReturnDict(dict.getUuid(), dict.getName());
//                                        String url = Global.BASE_JAVA_URL + GlobalMethord.莱恩斯合同表单数据 + rd.value;
//                                        StringRequest.getAsyn(url, new StringResponseCallBack() {
//                                            @Override
//                                            public void onResponse(String response) {
//                                                String data = JsonUtils.pareseData(response);
//                                                try {
//                                                    String receivePerson = JsonUtils.getStringValue(data, "consigneeName");
//                                                    String receiveMobile = JsonUtils.getStringValue(data, "consigneeMobile");
//                                                    String order_text_6 = JsonUtils.getStringValue(data, "payMenthod");
//                                                    for (EditText et : mEditList) {
//                                                        CellInfo tag = (CellInfo) et.getTag();
//                                                        if (tag.getBinding().equals("receivePerson")) {
//                                                            et.setText(receivePerson);
//                                                            tag.setValue(receivePerson);
//                                                        }
//                                                        if (tag.getBinding().equals("receiveMobile")) {
//                                                            et.setText(receiveMobile);
//                                                            tag.setValue(receiveMobile);
//                                                        }
//                                                        if (tag.getBinding().equals("order_text_6")) {
//                                                            Map<String, String> dictHashMap = mDictionaries
//                                                                    .get(tag.getDict());
//
//                                                            final List<ReturnDict> returnDicts = new ArrayList<>();
//
//                                                            if (dictHashMap != null) {
//                                                                for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
//                                                                    ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
//                                                                    returnDicts.add(dict1);
//                                                                    if (dict1.value.equals(order_text_6)) {
//                                                                        et.setText(dict1.text);
//                                                                        tag.setValue(order_text_6);
//                                                                        break;
//                                                                    }
//                                                                }
//                                                            }
//                                                        }
//                                                    }
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                                setEdittextTag(editText, ll_root, rd, fieldInfo, isDetails, index);
//                                            }
//
//                                            @Override
//                                            public void onFailure(Request request, Exception ex) {
//
//                                            }
//
//                                            @Override
//                                            public void onResponseCodeErro(String result) {
//
//                                            }
//                                        });
//                                    }
//                                });
//                            } else if (fieldInfo.getBinding().equals("仓库")) {
//                                if (!TextUtils.isEmpty(remoteUrl)) {
//                                    dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
//                                } else if (!TextUtils.isEmpty(fieldInfo.getDict())) {
//                                    dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
//                                }
//                                dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
//                                    @Override
//                                    public void onSelected(字典 dict) {
//                                        fieldInfo.setValue(dict.uuid);
//                                        editText.setText(dict.getName() + "");
//                                    }
//                                });
//                            } else {
//                                Map<String, String> map = new HashMap<>();
//                                if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
//                                    map = mDictionaries.get(fieldInfo.getDict() + "." + fieldInfo.getDisplayMemberPath());
//                                    if (map == null || map.size() <= 0) {
//                                        map = mDictionaries.get(fieldInfo.getDict());
//                                    }
//                                } else {
//                                    map = mDictionaries.get(fieldInfo.getDict());
//                                }
//                                dictionaryQueryDialog.show(fieldInfo.getDict(), map);
//                            }
//                            dictionaryQueryDialog
//                                    .setOnSelectedListener(new DictionaryQueryDialog.OnSelectedListener() {
//                                        @Override
//                                        public void onSelected(final ReturnDict dict) {
//                                            setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
//                                            if (((formName.equals("订单") && fieldInfo.getBinding().equals("productId")) ||
//                                                    formName.equals("特价申请") && fieldInfo.getBinding().equals("productIdNew")) && isDetails) {
//                                                productId = dict.value;
//                                                String specialUrl = Global.BASE_JAVA_URL +
//                                                        "crm/order/getSpecialPriceInfo?productId=" + productId + "&projectId=" + projectId;
//                                                StringRequest.getAsyn(specialUrl, new StringResponseCallBack() {
//                                                    @Override
//                                                    public void onResponse(String response) {
//                                                        String data = JsonUtils.pareseData(response);
//                                                        if (!TextUtils.isEmpty(data)) { //不为空说明该产品没有走过特价申请
//                                                            try {
//                                                                String productName = JsonUtils.getStringValue(data, "productName");
//                                                                String specificationType = JsonUtils.getStringValue(data, "specificationType");
//                                                                String discountPrice = JsonUtils.getStringValue(data, "discountPrice");
//
//                                                                //申请单明细
//                                                                Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
//                                                                for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
//                                                                    Map<Integer, List<EditText>> map1 = map.getValue();
//                                                                    Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
//                                                                    if (ll_root == map.getKey()) {
//                                                                        for (Map.Entry<Integer, List<EditText>> m : entries) {
//                                                                            if (index == m.getKey()) {
//                                                                                List<EditText> editTexts = m.getValue();
//                                                                                for (EditText et : editTexts) {
//                                                                                    CellInfo tag = (CellInfo) et.getTag();
//                                                                                    if (tag.getBinding().equals("productName")) {
//                                                                                        et.setText(productName);
//                                                                                        tag.setValue(productName);
//                                                                                    } else if (tag.getBinding().equals("specificationType")) {
//                                                                                        et.setText(specificationType);
//                                                                                        tag.setValue(specificationType);
//                                                                                    } else if (tag.getBinding().equals("price")) {
//                                                                                        et.setText(discountPrice);
//                                                                                        tag.setValue(discountPrice);
//                                                                                    } else if (tag.getBinding().equals("colorNumber")) {
//                                                                                        et.setText("");
//                                                                                        tag.setValue("");
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                        }
//                                                                        break;
//                                                                    }
//                                                                }
//                                                            } catch (Exception e) {
//                                                                e.printStackTrace();
//                                                            }
//                                                        } else {
//                                                            StringRequest.getAsyn(Global.BASE_JAVA_URL + "psi/sku/getSkuByProductId?productId=" + productId, new StringResponseCallBack() {
//                                                                @Override
//                                                                public void onResponse(String response) {
//                                                                    String data1 = JsonUtils.pareseData(response);
//                                                                    try {
//                                                                        String name = JsonUtils.getStringValue(data1, "name");
//                                                                        String price = JsonUtils.getStringValue(data1, "price");
//                                                                        String itemstyle = JsonUtils.getStringValue(data1, "itemstyle");
//
//                                                                        //申请单明细
//                                                                        Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
//                                                                        for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
//                                                                            Map<Integer, List<EditText>> map1 = map.getValue();
//                                                                            Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
//                                                                            if (ll_root == map.getKey()) {
//                                                                                for (Map.Entry<Integer, List<EditText>> m : entries) {
//                                                                                    if (index == m.getKey()) {
//                                                                                        List<EditText> editTexts = m.getValue();
//                                                                                        for (EditText et : editTexts) {
//                                                                                            CellInfo tag = (CellInfo) et.getTag();
//                                                                                            if (tag.getBinding().equals("productName")) {
//                                                                                                et.setText(name);
//                                                                                                tag.setValue(name);
//                                                                                            } else if (tag.getBinding().equals("specificationType")) {
//                                                                                                et.setText(itemstyle);
//                                                                                                tag.setValue(itemstyle);
//                                                                                            } else if (tag.getBinding().equals("price")) {
//                                                                                                et.setText(price);
//                                                                                                tag.setValue(price);
//                                                                                            } else if (tag.getBinding().equals("lionsAddOrder")) {
//                                                                                                et.setText("");
//                                                                                                tag.setValue("");
//                                                                                            }
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                                break;
//                                                                            }
//                                                                        }
//                                                                    } catch (JSONException e1) {
//                                                                        e1.printStackTrace();
//                                                                        setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
//                                                                    }
//
//
//                                                                }
//
//                                                                @Override
//                                                                public void onFailure(Request request, Exception ex) {
//
//                                                                }
//
//                                                                @Override
//                                                                public void onResponseCodeErro(String result) {
//
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Request request, Exception ex) {
//
//                                                    }
//
//                                                    @Override
//                                                    public void onResponseCodeErro(String result) {
//
//                                                    }
//                                                });
//                                            } else {
//                                                setEdittextTag(editText, ll_root, dict, fieldInfo, isDetails, index);
//                                            }
//
//                                            currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;
//                                            if ("请假类型".equals(fieldInfo.getBinding())) {
//                                                calculateLeaveDay();
//                                            }
//                                            if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
//                                                String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
//                                                v8.executeScript(js);
//                                                try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
//                                                    v8.executeScript(fieldInfo.getOnChange() + "();");
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//                                    });
//                        } else {
//                            if (formName.equals("特价申请") && fieldInfo.getBinding().equals("colorNumber")) {
//                                try {
//                                    JSONObject jo = new JSONObject();
//                                    jo.put("projectId", projectId);
//                                    jo.put("product", productId);
//                                    StringRequest.postAsyn(Global.BASE_JAVA_URL +
//                                            "crm/crm/project/getSampleColorNumber", jo, new StringResponseCallBack() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            try {
//                                                String data = JsonUtils.getStringValue(response, "Data");
//                                                if (!TextUtils.isEmpty(data)) {
//                                                    final List<字典> 字典s = JsonUtils.jsonToArrayEntity(data, 字典.class);
//                                                    final List<String> ls = new ArrayList<>();
//                                                    if (字典s.size() > 0) {
//                                                        for (int i = 0; i < 字典s.size(); i++) {
//                                                            ls.add(字典s.get(i).getName());
//                                                        }
//                                                    }
//                                                    if (ls.size() > 0) {
//                                                        mDictIosPicker.show(ls);
//                                                        mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
//                                                            @Override
//                                                            public void onSelected(int index) {
//                                                                editText.setText(字典s.get(index).getName());
//                                                                fieldInfo.setValue(字典s.get(index).getUuid());
//                                                            }
//                                                        });
//                                                    } else {
//                                                        showShortToast("没有可使用的色号/追单");
//                                                    }
//                                                } else {
//                                                    showShortToast("没有可使用的色号/追单");
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Request request, Exception ex) {
//
//                                        }
//
//                                        @Override
//                                        public void onResponseCodeErro(String result) {
//
//                                        }
//                                    });
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            } else if (fieldInfo.getBinding().equals("lionsAddOrder") && formName.equals("订单")) {
//                                try {
//                                    JSONObject jo = new JSONObject();
//                                    jo.put("projectId", projectId);
//                                    jo.put("product", productId);
//                                    StringRequest.postAsyn(Global.BASE_JAVA_URL +
//                                            "crm/crm/project/getSpecialPriceColorNumber", jo, new StringResponseCallBack() {
//                                        @Override
//                                        public void onResponse(String response) {
//                                            try {
//                                                String data = JsonUtils.getStringValue(response, "Data");
//                                                if (!TextUtils.isEmpty(data)) {
//                                                    final List<字典> 字典s = JsonUtils.jsonToArrayEntity(data, 字典.class);
//                                                    final List<String> ls = new ArrayList<>();
//                                                    if (字典s.size() > 0) {
//                                                        for (int i = 0; i < 字典s.size(); i++) {
//                                                            ls.add(字典s.get(i).getName());
//                                                        }
//                                                    }
//                                                    if (ls.size() > 0) {
//                                                        mDictIosPicker.show(ls);
//                                                        mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
//                                                            @Override
//                                                            public void onSelected(int index) {
//                                                                editText.setText(字典s.get(index).getName());
//                                                                fieldInfo.setValue(字典s.get(index).getUuid());
//                                                            }
//                                                        });
//                                                    } else {
//                                                        showShortToast("没有可使用的色号/追单");
//                                                    }
//                                                } else {
//                                                    showShortToast("没有可使用的色号/追单");
//                                                }
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Request request, Exception ex) {
//
//                                        }
//
//                                        @Override
//                                        public void onResponseCodeErro(String result) {
//
//                                        }
//                                    });
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } else if (fieldInfo.getBinding().equals("仓库")) {
//                                if (!TextUtils.isEmpty(remoteUrl)) {
//                                    dictionaryQueryDialogHelper.showDialogByUrl(Global.BASE_JAVA_URL + remoteUrl);
//                                } else if (!TextUtils.isEmpty(dict)) {
//                                    dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
//                                }
//                                dictionaryQueryDialogHelper.setOnSelectedListener(new DictionaryQueryDialogHelper.OnSelectedListener() {
//                                    @Override
//                                    public void onSelected(字典 dict) {
//                                        fieldInfo.setValue(dict.uuid);
//                                        editText.setText(dict.getName() + "");
//                                    }
//                                });
//                            } else {
//
//                                if (!TextUtils.isEmpty(dict)) {
//                                    currentDetailIndex = ll_root.indexOfChild(linearLayout) + 1;
//                                    dictionaryQueryDialogHelper.show(dict, fieldInfo.getFilter());
//                                    dictionaryQueryDialogHelper.setOnSelectedListener1(new DictionaryQueryDialogHelper.OnSelectedListener1() {
//                                        @Override
//                                        public void onSelected1(字典 dict) {
//                                            controlCellStatus(fieldInfo, dict.uuid, true);
//                                            fieldInfo.setValue(dict.uuid);
//                                            editText.setText(dict.getName() + "");
//                                            //处理表单联动
//                                            if (isDetails) {
//                                                getLoadRelatedDataDetail(fieldInfo, ll_root, index);
//                                            } else {
//                                                getLoadRelatedData(fieldInfo);
//                                            }
//                                            if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
//                                                String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
//                                                v8.executeScript(js);
//                                                try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
//                                                    v8.executeScript(fieldInfo.getOnChange() + "();");
//                                                } catch (Exception e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//                                    });
//                                }
//
//                            }
//                        }
//                    }
//                }
//            });


        }


        if (fieldInfo.getBinding().equals("plate_text_2")) {
            String s1 = fieldInfo.getText();
            if (!TextUtils.isEmpty(s1)) {
                if (s1.equals("厂内样板")) {
                    hiddenMasterAreas(Within_The_Production_Model, false);
                } else if (s1.equals("现场服务")) {
                    hiddenMasterAreas(Field_Service, false);
                }
            }

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    if (s1.equals("厂内样板")) {
                        tv_add_details.setVisibility(View.VISIBLE);
                        hiddenMasterAreas(Within_The_Production_Model, false);
                    } else if (s1.equals("现场服务")) {
                        tv_add_details.setVisibility(View.GONE);
                        hiddenMasterAreas(Field_Service, false);
                    }
                }
            });
        }


        if (fieldInfo.getBinding().equals("paint_text_20")) {
            String s = fieldInfo.getText();
            if (s.equals("是")) {
                hiddenMasterAreas(The_Scene_Model_YES, false);
            } else if (s.equals("否")) {
                hiddenMasterAreas(The_Scene_Model_NO, false);
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    if (s1.equals("是")) {
                        hiddenMasterAreas(The_Scene_Model_YES, false);
                    } else if (s1.equals("否")) {
                        hiddenMasterAreas(The_Scene_Model_NO, false);
                    }
                }
            });
        }
        if ("paint_text_16".equals(fieldInfo.getBinding())) {
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDictIosPicker.show(fieldInfo.getDict(), true);
                    mDictIosPicker.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
                        @Override
                        public void onSelectedDict(字典 dict) {
                            editText.setText(dict.getName());
                            fieldInfo.setValue(dict.getUuid());
                        }
                    });
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("不满意")) {
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("paint_text_17")) {
                                LinearLayout parent = (LinearLayout) et.getParent();
                                parent.setBackgroundColor(getResources().getColor(R.color.white));
                                //todo
                                et.setEnabled(true);
                                et.setHint("请输入不满意原因");
                            }
                        }
                    } else {
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("paint_text_17")) {
                                LinearLayout parent = (LinearLayout) et.getParent();
                                parent.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
                                et.setEnabled(false);
                                et.setHint("");
                            }
                        }
                    }
                }
            });
        }
        if (fieldInfo.getBinding().equals("isOweMoney") && formName.equals("退换货申请单")) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().equals("是")) {
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("oweAmount")) {
                                LinearLayout parent = (LinearLayout) et.getParent();
                                parent.setBackgroundColor(getResources().getColor(R.color.white));
                                et.setEnabled(true);
                                break;
                            }
                        }
                    } else if (s.toString().equals("否")) {
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("oweAmount")) {
                                LinearLayout parent = (LinearLayout) et.getParent();
                                parent.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
                                et.setEnabled(false);
                                break;
                            }
                        }
                    }
                }
            });
        }

        if (fieldInfo.getBinding().equals("projectId") || fieldInfo.getBinding().equals("paint_text_7")) {
            setEditTextEnabled(editText);
        }

        if (fieldInfo.getBinding().equals("projectId") && (formName.equals("里程碑状态变更申请单") || formName.equals("维保合同"))) {
            editText.setTextColor(context.getResources().getColor(
                    R.color.color_theme));
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProjectViewerActivity.class);
                    intent.putExtra("projectId", fieldValue);
                    startActivity(intent);
                }
            });
        }
        if (fieldInfo.getBinding().equals("reimburseDepartment") && formName.equals("支出凭证")) {
            departmentId = fieldValue;
        }

        if (formName.equals("差旅费报销单") && isDetails && fieldInfo.getBinding().equals("trafficTools")) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    double amount = 0;
                    if (!TextUtils.isEmpty(s1)) {
                        List<EditText> editTexts = detailsMap.get(ll_root).get(index);
                        for (EditText et : editTexts) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("travelAmount")) {
                                String s2 = et.getText().toString();
                                if (!TextUtils.isEmpty(s2)) {
                                    double d = Double.parseDouble(s2);
                                    if (s1.equals("航空")) {
                                        amount = (d + 50) / (1 + 0.09) * 0.09;
                                    } else if (s1.equals("铁路")) {
                                        amount = d / (1 + 0.09) * 0.09;
                                    } else if (s1.equals("公路水路")) {
                                        amount = d / (1 + 0.03) * 0.03;
                                    }
                                } else {
                                    break;
                                }
                            } else if (tag.getBinding().equals("taxAmount")) {
                                et.setText(ViewHelper.stringToDouble(amount + "", "00"));
                            }
                        }
                    }
                }
            });
        }

        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }

    /**
     * 临时抽取的一个方法
     *
     * @param editText
     * @param ll_root
     */
    private void setEdittextTag(EditText editText, LinearLayout ll_root, ReturnDict
            dict, CellInfo fieldInfo, boolean isDetails, int index) {
        CellInfo tag = (CellInfo) editText.getTag();
        tag.setValue(dict.value);
        editText.setTag(tag);
        Log.e("filedValue", editText + dict.value);
        editText.setText(dict.text + "");

        if (!TextUtils.isEmpty(fieldInfo.getLoadRelated())) {
            if (isDetails) {
                getLoadRelatedDataDetail(fieldInfo, ll_root, index);
            } else {
                getLoadRelatedData(fieldInfo);
            }
        }
    }


    private void addDateTimeEditView(final CellInfo fieldInfo,
                                     LinearLayout linearLayout, ViewGroup.LayoutParams params,
                                     boolean isDetails, int index, LinearLayout ll_root, final boolean isEditable) {
        final EditText editText = new EditText(context);
        if (fieldInfo.getBinding().equals("startTime") || "请假开始时间".equals(fieldInfo.getBinding())) {
            startField = fieldInfo;
        }
        if (fieldInfo.getBinding().equals("endTime") || "请假结束时间".equals(fieldInfo.getBinding())) {
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
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText, isEditable);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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
            //.toLowerCase()
            if (fieldInfo.getDefaultValue().contains("now")) {
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
                    InputSoftHelper.hiddenSoftInput(context, editText);
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

                            if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                v8.executeScript(js);
                                try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                    v8.executeScript(fieldInfo.getOnChange() + "();");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if ("startTime".equals(fieldInfo.getBinding()) || "请假开始时间".equals(fieldInfo.getBinding())) {
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
                            if ("endTime".equals(fieldInfo.getBinding()) || "请假结束时间".equals(fieldInfo.getBinding())) {
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
        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }


    /**
     * 选择是否
     */
    private void addCheckedBox(final CellInfo fieldInfo,
                               LinearLayout linearLayout, ViewGroup.LayoutParams params,
                               boolean isDetails, int index, LinearLayout ll_root, final boolean isEditable) {
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText, isEditable);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        // editText.setHint("点击选择");
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setLayoutParams(params);

        boolean readOnly = fieldInfo.getReadOnly();


        if (TextUtils.isEmpty(fieldInfo.getValue())) {
            fieldInfo.setValue("0");
        }


        //先获取dict.DisplayMemberPath
        // 看字典中是否有值，如果没有字典，获取dict
        Map<String, String> dictHashMap;
        if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
            dictHashMap = mDictionaries.get(fieldInfo.getDict()
                    + "." + fieldInfo.getDisplayMemberPath());
            if (dictHashMap == null
                    || (dictHashMap != null && dictHashMap.size() == 0)) {
                dictHashMap = mDictionaries
                        .get(fieldInfo.getDict());
            }
        } else {
            dictHashMap = mDictionaries
                    .get(fieldInfo.getDict());
        }


        List<字典> returnDicts = new ArrayList<>();

        if (dictHashMap != null) {
            for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                字典 dict1 = new 字典(map.getKey(), map.getValue());
                returnDicts.add(dict1);
            }
        }


        if (!TextUtils.isEmpty(fieldInfo.getValue())) {
            if ("true".equalsIgnoreCase(fieldInfo.getValue())) {
                fieldInfo.setValue("1");
            } else if ("false".equalsIgnoreCase(fieldInfo.getValue())) {
                fieldInfo.setValue("0");
            }
            try {
                int indexs = Integer.parseInt(fieldInfo.getValue());
                if (indexs < checkStrs.length) {
                    editText.setText(checkStrs[indexs]);
                }
            } catch (Exception e) {
                Log.e(getClass().getName(), e + "");
            }
        }

        if ("radiolist".equalsIgnoreCase(fieldInfo.getCellStyle())) {
            if (TextUtils.isEmpty(fieldInfo.getValue())) {
                editText.setText("");
                fieldInfo.setValue("");
            } else {
                editText.setText(dictHashMap.get(fieldInfo.getValue()));
            }
        }


        if (readOnly) {
            editText.setEnabled(false);
        }

        if (!fieldInfo.getReadOnly()) { // 如果控件可编辑，为其绑定监听事件
            if ("checkbox".equalsIgnoreCase(fieldInfo.getCellStyle())) {
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
            } else if ("radiolist".equalsIgnoreCase(fieldInfo.getCellStyle())) {
                editText.setEnabled(true);
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDictIosPicker.show(returnDicts, "name");
                        mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {
                                字典 字典 = returnDicts.get(index);
                                editText.setText(字典.getName());
                                fieldInfo.setValue(字典.getUuid());
                                if (!TextUtils.isEmpty(fieldInfo.getOnChange())) {
                                    String js = "var rowIndex = " + index + ";\r\n var isPostBack = true;";
                                    v8.executeScript(js);
                                    try { //判断是否配置的是方法名，取配置的方法看能否取到值，不能取到值再去执行onchange
                                        v8.executeScript(fieldInfo.getOnChange() + "();");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }
        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }


    /**
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param params
     * @param isDetails
     * @param index
     * @param ll_root
     */
    private void addComboxListView(final CellInfo fieldInfo,
                                   LinearLayout linearLayout, ViewGroup.LayoutParams params,
                                   boolean isDetails, int index, LinearLayout ll_root, final boolean isEditable) {
        String fieldValue = fieldInfo.getText();
        // new一个线性布局
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText, isEditable);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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

        //先获取dict.DisplayMemberPath
        // 看字典中是否有值，如果没有字典，获取dict
        Map<String, String> dictHashMap;
        if (!TextUtils.isEmpty(fieldInfo.getDisplayMemberPath())) {
            dictHashMap = mDictionaries.get(fieldInfo.getDict()
                    + "." + fieldInfo.getDisplayMemberPath());
            if (dictHashMap == null
                    || (dictHashMap != null && dictHashMap.size() == 0)) {
                dictHashMap = mDictionaries
                        .get(fieldInfo.getDict());
            }
        } else {
            dictHashMap = mDictionaries
                    .get(fieldInfo.getDict());
        }

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
            Map<String, String> finalDictHashMap = dictHashMap;
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 取消软键盘
//                    InputSoftHelper.hiddenSoftInput(context, editText);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dictIosMultiPicker.show(R.id.ll_form_info_root, finalDictHashMap);
                    dictIosMultiPicker
                            .setOnMultiSelectedListener(new DictIosMultiPicker.OnMultiSelectedListener() {
                                @Override
                                public void onSelected(String selectedIds,
                                                       String selectedNames) {
                                    fieldInfo.setValue(selectedIds);
                                    controlCellStatus(fieldInfo, selectedIds, true);
                                    editText.setText(StrUtils
                                            .pareseNull(selectedNames));
                                }
                            });
                }
            });
        }
        if (formName.equals("授信申请及合同") &&
                (fieldInfo.getBinding().equals("signedContract")
                        || fieldInfo.getBinding().equals("customerCompanyNature"))) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    if (!TextUtils.isEmpty(s1)) {
                        if (s1.equals("其他")) {
                            for (EditText et : mEditList) {
                                CellInfo tag = (CellInfo) et.getTag();
                                if (tag.getBinding().equals("otherCustomerCompanyNature") &&
                                        fieldInfo.getBinding().equals("customerCompanyNature")) {
                                    setEditTextEnabled(et, tag, false);
                                }
                                if (tag.getBinding().equals("otherSignedContract") &&
                                        fieldInfo.getBinding().equals("signedContract")) {
                                    setEditTextEnabled(et, tag, false);
                                }
                            }
                        } else {
                            for (EditText et : mEditList) {
                                CellInfo tag = (CellInfo) et.getTag();
                                if (tag.getBinding().equals("otherCustomerCompanyNature") &&
                                        fieldInfo.getBinding().equals("customerCompanyNature")) {
                                    setEditTextEnabled(et, tag, true);
                                }
                                if (tag.getBinding().equals("otherSignedContract") &&
                                        fieldInfo.getBinding().equals("signedContract")) {
                                    setEditTextEnabled(et, tag, true);
                                }
                            }
                        }
                    } else {
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("otherCustomerCompanyNature") &&
                                    fieldInfo.getBinding().equals("customerCompanyNature")) {
                                setEditTextEnabled(et, tag, true);
                            }
                            if (tag.getBinding().equals("otherSignedContract") &&
                                    fieldInfo.getBinding().equals("signedContract")) {
                                setEditTextEnabled(et, tag, false);
                            }
                        }
                    }
                }
            });

        }

        if (fieldInfo.getBinding().equals("contractType") && formName.equals("授信申请及合同")) {

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String s1 = s.toString();
                    if (!TextUtils.isEmpty(s1)) {
                        hiddenMasterAreas(Contract_ALL1);
                        if (s1.equals("B2-外借资产")) {
                            hiddenMasterAreas(Contract_B2, true);
                        } else if (s1.equals("C-内借建设资质")) {
                            hiddenMasterAreas(Contract_C, true);
                        } else if (s1.equals("B1-非全款合同,B2-外借资产,C-内借建设资质")
                                || s1.equals("B1-非全款合同,B2-外借资产")
                                || s1.equals("B1-非全款合同")) {
                            hiddenMasterAreas(Contract_B1, true);
                        }
                        if (s1.equals("F-其他")) {
                            for (EditText et : mEditList) {
                                CellInfo tag = (CellInfo) et.getTag();
                                if (tag.getBinding().equals("otherContractType")) {
                                    setEditTextEnabled(et, tag, false);
                                }
                            }
                        } else {
                            for (EditText et : mEditList) {
                                CellInfo tag = (CellInfo) et.getTag();
                                if (tag.getBinding().equals("otherContractType")) {
                                    setEditTextEnabled(et, tag, true);
                                }
                            }
                        }
                    } else {
                        hiddenMasterAreas(Contract_ALL1);
                        for (EditText et : mEditList) {
                            CellInfo tag = (CellInfo) et.getTag();
                            if (tag.getBinding().equals("otherContractType")) {
                                setEditTextEnabled(et, tag, true);
                            }
                        }
                    }
                }
            });
        }

        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }


    /**
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param params
     * @param isDetails
     * @param index
     * @param ll_root
     */
    private void addMutiComobox(final CellInfo fieldInfo,
                                LinearLayout linearLayout, ViewGroup.LayoutParams params,
                                boolean isDetails, int index, LinearLayout ll_root, final boolean isEditable) {
        String fieldValue = fieldInfo.getText();
        // new一个线性布局
        final EditText editText = new EditText(context);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText, isEditable);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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

        editText.setText(fieldInfo.getText());

        if (!readOnly) {
            editText.setEnabled(true); //
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // 取消软键盘
//                    InputSoftHelper.hiddenSoftInput(context, editText);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    dictIosMultiPicker.showByDictName(R.id.ll_form_info_root, fieldInfo.getDict());//"dict_license_seal_name"
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
        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }


    private void addOtherEditView(CellInfo fieldInfo,
                                  LinearLayout linearLayout, ViewGroup.LayoutParams params, boolean isDetails,
                                  int index, LinearLayout ll_root, final boolean isEditable) {
        EditText editText = new EditText(context);
        setEditEnable(fieldInfo, editText, isEditable);
        if (!fieldInfo.getReadOnly() && fieldInfo.getRequired()) {
            editText.setHint("必填");
        }
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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
        fieldInfo.setDetails(isDetails);
        editText.setTag(fieldInfo);
        if (isDetails) {
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        linearLayout.addView(editText);
        setCellValueHidden(fieldInfo, editText);
    }


    /**
     * 手写签名类型
     *
     * @param fieldInfo 一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param index
     * @param ll_root   用于生成表单的父布局
     * @param params
     */
    private void addSignatureView(final CellInfo fieldInfo, boolean isDetails,
                                  int index, LinearLayout ll_root, ViewGroup.LayoutParams params, final boolean isEditable) {
        final String fieldValue = fieldInfo.getValue();
        // new一个线性布局
        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        editText.setFocusable(false);
        setEditEnable(fieldInfo, editText, isEditable);
        editText.setVisibility(View.GONE);
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
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
        final ImageView ivSignature = new ImageView(context);
        // ivSignature.setImageResource(R.drawable.ico_nav);
        // ivSignature.setLayoutParams(new LayoutParams(300, 300));
        ivSignature.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivSignature.setAdjustViewBounds(true);
        // ivSignature.setMaxHeight(100);
        ivSignature.setMaxWidth(220);
        llOther.addView(ivSignature);

        if (!TextUtils.isEmpty(fieldValue)) {
            ImageUtils.displyImageById(fieldValue, ivSignature);
        } else {
            if (!fieldInfo.getReadOnly()) {
                ivSignature.setImageResource(R.drawable.ico_pencil_square);
            } else {
                ivSignature.setImageBitmap(null);
            }
        }

        if (!fieldInfo.getReadOnly()) {
            llOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputSoftHelper.hideSoftInputMethod(editText);
                    // 设置手写签名
                    signatureSet(fieldInfo, ivSignature);
                }
            });
        } else {
            // 不可点击
            if (TextUtils.isEmpty(fieldValue)) {
                ivSignature.setImageBitmap(null);
            }
        }

        editText.setTag(fieldInfo);
        if (isDetails) {
//            mDetailsEdits.add(editText);
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }

        ll_root.addView(llOther);
        ll_root.addView(editText);
    }


    /**
     * 多图片类型
     *
     * @param fieldInfo    一个配置单元<FieldInfo>的信息,包含了一行表单的信息
     * @param linearLayout 用于生成表单的父布局
     * @param index
     * @param params
     * @param ll_root
     */
    private void addMultiImageView(final CellInfo fieldInfo,
                                   LinearLayout linearLayout, boolean isDetails, int index, ViewGroup.
                                           LayoutParams params, LinearLayout ll_root, final boolean isEditable) {

        String fieldValue = fieldInfo.getValue();
        // fieldValue = "13288";
        // new一个线性布局
        final EditText editText = new EditText(context);
        editText.setEnabled(false);
        editText.setFocusable(false);
        editText.setVisibility(View.GONE);
        editText.setTextColor(context.getResources().getColor(
                R.color.text_form_black));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
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

        final MultipleAttachView gView = new MultipleAttachView(context);
        gView.setNumColumns(3);
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
        mAttachViews.add(gView);
        if (isDetails) {
            Map<Integer, List<EditText>> integerListMap = detailsMap.get(ll_root);
            integerListMap.get(index).add(editText);
        } else {
            mEditList.add(editText);
        }
        attachViewHashMap.put(fieldInfo, gView);
        linearLayout.addView(llOther);
        linearLayout.addView(editText);

        gView.setOnAddImageListener(new MultipleAttachView.OnAddImageListener() {
            @Override
            public void onAddImageListener() {
                mAttachViews.remove(gView);
                selectAttachView = new CellInfo();
                gView.setTag(selectAttachView);
                mAttachViews.add(gView);

            }
        });
    }


    /**
     * 判断单元格是否需要隐藏，如果需要隐藏，则隐藏
     *
     * @param cellInfo
     * @param et
     */
    private void setCellValueHidden(CellInfo cellInfo, EditText et) {
        if (cellInfo.getInvisible() != null && cellInfo.getInvisible()) {
            et.setVisibility(View.GONE);
        }
    }


    /**
     * 设置edittext 可点击但不可输入
     */
    private void setEditTextEnabled(EditText et) {
        et.setCursorVisible(false);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        et.setEnabled(true);
    }

    /**
     * 设置edittext 由只读变为可编辑
     *
     * @param et
     * @param info
     * @param readOnly true修改edittext为不可编辑 false修改edittext为可编辑
     */
    private void setEditTextEnabled(EditText et, CellInfo info, boolean readOnly) {
        LinearLayout parent = (LinearLayout) et.getParent();
        if (!readOnly) {
            parent.setBackgroundColor(getResources().getColor(R.color.white));
            info.setReadOnly(false);
            et.setEnabled(true);
        } else {
            et.setText("");
            parent.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
            info.setReadOnly(true);
            et.setEnabled(false);
        }
    }


    /***
     * 弹出手写签名输入板
     *
     * @param fieldInfo
     * @param ivSignature
     */
    private void signatureSet(final CellInfo fieldInfo,
                              final ImageView ivSignature) {
        SignaturePopWindow signaturePopWindow = new SignaturePopWindow(context);
        signaturePopWindow.show(R.id.ll_form_info_root);
        signaturePopWindow
                .setOnSaveSuccessedListener(new SignaturePopWindow.OnSaveSuccessedListener() {
                    @Override
                    public void onSaved(final String path) {
                        LogUtils.e(TAG, path);
                        // ImageLoader.getInstance().displayImage(path,
                        // ivSignature);
                        if (!TextUtils.isEmpty(path)) {
                            ivSignature.setImageBitmap(BitmapHelper
                                    .decodeSampleBitmapFromFile(path, 800, 800));
                            File file = new File(path);
                            if (file != null && file.exists()) {
                                ProgressDialogHelper.show(context, true);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final Attach attach = UploadHelper
                                                .uploadFileByHttpGetAttach(new File(
                                                        path));
                                        ((Activity) context)
                                                .runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        fieldInfo.setValue(attach.uuid);
                                                        ProgressDialogHelper
                                                                .dismiss();
                                                    }
                                                });
                                    }
                                }).start();
                            } else {
                            }

                        }
                    }
                });
    }

    /**
     * 隐藏单元格
     *
     * @param strs 需要隐藏的单元格的binding数组
     */
    private void hiddenMasterAreas(String[] strs) {
        for (EditText et : mEditList) {
            CellInfo info = (CellInfo) et.getTag();
            if (Arrays.asList(strs).contains(info.getBinding())) {
                LinearLayout parent = (LinearLayout) et.getParent();
                parent.setVisibility(View.GONE);
            } else {
                LinearLayout parent = (LinearLayout) et.getParent();
                parent.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 显示或隐藏单元格
     *
     * @param strs 需要显示或隐藏的单元格的binding数组
     * @param flag true 为显示 false为隐藏
     */
    private void hiddenMasterAreas(String[] strs, boolean flag) {
        if (flag) {
            for (EditText et : mEditList) {
                CellInfo info = (CellInfo) et.getTag();
                if (Arrays.asList(strs).contains(info.getBinding())) {
                    LinearLayout parent = (LinearLayout) et.getParent();
                    parent.setVisibility(View.VISIBLE);
                }
            }
        } else {
            for (EditText et : mEditList) {
                CellInfo info = (CellInfo) et.getTag();
                if (Arrays.asList(strs).contains(info.getBinding())) {
                    LinearLayout parent = (LinearLayout) et.getParent();
                    parent.setVisibility(View.GONE);
                } else {
                    LinearLayout parent = (LinearLayout) et.getParent();
                    parent.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /***
     * 设置大小写公式绑定
     *
     * @param etExpression 计算公式的文本框
     * @param expression   公式
     */
    private void setMoneyConvert(final EditText etExpression, String
            expression, List<EditText> editTexts) {
        // 包含公式数字大小写转换
        Logger.i(TAG + expression);
        List<EditText> list = new ArrayList<>();
        if (mEditList != editTexts) {
            list.addAll(mEditList);
            list.addAll(editTexts);
        } else {
            list.addAll(mEditList);
        }
        for (int j = 0; j < list.size(); j++) {
            final EditText eText = list.get(j);// 绑定公式计算的文本框监听
            CellInfo fieldInfo = (CellInfo) eText.getTag();
            String fieldName = fieldInfo.getBinding();
            if (!TextUtils.isEmpty(fieldName)
                    //.toLowerCase()
                    && expression.contains("(" + fieldName + ")")) {
                Logger.i(TAG + "涉及字段：" + fieldName);
                if (!TextUtils.isEmpty(fieldInfo.getText())) {
                    etExpression.setText(MoneyUtils.change(Double.valueOf(fieldInfo.getText())));
                }
                eText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        try {
                            Double money = Double.valueOf(s.toString());
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

                                                controlCellStatus(info, data1.getValue(), true);
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
     * 获取表单联动的信息
     *
     * @param info
     */
    private void getLoadRelatedDataDetail(CellInfo info, final LinearLayout linearLayout,
                                          int index) {
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
                                if (detailsMap != null && detailsMap.size() > 0) {
                                    Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
                                    for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                                        Map<Integer, List<EditText>> map1 = map.getValue();
                                        Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                                        if (linearLayout == map.getKey()) {
                                            for (Map.Entry<Integer, List<EditText>> m : entries) {
                                                if (index == m.getKey()) {
                                                    List<EditText> editTexts = m.getValue();
                                                    if (relatedData != null) {
                                                        for (RelatedData data1 : relatedData.getResultFieldMaps()) {
                                                            for (final EditText et : editTexts) {
                                                                CellInfo info = (CellInfo) et.getTag();
                                                                if (info.getBinding().equals(data1.getvSheetFieldName())) {
                                                                    controlCellStatus(info, data1.getValue(), true);

                                                                    info.setValue(data1.getValue());
                                                                    if (!TextUtils.isEmpty(StrUtils.removeSpace(info.getDict()))) {
                                                                        Map<String, String> map2 = mDictionaries.get(info.getDict());
                                                                        if (map2 != null) {
                                                                            String text = map2.get(data1.getValue());
                                                                            et.setText(text);
                                                                        }
                                                                    } else {
                                                                        et.setText(data1.getValue());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
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
        Iterator<Map.Entry<CellInfo, MultipleAttachView>> iterator = attachViewHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<CellInfo, MultipleAttachView> next = iterator.next();
            final CellInfo fieldInfo = next.getKey();
            MultipleAttachView attachView = next.getValue();
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
    private void setEditEnable(CellInfo fieldInfo, EditText editText, boolean isEditable) {
        if (fieldInfo.getReadOnly() && isEditable) {
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
     * @param lin
     * @param isLastDetail
     */

    private void CreateDetailLayout(List<CellInfo> list2, LinearLayout lin,
                                    int index, boolean isLastDetail) {
        Map<Integer, List<EditText>> integerListMap = detailsMap.get(lin);
        if (integerListMap == null) {
            integerListMap = new HashMap<>();
        }
        List<EditText> editTexts = new ArrayList<>();
        integerListMap.put(index, editTexts);
        detailsMap.put(lin, integerListMap);
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((LinearLayout.LayoutParams) params).setMargins(
                DisplayUtils.dip2px(context, 10),
                DisplayUtils.dip2px(context, 6),
                DisplayUtils.dip2px(context, 10),
                DisplayUtils.dip2px(context, 6));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);


        List<CellInfo> InfoList = new ArrayList<>();
        for (CellInfo info : list2) {
            CellInfo cellInfo = new CellInfo();
            try {
                InvokeUtils.reflectionAttr(info, cellInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            InfoList.add(cellInfo);
        }


        addHorionzalLine(linearLayout);
        createDetailsHeader(linearLayout, lin, index);
        addHorionzalLine(linearLayout);
        createUI(InfoList, linearLayout, index, lin);

        //如果是最后一条明细，并且是新建单子，再给所有单元格绑定公式
        if (isLastDetail) {
            setExpression(true, detailsMap.get(lin).get(index));
        }


        lin.addView(linearLayout);
    }


    /**
     * 创建申请明细头
     *
     * @param
     */
    private void createDetailsHeader(final LinearLayout root, final LinearLayout lin,
                                     int index) {
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
        textView.setText("明细" + index);
        textView.setPadding(leftPadding * 2, leftPadding * 2, leftPadding * 2, leftPadding * 2);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        final TextView tv_delete = new TextView(context);
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
        tv_delete.setTag(index);


//        mDetailsTitles.add(textView);
        detailsCTitles.get(lin).add(textView);
        root.addView(relativeLayout);

        tv_delete.setOnClickListener(new View.OnClickListener() {  //删除一行明细
            @Override
            public void onClick(View v) {
                boolean isRetainDetalis = false; //是否保留一条明细
                Map<Integer, List<EditText>> integerListMap = detailsMap.get(lin);
                List<EditText> editTexts = integerListMap.get(index);
                for (EditText et : editTexts) { //遍历明细中的单元格是否存在必填项
                    CellInfo tag = (CellInfo) et.getTag();
                    if (tag.getRequired()) {
                        isRetainDetalis = true;
                        break;
                    }
                }
                if (isRetainDetalis) {  //如果明细中有必填项
                    if (integerListMap.size() > 1) { //如果有多条明细可删除明细 保留至少一条明细
                        lin.removeView(root);
                        detailsCTitles.get(lin).remove(textView);
                        integerListMap.remove(tv_delete.getTag()); //删除这行明细表的数据

                        for (int i = 0; i < detailsCTitles.get(lin).size(); i++) {
                            TextView tv = detailsCTitles.get(lin).get(i);
                            tv.setText("明细" + (i + 1));
                        }
                        setExpression(true, detailsMap.get(lin).get(index));
                    } else {
                        showShortToast("至少要保留一条明细");
                    }
                } else { //如果明细中没有必填项 不受条件限制可直接删除
                    lin.removeView(root);
                    detailsCTitles.get(lin).remove(textView);
//                detailMap.remove(tv_delete.getTag());
                    integerListMap.remove(tv_delete.getTag()); //删除这行明细表的数据

                    for (int i = 0; i < detailsCTitles.get(lin).size(); i++) {
                        TextView tv = detailsCTitles.get(lin).get(i);
                        tv.setText("明细" + (i + 1));
                    }
                    setExpression(true, detailsMap.get(lin).get(index));
                }
            }
        });
    }

    private CommanAdapter<AuditeInfo> getAuditeAdapter(List<AuditeInfo> auto) {
        return new CommanAdapter<AuditeInfo>(auto, context, R.layout.item_node_flow) {
            @Override
            public void convert(int position, AuditeInfo item, BoeryunViewHolder viewHolder) {
                View status = viewHolder.getView(R.id.view_audit_status);
                TextView tvResult = viewHolder.getView(R.id.tv_result_node);  //审批结果
                LinearLayout llOpinion = viewHolder.getView(R.id.ll_opinion_audit_list);  //审批意见，如果是提交节点，不显示

                viewHolder.setTextValue(R.id.tv_node_title, item.getTitle()); //节点标题
                viewHolder.setTextValue(R.id.tv_audit_time, item.getProcessTime()); //审批时间
                viewHolder.setTextValue(R.id.tv_opinion_approval, item.getOpinion()); //审批意见
                if (!TextUtils.isEmpty(item.getUserName())) {
                    viewHolder.setTextValue(R.id.auditor_item_node,
                            "提交".equals(item.getTitle()) ? item.getUserName() : "审批人: " + item.getUserName()); //审核人
                } else {
                    viewHolder.setTextValue(R.id.auditor_item_node, "");
                }


                //审批用时
                if (approvalCostTime) {
                    if (!TextUtils.isEmpty(item.getTotalTime())) {
                        viewHolder.setTextValue(R.id.tv_cost_time, "用时" + item.getIntervalTime());
                    } else {
                        viewHolder.setTextValue(R.id.tv_cost_time, "");
                    }
                } else {
                    viewHolder.setTextValue(R.id.tv_cost_time, "");
                }

                llOpinion.setVisibility(View.VISIBLE);
                if ("通过".equals(item.getResult())) { //已通过
                    status.setBackground(getResources().getDrawable(R.drawable.circle_bg_green));
                    tvResult.setTextColor(getResources().getColor(R.color.hanyaRed));
                    tvResult.setText("同意");
                    tvResult.setVisibility(View.VISIBLE);
                } else if ("否决".equals(item.getResult())) {  //被否决
                    status.setBackground(getResources().getDrawable(R.drawable.popup_sys_circle_bg));
                    tvResult.setTextColor(getResources().getColor(R.color.color_red));
                    tvResult.setText("否决");
                    tvResult.setVisibility(View.VISIBLE);
                } else {
                    if (item.getNodeId() == null) {
                        item.setNodeId(0);
                    }
                    if (nextStepId == Integer.valueOf(item.getNodeId()) && !"提交".equals(item.getTitle())) { //待审核状态
                        status.setBackground(getResources().getDrawable(R.drawable.circle_bg_orenge));
                        tvResult.setTextColor(getResources().getColor(R.color.lightYellow));
                        tvResult.setText("待审批");
                        tvResult.setVisibility(View.VISIBLE);
                    } else {
                        llOpinion.setVisibility(View.GONE);
                        if ("提交".equals(item.getTitle())) { //提交节点
                            status.setBackground(getResources().getDrawable(R.drawable.circle_bg_green));
                            tvResult.setVisibility(View.GONE);
                            viewHolder.setTextValue(R.id.tv_cost_time, "");
                        } else {//未审核
                            status.setBackground(getResources().getDrawable(R.drawable.circle_bg_border_gray));
                            tvResult.setVisibility(View.GONE);
                        }
                    }
                }


                /**
                 * 如果是最后一个节点
                 */
                if (position == auto.size() - 1) {
                    viewHolder.getView(R.id.view_vertical_line).setVisibility(View.GONE);
                    if (nextStepId == -1) {  //证明流程已结束
                        tvResult.setText("");
                        status.setBackground(getResources().getDrawable(R.drawable.circle_bg_green));
                    }
                } else {
                    viewHolder.getView(R.id.view_vertical_line).setVisibility(View.VISIBLE);
                }

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
            if (fieldInfo == selectAttachView) {
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
                                    String expression, List<EditText> editTexts) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        List<EditText> list = new ArrayList<>();
        if (mEditList != editTexts) {
            list.addAll(mEditList);
            list.addAll(editTexts);
        } else {
            list.addAll(mEditList);
        }
        // 包含公式数字大小写转换
        int end;
        end = expression.indexOf(",");
        if (end == -1) {
            end = expression.indexOf(")");
        }
        if (end == -1) {
            end = expression.length();
        }

        int start;
        start = expression.indexOf("(");
        if (start == -1) {
            start = 0;
        }
        String operatorStr = expression.substring(
                start == 0 ? 0 : start + 1, end);
        if (!expression.contains("sum") && !expression.contains("thousand")) {
            operatorStr = expression;
        }
        Logger.i(TAG + "::expression--" + expression);
//        final String operatorStr = expression.substring(expression.indexOf("(") + 1, expression.indexOf(","));
//        if(!operatorStr1.contains(")")){
//            operatorStr1 += ")";
//        }
//        final String operatorStr = operatorStr1;
        //得到运算表达式中绑定的单元格的binding的数组
        String[] strings = StrUtils.splitFuhao(expression);
        for (String s : strings) {
            for (int j = 0; j < list.size(); j++) {
                final EditText eText = list.get(j);// 绑定公式计算的文本框监听
                final CellInfo fieldInfo = (CellInfo) eText.getTag();
                final String fieldName = fieldInfo.getBinding();
                final String fieldValue = fieldInfo.getValue();//.toLowerCase()
                String value = eText.getText().toString();
                if (!TextUtils.isEmpty(fieldName) && s.equals(fieldName)) { // && !TextUtils.isEmpty(fieldInfo.getExpression())
                    Logger.i(TAG + "::涉及字段：" + fieldName);
                    if (!TextUtils.isEmpty(value)) {
                        // 如果有值则
                        hashMap.put(fieldName, value);
                    }
                    String finalOperatorStr = operatorStr;
                    eText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            try {
                                // etExpression.setText(moneyUp + "");
                                // 以键值对的形式把 涉及公式字段和值保存到hashmap
                                String value = s.toString();
                                if (TextUtils.isEmpty(s.toString())) {
                                    value = "0";
                                }
                                hashMap.put(fieldName, value);
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
                            //todo
                            String str = finalOperatorStr;
                            Iterator<Map.Entry<String, String>> iter = hashMap
                                    .entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
                                        .next();
                                String key = entry.getKey();//.toLowerCase();
                                String value = entry.getValue();
                                Logger.i(TAG + key + "--" + value);
                                str = str.replace(key, value);
                            }
                            try {
                                // 表达式转为运算符
                                Expression parsiiExpression = Parser.parse(str);
                                double result = parsiiExpression.evaluate();
                                try {
                                    if (String.valueOf(result).lastIndexOf(".") + 4 > 0) {
                                        String substring = String.valueOf(result).substring(0, String.valueOf(result).lastIndexOf(".") + 4);
                                        result = Double.parseDouble(substring);
                                    }
                                } catch (Exception e1) {
                                    Logger.e(TAG + str);
                                }
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

    }


    /**
     * 检查单元格是否绑定单元格控制属性，如果有，改变单元格状态
     */
    private void setFormCellsStatus() {
        for (CellInfo cellInfo : allCellMap.keySet()) {
            controlCellStatus(cellInfo, TextUtils.isEmpty(cellInfo.getValue())
                    ? "" : cellInfo.getValue(), false);
        }
    }


    /**
     * 根据单元格值的变化来控制别的单元格的显示、隐藏、只读或必填
     *
     * @param cellInfo 值变化的单元格的实体
     * @param value    变化后的值
     */
    private void controlCellStatus(CellInfo cellInfo, String value, boolean isResetCellStatus) {
        if (isResetCellStatus) {
            resetChangedCellStatus(cellInfo);
        }
        changeCellStatus(cellInfo, value);
    }


    /**
     * 重置已经变化的单元格的状态
     *
     * @param cellInfo1
     */
    private void resetChangedCellStatus(CellInfo cellInfo1) {
        if (changedCellMap.size() > 0 && !TextUtils.isEmpty(cellInfo1.getFieldsSwitch())) {
            for (CellInfo cellInfo : changedCellMap.keySet()) {
                for (CellInfo info : allCellMap.keySet()) {
                    if (info.getBinding().equals(cellInfo.getBinding())) {
                        //首先把所有单元格设置为显示
                        allCellMap.get(info).setVisibility(View.VISIBLE);

                        //得到需要改变属性的单元格的父布局
                        LinearLayout linearLayout = allCellMap.get(info);
                        //得到他的子view的个数
                        int childCount = linearLayout.getChildCount();
                        //如果单元格被改变为必填,并且改变之前为非必填，恢复为非必填
                        if ("required".equals(changedCellMap.get(cellInfo)) && !cellInfo.getRequired()) {
                            info.setRequired(false);
                            //去掉红色必填星号
                            linearLayout.removeViewAt(0);
                            for (int i = 0; i < childCount; i++) {
                                try {
                                    EditText et = (EditText) linearLayout.getChildAt(i);
                                    et.setHint("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //如果单元格被改变为只读,并且改变之前不是为只读，恢复为非只读
                        if ("readonly".equals(changedCellMap.get(cellInfo)) && !cellInfo.getReadOnly()) {
                            info.setReadOnly(false);
                            linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                            for (int i = 0; i < childCount; i++) {
                                try {
                                    EditText et = (EditText) linearLayout.getChildAt(i);
//                                    et.setFocusable(true);
                                    et.setEnabled(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 根据单元格值的变化来控制别的单元格的显示、隐藏、只读或必填
     *
     * @param cellInfo 值变化的单元格的实体
     * @param value    变化后的值
     */
    private void changeCellStatus(CellInfo cellInfo, String value) {
        String fieldsSwitch = cellInfo.getFieldsSwitch();

        if (!TextUtils.isEmpty(fieldsSwitch)) {
            //得到需要值所对应的需要控制的单元格
            List<TabCellsController> cellsControllers =
                    JsonUtils.jsonToArrayEntity(fieldsSwitch, TabCellsController.class);
            if (cellsControllers != null && cellsControllers.size() > 0) {
                for (TabCellsController cellsController : cellsControllers) {
                    //判断如果显示的值和 需要控制单元格所对应的值 一样
                    if (value.equals(cellsController.getValue())) {
                        List<TabCellsController.Field> hideFields = cellsController.getHideFields();
                        List<TabCellsController.Field> readonlyFields = cellsController.getReadonlyFields();
                        List<TabCellsController.Field> requiredFields = cellsController.getRequiredFields();


                        if (hideFields != null && hideFields.size() > 0) {
                            //控制隐藏需要隐藏的单元格
                            for (TabCellsController.Field hideField : hideFields) {
                                for (CellInfo info : allCellMap.keySet()) {
                                    if (info.getBinding().equals(hideField.getField())) {
                                        //添加单元格信息到变换的单元格map里
                                        changedCellMap.put(info, "hide");

                                        allCellMap.get(info).setVisibility(View.GONE);
                                    }
                                }
                            }
                        }


                        if (readonlyFields != null && readonlyFields.size() > 0) {
                            //控制需要设置只读的单元格
                            for (TabCellsController.Field hideField : readonlyFields) {
                                for (CellInfo info : allCellMap.keySet()) {
                                    if (info.getBinding().equals(hideField.getField())) {
                                        //添加单元格信息到变换的单元格map里
                                        CellInfo info1 = new CellInfo();
                                        info1.setReadOnly(info.getReadOnly());
                                        info1.setBinding(info.getBinding());
                                        changedCellMap.put(info1, "readonly");


                                        //设置背景为只读
                                        allCellMap.get(info).setBackgroundColor
                                                (getResources().getColor(R.color.bg_quarter_gray));
                                        //设置单元格属性为只读
                                        info.setReadOnly(true);


                                        //得到需要改变属性的单元格的父布局
                                        LinearLayout linearLayout = allCellMap.get(info);
                                        //得到他的子view的个数
                                        int childCount = linearLayout.getChildCount();
                                        for (int i = 0; i < childCount; i++) {
                                            try {
                                                EditText et = (EditText) linearLayout.getChildAt(i);
                                                //设置输入框不可编辑
                                                et.setEnabled(false);
//                                                et.setFocusable(false);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }


                        if (requiredFields != null && requiredFields.size() > 0) {
                            //控制需要设置必填的单元格
                            for (TabCellsController.Field hideField : requiredFields) {
                                for (CellInfo info : allCellMap.keySet()) {
                                    if (info.getBinding().equals(hideField.getField())) {
                                        //添加单元格信息到变换的单元格map里
                                        CellInfo info1 = new CellInfo();
                                        info1.setRequired(info.getRequired());
                                        info1.setBinding(info.getBinding());
                                        changedCellMap.put(info1, "required");


                                        //得到需要改变属性的单元格的父布局
                                        LinearLayout linearLayout = allCellMap.get(info);

                                        //得到他的子view的个数
                                        int childCount = linearLayout.getChildCount();
                                        for (int i = 0; i < childCount; i++) {
                                            try {
                                                EditText et = (EditText) linearLayout.getChildAt(i);
                                                et.setHint("必填");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        //添加左侧红星必填标识
                                        TextView tvXing = new TextView(context);
                                        tvXing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                                        tvXing.setTextColor(context.getResources().getColor(R.color.color_red));
                                        tvXing.setText("*");

                                        //如果childCount==2，证明没有红星，再添加
                                        if (childCount == 2) {
                                            linearLayout.addView(tvXing, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
                                     String expression, List<EditText> editTexts) {
        List<EditText> list = new ArrayList<>();
        if (mEditList != editTexts) {
            list.addAll(mEditList);
            list.addAll(editTexts);
        } else {
            list.addAll(mEditList);
        }
        Logger.i(TAG + expression);
        String binding = expression.substring(expression.indexOf("sum(") + 4, expression.indexOf(")"));
        Logger.i("binding：：：" + expression);
        final HashMap<EditText, String> totalMap = new HashMap<>();
        int[] length = new int[list.size()];
        for (int j = 0; j < list.size(); j++) {
            int finalJ = j;
            final EditText etPress = list.get(j);
            final CellInfo info = (CellInfo) etPress.getTag();
//.toLowerCase()
            if (!TextUtils.isEmpty(info.getBinding()) && binding.contains(info.getBinding())) {
                if (!TextUtils.isEmpty(etPress.getText().toString())) {
                    totalMap.put(etPress, etPress.getText().toString());
                }
                int l = 0;//记录edittext变动之前，字符串的长度
                etPress.setInputType(etExpreInputType);
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        length[finalJ] = s.length();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (length[finalJ] != s.toString().length()) {
                            totalMap.put(etPress, s.toString());
                            Iterator<Map.Entry<EditText, String>> iterator = totalMap.entrySet().iterator();
                            Double value = 0.0;
                            while (iterator.hasNext()) {
                                String nextValue = iterator.next().getValue();
                                if (TextUtils.isEmpty(nextValue)) {
                                    nextValue = "0";
                                }
                                try {
                                    value += Double.valueOf(nextValue);
                                } catch (NumberFormatException e) {
//                                    value = String.valueOf(nextValue);
                                }
                            }
                            CellInfo tag = (CellInfo) etExpression.getTag();
                            String dataType = tag.getDataType();
                            if (dataType.equals("int")) {
                                etExpression.setText(value.intValue() + "");
                            } else {
                                etExpression.setText(value + "");
                            }
                        }
                    }
                };
                etPress.addTextChangedListener(textWatcher);
                //添加和删除明细时，显示正确的总和
                Iterator<Map.Entry<EditText, String>> iterator = totalMap.entrySet().iterator();
                String value = "0";
                while (iterator.hasNext()) {
                    String nextValue = iterator.next().getValue();
                    if (TextUtils.isEmpty(nextValue)) {
                        nextValue = "0";
                    }
                    try {
                        value = "" + DoubleUtil.sum(Double.valueOf(value), Double.valueOf(nextValue));
                    } catch (NumberFormatException e) {
                        value = String.valueOf(nextValue);
                    }
                }
                etExpression.setText(value + "");
            }

        }

        if (detailsMap != null && detailsMap.size() > 0) {
            Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries1 = detailsMap.entrySet();
            for (Map.Entry<View, Map<Integer, List<EditText>>> map : entries1) {
                Map<Integer, List<EditText>> map1 = map.getValue();
                Set<Map.Entry<Integer, List<EditText>>> entries = map1.entrySet();
                for (Map.Entry<Integer, List<EditText>> m : entries) {
                    List<EditText> edits = m.getValue();
                    for (EditText et : edits) {
                        CellInfo info = (CellInfo) et.getTag();
                        final String dataType = info.getDataType();
                        if (!TextUtils.isEmpty(info.getBinding()) && binding.toLowerCase().contains(info.getBinding().toLowerCase())) {
                            if (!info.getReadOnly() && !TextUtils.isEmpty(et.getText().toString())) {
                                totalMap.put(et, et.getText().toString());
                            }
                            if (!et.isEnabled()) {
                                totalMap.put(et, et.getText().toString());
                                Iterator<Map.Entry<EditText, String>> iterator = totalMap.entrySet().iterator();
                                double value = 0;
                                String text = "0";
                                while (iterator.hasNext()) {
                                    String nextValue = iterator.next().getValue();
                                    if (TextUtils.isEmpty(nextValue)) {
                                        nextValue = "0";
                                    }
                                    try {
                                        value += Double.valueOf(nextValue);
                                        text = String.valueOf(value);
                                    } catch (NumberFormatException e) {
                                        text = String.valueOf(nextValue);
                                    }
                                }
                                etExpression.setText(text);
                            }
                            et.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    double value = 0;
                                    String text = "0";
                                    try {
                                        totalMap.put(et, s.toString());
                                        Iterator<Map.Entry<EditText, String>> iterator = totalMap.entrySet().iterator();
                                        while (iterator.hasNext()) {
                                            String nextValue = iterator.next().getValue();
                                            if (TextUtils.isEmpty(nextValue)) {
                                                nextValue = "0";
                                            }
                                            try {
                                                value += Double.valueOf(nextValue);
                                                text = String.valueOf(value);
                                            } catch (NumberFormatException e) {
                                                text = String.valueOf(nextValue);
                                            }
                                        }
                                        if (!TextUtils.isEmpty(dataType)) {
                                            try {
                                                if (dataType.equalsIgnoreCase("int")) {
                                                    text = MoneyUtils.formatFloatNumberNoSplit(Double.valueOf(text), false);
                                                } else if (dataType.equalsIgnoreCase("double") || dataType.equalsIgnoreCase("decimal")) {
                                                    text = MoneyUtils.formatFloatNumberNoSplit(Double.valueOf(text), true);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        etExpression.setText(text);
                                    } catch (Exception e) {
                                        etExpression.setText(text);
                                    }
                                }
                            });
                            //添加和删除明细时，显示正确的总和
                            Iterator<Map.Entry<EditText, String>> iterator = totalMap.entrySet().iterator();
                            double value = 0;
                            String text = "0";
                            while (iterator.hasNext()) {
                                String nextValue = iterator.next().getValue();
                                if (TextUtils.isEmpty(nextValue)) {
                                    nextValue = "0";
                                }
                                try {
                                    value += Double.valueOf(nextValue);
                                    text = String.valueOf(value);
                                } catch (NumberFormatException e) {
                                    try {
                                        text = String.valueOf(nextValue);
                                    } catch (Exception e1) {
                                        text = "";
                                    }
                                }
                            }
                            if (!TextUtils.isEmpty(dataType)) {
                                try {
                                    if (dataType.equalsIgnoreCase("int")) {
                                        text = MoneyUtils.formatFloatNumberNoSplit(Double.valueOf(text), false);
                                    } else if (dataType.equalsIgnoreCase("double") || dataType.equalsIgnoreCase("decimal")) {
                                        text = MoneyUtils.formatFloatNumberNoSplit(Double.valueOf(text), true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            etExpression.setText(text);
                        }
                    }
                }

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
     * 获取pdf
     */
    private void getFormPdf() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.表单转为PDF;
//        String json = mFormJson.replaceAll(mFormJson.substring(mFormJson.indexOf("mainField") + 12, mFormJson.indexOf("mainField") + 12
//                + mFormJson.substring(mFormJson.indexOf("mainField") + 12).indexOf("\"")), "");
        String json = mFormJson.replaceAll("&&", "");
        HashMap<String, String> map = new HashMap<>();
        map.put("jsonVsheetDef", json);
        map.put("comments", "");
        map.put("lcgcList", "");
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                String fileName = JsonUtils.pareseData(response);
                if (!TextUtils.isEmpty(fileName)) {
                    downloadTempFile(fileName);
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

    private void downloadTempFile(String FileName) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.下载附件 + FileName;
        String time = ViewHelper.getCurrentFullTime();
        time = time.replaceAll(" ", "");
        time = time.replaceAll(":", "-");
        String fileName = formName + time + ".pdf";
        StringRequest.downloadFile(url, fileName, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("已下载到文件管理sd卡根目录中");
                OpenIntentUtils.openFile(context,
                        FilePathConfig.getCacheDirPath() + "/" + fileName);
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


    /**
     * 供js调用，隐藏单元格
     *
     * @param bindingArr 要隐藏单元格的binding
     */
    public void hide(V8Array bindingArr) {
        hideDetail(bindingArr, null, "");
    }


    /**
     * 供js调用，隐藏单元格
     *
     * @param bindingArr 要隐藏单元格的binding
     * @param detailName 明细名称
     * @param rowIndex   明细行号
     */
    public void hideDetail(V8Array bindingArr, Integer rowIndex, String detailName) {
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(rowIndex);
                    if (editTexts != null) {
                        hideShowCell(bindingArr, true, editTexts);
                    }
                }
            }
        } else {
            hideShowCell(bindingArr, true, mEditList);
        }

    }


    private void hideShowCell(V8Array bindingArr, boolean isHide, List<EditText> editTexts) {
        for (EditText editText : editTexts) {
            CellInfo cellInfo = (CellInfo) editText.getTag();
            for (int i = 0; i < bindingArr.length(); i++) {
                String binding = (String) bindingArr.get(i);
                if (cellInfo.getBinding().equals(binding)) {
                    LinearLayout parent = (LinearLayout) editText.getParent();
                    if (isHide) {
                        parent.setVisibility(View.GONE);
                    } else {
                        parent.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    /**
     * 供js调用，获取一个单元格的值
     *
     * @param binding 要获取值的单元格的binding
     * @return
     */
    public String getValue(String binding) {
        return getValueDetail(binding, null, "");
    }

    /**
     * 供js调用，获取一个单元格的值
     *
     * @param binding 要获取值的单元格的binding
     * @return
     */
    public String getQueryString(String binding) {
        if ("id".equals(binding)) {
            return formDataId;
        }
        return "";
    }

    /**
     * 供js调用，获取一个单元格的值
     *
     * @param binding    要获取值的单元格的binding
     * @param detailName 明细名称
     * @param rowIndex   明细行号
     * @return
     */
    public String getValueDetail(String binding, Integer rowIndex, String detailName) {
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(Integer.valueOf(rowIndex));
                    if (editTexts != null) {
                        for (EditText editText : editTexts) {
                            CellInfo cellInfo = (CellInfo) editText.getTag();
                            if (cellInfo.getBinding().equals(binding)) {
                                return cellInfo.getValue();
                            }
                        }
                    }
                }
            }
        } else {
            for (EditText editText : mEditList) {
                CellInfo cellInfo = (CellInfo) editText.getTag();
                if (cellInfo.getBinding().equals(binding)) {
                    if ("textbox".equalsIgnoreCase(cellInfo.getCellStyle())) {
                        cellInfo.setValue(editText.getText().toString());
                    }
                    return cellInfo.getValue();
                }
            }
        }
        return "";
    }


    /**
     * 供js调用，获取一个单元格的值
     *
     * @param bindingArr 要获取值的单元格的bindings
     * @param isReadOnly 是否设为只读
     * @return
     */
    public void setReadonly(V8Array bindingArr, Boolean isReadOnly) {
        setReadonlyDetail(bindingArr, isReadOnly, null, "");
    }

    /**
     * 供js调用，获取一个单元格的值
     *
     * @param bindingArr 要获取值的单元格的bindings
     * @param detailName 明细名称
     * @param isReadOnly 是否设为只读
     * @param rowIndex   明细行号
     * @return
     */
    public void setReadonlyDetail(V8Array bindingArr, Boolean isReadOnly, Integer rowIndex, String detailName) {//
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(rowIndex);
                    if (editTexts != null) {
                        setCellReadonly(bindingArr, isReadOnly, editTexts);
                    }
                }
            }
        } else {
            setCellReadonly(bindingArr, isReadOnly, mEditList);
        }
    }

    private void setCellReadonly(V8Array bindingArr, Boolean isReadOnly, List<EditText> editTexts) {
        for (EditText editText : editTexts) {
            CellInfo cellInfo = (CellInfo) editText.getTag();
            for (int i = 0; i < bindingArr.length(); i++) {
                String binding = (String) bindingArr.get(i);
                if (cellInfo.getBinding().equals(binding)) {
                    LinearLayout parent = (LinearLayout) editText.getParent();
                    if (isReadOnly) {
                        editText.setEnabled(false);
                        parent.setBackgroundColor(getResources().getColor(R.color.bg_quarter_gray));
                    } else {
                        editText.setEnabled(true);
                        parent.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
            }
        }
    }

    /**
     * 供js调用，设置单元格是否必填
     *
     * @param bindingArr 要设置必填的单元格的bindings
     * @param isRequired 是否设为只读
     * @return
     */
    public void setRequired(V8Array bindingArr, Boolean isRequired) {
        setRequiredDetail(bindingArr, isRequired, null, "");
    }

    /**
     * 供js调用，设置单元格是否必填
     *
     * @param bindingArr 要设置必填的单元格的bindings
     * @param detailName 明细名称
     * @param isRequired 是否设为只读
     * @param rowIndex   明细行号
     * @return
     */
    public void setRequiredDetail(V8Array bindingArr, Boolean isRequired, Integer rowIndex, String detailName) {
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(rowIndex);
                    if (editTexts != null) {
                        setCellRequired(bindingArr, isRequired, editTexts);
                    }
                }
            }
        } else {
            setCellRequired(bindingArr, isRequired, mEditList);
        }
    }

    /**
     * 供js调用，设置单元格是否必填
     *
     * @param bindingArr 要设置必填的单元格的bindings
     * @param isRequired 是否设为只读
     * @param editTexts  明细行号
     * @return
     */
    private void setCellRequired(V8Array bindingArr, Boolean isRequired, List<EditText> editTexts) {
        for (EditText editText : editTexts) {
            CellInfo cellInfo = (CellInfo) editText.getTag();
            for (int i = 0; i < bindingArr.length(); i++) {
                String binding = (String) bindingArr.get(i);
                if (cellInfo.getBinding().equals(binding)) {
                    LinearLayout linearLayout = (LinearLayout) editText.getParent();
                    //得到他的子view的个数
                    int childCount = linearLayout.getChildCount();
                    if (isRequired) {
                        if (!cellInfo.getRequired()) {
                            for (int j = 0; j < childCount; j++) {
                                try {
                                    TextView et = (TextView) linearLayout.getChildAt(j);
                                    et.setHint("必填");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            //添加左侧红星必填标识
                            TextView tvXing = new TextView(context);
                            tvXing.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                            tvXing.setTextColor(context.getResources().getColor(R.color.color_red));
                            tvXing.setText("*");

                            //如果childCount==2，证明没有红星，再添加
                            if (childCount == 2) {
                                linearLayout.addView(tvXing, 0);
                            }
                        }
                    } else {
                        if (cellInfo.getRequired()) {
                            linearLayout.removeViewAt(0);
                            for (int j = 0; j < childCount; j++) {
                                try {
                                    EditText et = (EditText) linearLayout.getChildAt(j);
                                    et.setHint("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    cellInfo.setRequired(isRequired);
                }
            }
        }
    }

    /**
     * 给主表单元格设置值
     *
     * @param binding
     * @param value
     */
    public void setValue(String binding, String value) {
        setValueDetail(binding, value, null, "");
    }

    /**
     * 给明细单元格设置值
     *
     * @param binding
     * @param value
     * @param rowIndex
     * @param detailName
     */
    public void setValueDetail(String binding, String value, Integer rowIndex, String detailName) {
        if ("undefined".equals(value)) { //如果要设置的值是undefined，不做处理
            return;
        }
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(rowIndex);
                    if (editTexts != null) {
                        setCellValue(binding, value, editTexts);
                    }
                }
            }
        } else {
            setCellValue(binding, value, mEditList);
        }
    }

    /**
     * 给单元格设置值
     *
     * @param binding   要设置值的单元格的binding
     * @param value     要设置的值
     * @param editTexts
     */
    private void setCellValue(String binding, String value, List<EditText> editTexts) {

        for (EditText editText : editTexts) {
            CellInfo cellInfo = (CellInfo) editText.getTag();
            //先取出要设置值的单元格
            if (binding.equals(cellInfo.getBinding())) {
                cellInfo.setValue(value);

                if ("textbox".equals(cellInfo.getCellStyle()) || "textarea".equals(cellInfo.getCellStyle())) {
                    editText.setText(StrUtils.pareseNull(value));
                } else if ("combobox".equals(cellInfo.getCellStyle())
                        || "dropdownlist".equals(cellInfo.getCellStyle())
                        || "checklistbox".equals(cellInfo.getCellStyle())) {
                    //去除单元格绑定的字典
                    final Map<String, String> dictHashMap = mDictionaries.get(cellInfo.getDict());
                    //将字典的UUID分割成数组
                    String[] dictIds = value.split(",");
                    StringBuilder dictNamesBuilder = new StringBuilder();
                    for (String dictId : dictIds) {
                        try {
                            //从字典集合里面去除字典值并拼接起来
                            dictNamesBuilder.append(dictHashMap.get(dictId))
                                    .append(",");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (dictNamesBuilder.length() > 0) {
                        editText.setText(dictNamesBuilder.substring(0,
                                dictNamesBuilder.length() - 1));
                    }else{
                        editText.setText("");
                    }
                } else if ("datepicker".equals(cellInfo.getCellStyle())) {
                    if (!TextUtils.isEmpty(cellInfo.getFormat())) {
                        String formatStr = cellInfo.getFormat();

                        // 服务器表单配置format 没有区分大小写
                        // 导致格式转换报错，若时间格式不对，会导致所有表单时间都会默认当前时间值，以及新建周报评论，申请单时保存不了
                        String format = (!TextUtils.isEmpty(formatStr) && formatStr
                                .contains("yyyy-mm-dd")) ? formatStr.replaceAll("yyyy-mm-dd",
                                "yyyy-MM-dd") : formatStr;
                        if (!TextUtils.isEmpty(format) && format.endsWith(":ss")) {
                            format = format.replaceAll(":ss", "");
                        }
                        String text = ViewHelper.convertStrToFormatDateStr(value,
                                format);
                        editText.setText(text);
                    } else {
                        editText.setText(value);
                    }
                }


            }
        }
    }


    /**
     * 显示一个单元格
     *
     * @param bindingArr
     */
    public void show(V8Array bindingArr) {
        showDetail(bindingArr, null, "");
    }

    /**
     * 显示一条明细
     *
     * @param bindingArr
     * @param rowIndex
     * @param detailName
     */
    public void showDetail(V8Array bindingArr, Integer rowIndex, String detailName) {
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(rowIndex);
                    if (editTexts != null) {
                        hideShowCell(bindingArr, false, editTexts);
                    }
                }
            }
        } else {
            hideShowCell(bindingArr, false, mEditList);
        }

    }

    public void setDictionary(String binding, V8Array value) {
        setDictionaryDetail(binding, value, null, "");
    }

    /**
     * 设置明细的字典项
     *
     * @param binding
     * @param value
     * @param rowIndex
     * @param detailName
     */
    public void setDictionaryDetail(String binding, V8Array value, Integer rowIndex, String detailName) {
        if (rowIndex != null && !TextUtils.isEmpty(detailName)) {
            for (Map.Entry<LinearLayout, String> Entry : detailTitles.entrySet()) {
                if (detailName.equals(Entry.getValue())) {
                    Map<Integer, List<EditText>> integerListMap = detailsMap.get(Entry.getKey());
                    List<EditText> editTexts = integerListMap.get(rowIndex);
                    if (editTexts != null) {
                        for (EditText editText : editTexts) {
                            CellInfo info = (CellInfo) editText.getTag();
                            if (binding.equals(info.getBinding())) {
                                if (!TextUtils.isEmpty(info.getDict())) {
                                    if (("出库单".equals(formName) || "入库单".equals(formName))
                                            && "skuId".equals(info.getBinding())) {
                                        info.setDict(info.getDict() + rowIndex);
                                    }
                                    LinkedHashMap<String, String> replaceMap = new LinkedHashMap<>();
                                    for (int i = 0; i < value.length(); i++) {
                                        V8Object object = (V8Object) value.get(i);
                                        String[] keys = object.getKeys();
                                        replaceMap.put((String) object.get(keys[0]), (String) object.get(keys[1]));
                                    }
                                    if ("name".equals(info.getDisplayMemberPath())) {
                                        mDictionaries.put(info.getDict(), replaceMap);
                                    } else {
                                        mDictionaries.put(info.getDict() + "." + info.getDisplayMemberPath(), replaceMap);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            for (EditText editText : mEditList) {
                CellInfo info = (CellInfo) editText.getTag();
                if (binding.equals(info.getBinding())) {
                    if (!TextUtils.isEmpty(info.getDict())) {
                        Map<String, String> replaceMap = new HashMap<>();
                        for (int i = 0; i < value.length(); i++) {
                            V8Object object = (V8Object) value.get(i);
                            String[] keys = object.getKeys();
                            for (String key : keys) {
                                replaceMap.put(key, (String) object.get(key));
                            }
                        }
                        if ("name".equals(info.getDisplayMemberPath())) {
                            mDictionaries.put(info.getDict(), replaceMap);
                        } else {
                            mDictionaries.put(info.getDict() + "." + info.getDisplayMemberPath(), replaceMap);
                        }
                    }
                }
            }
        }
    }

    public void getDictionary(String binding, String value) {

    }

    /**
     * 获取明细的 字典
     *
     * @param binding
     * @param rowIndex
     * @param detailName
     */
    public void getDictionaryDetail(String binding, Integer rowIndex, String detailName) {

    }


    /**
     * 添加一条明细
     *
     * @param detailName 明细名称
     */
    public void add(String detailName) {
        List<CellInfo> list = new ArrayList<>();
        List<CellInfo> detailList = new ArrayList<>();
        for (MuitiDetails muitiDetail : muitiDetails) {
            if (detailName.equals(muitiDetail.getDetailName())) {
                list = muitiDetail.getContent().get(0);
            }
        }
        if (list.size() > 0) {
            for (CellInfo info : list) {  //一个空的明细行，添加的时候添加空的明细行
                CellInfo infoNew = new CellInfo();
                try {
                    InvokeUtils.reflectionAttr(info, infoNew);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                infoNew.setText("");
                infoNew.setValue("");
                detailList.add(infoNew);
            }
        }
        LinearLayout linearLayout = null;
        int index = 1;
        for (int i = 0; i < ll_details.getChildCount(); i++) {
            View view = ll_details.getChildAt(i);
            if (view instanceof LinearLayout) {
                if (index == 1) { //第一个LinearLayout是明细头布局 跳过
                    index += 1;
                } else {
                    linearLayout = (LinearLayout) view;
                    break;
                }
            }
        }

        CreateDetailLayout(detailList, linearLayout, linearLayout.getChildCount() - 1, true);
    }


    /**
     * 请求接口，返回数据
     *
     * @param url
     * @param type   调用的接口的获取方式：get/post
     * @param params 参数
     * @return
     */
    public V8Object request(String url, String type, V8Object params, V8Object callBack) {
        CountDownLatch latch = new CountDownLatch(1);
        String URL = Global.BASE_JAVA_URL + url;
        final JSONObject[] result = {new JSONObject()};
        final String[] resp = {""};
        if ("get".equals(type)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringRequest.getSync(URL, new StringResponseCallBack() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                resp[0] = response;
                                result[0] = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            latch.countDown();
                        }

                        @Override
                        public void onFailure(Request request, Exception ex) {

                        }

                        @Override
                        public void onResponseCodeErro(String result) {
                            latch.countDown();
                        }
                    });
                }
            }).start();
        } else {
            Map<String, String> paramsKey = new HashMap<>();
            if (params != null) {
                if (((params.getString("method"))).toString().equals("undefined")) {
                    String[] keys = params.getKeys();
                    for (String key : keys) {
                        paramsKey.put(key, (String) params.get(key));
                    }
                } else {
                    paramsKey.put("method", (String) params.get("method"));
                    paramsKey.put("workflowTemplateId", workflowTemplateId);
                    if (params.get("paramsJson") != null) {
                        String paramsJson = "";
                        if (params.get("paramsJson") instanceof String) {
                            paramsJson = (String) params.get("paramsJson");
                        } else if (params.get("paramsJson") instanceof V8Object) {
                            V8Object object = (V8Object) params.get("paramsJson");
                            HashMap<String, Object> map = new HashMap<>();
                            returnParaMap(map, object);
                            object.release();
                            Gson gson = new Gson();
                            paramsJson = gson.toJson(map);
                        }
                        paramsKey.put("paramsJson", paramsJson);
                    }
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringRequest.postSync(URL, paramsKey, new StringResponseCallBack() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                resp[0] = response;
                                result[0] = new JSONObject(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            latch.countDown();
                        }

                        @Override
                        public void onFailure(Request request, Exception ex) {

                        }

                        @Override
                        public void onResponseCodeErro(String result) {
                            latch.countDown();
                        }
                    });
                }
            }).start();
        }

        try {
            latch.await();//等到计数为0时才会继续执行下去
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (v8Object != null && !v8Object.isReleased()) {
            v8Object.release();
        }
        v8Object = new V8Object(v8);
        JSONObject jsonObject = result[0];
        try {
            v8Object.add("Status", jsonObject.getString("Status"));
            Object data = jsonObject.get("Data");
            if (data instanceof String) {
                v8Object.add("Data", (String) data);
            } else if (data instanceof Double) {
                v8Object.add("Data", (Double) data);
            } else if (data instanceof Boolean) {
                v8Object.add("Data", (Boolean) data);
            } else if (data instanceof Integer) {
                v8Object.add("Data", (Integer) data);
            } else if (data instanceof JSONObject) {
                v8Value = new V8Object(v8);
                JSONObject object = (JSONObject) data;
                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = object.getString(key);
                    v8Value.add(key, value);
                }
                v8Object.add("Data", v8Value);
                v8Value.release();
            } else if (data instanceof JSONArray) {
                V8Array v8Array = new V8Array(v8);
                for (int i = 0; i < ((JSONArray) data).length(); i++) {
                    V8Object v8Object1 = new V8Object(v8);
                    JSONObject object = (JSONObject) ((JSONArray) data).get(i);
                    Iterator<String> keys = object.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        String value = object.getString(key);
                        v8Object1.add(key, value);
                    }
                    v8Array.push(v8Object1);
                    v8Object1.release();
                }
                v8Object.add("Data", v8Array);
                v8Array.release();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String res = "var res =" + resp[0] + ";";
            if (callBack != null && !TextUtils.isEmpty(callBack.toString())) {
                String jsBody = callBack.toString().substring
                        (callBack.toString().indexOf("{") + 1, callBack.toString().lastIndexOf("}"));
                v8.executeVoidScript(res + jsBody);
//                Object object1 = new JSONObject(resp[0]);
//                V8Function function = new V8Function(v8, new JavaCallback() {
//                    @Override
//                    public Object invoke(V8Object v8Object, V8Array v8Array) {
//                        return null;
//                    }
//                });
//                function.call()
//                V8Array array = new V8Array(v8);
//                array.push(object1);
//                v8.executeFunction("callback",array);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (params != null) {
            params.release();
        }
        if (callBack != null) {
            callBack.release();
        }
        return v8Object;
    }


    private void returnParaMap(HashMap<String, Object> map, V8Object object) {
        String[] keys = object.getKeys();
        for (String key : keys) {
            List<HashMap<String, String>> list = new ArrayList<>();
            HashMap<String, String> map1 = new HashMap<>();
            if (object.get(key) instanceof String) {
                map.put(key, object.getString(key));
            } else if (object.get(key).toString().equals("undefined")) {
//                map.put(key, "");
            } else if (object.get(key) instanceof V8Object) {
                V8Object object1 = (V8Object) object.get(key);
                String[] keys1 = object1.getKeys();
                for (String s : keys1) {
                    if (object1.get(s) instanceof V8Object) {
                        V8Object object2 = (V8Object) object1.get(s);
                        String[] keys2 = object2.getKeys();
                        for (String s1 : keys2) {
                            if (object2.get(s1) instanceof String) {
                                map1.put(s1, object2.getString(s1));
                            }
                        }
                        object2.release();
                    } else if (object1.get(s) instanceof String) {

                    }
                }
                list.add(map1);
                map.put(key, list);
                object1.release();
            }

        }
    }


    /**
     * 调用固定的方法，传入动态的参数，获取动态的返回数据
     *
     * @param method 方法名
     * @param map    参数
     * @return
     */
    public V8Object asyncRpc(String method, V8Object map, V8Object callBack) {
        V8Object object = new V8Object(v8);
        object.add("method", method);
        object.add("workflowTemplateId", workflowTemplateId);
        object.add("paramsJson", map);
        return request("/wf/form/vsheet/rpc", "post", object, callBack);
    }

    /**
     * 调用固定的方法，传入动态的参数，获取动态的返回数据
     *
     * @return
     */
    public void onsave(V8Object callBack, V8Object errorCallBack) {
        try {
            saveCallBack = callBack.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取表单的全部数据
     *
     * @return 表单的全部数据
     */
    public V8Object getFormData() {
        V8Object v8Array = new V8Object(v8);
        //放入 主表数据
        for (EditText editText : mEditList) {
            V8Object v8Object1 = new V8Object(v8);
            CellInfo info = (CellInfo) editText.getTag();
            if (("textbox".equalsIgnoreCase(info.getCellStyle())
                    || "textarea".equalsIgnoreCase(info.getCellStyle()))
                    && TextUtils.isEmpty(info.getDict())) {
                // 判断字典项
                String content = editText.getText().toString().trim();
                info.setValue(content);
            }
            v8Array.add(info.getBinding(), info.getValue() == null ? "" : info.getValue());
        }


        Set<Map.Entry<View, Map<Integer, List<EditText>>>> entries = detailsMap.entrySet();
        for (Map.Entry<View, Map<Integer, List<EditText>>> entry : entries) {
            //得到明细标题
            String detailTitle = detailTitles.get(entry.getKey());
            Map<Integer, List<EditText>> map = entry.getValue();
            V8Array detailArray = new V8Array(v8);
            for (Map.Entry<Integer, List<EditText>> listEntry : map.entrySet()) {
                V8Object v8Object1 = new V8Object(v8);
                for (EditText editText : listEntry.getValue()) {
                    V8Object object = new V8Object(v8);
                    CellInfo info = (CellInfo) editText.getTag();
                    if (("textbox".equalsIgnoreCase(info.getCellStyle())
                            || "textarea".equalsIgnoreCase(info.getCellStyle()))
                            && TextUtils.isEmpty(info.getDict())) {
                        // 判断字典项
                        String content = editText.getText().toString().trim();
                        info.setValue(content);
                    }
                    v8Object1.add(info.getBinding(), info.getValue());
                }
                detailArray.push(v8Object1);
                v8Object1.release();
            }
            v8Array.add(detailTitle, detailArray);
            detailArray.release();
        }

        return v8Array;
    }

    public void message(String msg, String type) {
        showShortToast(msg);
    }


    /**
     * 调用js方法，创建一个原生按钮，点击按钮可以调用js方法
     *
     * @param text    按钮显示的文字
     * @param methord 点击按钮需要调用的方法
     */
    public void createButton(String text, String methord) {
        tvTestBtn.setVisibility(View.VISIBLE);
        tvTestBtn.setText(text);

        tvTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isHaveFunction = false;
                    for (String functionArr : jsFunctionArrs) {
                        if (functionArr.contains(methord) && !functionArr.contains("onload()")) {
                            isHaveFunction = true;

                            int beginIndex = functionArr.indexOf("{");
                            int endIndex = functionArr.indexOf("}");

                            String str = functionArr.substring(beginIndex + 1, endIndex - 1);
                            String[] split = str.split("\n\t");
                            for (String s : split) {
                                v8.executeVoidScript(s);
                            }
                        }
                    }
                    if (!isHaveFunction) {
                        v8.executeVoidScript(methord);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 多选选择项，跳转页面选择后执行js方法
     *
     * @param method   方法名称
     * @param callBack
     */
    public void multipleSelectList(String method, String callBack) {
        JSSelectFunction = callBack;
        List<Map<String, String>> maps = new ArrayList<>();

        //得到接口返回的数据
        V8Object object = asyncRpc(method, null, null);
        //拿出来以后循环，放到list中
        V8Array array = (V8Array) object.get("Data");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                Map<String, String> map = new HashMap<>();
                V8Object v8Object = (V8Object) array.get(i);
                String[] keys = v8Object.getKeys();
                for (String key : keys) {
                    map.put(key, v8Object.getString(key));
                }
                maps.add(map);
            }
        }

        Intent intent = new Intent(context, FormJsSelectActivity.class);
        intent.putExtra("FormJsSelect", (Serializable) maps);
        intent.putExtra("isSingleSelect", false);
        startActivityForResult(intent, RESULT_JS_FORM_MUTI_SELECT);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    /**
     * 单选选择项，跳转页面选择后执行js方法
     *
     * @param method   方法名称
     * @param callBack
     */
    public void singleSelectList(String method, String callBack) {
        JSSelectFunction = callBack;
        List<Map<String, String>> maps = new ArrayList<>();

        //得到接口返回的数据
        V8Object object = asyncRpc(method, null, null);
        //拿出来以后循环，放到list中
        V8Array array = (V8Array) object.get("Data");
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                Map<String, String> map = new HashMap<>();
                V8Object v8Object = (V8Object) array.get(i);
                String[] keys = v8Object.getKeys();
                for (String key : keys) {
                    map.put(key, v8Object.getString(key));
                }
                maps.add(map);
            }
        }

        Intent intent = new Intent(context, FormJsSelectActivity.class);
        intent.putExtra("FormJsSelect", (Serializable) maps);
        intent.putExtra("isSingleSelect", true);
        startActivityForResult(intent, RESULT_JS_FORM_SINGLE_SELECT);
    }


    /**
     * 审批意见Adapter
     */
    private CommanAdapter<FormComment> getCommentAdapter(List<FormComment> list) {
        return new CommanAdapter<FormComment>(list, context, R.layout.item_form_comment_list) {
            @Override
            public void convert(int position, FormComment item, BoeryunViewHolder viewHolder) {

                if (item != null) {

                    viewHolder.setTextValue(R.id.tv_form_commentter, item.getDeptUser()); //部门人员

                    viewHolder.setTextValue(R.id.tv_form_comment_time, item.getProcessTime()); //时间

                    viewHolder.setTextValue(R.id.tv_form_comment_type, item.getName()); //意见类型

                    viewHolder.setTextValue(R.id.tv_form_content, item.getOpinion()); //意见

                }


            }
        };
    }


    /***
     * 判断类型是否是省市县选择类型
     *
     * @return
     * @formName 表单名称
     */
    private boolean isPrivinceType(String formName) {
        return "省".equals(formName) || "市".equals(formName)
                || "县".equals(formName);
    }


}
