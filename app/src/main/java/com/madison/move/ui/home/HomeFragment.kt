package com.madison.move.ui.home


import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataCategory
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.databinding.FragmentHomeBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.home.adapter.CarouselViewPagerAdapter
import com.madison.move.ui.home.adapter.CategoryAdapter
import com.madison.move.ui.home.adapter.VideoSuggestionAdapter
import com.madison.move.ui.offlinechannel.CommentFragment
import kotlin.math.abs

class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.HomeView {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var carouselViewPagerAdapter: CarouselViewPagerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var videoSuggestionAdapter: VideoSuggestionAdapter
    private var handler: Handler = Handler(Looper.getMainLooper())

    private var getSharedPreferences: SharedPreferences? = null
    var videoCarouselData = ArrayList<DataVideoSuggestion>()
    var featuredList = ArrayList<FeaturedFragment>()
    var categoryList = ArrayList<DataCategory>()
    var videoList = ArrayList<DataVideoSuggestion>()
    private var tokenUser: String? = null

    private var isGetCarouselSuccess = false
    private var isGetCategorySuccess = false
    private var isGetVideoSuggestionSuccess = false

    companion object {
        const val TOKEN_USER_PREFERENCE = "tokenUser"
        const val TOKEN = "token"
    }


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
        onRefreshData()
        //Slider for carousel
        handler.postDelayed(runnable, 3000)
    }

    private fun onRefreshData() {

        mListener?.onShowProgressBar()

        if (this::carouselViewPagerAdapter.isInitialized) {
            // Clear the data in the adapter
            carouselViewPagerAdapter.onClearCarousel()
            // Notify the adapter of the data change
            carouselViewPagerAdapter.notifyDataSetChanged();
            // Reset the ViewPager to the first item
            binding.viewPager.currentItem = 0
        }


        presenter?.apply {
            getFeaturedVideoData()
            getCategoryData()
        }



        getSharedPreferences = requireContext().getSharedPreferences(
            TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
        )

        tokenUser = getSharedPreferences?.getString(TOKEN, null)

        //if token not null get video suggestion for each user -- else get for all user
        videoList.clear()
        if (this::videoSuggestionAdapter.isInitialized) {
            videoSuggestionAdapter.notifyDataSetChanged()
        }

        if (tokenUser != null) {
            presenter?.getVideoSuggestionForUserData(tokenUser ?: "")
        } else {
            presenter?.getVideoSuggestionData()
        }

    }


    override fun onSuccessCarouselData(response: ObjectResponse<List<DataVideoSuggestion>>) {

        isGetCarouselSuccess = true

        val listFragmentSize = response.data?.size
        videoCarouselData.addAll(response.data ?: emptyList())


        if (listFragmentSize != null) {
            for (i in 0 until listFragmentSize) {
                featuredList.add(FeaturedFragment())
            }

        }
        presenter?.onShowFeaturedCarouselPresenter(featuredList, videoCarouselData)
    }

    override fun onSuccessCategoryData(objectResponse: ObjectResponse<List<DataCategory>>) {
        isGetCategorySuccess = true
        categoryList.addAll(objectResponse.data ?: emptyList())
        presenter?.onShowCategoryPresenter(categoryList)
    }

    override fun onSuccessVideoSuggestionData(videoSuggestionResponse: ObjectResponse<VideoSuggestion>) {
        isGetVideoSuggestionSuccess = true
        videoList.addAll(videoSuggestionResponse.data?.data ?: emptyList())
        presenter?.onShowVideoSuggestionPresenter(videoList)
    }

    override fun onSuccessVideoSuggestionForUser(videoSuggestionResponse: ObjectResponse<VideoSuggestion>) {
        isGetVideoSuggestionSuccess = true
        videoList.addAll(videoSuggestionResponse.data?.data ?: emptyList())
        presenter?.onShowVideoSuggestionPresenter(videoList)
    }

    override fun onErrorMoveData(error: String) {
        mListener?.onShowDisconnectDialog()
    }

    private val runnable = Runnable {
        binding.viewPager.currentItem = binding.viewPager.currentItem + 1
    }

    //Show Video To Carousel
    override fun onShowFeaturedCarousel(
        featuredFragmentList: ArrayList<FeaturedFragment>,
        videoCarouselData: ArrayList<DataVideoSuggestion>
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


        if (isGetCarouselSuccess && isGetCategorySuccess && isGetVideoSuggestionSuccess) {
            mListener?.onHideProgressBar()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000)
            }
        })

        carouselViewPagerAdapter.onClickVideoCarousel = object  : CarouselViewPagerAdapter.setListenerCarouselVideo{
            override fun onClickVideoCarousel(dataVideoCarousel: DataVideoSuggestion) {
                val activity = requireActivity() as AppCompatActivity
                val commentFragment = CommentFragment(dataVideoCarousel,null)
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame_main, commentFragment)
                    .commit()
            }

        }


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

        if (isGetCarouselSuccess && isGetCategorySuccess && isGetVideoSuggestionSuccess) {
            mListener?.onHideProgressBar()
        }

    }

    //Show list of Video Suggestion
    override fun onShowListVideoSuggestion(listVideoSuggestion: ArrayList<DataVideoSuggestion>) {

        videoSuggestionAdapter = VideoSuggestionAdapter(this, listVideoSuggestion)
        binding.listVideoSuggestion.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoSuggestionAdapter
        }

        if (isGetCarouselSuccess && isGetCategorySuccess && isGetVideoSuggestionSuccess) {
            mListener?.onHideProgressBar()
        }

    }
        videoSuggestionAdapter.onClickVideo = object : VideoSuggestionAdapter.setListenerVideoSuggestion{
            override fun onClickVideoSuggest(dataVideoSuggestion: DataVideoSuggestion) {
                val activity = requireActivity() as AppCompatActivity
                val commentFragment = CommentFragment(dataVideoSuggestion,null)
                activity.supportFragmentManager.beginTransaction().replace(R.id.content_frame_main,commentFragment).commit()
            }

        }
    }

}