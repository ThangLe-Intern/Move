package com.madison.move.ui.home

import com.madison.move.R
import com.madison.move.data.model.Category
import com.madison.move.data.model.MoveVideo
import com.madison.move.data.model.User

class HomePresenter(
    var homeView: HomeView,
    var fragmentFeaturedList: ArrayList<FeaturedFragment>,
    var categoryList: MutableList<Category>,
    var videoList: MutableList<MoveVideo>
) {

    fun onShowFeaturedCarouselPresenter() {
        getFeaturedVideoData(fragmentFeaturedList)
        homeView.onShowFeaturedCarousel(fragmentFeaturedList)
        homeView.onCarouselTransformer()
    }

    private fun getFeaturedVideoData(fragmentFeaturedList: ArrayList<FeaturedFragment>) {
        for (i in 1..4) {
            fragmentFeaturedList.add(FeaturedFragment())
        }
    }


    fun onShowCategoryPresenter() {
        getCategoryData(categoryList)
        homeView.onShowListCategory(categoryList)
    }

    private fun getCategoryData(categoryList: MutableList<Category>) {
        for (i in 1..2) {
            categoryList.add(Category(1, "MMA", R.drawable.img_category1, "10.2k views"))
            categoryList.add(Category(2, "HIIT", R.drawable.img_category2, "9.2k views"))
            categoryList.add(Category(3, "Just Move", R.drawable.img_category3, "8.2k views"))
        }

    }


    fun onShowVideoSuggestionPresenter() {
        getVideoSuggestionData(videoList)
        homeView.onShowListVideoSuggestion(videoList)
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