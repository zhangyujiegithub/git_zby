package com.biaozhunyuan.tianyi.attch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.ToastUtils;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.ximageview.XImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private XImageView mImageView;
    private ImageView ivBack;
    private ProgressBar progressBar;
    private DictIosPickerBottomDialog dialog;
    private Bitmap mBitmap;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("filepath", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("filepath")
                : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_image_details,
                container, false);
        mImageView = (XImageView) v.findViewById(R.id.image_details);
        ivBack = v.findViewById(R.id.iv_back_image_fragment);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        progressBar = (ProgressBar) v.findViewById(R.id.loading_details);
        dialog = new DictIosPickerBottomDialog(getActivity());


        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog.show("保存图片");
                return true;
            }
        });

        dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
            @Override
            public void onSelected(int index) {
                if (index == 0) {
                    saveImg();
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBitmap = ImageUtils.getBitmapByUrl(mImageUrl);
        mImageView.setImage(mBitmap);
    }


    private void saveImg() {
        try {
            if (mBitmap == null) {
                return;
            }
            String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
            String fileName = UUID.randomUUID().toString();
            File file = new File(dir + fileName + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showShort("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showShort("保存失败");
        }
    }
}
