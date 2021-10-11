package com.biaozhunyuan.tianyi.apply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.AuditSelectDeptModel;
import com.biaozhunyuan.tianyi.apply.model.PositionStaffModel;
import com.biaozhunyuan.tianyi.apply.model.WorkflowInstance;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.ToastUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.SinglePicker;
import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-07-06 17:45
 * 描述：中国结算审批选择下一步节点页面
 */
public class AuditSelectNextNodeActivity extends BaseActivity {

    private BoeryunHeaderView header;
    private LinearLayout llSelectDept;
    private LinearLayout llSelectPostion;
    private LinearLayout llSelectUser;
    private TextView tvDept;
    private TextView tvPosition;
    private ListView lv;

    private Context mContext;
    private int nodeId;
    private String workflowTemplateId;
    private String workflowId;
    private String formDataId;
    private String yiJianType;
    private boolean isAudite;
    private List<AuditSelectDeptModel> deptModelList;
    private SinglePicker deptPicker;
    private SinglePicker posPicker;
    private AuditSelectDeptModel selectDeptModel;
    private List<字典> positionList;
    private 字典 selectPosition;
    private CommanAdapter<PositionStaffModel> adapter;
    private List<PositionStaffModel> selectStaff = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audite_next_node);
        initView();
        getIntentData();
        getFormAuditeDept(false);
        setEvent();
    }

    private void initView() {
        llSelectDept = findViewById(R.id.ll_select_Dept);
        llSelectPostion = findViewById(R.id.ll_select_postion);
        llSelectUser = findViewById(R.id.ll_select_user);
        tvDept = findViewById(R.id.tv_dept);
        header = findViewById(R.id.header);
        tvPosition = findViewById(R.id.tv_position);
        lv = findViewById(R.id.lv);

        deptPicker = new SinglePicker(this, new ArrayList<AuditSelectDeptModel>());
        deptPicker.setCancelable(false);
        posPicker = new SinglePicker(this, new ArrayList<字典>());
        posPicker.setCancelable(false);

        positionList = new ArrayList<>();
        字典 sort = new 字典();
        sort.setName("顺序审批");
        sort.setUuid("");
        字典 concurrently = new 字典();
        concurrently.setName("同时审批 ");
        concurrently.setUuid("");
        positionList.add(0, sort);
        positionList.add(1, concurrently);
        posPicker.setItems(positionList);
        selectPosition = positionList.get(0);
        tvPosition.setText(selectPosition.getName());
    }

    private void getIntentData() {
        mContext = AuditSelectNextNodeActivity.this;
        nodeId = getIntent().getIntExtra("nodeId", 0);
        workflowTemplateId = getIntent().getStringExtra("workflowTemplateId");
        workflowId = getIntent().getStringExtra("workflowId");
        formDataId = getIntent().getStringExtra("formDataId");
        yiJianType = getIntent().getStringExtra("yiJianType");
        isAudite = getIntent().getBooleanExtra("isAudite", false);
    }

    private void setEvent() {
        header.setmButtonClickRightListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (selectStaff.size() == 0 && selectDeptModel.getType() != 5) {
                    ToastUtils.showShort("未选择人员");
                    return;
                }
                returnSelectStaff();
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

        llSelectDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deptModelList != null && deptModelList.size() > 0) {
                    deptPicker.setItems(deptModelList);
                    for (AuditSelectDeptModel model : deptModelList) {
                        if (model.getDefaultNode().equals(model.getNodeId() + "")) { //设置默认选中项
                            deptPicker.setSelectedItem(model);
                        }
                    }
                    deptPicker.show();
                } else {
                    getFormAuditeDept(true);
                }
            }
        });

        deptPicker.setOnItemPickListener(new SinglePicker.OnItemPickListener() {
            @Override
            public void onItemPicked(int index, Object item) {
                selectStaff.clear();
                selectDeptModel = (AuditSelectDeptModel) item;
                tvDept.setText(selectDeptModel.getOperatorName());
                getStaffLIst();
//                getSelectPosition(false);
            }
        });

        llSelectPostion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positionList != null && positionList.size() > 0) {
                    posPicker.setItems(positionList);
                    posPicker.show();
                } else {
                    getStaffLIst();
                }
            }
        });

        posPicker.setOnItemPickListener(new SinglePicker.OnItemPickListener() {
            @Override
            public void onItemPicked(int index, Object item) {
                selectPosition = (字典) item;
                tvPosition.setText(selectPosition.name);
//                getStaffByPost();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PositionStaffModel item = (PositionStaffModel) lv.getItemAtPosition(position);
                item.setSelect(!item.isSelect());
                if (item.isSelect()) {
                    selectStaff.add(item);
                } else {
                    selectStaff.remove(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void returnSelectStaff() {
        String auditorIds = "";
        for (PositionStaffModel model : selectStaff) {
            auditorIds += model.getId() + ",";
        }
        if (auditorIds.length() > 0) {
            auditorIds = auditorIds.substring(0, auditorIds.length() - 1);
        }
        Intent intent = new Intent(AuditSelectNextNodeActivity.this, FormInfoActivity.class);
        intent.putExtra("auditorIds", auditorIds);
        intent.putExtra("NodeSelectId", selectDeptModel.getNodeId() + "");
        intent.putExtra("NodeName", selectDeptModel.getName());
        if (1 == selectDeptModel.getIsSameTime()) {
            intent.putExtra("shenPiShunXu", "同时审批");
        } else {
            intent.putExtra("shenPiShunXu", "顺序审批");
        }
        intent.putExtra("isAudite", isAudite);
        intent.putExtra("nodeType", selectDeptModel.getType());
        setResult(RESULT_OK, intent);
        finish();
    }


    /**
     * 获取选择出口的数据
     */
    private void getFormAuditeDept(boolean isShow) {
        if (TextUtils.isEmpty(workflowId)) {
            getWorkFlowIdAndAuditeInfo(isShow);
        } else {
            getFormAuditeInfo(isShow);
        }
    }

    private void getFormAuditeInfo(boolean isShow) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.表单审批选择出口;

        Map<String, String> map = new HashMap<>();
        map.put("type", isAudite ? "2" : "1");// 提交的时候=1，审核的时候=2
        map.put("workflowTemplateId", workflowTemplateId);
        map.put("workflowId", workflowId);
        if (isAudite) {
            map.put("nodeId", nodeId % 1000 + "");
            map.put("yiJianType", yiJianType);
        }


//        workflowId: d422a6e0984044d3bbbfa03adabf6bb0
//        workflowTemplateId: 9c2558a510a042778e6adc6c36bb9113
//        nodeId: 6

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                deptModelList = JsonUtils.jsonToArrayEntity
                        (JsonUtils.pareseData(response), AuditSelectDeptModel.class);
                if (deptModelList != null && deptModelList.size() > 0) {
                    deptPicker.setItems(deptModelList);
                    for (AuditSelectDeptModel model : deptModelList) {
                        //设置默认选中项
                        if (model.getDefaultNode().equals(model.getNodeId() + "")) {
                            deptPicker.setSelectedItem(model);
                            selectDeptModel = model;
                        }
                    }
                    if (selectDeptModel == null) {
                        selectDeptModel = deptModelList.get(0);
                    }
                    if (!isShow) {
                        tvDept.setText(selectDeptModel.getOperatorName());
                        if (isAudite) {
                            getStaffLIst();
                        } else {
                            getWorkFlowId();
                        }
                    } else {
                        deptPicker.show();
                    }
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


    private void getWorkFlowIdAndAuditeInfo(boolean isShow) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取workFlowId;

        Map<String, String> map = new HashMap<>();
        map.put("formDataId", formDataId);
        map.put("templateId", workflowTemplateId);
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    WorkflowInstance workflowInstance = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), WorkflowInstance.class);
                    workflowId = workflowInstance.uuid;
                    getFormAuditeInfo(isShow);
                } catch (ParseException e) {
                    e.printStackTrace();
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




    private void getWorkFlowId() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取workFlowId;

        Map<String, String> map = new HashMap<>();
        map.put("formDataId", formDataId);
        map.put("templateId", workflowTemplateId);
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    WorkflowInstance workflowInstance = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), WorkflowInstance.class);
                    workflowId = workflowInstance.uuid;
                    getStaffByPost(selectDeptModel);
                } catch (ParseException e) {
                    e.printStackTrace();
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


    /**
     * 获取岗位
     */
    private void getSelectPosition(boolean isShow) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Map<String, String> map = new HashMap<>();
        map.put("dictionaryName", "base_position");
        map.put("filter", "find_in_set(uuid,'" + selectDeptModel.getStaffSelectValue() + "')");

        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                if ("岗位".equals(deptModelList.get(0).getStaffSelectType())) {
                    getStaffByPost(deptModelList.get(0).getStaffSelectValue());
                } else {
                    getStaffByPost(deptModelList.get(0));
                }
                List<字典> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                if (list != null && list.size() > 0) {
                    String Uuid = list.get(0).getUuid();
                    if (isShow) {
                        posPicker.show();
                    } else {
                        selectPosition = positionList.get(0);
                        tvPosition.setText(selectPosition.name);
                    }
                }
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                if ("岗位".equals(deptModelList.get(0).getStaffSelectType())) {
                    getStaffByPost(deptModelList.get(0).getStaffSelectValue());
                } else {
                    getStaffByPost(deptModelList.get(0));
                }
            }
        });
    }

    private void getStaffLIst() {
        //办结节点不用获取员工
        if (!"5".equals(selectDeptModel.getType() + "")) {
            if ("岗位".equals(selectDeptModel.getStaffSelectType())) {
                getStaffByPost(selectDeptModel.getStaffSelectValue());
            } else {
                getStaffByPost(selectDeptModel);
            }
        }

    }

    /**
     * 获取岗位员工
     */
    private void getStaffByPost(String Uuid) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取岗位员工 + "?postId=" + Uuid + "&name=";

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<PositionStaffModel> staff = JsonUtils.jsonToArrayEntity
                        (JsonUtils.pareseData(response), PositionStaffModel.class);
                if (staff != null) {

                    if (staff.size() == 1 && staff.get(0) != null) {
                        staff.get(0).setSelect(true);
                        selectStaff.add(staff.get(0));
                        if (deptModelList != null && deptModelList.size() == 1) {
                            returnSelectStaff();
                        }
                    }

                    adapter = getAdapter(staff);
                    lv.setAdapter(adapter);
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


    /**
     * 获取岗位员工
     */
    private void getStaffByPost(AuditSelectDeptModel dict) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取岗位员工2;

        Map<String, String> map = new HashMap<>();
        map.put("workflowId", workflowId);
        map.put("type", dict.getStaffSelectType());
        map.put("value", dict.getStaffSelectValue());
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<PositionStaffModel> staff = JsonUtils.jsonToArrayEntity
                        (JsonUtils.pareseData(response), PositionStaffModel.class);
                if (staff != null) {
                    if (staff.size() == 1 && staff.get(0) != null) {
                        staff.get(0).setSelect(true);
                        selectStaff.add(staff.get(0));
                        if (deptModelList != null && deptModelList.size() == 1) {
                            returnSelectStaff();
                        }
                    }
                    adapter = getAdapter(staff);
                    lv.setAdapter(adapter);
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

    private CommanAdapter<PositionStaffModel> getAdapter(List<PositionStaffModel> list) {
        return new CommanAdapter<PositionStaffModel>(list, mContext, R.layout.item_select_user) {
            @Override
            public void convert(int position, PositionStaffModel item, BoeryunViewHolder viewHolder) {
                if (item.isSelect()) {
                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_select);
                } else {
                    viewHolder.setImageResoure(R.id.choose_item_select_user, R.drawable.ic_un_select);
                }
                viewHolder.getView(R.id.head_item_select_user).setVisibility(View.GONE);
                viewHolder.setTextValue(R.id.name_item_select_user, item.getName());
            }
        };
    }
}
