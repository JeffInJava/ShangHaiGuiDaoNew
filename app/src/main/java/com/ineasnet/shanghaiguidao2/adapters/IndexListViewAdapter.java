package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;
import com.ineasnet.shanghaiguidao2.util.OnGridItemClickListener;
import com.ineasnet.shanghaiguidao2.util.TypeQueryUtil;
import com.ineasnet.shanghaiguidao2.view.MyGridView;

import org.xutils.DbManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class IndexListViewAdapter extends BaseAdapter{
    private Context mContext;
    private List<TypeVo> list;
    private DbManager db;
    private IndexListViewHolder vh;
    protected OnGridItemClickListener onGridItemClickListener = null;
    private MyGridAdapter adapter;
    public IndexListViewAdapter(Context context, DbManager db, List<TypeVo> list){
        this.mContext = context;
        this.db = db;
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
            vh = new IndexListViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_index_new,null);
            vh.tv_index_title = (TextView) convertView.findViewById(R.id.tv_index_title);
            vh.gv_index = (MyGridView) convertView.findViewById(R.id.gv_index);
            convertView.setTag(vh);
        }else{
            vh = (IndexListViewHolder) convertView.getTag();
        }
        TypeVo typeVo = list.get(position);
        vh.tv_index_title.setText(typeVo.getName());
        final List<TypeVo> secondList = TypeQueryUtil.QueryByType(mContext,db,typeVo.getId());
        adapter = new MyGridAdapter(mContext, secondList);
        vh.gv_index.setAdapter(adapter);
        vh.gv_index.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                onGridItemClickListener.getView(parent,view,position,secondList.get(position));
            }
        });
        return convertView;
    }

    private class IndexListViewHolder{
        //标题
        private TextView tv_index_title;
        //grid
        private MyGridView gv_index;
    }

    public void setOnGridItemClickListener(OnGridItemClickListener onGridItemClickListener){
        this.onGridItemClickListener = onGridItemClickListener;
    }
}
