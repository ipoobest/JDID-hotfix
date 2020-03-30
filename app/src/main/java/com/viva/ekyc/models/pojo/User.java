package com.viva.ekyc.models.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name_th")
    @Expose
    private String nameTh;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("official_address")
    @Expose
    private String officialAddress;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("current_address")
    @Expose
    private String currentAddress;
    @SerializedName("mariage_status")
    @Expose
    private String mariageStatus;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("company_address")
    @Expose
    private String companyAddress;
    @SerializedName("income")
    @Expose
    private Double income;
    @SerializedName("verify_by")
    @Expose
    private String verifyBy;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("portrait_url")
    @Expose
    private String portraitUrl;
    @SerializedName("refer_by")
    @Expose
    private String referBy;

    @SerializedName("back_idcard")
    @Expose
    private String backIdcard;

    public String getBackIdcard() {
        return backIdcard;
    }

    public void setBackIdcard(String backIdcard) {
        this.backIdcard = backIdcard;
    }

    public String getReferBy() {
        return referBy;
    }

    public void setReferBy(String referBy) {
        this.referBy = referBy;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(String officialAddress) {
        this.officialAddress = officialAddress;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getMariageStatus() {
        return mariageStatus;
    }

    public void setMariageStatus(String mariageStatus) {
        this.mariageStatus = mariageStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        this.income = income;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVerifyBy() {
        return verifyBy;
    }

    public void setVerifyBy(String verifyBy) {
        this.verifyBy = verifyBy;
    }
}
