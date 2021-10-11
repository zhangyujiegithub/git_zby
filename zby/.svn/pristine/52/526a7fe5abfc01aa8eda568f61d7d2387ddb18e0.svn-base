package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.activity.ActivityListActivity;
import com.biaozhunyuan.tianyi.activity.CaptureActivity;
import com.biaozhunyuan.tianyi.address.AddressListActivity;
import com.biaozhunyuan.tianyi.apply.ApplylistActivity;
import com.biaozhunyuan.tianyi.apply.model.WorkflowInstance;
import com.biaozhunyuan.tianyi.attendance.NewAttendanceActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.bespoke.BespokeListActivity;
import com.biaozhunyuan.tianyi.buglist.BugListActivity;
import com.biaozhunyuan.tianyi.business.BusinessListActivity;
import com.biaozhunyuan.tianyi.client.ClientListActivity;
import com.biaozhunyuan.tianyi.clue.ClueListActivity;
import com.biaozhunyuan.tianyi.common.model.other.FunctionBoard;
import com.biaozhunyuan.tianyi.contact.ContactRecordListActivity;
import com.biaozhunyuan.tianyi.curriculum.CurriculumlistActivity;
import com.biaozhunyuan.tianyi.dispatch.FawenActivity;
import com.biaozhunyuan.tianyi.examination.ExaminationlistActivity;
import com.biaozhunyuan.tianyi.expenseaccount.ExpenseAccountActivity;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.helper.JsonParser;
import com.biaozhunyuan.tianyi.helper.ParamCallback;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.indispatch.ShouwenFragment;
import com.biaozhunyuan.tianyi.information.InformationListActivity;
import com.biaozhunyuan.tianyi.invoices.InvoicesListActivity;
import com.biaozhunyuan.tianyi.log.LogListActivity;
import com.biaozhunyuan.tianyi.log.LogNewActivity;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.models.EnumFunctionPoint;
import com.biaozhunyuan.tianyi.models.MenuChildItem;
import com.biaozhunyuan.tianyi.models.MenuFunction;
import com.biaozhunyuan.tianyi.notice.NoticeListActivity;
import com.biaozhunyuan.tianyi.notice.NoticeNewActivity;
import com.biaozhunyuan.tianyi.product.ProductListActivity;
import com.biaozhunyuan.tianyi.project.ProjectAttendanceActivity;
import com.biaozhunyuan.tianyi.project.ProjectListActivity;
import com.biaozhunyuan.tianyi.space.SpaceListActivity;
import com.biaozhunyuan.tianyi.task.TaskListActivityNew;
import com.biaozhunyuan.tianyi.task.TaskNewActivity;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.InfoUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.ParamUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.DragFloatActionButton;
import com.biaozhunyuan.tianyi.view.NumImageView;
import com.biaozhunyuan.tianyi.view.OnViewGlobalLayoutListener;
import com.biaozhunyuan.tianyi.view.WaveView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;
import com.biaozhunyuan.tianyi.wechat.WeChatRecordActivity;
import com.biaozhunyuan.tianyi.workorder.WorkOrderListActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import me.leolin.shortcutbadger.ShortcutBadger;
import okhttp3.Request;

import static com.biaozhunyuan.tianyi.common.utils.InfoUtils.getPermissionInfo;

/**
 * 待办(新首页)
 */

public class MenuHomeFragmentNew extends Fragment {

    private Context mContext;//上下文
    private GridView menuGridView; //功能点导航Gridview
    private LinearLayout menu_ll; //功能点导航 更多按钮父容器
    private List<MenuChildItem> itemOAList = new ArrayList<MenuChildItem>(); //oa功能点
    private List<MenuChildItem> lastOAList = new ArrayList<MenuChildItem>(); //oa功能点
    private CommanAdapter<MenuChildItem> menuListAdapter; //展示在首页的功能点列表适配器
    private CommanAdapter<MenuChildItem> menuPopAdapter;  //展示在更多的功能点列表适配器
    private MenuFunction function;
    private Demand approvalDemand;
    private CommonPopupWindow popupWindow; //展示更多功能点的popupwindow
    private GridView popup_gv; //更多种的功能点导航Gridview
    private List<FunctionBoard> mLinearLayoutList;
    private View showView;
    private ImageView ivScan; //扫码
    private LinearLayout ll_root; //用户自定义布局 父容器
    private HomeTaskLayout taskLayout; //首页布局 任务模块
    private HomeApplyLayout applyLayout; //首页布局 待我审批模块
    private HomeNoticeLayout noticeLayout;//首页布局 通知模块
    private int screenHeight; //屏幕高度/2
    private DragFloatActionButton voiceButton; //悬浮的语音按钮
    private BaseSelectPopupWindow voicePop; //语音命令弹框
    private HomeActivity activity;
    private LinearLayout tvRedact; //编辑
    private List<H5Menu> h5Menus; //全部看板H5页面的集合

