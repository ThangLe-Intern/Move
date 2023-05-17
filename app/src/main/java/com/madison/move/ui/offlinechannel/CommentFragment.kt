package com.madison.move.ui.offlinechannel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.madison.move.R
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.offlinechannel.Adapter.ListCommentAdapter
import com.madison.move.ui.offlinechannel.Adapter.ListReplyAdapter


open class CommentFragment : Fragment(), CommentListener {
    private lateinit var binding: FragmentCommentBinding
    lateinit var adapterComment: ListCommentAdapter
    private var listComment: MutableList<Comment> = mutableListOf()

    companion object {
        var isFullScreen = false
        var isLock = false
    }
    private var savedPlayerState: PlayerState? = null

    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var fullscreen: ImageView

    inner class PlayerState(
        val playbackPosition: Long,
        val currentWindow: Int,
        val isPlaying: Boolean
    )


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


/*
        val count = activity?.supportFragmentManager?.backStackEntryCount
        Toast.makeText(activity,count.toString(), Toast.LENGTH_SHORT).show()
*/


        return binding.root
    }

//    private fun enterFullscreen() {
//        // Lưu trạng thái của ExoPlayer
//        savedPlayerState = PlayerState(
//            simpleExoPlayer.currentPosition,
//            simpleExoPlayer.currentWindowIndex,
//            simpleExoPlayer.playWhenReady
//        )
//
//        // Ẩn system UI
//        hideSystemUi()
//
//        // Chuyển sang Fragment mới
//        val fullscreenFragment = FullScreenFragment()
//        val bundle = Bundle()
//        val videoSource = Uri.parse("https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4")
//        val videoUrl = videoSource.toString()
//        val mediaItem = MediaItem.fromUri(videoSource)
//        bundle.putString("videoUrl", videoUrl) // Truyền URL video cho Fragment mới
//        fullscreenFragment.arguments = bundle
//        simpleExoPlayer.setMediaItem(mediaItem)
//        simpleExoPlayer.prepare()
//        simpleExoPlayer.play()
//
//        // Đặt Fragment mới vào Back Stack và chuyển đổi
//        requireFragmentManager()
//            .beginTransaction()
//            .replace(R.id.contrainVideo, fullscreenFragment)
//            .addToBackStack(null)
//            .commit()
//        // Cập nhật cờ và biến isFullScreen
//        isFullScreen = true
//    }
//
//    private fun hideSystemUi() {
//        binding.playView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//    }
//    private fun showSystemUi() {
//        binding.playView.systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE)
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fullscreen = view.findViewById<ImageView>(R.id.imgFullScreen)
        val lockscreen = view.findViewById<ImageView>(R.id.exoLock)

        fullscreen.setOnClickListener {
            if (!isFullScreen) {
                fullscreen.setImageDrawable(ContextCompat.getDrawable(requireContext().applicationContext, R.drawable.ic_fullscreen_exit))
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                fullscreen.setImageDrawable(ContextCompat.getDrawable(requireContext().applicationContext, R.drawable.ic_fullscreen))
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
            isFullScreen = !isFullScreen
        }

        lockscreen.setOnClickListener {
            if (!isLock) {
                lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_lock
                    )
                )
            } else {
                lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_lock_open
                    )
                )
            }
            isLock = !isLock
            lockScreen(isLock)
        }


        simpleExoPlayer = SimpleExoPlayer.Builder(requireContext())
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        binding.playView.player = simpleExoPlayer
        binding.playView.keepScreenOn = true

        simpleExoPlayer.addListener(object : Player.Listener {
            @Deprecated("Deprecated in Java")
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    binding.progressBar.visibility = View.VISIBLE
                } else if (playbackState == Player.STATE_READY) {
                    binding.progressBar.visibility = View.GONE
                }

//                if (!simpleExoPlayer.playWhenReady) {
//                    handler.removeCallbacks(updateProgressAction)
//                } else {
//                    onProgress()
//                }
            }
        })
        val videoSource = Uri.parse("https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4")
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()


    }


//    private val ad = 4000
//    private var check = false
//
//    private fun onProgress() {
//        val player = simpleExoPlayer
//        val position: Long = player.currentPosition
//        handler.removeCallbacks(updateProgressAction)
//        val playbackState = player.playbackState
//        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
//            var delayMs: Long
//            if (player.playWhenReady && playbackState == Player.STATE_READY) {
//                delayMs = 1000 - position % 1000
//                if (delayMs < 200) {
//                    delayMs += 1000
//                }
//            } else {
//                delayMs = 1000
//            }
//            if ((ad - 3000 <= position && position <= ad) && !check) {
//                check = true
//                initAd()
//            }
//            handler.postDelayed(updateProgressAction, delayMs)
//        }
//    }
//
//    var rewardedInterstitialAd: RewardedInterstitialAd? = null
//    private fun initAd() {
//        if (rewardedInterstitialAd != null) return
//        MobileAds.initialize(requireContext())
//        RewardedInterstitialAd.load(requireContext(), "ca-app-pub-3940256099942544/5354046379",
//            AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
//                override fun onAdLoaded(p0: RewardedInterstitialAd) {
//                    rewardedInterstitialAd = p0
//                    rewardedInterstitialAd!!.fullScreenContentCallback =
//                        object : FullScreenContentCallback() {
//                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                            }
//
//                            override fun onAdShowedFullScreenContent() {
//                                handler.removeCallbacks(updateProgressAction)
//                            }
//
//                            override fun onAdDismissedFullScreenContent() {
//                                //resume play
//                                simpleExoPlayer.playWhenReady = true
//                                rewardedInterstitialAd = null
//                                check = false
//                            }
//                        }
//                }
//
//                override fun onAdFailedToLoad(p0: LoadAdError) {
//                    rewardedInterstitialAd = null
//                }
//
//
//            })
//    }

//    private val updateProgressAction = Runnable { onProgress() }
    private fun lockScreen(lock: Boolean) {
        val sec_mid = view?.findViewById<LinearLayout>(R.id.sec_controlvid1)
        val sec_bottom = view?.findViewById<LinearLayout>(R.id.sec_controlvid2)
        if (lock) {
            sec_mid?.visibility = View.INVISIBLE
            sec_bottom?.visibility = View.INVISIBLE
        } else {
            sec_mid?.visibility = View.VISIBLE
            sec_bottom?.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        val orientation = requireActivity().resources.configuration.orientation

        if (isLock) {
            return
        }

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Lấy tham chiếu tới button fullscreen tương ứng từ Fragment

            fullscreen?.performClick()
        } else {
            requireActivity().onBackPressed()
        }
    }
//override fun onBackPressed() {
//    if (isLock) return
//    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//        val fullscreen = view?.findViewById<ImageView>(R.id.imgFullScreen)
//        fullscreen?.performClick()
//    } else   requireActivity().onBackPressed()
//}


    override fun onStop() {
        super.onStop()
        simpleExoPlayer.stop()
        Log.d("FragmentName1", "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
        Log.d("FragmentName2", "onDestroy() called")
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
        Log.d("FragmentName3", "onPause() called")
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
        adapterComment = ListCommentAdapter(listComment, object : ListCommentAdapter.ReplyListener {
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

    override fun buildMediaSource(parse: Uri?): MediaSource {
        TODO("Not yet implemented")
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







