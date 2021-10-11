package com.biaozhunyuan.tianyi.wechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.wechat.model.WeChatContact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-06-15 13:48
 * 描述：微信联系人单聊列表
 */
public class WeChatContactListFragment extends Fragment {

    private ListView lv;

    private Context mContext;
    private String staffId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_listview, null);
        initView(view);
        mContext = getActivity();
        getContactList();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            staffId = getArguments().getString("StaffId");
        }
    }

    private void initView(View view) {
        lv = view.findViewById(R.id.lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeChatContact item = (WeChatContact) lv.getItemAtPosition(position);
                Intent intent = new Intent(mContext, WeChatRecordInfoActivity.class);
                intent.putExtra("WeChatId", item.getWechatId());
                intent.putExtra("StaffId", staffId);
                intent.putExtra("WeChatName", TextUtils.isEmpty(item.getRemark())
                        ? item.getNickName() : item.getRemark());
                startActivity(intent);
            }
        });
    }

    public static WeChatContactListFragment getInstance(String staffId) {
        WeChatContactListFragment fragment = new WeChatContactListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("StaffId", staffId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void getContactList() {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.微信联系人列表;

        Map<String, String> map = new HashMap<>();
        map.put("staffId", staffId);
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<WeChatContact> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), WeChatContact.class);
                if (list != null && list.size() > 0) {
                    lv.setAdapter(getAdapter(list));
                }
                ProgressDialogHelper.dismiss();
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


    private CommanAdapter<WeChatContact> getAdapter(List<WeChatContact> list) {
        return new CommanAdapter<WeChatContact>(list, mContext, R.layout.item_we_chat_contact_list) {
            @Override
            public void convert(int position, WeChatContact item, BoeryunViewHolder viewHolder) {
                ImageUtils.displyImage(item.getCustomerIcon(), viewHolder.getView(R.id.iv_head));
                viewHolder.setTextValue(R.id.tv_name, TextUtils.isEmpty(item.getRemark())
                        ? item.getNickName() : item.getRemark());
                if (item.getMsgTotalCount() <= 0) {
                    viewHolder.setTextValue(R.id.tv_count, "");
                } else {
                    viewHolder.setTextValue(R.id.tv_count,
                            "(" + StrUtils.pareseNull(item.getMsgTotalCount() + "") + ")");
                }
            }
        };
    }
}
