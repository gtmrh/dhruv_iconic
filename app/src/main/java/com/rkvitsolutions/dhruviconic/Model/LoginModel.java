
package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LoginModel {

    @Expose
    private List<LoginData> data;
    @Expose
    private String message;
    @Expose
    private String status;
    @SerializedName("user_type")
    private String userType;

    public List<LoginData> getData() {
        return data;
    }

    public void setData(List<LoginData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
