package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestOTPForVerify {

    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("otp_ref")
    @Expose
    private String otpRef;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpRef() {
        return otpRef;
    }

    public void setOtpRef(String otpRef) {
        this.otpRef = otpRef;
    }

}

