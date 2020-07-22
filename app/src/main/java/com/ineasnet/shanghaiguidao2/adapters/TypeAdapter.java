package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.bean.CheckBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class TypeAdapter extends BaseAdapter{
    private Context mContext;
    private List<CheckBean> list = new ArrayList<>();
    private TypeViewHolder vh;

    public  TypeAdapter(Context context,List<CheckBean> list){
        this.list = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        if (list.size()>0){
            return list.size();
        }else{
            return 0;
        }

    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            vh = new TypeViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.spiner_item_layout,null);
            vh.textView = (TextView) view.findViewById(R.id.textView);
            view.setTag(vh);
        }else{
            vh = (TypeViewHolder) view.getTag();
        }
        CheckBean checkBean = list.get(i);
        if (checkBean.isCheck()){
            vh.textView.setTextColor(mContext.getResources().getColor(R.color.title_bg));
        }else{
            vh.textView.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        vh.textView.setText(checkBean.getName());
        return view;
    }

    private class TypeViewHolder{
        private TextView textView;
    }
}
