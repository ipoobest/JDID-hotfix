package com.jdid.ekyc.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDataCreateUser {
@SerializedName("status_code")
@Expose
private Integer statusCode;
@SerializedName("data")
@Expose
private Data data;
@SerializedName("message")
@Expose
private String message;
@SerializedName("created_at")
@Expose
private String createdAt;

public Integer getStatusCode() {
        return statusCode;
        }

public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        }

public Data getData() {
        return data;
        }

public void setData(Data data) {
        this.data = data;
        }

public String getMessage() {
        return message;
        }

public void setMessage(String message) {
        this.message = message;
        }

public String getCreatedAt() {
        return createdAt;
        }

public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        }
}
