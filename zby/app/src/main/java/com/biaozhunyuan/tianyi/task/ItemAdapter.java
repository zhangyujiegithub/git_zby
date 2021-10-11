package com.biaozhunyuan.tianyi.task;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.view.AvatarImageView;
import com.biaozhunyuan.tianyi.view.bragboard.adapter.VerticalAdapter;
import com.biaozhunyuan.tianyi.view.bragboard.helper.DragHelper;
import com.biaozhunyuan.tianyi.view.bragboard.model.DragColumn;

import java.util.List;

import okhttp3.Request;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/4/3
 * @discription 垂直排列的子项卡片
 * @usage null
 */
public class ItemAdapter extends VerticalAdapter<ItemAdapter.ViewHolder> {

    private static String UPDATE_TASK_CONTENT = "UPADTETASKCONTENT"; //刷新任务内容

    DictionaryHelper helper;
    private Dialog alertDialog;
    private EditText etMsg;
    private TextView status;
    private TextView excutor;
    private TextView begin;
    private TextView over;
    private Button delete;
    private Button save;

    public ItemAdapter(Context context, DragHelper dragHelper, List<DragColumn> oaWorkLaneList) {
        super(context, dragHelper, oaWorkLaneList);
        helper = new DictionaryHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (((String) payloads.get(0)).equals(UPDATE_TASK_CONTENT)) { //刷新任务内容
                WorkScheduleList workScheduleList = getData().get(position);
                holder.item_title.setText(workScheduleList.getContent());
            }
        }
    }

    @Override
    public void onBindViewHolder(Context context, final ViewHolder holder, @NonNull WorkScheduleList item, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragItem(holder); //长按拖动条目
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTaskDiaglog(item, position);
            }
        });
        holder.item_title.setText(item.getContent());

        ImageUtils.displyUserPhotoById(mContext, item.getExecutorIds(),
                holder.iv_head);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        AvatarImageView iv_head;

        public ViewHolder(View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
            iv_head = itemView.findViewById(R.id.iv_head);
        }
    }

    /**
     * 卡片详情/编辑
     * @param item
     * @param position
     */
    private void showTaskDiaglog(WorkScheduleList item, int position) {
        if (alertDialog == null) {
            WindowManager windowManager = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_task_info_board_view, null);
            alertDialog = new Dialog(mContext, R.style.AlertDialogStyle);
            alertDialog.setContentView(view);
            LinearLayout lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
            // 调整dialog背景大小
            lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                    .getWidth() * 0.85), LinearLayout.LayoutParams.WRAP_CONTENT));

            etMsg = view.findViewById(R.id.et_msg);
            status = view.findViewById(R.id.task_info_status);
            excutor = view.findViewById(R.id.task_info_excutor);
            begin = view.findViewById(R.id.task_info_beginTime);
            over = view.findViewById(R.id.task_info_overTime);
            delete = view.findViewById(R.id.btn_neg);
            save = view.findViewById(R.id.btn_pos);
        }
        etMsg.setText(item.getContent());
        etMsg.setSelection(etMsg.getText().toString().length());
        status.setText(getStatusById(item.getStatus()));
        excutor.setText(helper.getUserNamesById(item.getExecutorIds()));
        begin.setText(ViewHelper.convertStrToFormatDateStr(item.getBeginTime(), "yyyy年MM月dd日 HH:mm"));
        over.setText(ViewHelper.convertStrToFormatDateStr(item.getEndTime(), "yyyy年MM月dd日 HH:mm"));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTaskCard(item);
                InputSoftHelper.hideKeyboard(etMsg);
                alertDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                item.setContent(etMsg.getText().toString());
                saveTaskCard(item, position);
                InputSoftHelper.hideKeyboard(etMsg);
            }
        });
        alertDialog.show();
    }

    /**
     * 根据任务状态id获取任务状态名称
     *
     * @param uuid
     * @return
     */
    public String getStatusById(String uuid) {
        String status = "";
        if (TaskStatusEnum.已完成.getName().equals(uuid)) {
            status = TaskStatusEnum.已完成.getStatus();
        } else if (TaskStatusEnum.进行中.getName().equals(uuid)) {
            status = TaskStatusEnum.进行中.getStatus();
        } else if (TaskStatusEnum.已取消.getName().equals(uuid)) {
            status = TaskStatusEnum.已取消.getStatus();
        } else if (TaskStatusEnum.已逾期.getName().equals(uuid)) {
            status = TaskStatusEnum.已逾期.getStatus();
        }
        return status;
    }

    /**
     * 保存卡片
     * @param task
     */
    private void saveTaskCard(WorkScheduleList task, int position) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情保存卡片
                + "?uuid=" + task.getUuid() + "&content=" + task.getContent();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                notifyItemChanged(position, UPDATE_TASK_CONTENT);
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 删除卡片
     * @param task
     */
    private void deleteTaskCard(WorkScheduleList task){
        String url = Global.BASE_JAVA_URL + GlobalMethord.任务看板详情删除卡片 + "?tableName=oa_work_scheduling&ids=" + task.getUuid();
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                getData().remove(task);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseCodeErro(String result) {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
