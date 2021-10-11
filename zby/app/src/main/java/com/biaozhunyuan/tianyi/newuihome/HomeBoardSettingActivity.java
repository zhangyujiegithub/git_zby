package com.biaozhunyuan.tianyi.newuihome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.model.other.FunctionBoard;
import com.biaozhunyuan.tianyi.helper.ParamCallback;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.models.MenuFunction;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.utils.ParamUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.widget.dragSortListView.DragSortListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2019/4/9.
 * 首页看板设置页面（可设置首页显示的版块，和给显示的版块排序)
 */

public class HomeBoardSettingActivity extends BaseActivity {

    private Context context;
    private CommanAdapter<FunctionBoard> showAdapter;
    private CommanAdapter<FunctionBoard> hiddenAdapter;
    private DragSortListView dragSortListView;
    private BoeryunHeaderView headerView;
    private MenuFunction menuFunction;
    private ListView hiddenListView;
    private boolean applyExist;
    private boolean taskExist;
    private boolean noticeExist;

    private long lastClickTime = 0;//上次点击时间
    //点击时间间隔2秒
    private final int CLICK_INTERVER_TIME = 2000;
    private final int CLICK_INTERVER_HANDLER = 0x111;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CLICK_INTERVER_HANDLER) {
                List<FunctionBoard> dataList = showAdapter.getDataList();
                Log.e("CLICK_INTERVER_HANDLER", dataList.size() + "");
                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setIndex(i);
                }
                EventBus.getDefault().post(new BoardMessageEvent(dataList));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_home_board);
        initData();
        initView();
