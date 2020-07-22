package com.ineasnet.shanghaiguidao2.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.R.id;
import com.ineasnet.shanghaiguidao2.R.layout;
import com.ineasnet.shanghaiguidao2.R.menu;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.vo.AppVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserBitmap;
import com.ineasnet.shanghaiguidao2.entity.vo.VersionTreeVo;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.ApkUtil;
import com.ineasnet.shanghaiguidao2.util.QueryHistoryVo;
import com.ineasnet.shanghaiguidao2.util.QueryUtil;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.ineasnet.shanghaiguidao2.view.CommonProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import androidPT.ptUtil.JSONUtil;
import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;
import androidPT.ptUtil.storage.StorageUtil;


public class StartActivity extends MyBaseActivity implements CommonProgressDialog.OnOkAndCancelListener{
	
	private Context mContext;
	
	private String result;
	private StorageUtil storageUtil;
	private Dialog dialog;
	// private TextView tv_title;
	public static String token;

	private MyApplication mApp;
	private AlertDialog alertiDalog;
	private boolean isAuthed;
	private boolean isAuth = false;
	private String mac;
	private DbManager.DaoConfig config;
	private DbManager db;
	private List<UserBitmap> historyPic = new ArrayList<>();
	private CommonProgressDialog mDialog;
	private String appURL;
	private AppVo appVo;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String result = String.valueOf(msg.obj);
				if (!TextUtils.isEmpty(result)) {
					JSONObject object = JSON.parseObject(result);
					String message = object.getString("exception");
					if (message.contains("终端未授权")){
						message = "终端未授权，授权码是："+mac;
					}
					showAuthDialog(message);
				}
				break;
			case 2:
				Intent intent = new Intent(mContext, LoginActivity.class);
				startActivity(intent);
				finish();
				break;
				case 3:
					uploadHistoryPic();
					new Handler().postDelayed(new Runnable(){
						public void run() {
							//execute the task
							mac = getMacNew();
//							mac = "88:44:77:4f:38:d6";
							MyWebWraper.getInstance().getAuth(StartActivity.this, mac, cb_auth);
						}
					}, 2000);
					break;
				case 4:
					Toast.makeText(mContext, appVo.getException(), Toast.LENGTH_SHORT).show();
					mHandler.sendEmptyMessage(3);
					break;
				case 5:
					initMessageDialog(appVo.getAppUrl());
					break;
			default:
				break;
			}
		};
	};
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        mContext = StartActivity.this;
        
        storageUtil = new StorageUtil(mContext);
        mApp = (MyApplication) getApplication();
		config = mApp.getDaoConfig();
		db = x.getDb(config);
	    boolean flagRead = SettingUtils.get(mContext,"getfilelistn",true);
	    if (flagRead){
		    try {
			    db.dropTable(TreeVo.class);
		    } catch (DbException e) {
			    e.printStackTrace();
		    }
	    }
