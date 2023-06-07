package com.madison.move.ui.login

import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface LoginContract {
    interface LoginView : BaseView{
        fun onShowLoading()
        fun onEnableButtonLogin()
        fun onShowError(errorType: String)
        fun onSendDataToActivity(email:String, password:String)
        fun onResponseError(errorType: String)

    }

    interface Presenter:BasePresenter<LoginView>{
        fun onShowLoadingPresenter()
        fun onEnableButtonLoginPresenter()
        fun onShowErrorPresenter(errorType: String)
        fun onLoginClickPresenter(email:String,password:String)
    }
}