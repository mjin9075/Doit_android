package com.example.doit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DTO_facilityList {

//    @SerializedName("fList")
//    public List<fInfo> fList = new ArrayList();
//
//    public List<fInfo> getfList() {
//        return fList;
//    }
//
//    public void setfList(List<fInfo> fList) {
//        this.fList = fList;
//    }
//
//
//
//    public class fInfo {
//
//        @SerializedName("HM_ID")
//        private String HM_ID;
//
//        @SerializedName("facility_image")
//        private String facility_image;
//
//        @SerializedName("facility_category")
//        private String facility_category;
//
//        @SerializedName("facility_name")
//        private String facility_name;
//
//        @SerializedName("facility_business_hours")
//        private String facility_business_hours;
//
//        @SerializedName("facility_convenience")
//        private String facility_convenience;
//
//        @SerializedName("facility_etc")
//        private String facility_etc;
//
//        @SerializedName("facility_intro")
//        private String facility_intro;
//
//        @SerializedName("facility_address")
//        private String facility_address;
//
//        @SerializedName("facility_address_detail")
//        private String facility_address_detail;
//
//
//        public String getHM_ID() {
//            return HM_ID;
//        }
//
//        public void setHM_ID(String HM_ID) {
//            this.HM_ID = HM_ID;
//        }
//
//        public String getFacility_image() {
//            return facility_image;
//        }
//
//        public void setFacility_image(String facility_image) {
//            this.facility_image = facility_image;
//        }
//
//        public String getFacility_category() {
//            return facility_category;
//        }
//
//        public void setFacility_category(String facility_category) {
//            this.facility_category = facility_category;
//        }
//
//        public String getFacility_name() {
//            return facility_name;
//        }
//
//        public void setFacility_name(String facility_name) {
//            this.facility_name = facility_name;
//        }
//
//        public String getFacility_business_hours() {
//            return facility_business_hours;
//        }
//
//        public void setFacility_business_hours(String facility_business_hours) {
//            this.facility_business_hours = facility_business_hours;
//        }
//
//        public String getFacility_convenience() {
//            return facility_convenience;
//        }
//
//        public void setFacility_convenience(String facility_convenience) {
//            this.facility_convenience = facility_convenience;
//        }
//
//        public String getFacility_etc() {
//            return facility_etc;
//        }
//
//        public void setFacility_etc(String facility_etc) {
//            this.facility_etc = facility_etc;
//        }
//
//        public String getFacility_intro() {
//            return facility_intro;
//        }
//
//        public void setFacility_intro(String facility_intro) {
//            this.facility_intro = facility_intro;
//        }
//
//        public String getFacility_address() {
//            return facility_address;
//        }
//
//        public void setFacility_address(String facility_address) {
//            this.facility_address = facility_address;
//        }
//
//        public String getFacility_address_detail() {
//            return facility_address_detail;
//        }
//
//        public void setFacility_address_detail(String facility_address_detail) {
//            this.facility_address_detail = facility_address_detail;
//        }
//
//    }

    //    private Data data;
//
//    public Data getData() {
//        return data;
//    }
//
//    public void setData(Data data) {
//        this.data = data;
//    }

//    public class Data {
//
//        @SerializedName("fList")
//        @Expose
//        public List<DTO_HmFacility> fList = null;
//
//        public List<DTO_HmFacility> getfList() {
//            return fList;
//        }
//
//        public void setfList(List<DTO_HmFacility> fList) {
//            this.fList = fList;
//        }
//
//    }



    @SerializedName("fList")
    @Expose
    public List<DTO_HmFacility> fList = null;

    public List<DTO_HmFacility> getfList() {
        return fList;
    }

    public void setfList(List<DTO_HmFacility> fList) {
            this.fList = fList;
        }
}
