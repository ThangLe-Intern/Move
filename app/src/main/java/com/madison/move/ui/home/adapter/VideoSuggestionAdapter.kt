package com.madison.move.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madison.move.R
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.databinding.ItemVideoSuggestionBinding
import com.madison.move.ui.home.HomeFragment
import com.madison.move.ui.offlinechannel.CommentFragment
import kotlin.math.roundToInt

class VideoSuggestionAdapter(
    var activity: HomeFragment, var listVideo: ArrayList<DataVideoSuggestion>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onClickVideo: setListenerVideoSuggestion ?= null
    fun onClick(onClick: setListenerVideoSuggestion){
        this.onClickVideo = onClick
    }

    inner class ViewHolder(val binding: ItemVideoSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(video: DataVideoSuggestion) {

            binding.apply {
                txtVideoSuggestionUsername.text = video.username ?: ""
                txtVideoSuggestionUploadTime.text = activity.getString(
                    R.string.video_post_time, video.postedDayAgo.toString()
                )
                txtVideoSuggestionCategory.text = video.categoryName ?: ""
                txtTitleOfVideoSuggestion.text = video.title ?: ""

                val viewCount = video.countView ?: 0
                txtVideoSuggestionView.text = viewCount.toString()
            }

            val roundOff = (video.rating?.times(100.0))?.roundToInt()?.div(100.0) ?: 0
            binding.txtVideoSuggestionRateNumber.text = roundOff.toString()

            if (video.thumbnail != null) {
                Glide.with(activity).load(video.thumbnail)
                    .into(binding.imgVideoSuggestionThumbnail);
            }

            if (video.img != null) {
                Glide.with(activity).load(video.img).into(binding.imgVideoSuggestionUserAvatar)
            } else {
                binding.imgVideoSuggestionUserAvatar.setImageResource(R.drawable.avatar)
            }


            if (video.categoryName != null && video.categoryName == "Just Move") {
                binding.cardViewVideoSuggestionDuration.visibility = View.GONE
                binding.cardViewVideoSuggestionUserLevel.visibility = View.GONE
            } else {
                binding.cardViewVideoSuggestionDuration.visibility = View.VISIBLE
                binding.cardViewVideoSuggestionUserLevel.visibility = View.VISIBLE

                when (video.level) {
                    1 -> binding.txtVideoSuggestionUserLevel.text =
                        activity.getString(R.string.txt_level_beginner)
                    2 -> binding.txtVideoSuggestionUserLevel.text =
                        activity.getString(R.string.txt_level_inter)
                    3 -> binding.txtVideoSuggestionUserLevel.text =
                        activity.getString(R.string.txt_level_advanced)
                }

                when (video.duration) {
                    1 -> binding.txtVideoSuggestionDuration.text =
                        activity.getString(R.string.timeOfCategory)
                    2 -> binding.txtVideoSuggestionDuration.text =
                        activity.getString(R.string.duration_second)
                    3 -> binding.txtVideoSuggestionDuration.text =
                        activity.getString(R.string.duration_third)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemVideoSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listVideo[position])
    }

    interface setListenerVideoSuggestion{
        fun onClickVideoSuggest(dataVideoSuggestion: DataVideoSuggestion)
    }
}