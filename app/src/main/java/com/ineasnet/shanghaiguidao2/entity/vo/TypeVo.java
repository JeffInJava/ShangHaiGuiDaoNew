package com.ineasnet.shanghaiguidao2.entity.vo;

import android.support.annotation.NonNull;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 类别信息
 * Created by Administrator on 2017/6/30.
 */

@Table(name="type")
public class TypeVo implements Serializable{
    @Column(name = "tableId",isId = true)
    private int tableId;
    @Column(name = "id")
    private String id;
    @Column(name = "isExpert")
    private int isExpert;
    @Column(name = "name")
    private String name;
    @Column(name = "parentId")
    private String parentId;
    @Column(name = "categoryImg")
    private String categoryImg;


    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsExpert() {
        return isExpert;
    }

    public void setIsExpert(int isExpert) {
        this.isExpert = isExpert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

}
