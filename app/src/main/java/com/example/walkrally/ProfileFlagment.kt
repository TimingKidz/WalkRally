package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_flagment, container, false)
        val  btnLogout = view.findViewById<Button>(R.id.logoutButton)
        btnLogout.setOnClickListener{view ->
            Log.d("testbut","Good")
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity,LoginMain::class.java))
        }
        return view
    }

}
