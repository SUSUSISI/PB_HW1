package com.pb.hw.Retrofit

import com.google.gson.annotations.SerializedName

class RetrofitModel {

    @SerializedName("places")
    var list : ArrayList<Place>? = null

    class Place(
    @SerializedName("name") val name : String,
    @SerializedName("road_address") val add : String,
    @SerializedName("x") val longitude : Double,
    @SerializedName("y") val latitude : Double
    )
}