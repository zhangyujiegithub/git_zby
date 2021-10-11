package com.biaozhunyuan.tianyi.common.view;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;

import java.util.List;


public class AlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    public EditText et_msg;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showListMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean isShowEdit = false;
    private boolean isShowDropEt = false;
    private onClickListener onClickListener;
    public static SpinnerEditText spinnerEditText;
    private ListView lv;

    //    private static List<BaseBean> baseBeans = new ArrayList<>();
    public AlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alertdialog, null);

        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        txt_msg.setVisibility(View.GONE);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        et_msg = view.findViewById(R.id.et_dialog);
        btn_neg.setVisibility(View.GONE);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        btn_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);
        lv = view.findViewById(R.id.lv_dialog);
        lv.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

        spinnerEditText = view.findViewById(R.id.et_spinner);
        spinnerEditText.setVisibility(View.GONE);

        spinnerEditText.setRightCompoundDrawable(R.drawable.vector_drawable_arrowdown);

        spinnerEditText.setOnItemClickListener(new SpinnerEditText.OnItemClickListener() {
            @Override
            public void onItemClick(String t, SpinnerEditText var1, View var2, int position, long var4, String selectContent) {

            }
        });

        return this;
    }


    public AlertDialog setTitleGravity(int gravity) {
        txt_title.setGravity(gravity);
        return this;
    }

    public AlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            txt_title.setText("标题");
        } else {
            txt_title.setText(title);
        }
        return this;
    }

    public AlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_msg.setText("内容");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    public AlertDialog setListMsg(List<String> list) {
        showListMsg = true;
        lv.setAdapter(new ArrayAdapter<>(context,R.layout.item_alertdialog_list,list));
        return this;
    }
//    public static void getMessage(int index){
//
//        final List<String> strList = new ArrayList<>();
//        String url = Global.BASE_JAVA_URL+ GlobalMethord.审批语句 + "&pageIndex="+index;
//        StringRequest.getAsyn(url, new StringResponseCallBack() {
//            @Override
//            public void onResponse(String response) {
//                baseBeans = JsonUtils.pareseJsonToList(JsonUtils.pareseData(response), BaseBean.class);
//                for (int i=0;i<baseBeans.size();i++){
//                    strList.add(baseBeans.get(i).getName());
//                }
//                spinnerEditText.setNeedShowSpinner(true);
//                spinnerEditText.setList(strList);
//                spinnerEditText.setSelection(0);
//            }
//
//            @Override
//            public void onFailure(Request request, Exception ex) {
//
//            }
//
//            @Override
//            public void onResponseCodeErro(String result) {
//
//            }
//        });
//        //设置根据文本是否为空判断是否异常
//        spinnerEditText.autoCheckStatusByTextIsEmpty(true);
//    }

    public AlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public AlertDialog setPositiveButton(String text,
                                         final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setPositiveButton(String text,
                                         final onClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_msg.getText().toString().trim();
                listener.onClick(v, msg);
                dialog.dismiss();
            }
        });
        return this;
    }

    public AlertDialog setNegativeButton(String text,
                                         final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }


    /**
     * 设置输入框是否可用
     *
     * @param isEnable
     */
    public void setEditTextEnable(boolean isEnable) {
        isShowEdit = isEnable;
    }

    /**
     * 设置输入框中文字
     * @param text 要设置的文字
     */
    public void setEditTextString(String text) {
        et_msg.setText(text);
    }

    /**
     * 设置输入框中提示语
     * @param text 提示文字
     */
    public void setEditHintString(String text) {
        et_msg.setHint(text);
    }

    /**
     * 设置可选择的输入框是否可用
     *
     * @param isEnable
     */
    public void setDropEditTextEnable(boolean isEnable) {
        isShowDropEt = isEnable;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("提示");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        if(showListMsg){
            txt_msg.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }

        if (isShowDropEt) {
            spinnerEditText.setVisibility(View.VISIBLE);
        }

        if (isShowEdit) {
            et_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("确定");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
            btn_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
        }
    }

    public void show() {
        setLayout();
        dialog.show();

        if (isShowEdit) {
            et_msg.setFocusable(true);
            et_msg.setFocusableInTouchMode(true);
            et_msg.requestFocus();
            InputSoftHelper.hideShowSoft(context);
//            (Activity)context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        if (isShowDropEt) {
            spinnerEditText.setFocusable(true);
            spinnerEditText.setFocusableInTouchMode(true);
            spinnerEditText.requestFocus();
            InputSoftHelper.hideShowSoft(context);
        }

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isShowEdit) {
                    InputSoftHelper.hideShowSoft(context);
                }
            }
        });
    }

    public void dissMiss() {
        dialog.dismiss();
    }

    public interface onClickListener {
        void onClick(View v, String msg);
    }

    public void setOnclickListener(onClickListener listener) {
        onClickListener = listener;
    }

//    class BaseBean{
//        String name;
//        String id;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//    }

}
