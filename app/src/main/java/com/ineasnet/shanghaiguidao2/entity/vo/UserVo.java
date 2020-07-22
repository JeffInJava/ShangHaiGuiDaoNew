package com.ineasnet.shanghaiguidao2.entity.vo;

import com.ineasnet.shanghaiguidao2.entity.bean.User;

import java.io.Serializable;
import java.util.List;

/**
 * 组员列表
 * Created by Administrator on 2017/5/24.
 */

public class UserVo implements Serializable{
    //部门信息
    private String deptCode;
    //部门名称
    private String deptName;
    //部门列表
    private List<User> psmJson;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<User> getPsmList() {
        return psmJson;
    }

    public void setPsmList(List<User> psmList) {
        this.psmJson = psmList;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "deptCode='" + deptCode + '\'' +
                ", deptName='" + deptName + '\'' +
                ", psmList=" + psmJson +
                '}';
    }
}
