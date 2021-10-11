package com.biaozhunyuan.tianyi.task;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.NoScrollGridView;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import okhttp3.Request;

/**
 * 任务泳道图 任务看板
 */

public class TaskBoardFragment extends Fragment {

    private NoScrollListView lv;
    private Context mContext;
    private CommanAdapter<TaskBoard> adapter;
    private SmartRefreshLayout refreshLayout;
    private Display display;
    public static boolean isResume = false;
    private DictIosPickerBottomDialog dictDialog;
    private String projectId = "";
    private String departmentId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_board, null);
        mContext = getActivity();
        WindowManager windowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        initView(view);
        setOnTouchu();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBoardList();
    }

    private void setOnTouchu() {
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getBoardList();
            }
        });
    }

    private void getBoardList() {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板列表;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                refreshLayout.finishRefresh();
                List<TaskBoard> taskBoards = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), TaskBoard.class);
                if (taskBoards != null) {
                    for (int i = 0; i < taskBoards.size(); i++) {
                        List<OaWorkTaskPanelList> oaWorkTaskPanelList = taskBoards.get(i).getOaWorkTaskPanelList();
                        oaWorkTaskPanelList.add(new OaWorkTaskPanelList("", "创建看板.."));
                    }
                    for (int i = 0; i < taskBoards.size(); i++) {
                        if ("项目看板".equals(taskBoards.get(i).getTitle())
                                || "系统看板".equals(taskBoards.get(i).getTitle())) {
                            taskBoards.remove(i);
                        }
                    }
                    adapter = getAdapter(taskBoards);
                    lv.setAdapter(adapter);
                }
                ProgressDialogHelper.dismiss();
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

    private CommanAdapter<TaskBoard> getAdapter(List<TaskBoard> taskList) {
        return new CommanAdapter<TaskBoard>(taskList, getActivity(), R.layout.item_task_board_list) {

            @Override
            public void convert(int position, final TaskBoard item, final BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_board_title, item.getTitle());
                NoScrollGridView gridView = viewHolder.getView(R.id.item_gv);
                gridView.setAdapter(getItemAdapter(item.getOaWorkTaskPanelList()));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        OaWorkTaskPanelList oaWorkTaskPanelList = item.getOaWorkTaskPanelList().get(position);
                        if (oaWorkTaskPanelList.getTitle().equals("创建看板..") && oaWorkTaskPanelList.getContent().equals("")) {
                            String title = item.getTitle();
                            if (title.equals("个人看板")) {
                                showAddBoardDialog("0");
                            } else if (title.equals("项目看板")) {
                                showAddBoardDialog("1");
                            } else if (title.equals("部门看板")) {
                                showAddBoardDialog("2");
                            }
                        } else {
                            Intent intent = new Intent(getActivity(), TaskLaneTrelloActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("TaskBoard", oaWorkTaskPanelList);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
            }
        };
    }

    private CommanAdapter<OaWorkTaskPanelList> getItemAdapter(List<OaWorkTaskPanelList> taskList) {
        return new CommanAdapter<OaWorkTaskPanelList>(taskList, getActivity(), R.layout.item_board_grid_list) {

            @Override
            public void convert(int position, final OaWorkTaskPanelList item, final BoeryunViewHolder viewHolder) {
                String title = item.getTitle();
                String content = item.getContent();
                if (title.equals("创建看板..") && content.equals("")) {
                    LinearLayout ll = viewHolder.getView(R.id.ll_parent);
                    TextView titile = viewHolder.getView(R.id.tv_title);
                    ll.setBackgroundColor(Color.parseColor("#d8d8d8"));
                    titile.setTextColor(Color.parseColor("#444444"));
                }
                viewHolder.setTextValue(R.id.tv_title, title);
                viewHolder.setTextValue(R.id.tv_content, content);
            }
        };
    }

    private void initView(View view) {
        lv = view.findViewById(R.id.lv);
        refreshLayout = view.findViewById(R.id.refreshLayout);

        refreshLayout.setReboundDuration(200);//回弹动画时长（毫秒）
        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
//        refreshLayout.setRefreshHeader(new BezierRadarHeader(getActivity()).setEnableHorizontalDrag(true));
        dictDialog = new DictIosPickerBottomDialog(mContext);
    }

    /**
     * 新建看板弹出框
     *
     * @param panelType 0:个人看板 1:项目看板 2:部门看板
     *                  oa_project :项目字典
     *                  base_department : 部门字典
     */
    private void showAddBoardDialog(String panelType) {
        String type = panelType;
        String dict = "";
        projectId = "";
        departmentId = "";
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_task_board_view, null);
        Dialog alertDialog = new Dialog(mContext, R.style.AlertDialogStyle);
        alertDialog.setContentView(view);
        LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));
        EditText et_content = view.findViewById(R.id.et_dialog_content);
        EditText et_title = view.findViewById(R.id.et_dialog_title);
        Button btn_neg = view.findViewById(R.id.btn_neg);
        Button btn_pos = view.findViewById(R.id.btn_pos);
        LinearLayout ll_dict = view.findViewById(R.id.ll_dict);
        EditText dict_content = view.findViewById(R.id.et_dialog_dict_content);
        TextView dict_tv = view.findViewById(R.id.tv_dict);
        if (type.equals("0")) {
            ll_dict.setVisibility(View.GONE);
        } else {
            ll_dict.setVisibility(View.VISIBLE);
            if (type.equals("1")) {
                dict = "oa_project";
                dict_tv.setText("选择项目 :");
            } else {
                dict = "base_department";
                dict_tv.setText("选择部门 :");
            }
        }
        setEditTextEnabled(dict_content);
        String finalDict = dict;
        dict_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dictDialog.show(finalDict, true);
            }
        });

        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String title = et_title.getText().toString();
                if (!TextUtils.isEmpty(title) && title.length() >= 10) {
                    Toast.makeText(mContext, "看板名称最多只能输入十个字", Toast.LENGTH_SHORT).show();
                    et_title.setText(title.substring(0, 9));
                }
            }
        });
        dictDialog.setOnSelectedDictListener(new DictIosPickerBottomDialog.onSelectDictListener() {
            @Override
            public void onSelectedDict(字典 dict) {
                if (type.equals("1")) {
                    projectId = dict.getUuid();
                } else {
                    departmentId = dict.getUuid();
                }
                dict_content.setText(dict.getName());
            }
        });

        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                InputSoftHelper.hideKeyboard(et_content);
            }
        });
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (!TextUtils.isEmpty(title)) {
                    if ("2".equals(panelType) && TextUtils.isEmpty(departmentId)) {
                        Toast.makeText(mContext, "部门不能为空!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String url = Global.BASE_JAVA_URL + GlobalMethord.新建任务看板
                            + "?title=" + title + "&content=" + content + "&panelStyle=rgb(205,90,145)&panelType=" + type
                            + "&projectId=" + projectId + "&departmentId=" + departmentId;
                    StringRequest.getAsyn(url, new StringResponseCallBack() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(mContext, "新建成功", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            getBoardList();
                        }

                        @Override
                        public void onFailure(Request request, Exception ex) {

                        }

                        @Override
                        public void onResponseCodeErro(String result) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(mContext, "看板标题不能为空!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
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
}
