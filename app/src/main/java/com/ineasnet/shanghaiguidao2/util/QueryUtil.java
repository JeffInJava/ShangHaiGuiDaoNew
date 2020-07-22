package com.ineasnet.shanghaiguidao2.util;

import android.content.Context;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索工具类
 * Created by Administrator on 2017/6/29.
 */

public class QueryUtil {
    /**
     * 文件名称搜索
     * @param db
     * @param searchEdittext 搜索内容
     * @param type 搜索结构
     * @return
     */
    public static List<TreeVo> QueryByPDFName(Context context,DbManager db,String searchEdittext,String type,int page){
        String userId = SettingUtils.get(context,"userId");
        List<TreeVo> list = new ArrayList<>();
        String typeResult = "%"+type+"%";
        int num = page*10;
        try {
            //是否有输入内容
            if (searchEdittext == null||searchEdittext.equals("")){
                //是否有类别内容
                if (type == null||type.equals("")){
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("parentId","=","null").orderBy("id").limit(num).findAll();
                }else{
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("folderName","like",typeResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }

            }else{
                String searchResult = "%"+searchEdittext+"%";
                //是否有类别内容
                if (type == null||type.equals("")){
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("name","like",searchResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }else{
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("folderName","like",typeResult).and("name","like",searchResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 文件名称全文搜索
     * @param db
     * @param searchEdittext 搜索内容
     * @return
     */
    public static List<TreeVo> QueryByPDFName(Context context, DbManager db, String searchEdittext,int page){
        String userId = SettingUtils.get(context,"userId");
        List<TreeVo> list = new ArrayList<>();
        int num = page*10;
        try {
            if (searchEdittext == null||searchEdittext.equals("")){
                list = db.selector(TreeVo.class).where("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
            }else{
                String searchResult = "%"+searchEdittext+"%";
                list = db.selector(TreeVo.class).where("name","like",searchResult).and("userId","=",userId).and("parentId","=","null").orderBy("id").limit(num).findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 文号称搜索
     * @param db
     * @param searchEdittext 搜索内容
     * @param type 搜索结构
     * @return
     */
    public static List<TreeVo> QueryByPDFDocNum(Context context,DbManager db,String searchEdittext,String type,int page){
        String userId = SettingUtils.get(context,"userId");
        List<TreeVo> list = new ArrayList<>();
        String typeResult = "%"+type+"%";
        int num = page*10;
        try {
            //是否有输入内容
            if (searchEdittext == null||searchEdittext.equals("")){
                //是否有类别内容
                if (type == null||type.equals("")){
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }else{
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("folderName","like",typeResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }

            }else{
                String searchResult = "%"+searchEdittext+"%";
                //是否有类别内容
                if (type == null||type.equals("")){
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("title","like",searchResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }else{
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("folderName","like",typeResult).and("docNum","like",searchResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 文号称全文搜索
     * @param db
     * @param searchEdittext 搜索内容
     * @return
     */
    public static List<TreeVo> QueryByPDFDocNum(Context context, DbManager db, String searchEdittext,int page){
        String userId = SettingUtils.get(context,"userId");
        List<TreeVo> list = new ArrayList<>();
        int num = page*10;
        try {
            if (searchEdittext == null||searchEdittext.equals("")){
                list = db.selector(TreeVo.class).where("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
            }else{
                String searchResult = "%"+searchEdittext+"%";
                list = db.selector(TreeVo.class).where("docNum","like",searchResult).and("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 企标号搜索
     * @param db
     * @param searchEdittext 搜索内容
     * @param type 搜索结构
     * @return
     */
    public static List<TreeVo> QueryByPDFRuleNum(Context context,DbManager db,String searchEdittext,String type,int page){
        String userId = SettingUtils.get(context,"userId");
        List<TreeVo> list = new ArrayList<>();
        String typeResult = "%"+type+"%";
        int num = page*10;
        try {
            //是否有输入内容
            if (searchEdittext == null||searchEdittext.equals("")){
                //是否有类别内容
                if (type == null||type.equals("")){
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }else{
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("folderName","like",typeResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }

            }else{
                String searchResult = "%"+searchEdittext+"%";
                //是否有类别内容
                if (type == null||type.equals("")){
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("title","like",searchResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }else{
                    list = db.selector(TreeVo.class).where("userId","=",userId).and("folderName","like",typeResult).and("ruleNum","like",searchResult).and("parentId","=","null").limit(num).orderBy("id").findAll();
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 企标号全文搜索
     * @param db
     * @param searchEdittext 搜索内容
     * @return
     */
    public static List<TreeVo> QueryByPDFRuleNum(Context context, DbManager db, String searchEdittext,int page){
        String userId = SettingUtils.get(context,"userId");
        List<TreeVo> list = new ArrayList<>();
        int num = page*10;
        try {
            if (searchEdittext == null||searchEdittext.equals("")){
                list = db.selector(TreeVo.class).where("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
            }else{
                String searchResult = "%"+searchEdittext+"%";
                list = db.selector(TreeVo.class).where("ruleNum","like",searchResult).and("userId","=",userId).and("parentId","=","null").limit(num).orderBy("id").findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 搜索附件
     * @param db
     * @param parentId 搜索内容
     * @return
     */
    public static TreeVo QueryByPDF(Context context, DbManager db, String parentId){
        String userId = SettingUtils.get(context,"userId");
        TreeVo vo = null;
        List<TreeVo> list = new ArrayList<>();
        try {
                vo = db.selector(TreeVo.class).where("parentId","=",parentId).and("userId","=",userId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return vo;
    }



    /**
     * 搜索附件
     * @param db
     * @param parentId 搜索内容
     * @return
     */
    public static List<TreeVo> QueryByPDFList(Context context, DbManager db, String parentId){
        String userId = SettingUtils.get(context,"userId");
        TreeVo vo = null;
        List<TreeVo> list = new ArrayList<>();
        try {
            list = db.selector(TreeVo.class).where("parentId","=",parentId).and("userId","=",userId).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过id判断是否有
     * @param db
     * @param id 搜索内容
     * @return
     */
    public static TreeVo QueryByID(Context context, DbManager db, String id){
        String userId = SettingUtils.get(context,"userId");
        TreeVo vo = null;
        List<TreeVo> list = new ArrayList<>();
        try {
            vo = db.selector(TreeVo.class).where("id","=",id).and("userId","=",userId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return vo;
    }


    /**
     * 分页查询
     * @param context
     * @param db
     * @param isRead 是否是已读
     * @param page 页数
     * @return
     */
    public static List<TreeVo> QueryByPage(Context context, DbManager db, boolean isRead, int page){
        List<TreeVo> result = new ArrayList<>();
        List<TreeVo> numList = new ArrayList<>();
        String userId = SettingUtils.get(context,"userId");
        int num = page*10;
        int start = (page-1)*10;
        try {
            if (isRead){
                result = db.selector(TreeVo.class).where("userId","=",userId).and("read","=",isRead).and("parentId","=","null").orderBy("studyTime",true).limit(num).findAll();
            }else{
                result = db.selector(TreeVo.class).where("userId","=",userId).and("read","=",isRead).and("parentId","=","null").orderBy("createTime",true).limit(num).findAll();
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

}
