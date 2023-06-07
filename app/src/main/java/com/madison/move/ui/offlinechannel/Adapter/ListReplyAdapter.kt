package com.madison.move.ui.offlinechannel.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.Comment

class ListReplyAdapter(
    var listReply: MutableList<Comment>,
    private var context: Context,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
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

                btnLikeTick.visibility = View.GONE
                btnDisLiketike.visibility = View.GONE


                var currentNumber = 0
                btnLikeTick.visibility = View.GONE
                btnLike.setOnClickListener {
                    if (btnLikeTick.isGone) {
                        btnLikeTick.visibility = View.VISIBLE
                        currentNumber++
                        numberLike.text = currentNumber.toString()
                        btnDisLiketike.visibility = View.GONE
                    } else if (btnLikeTick.isVisible) {
                        btnLikeTick.visibility = View.GONE
                        currentNumber--
                        numberLike.text = currentNumber.toString()
                    }
                }

                btnDisLiketike.visibility = View.GONE
                btnDisLike.setOnClickListener {
                    if (btnDisLiketike.isGone) {
                        if (btnLikeTick.isVisible) {
                            currentNumber--
                            numberLike.text = currentNumber.toString()
                        }
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