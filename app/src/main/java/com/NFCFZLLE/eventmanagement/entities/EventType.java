package com.NFCFZLLE.eventmanagement.entities;

/**
 * Created by Muhammad Tahir Ashraf on 30/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class EventType {

    String id;
    String event_type_name;
    String company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEvent_type_name() {
        return event_type_name;
    }

    public void setEvent_type_name(String event_type_name) {
        this.event_type_name = event_type_name;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "id='" + id + '\'' +
                ", event_type_name='" + event_type_name + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
