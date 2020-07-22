package com.ineasnet.shanghaiguidao2.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ineasnet.shanghaiguidao2.R;

import androidPT.ptUtil.base.MyBaseFragment;
import androidPT.ptWidget.writepad.PaintView;

/**
 * Created by Administrator on 2017/6/14.
 */

public class BitmapFragment extends Fragment {
    private static PaintView pv_bitfg;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_bitmap,null);
        mContext = getActivity();
        // 获取屏幕尺寸
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;
//        pv_bitfg = new PaintView(mContext, screenWidth, screenHeight);
//        container.addView(pv_bitfg);
        pv_bitfg = (PaintView) view.findViewById(R.id.pv_bitfg);
//        pv_bitfg.setMath(screenWidth,screenHeight);
//        pv_bitfg.requestFocus();
        return view;
    }

    public static Bitmap getBitmap(){
        return pv_bitfg.getPaintBitmap();
    }

    public static void clear(){
        pv_bitfg.clear();
    }
}
