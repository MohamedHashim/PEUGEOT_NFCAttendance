package com.NFCFZLLE.eventmanagement.entities;

import java.util.List;

/**
 * Created by Muhammad Tahir Ashraf on 30/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class AppData extends ResponseObject {

    List<AttendeeType> attendeetype;
    List<EventType> eventtype;

    public List<AttendeeType> getAttendeetype() {
        return attendeetype;
    }

    public void setAttendeetype(List<AttendeeType> attendeetype) {
        this.attendeetype = attendeetype;
    }

    public List<EventType> getEventtype() {
        return eventtype;
    }

    public void setEventtype(List<EventType> eventtype) {
        this.eventtype = eventtype;
    }

    @Override
    public String toString() {
        return "AppData{" +
                "attendeetype=" + attendeetype +
                ", eventtype=" + eventtype +
                '}';
    }
}
