package com.example.walkrally

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class TeamList : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var createBut:Button
    lateinit var joinBut:Button
    lateinit var logoutBut:Button
    lateinit var listView: ListView
    lateinit var listViewC: ListView
    lateinit var team_list:ArrayList<String>
    lateinit var count_list:ArrayList<String>
    val team_Path = "Team"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)
        listView = findViewById(R.id.listView)
        listViewC = findViewById(R.id.listView2)
        ref = FirebaseDatabase.getInstance().getReference(team_Path)

        team_list = arrayListOf()
        count_list = arrayListOf()
        readdata()

        logoutBut = findViewById(R.id.btnLogout)
        logoutBut.setOnClickListener { view ->
            logOut()
        }
        createBut = findViewById(R.id.btnCreate)
        createBut.setOnClickListener { view ->
            val key = FirebaseDatabase.getInstance().getReference().child(team_Path).push().key
            val Current_user = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key
            creat_Team(key.toString(),"0","test",Current_user!!)
        }
        joinBut = findViewById(R.id.btnJoin)
        joinBut.setOnClickListener { view ->

        }


    }
    fun creat_Team( T_id:String,score:String, name:String, member:String){
        val team = Team(T_id, score, name)

        val mDatabase = FirebaseDatabase.getInstance().getReference().child(team_Path)
        mDatabase.child(T_id).setValue(team)
        val query = FirebaseDatabase.getInstance().getReference("TeamMembers")
            .child(T_id).child("1").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun join_Team(T_id:String){


        ref = FirebaseDatabase.getInstance().getReference("TeamMembers")
        val check = ref.child(T_id)
        check.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()){
                    Log.d("child",dataSnapshot.childrenCount.toString())
                    if(dataSnapshot.childrenCount < 3){
                        var check_id_exists = false
                        for(data in dataSnapshot.children){
                            if(data.value.toString().equals(FirebaseAuth.getInstance().currentUser!!.uid)){
                                Toast.makeText(applicationContext,"You are already in team.",Toast.LENGTH_SHORT).show()
                                check_id_exists = true
                                break
                            }
                        }
                        if(!check_id_exists){
                            val query = FirebaseDatabase.getInstance().getReference("TeamMembers")
                                .child(T_id).child((dataSnapshot.childrenCount + 1).toString()).setValue(FirebaseAuth.getInstance().currentUser!!.uid)

                        }

                    }else {
                        Toast.makeText(applicationContext,"Team is Full",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("failed", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })


    }



    fun logOut() {
        Log.d("testbut","Good")
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginMain::class.java))
    }

    fun readdata(){
        val postListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val team = p0.child("id").value.toString()
//                val cMember = p0.child("member").childrenCount.toString()
                Log.d("OB",team.toString())
                val t_idx = team_list.indexOf(team)
                team_list.set(t_idx,team)
//                count_list.set(t_idx,cMember)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
//                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Team(team_list[position])
                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val team = p0.child("id").value.toString()
//                val cMember = p0.child("member").childrenCount.toString()
                Log.d("OB",team.toString())
                team_list.add(team)
//                count_list.add(cMember)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
//                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Team(team_list[position])
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {

                val team = p0.child("id").value.toString()
//                val cMember = p0.child("member").childrenCount.toString()
                Log.d("OB",team.toString())
                team_list.remove(team)
//                count_list.remove(cMember)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
//                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Team(team_list[position])
                }

            }

        }
        val countListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//                val team = p0.child("id").value.toString()
                val team = p0.key.toString()
                val cMember = p0.childrenCount.toString()
//                Log.d("OB",team.toString())
                val t_idx = team_list.indexOf(team)
//                team_list.set(t_idx,team)
                count_list.set(t_idx,cMember)
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val cMember = p0.childrenCount.toString()
////                Log.d("OB",team.toString())
                count_list.add(cMember)
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val cMember = p0.childrenCount.toString()
                count_list.remove(cMember)
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)

            }

        }
        val team_name_list = FirebaseDatabase.getInstance().getReference(team_Path).orderByChild("id")
        team_name_list.addChildEventListener(postListener)
        val team_count_list = FirebaseDatabase.getInstance().getReference("TeamMembers").orderByKey()
        Log.d("team_Count_key",team_count_list.toString())
        team_count_list.addChildEventListener(countListener)
        

    }

//    val postListener = object : ValueEventListener {
//        override fun onCancelled(p0: DatabaseError) {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }
//
//        override fun onDataChange(p0: DataSnapshot) {
//            var team_list:ArrayList<String>
//            team_list = arrayListOf()
//            if(p0!!.exists()){
//
//                for(t in p0.children){
//
////                    val team = t.getValue(Team::class.java)!!.toString()
////                    team_list.add(team)
////                    val teamid = t.child()
//                    Log.d("OB",teamid)
//                }
////                Log.d("OB",p0.getValue(Team::class.java)!!.id.toString())
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
//                listView.setOnItemClickListener { parent, view, position, id ->
//                    Toast.makeText(applicationContext,team_list[position],Toast.LENGTH_SHORT).show()
//                    join_Team(team_list[position])
//                }
//            }
//        }
//
//    }
}
