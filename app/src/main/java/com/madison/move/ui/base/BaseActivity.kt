package com.madison.move.ui.base

import android.R
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

abstract class BaseActivity<Presenter : Any> : AppCompatActivity(), BaseView {
    var presenter: Presenter? = null

    open fun initView() {}

    open fun listener() {}

    protected abstract fun createPresenter(): Presenter?
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        onBottomNavigateSystemUI()
        initView()
        listener()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBottomNavigateSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(
            window,
            window.decorView.findViewById(R.id.content)
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (presenter is BasePresenter<*>) {
            (presenter as BasePresenter<*>).onDetach()
        }
    }
}
