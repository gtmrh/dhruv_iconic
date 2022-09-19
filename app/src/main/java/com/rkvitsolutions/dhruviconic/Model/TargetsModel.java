package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TargetsModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("EMPCode")
    @Expose
    private String eMPCode;
    @SerializedName("FDate")
    @Expose
    private String fDate;
    @SerializedName("EDate")
    @Expose
    private String eDate;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("DialCall")
    @Expose
    private String dialCall;
    @SerializedName("AnswerCall")
    @Expose
    private String answerCall;
    @SerializedName("TotalData")
    @Expose
    private String totalData;
    @SerializedName("Meeting")
    @Expose
    private String meeting;
    @SerializedName("SiteVisit")
    @Expose
    private String siteVisit;
    @SerializedName("Login")
    @Expose
    private String login;

    public String getDialCall() {
        return dialCall;
    }

    public void setDialCall(String dialCall) {
        this.dialCall = dialCall;
    }

    public String getAnswerCall() {
        return answerCall;
    }

    public void setAnswerCall(String answerCall) {
        this.answerCall = answerCall;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEMPCode() {
        return eMPCode;
    }

    public void setEMPCode(String eMPCode) {
        this.eMPCode = eMPCode;
    }

    public String getFDate() {
        return fDate;
    }

    public void setFDate(String fDate) {
        this.fDate = fDate;
    }

    public String getEDate() {
        return eDate;
    }

    public void setEDate(String eDate) {
        this.eDate = eDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTotalData() {
        return totalData;
    }

    public void setTotalData(String totalData) {
        this.totalData = totalData;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    public String getSiteVisit() {
        return siteVisit;
    }

    public void setSiteVisit(String siteVisit) {
        this.siteVisit = siteVisit;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}