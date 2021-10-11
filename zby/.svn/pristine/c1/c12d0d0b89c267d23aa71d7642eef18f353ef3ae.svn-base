package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.models.DeptTreeModel;
import com.biaozhunyuan.tianyi.models.StaffModel;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-07-15 14:14
 * 描述：中国结算通讯录列表
 */
public class AddressListFragment extends Fragment {

    private LinearLayout llDept;
    private TextView tvDept;
    private RelativeLayout rlTitle;
    private ListView lvParent;
    private LinearLayout llRoot;
    private LinearLayout llDeptName;
    private ListView lvChild;
    private PullToRefreshAndLoadMoreListView lv;

    private Context mContext;
    private DictIosPickerBottomDialog dialog;
    private Demand<StaffModel> demand;
    private String deptId = "";
    private int pageIndex = 1;
    private CommonPopupWindow deptPop;
    private List<DeptTreeModel> deptTreeModels;
    private List<StaffModel> staffModels;
    private CommanAdapter<DeptTreeModel> parentAdapter;
    private CommanAdapter<DeptTreeModel> childAdapter;
    private DeptTreeModel lastSelectPatent;
    private DeptTreeModel lastSelectChild;
    private CommanAdapter<StaffModel> adapter;

    private String department = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_list, null);
        initData();
        initViews(view);
        getDeptTree(false);
        setOnEvent();
        return view;
    }

    private void initData() {
        mContext = getActivity();
        staffModels = new ArrayList<>();
        dialog = new DictIosPickerBottomDialog(getActivity());
        initDemand();
    }

    private void initDemand() {
        demand = new Demand<>(StaffModel.class);
        demand.pageSize = 20;
        demand.pageIndex = 1;
    }


    private void initViews(View view) {
        llDept = view.findViewById(R.id.ll_dept);
        tvDept = view.findViewById(R.id.tv_dept);
        llRoot = view.findViewById(R.id.ll_root);
        llDeptName = view.findViewById(R.id.ll_dept_name);
        rlTitle = view.findViewById(R.id.rl_title);
        lv = view.findViewById(R.id.lv);
    }

    private void setOnEvent() {
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deptTreeModels == null) {
                    getDeptTree(true);
                } else {
                    if (deptPop != null) {
                        deptPop.showAsDropDown(rlTitle);
                    } else {
                        initPop();
                    }
                }
            }
        });

        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getStaffByDeptId(deptId);
            }
        });

        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getStaffByDeptId(deptId);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StaffModel item = adapter.getItem(position - 1);
                Intent intent = new Intent(mContext, StaffInfoActivity.class);
                intent.putExtra("StaffInfo", item);
                intent.putExtra("department", department);
                startActivity(intent);
            }
        });
    }


    private void initPop() {
        deptPop = new CommonPopupWindow.Builder(mContext)
                //设置PopupWindow布局
                .setView(R.layout.popup_spacelist)
                //设置宽高
                .setWidthAndHeight((ViewGroup.LayoutParams.MATCH_PARENT),
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                //设置动画
                .setAnimationStyle(R.style.AnimDown)
                //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                .setBackGroundLevel(1.0f)
                //设置PopupWindow里的子View及点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        lvParent = view.findViewById(R.id.lv_one);
                        lvChild = view.findViewById(R.id.lv_two);

                        parentAdapter = getParentAdapter(deptTreeModels);
                        childAdapter = getChildAdapter(new ArrayList<>());

                        lvParent.setAdapter(parentAdapter);
                        lvChild.setAdapter(childAdapter);

                        lvParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                DeptTreeModel item = parentAdapter.getItem(position);
                                if (lastSelectPatent != null) {
                                    lastSelectPatent.setSelect(false);
                                }
                                item.setSelect(true);
                                lastSelectPatent = item;
                                parentAdapter.notifyDataSetChanged();
                                if (null == item.getChildren()) {
                                    if (lastSelectChild != null) {
                                        lastSelectChild.setSelect(false);
                                    }
                                    item.setSelect(true);
                                    lastSelectChild = item;
                                    displayDeptLevel();
                                    tvDept.setText(item.getText());
                                    childAdapter.notifyDataSetChanged();
                                    pageIndex = 1;
                                    deptId = item.getId();
                                    deptPop.dismiss();
                                    getStaffByDeptId(item.getId());

                                } else {
                                    childAdapter.addBottom(item.getChildren(), true);
                                }
                            }
                        });

                        lvChild.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                DeptTreeModel item = childAdapter.getItem(position);
                                if (lastSelectChild != null) {
                                    lastSelectChild.setSelect(false);
                                }
                                item.setSelect(true);
                                lastSelectChild = item;
                                displayDeptLevel();
                                tvDept.setText(item.getText());
                                childAdapter.notifyDataSetChanged();
                                pageIndex = 1;
                                deptId = item.getId();
                                deptPop.dismiss();
                                department = item.getText();
                                getStaffByDeptId(item.getId());

                            }
                        });
                    }
                }).create();
        deptPop.showAsDropDown(rlTitle);
    }

    private void displayDeptLevel() {
        llDeptName.removeAllViews();
        TextView textView = new TextView(mContext);
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(R.color.fuzhuselan));
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        textView.setLayoutParams(lp);*/
        if (lastSelectChild != null) {
            textView.setText(lastSelectPatent.getText() + "  ->  " + lastSelectChild.getText());
        } else {
            textView.setText(lastSelectPatent.getText());
        }
        llDeptName.addView(textView);
    }


    private CommanAdapter<DeptTreeModel> getParentAdapter(List<DeptTreeModel> list) {
        return new CommanAdapter<DeptTreeModel>(list, mContext, R.layout.item_dept_tree_parent) {
            @Override
            public void convert(int position, DeptTreeModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, item.getText());
                if (item.isSelect()) {
                    viewHolder.setBackgroundColor(Color.parseColor("#ecedf1"));
                } else {
                    viewHolder.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };
    }

    private CommanAdapter<DeptTreeModel> getChildAdapter(List<DeptTreeModel> list) {
        return new CommanAdapter<DeptTreeModel>(list, mContext, R.layout.item_dept_tree_child) {
            @Override
            public void convert(int position, DeptTreeModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, item.getText());
                if (item.isSelect()) {
                    viewHolder.setBackgroundColor(Color.parseColor("#ecedf1"));
                } else {
                    viewHolder.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        };
    }

    private CommanAdapter<StaffModel> getUserAdapter(List<StaffModel> list) {
        return new CommanAdapter<StaffModel>(list, getActivity(), R.layout.item_workmate_list) {

            @Override
            public void convert(int position, final StaffModel item, BoeryunViewHolder viewHolder) {
                List<String> showList = new ArrayList<String>();

                viewHolder.setTextValue(R.id.name_item_workmate, item.getName());
                TextView tvPosition = viewHolder.getView(R.id.position_item_workmate);//职位
                if (TextUtils.isEmpty(item.getStation())) {
                    tvPosition.setVisibility(View.GONE);
                } else {
                    tvPosition.setVisibility(View.VISIBLE);
                    tvPosition.setText(StrUtils.removeSpace(item.getStation()));
                }
                viewHolder.setUserPhotoById("", item.getName(), R.id.head_item_workmate);
                viewHolder.setTextValue(R.id.landline_item_workmate, TextUtils.isEmpty(item.getTelephone()) ? "无" : item.getTelephone());
                viewHolder.setTextValue(R.id.tel_item_workmate, TextUtils.isEmpty(item.getMobilePhone()) ? "无" : item.getMobilePhone());
                if (!TextUtils.isEmpty(item.getTelephone())) {
                    showList.add(item.getTelephone());
                }
//                if (!TextUtils.isEmpty(item.getMobilePhone())) {
//                    showList.add(item.getMobilePhone());
//                }


                ImageView iv_message = viewHolder.getView(R.id.iv_send_message);

                ImageView iv_call = viewHolder.getView(R.id.iv_call_phone_workmate);

                iv_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showList.size() == 0) {
                            Toast.makeText(getActivity(), "没有联系方式", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.setTitle("发短信给" + item.getName());
                            dialog.show(showList);
                            dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                @Override
                                public void onSelected(int index) {
                                    String num = showList.get(index - 1);
                                    Uri smsToUri = Uri.parse("smsto:" + num);
                                    Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });


                //弹出打电话的弹出框
                iv_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showList.size() == 0) {
                            Toast.makeText(getActivity(), "没有联系方式", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.setTitle("联系" + item.getName());
                            dialog.show(showList);

                            dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                                @Override
                                public void onSelected(int index) {
                                    if (index != 0) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:" + showList.get(index - 1)));
                                        //添加到最近联系人
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        };
    }

    /**
     * 获取部门树
     */
    private void getDeptTree(boolean isShowPop) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.部门树;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                deptTreeModels = JsonUtils.jsonToArrayEntity(result, DeptTreeModel.class);
                if (deptTreeModels != null && deptTreeModels.size() > 0) {
                    lastSelectPatent = deptTreeModels.get(0);
//                    tvDept.setText(lastSelectPatent.getText());
//                    getStaffByDeptId(lastSelectPatent.getId());
                    for (int i = 0; i < deptTreeModels.size(); i++) {
                        if (deptTreeModels.get(i).getAttributes() != null && deptTreeModels.get(i).getAttributes().equals(Global.mUser.getDepartmentId())) {
                            lastSelectPatent = deptTreeModels.get(i);
                            department = deptTreeModels.get(i).getText();
                        }
                        if (deptTreeModels.get(i).getChildren() != null && deptTreeModels.get(i).getChildren().size() > 0) {
                            for (int j = 0; j < deptTreeModels.get(i).getChildren().size(); j++) {
                                if (deptTreeModels.get(i).getChildren().get(j).getAttributes() != null &&
                                        deptTreeModels.get(i).getChildren().get(j).getAttributes().equals(Global.mUser.getDepartmentId())) {
                                    lastSelectPatent = deptTreeModels.get(i).getChildren().get(j);
                                    department =  deptTreeModels.get(i).getChildren().get(j).getText();
                                }
                            }
                        }
                    }
                    tvDept.setText(lastSelectPatent.getText());
                    deptId = lastSelectPatent.getId();
                    getStaffByDeptId(lastSelectPatent.getId());
                    displayDeptLevel();
                }
                if (isShowPop) {
                    initPop();
                }
            }
        });
    }


    /**
     * 根据部门id获取员工
     *
     * @param deptId 部门id
     */
    private void getStaffByDeptId(String deptId) {
        demand.src = Global.BASE_JAVA_URL + GlobalMethord.获取部门员工 + "?departmentId=" + deptId;
        demand.pageIndex = pageIndex;
        if (pageIndex == 1) {
            lv.startRefresh();
        }
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<StaffModel> data = demand.data;
                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getUserAdapter(data);
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
}
