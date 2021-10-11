package com.biaozhunyuan.tianyi.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.utils.Base64Util;
import com.biaozhunyuan.tianyi.common.utils.HttpUtil;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.contact.Contact;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.PreferceManager;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.expenseaccount.camera.CameraActivity;
import com.biaozhunyuan.tianyi.expenseaccount.camera.FileUtil;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.CustomerDrawerLayout;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Request;

/**
 * 客户列表
 */
public class ClientListActivity extends BaseActivity {

    private static final int REQUEST_CODE_SCAN = 111;
    //    private BoeryunHeaderView headerview;
    private SimpleIndicator simpleIndicator;
    private AutoMaxHeightViewpager viewpager;
    private TextView tv_addNumber;
    private TextView tv_yeji;
    private TextView tv_mubiao;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private RefreshLayout refreshLayout;
    private ClientlistCustomerFragment clientlistCustomerFragment;  //客户列表
    private ClientlistbusinessFragment clientlistbusinessFragment;  //商机列表
    private ClientlistpublicFragment clientlistpublicFragment;  //公海客户

    private CustomerDrawerLayout mDrawerLayout;
    private PullToRefreshAndLoadMoreListView menu_lv; //菜单栏布局listview
    private LinearLayout menu_right; //菜单栏布局对象
    private Demand<Contact> menuDemand;
    private List<Contact> recordList;
    private String dictionary = "";
    private CommanAdapter<Contact> menuAdapter;
    private int menuPageIndex = 1; //当前页
    private String mCustomerId = "";
    private DictionaryHelper helper;
    private TextView tvContactCustomerName;
    private TextView tv_contactList;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private boolean isHaveSeasPermission = false; //是否有公海的权限

