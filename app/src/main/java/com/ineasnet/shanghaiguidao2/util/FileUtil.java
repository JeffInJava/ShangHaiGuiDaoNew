package com.ineasnet.shanghaiguidao2.util;

import java.io.File;
import java.io.IOException;

/**
 * 文件帮助类
 * Created by Administrator on 2017/5/17.
 */

public class FileUtil {

    /** 创建子类文件夹 **/
    public static void createFile(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
    }

    /** 创建父类文件夹 **/
    public static void createParentFile(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /** 创建文件 **/
    public static void createNewFile(String path){
        File file = new File(path);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
