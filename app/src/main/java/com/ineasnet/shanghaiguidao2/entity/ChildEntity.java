package com.ineasnet.shanghaiguidao2.entity;

import java.util.ArrayList;

public class ChildEntity {

	private int groupColor;

	private String groupName;

	private ArrayList<String> childNames;

	public int getGroupColor() {
		return groupColor;
	}

	public String getGroupName() {
		return groupName;
	}

	public ArrayList<String> getChildNames() {
		return childNames;
	}

	public void setGroupColor(int groupColor) {
		this.groupColor = groupColor;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setChildNames(ArrayList<String> childNames) {
		this.childNames = childNames;
	}

}
