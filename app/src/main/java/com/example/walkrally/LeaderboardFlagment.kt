package com.example.walkrally

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class LeaderboardFlagment : Fragment() {
    // TODO: Rename and change types of parameters

    var t_list = ArrayList<Team>()
    lateinit var recyclerView: RecyclerView
    var team_path = "Team"
    lateinit var textview: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_leaderboard_flagment, container, false)

        recyclerView = view.findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        readData()

        return view
    }

    fun readData(){

        val postListener = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    Log.d("nofroe",p0.key)
                    for(data in p0.children){
                        var team = data.getValue(Team::class.java)

                        Log.d("socre",team!!.score.toString())
                        t_list.add(team)

                    }
//                    Collections.reverse(t_list)
                    val s = t_list.sortedWith(compareBy(Team::Ttime))
                    recyclerView.adapter = LeaderAdapter(ArrayList(s))

                }
            }

        }
        val team_name_list = FirebaseDatabase.getInstance().getReference(team_path).orderByChild("event").equalTo(currentdata.t.event.toString())
        Log.d("ss",currentdata.t.event)
        team_name_list.addListenerForSingleValueEvent(postListener)



    }
}
