package com.example.walkrally

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
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

        map_view.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            googleMap = it
        }
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
                    drawCircle(location.latitude, location.longitude, 1.0)
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
            .strokeColor(ContextCompat.getColor(context!!, R.color.bottom_nav_item_color))
            .fillColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        circle?.remove() // Remove old circle.
        circle = googleMap?.addCircle(circleOptions) // Draw new circle.
    }

}
