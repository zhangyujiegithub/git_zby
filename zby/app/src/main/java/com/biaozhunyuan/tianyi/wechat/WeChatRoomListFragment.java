package com.biaozhunyuan.tianyi.wechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.wechat.model.WeChatRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * 作者： bohr
 * 日期： 2020-06-15 13:48
 * 描述：微信联系人群聊列表
 */
public class WeChatRoomListFragment extends Fragment {
    private ListView lv;

    private Context mContext;
    private String staffId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_listview, null);
        mContext = getActivity();
        initView(view);
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

    public static WeChatRoomListFragment getInstance(String staffId) {
        WeChatRoomListFragment fragment = new WeChatRoomListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("StaffId", staffId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        lv = view.findViewById(R.id.lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeChatRoom item = (WeChatRoom) lv.getItemAtPosition(position);
                Intent intent = new Intent(mContext, WeChatRecordInfoActivity.class);
                intent.putExtra("WeChatId", item.getRoomId());
                intent.putExtra("StaffId", staffId);
                intent.putExtra("WeChatName", item.getName());
                startActivity(intent);
            }
        });
    }

    private void getContactList() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.微信群聊列表;

        Map<String, String> map = new HashMap<>();
        map.put("staffId", staffId);
        StringRequest.postAsyn(url, map, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<WeChatRoom> list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), WeChatRoom.class);
                if (list != null && list.size() > 0) {
                    lv.setAdapter(getAdapter(list));
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

    private CommanAdapter<WeChatRoom> getAdapter(List<WeChatRoom> list) {
        return new CommanAdapter<WeChatRoom>(list, mContext, R.layout.item_we_chat_contact_list) {
            @Override
            public void convert(int position, WeChatRoom item, BoeryunViewHolder viewHolder) {
                viewHolder.getView(R.id.iv_head).setVisibility(View.GONE);
                viewHolder.setTextValue(R.id.tv_name, item.getName());
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
