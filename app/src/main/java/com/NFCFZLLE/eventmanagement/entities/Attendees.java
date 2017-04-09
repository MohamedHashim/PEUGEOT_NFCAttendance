package com.NFCFZLLE.eventmanagement.entities;

import java.io.Serializable;

/**
 * Created by Muhammad Tahir Ashraf on 22/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class Attendees implements Serializable {

    private String qr_code_attendee;

    private String linkedIn;

    private String attendee_type;

    private String phone_number;

    private String ticket_number;

    private String photo;

    private String serial_no;

    private String attendee_showed_up;

    private String first_name;

    private String twitter;

    private String nfc_attendee_id;

    private String title;

    private String email;

    private String event;

    private String event_name;

    private String expiration_date;

    private String company;

    private String company_name;

    private String facebook;

    private String registration_date;

    private String last_name;

    private String beacon_id;

    private String comments;

    private String seat_number;

    private String company_attendee_name;

    private String table_number;


    public String getCompany_attendee_name() {
        return company_attendee_name;
    }

    public void setCompany_attendee_name(String company_attendee_name) {
        this.company_attendee_name = company_attendee_name;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getQr_code_attendee() {
        return qr_code_attendee;
    }

    public void setQr_code_attendee(String qr_code_attendee) {
        this.qr_code_attendee = qr_code_attendee;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getAttendee_type() {
        return attendee_type;
    }

    public void setAttendee_type(String attendee_type) {
        this.attendee_type = attendee_type;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getAttendee_showed_up() {
        return attendee_showed_up;
    }

    public void setAttendee_showed_up(String attendee_showed_up) {
        this.attendee_showed_up = attendee_showed_up;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getNfc_attendee_id() {
        return nfc_attendee_id;
    }

    public void setNfc_attendee_id(String nfc_attendee_id) {
        this.nfc_attendee_id = nfc_attendee_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(String seat_number) {
        this.seat_number = seat_number;
    }

    @Override
    public String toString() {
        return "Attendees{" +
                "qr_code_attendee='" + qr_code_attendee + '\'' +
                ", linkedIn='" + linkedIn + '\'' +
                ", attendee_type='" + attendee_type + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", ticket_number='" + ticket_number + '\'' +
                ", photo='" + photo + '\'' +
                ", serial_no='" + serial_no + '\'' +
                ", attendee_showed_up='" + attendee_showed_up + '\'' +
                ", first_name='" + first_name + '\'' +
                ", twitter='" + twitter + '\'' +
                ", nfc_attendee_id='" + nfc_attendee_id + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", event='" + event + '\'' +
                ", event_name='" + event_name + '\'' +
                ", expiration_date='" + expiration_date + '\'' +
                ", company='" + company + '\'' +
                ", company_name='" + company_name + '\'' +
                ", facebook='" + facebook + '\'' +
                ", registration_date='" + registration_date + '\'' +
                ", last_name='" + last_name + '\'' +
                ", beacon_id='" + beacon_id + '\'' +
                ", comments='" + comments + '\'' +
                ", t_number='" + table_number + '\'' +
                ", c_name='" + company_attendee_name + '\'' +
                ", seat_number='" + seat_number + '\'' +
                '}';
    }

    public String getName() {
        return first_name + " " + last_name;
    }
}