package com.pb.hw

import SearchPlace.SearchActivity
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity(), OnMapReadyCallback, View.OnClickListener, ListClickListener {

    private var optionList1 = ArrayList<OptionItem>()
    private var optionList2 = ArrayList<OptionItem>()

    override fun onListClick(what: Int, pos: Int) {
        when (what) {
            0 -> {
                when (pos) {
                    0 -> myMap.mapType = NaverMap.MapType.Basic
                    1 -> myMap.mapType = NaverMap.MapType.Satellite
                    2 -> myMap.mapType = NaverMap.MapType.Terrain
                }
            }
            1 -> {
                when (pos) {
                    0 -> {
                        when (optionList1[pos].checked) {
                            0 -> roadViewButton.visibility = View.GONE
                            1 -> roadViewButton.visibility = View.VISIBLE
                        }
                    }
                    1 -> {
                        when (optionList1[pos].checked) {
                            0 -> myMap.isIndoorEnabled = false
                            1 -> myMap.isIndoorEnabled = true
                        }
                    }
                }
            }
            2 -> {
                when (pos) {
                    0 -> {
                        when (optionList2[pos].checked) {
                            0 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false)
                            1 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true)
                        }
                    }
                    1 -> {
                        when (optionList2[pos].checked) {
                            0 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false)
                            1 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true)
                        }
                    }
                    3 -> {
                        when (optionList2[pos].checked) {
                            0 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, false)
                            1 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true)
                        }
                    }
                    4 -> {
                        when (optionList2[pos].checked) {
                            0 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, false)
                            1 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true)
                        }
                    }
                    5 -> {
                        when (optionList2[pos].checked) {
                            0 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, false)
                            1 -> myMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, true)
                        }
                    }
                }
            }
        }

    }

    private var currentLocation: Location? = null
    private var searchMarker = Marker()
    private var infoWindow = InfoWindow()

    private lateinit var myMap: NaverMap
    private lateinit var locationSource: FusedLocationSource


    var touchY = 0
    private lateinit var gestureDetector: GestureDetector


    private fun initialBottomMenu() {
        val tab = TabLayout.Tab()

        /*
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.roadview))
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.bus))
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.car))
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.next))
        */



        gestureDetector = GestureDetector(this, object : GestureDetector.OnGestureListener {

            override fun onShowPress(p0: MotionEvent?) { ; }
            override fun onSingleTapUp(p0: MotionEvent?): Boolean { return true }
            override fun onDown(p0: MotionEvent?): Boolean { return true }
            override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
                Log.d("Fling!!","Fling!!")
                return true
            }

            override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
                if (p1 != null && p0 != null) {
                    val dis = p1.rawY.toInt() - p0.rawY.toInt()
                    if(touchY != dis){
                        Log.d("scroll", dis.toString())
                        bottom_menu.layoutParams.height = bottom_menu.layoutParams.height + touchY - dis
                        bottom_menu_invisible.layoutParams.height = bottom_menu.layoutParams.height + touchY - dis
                        touchY = dis
                        bottom_menu.requestLayout()
                        bottom_menu_invisible.requestLayout()
                        Log.d("size", bottom_menu.layoutParams.height.toString())
                    }
                }

                return true
            }
            override fun onLongPress(p0: MotionEvent?) { ; }
        })

        bottom_menu_tab.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    touchY = 0
                    Log.d("Touched", motionEvent.rawY.toInt().toString())
                }
            }
            gestureDetector.onTouchEvent(motionEvent)
        }



        bottom_menu_invisible.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                    touchY = 0
                    Log.d("Touched", motionEvent.rawY.toInt().toString())
                }
            }
            gestureDetector.onTouchEvent(motionEvent)
        }


        bottom_menu.setOnTouchListener { view, motionEvent -> true}

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        menuButton.setOnClickListener(this)
        mapOption.setOnClickListener(this)
        search.setOnClickListener(this)
        layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        initialDrawer()
        initialBottomMenu()


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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.menuButton -> layout_drawer.openDrawer(Gravity.LEFT)
            R.id.mapOption -> layout_drawer.openDrawer(Gravity.RIGHT)
            R.id.search -> DoSearch()
        }
    }


    private fun DoSearch() {
        val searchIntent = Intent(this, SearchActivity::class.java)
        if (currentLocation != null) {
            searchIntent.putExtra("currentLatitude", currentLocation?.latitude)
            searchIntent.putExtra("currentLongitude", currentLocation?.longitude)
        }
        searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        startActivity(searchIntent)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.anim_main)
    }

    private fun initialDrawer() {

        left_menu.setOnTouchListener(View.OnTouchListener { view, motionEvent -> true })
        right_menu.setOnTouchListener(View.OnTouchListener { view, motionEvent -> true })


        map_list.layoutManager = LinearLayoutManager(this)
        var mapList = ArrayList<MapItem>()
        mapList.add(MapItem("일반지도", R.drawable.map_standard))
        mapList.add(MapItem("위성지도", R.drawable.map_satelite))
        mapList.add(MapItem("지형도", R.drawable.map_ground))
        val adapter = MapListAdapter(mapList)
        adapter.setOnListClickListener(this)
        map_list.adapter = adapter


        option_list1.layoutManager = LinearLayoutManager(this)
        optionList1.add(OptionItem(R.drawable.roadview, "거리뷰", R.drawable.check_off, 0))
        optionList1.add(OptionItem(R.drawable.mapicon, "실내지도", R.drawable.check_off, 0))
        optionList1.add(OptionItem(R.drawable.star, "즐겨찾기", R.drawable.check_off, 0))
        val adapter1 = OptionListAdapter(optionList1)
        adapter1.setOnListClickListener(this)
        option_list1.adapter = adapter1

        option_list2.layoutManager = LinearLayoutManager(this)
        optionList2.add(OptionItem(R.drawable.traffic_light, "교통정보", R.drawable.check_off, 0))
        optionList2.add(OptionItem(R.drawable.bus, "대중교통", R.drawable.check_off, 0))
        optionList2.add(OptionItem(R.drawable.cctv, "CCTV", R.drawable.check_off, 0))
        optionList2.add(OptionItem(R.drawable.cycle, "자전거", R.drawable.check_off, 0))
        optionList2.add(OptionItem(R.drawable.mountain, "등산로", R.drawable.check_off, 0))
        optionList2.add(OptionItem(R.drawable.scale, "지적편집도", R.drawable.check_off, 0))
        var adapter2 = OptionListAdapter(optionList2)
        adapter2.setOnListClickListener(this)
        option_list2.adapter = adapter2

    }

    override fun onMapReady(p0: NaverMap) {
        myMap = p0
        myMap.locationSource = locationSource
        myMap.locationTrackingMode = LocationTrackingMode.Follow

        val uiSettings = myMap.uiSettings

        uiSettings.isScaleBarEnabled = true
        uiSettings.isLocationButtonEnabled = true
        uiSettings.isZoomControlEnabled = true
        myMap.addOnLocationChangeListener { location -> currentLocation = location }



        if (intent.hasExtra("name")) {
            searchMarker.position =
                LatLng(intent.getDoubleExtra("latitude", 0.0), intent.getDoubleExtra("longitude", 0.0))
            Log.d(
                "SearchPosition",
                "${intent.getDoubleExtra("latitude", 0.0)} , ${intent.getDoubleExtra("longitude", 0.0)}"
            )
            searchMarker.map = myMap

            val cameraUpdate = CameraUpdate.scrollTo(searchMarker.position)
            myMap.moveCamera(cameraUpdate)

            infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(baseContext) {
                override fun getText(p0: InfoWindow): CharSequence {
                    return "${intent.getStringExtra("name")}\n${intent.getStringExtra("add")}"
                }
            }
            infoWindow.open(searchMarker)


        } else {
            searchMarker.map = null
            infoWindow.close()
        }

    }


    override fun onBackPressed() {
        when {
            layout_drawer.isDrawerOpen(Gravity.LEFT) -> layout_drawer.closeDrawer(Gravity.LEFT)
            layout_drawer.isDrawerOpen(Gravity.RIGHT) -> layout_drawer.closeDrawer(Gravity.RIGHT)
            else -> super.onBackPressed()
        }
    }


    // 권한허용
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

