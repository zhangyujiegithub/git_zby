package com.biaozhunyuan.tianyi.buglist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.model.字典;
import com.biaozhunyuan.tianyi.common.helper.IOnUploadMultipleFileListener;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.ParseException;
import com.biaozhunyuan.tianyi.dynamic.Dynamic;
import com.biaozhunyuan.tianyi.common.global.Global;
import com.biaozhunyuan.tianyi.common.global.GlobalMethord;
import com.biaozhunyuan.tianyi.common.helper.DictionaryHelper;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.http.StringRequest;
import com.biaozhunyuan.tianyi.common.http.StringResponseCallBack;
import com.biaozhunyuan.tianyi.notice.SelectedNotifierActivity;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.utils.EmojiUtils;
import com.biaozhunyuan.tianyi.common.utils.JsonUtils;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;
import com.biaozhunyuan.tianyi.common.view.DictIosPickerBottomDialog;
import com.biaozhunyuan.tianyi.view.MultipleAttachView;
import com.biaozhunyuan.tianyi.view.VoiceInputView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

import static com.biaozhunyuan.tianyi.task.TaskNewActivity.REQUEST_SELECT_EXCUTORS;

/**
 * bug 详情
 */
public class BugInfoActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private EditText et_content;
    private TextView tv_excutor;
    private MultipleAttachView attachView;
    private Bug bug;
    private Dynamic dynamic;
    private DictionaryHelper helper = new DictionaryHelper(this);
    public static final String RESULT_SELECT_USER = "RESULT_SELECT_USER";
    private VoiceInputView inputView;
    private TextView tv_project; //选择项目父容器
    private ImageView iv_excutor;
    private ImageView iv_project;
    private LinearLayout ll_project; //选择项目
    private LinearLayout ll_excutors; //选择项目
    private DictIosPickerBottomDialog dialog;
    private List<字典> 字典s = new ArrayList<>();
    private ArrayList<String> datas = new ArrayList();
    private boolean isEnable = true; //是否为编辑
    private String URL = Global.BASE_JAVA_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bug);
        initView();
        getCompanyProject();
        getIntentData();
        setOnTouchEvent();
    }

    private void getIntentData() {
        if (getIntent().getExtras() != null) {
            boolean isFromTurnChat = getIntent().getBooleanExtra("isFromTurnChat", true);
            bug = (Bug) getIntent().getSerializableExtra("bugInfo");
            dynamic = (Dynamic) getIntent().getSerializableExtra("dynamicInfo");
            if (bug != null) {
                if (!isFromTurnChat) {
                    URL += GlobalMethord.新增BUG;
                } else {
                    URL += GlobalMethord.编辑BUG;
                }
                headerview.setTitle("编辑Bug");
                headerview.setRightTitle("保存");
                initData(isFromTurnChat);
            }

            if (dynamic != null) {
                getBugInfoById();
            }
        } else {
            URL += GlobalMethord.新增BUG;
        }
    }

    /**
     * 从实体类中取出数据设值
     */
    private void initData(boolean isFromTurnChat) {
        if (isFromTurnChat) {
            isEnable = false;
            iv_project.setVisibility(View.GONE);
            iv_excutor.setVisibility(View.GONE);
        } else {
            headerview.setTitle("新增Bug");
            headerview.setRightTitle("新增");
        }
        et_content.setText(bug.getContent());
        tv_excutor.setText(helper.getUserNameById(bug.getCurrentDesignateId()));
        attachView.loadImageByAttachIds(bug.getAttachmentIds());
        tv_project.setText(bug.getProjectManagementName());
    }

    /**
     * 获取可选择的项目
     */
    private void getCompanyProject() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取可选择的项目;
        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                字典s = JsonUtils.jsonToArrayEntity(JsonUtils.pareseData(response), 字典.class);
                if (字典s.size() > 0) {
                    for (字典 z : 字典s) {
                        datas.add(z.getName());
                    }
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


    private void getBugInfoById() {
        String url = Global.BASE_JAVA_URL + GlobalMethord.获取BUG + "?uuid=" + dynamic.getDataId();

        StringRequest.getAsyn(url, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    bug = JsonUtils.jsonToEntity(JsonUtils.pareseData(response), Bug.class);


                    URL += GlobalMethord.编辑BUG;
                    headerview.setTitle("编辑Bug");
                    headerview.setRightTitle("保存");
                    initData(false);
                } catch (ParseException e) {
                    e.printStackTrace();
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

    private void setOnTouchEvent() {
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                if (isCanSave()) {
                    attachView.uploadImage("bug", new IOnUploadMultipleFileListener() {
                        @Override
                        public void onStartUpload(int sum) {

                        }

                        @Override
                        public void onProgressUpdate(int completeCount) {

                        }

                        @Override
                        public void onComplete(String attachIds) {
                            bug.setAttachmentIds(attachIds);
                            saveBug();
                        }
                    });
                }
            }

            @Override
            public void onClickBack() {
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });
        tv_excutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    Intent intent = new Intent(BugInfoActivity.this, SelectedNotifierActivity.class);
                    intent.putExtra("isSingleSelect", true);
                    intent.putExtra("title", "选择执行人");
                    startActivityForResult(intent, REQUEST_SELECT_EXCUTORS);
                }
            }
        });

        tv_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    dialog.show(datas);
                    dialog.setOnSelectedListener(new DictIosPickerBottomDialog.OnSelectedListener() {
                        @Override
                        public void onSelected(int index) {
                            tv_project.setText(字典s.get(index).getName());
                            bug.setProjectManagement(字典s.get(index).getUuid());
                        }
                    });
                }
            }
        });
    }

    /**
     * 判断空值
     *
     * @return
     */

    private boolean isCanSave() {
        String content = et_content.getText().toString().trim();
        String excutor = tv_excutor.getText().toString().trim();
        String project = tv_project.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showShortToast("请输入Bug内容");
            return false;
        }
        if (TextUtils.isEmpty(excutor)) {
            showShortToast("请选择指派人");
            return false;
        }
        if (TextUtils.isEmpty(project)) {
            showShortToast("请选择项目");
            return false;
        }
        if (content.contains("%")) {
            showShortToast("非法字符:%!");
            return false;
        }

        if (EmojiUtils.containsEmoji(content)) {
            showShortToast("不支持表情符号!");
            return false;
        }
        bug.setContent(content);
        bug.setTitle(content);
        return true;
    }


    /**
     * 提交bug
     */
    private void saveBug() {
        StringRequest.postAsyn(URL, bug, new StringResponseCallBack() {
            @Override
            public void onResponse(String response) {
                showShortToast("指派成功");
                BugListActivity.isResume = true;
                finish();
            }

            @Override
            public void onFailure(Request request, Exception ex) {

            }

            @Override
            public void onResponseCodeErro(String result) {
                showShortToast("指派失败");
            }
        });

    }


    private void initView() {
        bug = new Bug();
        dialog = new DictIosPickerBottomDialog(BugInfoActivity.this);
        headerview = findViewById(R.id.header_new_bug);
        et_content = findViewById(R.id.et_content_new_bug); //bug内容
        attachView = findViewById(R.id.attach_add_bug);
        tv_excutor = findViewById(R.id.new_bug_excutor); //指派人
        inputView = findViewById(R.id.voice_view_new_task);
        ll_project = findViewById(R.id.ll_choosr_project_new); //选择项目父容器
        ll_excutors = findViewById(R.id.ll_choosr_excutors_bug_new); //选择指派人父容器
        tv_project = findViewById(R.id.new_bug_project); //选择项目
        iv_excutor = findViewById(R.id.iv_bug_excutor); //选择项目
        iv_project = findViewById(R.id.iv_bug_project); //选择项目
        attachView.setIsAdd(true);
        attachView.setMaxCount(9);
        attachView.loadImageByAttachIds("");
        inputView.setRelativeInputView(et_content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_EXCUTORS: //选择执行人
                    Bundle bundle = data.getExtras();
                    UserList userList = (UserList) bundle.getSerializable(RESULT_SELECT_USER);
                    if (userList != null) {
                        try {
                            List<User> users = userList.getUsers();
                            bug.setCurrentDesignateId(users.get(0).getUuid());
                            tv_excutor.setText(users.get(0).getName());
                        } catch (IndexOutOfBoundsException e) {
                            showShortToast("没有选择指派人");
                        }
                    }
                    break;
                default:
                    attachView.onActivityiForResultImage(requestCode,
                            resultCode, data);
                    break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        InputSoftHelper.hideKeyboard(et_content);
    }
}
