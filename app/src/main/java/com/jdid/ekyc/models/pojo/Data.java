package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("origin_url")
    @Expose
    private String originUrl;
    @SerializedName("quality")
    @Expose
    private Object quality;
    @SerializedName("subject_id")
    @Expose
    private int subjectId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("version")
    @Expose
    private Integer version;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public Object getQuality() {
        return quality;
    }

    public void setQuality(Object quality) {
        this.quality = quality;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
