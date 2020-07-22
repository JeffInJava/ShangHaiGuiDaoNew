package com.ineasnet.shanghaiguidao2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.UserAdapter;
import com.ineasnet.shanghaiguidao2.adapters.UserCheckAdapter;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.bean.UserVoBean;
import com.ineasnet.shanghaiguidao2.entity.vo.UserUpload;
import com.ineasnet.shanghaiguidao2.entity.vo.UserVo;
import com.ineasnet.shanghaiguidao2.network.Urls;
import com.ineasnet.shanghaiguidao2.util.QueryUser;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;


/**
 * 选择学习用户
 * Created by Administrator on 2017/5/23.
 */

public class UserCheckActivity extends MyBaseActivity{
    private ListView list_user;
    private UserCheckAdapter adapter;
    private TextView tv_title_content;
    private Context mContext;
    private Gson gson ;
    private List<User> list = new ArrayList<User>();
    private Button bt_check;
    private String tblStudyDataId;
    private String departmentNo;
    private String userName ;
    private boolean isFromPro;
    private String userId;
    private CheckBox lac_user_checkbox;
    private RelativeLayout rl_checkall;
    //全选
    private boolean isCheckAll = false;
    //
    private List<User> userInfo = new ArrayList<>();
    private MyApplication application;
    private DbManager.DaoConfig config;
    private DbManager db;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //获取用户组员信息
                case 1:
                    adapter = new UserCheckAdapter(mContext,list);
                    list_user.setAdapter(adapter);
                    break;
                case 2:
                    Toast.makeText(mContext, "组员分配成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserCheckActivity.this,MainActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("isFromPro", isFromPro);
                    intent.putExtra("departmengNo",departmentNo);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_user);
        mContext = this;
        gson = new Gson();
        application = (MyApplication) getApplication();
        config = application.getDaoConfig();
        db = x.getDb(config);
        initView();
        getData();

    }


    private void getData(){
        if (isOpenNetwork()){
            handler.sendEmptyMessage(LOADING);
            MyWebWraper.getInstance().getUserList(mContext,departmentNo,cb_getUserList);// 获取部门列表
        }else{
            try {
                if (db.findAll(User.class)!=null){
                    list = db.selector(User.class).where("userId","=",userId).findAll();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            updateList();
            mHandler.sendEmptyMessage(1);
        }
    }

    private HttpResultCallBack cb_getUserList = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            // MyWebWraper.getInstance().getFileList(mContext, cb);
            handler.sendEmptyMessage(LOADING_END);
            String result = String.valueOf(objs[0]);
            boolean isException = handleException(UserCheckActivity.this, result);

            if (isException) {
                Logger.json(result);
//                 userVoBean = gson.fromJson(result,UserVoBean.class);
                UserVo vo = gson.fromJson(result,UserVo.class);
                list = vo.getPsmList();
                if (userInfo!=null&&userInfo.size()>0){
                    for (int i=0;i<list.size();i++){
                        User user = list.get(i);
                        User userNew = QueryUser.QueryUser(mContext,db,user.getCode());
                        //判断本地是否有该用户
                        if (userNew == null){
                            try {
                                user.setUserId(userId);
                                db.save(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }else{
                    for (int i=0;i<list.size();i++){
                        User user = list.get(i);
                        user.setUserId(userId);
                            try {
                                db.save(user);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                    }
                }
                list.clear();
                try {
                    list = db.selector(User.class).where("userId","=",userId).findAll();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                updateList();
                mHandler.sendEmptyMessage(1);
            }
        }
    };

    /**
     * 整理人员数据
     */
    private void updateList(){
        List<User> checkOffUserList = new ArrayList<>();
        List<User> checkOnUserList = new ArrayList<>();
        for (int i=0;i<list.size();i++){
            if (!list.get(i).isCheck()){
                checkOffUserList.add(list.get(i));
            }else{
                checkOnUserList.add(list.get(i));
            }
        }
        list.clear();
        list.addAll(checkOnUserList);
        list.addAll(checkOffUserList);
        if (isCheckAllList()){
            isCheckAll = true;
            lac_user_checkbox.setChecked(true);
        }else{
            isCheckAll = false;
            lac_user_checkbox.setChecked(false);
        }
    }

    /**
     * 提交组员信息
     */
    private HttpResultCallBack cb_uploadUserList = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            // MyWebWraper.getInstance().getFileList(mContext, cb);
            String result = String.valueOf(objs[0]);
            boolean isException = handleException(UserCheckActivity.this, result);

            if (isException) {
                Logger.json(result);
                mHandler.sendEmptyMessage(2);
            }
        }
    };


    @Override
    protected void handleHttpResult() {

    }

    @Override
    protected void initView() {
        list_user = (ListView) findViewById(R.id.list_user);
        bt_check = (Button) findViewById(R.id.bt_check);
        tblStudyDataId = getIntent().getStringExtra("tblStudyDataId");
        departmentNo = SettingUtils.get(mContext,"departmentNo");
        userName = getIntent().getStringExtra("userName");
        isFromPro = getIntent().getBooleanExtra("isFromPro",false);
        userId = SettingUtils.get(UserCheckActivity.this,"userId");
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        lac_user_checkbox = (CheckBox) findViewById(R.id.lac_user_checkbox);
        rl_checkall = (RelativeLayout) findViewById(R.id.rl_checkall);
        try {
            userInfo = db.selector(User.class).where("userId","=",userId).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        list_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = list.get(i);
                if (user.isCheck()){
                    user.setCheck(false);
                }else{
                    user.setCheck(true);
                }
                try {
                    db.saveOrUpdate(user);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                list.set(i,user);
                if (isCheckAllList()){
                    isCheckAll = true;
                    lac_user_checkbox.setChecked(true);
                }else{
                    isCheckAll = false;
                    lac_user_checkbox.setChecked(false);
                }
                adapter.notifyDataSetChanged(list);
            }
        });
        bt_check.setOnClickListener(this);
        rl_checkall.setOnClickListener(this);
        tv_title_content.setText("分配组员");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_check:
                if (isChecked()){
                    SettingUtils.set(UserCheckActivity.this,"users"+userId,getCheckUserList());
                    Toast.makeText(mContext, "组员分配成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserCheckActivity.this,MainActivity.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    intent.putExtra("isFromPro", isFromPro);
                    intent.putExtra("departmengNo",departmentNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(mContext, "您还没有选择组员", Toast.LENGTH_SHORT).show();
                }

//                MyWebWraper.getInstance().uploadUserList(mContext,tblStudyDataId,getCheckList(),cb_uploadUserList);// 获取部门列表
                break;
            case R.id.rl_checkall:
                if (isCheckAll){
                    setCheckAll(false);
                    lac_user_checkbox.setChecked(false);
                    isCheckAll = false;
                }else{
                    setCheckAll(true);
                    lac_user_checkbox.setChecked(true);
                    isCheckAll = true;
                }
                break;
        }
    }



    private void setData(){
        String URL = Urls.UPDATE_CHECK_LIST;
        RequestParams params = new RequestParams(URL);
        params.setHeader("cookie", SettingUtils.get(mContext,"cookie"));
        params.addBodyParameter("tblStudyDataId",tblStudyDataId);
        params.addBodyParameter("userIds",getCheckList());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                setResult(1);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取所有已选择的成员名单
     * @return
     */
    private String getCheckList(){
        String resultStr;
        List<UserUpload> result = new ArrayList<UserUpload>();
        for (int i=0;i<list.size();i++){
            if (list.get(i).isCheck()){
                UserUpload upload = new UserUpload();
                upload.setId(list.get(i).getCode());
                result.add(upload);
            }
        }
        resultStr = gson.toJson(result);
            return resultStr;
    }

    /**
     * 是否有选择组员
     * @return
     */
    private boolean isChecked(){
        boolean flag = false;
        for (int i=0;i<list.size();i++){
            if (list.get(i).isCheck()){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取所有已选择的成员名单
     * @return
     */
    private String getCheckUserList(){
        String resultStr;
        List<User> result = new ArrayList<User>();
        for (int i=0;i<list.size();i++){
            if (list.get(i).isCheck()){
                User upload = new User();
                upload.setCode(list.get(i).getCode());
                upload.setName(list.get(i).getName());
                upload.setCheck(true);
                result.add(upload);
            }
        }
        resultStr = gson.toJson(list);
        return resultStr;
    }

    /**
     * 全选
     * @param isChecked
     */
    private void setCheckAll(boolean isChecked){
        for (int i=0;i<list.size();i++){
                User upload = list.get(i);
                upload.setCode(list.get(i).getCode());
                upload.setName(list.get(i).getName());
                upload.setCheck(isChecked);
                list.set(i,upload);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 是否全选
     * @return
     */
    private boolean isCheckAllList(){
        boolean flag = true;
        for (int i=0;i<list.size();i++){
            User upload = list.get(i);
            if (!upload.isCheck()){
                flag = false;
            }
        }
        return flag;
    }

}
