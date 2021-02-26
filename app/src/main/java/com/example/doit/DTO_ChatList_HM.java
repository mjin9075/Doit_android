package com.example.doit;

import com.google.gson.annotations.SerializedName;

public class DTO_ChatList_HM {

//    @SerializedName("HM_ID")
//    private String HM_ID;

    @SerializedName("ROOM_N")
    private String ROOM_N;



    public String getROOM_N() {
        return ROOM_N;
    }

    public void setROOM_N(String ROOM_N) {
        this.ROOM_N = ROOM_N;
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
