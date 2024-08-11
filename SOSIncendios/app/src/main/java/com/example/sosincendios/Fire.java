package com.example.sosincendios;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.UUID;

public class Fire implements Parcelable {
   private String fire_id, city, place, areaClass, detectedBy,
           dateNoticed, dateFinished, fightingStrategy, reason, affectedArea, diedAnimal, details, fireReportedBy, fireReportDay;
   private List<String> images;
   
   public Fire() {
      this.fire_id = String.valueOf(UUID.randomUUID());
   }
   
   protected Fire(Parcel in) {
      fire_id = in.readString();
      city = in.readString();
      place = in.readString();
      areaClass = in.readString();
      detectedBy = in.readString();
      dateNoticed = in.readString();
      dateFinished = in.readString();
      fightingStrategy = in.readString();
      reason = in.readString();
      affectedArea = in.readString();
      diedAnimal = in.readString();
      details = in.readString();
      fireReportedBy = in.readString();
      fireReportDay = in.readString();
      images = in.createStringArrayList();
   }
   
   public static final Creator<Fire> CREATOR = new Creator<Fire>() {
      @Override
      public Fire createFromParcel(Parcel in) {
         return new Fire(in);
      }
      
      @Override
      public Fire[] newArray(int size) {
         return new Fire[size];
      }
   };
   
   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(fire_id);
      dest.writeString(city);
      dest.writeString(place);
      dest.writeString(areaClass);
      dest.writeString(detectedBy);
      dest.writeString(dateNoticed);
      dest.writeString(dateFinished);
      dest.writeString(fightingStrategy);
      dest.writeString(reason);
      dest.writeString(affectedArea);
      dest.writeString(diedAnimal);
      dest.writeString(details);
      dest.writeString(fireReportedBy);
      dest.writeString(fireReportDay);
      dest.writeStringList(images);
   }
   
   @Override
   public int describeContents() {
      return 0;
   }
   
   // Getter and setter methods
   
   public String getFire_id() {
      return fire_id;
   }
   
   public void setFire_id(String fire_id) {
      this.fire_id = fire_id;
   }
   
   public List<String> getImages() {
      return images;
   }
   
   public void setImages(List<String> images) {
      this.images = images;
   }
   
   public String getCity() {
      return city;
   }
   
   public void setCity(String city) {
      this.city = city;
   }
   
   public String getPlace() {
      return place;
   }
   
   public void setPlace(String place) {
      this.place = place;
   }
   
   public String getAreaClass() {
      return areaClass;
   }
   
   public void setAreaClass(String areaClass) {
      this.areaClass = areaClass;
   }
   
   public String getDetectedBy() {
      return detectedBy;
   }
   
   public void setDetectedBy(String detectedBy) {
      this.detectedBy = detectedBy;
   }
   
   public String getDateNoticed() {
      return dateNoticed;
   }
   
   public void setDateNoticed(String dateNoticed) {
      this.dateNoticed = dateNoticed;
   }
   
   public String getDateFinished() {
      return dateFinished;
   }
   
   public void setDateFinished(String dateFinished) {
      this.dateFinished = dateFinished;
   }
   
   public String getFightingStrategy() {
      return fightingStrategy;
   }
   
   public void setFightingStrategy(String fightingStrategy) {
      this.fightingStrategy = fightingStrategy;
   }
   
   public String getReason() {
      return reason;
   }
   
   public void setReason(String reason) {
      this.reason = reason;
   }
   
   public String getAffectedArea() {
      return affectedArea;
   }
   
   public void setAffectedArea(String affectedArea) {
      this.affectedArea = affectedArea;
   }
   
   public String getDiedAnimal() {
      return diedAnimal;
   }
   
   public void setDiedAnimal(String diedAnimal) {
      this.diedAnimal = diedAnimal;
   }
   
   public String getDetails() {
      return details;
   }
   
   public void setDetails(String details) {
      this.details = details;
   }
   
   public String getFireReportedBy() {
      return fireReportedBy;
   }
   
   public void setFireReportedBy(String fireReportedBy) {
      this.fireReportedBy = fireReportedBy;
   }
   
   public String getFireReportDay() {
      return fireReportDay;
   }
   
   public void setFireReportDay(String fireReportDay) {
      this.fireReportDay = fireReportDay;
   }
}
