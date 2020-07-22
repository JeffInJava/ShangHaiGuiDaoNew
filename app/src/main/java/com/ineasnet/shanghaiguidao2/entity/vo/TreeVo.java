package com.ineasnet.shanghaiguidao2.entity.vo;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

@Table(name="pdf")
public class TreeVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="tableId",isId=true)
	private int tableId;
	@Column(name="id")
	private int id;
	@Column(name="userId")
	private String userId;
	@Column(name="name")
	private String name;
	@Column(name="parentId")
	private String parentId;
	@Column(name="docName")
	private String docName;
	@Column(name="pdfNameUrl")
	private String pdfNameUrl;
	@Column(name="pdfName")
	private String pdfName;
	@Column(name="tblStudyDataId")
	private int tblStudyDataId;
	@Column(name="studyStatus")
	private String studyStatus;
	@Column(name="studiedNum")
	private String studiedNum;
	@Column(name="docNum")
	private String docNum;
	@Column(name="ruleNum")
	private String ruleNum;
	@Column(name="departmentNos")
	private String departmentNos;
	@Column(name="departmentNames")
	private String departmentNames;
	@Column(name="createTime")
	private long createTime;
	@Column(name="createTimeShow")
	private String createTimeShow;
	@Column(name="docID")
	private String docID;
	@Column(name="folderName")
	private String folderName;
	@Column(name="hisStatus")
	private int hisStatus;
	@Column(name="pageCount")
	private int pageCount;
	@Column(name="sendRange")
	private String sendRange;
	@Column(name="sendRangeCode")
	private String sendRangeCode;
	@Column(name="title")
	private String title;
	private boolean isDownloading = false;
	@Column(name="isExpert")
	private int isExpert;
	private long size;
	@Column(name = "version")
	private String version;
	private String sendTime;
	@Column(name = "replaceID")
	private String replaceID;
	@Column(name = "medifyNum")
	private String medifyNum;
	@Column(name = "tblCategoryId")
	private int tblCategoryId;
	@Column(name = "isNew")
	private boolean isNew = true;
	@Column(name = "read")
	private boolean read = false;
	@Column(name = "studyTime")
	private long studyTime;
	private String studyTimeShow;

	public String getStudiedNum() {
		return studiedNum;
	}

	public void setStudiedNum(String studiedNum) {
		this.studiedNum = studiedNum;
	}

	public String getDepartmentNames() {
		return departmentNames;
	}

	public void setDepartmentNames(String departmentNames) {
		this.departmentNames = departmentNames;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

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

	// public int getStudyStatus() {
	// return studyStatus;
	// }
	// public void setStudyStatus(int studyStatus) {
	// this.studyStatus = studyStatus;
	// }

	public String getStudyStatus() {
		return studyStatus;
	}

	public void setStudyStatus(String studyStatus) {
		this.studyStatus = studyStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getPdfNameUrl() {
		return pdfNameUrl;
	}

	public void setPdfNameUrl(String pdfNameUrl) {
		this.pdfNameUrl = pdfNameUrl;
	}

	public String getPdfName() {
		return pdfName;
	}

	public void setPdfName(String pdfName) {
		this.pdfName = pdfName;
	}


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}


	public String getCreateTimeShow() {
		return createTimeShow;
	}

	public void setCreateTimeShow(String createTimeShow) {
		this.createTimeShow = createTimeShow;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public int getHisStatus() {
		return hisStatus;
	}

	public void setHisStatus(int hisStatus) {
		this.hisStatus = hisStatus;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public String getSendRange() {
		return sendRange;
	}

	public void setSendRange(String sendRange) {
		this.sendRange = sendRange;
	}

	public String getSendRangeCode() {
		return sendRangeCode;
	}

	public void setSendRangeCode(String sendRangeCode) {
		this.sendRangeCode = sendRangeCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDownloading() {
		return isDownloading;
	}

	public void setDownloading(boolean downloading) {
		isDownloading = downloading;
	}

	public int getIsExpert() {
		return isExpert;
	}

	public void setIsExpert(int isExpert) {
		this.isExpert = isExpert;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public int getTblStudyDataId() {
		return tblStudyDataId;
	}

	public void setTblStudyDataId(int tblStudyDataId) {
		this.tblStudyDataId = tblStudyDataId;
	}

	public String getReplaceID() {
		return replaceID;
	}

	public void setReplaceID(String replaceID) {
		this.replaceID = replaceID;
	}

	public String getMedifyNum() {
		return medifyNum;
	}

	public void setMedifyNum(String medifyNum) {
		this.medifyNum = medifyNum;
	}

	public int getTblCategoryId() {
		return tblCategoryId;
	}

	public void setTblCategoryId(int tblCategoryId) {
		this.tblCategoryId = tblCategoryId;
	}
	public int getTableId() {
		return tableId;
	}

	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean aNew) {
		isNew = aNew;
	}


	public long getStudyTime() {
		return studyTime;
	}

	public void setStudyTime(long studyTime) {
		this.studyTime = studyTime;
	}

	public String getStudyTimeShow() {
		return studyTimeShow;
	}

	public void setStudyTimeShow(String studyTimeShow) {
		this.studyTimeShow = studyTimeShow;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}
