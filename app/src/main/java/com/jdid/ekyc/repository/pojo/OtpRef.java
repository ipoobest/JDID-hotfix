package com.jdid.ekyc.repository.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpRef {
    @SerializedName("otp_ref")
    @Expose
    private String otpRef;

    public String getOtpRef() {
        return otpRef;
    }

    public void setOtpRef(String otpRef) {
        this.otpRef = otpRef;
    }
}
