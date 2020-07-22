package com.ineasnet.shanghaiguidao2.activity;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.R.layout;
import com.ineasnet.shanghaiguidao2.adapters.ArticleAdapter;
import com.ineasnet.shanghaiguidao2.adapters.MyListAdapter;
import com.ineasnet.shanghaiguidao2.adapters.TypeAdapter;
import com.ineasnet.shanghaiguidao2.adapters.UserCheckPopAdapter;
import com.ineasnet.shanghaiguidao2.dao.FileDao;
import com.ineasnet.shanghaiguidao2.entity.bean.CheckBean;
import com.ineasnet.shanghaiguidao2.entity.bean.ListVoBean;
import com.ineasnet.shanghaiguidao2.entity.bean.ReadBookBean;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.bean.UserListCheckedBean;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserHistoryVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserUpload;
import com.ineasnet.shanghaiguidao2.entity.vo.VersionTreeVo;
import com.ineasnet.shanghaiguidao2.network.FileName;
import com.ineasnet.shanghaiguidao2.util.DataFactory;
import com.ineasnet.shanghaiguidao2.util.ListUtil;
import com.ineasnet.shanghaiguidao2.util.QueryHistoryVo;
import com.ineasnet.shanghaiguidao2.util.QueryUtil;
import com.ineasnet.shanghaiguidao2.util.SettingUtils;
import com.ineasnet.shanghaiguidao2.util.TimeUtil;
import com.ineasnet.shanghaiguidao2.view.CommonProgressDialog;
import com.ineasnet.shanghaiguidao2.view.MyListView;
import com.ldd.pullview.AbPullToRefreshView;
import com.orhanobut.logger.Logger;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import javax.net.ssl.SSLContext;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpResultCallBack;
import androidPT.ptUtil.internet.MyWebWraper;

public class ReadStatusActivity extends MyBaseActivity implements CommonProgressDialog.OnOkAndCancelListener,View.OnClickListener,AbPullToRefreshView.OnHeaderRefreshListener,AbPullToRefreshView.OnFooterLoadListener {

	private TextView tv_title;
	private MyListView listView;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	
	private Context mContext;
	/**
	 * 已阅 true 未阅 false
	 */
	private boolean isReaded;
	
