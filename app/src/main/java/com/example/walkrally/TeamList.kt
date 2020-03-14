package com.example.walkrally

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_create_team.*
import java.util.ArrayList

class TeamList : AppCompatActivity() {
    var list: MutableList<String> = mutableListOf<String>()
    lateinit var ref: DatabaseReference
    lateinit var createBut:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)
        readdata()
        createBut = findViewById(R.id.btnCreate)
        createBut.setOnClickListener { view ->
            val key = FirebaseDatabase.getInstance().getReference().child("Teams").push().key
            val Cuser = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key
            creat_Team(Cuser!!,"0","",key.toString())
        }
        list_view.adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,list)

    }
    fun creat_Team( id:String,score:String, name:String, member:String){
        val team = Team(id,score,name,member)
        val mDatabase = FirebaseDatabase.getInstance().getReference().child("Teams")
        mDatabase.child(member).setValue(team)
    }

    fun join_Team(){

    }

    fun readdata(){
        ref = FirebaseDatabase.getInstance().getReference("Teams")
        val query = FirebaseDatabase.getInstance().getReference("Teams")
        query.addValueEventListener(postListener)
    }

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            for(u in dataSnapshot.children) {
                Log.d("test",u.toString())
                if(u!!.exists()){
                    val team = u.getValue(Team::class.java)
                    if(team != null){
                        list.add(team.name)
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
}
