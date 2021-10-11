package com.biaozhunyuan.tianyi.space;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.model.DictData;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.ProgressDialogHelper;
import com.biaozhunyuan.tianyi.helper.SelectLocationBiz;
import com.biaozhunyuan.tianyi.common.helper.ViewHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.notice.SelectedDepartmentActicity;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.AlertDialog;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.VoiceInputView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.notice.NoticeNewActivity.REQUEST_SELECT_PARTICIPANT;
import static com.biaozhunyuan.tianyi.notice.NoticeNewActivity.REQUEST_SELECT_SECTION;
import static com.biaozhunyuan.tianyi.notice.SelectedDepartmentActicity.RESULT_SELECT_USER;

/**
 * 新建空间
 */

public class SpaceNewActivity extends AppCompatActivity {
    public static final int RESULT_CODE_FAILED = 1;
    public static final int EDIT_CONTENT_CODE = 7;

    private static final String 个人空间 = "00b62d99fbf14c99ab0111dfb3e759d0";
    private static final String 公司空间 = "00b62d99fbf14c99ab0111dfb3e26878";
    private static final String 部门空间 = "00b62d99fbf14c99ab0111df57687980";
    private String URL = Global.BASE_JAVA_URL + GlobalMethord.发布空间;

    DictIosPickerBottomDialog dpbDialog;
    Toast toast;
    /**
     * 可见区域类型
     */
    final String[] mAreaTypeArrs = new String[]{"全体", "选择员工", "选择部门"};
    private boolean FLAG;
    private Context mContext;
    private ImageView ivBack;
    private TextView ivSave;
    private EditText mEditTextContent;
    private VoiceInputView inputView;
//    private DictWheelPicker picker;

    /**
     * 可见区域
     */
    private LinearLayout llArea;
    private TextView tvArea;
    private ImageView ivArea;
    private LinearLayout llLocation;
    private LinearLayout llBottom;
    private TextView tv_title;

    private RelativeLayout rl_record;
    private RelativeLayout rl_location;

    private int currentPos = -1;
    private Space space = new Space();
    private List<字典> mParentDicts;
    private 字典 selectParent;
    private List<字典> mChildDicts;
    private 字典 selectChild;

    /**
     * 可见范围类型
     */

    private DictIosPickerBottomDialog mDictIosPickerBottomDialog;
    String mContent = "";
    String mReleaseTime = "";
    String mTitle = "";


    private MultipleAttachView attachView;
    private List<字典> mlist = new ArrayList<>();
    private LinearLayout llArea_child;
    private TextView tvArea_child;
    private ImageView ivArea1_child;
    private List<String> strs = new ArrayList<String>();
    private EditText et_title;
    private LinearLayout llPublish;
    private TextView tvPublish;
    private DictionaryHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_space);
        FLAG = false;
        helper = new DictionaryHelper(this);
        initIntent();
        findviews();
        setonclick();
    }

    private void findviews() {

        // 初始化控件
        mContext = SpaceNewActivity.this;
        dpbDialog = new DictIosPickerBottomDialog(mContext);
        ivBack = (ImageView) findViewById(R.id.iv_back_share);
        tv_title = (TextView) findViewById(R.id.title_share_new);
        ivSave = (TextView) findViewById(R.id.iv_save_share);
        mEditTextContent = (EditText) findViewById(R.id.et_content_share);
        inputView = (VoiceInputView) findViewById(R.id.voice_input_view_share_add);
        tvArea = (TextView) findViewById(R.id.tv_area_share_new);
        llArea = (LinearLayout) findViewById(R.id.ll_select_area_share_new);
        ivArea = (ImageView) findViewById(R.id.iv_area_share_new);
        llLocation = (LinearLayout) findViewById(R.id.ll_space_info_location);
        llBottom = (LinearLayout) findViewById(R.id.ll_space_info_bottom);
        et_title = findViewById(R.id.et_title_notice_add);
        llPublish = findViewById(R.id.ll_select_publish);
        tvPublish = findViewById(R.id.tv_publish);

        attachView = findViewById(R.id.addImg_share);
        mEditTextContent.setHeight(100);
        mEditTextContent.setHint("请输入内容..");
        mReleaseTime = ViewHelper.getDateString();// 当前时间
        mDictIosPickerBottomDialog = new DictIosPickerBottomDialog(mContext);


        rl_location = (RelativeLayout) findViewById(R.id.rl_new_share_location);

        rl_record = (RelativeLayout) findViewById(R.id.rl_new_share_record);

        llArea_child = findViewById(R.id.ll_select_area_share_new_child);
        tvArea_child = findViewById(R.id.tv_area_share_new_child);
        ivArea1_child = findViewById(R.id.iv_area_share_new_child);
        attachView.loadImageByAttachIds("");
        attachView.setIsAdd(true);

        List<EditText> editList = new ArrayList<>();
        editList.add(mEditTextContent);
        editList.add(et_title);
        inputView.setRelativeListInputView(editList);
        llLocation.setVisibility(View.GONE);
        llBottom.setVisibility(View.GONE);

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
                final List<String> names = new ArrayList<>();
                if (mParentDicts != null) {
                    for (字典 dict : mParentDicts) {
                        names.add(dict.getName());
                    }
                }
                mDictIosPickerBottomDialog.show(names);

                mDictIosPickerBottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        tvArea.setText(names.get(index));
                        selectParent = mParentDicts.get(index);
                        if (selectChild != null) { //如果子分类不为空，置空
                            selectChild = null;
                            tvArea_child.setText("");
                        }
                    }
                });
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
    private void getChildClassify() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取字典;
        DictData data = new DictData();
        data.setDictionaryName("dict_zone_post_type");
