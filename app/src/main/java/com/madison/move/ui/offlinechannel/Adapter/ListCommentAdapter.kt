package com.madison.move.ui.offlinechannel.Adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.Comment
import com.madison.move.ui.offlinechannel.DataModelComment

class ListCommentAdapter(
    private var context: Context,
    var listComment: MutableList<Comment>,
    val replyListener: ReplyListener,
) :

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var adapterReply: ListReplyAdapter

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }


    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun onBind(comment: Comment) {

            val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)

            val adapterReply = comment.listChild?.let { ListReplyAdapter(it,context) } ?: ListReplyAdapter(
                listComment, context
            )
            itemView.findViewById<RecyclerView>(R.id.listReply).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterReply
            }
            binding.apply {
                comment.user?.avt?.let { avatar.setImageResource(it) }
                username.text = comment.user?.name
                commentTime.text = comment.timeOfComment
                commentContent.text = comment.content
                listReply.visibility = View.VISIBLE
            }
            binding.apply {
                layoutShow.setOnClickListener {
                    listReply.visibility = if (listReply.isVisible) View.GONE else View.VISIBLE
                    imgArrowDownGreen.setImageResource(
                        if (listReply.isVisible) R.drawable.ic_ic_arrow_up_green
                        else R.drawable.ic_arrow_down_green
                    )
                    txtShow.text = context.getString(
                        if (listReply.isVisible) R.string.Hide
                        else R.string.Show
                    )
                }
            }
            if (comment.listChild?.isNotEmpty() == true) {
                binding.layoutShow.visibility = View.VISIBLE
            } else {
                binding.layoutShow.visibility = View.GONE
            }

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

                if (comment.user?.isTicked == true) {
                    bluetick.visibility = View.GONE
                }
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

            binding.apply {
                layoutUserReply.visibility = View.GONE
                btnReply.setOnClickListener {
                    if (layoutUserReply.isGone) {
                        layoutUserReply.visibility = View.VISIBLE

                        comment.listChild?.let { it1 ->
                            replyListener.userComment(
                                cancelReplyButton,
                                sendButtonReply,
                                edtUserCommentReply,
                                it1,
                                listReply,
                                user4
                            )
                        }
                    } else {
                        layoutUserReply.visibility = View.GONE
                    }
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
        return if (listComment.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    interface ReplyListener {
        fun userComment(
            cancelButton: AppCompatButton,
            sendButton: AppCompatButton,
            editText: AppCompatEditText,
            listComment: MutableList<Comment>,
            list: RecyclerView,
            user: DataModelComment
        )

        fun onWriteCommentListener(
            editText: AppCompatEditText, cancelButton: AppCompatButton, sendButton: AppCompatButton
        )

        fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText)
        fun onSendUserReply(
            sendButton: AppCompatButton,
            list: RecyclerView,
            listComment: MutableList<Comment>,
            editText: AppCompatEditText,
            cancelButton: AppCompatButton,
            user: DataModelComment
        )

        fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton)
        fun hideKeyboard(view: View)
    }
}