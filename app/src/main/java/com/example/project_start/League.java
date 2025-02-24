package com.example.project_start;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class League {
    private String uid, LeagueName, description, logo;
    private int capacity, advancers, relegation;
    private boolean Started;

    public League()
    {

    }

    public League(String copyUid, String copyName, String copyDescription, int copyCap, int copyAdvance, int copyRelegated)
    {
        this.uid = copyUid;
        this.description = copyDescription;
        this.LeagueName = copyName;
        this.capacity = copyCap;
        this.advancers = copyAdvance;
        this.relegation = copyRelegated;
        this.Started = false;
    }

    public void setAdvancers(int advancers) {
        this.advancers = advancers;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLeagueName(String leagueName) {
        LeagueName = leagueName;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setRelegation(int relegation) {
        this.relegation = relegation;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAdvancers() {
        return advancers;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRelegation() {
        return relegation;
    }

    public String getDescription() {
        return description;
    }

    public String getLeagueName() {
        return LeagueName;
    }

    public String getLogo() {
        return logo;
    }

    public String getUid() {
        return uid;
    }

    public boolean getStarted() {
        return Started;
    }

    public void setStarted(boolean started) {
        Started = started;
    }

    public void setPicAsString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        this.logo = Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public Bitmap picToBitmap(){
        byte[] decodedString = Base64.decode(logo, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
