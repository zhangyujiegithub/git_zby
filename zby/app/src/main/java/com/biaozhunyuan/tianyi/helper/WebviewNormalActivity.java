package com.biaozhunyuan.tianyi.helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.FormInfoActivity;
import com.biaozhunyuan.tianyi.attch.AttachListActivity;
import com.biaozhunyuan.tianyi.business.Business;
import com.biaozhunyuan.tianyi.business.BusinessAddActivity;
import com.biaozhunyuan.tianyi.business.BusinessInfoActivity;
import com.biaozhunyuan.tianyi.client.AddRecordActivity;
import com.biaozhunyuan.tianyi.client.ChClientInfoActivity;
import com.biaozhunyuan.tianyi.client.Client;
import com.biaozhunyuan.tianyi.client.CustomerDetailsActivity;
import com.biaozhunyuan.tianyi.clue.ClueListInfoActivity;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.CookieUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.contact.ContactNewActivity;
import com.biaozhunyuan.tianyi.information.InformationModel;
import com.biaozhunyuan.tianyi.space.SpaceListActivity;
import com.biaozhunyuan.tianyi.utils.UrlUtils;
import com.biaozhunyuan.tianyi.view.CustomerDrawerLayout;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;
import okhttp3.Request;

import static com.biaozhunyuan.tianyi.client.CustomerDetailsActivity.EXTRA_CLIENT_ID;
import static com.biaozhunyuan.tianyi.clue.ClueListInfoActivity.CLUE_LISTINFO_EXTRA;
import static com.biaozhunyuan.tianyi.contact.ContactNewActivity.EXTRA_CLIENT_NAME;

/***
 * ??????????????????
 *
 * @author K
 *
 */
@SuppressLint("NewApi")
public class WebviewNormalActivity extends BaseActivity {

    public static final String EXTRA_URL = "extral_url_webview_normal";
    public static final String EXTRA_TITLE = "extral_title_webview_normal";
    public static final String EXTRA_IS_ROOM = "extral_isroom_webview_normal";
    public static final String EXTRA_IS_INTERCEPT = "extral_intercept_webview_normal";
    public static final int INTENT_NEW_BUSINESS = 100; //????????????
    private Context mContext;
    private WebView webView;
    private BoeryunHeaderView headerView;
    private DictIosPickerBottomDialog mDictIosPickerBottomDialog;
    private String mUrl;
    private String mTitle;
    private ProgressBar progressBar;
    private Dialog progressDialog;
    private boolean isShowShare = false; //????????????????????????

    public static boolean isZoom = false; //??????????????????
    private boolean isInterceptEvent = true; //????????????????????????????????????
    private boolean isHome = false; //???????????????????????????
    private InformationModel information;

    public static final int FILECHOOSER_RESULTCODE = 1;
    private final static int RESULT_CODE_ICE_CREAM = 2;

    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    private ValueCallback<Uri> mUploadMessage;

