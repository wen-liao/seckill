package com.ds.seckill.util.dto;

import java.io.Serializable;

public class DTO <T> implements Serializable {

    private boolean isSuccessful;
    private String statusCode;
    private String message;
    private T data;

    public DTO(boolean isSuccessful, String statusCode, String message, T data){
        this.isSuccessful = isSuccessful;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public boolean getIsSuccess() {
        return isSuccessful;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccessful = isSuccess;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
