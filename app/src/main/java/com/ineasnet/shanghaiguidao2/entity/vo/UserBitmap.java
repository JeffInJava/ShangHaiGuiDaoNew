package com.ineasnet.shanghaiguidao2.entity.vo;

import android.graphics.Bitmap;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * 用户签名
 * Created by Administrator on 2017/6/14.
 */
@Table(name = "hispic")
public class UserBitmap implements Serializable{
    @Column(name = "id",isId = true)
    private String id;
    //用户文件
    @Column(name = "name")
    private String name;
    //id
    @Column(name = "userId")
    private String userId;
    //签名图片
    private Bitmap bitmap = null;
    //图片地址
    @Column(name = "filaPath")
    private String filaPath;
    @Column(name = "createTime")
    private String createTime;
    //文件编号
    @Column(name = "tblStudyDataId")
    private String tblStudyDataId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFilaPath() {
        return filaPath;
    }

    public void setFilaPath(String filaPath) {
        this.filaPath = filaPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTblStudyDataId() {
        return tblStudyDataId;
    }

    public void setTblStudyDataId(String tblStudyDataId) {
        this.tblStudyDataId = tblStudyDataId;
    }
}
