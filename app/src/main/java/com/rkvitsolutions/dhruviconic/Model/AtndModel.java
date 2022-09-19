package com.rkvitsolutions.dhruviconic.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AtndModel {

    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public AtndModel withData(List<Object> data) {
        this.data = data;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AtndModel withStatus(String status) {
        this.status = status;
        return this;
    }

}