//		try {
//			db.dropTable(User.class);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
		initDialog();


		if (!isAuthed) {

			if (isOpenNetwork()) {
				String code = ApkUtil.getVersion(mContext);
//				float codeNo = Integer.valueOf(code)*100;
				MyWebWraper.getInstance().getAppVersion(mContext,"1",code,cb_version);

			} else {
				token = storageUtil.getToken();
				initErrorDialog(StartActivity.this,"服务器获取失败,请检查网络连接");
			}
//			Intent intent = new Intent(mContext, LoginActivity.class);
//			startActivity(intent);
//			finish();
		}
        
    }


    
    HttpResultCallBack cb_auth = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
			result = String.valueOf(objs[0]);
			boolean isException = handleException(StartActivity.this, result);
			if (isException) {
				isAuth = true;
				JSONObject jsonObject = (JSONObject) JSONUtil.parseObj(result);
				token = jsonObject.getString("root");
				storageUtil.storeToken(token);
				mApp.setAuthed(true);
				mHandler.sendEmptyMessage(2);
			} else {
				isAuth = false;
				mApp.setAuthed(false);
				storageUtil.storeToken("");
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result;
				mHandler.sendMessage(msg);

			}
		}
	};

	HttpResultCallBack cb_version = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
			String appresult = String.valueOf(objs[0]);
			boolean isException = handleException(StartActivity.this, appresult);
			if (appresult!=null&&appresult.contains("exception")){
				gson = new Gson();
				appVo = gson.fromJson(appresult,AppVo.class);
				mHandler.sendEmptyMessage(3);
				String code = ApkUtil.getVersion(mContext);
				SettingUtils.set(mContext,"version",code);
				return;
			}
			if(appresult!=null&&!appresult.equals("")){
				gson = new Gson();
				appVo = gson.fromJson(appresult,AppVo.class);
				SettingUtils.set(mContext,"version",appVo.getAppVersion());
				//是否有新版本
				if (appVo.getAppUrl()!=null&&!appVo.getAppUrl().equals("")){
					mHandler.sendEmptyMessage(5);
				}else{
					SettingUtils.set(mContext,"version",appVo.getAppVersion());
					mHandler.sendEmptyMessage(3);
				}
			}
			}

	};
    
    private void showAuthDialog(String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				mContext);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
		dialog = builder.create();
		dialog.show();
	}
    


	@Override
	protected void handleHttpResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}



	private String getMacNew(){
		String macString = "";
		Enumeration<NetworkInterface> interfaces = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iF = interfaces.nextElement();

				byte[] addr = iF.getHardwareAddress();
				if (addr == null || addr.length == 0) {
					continue;
				}

				StringBuilder buf = new StringBuilder();
				for (byte b : addr) {
					buf.append(String.format("%02X:", b));
				}
				if (buf.length() > 0) {
					buf.deleteCharAt(buf.length() - 1);
				}
				String mac = buf.toString();
				Log.d("mac", "interfaceName="+iF.getName()+", mac="+mac);
				if(iF.getName().equals("wlan0")){
					macString = mac;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return macString;
	}


	/**
	 * 上传图片
	 */
	private void uploadHistoryPic(){
		try {
			if (db.findAll(UserBitmap.class) != null) {
				historyPic = db.findAll(UserBitmap.class);
				if (historyPic!=null&&historyPic.size()>0){
					String tblStudyDataId = historyPic.get(0).getTblStudyDataId();
					String userId = historyPic.get(0).getUserId();
					String time = historyPic.get(0).getCreateTime();
					TreeVo resultTree = QueryUtil.QueryByID(mContext,db,tblStudyDataId);
					String studyId = "";
					if (!resultTree.getParentId().equals("null")){
						String parentId = resultTree.getParentId();
						TreeVo parentVo = QueryUtil.QueryByID(mContext,db,parentId);
						studyId = String.valueOf(parentVo.getId());
					}else{
						studyId = String.valueOf(resultTree.getId());
					}
					final List<String> filePath = new ArrayList<String>();
					final List<String> users = new ArrayList<String>();
					for (int i=0;i<historyPic.size();i++){
						String path = historyPic.get(i).getFilaPath();
						if (path!=null&&!path.equals("")){
							filePath.add(path);
							users.add(historyPic.get(i).getUserId());
						}
					}
					MyWebWraper.getInstance().uploadSign(mContext, tblStudyDataId, filePath,users, userId,time,new HttpResultCallBack() {

						@Override
						public void onResultCallBack(Object... objs) {
							// TODO Auto-generated method stub
							for (int i=0;i<historyPic.size();i++){
								try {
									db.delete(historyPic.get(i));
								} catch (DbException e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
	}






	private void downFile(String URL) {
		final String path = FileName.FILEPATH + "app-debug.apk";
		File file = new File(path);
		if (file.exists()){
			file.delete();
		}
		RequestParams params = new RequestParams(URL);
		params.setHeader("Cookie", SettingUtils.get(StartActivity.this, "cookie"));
		params.setSaveFilePath(path);
		final boolean[] flag = {true};
		x.http().get(params, new Callback.ProgressCallback<File>() {

			@Override
			public void onWaiting() {

			}

			@Override
			public void onStarted() {
				mDialog.show();
				//设置窗口的大小
				mDialog.getWindow().setLayout(500, 600);

			}

			@Override
			public void onLoading(long total, long current, boolean isDownloading) {
				String result = current + "/" + total;
				int totalNum = Integer.valueOf(String.valueOf(total));
				int currentNum = Integer.valueOf(String.valueOf(current));
				Log.d("下载进度", result);
				if (flag[0]){
					mDialog.setMax(totalNum);
					flag[0] = false;
				}
				mDialog.setProgress(currentNum);
			}

			@Override
			public void onSuccess(File result) {
				if (mDialog.isShowing()){
					mDialog.dismiss();
				}
				ApkUtil.installAPk(StartActivity.this,result);

			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				if (!ex.getMessage().equals("Invalid index 0, size is 0")){
//                    Toast.makeText(mContext, "文件下载失败", Toast.LENGTH_SHORT).show();
				}
				mDialog.dismiss();
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
	 * 下载进度条
	 */
	public void initDialog() {
		if (mDialog == null){
			mDialog = new CommonProgressDialog(StartActivity.this);
			mDialog.setMessage("正在下载");
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setMax(100);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			mDialog.setOnOkAndCancelListener(this);
		}

	}

	@Override
	public void onCancel(View v) {

	}


	/**
	 * 文件下载弹框
	 */
	private void initMessageDialog(final String URL){

		if (alertiDalog == null){
			// 创建构建器
			AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// 设置参数
			builder.setTitle("您有新版本需要安装，是否下载新版本");
			builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					SettingUtils.set(mContext,"version",appVo.getAppVersion());
					downFile(URL);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					mHandler.sendEmptyMessage(3);
				}
			});
			alertiDalog = builder.create();
		}
		alertiDalog.show();
	}


	@Override
	public void doNext() {
		super.doNext();
		if(TextUtils.isEmpty(token)){
			isAuth = false;
			finish();
		}else{
			isAuth = true;
			mHandler.sendEmptyMessage(2);
		}

	}
}
