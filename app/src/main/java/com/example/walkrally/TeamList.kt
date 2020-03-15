package com.example.walkrally

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_create_team.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)
        listView = findViewById(R.id.listView)
        listViewC = findViewById(R.id.listView2)
        ref = FirebaseDatabase.getInstance().getReference("Teams")

        team_list = arrayListOf()
        count_list = arrayListOf()
        readdata()

        logoutBut = findViewById(R.id.btnLogout)
        logoutBut.setOnClickListener { view ->
            logOut()
        }
        createBut = findViewById(R.id.btnCreate)
        createBut.setOnClickListener { view ->
            openDialog()
            val key = FirebaseDatabase.getInstance().getReference().child("Teams").push().key
            val Cuser = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key
            creat_Team(key.toString(),"0","test",Cuser!!)
        }
        joinBut = findViewById(R.id.btnJoin)
        joinBut.setOnClickListener { view ->

        }


    }

    private fun openDialog() {
        val teamCreateDialog = TeamCreateDialog()
        teamCreateDialog.show(supportFragmentManager, "example dialog")
    }

    fun creat_Team( id:String,score:String, name:String, member:String){
        val team = Team(id, score, name)

        val mDatabase = FirebaseDatabase.getInstance().getReference().child("Teams")
        mDatabase.child(id).setValue(team)
        ref = FirebaseDatabase.getInstance().getReference("Teams")
        val query = FirebaseDatabase.getInstance().getReference("Teams")
            .child(id).child("member").child(ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key.toString()).setValue(1)
    }

    fun join_Team(id:String){


        ref = FirebaseDatabase.getInstance().getReference("Teams")
        val check = ref.child(id).child("member")
        check.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()){
                    Log.d("child",dataSnapshot.childrenCount.toString())
                    if(dataSnapshot.childrenCount < 3){
                        val query = FirebaseDatabase.getInstance().getReference("Teams")
                            .child(id).child("member").child(ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key.toString()).setValue(1)
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
                val cMember = p0.child("member").childrenCount.toString()
                Log.d("OB",team.toString())
                val t_idx = team_list.indexOf(team)
                team_list.set(t_idx,team)
                count_list.set(t_idx,cMember)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)
                listView.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(applicationContext,team_list[position],Toast.LENGTH_SHORT).show()
                    join_Team(team_list[position])
                }
                listViewC.setOnItemClickListener { parent, view, position, id ->
                    join_Team(team_list[position])
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val team = p0.child("id").value.toString()
                val cMember = p0.child("member").childrenCount.toString()
                Log.d("OB",team.toString())
                team_list.add(team)
                count_list.add(cMember)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)
                listView.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(applicationContext,team_list[position],Toast.LENGTH_SHORT).show()
                    join_Team(team_list[position])
                }
                listViewC.setOnItemClickListener { parent, view, position, id ->
                    join_Team(team_list[position])
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

                val team = p0.child("id").value.toString()
                val cMember = p0.child("member").childrenCount.toString()
                Log.d("OB",team.toString())
                team_list.remove(team)
                count_list.remove(cMember)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listViewC.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,count_list)
                listView.setOnItemClickListener { parent, view, position, id ->
                    Toast.makeText(applicationContext,team_list[position],Toast.LENGTH_SHORT).show()
                    join_Team(team_list[position])
                }
                listViewC.setOnItemClickListener { parent, view, position, id ->
                    join_Team(team_list[position])
                }
            }

        }
        val query = FirebaseDatabase.getInstance().getReference("Teams").orderByChild("id")
        query.addChildEventListener(postListener)

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
