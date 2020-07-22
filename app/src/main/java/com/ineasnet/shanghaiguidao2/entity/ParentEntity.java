package com.ineasnet.shanghaiguidao2.entity;

import java.util.ArrayList;

public class ParentEntity {

	private int groupColor;

	private String groupName;

	private ArrayList<ChildEntity> childs;

	public int getGroupColor() {
		return groupColor;
	}

	public String getGroupName() {
		return groupName;
	}

	public ArrayList<ChildEntity> getChilds() {
		return childs;
	}

	public void setGroupColor(int groupColor) {
		this.groupColor = groupColor;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setChilds(ArrayList<ChildEntity> childs) {
		this.childs = childs;
	}

}
