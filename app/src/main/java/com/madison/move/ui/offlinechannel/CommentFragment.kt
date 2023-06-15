package com.madison.move.ui.offlinechannel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.model.TextTrack
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.google.gson.Gson
import com.madison.move.R
import com.madison.move.data.model.*
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.comment.SendComment
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.offlinechannel.Adapter.ListCommentAdapter
import kotlin.math.roundToInt

open class CommentFragment(
    private val dataVideoSuggestion: DataVideoSuggestion?,
) : BaseFragment<CommentPresenter>(), CommentListener, CommentContract.CommentContract,
    DataCallback {
    private lateinit var binding: FragmentCommentBinding
    lateinit var adapterComment: ListCommentAdapter
    private var listComment: MutableList<DataComment> = mutableListOf()
    private var listALLComment: MutableList<DataComment> = mutableListOf()
    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler
    private var tokenUser: String? = null
    private var getSharedPreferences: SharedPreferences? = null
    private var userData: DataUser? = null
    private var isReloadComment = false
    private var isLoadingCommentSuccess = true
    private var replyParentId = 0

    private var dataComment: ObjectResponse<List<DataComment>>? = null
    private var isPostView = false
    private var isDisconnect = false

    override fun createPresenter(): CommentPresenter? = CommentPresenter(this)

    companion object {
        const val TOKEN_USER_PREFERENCE = "tokenUser"
        const val TOKEN = "token"
        const val USER_DATA = "user"
    }


    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater, container, false)

        handler = Handler(Looper.getMainLooper())

        currentFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listComment.isNestedScrollingEnabled = false
    }


    override fun onResume() {
        super.onResume()
        //Check Internet Connection
        if (mListener?.isDeviceOnlineCheck() == false) {
            mListener?.onShowDisconnectDialog()
            isDisconnect = true
        }else{
            onRefreshData()
        }

    }

    open fun onRefreshData() {

        mListener?.onShowProgressBar()

        if (isDisconnect){
            //Get Video
            dataVideoSuggestion?.id?.let { presenter?.getVideoDetail(it) }
            isDisconnect = false
        }

        getSharedPreferences = requireContext().getSharedPreferences(
            TOKEN_USER_PREFERENCE, AppCompatActivity.MODE_PRIVATE
        )
        //Get Token From Preferences
        tokenUser = getSharedPreferences?.getString(TOKEN, null)

        //Get UserData From Preferences
        val jsonUser = getSharedPreferences?.getString(USER_DATA, null)
        userData = Gson().fromJson(jsonUser, DataUser::class.java)

        //Check If User login or not
        if (userData != null) {
            //Set Current User Data
            binding.apply {
                layoutUserComment.visibility = View.VISIBLE

                //Set User Avatar
                if (userData?.img != null) {
                    Glide.with(requireContext()).load(userData?.img).into(binding.userAvatar)
                } else {
                    binding.userAvatar.setImageResource(R.drawable.avatar)
                }
            }
        } else {
            //Do This When User Not Login
            binding.apply {
                layoutUserComment.visibility = View.GONE
            }
        }

        presenter?.apply {
            if (tokenUser != null) {
                getCommentVideo(("Bearer $tokenUser"), dataVideoSuggestion?.id ?: 0)
            } else {
                getCommentVideo("", dataVideoSuggestion?.id ?: 0)
            }
        }
    }


    override fun initView() {
        super.initView()

        //Get Video
        dataVideoSuggestion?.id?.let { presenter?.getVideoDetail(it) }

        binding.apply {

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
                val roundOff = (dataVideoSuggestion.rating.times(10))?.roundToInt()?.div(10.0)
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

        initScrollListener()
    }


    private fun playVideo(videoID: Int) {
        lifecycle.addObserver(binding.vimeoPlayerView)

        binding.vimeoPlayerView.clearCache()
        binding.vimeoPlayerView.initialize(true, videoID)
        binding.vimeoPlayerView.setFullscreenVisibility(true)
        binding.vimeoPlayerView.setMenuVisibility(true)


        binding.vimeoPlayerView.addReadyListener(object : VimeoPlayerReadyListener {
            override fun onReady(
                title: String?,
                duration: Float,
                textTrackArray: Array<out TextTrack>?
            ) {
                binding.imgVideoExist.visibility = View.GONE
                TimeCounter.resetTimer()
                TimeCounter.initialize()
                TimeCounter.setCallback(this@CommentFragment)
            }

            override fun onInitFailed() {
                Toast.makeText(activity, getString(R.string.video_exist), Toast.LENGTH_SHORT).show()
                binding.imgVideoExist.visibility = View.VISIBLE
            }

        })

        binding.vimeoPlayerView.addStateListener(object : VimeoPlayerStateListener {
            override fun onPlaying(duration: Float) {

                if (!isPostView) {
                    TimeCounter.startTimer()
                }
            }

            override fun onPaused(seconds: Float) {
                TimeCounter.pauseTimer()
            }

            override fun onEnded(duration: Float) {
                TimeCounter.resetTimer()
            }

        })

        binding.vimeoPlayerView.setFullscreenClickListener {
            //define the orientation
            val requestOrientation = VimeoPlayerActivity.REQUEST_ORIENTATION_LANDSCAPE
            playerFullScreenResultLauncher.launch(
                VimeoPlayerActivity.createIntent(
                    requireContext(),
                    requestOrientation,
                    binding.vimeoPlayerView,
                    TimeCounter.getTimeInSeconds()
                )
            )
        }
    }

    private var playerFullScreenResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val playAt =
                        it.data!!.getFloatExtra(VimeoPlayerActivity.RESULT_STATE_VIDEO_PLAY_AT, 0f)
                    binding.vimeoPlayerView.seekTo(playAt)

                    TimeCounter.setTimeInSeconds(
                        it.data!!.getIntExtra(
                            VimeoPlayerActivity.TIME_COUNTER,
                            0
                        )
                    )

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

    override fun onDataReceived(value: Int) {
        if (!isPostView) {
            if (tokenUser != null) {
                presenter?.postView(("Bearer $tokenUser"), dataVideoSuggestion?.id ?: 0, PostView("30"))
            } else {
                presenter?.postView("", dataVideoSuggestion?.id ?: 0, PostView("30"))
            }
        } else {
            TimeCounter.pauseTimer()
        }
    }


    override fun onSuccessGetVideoDetail(objectResponse: ObjectResponse<DataVideoDetail>) {
        playVideo(objectResponse.data?.urlVideo ?: 0)
    }

    override fun onSuccessGetCommentVideo(objectResponse: ObjectResponse<List<DataComment>>) {
        dataComment = objectResponse

        //Get List Comment
        getData()

        //Handle display view when login
        if (tokenUser != null) {
            userComment(
                binding.cancelButton,
                binding.sendButton,
                binding.edtUserComment
            )
        }
        listComment?.let { onLoadComment(it) }
        mListener?.onHideProgressBar()
    }

    override fun onError(errorMessage: String) {
        mListener?.onHideProgressBar()
        if (errorMessage == "Unable to resolve host \"api.move-intern-stg.madlab.tech\": No address associated with hostname"){
            mListener?.onShowDisconnectDialog()
        }else{
            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSuccessSendCommentVideo(objectResponse: ObjectResponse<CommentResponse>) {
        Toast.makeText(activity, getString(R.string.send_comment), Toast.LENGTH_SHORT).show()

        //Get Data Again
        presenter?.getCommentVideo(("Bearer $tokenUser"), dataVideoSuggestion?.id ?: 0)

    }

    override fun onSuccessSendReplyComment(objectResponse: ObjectResponse<CommentResponse>) {

        //Get Data Again
        presenter?.getCommentVideo(("Bearer $tokenUser"), dataVideoSuggestion?.id ?: 0)
    }

    override fun onSuccessPostView(objectResponse: ObjectResponse<PostViewResponse>) {
        Toast.makeText(activity, getString(R.string.post_view), Toast.LENGTH_SHORT).show()
        isPostView = true
    }

    override fun onSuccessCallLikeComment(objectResponse: LikeResponse) {
        presenter?.getCommentVideo(("Bearer $tokenUser"), dataVideoSuggestion?.id ?: 0)
    }

    override fun onSuccessCallDiskLikeComment(objectResponse: DiskLikeResponse) {
        presenter?.getCommentVideo(("Bearer $tokenUser"), dataVideoSuggestion?.id ?: 0)
    }


    override fun onBackPressed() {

    }

    override fun userComment(
        cancelButton: AppCompatButton,
        sendButton: AppCompatButton,
        editText: AppCompatEditText
    ) {
        cancelButton.visibility = View.GONE
        sendButton.visibility = View.GONE
        onWriteCommentListener(editText, cancelButton, sendButton)
        onCancelUserComment(cancelButton, editText)

        if (userData != null) {
            onSendUserComment(sendButton, editText, cancelButton, userData!!)
        }

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
                    if (!s.isNullOrEmpty() && editText.text?.trim().toString() != "") {
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
        editText: AppCompatEditText,
        cancelButton: AppCompatButton,
        user: DataUser
    ) {
        sendButton.setOnClickListener {
            if (tokenUser != null && dataVideoSuggestion?.id != null && isLoadingCommentSuccess) {
                isLoadingCommentSuccess = false
                
                presenter?.sendCommentVideo(
                    ("Bearer $tokenUser"),
                    dataVideoSuggestion?.id ?: 0,
                    SendComment(editText.text.toString().trim())
                )
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.cannot_send_comment),
                    Toast.LENGTH_SHORT
                ).show()
            }

            clearEdittext(editText, cancelButton)
        }
    }

    override fun onStop() {
        super.onStop()
        binding.vimeoPlayerView.pause()
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
        adapterComment = ListCommentAdapter(
            requireContext(),
            listComment,
            object : ListCommentAdapter.ReplyListener {
                override fun userComment(
                    cancelButton: AppCompatButton,
                    sendButton: AppCompatButton,
                    editText: AppCompatEditText,
                    parentCommentId: Int
                ) {

                    cancelButton.visibility = View.GONE
                    sendButton.visibility = View.GONE

                    onWriteCommentListener(editText, cancelButton, sendButton)
                    onCancelUserComment(cancelButton, editText)
                    onSendUserReply(
                        sendButton, parentCommentId, editText, cancelButton
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
                                if (!s.isNullOrEmpty() && editText.text?.trim().toString() != "") {
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
                    parentCommentId: Int,
                    editText: AppCompatEditText,
                    cancelButton: AppCompatButton
                ) {

                    sendButton.setOnClickListener {
                        if (tokenUser != null) {
                            presenter?.sendReplyComment(
                                ("Bearer $tokenUser"),
                                parentCommentId,
                                SendComment(editText.text.toString().trim())
                            )
                            replyParentId = parentCommentId
                        } else {
                            Toast.makeText(
                                activity,
                                getString(R.string.cannot_send_reply),
                                Toast.LENGTH_SHORT
                            )
                                .show()
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

                override fun onClickListReplyComment(commentId: Int) {
                    presenter?.callLikeComment("Bearer $tokenUser", commentId)
                    replyParentId = commentId
                }

                override fun onClickDisLikeReplyComment(commentId: Int) {
                    presenter?.callDiskLikeComment("Bearer $tokenUser", commentId)
                    replyParentId = commentId
                }
            }, replyParentId
        )

        adapterComment.onClickListComment = object : ListCommentAdapter.setListenerListComment {
            override fun onClickListComment(commentId: Int) {
                presenter?.callLikeComment("Bearer $tokenUser", commentId)
            }

            override fun onClickDisLikeComment(commentId: Int) {
                presenter?.callDiskLikeComment("Bearer $tokenUser", commentId)
            }
        }

        binding.listComment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterComment
        }
    }

    private fun getData() {

        if (listALLComment.isNotEmpty()) {
            isReloadComment = true
            listALLComment.clear()
        }
        dataComment?.data?.let { listALLComment.addAll(it) }
        addComment()

    }

    private fun addComment() {

        if (listALLComment.isNotEmpty() && listALLComment.size != listComment.size) {
            if (listComment.size != 0 && isReloadComment) {
                val lastIndexList = listComment.last()

                listComment.clear()

                for (i in listALLComment) {
                    listComment.add(i)
                    if (i == lastIndexList) {
                        isReloadComment = false
                        break
                    }
                }
                listALLComment = listALLComment.subtract(listComment.toSet()).toMutableList()
                listComment
                listALLComment
            } else {
                //Handle Add Data when Load more
                if (listALLComment.size >= 11) {
                    (0..9).forEach { i ->
                        listComment.add(listALLComment[i])
                    }
                    listALLComment.subList(0, 10).clear()
                } else {
                    listComment.addAll(listALLComment)
                    isReloadComment = true
                    listALLComment.clear()
                }
            }

        } else {
            listComment.clear()
            listComment.addAll(listALLComment)
            listALLComment.clear()
        }
        isLoadingCommentSuccess = true
    }

    private fun initScrollListener() {
        binding.listComment.isNestedScrollingEnabled = false

        binding.nestedComment.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (listALLComment.isNotEmpty()) {
                if (v.getChildAt(v.childCount - 1) != null) {
                    if (scrollY > oldScrollY) {
                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && !isLoading) {
                            isLoading = true
                            loadMore()
                        }
                    }
                }
            }
        })
    }

    private fun loadMore() {
        binding.progressBar.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({

            val scrollPosition: Int? = listComment.size
            if (scrollPosition != null) {
                adapterComment.notifyItemRemoved(scrollPosition)
            }

            addComment()
            adapterComment.notifyDataSetChanged()

            isLoading = false
            binding.progressBar.visibility = View.GONE
        }, 3000)
    }
}







