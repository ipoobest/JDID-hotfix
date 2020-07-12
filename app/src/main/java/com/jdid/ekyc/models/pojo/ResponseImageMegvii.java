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
    @SerializedName("page")
    @Expose
    private Page page;
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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Integer getTimecost() {
        return timecost;
    }

    public void setTimecost(Integer timecost) {
        this.timecost = timecost;
    }
}
