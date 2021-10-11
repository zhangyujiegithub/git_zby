package com.biaozhunyuan.tianyi.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 周期任务
 */

public class TaskPeriodicFragment extends Fragment {


    private PullToRefreshAndLoadMoreListView lv;
    private CommanAdapter<Task> adapter;
    private Demand<Task> demand;
    private int pageIndex = 1;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private Task currentItem;
    public static boolean isResume = false;
    private BoeryunSearchView seachButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_periodic, null);
        initView(v);
        initDemand();
        getTaskList();
        setOnTouch();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            getTaskList();
            isResume = true;
        }
    }

    private void setOnTouch() {
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getTaskList();
            }
        });
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getTaskList();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(getActivity(), TaskInfoActivityNew.class);
                    Task task = adapter.getDataList().get(position - 1);
                    Bundle bundle = new Bundle();
                    task.setPeriodTask(true);
                    bundle.putSerializable("taskInfo", task);
                    intent.putExtra("taskIntentInfo", bundle);
                    startActivity(intent);
                }
            }
        });
        seachButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                Map<String, String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_content", "1|" + str);
                demand.keyMap = searchMap;
                pageIndex = 1;
                getTaskList();
            }
        });
        seachButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                Map<String, String> searchMap = new HashMap<>();
                searchMap.put("searchField_string_content", "");
                demand.keyMap = searchMap;
                pageIndex = 1;
                getTaskList();
            }

            @Override
            public void OnClick() {

            }
        });
    }

    private void initDemand() {
        demand = new Demand<>(Task.class);
        demand.pageSize = 10;
        demand.dictionaryNames = "executorIds.base_Staff,cycleType.dict_cycle_type,cycleDay.dict_cycle_days";
        demand.sortField = "cycleStartTime desc";
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.周期任务列表;
    }

    private void getTaskList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<Task> data = demand.data;
                try {
                    for (Task t : data) {
                        t.setCycleTypeName(demand.getDictName(t, "cycleType"));
                        t.setCycleDayName(demand.getDictName(t, "cycleDay"));
                        t.setExecutorNames(demand.getDictName(t, "executorIds"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(data);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(data, false);
                    if (data != null && data.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });

    }

    private CommanAdapter<Task> getAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, getActivity(), R.layout.item_tasklist_list) {
            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                String cycleStartTime = ViewHelper.getStringFormat(item.getCycleStartTime(), "yyyy-MM-dd");
                String cycleEndTime = ViewHelper.getStringFormat(item.getCycleEndTime(), "yyyy-MM-dd");
                viewHolder.setTextValue(R.id.time_item_task_list,
                        cycleStartTime + " - " + cycleEndTime);
                viewHolder.setTextValue(R.id.textViewTitle_tasklist, item.getContent());
                viewHolder.setTextValue(R.id.name_item_task_list, item.getExecutorNames());

                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getExecutorIds());//创建人头像


//                TextView tvStatus = viewHolder.getView(R.id.tv_state_tasklist);
//                /**
//                 * 根据任务状态枚举类型显示状态
//                 *
//                 */
//                if (isAdded()) {
//                    if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
//                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
//                                R.color.color_status_wancheng), 5, 0));
//                    } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
//                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
//                                R.color.color_status_qidong), 5, 0));
//                    } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
//                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
//                                R.color.color_status_wancheng), 5, 0));
//                    } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
//                        tvStatus.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(getActivity().getResources().getColor(
//                                R.color.color_status_weiwancheng), 5, 0));
//                    }
//                }
//
//                final ImageView iv_support = viewHolder.getView(R.id.iv_item_task_support);
//                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_task_item);
//                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_task_item);

//                if (item.isLike()) {
//                    iv_support.setImageResource(R.drawable.icon_support_select);
//                } else {
//                    iv_support.setImageResource(R.drawable.icon_support);
//                }
//
//                tv_support.setText(item.getLikeNumber() + "");
//                tv_comment.setText(item.getCommentNumber() + "");
//
//                //新加: 点赞和评论
//
//                LinearLayout ll_support = viewHolder.getView(R.id.ll_task_item_support);//dianzan
//                LinearLayout ll_comment = viewHolder.getView(R.id.ll_task_item_comment);
//
//                ll_comment.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        currentItem = item;
////                        ll_bottom_comment.setVisibility(View.VISIBLE);
////                        et_comments.requestFocus();
////                        hideShowSoft();
//                        popWiw(currentItem);
//                    }
//                });
//
//                //点赞/取消赞
//                ll_support.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getExecutorIds());
//                        post.setDataType("任务计划");
//                        post.setDataId(item.getUuid());
//                        if (item.isLike()) { //取消点赞
//                            cancleSupport(post, item);
//                        } else { //点赞
//                            support(post, item);
//                        }
//                    }
//                });
            }
        };
    }

