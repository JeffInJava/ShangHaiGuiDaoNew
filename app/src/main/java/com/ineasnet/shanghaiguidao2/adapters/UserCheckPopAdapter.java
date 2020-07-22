package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.bean.User;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */

public class UserCheckPopAdapter extends BaseAdapter{
    private Context mContext;
    private List<User> list;
    private UserCheckViewHolder vh;
    public UserCheckPopAdapter(Context context, List<User> list){
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
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null){
            vh = new UserCheckViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_user_check_pop,null);
            vh.list_item_tv = (TextView) view.findViewById(R.id.list_item_tv_pop);
            vh.list_item_checkbox = (CheckBox) view.findViewById(R.id.list_item_checkbox_pop);
            view.setTag(vh);
        }else{
            vh = (UserCheckViewHolder) view.getTag();
        }
        User user = list.get(position);
        vh.list_item_checkbox.setChecked(user.isCheck());
        vh.list_item_tv.setText(user.getName());

        return view;
    }

    public void notifyDataSetChanged(List<User> list){
        this.list = list;
        notifyDataSetChanged();
    }

    private class UserCheckViewHolder{
        private CheckBox list_item_checkbox;
        private TextView list_item_tv;
    }
}
