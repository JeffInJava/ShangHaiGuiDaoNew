package com.ineasnet.shanghaiguidao2.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.bean.FileEntity;
import com.ineasnet.shanghaiguidao2.entity.bean.ListVoBean;
import com.ineasnet.shanghaiguidao2.entity.bean.LoginBean;
import com.ineasnet.shanghaiguidao2.entity.vo.ExceptionVo;
import com.ineasnet.shanghaiguidao2.entity.vo.FileIndexVo;
import com.ineasnet.shanghaiguidao2.entity.vo.LoginVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.VersionTreeVo;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.DataFactory;
import com.ineasnet.shanghaiguidao2.util.FileUtil;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.ineasnet.shanghaiguidao2.util.ZipExtractorTask;
import com.ineasnet.shanghaiguidao2.view.CommonProgressDialog;
import com.orhanobut.logger.Logger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import androidPT.ptUtil.DownLoadUtil;
import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;
import androidPT.ptUtil.storage.StorageUtil;

public class LoginActivity extends MyBaseActivity {

	private Button btn_login;
	private ToggleButton toggle_pwd;
	private TextView tv_proJudge, tv_resetPwd;
	private EditText edit_user, edit_pwd;
	private Context mContext;


	private String userName, password;
	private String result;
	private String sessionId;
	private String userId;
	private String indexFilePath;
	private String indexFolder;
	private String path;
	private String folder;
	private String getIndexUrl;
	private String departmentNo;
	private boolean isAutoLogin;
	private boolean isFromPro;

	private StorageUtil storageUtil;
	private DownLoadUtil downLoadUtil;
	private DbManager.DaoConfig daoConfig;
	private MyApplication application;
	private CommonProgressDialog mDialog;

	private DbManager db;
	//本地数据库
	private List<TreeVo> SQLiteList = new ArrayList<>();
	//版本改变
	private List<TreeVo> changeVersionList = new ArrayList<>();
	private List<TreeVo> webList = new ArrayList<>();
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					if (mDialog.isShowing()){
						mDialog.dismiss();
					}
					handler.sendEmptyMessage(LOADING_END);
					loginJump();
					break;
				case 2:
					handler.sendEmptyMessage(LOADING_END);
					String message = (String) msg.obj;
					if (message.contains("exception")){
						gson = new Gson();
						ExceptionVo exceptionVo = gson.fromJson(message,ExceptionVo.class);
						Toast.makeText(mContext, exceptionVo.getException(), Toast.LENGTH_SHORT).show();
					}

					break;
				case 3:
					handler.sendEmptyMessage(LOADING_END);
					Toast.makeText(LoginActivity.this, "该用户还未分配权限，请联系管理员", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					handler.sendEmptyMessage(LOADING_END);
					Toast.makeText(LoginActivity.this, "文件数据更新异常", Toast.LENGTH_SHORT).show();
					break;
				case 5:
					handler.sendEmptyMessage(LOADING_END);
					mDialog.show();
					break;
				case 6:
					int progress = (int) msg.obj;
					mDialog.setProgress(progress);
					break;
				case 7:
					handler.sendEmptyMessage(LOADING_END);
					break;
				case 8:
					if (mDialog.isShowing()){
						mDialog.dismiss();
					}
					mDialog.setMessage("正在更新文件数据，请稍后");
					mDialog.setProgress(0);
					handler.sendEmptyMessage(LOADING);
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mContext = LoginActivity.this;
		initView();
		initDialog();

	}

