package com.biaozhunyuan.tianyi.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.attendance.BaiduPlace;
import com.biaozhunyuan.tianyi.attendance.LocationListActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;
import com.biaozhunyuan.tianyi.contact.ConatactLaneFragment;
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.contact.CustomerContactListActivity;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.BaiduLocator;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.project.Project;
import com.biaozhunyuan.tianyi.project.ProjectListActivity;
import com.biaozhunyuan.tianyi.project.ProjectRecordFragment;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.supportAndComment.SupportListPost;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BottomCommentView;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;
import com.biaozhunyuan.tianyi.view.TimePickerView;
import com.biaozhunyuan.tianyi.view.VoiceInputView;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.contact.ContactNewActivity.EXTRA_CLIENT_ID;
import static com.biaozhunyuan.tianyi.contact.ContactNewActivity.EXTRA_CLIENT_NAME;
import static com.biaozhunyuan.tianyi.helper.SelectLocationBiz.SELECT_LOCATION_CODE;

/**
 * 客户项目通用的联系记录
 */

public class AddRecordActivity extends AppCompatActivity implements BDLocationListener {

    private BoeryunHeaderView headerView;
    private EditText et_content; //联系内容
    private MultipleAttachView attachView;
    private VoiceInputView voiceInputView;
    private TextView tv_projectName; //项目名称
    private LinearLayout ll_projectName; //项目名称父容器
    private TextView tvLocation; //定位
    private TextView tv_customerName; //客户名称
    private TagFlowLayout tgf_time; //选择联系时间
    private TagFlowLayout tgf_nextTime; //选择下次联系时间
    private TagFlowLayout tgf_stage; //选择阶段
    private TagFlowLayout flow_view;
    private TextView tv_stage; //阶段
    private TextView et_contactTime; //联系时间
    private TextView et_nextContactTime; //下次联系时间
    private EditText et_nextContact; //下次联系内容
    private TimePickerView pickerView;
    private TimePickerView nextPickerView;
    private Context mContext;
    private Toast toast;
    private String TAG = getClass().getSimpleName();
    private HashMap<String, ArrayList<表单字段>> mFormDataMap;
    private DictionaryHelper helper;
    private List<BaiduPlace> bpList;

    /**
     * 展开和收缩 字典项的箭头
     */
    private ImageView ivShowProjectTag;
    private ImageView ivShowStageTag;
    private ImageView ivShowWayTag;
    private ImageView ivShowTimeTag;
    private ImageView ivShowNextTimeTag;


    private LinearLayout llProjectStage;
    private LinearLayout llStage;
    private LinearLayout llway;
    private LinearLayout llTime;
    private LinearLayout llNextTime;

    private boolean isShowProjectTag = false;
    private boolean isShowStageTag = false;
    private boolean isShowWayTag = false;
    private boolean isShowTimeTag = false;
    private boolean isShowNextTimeTag = false;
    /**
     * 上传到服务器的经纬度
     */
    private static final double ERROR_LAT_LNG = 4.9E-324;
    double mLatitude = ERROR_LAT_LNG;
    double mLongitude = ERROR_LAT_LNG;
    private int radiusMap = 150;// 默认区域半径
    private List<字典> mDictStageList;
    private List<字典> mDictWayList;
    private Set<Integer> mSelectPosSet = new HashSet<Integer>();

    /**
     * 记录初次定位的经纬度
     */
    private double mLat;
    private double mLog;
    /**
     * 定位所需字段
     */
    String mLocation = null;
    private String mCity;
    private String mCountry;
    private ChClientBiz mClientBiz;
    private LinearLayout ll_root;
    private boolean today = true;

