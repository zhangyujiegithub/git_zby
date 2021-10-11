package com.biaozhunyuan.tianyi.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.helper.ZLServiceHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.model.Task;
import com.biaozhunyuan.tianyi.task.TaskInfoActivity;
import com.biaozhunyuan.tianyi.task.TaskStatusEnum;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import java.util.List;

import okhttp3.Request;

/**
 * 客户关联任务列表Fragment
 */
public class ClientTaskListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private static final String PARAM_PROJECT_ID = "PARAM_PROJECT_ID";
    // TODO: Rename and change types of parameters
    private String mClientId = "";
    private String[] status = new String[]{"完成", "取消"}; //tab的标题


    private Context context;
    private CommanAdapter<Task> adapter;
    private List<Task> recordList;
    private String dictionary = "";
    private DictionaryHelper helper;
    //    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1; //当前页
    private Demand<Task> demand;
    public static boolean isResume = false;

    private ZLServiceHelper zlServiceHelper = new ZLServiceHelper();


    private LinearLayout ll_bottom_comment;
    private EditText et_comment;
    private Button btn_comment;


    public ClientTaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Id 编号
     * @return A new instance of fragment ClientContactListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientTaskListFragment newInstance(String Id) {
        ClientTaskListFragment fragment = new ClientTaskListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_contact_list, container, false);
        context = getActivity();
        isResume = false;
        helper = new DictionaryHelper(context);
        initViews(view);
        initDemand();
        getWorkList();
        setOnEvent();
        return view;
    }


    private void initDemand() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务过滤 + "?customerId=" + mClientId;
        demand = new Demand<Task>(Task.class);
        demand.pageSize = 10;
        demand.sort = "desc";
        demand.sortField = "creationTime";
        demand.dictionaryNames = "creatorId.base_staff,executorIds.base_staff";
        demand.src = url;
    }

    @Override
    public void onResume() {
        if (isResume) {
            lv.startRefresh();
            pageIndex = 1;
            getWorkList();
            isResume = false;
        }
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initViews(View view) {
        lv = (PullToRefreshAndLoadMoreListView) view.findViewById(R.id.lv_fragment_client_contact_list);

        ll_bottom_comment = view.findViewById(R.id.ll_comment_share_bottom);
        et_comment = view.findViewById(R.id.et_input_bottom_comment);
        btn_comment = view.findViewById(R.id.btn_share_comment);

    }

    private void getWorkList() {

        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                recordList = demand.data;


                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(recordList);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(recordList, false);
                    if (recordList != null && recordList.size() == 0) {
                        lv.loadAllData();
                    }
                    lv.loadCompleted();
                }
                pageIndex += 1;

                dictionary = demand.dictionary;

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context, TaskInfoActivity.class);
                        Task task = adapter.getDataList().get(position - 1);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("taskInfo", task);
                        intent.putExtra("taskIntentInfo", bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                lv.onRefreshComplete();
                lv.loadCompleted();
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
        }
    }

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setOnEvent() {


        /**
         * 查看更多
         */
        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getWorkList();
            }
        });

        /**
         * 下拉刷新
         */
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getWorkList();
            }
        });
    }

    private CommanAdapter<Task> getAdapter(List<Task> taskList) {
        return new CommanAdapter<Task>(taskList, context, R.layout.item_task_list) {
            private DictIosPickerBottomDialog dialog;

            @SuppressLint("NewApi")
            @Override
            public void convert(int position, final Task item, final BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNamesById(item.getCreatorId()));
                viewHolder.setTextValue(R.id.tv_time_task_item, ViewHelper.getDateStringFormat(item.getCreationTime()));
                viewHolder.setTextValue(R.id.content_task_list, item.getContent());
                viewHolder.setTextValue(R.id.tv_excutor_task_item, helper.getUserNamesById(item.getParticipantIds()));
                viewHolder.setTextValue(R.id.deadline_task_item, ViewHelper.convertStrToFormatDateStr(item.getEndTime(), "yyyy-MM-dd kk:mm"));

                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getCreatorId());//创建人头像

                final ImageView ivStatus = viewHolder.getView(R.id.iv_status_task);


                /**
                 * 根据任务状态枚举类型显示状态
                 */
                if (TaskStatusEnum.已完成.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.ic_task_done);
                } else if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.ic_task_going);
                } else if (TaskStatusEnum.已取消.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.ic_task_cancle);
                } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                    ivStatus.setImageResource(R.drawable.ic_task_late);
                }

                if (!TextUtils.isEmpty(item.getAttachmentIds())) {
                    String[] arr = item.getAttachmentIds().split(",");
                    if (arr.length > 0) {
                        viewHolder.getView(R.id.ll_attch_task_list).setVisibility(View.VISIBLE);
                        viewHolder.setTextValue(R.id.tv_attch_count_task_list, arr.length + "个附件");
                    }
                } else {
                    viewHolder.getView(R.id.ll_attch_task_list).setVisibility(View.GONE);
                }


                ivStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog = new DictIosPickerBottomDialog(getActivity());
                        if (TaskStatusEnum.进行中.getName().equals(item.getStatus())) {
                            dialog.show(status);
                        } else if (TaskStatusEnum.已逾期.getName().equals(item.getStatus())) {
                            dialog.show(status);
                        } else {
                            Toast.makeText(context, "当前任务状态下不能修改任务状态!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {
                                if (index == 1) {
                                    item.setStatus(TaskStatusEnum.已取消.getName());
                                } else if (index == 0) {
                                    item.setStatus(TaskStatusEnum.已完成.getName());
                                }
                                saveTask(item);
                            }
                        });

                    }
                });


                LinearLayout ll_support = viewHolder.getView(R.id.ll_item_log_support);
                final LinearLayout ll_comment = viewHolder.getView(R.id.ll_item_log_comment);


                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);

                if (item.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getActivity().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getActivity().getColor(R.color.color_support_text));
                }

                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");


                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getExecutorIds());
                        post.setDataType("任务计划");
                        post.setDataId(item.getUuid());
                        if (item.isLike()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });


                ll_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ll_bottom_comment.setVisibility(View.VISIBLE);
                        et_comment.requestFocus();
                        hideShowSoft();
                    }
                });

                btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = et_comment.getText().toString().trim();
                        if (TextUtils.isEmpty(content)) {
                            Toast.makeText(context, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getExecutorIds());
                        post.setDataType("任务计划");
                        post.setDataId(item.getUuid());
                        post.setContent(content);
                        comment(post, item);
                    }
                });


            }
        };
    }

    /**
     * 保存任务
     */
    private void saveTask(Task task) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务保存;

        StringRequest.postAsyn(url, task, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "修改任务状态成功!", Toast.LENGTH_SHORT).show();
                lv.startRefresh();
                pageIndex = 1;
                getWorkList();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(context, JsonUtils.pareseMessage(result), Toast.LENGTH_SHORT);
            }
        });
    }


    /**
     * 点赞
     *
     * @param post
     */
    private void support(SupportAndCommentPost post, final Task record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLike(true);
                adapter.notifyDataSetChanged();
                ll_bottom_comment.setVisibility(View.GONE);
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
     * @param post 要取消点赞的实体的ID
     */
    private void cancleSupport(SupportAndCommentPost post, final Task record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLike(false);
                adapter.notifyDataSetChanged();
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
     * 评论
     *
     * @param post
     */
    public void comment(SupportAndCommentPost post, final Task record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论;
        et_comment.setText("");
        InputSoftHelper.hiddenSoftInput(context, et_comment);
        ll_bottom_comment.setVisibility(View.GONE);

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "评论成功", Toast.LENGTH_SHORT).show();
                record.setCommentNumber(record.getCommentNumber() + 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

}
