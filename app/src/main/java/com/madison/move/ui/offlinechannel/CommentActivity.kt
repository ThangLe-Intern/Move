package com.madison.move.ui.offlinechannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.madison.move.R
import com.madison.move.databinding.ActivityCommentBinding
import com.madison.move.ui.home.HomeFragment


class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // binding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(binding.frameCommentLayout.id, HomeFragment()).commit()

    }




}