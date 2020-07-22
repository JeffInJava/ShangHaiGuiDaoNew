package com.ineasnet.shanghaiguidao2.entity.vo;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/20.
 */
@Table(name = "usersHistory")
public class UserHistoryVo implements Serializable{
    @Column(name = "id",isId = true)
    private int id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "isCheck")
    private boolean isCheck = false;
    @Column(name = "userId")
    private String userId;
    @Column(name = "tblStudyDataId")
    private String tblStudyDataId;


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTblStudyDataId() {
        return tblStudyDataId;
    }

    public void setTblStudyDataId(String tblStudyDataId) {
        this.tblStudyDataId = tblStudyDataId;
    }
}
