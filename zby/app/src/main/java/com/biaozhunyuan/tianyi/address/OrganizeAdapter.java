package com.biaozhunyuan.tianyi.address;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.model.user.Organize;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;

import java.util.ArrayList;
import java.util.List;

public class OrganizeAdapter extends RecyclerView.Adapter<OrganizeAdapter.BaseAdapter> {

    private List<Organization> datas;
    private Context mContext;
    private OnItemClickListener itemClickListener;
    private DictIosPickerBottomDialog dialog;
    private DictionaryHelper helper;
    private boolean isShowWeChatRecord = false;

    public OrganizeAdapter(Context context, List<Organization> organizations) {
        datas = organizations;
        mContext = context;
        helper = new DictionaryHelper(mContext);
        dialog = new DictIosPickerBottomDialog(mContext);
    }

    public void setIsShowWeChatRecord(boolean isShowWeChatRecord) {
        this.isShowWeChatRecord = isShowWeChatRecord;
    }

    public void replaceAll(List<Organization> list) {
        datas.clear();
        if (list != null && list.size() > 0) {
            datas.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public OrganizeAdapter.BaseAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new organizeAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organize, parent, false));
            case 2:
                return new userAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate_list, parent, false));
        }
        return null;
    }

    public class BaseAdapter extends RecyclerView.ViewHolder {

        public BaseAdapter(View itemView) {
            super(itemView);
        }

        void setData(int position, Object object) {

        }
    }

    @Override
    public void onBindViewHolder(OrganizeAdapter.BaseAdapter holder, int position) {
        holder.setData(position, datas.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getDataType();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    /**
     * 员工类型holder
     */
    private class userAdapter extends BaseAdapter {

        private AvatarImageView ivHead;
        private TextView tvName;
        private TextView tvTel;
        private TextView tvPosition;
        private ImageView ivCall;
        private ImageView ivMessage;
        private ConstraintLayout root;

        public userAdapter(View itemView) {
            super(itemView);

            ivHead = itemView.findViewById(R.id.head_item_workmate);
            tvName = itemView.findViewById(R.id.name_item_workmate);
            tvTel = itemView.findViewById(R.id.tel_item_workmate);
            tvPosition = itemView.findViewById(R.id.position_item_workmate);
            ivCall = itemView.findViewById(R.id.iv_call_phone_workmate);
            ivMessage = itemView.findViewById(R.id.iv_send_message);
            root = itemView.findViewById(R.id.root);


            if (isShowWeChatRecord) {
                ivMessage.setVisibility(View.GONE);
                ivCall.setVisibility(View.GONE);
                itemView.findViewById(R.id.imageView8).setVisibility(View.GONE);
            }
        }

        @Override
        void setData(int position, Object object) {
            super.setData(position, object);
            List<String> showList = new ArrayList<>();
            Organization organization = (Organization) object;
            User item = organization.getUser();

            tvName.setText(item.getName());
            if (TextUtils.isEmpty(item.getPost())) {
                tvPosition.setVisibility(View.GONE);
            } else {
                tvPosition.setVisibility(View.VISIBLE);
                tvPosition.setText(StrUtils.removeSpace(item.getPost()));
            }
            ImageUtils.displyUserPhotoById(mContext, item.getUuid(), ivHead);


            if (Global.CURRENT_CROP_NAME.equals("天立化工")) {
                if (!TextUtils.isEmpty(item.getPhoneExt())) {
                    tvTel.setText(item.getPhoneExt());
                    if (!TextUtils.isEmpty(item.getPhoneExt())) {
                        showList.add(item.getPhoneExt());
                    }
                } else {
                    if (!TextUtils.isEmpty(item.getMobile())) {
                        showList.add(item.getMobile());
                    }
                    tvTel.setText(TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                }
            } else {
                tvTel.setText(TextUtils.isEmpty(item.getMobile()) ? "无" : item.getMobile());
                if (!TextUtils.isEmpty(item.getPhoneExt())) {
                    showList.add(item.getPhoneExt());
                }
                if (!TextUtils.isEmpty(item.getMobile())) {
                    showList.add(item.getMobile());
                }
            }


            ivMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showList.size() == 0) {
                        Toast.makeText(mContext, "没有联系方式", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.setTitle("发短信给" + item.getName());
                        dialog.show(showList);
                        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                            @Override
                            public void onSelected(int index) {

                                String num = showList.get(index - 1);
                                Uri smsToUri = Uri.parse("smsto:" + num);
                                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                                mContext.startActivity(intent);
                                helper.insertLatest(item);

                            }
                        });
                    }
                }
            });


            //弹出打电话的弹出框
            ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setTitle("联系" + item.getName());
                    dialog.show(showList);

                    dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                        @Override
                        public void onSelected(int index) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + showList.get(index - 1)));
                            helper.insertLatest(item);
                            //添加到最近联系人
                            mContext.startActivity(intent);
                        }
                    });
                }
            });


            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position, organization);
                    }
                }
            });
        }
    }

    /**
     * 部门类型holder
     */
    private class organizeAdapter extends BaseAdapter {
        private TextView tvName;
        private TextView tvCount;
        private LinearLayout root;

        public organizeAdapter(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_item_organize);
            tvCount = itemView.findViewById(R.id.tv_staff_count_item_organize);
            root = itemView.findViewById(R.id.root);
        }

        @Override
        void setData(int position, Object object) {
            super.setData(position, object);

            Organization organization = (Organization) object;
            Organize organize = organization.getOrganize();

            tvName.setText(organize.getName());
            tvCount.setText("(" + organize.getStaffNumber() + ")");


            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position, organization);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, Organization ogz);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }
}
