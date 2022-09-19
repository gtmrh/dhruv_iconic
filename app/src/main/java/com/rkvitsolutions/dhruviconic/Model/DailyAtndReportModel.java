package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyAtndReportModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("EMPCode")
    @Expose
    private String eMPCode;
    @SerializedName("INTime")
    @Expose
    private String iNTime;
    @SerializedName("OUTTime")
    @Expose
    private String oUTTime;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Dated")
    @Expose
    private String dated;

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

    public String getINTime() {
        return iNTime;
    }

    public void setINTime(String iNTime) {
        this.iNTime = iNTime;
    }

    public String getOUTTime() {
        return oUTTime;
    }

    public void setOUTTime(String oUTTime) {
        this.oUTTime = oUTTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

}