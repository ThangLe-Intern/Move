package com.madison.move.ui.base

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<Presenter : Any> : AppCompatActivity() {
    var presenter: Presenter? = null

    open fun initView() {}

    open fun listener() {}

    @NonNull
    protected abstract fun createPresenter(): Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()

        initView()
        listener()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (presenter is BasePresenter<*>) {
            (presenter as BasePresenter<*>).onDetach()
        }
    }
}
