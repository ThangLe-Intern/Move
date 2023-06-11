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
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ct7ct7ct7.androidvimeoplayer.model.PlayerState
import com.ct7ct7ct7.androidvimeoplayer.view.VimeoPlayerActivity
import com.google.gson.Gson
import com.madison.move.R
import com.madison.move.data.model.DataComment
import com.madison.move.data.model.DataUser
import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.videodetail.VideoDetailResponse
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.base.BaseFragment
import com.madison.move.ui.menu.MainMenuActivity
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
    private var listALLComment: MutableList<DataComment> = mutableListOf()
    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler
    private var tokenUser: String? = null
    private var getSharedPreferences: SharedPreferences? = null
    private var userData: DataUser? = null

    private var dataComment: ObjectResponse<List<DataComment>>? = null
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

    override fun onResume() {
        super.onResume()
        //Check Internet Connection
        if (mListener?.isDeviceOnlineCheck() == false) {
            mListener?.onShowDisconnectDialog()
        }

        onRefreshData()

    }

    private fun onRefreshData() {

//        mListener?.onShowProgressBar()
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

        //Clear List When Reload
        listALLComment.clear()
        listComment.clear()

        presenter?.apply {

/*            getVideoDetail(
                dataVideoSuggestion?.id ?: 0
            )
            getCommentVideo(dataVideoSuggestion?.id ?: 0)*/

            getVideoDetail(1)
            if (tokenUser != null) {
                getCommentVideo(("Bearer $tokenUser"), 1)
            } else {
                getCommentVideo("", 1)
            }
        }
    }


    override fun initView() {
        super.initView()
        binding.apply {

            if (dataVideoCarousel?.rating == null) {
                tvrateNumber.text = 0.toString()
            } else {
                val roundOff = (dataVideoCarousel.rating.times(100.0)).roundToInt()?.div(100.0)
                tvrateNumber.text = roundOff.toString()
            }
            if (dataVideoCarousel?.categoryName != null && dataVideoCarousel.categoryName == "Just Move") {
                cardviewTimeLine.visibility = View.GONE
                cardviewBeginner.visibility = View.GONE
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

        initScrollListener()
    }

    private fun playVideo(videoID: Int) {
        lifecycle.addObserver(binding.vimeoPlayerView)

        binding.vimeoPlayerView.clearCache()
        binding.vimeoPlayerView.initialize(true, 337510595)
        //        binding.vimeoPlayerView.initialize(true, videoID)
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

    private var playerFullScreenResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
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
        playVideo(objectResponse.data?.urlVideo ?: 0)
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

        binding.listComment.isNestedScrollingEnabled = false


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
                    list: RecyclerView
                ) {

                    cancelButton.visibility = View.GONE
                    sendButton.visibility = View.GONE

                    onWriteCommentListener(editText, cancelButton, sendButton)
                    onCancelUserComment(cancelButton, editText)
                    onSendUserReply(
                        sendButton, list, listCommentReply, editText, cancelButton
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

                        var adapterReply = ListReplyAdapter(requireContext(), listCommentReply)
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
        dataComment?.data?.let { listALLComment.addAll(it) }
        addComment()
    }

    private fun addComment() {
        if (listALLComment.size >= 11) {
            (0..9).forEach { i ->
                listComment.add(listALLComment[i])
            }
            listALLComment.subList(0, 10).clear()
        } else {
            listComment.addAll(listALLComment)
            listALLComment.clear()
        }
    }

    private fun initScrollListener() {
        binding.listComment.isNestedScrollingEnabled = false

        binding.nestedComment.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (listALLComment.isNotEmpty()) {
                if (v.getChildAt(v.childCount - 1) != null) {
                    if (scrollY > oldScrollY) {
                        if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) {
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

            addComment()

            adapterComment.notifyDataSetChanged()
            isLoading = false
            binding.progressBar.visibility = View.GONE
        }, 3000)

    }


}







