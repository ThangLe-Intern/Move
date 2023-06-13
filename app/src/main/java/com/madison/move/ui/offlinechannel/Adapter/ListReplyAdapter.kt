package com.madison.move.ui.offlinechannel.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.madison.move.R
import com.madison.move.data.model.DataUser
import com.madison.move.data.model.comment.DataComment
import com.madison.move.databinding.ItemUserCommentBinding
import com.madison.move.ui.offlinechannel.CommentFragment

class ListReplyAdapter(
    private var context: Context,
    var listReply: MutableList<DataComment>,
    var onClickListReplyComment : ListCommentAdapter.ReplyListener?,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var getSharedPreferences: SharedPreferences? = null
    private var userData: DataUser? = null

    inner class ViewHolder(val binding: ItemUserCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun onBind(dataComment: DataComment) {
            binding.apply {

                getSharedPreferences = context.getSharedPreferences(
                    CommentFragment.TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
                )
                val jsonUser = getSharedPreferences?.getString(ListCommentAdapter.USER_DATA, null)

                userData = Gson().fromJson(jsonUser, DataUser::class.java)

                if (userData != null){
                    btnReport.visibility= View.VISIBLE
                }else{
                    btnReport.visibility = View.GONE
                }
                btnLike.setOnClickListener {
                    Log.d("123123", (dataComment.id ?:0).toString())
                    dataComment.id?.let { id ->
                        onClickListReplyComment?.onClickListReplyComment(id) }
                }
                btnDisLike.setOnClickListener{
                    dataComment.id?.let { id -> onClickListReplyComment?.onClickDisLikeReplyComment(id) }
                }

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

                if (dataComment.isLiked == true) {
                    btnLike.setImageResource(R.drawable.ic_lickticked)
                }

                if (dataComment.isDisliked == true) {
                    btnDisLike.setImageResource(R.drawable.ic_diskliketicked)
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