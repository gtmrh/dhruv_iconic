package com.rkvitsolutions.dhruviconic.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JoiningsModel {

    @SerializedName("data")
    @Expose
    private List<String> data = null;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Qualification")
    @Expose
    private String qualification;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("Experience")
    @Expose
    private String experience;
    @SerializedName("Hiring")
    @Expose
    private String hiring;
    @SerializedName("Post")
    @Expose
    private String post;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Mode")
    @Expose
    private String mode;
    @SerializedName("Callcode")
    @Expose
    private String callcode;
    @SerializedName("JoiningDate")
    @Expose
    private String joiningDate;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Project")
    @Expose
    private String project;
    @SerializedName("Designation")
    @Expose
    private String designation;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getHiring() {
        return hiring;
    }

    public void setHiring(String hiring) {
        this.hiring = hiring;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCallcode() {
        return callcode;
    }

    public void setCallcode(String callcode) {
        this.callcode = callcode;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}