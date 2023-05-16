package com.madison.move.ui.menu

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.madison.move.R
import com.madison.move.data.model.User
import com.madison.move.databinding.ActivityMainMenuBinding
import com.madison.move.ui.base.BaseActivity
import com.madison.move.ui.faq.FAQFragment
import com.madison.move.ui.home.HomeFragment
import com.madison.move.ui.login.LoginDialogFragment
import com.madison.move.ui.profile.ProfileFragment


class MainMenuActivity : BaseActivity<MenuPresenter>(), MainContract.View,
    NavigationView.OnNavigationItemSelectedListener, LoginDialogFragment.OnInputListener {
    private lateinit var binding: ActivityMainMenuBinding

    private var userDung = User(
        1, "vudung", "vudung@gmail.com", "Vu Dung",
        "123", R.drawable.avatar, 1, "Male", "03/01/2001", 1,
        "Ham Ninh - QN - QB", false
    )

    override fun createPresenter(): MenuPresenter = MenuPresenter(this)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        if (!userDung.role) {
            binding.menulogout.text = getString(R.string.txt_log_in)
//            binding.menuTvSettting.visibility = View.GONE
        } else {
            binding.menulogout.text = getString(R.string.txt_log_out)
            binding.menuTvSettting.visibility = View.VISIBLE
        }
    }

    override fun initView() {
        setSupportActionBar(binding.layoutToolBar.toolbar)

        val navigationView = binding.navNew
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.layoutToolBar.toolbar,
            R.string.opem_name,
            R.string.close_name
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.groupItemChild.visibility = View.GONE
        binding.menuTvMore.setOnClickListener {
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
        binding.menuTvFollowing.setOnClickListener {
            recreate()
        }

        val imgViewClose: ImageView = findViewById(R.id.imgclose)
        imgViewClose.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // add home
        supportFragmentManager.beginTransaction().replace(binding.contentFrame.id, HomeFragment())
            .commit()
    }

    override fun listener() {
        binding.apply {
            menuTvFaq.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)

                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrame.id, FAQFragment()).commit()
            }

            layoutToolBar.imvLogo.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)

                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrame.id, HomeFragment()).commit()
            }

            menulogout.setOnClickListener {

                if (!userDung.role) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val loginDialog = LoginDialogFragment(this@MainMenuActivity)
                    val bundle = Bundle()
                    bundle.putParcelable("user", userDung)
                    loginDialog.arguments = bundle
                    loginDialog.show(supportFragmentManager, "login Dialog")
                } else {
                    userDung.role = false
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val intent = intent
                    finish()
                    startActivity(intent)
                }


            }

            menuTvSettting.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)

                val profileFragment = ProfileFragment()

                if (userDung.role) {
                    val bundle = Bundle()
                    bundle.putParcelable("user", userDung)
                    profileFragment.arguments = bundle
                }

                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrame.id, profileFragment).commit()
            }
        }
    }


    //Close Keyboard & Clear edit text focus when click outside
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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


    //Get Data User from dialog Fragment
    override fun sendInput(user: User) {
        userDung = user
        if (!userDung.role) {
            binding.menulogout.text = getString(R.string.txt_log_in)
            binding.menuTvSettting.visibility = View.GONE
        } else {
            binding.menulogout.text = getString(R.string.txt_log_out)
            binding.menuTvSettting.visibility = View.VISIBLE
        }
    }


}