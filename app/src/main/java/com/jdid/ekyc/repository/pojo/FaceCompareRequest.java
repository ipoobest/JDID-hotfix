package com.jdid.ekyc.repository.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceCompareRequest {

//                requestParams.put("database_image_content", base64Image1);
//            requestParams.put("database_image_type", 101);
//            requestParams.put("query_image_content", base64Image2);
//            requestParams.put("query_image_type", 301);
//            requestParams.put("true_negative_rate", "99.9");

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
