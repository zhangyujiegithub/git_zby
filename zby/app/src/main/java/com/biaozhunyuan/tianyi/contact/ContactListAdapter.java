package com.biaozhunyuan.tianyi.contact;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.utils.ShapeUtils;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 作者： bohr
 * 日期： 2020-06-02 10:55
 * 描述：
 */
public class ContactListAdapter extends BaseQuickAdapter<Contact, BaseViewHolder> {

    private DictionaryHelper dictionaryHelper;
    private Context mContext;
    private ContactClickListener contactClickListener;


    public ContactListAdapter(int layoutResId, @Nullable List<Contact> data) {
        super(layoutResId, data);
    }

    public void setDicHelper(DictionaryHelper helper) {
        dictionaryHelper = helper;
    }

    public void setContext(Context ontext) {
        mContext = ontext;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Contact item) {
        User user = dictionaryHelper.getUser(item.getAdvisorId());
        helper.setText(R.id.tv_name_contact_item, user.getName());
        helper.setText(R.id.tv_advisor_contact_item, TextUtils.isEmpty(item.getCustomerName())
                ? item.getProjectName() : item.getCustomerName());
        if (StrUtils.pareseNull(item.getContactTime()).contains(" 00:00:00")) {
            helper.setText(R.id.tv_time_contact_item,
                    ViewHelper.getDateStringFormat(item.getContactTime(), "yyyy-MM-dd"));
        } else {
            helper.setText(R.id.tv_time_contact_item,
                    ViewHelper.getDateStringFormat(StrUtils.pareseNull(item.getContactTime()), "yyyy-MM-dd HH:mm"));
        }
        helper.setText(R.id.content_contact_list, item.getContent());
        ImageUtils.displyImageById(user.getAvatar(), helper.getView(R.id.head_item_contact_list));
        MultipleAttachView attachView = helper.getView(R.id.attach_item_contact);

        if (!TextUtils.isEmpty(item.getAttachment())) {
            attachView.setVisibility(View.VISIBLE);
            attachView.loadImageByAttachIds(item.getAttachment());
        } else {
            attachView.setVisibility(View.GONE);
            attachView.loadImageByAttachIds("");
        }


        TextView tvStage = helper.getView(R.id.tv_status_item_contact);

        if (!TextUtils.isEmpty(item.getStageName())) {
            tvStage.setVisibility(View.VISIBLE);
            tvStage.setBackgroundDrawable(ShapeUtils.getRoundedColorDrawable(
                    mContext.getResources().getColor(R.color.hanyaRed),
                    ViewHelper.dip2px(mContext, 5), 0));
            tvStage.setText(item.getStageName());
        } else {
            tvStage.setVisibility(View.GONE);
        }

        //评论
        helper.getView(R.id.ll_item_log_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactClickListener != null) {
                    contactClickListener.onPopWiw(item);
                }
            }
        });

        LinearLayout ll_support = helper.getView(R.id.ll_item_log_support);//点赞
        final ImageView iv_support = helper.getView(R.id.iv_item_log_support);
        final TextView tv_support = helper.getView(R.id.tv_support_count_log_item);
        final TextView tv_comment = helper.getView(R.id.tv_comment_count_log_item);
        //点赞/取消赞
        ll_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportAndCommentPost post = new SupportAndCommentPost();
                post.setFromId(Global.mUser.getUuid());
                post.setToId(item.getCreatorId());
                post.setDataType("联系记录");
                post.setDataId(item.getUuid());
                if (item.isLike()) { //取消点赞
                    if (contactClickListener != null) {
                        contactClickListener.onCancleSupport(post, item);
                    }
                } else { //点赞
                    contactClickListener.onSupport(post, item);
                }
            }
        });


        if (item.isLike()) {
            iv_support.setImageResource(R.drawable.icon_support_select);
//                    tv_support.setTextColor(getActivity().getResources().getColor(R.color.color_support_text_like));
            tv_support.setTextColor(Color.parseColor("#01E0DF"));
        } else {
            iv_support.setImageResource(R.drawable.icon_support);
//                    tv_support.setTextColor(getActivity().getResources().getColor(R.color.color_support_text));
            tv_support.setTextColor(Color.parseColor("#999999"));
        }
        tv_support.setText(item.getLikeNumber() + "");
        tv_comment.setText(item.getCommentNumber() + "");

    }


    interface ContactClickListener {
        void onPopWiw(Contact contact);

        void onCancleSupport(SupportAndCommentPost post, Contact contact);

        void onSupport(SupportAndCommentPost post, Contact contact);
    }

    public void setOnContactClickListener(ContactClickListener listener) {
        contactClickListener = listener;
    }
}