	@Override
	protected void handleHttpResult() {
		// TODO Auto-generated method stub
		try {
			Gson gson = new Gson();
			LoginBean bean = gson.fromJson(result,LoginBean.class);
//			LoginBean bean = (LoginBean) JSONUtil.parseToObject(result,
//					LoginBean.class);
			sessionId = bean.getUser().getSessionId()==null?"":bean.getUser().getSessionId();
			userId = bean.getUser().getUserId()==null?"":bean.getUser().getUserId();
			userName = bean.getUser().getUserName()==null?"":bean.getUser().getUserName();
			departmentNo = bean.getUser().getDepartmentNo() ==null?"":bean.getUser().getDepartmentNo();
			SettingUtils.set(mContext,"userId",userId);
			SettingUtils.set(mContext,"userName",userName.substring(0,userName.length()-1));
			SettingUtils.set(mContext,"departmentNo",departmentNo);
			SettingUtils.set(mContext,"Account",userId);
			SettingUtils.set(mContext,"Password","");
			SQLiteList = queryAll(userId);
			getTypeList();

//			if(TextUtils.isEmpty(sessionId)||TextUtils.isEmpty(userId)||TextUtils.isEmpty(userName)){
//
//			}else{
//				storageUtil.storeLoginInfo(userName, password, userId, sessionId,
//						isAutoLogin);
//
//
//				if(isFromPro){
//					getIndexUrl = PropertiesUtil.getPropertiesURL(mContext,
//							"proIndexUrl");
//					indexFolder = indexFilePath + "indexAll/";// ftpIndexAll
//					FileUtil.createParentFile(indexFolder);
//					path = FileName.FILEPATH+"indexAll.zip";
//					FileUtil.createNewFile(path);
//					folder = FileName.FILEPATH+"indexAll/";
//					FileUtil.createParentFile(folder);
//				}else{
//					getIndexUrl = PropertiesUtil.getPropertiesURL(mContext,
//							"proIndexUrl");
//					indexFolder = indexFilePath + "ftpIndexAll/";// ftpIndexAll
//					FileUtil.createParentFile(indexFolder);
//					path = FileName.FILEPATH+"indexAll.zip";
//					FileUtil.createNewFile(path);
//					folder = FileName.FILEPATH+"ftpIndexAll/";
//					FileUtil.createParentFile(folder);
//				}
//				downLoadUtil = new DownLoadUtil(mContext, sessionId, indexFilePath,
//						path, folder);
//
//				MyWebWraper.getInstance().getIndexFileInfo(mContext, getIndexUrl,
//						cb_getIndexInfo);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	HttpResultCallBack cb_getIndexInfo = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
			Log.e("cb_getIndexInfo",
					"cb_getIndexInfo:" + String.valueOf(objs[0]));
			String result = String.valueOf(objs[0]);
			// FileIndexVo vo = JSON.parseObject(result, FileIndexVo.class);
			Gson gson = new Gson();
			FileIndexVo vo = gson.fromJson(result,FileIndexVo.class);
//			FileIndexVo vo = (FileIndexVo) JSONUtil.parseToObject(result,
//					FileIndexVo.class);
			String indexInfo = storageUtil.getStandardIndex(userId);
			if (!TextUtils.isEmpty(result) && !result.equals("null")
					&& result != null) {

				if (TextUtils.isEmpty(indexInfo)) {
					storageUtil.storeStandardIndex(userId, result);
					downLoadZip(vo.getFileNameUrl(),vo.getFileName());
//					downLoadUtil.downLoadIndex(vo.getFileNameUrl(),
//							 indexFolder, cb_index, handler);

				} else {
					FileIndexVo vo2 = gson.fromJson(result,FileIndexVo.class);
//					if (vo.getVersion() > vo2.getVersion()) {
						storageUtil.storeStandardIndex(userId, result);

//						downLoadUtil.downLoadIndex(vo.getFileNameUrl(),
//								 indexFolder, cb_index, handler);
					downLoadZip(vo.getFileNameUrl(),vo.getFileName());
//					} else {
//						loginJump();
//					}
				}
			} else {
				loginJump();
			}

		}
	};
	

	
	private void loginJump() {
		handler.sendEmptyMessage(LOADING_END);
		Intent intent;
		String json = SettingUtils.get(LoginActivity.this,"users"+userId);
		if (json!=null&&!json.equals("")){
			intent = new Intent(LoginActivity.this, MainActivity.class);
		}else{
			intent = new Intent(LoginActivity.this, UserCheckActivity.class);
		}
		ListVoBean listVoBean = new ListVoBean();
		listVoBean.setList(changeVersionList);
		intent.putExtra("versionBean",listVoBean);
		intent.putExtra("userId", userId);
		intent.putExtra("userName", userName);
		intent.putExtra("isFromPro", isFromPro);
		intent.putExtra("departmengNo",departmentNo);
		startActivity(intent);
		finish();
	}


