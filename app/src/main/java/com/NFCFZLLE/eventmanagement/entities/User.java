package com.NFCFZLLE.eventmanagement.entities;

import java.util.Arrays;

/**
 * Created by Muhammad Tahir Ashraf on 09/05/2016.
 * Email: tahir90.webdeveloper@gmail.com
 * Phone: +971569709138 , +923214745779
 * Skype: tahir90_webdeveloper
 */
public class User extends ResponseObject {

    private String postal;

    private String state;

    private String pic;

    private String lng;

    private String city;

    private String country;

    private String company;

    private String company_id;

    private String title;

    private String id;

    private String first_name;

    private String bio;

    private String address;

    private String email;

    private String dob;

    private String[] permissions;

    private String last_name;

//    private String created_at;

    private String gender;

    private String lat;

    private String site_id;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostal ()
    {
        return postal;
    }

    public void setPostal (String postal)
    {
        this.postal = postal;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getPic ()
    {
        return pic;
    }

    public void setPic (String pic)
    {
        this.pic = pic;
    }

    public String getLong ()
    {
        return lng;
    }

    public void setLong (String lng)
    {
        this.lng = lng;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String[] getPermissions ()
    {
        return permissions;
    }

    public void setPermissions (String[] permissions)
    {
        this.permissions = permissions;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

 /*   public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }
*/
    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    public String getLat ()
    {
        return lat;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "User{" +
                "postal='" + postal + '\'' +
                ", state='" + state + '\'' +
                ", pic='" + pic + '\'' +
                ", lng=" + lng +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", company_id='" + company_id + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", bio='" + bio + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", permissions=" + Arrays.toString(permissions) +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", site_id='" + site_id + '\'' +
                ", lat=" + lat +
                '}';
    }
}
