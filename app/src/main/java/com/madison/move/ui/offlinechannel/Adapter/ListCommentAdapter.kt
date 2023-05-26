package com.madison.move.ui.offlinechannel.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.Comment
import com.madison.move.ui.offlinechannel.DataModelComment
class ListCommentAdapter(
    var listComment: MutableList<Comment>,
    val replyListener: ReplyListener,

    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var adapterReply: ListReplyAdapter

    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(comment: Comment) {

            val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)

            val listReply = comment.listChild

            adapterReply = ListReplyAdapter(listReply)
            itemView.findViewById<RecyclerView>(R.id.listReply).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterReply
            }

            binding.avatar.setImageResource(comment.user.avt)
            binding.username.text = comment.user.name
            binding.commentTime.text = comment.timeOfComment
            binding.commentContent.text = comment.content

            binding.listReply.visibility = View.VISIBLE
            binding.layoutShow.setOnClickListener {
                binding.apply {
                    binding.listReply.visibility =
                        if (binding.listReply.isVisible) View.GONE else View.VISIBLE
                    imgArrowDownGreen.setImageResource(
                        if (binding.listReply.isVisible) R.drawable.ic_ic_arrow_up_green
                        else R.drawable.ic_arrow_down_green
                    )
                    txtShow.setText(
                        if (binding.listReply.isVisible) "Hide   replies"
                        else "Show replies"
                    )
                }
            }
            if (comment.listChild.isNotEmpty()){
                binding.layoutShow.visibility = View.VISIBLE
            }else{
                binding.layoutShow.visibility = View.GONE
            }


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
                    binding.btnDisLiketike.visibility =View.VISIBLE
                } else if (  binding.btnDisLiketike.isVisible && !isBtnDiskLike) {
                    binding.btnDisLiketike.visibility = View.GONE
                }


            }


            if (!comment.user.isTicked) {
                binding.bluetick.visibility = View.GONE
            }


            binding.cardViewReport.visibility = View.GONE
            binding.btnReport.setOnClickListener {

                if (binding.cardViewReport.isGone) {
                    binding.cardViewReport.visibility = View.VISIBLE
                } else {
                    binding.cardViewReport.visibility = View.GONE
                }
            }

            binding.cardViewReport.setOnClickListener {
                binding.cardViewReport.visibility = View.GONE
            }
            binding.sendButtonReply.setOnClickListener {
                notifyDataSetChanged()
            }


            /////////

            binding.layoutUserReply.visibility = View.GONE



            ///////
            itemView.findViewById<AppCompatTextView>(R.id.btnReply).setOnClickListener {
                if (binding.layoutUserReply.isGone) {
                    binding.layoutUserReply.visibility = View.VISIBLE

                    replyListener.userComment(
                        binding.cancelReplyButton,
                        binding.sendButtonReply,
                        binding.edtUserCommentReply,
                        listReply,
                        binding.listReply,
                        user4
                    )
                } else {
                    binding.layoutUserReply.visibility = View.GONE
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