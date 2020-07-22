package com.ineasnet.shanghaiguidao2.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.ineasnet.shanghaiguidao2.entity.vo.DownLoadListVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidPT.ptUtil.DownLoadTask;
import androidPT.ptUtil.ZipTool;
import androidPT.ptUtil.download.Downloader;
import androidPT.ptUtil.download.FileState;
import androidPT.ptUtil.download.LoadInfo;
import androidPT.ptUtil.internet.HttpInstance;
import androidPT.ptUtil.internet.HttpResultCallBack;

public class DownloadService extends Service{
	private String url;
	private Context mContext;
	private String path;
	private String sessionId;
	private String fileName;
	private String name;
	private int position;
	private TreeVo mVo;
	private String userId;
	private List<String> downloadList = new ArrayList<String>();
	private static String TAG = "downLoadService";
	private List<MyThread> listTread = new ArrayList<MyThread>();
	private List<MyThread> listTread_wait = new ArrayList<MyThread>();
	
	
	public static Map<String,Downloader> downloaders=new HashMap<String, Downloader>();;
	//存放每个下载文件完成的长度
	private Map<String,Integer> completeSizes=new HashMap<String, Integer>();
	//存放每个下载文件的总长度
	private Map<String,Integer> fileSizes=new HashMap<String, Integer>();
	private Downloader downloader;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String name = String.valueOf(msg.obj);
				int progress1 = msg.arg1;
				
				Intent intent = new Intent();
				if(downloadList.contains(name)){
					if(progress1==100){
						downloadList.remove(name);
						intent.setAction(HttpInstance.ACTION_DELETEDOWNLOAD);
						intent.putExtra("name", name);
						intent.putExtra("progress", progress1);
					}else{
						intent.setAction(HttpInstance.ACTION_UPDATEDOWNLOAD);
						intent.putExtra("name", name);
						intent.putExtra("progress", progress1);
					}
				}else{
					downloadList.add(name);
					intent.setAction(HttpInstance.ACTION_ADDDOWNLOAD);
					intent.putExtra("name", name);
					intent.putExtra("progress", progress1);
				}
				
				sendBroadcast(intent);
				break;

