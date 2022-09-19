package com.rkvitsolutions.dhruviconic.Model;

import com.google.gson.annotations.SerializedName;


public class LoginData {

    @SerializedName("Team")
    private String team;
    @SerializedName("Type")
    private String type;
    @SerializedName("UserCode")
    private String userCode;
    @SerializedName("Username")
    private String username;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
