package com.madison.move.ui.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.data.model.MoveVideo
import com.madison.move.ui.offlinechannel.CommentFragment

class VideoSuggestionAdapter(var listVideo: MutableList<MoveVideo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(video: MoveVideo) {

            itemView.findViewById<ConstraintLayout>(R.id.layout_video_suggestion).setOnClickListener {
                val activity:AppCompatActivity = it.context as AppCompatActivity
                val commentFragment = CommentFragment()

                activity.supportFragmentManager.beginTransaction().replace(R.id.content_frame_main,commentFragment).commit()
            }

            video.thumbnail?.let {
                itemView.findViewById<ImageView>(R.id.img_video_suggestion_thumbnail)
                    .setImageResource(
                        it
                    )
            }
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_view).text = "${video.view}k"
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_time).text =
                "${video.time}:00"
            video.user?.avatar?.let {
                itemView.findViewById<ImageView>(R.id.img_video_suggestion_user_avatar)
                    .setImageResource(
                        it
                    )
            }
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_username).text =
                video.user?.fullname
            itemView.findViewById<AppCompatTextView>(R.id.txt_title_of_video_suggestion).text =
                video.title
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_category).text =
                "${video.category?.name} • A day ago"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_video_suggestion, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listVideo[position])
    }

}