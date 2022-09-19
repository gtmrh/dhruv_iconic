package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LeaveListModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Dated")
    @Expose
    private String dated;
    @SerializedName("Empcode")
    @Expose
    private String empcode;
    @SerializedName("FName")
    @Expose
    private String fName;
    @SerializedName("Department")
    @Expose
    private String department;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("FromDate")
    @Expose
    private String fromDate;
    @SerializedName("ToDate")
    @Expose
    private String toDate;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("WEMPCode")
    @Expose
    private String wEMPCode;
    @SerializedName("WName")
    @Expose
    private String wName;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SStatus")
    @Expose
    private String sStatus;
    @SerializedName("LType")
    @Expose
    private String lType;

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

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWEMPCode() {
        return wEMPCode;
    }

    public void setWEMPCode(String wEMPCode) {
        this.wEMPCode = wEMPCode;
    }

    public String getWName() {
        return wName;
    }

    public void setWName(String wName) {
        this.wName = wName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSStatus() {
        return sStatus;
    }

    public void setSStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public String getLType() {
        return lType;
    }

    public void setLType(String lType) {
        this.lType = lType;
    }

}