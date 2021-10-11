package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.ApplyName;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.view.SimpleIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;


/**
 * Created by 王安民 on 2017/8/28.
 * 申请列表页面
 */
public class ApplylistActivity extends BaseActivity {

    private Context context;

    private String[] tabTitles = new String[]{"待我审批", "我的申请", "抄送给我", "我已审批"}; //tab的标题

    private ApplyViewPagerAdapter pagerAdapter;
    public static boolean isResume = false;


    private BoeryunHeaderView headerView;
    private SimpleIndicator indicator;
    private ViewPager viewPager;

    private LinearLayout ll_search;
    private LinearLayout ll_filter;
    private TextView tv_filter;
    private TextView tv_search;

    //    private PopupWindow popupWindow;
    private List<String> statusFilter;
    private TextView tv_select;
    private BoeryunSearchView searchView;


    private RelativeLayout rl_search_layout;
    private ListView lv_search_list;
    private ImageView iv_search_no_result;

    private boolean isSelectForm = false;
    private HashMap<Integer, String> filterMap = new HashMap<Integer, String>();
    private HashMap<Integer, String> searchMap = new HashMap<Integer, String>();
    private HashMap<Integer, Boolean> isSelectFormMap = new HashMap<Integer, Boolean>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_apply_list);
        context = getBaseContext();
        initViews();
        initData();
//        initPopupWindow();
        setOnEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume) {
            pagerAdapter.reloadData(viewPager.getCurrentItem());
            isResume = false;
        }
    }

    private void initViews() {
        headerView = (BoeryunHeaderView) findViewById(R.id.header_apply_list);
        indicator = (SimpleIndicator) findViewById(R.id.incator_title_apply_list);
        viewPager = (ViewPager) findViewById(R.id.pager_apply_list);
//        ll_search = (LinearLayout) findViewById(R.id.ll_search_apply_list);
//        ll_filter = (LinearLayout) findViewById(ll_filter_apply_list);
        tv_filter = (TextView) findViewById(R.id.tv_filter_apply_list);
        searchView = (BoeryunSearchView) findViewById(R.id.search_view_apply_list);
        tv_search = (TextView) findViewById(R.id.tv_search_apply_list);

        rl_search_layout = (RelativeLayout) findViewById(R.id.rl_search_layout_apply);
        lv_search_list = (ListView) findViewById(R.id.lv_searched_apply_list);
        iv_search_no_result = (ImageView) findViewById(R.id.iv_no_result_search_layout_apply);

        indicator.setTabItemTitles(tabTitles);
        indicator.setViewPager(viewPager, 0);
        indicator.setVisibleTabCount(4);
        tv_filter.setText("审批中");
        tv_filter.setTextColor(getResources().getColor(R.color.text_info));
    }

    private void initData() {
        FragmentManager fm = getSupportFragmentManager();
        pagerAdapter = new ApplyViewPagerAdapter(fm);
        viewPager.setAdapter(pagerAdapter);

        statusFilter = new ArrayList<>();
        statusFilter.add("全部");
        statusFilter.add("未处理");
        statusFilter.add("审批中");
        statusFilter.add("审批通过");
        statusFilter.add("审批拒绝");
    }

    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {
//                showPopupWindow();
            }

            @Override
            public void onClickSaveOrAdd() {
                startActivity(new Intent(ApplylistActivity.this, AllFormFragment.class));
            }
        });

