package com.example.walkrally;

import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.Config;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String email;
    public String id;
    public String age;
    public String telephone;
    public String name;
    public String img;
    public String event;
    public String team;

    public User(){

    }
    public User(String id,String email,String name,String telephone,String age,String img){
        this.id = id;
        this.email = email;
        this.name = name;
        this.telephone = telephone;
        this.age = age ;
        this.img = img;

    }

    public User(String event){
        this.event = event;
    }



    public User(String id,String email){
        this.id = id;
        this.email = email;
        this.name = "";
        this.telephone = "";
        this.age = "0";
        this.img = "";
        this.event = "";
        this.team = "";

    }
    public User(String name, String age, String telephone){
        this.name = name;
        this.age = age;
        this.telephone = telephone;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", id);
        result.put("email", email);


        return result;
    }

    public interface MyCallback {
        void onCallback(User value);
    }

    public interface MyCallbackk {
        void onCallbackk(Clues value);
    }
    public void readData(User.MyCallback myCallback) {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User value = dataSnapshot.getValue(User.class);
                        myCallback.onCallback(value);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }


    public void readTeamcp(User.MyCallbackk myCallback) {
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User value = dataSnapshot.getValue(User.class);
                        FirebaseDatabase.getInstance().getReference("Team").child(value.team)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Team value = dataSnapshot.getValue(Team.class);
                                        FirebaseDatabase.getInstance().getReference("Events").child(value.event).child("clues").child(value.checkp)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        String value = dataSnapshot.getValue(String.class);
                                                        FirebaseDatabase.getInstance().getReference("clues").child(value)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        Clues value = dataSnapshot.getValue(Clues.class);

                                                                        Log.d("TAG",value.ans+" "+value.img+" "+value.hint);
                                                                        myCallback.onCallbackk(value);

                                                                    }
                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {}
                                                                });
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {}
                                                });


                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }




}
