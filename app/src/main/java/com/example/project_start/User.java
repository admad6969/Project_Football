package com.example.project_start;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.ByteArrayOutputStream;
import java.util.Date;

@IgnoreExtraProperties
public class User
{
    private String uid;//user uid
    private String email,firstname, lastname;
    private Date date;
    private String pic;
    private String preferredClub;

    public User(String uid, String email, String firstname,String lastname, Date date,String pic) {
        this.uid = uid;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.pic = pic;
    }

    public void setPicAsString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        this.pic = Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public Bitmap picToBitmap(){
        byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPreferredClub() {
        return preferredClub;
    }

    public void setPreferredClub(String preferredClub) {
        this.preferredClub = preferredClub;
    }
}
