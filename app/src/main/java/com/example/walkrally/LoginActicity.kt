package com.example.walkrally

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login_acticity.*

class LoginActicity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_acticity)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        val btn_SignIn = findViewById(R.id.SignIn) as Button
        btn_SignIn.setOnClickListener {
            signIn(LoginEmail.text.toString(), LoginPassword.text.toString())

        }
        val btn_SignUp = findViewById(R.id.SignUp) as Button
        btn_SignUp.setOnClickListener{
            createAccount(LoginEmail.text.toString(), LoginPassword.text.toString())

        }

    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }


    private fun createAccount(email: String, password: String) {
        Log.d("createAccount", "createAccount:$email")
        if (!validateForm()) {
            return
        }
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignUpSuccess", "createUserWithEmail:success")
                    setContentView(R.layout.activity_main)
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignUpFailed", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }

            }
        // [END create_user_with_email]
    }
    private fun signIn(email: String, password: String) {
        Log.d("SignIn", "signIn:$email")
        if (!validateForm()) {
            return
        }

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInSuccess", "signInWithEmail:success")
                    setContentView(R.layout.activity_main)
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignInFailed", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }

                // [START_EXCLUDE]
//                if (!task.isSuccessful) {
//                    status.setText(R.string.auth_failed)
//                }
                //hideProgressBar()
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }
    private fun validateForm(): Boolean {
        var valid = true

        val email = LoginEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            LoginEmail.error = "Required."
            valid = false
        } else {
            LoginEmail.error = null
        }

        val password = LoginPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            LoginPassword.error = "Required."
            valid = false
        } else {
            LoginPassword.error = null
        }

        return valid
    }
}
