package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.Boolean as Boolean1


class TeamList : AppCompatActivity() , TeamCreateDialog.TeamCreateDialoglistener{

    lateinit var ref: DatabaseReference
    lateinit var createBut:Button
    lateinit var listView: ListView
    var t_list = ArrayList<Team>()
    lateinit var team_list:ArrayList<String>
    lateinit var count_list:ArrayList<String>
    val team_Path = "Team"
    val event_path = "Events"
    var joined = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_team)

        listView = findViewById(R.id.listView)

        ref = FirebaseDatabase.getInstance().getReference(team_Path)

        team_list = arrayListOf()
        count_list = arrayListOf()
        readdata()



        createBut = findViewById(R.id.btnCreate)
        createBut.setOnClickListener { view ->
            OpenDialog()
        }


    }

    fun OpenDialog(){
        var hh = TeamCreateDialog()
        hh.show(supportFragmentManager, "")
    }
    fun JoinDialog(TeamId:String){
        var st = TeamJoinDialog()
        st.setTeamId(TeamId)
        st.show(supportFragmentManager,"")
    }


    override fun applyTexts(Teamname: String,isOk: Boolean1) {

        if(isOk) { //is ok go create team
            val key = FirebaseDatabase.getInstance().getReference().child(team_Path).push().key
            User().readData(object : User.MyCallback {
                override fun onCallback(value: User) {
                    creat_Team(key.toString(), 0, Teamname, "1", "1", value.event)
                }
            })

        }
    }


    fun creat_Team( T_id:String,score:Int, name:String, mcount:String, checkpoint:String, event:String){
        val team = Team(T_id, score, name, mcount, checkpoint, event)
        FirebaseDatabase.getInstance().getReference().child(team_Path).child(T_id).setValue(team) // create Team "id" in Team

        FirebaseDatabase.getInstance().getReference("TeamMembers") // create TeamMembers
            .child(T_id).child("1").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
        FirebaseDatabase.getInstance().getReference("Users")//create child "team" in Users
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("team").setValue(T_id)
        val getEvent     = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("event")
        getEvent.addListenerForSingleValueEvent(object :ValueEventListener{ //read data in each Event
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()){
                    Team().readData(object : Team.MyCallback {
                        override fun onCallback(value: Team?) {
                            FirebaseDatabase.getInstance().getReference(event_path) // create Team at Events
                                .child(dataSnapshot.value.toString()).child("team").child(T_id).setValue(value!!.mcount)
                        }

                    })


                    val i = Intent(applicationContext,MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
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

        val check = FirebaseDatabase.getInstance().getReference("TeamMembers").child(T_id) //read TeamMembers
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
                            FirebaseDatabase.getInstance().getReference("Users") // create Team "id" in Users
                                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("team").setValue(T_id)
                            val i = Intent(applicationContext,MainActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(i)

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

        val getEvent     = FirebaseDatabase.getInstance().getReference("Users") // In Users.event
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("event")
        getEvent.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                if(dataSnapshot!!.exists()){
                    Team().readData(object : Team.MyCallback {
                        override fun onCallback(value: Team?) {
                            FirebaseDatabase.getInstance().getReference(event_path) // create Team at Events
                                .child(dataSnapshot.value.toString()).child("team").child(T_id).setValue(value!!.mcount)
                        }

                    })


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
        val postListener = object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val id = p0.child("id").value.toString()
                val name = p0.child("name").value.toString()
                val c = p0.child("mcount").value.toString()
                val t = Team(id, 0, name, c, "", "")
                Log.d("OB",c)
                for (i in 0..t_list.size-1){
                    if(t_list[i].id == id){
                        t_list[i] = t
                        break
                    }
                }
                var t_idx = team_list.indexOf(id)
                team_list.set(t_idx,id)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
                listView.setOnItemClickListener { parent, view, position, id ->

                    if(joined) {
                        join_Team(team_list[position])
                    }
                }

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                val id = p0.child("id").value.toString()
                val name = p0.child("name").value.toString()
                val c = p0.child("mcount").value.toString()
                val t = Team(id, 0, name, c, "", "")
                Log.d("OB",c)
                t_list.add(t)
                team_list.add(id)
                listView.adapter = TeamAdapter(applicationContext,R.layout.custom_team_list,t_list)
                listView.setOnItemClickListener { parent, view, position, id ->
                    JoinDialog(team_list[position])

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


                }

            }

        }
        val countTeamlistener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                var sum = 0
                for(data in p0.children){

                    sum += Integer.parseInt(data.child("mcount").value.toString())
                }
                User().readData(object : User.MyCallback {
                    override fun onCallback(value: User?) {


                        FirebaseDatabase.getInstance().getReference(event_path).child(value!!.event).child("mcount").setValue(sum.toInt())
                        Log.d("sum",sum.toString())
                    }

                })
            }
        }
        User().readData(object : User.MyCallback {
            override fun onCallback(value: User?) {
                val team_name_list = FirebaseDatabase.getInstance().getReference(team_Path).orderByChild("event").equalTo(value!!.event)
                team_name_list.addChildEventListener(postListener)
                val count_team_event = FirebaseDatabase.getInstance().getReference("Team").orderByChild("event").equalTo(value!!.event)
                count_team_event.addValueEventListener(countTeamlistener)
            }

        })
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
        val team_count_list = FirebaseDatabase.getInstance().getReference("TeamMembers").orderByKey()
        team_count_list.addChildEventListener(countListener)




    }


}
