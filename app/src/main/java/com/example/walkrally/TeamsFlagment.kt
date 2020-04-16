package com.example.walkrally

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TeamsFlagment : Fragment() {
    var t_list = ArrayList<User>()
    lateinit var recyclerView: RecyclerView
    var team_path = "TeamMembers"
    lateinit var textview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_teams_flagment, container, false)

        recyclerView = view.findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        readData()

        return view
    }

    fun readData(){

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    Log.d("nofroe",p0.key)
                    for(data in p0.children){
                        var users = data.getValue(User::class.java)

                        Log.d("socre",users!!.name.toString())
                        t_list.add(users)

                    }
//                    Collections.reverse(t_list)
                    recyclerView.adapter = TeamMemberAdapter(t_list)

                }
            }

        }
        val teammember_name_list = FirebaseDatabase.getInstance().getReference(team_path)
        teammember_name_list.addListenerForSingleValueEvent(postListener)



    }
}
