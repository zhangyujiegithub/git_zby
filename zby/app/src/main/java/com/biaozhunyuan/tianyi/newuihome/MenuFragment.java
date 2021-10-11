package com.biaozhunyuan.tianyi.newuihome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.CommonApplyListActivity;

/**
 * 作者： bohr
 * 日期： 2020-07-03 15:58
 * 描述：中国结算菜单页面
 */
public class MenuFragment extends Fragment {

    private LinearLayout llShowwen; //收文
    private LinearLayout llFawen; //发文
    private LinearLayout llQianbao; //签报
    private LinearLayout llkuadi; //跨地审批
    private LinearLayout llBumen; //部门审批
    private LinearLayout llruanjian; //软件开发

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, null);
        mContext = getActivity();
        initView(view);
        setOnEvent();
        return view;
    }

    private void initView(View view) {
        llShowwen = view.findViewById(R.id.ll_shouwen);
        llFawen = view.findViewById(R.id.ll_fawen);
        llQianbao = view.findViewById(R.id.ll_qianbao);
        llkuadi = view.findViewById(R.id.ll_kuadi);
        llBumen = view.findViewById(R.id.ll_bumen);
        llruanjian = view.findViewById(R.id.ll_ruanjian);
    }

    private void setOnEvent() {
        llShowwen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActiity("收文");
            }
        });

        llFawen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActiity("发文");
            }
        });
    }

    /**
     * 跳转到审批列表页面
     *
     * @param type 类型
     */
    private void startActiity(String type) {
        Intent intent = new Intent(mContext, CommonApplyListActivity.class);
        intent.putExtra("title", type);
        String doUrl = "";
        String unDoUrl = "";
        if ("收文".equals(type)) {
            doUrl = "oa/governmentDocument/receiveManage/getReceiveList?nodeId=-1"; //待办收文
            unDoUrl = "oa/governmentDocument/receiveManage/getReceivedList?filed=-1"; //已办收文
        } else if ("发文".equals(type)) {
            doUrl = "oa/governmentDocument/sendManage/getReceiveList?nodeId=-1"; //待办发文
            unDoUrl = "oa/governmentDocument/sendManage/getFinishedList?filed=-1"; //已办发文
        }
        intent.putExtra("doUrl", doUrl);
        intent.putExtra("unDoUrl", unDoUrl);
        startActivity(intent);
    }
}
