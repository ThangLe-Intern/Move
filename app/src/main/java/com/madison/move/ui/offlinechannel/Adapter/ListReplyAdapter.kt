package com.madison.move.ui.offlinechannel.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.Comment

class ListReplyAdapter(var listReply:MutableList<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(val binding: ItemUserCommentBinding):RecyclerView.ViewHolder(binding.root){

        fun onBind(comment: Comment) {
           binding.line2.visibility = View.GONE
            binding.layoutShow.visibility = View.GONE
           binding.avatar.setImageResource(comment.user.avt)
           binding.username.text = comment.user.name
           binding.commentTime.text = comment.timeOfComment
           binding.commentContent.text = comment.content
            if (!comment.user.isTicked) {
               binding.bluetick
                    .visibility = View.GONE
            }
           binding.btnReply.visibility = View.INVISIBLE


            binding.cardViewReport.visibility = View.GONE
            binding.btnReport.setOnClickListener {
                if ( binding.cardViewReport.isGone) {
                    binding.cardViewReport.visibility = View.VISIBLE
                } else {
                    binding.cardViewReport.visibility = View.GONE
                    notifyDataSetChanged()
                }
            }

            binding.cardViewReport.setOnClickListener {
                binding.cardViewReport.visibility = View.GONE
                notifyDataSetChanged()
            }


            val replyLayout: RelativeLayout = itemView.findViewById(R.id.layout_userReply)
            replyLayout.visibility = View.GONE


            val isBtnLike = false
            val isBtnDiskLike = false
            binding.btnLikeTick.visibility = View.GONE
            binding.btnDisLiketike.visibility = View.GONE
            val number = 0

            binding.btnLike.setOnClickListener {
                if ( binding.btnLikeTick.isGone) {
                    binding.btnLikeTick.visibility = View.VISIBLE
                    binding.btnDisLiketike.visibility = View.GONE
                } else if (binding.btnLikeTick.isVisible && !isBtnLike) {
                    binding.btnLikeTick.visibility = View.GONE
                }
            }
            binding.btnDisLike.setOnClickListener {
                if (  binding.btnDisLiketike.isGone) {
                    binding.btnLikeTick.visibility = View.GONE
                    binding.btnDisLiketike.visibility = View.VISIBLE
                } else if (  binding.btnDisLiketike.isVisible && !isBtnDiskLike) {
                    binding.btnDisLiketike.visibility = View.GONE
                }


            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  ViewHolder(
            ItemUserCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
          )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listReply[position])
    }

    override fun getItemCount(): Int {
        return listReply.size
    }


}