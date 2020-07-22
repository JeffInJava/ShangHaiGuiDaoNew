package com.ineasnet.shanghaiguidao2.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ineasnet.shanghaiguidao2.R;
import com.ineasnet.shanghaiguidao2.adapters.UserAdapter;
import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.vo.DepartmentVo;

import java.util.ArrayList;
import java.util.List;

import androidPT.ptWidget.spinner.AbstractSpinerAdapter;

/**
 * Created by Administrator on 2017/5/23.
 */

public class UserPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {
    private Context mContext;
    private ListView mListView;
    private UserAdapter mAdapter;
    private UserAdapter.IOnItemSelectListener mItemSelectListener;


    public UserPopupWindow(Context context)
    {
        super(context);
        mContext = context;
        init();
    }
    public void setItemListener(UserAdapter.IOnItemSelectListener listener){
        mItemSelectListener = listener;
    }


    public void setAdatper(UserAdapter adapter){
        mAdapter = adapter;
        mListView.setAdapter(mAdapter);
    }
    private void init()
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);


        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null){
            mItemSelectListener.onItemClick(pos);
        }
    }
}
