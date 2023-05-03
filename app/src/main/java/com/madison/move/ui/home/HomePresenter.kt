package com.madison.move.ui.home

class HomePresenter(var homeView: HomeView,var fragmentFeaturedList:ArrayList<FeaturedFragment>) {
    fun onHideSystemUIPresenter(){
        homeView.onHideSystemUI()
    }

    fun onShowFeaturedCarouselPresenter(){
        getFeaturedVideoData(fragmentFeaturedList)
        homeView.onShowFeaturedCarousel(fragmentFeaturedList)
        homeView.onCarouselTransformer()
    }

    fun getFeaturedVideoData(fragmentFeaturedList:ArrayList<FeaturedFragment>):ArrayList<FeaturedFragment>{
        for (i in 1..4) {
            fragmentFeaturedList.add(FeaturedFragment())
        }
        return fragmentFeaturedList
    }
}