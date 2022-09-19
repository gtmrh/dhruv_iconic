package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourceStsModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("EMPCode")
    @Expose
    private String eMPCode;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("WON")
    @Expose
    private String won;
    @SerializedName("None")
    @Expose
    private String none;
    @SerializedName("LoginProcessClient")
    @Expose
    private String loginProcessClient;
    @SerializedName("OnGoing")
    @Expose
    private String onGoing;
    @SerializedName("MeetingScheduled")
    @Expose
    private String meetingScheduled;
    @SerializedName("SiteVisitDone")
    @Expose
    private String siteVisitDone;
    @SerializedName("Loss")
    @Expose
    private String loss;
    @SerializedName("MeetingDone")
    @Expose
    private String meetingDone;
    @SerializedName("SiteVisitScheduled")
    @Expose
    private String siteVisitScheduled;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getEMPCode() {
        return eMPCode;
    }

    public void setEMPCode(String eMPCode) {
        this.eMPCode = eMPCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWon() {
        return won;
    }

    public void setWon(String won) {
        this.won = won;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }

    public String getLoginProcessClient() {
        return loginProcessClient;
    }

    public void setLoginProcessClient(String loginProcessClient) {
        this.loginProcessClient = loginProcessClient;
    }

    public String getOnGoing() {
        return onGoing;
    }

    public void setOnGoing(String onGoing) {
        this.onGoing = onGoing;
    }

    public String getMeetingScheduled() {
        return meetingScheduled;
    }

    public void setMeetingScheduled(String meetingScheduled) {
        this.meetingScheduled = meetingScheduled;
    }

    public String getSiteVisitDone() {
        return siteVisitDone;
    }

    public void setSiteVisitDone(String siteVisitDone) {
        this.siteVisitDone = siteVisitDone;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    public String getMeetingDone() {
        return meetingDone;
    }

    public void setMeetingDone(String meetingDone) {
        this.meetingDone = meetingDone;
    }

    public String getSiteVisitScheduled() {
        return siteVisitScheduled;
    }

    public void setSiteVisitScheduled(String siteVisitScheduled) {
        this.siteVisitScheduled = siteVisitScheduled;
    }

}