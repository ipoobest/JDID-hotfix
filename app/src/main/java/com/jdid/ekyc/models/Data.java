package com.jdid.ekyc.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

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