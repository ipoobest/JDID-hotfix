package com.viva.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVerifyUser {
    @SerializedName("verified_at")
    @Expose
    private String verifiedAt;
    @SerializedName("verified_count")
    @Expose
    private Integer verifiedCount;

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Integer getVerifiedCount() {
        return verifiedCount;
    }

    public void setVerifiedCount(Integer verifiedCount) {
        this.verifiedCount = verifiedCount;
    }
}
