package com.ineasnet.shanghaiguidao2.adapters;

import java.util.List;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.MyListAdapter.ViewHolder;
import com.ineasnet.shanghaiguidao2.entity.vo.DownLoadListVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyDownloadListAdapter extends BaseAdapter{
	private List<DownLoadListVo> mList;
	private LayoutInflater mInflater;
	
	public MyDownloadListAdapter(Context context , List<DownLoadListVo> list) {
		// TODO Auto-generated constructor stub
		this.mList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0)==null?"":mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mylistview_item, null);

			holder.iv = (ImageView) convertView
					.findViewById(R.id.list_item_img_arr);
			holder.tv = (TextView) convertView.findViewById(R.id.list_item_tv);
			holder.cb = (CheckBox) convertView
					.findViewById(R.id.list_item_checkbox);
			holder.bar = (ProgressBar) convertView
					.findViewById(R.id.list_item_progress);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv.setText(mList.get(arg0).getFileName());
		int progress = mList.get(arg0).getProgress() == 0 ? 0 : mList.get(arg0).getProgress();
		if(progress!=0){
			if(progress==100){
				holder.bar.setVisibility(View.GONE);
			}else{
				holder.bar.setVisibility(View.VISIBLE);
				holder.bar.setProgress(progress);
			}
		}
		
		return convertView;
	}
	
	
	class ViewHolder {
		TextView tv;
		ImageView iv;
		CheckBox cb;
		ProgressBar bar;

	}
	
	public void update(DownLoadListVo vo){
		for(int i = 0 ; i < mList.size() ; i++){
			DownLoadListVo curVo = mList.get(i);
			if(vo.getFileName().equals(curVo.getFileName())){
				curVo.setProgress(vo.getProgress());
				mList.set(i, curVo);
				break;
			}
		}
		notifyDataSetChanged();
	}
	
	public void add(String name){
//		mList.add(name);
//		notifyDataSetChanged();
	}
	
	public void delete(String name){
		mList.remove(name);
		notifyDataSetChanged();
	}

}
