package com.madison.move.ui.base

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<Presenter : BasePresenter<BaseView>> : AppCompatActivity() {
    protected var presenter: Presenter? = null
    @NonNull
    protected abstract fun createPresenter(): Presenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDetach()
    }
}
