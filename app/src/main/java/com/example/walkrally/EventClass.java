package com.example.walkrally;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventClass {
    public String id_event;
    public int mcount;

    public EventClass(String id_event, int mcount) {
        this.id_event = id_event;
        this.mcount = mcount;
    }

//    public interface MyCallback {
//        void onCallback(int value);
//    }
//
//    public void readData(MyCallback myCallback) {
//        FirebaseDatabase.getInstance().getReference("Events").child(id_event).child("team").orderByKey()
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot data : dataSnapshot.getChildren()){
//                            int sum = 0;
//                            sum += data.getValue(Integer.class );
//                            myCallback.onCallback(sum);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }
}
