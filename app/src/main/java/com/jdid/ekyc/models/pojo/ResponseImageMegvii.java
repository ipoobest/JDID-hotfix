package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseImageMegvii {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("timecost")
    @Expose
    private Integer timecost;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getTimecost() {
        return timecost;
    }

    public void setTimecost(Integer timecost) {
        this.timecost = timecost;
    }
}
