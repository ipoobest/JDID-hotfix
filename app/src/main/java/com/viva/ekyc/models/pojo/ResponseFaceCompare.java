package com.viva.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFaceCompare {
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("log_id")
    @Expose
    private float logId;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("cached")
    @Expose
    private Integer cached;
    @SerializedName("result")
    @Expose
    private ResultFaceCompare resultFaceCompare;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public float getLogId() {
        return logId;
    }

    public void setLogId(float logId) {
        this.logId = logId;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getCached() {
        return cached;
    }

    public void setCached(Integer cached) {
        this.cached = cached;
    }

    public ResultFaceCompare getResultFaceCompare() {
        return resultFaceCompare;
    }

    public void setResultFaceCompare(ResultFaceCompare resultFaceCompare) {
        this.resultFaceCompare = resultFaceCompare;
    }
}
