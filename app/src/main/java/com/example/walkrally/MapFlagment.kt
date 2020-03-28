package com.example.walkrally

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map_flagment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class MapFlagment : Fragment(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var circle: Circle? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MainActivity.REQUEST_CODE_ACCESS_LOCATION
        )
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!);
        map_view.onCreate(savedInstanceState)
        map_view.onResume()
        hint.setVisibility(View.INVISIBLE)
        map_view.getMapAsync(this)
//        Team().readData(object : Team.MyCallback {
//            override fun onCallback(value: Team) {
//                if(!value.isFin){
//                    User().readTeamcp(object : User.MyCallbackk {
//                        override fun onCallbackk(value: Clues) {
//                            if(value.Radius != 0.0){
//                                drawCircle(value.Latitude,value.Longtitude,value.Radius)
//                                hint.setVisibility(View.VISIBLE)
//                            }
//
//                        }
//                    })
//
//                }else{
//
//                }
//
//            }
//        })

    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
            if(!currentdata.t.isFin){
                if(currentdata.c.Radius != 0.0){
                    Log.d("aaaa", currentdata.c.Latitude.toString() +" "+ currentdata.c.Longtitude.toString()+" " + currentdata.c.Radius.toString())
                    Log.d("aaaa", currentdata.t.id.toString() +" "+ currentdata.t.isFin.toString()+" " + currentdata.t.checkp.toString())
                    Log.d("aaaa", currentdata.u.id.toString() +" "+ currentdata.u.email.toString()+" " + currentdata.u.event.toString())
                    drawCircle(currentdata.c.Latitude,currentdata.c.Longtitude,currentdata.c.Radius)
                    hint.setVisibility(View.VISIBLE)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hint.setOnClickListener(View.OnClickListener{ OpenDialog() })



    }

    fun OpenDialog(){

        var hh = hintDialog(currentdata.c.hint)
        hh.show(childFragmentManager, "")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_flagment, container, false)
    }

    /** Override the onRequestPermissionResult to use EasyPermissions */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * enableMyLocation() will enable the location of the map if the user has given permission
     * for the app to access their device location.
     * Android Studio requires an explicit check before setting map.isMyLocationEnabled to true
     * but we are using EasyPermissions to handle it so we can suppress the "MissingPermission"
     * check.
     */
    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(MainActivity.REQUEST_CODE_ACCESS_LOCATION)
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            googleMap.isMyLocationEnabled = true
            fusedLocationProviderClient!!.lastLocation.addOnSuccessListener {
                location: Location? ->
                if (location != null) {
                    val de = CameraPosition.builder()
                        .target(LatLng(location.latitude, location.longitude))
                        .zoom(64f).build()
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(de))
                }
            }
        } else {
            EasyPermissions.requestPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION,
                MainActivity.REQUEST_CODE_ACCESS_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun drawCircle(latitude: Double, longitude: Double, radius: Double) {
        val circleOptions = CircleOptions()
            .center(LatLng(latitude, longitude))
            .radius(radius)
            .strokeWidth(1.0f)
            .strokeColor(Color.parseColor("#2271cce7"))
            .fillColor(Color.parseColor("#2271cce7"))
        circle?.remove() // Remove old circle.
        circle = googleMap?.addCircle(circleOptions) // Draw new circle.
    }

}
