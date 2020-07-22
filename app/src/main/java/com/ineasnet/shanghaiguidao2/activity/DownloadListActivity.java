package com.ineasnet.shanghaiguidao2.activity;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.R.id;
import com.ineasnet.shanghaiguidao2.R.layout;
import com.ineasnet.shanghaiguidao2.adapters.MyDownloadListAdapter;
import com.ineasnet.shanghaiguidao2.entity.vo.DownLoadListVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import android.app.ActionBar.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidPT.ptUtil.base.MyBaseActivity;
import androidPT.ptUtil.internet.HttpInstance;

public class DownloadListActivity extends MyBaseActivity {
	private ListView listView;
	private List<DownLoadListVo> listName = new ArrayList<DownLoadListVo>();
	private MyDownloadListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_list);
		tv_head = (TextView)findViewById(R.id.tv_title_content);
		tv_head.setText("下载列表");
		SharedPreferences sp = getSharedPreferences("download", MODE_PRIVATE);
		String str_list = sp.getString("downloadlist", "");
		if(!TextUtils.isEmpty(str_list)){
			try {
				listName = JSON.parseArray(str_list, DownLoadListVo.class);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		listView = (ListView)findViewById(R.id.list_download);
		listView.setOnItemClickListener(itemClickListener);
		adapter = new MyDownloadListAdapter(DownloadListActivity.this, listName);
		listView.setAdapter(adapter);
		
//		TextView tv = new TextView(this);
//		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//		tv.setText("没有正在下载的文件");
//		listView.setEmptyView(tv);
	}
	@SuppressWarnings("resource")
	private static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}
	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			DownLoadListVo vo =(DownLoadListVo)adapter.getItem(arg2);
			TreeVo treeVo = vo.getVo();
			
			File file = new File(vo.getFilePath());
			if (file.exists()) {
				long size = 0;
				try {
					size = getFileSize(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (vo.getVo().getSize() == size) {
					Intent intent = new Intent(DownloadListActivity.this, PDFNewActivity.class);
					intent.putExtra("filePath", vo.getFilePath());
					intent.putExtra("tblStudyDataId", treeVo.getTblStudyDataId());
					intent.putExtra("userId", vo.getUserId());
					intent.putExtra("isFromIndex", false);
					intent.putExtra("page", 0);
					startActivity(intent);
				}else{
					Toast.makeText(DownloadListActivity.this, "请等待下载完成", Toast.LENGTH_SHORT)
					.show();
				}
			}
			
		}
	};
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(HttpInstance.ACTION_ADDDOWNLOAD);
		filter.addAction(HttpInstance.ACTION_DELETEDOWNLOAD);
		filter.addAction(HttpInstance.ACTION_UPDATEDOWNLOAD);
		filter.addAction(HttpInstance.ACTION_DOWNLOADFINISH);
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unregisterReceiver(mReceiver);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String name = intent.getStringExtra("name");
			Log.e("download", "name:"+name);
			if(intent.getAction().equals(HttpInstance.ACTION_ADDDOWNLOAD)){
//				listName.add(name);
//				adapter.add(name);
//				adapter.notifyDataSetChanged();
			}
			if(intent.getAction().equals(HttpInstance.ACTION_DELETEDOWNLOAD)){
//				adapter.delete(name);
				Log.e("finish", "111111111111111111");
			}
			if(intent.getAction().equals(HttpInstance.ACTION_UPDATEDOWNLOAD)){
				Log.e("update", "111111111111111111");
				DownLoadListVo vo = (DownLoadListVo) intent.getSerializableExtra("downloadvo");
				adapter.update(vo);
			}
			
			if(intent.getAction().equals(HttpInstance.ACTION_DOWNLOADFINISH)){
//				adapter.delete(name);
				Log.e("finish", "111111111111111111");
				DownLoadListVo vo = (DownLoadListVo) intent.getSerializableExtra("downloadvo");
				adapter.update(vo);
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void handleHttpResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

}
