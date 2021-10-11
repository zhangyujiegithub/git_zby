package com.biaozhunyuan.tianyi.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.biaozhunyuan.tianyi.common.helper.AttachBiz;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.view.NoScrollListView;
import com.biaozhunyuan.tianyi.common.attach.Attach;
import com.biaozhunyuan.tianyi.common.attach.DownloadAdapter;
import com.biaozhunyuan.tianyi.attch.PdfActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;


/**
 * Created by 王安民 on 2017/8/30.
 * 显示附件列表帮助类
 */

public class AttachPlayHelper {

    private String attachIds; //附件ids
    private NoScrollListView lv; //显示附件的列表
    private Activity context;
    private DownloadAdapter adapter;

    public AttachPlayHelper(Activity context, String attachIds, NoScrollListView listView) {
        this.attachIds = attachIds;
        this.lv = listView;
        this.context = context;
        if (!TextUtils.isEmpty(attachIds)) {
            getAttact();
        }
    }

    private void getAttact() {
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
                final List<Attach> attaches = JsonUtils.ConvertJsonToList(response, Attach.class);
                adapter = new DownloadAdapter(context, attaches, lv);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Attach attach = attaches.get(position);
                        if (checkEndsWithInStringArray(attach.filename, context.getResources()
                                .getStringArray(R.array.fileEndingImage))) {  //如果附件是图片类型，直接显示
                            ImageUtils.startSingleImageBrower(context, ImageUtils.getDownloadUrlById(attach.uuid));
                        } else if (checkEndsWithInStringArray(attach.filename, context.getResources()
                                .getStringArray(R.array.fileEndingPdf))) { //pdf格式
                            Intent intent = new Intent(context, PdfActivity.class);
                            intent.putExtra("filepath", ImageUtils.getDownloadUrlById(attach.uuid));
                            intent.putExtra("title", attach.filename);
                            context.startActivity(intent);
                        }
                    }
                });

//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Attach attach = attaches.get(position);
//                        String name = attach.getName();
//                        if (!name.startsWith("https://view.officeapps.live.com/op/view.aspx?src=")
//                                && (name.endsWith(".doc") || name.endsWith(".docx")
//                                || name.endsWith(".xls") || name.endsWith(".xlsx")
//                                || name.endsWith(".ppt") || name.endsWith(".pptx"))) {//如果是微软可以预览的文件类型，拼接url
//                            String url = "https://view.officeapps.live.com/op/view.aspx?src=" + Global.BASE_JAVA_URL + GlobalMethord.显示附件 + attach.getUuid();
//                            Intent intent = new Intent(context, WebviewNormalActivity.class);
//                            intent.putExtra(WebviewNormalActivity.EXTRA_TITLE, name);
//                            intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
//                            context.startActivity(intent);
//                        }
//                    }
//                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private CommanAdapter<Attach> getAdapter(List<Attach> attaches) {
        return new CommanAdapter<Attach>(attaches, context, R.layout.item_attacth_list) {
            @Override
            public void convert(int position, Attach item, BoeryunViewHolder viewHolder) {
                int id = AttachBiz.getImageResoureIdBySuffix(item.suffix);
                viewHolder.setImageResoure(R.id.iv_attach_list, id);
                String url = Global.BASE_JAVA_URL + GlobalMethord.显示附件 + item.getUuid();
                viewHolder.setImageByUrl(R.id.iv_attach_list, url);
                viewHolder.setTextValue(R.id.name_attach_list, item.getName());
            }
        };
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

}
