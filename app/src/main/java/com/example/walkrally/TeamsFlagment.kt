package com.example.walkrally

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_teams_flagment.*
import java.lang.reflect.Member


class TeamsFlagment : Fragment() {
 //   var t_list = ArrayList<Team>()
    lateinit var member: Member

    lateinit var ref: DatabaseReference
           val Current_user = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key
    var team_id = FirebaseDatabase.getInstance().getReference().child("Users").child(Current_user!!).child("team")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readdata()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.custom_team_member, container, false)
        member = view.findViewById(R.id.member)
   //     readData()
        return view
    }

    fun readdata(){
        val Listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val id = p0.key.toString()
                val c = p0.childrenCount.toString()
                var user = p0.child("team_id").value.toString()
                Log.d("tt", user)
//                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val id = p0.key.toString()
                val c = p0.childrenCount.toString()
                var user = p0.child("team_id").value.toString()
                Log.d("tt", user)
//                FirebaseDatabase.getInstance().getReference().child(team_Path).child(id).child("mcount").setValue(c)
//                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        }
//        val team_name_list = FirebaseDatabase.getInstance().getReference(team_Path).orderByChild("id")
//        team_name_list.addChildEventListener(postListener)
        val team_list = FirebaseDatabase.getInstance().getReference("TeamMembers").orderByKey()
        Log.d("team_key",team_list.toString())
        team_list.addChildEventListener(Listener)


    }


}
