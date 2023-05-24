package com.madison.move.data.source.remote.test

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.madison.move.data.model.carousel.CarouselResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object RetrofitApi {
    val moveApi: Vxx by lazy {
        Retrofit.Builder().baseUrl("https://api.move-intern-stg.madlab.tech/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Vxx::class.java)
    }
}

interface Vxx {
    @GET("videos-carousel")
    fun getMove(): Call<CarouselResponse>
}

class MoveViewModel : ViewModel() {
    private var carouselResponseData = MutableLiveData<CarouselResponse>()

    fun getMoveData() {
        RetrofitApi.moveApi.getMove().enqueue(
            object : Callback<CarouselResponse> {
                override fun onResponse(
                    call: Call<CarouselResponse>,
                    carouselResponse: Response<CarouselResponse>
                ) {
                    if (carouselResponse.body() != null) {
                        carouselResponseData.value = carouselResponse.body()
                    }
                }

                override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                }
            }
        )
    }

    fun setMoveData(): LiveData<CarouselResponse> {
        return carouselResponseData
    }
}

