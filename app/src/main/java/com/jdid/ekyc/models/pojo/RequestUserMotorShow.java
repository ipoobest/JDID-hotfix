package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserMotorShow {
    @SerializedName("subject_id")
    @Expose
    private int subjectId;
    @SerializedName("photo_id")
    @Expose
    private int photoId;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;
    @SerializedName("coffee")
    @Expose
    private String coffee;
    @SerializedName("status")
    @Expose
    private String status;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCoffee() {
        return coffee;
    }

    public void setCoffee(String coffee) {
        this.coffee = coffee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
