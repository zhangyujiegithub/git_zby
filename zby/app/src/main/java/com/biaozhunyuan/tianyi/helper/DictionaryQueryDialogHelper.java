package com.biaozhunyuan.tianyi.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.helper.ORMDataHelper;
import com.biaozhunyuan.tianyi.common.model.dict.LatestSelectedDict;
import com.biaozhunyuan.tianyi.base.BoeryunApp;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.client.InfoAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.MyFlowLayout;
import com.biaozhunyuan.tianyi.widget.MyProgressBar;
import com.j256.ormlite.dao.Dao;

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;


/**
 * 字典选择项全屏对话框，带有仿QQ搜索效果,自带内存缓存效果，保存最近搜索历史记录以流式布局的形式显示
 *
 * @author K
 * @since 2015-7-27
 */
@SuppressLint("NewApi")
public class DictionaryQueryDialogHelper {
    /**
     * 一个字典最大缓存数量
     */
    private final int MAX_LATEST_VALUE = 6;
    private Context mContext;
    private String mDictName;

    private String mOriginalColumnName;
    private String mFilter;

    public List<字典> mDictList;
    private ListView mLv;
    private MyProgressBar mPbar;
    private InfoAdapter mAdapter;
    private Dialog alertDialog;
    private static DictionaryQueryDialogHelper mDialogHelper;
    private Dao<LatestSelectedDict, Integer> mDao;
    private List<字典> emptyList = new ArrayList<字典>();

    /**
     * 选中字典的id,如果为-1表示没选中
     */
    // public int ID = -1;
    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;
    ZLServiceHelper zlServiceHelper = new ZLServiceHelper();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    mPbar.setVisibility(View.GONE);
                    clearLocalLatestList();
                    clearSoftReference();
                    List<字典> list = (List<字典>) msg.obj;
                    mAdapter.setList(list);
                    mAdapter.notifyDataSetChanged();
                    break;
                case FAILURE:
                    mPbar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private EditText etSearch;
    private EditText etInput;