//        ll_filter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (viewPager.getCurrentItem() != 0) {
//                    popupWindow.showAsDropDown(ll_filter, popupWindow.getWidth(), popupWindow.getHeight());
//                }
//            }
//        });
//
//        ll_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.getFocus();
//                searchView.setVisibility(View.VISIBLE);
//                rl_search_layout.setVisibility(View.VISIBLE);
//                ll_filter.setEnabled(false);
//            }
//        });
//
//
//        searchView.setOnButtonClickListener(new BoeryunSearchViewNoButton.OnButtonClickListener() {
//            @Override
//            public void OnCancle() {
//                searchView.setVisibility(View.GONE);
//                rl_search_layout.setVisibility(View.GONE);
//                ll_filter.setEnabled(true);
//                pagerAdapter.setValue("?searchField_dictionary_workflowTemplate=", viewPager.getCurrentItem());
//                tv_search.setText("搜索");
//                tv_search.setTextColor(getResources().getColor(R.color.text_info));
//                searchMap.put(viewPager.getCurrentItem(), "");
//            }
//
//            @Override
//            public void OnClick() {
//
//            }
//        });
//
//
//        //输入框搜索内容
//        searchView.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
//            @Override
//            public void OnSearched(String str) {
//                if (TextUtils.isEmpty(str)) {
//                    rl_search_layout.setVisibility(View.VISIBLE);
//                    iv_search_no_result.setVisibility(View.VISIBLE);
//                } else {
//                    getApplyNameByFilter(str);
//                }
//            }
//        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (!TextUtils.isEmpty(searchMap.get(position))) {
                    tv_search.setText(searchMap.get(position));
                    tv_search.setTextColor(getResources().getColor(R.color.fuzhuselan));
                } else {
                    tv_search.setText("搜索");
                    tv_search.setTextColor(getResources().getColor(R.color.text_info));
                }
                if (!TextUtils.isEmpty(filterMap.get(position))) {
                    tv_filter.setText(filterMap.get(position));
                    tv_filter.setTextColor(getResources().getColor(R.color.fuzhuselan));
                } else {
                    tv_filter.setText("筛选");
                    tv_filter.setTextColor(getResources().getColor(R.color.text_info));
                }
                if (position == 0) {
                    tv_filter.setText("审批中");
                    tv_filter.setTextColor(getResources().getColor(R.color.text_info));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


//    private void initPopupWindow() {
//        View view = View.inflate(context, R.layout.popwindow_task_filter, null);
//
//        RelativeLayout rl_back = (RelativeLayout) view.findViewById(R.id.rl_gray_task_list);
//
//        GridView gridView = (GridView) view.findViewById(R.id.gv_status_task);
//        gridView.setAdapter(getStatusAdapter(statusFilter));
//
//        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setTouchable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//
//        rl_back.setOnClickListener(new View.OnClickListener() { //点击灰色空白处弹出框消失
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//            }
//        });
//
//    }


    /**
     * 根据输入展示申请列表
     *
     * @param str
     */
    private void getApplyNameByFilter(String str) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.申请名称列表 + str;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                final List<ApplyName> list = JsonUtils.ConvertJsonToList(response, ApplyName.class);

                if (list != null && list.size() > 0) {
                    iv_search_no_result.setVisibility(View.GONE);
                    lv_search_list.setAdapter(getNameAdapter(list));


                    lv_search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position > 0) {
                                ApplyName name = list.get(position);
                                pagerAdapter.setValue("?searchField_dictionary_workflowTemplate=" + name.getUuid(), viewPager.getCurrentItem());
                                searchView.setVisibility(View.GONE);
                                rl_search_layout.setVisibility(View.GONE);
                                isSearch(true, name.getName());
                                searchMap.put(viewPager.getCurrentItem(), name.getName());
                                InputSoftHelper.hiddenSoftInput(context, searchView.geteText());
                            }
                        }
                    });
                } else {
                    iv_search_no_result.setVisibility(View.VISIBLE);
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

    private CommanAdapter<ApplyName> getNameAdapter(List<ApplyName> list) {
        return new CommanAdapter<ApplyName>(list, context, R.layout.item_apply_name) {
            @Override
            public void convert(int position, ApplyName item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_apply_name_list, item.getName());
            }
        };
    }


    /**
     * 申请状态弹出框
     *
     * @param list
     * @return
     */
    private CommanAdapter<String> getStatusAdapter(List<String> list) {
        return new CommanAdapter<String>(list, context, R.layout.item_status_filter) {
            @Override
            public void convert(int position, final String item, BoeryunViewHolder viewHolder) {
                final TextView textView = viewHolder.getView(R.id.tv_status_popup);

                textView.setText(item);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setBackgroundColor(getResources().getColor(R.color.fuzhuselan));
                        textView.setTextColor(Color.WHITE);

                        if (tv_select != null && !tv_select.equals(textView)) {
                            tv_select.setBackgroundColor(getResources().getColor(R.color.bg_half_gray));
                            tv_select.setTextColor(getResources().getColor(R.color.text_black));
                        }


//                        tv_filter.setText(item);
//                        tv_filter.setTextColor(getResources().getColor(R.color.fuzhuselan));
                        tv_select = textView;

                        filterMap.put(viewPager.getCurrentItem(), item);
                        isSearch(false, item);

                        String status = "";
                        if ("全部".equals(item)) {
                            status = "";
                        } else if ("未处理".equals(item)) {
                            status = "1";
                        } else if ("审批中".equals(item)) {
                            status = "2";
                        } else if ("审批拒绝".equals(item)) {
                            status = "3";
                        } else if ("审批通过".equals(item)) {
                            status = "5";
                        }
                        if (TextUtils.isEmpty(status)) {
                            pagerAdapter.setValue("", viewPager.getCurrentItem());
                        } else {
                            pagerAdapter.setValue("?status=" + status, viewPager.getCurrentItem());
                        }
//                        popupWindow.dismiss();
                    }
                });
            }
        };
    }


    /**
     * 判断是否是搜索表单
     *
     * @param isSearch
     * @param str
     */
    private void isSearch(boolean isSearch, String str) {
        if (isSearch) {
            tv_search.setTextColor(getResources().getColor(R.color.fuzhuselan));
            tv_search.setText(str);
            tv_filter.setText("筛选");
            tv_filter.setTextColor(getResources().getColor(R.color.text_info));
            filterMap.put(viewPager.getCurrentItem(), "");
        } else {
            tv_filter.setTextColor(getResources().getColor(R.color.fuzhuselan));
            tv_filter.setText(str);
            tv_search.setText("搜索");
            tv_search.setTextColor(getResources().getColor(R.color.text_info));
            searchMap.put(viewPager.getCurrentItem(), "");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.setOnCancleSearch(false);
    }
}
