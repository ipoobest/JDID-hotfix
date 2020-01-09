package com.viva.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceCompareResult {
    @SerializedName("rtn")
    @Expose
    private Integer rtn;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("pair_verify_result")
    @Expose
    private Integer pairVerifyResult;
    @SerializedName("pair_verify_similarity")
    @Expose
    private Integer pairVerifySimilarity;
    @SerializedName("query_image_package_result")
    @Expose
    private Object queryImagePackageResult;
    @SerializedName("verify_detail")
    @Expose
    private VerifyDetail verifyDetail;

    public Integer getRtn() {
        return rtn;
    }

    public void setRtn(Integer rtn) {
        this.rtn = rtn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPairVerifyResult() {
        return pairVerifyResult;
    }

    public void setPairVerifyResult(Integer pairVerifyResult) {
        this.pairVerifyResult = pairVerifyResult;
    }

    public Integer getPairVerifySimilarity() {
        return pairVerifySimilarity;
    }

    public void setPairVerifySimilarity(Integer pairVerifySimilarity) {
        this.pairVerifySimilarity = pairVerifySimilarity;
    }

    public Object getQueryImagePackageResult() {
        return queryImagePackageResult;
    }

    public void setQueryImagePackageResult(Object queryImagePackageResult) {
        this.queryImagePackageResult = queryImagePackageResult;
    }

    public VerifyDetail getVerifyDetail() {
        return verifyDetail;
    }

    public void setVerifyDetail(VerifyDetail verifyDetail) {
        this.verifyDetail = verifyDetail;
    }
}
