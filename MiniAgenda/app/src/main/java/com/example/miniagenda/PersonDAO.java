package com.example.miniagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
   private DbConnection connection;
   private SQLiteDatabase database;
   
   public PersonDAO(Context ctx) {
      connection = new DbConnection(ctx);
   }
   
   public long insertPerson(Person person){
      database = connection.getWritableDatabase();
      ContentValues personMap = new ContentValues();
      personMap.put("name", person.getName());
      personMap.put("email", person.getEmail());
      personMap.put("address", person.getAddress());
      personMap.put("phone", person.getPhone());
      personMap.put("profile_picture", person.getProfilePhoto());
      
      return database.insert(DbConnection.TABLE_NAME, null, personMap);
   }
   
   public List<Person> getAllPersons(){
      database = connection.getReadableDatabase();
      List<Person> persons = new ArrayList<>();
      Cursor cursor = database.query(DbConnection.TABLE_NAME, new String[]{
              "person_id",
                      "name", "email", "address", "phone", "profile_picture" },
              null, null, null, null, null);
      while(cursor.moveToNext()){
         Person p = new Person();
         p.setPerson_id(cursor.getInt(0));
         p.setName(cursor.getString(1));
         p.setEmail(cursor.getString(2));
         p.setAddress(cursor.getString(3));
         p.setPhone(cursor.getString(4));
         p.setProfilePhoto(cursor.getString(5));
         persons.add(p);
      }
      return persons;
   }
   public  void deletePerson(Person person){
      database.delete(DbConnection.TABLE_NAME, "person_id = ?", new String[]{
              person.getPerson_id().toString()
      });
   }
   
   public void updatePerson(Person mPerson) {
      database = connection.getWritableDatabase();
      ContentValues personMap = new ContentValues();
      
      personMap.put("name", mPerson.getName());
      personMap.put("email", mPerson.getEmail());
      personMap.put("address", mPerson.getAddress());
      personMap.put("phone", mPerson.getPhone());
      
//      System.out.println(mPerson.getPerson_id().toString());
      
      database.update(DbConnection.TABLE_NAME, personMap,"person_id = ?",
              new String[]{mPerson.getPerson_id().toString()});
   }
}
