package com.ineasnet.shanghaiguidao2.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 已选中的人员名单
 * Created by Administrator on 2017/6/15.
 */

public class UserListCheckedBean implements Serializable{
    private List<User> userList;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
