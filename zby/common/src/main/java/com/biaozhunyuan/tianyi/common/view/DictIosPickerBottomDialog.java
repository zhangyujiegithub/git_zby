package com.biaozhunyuan.tianyi.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;

/***
 * 字典仿IOS风格的底部选择
 *
 * @author K 2015/10/01 10:10
 */
public class DictIosPickerBottomDialog {
    private Dialog mDialog;
    private Context mContext;
    private CommanAdapter<String> adapter;
    private onSelectDictListener selectDictListener;

    /**
     * 是否是确定删除类型的对话框
     */
    private boolean isSureAndCancel;
    private OnSelectedListener mListener;

    private List<字典> mDictList;

    private boolean isShowTitle = false;
    private boolean isShowDict = false;
    private String title = "";
    private List<String> showData;

    /**
     * @param mContext
     */
    public DictIosPickerBottomDialog(Context mContext) {
        this.mContext = mContext;
        mDialog = new Dialog(mContext, R.style.styleNoFrameDialog_bottom);
        mDialog.setCancelable(true);
    }

    /**
     * 弹出IOS风格的确定选择
     *
     * @param info 提示信息，如是否确定
     */
    public void show(String info) {
        isSureAndCancel = true;
        ArrayList<String> list = new ArrayList<String>();
        list.add(info);
        show(list);
    }

    /**
     * 弹出IOS风格的确定选择
     *
     * @param info 提示信息，如是否确定
     */
    public void show(String info, boolean isDict, String filter, String colName) {
        getCustomDicts(info, filter, colName);
    }

    /**
     * 弹出IOS风格的确定选择
     *
     * @param info 提示信息，如是否确定
     */
    public void show(String info, boolean isDict) {
        getCustomDicts(info, "", "");
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param datas 数据源
     */
    public void show(String[] datas) {
        show(Arrays.asList(datas));
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param datas 数据源
     */
    public void show(List<String> datas) {
        View view = View.inflate(mContext, R.layout.pop_dict_ios_picker, null);
        if (showData == null) {
            showData = new ArrayList<String>();
        }
        showData.clear();
        if (isShowTitle && !TextUtils.isEmpty(title)) {
            showData.add(title);
        }
        showData.addAll(datas);
        initViews(showData, view);
        mDialog.setContentView(view);
        show();
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param datas     泛型类型的集合数据源
     * @param fieldName 泛型中作为显示名称的字段名称
     */
    public <T> void show(List<T> datas, String fieldName) {
        if (datas == null) {
            return;
        }

        List<String> list = new ArrayList<String>();
        for (T item : datas) {
            Class cs = item.getClass();
            try {
                Field field = cs.getField(fieldName);
                String value = (String) field.get(item);
                Logger.i("field" + "--" + value);
                list.add(value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        show(list);
    }

    private void initViews(List<String> datas, View view) {

        View top = (View) view.findViewById(R.id.top_dict_ios_picker);
        Button btnCancel = (Button) view
                .findViewById(R.id.btn_cancle_dict_picker);
        final ListView lv = (ListView) view
                .findViewById(R.id.lv_dict_ios_picker);
        adapter = getAdapter(datas);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mListener != null && !isShowDict) {
                    isSureAndCancel = false; // 恢复默认值
                    mDialog.dismiss();
                    mListener.onSelected(position);
                }

                if (selectDictListener != null && isShowDict) {
                    if (mDictList != null && mDictList.size() > 0) {
                        isSureAndCancel = false; // 恢复默认值
                        mDialog.dismiss();
                        if (mDictList.size() > position) {
                            selectDictListener.onSelectedDict(mDictList.get(position));
                        }
                    }
                }
                isShowDict = false;
            }
        });

        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null || selectDictListener != null) {
                    mDialog.dismiss();
                }
            }
        });

    }

    private void show() {
        mDialog.show();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (ViewHelper.getScreenWidth(mContext)); // 设置宽度
        lp.gravity = Gravity.BOTTOM;
        mDialog.getWindow().setAttributes(lp);
    }

    private CommanAdapter<String> getAdapter(final List<String> datas) {
        return new CommanAdapter<String>(datas, mContext,
                R.layout.item_dict_ios) {
            @Override
            public void convert(int position, String item,
                                BoeryunViewHolder viewHolder) {
                TextView tvName = viewHolder.getView(R.id.tv_name_dict_ios);
                tvName.setText(item + "");
                if (isSureAndCancel) {
                    // 选择功能则 字体颜色为红色
                    tvName.setTextColor(Color.RED);
                }
                if (isShowTitle && position == 0 && !TextUtils.isEmpty(title)) {
//                    tvName.setTextSize(16);
                    tvName.setTextColor(mContext.getResources().getColor(R.color.text_info));
                }
            }
        };
    }


    /**
     * 设置标题
     */
    public void setTitle(String title) {
        this.isShowTitle = true;
        this.title = title;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }

    public void setOnSelectedDictListener(onSelectDictListener onSelectedListener) {
        this.selectDictListener = onSelectedListener;
    }

    public interface OnSelectedListener {
        /**
         * 监听选中字典集合的序号
         */
        void onSelected(int index);
    }

    public interface onSelectDictListener {

        void onSelectedDict(字典 dict);
    }

    /***
     * 根据字典名称获得一个字典的集合
     *
     * @param dictTableName      字典表名称,如果是普通字典表，其余两个参数可传null
     * @return
     */
    public void getCustomDicts(final String dictTableName, String filter, String colName) {
        isShowDict = true;
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Logger.i("getDict" + url);
//        List<DictData> list = new ArrayList<DictData>();
        final DictData dictData = new DictData();
        dictData.setDictionaryName(dictTableName);
        dictData.setFull(true);
        if (!TextUtils.isEmpty(filter)) {
            dictData.setFilter(filter);
        }
        if (!TextUtils.isEmpty(colName)) {
            dictData.setColName(colName);
        }
//        list.add(dictData);
        StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                ProgressDialogHelper.dismiss();
                try {
                    mDictList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                    if (mDictList != null && mDictList.size() > 0) {
                        List<String> strs = new ArrayList<String>();
                        for (字典 dict : mDictList) {
                            strs.add(dict.getName());
                        }
                        show(strs);
                    } else {
                        Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
}