    private final int REQUEST_SCAN = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);
        initData();
        initView(view);
        getPermissionInfo();
        initMyApprovalDeamnd();
        ParamUtils.getMenuCountList(mContext);
        getHomeComposeType();
        showDefaultOAFunction();
        setOnTouch();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(getClass().getName(), "requestCode=" + requestCode + ",resultCode=" + resultCode);
        if (requestCode == REQUEST_SCAN && data != null) {
            String scanCode = data
                    .getStringExtra(CaptureActivity.RESULT_SCAN_CODE);
            if (!TextUtils.isEmpty(scanCode)) {

                Intent intent = new Intent(mContext, WebviewNormalActivity.class);
                if (scanCode.contains("http")) {
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, scanCode);
                } else {
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, Global.BASE_JAVA_URL + scanCode);
                }
                startActivity(intent);
            }
        }
    }

    /**
     * 获取首页排版 按顺序添加用户自定义的首页内容
     */
    private void getHomeComposeType() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取H5灵活菜单 + "?menuType=menu";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                mLinearLayoutList.clear();
                h5Menus = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), H5Menu.class);

                ParamUtils.getParam(ParamUtils.MENUHOME_FUNCTION_BOARD, new ParamCallback() {
                    @Override
                    public void onParam(String value) {
                        List<FunctionBoard> functionBoards = new ArrayList<>();
                        try {
                            JSONArray ja = new JSONArray(value);
                            for (int i = 0; i < ja.length(); i++) {
                                FunctionBoard fb = new FunctionBoard();
                                JSONObject jo = ja.getJSONObject(i);
                                fb.setIndex(Integer.parseInt(jo.optString("index")));
                                fb.setFunction(jo.optString("title"));
                                functionBoards.add(fb);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                    List<FunctionBoard> functionBoards = JsonUtils.jsonToArrayEntity(value, FunctionBoard.class);
                        if ("null".equals(value)) {
                            updateDataList(new ArrayList<>());
                        } else {
                            if (functionBoards.size() > 0) {
                                updateDataList(functionBoards);
                            } else {
                                initBoard();
                            }
                        }
                    }

                    @Override
                    public void onFailure() {
                        initBoard();
                    }
                });

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                initBoard();
            }
        });
    }

    /**
     * 更新界面
     */
    private void updateDataList(List<FunctionBoard> functionBoards) {
        for (FunctionBoard fb : functionBoards) {
            switch (fb.getFunction()) {
                case "通知":
                    addNoticeBoard(fb.getIndex());
                    break;
                case "申请":
                    addApplyBoard(fb.getIndex());
                    break;
                case "任务":
                    addTaskBoard(fb.getIndex());
                    break;
                default:
                    for (int i = 0; i < h5Menus.size(); i++) {
                        if (fb.getFunction().equals(h5Menus.get(i).getTitle())) {
                            addH5Board(h5Menus.get(i).getH5url(), fb.getFunction(), fb.getIndex());
                            break;
                        }
                    }
                    break;
            }
        }
        initParentLayout();
    }

    /**
     * 初始化首页模块看板
     */
    private void initBoard() {
        initLayout();
        initParentLayout();
    }


    /**
     * 初始化原生布局
     */
    private void initLayout() {
        addNoticeBoard(1);
        addApplyBoard(2);
        addTaskBoard(3);
//        addH5Board("https://www.baidu.com/", "百度", 4);
    }

    /**
     * 将集合中的layout添加到父容器中
     */
    private void initParentLayout() {
        ViewHelper.sortList(mLinearLayoutList);
        String value = ParamUtils.listToJson(mLinearLayoutList);
        if ("[]".equals(value)) {
            value = "null";
        }
        ParamUtils.addParam(ParamUtils.MENUHOME_FUNCTION_BOARD, value);
        ll_root.removeAllViews();
        for (FunctionBoard fb : mLinearLayoutList) {
            ll_root.addView(fb.getLayout());
        }
    }

    /**
     * 判断模块是否已经存在于页面 如果不存在则添加
     *
     * @param function
     * @param board
     */
    public void isExistFBandAdd(String function, FunctionBoard board) {
        boolean isRePeat = false;
        for (int i = 0; i < mLinearLayoutList.size(); i++) {
            FunctionBoard fb = mLinearLayoutList.get(i);
            if (function.equals(fb.getFunction())) {
                isRePeat = true;
                fb.setLayout(board.getLayout());
                fb.setIndex(board.getIndex());
            }
        }
        if (!isRePeat) {
            mLinearLayoutList.add(board);
        }
    }

    /**
     * 添加任务看板
     */
    private void addTaskBoard(int index) {
        if (taskLayout == null) {
            taskLayout = new HomeTaskLayout(mContext);
        }
        isExistFBandAdd("任务", new FunctionBoard(taskLayout, index, "任务"));
    }

    /**
     * 添加申请看板
     */
    private void addApplyBoard(int index) {
        if (applyLayout == null) {
            applyLayout = new HomeApplyLayout(mContext);
        }


        applyLayout.setOnAuditSuccessListener(new HomeApplyLayout.AuditSuccessListener() {
            @Override
            public void onAuditSuccess() {
                getMyApprovalList(); // 审批完成重新刷新待审批的数量
            }
        });
        isExistFBandAdd("申请", new FunctionBoard(applyLayout, index, "申请"));
    }

    /**
     * 添加通知看板
     */
    private void addNoticeBoard(int index) {
        if (noticeLayout == null) {
            noticeLayout = new HomeNoticeLayout(mContext);
        }
        isExistFBandAdd("通知", new FunctionBoard(noticeLayout, index, "通知"));
    }

    /**
     * 添加H5页面
     */
    private void addH5Board(String url, String title, int index) {
        HomeNormalWebViewLayout webviewLayout = new HomeNormalWebViewLayout(mContext, url, title);
        isExistFBandAdd(title, new FunctionBoard(webviewLayout, index, title));
    }


    public void getAllUser() {
        InfoUtils.getAllStaff(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
//                            getHomeComposeType();
                        if (applyLayout != null)
                            applyLayout.refreshDataList();
                        if (noticeLayout != null) {
                            noticeLayout.refreshData();
                        }
                        return true;
                }
                return false;
            }
        }));
    }

    private void setOnTouch() {
        tvRedact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeBoardSettingActivity.class);
                startActivity(intent);
            }
        });
        menu_ll.setOnClickListener(new View.OnClickListener() { //展开更多功能点导航
            @Override
            public void onClick(View v) {
                initPopupWindow();
            }
        });
        menuGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuChildItem item = itemOAList.get(position);
                skipMenuItemClick(item);
            }
        });
        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoicePop();
            }
        });

        /**
         * 扫描二维码
         */
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_SCAN);
            }
        });
    }

    /**
     * 语音命令弹框
     */
    private void showVoicePop() {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (voicePop == null) {
            voicePop = new BaseSelectPopupWindow(mContext, R.layout.pop_voice_order);
            voicePop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            voicePop.setFocusable(true);
            voicePop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            voicePop.setShowTitle(false);
            voicePop.setBackgroundDrawable(new ColorDrawable(0));


            ImageView mIvVoice = voicePop.getContentView().findViewById(R.id.iv_voice_value); //语音按钮
            WaveView mWaveView = voicePop.getContentView().findViewById(R.id.waveView);  //按下语音按钮时的动画效果
            TextView tvInputText = voicePop.getContentView().findViewById(R.id.voice_input); //语音输出文字
            SpeechRecognizer mIat = initVoice();
            RecognizerListener mRecoListener = new RecognizerListener() {

                @Override
                public void onVolumeChanged(int i) {
                    if (i == 0) {
                        mIvVoice.setImageResource(R.drawable.icon_voice_value1);
                    } else {
                        int value = i / 5;
                        switch (value) {
                            case 0:
                            case 1:
                                mIvVoice.setImageResource(R.drawable.icon_voice_value1);
                                break;
                            case 2:
                            case 3:
                                mIvVoice.setImageResource(R.drawable.icon_voice_value2);
                                break;
                            case 4:
                            case 5:
                                mIvVoice.setImageResource(R.drawable.icon_voice_value3);
                                break;
                            default:
                                mIvVoice.setImageResource(R.drawable.icon_voice_value4);
                                break;
                        }
                    }
                }

                @Override
                public void onBeginOfSpeech() {

                }

                @Override
                public void onEndOfSpeech() {
                    Logger.i("onResult" + "onEndOfSpeech");
                }

                @Override
                public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                    String results = recognizerResult.getResultString();
                    Logger.i("onResult" + results + "");
                    String text = JsonParser.parseIatResult(results);

                    if (tvInputText != null) {
//                        String inputText = tvInputText.getText().toString() + text;

                        if (text.contains("新建")) {
                            if (text.contains("任务")) {
                                startActivity(new Intent(mContext, TaskNewActivity.class));
                            } else if (text.contains("通知")) {
                                startActivity(new Intent(mContext, NoticeNewActivity.class));
                            } else if (text.contains("日志")) {
                                startActivity(new Intent(mContext, LogNewActivity.class));
                            }
                        } else {
                            tvInputText.setText(text);
                        }

                    }
                }

                @Override
                public void onError(SpeechError speechError) {
                    Logger.i(speechError.toString());
                }

                @Override
                public void onEvent(int i, int i1, int i2, String s) {

                }
            };

            mIvVoice.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mWaveView.startAnim(); //开始动画
                            mIvVoice.setImageResource(R.drawable.icon_voice_value1);
                            int i = mIat.startListening(mRecoListener);
                            if (i != ErrorCode.SUCCESS) {
                                Logger.d("识别失败,错误码: " + i);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            mWaveView.stopAnim();  //结束动画
                            mIat.stopListening();
                            mIvVoice.setImageResource(R.drawable.ico_voice_value_default);
                            break;
                    }
                    return true;
                }
            });
            voicePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lp.alpha = 1f;
                    activity.getWindow().setAttributes(lp);
                    activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
        }
        lp.alpha = 0.8f;
        activity.getWindow().setAttributes(lp);
        voicePop.showAtLocation(activity.getLayoutInflater().inflate(R.layout.fragment_menu_home, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private SpeechRecognizer initVoice() {
        //1.创建SpeechRecognizer对象，第二个参数：本地识别时传InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(getContext(), null);
        //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        return mIat;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (taskLayout != null) {
            taskLayout.refreshList();
        }
        if (applyLayout != null) {
            applyLayout.refreshDataList();
        }
        if (noticeLayout != null) {
            noticeLayout.refreshData();
        }
        getMyApprovalList();
    }

    private void initData() {
        mContext = getActivity();
        activity = (HomeActivity) mContext;
        mLinearLayoutList = new ArrayList<>();
        h5Menus = new ArrayList<>();
        function = new MenuFunction();
        screenHeight = ViewHelper.getScreenHeight(mContext) / 2; //屏幕高度
        EventBus.getDefault().register(this);
        ShortcutBadger.applyCount(mContext, 0);
    }

    /**
     * EventBus接收事件
     *
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(BoardMessageEvent messageEvent) {
        List<FunctionBoard> functionList = messageEvent.getFunctionList();
        for (FunctionBoard fb : functionList) { //在这里将需要展示的模块 从原有的集合中先取出来
            for (FunctionBoard functionBoard : mLinearLayoutList) {
                if (fb.getFunction().equals(functionBoard.getFunction())) {
//                    functionBoard.setIndex(fb.getIndex());
                    fb.setLayout(functionBoard.getLayout());
                }
            }
        }
        mLinearLayoutList = functionList;
        updateDataList(mLinearLayoutList);
    }


    private void initView(View view) {
        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动

        menuGridView = view.findViewById(R.id.Menu_ListView);
        tvRedact = view.findViewById(R.id.tv_redact);
        menu_ll = view.findViewById(R.id.Menu_ll);
        menu_ll.setMinimumWidth(ViewHelper.getScreenWidth(mContext) / 5);
        showView = view.findViewById(R.id.view);
        ll_root = view.findViewById(R.id.ll_home_root);
        ivScan = view.findViewById(R.id.iv_scan);
        voiceButton = view.findViewById(R.id.voiceButton);
    }

    private void initMyApprovalDeamnd() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.待我审批;
        approvalDemand = new Demand();
        approvalDemand.pageSize = 999;
        approvalDemand.src = url;
        approvalDemand.pageIndex = 1;
    }

    /**
     * 更多导航栏功能点popupwindow
     */
    private void initPopupWindow() {
        popupWindow = new CommonPopupWindow.Builder(getActivity())
                //设置PopupWindow布局
                .setView(R.layout.popup_home)
                //设置宽高
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                //设置动画
                .setAnimationStyle(R.style.AnimDown)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                .setBackGroundLevel(0.65f)
                //设置PopupWindow里的子View及点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        //给gridview限制最大高度
                        LinearLayout llGrid = view.findViewById(R.id.ll_grid);
                        llGrid.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(llGrid, screenHeight));

                        popup_gv = view.findViewById(R.id.popup_gv);
                        if (menuPopAdapter == null) {
                            menuPopAdapter = getMenuListAdapter(itemOAList);
                        }
                        popup_gv.setAdapter(menuPopAdapter);
                        popup_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                MenuChildItem item = itemOAList.get(position);
                                skipMenuItemClick(item);

                            }
                        });
                    }
                })
                //设置外部是否可点击 默认是true
                .setOutsideTouchable(true)
                //开始构建
                .create();
        popupWindow.showAsDropDown(showView);
    }

    /**
     * 显示oa默认功能点
     */
    private void showDefaultOAFunction() {
        List<EnumFunctionPoint> functionList = function.getDefaultOAFunctions();
//        getDefaultOAFunction(functionList);
        getMunuPoint(functionList);
    }

    private void getMunuPoint(final List<EnumFunctionPoint> list) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.功能模块权限;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<FunctionMenu> listName = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(response, "Data"), FunctionMenu.class);
                    if (listName == null || (listName != null && listName.size() == 0)) {
                        Toast toast = Toast.makeText(mContext, "您暂时无权限,请到PC端设置", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    PreferceManager.getInsance().saveValueBYkey("UserPermission", JsonUtils.pareseData(response));
                    for (int i = 0; i < listName.size(); i++) {
                        for (int j = 0; j < listName.get(i).getChildrenSubmenu().size(); j++) {
                            String name = listName.get(i).getChildrenSubmenu().get(j).getId();
                            switch (name) {
                                case "aa41149b35904c339e543bfc1d61c924": //我的考勤
                                    list.add(EnumFunctionPoint.ATTANCE);
                                    break;
                                case "d27c1cac654e11e7a75b001517543a38": //工作日志
                                    list.add(EnumFunctionPoint.LOG);
                                    break;
                                case "d5d3fda3654e11e7a75b001517543a38": //任务计划
                                    list.add(EnumFunctionPoint.TASK);
                                    break;
                                case "b4036abd654e11e7a75b001517543a38":  //公告通知
                                    list.add(EnumFunctionPoint.NOTICE);
                                    break;
                                case "41eac0a8ac64457d9d81fbb1b0bf5323":  //我的申请
                                    list.add(EnumFunctionPoint.APPLY);
                                    break;
                                case "21fe612a54d842238c7cc2fbe58c4ed4":  //莱恩斯我的申请
                                    list.add(EnumFunctionPoint.APPLY);
                                    PreferceManager.getInsance().saveValueBYkey(Global.mUser.getUuid() + "APPLY", "21fe612a54d842238c7cc2fbe58c4ed4");
                                    break;
                                case "72c118359e3b47a5bf859ea431131460":  //产品管理
                                    list.add(EnumFunctionPoint.PRODUCT);
                                    break;
                                case "预约管理":  //预约管理
                                    list.add(EnumFunctionPoint.CHANGHUI_BESPOKE_LIST);
                                    break;
                                case "1ac20722419e4f739d949b3d913ea778":  //微信聊天记录
                                    list.add(EnumFunctionPoint.WE_CHAT_RECORD);
                                    break;
                                case "b8295ea13ac1450993953daffd64432f":  //合同管理
                                    list.add(EnumFunctionPoint.CONPACT);
                                    break;
                                case "活动":
                                    list.add(EnumFunctionPoint.ACTIVITY);//活动
                                    break;
                                case "fe72556015b0452790a9c190ab4951f5": //联系记录
                                    list.add(EnumFunctionPoint.CONTACTS);
                                    break;
                                /*case "43eb05dfe7b74b3fbdc0a117e5d12d04":  //通讯录
                                    list.add(EnumFunctionPoint.INSIDE_COMMUNICATION);
                                    break;*/
                                case "b0526a815ddb4e40a50a67989d4e1c07":  //客户列表
                                    list.add(EnumFunctionPoint.CLIENT);
                                    break;
                                case "2158c04a54d34afdb745d021618ab230":  //项目列表
                                    list.add(EnumFunctionPoint.PROJECT);
                                    break;
                                case "aad9b4b6f4bf42df825710cd1684b283": //项目签到
                                    list.add(EnumFunctionPoint.PROJECT_ATTENDANCE);
                                    break;
                                case "发文管理":
                                    list.add(EnumFunctionPoint.DISPATCH);//发文管理
                                    break;
                                case "收文管理":
                                    list.add(EnumFunctionPoint.INCOMING);//收文管理
                                    break;
                                case "39c53047bf6141d3aa8981d5dbb6314f":  //项目列表
                                    list.add(EnumFunctionPoint.CRMPROJECT);
                                    break;
                                case "a9149cc0c9e54d36bb220f3cacd23c49":  //商机列表
                                    list.add(EnumFunctionPoint.CRMBUSINESS);
                                    break;
                                case "1e363a27c66a4430b2cfa994e7af0ee3": //考试中心
                                    list.add(EnumFunctionPoint.EXAMINATION);
                                    break;
                                case "241124f4c9164d85a3872a3edc5e06c7": //课程中心
                                    list.add(EnumFunctionPoint.CURRICULUM);
                                    break;
//                                case "12bd6b338726435aa4bfa6615ce0f769": //单据
//                                    list.add(EnumFunctionPoint.INVOICES);
//                                    break;
                                case "2b042ca7ae5e4e4d96eb42b185d95f6e"://工单
                                    list.add(EnumFunctionPoint.WORKORDER);
                                    break;
                                case "90fc3df83e6040139b7124c7bb594ca6"://空间
                                    list.add(EnumFunctionPoint.SPACE);
                                    break;
                                case "3b9e574627414b0d9fab8993ce36eb73"://活动
                                    list.add(EnumFunctionPoint.ACTIVITY);
                                    break;
                                case "d854ddc540d4486ea11bfacf3d0b464c"://线索
                                    list.add(EnumFunctionPoint.CRMCLUE);
                                    break;
                                case "086345144d4f4d2193751d8ca4f938b2"://bug管理
                                    list.add(EnumFunctionPoint.BUG);
                                    break;
                                case "b7219f5358c54f5795e02d3275867683": //资讯管理
                                    list.add(EnumFunctionPoint.INFORMATION);
                                    break;
                            }
                        }
                    }
//                    list.add(EnumFunctionPoint.APPLYFOR_INVOICE); //临时添加 报销单
                    itemOAList = function.getFunctions(list, true);
                    itemOAList = ParamUtils.sortMenuByClickNumber(itemOAList); //按照点击次数排序
                    sortMenuList(itemOAList);//排序
//                    lastOAList = setShowMenuNumber(itemOAList);
//                    menuListAdapter = getMenuListAdapter(lastOAList);
//                    menuGridView.setAdapter(menuListAdapter);
                    getH5Menu();
                    getMyApproval();
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
     * 功能点list排序
     *
     * @param list
     */
    private void sortMenuList(List<MenuChildItem> list) {
        //重新排序
        Collections.sort(list, new Comparator<MenuChildItem>() {
            @Override
            public int compare(MenuChildItem o1, MenuChildItem o2) {
                return o2.count - o1.count;
            }
        });
    }

    /**
     * 看板功能list排序
     */
    private void sortBoardList(List<FunctionBoard> list) {
        //重新排序
        Collections.sort(list, new Comparator<FunctionBoard>() {
            @Override
            public int compare(FunctionBoard o1, FunctionBoard o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });
    }

    /**
     * 设置功能点展示数量
     */
    private List<MenuChildItem> setShowMenuNumber(List<MenuChildItem> list) {
        List<MenuChildItem> childItemList = new ArrayList<>();
        if (list.size() > 5) {  //如果大于5 则只显示常用4条功能和更多按钮
            menu_ll.setVisibility(View.VISIBLE);
            menuGridView.setNumColumns(4);
            for (int i = 0; i < list.size() && i < 4; i++) {
                childItemList.add(list.get(i));
            }
        } else {  //如果不大于5 直接显示一列 隐藏更多
            menu_ll.setVisibility(View.GONE);
            menuGridView.setNumColumns(5);
            for (int i = 0; i < list.size(); i++) {
                childItemList.add(list.get(i));
            }
        }

        return childItemList;
    }

    /**
     * 获取H5菜单灵活配置
     */
    private void getH5Menu() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取H5灵活菜单;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<H5Menu> h5Menus = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), H5Menu.class);
                if (h5Menus != null && h5Menus.size() > 0) {
                    for (H5Menu h5Menu : h5Menus) {
                        MenuChildItem item = new MenuChildItem();
                        item.icon = h5Menu.getLogo();
                        item.category = "H5";
                        item.title = h5Menu.getTitle();
                        item.URL = h5Menu.getH5url();
                        itemOAList.add(item);
                    }
                }
                lastOAList = setShowMenuNumber(itemOAList);
                menuListAdapter = getMenuListAdapter(lastOAList);
                menuGridView.setAdapter(menuListAdapter);
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
     * 获取待我审批数量
     */
    public void getMyApproval() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getMyApprovalList();
            }
        });
    }

    /**
     * 待我审批数量
     */
    private void getMyApprovalList() {
        StringRequest.postAsyn(approvalDemand.src, approvalDemand, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<WorkflowInstance> workflowInstances = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.getStringValue(response, "Data"), "data"), WorkflowInstance.class);
                    if (workflowInstances != null && menuPopAdapter == null) { //更新首次进入popupwindow使用的数据源
                        for (int i = 0; i < itemOAList.size(); i++) {
                            if (itemOAList.get(i).title.equals("申请")) {
                                itemOAList.get(i).unreadCount = workflowInstances.size();
                                break;
                            }
                        }
                    }

                    if (workflowInstances != null && menuListAdapter != null) { //重新回到首页时 更新gridview中使用的数据源
                        List<MenuChildItem> dataList = menuListAdapter.getDataList();
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).title.equals("申请")) {
                                dataList.get(i).unreadCount = workflowInstances.size();
                                menuListAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                    if (workflowInstances != null && menuPopAdapter != null) { //重新回到首页时 更新popupwindow中使用的数据源
                        List<MenuChildItem> dataList1 = menuPopAdapter.getDataList();
                        for (int i = 0; i < dataList1.size(); i++) {
                            if (dataList1.get(i).title.equals("申请")) {
                                dataList1.get(i).unreadCount = workflowInstances.size();
                                menuPopAdapter.notifyDataSetChanged();
                                break;
                            }
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

    private CommanAdapter<MenuChildItem> getMenuListAdapter(List<MenuChildItem> gridItems) {
        return new CommanAdapter<MenuChildItem>(gridItems, getActivity(), R.layout.item_menu_list) {
            public void convert(int position, MenuChildItem item, BoeryunViewHolder viewHolder) {
                ImageView iv = viewHolder.getView(R.id.menu_icon);
                int padding = (int) ViewHelper.dip2px(mContext, 8);
                if ("H5".equals(item.category)) {
                    if (!TextUtils.isEmpty(item.icon)) {
                        String url = Global.BASE_JAVA_URL + GlobalMethord.显示H5菜单 + item.icon;
                        ImageUtils.displyImage(url, iv, R.drawable.icon_home_space_new);
                        iv.setPadding(padding, padding, padding, padding);
                    } else {
                        iv.setPadding(0, 0, 0, 0);
                        viewHolder.setImageResoure(R.id.menu_icon, R.drawable.icon_home_space_new);
                    }
                } else {
                    iv.setPadding(0, 0, 0, 0);
                    viewHolder.setImageResoure(R.id.menu_icon, item.ico);
                }
                if (item.title.equals("联系记录")) {
                    viewHolder.setTextValue(R.id.menu_tv, Global.CONTACT_TITLE);
                } else {
                    viewHolder.setTextValue(R.id.menu_tv, item.title);
                }

                NumImageView view = viewHolder.getView(R.id.menu_icon);
                if (item.title.equals("申请")) {
                    if (item.unreadCount > 0) {
                        view.setNum(item.unreadCount);
                        ShortcutBadger.applyCount(mContext, item.unreadCount);
                    } else {
                        view.setNum(0);
                        ShortcutBadger.applyCount(mContext, 0);
                    }
                } else {
                    view.setNum(0);
                }
            }
        };
    }

    /**
     * 功能点点击事件
     *
     * @param item
     */
    private void skipMenuItemClick(MenuChildItem item) {
        if ("H5".equals(item.category)) {
            Intent intent = new Intent(mContext, WebviewNormalActivity.class);
            intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, item.title);
            intent.putExtra("isHome", true);
            if (!TextUtils.isEmpty(item.URL)) {
                if (item.URL.contains("http")) {
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, item.URL);
                } else {
                    intent.putExtra(WebviewNormalActivity.EXTRA_URL, Global.BASE_JAVA_URL + item.URL);
                }
            }
            startActivity(intent);
        } else {
            ParamUtils.clickMenuAddParams(item.ponit); //每次点击菜单次数加一，并上传到服务器
            switch (item.ponit) {
                case ATTANCE:  //跳转考勤列表页面
                    setMemuItemClickListener(NewAttendanceActivity.class);
                    break;

                case TASK:  //跳转任务列表页面
                    setMemuItemClickListener(TaskListActivityNew.class);
                    break;

                case NOTICE:  //跳转通知列表页面
                    setMemuItemClickListener(NoticeListActivity.class);
                    break;

                case LOG:  //跳转日志列表页面
                    setMemuItemClickListener(LogListActivity.class);
                    break;

                case INSIDE_COMMUNICATION:  //跳转通讯录列表页面
                    setMemuItemClickListener(AddressListActivity.class);
                    break;

                case APPLY:  //跳转申请列表页面
                    setMemuItemClickListener(ApplylistActivity.class);
                    break;
                case CLIENT: //客户
                    setMemuItemClickListener(ClientListActivity.class);
                    break;
                case PRODUCT: //产品
                    setMemuItemClickListener(ProductListActivity.class);
                    break;
                case INVOICES: //单据
                    setMemuItemClickListener(InvoicesListActivity.class);
                    break;
                case WORKORDER: //工单
                    setMemuItemClickListener(WorkOrderListActivity.class);
                    break;
                case CHANGHUI_BESPOKE_LIST: //预约
                    setMemuItemClickListener(BespokeListActivity.class);
                    break;
                case ACTIVITY: //活动
                    setMemuItemClickListener(ActivityListActivity.class);
                    break;
                case CONTACTS: //跟进记录
                    setMemuItemClickListener(ContactRecordListActivity.class);
                    break;
                case WE_CHAT_RECORD://微信聊天记录
                    setMemuItemClickListener(WeChatRecordActivity.class);
                    break;
                case SPACE://空间
                    setMemuItemClickListener(SpaceListActivity.class);
                    break;
                case DISPATCH://发文
                    setMemuItemClickListener(FawenActivity.class);
                    break;
                case INCOMING://收文
                    setMemuItemClickListener(ShouwenFragment.class);
                    break;
                case CRMPROJECT: //CRM项目列表
                    setMemuItemClickListener(ProjectListActivity.class);
                    break;
                case PROJECT_ATTENDANCE: //项目打卡页面
                    setMemuItemClickListener(ProjectAttendanceActivity.class);
                    break;
                case CRMBUSINESS: //商机列表
                    setMemuItemClickListener(BusinessListActivity.class);
                    break;
                case EXAMINATION: //考试中心
                    setMemuItemClickListener(ExaminationlistActivity.class);
                    break;
                case CURRICULUM: //课程中心
                    setMemuItemClickListener(CurriculumlistActivity.class);
                    break;
                case APPLYFOR_INVOICE: //报销
                    setMemuItemClickListener(ExpenseAccountActivity.class);
                    break;
                case CRMCLUE: //线索
                    setMemuItemClickListener(ClueListActivity.class);
                    break;
                case BUG: //bug
                    setMemuItemClickListener(BugListActivity.class);
                    break;
                case INFORMATION: //资讯
                    setMemuItemClickListener(InformationListActivity.class);
                    break;
            }
        }
        //如果更多展开 就隐藏
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 模块点击事件监听
     *
     * @param newActivity 新跳转页面
     */
    private void setMemuItemClickListener(final Class<?> newActivity) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), newActivity);
        startActivity(intent);
    }

    /**
     * 接收选择的员工
     */
    public void receiveSelectedUser(User user) {
        if (taskLayout != null) {
            taskLayout.receiveSelectedUser(user);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
