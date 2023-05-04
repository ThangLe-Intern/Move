package com.madison.move.ui.menu


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.madison.move.R
import com.madison.move.databinding.ActivityMainMenuBinding
import androidx.appcompat.widget.Toolbar



class MainMenuActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = binding.navNew
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.opem_name,
            R.string.close_name
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.groupItemChild.visibility = View.GONE
        binding.menuTvMore.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                var isImageChanged = false
                val originalImage: Drawable = resources.getDrawable(R.drawable.ic_arrow_down, null)
                val newImage: Drawable = resources.getDrawable(R.drawable.ic_arrow_up, null)

                if (!isImageChanged && binding.groupItemChild.visibility == View.VISIBLE) {
                    binding.groupItemChild.visibility = View.GONE
                    binding.imgdown.setImageDrawable(newImage)
                    isImageChanged = true
                } else {
                    binding.imgdown.setImageDrawable(originalImage)
                    isImageChanged = false
                    binding.groupItemChild.visibility = View.VISIBLE
                }
            }
        })
        binding.menuTvFollowing.setOnClickListener {
            recreate()
        }

        val imgviewclose : ImageView =findViewById(R.id.imgclose)
        imgviewclose.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

    }



    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

}