	private List<TreeVo> allList = new ArrayList<>();
	private List<TreeVo> childList= new ArrayList<>();
	private ArticleAdapter listAdapter;
	//文件路径
	private String path;
	private AlertDialog dialog;
	private TreeVo dialogVo = null;
	private int dialogPosition;
	private TreeVo resultTree;
	private TreeVo secondVo = null;
	private PopupWindow window;
	private View userView = null;
	Button bt_check = null;
	Button bt_cancel = null;
	Button bt_search = null;
	ListView list_user = null;
	UserCheckPopAdapter userCheckAdapter = null;
	private List<User> userList = new ArrayList<>();
	private List<User> userListData = new ArrayList<>();
	private List<UserHistoryVo> listUsers = new ArrayList<>();
	private List<User> checkedList = new ArrayList<>();
	private List<TreeVo> allListNum = new ArrayList<>();
	private List<TreeVo> dayList = new ArrayList<>();
	private List<TreeVo> fourList = new ArrayList<>();
	private List<TreeVo> weekList = new ArrayList<>();
	private List<TreeVo> monthList = new ArrayList<>();
	private DbManager.DaoConfig config;
	private MyApplication application;
	private DbManager db;
	private CommonProgressDialog mDialog;
	private boolean isCancel; // 用来判断是否点击了取消
	private Intent intent;
	private boolean isFromIndexflag;
	private String userId;
	private Button bt_title_content;
	private PopupWindow searchTypeWindow;
	ListView list_user3 = null;
	private TypeAdapter searchAdapter;
	private List<CheckBean> searchList = new ArrayList<>();
	private int allSearch = 0;
	private int page = 1;
	//附件列表
	private List<TreeVo> secondList = new ArrayList<>();


	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				handler.sendEmptyMessage(LOADING_END);
				String result = String.valueOf(msg.obj);
				gson = new Gson();
				if(result!=null&&!result.equals("null")&&!result.equals("")&&!result.equals("[]")){
					try {
						allList = DataFactory.jsonToArrayPDFList(result);
//						ReadBookBean readBookBean = new ReadBookBean();
//						readBookBean = gson.fromJson(result,ReadBookBean.class);
//						allList = readBookBean.getRows();
						childList = new ArrayList<TreeVo>();

						//清空待阅数据
						if (allList!=null&&allList.size() > 0){
							for (int i=0;i<allList.size();i++){
								TreeVo vo = allList.get(i);
								TreeVo localVo = QueryUtil.QueryByID(mContext,db,String.valueOf(vo.getId()));
								if (localVo!=null){
									localVo.setRead(true);
									localVo.setStudyTime(allList.get(i).getStudyTime());
									try {
										db.saveOrUpdate(localVo);
										if (!localVo.getParentId().equals("null")){
											TreeVo parentVo = QueryUtil.QueryByID(mContext,db,String.valueOf(localVo.getParentId()));
											parentVo.setRead(true);
											parentVo.setStudyTime(allList.get(i).getStudyTime());
											db.saveOrUpdate(parentVo);
										}
									} catch (DbException e) {
										e.printStackTrace();
									}
								}

							}
						}
						//清空本地待阅数据
						else{
							removeAllRead();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}


				//是否是已阅
				if (isReaded){
					allList.clear();
					allList = QueryUtil.QueryByPage(mContext,db,isReaded,page);
					allListNum.addAll(allList);
					for (int i=0;i<allList.size();i++){
						int day = TimeUtil.getDayNum(allList.get(i).getStudyTime());
						if (day<1){
							dayList.add(allList.get(i));
						}
						if(day<4){
							fourList.add(allList.get(i));
						}
						if(day<7){
							weekList.add(allList.get(i));
						}
						if(day<31){
							monthList.add(allList.get(i));
						}
					}
					childList.clear();
				}else{
					List<TreeVo> allPDFList = QueryUtil.QueryByPage(mContext,db,isReaded,page);
					allList.clear();
					allList.addAll(allPDFList);
					allListNum = allList;
					childList.clear();
				}

				if (allList!=null&&allList.size() > 0) {
					if (listAdapter == null){
						listAdapter = new ArticleAdapter(mContext, allList);
						listView.setAdapter(listAdapter);
					}else{
						listAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(mContext, "未查询到结果", Toast.LENGTH_SHORT)
							.show();
				}
				break;
				//下载完毕
				case 11:
					TreeVo vo = (TreeVo) msg.obj;
					vo.setDownloading(false);
					allList.set(msg.arg1,vo);
					FileDao dao = new FileDao();
					dao.setPdfName(vo.getPdfName());
					dao.setPdfNameUrl(vo.getPdfNameUrl());
//					try {
//						db.save(dao);
//						fileDaoList = db.selector(FileDao.class).findAll();
					listAdapter = new ArticleAdapter(mContext, allList);
					listView.setAdapter(listAdapter);
//					} catch (DbException e) {
//						e.printStackTrace();
//					}
					Toast.makeText(mContext, "下载完毕", Toast.LENGTH_SHORT).show();
					break;
				//阅读PDF
				case 12:
					String userId = SettingUtils.get(ReadStatusActivity.this,"");
					String pdfName = resultTree.getPdfName();
					String path = FileName.FILEPATH + "ftpIndexAll/" + resultTree.getPdfName();
					UserListCheckedBean userList2 = new UserListCheckedBean();
					userList2.setUserList(checkedList);
					intent = new Intent(mContext,PDFNewActivity.class);
					intent.putExtra("users",userList2);
					intent.putExtra("filePath", path);
					intent.putExtra("tblStudyDataId", resultTree.getId());
					intent.putExtra("userId", userId);
					intent.putExtra("isFromIndex", isFromIndexflag);
					intent.putExtra("page", resultTree.getPageCount());
					intent.putExtra("departmentNo",MainActivity.departmentNo);
					intent.putExtra("fileName",pdfName);
					intent.putExtra("title",resultTree.getName());
					mContext.startActivity(intent);
					closePopupWindow();
					break;
			default:
				break;
			}
			
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_status);
		initView();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()){
			case R.id.bt_title_content:
				startSearchTypePopupWindow();
				break;
		}
	}

	@Override
	protected void handleHttpResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView)findViewById(R.id.tv_title_content);
		listView = (MyListView) findViewById(R.id.listview_read_status);
		bt_title_content = (Button) findViewById(R.id.bt_title_content);
		listView.setOnItemClickListener(listViewClick);
		mAbPullToRefreshView = (AbPullToRefreshView)findViewById(R.id.mPullRefreshView);

		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
		application = (MyApplication) getApplication();
		config = application.getDaoConfig();
		db = x.getDb(config);
		initData();
		initDialog();
		initUsers();
		initUserPop();
		initSearchTypeInfo();
		initSearchType();
	}


	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessage(LOADING);
		if (isOpenNetwork()){
			MyWebWraper.getInstance().getFileList(mContext, new HttpResultCallBack() {

				@Override
				public void onResultCallBack(Object... objs) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = 1 ;
					msg.obj = objs[0];
					mHandler.sendMessage(msg);
				}
			});
		}else{
			Message msg = new Message();
			msg.what = 1 ;
			msg.obj = "";
			mHandler.sendMessage(msg);
		}

	}

	private void initData(){
		mContext = ReadStatusActivity.this;
		isReaded = getIntent().getBooleanExtra("isRead", true);
		path = FileName.FILEPATH ;
		if(isReaded){
			tv_title.setText("已阅");
			bt_title_content.setVisibility(View.VISIBLE);
			bt_title_content.setOnClickListener(this);
		}else{
			tv_title.setText("待阅");
		}


	}


	/**
	 * listview OnItemClickListener
	 */
	AdapterView.OnItemClickListener listViewClick = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
			// TODO Auto-generated method stub
