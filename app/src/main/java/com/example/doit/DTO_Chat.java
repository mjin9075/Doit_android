package com.example.doit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DTO_Chat implements Serializable {

    public String roomN;
    public String senderID;
    public String message;
    public String time_c;
    public String time_d;
    public String my_name;
    public String my_img;
    public String Iam;

    public DTO_Chat() {
    }

    public DTO_Chat(String roomN, String senderID, String message, String time_c, String time_d, String my_name, String my_img, String iam) {
        this.roomN = roomN;
        this.senderID = senderID;
        this.message = message;
        this.time_c = time_c;
        this.time_d = time_d;
        this.my_name = my_name;
        this.my_img = my_img;
        this.Iam = iam;
    }

    public String getRoomN() {
        return roomN;
    }

    public void setRoomN(String roomN) {
        this.roomN = roomN;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime_c() {
        return time_c;
    }

    public void setTime_c(String time_c) {
        this.time_c = time_c;
    }

    public String getTime_d() {
        return time_d;
    }

    public void setTime_d(String time_d) {
        this.time_d = time_d;
    }

    public String getMy_name() {
        return my_name;
    }

    public void setMy_name(String my_name) {
        this.my_name = my_name;
    }

    public String getMy_img() {
        return my_img;
    }

    public void setMy_img(String my_img) {
        this.my_img = my_img;
    }

    public String getIam() {
        return Iam;
    }

    public void setIam(String iam) {
        Iam = iam;
    }
}
