package com.example.doit;

import com.google.gson.annotations.SerializedName;

import retrofit2.Retrofit;

public class DTO_User {

    @SerializedName("ID")
    private String ID;

    @SerializedName("passwd")
    private String passwd;

    @SerializedName("Email")
    private String Email;

    @SerializedName("Mtype")
    private String Mtype;



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMtype() {
        return Mtype;
    }

    public void setMtype(String mtype) {
        Mtype = mtype;
    }

}