//        data.setKey("公司空间");
        data.setFilter("parent='" + selectParent.getUuid() + "'");
        data.setFull(true);

        StringRequest.postAsyn(url, data, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                mChildDicts = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                final List<String> names = new ArrayList<>();
                if (mChildDicts != null) {
                    for (字典 dict : mChildDicts) {
                        names.add(dict.getName());
                    }
                }
                mDictIosPickerBottomDialog.show(names);

                mDictIosPickerBottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        tvArea_child.setText(names.get(index));
                        selectChild = mChildDicts.get(index);
                    }
                });
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {

            }
        });
    }

    private void initIntent() {
        if (getIntent().getExtras() != null) {
            currentPos = getIntent().getIntExtra("currentPos", -1);
        }
    }


    /**
     * 提示内容尚未保存是否退出
     */
    private void isBack() {
        String content = mEditTextContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            final AlertDialog dialog = new AlertDialog(SpaceNewActivity.this).builder()
                    .setTitle("返回")
                    .setMsg("内容未保存,是否返回?")
                    .setPositiveButton("确认", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
            dialog.show();

        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CODE_FAILED);
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setonclick() {


        attachView.loadImageByAttachIds(space.getFileAttachmentIds());

        // 添加监听事件
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isBack();
            }
        });


        rl_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectLocationBiz.selectLocation(mContext, 0, 0);
            }
        });

        /**
         * 语音输入
         */
        rl_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextContent.setText("");
                SpeechDialogHelper speechDialogHelper = new SpeechDialogHelper(
                        mContext, mEditTextContent, true);
                speechDialogHelper
                        .setOnCompleteListener(new SpeechDialogHelper.OnCompleteListener() {
                            @Override
                            public void onComplete(String result) {
                                mEditTextContent.setText(result);
                            }
                        });
            }
        });

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //发布空间
                mContent = mEditTextContent.getText().toString().replaceAll(" ", "");
                mTitle = et_title.getText().toString().replaceAll(" ", "");

                if (TextUtils.isEmpty(mTitle)) {
                    showShortToast("请添加标题");
                    return;
                }
                if (TextUtils.isEmpty(mContent)) {
                    showShortToast("请填写内容");
                    return;
                }
                if (selectParent == null) {
                    showShortToast("请选择类别");
                    return;
                }

                if (selectChild == null) {
                    showShortToast("请选择子类别");
                    return;
                }

                final AlertDialog dialog = new AlertDialog(SpaceNewActivity.this).builder()
                        .setTitle("发表帖子")
                        .setMsg("确认发布?")
                        .setPositiveButton("发布", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                uploadImg();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                dialog.show();

            }
        });

        llArea_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectParent == null) {
                    showShortToast("请选择空间分类!");
                    return;
                }
                getChildClassify();
            }
        });


        llArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentClassify();
            }
        });

        llPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDictIosPickerBottomDialog.show(mAreaTypeArrs);
                mDictIosPickerBottomDialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int index) {
                        Intent intent;
                        switch (index){
                            case 0:
                                tvPublish.setText("全体员工");
                                space.setRecipientIds("");
                                space.setReciveCategory("0");
                                break;
                            case 1:
                                intent = new Intent(SpaceNewActivity.this, SelectedNotifierActivity.class);
                                intent.putExtra("isSingleSelect",false);
                                startActivityForResult(intent, REQUEST_SELECT_PARTICIPANT);
                                break;
                            case 2:
                                intent = new Intent(SpaceNewActivity.this, SelectedDepartmentActicity.class);
                                startActivityForResult(intent, REQUEST_SELECT_SECTION);
                                break;
                        }
                    }
                });
            }
        });

    }

    /**
     * 上传附件
     */
    private void uploadImg() {
        attachView.uploadImage("space", new IOnUploadMultipleFileListener() {
            @SuppressLint("NewApi")
            @Override
            public void onStartUpload(int sum) {
                ProgressDialogHelper.show(SpaceNewActivity.this, "正在发布..");
            }

            @Override
            public void onProgressUpdate(int completeCount) {
            }

            @Override
            public void onComplete(String attachIds) {
                space.setFileAttachmentIds(attachIds);
                saveSpace();
            }
        });
    }

    /**
     * 通知列表刷新
     */
    public void refreshList(String type) {
        if (TextUtils.equals(type, 个人空间)) {
            PersonalListFragment.isResume = true;
        } else if (TextUtils.equals(type, 部门空间)) {
            DepartmentListFragment.isResume = true;
        } else if (TextUtils.equals(type, 公司空间)) {
            ComponyListFragment.isResume = true;
        }
    }

    /**
     * 发布空间
     */
    public void saveSpace() {
        space.setPostType(selectParent.getUuid());
        space.setPostTypeChild(selectChild.getUuid());
        space.setPostTitle(mTitle);
        space.setTextPart(mContent);
        StringRequest.postAsyn(URL, space, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("发布成功");
                ProgressDialogHelper.dismiss();
                SpaceListActivity.isResume = true;
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {
                showShortToast("发布失败");
                ProgressDialogHelper.dismiss();
            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("发布异常");
                ProgressDialogHelper.dismiss();
            }
        });

    }



    /**
     * 弹出短Toast提示信息
     */
    protected void showShortToast(String info) {
        if (toast == null) {
            toast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
        } else {
            toast.setText(info);
        }
        toast.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PARTICIPANT: //接受通知人
                    Bundle bundle1 = data.getExtras();
                    UserList userList1 = (UserList) bundle1.getSerializable(RESULT_SELECT_USER);
                    if (userList1 != null) {
                        List<User> users = userList1.getUsers();
                        String participant = "";
                        String mParticipant = "";
                        for (User user : users) {
                            mParticipant += user.getUuid() + ",";
                            participant += user.getName() + ",";
                        }
                        if (mParticipant.length() > 0) {
                            mParticipant = mParticipant.substring(0, mParticipant.length() - 1);
                            participant = participant.substring(0, participant.length() - 1);
                        }
                        space.setRecipientIds(mParticipant);
                        space.setReciveCategory("1");
                        tvPublish.setText(participant);
                    }
                    break;
                case REQUEST_SELECT_SECTION: //接受通知部门
                    Bundle bundle = data.getExtras();
                    List<String> select_dept = (List<String>) bundle.getSerializable(RESULT_SELECT_USER);
                    String participant = "";
                    String participantIDs = "";

                    if (select_dept.size() > 0) {
                        for (String s : select_dept) {
                            participant += helper.getDepartNameById(s) + ",";
                            participantIDs += s + ",";
                        }
                        if (participant.length() > 0) {
                            participant = participant.substring(0, participant.length() - 1);
                        }

                        if (participantIDs.length() > 0) {
                            participantIDs = participantIDs.substring(0, participantIDs.length() - 1);
                        }

                        space.setReciveCategory("2");
                        space.setRecipientIds(participantIDs);
                        tvPublish.setText(participant);
                        select_dept.clear();
                    }

                default:
                    attachView.onActivityiForResultImage(requestCode,
                            resultCode, data);
                    break;
            }
        }
    }

}
