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
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madison.move.R
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.offlinechannel.Adapter.ListCommentAdapter
import com.madison.move.ui.offlinechannel.Adapter.ListReplyAdapter


open class CommentFragment : Fragment(), CommentListener {
    private lateinit var binding: FragmentCommentBinding
    lateinit var adapterComment: ListCommentAdapter
    private var listComment: MutableList<Comment> = mutableListOf()

    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler
    var page = 1

    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater, container, false)


        val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)
        listComment?.let {
            userComment(
                binding.cancelButton,
                binding.sendButton,
                binding.edtUserComment,
                it,
                user4
            )
        }
        handler = Handler(Looper.getMainLooper())

        currentFragment = this

        val iframeContent =
            "<html><body style=\"margin:0; padding:0\"><iframe src=\"https://player.vimeo.com/video/831402333\" width=\"100%\" height=\"100%\" frameborder=\"0\" allow=\"autoplay; fullscreen\" allowfullscreen></iframe>" // Lấy nội dung iframe từ API

        binding.webView.getSettings().setJavaScriptEnabled(true)

        binding.webView.loadData(
            iframeContent,
            "text/html",
            "utf-8"
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScrollListener()
    }

    override fun onBackPressed() {
        TODO("Not yet implemented")
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
                            i.content?.let { it1 -> Log.d("DUNG", it1) }
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


        listComment?.add(
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

        listComment?.add(Comment(2, "ALO SONDASDK", "Just now", mutableListOf(), user2))
        listComment?.add(Comment(3, "KAMAVINGAR HALANDES", "Just now", mutableListOf(), user3))
        listComment?.add(Comment(4, "SDASDESADASD", "Just now", mutableListOf(), user4))


    }

    private fun initScrollListener() {
        binding.listComment.isNestedScrollingEnabled = false


        binding.nestedComment.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY > oldScrollY) {
                    if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
                        isLoading = true
                        loadMore()
                    }
                }
            }
        })
    }

    private fun loadMore() {
        val user1 = DataModelComment(R.drawable.avatar, "Vu Dung", false)
        binding.progressBar.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            listComment.removeAt(listComment.size - 1)
            val scrollPosition: Int = listComment.size
            adapterComment.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                listComment.add(
                    Comment(
                        1, "$currentSize", "Just now",
                        mutableListOf(
                            Comment(1, "HIHIHAHAHAH", "Just now", mutableListOf(), user1)
                        ), user1
                    )
                )
                currentSize++
            }
            adapterComment.notifyDataSetChanged()
            isLoading = false
            binding.progressBar.visibility = View.VISIBLE
        }, 3000)


    }


}







