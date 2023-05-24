package com.madison.move.ui.offlinechannel.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.ui.offlinechannel.Comment

class ListReplyAdapter(var listReply:MutableList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        fun onBind(comment: Comment) {
            itemView.findViewById<RelativeLayout>(R.id.line2).visibility = View.GONE
            itemView.findViewById<ImageView>(R.id.avatar).setImageResource(comment.user.avt)
            itemView.findViewById<TextView>(R.id.username).text = comment.user.name
            itemView.findViewById<TextView>(R.id.commentTime).text = comment.timeOfComment
            itemView.findViewById<TextView>(R.id.commentContent).text = comment.content
            if (!comment.user.isTicked) {
                itemView.findViewById<ImageView>(R.id.bluetick)
                    .visibility = View.GONE
            }
            itemView.findViewById<AppCompatTextView>(R.id.btnReply).visibility = View.INVISIBLE

            val reportCardView: CardView = itemView.findViewById(R.id.cardView_Report)
            val reportButton: AppCompatImageView = itemView.findViewById(R.id.btn_report)
            reportCardView.visibility = View.GONE
            reportButton.setOnClickListener {
                if (reportCardView.isGone) {
                    reportCardView.visibility = View.VISIBLE
                } else {
                    reportCardView.visibility = View.GONE
                }
            }

            reportCardView.setOnClickListener {
                reportCardView.visibility = View.GONE
            }

            val replyLayout: RelativeLayout = itemView.findViewById(R.id.layout_userReply)
            replyLayout.visibility = View.GONE

            val btnLike : ImageView = itemView.findViewById(R.id.btn_like)
            val btnDiskLike: ImageView = itemView.findViewById(R.id.btnDisLike)
            val btnLikeTick: ImageView = itemView.findViewById(R.id.btn_like_tick)
            val btnDiskLikeTick : ImageView = itemView.findViewById(R.id.btnDisLiketike)

            btnDiskLikeTick.visibility = View.GONE
            btnLikeTick.visibility= View.GONE
            var isBtnLike = false
            var isBtnDiskLike = false
            btnLike.setOnClickListener {
                if (btnLikeTick.isGone){
                    btnLikeTick.visibility = View.VISIBLE
                    btnDiskLikeTick.visibility = View.GONE
                }else if (btnLikeTick.isVisible && !isBtnLike){
                    btnLikeTick.visibility = View.GONE
                }
            }
            btnDiskLike.setOnClickListener {
                if (btnDiskLikeTick.isGone){
                    btnLikeTick.visibility= View.GONE
                    btnDiskLikeTick.visibility = View.VISIBLE
                }else if (btnDiskLikeTick.isVisible && !isBtnDiskLike){
                    btnDiskLikeTick.visibility = View.GONE
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_comment, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listReply[position])
    }

    override fun getItemCount(): Int {
        return listReply.size
    }


}