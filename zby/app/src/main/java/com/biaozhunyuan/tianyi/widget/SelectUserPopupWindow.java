//package com.biaozhunyuan.tianyi.widget;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.biaozhunyuan.tianyi.R;
//import com.biaozhunyuan.tianyi.com.biaozhunyuan.tianyi.common.global.Global;
//import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
//import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
//import com.biaozhunyuan.tianyi.common.http.StringRequest;
//import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
//import com.biaozhunyuan.tianyi.newquest.CheckBoxListViewItem;
//import com.biaozhunyuan.tianyi.newquest.SelectUserByNameAdapter;
//import com.biaozhunyuan.tianyi.newquest.SelectUserClientFilterAdapter;
//import com.biaozhunyuan.tianyi.newquest.SelectUser_LatestAdapter;
//import com.biaozhunyuan.tianyi.newquest.SpeechView;
//import com.biaozhunyuan.tianyi.apply.UserBiz;
//import com.biaozhunyuan.tianyi.newquest.Util;
//import com.biaozhunyuan.tianyi.notice.Department;
//import com.biaozhunyuan.tianyi.common.model.user.Latest;
//import com.biaozhunyuan.tianyi.common.utils.LogUtils;
//import com.biaozhunyuan.tianyi.common.model.user.User;
//import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
//import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
//import com.biaozhunyuan.tianyi.view.IndexBar;
//import com.j256.ormlite.dao.Dao;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//
//import okhttp3.Request;
//
///**
// * Created by new on 2016/12/2.
// * <p/>
// * ????????????????????????????????????????????????
// */
//public class SelectUserPopupWindow extends PopupWindow {
//
//    private Context context;
//    private View contentView;//????????????????????????
//    private boolean isSingle = false;//???????????????????????????,???????????????
//
//    private PopupWindow mPopupWindow;
//
//    private LinearLayout ll_back;
//
//    private Dao<Latest, Integer> latestDao;// latest???dao
//    private ORMDataHelper ormDataHelper;// ormdataHelper????????? ??????????????????getUserDao??????
//    private List<CheckBoxListViewItem> mDeptListViewItems = new ArrayList<CheckBoxListViewItem>();//??????
//    private List<CheckBoxListViewItem> mLatestListViewItems = new ArrayList<CheckBoxListViewItem>();//??????
//    private List<CheckBoxListViewItem> name_checkedUser = new ArrayList<CheckBoxListViewItem>();//?????????????????????????????????
//    private List<CheckBoxListViewItem> latest_checkedUser = new ArrayList<CheckBoxListViewItem>();//????????????????????????
//    private List<CheckBoxListViewItem> searchList = new ArrayList<>();//???????????????
//
//    private List<User> mDeptList;
//    private List<String> nameList;
//    private LinearLayout ll_bottom;
//    private LinearLayout ll_bottom_dept;
//    private LinearLayout ll_bottom_latest;
//
//    private SelectUserByNameAdapter nameAdapter;
//    private ListView lv_dept;
////    private ListView lv_dept;
//
//    private SelectUserClientFilterAdapter userAdapter;
//    //    private SelectUserClientFilterAdapter usersAdapter;
//    private List<Department> departmentNodes;
//
//    private ViewPager pager;
//    private Button btn_reset;
//    private Button btn_sure;
//    private ListView lv_name;
//    private TextView tv_select_count;//????????????????????????
//    private Button btn_name_sure;//????????????????????????
//    private TextView tv_select_count_latest;//???????????????????????????
//    private Button btn_name_sure_latest;//???????????????????????????
//    private boolean flag_contain_dept = true;//?????????????????????,????????????????????????
//    private boolean flag_select_all = false;//?????????????????????????????????
//    private RelativeLayout rl_self_dept;
//    private RelativeLayout rl_selectall;//?????????????????????
//    private ImageView iv_flag_all;
//    private ImageView iv_flag_select_all;
//    private BoeryunSearchView searchView;
//    private LinearLayout ll_show_select_list;//?????????????????????
//
//    private TextView tv_latest;
//    private TextView tv_dept;
//    private TextView tv_all;
//    private TextView tv_dept_name;
//
//    private IndexBar indexBar;
//    private ListView lv_latest;
//    private SelectUser_LatestAdapter latestAdapter;
//    private SelectUsersListener listener;
//    private int deptId = 0;
//    private PopupWindow selectPopupWindow; //???????????????????????????
//    private final View parentView;
//    private String searchStr;
//
//    private SpeechView speechView;
//    private Toast toast = null;
//
//    /**
//     * ????????????  setSingleChoose(true)
//     *
//     * @param contentViewId ????????????layoutId
//     * @param context
//     */
//    public SelectUserPopupWindow(int contentViewId, Context context) {
//        super();
//        this.context = context;
//        this.contentView = View.inflate(context, contentViewId, null);
//        parentView = View.inflate(context, R.layout.pager_select_user, null);
//        initView(parentView);
//        initData();
//        setOnClickListener();
//    }
//
//    /**
//     * ????????????
//     *
//     * @param flag
//     */
//    public void setSingleChoose(boolean flag) {
//        this.isSingle = flag;
//    }
//
//
//    private void initData() {
//        nameList = new ArrayList<String>();
//        ormDataHelper = ORMDataHelper.getInstance(context);
//        latestDao = ormDataHelper.getLatestDao();
//        getAllUsersByDept(UserBiz.getGlobalUser().getDepartmentId());
//        getDepartmentList();
//        initLatestUserList();
//    }
//
//    private void initView(View view) {
//
//        int pos[] = new int[2];
//        // ???????????????????????????????????????
//        int popHeight = ViewHelper.getScreenHeight(context) - ViewHelper.getStatusBarHeight(context) - Util.dip2px(context, 45);
//        mPopupWindow = new PopupWindow(parentView, ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT, true);
//
//        mPopupWindow.setAnimationStyle(R.style.AnimationFaded);//????????????
//        mPopupWindow.update();
//
//        // ??????????????? ???????????????
//        mPopupWindow.setTouchable(true);
//        mPopupWindow.setOutsideTouchable(true);
//        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context
//                .getResources(), (Bitmap) null));
//
//        pager = (ViewPager) view.findViewById(R.id.pager_select_user);
//        tv_all = (TextView) view.findViewById(R.id.tv_filter_select_user_all);
//        tv_dept = (TextView) view.findViewById(R.id.tv_filter_select_user_dept);
//        tv_latest = (TextView) view.findViewById(R.id.tv_filter_select_user_lastest);
//        ll_bottom = (LinearLayout) view.findViewById(R.id.ll_select_user_bottom);
//        btn_reset = (Button) view.findViewById(R.id.btn_cancel_select_user);
//        btn_sure = (Button) view.findViewById(R.id.btn_sure_select_user);
//        ll_back = (LinearLayout) view.findViewById(R.id.ll_pop_select_user_back);
//
//
//        btn_reset.setText("??????");
//
//
//        View pager1 = View.inflate(context, R.layout.pager_select_user_latest, null);
//        View pager2 = View.inflate(context, R.layout.pager_user_name, null);
//        View pager3 = View.inflate(context, R.layout.pager_select_user_latest, null);
//        lv_name = (ListView) pager2.findViewById(R.id.lv_select_user_by_name);
//        indexBar = (IndexBar) pager2.findViewById(R.id.index_bar);
//        rl_self_dept = (RelativeLayout) pager2.findViewById(R.id.rl_select_user_by_name_dept);
//        iv_flag_all = (ImageView) pager2.findViewById(R.id.iv_select_user_by_name_dept);
//        lv_latest = (ListView) pager1.findViewById(R.id.lv_select_user_latest);
//        lv_dept = (ListView) pager3.findViewById(R.id.lv_select_user_latest);
//        tv_select_count = (TextView) pager2.findViewById(R.id.tv_select_user_by_name_count);
//        btn_name_sure = (Button) pager2.findViewById(R.id.btn_select_user_by_name_sure);
//        tv_dept_name = (TextView) pager2.findViewById(R.id.tv_select_user_by_name_dept_name);
//        ll_bottom_dept = (LinearLayout) pager3.findViewById(R.id.ll_select_user_dept_bottom);
//        tv_select_count_latest = (TextView) pager1.findViewById(R.id.tv_select_user_by_name_count);
//        btn_name_sure_latest = (Button) pager1.findViewById(R.id.btn_select_user_by_name_sure);
//        ll_bottom_latest = (LinearLayout) pager1.findViewById(R.id.ll_select_user_dept_bottom);
//        rl_selectall = (RelativeLayout) pager2.findViewById(R.id.rl_select_user_by_name_dept_select_all);
//        iv_flag_select_all = (ImageView) pager2.findViewById(R.id.iv_select_user_by_name_dept_select_all);
//        searchView = (BoeryunSearchView) pager2.findViewById(R.id.searchview_selectuser);
//        ll_show_select_list = (LinearLayout) pager2.findViewById(R.id.ll_select_user_by_name_list);
//        speechView = (SpeechView) pager2.findViewById(R.id.speech_view_select_user);
////        lv_dept = (ListView) pager3.findViewById(R.id.edbl_pop_select_user);
//
//        tv_dept.setText("?????????");
//        tv_all.setText("?????????");
//
//        List<View> viewList = new ArrayList<View>();
//        viewList.add(pager1);
//        viewList.add(pager2);
//        viewList.add(pager3);
//
//
//        pager.setAdapter(new MyPagerAdapter(viewList));
//        pager.setCurrentItem(0);//???????????????????????????
//        tv_latest.setBackgroundResource(R.drawable.bg_log_circle);
//
//
//    }
//
//    private void setOnClickListener() {
//
//
//        ll_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mPopupWindow != null) {
//                    mPopupWindow.dismiss();
//                }
//            }
//        });
//
//        /**
//         * ??????????????????????????????
//         */
//        btn_name_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                List<User> checkedUsers = new ArrayList<User>();
//                for (CheckBoxListViewItem item : name_checkedUser) {
//                    User user = convertCheckListToUsers(item);
//                    checkedUsers.add(user);
//                }
//                addToLatest(checkedUsers);
//
//
//                User user = new User();
//
//                List<CheckBoxListViewItem> list = noRepeat(name_checkedUser);
//                String ids = GetSelectedUserId(list);
//                String names = getSelectedUserName(list);
//                user.setUserIds(ids);
//                user.setUserNames(names);
//                mPopupWindow.dismiss();
//                listener.onSelectUsersListener(user);
//            }
//        });
//
//        /**
//         * ???????????????????????????
//         */
//        btn_name_sure_latest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User user = new User();
//
//                List<CheckBoxListViewItem> list = noRepeat(latest_checkedUser);
//                String ids = GetSelectedUserId(list);
//                String names = getSelectedUserName(list);
//                user.setUserIds(ids);
//                user.setUserNames(names);
//                mPopupWindow.dismiss();
//                listener.onSelectUsersListener(user);
//            }
//        });
//
//        speechView.setOnSearchListener(new SpeechView.OnSearchListener() {
//            @Override
//            public void onSearched(String filter) {
//                searchStr = filter;
//                search(filter);
//            }
//        });
//
//
//        /**
//         * ????????????????????????????????????????????????
//         */
//        indexBar.setOnTouchingLetterChangedListener(new IndexBar.OnTouchingLetterChangedListener() {
//            @Override
//            public void onTouchingLetterChanged(String s) {
//                if (toast == null) {
//                    toast = Toast.makeText(context, s.charAt(0) + "", Toast.LENGTH_SHORT);
//                } else {
//                    toast.setText(s.charAt(0) + "");
//                }
//                toast.show();
//                //??????????????????????????????
//                int position = nameAdapter.getPositionForSection(s.toLowerCase().charAt(0));
//
//                if (position != 0) {
//                    lv_name.setSelection(position);
//                }
//            }
//        });
//
//        /**
//         * ??????
//         */
//        searchView.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
//            @Override
//            public void OnSearched(String str) {
//                searchStr = str;
//                search(str);
//            }
//        });
//
//        /**
//         * ??????
//         */
//        rl_selectall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                name_checkedUser.clear();
//                if (flag_select_all) {
//                    iv_flag_select_all.setImageResource(R.drawable.icon_flag_no_choose);
//                    for (CheckBoxListViewItem item : mDeptListViewItems) {
//                        item.setIsChecked(false);
//                        name_checkedUser.remove(item);
//                    }
////                    tv_select_count.setText("0???");
//                    flag_select_all = false;
//                } else {
//                    iv_flag_select_all.setImageResource(R.drawable.icon_flag_choose);
//                    for (CheckBoxListViewItem item : mDeptListViewItems) {
//                        item.setIsChecked(true);
//                        name_checkedUser.add(item);
//                    }
//                    flag_select_all = true;
//                }
//                tv_select_count.setText(name_checkedUser.size() + "???");
//                nameAdapter.notifyDataSetChanged();
//            }
//        });
//
//        /**
//         * ?????????
//         */
//        tv_dept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_dept.setTextColor(Color.parseColor("#a1cf03"));
//                tv_dept.setBackgroundResource(R.drawable.bg_log_circle);
//                tv_latest.setTextColor(Color.BLACK);
//                tv_all.setTextColor(Color.BLACK);
//                tv_latest.setBackgroundResource(0);
//                tv_all.setBackgroundResource(0);
////                ll_bottom.setVisibility(View.GONE);
//                pager.setCurrentItem(1);
//            }
//        });
//
//        /**
//         * ??????
//         */
//        tv_latest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_latest.setTextColor(Color.parseColor("#a1cf03"));
//                tv_latest.setBackgroundResource(R.drawable.bg_log_circle);
//                tv_dept.setTextColor(Color.BLACK);
//                tv_all.setTextColor(Color.BLACK);
//                tv_dept.setBackgroundResource(0);
//                tv_all.setBackgroundResource(0);
////                ll_bottom.setVisibility(View.VISIBLE);
//                pager.setCurrentItem(0);
//                initLatestUserList();
//            }
//        });
//
//        /**
//         * ??????
//         */
//        tv_all.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_all.setTextColor(Color.parseColor("#a1cf03"));
//                tv_all.setBackgroundResource(R.drawable.bg_log_circle);
//                tv_dept.setTextColor(Color.BLACK);
//                tv_latest.setTextColor(Color.BLACK);
//                tv_dept.setBackgroundResource(0);
//                tv_latest.setBackgroundResource(0);
////                ll_bottom.setVisibility(View.VISIBLE);
//                pager.setCurrentItem(2);
//            }
//        });
//
//
//        /**
//         * ?????????????????????????????????????????????????????????
//         */
//        ll_show_select_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSelectUserPopup(name_checkedUser);
//            }
//        });
//
//        /**
//         * ????????????????????????????????????????????????????????????
//         */
//        ll_bottom_latest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSelectUserPopup(latest_checkedUser);
//            }
//        });
//
//
//        /**
//         * viewpager???????????????
//         */
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                pager.setCurrentItem(position);
//                if (position == 0) { //??????
//                    tv_latest.setTextColor(Color.parseColor("#a1cf03"));
//                    tv_latest.setBackgroundResource(R.drawable.bg_log_circle);
//                    tv_dept.setTextColor(Color.BLACK);
//                    tv_all.setTextColor(Color.BLACK);
//                    tv_dept.setBackgroundResource(0);
//                    tv_all.setBackgroundResource(0);
//                    ll_bottom.setVisibility(View.GONE);
//                    ll_bottom_dept.setVisibility(View.VISIBLE);
//
//                    /**
//                     * ??????
//                     */
//                    btn_reset.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            for (CheckBoxListViewItem item : mLatestListViewItems) {
//                                item.setIsChecked(false);
//                            }
//                            latestAdapter.notifyDataSetChanged();
//                        }
//                    });
//
//
//                    /**
//                     * ??????
//                     */
//                    btn_sure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                    initLatestUserList();
//                }
//                if (position == 1) {    //?????????
//                    tv_dept.setTextColor(Color.parseColor("#a1cf03"));
//                    tv_dept.setBackgroundResource(R.drawable.bg_log_circle);
//                    tv_latest.setTextColor(Color.BLACK);
//                    tv_all.setTextColor(Color.BLACK);
//                    tv_latest.setBackgroundResource(0);
//                    tv_all.setBackgroundResource(0);
//                    ll_bottom.setVisibility(View.GONE);
//
//                    /**
//                     * ??????
//                     */
//                    btn_reset.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            for (CheckBoxListViewItem item : mDeptListViewItems) {
//                                item.setIsChecked(false);
//                            }
//                            nameAdapter.notifyDataSetChanged();
//                        }
//                    });
//
//                    /**
//                     * ??????
//                     */
//                    btn_sure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            List<User> checkedUsers = new ArrayList<User>();
//                            for (CheckBoxListViewItem item : name_checkedUser) {
//                                User user = convertCheckListToUsers(item);
//                                checkedUsers.add(user);
//                            }
//                            addToLatest(checkedUsers);
//                        }
//                    });
////                    ll_bottom.setVisibility(View.GONE);
//                }
//                if (position == 2) {    //?????????
//                    tv_all.setTextColor(Color.parseColor("#a1cf03"));
//                    tv_all.setBackgroundResource(R.drawable.bg_log_circle);
//                    tv_latest.setTextColor(Color.BLACK);
//                    tv_dept.setTextColor(Color.BLACK);
//                    tv_latest.setBackgroundResource(0);
//                    tv_dept.setBackgroundResource(0);
//                    ll_bottom.setVisibility(View.GONE);
//                    ll_bottom_dept.setVisibility(View.GONE);
//
//
//                    /**
//                     * ??????
//                     */
//                    btn_reset.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            userAdapter.reset();
//                        }
//                    });
//
//
//                    /**
//                     * ??????
//                     */
//                    btn_sure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//
//        /**
//         * ??????????????????????????????????????????,???????????????????????????
//         */
//        rl_self_dept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                flag_contain_dept = !flag_contain_dept;
//                if (flag_contain_dept) {
//                    iv_flag_all.setImageResource(R.drawable.icon_flag_choose);
//                    if (deptId != 0) {
//                        getAllUsersByDept(deptId);
//                    } else {
//                        getAllUsersByDept(UserBiz.getGlobalUser().getDptId());
//                    }
//                } else {
//                    iv_flag_all.setImageResource(R.drawable.icon_flag_no_choose);
//                    getAllUsersByDept(-1);
//                }
//
//            }
//        });
//
//        /**
//         * ????????????????????????????????????
//         */
//        lv_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CheckBoxListViewItem item;
//                List<User> list = new ArrayList<User>();
//                if (TextUtils.isEmpty(searchStr)) {
//                    item = mDeptListViewItems.get(position);
//                } else {
//                    item = searchList.get(position);
//                }
//                item.setIsChecked(!item.IsChecked);
//                if (!item.IsChecked) {  //??????????????????????????????,??????????????????
//                    flag_select_all = false;
//                    iv_flag_select_all.setImageResource(R.drawable.icon_flag_no_choose);
//                }
//                int i = 0;
//                for (CheckBoxListViewItem item1 : mDeptListViewItems) {
//                    if (item1.IsChecked) {
//                        i++;
//                    }
//                }
//                if (i == mDeptListViewItems.size()) {
//                    flag_select_all = true;
//                    iv_flag_select_all.setImageResource(R.drawable.icon_flag_choose);
//                }
//                if (item.IsChecked) {
//                    name_checkedUser.add(item);
//                    if (isSingle) {
//                        User user = new User();
//                        user.setId(name_checkedUser.get(0).Id);
//                        user.setUserName(name_checkedUser.get(0).Name);
//                        list.add(user);
//                        addToLatest(list);//?????????????????????????????????????????????????????????
//                        listener.onSelectUsersListener(user);
//                        mPopupWindow.dismiss();
//                    }
//                } else {
//                    name_checkedUser.remove(item);
//                }
//                tv_select_count.setText(name_checkedUser.size() + "???");
//                nameAdapter.notifyDataSetChanged();
//            }
//        });
//
//        /**
//         * ?????????????????????????????????
//         */
//        lv_latest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CheckBoxListViewItem item = mLatestListViewItems.get(position);
//                item.setIsChecked(!item.IsChecked);
//                if (item.IsChecked) {
//                    latest_checkedUser.add(item);
//                    if (isSingle) {
//                        User user = new User();
//                        user.setId(latest_checkedUser.get(0).Id);
//                        user.setUserName(latest_checkedUser.get(0).Name);
//                        listener.onSelectUsersListener(user);
//                        mPopupWindow.dismiss();
//                    }
//                } else {
//                    latest_checkedUser.remove(item);
//                }
//                tv_select_count_latest.setText(latest_checkedUser.size() + "???");
//                latestAdapter.notifyDataSetChanged();
//            }
//        });
//    }
//
//    public class MyPagerAdapter extends PagerAdapter {
//
//        private List<View> list;
//
//        public MyPagerAdapter(List<View> list) {
//            this.list = list;
//        }
//
//        @Override
//        public int getMsgTotalCount() {
//            return list.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(list.get(position));
//            return list.get(position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(list.get(position));
//        }
//    }
//
//    private void search(String filter) {
//
//        if (TextUtils.isEmpty(filter)) {
//            nameAdapter = new SelectUserByNameAdapter(context, mDeptListViewItems);
//        } else {
//            searchList.clear();
//            for (CheckBoxListViewItem item : mDeptListViewItems) {
//                if (item.Name.contains(filter)) {
//                    searchList.add(item);
//                }
//            }
//            nameAdapter = new SelectUserByNameAdapter(context, searchList);
//        }
//        lv_name.setAdapter(nameAdapter);
//    }
//
//    /**
//     * ?????????????????????  id = -1
//     */
//    private void getAllUsersByDept(int id) {
//        mDeptList = new ArrayList<User>();
//        String url = Global.BASE_URL + Global.EXTENSION
//                + "account/GetEmployeeListByDeptId/" + id;
//        StringRequest.getAsyn(url, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                LogUtils.i("SelectUserFragment", response);
//                mDeptList = JsonUtils.ConvertJsonToList(response, User.class);
//                Collections.sort(mDeptList, new PinyinComparator());
//                convertDataToLetterList(mDeptList);
//                nameAdapter = new SelectUserByNameAdapter(context, mDeptListViewItems);
//                lv_name.setAdapter(nameAdapter);
//                for (User user : mDeptList) {
//                    nameList.add(user.getName());
//                }
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//                Toast.makeText(context, "????????????", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//                Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    /**
//     * ???????????????????????????
//     * ???????????????????????????
//     *
//     * @return
//     */
//    private void getDepartmentList() {
//        String url = Global.BASE_URL + Global.EXTENSION
//                + "Tree/GetCommonCategratyList";
//        PostParams params = new PostParams();
//        params.setTableName("??????");
//
//        StringRequest.postAsyn(url, params, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                LogUtils.i("selectuserfragment", response);
//                departmentNodes = JsonUtils.ConvertJsonToList(response, DepartmentNode.class);
////                userAdapter = new ClientFilterExbanderbalListviewAdapter(departmentNodes, context, lv_dept);
//                userAdapter = new SelectUserClientFilterAdapter(departmentNodes, context, true);
//                userAdapter.setOnDeptSelectListener(new SelectUserClientFilterAdapter.DeptSelectListener() {
//                    @Override
//                    public void onDeptSelectListener(DepartmentNode node, boolean isChecked) {
//                        {
//                            flag_select_all = false;
//                            iv_flag_select_all.setImageResource(R.drawable.icon_flag_no_choose);
//                            pager.setCurrentItem(1);
//                            userAdapter.reset();
//                            deptId = node.getId();
//                            getAllUsersByDept(node.getId());
//                            tv_dept_name.setText(node.getName());
//                            flag_contain_dept = true;
//                        }
//                    }
//                });
//                lv_dept.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//                Toast.makeText(context, "????????????????????????", Toast.LENGTH_SHORT);
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//                Toast.makeText(context, "??????????????????????????????", Toast.LENGTH_SHORT);
//            }
//        });
//    }
//
//
//    /**
//     * ???CheckBoxListViewItem?????????user
//     *
//     * @param item
//     */
//
//    private User convertCheckListToUsers(CheckBoxListViewItem item) {
//        User user = new User();
//        user.setId(item.getId());
//        user.setName(item.Name);
//        user.setAvatarURI(item.getPic_url());
//        return user;
//    }
//
//    /**
//     * ???????????????????????????
//     *
//     * @param list
//     */
//    private List<CheckBoxListViewItem> noRepeat(List<CheckBoxListViewItem> list) {
//        Set<CheckBoxListViewItem> set = new TreeSet<>(new Comparator<CheckBoxListViewItem>() {
//            @Override
//            public int compare(CheckBoxListViewItem lhs, CheckBoxListViewItem rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
//        set.addAll(list);
//        list.clear();
//        list.addAll(set);
//        return list;
//    }
//
//    /**
//     * ??????????????????????????????????????????
//     *
//     * @param list ?????????????????????
//     */
//    private void addToLatest(List<User> list) {
//        for (int j = 0; j < list.size(); j++) {
//            Latest latest = new Latest();
//            latest.setId(Integer.parseInt(list.get(j).getId()));
//            latest.setPic_url(list.get(j).AvatarURI);
//            latest.setUserName(list.get(j).UserName);
//            latest.setDate(System.currentTimeMillis());
//
//            try {
//                List<Latest> removeLatests = latestDao.queryBuilder().where()
//                        .eq("id", latest.getId()).query();
//                latestDao.delete(removeLatests);
//                latestDao.create(latest);
//                // latestDao.createOrUpdate(latest);
//                // latestDao.createIfNotExists(latest);
//            } catch (SQLException e) {
//                e.printStackTrace();
//                LogUtils.e("SQLException", e + "");
//            }
//        }
//    }
//
//    /**
//     * ????????? ??????????????????
//     */
//    private void initLatestUserList() {
//        try {
//            if (latest_checkedUser != null) {
//                latest_checkedUser.clear();
//                tv_select_count_latest.setText(latest_checkedUser.size() + "???");
//            }
//            latestDao = ormDataHelper.getLatestDao();
//            List<Latest> mLatestList = latestDao.queryBuilder()
//                    .orderBy("date", false).limit((long) 20).query();
//            convertDataLatestToCheckBoxListViewItem(mLatestList);
//            latestAdapter = new SelectUser_LatestAdapter(context, mLatestListViewItems);
//            lv_latest.setAdapter(latestAdapter);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ????????????????????? CheckBoxListViewItem ??????
//     *
//     * @param list
//     */
//    private void convertDataToLetterList(List<User> list) {
//        mDeptListViewItems.clear();
//        for (User s : list) {
//            CheckBoxListViewItem cbItem1 = new CheckBoxListViewItem(
//                    s.AvatarURI, s.Id, s.getUserName(), false, s.getDptId());
//            mDeptListViewItems.add(cbItem1);
//        }
//    }
//
//    /**
//     * ??????????????????????????? CheckBoxListViewItem ??????
//     *
//     * @param list
//     */
//    private void convertDataLatestToCheckBoxListViewItem(
//            List<Latest> list) {
//        mLatestListViewItems = new ArrayList<CheckBoxListViewItem>();
//        for (Latest s : list) {
//            CheckBoxListViewItem cbItem1 = new CheckBoxListViewItem(
//                    s.getPic_url(), s.getId() + "", s.getUserName(), false, 0);
//            mLatestListViewItems.add(cbItem1);
//        }
//    }
//
//    private String GetSelectedUserId(List<CheckBoxListViewItem> list) {
//        String str = "";
//        for (CheckBoxListViewItem cb : list) {
//            str += cb.Id + ",";
//        }
//        if (str.endsWith(",")) {
//            str = str.substring(0, str.length() - 1);
//        }
//        LogUtils.i("---->>>", str);
//        return str;
//    }
//
//    private String getSelectedUserName(List<CheckBoxListViewItem> list) {
//        String str = "";
//        for (CheckBoxListViewItem cb : list) {
//            str += cb.Name + ",";
//        }
//        if (str.length() > 0) {
//            str = str.substring(0, str.length() - 1);
//        }
//        return str;
//    }
//
//
//    public class PinyinComparator implements Comparator<User> {
//
//        public int compare(User o1, User o2) {
//            //????????????????????????ListView?????????????????????ABCDEFG...?????????
//            String name1 = PinYinUtil.toPinyin(o1.UserName);
//            String name2 = PinYinUtil.toPinyin(o2.UserName);
//            if (TextUtils.isEmpty(name1)) {
//                name1 = "#";
//            }
//            if (TextUtils.isEmpty(name2)) {
//                name2 = "#";
//            }
//            //????????????????????????ListView?????????????????????ABCDEFG...?????????
//            if ((String.valueOf(name1.charAt(0))).equals("#")) {
//                return 0;
//            } else if (String.valueOf(name2.charAt(0)).equals("#")) {
//                return 1;
//            } else {
//                return ("" + name1.charAt(0)).compareToIgnoreCase(("" + name2.charAt(0)));
//            }
//        }
//    }
//
//
//    /**
//     * ?????????????????????????????????
//     */
//    private void showSelectUserPopup(List<CheckBoxListViewItem> list) {
//        View view = View.inflate(context, R.layout.pager_select_user_latest, null);
//        view.setBackgroundResource(R.color.bg_list);
//
//        LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_select_user_dept_bottom);
//        ListView lv_select_user = (ListView) view.findViewById(R.id.lv_select_user_latest);
//
//        lv_select_user.setAdapter(new MyAdapter(list));
//
//        ll_bottom.setVisibility(View.GONE);
//
//        selectPopupWindow = new PopupWindow(view, ViewHelper.getScreenWidth(context) / 3 * 2, Util.dip2px(context, 300));
//
//        selectPopupWindow.setAnimationStyle(R.style.AnimationFadeBottom);//????????????
//        selectPopupWindow.update();
//
//        // ??????????????? ???????????????
//        selectPopupWindow.setTouchable(true);
//        selectPopupWindow.setOutsideTouchable(true);
//        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable(context
//                .getResources(), (Bitmap) null));
//
//        selectPopupWindow.showAtLocation(view, Gravity.BOTTOM, ViewHelper.getScreenWidth(context) - selectPopupWindow.getWidth(), 0);
//    }
//
//    private class MyAdapter extends BaseAdapter {
//        private List<CheckBoxListViewItem> list;
//
//        public MyAdapter(List<CheckBoxListViewItem> list) {
//            this.list = list;
//        }
//
//        @Override
//        public int getMsgTotalCount() {
//            return list.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final CheckBoxListViewItem item = list.get(position);
//
//            View view = View.inflate(context, R.layout.item_selected_user, null);
//            TextView tv_name = (TextView) view.findViewById(R.id.tv_selected_user_name);
//            ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_selected_user_delete);
//
//
//            tv_name.setText(item.getName());
//            iv_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    list.remove(item);
//
//                    if (pager.getCurrentItem() == 1) {
//                        if (mDeptListViewItems.indexOf(item) != -1) {
//                            mDeptListViewItems.get(mDeptListViewItems.indexOf(item)).setIsChecked(false);
//                        }
//                        name_checkedUser = list;
//                        tv_select_count.setText(name_checkedUser.size() + "???");
//                        nameAdapter.notifyDataSetChanged();
//                    } else if (pager.getCurrentItem() == 0) {
//                        if (mLatestListViewItems.indexOf(item) != -1) {
//                            mLatestListViewItems.get(mLatestListViewItems.indexOf(item)).setIsChecked(false);
//                        }
//                        latest_checkedUser = list;
//                        tv_select_count_latest.setText(latest_checkedUser.size() + "???");
//                        latestAdapter.notifyDataSetChanged();
//                    }
//                    notifyDataSetChanged();
//                }
//            });
//            return view;
//        }
//    }
//
//    public void setOnDismissListener(OnDismissListener listener) {
//        mPopupWindow.setOnDismissListener(listener);
//    }
//
//    public void dismiss() {
//        mPopupWindow.dismiss();
//    }
//
//
//    public void show() {
////        mPopupWindow.showAtLocation(contentView, Gravity.BOTTOM, ViewHelper.getScreenWidth(context) - getWidth(), 0);\
//        /**
//         * ?????????????????????
//         */
//        if (mLatestListViewItems != null) {
//            for (CheckBoxListViewItem item : mLatestListViewItems) {
//                item.setIsChecked(false);
//            }
//            latestAdapter.notifyDataSetChanged();
//        }
//
//        if (mDeptListViewItems != null) {
//            for (CheckBoxListViewItem item : mDeptListViewItems) {
//                item.setIsChecked(false);
//            }
//            latestAdapter.notifyDataSetChanged();
//        }
//
//        name_checkedUser.clear();
//        latest_checkedUser.clear();
//        tv_select_count_latest.setText(latest_checkedUser.size() + "???");
//        tv_select_count.setText(name_checkedUser.size() + "???");
//
//        mPopupWindow.showAsDropDown(contentView);
//    }
//
//    /***
//     * @param isSingle ???????????????
//     */
//    public void show(boolean isSingle) {
//        this.isSingle = isSingle;
//        show();
//    }
//
//    /**
//     * ??????????????????????????????????????????
//     */
//    public interface SelectUsersListener {
//        void onSelectUsersListener(User user);
//    }
//
//    public void setOnSelectUsersListener(SelectUsersListener listener) {
//        this.listener = listener;
//    }
//}
