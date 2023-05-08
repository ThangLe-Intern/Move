package com.madison.move.ui.guidelines

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.madison.move.databinding.ActivityGuidelinesBinding
import com.madison.move.ui.home.HomeFragment

class GuidelinesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuidelinesBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        binding = ActivityGuidelinesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState, persistentState)

        supportFragmentManager.beginTransaction().replace(binding.frameGuidelinesLayout.id,HomeFragment()).commit()
    }
}