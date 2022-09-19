package com.rkvitsolutions.dhruviconic.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmpListModel {

@SerializedName("data")
@Expose
private List<Object> data = null;
@SerializedName("EMPCode")
@Expose
private String eMPCode;
@SerializedName("Name")
@Expose
private String name;

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

}