    /**
     * 弹出对话框并显示字典列表
     *
     * @param context 上下文
     */
    public DictionaryQueryDialogHelper(final Context context) {
        super();
        this.mContext = context;
        ORMDataHelper ormDataHelper = ORMDataHelper.getInstance(context);
        try {
            mDao = ormDataHelper.getDao(LatestSelectedDict.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initView();
    }

    public static DictionaryQueryDialogHelper getInstance(Context context) {
        if (mDialogHelper != null && mDialogHelper.mContext.equals(context)) {
            return mDialogHelper;
        } else {
            return new DictionaryQueryDialogHelper(context);
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
            mPbar = (MyProgressBar) view.findViewById(R.id.pbar_dict_select);
            mAdapter = new InfoAdapter(mContext, emptyList, null);
            mLv.setEmptyView(emptyView);
            mLv.setItemsCanFocus(false);
            mLv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mLv.setAdapter(mAdapter);
        }
    }

    private MyFlowLayout mFlowLayout;

    private void initEvent(View view) {
        ImageView ivCancle = (ImageView) view
                .findViewById(R.id.imageViewCancel_client);
        ImageView ivRefresh = (ImageView) view
                .findViewById(R.id.image_refresh_query_dict);

        etInput = (EditText) view.findViewById(R.id.et_input_dict);
        etSearch = (EditText) view.findViewById(R.id.et_search_dict);
        TextView tvCancel = (TextView) view
                .findViewById(R.id.tv_cancle_select_dict);

        mFlowLayout = (MyFlowLayout) view
                .findViewById(R.id.flow_layout_select_dict);
        ivCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputSoftHelper.hiddenSoftInput(mContext, etInput);
                alertDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        ivRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mDictName)) {
                    mPbar.setVisibility(View.VISIBLE);
                    clearLocalLatestList();
                    clearSoftReference();

                    mFlowLayout.removeAllViews();
                    mAdapter.notifyDataSetChanged();

                    startDownload(mDictName, mFilter);
                }
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
    public void show(String dictName) {
        show(dictName, "");
    }

    /**
     * 弹出字典选择对话框,不做任何绑定操作，选中字典后需要通过 {@link }回调自行处理
     *
     * @param dictName 字典名称
     * @param filter   附加条件
     */
    public void show(String dictName, String filter) {
        this.mDictName = dictName;
        this.mFilter = filter;
        show(null, dictName, filter);
    }

    /***
     * 弹出对话框,选中后将字典项实体绑定tvText的tag,字典名称在tvText上显示
     *
     * @param tvText   选中绑定字典项的文本框，选中后将字典项实体绑定TextView的tag,字典名称在TextView上显示
     * @param dictName 字典表名称
     */
    public void show(final TextView tvText, String dictName, String mFilter) {
        etSearch.setText("");
        this.mDictName = dictName;
        this.mFilter = mFilter;
        final List<LatestSelectedDict> localDicts = getLocalLatestList();
        mPbar.setVisibility(View.VISIBLE);
        mFlowLayout.removeAllViews();
        for (int i = 0; i < localDicts.size(); i++) {
            final LatestSelectedDict item = localDicts.get(i);
            // 流式布局的形式 显示本地最近选择
            final TextView tvName = (TextView) LayoutInflater.from(mContext)
                    .inflate(R.layout.tag_text, mFlowLayout, false);
            tvName.setText(localDicts.get(i).getName() + "");
            tvName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setFlowSelectedListener(tvText,
                            new 字典(item.getUuid(), item.getName()));
                }
            });
            mFlowLayout.addView(tvName);
        }

        if (!TextUtils.isEmpty(mFilter)) {
            // 如果过滤条件不为空，则先清空该字典原有缓存
            BoeryunApp.getDictHashMap().remove(dictName);
        }

        SoftReference<List<字典>> softReference = BoeryunApp.getDictHashMap()
                .get(dictName);
        if (softReference == null) {
            startDownload(dictName, mFilter);
            mAdapter.setList(emptyList);
            mAdapter.notifyDataSetChanged();
        } else {
            mDictList = BoeryunApp.getDictHashMap().get(dictName).get();
            if (mDictList == null) {
                mAdapter.setList(emptyList);
                mAdapter.notifyDataSetChanged();
                startDownload(dictName, mFilter);
            } else {
                mAdapter.setList(mDictList);
                mAdapter.notifyDataSetChanged();
                mPbar.setVisibility(View.GONE);
            }
        }
        setSelectedListener(tvText);
        clearLocalLatestList();
        clearSoftReference();
        alertDialog.show();
    }

    /**
     * 弹出字典选择对话框,不做任何绑定操作，选中字典后需要通过 {@link }回调自行处理
     *
     * @param url 获取字典的接口
     */
    public void showDialogByUrl(String url) {
        ProgressDialogHelper.show(mContext);
        try {
            StringRequest.getAsyn(url, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    ProgressDialogHelper.dismiss();
                    try {
                        mDictList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                        if (mDictList != null && mDictList.size() > 0) {
                            Message msg = handler.obtainMessage();
                            msg.what = SUCCESS;
                            msg.obj = mDictList;
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(FAILURE);
                        }
                    } catch (Exception e) {
                        handler.sendEmptyMessage(FAILURE);
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
        setSelectedListener(null);
        alertDialog.show();
    }

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
                InputSoftHelper.hiddenSoftInput(mContext,etInput);
                字典 item = (字典) mAdapter.getItem(pos);
                    setFlowSelectedListener(tvText, item);
            }
        });
    }

    ;

    /***
     * 绑定最近按钮选中监听事件
     *
     * @param tvText
     */
    private void setFlowSelectedListener(final TextView tvText, 字典 item) {
        if (tvText != null) {
            tvText.setText(item.getName());
            tvText.setTag(item);
        }

//        insertDbIfNoExist(item);
        etSearch.setText("");
        if (mOnSelectedListener != null) {
            mOnSelectedListener.onSelected(item);
        }

        if (mOnSelectedListener1 != null) {
            mOnSelectedListener1.onSelected1(item);
        }

        alertDialog.dismiss();
    }

    ;

    /**
     * 开始下载字典项
     */
    public void startDownload(String dictName, String mFilter) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // mDictList = zlServiceHelper.getDictList(dictName);
