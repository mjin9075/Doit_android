package com.example.doit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DTO_chatList_list {


    @SerializedName("fList")
    @Expose
    public List<DTO_ChatList> fList = null;

    public List<DTO_ChatList> getfList() {
        return fList;
    }

    public void setfList(List<DTO_ChatList> fList) {
            this.fList = fList;
        }
}
