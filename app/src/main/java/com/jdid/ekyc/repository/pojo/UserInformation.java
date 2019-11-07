package com.jdid.ekyc.repository.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInformation {
    @SerializedName("name_th")
    @Expose
    private String nameTh;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("purpose")
    @Expose
    private String purpose;
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("back_idcard")
    @Expose
    private Object backIdcard;
    @SerializedName("census_address")
    @Expose
    private Object censusAddress;
    @SerializedName("current_address")
    @Expose
    private String currentAddress;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("official_address")
    @Expose
    private String officialAddress;
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
    private String income;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("portrait_url")
    @Expose
    private String portraitUrl;
    @SerializedName("verified")
    @Expose
    private Boolean verified;
    @SerializedName("verified_at")
    @Expose
    private String verifiedAt;
    @SerializedName("verify_by")
    @Expose
    private String verifyBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("IAL")
    @Expose
    private Double iAL;

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Object getBackIdcard() {
        return backIdcard;
    }

    public void setBackIdcard(Object backIdcard) {
        this.backIdcard = backIdcard;
    }

    public Object getCensusAddress() {
        return censusAddress;
    }

    public void setCensusAddress(Object censusAddress) {
        this.censusAddress = censusAddress;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public String getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(String officialAddress) {
        this.officialAddress = officialAddress;
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

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(String verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getVerifyBy() {
        return verifyBy;
    }

    public void setVerifyBy(String verifyBy) {
        this.verifyBy = verifyBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getiAL() {
        return iAL;
    }

    public void setiAL(Double iAL) {
        this.iAL = iAL;
    }
}
