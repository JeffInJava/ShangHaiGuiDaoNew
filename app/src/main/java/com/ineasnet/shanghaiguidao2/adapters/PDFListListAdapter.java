package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class PDFListListAdapter extends BaseAdapter{
    private Context mContext;
    private List<TreeVo> list;
    private ViewHolder vh;
    public PDFListListAdapter(Context context, List<TreeVo> list){
        this.mContext = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pdftitle,null);
            vh.tv_pdftitle = (TextView) convertView.findViewById(R.id.tv_pdftitle);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_pdftitle.setText(list.get(position).getName());
        return convertView;
    }

    private class ViewHolder{
        //文件名
        TextView tv_pdftitle;
    }
}
