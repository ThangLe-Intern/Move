package com.madison.move.ui.home

import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.carousel.DataVideoCarousel
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.category.DataCategory
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface HomeContract {

    interface HomeView : BaseView {

        fun onShowFeaturedCarousel(
            featuredFragmentList: ArrayList<FeaturedFragment>,
            videoCarouselData: ArrayList<DataVideoCarousel>
        )

        fun onCarouselTransformer()
        fun onShowListCategory(listCategory: ArrayList<DataCategory>)
        fun onShowListVideoSuggestion(listVideoSuggestion: ArrayList<DataVideoSuggestion>)
        fun onSuccessCarouselData(response: CarouselResponse)
        fun onSuccessCategoryData(categoryResponse: CategoryResponse)
        fun onSuccessVideoSuggestionData(videoSuggestionResponse: VideoSuggestionResponse)
        fun onSuccessVideoSuggestionForUser(videoSuggestionResponse: VideoSuggestionResponse)
        fun onErrorMoveData(error: String)
    }

    interface Presenter : BasePresenter<HomeView> {
        fun onShowFeaturedCarouselPresenter(
            fragmentFeaturedList: ArrayList<FeaturedFragment>,
            videoCarouselData: ArrayList<DataVideoCarousel>
        )

        fun onShowCategoryPresenter(listCategory: ArrayList<DataCategory>)
        fun onShowVideoSuggestionPresenter(listVideoSuggestion: ArrayList<DataVideoSuggestion>)
        fun getFeaturedVideoData()

        fun getCategoryData()
        fun getVideoSuggestionData()
        fun getVideoSuggestionForUserData(token:String)
    }
}