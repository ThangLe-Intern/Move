package com.madison.move.ui.home

import com.madison.move.R
import com.madison.move.data.model.Category

class HomePresenter(
    var homeView: HomeView,
    var fragmentFeaturedList: ArrayList<FeaturedFragment>,
    var categoryList: MutableList<Category>
) {
    fun onHideSystemUIPresenter() {
        homeView.onHideSystemUI()
    }

    fun onShowFeaturedCarouselPresenter() {
        getFeaturedVideoData(fragmentFeaturedList)
        homeView.onShowFeaturedCarousel(fragmentFeaturedList)
        homeView.onCarouselTransformer()
    }

    private fun getFeaturedVideoData(fragmentFeaturedList: ArrayList<FeaturedFragment>): ArrayList<FeaturedFragment> {
        for (i in 1..4) {
            fragmentFeaturedList.add(FeaturedFragment())
        }
        return fragmentFeaturedList
    }


    fun onShowCategoryPresenter(){
        getCategoryData(categoryList)
        homeView.onShowListCategory(categoryList)
    }

    private fun getCategoryData(categoryList: MutableList<Category>):MutableList<Category> {
        for (i in 1..2) {
            categoryList.add(Category(1,"MMA", R.drawable.img_category1,  "10.2k views"))
            categoryList.add(Category(2,"HIIT", R.drawable.img_category2,  "9.2k views"))
            categoryList.add(Category(3,"Just Move", R.drawable.img_category3,  "8.2k views"))
        }
        return categoryList
    }
}