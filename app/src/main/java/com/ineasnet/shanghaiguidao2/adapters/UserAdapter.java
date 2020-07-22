package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.vo.DepartmentVo;

import java.util.ArrayList;
import java.util.List;

import androidPT.ptWidget.spinner.AbstractSpinerAdapter;

/**
 * Created by Administrator on 2017/5/23.
 */

public class UserAdapter extends BaseAdapter{
    private Context mContext;
    private List<DepartmentVo> mObjects = new ArrayList<DepartmentVo>();
    private List<User> list = new ArrayList<User>();
    private int mSelectItem = 0;

    private LayoutInflater mInflater;

    public  UserAdapter(Context context, List<User> list){
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
    }
    public static interface IOnItemSelectListener{
        public void onItemClick(int pos);
    };


    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        AbstractSpinerAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spiner_item_layout, null);
            viewHolder = new AbstractSpinerAdapter.ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AbstractSpinerAdapter.ViewHolder) convertView.getTag();
        }


        User item =  list.get(pos);
        viewHolder.mTextView.setText(item.getName());

        return convertView;
    }

    public static class ViewHolder
    {
        public TextView mTextView;
    }

}
