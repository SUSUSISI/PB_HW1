package com.pb.hw.Controller

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pb.hw.Model.MapItem
import com.pb.hw.R
import kotlinx.android.synthetic.main.maplist_item.view.*


class MapListAdapter(val mapList: ArrayList<MapItem>) : RecyclerView.Adapter<MapListAdapter.ViewHolder>(){

    private lateinit var listClickListener : ListClickListener
    private var selectedPos = -1
    private var selectedView : View? = null

    override fun getItemCount(): Int {
        return mapList.size
    }

    override fun onCreateViewHolder(mapItem: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(mapItem.context).inflate(R.layout.maplist_item,mapItem, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindItems(mapList[p1])
    }

    inner class ViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bindItems(mapItem : MapItem){

            itemView.mapname.text = mapItem.name
            GlideApp.with(itemView).load(mapItem.image).override(100,100).into(itemView.mapimage)
            if(layoutPosition == 0) {
                select(layoutPosition,itemView)
            }

            itemView.setOnClickListener {
                select(layoutPosition, itemView)
                listClickListener.onListClick(0,layoutPosition)
            }
        }
    }

    fun setOnListClickListener(listener: ListClickListener){
        listClickListener = listener
    }

    fun select(pos : Int, v :View ){

        when(selectedPos){
            -1 -> {
                selectedPos = pos
                selectedView = v
                selectedView!!.mapname.setTextColor(Color.parseColor("#000000"))
            }
            pos -> {;}
            else -> {
                selectedView!!.mapname.setTextColor(Color.parseColor("#afafaf"))
                v.mapname.setTextColor(Color.parseColor("#000000"))
                selectedPos = pos
                selectedView = v
            }
        }
    }

}