package com.jdid.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseSubjectMegvii {
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("birthday")
    @Expose
    private Object birthday;
    @SerializedName("come_from")
    @Expose
    private String comeFrom;
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("create_time")
    @Expose
    private Integer createTime;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("end_time")
    @Expose
    private Integer endTime;
    @SerializedName("entry_date")
    @Expose
    private Object entryDate;
    @SerializedName("extra_id")
    @Expose
    private Object extraId;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("groups")
    @Expose
    private List<Object> groups = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("interviewee")
    @Expose
    private String interviewee;
    @SerializedName("interviewee_pinyin")
    @Expose
    private String intervieweePinyin;
    @SerializedName("job_number")
    @Expose
    private String jobNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password_reseted")
    @Expose
    private Boolean passwordReseted;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photos")
    @Expose
    private List<Data> photos = null;
    @SerializedName("pinyin")
    @Expose
    private String pinyin;
    @SerializedName("purpose")
    @Expose
    private Integer purpose;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("start_time")
    @Expose
    private Integer startTime;
    @SerializedName("subject_type")
    @Expose
    private Integer subjectType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("visit_notify")
    @Expose
    private Boolean visitNotify;
    @SerializedName("wg_number")
    @Expose
    private String wgNumber;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Object getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Object entryDate) {
        this.entryDate = entryDate;
    }

    public Object getExtraId() {
        return extraId;
    }

    public void setExtraId(Object extraId) {
        this.extraId = extraId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public List<Object> getGroups() {
        return groups;
    }

    public void setGroups(List<Object> groups) {
        this.groups = groups;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterviewee() {
        return interviewee;
    }

    public void setInterviewee(String interviewee) {
        this.interviewee = interviewee;
    }

    public String getIntervieweePinyin() {
        return intervieweePinyin;
    }

    public void setIntervieweePinyin(String intervieweePinyin) {
        this.intervieweePinyin = intervieweePinyin;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPasswordReseted() {
        return passwordReseted;
    }

    public void setPasswordReseted(Boolean passwordReseted) {
        this.passwordReseted = passwordReseted;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Data> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Data> photos) {
        this.photos = photos;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Integer getPurpose() {
        return purpose;
    }

    public void setPurpose(Integer purpose) {
        this.purpose = purpose;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVisitNotify() {
        return visitNotify;
    }

    public void setVisitNotify(Boolean visitNotify) {
        this.visitNotify = visitNotify;
    }

    public String getWgNumber() {
        return wgNumber;
    }

    public void setWgNumber(String wgNumber) {
        this.wgNumber = wgNumber;
    }
}