			default:
				break;
			}
			
		};
	};
	
	private HttpResultCallBack download_cb = new HttpResultCallBack() {

		@Override
		public void onResultCallBack(Object... objs) {
			// TODO Auto-generated method stub
			DownLoadListVo vo = (DownLoadListVo) objs[0];
			String name = vo.getFileName();
			int progress = (int) objs[1];
			String filePath = String.valueOf(objs[2]);
			vo.setProgress(progress);
//			Log.e(TAG, "name:"+name);
//			Log.e(TAG, "progress:"+progress);
			
			Intent intent = new Intent();
			if(downloadList.contains(name)){
				if(progress==100){
					downloadList.remove(name);
//					intent.setAction(HttpInstance.ACTION_DELETEDOWNLOAD);
					intent.setAction(HttpInstance.ACTION_DOWNLOADFINISH);
					intent.putExtra("downloadvo", vo);
				}else{
					intent.setAction(HttpInstance.ACTION_UPDATEDOWNLOAD);
					intent.putExtra("downloadvo", vo);
				}
			}else{
				downloadList.add(name);
				intent.setAction(HttpInstance.ACTION_ADDDOWNLOAD);
			}
			intent.putExtra("name", name);
			intent.putExtra("progress", progress);
			intent.putExtra("filePath", filePath);
			
			sendBroadcast(intent);
			
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		path = bundle.getString("path");
		sessionId = bundle.getString("sessionId");
		url = bundle.getString("url");
		fileName = bundle.getString("fileName");
		name = bundle.getString("name");
		mVo = (TreeVo) bundle.getSerializable("vo");
		userId = bundle.getString("userId");
//		DownLoadTask downLoadUtil = new DownLoadTask(mContext, path, sessionId,position,download_cb,mHandler);
//		downLoadUtil.execute(url);
		
//		startDownload(fileName, url);
		
		int index = url.lastIndexOf("/") + 1;
		fileName = url.substring(index);
		
		SharedPreferences sp = getSharedPreferences("download", MODE_PRIVATE);
		String str_list = sp.getString("downloadlist", "");
		DownLoadListVo vo = new DownLoadListVo();
		List<DownLoadListVo> list = null;
		boolean isExist = false;
		try {
			if(TextUtils.isEmpty(str_list)){
				list = new ArrayList<DownLoadListVo>();
			}else{
				list = JSON.parseArray(str_list,DownLoadListVo.class);
				
				for(int i = 0 ; i < list.size() ; i++){
					DownLoadListVo vo2 = list.get(i);
					if(vo2.getFileName().equals(name)){
						vo.setFileName(name);
						vo.setFilePath(path+fileName);
						vo.setVo(mVo);
						vo.setUserId(userId);
						list.set(i, vo);
						isExist = true;
					}
				}
			}
			
			
			vo.setFileName(name);
			vo.setFilePath(path+fileName);
			vo.setVo(mVo);
			vo.setUserId(userId);
			
			if(!isExist){
				list.add(vo);
			}
			
			Editor editor = sp.edit();
			editor.putString("downloadlist", JSON.toJSONString(list));
			editor.commit();
			editor.clear();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		MyThread myThread = new MyThread(url,vo,download_cb);
		myThread.start();
		
		super.onStart(intent, startId);
	}
	
	class MyThread extends Thread{
		private String url;
		private DownLoadListVo mVo;
		private HttpResultCallBack mCb;
		public MyThread(String url,DownLoadListVo vo,HttpResultCallBack cb) {
			// TODO Auto-generated constructor stub
			this.url = url;
			this.mVo = vo;
			this.mCb = cb;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			download(url);
		}
		private void download(String url) {
			try {
				HttpClient client = new DefaultHttpClient();
				// sessionId = MainActivity.sessionId;
				HttpGet get = new HttpGet(url + "?_sessionid=" + sessionId);
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					long total = entity.getContentLength();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					
					File file = new File(path);
					if (!file.isDirectory()) {
						Log.e("file directory", "1111111");
						file.mkdirs();
					}
					
					int index = url.lastIndexOf("/") + 1;
					fileName = url.substring(index);
					
					FileOutputStream fos = new FileOutputStream(path
							+ fileName);
					
					byte[] buf = new byte[1024];
					int count = 0;
					int length = -1;
					Message msg = new Message();
					while ((length = is.read(buf, 0, 1024)) != -1) {
						// baos.write(buf, 0, length);
						fos.write(buf, 0, length);
						count += length;
						// 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
						int progress = (int) ((count / (float) total) * 100);
						mCb.onResultCallBack(mVo,progress,path+fileName);
//						msg.what = 1;
//						msg.obj = name;
//						msg.arg1 = progress;
//						
//						mHandler.sendMessage(msg);
//					publishProgress((int) ((count / (float) total) * 100));
						// // 为了演示进度,休眠500毫秒
						// Thread.sleep(500);
					}
					
					fos.close();
					is.close();
					
					
				}else{
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	public void startDownload(String fileName,String downPath)
	{
		//先从数据库中判断，这个文件是否已经在下载列表了
		int threadCount=10;//定义线程数
		downloader = downloaders.get(downPath);
		if(downloader==null)
		{
			downloader = new Downloader(downPath,path,fileName,sessionId,threadCount,this,mHandler);
			downloaders.put(downPath, downloader);//创建完一个新的下载器,必须把它加入到下载器集合里去
		}
		if (downloader.isdownloading())//如果正在下载则什么也不做
           return;
		//LoadInfo是一个实体类,里面封装了一些下载所需要的信息,每个loadinfo对应1个下载器
        LoadInfo loadInfo =downloader.getDownloaderInfors();
		FileState fileState = new FileState(fileName,downPath,loadInfo.getComplete(),loadInfo.getFileSize(),1);
//		dao.insertFileState(fileState,this);//在localdown_info表中插入一条下载数据
		completeSizes.put(downPath, loadInfo.getComplete());
		fileSizes.put(downPath, fileState.getFileSize());
		 // 调用方法开始下载
        downloader.download();
	}

}