//    /**
//     * 评论
//     *
//     * @param post
//     */
//    public void comment(SupportAndCommentPost post, final Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
////        et_comments.setText("");
////        InputSoftHelper.hiddenSoftInput(getActivity(), et_comments);
////        ll_bottom_comment.setVisibility(View.GONE);
//        hideShowSoft();
//        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
//                task.setCommentNumber(task.getCommentNumber() + 1);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//                Log.e("tag", "评论失败");
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//    }
//
//    /**
//     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
//     */
//    private void hideShowSoft() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//
//    /**
//     * 点赞
//     *
//     * @param post
//     * @param task
//     */
//    private void support(SupportAndCommentPost post, final Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;
//
//        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "点赞成功", Toast.LENGTH_SHORT).show();
//                task.setLikeNumber(task.getLikeNumber() + 1);
//                task.setLike(true);
//                adapter.notifyDataSetChanged();
////                ll_bottom_comment.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//    }
//
//    /**
//     * 取消点赞
//     *
//     * @param post 要取消点赞的实体的ID
//     * @param task
//     */
//    private void cancleSupport(SupportAndCommentPost post, final Task task) {
//        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;
//
//        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(getActivity(), "取消点赞成功", Toast.LENGTH_SHORT).show();
//                task.setLikeNumber(task.getLikeNumber() - 1);
//                task.setLike(false);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//    }
//
//
//    private void popWiw(final Task item) {
//
//        popWiw = new BaseSelectPopupWindow(getActivity(), R.layout.edit_data);
//        // popWiw.setOpenKeyboard(true);
//        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//        popWiw.setFocusable(true);
//        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popWiw.setShowTitle(false);
//        popWiw.setBackgroundDrawable(new ColorDrawable(0));
//        InputMethodManager im = (InputMethodManager) getActivity()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//        final TextView send = popWiw.getContentView().findViewById(
//                R.id.btn_send);
//        final TextEditTextView edt = popWiw.getContentView().findViewById(
//                R.id.edt_content);
//
//        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
//        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);
//        edt.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                if (TextUtils.isEmpty(edt.getText())) {
//                    send.setEnabled(false);
//                } else {
//                    send.setEnabled(true);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId,
//                                          KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND
//                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
//                        String content = edt.getText().toString().trim();
//
//                        SupportAndCommentPost post = new SupportAndCommentPost();
//                        post.setFromId(Global.mUser.getUuid());
//                        post.setToId(item.getExecutorIds());
//                        post.setDataType("任务计划");
//                        post.setDataId(item.getUuid());
//                        post.setContent(content);
//                        comment(post, item);
//
//                        popWiw.dismiss();
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
//        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            }
//        });
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
//                    // /提交内容
//                    String content = edt.getText().toString().trim();
//
//                    SupportAndCommentPost post = new SupportAndCommentPost();
//                    post.setFromId(Global.mUser.getUuid());
//                    post.setToId(item.getExecutorIds());
//                    post.setDataType("任务计划");
//                    post.setDataId(item.getUuid());
//                    post.setContent(content);
//                    comment(post, item);
//
//                    popWiw.dismiss();
//                }
//            }
//        });
//
//        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
//                | Gravity.CENTER_HORIZONTAL, 0, 0);
//    }


    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
        seachButton = v.findViewById(R.id.seach_button);
    }
}
