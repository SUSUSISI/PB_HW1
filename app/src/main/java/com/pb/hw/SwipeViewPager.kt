package com.pb.hw

import android.content.Context
import androidx.core.view.MotionEventCompat
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class SwipeViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var swipeEnabled = true


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean { return swipeEnabled }
    override fun onTouchEvent(ev: MotionEvent?): Boolean { return swipeEnabled }

    fun setPagingEnabled(enabled : Boolean){
        swipeEnabled = enabled
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

}