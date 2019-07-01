package SearchPlace

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pb.hw.R
import kotlinx.android.synthetic.main.searchlist_item.view.*


    class SearchListAdapter(val context: Context, val searchList, ArrayList<Place>) : RecyclerView.Adapter<>() {

    inner class Holder(itemView : View?) : RecyclerView.ViewHolder(itemView) {
        val placeName = itemView?.findViewById<TextView>(R.id.placeName)
        val placeAdd = itemView?.findViewById<TextView>(R.id.placeAdd)

        fun bind(place : Place, context: Context){
            placeName?.text = place.name
            placeAdd?.text = place.add
        }
    }


}