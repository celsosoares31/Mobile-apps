package com.example.miniagenda;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Person implements Serializable {
   
   private String name, phone, address, email, profilePhoto;
   private  Integer person_id;
   
   public Integer getPerson_id() {
      return person_id;
   }
   
   public void setPerson_id(Integer person_id) {
      this.person_id = person_id;
   }
   
   public Person(Integer id, String name, String phone, String address,
                 String email,
                 String photo) {
      this.person_id = id;
      this.name = name;
      this.phone = phone;
      this.address = address;
      this.email = email;
      this.profilePhoto = photo;
   }
   public Person() {
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getPhone() {
      return phone;
   }
   public void setPhone(String phone) {
      this.phone = phone;
   }
   public String getAddress() {
      return address;
   }
   public void setAddress(String address) {
      this.address = address;
   }
   public String getEmail() {
      return email;
   }
   public void setEmail(String email) {
      this.email = email;
   }
   public String getProfilePhoto() {
      return profilePhoto;
   }
   public void setProfilePhoto(String profilePhoto) {
      this.profilePhoto = profilePhoto;
   }
}
