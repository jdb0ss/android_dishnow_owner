package com.picke.dishnow_owner.Owner_User;

public class UserInfoClass {
    private String uid;
    private String resid;
    private String resname;
    private String resaddress;
    private String resphone;
    private String ownername;
    private String starttime;
    private String endtime;
    public String lat;
    public String lon;
    private String ownerphone;
    private String respassword;

    public String getId(){
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

    void setId(String uid){
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
