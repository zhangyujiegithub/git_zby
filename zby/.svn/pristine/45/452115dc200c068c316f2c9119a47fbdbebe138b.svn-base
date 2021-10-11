package com.biaozhunyuan.tianyi.client;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.chy.srlibrary.PTRLayoutView;
import com.chy.srlibrary.SwipeMenu;
import com.chy.srlibrary.SwipeMenuItem;
import com.chy.srlibrary.interfaceutil.SwipeMenuCreatorInterfaceUtil;
import com.chy.srlibrary.slistview.SMListView;
import com.chy.srlibrary.slistview.SMRListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 台账
 */

public class ClientStandingBookFragment extends Fragment {

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private static final String PARAM_CLIENT_TABLENAME = "PARAM_CLIENT_TABLENAME";
    private String mClientId = ""; //客户id
    private String tableName = ""; //表名
    private SMRListView lv;
    private Demand<MapBean> demand;
    private int pageIndex = 1;
    private CommanAdapter<MapBean> adapter;
    private List<MapBean> mapBeanList = new ArrayList<>(); //数据源
    private List<StandingBook> standingBooks = new ArrayList<>(); //存放布局列的集合
    public static boolean isResume = false;
    private int leftPadding;
    private AlertDialog dialog;
    private PTRLayoutView refresh_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
            tableName = getArguments().getString(PARAM_CLIENT_TABLENAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clientstandingbook, null);
        initView(v);
        getItemField();
        setOnTouchEvent();
        return v;
    }

    private void getItemField() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情台账条目显示字段;
        JSONObject js = new JSONObject();
        try {
            js.put("tableName", tableName);
        } catch (Exception e) {

        }
        StringRequest.postAsyn(url, js, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                standingBooks = JsonUtils.jsonToArrayEntity(result, StandingBook.class);
//                viewList.clear();
                String dictionaryNames = getDictionaryNames(standingBooks);
                initDemand(dictionaryNames);
            }
        });
    }

    /**
     * 生成Textview 添加到Linearlayout中 获取需要查询字典的字段
     *
     * @param sb
     */
    private String getDictionaryNames(List<StandingBook> sb) {
        String dictionaryNames = "";
        for (StandingBook stand : sb) {
            if (stand.getType().equals("dictionary")) { //如果动态字段的类型为字典 拼接字段名.表名 从列表接口查询
                dictionaryNames += stand.getField() + "." + stand.getRemoteDictionaryName() + ",";
            }
        }
        if (!TextUtils.isEmpty(dictionaryNames)) {
            return dictionaryNames.substring(0, dictionaryNames.length() - 1);
        } else {
            return "";
        }
    }


    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        refresh_view = v.findViewById(R.id.refresh_view);
        /** 屏幕密度：每英寸有多少个显示点，和分辨率不同 */
        final float scale = getActivity().getResources().getDisplayMetrics().density;
        leftPadding = (int) (5 * scale + 0.5f);

        setListViewMenu();
    }

    private void setListViewMenu() {
        final SwipeMenuCreatorInterfaceUtil creator = new SwipeMenuCreatorInterfaceUtil() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.parseColor("#FF0000")));
                deleteItem.setWidth(300);
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.parseColor("#ffffff"));
                deleteItem.setTitleSize(14);
                menu.addMenuItem(deleteItem);
            }
        };
        // 设置侧滑删除(如果不设置则不会有侧滑删除，仅有刷新)
        lv.setMenuCreator(creator);
        // 点击侧滑按钮的响应事件（如果涉及自定义的侧滑布局，可参考SwipMenuListView的实现方法）
        // 侧滑的监听事件
        lv.setOnMenuItemClickListener(new SMListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int i, SwipeMenu swipeMenu, int i1) {
                switch (i1) {
                    case 0:
                        if (((CustomerDetailsActivity) getActivity()).isMineCustomer()) { //判断是否为自己的客户
                            MapBean item = adapter.getItem(i);
                            showDeleteDialog(item);
                        } else {
                            Toast.makeText(getActivity(), "您无权删除该客户台账", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });

        lv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { //失去焦点 条目恢复
                    lv.smoothCloseMenu();
                }
            }
        });

        refresh_view.setOnRefreshListener(new PTRLayoutView.OnRefreshListener() {
            @Override
            public void onRefresh(PTRLayoutView ptrLayoutView) {
                pageIndex = 1;
                getItemField();
            }

            @Override
            public void onLoadMore(PTRLayoutView ptrLayoutView) {
                getList();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            getItemField();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isResume = false;
    }

    private void setOnTouchEvent() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapBean item = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), StandingBookInfoActivity.class);
                intent.putExtra("id", item.getUuid());
                intent.putExtra("tableName", tableName);
                intent.putExtra("isMineCustomer", ((CustomerDetailsActivity) getActivity()).isMineCustomer());
                startActivity(intent);
            }
        });