//                getCustomDicts(dictName);
//
//            }
//        }).start();
        getCustomDicts(dictName, mFilter);
    }

    /***
     * 搜索
     *
     * @param filter
     */
    private void search(String filter) {
        if (mDictList != null && mDictList.size() > 0) {
            List<字典> list = new ArrayList<字典>();
            for (int i = 0; i < mDictList.size(); i++) {
                if (!TextUtils.isEmpty(mDictList.get(i).getName())) {
                    if (mDictList.get(i).getName().contains(filter)) {
                        list.add(mDictList.get(i));
                    }
                }
            }
            mAdapter.setList(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    /***
     * 选中字典项，添加到最近选中数据库
     *
     * @param dict
     */
    private void insertDbIfNoExist(字典 dict) {
        try {
            int count = mDao.queryBuilder().where().eq("DictName", mDictName)
                    .query().size();
            if (count >= MAX_LATEST_VALUE) {
                // 如果超出最大数量，先删除最后更新时间小的，间隔远的
                long deleteCount = count / 2;
                mDao.delete(mDao.queryBuilder().orderBy("updateTime", true)
                        .limit(deleteCount).query());
            }

            // 查询相同字典项
            LatestSelectedDict updateDict = mDao.queryBuilder().where()
                    .eq("DictName", mDictName).and().eq("Id", dict.getUuid())
                    .queryForFirst();
            mDao.delete(updateDict);

            updateDict = new LatestSelectedDict(dict.getUuid(), dict.getName(),
                    mDictName);
            mDao.create(updateDict);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * 获取本地最近选择列表
     *
     * @return
     */
    private List<LatestSelectedDict> getLocalLatestList() {
        try {
            return mDao.queryBuilder().orderBy("updateTime", false).where()
                    .eq("DictName", mDictName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<LatestSelectedDict>();
    }

    /***
     * 清空本地本地最近选择列表
     *
     * @return
     */
    private void clearLocalLatestList() {
        try {
            List<LatestSelectedDict> deleteList = mDao.queryBuilder().where()
                    .eq("DictName", mDictName).query();
            mDao.delete(deleteList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * 清空软引用
     */
    private void clearSoftReference() {
        SoftReference<List<字典>> softReference = BoeryunApp.getDictHashMap()
                .get(mDictName);
        if (softReference != null) {
            softReference.clear();
        }

    }

    private OnSelectedListener mOnSelectedListener;
    private OnSelectedListener1 mOnSelectedListener1;

    public interface OnSelectedListener {
        public void onSelected(字典 dict);
    }

    /***
     * 字典选中事件监听
     */
    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    public interface OnSelectedListener1 {
        public void onSelected1(字典 dict);
    }

    /***
     * 字典选中事件监听
     */
    public void setOnSelectedListener1(OnSelectedListener1 onSelectedListener) {
        this.mOnSelectedListener1 = onSelectedListener;
    }


    /***
     * 根据字典名称获得一个字典的集合
     *
     * @param dictTableName      字典表名称,如果是普通字典表，其余两个参数可传null
     * @return
     */
    public void getCustomDicts(final String dictTableName, String mFilter) {
        ProgressDialogHelper.show(mContext);
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        Logger.i("getDict" + url);
//        List<DictData> list = new ArrayList<DictData>();
        final DictData dictData = new DictData();
        dictData.setDictionaryName(dictTableName);
        dictData.setFull(true);
        dictData.setFilter(mFilter);
//        list.add(dictData);
        try {
            StringRequest.postAsyn(url, dictData, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    ProgressDialogHelper.dismiss();
                    try {
                        mDictList = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                        if (mDictList != null && mDictList.size() > 0) {
                            SoftReference<List<字典>> softRef = new SoftReference<List<字典>>(
                                    mDictList);
                            BoeryunApp.getDictHashMap().put(dictTableName, softRef);
                            Message msg = handler.obtainMessage();
                            msg.what = SUCCESS;
                            msg.obj = mDictList;
                            handler.sendMessage(msg);
                        } else {
                            handler.sendEmptyMessage(FAILURE);
                        }
                    } catch (Exception e) {
                        handler.sendEmptyMessage(FAILURE);
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
}
