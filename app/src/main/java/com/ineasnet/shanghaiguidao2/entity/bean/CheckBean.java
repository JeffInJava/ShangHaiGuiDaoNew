package com.ineasnet.shanghaiguidao2.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/30.
 */

public class CheckBean implements Serializable{
    private String name;
    private boolean isCheck = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
