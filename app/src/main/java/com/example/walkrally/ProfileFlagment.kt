package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFlagment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFlagment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var textViewEmail: TextView
    lateinit var textViewName:TextView
    lateinit var age:TextView
    lateinit var phone:TextView
    lateinit var btnLogout: Button
    lateinit var btnTest:FloatingActionButton
    lateinit var ref: DatabaseReference
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile_flagment, container, false)
        btnLogout = view.findViewById(R.id.logoutButton)

        textViewEmail = view.findViewById(R.id.emailView)
        textViewName = view.findViewById(R.id.nameView)
        age = view.findViewById(R.id.ageView)
        phone = view.findViewById(R.id.phoneView)
        btnTest = view.findViewById(R.id.floatingActionButton3)
        btnTest.setOnClickListener { view ->
            startActivity(Intent(activity,testimage::class.java))
        }
        btnLogout.setOnClickListener{view ->
            logOut()
        }
        readdata()

        return view
    }

    fun logOut() {
        Log.d("testbut","Good")
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(activity,LoginMain::class.java))
    }

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            for(u in dataSnapshot.children) {
                if(u!!.exists()){
                    val user = u.getValue(User::class.java)
                    if(user != null){
                        textViewEmail.setText(user.email)
                        textViewName.setText(user.name)
                        age.setText(user.age)
                        phone.setText(user.telephone)
                    }
                }

            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("failed", "loadPost:onCancelled", databaseError.toException())
            // ...
        }
    }
    fun readdata(){
        ref = FirebaseDatabase.getInstance().getReference("Users")
        val query = FirebaseDatabase.getInstance().getReference("Users")
            .orderByChild("id")
            .equalTo(ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key)
        query.addValueEventListener(postListener)
    }


}
