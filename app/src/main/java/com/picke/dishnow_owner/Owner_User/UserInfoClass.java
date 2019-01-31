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
    private String resphone;
    private String ownername;
    private String starttime;
    private String endtime;
    private String lat;
    private String lon;
    private String ownerphone;
    private String respassword;

    public String getuId(){
        return uid;
    }

    public String getResid() {
        return resid;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
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

    void setuId(String uid){
        this.uid = uid;
    }

    void setResname(String resname){
        this.resname = resname;
    }
    void setResaddress(String resaddress){
        this.resaddress = resaddress;
    }
    void setResphone (String resphone){
        this.resphone = resphone;
    }
    void setOwnername(String ownername){
        this.ownername = ownername;
    }
    void setStarttime(String starttime){
        this.starttime = starttime;
    }
    void setEndtime(String endtime){
        this.endtime = endtime;
    }
    void setLat(String lat){
        this.lat = lat;
    }
    void setLon(String lon){
        this.lon = lon;
    }
    void setOwnerphone(String ownerphone){
        this.ownerphone = ownerphone;
    }
    void setPassword(String password){
        this.respassword = password;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }
}