    private LinearLayout llBack;
    private ImageView ivAdd;
    private TextView tvMore;

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_new_client_list);
        initView();
        initPopwindow();
        getPermission();


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });
    }

    private void getPermission() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.功能模块权限;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                //如果权限中包含公海权限id
                if (response.contains("86f70dbd246d43eba730d5612d10b3d5")) {
                    isHaveSeasPermission = true;
                }
                initFragment();

                setTouchEvent();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(String message) {
        if ("提取客户成功".equals(message)) {
            clientlistCustomerFragment.refreshData(refreshLayout);
            EventBus.getDefault().removeStickyEvent(message);
        } else if ("放入公海成功".equals(message)) {
            clientlistpublicFragment.refreshData(refreshLayout);
            EventBus.getDefault().removeStickyEvent(message);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            getScanInfo();
        }
    }

    private void getScanInfo() {
        ProgressDialogHelper.show(this, "识别中...");
        String accessToken = PreferceManager.getInsance().getValueBYkey("BaiduDiscriminateAccessToken");

        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/business_card";

        byte[] imgData;

        String params;
        try {
            imgData = com.biaozhunyuan.tianyi.common.utils.FileUtil.readFileByBytes(FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            String imgStr = Base64Util.encode(imgData);
            params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8") + "&accuracy" + "=" + "high";

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        String scanResult = HttpUtil.post(otherHost, accessToken, params);
                        LogUtils.i("scanResult:", scanResult);
                        ProgressDialogHelper.dismiss();
                        String result = JsonUtils.getStringValue(scanResult, "words_result");
                        String name = StrUtils.removeRex(JsonUtils.getStringValue(result, "NAME")).replaceAll("\"", "");
                        String postion = StrUtils.removeRex(JsonUtils.getStringValue(result, "TITLE")).replaceAll("\"", "");
                        String mobile = StrUtils.removeRex(JsonUtils.getStringValue(result, "MOBILE")).replaceAll("\"", "");
                        String company = StrUtils.removeRex(JsonUtils.getStringValue(result, "COMPANY")).replaceAll("\"", "");
                        String url = StrUtils.removeRex(JsonUtils.getStringValue(result, "URL")).replaceAll("\"", "");
                        String addr = StrUtils.removeRex(JsonUtils.getStringValue(result, "ADDR")).replaceAll("\"", "");
                        String email = StrUtils.removeRex(JsonUtils.getStringValue(result, "EMAIL")).replaceAll("\"", "");
                        String tel = StrUtils.removeRex(JsonUtils.getStringValue(result, "TEL")).replaceAll("\"", "");
                        String fax = StrUtils.removeRex(JsonUtils.getStringValue(result, "FAX")).replaceAll("\"", "");

                        ScanCustomerModel model = new ScanCustomerModel();
                        model.setName(name);
                        model.setPostion(postion);
                        model.setMobile(mobile);
                        model.setCompany(company);
                        model.setUrl(url);
                        model.setAddr(addr);
                        model.setEmail(email);
                        model.setTel(tel);
                        model.setFax(fax);

                        Intent intent = new Intent(ClientListActivity.this, ChClientInfoActivity.class);
                        intent.putExtra(ChClientInfoActivity.EXTRA_CLIENT_ID, "0");
                        intent.putExtra("ScanCustomer", model);
                        intent.putExtra("isReadOnly", true);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取业务员 业绩和客户
     */
    private void getData() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    Client client = JsonUtils.jsonToEntity(JsonUtils.getStringValue(response, "Data"), Client.class);
                    if (!TextUtils.isEmpty(client.getSumAmountSales()) && !"0".equals(client.getSumAmountSales())) {
                        tv_yeji.setText(ViewHelper.stringToDouble(client.getSumAmountSales(), "0") + "万");
                    } else {
                        tv_yeji.setText("无");
                    }
                    if (!TextUtils.isEmpty(client.getTarget()) && !client.getTarget().equals("0")) {
                        tv_mubiao.setText(client.getTarget().equals("未设置目标") ? client.getTarget() : client.getTarget() + "万");
                    } else {
                        tv_mubiao.setText("无");
                    }
                    tv_addNumber.setText("新增客户数 : " + client.getNewCustomer());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                tv_yeji.setText("无");
                tv_mubiao.setText("无");
                tv_addNumber.setText("新增客户数 : 0");
            }
        });
    }

    /**
     * 打开侧拉菜单
     *
     * @param client
     */
    public void openDrawerLayout(Client client) {
        tvContactCustomerName.setText(client.getName() + "的联系记录");
        menuPageIndex = 1;
        if (menuDemand == null) {
            initMenuDemand();
        }
        if (menuAdapter != null) {
            menuAdapter.clearData();
        }
        mDrawerLayout.openDrawer(menu_right); //打开侧拉菜单
        mCustomerId = client.getUuid();
        setMenuListData();
    }

    /**
     * 关闭侧拉菜单
     */
    public void closeDrawerLayout() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(menu_right)) {
                mDrawerLayout.closeDrawer(menu_right, true);

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
     * 获取侧拉菜单list数据
     */
    private void setMenuListData() {
        menuDemand.pageIndex = menuPageIndex;
        menuDemand.src = Global.BASE_JAVA_URL + GlobalMethord.项目联系记录 + "?customerId=" + mCustomerId;
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
                            menu_lv.loadAllData();
                        }
                        menu_lv.loadCompleted();
                    }
                    menuPageIndex += 1;

                    dictionary = menuDemand.dictionary;

                    if (recordList != null) {
                        for (Contact contact : recordList) {
                            try {
                                contact.setCustomerName(menuDemand.getDictName(contact, "customerId"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    /**
     * 侧拉菜单listview适配器
     *
     * @param gridItems
     * @return
     */
    private CommanAdapter<Contact> getMenuAdapter(
            List<Contact> gridItems) {
        return new CommanAdapter<Contact>(gridItems, this,
                R.layout.item_contract_client_list) {
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
//                viewHolder.setTextValue(R.id.tv_advisor_contact_item, item.getCustomerName());
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

                //评论
                viewHolder.getView(R.id.ll_item_log_comment).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popWiw(item);
                    }
                });

                LinearLayout ll_support = viewHolder.getView(R.id.ll_item_log_support);//点赞
                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                final TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);
                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("联系记录");
                        post.setDataId(item.getUuid());
                        if (item.isLike()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });

                if (item.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
//                    tv_support.setTextColor(getColor(R.color.color_support_text_like));
                    tv_support.setTextColor(Color.parseColor("#01E0DF"));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(Color.parseColor("#999999"));
//                    tv_support.setTextColor(getColor(R.color.color_support_text));
                }
                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");

            }
        };
    }

    /**
     * 新建客户 (临时抽取的方法)
     */
    private void addCustomer() {
        int currentItem = viewpager.getCurrentItem();
        if (currentItem == 0 || currentItem == 1) {
            PreferceManager.getInsance().saveValueBYkey("customer" + Global.mUser.getUuid(), "customer_list");
        } else if (currentItem == 2) {
            PreferceManager.getInsance().saveValueBYkey("customer" + Global.mUser.getUuid(), "customer_public");
        }

        Intent intent = new Intent(ClientListActivity.this, ChClientInfoActivity.class);
        intent.putExtra(ChClientInfoActivity.EXTRA_CLIENT_ID, "0");
        intent.putExtra("isReadOnly", true);
        startActivity(intent);
        closeDrawerLayout();
    }

    private void setTouchEvent() {

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCanAddCustomer();
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackgroundAlpha(0.5f);
                popupWindow.showAsDropDown(tvMore);
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (viewpager.getCurrentItem() == 0) {
                    clientlistCustomerFragment.loadMoreData(refreshLayout);
                } else if (viewpager.getCurrentItem() == 1) {
                    clientlistbusinessFragment.loadMoreData(refreshLayout);
                } else if (viewpager.getCurrentItem() == 2) {
                    clientlistpublicFragment.loadMoreData(refreshLayout);
                }
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
                refreshLayout.setNoMoreData(false);
                if (viewpager.getCurrentItem() == 0) {
                    clientlistCustomerFragment.refreshData(refreshLayout);
                } else if (viewpager.getCurrentItem() == 1) {
                    clientlistbusinessFragment.refreshData(refreshLayout);
                } else if (viewpager.getCurrentItem() == 2) {
                    clientlistpublicFragment.refreshData(refreshLayout);
                }
            }
        });

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewpager.resetHeight(position);
            }

            @Override
            public void onPageSelected(int position) {
                refreshLayout.setNoMoreData(false);//恢复没有更多数据的原始状态
                closeDrawerLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        menu_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Contact record = (Contact) menuAdapter.getDataList().get(position - 1);
                    Intent intent = new Intent(ClientListActivity.this, AddRecordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contactInfo", record);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        /**
         * 查看更多
         */
        menu_lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                setMenuListData();
            }
        });

        /**
         * 下拉刷新
         */
        menu_lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                menuPageIndex = 1;
                setMenuListData();
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); //开启手势滑动
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private void initPopwindow() {
        View view = View.inflate(this, R.layout.popup_more_client_list, null);
        LinearLayout llImport = view.findViewById(R.id.ll_import);
        LinearLayout llScan = view.findViewById(R.id.ll_scan);
        popupWindow = new PopupWindow(view, (int) ViewHelper.dip2px(getBaseContext(), 150), ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        llImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImportCustomerActivity();
                popupWindow.dismiss();
            }
        });

        llScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipScanActivtity();
                popupWindow.dismiss();
            }
        });
    }

    private void initView() {
        tv_addNumber = findViewById(R.id.tv_add_customer_number);
        tv_yeji = findViewById(R.id.tv_yeji);
        tv_mubiao = findViewById(R.id.tv_mubiao);
        simpleIndicator = findViewById(R.id.simpleindicatior);
        viewpager = findViewById(R.id.boeryun_viewpager);
        refreshLayout = findViewById(R.id.refreshLayout);
        tvContactCustomerName = findViewById(R.id.tv_menu_contact);
        mDrawerLayout = findViewById(R.id.mDrawerLayout);
        menu_lv = findViewById(R.id.menu_lv);
        tv_contactList = findViewById(R.id.tv_contactlist);
        menu_right = findViewById(R.id.menu_right_rl);
        helper = new DictionaryHelper(this);

        llBack = findViewById(R.id.ll_back);
        ivAdd = findViewById(R.id.iv_add_headerview);
        tvMore = findViewById(R.id.tv_right_title_headerview);

        refreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）
        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        refreshLayout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多
        refreshLayout.setEnableScrollContentWhenLoaded(false); //是否在加载完成时滚动列表显示新的内容
        refreshLayout.setEnableScrollContentWhenRefreshed(false);//是否在刷新完成时滚动列表显示新的内容
        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);//是否在全部加载结束之后Footer跟随内容1.0.4
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);// 禁止手势滑动
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        int screenWidth = getScreenWidth();
        ViewGroup.LayoutParams params = menu_right.getLayoutParams();
        params.width = screenWidth / 5 * 3;

        menu_right.setLayoutParams(params);

    }

    private void initFragment() {
        clientlistCustomerFragment = new ClientlistCustomerFragment(viewpager, 0);
        clientlistbusinessFragment = new ClientlistbusinessFragment(viewpager, 1);
//        clientlistNoticeFragment = new ClientMyNoticeFragment(viewpager, 3);
        mFragments.add(clientlistCustomerFragment);
        mFragments.add(clientlistbusinessFragment);
        if (isHaveSeasPermission) {
            clientlistpublicFragment = new ClientlistpublicFragment(viewpager, 2);
            mFragments.add(clientlistpublicFragment);
        }

        titles.add("客户");
        titles.add("商机");
        if (isHaveSeasPermission) {
            titles.add("公海");
        }

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
        simpleIndicator.setTabItemTitles(titles);
        simpleIndicator.setViewPager(viewpager, 0);
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * Drawerlayout back键关闭
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(menu_right))
            mDrawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    private void popWiw(final Contact item) {

        popWiw = new BaseSelectPopupWindow(ClientListActivity.this, R.layout.edit_data);
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
        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });

        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.activity_new_client_list, null), Gravity.BOTTOM
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
                Toast.makeText(ClientListActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                space.setCommentNumber(space.getCommentNumber() + 1);
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Log.e("tag", "评论失败");
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
     * 点赞
     *
     * @param post
     * @param record
     */
    private void support(SupportAndCommentPost post, final Contact record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ClientListActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLike(true);
                menuAdapter.notifyDataSetChanged();
//                ll_bottom.setVisibility(View.GONE);
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
     * 取消点赞
     *
     * @param post   要取消点赞的实体的ID
     * @param record
     */
    private void cancleSupport(SupportAndCommentPost post, final Contact record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ClientListActivity.this, "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLike(false);
                menuAdapter.notifyDataSetChanged();
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
     * 是否可以新建客户
     */
    private void isCanAddCustomer() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.判断是否可以新建客户;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    String data = JsonUtils.pareseData(response);
                    String isCreatable = JsonUtils.getStringValue(data, "isCreatable");
                    if ("true".equals(isCreatable)) {
                        addCustomer();
                    } else {
                        showShortToast("拥有客户数据已达到最大限制");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    addCustomer();
                }


            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                addCustomer();
            }
        });
    }


    /**
     * 导入客户
     */
    private void startImportCustomerActivity() {
        PermissionsUtil.requestPermission(getBaseContext(), new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                Intent intent = new Intent(ClientListActivity.this, ImportCustomerActivity.class);
                startActivity(intent);
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                showShortToast("请开启通讯录权限");
            }
        }, Manifest.permission.READ_CONTACTS);
    }

    /**
     * 调用相机识别发票
     */
    private void skipScanActivtity() {
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_BANK_CARD);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
