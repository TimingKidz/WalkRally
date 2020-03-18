package com.example.walkrally

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class Event : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var ref: DatabaseReference
    lateinit var event_list: ArrayList<String>
    lateinit var textView: TextView
    val event_path = "Event_Test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        textView = findViewById(R.id.textView)
        listView = findViewById(R.id.listView)
        ref = FirebaseDatabase.getInstance().getReference(event_path)
        event_list = arrayListOf()
        readdata(textView)


    }
    fun join_Event(E_id:String,textView: TextView){
    ref = FirebaseDatabase.getInstance().getReference(event_path)
    val check = ref.child(E_id).child("team")
    check.addListenerForSingleValueEvent(object :ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            if(dataSnapshot!!.exists()){
                Log.d("child",dataSnapshot.childrenCount.toString())
                if(dataSnapshot.childrenCount < 60){

                    textView.setText(E_id)
                    val updateEvent = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("event").setValue(E_id)
                    val i = Intent(applicationContext,TeamList::class.java)
                    startActivity(i)
                }else {
                    Toast.makeText(applicationContext,"Team is Full", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("failed", "loadPost:onCancelled", databaseError.toException())
            // ...
        }
    })
//        startActivity(Intent(this,TeamList::class.java))
    }

    fun readdata(textView: TextView){
        val event_count_list = FirebaseDatabase.getInstance().getReference(event_path).orderByKey()
        val countListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//                val team = p0.child("id").value.toString()
                val event = p0.key.toString()

//                Log.d("OB",team.toString())
                val t_idx = event_list.indexOf(event)
//                team_list.set(t_idx,team)
                event_list.set(t_idx,event)
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,team_list)
                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,event_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Event(event_list[position],textView)
                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val event = p0.key.toString()
////                Log.d("OB",team.toString())
                event_list.add(event)

                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,event_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Event(event_list[position],textView)

                }

            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val event = p0.childrenCount.toString()
                event_list.remove(event)

                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,event_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Event(event_list[position],textView)
                }

            }

        }
        event_count_list.addChildEventListener(countListener)
    }
}
