package com.ineasnet.shanghaiguidao2.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;

import androidPT.ptUtil.base.MyBaseActivity;

/**
 * 修改密码
 * Created by Administrator on 2017/7/28.
 */

public class UpdatePassWordActivity extends MyBaseActivity{
    //旧密码
    private String oldPwd;
    private EditText edit_old;
    //新密码
    private String newPwd;
    private EditText edit_new;
    //标题
    private TextView tv_title_content;
    //提交
    private Button btn_update;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_changepwd);
        initView();
    }

    @Override
    protected void handleHttpResult() {

    }

    @Override
    protected void initView() {
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        tv_title_content.setText("修改密码");
        edit_old = (EditText) findViewById(R.id.edit_old);
        edit_new = (EditText) findViewById(R.id.edit_new);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update:
                
                break;
        }
    }
}
