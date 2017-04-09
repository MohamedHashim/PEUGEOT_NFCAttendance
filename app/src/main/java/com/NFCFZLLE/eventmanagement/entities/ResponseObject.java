package com.NFCFZLLE.eventmanagement.entities;

/**
 * Created by Muhammad Tahir Ashraf on 09/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class ResponseObject {

    private String errorMessage;
    private String successMessage;
    private User data;
    private Attendees attendee;
    private AppData appData;


    public ResponseObject() {
    }

    public AppData getAppData() {
        return appData;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public Attendees getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendees attendee) {
        this.attendee = attendee;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "errorMessage='" + errorMessage + '\'' +
                ", successMessage='" + successMessage + '\'' +
                ", data=" + data +
                ", attendee=" + attendee +
                '}';
    }
}
