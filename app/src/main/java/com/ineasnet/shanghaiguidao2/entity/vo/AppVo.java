package com.ineasnet.shanghaiguidao2.entity.vo;

/**
 * app版本信息
 * Created by Administrator on 2017/7/25.
 */

public class AppVo {
    /** App名称 **/
    private String appDesc;
    private int appFlag;
    private int appMin;
    /** App下载路径 **/
    private String appUrl;
    /** App版本名 **/
    private String appVersion;
    /** App版本号 **/
    private int versionNo;
    /** App返回语句 **/
    private String exception;
    /** App返回号 **/
    private String exceptionCode;
    /** App返回号 **/
    private String exceptionKey;

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public int getAppFlag() {
        return appFlag;
    }

    public void setAppFlag(int appFlag) {
        this.appFlag = appFlag;
    }

    public int getAppMin() {
        return appMin;
    }

    public void setAppMin(int appMin) {
        this.appMin = appMin;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionKey() {
        return exceptionKey;
    }

    public void setExceptionKey(String exceptionKey) {
        this.exceptionKey = exceptionKey;
    }
}
