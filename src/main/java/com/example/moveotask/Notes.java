package com.example.moveotask;

import com.google.android.gms.maps.model.LatLng;
import java.sql.Timestamp;

public class Notes {
    private String noteId;
    private String title;
    private String content;
    private String userEmail;
    private Timestamp date;
    private LatLng position;

    public Notes(){

    }

    public Notes(String noteId, String title, String content, String userEmail, LatLng position, Timestamp date) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.userEmail = userEmail;
        this.position = position;
        this.date = date;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


}
