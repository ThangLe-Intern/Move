package com.madison.move.ui.base


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.madison.move.ui.menu.MainContract
import com.madison.move.ui.menu.MainInterface

abstract class BaseFragment<Presenter : Any> : Fragment(), BaseView {

    var presenter: Presenter? = null

    open fun initView() {}

    open fun listener() {}

    var mListener: MainInterface? = null

    companion object {
        const val NO_INTERNET = "No internet Connection, Please try again! "
        const val INTERNET = "You are Online"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Initialize the interface variable
        mListener = activity as MainInterface
        if (mListener == null) {
            throw ClassCastException("$activity must implement MainInterface")
        }
    }

    protected abstract fun createPresenter(): Presenter?

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        listener()
        if (mListener?.isDeviceOnlineCheck() == false) {
            Toast.makeText(activity, NO_INTERNET, Toast.LENGTH_SHORT).show()
            mListener?.onShowDisconnectDialog()
        }
    }

    fun onReloadUserMenu(){
        mListener?.onReloadUserInfoMenu()
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