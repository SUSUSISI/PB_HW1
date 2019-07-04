package com.pb.hw.Controller

import com.pb.hw.Retrofit.RetrofitInterface
import com.pb.hw.Retrofit.RetrofitModel
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.naver.maps.geometry.LatLng
import com.pb.hw.R
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var imm : InputMethodManager
    private lateinit var mRetrofit : Retrofit
    private lateinit var currentLocation : LatLng
    val searchHistorydb = MyDataBaseHelper(this, "search_table")
    private var adapter : SearchListAdapter? = null

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backButton -> finish()
            R.id.delete_btn -> deleteHistory()
        }
    }

    private fun deleteHistory() {
        searchHistorydb.deleteAllData()
        searchlist.adapter = SearchListAdapter(searchHistorydb.getAllData())
        searchlist.adapter?.notifyDataSetChanged()
    }

    private fun do_search() {
        setRetrofitInit()
        val retrofitInterface = mRetrofit.create(RetrofitInterface::class.java)

        if(currentLocation.latitude == 0.0){
            currentLocation = LatLng(35.23083313833922,129.0826465934515)
        }

        val call = retrofitInterface.getRerofitModel(search_input.text.toString(), "${currentLocation.longitude}, ${currentLocation.latitude}")

        call.enqueue(object : Callback<RetrofitModel> {
            override fun onResponse(call: Call<RetrofitModel>, response: Response<RetrofitModel>) {
                val data = response.body()
                val list = data!!.list

                if (list != null) {
                    adapter = SearchListAdapter(list)
                    searchlist.adapter = adapter
                    adapter?.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<RetrofitModel>, t: Throwable) {

            }
        })


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setContentView(R.layout.activity_search)

        adapter = SearchListAdapter(searchHistorydb.getAllData())
        searchlist.adapter = adapter
        searchlist.adapter?.notifyDataSetChanged()
        searchlist.layoutManager = LinearLayoutManager(this)

        currentLocation = LatLng(intent.getDoubleExtra("currentLatitude", 0.0),intent.getDoubleExtra("currentLongitude", 0.0))

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        backButton.setOnClickListener(this)
        delete_btn.setOnClickListener(this)

        search_input.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH ->
                    do_search()
                else -> false
            }
            true
        }
    }



    private fun setRetrofitInit() {
            mRetrofit = Retrofit.Builder().baseUrl("https://naveropenapi.apigw.ntruss.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    override fun onPause() {
        imm.hideSoftInputFromWindow(search_input.windowToken, 0)
        super.onPause()
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_main, R.anim.abc_fade_out)
    }


}
