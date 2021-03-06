package com.example.walkrally;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.security.Timestamp;

public class LoginMain extends AppCompatActivity {

    EditText emailId, password;
    Button btnSignIn;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailText);
        password = findViewById(R.id.passText);
        btnSignIn = findViewById(R.id.loginButton);

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
//                if( mFirebaseUser != null ){
//                    Toast.makeText(LoginMain.this,"You are logged in",Toast.LENGTH_SHORT).show();
//                    new User().readData(new User.MyCallback() {
//                        @Override
//                        public void onCallback(User value) {
//                            Intent i;
//                            if(value.event.equals("")){
//                                i = new Intent(LoginMain.this,Event.class);
//                            }else if(value.team.equals("")){
//                                i = new Intent(LoginMain.this,TeamList.class);
//                            }else {
//                                i = new Intent(LoginMain.this,MainActivity.class);
//                            }
//                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(i);
//                        }
//                    });
//
//                }
//                else{
//                    Toast.makeText(LoginMain.this,"Please Login",Toast.LENGTH_SHORT).show();
//                }
//            }
//        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()) {
                    emailId.setError("Fields Are Empty!");
                    emailId.requestFocus();
                    password.setError("");
                }
                else  if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                }else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginMain.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginMain.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(LoginMain.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void onSignUpClick(View v) {
        Intent intSignUp = new Intent(LoginMain.this, Register.class);
        startActivity(intSignUp);
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//    }
}

