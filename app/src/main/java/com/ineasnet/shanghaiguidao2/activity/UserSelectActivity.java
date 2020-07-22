package com.ineasnet.shanghaiguidao2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.UserAdapter;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.vo.UserVo;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;

import java.util.ArrayList;
import java.util.List;

import androidPT.ptUtil.base.MyBaseActivity;

/**
 * 选择组员
 * Created by Administrator on 2017/5/24.
 */

public class UserSelectActivity extends MyBaseActivity{
    private ListView list_user;
    private UserAdapter adapter;
    private Context mContext;
    private Gson gson ;
    private List<User> list = new ArrayList<User>();
    private String tblStudyDataId;
    private String departmentNo;
    private Button bt_check;
    private String path;
    private int page;
    private boolean flag;
    private String fileName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        gson = new Gson();
        setContentView(R.layout.ac_user);
        initView();
    }

    @Override
    protected void handleHttpResult() {

    }

    @Override
    protected void initView() {
        list_user = (ListView) findViewById(R.id.list_user);
        bt_check = (Button) findViewById(R.id.bt_check);
        tblStudyDataId = getIntent().getStringExtra("tblStudyDataId");
        departmentNo = SettingUtils.get(mContext,"departmentNo");
        UserVo vo = (UserVo) getIntent().getSerializableExtra("bean");
        list = vo.getPsmList();
        adapter = new UserAdapter(mContext,list);
        list_user.setAdapter(adapter);
        bt_check.setVisibility(View.GONE);
        path = getIntent().getStringExtra("filePath");
        flag = getIntent().getBooleanExtra("isFromIndex",false);
        tblStudyDataId = getIntent().getStringExtra("tblStudyDataId");
        page = getIntent().getIntExtra("page",0);
        fileName = getIntent().getStringExtra("fileName");
        list_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, PDFNewActivity.class);
                intent.putExtra("filePath", path);
                intent.putExtra("tblStudyDataId", tblStudyDataId);
                intent.putExtra("userId", list.get(i).getCode());
                intent.putExtra("isFromIndex", flag);
                intent.putExtra("page", page);
                intent.putExtra("userName",list.get(i).getName());
                intent.putExtra("fileName",fileName);
                intent.putExtra("departmentNo",SettingUtils.get(mContext,"departmentNo"));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
