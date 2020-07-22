package com.ineasnet.shanghaiguidao2.util;

import android.content.Context;

import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.vo.VersionTreeVo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/17.
 */

public class QueryHistoryVo {
    /**
     * 通过用户Id查询
     * @param db
     * @return
     */
    public static List<VersionTreeVo> QueryVersionAll(Context context, DbManager db){
        String userId = SettingUtils.get(context,"userId");
        List<VersionTreeVo> list = new ArrayList<>();
        try {
            list = db.selector(VersionTreeVo.class).where("userId","=",userId).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过用户Id查询
     * @param db
     * @param id 搜索内容
     * @return
     */
    public static VersionTreeVo QueryVersionById(Context context, DbManager db, String id){
        String userId = SettingUtils.get(context,"userId");
        VersionTreeVo versionTreeVo = null;
        try {
            versionTreeVo = db.selector(VersionTreeVo.class).where("id","=",id).and("userId","=",userId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return versionTreeVo;
    }
}
