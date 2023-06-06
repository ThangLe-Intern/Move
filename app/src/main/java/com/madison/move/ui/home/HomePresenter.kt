package com.madison.move.ui.home

import com.madison.move.data.DataManager
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(
    override var view: HomeContract.HomeView?
) : HomeContract.Presenter {

    private val dataManager: DataManager = DataManager.instance


    override fun onShowFeaturedCarouselPresenter(
        fragmentFeaturedList: ArrayList<FeaturedFragment>,
        videoCarouselData: ArrayList<DataVideoSuggestion>
    ) {
        view?.onShowFeaturedCarousel(fragmentFeaturedList, videoCarouselData)
        view?.onCarouselTransformer()
    }

    override fun getFeaturedVideoData() {
        dataManager.movieRepository.getCarousel()?.enqueue(object : Callback<ObjectResponse<List<DataVideoSuggestion>>> {
            override fun onResponse(
                call: Call<ObjectResponse<List<DataVideoSuggestion>>>, carouselResponse: Response<ObjectResponse<List<DataVideoSuggestion>>>
            ) {
                if (carouselResponse.body() != null) {
                    carouselResponse.body()
                    view?.onSuccessCarouselData(carouselResponse.body()!!)
                }
            }

            override fun onFailure(call: Call<ObjectResponse<List<DataVideoSuggestion>>>, t: Throwable) {
                view?.onErrorMoveData(t.message.toString())
            }
        })
    }

    override fun getCategoryData() {
        dataManager.movieRepository.getCategory()?.enqueue(object : Callback<ObjectResponse<List<DataCategory>>> {
            override fun onResponse(
                call: Call<ObjectResponse<List<DataCategory>>>, objectResponse: Response<ObjectResponse<List<DataCategory>>>
            ) {
                if (objectResponse.body() != null) {
                    view?.onSuccessCategoryData(objectResponse.body()!!)
                }
            }

            override fun onFailure(call: Call<ObjectResponse<List<DataCategory>>>, t: Throwable) {
                view?.onErrorMoveData(t.message.toString())
            }
        })
    }

    override fun onShowCategoryPresenter(listCategory: ArrayList<DataCategory>) {
        view?.onShowListCategory(listCategory)
    }


    override fun getVideoSuggestionData() {
        dataManager.movieRepository.getVideoSuggestion()
            ?.enqueue(object : Callback<ObjectResponse<VideoSuggestion>> {
                override fun onResponse(
                    call: Call<ObjectResponse<VideoSuggestion>>,
                    videoSuggestionResponse: Response<ObjectResponse<VideoSuggestion>>
                ) {
                    if (videoSuggestionResponse.body() != null) {
                        view?.onSuccessVideoSuggestionData(videoSuggestionResponse.body()!!)
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<VideoSuggestion>>, t: Throwable) {
                    view?.onErrorMoveData(t.message.toString())
                }
            })
    }

    override fun getVideoSuggestionForUserData(token: String) {
        dataManager.movieRepository.getVideoSuggestionForUser("Bearer $token")
            ?.enqueue(object : Callback<ObjectResponse<VideoSuggestion>> {
                override fun onResponse(
                    call: Call<ObjectResponse<VideoSuggestion>>,
                    videoSuggestionResponse: Response<ObjectResponse<VideoSuggestion>>
                ) {
                    if (videoSuggestionResponse.body() != null) {
                        view?.onSuccessVideoSuggestionForUser(videoSuggestionResponse.body()!!)
                    }
                }

                override fun onFailure(call: Call<ObjectResponse<VideoSuggestion>>, t: Throwable) {
                    view?.onErrorMoveData(t.message.toString())
                }
            })
    }


    override fun onShowVideoSuggestionPresenter(listVideoSuggestion: ArrayList<DataVideoSuggestion>) {
        view?.onShowListVideoSuggestion(listVideoSuggestion)
    }

}