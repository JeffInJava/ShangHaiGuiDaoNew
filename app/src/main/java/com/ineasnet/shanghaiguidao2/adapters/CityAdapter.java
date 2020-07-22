package com.ineasnet.shanghaiguidao2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.util.ROnItemClickListener;
import com.orhanobut.logger.Logger;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    protected Context mContext;
    protected List<User> mDatas;
    protected LayoutInflater mInflater;
    protected ROnItemClickListener rOnItemClickListener;

    public CityAdapter(Context mContext, List<User> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<User> getDatas() {
        return mDatas;
    }

    public CityAdapter setDatas(List<User> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_city, parent, false));
    }

    @Override
    public void onBindViewHolder(final CityAdapter.ViewHolder holder, final int position) {
        final User cityBean = mDatas.get(position);
        holder.tvCity.setText(cityBean.getName());
        holder.list_checkbox.setChecked(cityBean.isCheck());
        holder.list_checkbox.setFocusable(false);
        holder.list_checkbox.setClickable(false);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rOnItemClickListener.getView(v,position);
            }
        });
//        holder.avatar.setImageResource(R.drawable.friend);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        ImageView avatar;
        View content;
        CheckBox list_checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            content = itemView.findViewById(R.id.content);
            list_checkbox = (CheckBox) itemView.findViewById(R.id.list_checkbox);
        }
    }

    public void setrOnItemClickListener(ROnItemClickListener onItemClickListener){
        this.rOnItemClickListener = onItemClickListener;
    }
}
