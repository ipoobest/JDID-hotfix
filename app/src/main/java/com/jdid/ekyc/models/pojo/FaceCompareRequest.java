package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceCompareRequest {

    @SerializedName("database_image_content")
    @Expose
    private String databaseImageContent;
    @SerializedName("database_image_type")
    @Expose
    private Integer databaseImageType;
    @SerializedName("query_image_content")
    @Expose
    private String queryImageContent;
    @SerializedName("query_image_type")
    @Expose
    private Integer queryImageType;
    @SerializedName("true_negative_rate")
    @Expose
    private String trueNegativeRate;

    public String getDatabaseImageContent() {
        return databaseImageContent;
    }

    public void setDatabaseImageContent(String databaseImageContent) {
        this.databaseImageContent = databaseImageContent;
    }

    public Integer getDatabaseImageType() {
        return databaseImageType;
    }

    public void setDatabaseImageType(Integer databaseImageType) {
        this.databaseImageType = databaseImageType;
    }

    public String getQueryImageContent() {
        return queryImageContent;
    }

    public void setQueryImageContent(String queryImageContent) {
        this.queryImageContent = queryImageContent;
    }

    public Integer getQueryImageType() {
        return queryImageType;
    }

    public void setQueryImageType(Integer queryImageType) {
        this.queryImageType = queryImageType;
    }

    public String getTrueNegativeRate() {
        return trueNegativeRate;
    }

    public void setTrueNegativeRate(String trueNegativeRate) {
        this.trueNegativeRate = trueNegativeRate;
    }
}
