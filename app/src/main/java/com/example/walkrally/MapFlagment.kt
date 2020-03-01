package com.example.walkrally

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.fragment_map_flagment.*

class MapFlagment : Fragment(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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

}
