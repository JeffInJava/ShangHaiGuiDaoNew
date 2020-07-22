package com.ineasnet.shanghaiguidao2.adapters;

import java.util.List;

import com.bumptech.glide.Glide;
import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;
import com.ineasnet.shanghaiguidao2.network.Urls;
import com.ineasnet.shanghaiguidao2.util.ROnItemClickListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<TypeVo> headList;
	private String name;

	public MyGridAdapter(Context context, List<TypeVo> headList) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.headList = headList;
		this.mInflater = LayoutInflater.from(context);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (headList!=null&&headList.size()>0){
			return headList.size();
		}else{
			return 0;
		}

	}

	public void notifyDataSetChanged (List<TypeVo> list){
		if (list!=null&&list.size()>0){
			headList.clear();
			headList.addAll(list);
			notifyDataSetChanged();
		}

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return headList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.mygridview_item, null);

			holder.iv = (ImageView) convertView
					.findViewById(R.id.grid_item_img);
			holder.tv = (TextView) convertView.findViewById(R.id.grid_item_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TypeVo current_tree = headList.get(position);

		name = current_tree.getName();
		String imageURL = Urls.BASEIMAGE+current_tree.getCategoryImg();
		if (current_tree.getCategoryImg()!=null&&!current_tree.getCategoryImg().equals("")){
			Glide.with(mContext).load(imageURL).error(R.drawable.folder).into(holder.iv);
		}else{
			holder.iv.setBackgroundResource(R.drawable.folder);
		}
		
		holder.tv.setText(name);

		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv;
	}

}
