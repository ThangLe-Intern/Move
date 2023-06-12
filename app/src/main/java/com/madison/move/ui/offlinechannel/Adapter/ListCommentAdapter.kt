package com.madison.move.ui.offlinechannel.Adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.madison.move.R
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.DataUser
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.CommentFragment

class ListCommentAdapter(
    private var context: Context,
    var listComment: MutableList<DataComment>,
    val replyListener: ReplyListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterReply: ListReplyAdapter? = null
    private var getSharedPreferences: SharedPreferences? = null
    private var userData: DataUser? = null


    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
        const val USER_DATA = "user"

    }


    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun onBind(dataComment: DataComment) {

            getSharedPreferences = context.getSharedPreferences(
                CommentFragment.TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
            )

            val jsonUser = getSharedPreferences?.getString(USER_DATA, null)
            userData = Gson().fromJson(jsonUser, DataUser::class.java)

            val adapterReply = ListReplyAdapter(
                context, dataComment.replies as MutableList<DataComment>
            )

            //Add Reply Data
            binding.listReply.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterReply
            }

            //Handle Show/Hide Reply
            binding.apply {
                if (dataComment.replies.isEmpty()) {
                    layoutShow.visibility = View.GONE
                } else {
                    layoutShow.visibility = View.VISIBLE
                    txtShow.text =
                        context.getString(R.string.Show, dataComment.replies.size.toString() ?: "")

                    layoutShow.setOnClickListener {
                        listReply.visibility = if (listReply.isGone) View.VISIBLE else View.GONE
                        imgArrowDownGreen.setImageResource(
                            if (listReply.isVisible) R.drawable.ic_ic_arrow_up_green
                            else R.drawable.ic_arrow_down_green
                        )

                        if (listReply.isVisible) {
                            txtShow.text = context.getString(
                                R.string.Hide, dataComment.replies.size.toString() ?: ""
                            )
                        } else {
                            txtShow.text = context.getString(
                                R.string.Show, dataComment.replies.size.toString() ?: ""
                            )
                        }

                    }
                }
            }

            //Handle Btn Report
            binding.apply {
                //Handle Button Report
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

                    val x = location[0] - dialogView.width - 1
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

                sendButtonReply.setOnClickListener {
                    notifyDataSetChanged()
                }
            }

            //Set User Comment Info
            binding.apply {

                //Set Avatar
                if (dataComment.user?.img != null) {
                    Glide.with(context).load(dataComment.user.img).into(binding.avatar)
                } else {
                    binding.avatar.setImageResource(R.drawable.avatar)
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
                if (dataComment.isLiked == true) {
                    btnLikeTick.visibility = View.VISIBLE
                    btnLike.visibility = View.GONE
                    btnDisLiketike.visibility = View.GONE
                }

                if (dataComment.isDisliked == true) {
                    btnLikeTick.visibility = View.GONE
                    btnDisLike.visibility = View.GONE
                    btnDisLiketike.visibility = View.VISIBLE
                }

                if (dataComment.likeCount != null && dataComment.likeCount > 0) {
                    numberLike.visibility = View.VISIBLE
                    numberLike.text = dataComment.likeCount.toString()
                } else {
                    numberLike.visibility = View.GONE
                }
            }

            //Handle Like-Dislike
            binding.apply {
                var currentNumber: Int? = 0
                btnLikeTick.visibility = View.GONE
                btnLike.setOnClickListener {
                    if (btnLikeTick.isGone) {
                        btnLikeTick.visibility = View.VISIBLE
                        currentNumber = currentNumber?.plus(1) ?: 1
                        numberLike.text = currentNumber.toString()
                        btnDisLiketike.visibility = View.GONE
                    } else if (btnLikeTick.isVisible) {
                        btnLikeTick.visibility = View.GONE
                        currentNumber = currentNumber?.minus(1) ?: 0
                        numberLike.text = currentNumber.toString()
                    }
                }

                //Handle Dislike Button
                btnDisLiketike.visibility = View.GONE
                btnDisLike.setOnClickListener {
                    if (btnDisLiketike.isGone) {
                        if (btnLikeTick.isVisible) {
                            currentNumber = currentNumber?.minus(1) ?: 0
                            numberLike.text = currentNumber.toString()
                        }
                        btnLikeTick.visibility = View.GONE
                        btnDisLiketike.visibility = View.VISIBLE
                    } else if (btnDisLiketike.isVisible) {
                        btnDisLiketike.visibility = View.GONE
                    }
                }
            }

            //Handle Show/Hide Reply Button
            binding.apply {
                layoutUserReply.visibility = View.GONE

                if (userData != null) {
                    if (userData?.img != null) {
                        Glide.with(context).load(userData?.img).into(userAvatarReply)
                    } else {
                        userAvatarReply.setImageResource(R.drawable.avatar)
                    }
                    btnReply.setOnClickListener {
                        if (layoutUserReply.isGone) {
                            layoutUserReply.visibility = View.VISIBLE

                            replyListener.userComment(
                                cancelReplyButton,
                                sendButtonReply,
                                edtUserCommentReply,
                                dataComment.id ?: 0
                            )

                        } else {
                            layoutUserReply.visibility = View.GONE
                        }
                    }
                } else {
                    btnReply.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            ViewHolder(
                ItemUserCommentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            ViewHolder(
                ItemUserCommentBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listComment[position])

    }

    override fun getItemCount(): Int {
        return if (listComment == null) 0 else listComment.size
    }

    override fun getItemViewType(position: Int): Int {
//        return VIEW_TYPE_ITEM
        return if (listComment.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    interface ReplyListener {
        fun userComment(
            cancelButton: AppCompatButton,
            sendButton: AppCompatButton,
            editText: AppCompatEditText,
            parentCommentId: Int
        )

        fun onWriteCommentListener(
            editText: AppCompatEditText, cancelButton: AppCompatButton, sendButton: AppCompatButton
        )

        fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText)
        fun onSendUserReply(
            sendButton: AppCompatButton,
            parentCommentId: Int,
            editText: AppCompatEditText,
            cancelButton: AppCompatButton
        )

        fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton)
        fun hideKeyboard(view: View)
    }
}