package com.ineasnet.shanghaiguidao2.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.MyGridAdapter.ViewHolder;
import com.ineasnet.shanghaiguidao2.dao.FileDao;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import androidPT.ptUtil.MyApplication;
import androidPT.ptUtil.PropertiesUtil;

public class MyListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<TreeVo> childList;
	private MyApplication application;
	private DbManager db;
	private String name;
	private String path;
	List<FileDao> fileDaoList = new ArrayList<>();

	public MyListAdapter(Context context, List<TreeVo> childList) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.childList = childList;
		this.mInflater = LayoutInflater.from(context);
		application = new MyApplication();
		db = x.getDb(application.getDaoConfig());
		try {
			fileDaoList=db.selector(FileDao.class).findAll();
		} catch (DbException e) {
			e.printStackTrace();
		}
		path = PropertiesUtil.getPropertiesURL(context, "filePath");
	}

	public void notifyDataSetChanged(List<TreeVo> list){
		childList.clear();
		if (list!=null&&list.size()>0){
			childList.addAll(list);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (childList.size()>0){
			return childList.size();
		}else{
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return childList.get(position);
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

		TreeVo vo = childList.get(position);
		String name  = "";
		if (vo.getTitle()!=null&&!vo.getTitle().equals("")){
			name = vo.getTitle();
		}else if (vo.getName()!=null&&!vo.getName().equals("")){
			name = vo.getName();
		}
		holder.tv.setText(name);

		String pdfName = vo.getPdfName() == null ? "" : vo.getPdfName();
		if(!TextUtils.isEmpty(pdfName)){
			File file = new File(path+"/ftpIndexAll/" + pdfName);
			boolean flag = false;
//			if (fileDaoList == null||fileDaoList.size()<=0){
//				flag = false;
//			}else{
//				for (int i=0;i<fileDaoList.size();i++){
//					if (pdfName.equals(fileDaoList.get(i).getPdfName())){
//						flag = true;
//					}
//				}
				if (file.exists()){
					flag = true;
				}else{
					flag = false;
				}

//			}

			if (!flag) {
				holder.tv.setTextColor(mContext.getResources().getColor(
						R.color.color_seperate));
			} else {
				holder.tv.setTextColor(mContext.getResources().getColor(
						android.R.color.black));
			}
		}else{
			holder.tv.setTextColor(mContext.getResources().getColor(
					R.color.color_seperate));
		}


		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv;
		CheckBox cb;
		ProgressBar bar;

	}

//	public void notifyDataSetChanged(){
//
//		try {
//			fileDaoList=db.selector(FileDao.class).findAll();
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
//		notifyDataSetChanged();
//	}
	
	public void setCheckBox(){
		
	}

}
