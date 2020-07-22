package com.ineasnet.shanghaiguidao2.util;

import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 数据操作工具类
 * Created by Administrator on 2017/7/7.
 */

public class ListUtil {

    /**
     * List去重
     * @param list
     * @return
     */
    public static List<TreeVo> removeSameList(List<TreeVo> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if  (list.get(j).getId() == list.get(i).getId())   {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    /**
     * List去除附件
     * @param list
     * @return
     */
    public static List<TreeVo> removeSecondList(List<TreeVo> list) {
        for (int i = 0; i < list.size(); i++) {
                if  (list.get(i).getName().contains("附件"))   {
                    list.remove(i);
                }
        }
        return list;
    }


    }
