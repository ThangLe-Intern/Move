package com.madison.move.ui.home

import com.madison.move.data.model.Category
import com.madison.move.data.model.MoveVideo

interface HomeView {
    fun onShowFeaturedCarousel(featuredFragmentList: ArrayList<FeaturedFragment>)
    fun onCarouselTransformer()
    fun onHideSystemUI()

    fun onShowListCategory(listCategory: MutableList<Category>)
    fun onShowListVideoSuggestion(listVideo: MutableList<MoveVideo>)

}