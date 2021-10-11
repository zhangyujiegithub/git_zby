package com.biaozhunyuan.tianyi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.attch.AttachInfo;
import com.biaozhunyuan.tianyi.attch.EnumAttachType;
import com.biaozhunyuan.tianyi.common.attach.OpenFilesIntent;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.attch.PdfActivity;
import com.biaozhunyuan.tianyi.attch.UploadAttachAdapter;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.global.AttachInfoCache;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.AttachBiz;
import com.biaozhunyuan.tianyi.common.helper.BitmapHelper;
import com.biaozhunyuan.tianyi.common.helper.SelectPhotoBiz;
import com.biaozhunyuan.tianyi.common.helper.UploadHelper;
import com.biaozhunyuan.tianyi.helper.ZLServiceHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/***
 * 多附件显示上传控件
 *
 * @author K
 *         2016-01-01
 */
public class MultipleAttachView extends GridView {

    private final static int CODE_DOWNLOAD_ATTACH_TYPE = 3;
    private final static int CODE_REQUEST_SELECT_FILE = 998;
    private static int mNumColumns = -1;
    private final String TAG = "MultipleAttachView";
    List<AttachInfo> mAttachInfos;
    private Context mContext;
    private Handler mHandler;
    private Integer maxCount; //最大数量
    private DictIosPickerBottomDialog mDictIosPicker;
    private ZLServiceHelper mZlServiceHelper;
    private UploadAttachAdapter mAdapter;
    private String mAttachIds;
    private boolean onlyCanTackPhoto = false; //是否只可以拍照
    /**
     * 是否新建
     */
    private boolean mIsAdd;
    private OnAddImageListener mOnAddImageListener;

    public MultipleAttachView(Context context) {
        this(context, null);
    }

