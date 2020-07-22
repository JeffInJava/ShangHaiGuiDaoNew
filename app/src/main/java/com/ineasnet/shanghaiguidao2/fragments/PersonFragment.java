package com.ineasnet.shanghaiguidao2.fragments;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.activity.DownloadListActivity;
import com.ineasnet.shanghaiguidao2.activity.LoginActivity;
import com.ineasnet.shanghaiguidao2.activity.MainActivity;
import com.ineasnet.shanghaiguidao2.activity.ReadStatusActivity;
import com.ineasnet.shanghaiguidao2.activity.SelectUserActivity;
import com.ineasnet.shanghaiguidao2.activity.StartActivity;
import com.ineasnet.shanghaiguidao2.activity.UserCheckActivity;
import com.ineasnet.shanghaiguidao2.entity.vo.AppVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.ApkUtil;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.ineasnet.shanghaiguidao2.view.CommonProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.base.MyBaseFragment;
import androidPT.ptUtil.internet.HttpInstance;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;

public class PersonFragment extends MyBaseFragment implements OnClickListener,CommonProgressDialog.OnOkAndCancelListener{

	private TextView tv_userName, tv_status;
	private TextView tv_head;

	private LinearLayout layout_read, layout_unread,layout_downloadList, layout_version,layout_exit;

	/*************************************************/
	private View v;

	private String userId;
	private String userName;
	boolean isFromPro = false;
	private String departmentNo;
	private DbManager.DaoConfig config;
	private MyApplication application;
	private DbManager db;
	private CommonProgressDialog mDialog;
	private Context mContext;
	private String appURL;
	private AppVo appVo;
	private AlertDialog alertiDalog;
	private ImageView iv_red_doit;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					iv_red_doit.setVisibility(View.VISIBLE);
					break;
				case 2:
					iv_red_doit.setVisibility(View.GONE);
					break;
				case 3:
					break;
				case 4:
					Toast.makeText(mContext, appVo.getException(), Toast.LENGTH_SHORT).show();
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
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		v = inflater.inflate(R.layout.fragment_personal, null);
		mContext = getActivity();
		userId = SettingUtils.get(getActivity(),"userId");
		userName = SettingUtils.get(getActivity(),"userName");
		isFromPro = MainActivity.isFromPro;
		departmentNo =SettingUtils.get(getActivity(),"departmentNo");
		String str_head = getResources().getString(R.string.str_personal);
		tv_head = (TextView) v.findViewById(R.id.tv_title_content);
		tv_head.setText(str_head);

		init();
		application = (MyApplication) getActivity().getApplication();
		config = application.getDaoConfig();
		db = x.getDb(config);
		initDialog();
		return v;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		tv_userName = (TextView) v.findViewById(R.id.tv_nickName);
		tv_status = (TextView) v.findViewById(R.id.tv_userStatus);
		layout_read = (LinearLayout) v.findViewById(R.id.layout_read);
		layout_unread = (LinearLayout) v.findViewById(R.id.layout_unread);
		layout_downloadList = (LinearLayout) v.findViewById(R.id.layout_downloadlist);
		layout_exit = (LinearLayout) v.findViewById(R.id.layout_exit);
		layout_version = (LinearLayout) v.findViewById(R.id.layout_version);
		iv_red_doit = (ImageView) v.findViewById(R.id.iv_red_doit);

		tv_userName.setText(userName);
		tv_status.setText("在线");

		layout_read.setOnClickListener(this);
		layout_unread.setOnClickListener(this);
		layout_exit.setOnClickListener(this);
		layout_version.setOnClickListener(this);
		layout_downloadList.setOnClickListener(this);
		String code = ApkUtil.getVersion(mContext);
		MyWebWraper.getInstance().getAppVersion(mContext,"1",code,cb_version);

	}

	@Override
	protected void handleHttpResult() {
		// TODO Auto-generated method stub

	}
	//youye8029@163.com
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.layout_read:
//			intent.setAction(HttpInstance.ACTION_READ);
//			switchToIndex(intent);
			intent.setClass(getActivity(), ReadStatusActivity.class);
			intent.putExtra("isRead", true);
			getActivity().startActivity(intent);
			
			break;
		case R.id.layout_unread:
//			intent.setAction(HttpInstance.ACTION_UNREAD);
//			switchToIndex(intent);
			intent.setClass(getActivity(), ReadStatusActivity.class);
			intent.putExtra("isRead", false);
			getActivity().startActivity(intent);
			break;
		case R.id.layout_exit:
			intent.setClass(getActivity(), LoginActivity.class);
			getActivity().startActivity(intent);
			getActivity().finish();
			break;
		case R.id.layout_downloadlist:
			intent.setClass(getActivity(), SelectUserActivity.class);
			intent.putExtra("userId", userId);
			intent.putExtra("userName", userName);
			intent.putExtra("isFromPro", isFromPro);
			intent.putExtra("departmengNo",departmentNo);
			getActivity().startActivity(intent);
			break;
			case R.id.layout_version:
				if (appVo!=null&&appVo.getAppUrl()!=null&&!appVo.getAppUrl().equals("")){
					mHandler.sendEmptyMessage(5);
				}else{
					Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
				}
				break;
		default:
			break;
		}
	}
	
	private void switchToIndex(Intent intent){
		MainActivity activity = (MainActivity) getActivity();
		activity.switchToIndex();
		getActivity().sendBroadcast(intent);
	}

	HttpResultCallBack cb_version = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
			String appresult = String.valueOf(objs[0]);
			boolean isException = handleException(getActivity(), appresult);
			if (appresult!=null&&appresult.contains("exception")){
				gson = new Gson();
				appVo = gson.fromJson(appresult,AppVo.class);
				mHandler.sendEmptyMessage(2);
//				Toast.makeText(mContext,appVo.getException(),Toast.LENGTH_SHORT).show();
				return;
			}
			if(appresult!=null&&!appresult.equals("")){
				gson = new Gson();
				appVo = gson.fromJson(appresult,AppVo.class);
				//是否有新版本
				if (appVo.getAppUrl()!=null&&!appVo.getAppUrl().equals("")){
					mHandler.sendEmptyMessage(1);
				}else{
					mHandler.sendEmptyMessage(2);
				}
			}else{
				mHandler.sendEmptyMessage(2);
			}
		}

	};


	/**
	 * 下载进度条
	 */
	public void initDialog() {
		if (mDialog == null){
			mDialog = new CommonProgressDialog(getActivity());
			mDialog.setMessage("正在下载");
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setMax(100);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			mDialog.setOnOkAndCancelListener(this);
		}

	}



	private void downFile(String URL) {
		final String path = FileName.FILEPATH + "app-debug.apk";
		File file = new File(path);
		if (file.exists()){
			file.delete();
		}
		RequestParams params = new RequestParams(URL);
		params.setHeader("Cookie", SettingUtils.get(getActivity(), "cookie"));
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
				ApkUtil.installAPk(getActivity(),result);

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
	 * 文件下载弹框
	 */
	private void initMessageDialog(final String URL){

		if (alertiDalog == null){
			// 创建构建器
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// 设置参数
			builder.setTitle("您有新版本需要安装，是否下载新版本");
			builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					downFile(URL);
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			alertiDalog = builder.create();
		}
		alertiDalog.show();
	}


	@Override
	public void onCancel(View v) {
		
	}
}
