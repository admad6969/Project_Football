package com.example.project_start;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Team {
    private String uid, teamName, manager, captain, logo;
    private League inLeague;
    private boolean accepted;


    public Team()
    {

    }
    public Team(String copyUid, String copyName, String copyCaptain, String copyManager)
    {
        this.uid = copyUid;
        this.manager = copyManager;
        this.captain = copyCaptain;
        this.teamName = copyName;
        this.inLeague = new League();
        this.accepted = false;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean getAccepted(){
        return this.accepted;
    }


    public void setInLeague(League inLeague) {
        this.inLeague = inLeague;
    }

    public League getInLeague() {
        return inLeague;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getCaptain() {
        return captain;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getManager() {
        return manager;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getLogo() {
        return logo;
    }

    public String getUid() {
        return uid;
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