//        getFunctionList();
        getMenuListAndShow(new ArrayList<>());
        setOnEvent();
    }

    private void getFunctionList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取H5灵活菜单 + "?menuType=menu";
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<H5Menu> h5Menus = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), H5Menu.class);

                getMenuListAndShow(h5Menus);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }

    private void getMenuListAndShow(List<H5Menu> h5Menus) {
        ParamUtils.getParam(ParamUtils.MENUHOME_FUNCTION_BOARD, new ParamCallback() {
            @Override
            public void onParam(String value) {
                List<FunctionBoard> functionBoards = new ArrayList<>();
                List<FunctionBoard> functionBoardsShowList = new ArrayList<>();
                List<FunctionBoard> functionBoardsHiddenList = new ArrayList<>();
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
                    functionBoards = new ArrayList<>();
                }

                for (FunctionBoard fb : functionBoards) {
                        /*for (H5Menu h5 : h5Menus) {
                            boolean isExist = false;
                            if (fb.getFunction().equals(h5.getTitle())) {
                                isExist = true;
                            }
                            if (isExist) { //如果存在 添加到显示的列表中
                                functionBoardsShowList.add(new FunctionBoard(h5.getH5url(), fb.getIndex(), fb.getFunction()));
                            } else { //如果不存在 添加到隐藏列表中
                                if (!removeDuplicate(h5, functionBoardsHiddenList)) {
                                    functionBoardsHiddenList.add(new FunctionBoard(h5.getH5url(), 0, h5.getTitle()));
                                }
                            }
                        }*/
                    if (fb.getFunction().equals("申请")) {
                        applyExist = true;
                    } else if (fb.getFunction().equals("任务")) {
                        taskExist = true;
                    } else if (fb.getFunction().equals("通知")) {
                        noticeExist = true;
                    }
                }

                if (!noticeExist) {
                    functionBoardsHiddenList.add(0, new FunctionBoard(0, "通知"));
                }
                if (!applyExist) {
                    functionBoardsHiddenList.add(0, new FunctionBoard(0, "申请"));
                }
                if (!taskExist) {
                    functionBoardsHiddenList.add(0, new FunctionBoard(0, "任务"));
                }
                Iterator<FunctionBoard> iterator = functionBoardsHiddenList.iterator();
                while (iterator.hasNext()) {
                    FunctionBoard next = iterator.next();
                    for (FunctionBoard fb : functionBoardsShowList) {
                        if (next.getFunction().equals(fb.getFunction())) {
                            iterator.remove();
                        }
                    }
                }
                for (FunctionBoard fb : functionBoardsShowList) {
                    functionBoards.add(fb);
                }
                List<FunctionBoard> functions = menuFunction.getFunctions(functionBoards);
                List<FunctionBoard> functionHidden = menuFunction.getFunctions(functionBoardsHiddenList);
                ViewHelper.sortList(functions);
                showAdapter = getShowBoardAdapter(functions);
                hiddenAdapter = getHiddenAdapter(functionHidden);
                dragSortListView.setAdapter(showAdapter);
                hiddenListView.setAdapter(hiddenAdapter);
            }

            @Override
            public void onFailure() {

            }
        });
    }


    //把list里的对象遍历一遍，用list.contain()，如果不存在就放入到另外一个list集合中
    public boolean removeDuplicate(H5Menu h5, List<FunctionBoard> h5Menus) {
        boolean isRePeat = false;
        for (FunctionBoard fb : h5Menus) {
            if (fb.getFunction().equals(h5.getTitle())) {
                isRePeat = true;
                break;
            }
        }
        return isRePeat;
    }


    private void initData() {
        menuFunction = new MenuFunction();
        context = HomeBoardSettingActivity.this;
//        userDao = ORMDataHelper.getInstance(context).getUserDao();
        applyExist = false;
        taskExist = false;
        noticeExist = false;
    }

    private void initView() {
        dragSortListView = findViewById(R.id.drag_list_view);
        headerView = findViewById(R.id.headerview);
        hiddenListView = findViewById(R.id.hiddenListView);
    }


    private void setOnEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                if (handler.hasMessages(CLICK_INTERVER_HANDLER)) { //判断是否消息队列发送完毕
                    handler.removeMessages(CLICK_INTERVER_HANDLER);//移除延迟发送的消息
                    handler.sendEmptyMessage(CLICK_INTERVER_HANDLER);
                }
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });


        /**
         * 拖动完成事件
         */
        DragSortListView.DropListener dropListener = new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                FunctionBoard item = showAdapter.getItem(from);
                showAdapter.remove(from);
                showAdapter.insert(to, item);
                sendBroadEvent();
            }
        };

        dragSortListView.setDropListener(dropListener);
    }

    /**
     * 发送广播事件
     */
    @SuppressLint("NewApi")
    private void sendBroadEvent() {
        long currentClickTime = SystemClock.uptimeMillis();
        if (currentClickTime - lastClickTime <= CLICK_INTERVER_TIME) {
            handler.removeMessages(CLICK_INTERVER_HANDLER);
        } else {
            lastClickTime = currentClickTime;
        }
        handler.sendEmptyMessageDelayed(CLICK_INTERVER_HANDLER, CLICK_INTERVER_TIME);
    }

    private CommanAdapter<FunctionBoard> getShowBoardAdapter(final List<FunctionBoard> list) {
        return new CommanAdapter<FunctionBoard>(list, context, R.layout.item_board_setting_list) {
            /***
             * 移除其中某一条
             * @param item
             */
            @Override
            public void remove(FunctionBoard item) {
                super.remove(item);
            }

            @Override
            public void convert(int position, final FunctionBoard item, BoeryunViewHolder viewHolder) {
                viewHolder.setImageResoure(R.id.circleImageView, item.getIco());
                viewHolder.setTextValue(R.id.tv_board_title, item.getFunction());
                if (!TextUtils.isEmpty(item.getFunction())
                        && ("申请".equals(item.getFunction()) || "通知".equals(item.getFunction()))) {
                    viewHolder.getView(R.id.tv_hint).setVisibility(View.VISIBLE);
                } else {
                    viewHolder.getView(R.id.tv_hint).setVisibility(View.INVISIBLE);
                }
                //隐藏
                viewHolder.getView(R.id.iv_hidden).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(item);
                        hiddenAdapter.addData(item, 0);
                        sendBroadEvent();
                    }
                });
                ImageView iv_move = viewHolder.getView(R.id.iv_move);
            }

        };
    }

    private CommanAdapter<FunctionBoard> getHiddenAdapter(final List<FunctionBoard> list) {
        return new CommanAdapter<FunctionBoard>(list, context, R.layout.item_board_setting_hidden_list) {
            /***
             * 移除其中某一条
             * @param item
             */
            @Override
            public void remove(FunctionBoard item) {
                super.remove(item);
            }

            @Override
            public void convert(int position, final FunctionBoard item, BoeryunViewHolder viewHolder) {
                viewHolder.setImageResoure(R.id.circleImageView, item.getIco());
                viewHolder.setTextValue(R.id.tv_board_title, item.getFunction());
                //显示
                viewHolder.getView(R.id.iv_show).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(item);
                        showAdapter.addData(item, showAdapter.getCount());
                        sendBroadEvent();
                    }
                });
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (handler.hasMessages(CLICK_INTERVER_HANDLER)) { //判断是否消息队列发送完毕
            handler.removeMessages(CLICK_INTERVER_HANDLER);//移除延迟发送的消息
            handler.sendEmptyMessage(CLICK_INTERVER_HANDLER);
        }
        finish();
        super.onBackPressed();
    }
}
