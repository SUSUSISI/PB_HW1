package com.pb.hw.Controller

import com.pb.hw.Retrofit.RetrofitModel
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pb.hw.R
import kotlinx.android.synthetic.main.searchlist_item.view.*


class SearchListAdapter(val searchList: ArrayList<RetrofitModel.Place>) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>(){

        override fun getItemCount(): Int {
            return searchList.size
        }

        override fun onCreateViewHolder(place: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(place.context).inflate(R.layout.searchlist_item,place, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            holder.bindItems(searchList[p1])
        }

        inner class ViewHolder(view :View): RecyclerView.ViewHolder(view){
            fun bindItems(place : RetrofitModel.Place){
                itemView.placeName.text = place.name
                itemView.placeAdd.text = place.add


                itemView.setOnClickListener {

                    select(itemView.context,layoutPosition)
                }
            }


        }

        fun select(context: Context, pos : Int){
            val mIntent = Intent(context, MainActivity::class.java)
            mIntent.putExtra("name",searchList[pos].name)
            mIntent.putExtra("add",searchList[pos].add)
            mIntent.putExtra("latitude",searchList[pos].latitude)
            mIntent.putExtra("longitude",searchList[pos].longitude)
            context.startActivity(mIntent)

            when(MyDataBaseHelper(context, "search_table").insertData(searchList[pos])){
                true -> Log.d("insert","success")
                false -> Log.d("insert","failed")
            }
        }

}