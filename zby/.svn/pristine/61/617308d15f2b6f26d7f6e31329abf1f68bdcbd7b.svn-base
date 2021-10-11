package com.biaozhunyuan.tianyi.client;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.biaozhunyuan.tianyi.common.view.AlertDialog;

import org.json.JSONException;

import java.util.List;

import okhttp3.Request;

/**
 * 往来文件
 */

public class ClientAttachFragment extends Fragment {

    private static final String PARAM_CLIENT_ID = "PARAM_CLIENT_ID";
    private String mClientId = "";
    private ListView lv;
    private Attach mAttach;
    private Demand<Attach> demand;
    private int pageIndex = 1;
    private String dictionary = "";
    private CommanAdapter<Attach> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getString(PARAM_CLIENT_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_attenach, null);
        initView(v);
        getAttachList();
        setOnTouchEvent();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void setOnTouchEvent() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attach attach = adapter.getDataList().get(position);
                String url = Global.BASE_JAVA_URL + GlobalMethord.下载往来文件 + attach.getAttachments();
                isDownLoadDialog(url, attach.getName());
            }
        });
    }


    public void getAttachList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.客户往来文件 + mClientId;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Attach> data = JsonUtils.jsonToArrayEntity(JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), Attach.class);
                    adapter = getAdapter(data);
                    lv.setAdapter(adapter);
                } catch (JSONException e) {
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

    private CommanAdapter<Attach> getAdapter(List<Attach> list) {
        return new CommanAdapter<Attach>(list, getActivity(), R.layout.fragment_client_list_attach) {
            @Override
            public void convert(int position, Attach item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_attach_name, item.getName());
                viewHolder.setTextValue(R.id.tv_attach_time, item.getCreateTime());
            }
        };
    }

    private void initView(View v) {
        lv = v.findViewById(R.id.lv);
    }

    private void isDownLoadDialog(final String downUrl, final String fileName) {
        final AlertDialog dialog = new AlertDialog(getActivity());
        dialog.builder();
        dialog.setTitle("产品附件");
        dialog.setMsg("是否下载产品附件?");
        dialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "后台开始下载..", Toast.LENGTH_SHORT).show();
                dialog.dissMiss();
                StringRequest.downloadFile(downUrl, fileName, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), "下载成功,请前往SD卡根目录查看 :" + fileName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Request request, Exception ex) {
                        Toast.makeText(getActivity(), "下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponseCodeErro(String result) {

                    }
                });

            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dissMiss();
            }
        });
        dialog.show();
    }


    public static ClientAttachFragment newInstance(String Id) {
        ClientAttachFragment fragment = new ClientAttachFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CLIENT_ID, Id);
        fragment.setArguments(args);
        return fragment;
    }
}
