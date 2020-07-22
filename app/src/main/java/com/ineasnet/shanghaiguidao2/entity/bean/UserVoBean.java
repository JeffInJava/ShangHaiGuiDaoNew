package com.ineasnet.shanghaiguidao2.entity.bean;

import com.ineasnet.shanghaiguidao2.entity.vo.UserVo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class UserVoBean implements Serializable{
    private List<UserVo> root;

    public List<UserVo> getRoot() {
        return root;
    }

    public void setRoot(List<UserVo> root) {
        this.root = root;
    }
}
