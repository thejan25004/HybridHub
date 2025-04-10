package org.example.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int technicianId;
    private String name;
    private String expertise;
    private String facebookLink;
    private String instagramLink;
    private String twitterLink;
    private String photoUrl;

    public Technician() {
    }

    public Technician(int technicianId, String name, String expertise, String facebookLink, String instagramLink, String twitterLink, String photoUrl) {
        this.technicianId = technicianId;
        this.name = name;
        this.expertise = expertise;
        this.facebookLink = facebookLink;
        this.instagramLink = instagramLink;
        this.twitterLink = twitterLink;
        this.photoUrl = photoUrl;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "Technician{" +
                "technicianId=" + technicianId +
                ", name='" + name + '\'' +
                ", expertise='" + expertise + '\'' +
                ", facebookLink='" + facebookLink + '\'' +
                ", instagramLink='" + instagramLink + '\'' +
                ", twitterLink='" + twitterLink + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}



//package org.example.carservice.entity;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//@Entity
//public class Technician {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int technicianId;
//    private String name;
//    private String expertise;
//
//    public Technician() {
//    }
//
//    public Technician(int technicianId, String name, String expertise) {
//        this.technicianId = technicianId;
//        this.name = name;
//        this.expertise = expertise;
//    }
//
//    public int getTechnicianId() {
//        return technicianId;
//    }
//
//    public void setTechnicianId(int technicianId) {
//        this.technicianId = technicianId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getExpertise() {
//        return expertise;
//    }
//
//    public void setExpertise(String expertise) {
//        this.expertise = expertise;
//    }
//
//    @Override
//    public String toString() {
//        return "Technician{" +
//                "technicianId=" + technicianId +
//                ", name='" + name + '\'' +
//                ", expertise='" + expertise + '\'' +
//                '}';
//    }
//}