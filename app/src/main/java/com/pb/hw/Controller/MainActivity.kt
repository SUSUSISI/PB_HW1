package com.pb.hw.Controller

import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.Gravity
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.pb.hw.Model.MapItem
import com.pb.hw.Model.OptionItem
import com.pb.hw.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity(), OnMapReadyCallback, View.OnClickListener,
    ListClickListener {

    private var optionList1 = ArrayList<OptionItem>()
    private var optionList2 = ArrayList<OptionItem>()
    var screeanSize = Point()
    private lateinit var db: MyDataBaseHelper


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //db = MyDataBaseHelper(applicationContext,"search_table")
        //db.dropTable()
        windowManager.defaultDisplay.getSize(screeanSize)
        bottom_menu_tab.layoutParams.width = screeanSize.x - (20 * resources.displayMetrics.density).toInt()
        bottom_menu_tab.requestLayout()

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

    private fun initialBottomMenu() {
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.roadview))
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.bus))
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.car))
        bottom_menu_tab.addTab(bottom_menu_tab.newTab().setIcon(R.drawable.next))
        bottom_menu_tab.getTabAt(0)?.icon?.alpha = 50

        bottom_menu_tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.icon?.alpha = 255
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.icon?.alpha = 50

                p0?.position?.let { bottom_menu_viewpager.setCurrentItem(it, false) }
            }
        })
        bottom_menu_viewpager.setPagingEnabled(false)
        bottom_menu_viewpager.adapter =
            BottomMenuAdapter(supportFragmentManager, bottom_menu_tab.tabCount)


        val bottomSheetBehavior = BottomSheetBehavior.from(bottomsheet)
        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(p0: View, p1: Float) {
                val change = (p1 * 100).toInt()
                if (change <= 20) {
                    bottom_menu_tab.layoutParams.width =
                        screeanSize.x + ((change - 20) * resources.displayMetrics.density).toInt()
                    bottom_menu_tab.requestLayout()
                } else {
                    bottom_menu_tab.layoutParams.width = screeanSize.x
                    bottom_menu_tab.requestLayout()
                }
            }


            override fun onStateChanged(p0: View, p1: Int) {

            }

        })


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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        }
        startActivity(searchIntent)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.anim_main)
    }

    private fun initialDrawer() {

        left_menu.setOnTouchListener(View.OnTouchListener { _, _ -> true })
        right_menu.setOnTouchListener(View.OnTouchListener { _, _ -> true })


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
        optionList2.add(
            OptionItem(
                R.drawable.traffic_light,
                "교통정보",
                R.drawable.check_off,
                0
            )
        )
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
        uiSettings.isZoomControlEnabled = true
        uiSettings.isLocationButtonEnabled = false

        myMapLocationButton.map = myMap

        myMap.addOnLocationChangeListener { location -> currentLocation = location }



        if (intent.hasExtra("name")) {
            searchMarker.position =
                LatLng(intent.getDoubleExtra("latitude", 0.0), intent.getDoubleExtra("longitude", 0.0))
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

