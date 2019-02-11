package com.picke.dishnow_owner.Owner_User;

import android.content.Context;

public class UserInfoClass {
    private static UserInfoClass userInfoClass=null;
    private UserInfoClass(Context context){}

    public static UserInfoClass getInstance(Context context){
        if(userInfoClass == null){
            userInfoClass = new UserInfoClass(context);
        }
        return userInfoClass;
    }

    private String uid;
    private String resid;
    private String resname;
    private String resaddress;
    private String resadd_num;
    private String resadd_detail;
    private String resphone;
    private String ownername;
    private String starttime;
    private String endtime;
    private String lat;
    private String lon;
    private String ownerphone;
    private String respassword;

    public String getResadd_num(){return resadd_num;}

    public String getResadd_detail(){return resadd_detail;}

    public String getuId(){
        return uid;
    }

    public String getResid() {
        return resid;
    }

    public String getLat() {
        return this.lat;
    }

    public String getLon() {
        return this.lon;
    }

    public String getOwnerphone() {
        return ownerphone;
    }

    public String getPassword() {
        return respassword;
    }

    public String getResname() {
        return resname;
    }

    public String getResaddress() {
        return resaddress;
    }

    public String getResphone() {
        return resphone;
    }

    public String getOwnername() {
        return ownername;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setuId(String uid){
        this.uid = uid;
    }
    public void setResname(String resname){
        this.resname = resname;
    }
    public void setResaddress(String resaddress){
        this.resaddress = resaddress;
    }
    public void setResphone (String resphone){
        this.resphone = resphone;
    }
    public void setOwnername(String ownername){
        this.ownername = ownername;
    }
    public void setStarttime(String starttime){
        this.starttime = starttime;
    }
    public void setEndtime(String endtime){
        this.endtime = endtime;
    }
    public void setLat(String lat){
        this.lat = lat;
    }
    public void setLon(String lon){
        this.lon = lon;
    }
    public void setOwnerphone(String ownerphone){
        this.ownerphone = ownerphone;
    }
    public void setPassword(String password){
        this.respassword = password;
    }
    public void setResadd_num(String resadd_num){this.resadd_num = resadd_num;}
    public void setResadd_detail(String resadd_detail){this.resadd_detail = resadd_detail;}

    public void setResid(String resid) {
        this.resid = resid;
    }
}
