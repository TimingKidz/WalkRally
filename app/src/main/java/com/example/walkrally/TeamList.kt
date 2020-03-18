package com.example.walkrally

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class TeamList : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    lateinit var createBut:Button
    lateinit var joinBut:Button
    lateinit var logoutBut:Button
    lateinit var listView: ListView
    lateinit var listViewC: ListView
    var t_list = ArrayList<Team>()
    lateinit var team_list:ArrayList<String>
    lateinit var count_list:ArrayList<String>
    val team_Path = "Team"
    val event_path = "Event_Test"


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
            creat_Team(key.toString(),"0","test","1","1","")
        }
        joinBut = findViewById(R.id.btnJoin)
        joinBut.setOnClickListener { view ->

        }


    }
    fun creat_Team( T_id:String,score:String, name:String, mcount:String, checkpoint:String, event:String){
        val team = Team(T_id, score, name, mcount, checkpoint, event)

        val mDatabase = FirebaseDatabase.getInstance().getReference().child(team_Path)
        mDatabase.child(T_id).setValue(team)
        FirebaseDatabase.getInstance().getReference("TeamMembers")
            .child(T_id).child("1").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
        val in_team = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("team").setValue(T_id)
        ref = FirebaseDatabase.getInstance().getReference("Users")
        val getEvent     = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).child("event")
        getEvent.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()){
                    Log.d("event",dataSnapshot.value.toString())
                    val in_Event = FirebaseDatabase.getInstance().getReference(event_path)
                        .child(dataSnapshot.value.toString()).child("team").child(T_id).setValue(T_id)

                    val i = Intent(applicationContext,MainActivity::class.java)
                    startActivity(i)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("failed", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    fun join_Team(T_id:String){

        ref = FirebaseDatabase.getInstance().getReference("TeamMembers")
        val in_team = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("team").setValue(T_id)
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
        ref = FirebaseDatabase.getInstance().getReference("Users")
        val getEvent     = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).child("event")
        getEvent.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()){
                    val in_Event = FirebaseDatabase.getInstance().getReference(event_path)
                        .child(dataSnapshot.value.toString()).child("team").child(T_id).setValue(T_id)
                    val i = Intent(applicationContext,MainActivity::class.java)
                    startActivity(i)

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
                val id = p0.child("id").value.toString()
                val name = p0.child("name").value.toString()
                val c = p0.child("mcount").value.toString()
                val t = Team(id, "", name, c, "", "")
                Log.d("OB",c)
                for (i in 0..t_list.size){
                    if(t_list[i].id == id){
                        t_list[i] = t
                    }
                }
                var t_idx = team_list.indexOf(id)
                team_list.set(t_idx,id)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Team(team_list[position])
                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val id = p0.child("id").value.toString()
                val name = p0.child("name").value.toString()
                val c = p0.child("mcount").value.toString()
                val t = Team(id, "", name, c, "", "")
                Log.d("OB",c)
                t_list.add(t)
                team_list.add(id)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Team(team_list[position])
                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {

                val id = p0.child("id").value.toString()
                for (i in 0..t_list.size){
                    if(t_list[i].id == id){
                        t_list.removeAt(i)
                    }
                }
                team_list.remove(id)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
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
                val id = p0.key.toString()
                val c = p0.childrenCount.toString()
                FirebaseDatabase.getInstance().getReference().child(team_Path).child(id).child("mcount").setValue(c)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val id = p0.key.toString()
                val c = p0.childrenCount.toString()
                FirebaseDatabase.getInstance().getReference().child(team_Path).child(id).child("mcount").setValue(c)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)

            }

            override fun onChildRemoved(p0: DataSnapshot) {

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
