package com.madison.move.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.data.model.MoveVideo

class VideoSuggestionAdapter(var listVideo:MutableList<MoveVideo>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class ViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem){
        fun onBind(video: MoveVideo){
            itemView.findViewById<ImageView>(R.id.img_video_suggestion_thumbnail).setImageResource(video.thumbnail)
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_view).text = "${video.view}k"
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_time).text = "${video.time}:00"
            itemView.findViewById<ImageView>(R.id.img_video_suggestion_user_avatar).setImageResource(video.user.avatar)
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_username).text = video.user.fullname
            itemView.findViewById<AppCompatTextView>(R.id.txt_title_of_video_suggestion).text = video.title
            itemView.findViewById<TextView>(R.id.txt_video_suggestion_category).text = "${video.category.name} • A day ago"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_video_suggestion,parent,false))
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listVideo[position])
    }

}