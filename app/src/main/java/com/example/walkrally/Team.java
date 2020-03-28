package com.example.walkrally;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;

public class Team {
    public String id;
    public int score;
    public String name;
    public String checkp;
    public String event;
    public boolean isFin;
    public String mcount;
    public long time0;
    public long time1;
    public long Ttime;

    public Team(){

    }
    public Team(String id,int score, String name, String mcount,String checkpoint , String event){
        this.id = id;
        this.score = score;
        this.name = name;
        this.mcount = mcount;
        this.event = event;
        this.checkp = checkpoint;
        this.isFin = false;
        this.time0 = 0;
        this.time1=0;
        this.Ttime=0;
    }

    public interface MyCallback {
        void onCallback(Team value);
    }

    public void readData(Team.MyCallback myCallback) {
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
                                        myCallback.onCallback(value);
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
