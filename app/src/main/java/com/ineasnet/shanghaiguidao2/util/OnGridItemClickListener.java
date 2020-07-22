package com.ineasnet.shanghaiguidao2.util;

import android.view.View;
import android.view.ViewGroup;

import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;

/**
 * Created by Administrator on 2017/7/28.
 */

public interface OnGridItemClickListener {
    public void getView(ViewGroup viewGroup, View view, int position, TypeVo typeVo);
}
