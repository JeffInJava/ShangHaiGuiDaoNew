package com.ineasnet.shanghaiguidao2.entity.vo;

import java.io.Serializable;

public class DownLoadListVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private String filePath;
	private TreeVo vo;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public TreeVo getVo() {
		return vo;
	}
	public void setVo(TreeVo vo) {
		this.vo = vo;
	}
	private int progress;
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}
