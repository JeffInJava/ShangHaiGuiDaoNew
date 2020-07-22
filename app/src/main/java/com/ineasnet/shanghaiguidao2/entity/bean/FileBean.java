package com.ineasnet.shanghaiguidao2.entity.bean;


public class FileBean {
	private String id;
	private String pid;
	private String name;
	
	/**
	 * 0 
	 * 1
	 * 2
	 * 3
	 */
	private int status;
	
	
	public FileBean(String id , String pid , String name){
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
