package com.ineasnet.shanghaiguidao2.entity.bean;

public class MyNodeBean {
	/**
	 * 节点Id
	 */
	private String id;
	/**
	 * 节点父id
	 */
	private String pId;
	/**
	 * 节点name
	 */
	private String name;
	/**
	 * 
	 */
	private String desc;
	/**
	 * 节点名字长度
	 */
	private long length;

	private String docname;
	private String docnameurl;
	private String tblStudyDataId;
	/**
	 * 0 父节点 1 未下载 2 已下载 3 未阅读 4 已阅读
	 */
	private int status;

	private String studyStatus;

	private String docNum;
	private String ruleNum;
	
	private String departmentNos;

	public String getDepartmentNos() {
		return departmentNos;
	}

	public void setDepartmentNos(String departmentNos) {
		this.departmentNos = departmentNos;
	}

	public String getDocNum() {
		return docNum;
	}

	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}

	public String getRuleNum() {
		return ruleNum;
	}

	public void setRuleNum(String ruleNum) {
		this.ruleNum = ruleNum;
	}

	private int progress;

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}


	public String getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(String studyStatus) {
		this.studyStatus = studyStatus;
	}

	public MyNodeBean(String id, String pId, String name, int status,
			String docName, String docNameUrl, String tblStudyDataId,
			String studyStatus, String docNum, String ruleNum, int progress,String departmentNos) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.status = status;
		this.docname = docName;
		this.docnameurl = docNameUrl;
		this.tblStudyDataId = tblStudyDataId;
		this.studyStatus = studyStatus;
		this.progress = progress;
		this.departmentNos = departmentNos;
	}

	// public int getId() {
	// return id;
	// }
	// public void setId(int id) {
	// this.id = id;
	// }
	// public int getPid() {
	// return pId;
	// }
	// public void setPid(int pId) {
	// this.pId = pId;
	// }

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getDocName() {
		return docname;
	}

	public void setDocName(String docName) {
		this.docname = docName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public String getDocnameurl() {
		return docnameurl;
	}

	public void setDocnameurl(String docnameurl) {
		this.docnameurl = docnameurl;
	}

	public String getTblStudyDataId() {
		return tblStudyDataId;
	}

	public void setTblStudyDataId(String tblStudyDataId) {
		this.tblStudyDataId = tblStudyDataId;
	}

}
