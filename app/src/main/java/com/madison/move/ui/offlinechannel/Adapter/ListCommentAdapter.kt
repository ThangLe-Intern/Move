package com.madison.move.ui.offlinechannel.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
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

    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(comment: Comment) {

            val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)

            adapterReply = ListReplyAdapter(comment.listChild)
            itemView.findViewById<RecyclerView>(R.id.listReply).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterReply
            }


            binding.apply {
                avatar.setImageResource(comment.user.avt)
                username.text = comment.user.name
                commentTime.text = comment.timeOfComment
                commentContent.text = comment.content
                listReply.visibility = View.VISIBLE
            }


            binding.layoutShow.setOnClickListener {
                binding.apply {
                    listReply.visibility =
                        if (listReply.isVisible) View.GONE else View.VISIBLE
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
            if (comment.listChild.isNotEmpty()) {
                binding.layoutShow.visibility = View.VISIBLE
            } else {
                binding.layoutShow.visibility = View.GONE
            }

            binding.apply {
                btnLikeTick.visibility = View.GONE
                btnLike.setOnClickListener {
                    if (btnLikeTick.isGone) {
                        btnLikeTick.visibility = View.VISIBLE
                        btnDisLiketike.visibility = View.GONE
                    } else if (binding.btnLikeTick.isVisible) {
                        btnLikeTick.visibility = View.GONE
                    }
                }

                btnDisLiketike.visibility = View.GONE
                btnDisLike.setOnClickListener {
                    if (btnDisLiketike.isGone) {
                        btnLikeTick.visibility = View.GONE
                        btnDisLiketike.visibility = View.VISIBLE
                    } else if (btnDisLiketike.isVisible) {
                        btnDisLiketike.visibility = View.GONE
                    }
                }

                if (!comment.user.isTicked) {
                    bluetick.visibility = View.GONE
                }

                var isReportVisible = false
                cardViewReport.visibility = View.GONE
                btnReport.setOnClickListener {

                    isReportVisible = !isReportVisible
                    cardViewReport.visibility = if (isReportVisible) View.VISIBLE else View.GONE
                }



              rootView.setOnClickListener {
                    if (isReportVisible) {
                        isReportVisible = false
                        cardViewReport.visibility = View.GONE
                    }
                }

                cardViewReport.setOnClickListener {
                    cardViewReport.visibility = View.GONE
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

                        replyListener.userComment(
                            cancelReplyButton,
                            sendButtonReply,
                            edtUserCommentReply,
                            comment.listChild,
                            listReply,
                            user4
                        )
                    } else {
                        layoutUserReply.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemUserCommentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(listComment[position])
    }

    override fun getItemCount(): Int {
        return listComment.size
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
            editText: AppCompatEditText,
            cancelButton: AppCompatButton,
            sendButton: AppCompatButton
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