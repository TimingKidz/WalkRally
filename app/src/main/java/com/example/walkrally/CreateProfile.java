package com.example.walkrally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class CreateProfile extends AppCompatActivity {
    EditText nameText,ageText,phoneText;
    Button btnNext;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        nameText = findViewById(R.id.nameView);
        ageText = findViewById(R.id.ageView);
        phoneText = findViewById(R.id.phoneView);
        btnNext = findViewById(R.id.btnNext);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readData();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String name = nameText.getText().toString();
                    final String age = ageText.getText().toString();
                    final String phone = phoneText.getText().toString();
                    Log.d("test",name);
                    Log.d("test",age);
                    Log.d("test",phone);
                    writeData(name,age,phone);
                    Intent i = new Intent(CreateProfile.this, SplashScreen.class);
                    startActivity(i);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void readData(){

        String uid = FirebaseAuth.getInstance().getUid().toString();
        FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("id")
                .equalTo(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot d :dataSnapshot.getChildren()){
                            Log.d("test",d.getValue().toString());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void writeData(String name, String age, String phone){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rootRef.child("Users").child(uid).child("name").setValue(name);
        rootRef.child("Users").child(uid).child("age").setValue(age);
        rootRef.child("Users").child(uid).child("telephone").setValue(phone);
    }


}
