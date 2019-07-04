package com.pb.hw.Controller

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.pb.hw.R
import kotlinx.android.synthetic.main.bottom_menu_near_place_icon.view.*


class CustomIconView : LinearLayout {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet ) : super(context,attrs){
        initView()
        getAttrs(attrs)
    }

    private fun getAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomMenuNearPlaceIcon)
        setTypeArray(typedArray)

    }

    private fun setTypeArray(typedArray: TypedArray?) {

        if(typedArray != null){
            symbol.setBackgroundResource(typedArray.getResourceId(
                R.styleable.BottomMenuNearPlaceIcon_bg,
                R.drawable.circular_orange
            ))
            symbol.setImageResource(typedArray.getResourceId(
                R.styleable.BottomMenuNearPlaceIcon_symbol,
                R.drawable.restaurant
            ))

            //symbol.setColorFilter(Color.parseColor("#000000"))

            text.setText(typedArray.getString(R.styleable.BottomMenuNearPlaceIcon_text))
            typedArray.recycle()
        }

    }

    private fun initView() {
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addView(li.inflate(R.layout.bottom_menu_near_place_icon,this,false))
    }


}