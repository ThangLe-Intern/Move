package com.madison.move.ui.offlinechannel.Adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
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
    var replyParentId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterReply: ListReplyAdapter? = null
    private var getSharedPreferences: SharedPreferences? = null
    private var userData: DataUser? = null


    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
        const val USER_DATA = "user"

    }

    var onClickListComment: setListenerListComment? = null


    interface setListenerListComment {
        fun onClickListComment(commentId: Int)
        fun onClickDisLikeComment(commentId: Int)
    }


    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun onBind(dataComment: DataComment) {
            binding.listReply.isNestedScrollingEnabled = false

            getSharedPreferences = context.getSharedPreferences(
                CommentFragment.TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
            )

            val jsonUser = getSharedPreferences?.getString(USER_DATA, null)
            userData = Gson().fromJson(jsonUser, DataUser::class.java)

            var adapterReply = ListReplyAdapter(
                context, dataComment.replies as MutableList<DataComment>,
                replyListener
            )


            //Add Reply Data
            binding.listReply.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterReply
            }

            //Handle Show/Hide Reply
            binding.apply {

                if (dataComment.user?.isSuspended == 1) {
                    dataComment.user.img = avatar.setImageResource(R.drawable.ic_avatar_banned).toString()
                    dataComment.user.username = context.getString(R.string.userband)
                    dataComment.content = context.getString(R.string.commentband)
                    btnLike.visibility = View.GONE
                    btnDisLike.visibility = View.GONE
                    numberLike.visibility = View.GONE
                    btnReply.visibility = View.GONE
                }

                if (dataComment.replies.isEmpty()) {
                    layoutShow.visibility = View.GONE
                } else {
                    layoutShow.visibility = View.VISIBLE
                    txtShow.text =
                        context.getString(R.string.Show, dataComment.replies.size.toString() ?: "")

                    if (replyParentId != 0) {
                        if (dataComment.id == replyParentId) {
                            listReply.visibility = View.VISIBLE
                        } else {
                            dataComment.replies.forEach {
                                if (it.id == replyParentId) {
                                    listReply.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

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


            //Set User Comment Info
            binding.apply {
                //Set Avatar
                dataComment.user?.img?.let {
                    if (dataComment.user.isSuspended == 0) {
                        Glide.with(context).load(it).into(avatar)
                    } else {
                        dataComment.user.img = avatar.setImageResource(R.drawable.ic_avatar_banned).toString()
                    }
                } ?: run {
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
                if (dataComment.isLiked == true) {
                    btnLike.setImageResource(R.drawable.ic_lickticked)
                }
                if (dataComment.isDisliked == true) {
                    btnDisLike.setImageResource(R.drawable.ic_diskliketicked)
                }
                if (dataComment.likeCount != null && dataComment.likeCount > 0 && dataComment.user?.isSuspended == 0) {
                    numberLike.visibility = View.VISIBLE
                    numberLike.text = dataComment.likeCount.toString()
                } else {
                    numberLike.visibility = View.GONE
                }

                if (dataComment.dislikeCount != null && dataComment.dislikeCount > 0) {
                    numberDislike.text = dataComment.dislikeCount.toString()
                }

                btnLike.setOnClickListener {
                    if (userData != null) {
                        if (dataComment.isLiked == true) {

                            btnLike.setImageResource(R.drawable.ic_likenottick)
                            btnDisLike.setImageResource(R.drawable.ic_disklikenottick)
                        } else {
                            btnLike.setImageResource(R.drawable.ic_lickticked)
                            btnDisLike.setImageResource(R.drawable.ic_disklikenottick)
                        }
                        dataComment.id?.let { id -> onClickListComment?.onClickListComment(id) }
                    }
                }
                btnDisLike.setOnClickListener {
                    if (userData != null) {
                        if (dataComment.isDisliked == true) {
                            btnLike.setImageResource(R.drawable.ic_likenottick)
                            btnDisLike.setImageResource(R.drawable.ic_disklikenottick)
                        } else {
                            btnLike.setImageResource(R.drawable.ic_likenottick)
                            btnDisLike.setImageResource(R.drawable.ic_diskliketicked)
                        }
                        dataComment.id?.let { id -> onClickListComment?.onClickDisLikeComment(id) }
                    }
                }

                //Handle Show/Hide Reply Button
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
                    btnReport.visibility = View.GONE
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
            editText: AppCompatEditText,
            cancelButton: AppCompatButton,
            sendButton: AppCompatButton
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

        fun onClickListReplyComment(commentId: Int)
        fun onClickDisLikeReplyComment(commentId: Int)

    }
}