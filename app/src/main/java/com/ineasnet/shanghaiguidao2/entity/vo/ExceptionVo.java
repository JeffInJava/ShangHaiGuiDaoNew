package com.ineasnet.shanghaiguidao2.entity.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/21.
 */

public class ExceptionVo implements Serializable {
    /**
     * exception : java.lang.NullPointerException[174052(/login)]
     * exceptionCode : 99
     * exceptionKey : null
     */

    private String exception;
    private int exceptionCode;
    private String exceptionKey;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public String getExceptionKey() {
        return exceptionKey;
    }

    public void setExceptionKey(String exceptionKey) {
        this.exceptionKey = exceptionKey;
    }
}
