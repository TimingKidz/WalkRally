package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.*
import java.util.*
import kotlin.collections.ArrayList


class LeaderboardFlagment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var Tscore: ListView
    lateinit var Yscore: TextView
    var t_list = ArrayList<Team>()
    lateinit var listView: ListView
    var team_path = "Team"
    lateinit var textview: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        Tscore = view?.findViewById(R.id.TScore)
//        Yscore = view?.findViewById(R.id.YScore)
//
//        readData()
//
//
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_leaderboard_flagment, container, false)
//        Tscore = view.findViewById(R.id.TScore)
//        Yscore = view.findViewById(R.id.YScore)
        listView = view.findViewById(R.id.listView)
        textview = view.findViewById(R.id.textview)
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
                    Collections.reverse(t_list)
                    listView.adapter = LeaderAdapter(activity!!.applicationContext,R.layout.custom_learderboard_list,t_list)

                }
            }

        }
        val team_name_list = FirebaseDatabase.getInstance().getReference(team_path).orderByChild("score")
        team_name_list.addListenerForSingleValueEvent(postListener)



    }
}
