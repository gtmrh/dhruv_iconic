package com.rkvitsolutions.dhruviconic.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SalesLeadsModel {

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
    @SerializedName("Sourse")
    @Expose
    private String sourse;

    public List<Object> getData() {
        return data;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
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

}