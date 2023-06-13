package com.madison.move.ui.offlinechannel.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.madison.move.R
import com.madison.move.data.model.comment.DataComment
import com.madison.move.databinding.ItemUserCommentBinding

class ListReplyAdapter(
    private var context: Context,
    var listReply: MutableList<DataComment>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onClickListReplyComment: ListCommentAdapter.ReplyListener? = null
    var onClickDisLikeReplyComment: ListCommentAdapter.ReplyListener? = null


    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun onBind(dataComment: DataComment) {
            binding.apply {

                userAvatarReply.setImageResource(R.drawable.avatar)

                line2.visibility = View.GONE
                layoutShow.visibility = View.GONE


                btnReply.visibility = View.INVISIBLE

                btnReport.setOnClickListener {
                    val inflater = LayoutInflater.from(context)
                    val dialogView = inflater.inflate(R.layout.dialog_report, null)

                    val popupWindow = PopupWindow(
                        dialogView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true
                    )

                    val location = IntArray(2)
                    btnReport.getLocationInWindow(location)

                    val x = location[0] - dialogView.width - 1 // Dịch dialog sang bên trái 1 đơn vị
                    val y = location[1] - dialogView.height

                    popupWindow.showAtLocation(btnReport, Gravity.NO_GRAVITY, x, y)

                    dialogView.viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            dialogView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                            val popupX = x - (dialogView.width - btnReport.width) / 2
                            val popupY = y - dialogView.height

                            popupWindow.update(popupX, popupY, -1, -1, true)
                        }
                    })

                    dialogView.setOnTouchListener { _, _ ->
                        popupWindow.dismiss()
                        true
                    }
                }

                layoutUserReply.visibility = View.GONE


                //Set User Reply Info
                //Set User Comment Info
                if (dataComment.isLiked == true) {
                    btnLike.setImageResource(R.drawable.ic_lickticked)
//                    btnDisLike.setImageResource(R.drawable.ic_disklikenottick)
                }

                if (dataComment.isDisliked == true) {
                    btnDisLike.setImageResource(R.drawable.ic_diskliketicked)
//                    btnLike.setImageResource(R.drawable.ic_likenottick)
                }
                btnLike.setOnClickListener {
                    dataComment.id?.let { id -> onClickListReplyComment?.onClickListReplyComment(id) }
                }
                btnDisLike.setOnClickListener {
                    dataComment.id?.let { id ->
                        onClickDisLikeReplyComment?.onClickDisLikeReplyComment(
                            id
                        )
                    }
                }

                //Set Avatar
                if (dataComment.user?.img != null) {
                    Glide.with(context).load(dataComment.user.img).into(avatar)
                } else {
                    avatar.setImageResource(R.drawable.avatar)
                }

                //Set Username
                username.text =
                    dataComment.user?.username ?: context.getString(R.string.txt_no_us_name)

                //Set Role
                if (dataComment.user?.role == 0) {
                    bluetick.visibility = View.GONE
                } else {
                    bluetick.visibility = View.VISIBLE
                }

                //Set Comment Time
                commentTime.text = dataComment.createdTime ?: ""

                //Set Content Comment
                commentContent.text = dataComment.content ?: ""

                //Set User Like or Dislike Comment


                if (dataComment.likeCount != null && dataComment.likeCount > 0) {
                    numberLike.visibility = View.VISIBLE
                    numberLike.text = dataComment.likeCount.toString()
                } else {
                    numberLike.visibility = View.GONE
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