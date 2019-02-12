package com.picke.dishnow_owner.Owner_User;

import android.content.Context;

public class UserAuthClass {
    private static UserAuthClass userAuthClass=null;
    public UserAuthClass(Context context){}

    public static UserAuthClass getInstance(Context context){
        if(userAuthClass == null){
                userAuthClass = new UserAuthClass(context);
        }
        return userAuthClass;
    }

    private String uid;
    private String ownerid;
    private String ownerpassword;
    private String owneremail;
    private String ownername;
    private String ownerphone;
    private String ownersex;
    private String ownerbirth;

    public String getUid() {
        return uid;
    }

    public void setOwnersex(String ownersex){ this.ownersex = ownersex;}

    public void setOwnerbirth(String ownerbirth){
        this.ownerbirth = ownerbirth.substring(0,4)+
                "-"+ownerbirth.substring(4,6)+"-"+ownerbirth.substring(6,8);
    }

    public String getOwnersex(){return ownersex;}

    public String getOwnerbirth() {return ownerbirth;}

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnerpassword() {
        return ownerpassword;
    }

    public void setOwnerpassword(String ownerpassword) {
        this.ownerpassword = ownerpassword;
    }

    public String getOwneremail() {
        return owneremail;
    }

    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOwnerphone() {
        return ownerphone;
    }

    public void setOwnerphone(String ownerphone) {
        this.ownerphone = ownerphone;
    }
}
