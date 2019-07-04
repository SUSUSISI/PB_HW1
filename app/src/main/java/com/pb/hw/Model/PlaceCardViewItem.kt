package com.pb.hw.Model


data class PlaceCardViewItem(
    val placePhoto : ArrayList<Int>,
    val placeName : String
){
    var placeExplanation = ""
    var placeType = ""
    var placeTag = ""
    var placeDistance = "-"
    var placeReviewCount = "-"
    var placePrice = "-"


    fun placeExplanation (s : String) : PlaceCardViewItem {
        placeExplanation = s
        return this
    }
    fun placeType (s : String) : PlaceCardViewItem {
        placeType = s
        return this
    }
    fun placeTag (s : String) : PlaceCardViewItem {
        placeTag = s
        return this
    }
    fun placeDistance (s : String) : PlaceCardViewItem {
        placeDistance = s
        return this
    }
    fun placeReviewCount (s : String) : PlaceCardViewItem {
        placeReviewCount = s
        return this
    }
    fun placePrice (s : String) : PlaceCardViewItem {
        placePrice = s
        return this
    }


}