//			TreeVo vo = allList.get(arg2);
			String id = String.valueOf(allList.get(arg2).getId());
			TreeVo vo = QueryUtil.QueryByID(mContext,db,id);
			boolean isLast = true;

				if (vo.getPdfNameUrl() == null) {
					Toast.makeText(mContext, "没有文件", Toast.LENGTH_SHORT).show();
					return;
				}

				String pdfName = vo.getPdfName();
				boolean flag = false;
				File file = new File(path+"ftpIndexAll/" + pdfName);
				if (!file.exists()){
					flag = true;
				}
				//本地无文件并且处于非下载状态
				if (flag) {
					dialogVo = vo;
					dialogPosition = arg2;
					initMessageDialog(vo,arg2,"该文件还未下载，是否下载该文件");
				}
				else if(vo.isNew() == false){
					dialogVo = vo;
					dialogPosition = arg2;
					initMessageDialog(vo,arg2,"该文件已有新版本，请下载新版本阅读");
				}
				else {
					int position = arg2-listView.getFirstVisiblePosition();
					jumpToPDFShow(vo, position, false, 0,arg1);
				}

		}
	};


	/**
	 * 设置搜索类型数据
	 */
	private void initSearchTypeInfo(){
		CheckBean checkBean1 = new CheckBean();
		checkBean1.setName("全部");
		checkBean1.setCheck(true);
		CheckBean checkBean2 = new CheckBean();
		checkBean2.setName("一天内");
		CheckBean checkBean3 = new CheckBean();
		checkBean3.setName("四天内");
		CheckBean checkBean4 = new CheckBean();
		checkBean4.setName("一周内");
		CheckBean checkBean5 = new CheckBean();
		checkBean5.setName("一月内");
		searchList.add(checkBean1);
		searchList.add(checkBean2);
		searchList.add(checkBean3);
		searchList.add(checkBean4);
		searchList.add(checkBean5);
	}



	/**
	 * 文件下载弹框
	 */
	private void initMessageDialog(final TreeVo vo,final int position,String title){
		if (dialog == null){
			// 创建构建器
			AlertDialog.Builder builder = new AlertDialog.Builder(ReadStatusActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
			// 设置参数
			builder.setTitle(title);
			builder .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

				@Override
				public void onClick(DialogInterface dialog,
									int which) {
					// TODO Auto-generated method stub
					downFile(dialogVo,dialogPosition);
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
			dialog = builder.create();
		}
		dialog.show();
	}


	private void jumpToPDFShow(TreeVo vo, int position, boolean isFromIndex,
							   int page,View view) {

		ImageView iv_article = (ImageView) view.findViewById(R.id.iv_article);
//		isFromIndexflag = isFromIndex;
		resultTree = vo;
		startPopupWindow(vo,iv_article,position);
	}





	private void downFile(final TreeVo vo, final int position) {
		String path = FileName.FILEPATH + "ftpIndexAll/" + dialogVo.getPdfName();
		String url = dialogVo.getPdfNameUrl();
		url = url.replace("http","https");
		RequestParams params = new RequestParams(url);
		SSLContext sslContext = PropertiesUtil.getSSLContext(mContext);
		/*if (null == sslContext) {
			Toast.makeText(mContext, "证书为空", Toast.LENGTH_SHORT).show();
			return ;
		}
		params.setSslSocketFactory(sslContext.getSocketFactory());*/
		params.setHeader("Cookie", SettingUtils.get(ReadStatusActivity.this, "cookie"));
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

				if (isDownloading){
					dialogVo.setDownloading(true);
					allList.set(dialogPosition,dialogVo);
					mHandler.sendEmptyMessage(10);
					mDialog.setProgress(currentNum);
				}

			}

			@Override
			public void onSuccess(File result) {
				if (mDialog.isShowing()){
					mDialog.dismiss();
				}
				allList = allListNum;
				//更新版本
				VersionTreeVo versionTreeVo = QueryHistoryVo.QueryVersionById(mContext,db,String.valueOf(vo.getId()));
				if (versionTreeVo!=null){
					vo.setVersion(versionTreeVo.getVersion());
					vo.setNew(true);
					try {
						db.saveOrUpdate(vo);
						db.delete(versionTreeVo);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}

				if ( QueryUtil.QueryByPDF(mContext,db,String.valueOf(vo.getId())) == null) {
					Message msg = new Message();
					msg.what = 11;
					msg.obj = vo;
					msg.arg1 = position;
					mHandler.sendMessage(msg);
				}else{
					secondVo = QueryUtil.QueryByPDF(mContext,db,String.valueOf(vo.getId()));
					secondList = QueryUtil.QueryByPDFList(mContext,db,String.valueOf(vo.getId()));
					downFileSecond();
					Message msg = new Message();
					msg.what = 11;
					msg.obj = vo;
					msg.arg1 = position;
					mHandler.sendMessage(msg);
				}

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

	private void downFileSecond() {
		String path = FileName.FILEPATH + "ftpIndexAll/" + secondList.get(0).getPdfName();
		String url = secondList.get(0).getPdfNameUrl();
		url = url.replace("http","https");
		RequestParams params = new RequestParams(url);
		SSLContext sslContext = PropertiesUtil.getSSLContext(mContext);
		/*if (null == sslContext) {
			Toast.makeText(mContext, "证书为空", Toast.LENGTH_SHORT).show();
			return ;
		}
		params.setSslSocketFactory(sslContext.getSocketFactory());*/
		params.setHeader("Cookie", SettingUtils.get(this, "cookie"));
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

				if (isDownloading){
					dialogVo.setDownloading(true);
					allList.set(dialogPosition,dialogVo);
//                    mHandler.sendEmptyMessage(10);
					mDialog.setProgress(currentNum);
				}

			}

			@Override
			public void onSuccess(File result) {
				if (mDialog.isShowing()){
					mDialog.dismiss();
				}
				//更新版本
				VersionTreeVo versionTreeVo = QueryHistoryVo.QueryVersionById(mContext,db,String.valueOf(secondList.get(0).getId()));
				if (versionTreeVo!=null){
					secondList.get(0).setVersion(versionTreeVo.getVersion());
					secondList.get(0).setNew(true);
					try {
						db.saveOrUpdate(secondList.get(0));
						db.delete(versionTreeVo);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
				secondList.remove(0);
				if (secondList!=null&&secondList.size()>0){
					downFileSecond();
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Toast.makeText(mContext, "文件下载失败", Toast.LENGTH_SHORT).show();
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



	private void startPopupWindow(TreeVo vo,View view,int position){
		WindowManager.LayoutParams params=getWindow().getAttributes();
		params.alpha=1f;
		getWindow().setAttributes(params);
		resultTree = vo;
		getUserData(String.valueOf(vo.getId()));
		if (position<4){
			userView.setBackground(getResources().getDrawable(R.drawable.dialog_users_bg_up));
			window.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_users_bg_up));
		}else{
			userView.setBackground(getResources().getDrawable(R.drawable.dialog_users_bg));
			window.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_users_bg));
		}
		userCheckAdapter.notifyDataSetChanged(userList);
		window.showAsDropDown(view,200,0);
//        window.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
	}

	/**
	 * 判断是否之前点击过
	 * @param data
	 */
	private void getUserData(String data){
		if (listUsers!=null){
			listUsers.clear();
		}
		try {
			listUsers = db.selector(UserHistoryVo.class).where("userId","=",userId).and("tblStudyDataId","=",data).findAll();
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (listUsers!=null&&listUsers.size()>0){
			for (int i=0;i<userList.size();i++){
				User user = userList.get(i);
				user.setCheck(false);
				userList.set(i,user);
			}
			for (int i=0;i< userList.size();i++){
				UserHistoryVo historyVo = null;
				try {
					historyVo = db.selector(UserHistoryVo.class).where("userId","=",userId).and("tblStudyDataId","=",data).and("code","=", userList.get(i).getCode()).findFirst();
				} catch (DbException e) {
					e.printStackTrace();
				}
				if (historyVo!=null){
					User user = userList.get(i);
					user.setCheck(historyVo.isCheck());
					userList.set(i,user);
				}
			}
		}else{
			if (userList!=null){
				userList.clear();
			}
			try {
				userListData = db.selector(User.class).where("userId","=",userId).findAll();
			} catch (DbException e) {
				e.printStackTrace();
			}
			userList = userListData;
		}
		List<User> checkOffUserList = new ArrayList<>();
		List<User> checkOnUserList = new ArrayList<>();
		for (int i=0;i<userList.size();i++){
			if (!userList.get(i).isCheck()){
				checkOffUserList.add(userList.get(i));
			}else{
				checkOnUserList.add(userList.get(i));
			}
		}
		userList.clear();
		userList.addAll(checkOnUserList);
		userList.addAll(checkOffUserList);
	}

	/**
	 * 历史人员转化为user
	 * @param list
	 * @return
	 */
	private List<UserHistoryVo> getListUser(List<User> list){
		List<UserHistoryVo> result = new ArrayList<>();
		for (int i=0;i<list.size();i++){
			UserHistoryVo user = new UserHistoryVo();
//			user.setId(list.get(i).getId());
			user.setCode(list.get(i).getCode());
			user.setName(list.get(i).getName());
			user.setUserId(userId);
			user.setCheck(list.get(i).isCheck());
			user.setTblStudyDataId(String.valueOf(resultTree.getId()));
			result.add(user);
		}
		return result;
	}


	private void initUsers(){
		gson = new Gson();
		userId = SettingUtils.get(mContext,"userId");
//        String usersJson = SettingUtils.get(mContext,"users"+userId);
//        userList = gson.fromJson(usersJson, new TypeToken<List<User>>(){}.getType());
		try {
			userListData = db.selector(User.class).findAll();
		} catch (DbException e) {
			e.printStackTrace();
		}
		List<User> checkOffUserList = new ArrayList<>();
		List<User> checkOnUserList = new ArrayList<>();
		for (int i=0;i<userListData.size();i++){
			if (!userListData.get(i).isCheck()){
				checkOffUserList.add(userListData.get(i));
			}else{
				checkOnUserList.add(userListData.get(i));
			}
		}
		userList.clear();
		userList.addAll(checkOnUserList);
		userList.addAll(checkOffUserList);
	}

	/**
	 * 人员选择框
	 */
	private void initUserPop(){
		//TODO
		if (window == null){
			LayoutInflater lay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			userView = lay.inflate(R.layout.pop_user, null);
			userView.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_users_bg));
			bt_check = (Button) userView.findViewById(R.id.bt_check_pop);
			bt_cancel = (Button) userView.findViewById(R.id.bt_check_cancel_pop);
			bt_search = (Button) userView.findViewById(R.id.bt_check_search_pop);
			list_user = (ListView) userView.findViewById(R.id.list_user_pop);
			bt_search.setVisibility(View.VISIBLE);
			userCheckAdapter = new UserCheckPopAdapter(mContext,userList);
			list_user.setAdapter(userCheckAdapter);
			window = new PopupWindow(userView, 800,600);
			window.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_users_bg));
		}
		list_user.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				User user = userList.get(i);
				if (user.isCheck()){
					user.setCheck(false);
				}else{
					user.setCheck(true);
				}
				userList.set(i,user);
				userCheckAdapter.notifyDataSetChanged(userList);
			}

		});

		bt_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				closePopupWindow();
			}
		});
		bt_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				checkedList.clear();
				for (int i=0;i<userList.size();i++){
					if (userList.get(i).isCheck()){
						checkedList.add(userList.get(i));
					}
				}
				if(checkedList.size()>0){
					String tblStudyDataId = String.valueOf(resultTree.getId());
					mHandler.sendEmptyMessage(12);
//					MyWebWraper.getInstance().uploadUserList(mContext,tblStudyDataId,getCheckList(),cb_uploadUserList);// 获取部门列表
				}else{
					Toast.makeText(mContext, "您还没有选择组员", Toast.LENGTH_SHORT).show();
				}

			}
		});
		bt_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String tblStudyDataId = String.valueOf(resultTree.getId());
				Intent intent = new Intent(ReadStatusActivity.this,UserCheckAddActivity.class);
				intent.putExtra("tblStudyDataId",tblStudyDataId);
				startActivityForResult(intent,1);
			}
		});
		//使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。
		//如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。
		//listview的话就没有了作用。
		window.setFocusable(true);//如果不设置setFocusable为true，popupwindow里面是获取不到焦点的，那么如果popupwindow里面有输入框等的话就无法输入。
		window.setOutsideTouchable(false);
		window.update();
	}

	/**
	 * 获取所有已选择的成员名单
	 * @return
	 */
	private String getCheckList(){
		String resultStr;
		List<UserUpload> result = new ArrayList<UserUpload>();
		for (int i=0;i<checkedList.size();i++){
			if (checkedList.get(i).isCheck()){
				UserUpload upload = new UserUpload();
				upload.setId(checkedList.get(i).getCode());
				result.add(upload);
			}
		}
		resultStr = gson.toJson(result);
		return resultStr;
	}

	/**
	 * 下载进度条
	 */
	public void initDialog() {
		if (mDialog == null){
			mDialog = new CommonProgressDialog(ReadStatusActivity.this);
			mDialog.setMessage("正在下载");
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setMax(100);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(false);
			mDialog.setOnOkAndCancelListener(this);
		}

	}

	private void closePopupWindow(){
		if (window != null && window.isShowing()) {
			window.dismiss();
//            popupWindow = null;
			WindowManager.LayoutParams params=getWindow().getAttributes();
			params.alpha=1f;
			getWindow().setAttributes(params);
		}
	}

	@Override
	public void onCancel(View v) {
		mDialog.dismiss();
		isCancel = true;
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
			if (result!=null&&!result.equals("")&&!result.contains("exception")) {
				Logger.json(result);
				List<User> checkList = new ArrayList<>();
				for (int i=0;i<userList.size();i++){
					if (userList.get(i).isCheck()){
						checkList.add(userList.get(i));
					}
				}
				List<UserHistoryVo> historyVos = getListUser(checkList);
				try {
					db.delete(UserHistoryVo.class, WhereBuilder.b("userId","=",userId).and("tblStudyDataId","=",resultTree.getId()));
					for (int i=0;i<historyVos.size();i++){
						UserHistoryVo userHistoryVo = historyVos.get(i);
						db.save(userHistoryVo);
					}
				} catch (DbException e) {
					e.printStackTrace();
				}

				mHandler.sendEmptyMessage(12);
			}
		}
	};


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (data!=null){
			switch (resultCode){
				case 1:
					UserListCheckedBean checkedBean = (UserListCheckedBean) data.getSerializableExtra("data");
					List<User> dataList = checkedBean.getUserList();
					for (int i=0;i<userList.size();i++){
						User dataUser = userList.get(i);
						for (int j=0;j<dataList.size();j++){
							if (dataUser.getCode().equals(dataList.get(j).getCode())){
								dataUser.setCheck(true);
								userList.set(i,dataUser);
								break;
							}
						}
					}
					List<User> checkOffUserList = new ArrayList<>();
					List<User> checkOnUserList = new ArrayList<>();
					for (int i=0;i<userList.size();i++){
						if (!userList.get(i).isCheck()){
							checkOffUserList.add(userList.get(i));
						}else{
							checkOnUserList.add(userList.get(i));
						}
					}
					userList.clear();
					userList.addAll(checkOnUserList);
					userList.addAll(checkOffUserList);
					userCheckAdapter.notifyDataSetChanged(userList);
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	private void startSearchTypePopupWindow(){
		WindowManager.LayoutParams params=getWindow().getAttributes();
		params.alpha=1f;
		getWindow().setAttributes(params);
//        typeWindow.showAtLocation(bt_type, Gravity.CENTER_VERTICAL, 0, 0);
		searchTypeWindow.showAsDropDown(bt_title_content,-200,20);
	}



	/**
	 * 查询类别选择框
	 */
	private void initSearchType(){
		//TODO
		if (searchTypeWindow == null){
			LayoutInflater lay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = lay.inflate(R.layout.dialog_type, null);

			list_user3 = (ListView) v.findViewById(R.id.lv_dialog);
			searchAdapter = new TypeAdapter(mContext,searchList);
			list_user3.setAdapter(searchAdapter);
			searchTypeWindow = new PopupWindow(v, 300,500);
			searchTypeWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_message_bg));
		}
		list_user3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				allSearch = i;
				for (int j = 0;j<searchList.size();j++){
					if (j == i){
						CheckBean checkBean = searchList.get(i);
						checkBean.setCheck(true);
						searchList.set(i,checkBean);
					}else{
						CheckBean checkBean = searchList.get(j);
						checkBean.setCheck(false);
						searchList.set(j,checkBean);
					}
				}
				searchAdapter.notifyDataSetChanged();
				searchTypeWindow.dismiss();
				getData();
			}

		});

		//使窗口里面的空间显示其相应的效果，比较点击button时背景颜色改变。
		//如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。
		//listview的话就没有了作用。
		searchTypeWindow.setFocusable(true);//如果不设置setFocusable为true，popupwindow里面是获取不到焦点的，那么如果popupwindow里面有输入框等的话就无法输入。
		searchTypeWindow.setOutsideTouchable(false);
		searchTypeWindow.update();
	}

	private void getData(){
		switch (allSearch){
			case 0:
				allList.clear();
				allList.addAll(allListNum);
				break;
			case 1:
				allList.clear();
				allList.addAll(dayList);
				break;
			case 2:
				allList.clear();
				allList.addAll(fourList);
				break;
			case 3:
				allList.clear();
				allList.addAll(weekList);
				break;
			case 4:
				allList.clear();
				allList.addAll(monthList);
				break;

		}
		bt_title_content.setText(searchList.get(allSearch).getName());
		if (listAdapter == null){
			listAdapter = new ArticleAdapter(mContext, allList);
			listView.setAdapter(listAdapter);
		}else{
			listAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onFooterLoad(AbPullToRefreshView abPullToRefreshView) {
		page += 1;
		List<TreeVo> resultList = new ArrayList<>();
		resultList = QueryUtil.QueryByPage(mContext,db,isReaded,page);
		allList.clear();
		allList.addAll(resultList);
		allListNum = allList;
		listAdapter.notifyDataSetChanged();
		mAbPullToRefreshView.onFooterLoadFinish();
		mAbPullToRefreshView.onHeaderRefreshFinish();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView abPullToRefreshView) {
		page = 1;
		allList.clear();
		List<TreeVo> resultList = new ArrayList<>();
		resultList = QueryUtil.QueryByPage(mContext,db,isReaded,page);
		allList.clear();
		allList.addAll(resultList);
		allListNum = allList;
		listAdapter.notifyDataSetChanged();
		mAbPullToRefreshView.onFooterLoadFinish();
		mAbPullToRefreshView.onHeaderRefreshFinish();
	}


	/**
	 * 清空待阅数据
	 */
	private void removeAllRead(){
		String userId = SettingUtils.get(mContext,"userId");
		TreeVo vo = null;
		List<TreeVo> list = new ArrayList<>();
		try {
			list = db.selector(TreeVo.class).where("userId","=",userId).and("read","=",false).findAll();
			if (list!=null&&list.size()>0){
				for (int i=0;i<list.size();i++){
					TreeVo treeVo = list.get(i);
					treeVo.setRead(true);
					db.saveOrUpdate(treeVo);
				}
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
	}

}
