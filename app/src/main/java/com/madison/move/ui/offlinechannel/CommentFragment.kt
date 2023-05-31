package com.madison.move.ui.offlinechannel

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.offlinechannel.Adapter.ListCommentAdapter
import com.madison.move.ui.offlinechannel.Adapter.ListReplyAdapter

open class CommentFragment(private val dataVideoSuggestion: DataVideoSuggestion?) : BaseFragment<CommentPresenter>(), CommentListener,CommentContract.CommentContract {
    private lateinit var binding: FragmentCommentBinding
    lateinit var adapterComment: ListCommentAdapter
    private var listComment: MutableList<Comment> = mutableListOf()
    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler


    override fun createPresenter(): CommentPresenter? = CommentPresenter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater, container, false)

        val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)
        userComment(
            binding.cancelButton,
            binding.sendButton,
            binding.edtUserComment,
            listComment,
            user4
        )
        handler = Handler(Looper.getMainLooper())

        currentFragment = this

/*        val iframeContent =
            "<html><body style=\"margin:0; padding:0\"><iframe src=\"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_5MB.mp4\" width=\"100%\" height=\"100%\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen></iframe>" // Lấy nội dung iframe từ API

        binding.webView.getSettings().setJavaScriptEnabled(true)

        binding.webView.loadData(
            iframeContent,
            "text/html",
            "utf-8"
        )*/

        binding.nameUserProflie.text = dataVideoSuggestion?.username.toString()
        binding.tvJust.text = dataVideoSuggestion?.categoryName.toString()
        binding.tvrateNumber.text = dataVideoSuggestion?.rating.toString()
        if (dataVideoSuggestion?.categoryName != null && dataVideoSuggestion.categoryName == "Just Move") {
            binding.cardviewTimeLine.visibility = View.INVISIBLE
            binding.cardviewBeginner.visibility = View.INVISIBLE
        } else {
            binding.cardviewTimeLine.visibility = View.VISIBLE
            binding.cardviewBeginner.visibility = View.VISIBLE

            when (dataVideoSuggestion?.level) {
                1 -> binding.txtBeginner.text =
                    activity?.getString(R.string.txt_level_beginner)
                2 -> binding.txtBeginner.text =
                    activity?.getString(R.string.txt_level_inter)
                3 -> binding.txtBeginner.text =
                    activity?.getString(R.string.txt_level_advanced)
            }

            when (dataVideoSuggestion?.duration) {
                1 -> binding.txtTimeLine.text =
                    activity?.getString(R.string.timeOfCategory)
                2 -> binding.txtTimeLine.text =
                    activity?.getString(R.string.duration_second)
                3 -> binding.txtTimeLine.text =
                    activity?.getString(R.string.duration_third)
            }
        }





        presenter?.apply {
            getVideoDetail("Bearer 573|FFsfn3YCojvXRiF21RkYBSTLPh5XOAO7udNrZr0Y", dataVideoSuggestion?.id ?: 0)
        }
        return binding.root
    }



    override fun onBottomNavigateSystemUI() {

    }

    override fun onSuccessGetVideoSuggestion(videoDetailsSuggestionResponse: VideoDetailResponse) {
        val iframeContent =
            "<html><body style=\"margin:0; padding:0\"><iframe src=\"https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_5MB.mp4\" width=\"100%\" height=\"100%\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen></iframe>" // Lấy nội dung iframe từ API

//        val iframeContent2 =
//            "<html><body style=\"margin:0; padding:0\"><iframe src=\"{${videoDetailsSuggestionResponse.posts.urlVideo}}\" width=\"100%\" height=\"100%\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen></iframe>" // Lấy nội dung iframe từ API

        binding.webView.getSettings().setJavaScriptEnabled(true)

        binding.webView.loadData(
            iframeContent,
            "text/html",
            "utf-8"
        )

    }

    override fun onError(errorMessage: String) {

    }

    override fun onBackPressed() {

    }

    override fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
        listComment: MutableList<Comment>,
        user: DataModelComment
    ) {
        cancelButton.visibility = View.GONE
        sendButton.visibility = View.GONE
        onWriteCommentListener(editText, cancelButton, sendButton)
        onCancelUserComment(cancelButton, editText)
        onSendUserComment(sendButton, listComment, editText, cancelButton, user)
        onLoadComment(listComment)

    }

    override fun onWriteCommentListener(
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton
    ) {

        editText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        cancelButton.visibility = View.VISIBLE
                        sendButton.visibility = View.VISIBLE
                    } else {
                        cancelButton.visibility = View.GONE
                        sendButton.visibility = View.GONE
                    }
                }

                override fun afterTextChanged(s: Editable?) {
/*                if (!s.isNullOrEmpty()) {
                    binding.edtUserComment.clearFocus()
                }*/
                }
            })
            onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (!hasFocus) {
                        v?.let { hideKeyboard(it) }
                    }
                }
        }
    }

    override fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCancelUserComment(cancelButton: AppCompatButton, editText: AppCompatEditText) {
        cancelButton.setOnClickListener {
            clearEdittext(editText, cancelButton)
        }
    }

    override fun onSendUserComment(
        sendButton: AppCompatButton,
        listComment: MutableList<Comment>,
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: DataModelComment
    ) {
        sendButton.setOnClickListener {
            listComment.add(
                0,
                Comment(4, editText.text.toString().trim(), "Just now", mutableListOf(), user)
            )
            binding.listComment.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = adapterComment
            }
            clearEdittext(editText, cancelButton)
        }
    }

    override fun clearEdittext(editText: AppCompatEditText, cancelButton: AppCompatButton) {
        editText.text = null
        editText.clearFocus()

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            cancelButton.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    override fun onLoadComment(listComment: MutableList<Comment>) {
        getData()
        adapterComment = ListCommentAdapter(
            requireContext(),
            listComment,
            object : ListCommentAdapter.ReplyListener {
                override fun userComment(
                    cancelButton: AppCompatButton,
                    sendButton: AppCompatButton,
                    editText: AppCompatEditText,
                    listCommentReply: MutableList<Comment>,
                    list: RecyclerView,
                    user: DataModelComment
                ) {

                    cancelButton.visibility = View.GONE
                    sendButton.visibility = View.GONE
                    onWriteCommentListener(editText, cancelButton, sendButton)
                    onCancelUserComment(cancelButton, editText)
                    onSendUserReply(
                        sendButton,
                        list,
                        listCommentReply,
                        editText,
                        cancelButton,
                        user
                    )
                }

                override fun onWriteCommentListener(
                    editText: AppCompatEditText,
                    cancelButton: AppCompatButton,
                    sendButton: AppCompatButton
                ) {
                    editText.apply {
                        addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                if (!s.isNullOrEmpty()) {
                                    cancelButton.visibility = View.VISIBLE
                                    sendButton.visibility = View.VISIBLE
                                } else {
                                    cancelButton.visibility = View.GONE
                                    sendButton.visibility = View.GONE
                                }
                            }
                            override fun afterTextChanged(s: Editable?) {
                            }

                        })
                        onFocusChangeListener =
                            View.OnFocusChangeListener { v, hasFocus ->
                                if (!hasFocus) {
                                    v?.let { hideKeyboard(it) }
                                }
                            }
                    }

                }

                override fun onCancelUserComment(
                    cancelButton: AppCompatButton,
                    editText: AppCompatEditText
                ) {
                    cancelButton.setOnClickListener {
                        clearEdittext(editText, cancelButton)
                    }
                }

                override fun onSendUserReply(
                    sendButton: AppCompatButton,
                    list: RecyclerView,
                    listCommentReply: MutableList<Comment>,
                    editText: AppCompatEditText,
                    cancelButton: AppCompatButton,
                    user: DataModelComment
                ) {
                    sendButton.setOnClickListener {
                        listCommentReply.add(
                            0,
                            Comment(
                                5,
                                editText.text.toString().trim(),
                                "Just now",
                                mutableListOf(),
                                user,
                                true
                            )
                        )

                        for (i in listCommentReply) {
                            Log.d("DUNG", i.content)
                        }

                        var adapterReply = ListReplyAdapter(listCommentReply)
                        list.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = adapterReply
                        }
                        clearEdittext(editText, cancelButton)
                    }
                }

                override fun clearEdittext(
                    editText: AppCompatEditText,
                    cancelButton: AppCompatButton
                ) {
                    editText.text = null
                    editText.clearFocus()

                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        cancelButton.windowToken,
                        InputMethodManager.RESULT_UNCHANGED_SHOWN
                    )
                }

                override fun hideKeyboard(view: View) {
                    val inputMethodManager =
                        requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }


            })

        binding.listComment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterComment
        }
    }

    private fun getData() {

        val user1 = DataModelComment(R.drawable.avatar, "Vu Dung", false)
        val user2 = DataModelComment(R.drawable.avatar, "Taylor Swift", true)
        val user3 = DataModelComment(R.drawable.avatar, "Justin Bieber", false)
        val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)


        listComment.add(
            Comment(
                1, "DSMLMFLSKEMFKLM", "Just now",
                mutableListOf(
                    Comment(1, "HIHIHAHAHAH", "Just now", mutableListOf(), user1),
                    Comment(2, "ASDASDASDASSD", "Just now", mutableListOf(), user2),
                    Comment(3, "BDSDBADASB", "Just now", mutableListOf(), user1),
                    Comment(4, "WEREWREWREWREW", "Just now", mutableListOf(), user2),
                ), user1
            )
        )

        listComment.add(Comment(2, "ALO SONDASDK", "Just now", mutableListOf(), user2))
        listComment.add(Comment(3, "KAMAVINGAR HALANDES", "Just now", mutableListOf(), user3))
        listComment.add(Comment(4, "SDASDESADASD", "Just now", mutableListOf(), user4))


    }


}







