package com.madison.move.ui.menu


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.madison.move.R
import com.madison.move.data.model.login.DataUserLogin
import com.madison.move.data.model.login.LoginResponse
import com.madison.move.data.model.logout.LogoutResponse
import com.madison.move.databinding.ActivityMainMenuBinding
import com.madison.move.ui.base.BaseActivity
import com.madison.move.ui.faq.FAQFragment
import com.madison.move.ui.guidelines.GuidelinesFragment
import com.madison.move.ui.home.HomeFragment
import com.madison.move.ui.login.LoginDialogFragment
import com.madison.move.ui.offlinechannel.CommentFragment
import com.madison.move.ui.profile.ProfileFragment


class MainMenuActivity : BaseActivity<MenuPresenter>(), MainContract.View,
    NavigationView.OnNavigationItemSelectedListener, LoginDialogFragment.OnInputListener {
    lateinit var mainMenuBinding: ActivityMainMenuBinding
    private var dataUserLogin: DataUserLogin? = null
    private var tokenUser: String? = null
    private var tokenResponse: LoginResponse? = null
    private var getSharedPreferences: SharedPreferences? = null

    companion object {
        const val TOKEN_USER_PREFERENCE = "tokenUser"
        const val TOKEN = "token"
    }

    override fun createPresenter(): MenuPresenter = MenuPresenter(this)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        //clear token when re-launch app
        val settings = getSharedPreferences(TOKEN_USER_PREFERENCE, Context.MODE_PRIVATE)
        settings.edit().clear().apply()

        mainMenuBinding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(mainMenuBinding.root)
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()

        getSharedPreferences = getSharedPreferences(TOKEN_USER_PREFERENCE, MODE_PRIVATE)
        tokenUser = getSharedPreferences?.getString(TOKEN, null)

        //If token null show menu of login -- if not null show menu logout
        if (tokenUser == null) {
            mainMenuBinding.apply {
                menulogout.text = getString(R.string.txt_log_in)
                menuTvSettting.visibility = View.GONE
                layoutUserInfo.constraintLayout.visibility = View.GONE
                menuTvFollowing.visibility = View.GONE
            }
        } else {
            mainMenuBinding.apply {
                menulogout.text = getString(R.string.txt_log_out)
                menuTvSettting.visibility = View.VISIBLE
                layoutUserInfo.constraintLayout.visibility = View.VISIBLE
                layoutUserInfo.txtUsernameNavbar.text = dataUserLogin?.username
                menuTvFollowing.visibility = View.VISIBLE
            }
        }
    }

    fun isDeviceOnline(context: Context): Boolean {
        val connMgr = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        val isOnline = networkInfo != null && networkInfo.isConnected
        if (!isOnline) Toast.makeText(context, " No internet Connection ", Toast.LENGTH_SHORT)
            .show()
        return isOnline
    }


    override fun initView() {
        setSupportActionBar(mainMenuBinding.layoutToolBar.toolbar)

        val navigationView = mainMenuBinding.navNew
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            mainMenuBinding.drawerLayout,
            mainMenuBinding.layoutToolBar.toolbar,
            R.string.opem_name,
            R.string.close_name
        )



        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)

        mainMenuBinding.groupItemChild.visibility = View.GONE
        toggle.setToolbarNavigationClickListener { view ->
            mainMenuBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        mainMenuBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        mainMenuBinding.groupItemChild.visibility = View.GONE
        mainMenuBinding.menuTvMore.setOnClickListener {
            var isImageChanged = false
            val originalImage: Drawable = resources.getDrawable(R.drawable.ic_arrow_down, null)
            val newImage: Drawable = resources.getDrawable(R.drawable.ic_arrow_up, null)

            if (!isImageChanged && mainMenuBinding.groupItemChild.visibility == View.VISIBLE) {
                mainMenuBinding.groupItemChild.visibility = View.GONE
                mainMenuBinding.imgdown.setImageDrawable(originalImage)
                isImageChanged = true
            } else {
                mainMenuBinding.imgdown.setImageDrawable(newImage)
                isImageChanged = false
                mainMenuBinding.groupItemChild.visibility = View.VISIBLE
            }
        }
        mainMenuBinding.menuTvFollowing.setOnClickListener {
            recreate()
        }

        val imgViewClose: ImageView = findViewById(R.id.imgclose)
        imgViewClose.setOnClickListener {
            mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // add home
        supportFragmentManager.beginTransaction()
            .replace(mainMenuBinding.contentFrameMain.id, HomeFragment()).commit()

    }
    override fun listener() {

        mainMenuBinding.layoutUserInfo.constraintLayout.setOnClickListener {
            mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
            supportFragmentManager.beginTransaction()
                .replace(mainMenuBinding.contentFrameMain.id, ProfileFragment()).commit()
        }


        mainMenuBinding.apply {
            menuTvFaq.setOnClickListener {
                mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, FAQFragment()).commit()
            }

            menuTvGuideline.setOnClickListener {
                mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, GuidelinesFragment()).commit()

            }

            mainMenuBinding.menuTvFollowing.setOnClickListener {
                mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, CommentFragment()).commit()
            }

            layoutToolBar.imvLogo.setOnClickListener {
                mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, HomeFragment()).commit()
            }

            menulogout.setOnClickListener {

                if (tokenUser == null) {
                    mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    val loginDialog = LoginDialogFragment(this@MainMenuActivity)
                    loginDialog.show(supportFragmentManager, "login Dialog")
                } else {
                    mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)

                    tokenUser?.let { token -> presenter?.logoutRequest(token) }

                    mainMenuBinding.apply {
                        menulogout.text = getString(R.string.txt_log_in)
                        menuTvSettting.visibility = View.GONE
                        layoutUserInfo.constraintLayout.visibility = View.GONE
                        menuTvFollowing.visibility = View.GONE
                    }
                }
            }

            layoutUserInfo.logo.setOnClickListener {
                mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, HomeFragment()).commit()
            }

            menuTvSettting.setOnClickListener {
                mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, ProfileFragment()).commit()
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
        if (mainMenuBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainMenuBinding.drawerLayout.closeDrawer(GravityCompat.START)
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

        Toast.makeText(this, loginResponse.message.toString(), Toast.LENGTH_SHORT).show()

        //Set Data to Preferences
        val sharedPreferences = getSharedPreferences(TOKEN_USER_PREFERENCE, MODE_PRIVATE)
        sharedPreferences?.edit()?.putString(TOKEN, tokenUser.toString())?.apply()

        mainMenuBinding.apply {
            menulogout.text = getString(R.string.txt_log_out)
            menuTvSettting.visibility = View.VISIBLE
            menuTvFollowing.visibility = View.VISIBLE
            layoutUserInfo.constraintLayout.visibility = View.VISIBLE

            //Set User Information To Menu
            layoutUserInfo.txtUsernameNavbar.text = dataUserLogin?.username.toString()
            if (dataUserLogin?.img != null) {
                Glide.with(this@MainMenuActivity).load(dataUserLogin?.img)
                    .into(mainMenuBinding.layoutUserInfo.imgMenuUserAvatar)
            }

            if (dataUserLogin?.kol == 0) {
                mainMenuBinding.layoutUserInfo.imgBlueTickNavbar.isVisible = false
            }
        }

        //Reload Current Screen
        val currentFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.content_frame_main)

        when (currentFragment) {
            is HomeFragment -> currentFragment.onResume()
            is FAQFragment -> {
                //Refresh Data FAQ when Logout
            }
            is ProfileFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, HomeFragment()).commit()
            }
        }

    }

    override fun onSuccessLogout(logoutResponse: LogoutResponse) {
        Toast.makeText(this, logoutResponse.message?: "", Toast.LENGTH_SHORT).show()
        //clear token when logout
        val settings = getSharedPreferences(TOKEN_USER_PREFERENCE, Context.MODE_PRIVATE)
        settings.edit().clear().apply()
        tokenUser = null
        tokenResponse = null

        //Reload Current Screen
        val currentFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.content_frame_main)

        when (currentFragment) {
            is HomeFragment -> currentFragment.onResume()
            is FAQFragment -> {
                //Refresh Data FAQ when Logout
            }
            is ProfileFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(mainMenuBinding.contentFrameMain.id, HomeFragment()).commit()
            }
        }
    }

    override fun onError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }


    override fun onStop() {
        super.onStop()
    }


}