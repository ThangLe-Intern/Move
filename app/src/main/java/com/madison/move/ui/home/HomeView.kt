package com.madison.move.ui.home

interface HomeView {
    fun onShowFeaturedCarousel(featuredFragmentList: ArrayList<FeaturedFragment>)
    fun onCarouselTransformer()
    fun onHideSystemUI()

}