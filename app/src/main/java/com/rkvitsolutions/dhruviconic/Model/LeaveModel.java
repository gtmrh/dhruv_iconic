package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;

import java.util.List;


public class LeaveModel {

    @Expose
    private List<Object> data;
    @Expose
    private String status;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
