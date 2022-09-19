package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlotModel {

@SerializedName("data")
@Expose
private List<Object> data = null;
@SerializedName("PROCode")
@Expose
private String pROCode;
@SerializedName("PROName")
@Expose
private String pROName;
@SerializedName("PLCode")
@Expose
private String pLCode;
@SerializedName("PLName")
@Expose
private String pLName;
@SerializedName("Status")
@Expose
private String status;

public List<Object> getData() {
return data;
}

public void setData(List<Object> data) {
this.data = data;
}

public String getPROCode() {
return pROCode;
}

public void setPROCode(String pROCode) {
this.pROCode = pROCode;
}

public String getPROName() {
return pROName;
}

public void setPROName(String pROName) {
this.pROName = pROName;
}

public String getPLCode() {
return pLCode;
}

public void setPLCode(String pLCode) {
this.pLCode = pLCode;
}

public String getPLName() {
return pLName;
}

public void setPLName(String pLName) {
this.pLName = pLName;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}