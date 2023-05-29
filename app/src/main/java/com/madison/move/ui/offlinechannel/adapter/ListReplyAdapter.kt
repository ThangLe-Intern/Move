package com.madison.move.ui.offlinechannel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.Comment

class ListReplyAdapter(var listReply: MutableList<Comment>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(comment: Comment) {
            binding.apply {
                line2.visibility = View.GONE
                layoutShow.visibility = View.GONE
                avatar.setImageResource(comment.user.avt)
                username.text = comment.user.name
                commentTime.text = comment.timeOfComment
                commentContent.text = comment.content
                if (!comment.user.isTicked) {
                    bluetick
                        .visibility = View.GONE
                }
                btnReply.visibility = View.INVISIBLE

                cardViewReport.visibility = View.GONE
                btnReport.setOnClickListener {
                    if (cardViewReport.isGone) {
                        cardViewReport.visibility = View.VISIBLE
                    } else {
                        cardViewReport.visibility = View.GONE
                    }
                }

                cardViewReport.setOnClickListener {
                    cardViewReport.visibility = View.GONE
                }

                layoutUserReply.visibility = View.GONE

                btnLikeTick.visibility = View.GONE
                btnDisLiketike.visibility = View.GONE

                btnLike.setOnClickListener {
                    if (btnLikeTick.isGone) {
                        btnLikeTick.visibility = View.VISIBLE
                        btnDisLiketike.visibility = View.GONE
                    } else if (btnLikeTick.isVisible) {
                        btnLikeTick.visibility = View.GONE
                    }
                }
                btnDisLike.setOnClickListener {
                    if (btnDisLiketike.isGone) {
                        btnLikeTick.visibility = View.GONE
                        btnDisLiketike.visibility = View.VISIBLE
                    } else if (btnDisLiketike.isVisible) {
                        btnDisLiketike.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemUserCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listReply[position])
    }

    override fun getItemCount(): Int {
        return listReply.size
    }


}