package com.madison.move.ui.login

import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface LoginContract {
    interface LoginView : BaseView{
        fun onShowLoading()
        fun onDisableButtonLogin()
        fun onEnableButtonLogin()
    }

    interface Presenter:BasePresenter<LoginView>{
        fun onShowLoadingPresenter()
        fun onDisableButtonLoginPresenter()
        fun onEnableButtonLoginPresenter()
        fun onLoginClick()
    }
}