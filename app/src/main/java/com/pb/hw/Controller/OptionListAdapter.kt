package com.pb.hw.Controller

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pb.hw.Model.OptionItem
import com.pb.hw.R
import kotlinx.android.synthetic.main.optionlist_item.view.*

class OptionListAdapter(val optionList: ArrayList<OptionItem>) : RecyclerView.Adapter<OptionListAdapter.ViewHolder>(){

    private lateinit var listClickListener : ListClickListener

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onCreateViewHolder(optionItem: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(optionItem.context).inflate(R.layout.optionlist_item,optionItem, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindItems(optionList[p1])
    }

    inner class ViewHolder(view : View): RecyclerView.ViewHolder(view){

        fun bindItems(optionItem : OptionItem){

            itemView.option_icon.setImageResource(optionItem.option_icon)
            itemView.option_icon.imageAlpha = 50
            itemView.option_name.text = optionItem.name
            itemView.option_check.setImageResource(optionItem.check_icon)
            itemView.setOnClickListener {
                select(layoutPosition, itemView)

            }
        }
    }

    fun setOnListClickListener(listener: ListClickListener){
        listClickListener = listener
    }

    fun select(pos : Int, v : View){
        when(optionList[pos].checked){
            0 -> {
                v.option_check.setImageResource(R.drawable.check_on)
                v.option_name.setTextColor(Color.parseColor("#000000"))
                v.option_icon.imageAlpha = 255
                optionList[pos].checked = 1
                if(optionList.size <= 3)
                listClickListener.onListClick(1,pos)
                else
                listClickListener.onListClick(2,pos)

            }
            1 -> {
                v.option_check.setImageResource(R.drawable.check_off)
                v.option_name.setTextColor(Color.parseColor("#afafaf"))
                v.option_icon.imageAlpha = 50
                optionList[pos].checked = 0
                if(optionList.size <= 3)
                    listClickListener.onListClick(1,pos)
                else
                    listClickListener.onListClick(2,pos)

            }
        }
    }

}