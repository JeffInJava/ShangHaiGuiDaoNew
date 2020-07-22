package com.ineasnet.shanghaiguidao2.entity.bean;

import java.util.List;

import com.ineasnet.shanghaiguidao2.entity.vo.QueueVo;


public class QueueBean {
	private String type;
	private List<QueueVo> list;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<QueueVo> getList() {
		return list;
	}
	public void setList(List<QueueVo> list) {
		this.list = list;
	}
	
}
