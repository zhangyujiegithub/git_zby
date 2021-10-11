package com.biaozhunyuan.tianyi.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.base.BaseActivity;
import com.biaozhunyuan.tianyi.common.base.BoeryunViewHolder;
import com.biaozhunyuan.tianyi.common.base.CommanAdapter;
import com.biaozhunyuan.tianyi.common.model.user.User;
import com.biaozhunyuan.tianyi.common.model.user.UserList;
import com.biaozhunyuan.tianyi.common.view.BoeryunHeaderView;

import java.util.ArrayList;
import java.util.List;

import static com.biaozhunyuan.tianyi.apply.FormInfoActivity.RESULT_SELECT_USER;

public class SelectedUserListActivity extends BaseActivity {

    private BoeryunHeaderView headerview;
    private ListView mLv;
    private List<User> user_list = new ArrayList<>();
    public CommanAdapter<User> adapter;
    private Context context;
    private User user = new User();
    private boolean isSelect;
    private List<User> users = new ArrayList<>();
    private boolean isSingleSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user_list);
        context = getBaseContext();
        initView();
        getSelectList();
        setTouchEvent();
        setData();

    }

    private void setData() {
        adapter = getUserAdapter(user_list);
        mLv.setAdapter(adapter);

        if(isSelect){
            mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(isSingleSelect){
                        User item = adapter.getItem(position);
                        users.add(item);
                        returnResult(users);
                    } else {
                        User item = adapter.getItem(position);
                        if(item.ischeck()){
                            users.remove(item);
                        }else {
                            users.add(item);
                        }
                        item.setIscheck(!item.ischeck());
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void setTouchEvent() {
        headerview.setOnButtonClickListener(new BoeryunHeaderView.OnButtonClickRightListener() {
            @Override
            public void onRightTextClick() {
                returnResult(users);
            }

            @Override
            public void onClickBack() {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                UserList userList = new UserList();
                userList.setUsers(user_list);
                bundle.putSerializable("newseleteduser",userList);
                intent.putExtras(bundle);
                setResult(111,intent);
                finish();
            }

            @Override
            public void onClickFilter() {

            }

            @Override
            public void onClickSaveOrAdd() {

            }
        });
    }

    private void initView() {
        headerview = findViewById(R.id.headerview);
        mLv = findViewById(R.id.select_user_lv);

    }

    public void getSelectList(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle.getSerializable("userlist")!=null){
            UserList userList = (UserList) bundle.getSerializable("userlist");
            user_list = userList.getUsers();
            isSelect = false;
            headerview.setRightTitleVisible(false);
        }else if(bundle.getSerializable("auditorList")!=null){
            UserList userList = (UserList) bundle.getSerializable("auditorList");
            user_list = userList.getUsers();
            isSelect = true;
            headerview.setTitle("选择下一步审核人");
            headerview.setRightTitleVisible(true);
        }
        isSingleSelect = intent.getBooleanExtra("isSingleSelect", false);
        if(isSingleSelect){ //true 为单选模式
            headerview.setRightTitleVisible(false);
        } else { //false 可多选

        }
    }

    private CommanAdapter<User> getUserAdapter(List<User> list) {
        return new CommanAdapter<User>(user_list,context,R.layout.item_selecteduser) {
            @Override
            public void convert(final int position, User item, BoeryunViewHolder viewHolder) {
                viewHolder.setTextValue(R.id.select_username, item.getName());
                Button delete = viewHolder.getView(R.id.select_delete);
                ImageView iv_select = viewHolder.getView(R.id.iv_select);
                if(!isSelect){
                    delete.setVisibility(View.VISIBLE);
                    iv_select.setVisibility(View.GONE);
                }else {
                    delete.setVisibility(View.GONE);
                    iv_select.setVisibility(View.VISIBLE);
                }

                if(item.ischeck()){
                    iv_select.setImageResource(R.drawable.ic_select);
                }else {
                    iv_select.setImageResource(R.drawable.ic_cancle_select);
                }

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user_list.remove(user_list.get(position));
                        notifyDataSetChanged();
                    }
                });
            }
        };
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            UserList userList = new UserList();
            userList.setUsers(user_list);
            bundle.putSerializable("newseleteduser",userList);
            intent.putExtras(bundle);
            setResult(111,intent);
            finish();
        }
        return true;
    }


    /**
     * 返回数据
     */
    public void returnResult(List<User> selectUsers) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        UserList userList = new UserList();
        userList.setUsers(selectUsers);
        bundle.putSerializable(RESULT_SELECT_USER, userList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

}
