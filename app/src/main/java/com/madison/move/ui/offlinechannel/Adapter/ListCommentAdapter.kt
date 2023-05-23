package com.madison.move.ui.offlinechannel.Adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.madison.move.R
import com.madison.move.ui.offlinechannel.Comment
import com.madison.move.ui.offlinechannel.DataModelComment
import java.util.zip.Inflater

class ListCommentAdapter(
    var listComment: MutableList<Comment>,
    val replyListener: ReplyListener,

    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var adapterReply: ListReplyAdapter

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(comment: Comment) {
            ///////////
            val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)

            var listReply = comment.listChild

            adapterReply = ListReplyAdapter(listReply)
            itemView.findViewById<RecyclerView>(R.id.listReply).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterReply
            }

            itemView.findViewById<ImageView>(R.id.avatar).setImageResource(comment.user.avt)
            itemView.findViewById<TextView>(R.id.username).text = comment.user.name
            itemView.findViewById<TextView>(R.id.commentTime).text = comment.timeOfComment
            itemView.findViewById<TextView>(R.id.commentContent).text = comment.content

            val btnLike: ImageView =itemView.findViewById(R.id.btnLike)
            val btnDiskLike: ImageView = itemView.findViewById(R.id.btnDisLike)




            if (!comment.user.isTicked) {
                itemView.findViewById<ImageView>(R.id.bluetick)
                    .visibility = View.GONE
            }

            //////
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


            /////////
            val replyLayout: RelativeLayout = itemView.findViewById(R.id.layout_userReply)
            replyLayout.visibility = View.GONE

            val cancelReply: AppCompatButton = itemView.findViewById(R.id.cancelReply_button)
            val sendReply: AppCompatButton = itemView.findViewById(R.id.send_buttonReply)
            val edtUserReply: AppCompatEditText = itemView.findViewById(R.id.edt_userCommentReply)

            ///////
            itemView.findViewById<AppCompatTextView>(R.id.btnReply).setOnClickListener {
                if (replyLayout.isGone) {
                    replyLayout.visibility = View.VISIBLE

                    replyListener.userComment(
                        cancelReply,
                        sendReply,
                        edtUserReply,
                        listReply,
                        itemView.findViewById(R.id.listReply),
                        user4
                    )
                } else {
                    replyLayout.visibility = View.GONE
                }
            }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_comment, parent, false)
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