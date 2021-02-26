package com.example.doit;

import com.google.gson.annotations.SerializedName;

public class DTO_ChatRoom {

//    @SerializedName("HM_ID")
//    private String HM_ID;

    @SerializedName("ROOM_N")
    public String ROOM_N;

    @SerializedName("MSG")
    public String MSG;

    @SerializedName("TIME")
    public String TIME;

    @SerializedName("TIME_D")
    public String TIME_D;

    public String getROOM_N() {
        return ROOM_N;
    }

    public void setROOM_N(String ROOM_N) {
        this.ROOM_N = ROOM_N;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getTIME_D() {
        return TIME_D;
    }

    public void setTIME_D(String TIME_D) {
        this.TIME_D = TIME_D;
    }


    //
//    public String getHM_ID() {
//        return HM_ID;
//    }
//
//    public void setHM_ID(String HM_ID) {
//        this.HM_ID = HM_ID;
//    }





}