    private static String YEAR_MONTH_DAY_HOUR_MINUTE = "yyyy-MM-dd HH:mm"; // format 年月日时分
    private List<String> nextTimeList;
    private List<String> timeList;
    private List<String> flowViewList;
    private List<String> projectNameList = new ArrayList<>();
    private List<字典> timeListDict; //
    private List<字典> timeNextListDict;
    private LayoutInflater mInflater;
    private Contact mContact;
    private TagFlowLayout tgf_contactWay; //选择联系方式
    private TextView tv_contactWay; //联系方式
    private TextView tv_support; //点赞数量
    private TextView tv_nocomment; //暂无评论
    private TextView tv_comment; //评论数量
    private ImageView iv_support; //点赞
    private TextView tv_supportuser; //点赞人
    private TextView tv_viewcount; //浏览量
    private String supportUser = ""; //点赞人名称
    private LinearLayout ll_support; //点赞父容器
    private LinearLayout ll_viewcount; //浏览量父容器
    private LinearLayout ll_parent; //父容器
    private BottomCommentView commentView; //父容器
    private NoScrollListView lv; //评论列表
    private LinearLayout ll_customer;
    private LinearLayout ll_support_comment_parent;
    private ImageView iv_connment; //评论
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private TagFlowLayout tgf_project; //选择关联项目
    private TextView tv_project; //项目
    private LinearLayout ll_project; //项目的容器
    private RelativeLayout rl_project; //关联项目选择器容器
    private List<Project> projectDataList;
    private boolean isFromCustomer = true; //判断是从客户还是项目跳转进来的
    private BaiduPlace mBaiduPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        initView();
        initData();
        getIntentData();
        if (today && mContact != null && StrUtils.pareseNull(mContact.getAdvisorId()).equals(Global.mUser.getUuid())) {
            initTagData();
            if (!isFromCustomer) {
                getCustomDicts("crm_project_stage");
            } else {
                getCustomDicts("dict_contact_stage");
            }
            getCustomDicts("dict_contact_way");
            if (isFromCustomer) {
                getProjectNames();
            }
            requestLocating(); //定位
        }
        setOnTouchEvent();
    }

    /**
     * 获取可关联的项目
     */
    private void getProjectNames() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情项目列表 + mContact.getCustomerId();
        Demand<Project> demand = new Demand(Project.class);
        demand.pageSize = 10;
        demand.sortField = "createTime desc";
        demand.src = url;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                projectDataList = demand.data;
                if (projectDataList.size() > 0) {
                    rl_project.setVisibility(View.VISIBLE);
                    for (Project p : projectDataList) {
                        projectNameList.add(p.getName());
                    }
                    TagAdapter<String> tagAdapter = new TagAdapter<String>(projectNameList) {
                        @Override
                        public View getView(FlowLayout parent, int position, String t) {
                            Logger.i("tagA" + position + "--" + t);
                            final TextView tv = (TextView) mInflater.inflate(
                                    R.layout.item_label_customer_list, tgf_project, false);
                            tv.setText(t);
                            return tv;
                        }
                    };
                    tgf_project.setAdapter(tagAdapter);
                    tagAdapter.setSelectedList(0);
                    if (!TextUtils.isEmpty(tv_project.getText().toString())) {
                        for (int i = 0; i < projectDataList.size(); i++) {
                            if (projectDataList.get(i).getUuid().equals(mContact.getProjectId())) {
                                tagAdapter.setSelectedList(i);
                            }
                        }
                    }
                } else {
                    ll_project.setVisibility(View.GONE);
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
        if (getIntent().getSerializableExtra("dynamicInfo") != null) { //从动态中进入
            Dynamic dynamicInfo = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            getDynamicInfo(dynamicInfo);
        } else {
            if (getIntent().getSerializableExtra("project_add") != null) { //从项目新建
                Project project = (Project) getIntent().getSerializableExtra("project_add");
                mContact = new Contact();
                mContact.setAdvisorId(Global.mUser.getUuid());
                setContactTime(mContact); //联系时间默认为当前时间
                isFromCustomer = false;
                setRelevance(project.getName(), project.getUuid()); //关联项目
                getCustomerFormById("0");//获取动态字段
            } else if (getIntent().getSerializableExtra("project") != null) { //从项目列表查看
                mContact = (Contact) getIntent().getSerializableExtra("project");
                isFromCustomer = false;
                setRelevance(mContact.getName(), mContact.getProjectId()); //关联项目
                getCustomerFormById(mContact.getUuid()); //获取动态字段
                headerView.setTitle("编辑" + Global.CONTACT_TITLE);
                setEnable(mContact.getContactTime()); //设置联系记录是否只读
                if (mContact.isHiddenMore()) {
                    headerView.setRightTitleVisible(false);
                }
                setSupportAndCommentData(mContact); //设置点赞和评论数据
            } else if (getIntent().getExtras().getSerializable("contactInfo") != null) { //从客户详情联系记录查看
                mContact = (Contact) getIntent().getExtras().getSerializable("contactInfo");
                headerView.setTitle("编辑" + Global.CONTACT_TITLE);
                isFromCustomer = true;
                setRelevance(StrUtils.pareseNull(mContact.getCustomerName()), StrUtils.pareseNull(mContact.getCustomerId()));//关联客户
                getCustomerFormById(mContact.getUuid());//获取动态字段
                setEnable(mContact.getContactTime()); //设置联系记录是否只读
                setSupportAndCommentData(mContact); //设置点赞和评论数据
            } else if (getIntent().getExtras().getString(EXTRA_CLIENT_NAME) != null) { //从客户详情联系记录新建
                String clientName = getIntent().getStringExtra(EXTRA_CLIENT_NAME);
                String advisorId = getIntent().getStringExtra("advisorId");
                mContact = new Contact();
                mContact.setAdvisorId(advisorId);
                isFromCustomer = true;
                setRelevance(clientName, getIntent().getStringExtra(EXTRA_CLIENT_ID));//关联客户
                setContactTime(mContact); //联系时间默认为当前时间
                getCustomerFormById("0");//获取动态字段
            } else {
                getCustomerFormById("0");//获取动态字段
            }
        }
    }

    /**
     * 根据动态详情获取联系记录详细信息
     */
    private void getDynamicInfo(Dynamic dynamic) {
        String tableName = "";
        if ("项目联系提醒".equals(dynamic.getDataType())) {
            tableName = "crm_project_contact";
            isFromCustomer = false;
        } else if ("客户联系提醒".equals(dynamic.getDataType())) {
            tableName = "crm_contact";
            isFromCustomer = true;
        }
        String url = Global.BASE_JAVA_URL + GlobalMethord.联系记录根据ID查详情
                + dynamic.getDataId() + "&tableName=" + tableName;
        StringRequest.getAsyn(url,
                new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            mContact = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Contact.class);
                            if (isFromCustomer) {
                                headerView.setTitle("编辑" + Global.CONTACT_TITLE);
                                setRelevance(StrUtils.pareseNull(mContact.getCustomerName()), StrUtils.pareseNull(mContact.getCustomerId()));//关联客户
                                getCustomerFormById(mContact.getUuid());//获取动态字段
                                setEnable(mContact.getContactTime()); //设置联系记录是否只读
                            } else {
                                setRelevance(mContact.getProjectName(), mContact.getProjectId()); //关联项目
                                getProjectNames();
                                getCustomerFormById(mContact.getUuid()); //获取动态字段
                                headerView.setTitle("编辑" + Global.CONTACT_TITLE);
                                setEnable(mContact.getContactTime()); //设置联系记录是否只读
                                if (mContact.isHiddenMore()) {
                                    headerView.setRightTitleVisible(false);
                                }
                            }
                            if (!isFromCustomer) {
                                getCustomDicts("crm_project_stage");
                            } else {
                                getCustomDicts("dict_contact_stage");
                            }
                            getCustomDicts("dict_contact_way");
                            setSupportAndCommentData(mContact); //设置点赞和评论数据
                        } catch (ParseException e) {
                            e.printStackTrace();
                            showShortToast("数据加载失败");
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
     * 设置点赞和评论数据
     */
    private void setSupportAndCommentData(Contact contact) {
        ll_support_comment_parent.setVisibility(View.VISIBLE);
        if (contact.isLike()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
            tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
        }
        tv_viewcount.setText("浏览" + contact.getFavoriteNumber() + "次");
        commentView.setIsLike(contact.isLike());

        tv_comment.setText(contact.getCommentNumber() + "");
        tv_support.setText(contact.getLikeNumber() + "");

        getCommentList(contact);
        getSupportList(contact);
    }

    /**
     * 获取点赞列表
     *
     * @param contact
     */
    private void getSupportList(Contact contact) {
        SupportListPost post = new SupportListPost();
        post.setDataType("联系记录");
        post.setDataId(contact.getUuid());
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞列表;
        supportUser = "";
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    ll_support.setVisibility(View.VISIBLE);
                    for (int i = 0; i < list.size(); i++) {
                        supportUser += helper.getUserNameById(list.get(i).getFromId()) + "、";
                    }
                    String substring = supportUser.substring(0, supportUser.length() - 1);
                    tv_supportuser.setText(substring);
                } else {
                    ll_support.setVisibility(View.GONE);
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
     * 获取评论列表
     *
     * @param contact
     */
    private void getCommentList(Contact contact) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论列表;
        SupportListPost post = new SupportListPost();
        post.setDataType("联系记录");
        post.setDataId(contact.getUuid());

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                if (list != null && list.size() > 0) {
                    tv_nocomment.setVisibility(View.GONE);
                    lv.setVisibility(View.VISIBLE);
                    lv.setAdapter(getAdapter(list));
                    tv_comment.setText(list.size() + "");
                } else {
                    tv_nocomment.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
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
     * 评论列表适配器
     *
     * @param list
     * @return
     */
    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, this, R.layout.item_common_list) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
                TextView tv_content = viewHolder.getView(R.id.tv_time_task_item);
                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getFromId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNameById(item.getFromId()));//点赞人姓名
//                viewHolder.setTextValue(R.id.tv_time_task_item, ViewHelper.convertStrToFormatDateStr(item.getTime(), "MM月dd日 HH:mm"));//点赞时间
                tv_content.setText(item.getContent());  //评论内容
            }
        };
    }

    /**
     * 设置联系记录是否只读
     */
    private void setEnable(String createTime) {
        today = ViewHelper.isToday(StrUtils.pareseNull(createTime));
        if (!today) { //如果联系记录不是今天 不可以编辑
            attachView.setEnabled(false);
            attachView.setIsAdd(false);
            traversalView(ll_parent);
            headerView.setTitle("查看" + Global.CONTACT_TITLE);
            headerView.setRightTitleVisible(false);
            headerView.setRightIconDrawable(R.drawable.icon_client_historyrecord);
            if (mContact.isHiddenMore()) {
                headerView.setRightIconVisible(false);
            } else {
                headerView.setRightIconVisible(true);
            }
        } else if (!StrUtils.pareseNull(mContact.getAdvisorId()).equals(Global.mUser.getUuid())) { //如果不是本人的客户 不可以编辑
            attachView.setEnabled(false);
            attachView.setIsAdd(false);
            traversalView(ll_parent);
            headerView.setRightIconDrawable(R.drawable.icon_client_historyrecord);
            headerView.setTitle("查看" + Global.CONTACT_TITLE);
            headerView.setRightTitleVisible(false);
            if (mContact.isHiddenMore()) {
                headerView.setRightIconVisible(false);
            } else {
                headerView.setRightIconVisible(true);
            }
        } else {
            attachView.setIsAdd(true);
        }
    }

    /**
     * 设置关联项目或关联客户
     * <p>
     * 为true 关联客户 隐藏项目 为false 关联项目 隐藏客户
     */
    private void setRelevance(String str, String uuid) {
        if (isFromCustomer) {
            ll_projectName.setVisibility(View.GONE);
            ll_customer.setVisibility(View.VISIBLE);
            ll_project.setVisibility(View.VISIBLE);
            tv_project.setText("项目名称 :" + StrUtils.pareseNull(mContact.getProjectName()));
            tv_customerName.setText("客户名称 :" + str);
            mContact.setCustomerId(uuid);
        } else {
            ll_projectName.setVisibility(View.VISIBLE);
            ll_customer.setVisibility(View.GONE);
            tv_projectName.setText(str);
            mContact.setProjectId(uuid);
        }
    }

    /**
     * 设置默认联系时间
     */
    private void setContactTime(Contact contact) {
        et_contactTime.setText(Global.CONTACT_TIME + " :" + ViewHelper.formatDateToStr(new Date(), YEAR_MONTH_DAY_HOUR_MINUTE)); //联系时间默认当前时间
        contact.setContactTime(ViewHelper.formatDateToStr(new Date(), YEAR_MONTH_DAY_HOUR_MINUTE));
    }

    /**
     * 从列表传进来的数据回显
     */
    private void setRecord() {
        tv_stage.setText(Global.CONTACT_STAGE + " :" + StrUtils.pareseNull(mContact.getStageName()));
        et_content.setText(mContact.getContent());
        et_contactTime.setText(Global.CONTACT_TIME + " :" + StrUtils.pareseNull(mContact.getContactTime()));
        et_nextContact.setText(StrUtils.pareseNull(mContact.getNextContactContent()));
        et_nextContactTime.setText("下次" + Global.CONTACT_TIME + " :" + StrUtils.pareseNull(mContact.getNextContactTime()));
        tv_contactWay.setText("沟通方式 :" + StrUtils.pareseNull(mContact.getContactWayName()));
        tvLocation.setText(mContact.getAddress());
        attachView.loadImageByAttachIds(mContact.getAttachment());
    }

    private void getCustomerFormById(String id) {
        if (!id.equals("0")) {
            setRecord();
        } else {
            attachView.setIsAdd(true);
            attachView.loadImageByAttachIds("");
        }
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.CRM动态字段;
        JSONObject object = new JSONObject();
        try { //crm_contact
            if (isFromCustomer) {
                object.put("type", "crm_contact");
            } else {
                object.put("type", "crm_project_contact");
            }
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, object, new StringResponseCallBack() {
            @Override
            public void onResponse(String result) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String response) {
                Logger.i(TAG + response);
                ProgressDialogHelper.dismiss();
                动态表单ViewModel formViewModel = null;
                try {
                    formViewModel = JsonUtils.jsonToEntity(
                            response, 动态表单ViewModel.class);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (formViewModel != null) {
                    List<String> titles = new ArrayList<String>();
                    for (动态表单分类 categray : formViewModel.动态表单分类s) {
                        titles.add(categray.分类名称);
                        Logger.i("CategrayTag::" + categray.分类名称 + "");
                        mFormDataMap.put(categray.分类名称, new ArrayList<表单字段>());
                    }
                    if (formViewModel.表单字段s != null
                            && formViewModel.表单字段s.size() > 0) {
                        // 根据分类Tab
                        for (表单字段 form : formViewModel.表单字段s) {
                            if (form.Name.equals("uuid")) {
                                form.TypeName = "其他";
                                mFormDataMap.put(form.TypeName,
                                        new ArrayList<表单字段>());
                                mFormDataMap.get(form.TypeName).add(form);
                                break;
                            }
                        }
                    }

                    if (formViewModel.表单字段s != null
                            && formViewModel.表单字段s.size() > 0) {
                        // 根据分类Tab
                        for (表单字段 form : formViewModel.表单字段s) {
                            if (!mFormDataMap.containsKey(form.TypeName)) {
                                mFormDataMap.put(form.TypeName,
                                        new ArrayList<表单字段>());
                            }
//							if(!"编号".equals(form.Name))
//							{   //完成分类，编号字段不显示
//								mFormDataMap.get(form.TypeName).add(form);
//							}
                            if (!form.Name.equals("uuid")) {
                                mFormDataMap.get(form.TypeName).add(form);
                            }
                        }

                        表单字段 tempIdForm = null;
                        String firstKey = "";
                        Iterator<Map.Entry<String, ArrayList<表单字段>>> item = mFormDataMap.entrySet().iterator();
                        while (item.hasNext()) {
                            Map.Entry<String, ArrayList<表单字段>> entry = item.next();
                            List<表单字段> formList = entry.getValue();
                            String formName = entry.getKey();

                            //特殊处理，当分类下没有内容 或只有一个编号字段则去除tab
                            if (formList == null || formList.size() == 0) {
//                                ||
                                titles.remove(formName);
                            } else if ((formList.size() == 1 && "编号".equals(formList.get(0).Name))) {
                                titles.remove(formName);
                                tempIdForm = formList.get(0);
                            }

                            if (TextUtils.isEmpty(firstKey)) {
                                firstKey = formName;
                            }
                        }


                        if (tempIdForm != null) {
                            //特殊处理id字段到第一分类
                            mFormDataMap.get(firstKey).add(tempIdForm);
                        }
                        for (int i = 0; i < titles.size(); i++) {
                            String s = titles.get(i);
                            if (TextUtils.isEmpty(s)) {
//                                titles.set(titles.indexOf(s), "其他");
                                titles.remove(i);
                                titles.add("其他");
                            }
                        }
//                        mIndicator.setTabItemTitles(titles);

                        for (String title : titles) {
//                            if (title.equals("其他")) {
//                                title = "";
//                            }
                            Iterator<Map.Entry<String, ArrayList<表单字段>>> it = mFormDataMap
                                    .entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, ArrayList<表单字段>> entry = it
                                        .next();
                                String keyStr = entry.getKey();
                                Logger.i("EQU" + keyStr + "---" + title);
                                if (keyStr.equals(title)) {
                                    ArrayList<表单字段> formList = entry.getValue();

                                    if (formList != null && formList.size() > 0) {
                                        mClientBiz = new ChClientBiz(mContext, formList, ll_root, today, true);
                                        mClientBiz.generateViews();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Logger.e(TAG + ex + "");
                ProgressDialogHelper.dismiss();
            }
        });
    }


    private void requestLocating() {
        try {
            BaiduLocator.requestLocation(getApplicationContext(), AddRecordActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<表单字段> getFormList() {
        ArrayList<表单字段> list = new ArrayList<>();
        if (mClientBiz != null) {
            list = mClientBiz.getFormList();
        }
        return list;
    }

    private void saveCustomerForm(ArrayList<表单字段> formList) {
//        ClientInfo info = new ClientInfo();
//        info.setJsonData(formList);
//        info.setType("crm_customer");

        String url = Global.BASE_JAVA_URL + GlobalMethord.保存动态字段;
        String poststr;
        if (isFromCustomer) {
            poststr = "crm_contact";
        } else {
            poststr = "crm_project_contact";
        }
        StringRequest.postAsynNoMap(url, poststr, formList, new StringResponseCallBack() {
            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast(JsonUtils.pareseMessage(result));
                Logger.d(TAG + result + "");
            }

            @Override
            public void onResponse(String response) {
                showShortToast("保存成功");
                try {
                    String data = JsonUtils.getStringValue(response, "Data");
                    saveRecord(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                Logger.e(TAG + ex + "");
                showShortToast("网络不给力，请稍后再试");
            }
        });
    }

    private void saveRecord(String data) {
        mContact.setUuid(data);
        String url = Global.BASE_JAVA_URL + GlobalMethord.添加跟进记录;
        StringRequest.postAsyn(url, mContact, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                ProjectRecordFragment.isReasum = true;
                ClientContactListFragment.isResume = true;
                ConatactLaneFragment.isReasume = true;
                ClientContactListFragment.isResume = true;
                ProjectListActivity.isResume = true;
                String message = JsonUtils.pareseMessage(response);
                showShortToast(TextUtils.isEmpty(message) ? "保存成功" : message);
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
                showShortToast("网络错误");
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                showShortToast(JsonUtils.pareseMessage(result));
            }
        });
    }

    private boolean isCanSave() {
        String content = et_content.getText().toString();
        String contactTime = et_contactTime.getText().toString();
        String location = tvLocation.getText().toString();
        String customerName = tv_customerName.getText().toString();
        String stage = tv_stage.getText().toString();
        String contactWay = tv_contactWay.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showShortToast("请输入" + Global.CONTACT_CONTENT);
            return false;
        }
        if (TextUtils.isEmpty(contactTime)) {
            showShortToast("请选择" + Global.CONTACT_TIME);
            return false;
        }
        if (TextUtils.isEmpty(stage)) {
            showShortToast("当前" + Global.CONTACT_STAGE + "不能为空");
            return false;
        }
        if (TextUtils.isEmpty(contactWay)) {
            showShortToast("沟通方式不能为空");
            return false;
        } else if (contactWay.contains("拜访沟通") && TextUtils.isEmpty(location)) {
            showShortToast("联系地址不能为空");
            return false;
        }
        mContact.setContent(content);
        mContact.setNextContactContent(et_nextContact.getText().toString());
        return true;
    }

    private void setOnTouchEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                final ArrayList<表单字段> list = getFormList();
                String result = ChClientBiz.checkNull(list);
                if (isCanSave()) {
                    if (!TextUtils.isEmpty(result)) {
                        showShortToast(result);
                    } else {
                        String idCardReg = ChClientBiz.checkCardRegEx(list);
                        if (!TextUtils.isEmpty(idCardReg)) {
                            // 先校验身份证号
                            showShortToast(idCardReg);
                        } else {
//                        result = ChClientBiz.checkRegEx(list);
                            if (!TextUtils.isEmpty(result)) {
                                showShortToast(result);
                            } else {
                                ProgressDialogHelper.show(mContext);
                                attachView.uploadImage("project", new IOnUploadMultipleFileListener() {
                                    @Override
                                    public void onStartUpload(int sum) {

                                    }

                                    @Override
                                    public void onProgressUpdate(int completeCount) {

                                    }

                                    @Override
                                    public void onComplete(String attachIds) {
                                        mContact.setAttachment(attachIds);
                                        saveCustomerForm(list);
                                    }
                                });
                            }
                        }
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
                Intent intent = new Intent(mContext, CustomerContactListActivity.class);
                intent.putExtra("customerId", mContact.getCustomerId());
                startActivity(intent);
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //地址
                if (mLatitude == ERROR_LAT_LNG && mLongitude == ERROR_LAT_LNG) {
                    showShortToast("定位失败,请尝试打开GPS或连接wifi");
                    requestLocating();
                } else {
                    selectLocation(mContext, mLatitude, mLongitude);
                }

            }
        });

        et_contactTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //联系时间
                pickerView.show();
                pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        if (ViewHelper.getTimeDistance(new Date(), date) <= 0) {
                            et_contactTime.setText(Global.CONTACT_TIME + " :" + ViewHelper.formatDateToStr(date, YEAR_MONTH_DAY_HOUR_MINUTE));
                            mContact.setContactTime(ViewHelper.formatDateToStr(date, YEAR_MONTH_DAY_HOUR_MINUTE));
                            tgf_time.clearSelected();
                        } else {
                            showShortToast("不可以选择今天以后的" + Global.CONTACT_TIME);
                        }
                    }
                });
            }
        });
        et_nextContactTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //下次联系时间
                if (today) {
                    nextPickerView.show();
                    nextPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date) {
                            if (ViewHelper.getTimeDistance(new Date(), date) >= 0) {
                                et_nextContactTime.setText("下次" + Global.CONTACT_TIME + " :" + ViewHelper.formatDateToStr1(date));
                                mContact.setNextContactTime(ViewHelper.formatDateToStr1(date));
                                tgf_nextTime.clearSelected();
                            } else {
                                showShortToast("不可以选择今天以前的下次" + Global.CONTACT_TIME);
                            }
                        }
                    });
                }
            }
        });
        tgf_project.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (today) {
                    mSelectPosSet = selectPosSet;
                    String customerType = "";
                    String uuid = "";
                    for (Integer pos : mSelectPosSet) {
                        customerType = projectDataList.get(pos).getName();
                        uuid = projectDataList.get(pos).getUuid();
                    }
                    tv_project.setText("项目名称 :" + customerType);
                    mContact.setProjectId(uuid);
                }
            }
        });
        tgf_stage.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) { //联系阶段
                if (today) {
                    mSelectPosSet = selectPosSet;
                    String customerType = "";
                    String uuid = "";
                    for (Integer pos : mSelectPosSet) {
                        customerType = mDictStageList.get(pos).getName();
                        uuid = mDictStageList.get(pos).getUuid();
                    }
                    tv_stage.setText(Global.CONTACT_STAGE + " :" + customerType);
                    mContact.setStage(uuid);
                }
            }
        });

        tgf_time.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) { //选择联系时间
                if (today) {
                    mSelectPosSet = selectPosSet;
                    String time = "";
                    String uuid = "";
                    for (Integer pos : mSelectPosSet) {
                        time = timeListDict.get(pos).getName();
                        uuid = timeListDict.get(pos).getUuid();
                    }
                    et_contactTime.setText(Global.CONTACT_TIME + " :" + time);
                    mContact.setContactTime(uuid);
                }
            }
        });
        tgf_nextTime.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) { //选择下次联系时间
                if (today) {
                    mSelectPosSet = selectPosSet;
                    String time = "";
                    String uuid = "";
                    for (Integer pos : mSelectPosSet) {
                        time = timeNextListDict.get(pos).getName();
                        uuid = timeNextListDict.get(pos).getUuid();
                    }
                    et_nextContactTime.setText("下次" + Global.CONTACT_TIME + " :" + time);
                    mContact.setNextContactTime(uuid);
                }
            }
        });
        tgf_contactWay.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) { //沟通方式
                if (today) {
                    mSelectPosSet = selectPosSet;
                    String customerType = "";
                    String uuid = "";
                    for (Integer pos : mSelectPosSet) {
                        customerType = mDictWayList.get(pos).getName();
                        uuid = mDictWayList.get(pos).getUuid();
                    }
                    tv_contactWay.setText("联系方式 :" + customerType);
                    mContact.setContactWay(uuid);
                }
            }
        });

        commentView.setOnSupportSuccessListener(new BottomCommentView.SupportSuccessListener() {
            @Override
            public void onSupportSuccess() { //点赞成功，重新加载点赞列表
//                fragment.reloadSupport();
                getSupportList(mContact);
            }
        });

        commentView.setOnCommentListener(new BottomCommentView.CommentListener() {
            @Override
            public void onComment(String count) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mContact.getUuid());
                post.setDataType("联系记录");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mContact.getUuid());
                post.setContent(count);
                commentView.comment(post);
            }
        });


        commentView.setOnCommentSuccessListener(new BottomCommentView.CommentSuccessListener() {
            @Override
            public void onCommentSuccess() {
//                fragment.reloadComment();
                tv_comment.setText((mContact.getCommentNumber() + 1) + "");
                getCommentList(mContact);
            }
        });

        iv_connment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWiw(mContact);
            }
        });

        iv_support.setOnClickListener(new View.OnClickListener() {
            private int likeNumber;

            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setDataId(mContact.getUuid());
                post.setDataType("联系记录");
                post.setFromId(Global.mUser.getUuid());
                post.setToId(mContact.getUuid());
                if (mContact.isLike()) { //取消点赞
                    likeNumber = mContact.getLikeNumber() - 1;
                    tv_support.setText(likeNumber + "");
                    commentView.cancleSupport(post);
                    mContact.setLike(false);
                } else {
                    likeNumber = (mContact.getLikeNumber() + 1);
                    tv_support.setText(likeNumber + "");
                    mContact.setLike(true);
                    commentView.support(post);
                }
                mContact.setLikeNumber(likeNumber);
                if (mContact.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
                ProjectRecordFragment.isReasum = true;
                ClientContactListFragment.isResume = true;
                ConatactLaneFragment.isReasume = true;
                ClientContactListFragment.isResume = true;
            }
        });

        ll_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today) {
                    return;
                }
                if (isShowProjectTag) {
                    tgf_project.setVisibility(View.GONE);
                    ivShowProjectTag.setImageResource(R.drawable.arrow_right);
                } else {
                    tgf_project.setVisibility(View.VISIBLE);
                    ivShowProjectTag.setImageResource(R.drawable.arrow_down);
                }
                isShowProjectTag = !isShowProjectTag;
            }
        });

        llStage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today) {
                    return;
                }
                if (isShowStageTag) {
                    tgf_stage.setVisibility(View.GONE);
                    ivShowStageTag.setImageResource(R.drawable.arrow_right);
                } else {
                    tgf_stage.setVisibility(View.VISIBLE);
                    ivShowStageTag.setImageResource(R.drawable.arrow_down);
                }
                isShowStageTag = !isShowStageTag;
            }
        });

        llway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today) {
                    return;
                }
                if (isShowWayTag) {
                    tgf_contactWay.setVisibility(View.GONE);
                    ivShowWayTag.setImageResource(R.drawable.arrow_right);
                } else {
                    tgf_contactWay.setVisibility(View.VISIBLE);
                    ivShowWayTag.setImageResource(R.drawable.arrow_down);
                }
                isShowWayTag = !isShowWayTag;
            }
        });

        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today) {
                    return;
                }
                if (isShowTimeTag) {
                    tgf_time.setVisibility(View.GONE);
                    ivShowTimeTag.setImageResource(R.drawable.arrow_right);
                } else {
                    tgf_time.setVisibility(View.VISIBLE);
                    ivShowTimeTag.setImageResource(R.drawable.arrow_down);
                }
                isShowTimeTag = !isShowTimeTag;
            }
        });

        llNextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today) {
                    return;
                }
                if (isShowNextTimeTag) {
                    tgf_nextTime.setVisibility(View.GONE);
                    ivShowNextTimeTag.setImageResource(R.drawable.arrow_right);
                } else {
                    tgf_nextTime.setVisibility(View.VISIBLE);
                    ivShowNextTimeTag.setImageResource(R.drawable.arrow_down);
                }
                isShowNextTimeTag = !isShowNextTimeTag;
            }
        });


    }

    private void initData() {
        mContext = AddRecordActivity.this;
        mInflater = LayoutInflater.from(mContext);
        helper = new DictionaryHelper(mContext);
        mFormDataMap = new HashMap<String, ArrayList<表单字段>>();
        pickerView = new TimePickerView(mContext, TimePickerView.Type.ALL);
        pickerView.setRange(ViewHelper.getYear() - 1, ViewHelper.getYear());
        pickerView.setTime(new Date());
        pickerView.setCyclic(false);
        pickerView.setCancelable(true);

        nextPickerView = new TimePickerView(mContext, TimePickerView.Type.YEAR_MONTH_DAY);
        nextPickerView.setRange(ViewHelper.getYear() - 1, ViewHelper.getYear());
        nextPickerView.setTime(ViewHelper.getFetureDate(7));
        nextPickerView.setCyclic(false);
        nextPickerView.setCancelable(true);

        tgf_stage.setMaxSelectCount(1);//最大选择数量
        tgf_time.setMaxSelectCount(1);
        tgf_nextTime.setMaxSelectCount(1);
        tgf_contactWay.setMaxSelectCount(1);
        tgf_project.setMaxSelectCount(1);

    }


    private void initTagData() {
        timeListDict = new ArrayList<>();         //联系时间  tagflowlayout数据源
        timeList = new ArrayList<>();
        timeListDict.add(new 字典(ViewHelper.formatDateToStr1(new Date()), "今天"));
        timeListDict.add(new 字典(ViewHelper.getDateYestoday(), "昨天"));
        timeListDict.add(new 字典(ViewHelper.getFormerlyDateStr(3), "3天前"));
        timeListDict.add(new 字典(ViewHelper.getFormerlyDateStr(7), "一周前"));
        for (字典 z : timeListDict) {
            timeList.add(z.getName());
        }
        initTagLayout(tgf_time, timeList, mInflater);

        timeNextListDict = new ArrayList<>();        //下次联系时间 tagflowlayout数据源
        timeNextListDict.add(new 字典(ViewHelper.formatDateToStr1(new Date()), "今天"));
        timeNextListDict.add(new 字典(ViewHelper.getDateYestoday(), "明天"));
        timeNextListDict.add(new 字典(ViewHelper.getFetureDateStr(3), "3天后"));
        timeNextListDict.add(new 字典(ViewHelper.getFetureDateStr(7), "一周后"));
        nextTimeList = new ArrayList<>();
        for (字典 z : timeNextListDict) {
            nextTimeList.add(z.getName());
        }
        initTagLayout(tgf_nextTime, nextTimeList, mInflater);

        flowViewList = new ArrayList<>(); //补充联系内容
        flowViewList.add("签订合同");
        flowViewList.add("项目在开发阶段");
        flowViewList.add("客户验收");

        TagAdapter<String> tagAdapter = new TagAdapter<String>(flowViewList) {
            @Override
            public View getView(FlowLayout parent, int position, String t) {
                Logger.i("tagA" + position + "--" + t);
                final TextView tv = (TextView) mInflater.inflate(
                        R.layout.item_label_customer_list, flow_view, false);
                tv.setText(t);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_content.setText(et_content.getText().toString() + tv.getText().toString());
                    }
                });
                return tv;
            }
        };
        flow_view.setAdapter(tagAdapter);
    }


    private void initView() {
        ll_root = findViewById(R.id.ll_root_ch_client_tab);
        headerView = findViewById(R.id.header_new_contact);
        attachView = findViewById(R.id.attach_add_constact);
        voiceInputView = (VoiceInputView) findViewById(R.id.voice_view_new_log);
        et_content = findViewById(R.id.editTextContent_constact1); //联系内容
        tv_projectName = findViewById(R.id.tv_project_name); //项目名称
        ll_projectName = findViewById(R.id.ll_project_name); //项目名称父容器
        tvLocation = findViewById(R.id.tvLocation_newconstact); //定位
        tv_customerName = findViewById(R.id.etClientName_newconstact1); //客户名称
        tv_stage = findViewById(R.id.tv_status); //阶段
        tgf_stage = findViewById(R.id.tgf_stage); //选择阶段
        tv_contactWay = findViewById(R.id.tv_contact_way); //联系方式
        tgf_contactWay = findViewById(R.id.tgf_contactway); //选择联系方式
        flow_view = findViewById(R.id.flow_view); //补填内容
        et_contactTime = findViewById(R.id.etContactStatus_newconstact1); //联系时间
        et_nextContactTime = findViewById(R.id.tv_new_client_contact_project); //联系时间
        et_nextContact = findViewById(R.id.et_next_contact_content);  //下次联系内容
        tgf_time = findViewById(R.id.tgf_constact_time);  //选择联系时间
        tgf_nextTime = findViewById(R.id.tgf_constact_next_time); //选择下次联系时间
        ll_parent = findViewById(R.id.ll_parent); //父容器
        ll_customer = findViewById(R.id.ll_customer);
        tv_supportuser = findViewById(R.id.tv_support_user);
        ll_support = findViewById(R.id.ll_support_list);
        iv_support = findViewById(R.id.iv_support); //点赞
        tv_support = findViewById(R.id.tv_support); //点赞数量
        tv_comment = findViewById(R.id.tv_comment); //评论
        tv_nocomment = findViewById(R.id.tv_nocomment); //暂无评论
        ll_viewcount = findViewById(R.id.ll_viewcount); //浏览量父容器
        tv_viewcount = findViewById(R.id.tv_viewcount); //浏览量
        commentView = findViewById(R.id.comment_log_info); //点赞评论输入框
        lv = findViewById(R.id.contactinfo_listview); //评论列表
        tgf_project = findViewById(R.id.tgf_projectname); //选择关联项目
        tv_project = findViewById(R.id.tv_project_dict); //项目
        ll_project = findViewById(R.id.ll_project);// 项目布局容器
        rl_project = findViewById(R.id.rl_project); //关联项目选择器容器
        iv_connment = findViewById(R.id.iv_comment);
        ll_support_comment_parent = findViewById(R.id.ll_support_comment_parent);


        ivShowProjectTag = findViewById(R.id.iv_show_tag_project);
        ivShowStageTag = findViewById(R.id.iv_show_tag_stage);
        ivShowWayTag = findViewById(R.id.iv_show_tag_way);
        ivShowTimeTag = findViewById(R.id.iv_show_tag_contact_time);
        ivShowNextTimeTag = findViewById(R.id.iv_show_tag_next_time);
        llStage = findViewById(R.id.ll_status);
        llway = findViewById(R.id.ll_contactway);
        llTime = findViewById(R.id.ll_contact_time);
        llNextTime = findViewById(R.id.ll_next_contact_time);

        headerView.setTitle(Global.CONTACT_TITLE);
        tv_stage.setHint("请选择" + Global.CONTACT_STAGE);
        et_content.setHint("请输入" + Global.CONTACT_CONTENT);
        et_contactTime.setHint("请选择" + Global.CONTACT_TIME);
        et_nextContactTime.setHint("请选择" + Global.CONTACT_NEXTCONTACTTIME);
        et_nextContact.setHint("请输入" + Global.CONTACT_NEXTCONTENT);
        voiceInputView.setRelativeInputView(et_content);
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        Logger.i("BDLocationListener:::onReceiveLocation is running");
        if (location == null) {
            showShortToast("失败");
            Logger.i("BDLocationListener::onReceiveLocation is null");
        } else {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            mLatitude = location.getLatitude();
            mLat = location.getLatitude();
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            mLongitude = location.getLongitude();

            mLog = location.getLongitude();


//            BaiduLocator.stop();
            // mLongitude = 99.820494;
            // mLog = 99.820494;
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Context context = getApplicationContext();
                CharSequence text = "需要连接到4G或者wifi因特网！";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                BaiduLocator.stop();
            }

            mLocation = location.getAddrStr();
            // mProvince = location.getProvince();
            mCity = location.getCity();
            mCountry = location.getCountry();

            if (TextUtils.isEmpty(mLocation)) {
                requestLocating();
                return;
            }

            //实例化一个当前定位到的地址对象
            mBaiduPlace = new BaiduPlace();
            mBaiduPlace.location = new BaiduPlace.Location();
            mBaiduPlace.location.lat = mLat;
            mBaiduPlace.location.lng = mLog;
            mBaiduPlace.name = mLocation + location.getStreetNumber();
            mBaiduPlace.address = mCity;
            mBaiduPlace.city = mCity;

            Logger.e("BDLocationListener" + sb.toString());
            BaiduLocator.stop();
            tvLocation.setText(mLocation + location.getStreetNumber());
            mContact.setLat(mLatitude + "");
            mContact.setLng(mLongitude + "");
            mContact.setAddress(mLocation);
//            String locationRect = EarthMapUtils.getLocationRect(mLat, mLog,
//                    radiusMap);
//            String url = "http://api.map.baidu.com/place/v2/search?query=楼$酒店$大厦$公司$小区$中心$公交$银行$学校$街道$路&bounds="
//                    + locationRect
//                    + "&output=json&ak=1lefPqdAWokm2tpRg3o9IhUfwjxvk1ci";
//            getLocationList(url, mCountry, mCity);
        }

    }

    private void getLocationList(final String url, final String country,
                                 final String city) {

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                try {
                    JSONObject jo = new JSONObject(result);
                    int status = jo.getInt("status");
                    String message = jo.getString("message");
                    String results = jo.getString("results");
                    if (status == 0 && "ok".equals(message)) {
                        bpList = JsonUtils.pareseJsonToList(
                                results, BaiduPlace.class);
                        Logger.i("地址个数::" + bpList.size());
                        if (bpList.size() > 0) {
                            if (bpList.size() > 1) {
                                for (int i = 0; i < bpList.size(); i++) {
                                    BaiduPlace temp = bpList.get(i);
                                    Logger.d("distance2::" + temp.name
                                            + temp.address + "---"
                                            + temp.location.lat + ","
                                            + temp.location.lng);
                                }

                                // 根据距离由近到远排序
                                for (int i = 0; i < bpList.size(); i++) {
                                    BaiduPlace temp = bpList.get(i);
                                    Logger.i("distance::" + temp.name
                                            + temp.address + "---"
                                            + temp.location.lat + ","
                                            + temp.location.lng);
                                }
                            }
                            BaiduPlace bPlace = bpList.get(0);

                            mLatitude = bPlace.location.lat;
                            mLongitude = bPlace.location.lng;
                            mLocation = getLocationAddress(country, city, bPlace);
                            tvLocation.setText(mLocation);
                            mContact.setLat(mLatitude + "");
                            mContact.setLng(mLongitude + "");
                            mContact.setAddress(mLocation);
                        } else {
                            mLatitude = ERROR_LAT_LNG;
                            mLongitude = ERROR_LAT_LNG;
                            requestLocating();
                            showShortToast("定位失败,请尝试打开GPS或连接wifi");
//                            mLocation = "天安门 - 北京市东城区东长安街";
//                            tvLocation.setText(mLocation);
//                            mContact.setLat(mLatitude + "");
//                            mContact.setLng(mLongitude + "");
//                            mContact.setAddress(mLocation);
                        }

                    }
                } catch (Exception e) {
                    Logger.e("" + e.getMessage());
                }
            }
        });
    }

    /***
     * 获取定位地址
     *
     * @param country 县
     * @param city    市
     * @param place
     * @return
     */
    private String getLocationAddress(final String country, final String city,
                                      BaiduPlace place) {
        String address = place.name + " (" + place.address + ")";
        if (!TextUtils.isEmpty(city) && !address.contains(city)) {
            address = city + " " + address;
        }
        return address;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCATION_CODE:
                    BaiduPlace place = SelectLocationBiz.onActivityGetPlace(
                            requestCode, data);
                    if (place != null) {
                        mBaiduPlace = place;
                        String address = place.name + " (" + place.address + ")";
                        mCity = place.city;
                        mLatitude = place.location.lat;
                        mLongitude = place.location.lng;
                        tvLocation.setText(address);
                        mContact.setLat(mLatitude + "");
                        mContact.setLng(mLongitude + "");
                        mContact.setAddress(mLocation);
                    }
                    break;

                default:
                    attachView.onActivityiForResultImage(requestCode,
                            resultCode, data);
                    break;

            }

        }
    }

    /***
     * 从参数经纬度附近地址列表选择一个地点
     * SelectLocationBiz.SELECT_LOCATION_CODE
     *
     * @param context
     *            上下文
     * @param lat
     *            经度
     * @param lng
     *            纬度
     */
    private void selectLocation(Context context, double lat, double lng) {
//lat 30.005475  lng 104.187905
        Intent intent = new Intent(context, LocationListActivity.class);
        if (lng != 0 && lat != 0) {
            intent.putExtra(LocationListActivity.LATITUDE, lat);
            intent.putExtra(LocationListActivity.LONGITUDE, lng);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("location_place", mBaiduPlace);
        intent.putExtras(bundle);

        ((Activity) context).startActivityForResult(intent,
                SELECT_LOCATION_CODE);
    }

    /**
     * 遍历所有view
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        setArrowhide();
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                view.setEnabled(false);
            }
        }
    }


    /**
     * 设置小箭头隐藏
     */
    private void setArrowhide() {
        ivShowProjectTag.setVisibility(View.GONE);
        ivShowStageTag.setVisibility(View.GONE);
        ivShowWayTag.setVisibility(View.GONE);
        ivShowTimeTag.setVisibility(View.GONE);
        ivShowNextTimeTag.setVisibility(View.GONE);
    }

    /***
     * 根据字典名称获得一个字典的集合
     *
     * @param dictTableName      字典表名称,如果是普通字典表，其余两个参数可传null
     * @return
     */
    public void getCustomDicts(final String dictTableName) {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Logger.i("getDict" + url);
//        List<DictData> list = new ArrayList<DictData>();
        final DictData dictData = new DictData();
        dictData.setDictionaryName(dictTableName);
        dictData.setFull(true);
//        list.add(dictData);
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    if (dictTableName.equals("dict_contact_stage") || "crm_project_stage".equals(dictTableName)) {
                        mDictStageList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                        if (mDictStageList != null && mDictStageList.size() > 0) {
                            List<String> strs = new ArrayList<String>();
                            for (字典 dict : mDictStageList) {
                                strs.add(dict.getName());
                                if (dict.getUuid().equals(mContact.getStage())) {
                                    tv_stage.setText(Global.CONTACT_STAGE + " :" + dict.getName());
                                }
                            }
                            TagAdapter<String> tagAdapter = new TagAdapter<String>(strs) {
                                @Override
                                public View getView(FlowLayout parent, int position, String t) {
                                    Logger.i("tagA" + position + "--" + t);
                                    final TextView tv = (TextView) mInflater.inflate(
                                            R.layout.item_label_customer_list, tgf_stage, false);
                                    tv.setText(t);
                                    return tv;
                                }
                            };
                            tgf_stage.setAdapter(tagAdapter);
                            //如果是新建联系记录，默认选中第一个字典项
                            if (mContact != null
                                    && TextUtils.isEmpty(mContact.getUuid())
                                    && TextUtils.isEmpty(mContact.getStage())) {
                                //如果是从项目新建联系记录
                                if (!isFromCustomer) {
                                    tagAdapter.setSelectedList(0);
                                    tv_stage.setText(strs.get(0));
                                    mContact.setStage(mDictStageList.get(0).getUuid());
                                } else {
                                    getCustomerCurrentStage();
                                }
                            }
                            if (!TextUtils.isEmpty(tv_stage.getText().toString())) {
                                for (int i = 0; i < mDictStageList.size(); i++) {
                                    if (mDictStageList.get(i).getUuid().equals(mContact.getStage())) {
                                        tagAdapter.setSelectedList(i);
                                    }
                                }
                            }
                        }
                    } else if (dictTableName.equals("dict_contact_way")) {
                        mDictWayList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                        if (mDictWayList != null && mDictWayList.size() > 0) {
                            List<String> strs = new ArrayList<String>();
                            for (字典 dict : mDictWayList) {
                                strs.add(dict.getName());
                                if (dict.getUuid().equals(mContact.getContactWay())) {
                                    tv_contactWay.setText("沟通方式：" + dict.getName());
                                }
                            }
                            TagAdapter<String> tagAdapter = new TagAdapter<String>(strs) {
                                @Override
                                public View getView(FlowLayout parent, int position, String t) {
                                    Logger.i("tagA" + position + "--" + t);
                                    final TextView tv = (TextView) mInflater.inflate(
                                            R.layout.item_label_customer_list, tgf_contactWay, false);
                                    tv.setText(t);
                                    return tv;
                                }
                            };
                            tgf_contactWay.setAdapter(tagAdapter);
                            //如果是新建联系记录，默认选中第一个字典项
                            if (mContact != null
                                    && TextUtils.isEmpty(mContact.getUuid())
                                    && TextUtils.isEmpty(mContact.getContactWay())) {
                                tagAdapter.setSelectedList(0);
                                mContact.setContactWay(mDictWayList.get(0).getUuid());
                                tv_contactWay.setText(strs.get(0));
                            }
                            if (!TextUtils.isEmpty(tv_contactWay.getText().toString())) {
                                for (int i = 0; i < mDictWayList.size(); i++) {
                                    if (mDictWayList.get(i).getUuid().equals(mContact.getContactWay())) {
                                        tagAdapter.setSelectedList(i);
                                    }
                                }
                            }
                        }
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
     * 获取客户当前的联系阶段
     */
    private void getCustomerCurrentStage() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详细信息 + mContact.getCustomerId();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    Client client = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Client.class);
                    if (client != null) {
                        for (int i = 0; i < mDictStageList.size(); i++) {
                            字典 字典 = mDictStageList.get(i);
                            if (字典.uuid.equals(client.getContactStage())) {
                                tgf_stage.getAdapter().setSelectedList(i);
                                mContact.setStage(字典.uuid);
                                tv_stage.setText(字典.name);
                                break;
                            }
                        }
                        if (TextUtils.isEmpty(mContact.getStage())) {
                            tgf_stage.getAdapter().setSelectedList(0);
                            mContact.setStage(mDictStageList.get(0).getUuid());
                            tv_stage.setText(mDictStageList.get(0).getName());
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

            }
        });
    }

    /**
     * 初始化TagFlowlayout数据源
     *
     * @param tgf
     * @param strs
     * @param mInflater
     */
    private void initTagLayout(final TagFlowLayout tgf, List<String> strs, final LayoutInflater mInflater) {
        TagAdapter<String> tagAdapter = new TagAdapter<String>(strs) {
            @Override
            public View getView(FlowLayout parent, int position, String t) {
                Logger.i("tagA" + position + "--" + t);
                final TextView tv = (TextView) mInflater.inflate(
                        R.layout.item_label_customer_list, tgf, false);
                tv.setText(t);
                return tv;
            }
        };
        tgf.setAdapter(tagAdapter);
    }

    private void popWiw(final Contact item) {

        popWiw = new BaseSelectPopupWindow(this, R.layout.edit_data);
        // popWiw.setOpenKeyboard(true);
        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWiw.setFocusable(true);
        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWiw.setShowTitle(false);
        popWiw.setBackgroundDrawable(new ColorDrawable(0));
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final TextView send = popWiw.getContentView().findViewById(
                R.id.btn_send);
        final TextEditTextView edt = popWiw.getContentView().findViewById(
                R.id.edt_content);

        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (TextUtils.isEmpty(edt.getText())) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                        String content = edt.getText().toString().trim();

                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("联系记录");
                        post.setDataId(item.getUuid());
                        post.setContent(content);
                        comment(post, item);
                        popWiw.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                    // /提交内容
                    String content = edt.getText().toString().trim();

                    SupportAndCommentPost post = new SupportAndCommentPost();
                    post.setFromId(Global.mUser.getUuid());
                    post.setToId(item.getCreatorId());
                    post.setDataType("联系记录");
                    post.setDataId(item.getUuid());
                    post.setContent(content);
                    comment(post, item);
                    popWiw.dismiss();
                }
            }
        });
        /*popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });*/

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.activity_add_record, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Contact space) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
//        et_comment.setText("");
//        InputSoftHelper.hiddenSoftInput(getActivity(), et_comment);
//        ll_bottom.setVisibility(View.GONE);
        hideShowSoft();
        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                space.setCommentNumber(space.getCommentNumber() + 1);
                getCommentList(mContact);
                ProjectRecordFragment.isReasum = true;
                ClientContactListFragment.isResume = true;
                ConatactLaneFragment.isReasume = true;
                ClientContactListFragment.isResume = true;
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
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 根据项目id 获取项目名称
     *
     * @param uuid
     * @param tv
     */
    private void getProjectName(String uuid, TextView tv) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取项目名称;
        JSONObject js = new JSONObject();
        try {
            js.put("ids", uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, js, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                tv.setText(JsonUtils.pareseData(response));
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (InputSoftHelper.isSoftShowing(this)) {
            InputSoftHelper.hideShowSoft(mContext);
        }
    }

    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }
}
