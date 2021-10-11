package com.biaozhunyuan.tianyi.clue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.BoeryunSearchView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.clue.ClueListInfoActivity.CLUE_LISTINFO_EXTRA;
import static com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity.RESULT_SELECT_USER;
import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_PARTICIPANT;

public class ClueListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private BoeryunSearchView searchButton;
    private PullToRefreshAndLoadMoreListView lv;
    private int pageIndex = 1;
    private CommanAdapter<Clue> adapter;
    private Demand<Clue> demand;
    private Context context;
    private String currentId; //当前选中条目uuid
    public static boolean isResume = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clue_list);
        context = this;
        initView();
        initDemand();
        getList();
        setOnTouchEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isResume){
            pageIndex = 1;
            getList();
            isResume = false;
        }
    }

    private void setOnTouchEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {
                Intent intent = new Intent(context,ClueListInfoActivity.class);
                startActivity(intent);
            }
        });
        searchButton.setOnSearchedListener(new BoeryunSearchView.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                demand.resetFuzzySearchField(true);
                demand.fuzzySearch = str;
                pageIndex = 1;
                getList();
            }
        });
        searchButton.setOnButtonClickListener(new BoeryunSearchView.OnButtonClickListener() {
            @Override
            public void OnCancle() {
                demand.resetFuzzySearchField(false);
                pageIndex = 1;
                getList();
            }

            @Override
            public void OnClick() {

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    Clue item = adapter.getItem(position - 1);
                    Intent intent = new Intent(context,ClueListInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CLUE_LISTINFO_EXTRA,item);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getList();
            }
        });
        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getList();
            }
        });
    }

    private void initDemand() {
        demand = new Demand(Clue.class);
        demand.sortField = "laseUpdateTime desc";
        demand.dictionaryNames = "advisorId.base_staff,source.dict_customer_source,classification.dict_customer_type,inProduct.inv_sku,status.dict_clue_status";
        demand.pageSize = 10;
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.线索列表;
        demand.setFuzzySearch("crm_clue");
    }

    private void getList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                List<Clue> data = demand.data;
                for (final Clue clue : data) {
                    try {
                        clue.setStatusName(demand.getDictName(clue, "status"));
                        clue.setAdvisorName(demand.getDictName(clue,"advisorId"));
                        clue.setSourceName(demand.getDictName(clue,"source"));
                        clue.setClassificationName(demand.getDictName(clue,"classification"));
                        clue.setInProductName(demand.getDictName(clue,"inProduct"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                ProgressDialogHelper.dismiss();
                showShortToast("网络请求数据出错");
            }
        });
    }

    public CommanAdapter<Clue> getAdapter(List<Clue> list) {
        return new CommanAdapter<Clue>(list,context,R.layout.item_clue_list) {
            @Override
            public void convert(int position, Clue item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.advisor_name,item.getAdvisorName()); //创建人
                viewHolder.setUserPhoto(R.id.circleImageView,item.getAdvisorId()); //创建人头像
                viewHolder.setTextValue(R.id.client_name,item.getCompanyName()); //公司名称
                viewHolder.setTextValue(R.id.tv_status,item.getStatusName()); //状态
                viewHolder.setTextValue(R.id.contact_name,item.getExpectMoney()); //预计金额
                viewHolder.setTextValue(R.id.contact_location,item.getLaseUpdateTime()); //最后更新时间
                //分配
                viewHolder.getView(R.id.imageView9).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentId = item.getUuid();
                        Intent intent = new Intent(context, SelectedNotifierActivity.class);
                        intent.putExtra("title", "选择理财师");
                        startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                    }
                });
                //转化
                viewHolder.getView(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = Global.BASE_JAVA_URL + GlobalMethord.线索转化;
                        showDialog(url,"转化为销售机会",item.getUuid());
                    }
                });
                //关闭
                viewHolder.getView(R.id.imageView11).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = Global.BASE_JAVA_URL + GlobalMethord.线索关闭;
                        showDialog(url,"关闭",item.getUuid());
                    }
                });
                //删除
                viewHolder.getView(R.id.imageView15).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = Global.BASE_JAVA_URL + GlobalMethord.线索删除;
                        showDialog(url,"删除",item.getUuid());
                    }
                });
            }
        };
    }

    private void showDialog(String url,String title,String ids){
        AlertDialog dialog = new AlertDialog(context).builder()
                .setTitle(title)
                .setMsg("是否" + title + "?")
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jo = new JSONObject();
                        try {
                            jo.put("ids",ids);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        StringRequest.postAsyn(url,jo, new StringResponseCallBack() {
                            @Override
                            public void onResponse(String response) {
                                showShortToast(JsonUtils.pareseData(response));
                                ProgressDialogHelper.show(context);
                                pageIndex = 1;
                                getList();
                            }

                            @Override
                            public void onFailure(Request request, Exception ex) {

                            }

                            @Override
                            public void onResponseCodeErro(String result) {
                                showShortToast(JsonUtils.pareseData(result));
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.show();
    }

    private void initView() {
        headerView = findViewById(R.id.headerview);
        searchButton = findViewById(R.id.seach_button);
        lv = findViewById(R.id.lv);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_SELECT_PARTICIPANT) {
            Bundle bundle1 = data.getExtras();
            UserList userList1 = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
            String shareClientIds = "";
            if (userList1 != null) {
                List<User> users = userList1.getUsers();
                for (User user : users) {
                    shareClientIds += user.getUuid() + ",";
                }
                if (shareClientIds.length() > 0) {
                    shareClientIds = shareClientIds.substring(0, shareClientIds.length() - 1);
                }
            }
            JSONObject js = new JSONObject();
            try {
                js.put("newAdvisorId",shareClientIds);
                js.put("ids",currentId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringRequest.postAsyn(Global.BASE_JAVA_URL + GlobalMethord.线索分配, js, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    pageIndex = 1;
                    getList();
                    Toast.makeText(context, JsonUtils.pareseData(response),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Request request, Exception ex) {

                }

                @Override
                public void onResponseCodeErro(String result) {
                    Toast.makeText(context,JsonUtils.pareseData(result),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
