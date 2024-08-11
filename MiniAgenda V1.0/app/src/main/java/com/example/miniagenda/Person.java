package com.example.miniagenda;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.UUID;

public class Person implements Serializable{
   
   private String name, phone, address, email, profilePhoto;
   
   public void setPerson_id(String person_id) {
      this.person_id = person_id;
   }
   
   private String person_id;
   public String getPerson_id() {
      return person_id;
   }

   public Person() {
      person_id = UUID.randomUUID().toString();
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
