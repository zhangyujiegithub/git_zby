package com.biaozhunyuan.tianyi.common.attach;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.common.R;
import com.biaozhunyuan.tianyi.common.helper.AttachBiz;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.global.FilePathConfig;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;

import java.io.File;
import java.util.List;

public class DownloadAdapter extends BaseAdapter {

    private Context mContext;
    private ListView mLv;
    private List<Attach> mList;
    private BoeryunDownloadManager mDownloadManager;
    private int headerCount = 0;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            DownloadFile downloadFile = (DownloadFile) msg.obj;

            int pos = getUpdatePos(downloadFile.atttachId);

            if (pos != -1) {
                Attach attach = mList.get(pos);
                attach.totalSize = downloadFile.totalSize;
                attach.downloadSize = downloadFile.downloadSize;
                attach.downloadState = downloadFile.downloadState;

                // notifyDataSetChanged会执行getView函数，更新所有可视item的数据
                // notifyDataSetChanged();
                // 只更新指定item的数据，提高了性能
                updateView(pos, attach);
            }
        }

        ;
    };

    /**
     * @param mContext
     * @param mList
     */
    public DownloadAdapter(Context mContext, List<Attach> mList, ListView lv) {
        this.mContext = mContext;
        this.mList = mList;
        this.mLv = lv;
        mDownloadManager = BoeryunDownloadManager.getInstance();
        mDownloadManager.setHandler(mHandler);
    }

    public void addBottom(List<Attach> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setHeaderCount(int count) {
        headerCount = count;
    }

    // 更新指定item的数据
    private void updateView(int offset, Attach attach) {

        View view = mLv.getChildAt(offset + headerCount);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder.pBar.getProgress() != 0) {
            holder.pBar.setMax(attach.totalSize);
        }
        holder.pBar.setProgress(attach.downloadSize);
        holder.tvAttchmentName.setText(attach.filename);
        String downloadSize = String.format("%.2f",
                (attach.downloadSize * 1.0f / 1024 / 1024));
        String totoalSize = String.format("%.2f",
                (attach.totalSize * 1.0f / 1024 / 1024));
        holder.tvSize.setText(downloadSize + "/");
        holder.tvTotal.setText(totoalSize + "MB");
        // Drawable drawable = mContext.mContext.getResources().getDrawable(
        // R.drawable.app_icon);
        // holder.icon.setImageDrawable(drawable);

        switch (attach.downloadState) {
            case BoeryunDownloadManager.DOWNLOAD_STATE_DOWNLOADING:
                holder.btnDown.setText("下载中");
                holder.tvStatus.setText("下载中");
                // this.changeBtnStyle(holder.btn, false);
                break;
            case BoeryunDownloadManager.DOWNLOAD_STATE_FINISH:
                holder.btnDown.setText("打开");
                holder.tvStatus.setText("已下载");

                holder.tvSize.setText("");
                holder.tvTotal.setText("");
                holder.pBar.setVisibility(View.INVISIBLE);
                // this.changeBtnStyle(holder.btn, false);
                break;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Attach getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.item_attchment_download, null);
            ViewHolder vh = new ViewHolder();
            vh.tvAttchmentName = (TextView) view
                    .findViewById(R.id.download_attchment_name);
            vh.ivAttachIco = (ImageView) view
                    .findViewById(R.id.download_attchment_ico);
            vh.tvStatus = (TextView) view.findViewById(R.id.tv_status_download);
            vh.tvSize = (TextView) view.findViewById(R.id.tv_size_download);
            vh.tvTotal = (TextView) view
                    .findViewById(R.id.tv_filetotal_download);
            vh.pBar = (ProgressBar) view
                    .findViewById(R.id.pbar_download_attach);
            vh.rlFileName = view.findViewById(R.id.rl_filename);
            vh.btnDown = (Button) view.findViewById(R.id.btn_download_attach);
            view.setTag(vh);
        }

        final ViewHolder vHolder = (ViewHolder) view.getTag();

        final Attach item = mList.get(position);

        if (checkEndsWithInStringArray(item.filename, mContext.getResources()
                .getStringArray(R.array.fileEndingImage))) {  //如果附件是图片类型，直接显示
            ImageUtils.displyImage(Global.BASE_JAVA_URL + GlobalMethord.显示附件 + item.getUuid(), vHolder.ivAttachIco);
            vHolder.tvAttchmentName.setText(item.filename + "");
            vHolder.tvStatus.setVisibility(View.INVISIBLE);
            vHolder.btnDown.setVisibility(View.INVISIBLE);
            vHolder.rlFileName.setVisibility(View.VISIBLE);
        } /*else if (checkEndsWithInStringArray(item.filename, mContext.getResources()
                .getStringArray(R.array.fileEndingPdf))) { //pdf格式
            vHolder.ivAttachIco.setImageResource(R.drawable.ico_pdf);
            vHolder.tvAttchmentName.setText(item.filename + "");
            vHolder.tvStatus.setVisibility(View.INVISIBLE);
            vHolder.btnDown.setVisibility(View.INVISIBLE);
            vHolder.rlFileName.setVisibility(View.VISIBLE);
        }*/ else { //不是图片类型需要下载
            String suffix = item.suffix;
            vHolder.tvAttchmentName.setText(item.getName());
            vHolder.tvAttchmentName.setText(item.filename + "");
            vHolder.tvStatus.setVisibility(View.VISIBLE);
            vHolder.btnDown.setVisibility(View.VISIBLE);
            vHolder.rlFileName.setVisibility(View.VISIBLE);
            if (new File(FilePathConfig.getCacheDirPath() + File.separator
                    + item.getUuid() + "_" + item.filename).exists()) {
                vHolder.tvStatus.setText("已下载");
                vHolder.btnDown.setText("打开");
                vHolder.tvSize.setText("");
                vHolder.tvTotal.setText("");
                vHolder.pBar.setVisibility(View.INVISIBLE);
                item.downloadState = BoeryunDownloadManager.DOWNLOAD_STATE_FINISH;
            } else {
                vHolder.tvStatus.setText("未下载");
                vHolder.btnDown.setText("下载");
                item.downloadState = BoeryunDownloadManager.DOWNLOAD_STATE_NORMAL;
            }
            setIco(suffix, vHolder.ivAttachIco);

            vHolder.btnDown.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** 已下载完成直接打开 */
                    if (item.downloadState == BoeryunDownloadManager.DOWNLOAD_STATE_FINISH) {
                        open(item.getUuid() + "_" + item.getName());
                    } else {
                        DownloadFile downloadFile = new DownloadFile();
                        downloadFile.atttachId = item.getUuid();
                        downloadFile.attachName = item.getUuid() + "_" + item.filename;
                        downloadFile.totalSize = item.totalSize;
                        downloadFile.downloadSize = item.downloadSize;
//                    downloadFile.url = "http://www.boeryun.com:8076/Upload/Upload2017/127/8/5/31/1496240031385.7.xlsx";
                        downloadFile.url = Global.BASE_JAVA_URL + GlobalMethord.显示附件 + item.getUuid();
                        mDownloadManager.download(downloadFile);
                        downloadFile.downloadState = BoeryunDownloadManager.DOWNLOAD_STATE_WAITING;
                        vHolder.btnDown.setText("排队中");
                        vHolder.pBar.setVisibility(View.VISIBLE);
                        // mDownloadManager.download(filepath, fileName, pBar)
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取下载完成需要修改了pos 获取失败返回-1
     */
    private int getUpdatePos(String attachId) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).uuid.equals(attachId)) {
                return i;
            }
        }
        return -1;
    }

    private void setIco(String suffix, ImageView iv) {
        if (!TextUtils.isEmpty(suffix)) {
            iv.setImageResource(AttachBiz.getImageResoureIdBySuffix(suffix));
        }
    }

    // 定义用于检查要打开的附件文件的后缀是否在遍历后缀数组中
    private boolean checkEndsWithInStringArray(String checkItsEnd,
                                               String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.toLowerCase().endsWith(aEnd.toLowerCase()))
                return true;
        }
        return false;
    }

    /***
     * 打开附件文件的方法
     *
     * @param name
     *            后缀名
     */
    private void open(String name) {
        String fileName = FilePathConfig.getCacheDirPath() + File.separator
                + name;
        File currentPath = new File(fileName);
        if (currentPath != null && currentPath.isFile()) {
            Logger.i("pathname" + "-->" + fileName);
            Intent intent = null;
            if (checkEndsWithInStringArray(fileName, mContext.getResources()
                    .getStringArray(R.array.fileEndingImage))) {
                intent = OpenFilesIntent.getImageFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingWebText))) {
                intent = OpenFilesIntent.getHtmlFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingPackage))) {
                intent = OpenFilesIntent.getApkFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingAudio))) {
                intent = OpenFilesIntent.getAudioFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingVideo))) {
                intent = OpenFilesIntent.getVideoFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingText))) {
                intent = OpenFilesIntent.getTextFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingPdf))) {
                intent = OpenFilesIntent.getPdfFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingWord))) {
//                intent = OpenFilesIntent.getWordFileIntent(currentPath);
                intent = OpenFilesIntent.getWpsFileIntent(currentPath, mContext);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingExcel))) {
                intent = OpenFilesIntent.getExcelFileIntent(currentPath);
            } else if (checkEndsWithInStringArray(fileName, mContext
                    .getResources().getStringArray(R.array.fileEndingPPT))) {
                intent = OpenFilesIntent.getPPTFileIntent(currentPath);
            } else {
                intent = OpenFilesIntent.getOtherFileIntent(currentPath);
            }

            if (intent != null) {
                try {
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("Open" + e.getMessage() + "");
                    Toast.makeText(mContext, "系统未检测到打开文件的程序，请选择",
                            Toast.LENGTH_LONG).show();
                    intent = OpenFilesIntent.getOtherFileIntent(currentPath);
                    try {
                        mContext.startActivity(intent);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Logger.e("Open2" + e.getMessage() + "");
                    }
                }
            }
        } else {
            Toast.makeText(mContext, "抱歉,这不是一个合法文件！", Toast.LENGTH_LONG).show();
        }
    }

    private class ViewHolder {
        TextView tvAttchmentName;
        TextView tvStatus;
        ImageView ivAttachIco;
        TextView tvSize;
        TextView tvTotal;
        RelativeLayout rlFileName;
        ProgressBar pBar;
        Button btnDown;
    }
}