    public MultipleAttachView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleAttachView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        mDictIosPicker = new DictIosPickerBottomDialog(mContext);
        mZlServiceHelper = new ZLServiceHelper();
        initHandler();
        if (mNumColumns >= 1) {
            setNumColumns(mNumColumns);
        }
        Logger.i("mNumColumns" + mNumColumns + "==" + getNumColumns());
        Logger.i(TAG + "MultipleAttachView=setAdapter");
        mAdapter = new UploadAttachAdapter(mContext, "", mIsAdd);
        setAdapter(mAdapter);
        setOnEvent();
    }

    public static MultipleAttachView getInstance(Context context, int numColumns) {
        mNumColumns = numColumns;
        return new MultipleAttachView(context);
    }

    private void setOnEvent() {
        mAdapter.setOnAdapterItemClickListener(new UploadAttachAdapter.OnAdapterItemClickListener() {
            @Override
            public void onOpenUrl(int pos, List<AttachInfo> attachInfos) {
                ArrayList<String> urls = new ArrayList<>();
                for (AttachInfo info : attachInfos) {
                    String suffix = "";
                    try {
                        suffix = info.getAttach().getName().substring(info.getAttach().getName().lastIndexOf(".") + 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (info.getAttach() != null && ImageUtils.isImage(suffix)) {
                        urls.add(ImageUtils.getDownloadUrlById(info.getAttach().uuid));
                    }
                }
                if (pos < attachInfos.size()) {
                    AttachInfo attachInfo = attachInfos.get(pos);
                    Attach attach = attachInfo.getAttach();
                    if (attach != null) {
                        String suffix = "";
                        try {
                            suffix = attach.getName().substring(attach.getName().lastIndexOf(".") + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (ImageUtils.isImage(suffix)) {
//                            ImageUtils.startSingleImageBrower(mContext,
//                                    ImageUtils.getDownloadUrlById(attach.uuid));
                            ImageUtils.startImageBrower(mContext, pos,
                                    urls);
                        } else if (checkEndsWithInStringArray(attach.filename, mContext.getResources()
                                .getStringArray(R.array.fileEndingPdf))) {
                            Intent intent = new Intent(mContext, PdfActivity.class);
                            intent.putExtra("filepath", ImageUtils.getDownloadUrlById(attach.uuid));
                            intent.putExtra("title", attach.filename);
                            mContext.startActivity(intent);
                        } else {
                            AttachBiz.startAttachActivity(mContext, attach);
                        }
                    } else if (attachInfo.getType() == EnumAttachType.本地路径 && !TextUtils.isEmpty(attachInfo.getLocalPath())) {
//                        AttachBiz.startLocalImageActivity(mContext, attachInfo.getLocalPath());
                        OpenFilesIntent.open(mContext, attachInfo.getLocalPath());
                    }
                }
            }

            @Override
            public void onOpenLocal(int pos, List<AttachInfo> attachInfos) {

            }

            @Override
            public void onAdd() {
                if (mOnAddImageListener != null) {
                    mOnAddImageListener.onAddImageListener();
                }
                doPickPhoto();
                mAdapter.notifySetChangeIsDelete(false);
            }
        });

        /*setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                LogUtils.i(TAG, "长按...");
                mAdapter.notifySetChangeIsDelete(true);
                return true;
            }
        });*/

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mAdapter.notifySetChangeIsDelete(false);
                }
            }
        });
    }

    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        LogUtils.i(TAG, "onMeasure=" + width);

        mAdapter.setPicWidth(width / getNumColumns());
        // mAdapter.notifyDataSetChanged();
    }

    public boolean isAdd() {
        return mIsAdd;
    }

    public void setIsAdd(boolean isAdd) {
        mAdapter.notifySetChangeIsAdd(isAdd);
    }


    /**
     * 设置是否只可以拍照
     *
     * @param canTackPhoto
     */
    public void setOnlyCanTackPhoto(boolean canTackPhoto) {
        onlyCanTackPhoto = canTackPhoto;
    }

    /***
     * 加载指定附件编号的图片
     *
     * @param attachIds
     */
    public void loadImageByAttachIds(final String attachIds) {
        if (TextUtils.isEmpty(attachIds)) {
            //附件为空
            this.mAttachIds = attachIds;
            mAttachInfos = new ArrayList<AttachInfo>();
            mAdapter.addItems(mAttachInfos, true);
            return;
        }

        mAttachInfos = AttachInfoCache.getInstance().get(attachIds);
        if (mAttachInfos != null && mAttachInfos.size() > 0) {
            mAdapter.addItems(mAttachInfos, true);
        } else {
            if (!attachIds.equals(mAttachIds)) {
                mAttachInfos = getAttachInfos(attachIds);
                mAdapter.addItems(mAttachInfos, true);
            } else {
                this.mAttachIds = attachIds;
            }
            loadAttachsFromServer(attachIds, mAttachInfos);
        }
    }

    /***
     * 预加载指定附件，先占位不访问网络
     *
     * @param attachIds
     */
    public void preLoadImageByAttachIds(final String attachIds) {
        this.mAttachIds = attachIds;
        final List<AttachInfo> attachInfos = getAttachInfos(attachIds);
        mAdapter.addItems(attachInfos, true);
    }

    /***
     * 预加载指定附件，先占位不访问网络
     *
     * @param localPath
     */
    public void preLoadFileByLocalPath(String localPath) {
        AttachInfo attachInfo = new AttachInfo();
        attachInfo.setLocalPathUpdateType(localPath);
        List<AttachInfo> attachInfos = new ArrayList<>();
        attachInfos.add(attachInfo);
        mAdapter.addItems(attachInfos, true);
    }

    private List<AttachInfo> getAttachInfos(String attachIds) {
        mAttachInfos = new ArrayList<AttachInfo>();
        if (!TextUtils.isEmpty(attachIds)) {
            String[] mAttachArr = attachIds.split(",");
            for (String attachId : mAttachArr) {
                AttachInfo attachInfo = new AttachInfo();
                if (!TextUtils.isEmpty(attachId)) {
                    attachInfo.setIdAndUpdateType(attachId);
                    mAttachInfos.add(attachInfo);
                }
            }
        }
        return mAttachInfos;
    }

    private void loadAttachsFromServer(final String attachIds,
                                       final List<AttachInfo> attachInfos) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jo = new JSONObject();
                try {
                    jo.put("attachIds", attachIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = Global.BASE_JAVA_URL + GlobalMethord.附件列表;
                StringRequest.postAsyn(url, jo, new StringResponseCallBack() {
                    @Override
                    public void onResponse(String response) {
                        Logger.i(response);
                        List<Attach> attaches = JsonUtils.ConvertJsonToList(response, Attach.class);

                        if (attaches != null) {
                            for (Attach attach : attaches) {
                                for (AttachInfo attachInfo : attachInfos) {
                                    if (attachInfo.getType() == EnumAttachType.附件号
                                            && attachInfo.getUuid().equals(attach.getUuid())) {
                                        attachInfo.setAttach(attach);
                                        break;
                                    }
                                }
                            }

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.addItems(attachInfos, true);
                                    AttachInfoCache.getInstance().put(attachIds, attachInfos);
                                }
                            });
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
        }).start();
    }


    /**
     * 设置可选取最大数量
     *
     * @param count
     */
    public void setMaxCount(int count) {
        maxCount = count + 1;
    }

    /**
     * 按下拍照按钮
     */
    private void doPickPhoto() {
        if (maxCount != null && mAdapter.getCount() == maxCount) {
            Toast.makeText(mContext, "附件数已到达最大数量", Toast.LENGTH_SHORT).show();
            return;
        }
        if (onlyCanTackPhoto) {
            SelectPhotoBiz.doTakePhoto(mContext);
        } else {
            String[] choices = new String[]{"拍照", "从相册中选择", "选择文件"};
            mDictIosPicker.show(choices);
            mDictIosPicker.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                @Override
                public void onSelected(int index) {
                    onSelectPicture(index);
                }
            });
        }
    }

    private void onSelectPicture(int which) {
        switch (which) {
            case 0:
                SelectPhotoBiz.doTakePhoto(mContext);
                break;
            case 1:
                if (maxCount != null) {
                    SelectPhotoBiz.selectPhoto(mContext, maxCount - mAdapter.getCount());
                } else {
                    SelectPhotoBiz.selectPhoto(mContext);
                }
                break;
            case 2:
                if (maxCount != null) {
                    new LFilePicker()
                            .withActivity((Activity) mContext)
                            .withRequestCode(CODE_REQUEST_SELECT_FILE)
                            .withMutilyMode(false)
                            .withTitle("选择文件")
                            .withMaxNum(maxCount - mAdapter.getCount())
                            .start();
                } else {
                    new LFilePicker()
                            .withActivity((Activity) mContext)
                            .withRequestCode(CODE_REQUEST_SELECT_FILE)
                            .withMutilyMode(false)
                            .withTitle("选择文件")
                            .start();
                }
                break;
        }
    }

    /***
     * 获取本地待上传路径
     *
     * @return
     */
    private List<String> getLocalPathList() {
        return mAdapter.getLocalPathList();
    }

    /**
     * 获取不包括新建 数据源
     */
    public List<AttachInfo> getAttachDataList() {
        return mAdapter.getAttachDataList();
    }

    /**
     * 上传图片
     *
     * @param type                         图片所在的功能：上传图片按照功能分类，服务器保存图片按照功能保存
     * @param onUploadMultipleFileListener
     */
    public void uploadImage(final String type, final IOnUploadMultipleFileListener onUploadMultipleFileListener) {
        final UploadHelper uploadHelper = UploadHelper.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadHelper.uploadMultipleFiles(type, getLocalPathList(), true,
                        new IOnUploadMultipleFileListener() {
                            @Override
                            public void onProgressUpdate(int completeCount) {
                                if (onUploadMultipleFileListener != null) {
                                    onUploadMultipleFileListener
                                            .onProgressUpdate(completeCount);
                                }
                            }

                            @Override
                            public void onComplete(String AttachIds) {
                                String existAttachIds = mAdapter.getAttachIds();
                                if (TextUtils.isEmpty(existAttachIds)) {
                                    existAttachIds = AttachIds;
                                } else if (!TextUtils.isEmpty(AttachIds)) {
                                    existAttachIds += "," + AttachIds;
                                }

                                if (onUploadMultipleFileListener != null) {
                                    onUploadMultipleFileListener
                                            .onComplete(existAttachIds);
                                }
                            }

                            @Override
                            public void onStartUpload(int sum) {
                                if (onUploadMultipleFileListener != null) {
                                    onUploadMultipleFileListener
                                            .onStartUpload(sum);
                                }
                            }
                        });
            }
        }).start();
    }

    public void onActivityiForResultImage(int requestCode, int resultCode,
                                          Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == SelectPhotoBiz.REQUESTCODE_TAKE_PHOTO) {
                String photoPath = SelectPhotoBiz.getPhotoPath(mContext);
                if (!TextUtils.isEmpty(photoPath)) {
                    Bitmap photoBitmap = BitmapHelper.zoomBitmap(photoPath,
                            100, 100);
                    if (photoBitmap != null) {
                        AttachInfo attachInfo = new AttachInfo();
                        attachInfo.setLocalPathUpdateType(photoPath);
                        mAdapter.addItem(attachInfo);
                    }
                }
            } else if (requestCode == SelectPhotoBiz.REQUESTCODE_SELECT_PHOTO) {
                List<String> pathList = SelectPhotoBiz
                        .getSelectPathListOnActivityForResult(data);
                if (pathList != null && pathList.size() != 0) {
                    List<AttachInfo> selectInfoList = new ArrayList<AttachInfo>();
                    for (String path : pathList) {
                        AttachInfo attachInfo = new AttachInfo();
                        attachInfo.setLocalPathUpdateType(path);
                        selectInfoList.add(attachInfo);
                    }
                    mAdapter.addItems(selectInfoList, false);
                }
            } else if (requestCode == CODE_REQUEST_SELECT_FILE) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                if (list != null && list.size() != 0) {
                    List<AttachInfo> selectInfoList = new ArrayList<AttachInfo>();
                    for (String path : list) {
                        AttachInfo attachInfo = new AttachInfo();
                        attachInfo.setLocalPathUpdateType(path);
                        selectInfoList.add(attachInfo);
                    }
                    mAdapter.addItems(selectInfoList, false);
                }
            }
        }
    }

    private void initHandler() {
        mHandler = new Handler(mContext.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case CODE_DOWNLOAD_ATTACH_TYPE:

                        break;

                    default:
                        break;
                }
            }
        };
    }

    public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
        this.mOnAddImageListener = onAddImageListener;
    }

    public interface OnAddImageListener {
        void onAddImageListener();
    }
}
