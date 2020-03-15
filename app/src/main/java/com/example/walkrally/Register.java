package com.example.walkrally;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText emailId, password,passwordtwo;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailText);
        password = findViewById(R.id.passText);
        passwordtwo = findViewById(R.id.passText2);
        btnSignUp = findViewById(R.id.signupButton);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    public void register(){
        final String email = emailId.getText().toString();

        String pwd = password.getText().toString();
        String pwd2 = passwordtwo.getText().toString();
        if(email.isEmpty()){
            emailId.setError("Please enter email id");
            emailId.requestFocus();
        }
        else if(pwd.length() <6  && pwd2.length() < 6 && pwd.length() > 25 && pwd2.length() > 25){
            password.setError("Your password must be between 6 and 24 characters.");
            password.requestFocus();
        }
        else  if(pwd.isEmpty()){
            password.setError("Please enter your password");
            password.requestFocus();
        }
        else if(pwd2.isEmpty()){
            passwordtwo.setError("Please confirmed your password");
            passwordtwo.requestFocus();
        }
        else if(!(pwd.equals(pwd2))){
            passwordtwo.setError("diff from password");
            passwordtwo.requestFocus();
        }
        else  if(email.isEmpty() && pwd.isEmpty() && pwd2.isEmpty()){
            Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
        }
        else  if(!(email.isEmpty() && pwd.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        emailId.setError("Error: Your email address is invalid.");
                        emailId.requestFocus();
                    }
                    else {

                        User user = new User(
                                mFirebaseAuth.getInstance().getCurrentUser().getUid(),
                                email
                        );
                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Register success", Toast.LENGTH_SHORT).show();
                                } else {
                                    //display a failure message

                                }
                            }
                        });
                        startActivity(new Intent(Register.this,CreateProfile.class));
                    }
                }
            });
        }
        else{
            Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

        }
    }
}
