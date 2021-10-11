package com.biaozhunyuan.tianyi.supportAndComment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.AutoMaxHeightViewpager;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;

import java.util.List;

import okhttp3.Request;

/**
 * Created by wangAnMin on 2018/3/6.
 * 通用的点赞列表页面
 */

public class CommentListFragment extends Fragment {

    private NoScrollListView lv;
    private SupportListPost commentPost;
    private DictionaryHelper helper;
    private AutoMaxHeightViewpager viewpager;
    private TextView tv_noData;
    private CommentCountListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            commentPost = (SupportListPost) getArguments().getSerializable("SupportListPost");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_support_comment, null);
        lv = view.findViewById(R.id.lv_common_support);
        tv_noData = view.findViewById(R.id.tv_no_data);
        helper = new DictionaryHelper(getActivity());
        if (viewpager != null) {
            viewpager.setObjectForPosition(view, 1);
        }
        if (commentPost != null) {
            getCommentList(commentPost);
        }
        return view;
    }

    public static CommentListFragment newInstance(SupportListPost post) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("SupportListPost", post);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 设置viewpager
     *
     * @param viewpager
     */
    public void setViewpager(AutoMaxHeightViewpager viewpager) {
        this.viewpager = viewpager;
    }


    /**
     * 获取点赞列表
     *
     * @param post
     */
    private void getCommentList(SupportListPost post) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.评论列表;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<SupportAndCommentPost> list = JsonUtils.ConvertJsonToList(response, SupportAndCommentPost.class);
                lv.setAdapter(getAdapter(list));
                if (list != null) {
                    if (listener != null) {
                        listener.getcount(list.size());
                    }
                    if (list.size() == 0) {
                        tv_noData.setVisibility(View.VISIBLE);
                        tv_noData.setText("暂无评论");
                    } else if (list.size() > 0) {
                        tv_noData.setVisibility(View.GONE);
                    }
                } else {
                    tv_noData.setVisibility(View.VISIBLE);
                    tv_noData.setText("暂无评论");
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

    private CommanAdapter<SupportAndCommentPost> getAdapter(List<SupportAndCommentPost> list) {
        return new CommanAdapter<SupportAndCommentPost>(list, getActivity(), R.layout.item_suppotr_common) {
            @Override
            public void convert(int position, SupportAndCommentPost item, BoeryunViewHolder viewHolder) {
                TextView tv_content = viewHolder.getView(R.id.tv_content_comment);
                tv_content.setVisibility(View.VISIBLE);

                viewHolder.setUserPhoto(R.id.head_item_task_list, item.getFromId());//点赞人头像
                viewHolder.setTextValue(R.id.tv_creater_task_item, helper.getUserNameById(item.getFromId()));//点赞人姓名
                viewHolder.setTextValue(R.id.tv_time_task_item, ViewHelper.convertStrToFormatDateStr(item.getTime(), "MM月dd日 HH:mm"));//点赞时间
                tv_content.setText(item.getContent());  //评论内容
            }
        };
    }


    public interface CommentCountListener {
        void getcount(int count);
    }

    public void setOnCommentCountListener(CommentCountListener listener) {
        this.listener = listener;
    }


    /**
     * 重新加载数据
     */
    public void reload() {
        getCommentList(commentPost);
    }

}
