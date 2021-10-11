package com.biaozhunyuan.tianyi.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.form.ReturnDict;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunSearchViewNoButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import okhttp3.Request;

/***
 * 字典仿IOS风格的底部选择 流式布局多选
 *
 * @author K 2015/11/05 10:13
 */
public class DictIosMultiPicker {

    private Context mContext;
    private OnMultiSelectedListener mListener;
    private LayoutInflater mInflater;

    private Set<Integer> mSelectPosSet = new HashSet<Integer>();

    private List<ReturnDict> mDictList;
    private int mRootId;

    private List<String> searchList = new ArrayList<>();
    private Set<String> selectedList = new HashSet<>();
    private TagFlowLayout tagFlowLayout;
    private boolean isSearching; //是否正在搜索字典项

    /**
     * @param mContext
     */
    public DictIosMultiPicker(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDictList = new ArrayList<ReturnDict>();
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param datas 数据源
     */
    private void show(List<String> datas) {
        View parentView = ((Activity) mContext).findViewById(mRootId);
        View view = View.inflate(mContext, R.layout.pop_dict_ios_multi_picker,
                null);
        final PopupWindow popupWindow = new PopupWindow(view,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        initViews(datas, view, popupWindow);
        initPopupWindow(parentView, popupWindow);
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param mainLayoutId layout文件的根节点id
     * @param hashmapDict  泛型类型的集合数据源
     */
    public void show(int mainLayoutId, Map<String, String> hashmapDict) {
        mDictList.clear();

        if (hashmapDict == null) {
            show(mainLayoutId, mDictList);
        } else {
            Iterator<Entry<String, String>> iterator = hashmapDict.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                mDictList.add(new ReturnDict(entry.getKey(), entry.getValue()));
            }
            show(mainLayoutId, mDictList);
        }
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param mainLayoutId layout文件的根节点id
     * @param datas        泛型类型的集合数据源
     */
    public void show(int mainLayoutId, List<ReturnDict> datas) {
        this.mRootId = mainLayoutId;
        this.mDictList = datas;

        List<String> list = new ArrayList<String>();
        for (ReturnDict item : datas) {
            list.add(item.text);
        }
        show(list);
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param mainLayoutId layout文件的根节点id
     * @param dictName     字典名称
     */
    public void showByDictName(int mainLayoutId, String dictName) {
        showByDictName(mainLayoutId, dictName, "");
    }

    /**
     * 弹出IOS风格的底部字典选择
     *
     * @param mainLayoutId layout文件的根节点id
     * @param dictName     字典名称
     */
    public void showByDictName(int mainLayoutId, String dictName, String filter) {
        this.mRootId = mainLayoutId;

        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Logger.i("getDict::" + url);
//        List<DictData> list = new ArrayList<DictData>();
        final DictData dictData = new DictData();
        dictData.setDictionaryName(dictName);
        dictData.setFilter(filter);
        dictData.setFull(true);
//        list.add(dictData);
        try {
            ProgressDialogHelper.show(mContext);
            StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {

                    ProgressDialogHelper.dismiss();
                    Logger.i("getDict" + response);
                    List<字典> list = null;
                    try {
                        list = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                        mDictList = new ArrayList<ReturnDict>();
                        List<String> dictNameList = new ArrayList<String>();
                        for (字典 item : list) {
                            dictNameList.add(item.getName());
                            mDictList.add(new ReturnDict(item.getUuid(), item.getName()));
                        }
                        show(dictNameList);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPopupWindow(View parentView, final PopupWindow popupWindow) {
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1);
            }
        });

        popupWindow.setAnimationStyle(R.style.AnimationFadeBottom);
        setBackgroundAlpha(0.5f);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.LEFT,
                0, 0);
    }

    private void initViews(List<String> datas, View view,
                           final PopupWindow popupWindow) {

        View top = (View) view.findViewById(R.id.top_dict_ios_multi_picker);
        Button btnSure = (Button) view
                .findViewById(R.id.btn_sure_dict_multi_picker);
        BoeryunSearchViewNoButton seachButton = view.findViewById(R.id.seach_button);
        tagFlowLayout = (TagFlowLayout) view
                .findViewById(R.id.tgflowlayout_multi_picker);
        setTagAdapter(datas);

        tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (isSearching) {
                    for (int i = 0; i < datas.size(); i++) {
                        for (int j = 0; j < searchList.size(); j++) {
                            if (datas.get(i).equals(searchList.get(j))) {
                                mSelectPosSet.add(i);
                                selectedList.add(datas.get(i));
                            }
                        }
                    }
                } else {
                    selectedList.clear();
                    mSelectPosSet = selectPosSet;
                    for (Integer i : mSelectPosSet) {
                        selectedList.add(datas.get(i));
                    }
                }
            }
        });

        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (mListener != null) {
                        List<ReturnDict> selectDicts = new ArrayList<ReturnDict>();
                        for (Integer pos : mSelectPosSet) {
                            selectDicts.add(mDictList.get(pos));
                        }
                        StringBuilder sbIds = new StringBuilder();
                        StringBuilder sbNames = new StringBuilder();

                        String idsStr = "";
                        String namesStr = "";
                        for (ReturnDict dict : selectDicts) {
                            sbIds.append(dict.value).append(",");
                            sbNames.append(dict.text).append(",");
                        }
                        if (selectDicts.size() > 0) {
                            idsStr = sbIds.substring(0, sbIds.length() - 1);
                            namesStr = sbNames.substring(0,
                                    sbNames.length() - 1);
                        }
                        // mListener.onSelected(mSelectPosSet);
                        mListener.onSelected(idsStr, namesStr);
                    }
                    popupWindow.dismiss();
                }
            }
        });
        seachButton.setOnSearchedListener(new BoeryunSearchViewNoButton.OnSearchedListener() {
            @Override
            public void OnSearched(String str) {
                if (!TextUtils.isEmpty(str)) {
                    isSearching = true;
                    searchList.clear();
                    for (String s : datas) {
                        if (s.contains(str)) {
                            searchList.add(s);
                        }
                    }
                    setTagAdapter(searchList);
                } else {
                    isSearching = false;
                    setTagAdapter(datas);
                }
            }
        });
    }

    private void setTagAdapter(List<String> datas) {
        TagAdapter<String> tagAdapter = new TagAdapter<String>(datas) {
            @Override
            public View getView(FlowLayout parent, int position, String t) {
                Logger.i("tagA" + position + "--" + t);
                TextView tv = (TextView) mInflater.inflate(
                        R.layout.item_tag_flowlayout, tagFlowLayout, false);
                tv.setText(t);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter);
        tagFlowLayout.clearSelected();
        /**
         * 设置当前选中项
         */
        List<String> select = new ArrayList<String>(selectedList);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            for (int j = 0; j < selectedList.size(); j++) {
                if (datas.get(i).equals(select.get(j))) {
                    list.add(i);
                }
            }
        }
        tagAdapter.setSelectedList(list);
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

    public void setOnMultiSelectedListener(
            OnMultiSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }

    public interface OnMultiSelectedListener {

        void onSelected(String selectedIds, String selectedNames);
    }
}
