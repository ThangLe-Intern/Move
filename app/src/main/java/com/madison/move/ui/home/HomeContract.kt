package com.madison.move.ui.home

import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface HomeContract {

    interface HomeView : BaseView {

        fun onShowFeaturedCarousel(
            featuredFragmentList: ArrayList<FeaturedFragment>,
            videoCarouselData: ArrayList<DataVideoSuggestion>
        )

        fun onCarouselTransformer()
        fun onShowListCategory(listCategory: ArrayList<DataCategory>)
        fun onShowListVideoSuggestion(listVideoSuggestion: ArrayList<DataVideoSuggestion>)
        fun onSuccessCarouselData(response: ObjectResponse<List<DataVideoSuggestion>>)
        fun onSuccessCategoryData(objectResponse: ObjectResponse<List<DataCategory>>)
        fun onSuccessVideoSuggestionData(videoSuggestionResponse: ObjectResponse<VideoSuggestion>)
        fun onSuccessVideoSuggestionForUser(videoSuggestionResponse: ObjectResponse<VideoSuggestion>)
        fun onErrorMoveData(error: String)
    }

    interface Presenter : BasePresenter<HomeView> {
        fun onShowFeaturedCarouselPresenter(
            fragmentFeaturedList: ArrayList<FeaturedFragment>,
            videoCarouselData: ArrayList<DataVideoSuggestion>
        )

        fun onShowCategoryPresenter(listCategory: ArrayList<DataCategory>)
        fun onShowVideoSuggestionPresenter(listVideoSuggestion: ArrayList<DataVideoSuggestion>)
        fun getFeaturedVideoData()

        fun getCategoryData()
        fun getVideoSuggestionData()
        fun getVideoSuggestionForUserData(token: String)
    }
}