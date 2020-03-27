package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList


class Event : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var ref: DatabaseReference
    lateinit var event_list: ArrayList<EventClass>
    lateinit var ES_list:ArrayList<String>
    val event_path = "Events"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        listView = findViewById(R.id.rView)
        ref = FirebaseDatabase.getInstance().getReference(event_path)
        ES_list = arrayListOf()
        event_list = arrayListOf()
        readdata()


    }
    fun join_Event(E_id:String){
    ref = FirebaseDatabase.getInstance().getReference(event_path)
    val check = ref.child(E_id).child("team")
    check.addListenerForSingleValueEvent(object :ValueEventListener{
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            if(dataSnapshot!!.exists()){
                Log.d("child",dataSnapshot.childrenCount.toString())
                if(dataSnapshot.childrenCount < 60){


                    FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).child("event").setValue(E_id)
                    val i = Intent(applicationContext,TeamList::class.java)
                    startActivity(i)
                }else {
                    Toast.makeText(applicationContext,"Team is Full", Toast.LENGTH_SHORT).show()
                }
            }else {
                FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).child("event").setValue(E_id)
                val i = Intent(applicationContext,TeamList::class.java)
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

    fun readdata(){
        val event_count_list = FirebaseDatabase.getInstance().getReference(event_path).orderByKey()
        val countListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//                val team = p0.child("id").value.toString()
//                val event = p0.key.toString()
//
//                val t_idx = event_list.indexOf(event)
////                team_list.set(t_idx,team)
//                event_list.set(t_idx,event)
//
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,event_list)
//                listView.setOnItemClickListener { parent, view, position, id ->
//
//                    join_Event(event_list[position])
//                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val id_event = p0.key.toString()
                var mcount:Int
                mcount = 0
                if(p0.child("mcount").exists()) {
                    mcount = Integer.parseInt(p0.child("mcount").value.toString())
                }
                val eventclass = EventClass(id_event,mcount)
                Log.d("id_event",id_event)
                Log.d("Mcount",mcount.toString())
                event_list.add(eventclass)
                ES_list.add(id_event)
                listView.adapter = EventAdapter(applicationContext,R.layout.custom_event_list,event_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    join_Event(ES_list[position])
                }



            }

            override fun onChildRemoved(p0: DataSnapshot) {
//                val event = p0.childrenCount.toString()
//                event_list.remove(event)
//
//                listView.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,event_list)
//                listView.setOnItemClickListener { parent, view, position, id ->
//
//                    join_Event(event_list[position])
//                }

            }

        }
        event_count_list.addChildEventListener(countListener)
    }
}
