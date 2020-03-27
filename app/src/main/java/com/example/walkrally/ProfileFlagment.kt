package com.example.walkrally

import a.a.a
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


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


    fun readdata(){
        var a ="kk"
        User().readTeamcp(object : User.MyCallbackk {
            override fun onCallbackk(value: Clues) {
            }
        })


//        ref = FirebaseDatabase.getInstance().getReference("Users")
//        var a = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
//        val query = FirebaseDatabase.getInstance().getReference("Users")
//            .orderByChild("id")
//            .equalTo(ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key)
//        a.addValueEventListener(postListener)
    }


}
