package com.pb.hw

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.naver.maps.map.util.FusedLocationSource
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.os.Build
import android.view.View
import com.naver.maps.map.*
import com.naver.maps.map.widget.ZoomControlView


class MainActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var myMap : NaverMap
    private lateinit var locationSource: FusedLocationSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN



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
