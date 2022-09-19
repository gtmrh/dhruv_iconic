package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllFollowupReportModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("MobileNo")
    @Expose
    private String mobileNo;
    @SerializedName("Mode")
    @Expose
    private String mode;
    @SerializedName("Purpose")
    @Expose
    private String purpose;
    @SerializedName("Conversation")
    @Expose
    private String conversation;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("CCode")
    @Expose
    private String cCode;
    @SerializedName("NDate")
    @Expose
    private String nDate;
    @SerializedName("Sourse")
    @Expose
    private String sourse;
    @SerializedName("FName")
    @Expose
    private String fName;
    @SerializedName("CType")
    @Expose
    private String cType;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCCode() {
        return cCode;
    }

    public void setCCode(String cCode) {
        this.cCode = cCode;
    }

    public String getNDate() {
        return nDate;
    }

    public void setNDate(String nDate) {
        this.nDate = nDate;
    }

    public String getSourse() {
        return sourse;
    }

    public void setSourse(String sourse) {
        this.sourse = sourse;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getCType() {
        return cType;
    }

    public void setCType(String cType) {
        this.cType = cType;
    }

}