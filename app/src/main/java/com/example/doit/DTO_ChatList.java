package com.example.doit;

import com.google.gson.annotations.SerializedName;

public class DTO_ChatList {

    //gm리스트에서 필요하것 - hm의 f_name, img, msg, 날짜
    //hm리스트에서 필요하것 - gm의 id, msg, 날짜


    @SerializedName("ROOM_N")
    public String ROOM_N;

    @SerializedName("GM_ID")
    public String GM_ID;

    @SerializedName("MSG")
    public String MSG;

    @SerializedName("TIME")
    public String TIME;

    @SerializedName("TIME_D")
    public String TIME_D;

    @SerializedName("HM_ID")
    public String HM_ID;


    @SerializedName("facility_name")
    public String facility_name;

    @SerializedName("facility_image")
    public String facility_image;

//    public DTO_ChatList(String ROOM_N, String GM_ID, String MSG) {
//        this.ROOM_N = ROOM_N;
//        this.GM_ID = GM_ID;
//        this.MSG = MSG;
//    }


    public String getGM_ID() {
        return GM_ID;
    }

    public void setGM_ID(String GM_ID) {
        this.GM_ID = GM_ID;
    }

    public String getMSG() {
        return MSG;
    }

    public void setMSG(String MSG) {
        this.MSG = MSG;
    }

    public String getROOM_N() {
        return ROOM_N;
    }

    public void setROOM_N(String ROOM_N) {
        this.ROOM_N = ROOM_N;
    }

    public String getFacility_name() {
        return facility_name;
    }

    public void setFacility_name(String facility_name) {
        this.facility_name = facility_name;
    }

    public String getFacility_image() {
        return facility_image;
    }

    public void setFacility_image(String facility_image) {
        this.facility_image = facility_image;
    }

    public String getHM_ID() {
        return HM_ID;
    }

    public void setHM_ID(String HM_ID) {
        this.HM_ID = HM_ID;
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
}
