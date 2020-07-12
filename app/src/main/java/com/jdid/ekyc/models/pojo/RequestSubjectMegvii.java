package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestSubjectMegvii {
    @SerializedName("birthday")
    @Expose
    private Integer birthday;
    @SerializedName("entry_date")
    @Expose
    private Integer entryDate;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("group_ids")
    @Expose
    private List<Object> groupIds = null;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo_ids")
    @Expose
    private List<Integer> photoIds = null;
    @SerializedName("subject_type")
    @Expose
    private Integer subjectType;

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public Integer getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Integer entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<Object> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<Object> groupIds) {
        this.groupIds = groupIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPhotoIds() {
        return photoIds;
    }

    public void setPhotoIds(List<Integer> photoIds) {
        this.photoIds = photoIds;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }
}
