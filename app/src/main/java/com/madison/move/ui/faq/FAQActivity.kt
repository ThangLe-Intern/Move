package com.madison.move.ui.faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.madison.move.databinding.ActivityMainFaqBinding
import com.madison.move.ui.home.HomeFragment

class FAQActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainFaqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(binding.frameFaqLayout.id, HomeFragment())
            .commit()
    }
}