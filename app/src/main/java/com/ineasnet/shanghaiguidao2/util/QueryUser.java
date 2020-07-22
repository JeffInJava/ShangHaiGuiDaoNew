package com.ineasnet.shanghaiguidao2.util;

import android.content.Context;

import com.ineasnet.shanghaiguidao2.entity.bean.User;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.UserHistoryVo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户查询工具类
 * Created by Administrator on 2017/7/10.
 */

public class QueryUser {

    /**
     * 通过用户名查询
     * @param db
     * @param searchEdittext 搜索内容
     * @return
     */
    public static List<User> QueryUserName(Context context, DbManager db, String searchEdittext){
        String userId = SettingUtils.get(context,"userId");
        List<User> list = new ArrayList<>();
        try {
                String searchResult = "%"+searchEdittext+"%";
                list = db.selector(User.class).where("name","like",searchResult).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 通过id查询
     * @param db
     * @param userId 搜索内容
     * @return
     */
    public static User QueryUser(Context context, DbManager db, String userId){
        User user = null;
        try {
//                String searchResult = "%"+searchEdittext+"%";
            user = db.selector(User.class).where("code","=",userId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return user;
    }


    /**
     * 通过id查询
     * @param db
     * @param id 搜索内容
     * @return
     */
    public static UserHistoryVo QueryUserHistory(Context context, DbManager db, String id){
        UserHistoryVo user = null;
        try {
            user = db.selector(UserHistoryVo.class).where("id","=",id).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return user;
    }
}
