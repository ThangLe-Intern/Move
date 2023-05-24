package com.madison.move.ui.home

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.madison.move.R
import com.madison.move.data.DataManager
import com.madison.move.data.model.Category
import com.madison.move.data.model.MoveVideo
import com.madison.move.data.model.User
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.carousel.DataVideoCarousel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    override var view: HomeContract.HomeView?,
    var fragmentFeaturedList: ArrayList<FeaturedFragment>,
    var categoryList: MutableList<Category>,
    var videoList: MutableList<MoveVideo>
): HomeContract.Presenter {

    private val dataManager: DataManager = DataManager.instance
    override fun onShowFeaturedCarouselPresenter(fragmentFeaturedList: ArrayList<FeaturedFragment>) {
        view?.onShowFeaturedCarousel(fragmentFeaturedList)
        view?.onCarouselTransformer()
    }

    override fun getFeaturedVideoData() {
        dataManager.movieRepository.getCarousel()?.enqueue(
                object : Callback<CarouselResponse> {
                    override fun onResponse(
                        call: Call<CarouselResponse>,
                        carouselResponse: Response<CarouselResponse>
                    ) {
                        if (carouselResponse.body() != null) {
                            view?.onSuccessMoveData(carouselResponse.body()!!)
                        }
                    }

                    override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                        Log.e("ERROR", t.message.toString())
                        view?.onErrorMoveData(t.message.toString())
                    }
                }
            )
    }



    override fun onShowCategoryPresenter() {
        getCategoryData(categoryList)
        view?.onShowListCategory(categoryList)
    }

    private fun getCategoryData(categoryList: MutableList<Category>) {
        for (i in 1..2) {
            categoryList.add(Category(1, "MMA", R.drawable.img_category1, "10.2k views"))
            categoryList.add(Category(2, "HIIT", R.drawable.img_category2, "9.2k views"))
            categoryList.add(Category(3, "Just Move", R.drawable.img_category3, "8.2k views"))
        }

    }


    override fun onShowVideoSuggestionPresenter() {
        getVideoSuggestionData(videoList)
        view?.onShowListVideoSuggestion(videoList)
    }

    private fun getVideoSuggestionData(videoList: MutableList<MoveVideo>) {
        for (i in 1..6) {
            videoList.add(
                MoveVideo(
                    1,
                    "Legs days",
                    R.drawable.img_feature1,
                    "url",
                    "Just Move",
                    1,
                    30,
                    1,
                    1,
                    12,
                    30,
                    Category(3, "Just Move", R.drawable.img_category3, "8.2k views"),
                    User(
                        1, "vudung", "nguyenvudung@gmail.com", "Vu Dung",
                        "123", R.drawable.avatar, 1, "Male", "03/01/2001", 1,
                        "Ham Ninh - QN - QB"
                    )
                )
            )
        }
    }
}