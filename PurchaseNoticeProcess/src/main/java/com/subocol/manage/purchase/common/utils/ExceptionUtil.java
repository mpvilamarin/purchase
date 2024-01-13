package com.subocol.manage.purchase.common.utils;


import lombok.Getter;

@Getter
public class ExceptionUtil extends RuntimeException {

    private  int code;
    private String message;
    private String ex;

    public ExceptionUtil(int code, String message, String cause) {
        super(message);
        this.message = message;
        this.code = code;
        this.ex = cause;
    }

    public ExceptionUtil(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ExceptionUtil(ExceptionUtil exceptionUtil) {
        super(exceptionUtil.getMessage());
        this.code = exceptionUtil.getCode();
        this.ex = exceptionUtil.getCause().toString();
    }

    public int getStatusCode() {
        return code;
    }

}
