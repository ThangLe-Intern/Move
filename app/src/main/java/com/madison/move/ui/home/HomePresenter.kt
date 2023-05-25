package com.madison.move.ui.home

import android.util.Log
import com.madison.move.R
import com.madison.move.data.DataManager
import com.madison.move.data.model.Category
import com.madison.move.data.model.MoveVideo
import com.madison.move.data.model.User
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.carousel.DataVideoCarousel
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.category.DataCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    override var view: HomeContract.HomeView?,
    var videoList: MutableList<MoveVideo>
): HomeContract.Presenter {

    private val dataManager: DataManager = DataManager.instance


    override fun onShowFeaturedCarouselPresenter(fragmentFeaturedList: ArrayList<FeaturedFragment>,videoCarouselData:ArrayList<DataVideoCarousel>) {
        view?.onShowFeaturedCarousel(fragmentFeaturedList,videoCarouselData)
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
                            view?.onSuccessCarouselData(carouselResponse.body()!!)
                        }
                    }

                    override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                        Log.e("ERROR", t.message.toString())
                        view?.onErrorMoveData(t.message.toString())
                    }
                }
            )
    }

    override fun getCategoryData() {
        dataManager.movieRepository.getCategory()?.enqueue(
            object : Callback<CategoryResponse> {
                override fun onResponse(
                    call: Call<CategoryResponse>,
                    categoryResponse: Response<CategoryResponse>
                ) {
                    if (categoryResponse.body() != null) {
                        view?.onSuccessCategoryData(categoryResponse.body()!!)
                    }
                }
                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    view?.onErrorMoveData(t.message.toString())
                }
            }
        )
    }


    override fun onShowCategoryPresenter(listCategory: ArrayList<DataCategory>) {
        view?.onShowListCategory(listCategory)
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