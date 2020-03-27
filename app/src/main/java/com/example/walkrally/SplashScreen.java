package com.example.walkrally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
//                    Toast.makeText(LoginMain.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    new User().readData(new User.MyCallback() {
                        @Override
                        public void onCallback(User value) {
                            Intent i;
                            if(value.name.equals("")){
                                i = new Intent(SplashScreen.this,CreateProfile.class);

                            }else if(value.event.equals("")){
                                i = new Intent(SplashScreen.this,Event.class);
                            }else if(value.team.equals("")){
                                i = new Intent(SplashScreen.this,TeamList.class);
                            }else{
                                i = new Intent(SplashScreen.this,MainActivity.class);
                            }
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });

                }else{
                    Intent i = new Intent(SplashScreen.this,LoginMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
//                  Toast.makeText(LoginMain.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
