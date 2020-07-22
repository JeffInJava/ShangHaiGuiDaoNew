package com.ineasnet.shanghaiguidao2.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ineasnet.shanghaiguidao2.entity.vo.TreeVo;
import com.ineasnet.shanghaiguidao2.entity.vo.TypeVo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * json解析工具类
 * Created by Administrator on 2017/6/30.
 */

public class DataFactory {

    public static Object getInstanceByJson(Class<?> clazz, String json)
    {
        Object obj = null;
        Gson gson = new Gson();
        obj = gson.fromJson(json, clazz);
        return obj;
    }

    /**
     * @author I321533
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
    {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**
     * @param json
     * @return
     */
    public static List<TypeVo> jsonToArrayList(String json)
    {
        Type type = new TypeToken<ArrayList<TypeVo>>()
        {}.getType();
        List<TypeVo> list = new ArrayList<>();
        list = new Gson().fromJson(json, type);
        return list;
    }


    /**
     * @param json
     * @return
     */
    public static List<TreeVo> jsonToArrayPDFList(String json)
    {
        Type type = new TypeToken<ArrayList<TreeVo>>()
        {}.getType();
        List<TreeVo> list = new ArrayList<>();
        list = new Gson().fromJson(json, type);
        return list;
    }
}
