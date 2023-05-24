package com.madison.move.ui.home

import com.madison.move.data.model.Category
import com.madison.move.data.model.MoveVideo
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface HomeContract {

    interface HomeView : BaseView {

        fun onShowFeaturedCarousel(featuredFragmentList: ArrayList<FeaturedFragment>)
        fun onCarouselTransformer()
        fun onShowListCategory(listCategory: MutableList<Category>)
        fun onShowListVideoSuggestion(listVideo: MutableList<MoveVideo>)

        fun onSuccessMoveData(response: CarouselResponse)
        fun onErrorMoveData(error: String)
    }
    interface Presenter: BasePresenter<HomeView>{
        fun onShowFeaturedCarouselPresenter(fragmentFeaturedList: ArrayList<FeaturedFragment>)
        fun onShowCategoryPresenter()
        fun onShowVideoSuggestionPresenter()
        fun getFeaturedVideoData()
    }
}