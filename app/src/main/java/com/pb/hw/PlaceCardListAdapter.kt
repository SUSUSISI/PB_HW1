package com.pb.hw

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.optionlist_item.view.*
import kotlinx.android.synthetic.main.place_card_view.view.*
import java.text.DecimalFormat


class PlaceCardListAdapter(val placeCardList : ArrayList<PlaceCardViewItem>) : RecyclerView.Adapter<PlaceCardListAdapter.ViewHolder>(){

    //private lateinit var listClickListener : ListClickListener

    override fun getItemCount(): Int {
        return placeCardList.size
    }

    override fun onCreateViewHolder(placeCardView: ViewGroup, viewType: Int): PlaceCardListAdapter.ViewHolder {
        val v = LayoutInflater.from(placeCardView.context).inflate(R.layout.place_card_view,placeCardView, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: PlaceCardListAdapter.ViewHolder, p1: Int) {
        holder.bindItems(placeCardList[p1])
    }

    inner class ViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bindItems(placeCardViewItem: PlaceCardViewItem){


            // 수정되야할 부분
            //itemView.place_card_view_placePhoto.setImageResource(placeCardViewItem.placePhoto[0])
            GlideApp.with(itemView).load(placeCardViewItem.placePhoto[0]).override(itemView.place_card_view_placePhoto.layoutParams.width,itemView.place_card_view_placePhoto.layoutParams.height).into(itemView.place_card_view_placePhoto)
            itemView.place_card_view_placeName.text = placeCardViewItem.placeName
            itemView.place_card_view_placeType.text = placeCardViewItem.placeType
            itemView.place_card_view_placeExplanation.text = placeCardViewItem.placeExplanation
            itemView.place_card_view_placeDistance.text = placeCardViewItem.placeDistance
            itemView.place_card_view_placeReviewCount.text = placeCardViewItem.placeReviewCount
            itemView.place_card_view_placePrice.text = placeCardViewItem.placePrice
            itemView.place_card_view_placeTag.text = placeCardViewItem.placeTag
            itemView.setOnClickListener {
                select(layoutPosition, itemView)

            }
        }
    }

    fun select(pos : Int, v : View){
    }
}

