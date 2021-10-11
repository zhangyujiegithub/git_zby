package com.biaozhunyuan.tianyi.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.apply.TagAdapter;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.utils.LogUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.FlowLayout;
import com.biaozhunyuan.tianyi.view.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.Request;

public class ClientAddTagActivity extends AppCompatActivity {
    public static final String CLIENT_TAG = "add_clinet_tag";
    private FlowLayout flowLayout;//上面的flowLayout
    private TagFlowLayout allFlowLayout;//所有标签的TagFlowLayout
    private List<String> label_list = new ArrayList<String>();//上面的标签列表
    private List<String> all_label_List = new ArrayList<String>();//所有标签列表
    final List<TextView> labels = new ArrayList<TextView>();//存放标签
    final List<Boolean> labelStates = new ArrayList<Boolean>();//存放标签状态
    final Set<Integer> set = new HashSet<Integer>();//存放选中的
    private TagAdapter<String> tagAdapter;//标签适配器
    private String tagStr = "";
    private LinearLayout.LayoutParams params;
    private EditText editText;
    private ImageView iv_back;
    private ImageView iv_save;
    private int tagType;
        private List<TagResponse> tagResponses = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_add_tag);
        initView();
        initData();
        initEdittext();

        setOnClickListener();


    }

    private void setOnClickListener() {
        /**
         * 点击上边的flowlayout添加自定义标签
         */
        flowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextContent = editText.getText().toString();
                if (TextUtils.isEmpty(editTextContent)) {
                    tagNormal();
                } else {
                    addLabel(editText);
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 保存标签
         */
        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextContent = editText.getText().toString();
                if (TextUtils.isEmpty(editTextContent)) {
                    tagNormal();
                } else {
                    addLabel(editText);
                }
                String str = "";
                for (TextView textView : labels) {
                    String tag = textView.getText().toString().trim();
                    if (labels.indexOf(textView) != labels.size() - 1) {
                        str = str + tag + ",";
                    } else {
                        str = str + tag;
                    }
                    if (!TextUtils.isEmpty(tagStr) && tagStr.contains("," + tag + ",")) {
                        continue;
                    }
                    postTag(tag);
                }
                Intent intent = new Intent();
                intent.putExtra(CLIENT_TAG, str);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        flowLayout = (FlowLayout) findViewById(R.id.addtag_layout_client);
        allFlowLayout = (TagFlowLayout) findViewById(R.id.tag_layout_client);
        iv_back = (ImageView) findViewById(R.id.iv_back_add_clients);
        iv_save = (ImageView) findViewById(R.id.iv_save_add_client_tags);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        tagType = getIntent().getExtras().getInt("TAG_TYPE");
        if (tagType != 0) {
            final Tag tag = new Tag();
            tag.setCorp(15);
//            Tag.Entity entity = new Tag.Entity();
//            entity.set分类(tagType);
            tag.set分类(tagType);

            LogUtils.i("ClientAddTagActivity____________________", tag.getCorp()+"");


            String url = Global.BASE_JAVA_URL + GlobalMethord.查询标签;

            StringRequest.postAsyn(url, tag, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {

                    try {
                        String result = JsonUtils.getStringValue(response, "Data");
                        tagResponses = JsonUtils.jsonToArrayEntity(result, TagResponse.class);

                        if (tagResponses != null && tagResponses.size() > 0) {
                            for (int i = 0; i < tagResponses.size(); i++) {
                                for (String s : tagResponses.get(i).getName().split(",")) {
                                    if (!all_label_List.contains(s)) {
                                        all_label_List.add(s);
                                        tagStr += s + ",";
                                    }
                                }
                            }
                            tagStr = "," + tagStr;
                            initAllLeblLayout();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    set1.addAll(all_label_List);
//                    all_label_List.clear();
//                    all_label_List.addAll(set1);

                }

                @Override
                public void onFailure(Request request, Exception ex) {
                    Toast.makeText(ClientAddTagActivity.this,"获取失败1",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponseCodeErro(String res) {
                    Toast.makeText(ClientAddTagActivity.this,"获取失败2",Toast.LENGTH_SHORT).show();

                }
            });
        }
//        all_label_List.addAll(set1);
//        all_label_List.add("CRM定制开发");
//        all_label_List.add("OA系统");
//        all_label_List.add("理财产品");
//        all_label_List.add("金融行业");
//        all_label_List.add("畜牧行业");
//        all_label_List.add("功能完善");
//        all_label_List.add("波尔云");
//        all_label_List.add("闪电办公");
//        all_label_List.add("效率高");
//        all_label_List.add("界面美观");

        for (int i = 0; i < label_list.size(); i++) {
            editText = new EditText(getApplicationContext());//new 一个EditText
            editText.setText(label_list.get(i));
            addLabel(editText);//添加标签
        }

    }


    /**
     * 初始化默认的添加标签
     */
    private void initEdittext() {
        editText = new EditText(getApplicationContext());
        editText.setHint("添加标签");
        //设置固定宽度
        editText.setMinEms(4);
        editText.setTextSize(12);
        //设置shape
        editText.setBackgroundResource(R.drawable.flag_edit);
        editText.setHintTextColor(Color.parseColor("#b4b4b4"));
        editText.setTextColor(Color.parseColor("#000000"));
        //设置固定宽度
        editText.setMinEms(4);
        editText.setTextSize(12);
        //设置shape
        editText.setLayoutParams(params);
        //添加到layout中
        flowLayout.addView(editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tagNormal();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }




    /**
     * 初始化所有标签列表
     */
    private void initAllLeblLayout() {
        //初始化适配器
        tagAdapter = new TagAdapter<String>(all_label_List) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.flag_adapter,
                        allFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        };

        allFlowLayout.setAdapter(tagAdapter);

        //根据上面标签来判断下面的标签是否含有上面的标签
        for (int i = 0; i < label_list.size(); i++) {
            for (int j = 0; j < all_label_List.size(); j++) {
                if (label_list.get(i).equals(
                        all_label_List.get(j))) {
                    int[] arr = {i};
                    tagAdapter.setSelectedList(arr);//设为选中
                }
            }
        }
        tagAdapter.notifyDataChanged();


        //给下面的标签添加监听
        allFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (labels.size() == 0) {
                    editText.setText(all_label_List.get(position));
                    addLabel(editText);
                    return false;
                }
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < labels.size(); i++) {
                    list.add(labels.get(i).getText().toString());
                }
                //如果上面包含点击的标签就删除
                if (list.contains(all_label_List.get(position))) {
                    for (int i = 0; i < list.size(); i++) {
                        if (all_label_List.get(position).equals(list.get(i))) {
                            flowLayout.removeView(labels.get(i));
                            labels.remove(i);
                            tagAdapter.setDeleteList(position);
                        }
                    }

                } else {
                    editText.setText(all_label_List.get(position));
                    addLabel(editText);
                }

                return false;
            }
        });

        //已经选中的监听
        allFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {

            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                set.clear();
                set.addAll(selectPosSet);
            }
        });
    }

    /**
     * 添加标签
     *
     * @param editText
     * @return
     */
    private boolean addLabel(EditText editText) {
        String editTextContent = editText.getText().toString();
        //判断输入是否为空
        if (editTextContent.equals(""))
            return true;
        //判断是否重复
        for (TextView tag : labels) {
            String tempStr = tag.getText().toString();
            if (tempStr.equals(editTextContent)) {
                editText.setText("");
                editText.requestFocus();
                return true;
            }
        }
        for (int i = 0; i < all_label_List.size(); i++) {
            if (editTextContent.equals(all_label_List.get(i))) {
                tagAdapter.setSelectedList(i);
                tagAdapter.notifyDataChanged();
            }
        }
        //添加标签
        final TextView temp = getTag(editText.getText().toString());
        labels.add(temp);
        labelStates.add(false);
        //添加点击事件，点击变成选中状态，选中状态下被点击则删除
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curIndex = labels.indexOf(temp);
                if (!labelStates.get(curIndex)) {
                    //显示 ×号删除
                    temp.setText(temp.getText() + " ×");
                    temp.setBackgroundResource(R.drawable.label_del);
                    temp.setTextColor(Color.parseColor("#ffffff"));
                    //修改选中状态
                    labelStates.set(curIndex, true);
                } else {
                    delByTest(temp.getText().toString());
                    flowLayout.removeView(temp);
                    labels.remove(curIndex);
                    labelStates.remove(curIndex);
                    for (int i = 0; i < label_list.size(); i++) {
                        for (int j = 0; j < labels.size(); j++) {
                            if (label_list.get(i).equals(
                                    labels.get(j).getText())) {
                                int[] arr = {i};
                                tagAdapter.setSelectedList(arr);
                            }
                        }
                    }
                    tagAdapter.notifyDataChanged();
                }
            }
        });
        flowLayout.addView(temp);
        //让输入框在最后一个位置上
        editText.bringToFront();
        //清空编辑框
        editText.setText("");
        editText.requestFocus();
        return true;

    }

    /**
     * 根据字符删除标签
     *
     * @param text
     */
    private void delByTest(String text) {

        for (int i = 0; i < all_label_List.size(); i++) {
            String a = all_label_List.get(i) + " ×";
            if (a.equals(text)) {
                set.remove(i);
            }
        }
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(set);
        int[] arr = new int[set.size()];
        for (int i = 0; i < list.size(); i++) {
            int j = list.get(i);
            arr[i] = j;
        }
        tagAdapter.setSelectedList(arr);//重置选中的标签

    }


    /**
     * 标签恢复到正常状态
     */
    private void tagNormal() {
        //输入文字时取消已经选中的标签
        for (int i = 0; i < labelStates.size(); i++) {
            if (labelStates.get(i)) {
                TextView tmp = labels.get(i);
                tmp.setText(tmp.getText().toString().replace(" ×", ""));
                labelStates.set(i, false);
                tmp.setBackgroundResource(R.drawable.label_normal);
                tmp.setTextColor(Color.parseColor("#ABDC47"));
            }
        }
    }


    /**
     * 创建一个正常状态的标签
     *
     * @param label
     * @return
     */
    private TextView getTag(String label) {
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(12);
        textView.setBackgroundResource(R.drawable.label_normal);
        textView.setTextColor(Color.parseColor("#01C0C8"));
        textView.setText(label);
        textView.setLayoutParams(params);
        return textView;
    }


    /**
     * 向服务器提交对应的标签
     *
     * @param str 标签的字符串 ： 美观，大方，得体
     */
    private void postTag(String str) {
        if (tagType != 0) {
            Tag tag = new Tag();
            tag.setCorp(15);
//            Tag.Entity entity = new Tag.Entity();
//            entity.set分类(tagType);
//            entity.set名称(str);
            tag.set分类(tagType);
            LogUtils.i("ClientAddTagActivity", tag.toString());
            String url = Global.BASE_JAVA_URL + GlobalMethord.保存标签;


            StringRequest.postAsyn(url, tag, new StringResponseCallBack() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(ClientAddTagActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    LogUtils.i("ClientAddTagActivity", response);
                }

                @Override
                public void onFailure(Request request, Exception ex) {
                    Toast.makeText(ClientAddTagActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponseCodeErro(String result) {
                    Toast.makeText(ClientAddTagActivity.this,"系统繁忙",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
