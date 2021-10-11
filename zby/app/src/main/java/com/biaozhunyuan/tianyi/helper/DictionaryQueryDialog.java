package com.biaozhunyuan.tianyi.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.attch.InfoAdapter;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.view.MyFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字典选择项全屏对话框，带有仿QQ搜索效果,自带内存缓存效果，保存最近搜索历史记录以流式布局的形式显示
 *
 * @author K
 * @since 2015-7-27
 */
@SuppressLint("NewApi")
public class DictionaryQueryDialog {
    /**
     * 选中字典的id,如果为-1表示没选中
     */
    // public int ID = -1;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;
    private static DictionaryQueryDialog mDialogHelper;
    /**
     * 一个字典最大缓存数量
     */
    private final int MAX_LATEST_VALUE = 6;
    public List<ReturnDict> mDictList;
    ZLServiceHelper zlServiceHelper = new ZLServiceHelper();
    private Context mContext;
    private String mDictName;
    private String mOriginalColumnName;
    private String mFilter;
    private ListView mLv;
    private InfoAdapter mAdapter;
    private Dialog alertDialog;
    //    private Dao<LatestSelectedDict, Integer> mDao;
    private EditText etSearch;
    private MyFlowLayout mFlowLayout;
    private OnSelectedListener mOnSelectedListener;

    /**
     * 弹出对话框并显示字典列表
     *
     * @param context 上下文
     */
    public DictionaryQueryDialog(final Context context) {
        super();
        this.mContext = context;
        mDictList = new ArrayList<ReturnDict>();
        initView();
    }

    public static DictionaryQueryDialog getInstance(Context context) {
        if (mDialogHelper != null && mDialogHelper.mContext.equals(context)) {
            return mDialogHelper;
        } else {
            return new DictionaryQueryDialog(context);
        }
    }

    private void initView() {
        if (alertDialog == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.dialog_select_search_dict,
                    null);
            View emptyView = inflater.inflate(R.layout.textview_empty, null);
            alertDialog = new Dialog(mContext, R.style.Dialog_Fullscreen);
            // alertDialog.setContentView(R.layout.dialog_select_search_dict);
            alertDialog.setContentView(view);
            initEvent(view);
            mLv = (ListView) view.findViewById(R.id.lv_dict_select);
            mAdapter = new InfoAdapter(mContext, mDictList, null);
            mLv.setEmptyView(emptyView);
            mLv.setItemsCanFocus(false);
            mLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mLv.setAdapter(mAdapter);
        }
    }

    private void initEvent(View view) {
        ImageView ivCancle = (ImageView) view
                .findViewById(R.id.imageViewCancel_client);
        ImageView ivRefresh = (ImageView) view
                .findViewById(R.id.image_refresh_query_dict);

        EditText etInput = (EditText) view.findViewById(R.id.et_input_dict);
        etSearch = (EditText) view.findViewById(R.id.et_search_dict);
        TextView tvCancel = (TextView) view
                .findViewById(R.id.tv_cancle_select_dict);

        mFlowLayout = (MyFlowLayout) view
                .findViewById(R.id.flow_layout_select_dict);
        ivCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShowSoft();
                alertDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        etInput.setInputType(InputType.TYPE_NULL); // 获取焦点，并且不让弹出软键盘
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 监听文本变化
                Logger.i("onText" + s.toString());
                search(s.toString());
            }
        });
    }

    /**
     * 弹出字典选择对话框,不做任何绑定操作，选中字典后需要通过 {@link }回调自行处理
     *
     * @param dictName 字典名称
     */
    public void show(String dictName, Map<String, String> dictHashMap) {
        final List<ReturnDict> returnDicts = new ArrayList<>();

        if (dictHashMap != null) {
            for (Map.Entry<String, String> map : dictHashMap.entrySet()) {
                ReturnDict dict1 = new ReturnDict(map.getKey(), map.getValue());
                returnDicts.add(dict1);
            }
        }
        show(dictName,returnDicts);
    }

    /**
     * 弹出字典选择对话框,不做任何绑定操作，选中字典后需要通过 {@link }回调自行处理
     *
     * @param dictName 字典名称
     */
    public void show(String dictName, List<ReturnDict> list) {
        show(null, dictName, list);
    }

    /**
     * 弹出字典选择对话框,不做任何绑定操作，选中字典后需要通过 {@link }回调自行处理
     *
     * @param dictName           字典名称
     * @param originalColumnName 原始数据库列表 AS 字典.名称
     * @param filter             附加条件
     */
    public void show(String dictName, String originalColumnName, String filter, List<ReturnDict> list) {
        this.mOriginalColumnName = originalColumnName;
        this.mFilter = filter;
        show(null, dictName, list);
    }

    ;

    /***
     * 弹出对话框,选中后将字典项实体绑定tvText的tag,字典名称在tvText上显示
     *
     * @param tvText   选中绑定字典项的文本框，选中后将字典项实体绑定TextView的tag,字典名称在TextView上显示
     * @param dictName 字典表名称
     */
    public void show(final TextView tvText, String dictName, List<ReturnDict> list) {
        etSearch.setText("");
        this.mDictName = dictName;
        this.mDictList = list;
        mFlowLayout.removeAllViews();


        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();
        setSelectedListener(tvText);

        alertDialog.show();
    }

    ;

    /***
     * 绑定listView的选中事件
     *
     * @param tvText
     */
    private void setSelectedListener(final TextView tvText) {
        mLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
                                    long arg3) {
                hideShowSoft();
                ReturnDict item = (ReturnDict) mAdapter.getItem(pos);

                setFlowSelectedListener(tvText, item);
            }
        });
    }

    /***
     * 绑定最近按钮选中监听事件
     *
     * @param tvText
     */
    private void setFlowSelectedListener(final TextView tvText, ReturnDict item) {
        if (tvText != null) {
            tvText.setText(item.value);
            tvText.setTag(item);
        }


        if (mOnSelectedListener != null) {
            mOnSelectedListener.onSelected(item);
        }

        alertDialog.dismiss();
    }


    /***
     * 搜索
     *
     * @param filter
     */
    private void search(String filter) {
        if (mDictList != null && mDictList.size() > 0) {
            List<ReturnDict> list = new ArrayList<ReturnDict>();
            for (int i = 0; i < mDictList.size(); i++) {
                if (!TextUtils.isEmpty(mDictList.get(i).text)) {
                    if (mDictList.get(i).text.contains(filter)) {
                        list.add(mDictList.get(i));
                    }
                }
            }
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }
    }


    /***
     * 字典选中事件监听
     */
    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public interface OnSelectedListener {
        void onSelected(ReturnDict dict);
    }

    public interface OnSelectedListener1 {
        void onSelected1(ReturnDict dict);
    }
}
