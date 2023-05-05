package com.madison.move.ui.base

import android.R
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

abstract class BaseFragment<Presenter : Any> : Fragment(), BaseView {

    var presenter: Presenter? = null

    open fun initView() {}

    open fun listener() {}


    protected abstract fun createPresenter(): Presenter?

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        initView()
        listener()
    }

    override fun onBottomNavigateSystemUI() {

    }


    override fun onDestroy() {
        super.onDestroy()
        if (presenter is BasePresenter<*>) {
            (presenter as BasePresenter<*>).onDetach()
        }
    }
}