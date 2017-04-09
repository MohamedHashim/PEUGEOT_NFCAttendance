package com.NFCFZLLE.eventmanagement.entities;

/**
 * Created by Muhammad Tahir Ashraf on 30/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class AttendeeType {

    String id;
    String attendees_type;
    String company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendees_type() {
        return attendees_type;
    }

    public void setAttendees_type(String attendees_type) {
        this.attendees_type = attendees_type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "AttendeeType{" +
                "id='" + id + '\'' +
                ", attendees_type='" + attendees_type + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
