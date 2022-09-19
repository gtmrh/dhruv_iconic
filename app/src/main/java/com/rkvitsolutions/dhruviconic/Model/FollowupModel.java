package com.rkvitsolutions.dhruviconic.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class FollowupModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Dated")
    @Expose
    private String dated;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("Profession")
    @Expose
    private String profession;
    @SerializedName("Budget")
    @Expose
    private String budget;
    @SerializedName("PhoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Mode")
    @Expose
    private String mode;
    @SerializedName("Purpose")
    @Expose
    private String purpose;
    @SerializedName("Conversation")
    @Expose
    private String conversation;
    @SerializedName("CCode")
    @Expose
    private String cCode;
    @SerializedName("NDate")
    @Expose
    private String NDate;
    @SerializedName("CType")
    @Expose
    private String CType;
    @SerializedName("Sourse")
    @Expose
    private String sourse;


    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }

    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    public String getCType() {
        return CType;
    }

    public void setCType(String CType) {
        this.CType = CType;
    }

    public FollowupModel(List<Object> data) {
        this.data = data;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    public String getCCode() {
        return cCode;
    }

    public void setCCode(String cCode) {
        this.cCode = cCode;
    }

    public String getNDate() {
        return NDate;
    }

    public void setNDate(String NDate) {
        this.NDate = NDate;
    }
}