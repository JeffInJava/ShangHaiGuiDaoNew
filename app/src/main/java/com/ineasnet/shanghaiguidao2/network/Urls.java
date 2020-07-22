package com.ineasnet.shanghaiguidao2.network;

/**
 * Created by Administrator on 2017/5/24.
 */

public class Urls {
    /** Base **/
    public static String BASEURL = "http://192.168.3.214:8080/paperless/";//刘恒
    public static String BASEIMAGE = "http://10.20.1.50:8080";//刘恒
    /** 获取班组成员列表 **/
    public static String GET_CHECK_LIST = BASEURL+"admin/user/search_userlist_by_department.action";
    /** 分配学习资料 **/
    public static String UPDATE_CHECK_LIST = BASEURL+"create_user_study.action";
}
