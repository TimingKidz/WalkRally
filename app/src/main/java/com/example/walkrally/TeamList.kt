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

    lateinit var ref: DatabaseReference
    lateinit var createBut:Button
    lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)
        listView = findViewById(R.id.listView)
        ref = FirebaseDatabase.getInstance().getReference("Teams")
        readdata()


        createBut = findViewById(R.id.btnCreate)
        createBut.setOnClickListener { view ->
            val key = FirebaseDatabase.getInstance().getReference().child("Teams").push().key
            val Cuser = ref.child(FirebaseAuth.getInstance().currentUser!!.uid).key
            creat_Team(key.toString(),"0","test",Cuser!!)
        }


    }
    fun creat_Team( id:String,score:String, name:String, member:String){
        val team = Team(id,score,name,member)
        val mDatabase = FirebaseDatabase.getInstance().getReference().child("Teams")
        mDatabase.child(id).setValue(team)
    }

    fun join_Team(){

    }

    fun readdata(){

        val query = FirebaseDatabase.getInstance().getReference("Teams").orderByChild("id")
        query.addValueEventListener(postListener)
    }

    val postListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onDataChange(p0: DataSnapshot) {
            var team_list:ArrayList<String>
            team_list = arrayListOf()
            if(p0!!.exists()){

                for(t in p0.children){

                    val team = t.getValue(Team::class.java)
                    team_list.add(team!!.id)
                }
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)

            }
        }

    }
}
