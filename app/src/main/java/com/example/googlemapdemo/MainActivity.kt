package com.example.googlemapdemo

import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap: GoogleMap? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //search button
        Places.initialize(applicationContext, getString(R.string.google_map_api_key))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Toast.makeText(this@MainActivity, "Some Error in Search", Toast.LENGTH_SHORT).show()
            }

            override fun onPlaceSelected(place: Place) {
                val add = place.address
                val id = place.id
                val latLng = place.latLng!!
                zoomOnMap(latLng)
            }

        })


        //map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //menu button
        val mapOptionButton: ImageButton = findViewById(R.id.mapOptionsMenu)
        val popupMenu = PopupMenu(this, mapOptionButton)
        popupMenu.menuInflater.inflate(R.menu.map_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            changeMap(menuItem.itemId)
            true
        }

        mapOptionButton.setOnClickListener {
            popupMenu.show()
        }

    }

    private fun zoomOnMap(latLng: LatLng) {
        val newLatLngZoom =
            CameraUpdateFactory.newLatLngZoom(latLng, 12f) // 12f -> amount of zoom level
        mGoogleMap?.animateCamera(newLatLngZoom)
    }


    private fun changeMap(itemId: Int) {
        when (itemId) {
            R.id.normal_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybrid_map_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            R.id.satellite_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrain_map -> mGoogleMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        //simple marker
        addMarker(LatLng(13.123,12.123))

        //draggable marker
        addDraggableMarker(LatLng(12.456,34.567))

        //custom marker
        addCustomMarker(LatLng(34.322,76.456))

    }

    private fun addMarker(position:LatLng)
    {
        // Add simple marker
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Marker")
        )
    }
    private fun addDraggableMarker(position:LatLng)
    {
        // Add draggable marker
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Draggable Marker")
                .draggable(true)
        )
    }
    private fun addCustomMarker(position:LatLng)
    {
        // Add custom Marker
        mGoogleMap?.addMarker(
            MarkerOptions()
                .position(position)
                .title("Custom Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag_marker))
        )
    }


}