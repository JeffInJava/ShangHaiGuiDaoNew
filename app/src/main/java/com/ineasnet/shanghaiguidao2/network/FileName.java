package com.ineasnet.shanghaiguidao2.network;

import android.os.Environment;

/**
 * 存储文件名
 * Created by zhangp01 on 2016/4/13.
 */
public class FileName {
    public static final String BASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"";//根目录
    /** 次级目录 **/
    public static final String FILEPATH = BASEPATH
            + "/shangHaiGuidaoNew/";
    /** PDF文件目录 **/
    public static final String FILE_PDF = FILEPATH
            + "/pdf/";
    /** 专家PDF文件目录 **/
    public static final String FILE_PDF_PRO = FILEPATH
            + "/pdfpro/";

    /* 头像文件 */
    public static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
}
