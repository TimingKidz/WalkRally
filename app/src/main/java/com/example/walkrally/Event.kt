package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Event : AppCompatActivity() ,EventJoinDialog.EventJoinDialoglistener{
    lateinit var listView: ListView
    lateinit var ref: DatabaseReference
    lateinit var event_list: ArrayList<EventClass>
    lateinit var ES_list:ArrayList<String>
    val event_path = "Events"
    var position_t = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        listView = findViewById(R.id.rView)
        ref = FirebaseDatabase.getInstance().getReference(event_path)
        ES_list = arrayListOf()
        event_list = arrayListOf()
        readdata()
    }

    fun Joindialog(){
        var st = EventJoinDialog()
        st.show(supportFragmentManager,"")
    }

    override fun senddata(isJoin: Boolean?) {
        if(isJoin!!){
            readdataT(object : MyCallback {
                override fun onCallback(value: ArrayList<String>) {
                    join_Event(value[position_t])
                }
            })
            Log.d("pos",position_t.toString())
        }
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


    interface MyCallback {
        fun onCallback(value: ArrayList<String>)
    }


    fun readdata(){
        Log.d("inner read","in")
        val event_count_list = FirebaseDatabase.getInstance().getReference(event_path).orderByKey()
        val countListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {


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
                    position_t = position
                    Log.d("canPress","Press")
                    Joindialog()
//                    join_Event(ES_list[position])
                }



            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        }
        event_count_list.addChildEventListener(countListener)
    }

    fun readdataT(myCallback:MyCallback){
        Log.d("inner read","in")
        val event_count_list = FirebaseDatabase.getInstance().getReference(event_path).orderByKey()
        val countListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {


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
                myCallback.onCallback(ES_list)



            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        }
        event_count_list.addChildEventListener(countListener)
    }

}
