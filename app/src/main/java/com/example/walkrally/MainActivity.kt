package com.example.walkrally

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_teams_flagment.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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
                startActivity(Intent(this@MainActivity, AugmentedImageActivity::class.java)) // Switch to TransactionAdd.kt page
//                overridePendingTransition(R.anim.bottom_up, R.anim.nothing) // Setting Transition
                return@OnNavigationItemSelectedListener false
            }
            R.id.item_teams -> {
                println("Teams pressed")
                replaceFragment(TeamsFlagment())
                SetNavigationVisibiltity(true)
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
        leave.setVisibility(View.INVISIBLE)
        Team().readData(object : Team.MyCallback {
            override fun onCallback(value: Team) {
                if(value.isFin){
                    leave.setVisibility(View.VISIBLE)
                    leave.setOnClickListener{view ->
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("event").setValue("")
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("team").setValue("")
                        startActivity(Intent(this@MainActivity, LoginMain::class.java))
                    }
                }else{

                }

            }
        })

        //Bottom Navigation View
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigationView.setSelectedItemId(R.id.item_map)
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