    /**
     * ???????????? ????????????????????????
     */
    private Demand<Contact> menuDemand;
    private List<Contact> recordList;
    private CustomerDrawerLayout mDrawerLayout;
    private LinearLayout menu_right;
    private TextView tvContactCustomerName;
    private CommanAdapter<Contact> menuAdapter;
    private int menuPageIndex = 1; //?????????
    private DictionaryHelper helper;
    private String mCustomerId = "";
    private PullToRefreshAndLoadMoreListView menu_lv; //???????????????listview
    private TextView tv_contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        intData();
        initView();
        loadWebView();
        setOnEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void intData() {
        mContext = this;
        helper = new DictionaryHelper(mContext);
        mDictIosPickerBottomDialog = new DictIosPickerBottomDialog(mContext);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        isHome = getIntent().getBooleanExtra("isHome", false);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        information = (InformationModel) getIntent().getSerializableExtra("InformationInfo");
        isZoom = getIntent().getBooleanExtra(EXTRA_IS_ROOM, false);
        isShowShare = getIntent().getBooleanExtra("isShowShare", false);
        isInterceptEvent = getIntent().getBooleanExtra(EXTRA_IS_INTERCEPT, true);
        Logger.i("url" + mUrl);

        if (mUrl.contains("zoom=true")) {
            isZoom = true;
        }


    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (isShowShare) {
                    shareArticle();
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

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //??????????????????
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// ??????????????????
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        menu_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Contact record = (Contact) menuAdapter.getDataList().get(position - 1);
                    Intent intent = new Intent(WebviewNormalActivity.this, AddRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contactInfo", record);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        menu_lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                menuPageIndex = 1;
                setMenuListData();
            }
        });
        menu_lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                setMenuListData();
            }
        });
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webview_webview);
        headerView = (BoeryunHeaderView) findViewById(R.id.header_webview);
        progressBar = (ProgressBar) findViewById(R.id.pbar_calendar_item);
        progressBar.setMax(100);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// ??????????????????
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        menu_right = findViewById(R.id.menu_right_rl);
        tvContactCustomerName = findViewById(R.id.tv_menu_contact);
        menu_lv = findViewById(R.id.menu_lv);
        tv_contactList = findViewById(R.id.tv_contactlist);

        int screenWidth = ViewHelper.getScreenWidth(mContext);
        ViewGroup.LayoutParams params = menu_right.getLayoutParams();
        params.width = screenWidth / 5 * 3;

        menu_right.setLayoutParams(params);

        if (!TextUtils.isEmpty(mTitle)) {
            headerView.setTitle(mTitle);
        }

        if (!TextUtils.isEmpty(mUrl) && mUrl.contains("?isHiddenTitle=1")) { //???????????????
            headerView.setVisibility(View.GONE);
        }

        if (isShowShare) {
            headerView.setRightTitleVisible(true);
            headerView.setRightTitle("??????");
        }

        progressDialog = new Dialog(WebviewNormalActivity.this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("?????????...");
        progressDialog.show();
    }


    /**
     * ???????????????????????????
     */
    private void loadWebView() {
        // requestFocusForForm();
        // textViewTitle.setText(typeName);
        // ??????????????????????????????
        // ???25%?????????????????????????????????????????????????????????????????????
        webView.setInitialScale(25);
        // ?????????????????????
        WebSettings webSettings = webView.getSettings();
        // ??????????????????javascript?????????
        webSettings.setJavaScriptEnabled(true);
        // ??????webview?????????????????????
        webSettings.setUseWideViewPort(true);
        // ???webview??????????????????
        webSettings.setLoadWithOverviewMode(true);
        // ??????????????????
        webSettings.setBuiltInZoomControls(isZoom);
        // ????????????
        webSettings.setSupportZoom(isZoom);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);
        synCookies(mContext, mUrl);
        Logger.i("url_webview::" + mUrl);
        //headers.put("Cookie",);
        webView.loadData("", "text/html", "UTF-8");
        webView.loadUrl(mUrl);
        if (mUrl.contains("wf/form/vsheet/form?") && isHome) { //???????????????????????????????????????
            webView.stopLoading();
            String workflowTemplateId = "";
            String id = "0";
            if (mUrl.contains("workflowTemplateId")) {
                workflowTemplateId = mUrl.substring(mUrl.indexOf("workflowTemplateId=") + 19, mUrl.indexOf("workflowTemplateId=") + 19 + 32);
            }
            Intent intent = new Intent(mContext, FormInfoActivity.class);
            intent.putExtra("workflowTemplateId", workflowTemplateId);
            startActivity(intent);
            finish();
        }

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);//??????????????????????????????
                    progressDialog.dismiss();
                } else {
                    progressBar.setVisibility(View.VISIBLE);//????????????????????????????????????
                    progressBar.setProgress(newProgress);//???????????????
                }
            }


            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (isInterceptEvent) {
                    String url = request.getUrl().toString().trim();
                    if (url.contains("/colseWebView")) { //????????????
                        webView.stopLoading();
                        finish();
                    } else if (url.contains("/customer/detail?")) { //?????????????????????
                        webView.stopLoading();
                        String[] split = url.split("=");
                        String customerId = split[1];

                        Intent intent = new Intent(mContext, CustomerDetailsActivity.class);
                        intent.putExtra(EXTRA_CLIENT_ID, customerId);
                        startActivity(intent);
                    } else if (url.contains("/customer/create")) { //?????????????????????
                        webView.stopLoading();
                        Intent intent = new Intent(mContext, ChClientInfoActivity.class);
                        intent.putExtra(ChClientInfoActivity.EXTRA_CLIENT_ID, "0");
                        intent.putExtra("isReadOnly", true);
                        startActivity(intent);
                    } else if (url.contains("wf/form/vsheet/form?")) { //?????????????????????
                        webView.stopLoading();
                        //??????URL??????????????????%???????????????"%25"??????????????????????????????
                        String str = UrlUtils.formatEncode(url).replaceAll("%25", "%");
                        String[] splitId = url.split("id=");
                        String[] split = str.split("\\?");

                        Map<String, String> map = new HashMap<>();
                        String[] split2 = url.split("id=0");
                        if (split2.length > 1 && !TextUtils.isEmpty(split2[1])) {
                            String[] split3 = split2[1].split("detaildata=");
                            String s;
                            if (split3.length > 0) {
                                s = split3[0];
                            } else {
                                s = split2[1];
                            }
                            String[] split0 = s.split("&");
                            for (int i = 0; i < split0.length; i++) {
                                String[] split1 = split0[i].split("=");
                                if (split1.length > 1) {
                                    map.put(split1[0], split1[1]);
                                }
                            }
                        }
                        String formUrl = Global.BASE_JAVA_URL + GlobalMethord.???????????? + "?" + split[1];
                        Intent intent = new Intent(mContext, FormInfoActivity.class);
                        intent.putExtra("exturaUrl", formUrl);
                        intent.putExtra("extentMap", (Serializable) map);
                        if (splitId[1].length() > 1) {
                            intent.putExtra("formDataId", splitId[1].split("&")[0]);
                        }
                        startActivity(intent);
                    } else if (url.contains("tel:")) { //????????????????????????
                        webView.stopLoading();
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } else if (url.contains("/crm/html/clue/detail?")) { //????????????
                        String[] split = url.split("id=");
                        String formUrl = Global.BASE_JAVA_URL + GlobalMethord.???????????? + split[1];
                        Intent intent = new Intent(WebviewNormalActivity.this, ClueListInfoActivity.class);
                        intent.putExtra(CLUE_LISTINFO_EXTRA, formUrl);
                        startActivity(intent);
                        webView.stopLoading();
                    } else if (url.contains("/spaceDeleteSkip")) { //????????????????????????
                        webView.stopLoading();
                        finish();
                        SpaceListActivity.isResume = true;
                    } else if (url.contains("/attachment/downloadFile?")) { //????????????
                        webView.stopLoading();
                        String[] split = url.split("id=");
                        String downUrl = Global.BASE_JAVA_URL + GlobalMethord.????????????
                                + split[1];
                        StringRequest.downloadFile(downUrl, "????????????.pdf", new StringResponseCallBack() {
                            @Override
                            public void onResponse(String response) {
                                showShortToast("????????????????????????????????????sd???????????????");
                            }

                            @Override
                            public void onFailure(Request request, Exception ex) {

                            }

                            @Override
                            public void onResponseCodeErro(String result) {

                            }
                        });

                    } else if (url.contains("downloadFileByMobile?")) { //??????????????????
                        webView.stopLoading();
                        String[] split = url.split("uuid=");
                        String attachIds = split[1];
                        Intent intent = new Intent(mContext, AttachListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AttachListActivity.ATTACH_IDS, attachIds);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (url.contains("contact/detail?")) { //??????????????????
                        webView.stopLoading();
                        String substring = url.substring(url.indexOf("?") + 1, url.length());
                        String[] split = substring.split("&");
                        Intent intent = new Intent(mContext, AddRecordActivity.class);
                        for (int i = 0; i < split.length; i++) {
                            String s = split[i];
                            if (s.contains("customerName")) {
                                intent.putExtra(EXTRA_CLIENT_NAME, URLDecoder.decode(s.split("=")[1]));
                            } else if (s.contains("customerId")) {
                                intent.putExtra(ContactNewActivity.EXTRA_CLIENT_ID, s.split("=")[1]);
                            }
                        }
                        intent.putExtra("advisorId", Global.mUser.getUuid());
                        startActivity(intent);
                    } else if (url.contains("contact/contactTile?")) {
                        webView.stopLoading();
                        Client client = new Client();
                        String[] split = url.split("&");
                        for (int i = 0; i < split.length; i++) {
                            String s = split[i];
                            if (s.contains("customerId")) {
                                client.setUuid(s.split("=")[1]);
                            } else if (s.contains("customerName")) {
                                client.setName(URLDecoder.decode(s.split("=")[1]));
                            }
                        }
                        openDrawerLayout(client);
                    } else if (url.contains("crm/html/saleschance/detail?uuid=")) {
                        webView.stopLoading();
                        String[] split = url.split("uuid=");
                        String[] split1 = split[1].split("&advisorId=");
                        String uuid = split1[0];
                        Intent intent = new Intent();
                        if (uuid.equals("0")) {
                            intent.setClass(mContext, BusinessAddActivity.class);
                        } else {
                            String advisorId = split1[1];
                            Business business = new Business();
                            business.setCanSave(advisorId.equals(Global.mUser.getUuid()));
                            business.setUuid(uuid);
                            intent.putExtra("Business", business);
                            intent.setClass(mContext, BusinessInfoActivity.class);
                        }
                        startActivityForResult(intent, INTENT_NEW_BUSINESS);
                    } else if (url.contains("/shareImg?imgUrl=")) {
                        webView.stopLoading();
                        String imgId = url.substring(url.indexOf("?uuid=") + 6);
                        String downloadUrl = url.substring(url.indexOf("?imgUrl=") + 8);
                        String fileName = imgId + ".jpg";

                        String filePath = FilePathConfig.getCacheDirPath();
                        File file = new File(filePath, fileName);
                        if (file.exists() && file.length() > 0) {
                            showShare(filePath, fileName);
                        } else {
                            ProgressDialogHelper.show(mContext, "?????????");
                            StringRequest.downloadFile(downloadUrl, fileName, new StringResponseCallBack() {
                                @Override
                                public void onResponse(String response) {
                                    ProgressDialogHelper.dismiss();
                                    showShare(FilePathConfig.getCacheDirPath(), fileName);
                                }

                                @Override
                                public void onFailure(Request request, Exception ex) {

                                }

                                @Override
                                public void onResponseCodeErro(String result) {
                                    ProgressDialogHelper.dismiss();
                                }
                            });
                        }
                    } else if (url.contains("crowdfunding/detail")) {
                        webView.stopLoading();
                        String para = url.substring(url.indexOf("crowdfunding/detail") + 19);
                        if (para.contains("workFlowId")) {
                            para = para.replaceAll("workFlowId", "workflowId");
                        }
                        String formUrl = Global.BASE_JAVA_URL + GlobalMethord.???????????? + para;
                        Intent intent = new Intent(mContext, FormInfoActivity.class);
                        intent.putExtra("exturaUrl", formUrl);
                        startActivity(intent);
                    }
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.getSettings().setJavaScriptEnabled(true);
                super.onPageFinished(view, url);
            }
        });

    }

    /**
     * ????????????cookie
     */
    public void synCookies(Context context, String url) {
//        List<Cookie> cookies = CookieUtil.getInstance().loadForRequest(HttpUrl.get(URI.create(url)));
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//??????
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.JSESSIONID + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.JSESSIONID));
        cookieManager.setCookie(Global.BASE_JAVA_URL, CookieUtils.rememberMe + "=" + PreferceManager.getInsance().getValueBYkey(CookieUtils.rememberMe));
        CookieSyncManager.getInstance().sync();
    }


    // 2.????????????????????????????????????
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILECHOOSER_RESULTCODE);
    }

    // 3.?????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else if (requestCode == INTENT_NEW_BUSINESS) {  //??????????????????????????????
            webView.reload();
        }
    }

    // 4. ?????????????????????Html??????
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }


    @Override
    // ????????????
    // ??????Activity??????onKeyDown(int keyCoder,KeyEvent event)??????
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mDrawerLayout.isDrawerOpen(menu_right)) {
            mDrawerLayout.closeDrawers();
            return true;
        } else {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                webView.goBack(); // goBack()????????????WebView???????????????
                return true;
            } else {
                finish();
                return true;
            }
        }
    }

    private void initMenuDemand() {
        menuDemand = new Demand(Contact.class);
        menuDemand.pageSize = 10;
        menuDemand.sortField = "createTime desc";
        menuDemand.dictionaryNames = "projectId.crm_project,customerId.crm_customer,stage.dict_contact_stage,contactWay.dict_contact_way";
    }

    /**
     * ??????????????????
     *
     * @param client
     */
    public void openDrawerLayout(Client client) {
        tvContactCustomerName.setText(client.getName() + "???????????????");
        menuPageIndex = 1;
        if (menuDemand == null) {
            initMenuDemand();
        }
        if (menuAdapter != null) {
            menuAdapter.clearData();
        }
        mDrawerLayout.openDrawer(menu_right); //??????????????????
        mCustomerId = client.getUuid();
        setMenuListData();
    }

    /**
     * ??????????????????
     */
    public void closeDrawerLayout() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(menu_right)) {
                mDrawerLayout.closeDrawer(menu_right, true);

            }
        }
    }

    /**
     * ????????????listview?????????
     *
     * @param gridItems
     * @return
     */
    private CommanAdapter<Contact> getMenuAdapter(
            List<Contact> gridItems) {
        return new CommanAdapter<Contact>(gridItems, this,
                R.layout.item_contract_list) {
            @SuppressLint("NewApi")
            @Override
            public void convert(int position, final Contact item,
                                BoeryunViewHolder viewHolder) {
                MultipleAttachView view = viewHolder.getView(R.id.attach_item_contact);
                if (TextUtils.isEmpty(item.getAttachment())) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                view.loadImageByAttachIds(item.getAttachment());
                viewHolder.setTextValue(R.id.tv_name_contact_item, helper.getUserNameById(item.getAdvisorId()));
                viewHolder.setTextValue(R.id.tv_advisor_contact_item, item.getCustomerName());
                viewHolder.setUserPhotoById(R.id.head_item_contact_list, helper.getUser(item.getAdvisorId()));
                if (item.getContactTime().contains(" 00:00:00")) {
                    viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getContactTime(), "yyyy-MM-dd"));
                } else {
                    viewHolder.setTextValue(R.id.tv_time_contact_item, ViewHelper.getDateStringFormat(item.getContactTime(), "yyyy-MM-dd HH:mm"));
                }
                viewHolder.setTextValue(R.id.content_contact_list, item.getContent());
                TextView tv_status = viewHolder.getView(R.id.tv_status_item_contact);
                if (!TextUtils.isEmpty(item.getStageName())) {
                    tv_status.setVisibility(View.VISIBLE);
                    tv_status.setText(item.getStageName());
                } else {
                    tv_status.setVisibility(View.GONE);
                }

//                //??????
//                viewHolder.getView(R.id.ll_item_log_comment).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        popWiw(item);
//                    }
//                });

                LinearLayout ll_support = viewHolder.getView(R.id.ll_item_log_support);//??????
                LinearLayout ll_comment = viewHolder.getView(R.id.ll_item_log_comment);//??????
                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                final TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);
                ll_support.setVisibility(View.GONE);
                ll_comment.setVisibility(View.GONE);
                iv_support.setVisibility(View.GONE);
                tv_support.setVisibility(View.GONE);
                tv_comment.setVisibility(View.GONE);
