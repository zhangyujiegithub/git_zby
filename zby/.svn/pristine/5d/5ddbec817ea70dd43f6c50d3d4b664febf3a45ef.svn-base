package com.biaozhunyuan.tianyi.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;


/***
 * 顶部搜索控件,没有搜索按钮，根据输入框内输入文字搜索内容
 *
 * @author kjx
 */
public class BoeryunSearchViewNoButton extends RelativeLayout {
    private OnSearchedListener mOnSearchedListener;
    private OnSearchButtonClickListener searchButtonClickListener;
    private OnButtonClickListener mOnButtonClickListener;
    private OnEditorActionListener mEditorActionListener;
    private String mSearchHintText;
    private TextView tv_hint;
    private EditText eText;
    private RelativeLayout rlIco;
    private ImageView ivClear;
    private Context mContext;
    private TextView tvSearch;

    public BoeryunSearchViewNoButton(Context context) {
        this(context, null, 0);
    }

    public BoeryunSearchViewNoButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoeryunSearchViewNoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View view = LayoutInflater.from(context).inflate(
                R.layout.control_searchview_no_button, this, true);
        mContext = context;
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.BoeryunSearchView, defStyle, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.BoeryunSearchView_hintText) {
                mSearchHintText = typedArray.getString(index);
            }
        }
        initView(view);
        Logger.i("BoeryunSearchView_INIT" + "INIT");
    }

    private void initView(View view) {
        rlIco = (RelativeLayout) view
                .findViewById(R.id.rl_search_ico);
        eText = (EditText) view
                .findViewById(R.id.et_search_text);
//        TextView ivCancle = (TextView) findViewById(R.id.tv_search_view_cancle);
        tvSearch = (TextView) findViewById(R.id.tv_search_view_search);
        ivClear = (ImageView) view.findViewById(R.id.iv_clear_control);
        tv_hint = (TextView) findViewById(R.id.tv_search_view_hint);

        if (!TextUtils.isEmpty(mSearchHintText)) {
            eText.setHint(mSearchHintText);
        }

        eText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Logger.i("BoeryunSearchView" + s + "");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.i("Editable" + s + "");
//                if (TextUtils.isEmpty(s.toString())) {
//                    rlIco.setVisibility(VISIBLE);
//                } else {
//                    rlIco.setVisibility(GONE);
//                }
                if (mOnSearchedListener != null) {
                    mOnSearchedListener.OnSearched(s.toString());
                }
            }
        });

        eText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (mOnSearchedListener != null) {
                        mOnSearchedListener.OnSearched(eText.getText().toString());
                    }
                    InputSoftHelper.hiddenSoftInput(getContext(), eText);
                }
                return false;
            }
        });

        /** 覆盖于搜索框上的按钮点击事件 */
        rlIco.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchGetFoucus();
            }
        });

        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击取消按钮，清空搜索文字
                InputSoftHelper.hiddenSoftInput(getContext(), eText);
                eText.setText("");
                rlIco.setVisibility(View.VISIBLE);
                if (mOnButtonClickListener != null) {
                    mOnButtonClickListener.OnCancle();
                }
            }
        });

        tvSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchButtonClickListener != null) {
                    InputSoftHelper.hiddenSoftInput(mContext, eText);
                    searchButtonClickListener.OnClick();
                }
            }
        });
    }

    public void setSearchGetFoucus() {
        eText.requestFocus();
        eText.setFocusable(true);
        eText.setFocusableInTouchMode(true);
        InputSoftHelper.ShowKeyboard(eText);
        if (mOnButtonClickListener != null) {
            mOnButtonClickListener.OnClick();
            rlIco.setVisibility(View.GONE);
        } else {
            rlIco.setVisibility(View.GONE);
        }
    }

    public void setSearchText(String text) {
        tvSearch.setText(text);
    }

    /**
     * 给搜索框设置搜索提示语
     *
     * @param str
     */
    public void setEditTextHint(String str) {
    }

    /**
     * 显示输入框，输入框获取焦点
     */
    public void getFocus() {
        rlIco.setVisibility(View.GONE);
        eText.requestFocus();
    }

    public void setSearchButtonVisable() {
        rlIco.setVisibility(View.GONE);
        tvSearch.setVisibility(VISIBLE);
        InputSoftHelper.openKeyBoard(eText);
    }

    public EditText geteText() {
        return eText;
    }

    public void setOnCancleSearch(boolean isRefresh) {
        if (isRefresh) {
            setOnCancleSearch();
        } else {
            // 点击取消按钮，清空搜索文字
            InputSoftHelper.hiddenSoftInput(getContext(), eText);
            eText.setText("");
            rlIco.setVisibility(View.VISIBLE);
        }
    }

    public void setOnCancleSearch() {
        // 点击取消按钮，清空搜索文字
        InputSoftHelper.hiddenSoftInput(getContext(), eText);
        eText.setText("");
        rlIco.setVisibility(View.VISIBLE);
        if (mOnButtonClickListener != null) {
            mOnButtonClickListener.OnCancle();
        }
    }


    /**
     * 设置搜索提示语
     *
     * @param str
     */
    public void setHint(String str) {
        ViewHelper.setEditTextHintSize(eText, str, 13);
        tv_hint.setText(str);
    }

    /***
     * 搜索框按钮点击事件监听
     *
     * @param mEditorActionListener
     */
    public void setOnEditorActionListener(
            OnEditorActionListener mEditorActionListener) {
        this.mEditorActionListener = mEditorActionListener;
    }

    /***
     * 搜索框键盘回车点击事件监听
     *
     * @param onButtonClickListener
     */
    public void setOnButtonClickListener(
            OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    /**
     * 监听文字变化
     */
    public void setOnSearchedListener(OnSearchedListener onSearchedListener) {
        this.mOnSearchedListener = onSearchedListener;
    }

    public interface OnSearchedListener {
        public abstract void OnSearched(String str);
    }

    public interface OnButtonClickListener {
        public abstract void OnCancle();

        public abstract void OnClick();
    }

    public interface OnEditorActionListener {
        public abstract void OnDone();
    }

    public interface OnSearchButtonClickListener {
        void OnClick();
    }

    public void setOnSearchButtonListener(OnSearchButtonClickListener onSearchedListener) {
        this.searchButtonClickListener = onSearchedListener;
    }

}
