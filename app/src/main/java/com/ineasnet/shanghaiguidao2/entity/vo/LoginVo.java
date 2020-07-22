package com.ineasnet.shanghaiguidao2.entity.vo;

public class LoginVo {
	private String sessionId;
	private String userId;
	private String userName;
	private String password;
	private String departmentNo;
	private boolean superAdmin;
	private String userPathCode;
	private String userRole;
	private String userRoleKey;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartmentNo() {
		return departmentNo;
	}

	public void setDepartmentNo(String departmentNo) {
		this.departmentNo = departmentNo;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public String getUserPathCode() {
		return userPathCode;
	}

	public void setUserPathCode(String userPathCode) {
		this.userPathCode = userPathCode;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserRoleKey() {
		return userRoleKey;
	}

	public void setUserRoleKey(String userRoleKey) {
		this.userRoleKey = userRoleKey;
	}
}
