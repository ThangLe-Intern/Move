package com.madison.move.ui.home


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.carousel.DataVideoCarousel
import com.madison.move.data.model.category.CategoryResponse
import com.madison.move.data.model.category.DataCategory
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestionResponse
import com.madison.move.databinding.FragmentHomeBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.home.adapter.CarouselViewPagerAdapter
import com.madison.move.ui.home.adapter.CategoryAdapter
import com.madison.move.ui.home.adapter.VideoSuggestionAdapter
import com.madison.move.ui.login.LoginDialogFragment
import kotlin.math.abs

class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.HomeView {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var carouselViewPagerAdapter: CarouselViewPagerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var videoSuggestionAdapter: VideoSuggestionAdapter
    private lateinit var handler: Handler

    private var getSharedPreferences: SharedPreferences? = null
    var videoCarouselData: ArrayList<DataVideoCarousel> = arrayListOf()
    var featuredList: ArrayList<FeaturedFragment> = arrayListOf()
    var categoryList: ArrayList<DataCategory> = arrayListOf()
    var videoList: ArrayList<DataVideoSuggestion> = arrayListOf()
    private var tokenUser: String? = null


    override fun createPresenter(): HomePresenter = HomePresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //Disable nested scroll of recyclerview
        binding.listVideoSuggestion.isNestedScrollingEnabled = false

        presenter?.apply {
            getFeaturedVideoData()
            getCategoryData()
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        getSharedPreferences = requireContext().getSharedPreferences(
            "tokenUser", AppCompatActivity.MODE_PRIVATE
        )
        tokenUser = getSharedPreferences?.getString("token", null)


        //if token not null get video suggestion for each user -- else get for all user
        if (tokenUser != null) {
            presenter?.getVideoSuggestionForUserData(tokenUser.toString())
        }else{
            presenter?.getVideoSuggestionData()
        }


        //Slider for carousel
        handler = Handler(Looper.myLooper()!!)
        handler.postDelayed(runnable, 3000)
    }


    override fun onSuccessCarouselData(response: CarouselResponse) {

        val listFragmentSize = response.data?.size
        videoCarouselData = response.data as ArrayList

        for (i in 0 until listFragmentSize!!) {
            featuredList.add(FeaturedFragment())
        }

        presenter?.onShowFeaturedCarouselPresenter(featuredList, videoCarouselData)
    }

    override fun onSuccessCategoryData(categoryResponse: CategoryResponse) {
        categoryList = categoryResponse.data as ArrayList<DataCategory>
        presenter?.onShowCategoryPresenter(categoryList)
    }

    override fun onSuccessVideoSuggestionData(videoSuggestionResponse: VideoSuggestionResponse) {
        videoList = videoSuggestionResponse.videoSuggestion.data as ArrayList<DataVideoSuggestion>
        presenter?.onShowVideoSuggestionPresenter(videoList)
    }

    override fun onSuccessVideoSuggestionForUser(videoSuggestionResponse: VideoSuggestionResponse) {
        videoList = videoSuggestionResponse.videoSuggestion.data as ArrayList<DataVideoSuggestion>
        presenter?.onShowVideoSuggestionPresenter(videoList)
    }

    override fun onErrorMoveData(error: String) {
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
    }

    private val runnable = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    //Show Video To Carousel
    override fun onShowFeaturedCarousel(
        featuredFragmentList: ArrayList<FeaturedFragment>,
        videoCarouselData: ArrayList<DataVideoCarousel>
    ) {
        handler = Handler(Looper.myLooper()!!)
        carouselViewPagerAdapter = CarouselViewPagerAdapter(
            this@HomeFragment, featuredFragmentList, videoCarouselData, binding.viewPager
        )

        binding.viewPager.apply {
            adapter = carouselViewPagerAdapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000)
            }
        })


    }

    //Add Transformation for Carousel
    override fun onCarouselTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))

        val screenHeight = resources.displayMetrics.widthPixels
        val nextItemTranslationX = 5.5f * screenHeight / 60

        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleX = 0.85f + r * 0.08f
            // translation X
            page.translationX = -position * nextItemTranslationX
        }
        binding.viewPager.setPageTransformer(transformer)
    }


    //Show list of Category
    override fun onShowListCategory(listCategory: ArrayList<DataCategory>) {
        categoryAdapter = CategoryAdapter(this, listCategory)
        binding.listCategory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    //Show list of Video Suggestion
    override fun onShowListVideoSuggestion(listVideoSuggestion: ArrayList<DataVideoSuggestion>) {
        videoSuggestionAdapter = VideoSuggestionAdapter(this, listVideoSuggestion)
        binding.listVideoSuggestion.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoSuggestionAdapter
        }
    }


}