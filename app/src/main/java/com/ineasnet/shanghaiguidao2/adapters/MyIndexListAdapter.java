package com.ineasnet.shanghaiguidao2.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.MyListAdapter.ViewHolder;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyIndexListAdapter extends BaseAdapter{
	
	private Map<String, Object> map;
	private List<List<String>> pageContents;
	private List<List<Integer>> pages;
	private LayoutInflater mInflater;
	private Map<String, Object> map_result;
	private TreeVo mVo;
	
	public MyIndexListAdapter(Context context,Map<String, Object> map,TreeVo vo) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.map = map;
		pageContents = (List<List<String>>) map.get("pageContents");
		pages = (List<List<Integer>>) map.get("pages");
		this.mVo = vo;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageContents.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		map_result = new HashMap<String, Object>();
		map_result.put("vo", mVo);
		map_result.put("page", pages.get(position).get(0));
		return map_result;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mylistview_item, null);

			holder.iv = (ImageView) convertView
					.findViewById(R.id.list_item_img_arr);
			holder.tv = (TextView) convertView.findViewById(R.id.list_item_tv);
			holder.cb = (CheckBox)convertView.findViewById(R.id.list_item_checkbox);
			holder.bar = (ProgressBar)convertView.findViewById(R.id.list_item_progress);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String content = pageContents.get(position).get(0);
//		content = subStringContent(content);
		content = content.replaceAll("\r", "");
		content = content.replaceAll("\n", "");
		holder.tv.setText(content);
//		String name = vo.getName();
//		holder.tv.setText(name);
		
		return convertView;
	}
	class ViewHolder{
		TextView tv;
		ImageView iv;
		CheckBox cb;
		ProgressBar bar;
	}
}
