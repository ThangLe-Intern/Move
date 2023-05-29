package com.madison.move.ui.menu


import android.content.Context
import android.content.SharedPreferences
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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.madison.move.R
import com.madison.move.data.DataManager
import com.madison.move.data.model.User
import com.madison.move.data.model.login.DataUserLogin
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.databinding.ActivityMainMenuBinding
import com.madison.move.ui.base.BaseActivity
import com.madison.move.ui.faq.FAQFragment
import com.madison.move.ui.guidelines.GuidelinesFragment
import com.madison.move.ui.home.HomeFragment
import com.madison.move.ui.login.LoginDialogFragment
import com.madison.move.ui.offlinechannel.CommentFragment
import com.madison.move.ui.profile.ProfileFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainMenuActivity : BaseActivity<MenuPresenter>(), MainContract.View,
    NavigationView.OnNavigationItemSelectedListener, LoginDialogFragment.OnInputListener {
    private lateinit var binding: ActivityMainMenuBinding
    private var dataUserLogin: DataUserLogin? = null
    private var tokenUser: String? = null
    private var tokenResponse: LoginResponse? = null
    private var getSharedPreferences: SharedPreferences? = null
    private var userDung = User(
        1, "vudung", "vudung@gmail.com", "", "123", R.drawable.avatar, 1, "Male", "", 1, "", false
    )


    override fun createPresenter(): MenuPresenter = MenuPresenter(this)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        //clear token when re-launch app
        val settings = getSharedPreferences("tokenUser", Context.MODE_PRIVATE)
        settings.edit().clear().apply()

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        getSharedPreferences = getSharedPreferences("tokenUser", MODE_PRIVATE)
        tokenUser = getSharedPreferences?.getString("token", null)

        //If token null show menu of login -- if not null show menu logout
        if (tokenUser == null) {
            binding.apply {
                menulogout.text = getString(R.string.txt_log_in)
                menuTvSettting.visibility = View.GONE
                layoutUserInfo.constraintLayout.visibility = View.GONE
                menuTvFollowing.visibility = View.GONE
            }
        } else {
            binding.apply {
                menulogout.text = getString(R.string.txt_log_out)
                menuTvSettting.visibility = View.VISIBLE
                layoutUserInfo.constraintLayout.visibility = View.VISIBLE
                layoutUserInfo.txtUsernameNavbar.text = userDung.username
                menuTvFollowing.visibility = View.VISIBLE
            }
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



        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)

        binding.groupItemChild.visibility = View.GONE
        toggle.setToolbarNavigationClickListener { view ->
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        binding.groupItemChild.visibility = View.GONE
        binding.menuTvMore.setOnClickListener {
            var isImageChanged = false
            val originalImage: Drawable = resources.getDrawable(R.drawable.ic_arrow_down, null)
            val newImage: Drawable = resources.getDrawable(R.drawable.ic_arrow_up, null)

            if (!isImageChanged && binding.groupItemChild.visibility == View.VISIBLE) {
                binding.groupItemChild.visibility = View.GONE
                binding.imgdown.setImageDrawable(originalImage)
                isImageChanged = true
            } else {
                binding.imgdown.setImageDrawable(newImage)
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
        supportFragmentManager.beginTransaction()
            .replace(binding.contentFrameMain.id, HomeFragment(), "MY_FRAGMENT").commit()


    }


    override fun listener() {


        binding.layoutUserInfo.constraintLayout.setOnClickListener {

            val profileFragment = ProfileFragment()

            if (userDung.role) {
                val bundle = Bundle()
                bundle.putParcelable("user", userDung)
                profileFragment.arguments = bundle
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            supportFragmentManager.beginTransaction()
                .replace(binding.contentFrameMain.id, profileFragment).commit()

        }


        binding.apply {
            menuTvFaq.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrameMain.id, FAQFragment()).commit()
            }

            menuTvGuideline.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrameMain.id, GuidelinesFragment()).commit()

            }

            binding.menuTvFollowing.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrameMain.id, CommentFragment()).commit()

            }

            layoutToolBar.imvLogo.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrameMain.id, HomeFragment()).commit()
            }

            menulogout.setOnClickListener {

                if (tokenUser == null) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val loginDialog = LoginDialogFragment(this@MainMenuActivity)
                    loginDialog.show(supportFragmentManager, "login Dialog")
                } else {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    //clear token when logout
                    val settings = getSharedPreferences("tokenUser", Context.MODE_PRIVATE)
                    settings.edit().clear().apply()
                    tokenUser = null
                    tokenResponse = null

                    binding.apply {
                        menulogout.text = getString(R.string.txt_log_in)
                        menuTvSettting.visibility = View.GONE
                        layoutUserInfo.constraintLayout.visibility = View.GONE
                        menuTvFollowing.visibility = View.GONE
                    }

                    //reload homepage
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame_main, HomeFragment()).commit()

                }
            }

            layoutUserInfo.logo.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrameMain.id, HomeFragment()).commit()

            }



            menuTvSettting.setOnClickListener {
                binding.drawerLayout.closeDrawer(GravityCompat.START)

                val profileFragment = ProfileFragment()

                val bundle = Bundle()
                bundle.putParcelable("user", userDung)
                profileFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(binding.contentFrameMain.id, profileFragment).commit()

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


    //Get Token From Server
    override fun sendData(email: String, password: String, fragment: DialogFragment) {
        presenter?.onGetTokenPresenter(email, password, fragment)
    }

    override fun onSuccessGetToken(loginResponse: LoginResponse) {

        tokenResponse = loginResponse
        tokenUser = tokenResponse?.token
        dataUserLogin = tokenResponse?.dataUserLogin

        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

        //Set Data to Preferences
        val sharedPreferences = getSharedPreferences("tokenUser", MODE_PRIVATE)
        sharedPreferences?.edit()?.putString("token", tokenUser.toString())?.apply()

        binding.apply {
            menulogout.text = getString(R.string.txt_log_out)
            menuTvSettting.visibility = View.VISIBLE
            menuTvFollowing.visibility = View.VISIBLE
            layoutUserInfo.constraintLayout.visibility = View.VISIBLE
            layoutUserInfo.txtUsernameNavbar.text = dataUserLogin?.username.toString()
            if (dataUserLogin?.img != null){
                Glide.with(this@MainMenuActivity)
                    .load(dataUserLogin?.img)
                    .into(binding.layoutUserInfo.imgMenuUserAvatar)
            }
        }


        //Reload Current Screen
        val currentFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.content_frame_main)
        if (currentFragment is HomeFragment) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        } else if (currentFragment is FAQFragment) {

        }

    }


    override fun onStop() {
        super.onStop()
    }


}