//        /**
//         * 查看更多
//         */
//        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                getList();
//            }
//        });
//
//        /**
//         * 下拉刷新
//         */
//        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageIndex = 1;
//                getList();
//            }
//        });
    }

    private void showDeleteDialog(MapBean item) {
        dialog = new AlertDialog(getActivity()).builder().
                setTitle("删除")
                .setMsg("是否删除该条台账?")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dissMiss();
                    }
                })
                .setPositiveButton("删除", new AlertDialog.onClickListener() {
                    @Override
                    public void onClick(View v, String msg) {
                        ProgressDialogHelper.show(getActivity());
                        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情台账删除 + tableName + "&ids=" + item.getUuid();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                StringRequest.deleteAsyn(url, new StringResponseCallBack() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(getActivity(), JsonUtils.pareseData(response), Toast.LENGTH_SHORT).show();
                                        adapter.remove(item);
                                    }

                                    @Override
                                    public void onFailure(Request request, Exception ex) {

                                    }

                                    @Override
                                    public void onResponseCodeErro(String result) {
                                        ProgressDialogHelper.dismiss();
                                        Toast.makeText(getActivity(), JsonUtils.pareseData(result), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).start();
                    }
                });
        dialog.show();
    }

    private void initDemand(String dictionaryNames) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户详情台账数据列表 + tableName + "&customerId=" + mClientId;
        demand = new Demand(MapBean.class);
        demand.pageSize = 99;
        demand.dictionaryNames = dictionaryNames;
        demand.src = url;
        getList();
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                refresh_view.refreshFinish(PTRLayoutView.SUCCEED);
                refresh_view.loadmoreFinish(PTRLayoutView.SUCCEED);
                try {
                    List<MapBean> mapList = new ArrayList<>();
                    JSONObject js = new JSONObject(response);
                    JSONObject Data = js.getJSONObject("Data");
                    JSONArray data = Data.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        Map<String, String> map = new HashMap<String, String>();//创建一个Map来存放动态Key和value
                        MapBean mapBean = new MapBean();
                        JSONObject jo = data.getJSONObject(i);
                        Iterator<String> iterator = jo.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next(); //获取jsonobject的key
                            if (key.equals("uuid")) { //如果是uuid单独取出设置到javabean中
                                mapBean.setUuid(jo.optString(key));
                            } else {
                                map.put(key, jo.optString(key));
                            }
                        }
                        mapBean.setMap(map);
                        mapList.add(mapBean);
                    }
                    mapBeanList = mapList;
                    /**
                     * 给map集合设置字典值
                     */
                    if (standingBooks.size() > 0) {
                        StandingBook tag = null;
                        HashMap<String, Map<String, String>> dict = demand.dict; //拿到从列表接口得到的字典数据
                        for (int j = 0; j < standingBooks.size(); j++) {
                            tag = standingBooks.get(j);//通过textview获取所有绑定的动态字段
                            if (tag.getType().equals("dictionary")) { //判断类型是否为字典
                                for (int i = 0; i < mapList.size(); i++) {
                                    Map<String, String> map = mapList.get(i).getMap(); //拿到列表每条的map集合然后遍历
                                    Iterator<Map.Entry<String, String>> iteratorList = map.entrySet().iterator();
                                    while (iteratorList.hasNext()) {
                                        Map.Entry<String, String> nextList = iteratorList.next();
                                        //遍历从列表拿到的字典数据 循环对比配对设值
                                        Iterator<Map.Entry<String, Map<String, String>>> iteratorDictionary = dict.entrySet().iterator();
                                        while (iteratorDictionary.hasNext()) {
                                            Map.Entry<String, Map<String, String>> nextDictionary = iteratorDictionary.next();
                                            if (nextDictionary.getKey().contains(nextList.getKey())) { //通过列表条目的map和字典数据map循环对比key值来配对
                                                Map<String, String> mapValue = nextDictionary.getValue(); //key值匹配成功后 取出字典数据的value 也是一个map集合然后遍历
                                                Iterator<Map.Entry<String, String>> dictIterator = mapValue.entrySet().iterator();
                                                while (dictIterator.hasNext()) {
                                                    Map.Entry<String, String> nextDict = dictIterator.next();
                                                    if (nextList.getValue().equals(nextDict.getKey())) { //列表条目的map的value就是需要更换的id  字典数据的map的value 就是id的值 循环匹配并替换map值
                                                        map.put(nextList.getKey(), nextDict.getValue());
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(mapBeanList);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(mapBeanList, false);
                    if (mapBeanList != null && mapBeanList.size() == 0) {
                        Toast.makeText(getActivity(), "已经加载了全部数据", Toast.LENGTH_SHORT).show();
                    }
//                    lv.loadCompleted();
                }
//                pageIndex += 1;
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

    private CommanAdapter<MapBean> getAdapter(List<MapBean> gridItems) {
        return new CommanAdapter<MapBean>(gridItems, getActivity(),
                R.layout.item_standingbook_list) {

            @Override
            public void convert(int position, MapBean item, BoeryunViewHolder viewHolder) {

                LinearLayout ll_parent = viewHolder.getView(R.id.ll_parent);
                if (ll_parent.getChildCount() == 0) {
                    if (standingBooks.size() > 0) {
                        int size = standingBooks.size() > 4 ? 4 : standingBooks.size();
                        for (int i = 0; i < size; i++) { //遍历容器中所有view
                            StandingBook tag = standingBooks.get(i);
                            Map<String, String> map = item.getMap();
                            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
                            while (iterator.hasNext()) {
                                Map.Entry<String, String> next = iterator.next();
                                if (tag.getField().equals(next.getKey())) { //如果绑定的tag和item对象是对应的 给view设置text
                                    if (!TextUtils.isEmpty(tag.getHeader()) && !TextUtils.isEmpty(next.getValue())) {
                                        createUi(tag, ll_parent, tag.getHeader() + " : " + next.getValue());
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public static ClientStandingBookFragment newInstance(String Id, String tableName) {
        ClientStandingBookFragment fragment = new ClientStandingBookFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        args.putString(PARAM_CLIENT_TABLENAME, tableName);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 生成textview控件
     *
     * @param sb
     */
    private void createUi(StandingBook sb, LinearLayout ll_parent, String text) {
        if (!TextUtils.isEmpty(text)) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(getActivity());
            textView.setTextColor(getActivity().getResources().getColor(
                    R.color.titlecolor));
            params.setMargins(0, 8, 0, 0);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(params);
            textView.setPadding(leftPadding * 2, leftPadding, leftPadding * 2, leftPadding);
            textView.setGravity(Gravity.CENTER);
            textView.setText(text);
            textView.setTag(sb); //绑定实体类
            ll_parent.addView(textView);
        }
    }
}
