package com.pb.hw.Retrofit

import com.pb.hw.Retrofit.RetrofitModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RetrofitInterface {


    @Headers("X-NCP-APIGW-API-KEY-ID:f9jmh5xw8t", "X-NCP-APIGW-API-KEY:2mzPF4BcwL7yuA2D4aoApCPN1y42hFzsnncXLoJK")
    @GET("/map-place/v1/search")
    fun getRerofitModel(@Query(value="query") place: String, @Query(value="coordinate") xy: String) : Call<RetrofitModel>
}