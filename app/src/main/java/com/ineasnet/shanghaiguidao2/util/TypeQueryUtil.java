package com.ineasnet.shanghaiguidao2.util;

import android.content.Context;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 类别查询
 * Created by Administrator on 2017/6/30.
 */

public class TypeQueryUtil {

    /**
     * 类别搜索
     * @param db
     * @param parendId 父类id
     * @return
     */
    public static List<TypeVo> QueryByType(Context context, DbManager db, String parendId){
        List<TypeVo> list = new ArrayList<TypeVo>();
        try {
            if (parendId == null||parendId.equals("")){
                list = db.selector(TypeVo.class).findAll();
//                list = sort(list);
            }else{
                list = db.selector(TypeVo.class).where("parentId","=",parendId).orderBy("tableId").findAll();
//                list = sort(list);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 类别搜索
     * @param db
     * @param parendId 父类id
     * @return
     */
    public static List<TypeVo> QueryByTypeFirst(Context context, DbManager db, String parendId){
        List<TypeVo> list = new ArrayList<TypeVo>();
        try {
            if (parendId == null||parendId.equals("")){
                list = db.selector(TypeVo.class).orderBy("id").findAll();
            }else{
                list = db.selector(TypeVo.class).where("parentId","=",parendId).orderBy("id",true).findAll();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 类别搜索
     * @param db
     * @param id id
     * @return
     */
    public static TypeVo QueryByTypeID(Context context, DbManager db, String id){
        List<TypeVo> list = new ArrayList<TypeVo>();
        TypeVo result = new TypeVo();
        try {
            if (id == null||id.equals("")){
                result = db.selector(TypeVo.class).findFirst();
            }else{
                result = db.selector(TypeVo.class).where("id","=",id).findFirst();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 类别搜索
     * @param db
     * @param name 类别名称
     * @return
     */
    public static TypeVo QueryByTypeVo(Context context, DbManager db, String name){
        TypeVo list = new TypeVo();
        try {
                list = db.selector(TypeVo.class).where("name","=",name).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 排序
     * @param list
     * @return
     */
    private static List<TypeVo> sort(List<TypeVo> list){
        List<TypeVo> oneList = new ArrayList<>();
        List<TypeVo> twoList = new ArrayList<>();
        List<TypeVo> otherList = new ArrayList<>();
        if (list!=null&&list.size()>0){
            //去掉2位数
            for (int i=0;i<list.size();i++){
                boolean flag = isNumeric(list.get(i).getName().substring(0,2));
                if (flag){
                    twoList.add(list.get(i));
                }
            }
            for (int i=0;i<twoList.size();i++){
                if (list.contains(twoList.get(i))){
                    list.remove(twoList.get(i));
                }
            }
            //去掉1位数
            for (int i=0;i<list.size();i++){
                boolean flag = isNumeric(list.get(i).getName().substring(0,1));
                if (flag){
                    oneList.add(list.get(i));
                }
            }
            for (int i=0;i<oneList.size();i++){
                if (list.contains(oneList.get(i))){
                    list.remove(oneList.get(i));
                }
            }
            otherList.addAll(list);
            list.clear();
            if (otherList!=null&&otherList.size()>0){
                list.addAll(otherList);
            }
            if (oneList!=null&&oneList.size()>0){
                list.addAll(sortUp(oneList));
            }

            if (twoList.size()>0){
                list.addAll(sortUp(twoList));
            }
            return list;
        }else{
            return null;
        }
    }


    private static List<TypeVo> sortUp(List<TypeVo> list){
        if (list!=null&&list.size()>0){
            List<TypeVo> testList = new ArrayList<>();
            int[] array = new int[list.size()];// 定义一个数组用来存放数字
            Map<Integer,Integer> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                array[i] = getNum(list.get(i).getName());// 遍历集合，调用方法获得里面的数字并存入数组
                map.put(getNum(list.get(i).getName()),i);
            }
            Arrays.sort(array);// 升序排序
            for (int i=0;i<array.length;i++){
                int position = map.get(array[i]);
                testList.add(list.get(position));
            }
            list.clear();
            list.addAll(testList);
            return list;
        }

        return list;
    }


    /**
     * 判断是否是数字
     * @param str
     * @return
     */
    private static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
//        Pattern pattern = Pattern.compile("[0-9]*");
//        return pattern.matcher(str).matches();
    }


    public static int getNum(String s) {// 获取字符串中的数字
        String num = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {// 遍历判断是否为数字
                num += s.charAt(i);
            }
        }
        return Integer.parseInt(num);
    }
}
