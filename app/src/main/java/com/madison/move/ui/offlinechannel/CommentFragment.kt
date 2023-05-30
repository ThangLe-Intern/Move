package com.madison.move.ui.offlinechannel

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.gms.ads.*
import com.madison.move.R
import com.madison.move.databinding.FragmentCommentBinding
import com.madison.move.ui.offlinechannel.Adapter.ListCommentAdapter
import com.madison.move.ui.offlinechannel.Adapter.ListReplyAdapter


open class CommentFragment : Fragment(), CommentListener {
    private lateinit var binding: FragmentCommentBinding
    lateinit var adapterComment: ListCommentAdapter
    private var listComment: MutableList<Comment> = mutableListOf()

    companion object {
        var IS_FULL_SCREEN = false
        var IS_LOCK = false
        private const val INITIAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private var currentFragment: Fragment? = null
    private lateinit var handler: Handler
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var fullscreen: ImageView


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





        return binding.root
    }

    private fun hideViews() {

        binding.layoutInforAndComent.visibility = View.GONE

        val view: View? = activity?.findViewById(R.id.layout_tool_bar)
        view?.visibility = View.GONE

        val layoutParams = binding.playView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.playView.layoutParams = layoutParams
    }

    private fun showViews() {

        binding.layoutInforAndComent.visibility = View.VISIBLE

        val view: View? = activity?.findViewById(R.id.layout_tool_bar)
        view?.visibility = View.VISIBLE

        val heightInDp = 230
        val scale = resources.displayMetrics.density
        val heightInPx = (heightInDp * scale + 0.5f).toInt()
        val layoutParams = binding.playView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = heightInPx
        binding.playView.layoutParams = layoutParams
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialOrientation = requireActivity().requestedOrientation
        val fullscreen = view.findViewById<ImageView>(R.id.img_full_screeen)
        val lockscreen = view.findViewById<ImageView>(R.id.img_lock)
        val imgback = view.findViewById<ImageView>(R.id.img_back)
        val btnSettings = view.findViewById<ImageView>(R.id.img_settings)

        btnSettings.setOnClickListener {
            showSettingsDialog()
        }

        fullscreen.setOnClickListener {
            if (!IS_FULL_SCREEN) {
                imgback.visibility = View.VISIBLE
                fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext().applicationContext,
                        R.drawable.ic_fullscreen_exit
                    )
                )
                requireActivity().requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                hideViews()
            } else {
                imgback.visibility = View.GONE
                fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext().applicationContext,
                        R.drawable.ic_fullscreen
                    )
                )
                requireActivity().requestedOrientation = initialOrientation
                showViews()
            }
            IS_FULL_SCREEN = !IS_FULL_SCREEN
        }

        imgback.visibility = View.GONE
        imgback.setOnClickListener {
            if (!IS_FULL_SCREEN) {
                imgback.visibility = View.GONE
            } else {
                imgback.visibility = View.GONE
                fullscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext().applicationContext,
                        R.drawable.ic_fullscreen
                    )
                )
                requireActivity().requestedOrientation = initialOrientation
                showViews()
            }
        }

        lockscreen.setOnClickListener {
            if (!IS_LOCK) {
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
            IS_LOCK = !IS_LOCK
            lockScreen(IS_LOCK)
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

            }
        })
        val videoSource = Uri.parse("https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4")
        val mediaItem = MediaItem.fromUri(videoSource)
        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()

    }

    private fun showSettingsDialog() {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Cài đặt video")
            .setItems(arrayOf("Chất lượng", "Phụ đề", "Âm lượng")) { _, which ->
                // Xử lý sự kiện khi tùy chọn được chọn
                when (which) {
                    0 -> showQualityOptions()
                    1 -> showSubtitleOptions()
                    2 -> showVolumeOptions()
                }
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun showQualityOptions() {
        // Hiển thị dialog cho người dùng chọn chất lượng video
        // Xử lý sự kiện khi người dùng chọn chất lượng
    }

    private fun showSubtitleOptions() {
        // Hiển thị dialog cho người dùng chọn chất lượng video
        // Xử lý sự kiện khi người dùng chọn chất lượng
    }

    private fun showVolumeOptions() {
        val volumeOptions = arrayOf("Âm lượng cao", "Âm lượng trung bình", "Âm lượng thấp")
        val dialog = AlertDialog.Builder(context)
            .setTitle("Chọn âm lượng")
            .setItems(volumeOptions) { _, which ->
                // Xử lý sự kiện khi người dùng chọn âm lượng
                val selectedVolume = volumeOptions[which]
                // Áp dụng âm lượng được chọn vào ExoPlayer
                applyVolume(selectedVolume)
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun applyVolume(selectedVolume: String) {
        // Áp dụng mức âm lượng được chọn vào ExoPlayer
        when (selectedVolume) {
            "Âm lượng cao" -> {
                // Đặt mức âm lượng cao cho ExoPlayer
                simpleExoPlayer.volume = 1.0f
            }
            "Âm lượng trung bình" -> {
                // Đặt mức âm lượng trung bình cho ExoPlayer
                simpleExoPlayer.volume = 0.5f
            }
            "Âm lượng thấp" -> {
                // Đặt mức âm lượng thấp cho ExoPlayer
                simpleExoPlayer.volume = 0.2f
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val currentOrientation = newConfig.orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT && IS_FULL_SCREEN) {
            requireActivity().requestedOrientation = INITIAL_ORIENTATION
            IS_FULL_SCREEN = false
        }
    }


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

        if (IS_LOCK) {
            return
        }

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Lấy tham chiếu tới button fullscreen tương ứng từ Fragment

            fullscreen?.performClick()
        } else {
            requireActivity().onBackPressed()
        }
    }


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







