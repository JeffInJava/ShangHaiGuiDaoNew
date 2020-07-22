package androidPT.ptWidget.spinner;

import java.util.ArrayList;
import java.util.List;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.vo.DepartmentVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AbstractSpinerAdapter extends BaseAdapter {

	public static interface IOnItemSelectListener{
		public void onItemClick(int pos);
	};
	
	 private Context mContext;   
	 private List<DepartmentVo> mObjects = new ArrayList<DepartmentVo>();
	 private int mSelectItem = 0;
	    
	 private LayoutInflater mInflater;
	
	 public  AbstractSpinerAdapter(Context context){
		 init(context);
	 }
	 
	 public void refreshData(List<DepartmentVo> objects, int selIndex){
		 mObjects = objects;
		 if (selIndex < 0){
			 selIndex = 0;
		 }
		 if (selIndex >= mObjects.size()){
			 selIndex = mObjects.size() - 1;
		 }
		 
		 mSelectItem = selIndex;
	 }
	 
	 private void init(Context context) {
	        mContext = context;
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }
	    
	    
	@Override
	public int getCount() {

		return mObjects.size();
	}

	@Override
	public Object getItem(int pos) {
		return mObjects.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		 ViewHolder viewHolder;
    	 
	     if (convertView == null) {
	    	 convertView = mInflater.inflate(R.layout.spiner_item_layout, null);
	         viewHolder = new ViewHolder();
	         viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
	         convertView.setTag(viewHolder);
	     } else {
	         viewHolder = (ViewHolder) convertView.getTag();
	     }

	     
	     DepartmentVo item =  mObjects.get(pos);
		 viewHolder.mTextView.setText(item.getDepartmentName());

	     return convertView;
	}

	public static class ViewHolder
	{
	    public TextView mTextView;
    }


}