	/**
	 * 下载进度条
	 */
	public void initDialog() {
		if (mDialog == null){
			mDialog = new CommonProgressDialog(LoginActivity.this);
			mDialog.setMessage("正在更新数据，请稍后");
			//设置窗口的大小

			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setMax(100);
			mDialog.setIsVisible(false);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
		}

	}


	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		str_head = getResources().getString(R.string.str_title_standard_login);

		
		tv_proJudge = (TextView) findViewById(R.id.tv_proJudge);
		tv_resetPwd = (TextView) findViewById(R.id.tv_reset_pwd);
		edit_user = (EditText) findViewById(R.id.edit_user);
		edit_pwd = (EditText) findViewById(R.id.edit_pwd);
		btn_login = (Button) findViewById(R.id.btn_login);
		toggle_pwd = (ToggleButton)findViewById(R.id.togglePwd);


		btn_login.setOnClickListener(this);
		tv_proJudge.setOnClickListener(this);
	    tv_resetPwd.setOnClickListener(this);
		toggle_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					edit_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					edit_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		
		
		storageUtil = new StorageUtil(this);
		String account = SettingUtils.get(mContext,"Account");
		edit_user.setText(account);
		indexFilePath = PropertiesUtil.getPropertiesURL(mContext,
				"filePath");
		application = (MyApplication) getApplication();
		daoConfig = application.getDaoConfig();
		db = x.getDb(daoConfig);

	}

	private void login(String userName, String password) {
		if (isOpenNetwork()) {
			handler.sendEmptyMessage(LOADING);
			MyWebWraper.getInstance().login(this, userName, password, cb);
		} else {
			String userInfos = storageUtil.getUserInfoList();
			if (userInfos.contains(userName)) {
				List<LoginVo> list = JSON.parseArray(userInfos, LoginVo.class);
				for(LoginVo vo : list){
					if(vo.getUserName().equals(userName)){
						userId = vo.getUserId();
					}
				}
				loginJump();
			} else {
				Toast.makeText(LoginActivity.this, "该账号从未成功登陆该设备，请在网络正常的状态下使用",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	HttpResultCallBack cb = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
			result = String.valueOf(objs[0]);
			boolean isException = handleException(LoginActivity.this, result);
			if (!result.contains("exception")) {
				handleHttpResult();
			}else{
				Message msg = new Message();
				msg.what = 2;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			isFromPro = false;
			userName = edit_user.getText().toString().trim();
			password = edit_pwd.getText().toString().trim();
			if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {

			} else {
				login(userName, password);
			}

			break;
		case R.id.tv_proJudge:
			isFromPro = true;
			login(PropertiesUtil.getPropertiesURL(this, "proUserName"),
					PropertiesUtil.getPropertiesURL(this, "proPassword"));
			break;
		case R.id.tv_reset_pwd:

			break;

		default:
			break;
		}
	}




	/**
	 *下载文件索引
	 * @param url 下载地址
	 * @param name 文件名
	 */
	private void downLoadZip(String url, String name){
		final String path;
		if (isFromPro){
			path = folder+name;
		}else{
			path = FileName.FILEPATH+name;
		}
		File file = new File(path);
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RequestParams params = new RequestParams(url);
		params.setHeader("Cookie",downLoadUtil.getSessionId());
		params.setSaveFilePath(path);
		x.http().get(params, new Callback.CommonCallback<File>() {

			@Override
			public void onSuccess(File result) {
//				SettingUtils.set(context,"fileVersion",vo.getVersion());
				doZipExtractorWork(path,folder);


			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				loginJump();
//				prompt("文件下载失败");
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
	 * 解压文件
	 * @param fileIn
	 * @param fileOut
	 */
	public void doZipExtractorWork(String fileIn,String fileOut){
		//ZipExtractorTask task = new ZipExtractorTask("/storage/usb3/system.zip", "/storage/emulated/legacy/", this, true);
		ZipExtractorTask task = new ZipExtractorTask(fileIn, fileOut, this, true);
		task.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	private void getPDFList(){
		mHandler.sendEmptyMessage(8);
		MyWebWraper.getInstance().getAllPDFList(LoginActivity.this, selectPDF);
	}



	//文件索引目录
	private HttpResultCallBack selectPDF = new HttpResultCallBack() {
		@Override
		public void onResultCallBack(Object... objs) {
			if (String.valueOf(objs[0])!=null&&!String.valueOf(objs[0]).equals("")){
				String result = String.valueOf(objs[0]);
				if (!result.contains("exception")) {
					gson = new Gson();
					List<TreeVo> searchResultList = new ArrayList<>();
					searchResultList = DataFactory.jsonToArrayPDFList(result);
					if (searchResultList != null&&searchResultList.size()>0) {
							//数据库中有该用户数据
							if (SQLiteList!=null&&SQLiteList.size()>0){
//								boolean flagRead = SettingUtils.get(mContext,"isRead",true);
//								if (flagRead){
//									try {
//										db.dropTable(TreeVo.class);
//									} catch (DbException e) {
//										e.printStackTrace();
//									}
//								}
								List<String> idList = new ArrayList<>();
								mDialog.getWindow().setLayout(500, 600);
								mDialog.setMax(searchResultList.size()-1);
								//数据库中有数据
								mHandler.sendEmptyMessage(5);
								for (int i=0;i<searchResultList.size();i++){
									Message msg = new Message();
									msg.what = 6;
									msg.obj = i;
									mHandler.sendMessage(msg);
									TreeVo vo = searchResultList.get(i);
									idList.add(vo.getDocID());
									vo.setUserId(userId);
									//判断数据库中是否有该条数据或版本有变动
										boolean flag =true;
											//是否需要入库
											TreeVo treeVo = queryVoById(vo.getId());
											if (treeVo!=null){
												flag = false;
												//数据库中有该条数据，版本不一致则删除旧版本
												if (vo.getVersion()!=null&&!vo.getVersion().equals("")){
													if (!vo.getVersion().equals(treeVo.getVersion())){
														vo.setNew(false);
														VersionTreeVo versionTreeVo = setVersion(vo);
														try {
															db.saveOrUpdate(vo);
															db.save(versionTreeVo);
														} catch (DbException e) {
															e.printStackTrace();
														}
														changeVersionList.add(vo);
//														String path = PropertiesUtil.getPropertiesURL(LoginActivity.this, "filePath");
//														File file = new File(path+"/ftpIndexAll/" + vo.getPdfName());
//														if (file.exists()){
//															file.delete();
//														}
													}
												}
												//获取已阅待阅
												treeVo.setRead(vo.isRead());
												try {
													db.saveOrUpdate(treeVo);
												}catch (Exception e){
													e.printStackTrace();
												}
											}
										if (flag){
											insertData(vo);
										}


								}
								//文件报废
								for (int i=0;i<SQLiteList.size();i++){
									if (!idList.contains(SQLiteList.get(i).getDocID())){
										TreeVo vo = SQLiteList.get(i);
										try {
											db.delete(vo);
										} catch (DbException e) {
											e.printStackTrace();
										}
									}
								}

							}
							//数据库中没有数据就纯添加
							else{
								mDialog.getWindow().setLayout(500, 600);
								mDialog.setMax(searchResultList.size()-1);
								//数据库中有数据
								mHandler.sendEmptyMessage(5);
								for (int i=0;i<searchResultList.size();i++){
									Message msg = new Message();
									msg.what = 6;
									msg.obj = i;
									mHandler.sendMessage(msg);
									TreeVo vo = searchResultList.get(i);
									vo.setUserId(userId);
									boolean flagRead = SettingUtils.get(mContext,"getfilelistn",true);
									if (flagRead){
										vo.setRead(true);
									}else{
										vo.setRead(false);
									}

									insertData(vo);
								}
								SettingUtils.set(mContext,"getfilelistn",false);
							}

						mHandler.sendEmptyMessage(1);
					}

				}else{
					mHandler.sendEmptyMessage(4);
				}
			}

		}
	};


	private void insertData(TreeVo vo){
		try {
			db.save(vo);
			//db.saveOrUpdate(person);
			//db.saveBindingId(person);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户ID查询PDF信息
	 * @param userId
	 * @return
	 */
	private List<TreeVo> queryAll(String userId) {
		List<TreeVo> persons = new ArrayList<TreeVo>();
		try {
			persons = db.selector(TreeVo.class).where("userId","=",userId).findAll();
//					.and("id","<",4)
		} catch (DbException e) {
			e.printStackTrace();
		}
		return persons;
	}


	/**
	 * 根据D查询PDF信息
	 * @param id
	 * @return
	 */
	private TreeVo queryVoById(int id) {
		TreeVo persons = new TreeVo();
		try {
			persons = db.selector(TreeVo.class).where("id","=",id).findFirst();
//					.and("id","<",4)
		} catch (DbException e) {
			e.printStackTrace();
		}
		return persons;
	}

	/**
	 * 获取文件类别信息
	 */
	private void getTypeList(){
		MyWebWraper.getInstance().getTypeList(LoginActivity.this, cb_typelist);
	}




	private HttpResultCallBack cb_typelist = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
//			handler.sendEmptyMessage(LOADING_END);
			String result = String.valueOf(objs[0]);
			boolean isException = handleException(LoginActivity.this, result);
			if (!result.equals("")&&!result.equals("[]")) {
				try {

				List<TypeVo> typeVoList = new ArrayList<>();
				typeVoList = DataFactory.jsonToArrayList(result);
					if (typeVoList!=null&&typeVoList.size()>0){
						db.dropTable(TypeVo.class);
						mDialog.getWindow().setLayout(500, 600);
						mDialog.setMax(typeVoList.size()-1);
						mDialog.setMessage("正在更新类别数据,请稍后");
						mHandler.sendEmptyMessage(5);
						for (int i=0;i<typeVoList.size();i++){
							Message msg = new Message();
							msg.what = 6;
							msg.obj = i;
							mHandler.sendMessage(msg);
							TypeVo vo = typeVoList.get(i);
							if (vo.getName().contains("运管中心规章制度体系")){
								vo.setName("企业标准");
							}
							if (!vo.getName().equals("理票务管理")){
								db.save(vo);
							}

						}
						getPDFList();
					}else{
						handler.sendEmptyMessage(LOADING_END);
						Toast.makeText(LoginActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
					}

				} catch (DbException e) {
					e.printStackTrace();
				}
			}else{
				mHandler.sendEmptyMessage(3);
			}

		}
	};


	private VersionTreeVo setVersion(TreeVo vo){
		VersionTreeVo versionTreeVo = new VersionTreeVo();
		versionTreeVo .setTableId(vo.getTableId());
		versionTreeVo.setId(vo.getId());
		versionTreeVo.setUserId(vo.getUserId());
		versionTreeVo.setName(vo.getName());
		versionTreeVo.setParentId(vo.getParentId());
		versionTreeVo.setDocNum(vo.getDocNum());
		versionTreeVo.setPdfNameUrl(vo.getPdfNameUrl());
		versionTreeVo.setPdfName(vo.getPdfName());
		versionTreeVo.setTblStudyDataId(vo.getTblStudyDataId());
		versionTreeVo.setStudyStatus(vo.getStudyStatus());
		versionTreeVo.setStudiedNum(vo.getStudiedNum());
		versionTreeVo.setDocName(vo.getDocName());
		versionTreeVo.setRuleNum(vo.getRuleNum());
		versionTreeVo.setDepartmentNos(vo.getDepartmentNos());
		versionTreeVo.setDepartmentNames(vo.getDepartmentNames());
		versionTreeVo.setCreateTime(vo.getCreateTime());
		versionTreeVo.setCreateTimeShow(vo.getCreateTimeShow());
		versionTreeVo.setDocID(vo.getDocID());
		versionTreeVo.setFolderName(vo.getFolderName());
		versionTreeVo.setHisStatus(vo.getHisStatus());
		versionTreeVo.setPageCount(vo.getPageCount());
		versionTreeVo.setSendRange(vo.getSendRange());
		versionTreeVo.setSendRangeCode(vo.getSendRangeCode());
		versionTreeVo.setTitle(vo.getTitle());
		versionTreeVo.setIsExpert(vo.getIsExpert());
		versionTreeVo.setVersion(vo.getVersion());
		versionTreeVo.setReplaceID(vo.getReplaceID());
		versionTreeVo.setMedifyNum(vo.getMedifyNum());
		versionTreeVo.setTblCategoryId(vo.getTblCategoryId());
		return versionTreeVo;
	}



}
