package com.ineasnet.shanghaiguidao2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.CityAdapter;
import com.ineasnet.shanghaiguidao2.adapters.UserCheckAdapter;
import com.ineasnet.shanghaiguidao2.decoration.DividerItemDecoration;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.bean.UserListCheckedBean;
import com.ineasnet.shanghaiguidao2.entity.vo.UserUpload;
import com.ineasnet.shanghaiguidao2.entity.vo.UserVo;
import com.ineasnet.shanghaiguidao2.network.Urls;
import com.ineasnet.shanghaiguidao2.util.QueryUser;
import com.ineasnet.shanghaiguidao2.util.ROnItemClickListener;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.mcxtzhang.indexlib.IndexBar.widget.IndexBar;
import com.mcxtzhang.indexlib.suspension.SuspensionDecoration;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;

/**
 * Created by Administrator on 2017/7/20.
 */

public class UserCheckAddActivity extends MyBaseActivity{
    private TextView tv_title_content;
    private Context mContext;
    private Gson gson ;
    private List<User> list = new ArrayList<User>();
    private List<User> listData = new ArrayList<User>();
    private Button bt_select_user;
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
    private RecyclerView mRv;
    private CityAdapter mAdapter;
    private LinearLayoutManager mManager;
    private SuspensionDecoration mDecoration;

    /**
     * 右侧边栏导航区域
     */
    private IndexBar mIndexBar;

    /**
     * 显示指示器DialogText
     */
    private TextView mTvSideBarHint;
    //搜索框
    private EditText edit_search_user;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //获取用户组员信息
                case 1:
                    mAdapter.setDatas(list);
                    mAdapter.notifyDataSetChanged();
                    mIndexBar.setmSourceDatas(list)//设置数据
                            .invalidate();
                    mDecoration.setmDatas(list);
                    break;
                case 2:
                    Toast.makeText(mContext, "组员分配成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserCheckAddActivity.this,MainActivity.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_select_user);
        mContext = this;
        gson = new Gson();
        application = (MyApplication) getApplication();
        config = application.getDaoConfig();
        db = x.getDb(config);
        init();
        getData();
    }

    @Override
    protected void handleHttpResult() {

    }

    @Override
    protected void initView() {

    }

    private void init(){
        tblStudyDataId = getIntent().getStringExtra("tblStudyDataId");
        departmentNo = SettingUtils.get(mContext,"departmentNo");
        userName = getIntent().getStringExtra("userName");
        isFromPro = getIntent().getBooleanExtra("isFromPro",false);
        userId = SettingUtils.get(UserCheckAddActivity.this,"userId");
        lac_user_checkbox = (CheckBox) findViewById(R.id.lac_user_checkbox);
        tv_title_content = (TextView) findViewById(R.id.tv_title_content);
        rl_checkall = (RelativeLayout) findViewById(R.id.rl_checkall);
        bt_select_user = (Button) findViewById(R.id.bt_select_user);
        edit_search_user = (EditText) findViewById(R.id.edit_search_user);
        rl_checkall.setOnClickListener(this);
        bt_select_user.setOnClickListener(this);
        mRv = (RecyclerView) findViewById(R.id.rv);
        try {
            userInfo = db.selector(User.class).where("userId","=",userId).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        mRv.setLayoutManager(mManager = new LinearLayoutManager(this));

        mAdapter = new CityAdapter(this, list);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this, list));
        //如果add两个，那么按照先后顺序，依次渲染。
        mRv.addItemDecoration(new DividerItemDecoration(UserCheckAddActivity.this, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar
        mTvSideBarHint = (TextView) findViewById(R.id.tvSideBarHint);//HintTextView
        mIndexBar = (IndexBar) findViewById(R.id.indexBar);//IndexBar

        //indexbar初始化
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager);//设置RecyclerView的LayoutManager
        mAdapter.setrOnItemClickListener(new ROnItemClickListener() {
            @Override
            public void getView(View view, int position) {
                User user = list.get(position);
                if (user.isCheck()){
                    user.setCheck(false);
                }else{
                    user.setCheck(true);
                }
//                try {
//                    db.saveOrUpdate(user);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
                list.set(position,user);
                if (isCheckAllList()){
                    isCheckAll = true;
                    lac_user_checkbox.setChecked(true);
                }else{
                    isCheckAll = false;
                    lac_user_checkbox.setChecked(false);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        edit_search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.clear();
                list = QueryUser.QueryUserName(mContext,db,String.valueOf(s));
                mAdapter.setDatas(list);
                mAdapter.notifyDataSetChanged();
                mIndexBar.setmSourceDatas(list)//设置数据
                        .invalidate();
                mDecoration.setmDatas(list);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_title_content.setText("分配组员");
    }


    private void getData(){
//        if (isOpenNetwork()){
//            handler.sendEmptyMessage(LOADING);
//            MyWebWraper.getInstance().getUserList(mContext,departmentNo,cb_getUserList);// 获取部门列表
//        }else{
            try {
                if (db.findAll(User.class)!=null){
                    list = db.selector(User.class).where("userId","=",userId).findAll();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
            updateList();
            mHandler.sendEmptyMessage(1);
//        }
    }

    private HttpResultCallBack cb_getUserList = new HttpResultCallBack() {

        @Override
        public void onResultCallBack(Object... objs) {
            // TODO Auto-generated method stub
            // MyWebWraper.getInstance().getFileList(mContext, cb);
            handler.sendEmptyMessage(LOADING_END);
            String result = String.valueOf(objs[0]);
            boolean isException = handleException(UserCheckAddActivity.this, result);

            if (isException) {
                Logger.json(result);
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
                    listData.addAll(list);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
            case R.id.bt_select_user:
                if (isChecked()){
                    Toast.makeText(mContext, "组员分配成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(UserCheckAddActivity.this,MainActivity.class);
//                    intent.putExtra("userId", userId);
//                    intent.putExtra("userName", userName);
//                    intent.putExtra("isFromPro", isFromPro);
//                    intent.putExtra("departmengNo",departmentNo);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                    List<User> result = new ArrayList<User>();
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).isCheck()){
                            result.add(list.get(i));
                        }
                    }
                    Intent data = new Intent();
                    UserListCheckedBean checkedBean = new UserListCheckedBean();
                    checkedBean.setUserList(result);
                    data.putExtra("data",checkedBean);
                    setResult(1,data);
                    finish();
                }else{
                    Toast.makeText(mContext, "您还没有选择组员", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
        mAdapter.setDatas(list);
        mAdapter.notifyDataSetChanged();
        mIndexBar.setmSourceDatas(list)//设置数据
                .invalidate();
        mDecoration.setmDatas(list);
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
