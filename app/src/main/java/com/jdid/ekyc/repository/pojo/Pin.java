package com.jdid.ekyc.repository.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pin {
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("pin")
    @Expose
    private String pin;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
