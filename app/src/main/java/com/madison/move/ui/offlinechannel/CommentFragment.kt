package com.madison.move.ui.offlinechannel

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.madison.move.R
import com.madison.move.data.model.DataComment
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.offlinechannel.Adapter.ListCommentAdapter
import com.madison.move.ui.offlinechannel.Adapter.ListReplyAdapter
import kotlin.math.roundToInt

open class CommentFragment(
    private val dataVideoSuggestion: DataVideoSuggestion?,
    private val dataVideoCarousel: DataVideoSuggestion?
) : BaseFragment<CommentPresenter>(), CommentListener, CommentContract.CommentContract {
    private lateinit var binding: FragmentCommentBinding
    lateinit var adapterComment: ListCommentAdapter
    private var listComment: MutableList<DataComment> = mutableListOf()
    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler

    private var dataComment: ObjectResponse<List<DataComment>>? = null
    override fun createPresenter(): CommentPresenter? = CommentPresenter(this)

    override fun onResume() {
        super.onResume()
        //Check Internet Connection
        if (mListener?.isDeviceOnlineCheck() == false) {
            mListener?.onShowDisconnectDialog()
        }
    }

    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater, container, false)


        presenter?.apply {
/*            getVideoDetail(
                dataVideoSuggestion?.id ?: 0
            )
            getCommentVideo(dataVideoSuggestion?.id ?: 0)*/
            getVideoDetail(1)
            getCommentVideo(1)
        }

        handler = Handler(Looper.getMainLooper())

        currentFragment = this

        binding.apply {

            nameUserProflie.text = dataVideoCarousel?.username.toString()
            tvJust.text =
                getString(R.string.video_category, dataVideoCarousel?.categoryName.toString())
            tvrateNumber.text = dataVideoCarousel?.rating.toString()

            if (dataVideoCarousel?.img != null) {
                activity?.let { Glide.with(it).load(dataVideoCarousel.img).into(avartProfile) }
            } else {
                avartProfile.setImageResource(R.drawable.avatar)
            }

            userAvatar.setImageResource(R.drawable.avatar)

            if (dataVideoCarousel?.rating == null) {
                tvrateNumber.text = 0.toString()
            } else {
                val roundOff = (dataVideoCarousel.rating.times(100.0)).roundToInt()?.div(100.0)
                tvrateNumber.text = roundOff.toString()
            }
            if (dataVideoCarousel?.categoryName != null && dataVideoCarousel.categoryName == "Just Move") {
                cardviewTimeLine.visibility = View.INVISIBLE
                cardviewBeginner.visibility = View.INVISIBLE
            } else {
                cardviewTimeLine.visibility = View.VISIBLE
                cardviewBeginner.visibility = View.VISIBLE

                when (dataVideoCarousel?.level) {
                    1 -> txtBeginner.text = activity?.getString(R.string.txt_level_beginner)
                    2 -> txtBeginner.text = activity?.getString(R.string.txt_level_inter)
                    3 -> txtBeginner.text = activity?.getString(R.string.txt_level_advanced)
                }

                when (dataVideoCarousel?.duration) {
                    1 -> txtTimeLine.text = activity?.getString(R.string.timeOfCategory)
                    2 -> txtTimeLine.text = activity?.getString(R.string.duration_second)
                    3 -> txtTimeLine.text = activity?.getString(R.string.duration_third)
                }
            }


            nameUserProflie.text = dataVideoSuggestion?.username.toString()
            tvJust.text =
                getString(R.string.video_category, dataVideoSuggestion?.categoryName.toString())
            tvrateNumber.text = dataVideoSuggestion?.rating.toString()

            if (dataVideoSuggestion?.img != null) {
                activity?.let { Glide.with(it).load(dataVideoSuggestion.img).into(avartProfile) }
            } else {
                avartProfile.setImageResource(R.drawable.avatar)
            }

            if (dataVideoSuggestion?.rating == null) {
                tvrateNumber.text = 0.toString()
            } else {
                val roundOff = (dataVideoSuggestion.rating.times(100.0))?.roundToInt()?.div(100.0)
                tvrateNumber.text = roundOff.toString()
            }
            if (dataVideoSuggestion?.categoryName != null && dataVideoSuggestion.categoryName == "Just Move") {
                cardviewTimeLine.visibility = View.INVISIBLE
                cardviewBeginner.visibility = View.INVISIBLE
            } else {
                cardviewTimeLine.visibility = View.VISIBLE
                cardviewBeginner.visibility = View.VISIBLE

                when (dataVideoSuggestion?.level) {
                    1 -> txtBeginner.text = activity?.getString(R.string.txt_level_beginner)
                    2 -> txtBeginner.text = activity?.getString(R.string.txt_level_inter)
                    3 -> txtBeginner.text = activity?.getString(R.string.txt_level_advanced)
                }

                when (dataVideoSuggestion?.duration) {
                    1 -> txtTimeLine.text = activity?.getString(R.string.timeOfCategory)
                    2 -> txtTimeLine.text = activity?.getString(R.string.duration_second)
                    3 -> txtTimeLine.text = activity?.getString(R.string.duration_third)
                }
            }
        }


        return binding.root
    }


    override fun initView() {
        super.initView()

    }

    private fun playVideo(videoID: Int) {
        lifecycle.addObserver(binding.vimeoPlayerView)
        binding.vimeoPlayerView.clearCache()
//        binding.vimeoPlayerView.initialize(true, videoID)
        binding.vimeoPlayerView.initialize(true, 832119390)
        binding.vimeoPlayerView.setFullscreenVisibility(true)
        binding.vimeoPlayerView.setMenuVisibility(true)

        binding.vimeoPlayerView.setFullscreenClickListener {
            //define the orientation
            val requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_LANDSCAPE
            playerFullScreenResultLauncher.launch(
                VimeoPlayerActivity.createIntent(
                    requireContext(), requestOrientation, binding.vimeoPlayerView
                )
            )
        }
    }

    var playerFullScreenResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val playAt =
                    it.data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
                binding.vimeoPlayerView.seekTo(playAt)

                val playerState =
                    it.data!!.getStringExtra(VimeoPlayerActivity.RESULT_STATE_PLAYER_STATE)
                        ?.let { PlayerState.valueOf(it) }
                when (playerState) {
                    PlayerState.PLAYING -> binding.vimeoPlayerView.play()
                    PlayerState.PAUSED -> binding.vimeoPlayerView.pause()
                    else -> {

                    }
                }
            } else {
                Toast.makeText(activity, "Null Video Data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onBottomNavigateSystemUI() {

    }

    override fun onSuccessGetVideoSuggestion(videoDetailsSuggestionResponse: VideoDetailResponse) {

    }

    override fun onSuccessGetVideoDetail(objectResponse: ObjectResponse<DataVideoDetail>) {
        objectResponse.data?.video?.urlVideo?.let { playVideo(it) }

    }

    override fun onSuccessGetCommentVideo(objectResponse: ObjectResponse<List<DataComment>>) {
        dataComment = objectResponse

        val user4 = DataModelComment(R.drawable.avatar, "Nguyen Vu Dung", true)

        userComment(
            binding.cancelButton, binding.sendButton, binding.edtUserComment, listComment, user4
        )

    }

    override fun onError(errorMessage: String) {
        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initScrollListener()
    }

    override fun onBackPressed() {

    }

    override fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText,
        listComment: MutableList<DataComment>,
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
        editText: AppCompatEditText, cancelButton: AppCompatButton, sendButton: AppCompatButton
    ) {

        editText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
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
                }
            })
            onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
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
        listComment: MutableList<DataComment>,
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: DataModelComment
    ) {
        sendButton.setOnClickListener {
/*            listComment.add(
                0, Comment(4, editText.text.toString().trim(), "Just now", mutableListOf(), user)
            )*/

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
            cancelButton.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    override fun onLoadComment(listComment: MutableList<DataComment>) {

        getData()

        adapterComment = ListCommentAdapter(requireContext(),
            listComment,
            object : ListCommentAdapter.ReplyListener {
                override fun userComment(
                    cancelButton: AppCompatButton,
                    sendButton: AppCompatButton,
                    editText: AppCompatEditText,
                    listCommentReply: MutableList<DataComment>,
                    list: RecyclerView,
                    user: DataModelComment
                ) {

                    cancelButton.visibility = View.GONE
                    sendButton.visibility = View.GONE
                    onWriteCommentListener(editText, cancelButton, sendButton)
                    onCancelUserComment(cancelButton, editText)
                    onSendUserReply(
                        sendButton, list, listCommentReply, editText, cancelButton, user
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
                                s: CharSequence?, start: Int, count: Int, after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?, start: Int, before: Int, count: Int
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
                        onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                            if (!hasFocus) {
                                v?.let { hideKeyboard(it) }
                            }
                        }
                    }

                }

                override fun onCancelUserComment(
                    cancelButton: AppCompatButton, editText: AppCompatEditText
                ) {
                    cancelButton.setOnClickListener {
                        clearEdittext(editText, cancelButton)
                    }
                }

                override fun onSendUserReply(
                    sendButton: AppCompatButton,
                    list: RecyclerView,
                    listCommentReply: MutableList<DataComment>,
                    editText: AppCompatEditText,
                    cancelButton: AppCompatButton,
                    user: DataModelComment
                ) {
                    sendButton.setOnClickListener {
/*                        listCommentReply.add(
                            0, Comment(
                                5,
                                editText.text.toString().trim(),
                                "Just now",
                                mutableListOf(),
                                user,
                                true
                            )
                        )*/

                        for (i in listCommentReply) {
                            i.content?.let { it1 -> Log.d("DUNG", it1) }
                        }

                        var adapterReply = ListReplyAdapter(listCommentReply, requireContext())
                        list.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = adapterReply
                        }
                        clearEdittext(editText, cancelButton)
                    }
                }

                override fun clearEdittext(
                    editText: AppCompatEditText, cancelButton: AppCompatButton
                ) {
                    editText.text = null
                    editText.clearFocus()

                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        cancelButton.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN
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
        Log.d("KKE", dataComment?.data.toString() + "Get Data")

        dataComment?.data?.let { listComment.addAll(it) }
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
/*                listComment.add(
                    Comment(
                        1, "$currentSize", "Just now", mutableListOf(
                            Comment(1, "HIHIHAHAHAH", "Just now", mutableListOf(), user1)
                        ), user1
                    )
                )*/
                currentSize++
            }
            adapterComment.notifyDataSetChanged()
            isLoading = false
            binding.progressBar.visibility = View.VISIBLE
        }, 3000)


    }


}







