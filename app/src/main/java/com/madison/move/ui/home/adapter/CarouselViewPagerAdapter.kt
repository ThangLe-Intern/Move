package com.madison.move.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.madison.move.R
import com.madison.move.data.model.carousel.DataVideoCarousel
import com.madison.move.ui.home.FeaturedFragment
import com.madison.move.ui.home.HomeFragment
import com.madison.move.ui.offlinechannel.CommentFragment
import kotlin.math.roundToInt

class CarouselViewPagerAdapter(
    var activity: HomeFragment,
    var listFragment: ArrayList<FeaturedFragment>,
    var videoCarouselData:ArrayList<DataVideoCarousel>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(fragment: FeaturedFragment,videoCarousel:DataVideoCarousel) {

            itemView.findViewById<AppCompatTextView>(R.id.txt_feature_username).text = videoCarousel.username
            itemView.findViewById<AppCompatTextView>(R.id.txt_feature_video_category).text = videoCarousel.category_name
            itemView.findViewById<AppCompatTextView>(R.id.txt_feature_video_title).text = videoCarousel.title
            itemView.findViewById<AppCompatTextView>(R.id.txt_view_count).text = videoCarousel.count_view.toString()

            val roundOff = (videoCarousel.rating * 100.0).roundToInt() / 100.0
            itemView.findViewById<AppCompatTextView>(R.id.txt_featured_rate_number).text = roundOff.toString()


            val imageView = itemView.findViewById<AppCompatImageView>(R.id.img_featured_video_thumbnail)
            if (videoCarousel.img != "null"){
                Glide.with(activity)
                    .load(videoCarousel.thumbnail)
                    .into(imageView);
            }



            itemView.findViewById<ConstraintLayout>(R.id.layout_feature_fragment).setOnClickListener {
                val activity: AppCompatActivity = it.context as AppCompatActivity
                val commentFragment = CommentFragment()
                activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame_main, commentFragment)
                    .commit()

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_featured, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == listFragment.lastIndex) {
            viewPager2.post(runnable)
        }

        (holder as ViewHolder).onBind(listFragment[position],videoCarouselData[position])

    }


    private val runnable = Runnable {
//        viewPager2.currentItem = 0
        listFragment.addAll(listFragment)
        videoCarouselData.addAll(videoCarouselData)
        notifyDataSetChanged()
    }
}