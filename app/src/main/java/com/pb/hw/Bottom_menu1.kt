package com.pb.hw

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_bottom_menu1.*
import kotlinx.android.synthetic.main.fragment_bottom_menu1.view.*
import java.text.DecimalFormat

class Bottom_menu1 : Fragment() {

    private var placeRecommenationList = ArrayList<PlaceCardViewItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view =inflater.inflate(R.layout.fragment_bottom_menu1,container,false)
        val formatter = DecimalFormat("###,###")
        placeRecommenationList.add(
            PlaceCardViewItem(arrayListOf(R.drawable.map_satelite),"회국수할매집")
                .placeType("한식")
                .placeExplanation("백종원3대천왕 김준현이 호로록 다녀간 부산 맛집")
                .placeDistance(formatter.format(443))
                .placeReviewCount(formatter.format(116))
                .placePrice(formatter.format(5000))
                .placeTag("회국수  비빔국수  충무김밥  수수한  착한가격"))
        placeRecommenationList.add(
            PlaceCardViewItem(arrayListOf(R.drawable.map_satelite),"회국수할매집")
                .placeType("한식")
                .placeExplanation("백종원3대천왕 김준현이 호로록 다녀간 부산 맛집")
                .placeDistance(formatter.format(443))
                .placeReviewCount(formatter.format(116))
                .placePrice(formatter.format(5000))
                .placeTag("회국수  비빔국수  충무김밥  수수한  착한가격"))
        placeRecommenationList.add(
            PlaceCardViewItem(arrayListOf(R.drawable.map_satelite),"회국수할매집")
                .placeType("한식")
                .placeExplanation("백종원3대천왕 김준현이 호로록 다녀간 부산 맛집")
                .placeDistance(formatter.format(443))
                .placeReviewCount(formatter.format(116))
                .placePrice(formatter.format(5000))
                .placeTag("회국수  비빔국수  충무김밥  수수한  착한가격"))
        placeRecommenationList.add(
            PlaceCardViewItem(arrayListOf(R.drawable.map_satelite),"회국수할매집")
                .placeType("한식")
                .placeExplanation("백종원3대천왕 김준현이 호로록 다녀간 부산 맛집")
                .placeDistance(formatter.format(443))
                .placeReviewCount(formatter.format(116))
                .placePrice(formatter.format(5000))
                .placeTag("회국수  비빔국수  충무김밥  수수한  착한가격"))

        placeRecommenationList.add(
            PlaceCardViewItem(arrayListOf(R.drawable.map_satelite),"회국수할매집")
                .placeType("한식")
                .placeExplanation("백종원3대천왕 김준현이 호로록 다녀간 부산 맛집")
                .placeDistance(formatter.format(443))
                .placeReviewCount(formatter.format(116))
                .placePrice(formatter.format(5000))
                .placeTag("회국수  비빔국수  충무김밥  수수한  착한가격"))



        view.bottom_menu_near_recommendation_list.layoutManager =
            LinearLayoutManager(activity)
        val adapter = PlaceCardListAdapter(placeRecommenationList)
        view.bottom_menu_near_recommendation_list.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }


}
