package com.pb.hw

import SearchPlace.SearchActivity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.naver.maps.map.util.FusedLocationSource
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.os.Build
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.View
import com.naver.maps.map.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity(), OnMapReadyCallback, View.OnClickListener {
    private var currentLocation : Location? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.menuButton -> layout_drawer.openDrawer(Gravity.LEFT)
            R.id.mapOption -> layout_drawer.openDrawer(Gravity.RIGHT)
            R.id.search -> DoSearch()
        }
    }


    private fun DoSearch() {
        val searchIntent = Intent(this, SearchActivity::class.java)
        if ( currentLocation != null) {
            searchIntent.putExtra("currentLatitude", currentLocation?.latitude)
            searchIntent.putExtra("currentLongitude", currentLocation?.longitude)
        }
        startActivity(searchIntent)
        overridePendingTransition(R.anim.abc_fade_in,R.anim.anim_main)
    }

    private lateinit var myMap : NaverMap
    private lateinit var locationSource: FusedLocationSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        menuButton.setOnClickListener(this)
        mapOption.setOnClickListener(this)
        search.setOnClickListener(this)
        layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)



        // for naver map
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.myMap) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.myMap, it).commit()
            }
        mapFragment.getMapAsync(this)

        // for 위치추적
        locationSource =
            FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onMapReady(p0: NaverMap) {
        myMap = p0
        myMap.locationSource = locationSource
        myMap.locationTrackingMode = LocationTrackingMode.Follow

        val uiSettings = myMap.uiSettings

        uiSettings.isScaleBarEnabled = true
        uiSettings.isLocationButtonEnabled = true
        uiSettings.isZoomControlEnabled = true
        myMap.addOnLocationChangeListener { location -> currentLocation = location}
    }


    override fun onBackPressed() {
        when {
            layout_drawer.isDrawerOpen(Gravity.LEFT) -> layout_drawer.closeDrawer(Gravity.LEFT)
            layout_drawer.isDrawerOpen(Gravity.RIGHT) -> layout_drawer.closeDrawer(Gravity.RIGHT)
            else -> super.onBackPressed()
        }
    }





    // 권한허용
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}
