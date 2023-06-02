package com.madison.move.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.madison.move.R
import com.madison.move.data.model.carousel.DataVideoCarousel
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.databinding.FragmentFeaturedBinding
import com.madison.move.ui.home.FeaturedFragment
import com.madison.move.ui.home.HomeFragment
import com.madison.move.ui.offlinechannel.CommentFragment
import kotlin.math.roundToInt

class CarouselViewPagerAdapter(
    var activity: HomeFragment,
    var listFragment: ArrayList<FeaturedFragment>,
    var videoCarouselData: ArrayList<DataVideoSuggestion>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickVideoCarousel : ListenerCarouselVideo ?= null

    fun onClick(onClickCarousel: ListenerCarouselVideo){
        this.onClickVideoCarousel = onClickCarousel
    }

    inner class ViewHolder(val binding: FragmentFeaturedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(fragment: FeaturedFragment, videoCarousel: DataVideoSuggestion) {
            binding.apply {
                layoutFeatureFragment.setOnClickListener {
                    onClickVideoCarousel?.onClickVideoCarousel(videoCarousel)
                }

                txtFeatureUsername.text = videoCarousel.username
                txtFeatureVideoTitle.text = videoCarousel.title
                txtViewCount.text = videoCarousel.countView.toString()
                txtFeatureVideoCategory.text = videoCarousel.categoryName
            }

            if (videoCarousel.categoryName != null && videoCarousel.categoryName == "Just Move") {
                binding.cardViewVideoFeaturedDuration.visibility = View.INVISIBLE
                binding.cardViewLevelOfUser.visibility = View.INVISIBLE
            }else{
                binding.cardViewVideoFeaturedDuration.visibility = View.VISIBLE
                binding.cardViewLevelOfUser.visibility = View.VISIBLE

                when(videoCarousel.level){
                    1 -> binding.txtLevelOfUser.text = activity.getString(R.string.txt_level_beginner)
                    2 -> binding.txtLevelOfUser.text = activity.getString(R.string.txt_level_inter)
                    3 -> binding.txtLevelOfUser.text = activity.getString(R.string.txt_level_advanced)
                }

                when(videoCarousel.duration){
                    1 -> binding.txtVideoFeaturedDuration.text = activity.getString(R.string.timeOfCategory)
                    2 -> binding.txtVideoFeaturedDuration.text = activity.getString(R.string.duration_second)
                    3 -> binding.txtVideoFeaturedDuration.text = activity.getString(R.string.duration_third)
                }
            }

            val roundOff = (videoCarousel.rating?.times(100.0))?.roundToInt()?.div(100.0)
            binding.txtFeaturedRateNumber.text = roundOff.toString()

            if (videoCarousel.thumbnail != null) {
                Glide.with(activity)
                    .load(videoCarousel.thumbnail)
                    .into(binding.imgFeaturedVideoThumbnail);
            }

            if (videoCarousel.img != null) {
                Glide.with(activity)
                    .load(videoCarousel.img)
                    .into(binding.imgFeaturedUserAvatar)
            } else {
                binding.imgFeaturedUserAvatar.setImageResource(R.drawable.avatar)
            }




        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            FragmentFeaturedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == listFragment.lastIndex) {
            viewPager2.post(runnable)
        }

        (holder as ViewHolder).onBind(listFragment[position], videoCarouselData[position])

    }


    private val runnable = Runnable {
//        viewPager2.currentItem = 0
        listFragment.addAll(listFragment)
        videoCarouselData.addAll(videoCarouselData)
        notifyDataSetChanged()
    }

    interface ListenerCarouselVideo{
        fun onClickVideoCarousel(dataVideoCarousel: DataVideoSuggestion)
    }
}