package com.madison.move.ui.home

import android.util.Log
import com.madison.move.data.DataManager
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.carousel.DataVideoCarousel
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.category.DataCategory
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    override var view: HomeContract.HomeView?
) : HomeContract.Presenter {

    private val dataManager: DataManager = DataManager.instance


    override fun onShowFeaturedCarouselPresenter(
        fragmentFeaturedList: ArrayList<FeaturedFragment>,
        videoCarouselData: ArrayList<DataVideoCarousel>
    ) {
        view?.onShowFeaturedCarousel(fragmentFeaturedList, videoCarouselData)
        view?.onCarouselTransformer()
    }

    override fun getFeaturedVideoData() {
        dataManager.movieRepository.getCarousel()?.enqueue(object : Callback<CarouselResponse> {
            override fun onResponse(
                call: Call<CarouselResponse>, carouselResponse: Response<CarouselResponse>
            ) {
                if (carouselResponse.body() != null) {
                    view?.onSuccessCarouselData(carouselResponse.body()!!)
                }
            }

            override fun onFailure(call: Call<CarouselResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                view?.onErrorMoveData(t.message.toString())
            }
        })
    }

    override fun getCategoryData() {
        dataManager.movieRepository.getCategory()?.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(
                call: Call<CategoryResponse>, categoryResponse: Response<CategoryResponse>
            ) {
                if (categoryResponse.body() != null) {
                    view?.onSuccessCategoryData(categoryResponse.body()!!)
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.e("ERROR", t.message.toString())
                view?.onErrorMoveData(t.message.toString())
            }
        })
    }

    override fun onShowCategoryPresenter(listCategory: ArrayList<DataCategory>) {
        view?.onShowListCategory(listCategory)
    }


    override fun getVideoSuggestionData() {
        dataManager.movieRepository.getVideoSuggestion()
            ?.enqueue(object : Callback<VideoSuggestionResponse> {
                override fun onResponse(
                    call: Call<VideoSuggestionResponse>,
                    videoSuggestionResponse: Response<VideoSuggestionResponse>
                ) {
                    if (videoSuggestionResponse.body() != null) {
                        view?.onSuccessVideoSuggestionData(videoSuggestionResponse.body()!!)
                    }
                }

                override fun onFailure(call: Call<VideoSuggestionResponse>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    view?.onErrorMoveData(t.message.toString())
                }
            })
    }

    override fun getVideoSuggestionForUserData(token: String) {
        dataManager.movieRepository.getVideoSuggestionForUser("Bearer $token")
            ?.enqueue(object : Callback<VideoSuggestionResponse> {
                override fun onResponse(
                    call: Call<VideoSuggestionResponse>,
                    videoSuggestionResponse: Response<VideoSuggestionResponse>
                ) {
                    if (videoSuggestionResponse.body() != null) {
                        view?.onSuccessVideoSuggestionForUser(videoSuggestionResponse.body()!!)
                    }
                }

                override fun onFailure(call: Call<VideoSuggestionResponse>, t: Throwable) {
                    Log.e("ERROR", t.message.toString())
                    view?.onErrorMoveData(t.message.toString())
                }
            })
    }


    override fun onShowVideoSuggestionPresenter(listVideoSuggestion: ArrayList<DataVideoSuggestion>) {
        view?.onShowListVideoSuggestion(listVideoSuggestion)
    }

}