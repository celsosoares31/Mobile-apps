package com.example.agenda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PersonAdapter extends BaseAdapter {
   Activity activity;
   List<Person> personList;
   
   public PersonAdapter(Activity activity, List<Person> personList) {
      this.activity = activity;
      this.personList = personList;
   }
   
   @Override
   public int getCount() {
      return personList.size();
   }
   
   @Override
   public Object getItem(int position) {
      return personList.get(position);
   }
   
   @Override
   public long getItemId(int position) {
      return 0;
   }
   
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      @SuppressLint("ViewHolder")
      View v = activity.getLayoutInflater().inflate(R.layout.agenda_item,
              parent, false);
      TextView userName = v.findViewById(R.id.userName);
      TextView userEmail = v.findViewById(R.id.userEmail);
      TextView userAddress = v.findViewById(R.id.userAddress);
      TextView userPhone = v.findViewById(R.id.userPhone);
      
      Person person = personList.get(position);
      
      userName.setText(person.getName());
      userEmail.setText(person.getEmail());
      userAddress.setText(person.getAddress());
      userPhone.setText(person.getPhone());
      
      return v;
   }
}
