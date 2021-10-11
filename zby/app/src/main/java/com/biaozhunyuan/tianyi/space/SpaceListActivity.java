package com.biaozhunyuan.tianyi.space;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.helper.WebviewNormalActivity;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.common.model.request.Demand;
import com.biaozhunyuan.tianyi.supportAndComment.SupportAndCommentPost;
import com.biaozhunyuan.tianyi.common.utils.ImageUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.view.BaseSelectPopupWindow;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.common.view.PullToRefreshAndLoadMoreListView;
import com.biaozhunyuan.tianyi.view.commonpupupwindow.CommonPopupWindow;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xrichtext.ImageLoader;
import cn.droidlover.xrichtext.XRichText;
import okhttp3.Request;


/**
 * 空间列表
 */

public class SpaceListActivity extends BaseActivity {

    private BoeryunHeaderView headerView;
    private PullToRefreshAndLoadMoreListView lv;

    private String ALL_LIST_URL = Global.BASE_JAVA_URL + GlobalMethord.空间列表 + "?getmytranslate=0"; //获取全部空间数据的接口
    private String FILTATE_LIST_URL = Global.BASE_JAVA_URL + GlobalMethord.空间列表过滤 + "?postType="; //根据过滤条件获取空间数据的接口
    public static boolean isResume = false;
    private Context mContext;
    private int pageIndex = 1;
    private Demand<Space> demand;
    private List<Space> spaceList = new ArrayList<>();
    private CommanAdapter adapter;
    private CommanAdapter<字典> lv1dictionaryAdapter;
    private CommanAdapter<字典> lv2dictionaryAdapter;
    private LinearLayout ll_comment;
    private DictionaryHelper dictionaryHelper;
    private BaseSelectPopupWindow popWiw;// 回复的 编辑框
    private CommonPopupWindow popupWindow;
    private List<字典> mParentDicts;
    private ListView lv_one; //筛选1级分类的listview
    private int list1Selection = 0;//默认选中的条目
    private int list2Selection = 0;//默认选中的条目
    private ListView lv_two;
    private boolean isFirst = true; //判断是否首次初始化1级分类列表
    private 字典 currentItem1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).statusBarColor(R.color.statusbar_normal).statusBarDarkFont(true).fitsSystemWindows(true).init();
        setContentView(R.layout.activity_space);
        initViews();
        initDemand();
        initData();
        getParentClassify();
        getSpaceList();
        setTouchEvent();
    }

    private void initData() {
        mContext = SpaceListActivity.this;
        dictionaryHelper = new DictionaryHelper(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResume) {
            pageIndex = 1;
            demand.src = ALL_LIST_URL;
            popupWindow = null;
            getSpaceList();
            isResume = false;
        }
    }

    /**
     * 获取一级分类选项
     */
    private void getParentClassify() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        DictData data = new DictData();
        data.setDictionaryName("dict_zone_post_type");
        data.setFilter("parent='0'");
        data.setFull(true);

        StringRequest.postAsyn(url, data, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                mParentDicts = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                mParentDicts.add(0, new 字典("", "全部"));
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    /**
     * 获取子级分类选项
     */
    private void getChildClassify(字典 selectChild, ListView lv) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        DictData data = new DictData();
        data.setDictionaryName("dict_zone_post_type");
//        data.setKey("公司空间");
        data.setFilter("parent='" + selectChild.getUuid() + "'");
        data.setFull(true);

        StringRequest.postAsyn(url, data, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                List<字典> 字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                字典 字典 = new 字典("", "全部");
                字典s.add(0, 字典);
                lv2dictionaryAdapter = getDictionaryAdapter2(字典s);
                lv.setAdapter(lv2dictionaryAdapter);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private CommanAdapter<字典> getDictionaryAdapter(List<字典> list) {
        return new CommanAdapter<字典>(list, mContext, R.layout.item_dictionary) {
            @Override
            public void convert(int position, 字典 item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, item.getName());
                LinearLayout ll_parent = viewHolder.getView(R.id.ll_parent);
                if (item.getName().equals("全部") && isFirst) {
                    ll_parent.setBackgroundColor(getResources().getColor(R.color.tv_search_grey));
                    isFirst = false;
                }
            }
        };
    }

    private CommanAdapter<字典> getDictionaryAdapter2(List<字典> list) {
        return new CommanAdapter<字典>(list, mContext, R.layout.item_dictionary2) {
            @Override
            public void convert(int position, 字典 item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_text, "· " + item.getName());
            }
        };
    }

    /**
     * 获取列表信息并展示
     */
    public void getSpaceList() {
        demand.pageIndex = pageIndex;
        demand.init(new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                spaceList = demand.data;

                lv.onRefreshComplete();
                if (pageIndex == 1) {
                    adapter = getAdapter(spaceList);
                    lv.setAdapter(adapter);
                } else {
                    adapter.addBottom(spaceList, false);
                    lv.loadCompleted();
                    if (spaceList != null && spaceList.size() == 0) {
                        lv.loadAllData();
                    }
                }
                pageIndex += 1;
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                lv.loadCompleted();
                try {
                    String status = JsonUtils.getStringValue(result, "Status");
                    String data = JsonUtils.getStringValue(result, "Data");
                    Toast.makeText(getBaseContext(), data, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void initDemand() {
        String url = ALL_LIST_URL;
        demand = new Demand<>(Space.class);
        demand.pageSize = 10;
        demand.dictionaryNames = "creatorId.base_staff,creatorDepartmentId.base_department";
        demand.src = url;
    }


    private void setTouchEvent() {
        headerView.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickListener() {
            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {
                initPopupWindow();
            }

            @Override
            public void onClickSaveOrAdd() {
                Intent intent = new Intent(SpaceListActivity.this, SpaceNewActivity.class);
                startActivity(intent);
            }
        });//头部布局点击事件


        lv.setOnRefreshListener(new PullToRefreshAndLoadMoreListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getSpaceList();
            }
        });


        lv.setOnLoadMore(new PullToRefreshAndLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getSpaceList();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startInfoActivity((Space) adapter.getDataList().get(position - 1));
            }
        });

    }

    private void initViews() {
        headerView = findViewById(R.id.header_space_list);
        lv = findViewById(R.id.lisview);
    }

    private void initPopupWindow() {
        if (popupWindow == null) {
            isFirst = true;
            popupWindow = new CommonPopupWindow.Builder(mContext)
                    //设置PopupWindow布局
                    .setView(R.layout.popup_spacelist)
                    //设置宽高
                    .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    //设置动画
                    .setAnimationStyle(R.style.AnimDown)
                    //设置背景颜色，取值范围0.0f-1.0f 值越小越暗 1.0f为透明
                    .setBackGroundLevel(1.0f)
                    //设置PopupWindow里的子View及点击事件
                    .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                        @Override
                        public void getChildView(View view, int layoutResId) {
                            lv_one = view.findViewById(R.id.lv_one);
                            lv_two = view.findViewById(R.id.lv_two);
                            lv1dictionaryAdapter = getDictionaryAdapter(mParentDicts);
                            lv_one.setAdapter(lv1dictionaryAdapter);
                            lv_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    list1Selection = position;
                                    for (int i = 0; i < parent.getCount(); i++) { //给选中的条目设置背景色
                                        View v = parent.getChildAt(i);
                                        if (position == i) {
                                            v.setBackgroundColor(getResources().getColor(R.color.tv_search_grey));
                                        } else {
                                            v.setBackgroundColor(Color.WHITE);
                                        }
                                    }
                                    currentItem1 = lv1dictionaryAdapter.getItem(position);
                                    if (currentItem1.getName().equals("全部")) { //如果选择全部,则获取空间列表
                                        pageIndex = 1;
                                        demand.src = ALL_LIST_URL;
                                        getSpaceList();
                                        list2Selection = 0; //二级分类的listview 选中状态和adapter清空
                                        if (lv2dictionaryAdapter != null) {
                                            lv2dictionaryAdapter.clearData();
                                            lv2dictionaryAdapter.notifyDataSetChanged();
                                        }
                                        popupWindow.dismiss();
                                    } else { //添加筛选条件,过滤列表
                                        getChildClassify(currentItem1, lv_two);
                                    }
                                }
                            });
                            lv_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    list2Selection = position;
                                    for (int i = 0; i < parent.getCount(); i++) { //给选中的条目设置背景色
                                        View v = parent.getChildAt(i);
                                        if (position == i) {
                                            v.setBackgroundColor(getResources().getColor(R.color.tv_search_grey));
                                        } else {
                                            v.setBackgroundColor(Color.WHITE);
                                        }
                                    }
                                    if (lv2dictionaryAdapter != null) {
                                        字典 item = lv2dictionaryAdapter.getItem(position);
                                        String url = "";
                                        if (item.getName().equals("全部")) {
                                            url = FILTATE_LIST_URL + currentItem1.getUuid();
                                        } else {
                                            url = FILTATE_LIST_URL + item.getUuid();
                                        }
                                        popupWindow.dismiss();
                                        pageIndex = 1;
                                        demand.src = url;
                                        getSpaceList();
                                    }
                                }
                            });
                        }
                    })
                    //设置外部是否可点击 默认是true
                    .setOutsideTouchable(true)
                    //开始构建
                    .create();
        } else {
            lv_one.setSelection(list1Selection);
            lv_two.setSelection(list2Selection);
        }
        popupWindow.showAsDropDown(headerView);
    }

    private CommanAdapter<Space> getAdapter(List<Space> list) {
        return new CommanAdapter<Space>(list, getBaseContext(), R.layout.item_spacelist_new1) {
            @Override
            public void convert(int position, final Space item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.tv_space_title, item.getPostTitle());
                viewHolder.setTextValue(R.id.tv_post_type_name, item.getPostTypeName());
                XRichText tvContent = viewHolder.getView(R.id.tv_content_notice_item);
                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startInfoActivity(item);
                    }
                });
                tvContent.callback(new XRichText.BaseClickCallback() {
                    @Override
                    public void onImageClick(List<String> urlList, int position) {
                        super.onImageClick(urlList, position);
                        startInfoActivity(item);
                    }

                    @Override
                    public boolean onLinkClick(String url) {
                        return super.onLinkClick(url);
                    }

                    @Override
                    public void onFix(XRichText.ImageHolder holder) {
                        super.onFix(holder);
                    }
                }).imageDownloader(new ImageLoader() {
                    @Override
                    public Bitmap getBitmap(String url) throws IOException {
                        return ImageUtils.getBitmapByUrl(Global.BASE_JAVA_URL + url);
                    }
                }).text(item.getTextPart());
                viewHolder.setTextValue(R.id.tv_creater_notice_item, dictionaryHelper.getUserNameById(item.getCreatorId())); //创建人名称
                viewHolder.setTextValue(R.id.tv_time_notice_item, ViewHelper.getDateStringFormat(item.getCreateTime())); //创建时间
                viewHolder.setUserPhoto(R.id.iv_head_item_notice_list, item.getCreatorId());//创建人头像
                MultipleAttachView attachView = viewHolder.getView(R.id.multi_attach_notice_item);
                if (TextUtils.isEmpty(item.getFileAttachmentIds())) {
                    attachView.setVisibility(View.GONE);
                } else {
                    attachView.setVisibility(View.VISIBLE);
                    attachView.loadImageByAttachIds(item.getFileAttachmentIds());
                }

                //新加: 点赞和评论

                LinearLayout ll_support = viewHolder.getView(R.id.nl_item_log_support);//点赞
                ll_comment = viewHolder.getView(R.id.nl_item_log_comment);

                ll_comment.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        popWiw(item);
                    }
                });

                //点赞/取消赞
                ll_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SupportAndCommentPost post = new SupportAndCommentPost();
                        post.setFromId(Global.mUser.getUuid());
                        post.setToId(item.getCreatorId());
                        post.setDataType("帖子");
                        post.setDataId(item.getUuid());
                        if (item.isLike()) { //取消点赞
                            cancleSupport(post, item);
                        } else { //点赞
                            support(post, item);
                        }
                    }
                });


                final ImageView iv_support = viewHolder.getView(R.id.iv_item_log_support);
                final TextView tv_support = viewHolder.getView(R.id.tv_support_count_log_item);
                TextView tv_comment = viewHolder.getView(R.id.tv_comment_count_log_item);

                if (item.isLike()) {
                    iv_support.setImageResource(R.drawable.icon_support_select);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text_like));
                } else {
                    iv_support.setImageResource(R.drawable.icon_support);
                    tv_support.setTextColor(getResources().getColor(R.color.color_support_text));
                }
                tv_support.setText(item.getLikeNumber() + "");
                tv_comment.setText(item.getCommentNumber() + "");
            }
        };
    }

    private void startInfoActivity(Space item) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.空间详情H5 + item.getUuid();
        Intent intent = new Intent(mContext, WebviewNormalActivity.class);
        intent.putExtra(WebviewNormalActivity.EXTRA_URL, url);
        startActivity(intent);
    }

    private void popWiw(final Space item) {

        popWiw = new BaseSelectPopupWindow(getBaseContext(), R.layout.edit_data);
        // popWiw.setOpenKeyboard(true);
        popWiw.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWiw.setFocusable(true);
        popWiw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWiw.setShowTitle(false);
        popWiw.setBackgroundDrawable(new ColorDrawable(0));
        InputMethodManager im = (InputMethodManager) getBaseContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final TextView send = popWiw.getContentView().findViewById(
                R.id.btn_send);
        final TextEditTextView edt = popWiw.getContentView().findViewById(
                R.id.edt_content);

        edt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        edt.setImeOptions(EditorInfo.IME_ACTION_SEND);

        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (TextUtils.isEmpty(edt.getText())) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                        String content = edt.getText().toString().trim();


                        comment(content, item);
                        popWiw.dismiss();
                    }
                    return true;
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edt.getText().toString().trim())) {
                    // /提交内容
                    String content = edt.getText().toString().trim();

                    SupportAndCommentPost post = new SupportAndCommentPost();
                    post.setFromId(Global.mUser.getUuid());
                    post.setToId(item.getCreatorId());
                    post.setDataType("帖子");
                    post.setDataId(item.getUuid());
                    post.setContent(content);
                    comment(content, item);
                    popWiw.dismiss();
                }
            }
        });
        popWiw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });


        popWiw.showAtLocation(getLayoutInflater().inflate(R.layout.fragment_personallist, null), Gravity.BOTTOM
                | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 评论
     *
     * @param content
     */
    public void comment(String content, final Space space) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.日志添加评论;
        hideShowSoft();

        JSONObject ob = new JSONObject();
        try {
            ob.put("dataType", "帖子");
            ob.put("dataId", space.getUuid());
            ob.put("content", content);
            ob.put("toId", space.getCreatorId());
            ob.put("fromId", Global.mUser.getUuid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest.postAsyn(url, ob, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), "评论成功", Toast.LENGTH_SHORT).show();
                space.setCommentNumber(space.getCommentNumber() + 1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                Log.e("tag", "评论失败");
            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }


    /**
     * 点赞
     *
     * @param post
     * @param record
     */
    private void support(SupportAndCommentPost post, final Space record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() + 1);
                record.setLike(true);
                adapter.notifyDataSetChanged();
//                ll_bottom.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    /**
     * 取消点赞
     *
     * @param post   要取消点赞的实体的ID
     * @param record
     */
    private void cancleSupport(SupportAndCommentPost post, final Space record) {
        String url = Global.BASE_JAVA_URL + GlobalMethord.取消点赞;

        StringRequest.postAsyn(url, post, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getBaseContext(), "取消点赞成功", Toast.LENGTH_SHORT).show();
                record.setLikeNumber(record.getLikeNumber() - 1);
                record.setLike(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    /**
     * 如果输入法已经在屏幕上显示，则隐藏输入法，反之则显示
     */
    private void hideShowSoft() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
