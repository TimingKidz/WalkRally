package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // Show/Hide Nav Bar
    public fun SetNavigationVisibiltity(b: Boolean) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        if (b) {
            bottomNavigationView.setVisibility(View.VISIBLE)
        } else {
            bottomNavigationView.setVisibility(View.GONE)
        }
    }


    // Nav Bar Item Action (Use Fragment and Activity)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.item_map -> {
                println("Map pressed")
                replaceFragment(MapFlagment())
                SetNavigationVisibiltity(true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_leaderboard -> {
                println("Leaderboard pressed")
                replaceFragment(LeaderboardFlagment())
                SetNavigationVisibiltity(true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_qr -> {
                if(currentdata.t.isFin){
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("event").setValue("")
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("team").setValue("")
                    startActivity(Intent(this@MainActivity, SplashScreen::class.java))
                }else{
                    startActivity(Intent(this@MainActivity, AugmentedImageActivity::class.java)) // Switch to TransactionAdd.kt page
//                overridePendingTransition(R.anim.bottom_up, R.anim.nothing) // Setting Transition
                    return@OnNavigationItemSelectedListener false
                }

            }
            R.id.item_teams -> {
                println("Teams pressed")
                currentdata.t_list.clear()
                val postListener = object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        var idlist = ArrayList<String>()
                        if(p0!!.exists()){
                            for( x in 1 until 4){
                                if(p0.child(x.toString()).value == null){
                                    break
                                }
                                idlist.add(p0.child(x.toString()).value.toString())

                            }
                            for(data in idlist){
                                FirebaseDatabase.getInstance().getReference("Users").child(data)
                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                val value = dataSnapshot.getValue(User::class.java)
                                                Log.d("socre",value!!.name.toString())
                                                currentdata.t_list.add(value!!)
                                                replaceFragment(TeamsFlagment())
                                                SetNavigationVisibiltity(true)

                                            }
                                            override fun onCancelled(databaseError: DatabaseError) {}
                                        })
                            }
//                    Collections.reverse(t_list)
                        }
                    }

                }
                val teammember_name_list = FirebaseDatabase.getInstance().getReference("TeamMembers").child(currentdata.u.team)
                teammember_name_list.addListenerForSingleValueEvent(postListener)
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_profile -> {
                println("Profile pressed")
                replaceFragment(ProfileFlagment())
                SetNavigationVisibiltity(true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    // Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

                if(currentdata.t.isFin){
                    val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
                    bottomNavigationView.menu.findItem(R.id.item_qr).setTitle("Leave")
                    bottomNavigationView.menu.findItem(R.id.item_qr).setIcon(R.drawable.ic_arrow_forward_black_24dp)
                }




        //Bottom Navigation View
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.setSelectedItemId(R.id.item_map)
        ServerValue.TIMESTAMP
    }


    // Custom Toolbar
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.toolbar, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    // Fragment
    private fun addFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    companion object {
        const val REQUEST_CODE_ACCESS_LOCATION = 123
    }
}