//                //??????/?????????
//                ll_support.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getCreatorId());
//                        post.setDataType("????????????");
//                        post.setDataId(item.getUuid());
//                        if (item.isLike()) { //????????????
//                            cancleSupport(post, item);
//                        } else { //??????
//                            support(post, item);
//                        }
//                    }
//                });
//
//                if (item.isLike()) {
//                    iv_support.setImageResource(R.drawable.icon_support_select);
////                    tv_support.setTextColor(getColor(R.color.color_support_text_like));
//                    tv_support.setTextColor(Color.parseColor("#01E0DF"));
//                } else {
//                    iv_support.setImageResource(R.drawable.icon_support);
//                    tv_support.setTextColor(Color.parseColor("#999999"));
////                    tv_support.setTextColor(getColor(R.color.color_support_text));
//                }
//                tv_support.setText(item.getLikeNumber() + "");
//                tv_comment.setText(item.getCommentNumber() + "");

            }
        };
    }

    /**
     * ??????????????????list??????
     */
    private void setMenuListData() {
        menuDemand.pageIndex = menuPageIndex;
        menuDemand.src = Global.BASE_JAVA_URL + GlobalMethord.?????????????????? + "?customerId=" + mCustomerId;
        menuDemand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                recordList = menuDemand.data;
                if (recordList.size() == 0 && menuPageIndex == 1) {
                    tv_contactList.setVisibility(View.VISIBLE);
                    menu_lv.setVisibility(View.GONE);
                } else {
                    tv_contactList.setVisibility(View.GONE);
                    menu_lv.setVisibility(View.VISIBLE);
                    try {
                        for (Contact project : recordList) {
                            project.setStageName(menuDemand.getDictName(project, "stage"));
                            project.setCustomerName(menuDemand.getDictName(project, "customerId"));
                            project.setProjectName(menuDemand.getDictName(project, "projectId"));
                            project.setContactWayName(menuDemand.getDictName(project, "contactWay"));
                        }
                    } catch (Exception e) {

                    }
                    menu_lv.onRefreshComplete();
                    if (menuPageIndex == 1) {
                        menuAdapter = getMenuAdapter(recordList);
                        menu_lv.setAdapter(menuAdapter);
                    } else {
                        menuAdapter.addBottom(recordList, false);
                        if (recordList != null && recordList.size() == 0) {
                            Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        menu_lv.loadCompleted();
                    }
                    menuPageIndex += 1;

                    String dictionary = menuDemand.dictionary;
                    List<ReturnDict> dictList = JsonUtils.getDictByName(dictionary, "customerId.crm_customer");

                    if (recordList != null) {
                        for (Contact contact : recordList) {
                            contact.setCustomerName(JsonUtils.getDictValueById(dictList, contact.getCustomerId()));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                ProgressDialogHelper.dismiss();
                menu_lv.onRefreshComplete();
                menu_lv.loadCompleted();
            }
        });

    }

    private void showShare(String path, String fileName) {
        String text = getString(R.string.app_name);
        OnekeyShare oks = new OnekeyShare();
        //??????sso??????
        oks.disableSSOWhenAuthorize();
        File file = new File(path, fileName);


// ?????????Notification??????????????????  2.5.9?????????????????????????????????
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title???????????????????????????????????????????????????????????????QQ????????????
//        oks.setTitle("");
        // titleUrl?????????????????????????????????????????????QQ????????????
//        oks.setTitleUrl("");
        // text???????????????????????????????????????????????????
//        oks.setText("");
        // imagePath???????????????????????????Linked-In?????????????????????????????????
        if (file.exists()) {
            oks.setImagePath(file.getAbsolutePath());//??????SDcard????????????????????????
        }
        // url???????????????????????????????????????????????????
//        oks.setUrl("");
        // comment???????????????????????????????????????????????????QQ????????????
//        oks.setComment("????????????????????????");
        // site??????????????????????????????????????????QQ????????????
//        oks.setSite(getString(R.string.app_name));
        // siteUrl??????????????????????????????????????????QQ????????????
//        oks.setSiteUrl(text);

// ????????????GUI
        oks.show(this);
    }


    private void createLogoDisk() {
        try {
            File file = new File(PhotoHelper.PATH, "logo.jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_fang);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * ????????????
     */
    private void shareArticle() {
        createLogoDisk();
        String url = mUrl.substring(0, mUrl.length() - 1) + "0";
        OnekeyShare oks = new OnekeyShare();
        //??????sso??????
        oks.disableSSOWhenAuthorize();
        File file = new File(PhotoHelper.PATH, "logo.jpg");

        // title???????????????????????????????????????????????????????????????QQ????????????
        if (information != null) {
            oks.setTitle(information.getTitle());
        } else {
            oks.setTitle(getResources().getString(R.string.app_name));
        }
        // text???????????????????????????????????????????????????
        oks.setText("");
        if (file.exists()) {
            oks.setImagePath(file.getAbsolutePath());//??????SDcard????????????????????????
        }
        // url???????????????????????????????????????????????????
        oks.setUrl(url);
        // titleUrl QQ???QQ??????????????????
        oks.setTitleUrl(url);

        // ????????????GUI
        oks.show(this);
    }

}
