package com.ineasnet.shanghaiguidao2.entity.vo;

import java.io.Serializable;

/**
 * 已审批人员名单列表
 * Created by Administrator on 2017/5/24.
 */

public class CheckFileVo implements Serializable{

    /**
     * id : 4
     * status : 0
     * tblStudyDataId : 202
     * userId : 99999999
     * userName : superpro
     */

    private int id;
    private int status;
    private int tblStudyDataId;
    private String userId;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTblStudyDataId() {
        return tblStudyDataId;
    }

    public void setTblStudyDataId(int tblStudyDataId) {
        this.tblStudyDataId = tblStudyDataId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
