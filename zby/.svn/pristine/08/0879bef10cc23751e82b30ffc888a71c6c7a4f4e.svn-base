package com.biaozhunyuan.tianyi.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.model.OpinionModel;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.GsonTool;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.utils.StrUtils;

import org.json.JSONException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;

public class ApplyOpinionDialog extends DialogFragment {

    private View mLayout;
    private EditText etOpinion;
    private ListView lvOpinion;
    private Button btnCancel;
    private Button btnSure;
    private DismissListener listener;

    public interface DismissListener {
        void onDismissComplete(String opinion);
    }

    public void setDismissListener(DismissListener listener1) {
        listener = listener1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
    }

    public void setText(String text) {
        if (etOpinion != null)
            etOpinion.setText(text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String text = "";
        if (bundle != null) {
            text = bundle.getString("text");
        }
        mLayout = inflater.inflate(R.layout.pop_flow_opinion, container);
        etOpinion = mLayout.findViewById(R.id.et_opinion);
        lvOpinion = mLayout.findViewById(R.id.lv_opinion);
        btnCancel = mLayout.findViewById(R.id.btn_cancel);
        btnSure = mLayout.findViewById(R.id.btn_sure);

        if (!TextUtils.isEmpty(text)) {
            etOpinion.setText(text);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etOpinion.setText("");
                if (listener != null) {
                    listener.onDismissComplete("");
                }
                dismiss();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDismissComplete(etOpinion.getText().toString());
                }
                dismiss();
            }
        });

        lvOpinion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpinionModel item = (OpinionModel) lvOpinion.getItemAtPosition(position);
                etOpinion.setText(etOpinion.getText().toString() + item.getCommonOption());
            }
        });

        getOpinionList(lvOpinion);
        return mLayout;
    }


    private void getOpinionList(ListView lv) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取审批意见;

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    List<OpinionModel> list = GsonTool.jsonToArrayEntity(
                            JsonUtils.getStringValue(JsonUtils.pareseData(response), "data"), OpinionModel.class);
                    lv.setAdapter(getAdapter(list));
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


    private CommanAdapter<OpinionModel> getAdapter(List<OpinionModel> list) {
        return new CommanAdapter<OpinionModel>(list, getContext(), R.layout.item_common) {
            @Override
            public void convert(int position, OpinionModel item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv, StrUtils.pareseNull(item.getCommonOption()));
            }
        };
    }

}
