package com.pb.hw

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class BottomMenuAdapter(fm: FragmentManager, n : Int) : FragmentStatePagerAdapter(fm) {
    private val mPageCount = n


    override fun getItem(p0: Int): Fragment {
        return when(p0){
            1 -> Bottom_menu2()
            2 -> Bottom_menu3()
            3 -> Bottom_menu4()
            else -> Bottom_menu1()
        }
    }

    override fun getCount(): Int {
        return mPageCount
    }
}