package com.pb.hw

import SearchPlace.RetrofitModel
import SearchPlace.SearchListAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.maplist_item.view.*
import kotlinx.android.synthetic.main.searchlist_item.view.*



class MapListAdapter(val mapList: ArrayList<MapItem>) : RecyclerView.Adapter<MapListAdapter.ViewHolder>(){

    private lateinit var listClickListener : ListClickListener
    private var selectedPos = -1
    private var selectedView : View? = null

    override fun getItemCount(): Int {
        Log.d("initial", "size : ${mapList.size}")
        return mapList.size
    }

    override fun onCreateViewHolder(mapItem: ViewGroup, viewType: Int): MapListAdapter.ViewHolder {
        val v = LayoutInflater.from(mapItem.context).inflate(R.layout.maplist_item,mapItem, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: MapListAdapter.ViewHolder, p1: Int) {
        holder.bindItems(mapList[p1])
    }

    inner class ViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bindItems(mapItem : MapItem){

            Log.d("initial", mapItem.name)
            itemView.mapname.text = mapItem.name
            itemView.mapimage.setImageResource(mapItem.image)

            if(position == 0) {
                select(position,itemView)
            }

            itemView.setOnClickListener {
                select(position, itemView)
                listClickListener.onListClick(0,position)
            }
        }
    }

    fun setOnListClickListener(listener: ListClickListener ){
        listClickListener = listener
    }

    fun select(pos : Int, v :View ){

        Log.d("Clicked", v.mapname.text as String?)
        when(selectedPos){
            -1 -> {
                selectedPos = pos
                selectedView = v
                selectedView!!.mapname.setTextColor(Color.parseColor("#000000"))
            }
            pos -> null
            else -> {
                selectedView!!.mapname.setTextColor(Color.parseColor("#afafaf"))
                v.mapname.setTextColor(Color.parseColor("#000000"))
                selectedPos = pos
                selectedView = v
            }
        }
    }

}