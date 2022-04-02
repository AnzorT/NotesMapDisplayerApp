package com.example.moveotask;

import com.google.android.gms.maps.model.LatLng;
import java.time.ZonedDateTime;

/**
 * <p>
 *     Author: Anzor Torikashvili.
 *     <br>
 *     This class describes Note.
 * </p>
 */
public class Note {
    private String noteId;
    private String title;
    private String content;
    private String userEmail;
    private ZonedDateTime date;
    private LatLng position;

    /**
     * Note's default constructor
     */
    public Note(){

    }

    /**
     * Note's constructor.
     * @param noteId id of the note.
     * @param title title of the note.
     * @param content content of the note.
     * @param userEmail email of the user who created the note.
     * @param position current location of the device on the map where the note was created.
     * @param date date to present on the Note.
     */
    public Note(String noteId, String title, String content, String userEmail, LatLng position, ZonedDateTime date) {
        this.noteId = noteId;
        this.title = title;
        this.content = content;
        this.userEmail = userEmail;
        this.position = position;
        this.date = date;
    }

    /**
     * Return String type.
     * @return id of the note.
     */
    public String getNoteId() {
        return noteId;
    }

    /**
     * set noteId field to the value this function receives.
     * @param noteId the value we will input into noteId.
     */
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    /**
     * return String type.
     * @return title of the note.
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title field to the value this function receives.
     * @param title the value we will input into title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return String type.
     * @return content of the note.
     */
    public String getContent() {
        return content;
    }

    /**
     * set content field to the value this function receives.
     * @param content the value we will input into content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Return String type.
     * @return email of the user who created this note.
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * set userEmail field to the value this function receives.
     * @param userEmail the value we will input into userEmail.
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Return LatLng type.
     * @return position which contains values where the note was created.
     */
    public LatLng getPosition() {
        return position;
    }

    /**
     * set position field to the value this function receives.
     * @param position the value we will input into position.
     */
    public void setPosition(LatLng position) {
        this.position = position;
    }

    /**
     * Return ZoneDateTime type.
     * @return the date the note was created.
     */
    public ZonedDateTime getDate() {
        return date;
    }

    /**
     * set date field to the value this function receives.
     * @param date the value we will input into date.
     */
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
}
